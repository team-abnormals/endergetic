package com.minecraftabnormals.endergetic.common.entities;

import java.util.List;

import javax.annotation.Nullable;

import com.minecraftabnormals.endergetic.core.registry.EEBlocks;
import com.minecraftabnormals.endergetic.core.registry.EEEntities;
import com.minecraftabnormals.endergetic.core.registry.EESounds;
import com.teamabnormals.abnormals_core.core.utils.MathUtils;
import com.teamabnormals.abnormals_core.core.utils.NetworkUtil;

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
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class PoiseClusterEntity extends LivingEntity {
	private static final int MAX_BLOCKS_TO_MOVE_UP = 30;
	private static final DataParameter<BlockPos> ORIGIN = EntityDataManager.createKey(PoiseClusterEntity.class, DataSerializers.BLOCK_POS);
	private static final DataParameter<Integer> BLOCKS_TO_MOVE_UP = EntityDataManager.createKey(PoiseClusterEntity.class, DataSerializers.VARINT);
	private static final DataParameter<Integer> TIMES_HIT = EntityDataManager.createKey(PoiseClusterEntity.class, DataSerializers.VARINT);
	private static final DataParameter<Boolean> ASCEND = EntityDataManager.createKey(PoiseClusterEntity.class, DataSerializers.BOOLEAN);
	private boolean playedSound;
	
	public PoiseClusterEntity(EntityType<? extends PoiseClusterEntity> cluster, World worldIn) {
		super(EEEntities.POISE_CLUSTER.get(), worldIn);
	}
	
	public PoiseClusterEntity(World worldIn, BlockPos origin, double x, double y, double z) {
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
	}
	
	@Override
	public void readAdditional(CompoundNBT nbt) {
		super.readAdditional(nbt);
		this.setOrigin(BlockPos.fromLong(nbt.getLong("OriginPos")));
		this.setBlocksToMoveUp(nbt.getInt("BlocksToMoveUp"));
		this.setTimesHit(nbt.getInt("TimesHit"));
		this.setAscending(nbt.getBoolean("IsAscending"));
	}
	
	@Override
	public void tick() {
		this.moveEntitiesUp(false);
		super.tick();
		
		this.renderYawOffset = this.prevRenderYawOffset = 180.0F;
		this.rotationYaw = this.prevRotationYaw = 180.0F;
		
		if (this.getPosY() + 1.0F < (this.getOrigin().getY() + this.getBlocksToMoveUp()) && this.isAscending()) {
			this.setMotion(0.0F, 0.05F, 0.0F);
		}
		
		if (this.getPosY() + 1.0F >= this.getOrigin().getY() + this.getBlocksToMoveUp()) {
			if (!this.world.isRemote) {
				this.setAscending(false);
			}
			this.setBlocksToMoveUp(0);
		}
		
		if (!this.isAscending()) {
			if (this.getPosY() > this.getOrigin().getY()) {
				this.setMotion(0, -0.05F, 0);
			} else if (Math.ceil(this.getPosY()) == this.getOrigin().getY() && this.ticksExisted > 10) {
				for (int i = 0; i < 8; i++) {
					double offsetX = MathUtils.makeNegativeRandomly(this.rand.nextFloat() * 0.25F, this.rand);
					double offsetZ = MathUtils.makeNegativeRandomly(this.rand.nextFloat() * 0.25F, this.rand);
				
					double x = this.getOrigin().getX() + 0.5D + offsetX;
					double y = this.getOrigin().getY() + 0.5D + (this.rand.nextFloat() * 0.05F);
					double z = this.getOrigin().getZ() + 0.5D + offsetZ;
				
					if (this.isServerWorld()) {
						NetworkUtil.spawnParticle("endergetic:short_poise_bubble", x, y, z, MathUtils.makeNegativeRandomly((rand.nextFloat() * 0.1F), rand) + 0.025F, (rand.nextFloat() * 0.15F) + 0.1F, MathUtils.makeNegativeRandomly((rand.nextFloat() * 0.1F), rand) + 0.025F);
					}
				}
				this.world.setBlockState(this.getOrigin(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
				this.remove();
			}
			
			if (this.isBlockBlockingPath(true) && this.ticksExisted > 10) {
				BlockPos pos = this.getPosition();
				
				for (int i = 0; i < 8; i++) {
					double offsetX = MathUtils.makeNegativeRandomly(this.rand.nextFloat() * 0.25F, this.rand);
					double offsetZ = MathUtils.makeNegativeRandomly(this.rand.nextFloat() * 0.25F, this.rand);
				
					double x = pos.getX() + 0.5D + offsetX;
					double y = pos.getY() + 0.5D + (this.rand.nextFloat() * 0.05F);
					double z = pos.getZ() + 0.5D + offsetZ;
				
					if (this.isServerWorld()) {
						NetworkUtil.spawnParticle("endergetic:short_poise_bubble", x, y, z, MathUtils.makeNegativeRandomly((this.rand.nextFloat() * 0.1F), this.rand) + 0.025F, (this.rand.nextFloat() * 0.15F) + 0.1F, MathUtils.makeNegativeRandomly((this.rand.nextFloat() * 0.1F), this.rand) + 0.025F);
					}
				}
				
				this.world.setBlockState(pos, EEBlocks.POISE_CLUSTER.get().getDefaultState());
				this.remove();
			}
		}
		
		/*
		 * Used to make it try to move down if  there is another entity of itself above it
		 */
		AxisAlignedBB bb = this.getBoundingBox().offset(0, 1, 0);
		List<Entity> entities = this.getEntityWorld().getEntitiesWithinAABB(Entity.class, bb);
		int entityCount = entities.size();
		boolean hasEntity = entityCount > 0;
		if (hasEntity && this.isAscending()) {
			
			for (int i = 0; i < entities.size(); i++) {
				Entity entity = entities.get(i);
				
				if (entity instanceof PoiseClusterEntity) {
					if (!this.world.isRemote) {
						this.setAscending(false);
					}
					this.setBlocksToMoveUp(0);
				}
				
				entity.fallDistance = 0.0F;
			}
			
		}
		
		/*
		 * Tell it to being moving down if  a block is blocking its way up at its position above
		 */
		if (this.isAscending()) {
			if (this.prevPosY == this.getPosY() && this.isBlockBlockingPath(false)) {
				this.descentCluster();
			}
			
			if (this.prevPosY == this.getPosY() && this.ticksExisted % 25 == 0 && this.getPosY() + 1.0F >= this.getOrigin().getY() + this.getBlocksToMoveUp()) {
				this.descentCluster();
			}
		}
		
		if (this.getBlocksToMoveUp() > MAX_BLOCKS_TO_MOVE_UP) {
			this.setBlocksToMoveUp(MAX_BLOCKS_TO_MOVE_UP);
		}
		
		this.clearActivePotions();
		this.extinguish();
		
		if (this.getHealth() != 0) this.setHealth(100);
		
		if (!this.world.isRemote) {
			if (!this.playedSound) {
				this.world.setEntityState(this, (byte) 1);
				this.playedSound = true;
			}
		}
		
		this.moveEntitiesUp(true);
	}
	
	@Override
	public boolean hitByEntity(Entity entityIn) {
		this.setTimesHit(this.getTimesHit() + 1);
		if (this.getTimesHit() >= 3) {
			if (!this.world.isRemote) {
				Block.spawnAsEntity(this.world, this.getPosition(), new ItemStack(EEBlocks.POISE_CLUSTER.get()));
			}
			this.remove();
			return true;
		}
		this.setAscending(true);
		this.setBlocksToMoveUp((int) (Math.ceil(this.getPosY()) - this.getOrigin().getY()) + 10);
		return false;
	}
	
	@Override
	protected void damageEntity(DamageSource damageSrc, float damageAmount) {
		if (damageSrc.isProjectile()) {
			this.setTimesHit(this.getTimesHit() + 1);
			
			if (this.getTimesHit() >= 3) {
				if (!this.getEntityWorld().isRemote) {
					Block.spawnAsEntity(this.world, this.getPosition(), new ItemStack(EEBlocks.POISE_CLUSTER.get()));
				}
				this.remove();
			}
			
			if ((int) (Math.ceil(this.getPosY()) - this.getOrigin().getY()) + 10 < 30) {
				this.setAscending(true);
				this.setBlocksToMoveUp((int) (Math.ceil(this.getPosY()) - this.getOrigin().getY()) + 10);
			} else {
				this.remove();
			}
		}
		
		super.damageEntity(damageSrc, damageAmount);
	}
	
	@OnlyIn(Dist.CLIENT)
	@Override
	public void handleStatusUpdate(byte id) {
		if (id == 1) {
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
	public boolean onLivingFall(float distance, float damageMultiplier) {
		return false;
	}
	
	@Override
	public boolean isInvulnerableTo(DamageSource source) {
		return source == DamageSource.IN_WALL || super.isInvulnerableTo(source);
	}
	
	@Override
	public boolean isAlive() {
		return false;
	}
	
	@Override
	protected float getStandingEyeHeight(Pose poseIn, EntitySize size) {
		return size.height;
	}
	
	private boolean isBlockBlockingPath(boolean down) {
		Vector3d eyePos = down ? this.getPositionVec() : this.getEyePosition(1.0F);
		return this.world.rayTraceBlocks(new RayTraceContext(
			eyePos,
			eyePos.add(this.getMotion()),
			RayTraceContext.BlockMode.OUTLINE,
			RayTraceContext.FluidMode.ANY,
			this
		)).getType() != Type.MISS;
	}
	
	private void moveEntitiesUp(boolean afterTick) {
		if (this.getMotion().length() > 0 && this.isAscending()) {
			AxisAlignedBB clusterBB = this.getBoundingBox().offset(0.0F, 0.01F, 0.0F);
			List<Entity> entitiesAbove = this.world.getEntitiesWithinAABBExcludingEntity(null, clusterBB);
			if (!entitiesAbove.isEmpty()) {
				for (int i = 0; i < entitiesAbove.size(); i++) {
					Entity entity = entitiesAbove.get(i);
					if (!entity.isPassenger() && !(entity instanceof PoiseClusterEntity || (entity instanceof PlayerEntity && !afterTick)) && entity.getPushReaction() != PushReaction.IGNORE) {
						AxisAlignedBB entityBB = entity.getBoundingBox();
						double distanceMotion = (clusterBB.maxY - entityBB.minY) + (entity instanceof PlayerEntity ? 0.0225F : 0.02F);

						if (entity instanceof PlayerEntity) {
							entity.move(MoverType.PISTON, new Vector3d(0.0F, distanceMotion, 0.0F));
						} else {
							entity.move(MoverType.SELF, new Vector3d(0.0F, distanceMotion, 0.0F));
						}
						entity.setOnGround(true);
					}
				}
			}
		}
	}
	
	protected void descentCluster() {
		if (!this.world.isRemote) {
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
	public void applyKnockback(float strengthIn, double ratioXIn, double ratioZIn) {}
	
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
	public void setFire(int seconds) {}
	
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
		private final PoiseClusterEntity cluster;
		private int ticksRemoved;
		
		protected PoiseClusterSound(PoiseClusterEntity cluster) {
			super(EESounds.POISE_CLUSTER_AMBIENT.get(), SoundCategory.NEUTRAL);
			this.cluster = cluster;
			this.repeat = true;
			this.repeatDelay = 0;
			this.volume = 1.0F;
			this.x = (float) cluster.getPosX();
			this.y = (float) cluster.getPosY();
			this.z = (float) cluster.getPosZ();
			
			this.pitch = cluster.getRNG().nextFloat() * 0.3F + 0.8F;
		}
		
		@Override
		public boolean canBeSilent() {
			return true;
		}
		
		public void tick() {
			if (this.cluster.isAlive()) {
				this.x = (float) this.cluster.getPosX();
				this.y = (float) this.cluster.getPosY();
				this.z = (float) this.cluster.getPosZ();
			} else {
				this.ticksRemoved++;
				if (this.ticksRemoved > 10) {
					this.func_239509_o_();
				}
			}
			
			this.volume = Math.max(0.0F, this.volume - ((float) this.ticksRemoved / 10.0F));
		}
	}
}
