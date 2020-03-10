package endergeticexpansion.common.entities;

import java.util.List;

import javax.annotation.Nullable;

import endergeticexpansion.api.util.MathUtils;
import endergeticexpansion.api.util.NetworkUtil;
import endergeticexpansion.core.registry.EEBlocks;
import endergeticexpansion.core.registry.EEEntities;
import endergeticexpansion.core.registry.EESounds;
import net.minecraft.block.Block;
import net.minecraft.block.material.PushReaction;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.TickableSound;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.Pose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.HandSide;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class EntityPoiseCluster extends LivingEntity {
	private static final int MAX_BLOCKS_TO_MOVE_UP = 30;
	private static final DataParameter<BlockPos> ORIGIN = EntityDataManager.createKey(EntityPoiseCluster.class, DataSerializers.BLOCK_POS);
	private static final DataParameter<Integer> BLOCKS_TO_MOVE_UP = EntityDataManager.createKey(EntityPoiseCluster.class, DataSerializers.VARINT);
	private static final DataParameter<Integer> TIMES_HIT = EntityDataManager.createKey(EntityPoiseCluster.class, DataSerializers.VARINT);
	private static final DataParameter<Boolean> ASCEND = EntityDataManager.createKey(EntityPoiseCluster.class, DataSerializers.BOOLEAN);
	private boolean playedSound;
	
	public EntityPoiseCluster(EntityType<? extends EntityPoiseCluster> cluster, World worldIn) {
		super(EEEntities.POISE_CLUSTER.get(), worldIn);
	}
	
	public EntityPoiseCluster(World worldIn, BlockPos origin, double x, double y, double z) {
		this(EEEntities.POISE_CLUSTER.get(), worldIn);
		this.setHealth(100);
		this.setOrigin(new BlockPos(origin));
		this.setPosition(x + 0.5D, y, z + 0.5D);
		this.setNoGravity(true);
		this.prevRenderYawOffset = 180.0F;
		this.renderYawOffset = 180.0F;
		this.rotationYaw = 180.0F;
	}
	
	@Override
	protected void registerData() {
		super.registerData();
		this.dataManager.register(ORIGIN, BlockPos.ZERO);
		this.dataManager.register(BLOCKS_TO_MOVE_UP, 0);
		this.dataManager.register(TIMES_HIT, 0);
		this.dataManager.register(ASCEND, true);
	}
	
	@Override
	public void writeAdditional(CompoundNBT nbt) {
		super.writeAdditional(nbt);
		nbt.putLong("OriginPos", this.getOrigin().toLong());
		nbt.putInt("BlocksToMoveUp", this.getBlocksToMoveUp());
		nbt.putInt("TimesHit", this.getTimesHit());
		nbt.putBoolean("IsAscending", this.isAscending());
		nbt.putBoolean("PlayedClusterHoveringSound", this.playedSound);
	}
	
	@Override
	public void readAdditional(CompoundNBT nbt) {
		super.readAdditional(nbt);
		this.setOrigin(BlockPos.fromLong(nbt.getLong("OriginPos")));
		this.setBlocksToMoveUp(nbt.getInt("BlocksToMoveUp"));
		this.setTimesHit(nbt.getInt("TimesHit"));
		this.setAscending(nbt.getBoolean("IsAscending"));
		this.playedSound = nbt.getBoolean("PlayedClusterHoveringSound");
	}
	
	@Override
	public void tick() {
		this.moveEntitiesUp(false);
		super.tick();
		
		this.renderYawOffset = this.prevRenderYawOffset = 180.0F;
		this.rotationYaw = this.prevRotationYaw = 180.0F;
		
		if(this.posY + 1.0F < (this.getOrigin().getY() + this.getBlocksToMoveUp()) && this.isAscending()) {
			this.setMotion(0.0F, 0.05F, 0.0F);
		}
		
		if(this.posY + 1.0F >= this.getOrigin().getY() + this.getBlocksToMoveUp()) {
			if(!this.world.isRemote) {
				this.setAscending(false);
			}
			this.setBlocksToMoveUp(0);
		}
		
		if(!this.isAscending()) {
			if(this.posY > this.getOrigin().getY()) {
				this.setMotion(0, -Math.toRadians(3), 0);
			} else if(Math.ceil(this.posY) == this.getOrigin().getY() && this.ticksExisted > 10) {
				for(int i = 0; i < 8; i++) {
					double offsetX = MathUtils.makeNegativeRandomly(this.rand.nextFloat() * 0.25F, this.rand);
					double offsetZ = MathUtils.makeNegativeRandomly(this.rand.nextFloat() * 0.25F, this.rand);
				
					double x = this.getOrigin().getX() + 0.5D + offsetX;
					double y = this.getOrigin().getY() + 0.5D + (this.rand.nextFloat() * 0.05F);
					double z = this.getOrigin().getZ() + 0.5D + offsetZ;
				
					if(this.isServerWorld()) {
						NetworkUtil.spawnParticle("endergetic:short_poise_bubble", x, y, z, MathUtils.makeNegativeRandomly((rand.nextFloat() * 0.1F), rand) + 0.025F, (rand.nextFloat() * 0.15F) + 0.1F, MathUtils.makeNegativeRandomly((rand.nextFloat() * 0.1F), rand) + 0.025F);
					}
				}
				this.world.setBlockState(this.getOrigin(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
				this.remove();
			}
			
			if(this.isBlockBlockingPath(true) && this.ticksExisted > 10) {
				BlockPos pos = this.getPosition();
				
				for(int i = 0; i < 8; i++) {
					double offsetX = MathUtils.makeNegativeRandomly(this.rand.nextFloat() * 0.25F, this.rand);
					double offsetZ = MathUtils.makeNegativeRandomly(this.rand.nextFloat() * 0.25F, this.rand);
				
					double x = pos.getX() + 0.5D + offsetX;
					double y = pos.getY() + 0.5D + (this.rand.nextFloat() * 0.05F);
					double z = pos.getZ() + 0.5D + offsetZ;
				
					if(this.isServerWorld()) {
						NetworkUtil.spawnParticle("endergetic:short_poise_bubble", x, y, z, MathUtils.makeNegativeRandomly((this.rand.nextFloat() * 0.1F), this.rand) + 0.025F, (this.rand.nextFloat() * 0.15F) + 0.1F, MathUtils.makeNegativeRandomly((this.rand.nextFloat() * 0.1F), this.rand) + 0.025F);
					}
				}
				
				this.world.setBlockState(pos, EEBlocks.POISE_CLUSTER.get().getDefaultState());
				this.remove();
			}
		}
		
		/*
		 * Used to make it try to move down if there is another entity of itself above it
		 */
		AxisAlignedBB bb = this.getBoundingBox().offset(0, 1, 0);
		List<Entity> entities = this.getEntityWorld().getEntitiesWithinAABB(Entity.class, bb);
		int entityCount = entities.size();
		boolean hasEntity = entityCount > 0;
		if(hasEntity && this.isAscending()) {
			
			for(int i = 0; i < entities.size(); i++) {
				Entity entity = entities.get(i);
				
				if(entity instanceof EntityPoiseCluster) {
					if(!this.world.isRemote) {
						this.setAscending(false);
					}
					this.setBlocksToMoveUp(0);
				}
				
				entity.fallDistance = 0.0F;
			}
			
		}
		
		/*
		 * Tell it to being moving down if a block is blocking its way up at its position above
		 */
		if(this.isAscending()) {
			if(this.isBlockBlockingPath(false)) {
				this.descentCluster();
			}
			
			if(this.prevPosY == this.posY && this.ticksExisted % 25 == 0 && this.posY + 1.0F >= this.getOrigin().getY() + this.getBlocksToMoveUp()) {
				this.descentCluster();
			}
		}
		
		if(this.getBlocksToMoveUp() > MAX_BLOCKS_TO_MOVE_UP) {
			this.setBlocksToMoveUp(MAX_BLOCKS_TO_MOVE_UP);
		}
		
		this.clearActivePotions();
		this.extinguish();
		
		if(this.getHealth() != 0) this.setHealth(100);
		
		if(!this.world.isRemote) {
			if(!this.playedSound) {
				this.world.setEntityState(this, (byte) 1);
				this.playedSound = true;
			}
		}
		
		this.moveEntitiesUp(true);
	}
	
	@Override
	public boolean hitByEntity(Entity entityIn) {
		this.setTimesHit(this.getTimesHit() + 1);
		if(this.getTimesHit() >= 3) {
			if(!this.world.isRemote) {
				Block.spawnAsEntity(this.world, this.getPosition(), new ItemStack(EEBlocks.POISE_CLUSTER.get()));
			}
			this.remove();
			return true;
		}
		this.setAscending(true);
		this.setBlocksToMoveUp((int) (Math.ceil(this.posY) - this.getOrigin().getY()) + 10);
		return false;
	}
	
	@Override
	protected void damageEntity(DamageSource damageSrc, float damageAmount) {
		if(damageSrc.isProjectile()) {
			this.setTimesHit(this.getTimesHit() + 1);
			
			if(this.getTimesHit() >= 3) {
				if(!this.getEntityWorld().isRemote) {
					Block.spawnAsEntity(this.world, this.getPosition(), new ItemStack(EEBlocks.POISE_CLUSTER.get()));
				}
				this.remove();
			}
			
			if((int) (Math.ceil(this.posY) - this.getOrigin().getY()) + 10 < 30) {
				this.setAscending(true);
				this.setBlocksToMoveUp((int) (Math.ceil(this.posY) - this.getOrigin().getY()) + 10);
			} else {
				this.remove();
			}
		}
		
		super.damageEntity(damageSrc, damageAmount);
	}
	
	@OnlyIn(Dist.CLIENT)
	@Override
	public void handleStatusUpdate(byte id) {
		if(id == 1) {
			Minecraft.getInstance().getSoundHandler().play(new PoiseClusterSound(this));
		} else {
			super.handleStatusUpdate(id);
		}
	}
	
	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return EESounds.CLUSTER_BREAK.get();
	}
	
	@Override
	public void fall(float distance, float damageMultiplier) {}
	
	@Override
	public boolean isInvulnerableTo(DamageSource source) {
		return source == DamageSource.IN_WALL || super.isInvulnerableTo(source);
	}
	
	@Override
	protected float getStandingEyeHeight(Pose poseIn, EntitySize size) {
		return size.height;
	}
	
	private boolean isBlockBlockingPath(boolean down) {
		Vec3d eyePos = down ? this.getPositionVec() : this.getEyePosition(1.0F);
		return this.world.rayTraceBlocks(new RayTraceContext(
			eyePos,
			eyePos.add(this.getMotion()),
			RayTraceContext.BlockMode.OUTLINE,
			RayTraceContext.FluidMode.ANY,
			this
		)).getType() != Type.MISS;
	}
	
	private void moveEntitiesUp(boolean afterTick) {
		if(this.getMotion().length() > 0 && this.isAscending()) {
			AxisAlignedBB clusterBB = this.getBoundingBox().offset(0.0F, 0.01F, 0.0F);
			List<Entity> entitiesAbove = this.world.getEntitiesWithinAABBExcludingEntity(null, clusterBB);
			if(!entitiesAbove.isEmpty()) {
				for(int i = 0; i < entitiesAbove.size(); i++) {
					Entity entity = entitiesAbove.get(i);
					if(!entity.isPassenger() && !(entity instanceof EntityPoiseCluster || (entity instanceof PlayerEntity && !afterTick)) && entity.getPushReaction() != PushReaction.IGNORE) {
						AxisAlignedBB entityBB = entity.getBoundingBox();
						double distanceMotion = (clusterBB.maxY - entityBB.minY) + (entity instanceof PlayerEntity ? 0.0225F : 0.02F);

						if(entity instanceof PlayerEntity) {
							entity.move(MoverType.PISTON, new Vec3d(0.0F, distanceMotion, 0.0F));
						} else {
							entity.move(MoverType.SELF, new Vec3d(0.0F, distanceMotion, 0.0F));
						}
						entity.onGround = true;
					}
				}
			}
		}
	}
	
	protected void descentCluster() {
		if(!this.world.isRemote) {
			this.setAscending(false);
		}
		this.setBlocksToMoveUp(0);
	}
	
	public void setOrigin(BlockPos pos) {
		this.dataManager.set(ORIGIN, pos);
	}

	public BlockPos getOrigin() {
		return this.dataManager.get(ORIGIN);
	}
	
	public void setBlocksToMoveUp(int value) {
		this.dataManager.set(BLOCKS_TO_MOVE_UP, value);
	}
	
	public int getBlocksToMoveUp() {
		return this.dataManager.get(BLOCKS_TO_MOVE_UP);
	}
	
	protected void setTimesHit(int hits) {
		this.dataManager.set(TIMES_HIT, hits);
	}
	
	protected int getTimesHit() {
		return this.dataManager.get(TIMES_HIT);
	}
	
	protected void setAscending(boolean acscending) {
		this.dataManager.set(ASCEND, acscending);
	}
	
	public boolean isAscending() {
		return this.dataManager.get(ASCEND);
	}
	
	@Override
	public void knockBack(Entity entityIn, float strength, double xRatio, double zRatio) {}
	
	@Override
	public CreatureAttribute getCreatureAttribute() {
		return CreatureAttribute.ILLAGER;
	}
	
	@OnlyIn(Dist.CLIENT)
	@Override
	public boolean canRenderOnFire() {
		return false;
	}
	
	@Nullable
	public AxisAlignedBB getCollisionBox(Entity entityIn) {
		return entityIn.canBePushed() ? entityIn.getBoundingBox() : null;
	}
	
	@Nullable
	public AxisAlignedBB getCollisionBoundingBox() {
		return this.getBoundingBox();
	}
	
	@Override
	protected float getWaterSlowDown() {
		return 0;
	}
	
	@Override
	public boolean isPushedByWater() {
		return false;
	}
	
	@Override
	public boolean canBePushed() {
		return true;
	}

	@Override
	protected boolean canTriggerWalking() {
		return false;
	}
	
	@Override
	public boolean startRiding(Entity entityIn, boolean force) {
		return false;
	}
	
	@Override
	public boolean canBreatheUnderwater() {
		return true;
	}
	
	@Override
	protected void dealFireDamage(int amount) {}
	
	@Override
	public Iterable<ItemStack> getArmorInventoryList() {
		return NonNullList.withSize(4, ItemStack.EMPTY);
	}

	@Override
	public ItemStack getItemStackFromSlot(EquipmentSlotType slotIn) {
		return ItemStack.EMPTY;
	}

	@Override
	public void setItemStackToSlot(EquipmentSlotType slotIn, ItemStack stack) {}

	@Override
	public HandSide getPrimaryHand() {
		return HandSide.RIGHT;
	}
	
	@OnlyIn(Dist.CLIENT)
	private class PoiseClusterSound extends TickableSound {
		private final EntityPoiseCluster cluster;
		private int ticksRemoved;
		
		protected PoiseClusterSound(EntityPoiseCluster cluster) {
			super(EESounds.POISE_CLUSTER_AMBIENT.get(), SoundCategory.NEUTRAL);
			this.cluster = cluster;
			this.repeat = true;
			this.repeatDelay = 0;
			this.volume = 1.0F;
			this.x = (float) cluster.posX;
			this.y = (float) cluster.posY;
			this.z = (float) cluster.posZ;
		}
		
		@Override
		public boolean canBeSilent() {
			return true;
		}
		
		public void tick() {
			if(this.cluster.isAlive()) {
				this.x = (float) this.cluster.posX;
				this.y = (float) this.cluster.posY;
				this.z = (float) this.cluster.posZ;
			} else {
				this.ticksRemoved++;
				if(this.ticksRemoved > 10) {
					this.donePlaying = true;
				}
			}
			
			double distance = Math.sqrt(Minecraft.getInstance().player.getDistanceSq(this.cluster));
			float volume = MathHelper.clamp((float) (1.5F / distance), 0.0F, 0.65F);
			
			this.volume = volume < 0.15F ? 0.0F : volume;
			this.volume = Math.max(0.0F, this.volume - ((float) this.ticksRemoved / 10.0F));
		}
	}
}
