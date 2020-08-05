package com.minecraftabnormals.endergetic.common.entities.bolloom;

import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.minecraftabnormals.endergetic.api.entity.util.EntityItemStackHelper;
import com.minecraftabnormals.endergetic.core.registry.EEBlocks;
import com.minecraftabnormals.endergetic.core.registry.EEEntities;
import com.minecraftabnormals.endergetic.core.registry.EEItems;

import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.Util;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;

public class BolloomBalloonEntity extends Entity {
	public static final Map<UUID, Map<UUID, Integer>> BALLOONS_ON_BOAT_MAP = Maps.newHashMap();
	
	private static final DataParameter<Float> ORIGINAL_X = EntityDataManager.createKey(BolloomBalloonEntity.class, DataSerializers.FLOAT);
	private static final DataParameter<Float> ORIGINAL_Z = EntityDataManager.createKey(BolloomBalloonEntity.class, DataSerializers.FLOAT);
	private static final DataParameter<Float> ORIGINAL_Y = EntityDataManager.createKey(BolloomBalloonEntity.class, DataSerializers.FLOAT);
	private static final DataParameter<Float> ANGLE = EntityDataManager.createKey(BolloomBalloonEntity.class, DataSerializers.FLOAT);
	private static final DataParameter<Float> DESIRED_ANGLE = EntityDataManager.createKey(BolloomBalloonEntity.class, DataSerializers.FLOAT);
	private static final DataParameter<Float> SWAY = EntityDataManager.createKey(BolloomBalloonEntity.class, DataSerializers.FLOAT);
	private static final DataParameter<Boolean> UNTIED = EntityDataManager.createKey(BolloomBalloonEntity.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Optional<UUID>> KNOT_UNIQUE_ID = EntityDataManager.<Optional<UUID>>createKey(BolloomBalloonEntity.class, DataSerializers.OPTIONAL_UNIQUE_ID);
	private static final DataParameter<BlockPos> FENCE_POS = EntityDataManager.createKey(BolloomBalloonEntity.class, DataSerializers.BLOCK_POS);
	private static final DataParameter<Integer> TICKSEXISTED = EntityDataManager.createKey(BolloomBalloonEntity.class, DataSerializers.VARINT); //Vanilla's ticksExisted isn't synced between server and client
	private static final DataParameter<Integer> HIDE_TIME = EntityDataManager.createKey(BolloomBalloonEntity.class, DataSerializers.VARINT);
	protected static final DataParameter<Byte> COLOR = EntityDataManager.createKey(BolloomBalloonEntity.class, DataSerializers.BYTE);
	public float prevVineAngle;
	public float prevAngle;
	private boolean hasModifiedBoatIndex;
	
	public BolloomBalloonEntity(EntityType<? extends BolloomBalloonEntity> entityType, World world) {
		super(entityType, world);
	}
	
	public BolloomBalloonEntity(FMLPlayMessages.SpawnEntity spawnEntity, World world) {
		this(EEEntities.BOLLOOM_BALLOON.get(), world);
	}
	
	/*
	 * Used for Adding onto a fence
	 */
	public BolloomBalloonEntity(World world, UUID ownerKnot, BlockPos pos, float offset) {
		this(EEEntities.BOLLOOM_BALLOON.get(), world);
		float xOffset = this.rand.nextBoolean() ? -offset : offset;
		float zOffset = this.rand.nextBoolean() ? -offset : offset;
		this.setPosition(pos.getX() + 0.5F + xOffset, pos.getY() + 3, pos.getZ() + 0.5F + zOffset);
		this.setOriginalPos(pos.getX() + 0.5F + xOffset, pos.getY() + 3, pos.getZ() + 0.5F + zOffset);
		this.getDataManager().set(FENCE_POS, pos);
		this.setKnotId(ownerKnot);
		this.prevPosX = pos.getX() + 0.5F + xOffset;
		this.prevPosY = pos.getY() + 3;
		this.prevPosZ = pos.getZ() + 0.5F + zOffset;
	}
	
	/*
	 * Used for Dispensers
	 */
	public BolloomBalloonEntity(World world, BlockPos pos) {
		this(EEEntities.BOLLOOM_BALLOON.get(), world);
		this.setPosition(pos.getX() + 0.5F, pos.getY(), pos.getZ() + 0.5F);
		this.setOriginalPos(pos.getX() + 0.5F, pos.getY(), pos.getZ() + 0.5F);
		this.setUntied();
		this.setDesiredAngle((float) (rand.nextDouble() * 2 * Math.PI));
		this.setAngle((float) (rand.nextDouble() * 2 * Math.PI));
		this.prevPosX = pos.getX() + 0.5F;
		this.prevPosY = pos.getY();
		this.prevPosZ = pos.getZ() + 0.5F;
	}
	
	@Override
	public void tick() {
		this.prevPosX = this.getPosX();
		this.prevPosY = this.getPosY();
		this.prevPosZ = this.getPosZ();
		this.prevVineAngle = this.getVineAngle();
		this.prevAngle = this.getAngle();
		
		if (this.world.isAreaLoaded(getFencePos(), 1) && !this.isUntied()) {
			if (!this.world.getBlockState(this.getFencePos()).getBlock().isIn(BlockTags.FENCES)) {
				if(!this.world.isRemote && this.getKnot() != null) {
					((BolloomKnotEntity) this.getKnot()).setBalloonsTied(((BolloomKnotEntity) this.getKnot()).getBalloonsTied() - 1);
				}
				this.setUntied();
			}
		}
		
		this.setSway((float) Math.sin((2 * Math.PI / 100 * this.getTicksExisted())) * 0.5F);
		
		if (!this.world.isRemote) {
			if (this.getTicksExisted() % 45 == 0) {
			    this.setDesiredAngle((float) (this.rand.nextDouble() * 2 * Math.PI));
			}
			
			if (this.getPosY() > 254 && this.isUntied()) {
				this.onBroken();
				this.remove();
			}
			
			float dangle = this.getDesiredAngle() - this.getAngle();
			
			while (dangle > Math.PI) {
			    dangle -= 2 * Math.PI;
			}
			
			while (dangle <= -Math.PI) {
			    dangle += 2 * Math.PI;
			}
			
			if (Math.abs(dangle) <= 0.1F) {
			    this.setAngle(this.getAngle() + dangle);
			} else if (dangle > 0) {
			    this.setAngle(this.getAngle() + 0.03F);
			} else {
			    this.setAngle(this.getAngle() - 0.03F);
			}
		}
		if (this.world.isAreaLoaded(this.func_233580_cy_(), 1)) {
			if (!this.isUntied()) {
				this.setPosition(
					this.getDataManager().get(ORIGINAL_X) + this.getSway() * Math.sin(-this.getAngle()),
					this.getSetY(),
					this.getDataManager().get(ORIGINAL_Z) + this.getSway() * Math.cos(-this.getAngle())
				);
			} else {
				if (this.getRidingEntity() == null) {
					this.move(MoverType.SELF, this.getMotion());
					this.setMotion(Math.sin(this.getAngle()) * Math.cos(this.getAngle()) * 0.05F, Math.toRadians(4), Math.cos(this.getVineAngle()) * Math.cos(-this.getAngle()) * 0.05F);
				}
			}
		}
		if (this.checkForBlocksDown()) {
			if (!this.getEntityWorld().isRemote && this.getKnot() != null && !this.isUntied()) {
				((BolloomKnotEntity) this.getKnot()).setBalloonsTied(((BolloomKnotEntity)this.getKnot()).getBalloonsTied() - 1);
			}
			this.setUntied();
		}
		
		/**
		 * Sometimes the order of riding and dismounting shifts causing orders to be bumped above 3, this is a fix for it.
		 */
		Entity ridingEntity = this.getRidingEntity();
		if (ridingEntity instanceof BoatEntity) {
			Map<UUID, Integer> orderMap = BALLOONS_ON_BOAT_MAP.get(ridingEntity.getUniqueID());
			if (orderMap != null) {
				UUID uuid = this.getUniqueID();
				if (orderMap.containsKey(uuid) && orderMap.get(uuid) >= 4) {
					orderMap.put(uuid, this.getClosestOrder(orderMap));
				}
			}
		}
		
		/**
		 * Often it takes a bit too long to sync the position of the Balloon on the boat to the client to the server, so this temporarily hides it.
		 */
		if (this.getHideTime() > 0) this.decrementHideTime();
		
		this.incrementTicksExisted();
	}
	
	@Override
	protected void registerData() {
		this.getDataManager().register(KNOT_UNIQUE_ID, Optional.empty());
		this.getDataManager().register(ORIGINAL_X, 0F);
		this.getDataManager().register(ORIGINAL_Z, 0F);
		this.getDataManager().register(ORIGINAL_Y, 0F);
		this.getDataManager().register(FENCE_POS, BlockPos.ZERO);
		this.getDataManager().register(UNTIED, false);
		this.getDataManager().register(ANGLE, 0F);
		this.getDataManager().register(SWAY, 0F);
		this.getDataManager().register(DESIRED_ANGLE, 0F);
		this.getDataManager().register(TICKSEXISTED, 0);
		this.getDataManager().register(HIDE_TIME, 0);
		this.getDataManager().register(COLOR, (byte)16);
	}

	@Override
	protected void writeAdditional(CompoundNBT nbt) {
		if (this.getKnotId() != null) {
			nbt.putUniqueId("KnotUUID", this.getKnotId());
		}
		nbt.putBoolean("UNTIED", this.getDataManager().get(UNTIED));
		nbt.putFloat("ORIGIN_X", this.getDataManager().get(ORIGINAL_X));
		nbt.putFloat("ORIGIN_Y", this.getDataManager().get(ORIGINAL_Y));
		nbt.putFloat("ORIGIN_Z", this.getDataManager().get(ORIGINAL_Z));
		nbt.putLong("FENCE_POS", this.getDataManager().get(FENCE_POS).toLong());
		nbt.putByte("Color", this.dataManager.get(COLOR));
	}
	
	@Override
	protected void readAdditional(CompoundNBT nbt) {
		this.setKnotId(nbt.contains("KnotUUID") ? nbt.getUniqueId("KnotUUID") : null);
		this.getDataManager().set(UNTIED, nbt.getBoolean("UNTIED"));
		this.getDataManager().set(ORIGINAL_X, nbt.getFloat("ORIGIN_X"));
		this.getDataManager().set(ORIGINAL_Y, nbt.getFloat("ORIGIN_Y"));
		this.getDataManager().set(ORIGINAL_Z, nbt.getFloat("ORIGIN_Z"));
		this.getDataManager().set(FENCE_POS, BlockPos.fromLong(nbt.getLong("FENCE_POS")));
		this.dataManager.set(COLOR, nbt.getByte("Color"));
	}
	
	@Override
	public boolean canBeCollidedWith() {
		return true;
	}
	
	public void onBroken() {
		this.playSound(SoundEvents.BLOCK_WET_GRASS_BREAK, 1.0F, 1.0F);
		if (this.getKnot() != null) {
			((BolloomKnotEntity) this.getKnot()).setBalloonsTied(((BolloomKnotEntity)this.getKnot()).getBalloonsTied() - 1);
		}
		if (this.world instanceof ServerWorld) {
			((ServerWorld)this.world).spawnParticle(new BlockParticleData(ParticleTypes.BLOCK, EEBlocks.BOLLOOM_PARTICLE.get().getDefaultState()), this.getPosX(), this.getPosY() + (double)this.getHeight() / 1.5D, this.getPosZ(), 10, (double)(this.getWidth() / 4.0F), (double)(this.getHeight() / 4.0F), (double)(this.getWidth() / 4.0F), 0.05D);
		}
	}
	
	@Override
	public void onKillCommand() {
		if (!this.getEntityWorld().isRemote) {
			if (this.getKnot() != null) {
				((BolloomKnotEntity) this.getKnot()).setBalloonsTied(((BolloomKnotEntity)this.getKnot()).getBalloonsTied() - 1);
			}
		}
		super.onKillCommand();
	}
	
	@SuppressWarnings("deprecation")
	public boolean attackEntityFrom(DamageSource source, float amount) {
		if (this.isInvulnerableTo(source)) {
			return false;
		} else {
			if (!this.removed && !this.world.isRemote) {
				this.remove();
				this.markVelocityChanged();
				this.onBroken();
			}
			return true;
		}
	}
	
	public boolean isInvulnerableTo(DamageSource source) {
		return this.isInvulnerable() && source != DamageSource.OUT_OF_WORLD && source != DamageSource.CRAMMING;
	}
	
	@Override
	public boolean hitByEntity(Entity entityIn) {
		return entityIn instanceof PlayerEntity ? this.attackEntityFrom(DamageSource.causePlayerDamage((PlayerEntity)entityIn), 0.0F) : false;
	}
	
	@Nullable
	@OnlyIn(Dist.CLIENT)
	public DyeColor getColor() {
		Byte obyte = this.dataManager.get(COLOR);
		return obyte != 16 && obyte <= 15 ? DyeColor.byId(obyte) : null;
	}
	
	public void setColor(@Nullable DyeColor color) {
		if(color == null) {
			this.dataManager.set(COLOR, (byte) 16);
		} else {
			this.dataManager.set(COLOR, (byte) color.getId());
		}
	}
	
	@Nullable
	public Entity getKnot() {
		return ((ServerWorld)world).getEntityByUuid(getKnotId());
	}
	
	@OnlyIn(Dist.CLIENT)
	public float[] getVineAnimation(float partialTicks) {
		return new float[] {
			MathHelper.lerp(partialTicks, this.prevVineAngle, this.getVineAngle()),
			MathHelper.lerp(partialTicks, this.prevAngle, this.getAngle()),
		};
	}
	
	public boolean checkForBlocksDown() {
		for (int i = 0; i < 3; i++) {
			BlockPos pos = this.getFencePos().up(3).down(i);
			if(this.getEntityWorld().isAreaLoaded(pos, 1)) {
				if(!this.getEntityWorld().getBlockState(pos).getMaterial().isReplaceable() || this.getEntityWorld().getBlockState(pos).getBlock() == Blocks.LAVA) {
					return true;
				}
			}
		}
		return false;
	}
	
	@Override
	public ActionResultType applyPlayerInteraction(PlayerEntity player, Vector3d vec, Hand hand) {
		ItemStack itemstack = player.getHeldItem(hand);
		if (itemstack.getItem() instanceof DyeItem && this.getColor() != ((DyeItem) itemstack.getItem()).getDyeColor()) {
			if(!this.world.isRemote) {
				this.setColor(((DyeItem) itemstack.getItem()).getDyeColor());
				EntityItemStackHelper.consumeItemFromStack(player, itemstack);
			}
			return ActionResultType.CONSUME;
		}
		return super.applyPlayerInteraction(player, vec, hand);
	}
	
	@Override
	public void updateRidden() {
		this.setMotion(Vector3d.ZERO);
		if (canUpdate())
		this.tick();
		if (this.isPassenger()) {
			this.setRidingPosition();
		}
	}
	
	@Override
	public double getMountedYOffset() {
		return 1.75F;
	}
	
	@Override
	public boolean canRiderInteract() {
		return true;
	}
	
	@Override
	public boolean startRiding(Entity entity, boolean force) {
		if (super.startRiding(entity, force)) {
			if (entity instanceof BoatEntity) {
				UUID boatUUID = entity.getUniqueID();
				if (BALLOONS_ON_BOAT_MAP.containsKey(boatUUID)) {
					Map<UUID, Integer> orderMap = BALLOONS_ON_BOAT_MAP.get(boatUUID);
					if (!this.hasModifiedBoatIndex) {
						orderMap.put(this.getUniqueID(), this.getClosestOrder(orderMap));
						this.hasModifiedBoatIndex = true;
					}
				} else {
					BALLOONS_ON_BOAT_MAP.put(boatUUID, Util.make(Maps.newHashMap(), (map) -> {
						map.put(this.getUniqueID(), 0);
					}));
				}
			}
			return true;
		}
		return false;
	}
	
	@Override
	public void func_233575_bb_() {
		if (this.getRidingEntity() instanceof BoatEntity && !this.isAlive()) {
			UUID boatUUID = this.getRidingEntity().getUniqueID();
			UUID uuid = this.getUniqueID();
			if (BALLOONS_ON_BOAT_MAP.containsKey(boatUUID) && BALLOONS_ON_BOAT_MAP.get(boatUUID).containsKey(uuid)) {
				BALLOONS_ON_BOAT_MAP.get(boatUUID).remove(uuid);
			}
		}
		super.func_233575_bb_();
	}
	
	public void setRidingPosition() {
		Entity ridingEntity = this.getRidingEntity();
		if (ridingEntity instanceof BoatEntity) {
			Map<UUID, Integer> orderMap = BALLOONS_ON_BOAT_MAP.get(ridingEntity.getUniqueID());
			UUID uuid = this.getUniqueID();
			if (orderMap == null || !orderMap.containsKey(uuid)) return;
			
			float x = 0.0F, z = 0.0F;
			switch (orderMap.get(uuid)) {
				default:
				case 0:
					x = 0.9F;
					z = -0.5F;
					break;
				case 1:
					x = 0.9F;
					z = 0.5F;
					break;
				case 2:
					x = -0.9F;
					z = -0.5F;
					break;
				case 3:
					x = -0.9F;
					z = 0.5F;
					break;
			}
			Vector3d ridingOffset = (new Vector3d(x, 0.0D, z)).rotateYaw((float) (-ridingEntity.rotationYaw * (Math.PI / 180F) - (Math.PI / 2F)));
			this.setPosition(ridingEntity.getPosX() + ridingOffset.getX() + this.getSway() * Math.sin(-this.getAngle()), ridingEntity.getPosY() + this.getMountedYOffset() + this.getRidingEntity().getEyeHeight(), ridingEntity.getPosZ() + ridingOffset.getZ() + this.getSway() * Math.cos(-this.getAngle()));
		} else {
			this.setPosition(ridingEntity.getPosX() + this.getSway() * Math.sin(-this.getAngle()), ridingEntity.getPosY() + this.getMountedYOffset() + this.getRidingEntity().getEyeHeight(), ridingEntity.getPosZ() + this.getSway() * Math.cos(-this.getAngle()));
		}
	}
	
	public void setBoatPosition() {
		this.setRidingPosition();
		this.dataManager.set(HIDE_TIME, 2);
	}
	
	private int getClosestOrder(Map<UUID, Integer> orderMap) {
		Set<Integer> missingNumbers = Sets.newHashSet();
		Set<Integer> orders = orderMap.values().stream().collect(Collectors.toSet());
		for (int i = 0; i < 4; i++) {
			if (!orders.contains(i)) {
				missingNumbers.add(i);
			}
		}
		return missingNumbers.isEmpty() ? orders.size() : missingNumbers.stream().sorted(Comparator.comparingInt(Math::abs)).collect(Collectors.toList()).get(0);
	}
	
	@Override
	public void addVelocity(double x, double y, double z) {
		if(!this.isUntied()) return;
		super.addVelocity(x, y, z);
	}
	
	@Override
	protected Vector3d handlePistonMovement(Vector3d pos) {
		return Vector3d.ZERO;
	}
	
	@Override
	public ItemStack getPickedResult(RayTraceResult target) {
		return new ItemStack(EEItems.BOLLOOM_BALLOON.get());
	}
	
	@Nullable
    public UUID getKnotId() {
        return this.dataManager.get(KNOT_UNIQUE_ID).orElse((UUID)null);
    }

    public void setKnotId(@Nullable UUID knotUUID) {
        this.dataManager.set(KNOT_UNIQUE_ID, Optional.ofNullable(knotUUID));
    }
    
    public void setAngle(float degree) {
		this.getDataManager().set(ANGLE, degree);
	}
	
	public float getAngle() {
		return this.getDataManager().get(ANGLE);
	}
	
	public void setDesiredAngle(float angle) {
		this.getDataManager().set(DESIRED_ANGLE, angle);
	}
	
	public float getDesiredAngle() {
		return this.getDataManager().get(DESIRED_ANGLE);
	}
    
    public float getVineAngle() {
		return (float) Math.atan(this.getSway() / 2F);
	}
    
    public void setSway(float sway) {
		this.dataManager.set(SWAY, sway);
	}
    
    public float getSway() {
		return this.dataManager.get(SWAY);
	}
    
    public void setUntied() {
		this.getDataManager().set(UNTIED, true);
	}
    
    public boolean isUntied() {
		return this.getDataManager().get(UNTIED);
	}
    
    public float getSetY() {
		return this.getDataManager().get(ORIGINAL_Y);
	}
    
    public void setOriginalPos(float x, float y, float z) {
		this.getDataManager().set(ORIGINAL_X, (float) x);
		this.getDataManager().set(ORIGINAL_Y, (float) y);
		this.getDataManager().set(ORIGINAL_Z, (float) z);
	}
    
    public int getTicksExisted() {
		return this.getDataManager().get(TICKSEXISTED);
	}

	public void incrementTicksExisted() {
		this.getDataManager().set(TICKSEXISTED, getTicksExisted() + 1);
	}
	
	public int getHideTime() {
		return this.getDataManager().get(HIDE_TIME);
	}

	public void decrementHideTime() {
		this.getDataManager().set(HIDE_TIME, this.getHideTime() - 1);
	}
	
	public BlockPos getFencePos() {
		return this.getDataManager().get(FENCE_POS);
	}
    
    @Override
    protected boolean canTriggerWalking() {
    	return false;
    }
    
	@Override
	@SuppressWarnings("deprecation")
	public boolean canBePushed() {
		return !removed;
	}
	
	@Nullable
	public AxisAlignedBB getCollisionBoundingBox() {
		return this.getBoundingBox();
	}
	
	@Nullable
	public AxisAlignedBB getCollisionBox(Entity entityIn) {
		return entityIn.canBePushed() ? entityIn.getBoundingBox() : null;
	}
    
    @Override
	public AxisAlignedBB getRenderBoundingBox() {
		return super.getRenderBoundingBox().grow(5);
	}

	@Override
	public IPacket<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

}
