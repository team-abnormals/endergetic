package endergeticexpansion.common.entities.bolloom;

import java.util.Optional;
import java.util.UUID;

import javax.annotation.Nullable;

import endergeticexpansion.common.capability.balloons.BalloonProvider;
import endergeticexpansion.core.registry.EEBlocks;
import endergeticexpansion.core.registry.EEEntities;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MoverType;
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
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

public class EntityBolloomBalloon extends Entity {
	private static final DataParameter<Float> ORIGINAL_X = EntityDataManager.createKey(EntityBolloomBalloon.class, DataSerializers.FLOAT);
	private static final DataParameter<Float> ORIGINAL_Z = EntityDataManager.createKey(EntityBolloomBalloon.class, DataSerializers.FLOAT);
	private static final DataParameter<Float> ORIGINAL_Y = EntityDataManager.createKey(EntityBolloomBalloon.class, DataSerializers.FLOAT);
	private static final DataParameter<Float> ANGLE = EntityDataManager.createKey(EntityBolloomBalloon.class, DataSerializers.FLOAT);
	private static final DataParameter<Float> DESIRED_ANGLE = EntityDataManager.createKey(EntityBolloomBalloon.class, DataSerializers.FLOAT);
	private static final DataParameter<Float> SWAY = EntityDataManager.createKey(EntityBolloomBalloon.class, DataSerializers.FLOAT);
	private static final DataParameter<Boolean> UNTIED = EntityDataManager.createKey(EntityBolloomBalloon.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Boolean> ATTACHED_ENTITY = EntityDataManager.createKey(EntityBolloomBalloon.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Optional<UUID>> ATTACHED_ENTITY_UNIQUE_ID = EntityDataManager.<Optional<UUID>>createKey(EntityBolloomBalloon.class, DataSerializers.OPTIONAL_UNIQUE_ID);
	private static final DataParameter<Optional<UUID>> KNOT_UNIQUE_ID = EntityDataManager.<Optional<UUID>>createKey(EntityBolloomBalloon.class, DataSerializers.OPTIONAL_UNIQUE_ID);
	private static final DataParameter<BlockPos> FENCE_POS = EntityDataManager.createKey(EntityBolloomBalloon.class, DataSerializers.BLOCK_POS);
	private static final DataParameter<Integer> TICKSEXISTED = EntityDataManager.createKey(EntityBolloomBalloon.class, DataSerializers.VARINT); //Vanilla's ticksExisted isn't synced between server and client
	protected static final DataParameter<Byte> COLOR = EntityDataManager.createKey(EntityBolloomBalloon.class, DataSerializers.BYTE);
	public float prevVineAngle;
	public float prevAngle;
	
	public EntityBolloomBalloon(EntityType<? extends EntityBolloomBalloon> entityType, World world) {
		super(entityType, world);
	}
	
	/*
	 * Used for Adding onto a fence
	 */
	public EntityBolloomBalloon(World world, UUID ownerKnot, BlockPos pos, float offset) {
		this(EEEntities.ObjectEntites.BOLLOOM_BALLOON, world);
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
	public EntityBolloomBalloon(World world, BlockPos pos) {
		this(EEEntities.ObjectEntites.BOLLOOM_BALLOON, world);
		this.setPosition(pos.getX() + 0.5F, pos.getY(), pos.getZ() + 0.5F);
		this.setOriginalPos(pos.getX() + 0.5F, pos.getY(), pos.getZ() + 0.5F);
		this.setUntied();
		this.getDataManager().set(DESIRED_ANGLE, (float) (rand.nextDouble() * 2 * Math.PI));
		this.setAngle((float) (rand.nextDouble() * 2 * Math.PI));
		this.prevPosX = pos.getX() + 0.5F;
		this.prevPosY = pos.getY();
		this.prevPosZ = pos.getZ() + 0.5F;
	}

	/*
	 * Used for attaching to entities
	 */
	public EntityBolloomBalloon(World world, Entity entity) {
		this(EEEntities.ObjectEntites.BOLLOOM_BALLOON, world);
		this.setPosition(entity.posX, (entity.posY + 2 + entity.getEyeHeight()), entity.posZ);
		this.setAttachedEntityId(entity.getUniqueID());
		if(!world.isRemote) {
			this.setOriginalPos((float)entity.posX, (float) ((float)entity.posY + 2 + entity.getEyeHeight()), (float)entity.posZ);
		}
		this.getDataManager().set(ATTACHED_ENTITY, true);
	}
	
	@Override
	public void tick() {
		this.prevPosX = posX;
		this.prevPosY = posY;
		this.prevPosZ = posZ;
		this.prevVineAngle = this.getVineAngle();
		this.prevAngle = this.getAngle();
		if(!this.getDataManager().get(ATTACHED_ENTITY)) {
			if(this.getEntityWorld().isAreaLoaded(getFencePos(), 1) && !this.isUntied()) {
				if(!this.getEntityWorld().getBlockState(this.getFencePos()).getBlock().isIn(BlockTags.FENCES)) {
					if(!this.getEntityWorld().isRemote && this.getKnot() != null) {
						((EntityBolloomKnot)this.getKnot()).setBalloonsTied(((EntityBolloomKnot)this.getKnot()).getBalloonsTied() - 1);
					}
					this.setUntied();
				}
			}
		}
		if(world.isAreaLoaded(this.getPosition(), 1)) {
			this.dataManager.set(SWAY, (float) Math.sin((2 * Math.PI / 100 * this.getTicksExisted())) * 0.5F);
		}
		if(!world.isRemote) {
			
			if(this.getDataManager().get(ATTACHED_ENTITY) && this.getAttachedEntity() != null) {
				this.setOriginalPos(
					(float)this.getAttachedEntity().posX,
					(float)this.getAttachedEntity().posY + 2 + this.getAttachedEntity().getEyeHeight(),
					(float)this.getAttachedEntity().posZ
				);
				this.prevPosX = this.getAttachedEntity().prevPosX;
				this.prevPosY = this.getAttachedEntity().prevPosY + 2 + this.getAttachedEntity().getEyeHeight();
				this.prevPosZ = this.getAttachedEntity().prevPosZ;
			}
			
			if (this.getTicksExisted() % 45 == 0) {
			    this.getDataManager().set(DESIRED_ANGLE, (float) (rand.nextDouble() * 2 * Math.PI));
			}
			
			if(this.posY > 254 && this.isUntied()) {
				this.onBroken(this);
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
		if(world.isAreaLoaded(this.getPosition(), 1)) {
			if(!this.isUntied()) {
				this.setPosition(
					this.getDataManager().get(ORIGINAL_X) + this.dataManager.get(SWAY) * Math.sin(-this.getAngle()),
					this.getSetY(),
					this.getDataManager().get(ORIGINAL_Z) + this.dataManager.get(SWAY) * Math.cos(-this.getAngle())
				);
			} else {
				this.move(MoverType.SELF, this.getMotion());
				this.setMotion(Math.sin(this.getVineAngle()) * Math.sin(-this.getAngle()) * 0.05F, Math.toRadians(4), Math.cos(this.getVineAngle()) * Math.cos(-this.getAngle()) * 0.05F);
			}
		}
		if(!this.getDataManager().get(ATTACHED_ENTITY)) {
			if(this.checkForBlocksDown()) {
				if(!this.getEntityWorld().isRemote && this.getKnot() != null && !this.isUntied()) {
					((EntityBolloomKnot)this.getKnot()).setBalloonsTied(((EntityBolloomKnot)this.getKnot()).getBalloonsTied() - 1);
				}
				this.setUntied();
			}
		}
//		if(!world.isRemote()) {
//			if(this.getAttachedEntity() != null) {
//				if(!this.getAttachedEntity().isAlive() && this.getDataManager().get(ATTACHED_ENTITY)) {
//					if(this.getAttachedEntity() != null) {
//						this.getAttachedEntity().getCapability(BalloonProvider.BALLOON_CAP, null)
//						.ifPresent(balloons -> {
//							balloons.decrementBalloons(1);
//						});
//					}
//					this.setUntied();
//					this.getDataManager().set(ATTACHED_ENTITY, false);
//				}
//				if(this.getAttachedEntity().isAlive() && this.getDataManager().get(ATTACHED_ENTITY)) {
//					this.applyMotionToAttachedEntity(getAttachedEntity());
//				}
//			}
//		}
		this.incrementTicksExisted();
	}
	
	@Override
	protected void registerData() {
		this.getDataManager().register(KNOT_UNIQUE_ID, Optional.empty());
		this.getDataManager().register(ATTACHED_ENTITY_UNIQUE_ID, Optional.empty());
		this.getDataManager().register(ORIGINAL_X, 0F);
		this.getDataManager().register(ORIGINAL_Z, 0F);
		this.getDataManager().register(ORIGINAL_Y, 0F);
		this.getDataManager().register(FENCE_POS, BlockPos.ZERO);
		this.getDataManager().register(UNTIED, false);
		this.getDataManager().register(ATTACHED_ENTITY, false);
		this.getDataManager().register(ANGLE, 0F);
		this.getDataManager().register(SWAY, 0F);
		this.getDataManager().register(DESIRED_ANGLE, 0F);
		this.getDataManager().register(TICKSEXISTED, 0);
		this.getDataManager().register(COLOR, (byte)16);
	}

	@Override
	protected void writeAdditional(CompoundNBT nbt) {
		if(this.getKnotId() != null) {
			nbt.putUniqueId("KnotUUID", this.getKnotId());
		}
		if(this.getAttachedEntityId() != null) {
			nbt.putUniqueId("AttachedEntityUUID", this.getAttachedEntityId());
		}
		nbt.putBoolean("UNTIED", this.getDataManager().get(UNTIED));
		nbt.putBoolean("ATTACHED", this.getDataManager().get(ATTACHED_ENTITY));
		nbt.putFloat("ORIGIN_X", this.getDataManager().get(ORIGINAL_X));
		nbt.putFloat("ORIGIN_Y", this.getDataManager().get(ORIGINAL_Y));
		nbt.putFloat("ORIGIN_Z", this.getDataManager().get(ORIGINAL_Z));
		nbt.putLong("FENCE_POS", this.getDataManager().get(FENCE_POS).toLong());
		nbt.putByte("Color", this.dataManager.get(COLOR));
	}
	
	@Override
	protected void readAdditional(CompoundNBT nbt) {
		this.setKnotId(nbt.getUniqueId("KnotUUID"));
		this.setAttachedEntityId(nbt.getUniqueId("AttachedEntityUUID"));
		this.getDataManager().set(UNTIED, nbt.getBoolean("UNTIED"));
		this.getDataManager().set(ATTACHED_ENTITY, nbt.getBoolean("ATTACHED"));
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
	
	public void onBroken(@Nullable Entity brokenEntity) {
		this.playSound(SoundEvents.BLOCK_WET_GRASS_BREAK, 1.0F, 1.0F);
		if(this.getKnot() != null) {
			((EntityBolloomKnot)this.getKnot()).setBalloonsTied(((EntityBolloomKnot)this.getKnot()).getBalloonsTied() - 1);
		}
		if(this.getAttachedEntity() != null) {
			this.getAttachedEntity().getCapability(BalloonProvider.BALLOON_CAP, null)
			.ifPresent(balloons -> {
				balloons.decrementBalloons(1);
			});
		}
		this.doParticles();
	}
	
	@Override
	public void onKillCommand() {
		if(!this.getEntityWorld().isRemote) {
			if(this.getKnot() != null) {
				((EntityBolloomKnot)this.getKnot()).setBalloonsTied(((EntityBolloomKnot)this.getKnot()).getBalloonsTied() - 1);
			}
			if(this.getAttachedEntity() != null) {
				this.getAttachedEntity().getCapability(BalloonProvider.BALLOON_CAP, null)
				.ifPresent(balloons -> {
					balloons.decrementBalloons(1);
				});
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
				this.onBroken(source.getTrueSource());
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
	
	private void doParticles() {
		if (this.world instanceof ServerWorld) {
			((ServerWorld)this.world).spawnParticle(new BlockParticleData(ParticleTypes.BLOCK, EEBlocks.BOLLOOM_PARTICLE.getDefaultState()), this.posX, this.posY + (double)this.getHeight() / 1.5D, this.posZ, 10, (double)(this.getWidth() / 4.0F), (double)(this.getHeight() / 4.0F), (double)(this.getWidth() / 4.0F), 0.05D);
		}
	}
	
	public void applyMotionToAttachedEntity(Entity entity) {
		this.getAttachedEntity().getCapability(BalloonProvider.BALLOON_CAP, null)
		.ifPresent(balloons -> {
			if(entity instanceof LivingEntity) {
				if(balloons.getBalloonsTied() == 1 && !entity.onGround) {
					//entity.setMotionMultiplier(world.getBlockState(entity.getPosition()), new Vec3d(1, 0.8F, 1));
					entity.setMotion(entity.getMotion().mul(1, 0.8, 1));
					entity.fallDistance = 0;
				} else if(balloons.getBalloonsTied() == 2 && !entity.onGround) {
					//entity.setMotionMultiplier(world.getBlockState(entity.getPosition()), new Vec3d(1, 0.5F, 1));
					entity.setMotion(entity.getMotion().mul(1, 0.5, 1));
					entity.fallDistance = 0;
				} else if(balloons.getBalloonsTied() == 3) {
				
				} else if(balloons.getBalloonsTied() == 4) {
				
				}
			entity.fallDistance = 0;
			}
		});
	}
	
	public static void addBalloonToEntity(Entity entity) {
		EntityBolloomBalloon balloon = new EntityBolloomBalloon(entity.getEntityWorld(), entity);
		entity.getEntityWorld().addEntity(balloon);
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
	
	@Nullable
	public Entity getAttachedEntity() {
		return ((ServerWorld)world).getEntityByUuid(getAttachedEntityId());
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
	public final boolean processInitialInteract(PlayerEntity player, Hand hand) {
		ItemStack itemstack = player.getHeldItem(hand);
		if (itemstack.getItem() instanceof DyeItem) {
			if(!world.isRemote) {
				this.setColor(((DyeItem)itemstack.getItem()).getDyeColor());
				itemstack.shrink(1);
			}
		}
		return super.processInitialInteract(player, hand);
	}
	
	@Override
	protected Vec3d handlePistonMovement(Vec3d pos) {
		return Vec3d.ZERO;
	}
	
	@Nullable
    public UUID getKnotId() {
        return this.dataManager.get(KNOT_UNIQUE_ID).orElse((UUID)null);
    }

    public void setKnotId(@Nullable UUID knotUUID) {
        this.dataManager.set(KNOT_UNIQUE_ID, Optional.ofNullable(knotUUID));
    }
    
    @Nullable
    public UUID getAttachedEntityId() {
        return this.dataManager.get(ATTACHED_ENTITY_UNIQUE_ID).orElse((UUID)null);
    }

    public void setAttachedEntityId(@Nullable UUID knotUUID) {
        this.dataManager.set(ATTACHED_ENTITY_UNIQUE_ID, Optional.ofNullable(knotUUID));
    }
    
    public void setAngle(float degree) {
		this.getDataManager().set(ANGLE, degree);
	}
	
	public float getAngle() {
		return this.getDataManager().get(ANGLE);
	}
	
	public float getDesiredAngle() {
		return this.getDataManager().get(DESIRED_ANGLE);
	}
    
    public float getVineAngle() {
		return (float) Math.atan(this.dataManager.get(SWAY) / 2F);
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
	
	public void applyEntityCollision(Entity entityIn) {
		if (entityIn instanceof EntityBolloomBalloon) {
			if (entityIn.getBoundingBox().minY < this.getBoundingBox().maxY) {
				super.applyEntityCollision(entityIn);
			}
		} else if (entityIn.posY >= this.getBoundingBox().minY) {
			super.applyEntityCollision(entityIn);
		}
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
