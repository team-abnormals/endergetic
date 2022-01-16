package com.minecraftabnormals.endergetic.common.entities;

import com.minecraftabnormals.abnormals_core.core.util.MathUtil;
import com.minecraftabnormals.abnormals_core.core.util.NetworkUtil;
import com.minecraftabnormals.endergetic.core.registry.EEBlocks;
import com.minecraftabnormals.endergetic.core.registry.EEEntities;
import com.minecraftabnormals.endergetic.core.registry.EESounds;
import net.minecraft.block.Block;
import net.minecraft.block.material.PushReaction;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.TickableSound;
import net.minecraft.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class PoiseClusterEntity extends LivingEntity {
	private static final int MAX_BLOCKS_TO_MOVE_UP = 30;
	private static final DataParameter<BlockPos> ORIGIN = EntityDataManager.defineId(PoiseClusterEntity.class, DataSerializers.BLOCK_POS);
	private static final DataParameter<Integer> BLOCKS_TO_MOVE_UP = EntityDataManager.defineId(PoiseClusterEntity.class, DataSerializers.INT);
	private static final DataParameter<Integer> TIMES_HIT = EntityDataManager.defineId(PoiseClusterEntity.class, DataSerializers.INT);
	private static final DataParameter<Boolean> ASCEND = EntityDataManager.defineId(PoiseClusterEntity.class, DataSerializers.BOOLEAN);
	private boolean playedSound;

	public PoiseClusterEntity(EntityType<? extends PoiseClusterEntity> cluster, World worldIn) {
		super(EEEntities.POISE_CLUSTER.get(), worldIn);
	}

	public PoiseClusterEntity(World worldIn, BlockPos origin, double x, double y, double z) {
		this(EEEntities.POISE_CLUSTER.get(), worldIn);
		this.setHealth(100);
		this.setOrigin(new BlockPos(origin));
		this.setPos(x + 0.5D, y, z + 0.5D);
		this.setNoGravity(true);
		this.yBodyRotO = 180.0F;
		this.yBodyRot = 180.0F;
		this.yRot = 180.0F;
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(ORIGIN, BlockPos.ZERO);
		this.entityData.define(BLOCKS_TO_MOVE_UP, 0);
		this.entityData.define(TIMES_HIT, 0);
		this.entityData.define(ASCEND, true);
	}

	@Override
	public void addAdditionalSaveData(CompoundNBT nbt) {
		super.addAdditionalSaveData(nbt);
		nbt.putLong("OriginPos", this.getOrigin().asLong());
		nbt.putInt("BlocksToMoveUp", this.getBlocksToMoveUp());
		nbt.putInt("TimesHit", this.getTimesHit());
		nbt.putBoolean("IsAscending", this.isAscending());
	}

	@Override
	public void readAdditionalSaveData(CompoundNBT nbt) {
		super.readAdditionalSaveData(nbt);
		this.setOrigin(BlockPos.of(nbt.getLong("OriginPos")));
		this.setBlocksToMoveUp(nbt.getInt("BlocksToMoveUp"));
		this.setTimesHit(nbt.getInt("TimesHit"));
		this.setAscending(nbt.getBoolean("IsAscending"));
	}

	@Override
	public void tick() {
		super.tick();
		if (this.isAscending()) {
			this.moveEntitiesUp();
		}

		this.yBodyRot = this.yBodyRotO = 180.0F;
		this.yRot = this.yRotO = 180.0F;

		if (this.getY() + 1.0F < (this.getOrigin().getY() + this.getBlocksToMoveUp()) && this.isAscending()) {
			this.setDeltaMovement(0.0F, 0.05F, 0.0F);
		}

		if (this.getY() + 1.0F >= this.getOrigin().getY() + this.getBlocksToMoveUp()) {
			if (!this.level.isClientSide) {
				this.setAscending(false);
			}
			this.setBlocksToMoveUp(0);
		}

		if (!this.isAscending()) {
			if (this.getY() > this.getOrigin().getY()) {
				this.setDeltaMovement(0, -0.05F, 0);
			} else if (Math.ceil(this.getY()) == this.getOrigin().getY() && this.tickCount > 10) {
				for (int i = 0; i < 8; i++) {
					double offsetX = MathUtil.makeNegativeRandomly(this.random.nextFloat() * 0.25F, this.random);
					double offsetZ = MathUtil.makeNegativeRandomly(this.random.nextFloat() * 0.25F, this.random);

					double x = this.getOrigin().getX() + 0.5D + offsetX;
					double y = this.getOrigin().getY() + 0.5D + (this.random.nextFloat() * 0.05F);
					double z = this.getOrigin().getZ() + 0.5D + offsetZ;

					if (this.isEffectiveAi()) {
						NetworkUtil.spawnParticle("endergetic:short_poise_bubble", x, y, z, MathUtil.makeNegativeRandomly((random.nextFloat() * 0.1F), random) + 0.025F, (random.nextFloat() * 0.15F) + 0.1F, MathUtil.makeNegativeRandomly((random.nextFloat() * 0.1F), random) + 0.025F);
					}
				}
				this.level.setBlockAndUpdate(this.getOrigin(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
				this.remove();
			}

			if (this.isBlockBlockingPath(true) && this.tickCount > 10) {
				BlockPos pos = this.blockPosition();

				for (int i = 0; i < 8; i++) {
					double offsetX = MathUtil.makeNegativeRandomly(this.random.nextFloat() * 0.25F, this.random);
					double offsetZ = MathUtil.makeNegativeRandomly(this.random.nextFloat() * 0.25F, this.random);

					double x = pos.getX() + 0.5D + offsetX;
					double y = pos.getY() + 0.5D + (this.random.nextFloat() * 0.05F);
					double z = pos.getZ() + 0.5D + offsetZ;

					if (this.isEffectiveAi()) {
						NetworkUtil.spawnParticle("endergetic:short_poise_bubble", x, y, z, MathUtil.makeNegativeRandomly((this.random.nextFloat() * 0.1F), this.random) + 0.025F, (this.random.nextFloat() * 0.15F) + 0.1F, MathUtil.makeNegativeRandomly((this.random.nextFloat() * 0.1F), this.random) + 0.025F);
					}
				}

				this.level.setBlockAndUpdate(pos, EEBlocks.POISE_CLUSTER.get().defaultBlockState());
				this.remove();
			}
		}

		/*
		 * Used to make it try to move down if  there is another entity of itself above it
		 */
		AxisAlignedBB bb = this.getBoundingBox().move(0, 1, 0);
		List<Entity> entities = this.getCommandSenderWorld().getEntitiesOfClass(Entity.class, bb);
		int entityCount = entities.size();
		boolean hasEntity = entityCount > 0;
		if (hasEntity && this.isAscending()) {
			for (Entity entity : entities) {
				if (entity instanceof PoiseClusterEntity) {
					if (!this.level.isClientSide) {
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
			if (this.yo == this.getY() && this.isBlockBlockingPath(false)) {
				this.beingDescending();
			}

			if (this.yo == this.getY() && this.tickCount % 25 == 0 && this.getY() + 1.0F >= this.getOrigin().getY() + this.getBlocksToMoveUp()) {
				this.beingDescending();
			}
		}

		if (this.getBlocksToMoveUp() > MAX_BLOCKS_TO_MOVE_UP) {
			this.setBlocksToMoveUp(MAX_BLOCKS_TO_MOVE_UP);
		}

		this.removeAllEffects();
		this.clearFire();

		if (this.getHealth() != 0) this.setHealth(100);

		if (!this.level.isClientSide) {
			if (!this.playedSound) {
				this.level.broadcastEntityEvent(this, (byte) 1);
				this.playedSound = true;
			}
		}
	}

	@Override
	public boolean skipAttackInteraction(Entity entityIn) {
		this.setTimesHit(this.getTimesHit() + 1);
		if (this.getTimesHit() >= 3) {
			if (!this.level.isClientSide) {
				Block.popResource(this.level, this.blockPosition(), new ItemStack(EEBlocks.POISE_CLUSTER.get()));
			}
			this.remove();
			return true;
		}
		this.setAscending(true);
		this.setBlocksToMoveUp((int) (Math.ceil(this.getY()) - this.getOrigin().getY()) + 10);
		return false;
	}

	@Override
	protected void actuallyHurt(DamageSource damageSrc, float damageAmount) {
		if (damageSrc.isProjectile()) {
			this.setTimesHit(this.getTimesHit() + 1);

			if (this.getTimesHit() >= 3) {
				if (!this.getCommandSenderWorld().isClientSide) {
					Block.popResource(this.level, this.blockPosition(), new ItemStack(EEBlocks.POISE_CLUSTER.get()));
				}
				this.remove();
			}

			if ((int) (Math.ceil(this.getY()) - this.getOrigin().getY()) + 10 < 30) {
				this.setAscending(true);
				this.setBlocksToMoveUp((int) (Math.ceil(this.getY()) - this.getOrigin().getY()) + 10);
			} else {
				this.remove();
			}
		}

		super.actuallyHurt(damageSrc, damageAmount);
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void handleEntityEvent(byte id) {
		if (id == 1) {
			Minecraft.getInstance().getSoundManager().play(new PoiseClusterSound(this));
		} else {
			super.handleEntityEvent(id);
		}
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return EESounds.CLUSTER_BREAK.get();
	}

	@Override
	public boolean causeFallDamage(float distance, float damageMultiplier) {
		return false;
	}

	@Override
	public boolean isInvulnerableTo(DamageSource source) {
		return source == DamageSource.IN_WALL || super.isInvulnerableTo(source);
	}

	@Override
	protected float getStandingEyeHeight(Pose poseIn, EntitySize size) {
		return size.height;
	}

	private boolean isBlockBlockingPath(boolean down) {
		Vector3d eyePos = down ? this.position() : this.getEyePosition(1.0F);
		return this.level.clip(new RayTraceContext(
				eyePos,
				eyePos.add(this.getDeltaMovement()),
				RayTraceContext.BlockMode.OUTLINE,
				RayTraceContext.FluidMode.ANY,
				this
		)).getType() != Type.MISS;
	}

	private void moveEntitiesUp() {
		if (this.getDeltaMovement().length() > 0 && this.isAscending()) {
			AxisAlignedBB clusterBB = this.getBoundingBox().move(0.0F, 0.01F, 0.0F);
			List<Entity> entitiesAbove = this.level.getEntities(this, clusterBB);
			if (!entitiesAbove.isEmpty()) {
				for (Entity entity : entitiesAbove) {
					if (!entity.isPassenger() && !(entity instanceof PoiseClusterEntity) && entity.getPistonPushReaction() != PushReaction.IGNORE) {
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

	private void beingDescending() {
		if (!this.level.isClientSide) {
			this.setAscending(false);
		}
		this.setBlocksToMoveUp(0);
	}

	public void setOrigin(BlockPos pos) {
		this.entityData.set(ORIGIN, pos);
	}

	public BlockPos getOrigin() {
		return this.entityData.get(ORIGIN);
	}

	public void setBlocksToMoveUp(int value) {
		this.entityData.set(BLOCKS_TO_MOVE_UP, value);
	}

	public int getBlocksToMoveUp() {
		return this.entityData.get(BLOCKS_TO_MOVE_UP);
	}

	protected void setTimesHit(int hits) {
		this.entityData.set(TIMES_HIT, hits);
	}

	protected int getTimesHit() {
		return this.entityData.get(TIMES_HIT);
	}

	protected void setAscending(boolean acscending) {
		this.entityData.set(ASCEND, acscending);
	}

	public boolean isAscending() {
		return this.entityData.get(ASCEND);
	}

	@Override
	public void knockback(float strengthIn, double ratioXIn, double ratioZIn) {
	}

	@Override
	protected void doPush(Entity entityIn) {
	}

	@Override
	public CreatureAttribute getMobType() {
		return CreatureAttribute.ILLAGER;
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public boolean displayFireAnimation() {
		return false;
	}

	@Override
	public boolean canBeCollidedWith() {
		return true;
	}

	@Nullable
	public AxisAlignedBB getCollisionBox(Entity entityIn) {
		return entityIn.isPushable() ? entityIn.getBoundingBox() : null;
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
	public boolean isPushedByFluid() {
		return false;
	}

	@Override
	public boolean isPushable() {
		return true;
	}

	@Override
	protected boolean isMovementNoisy() {
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
	public void setSecondsOnFire(int seconds) {
	}

	@Override
	public Iterable<ItemStack> getArmorSlots() {
		return NonNullList.withSize(4, ItemStack.EMPTY);
	}

	@Override
	public ItemStack getItemBySlot(EquipmentSlotType slotIn) {
		return ItemStack.EMPTY;
	}

	@Override
	public void setItemSlot(EquipmentSlotType slotIn, ItemStack stack) {
	}

	@Override
	public HandSide getMainArm() {
		return HandSide.RIGHT;
	}

	@OnlyIn(Dist.CLIENT)
	private static class PoiseClusterSound extends TickableSound {
		private final PoiseClusterEntity cluster;
		private int ticksRemoved;

		private PoiseClusterSound(PoiseClusterEntity cluster) {
			super(EESounds.POISE_CLUSTER_AMBIENT.get(), SoundCategory.NEUTRAL);
			this.cluster = cluster;
			this.looping = true;
			this.delay = 0;
			this.volume = 1.0F;
			this.x = (float) cluster.getX();
			this.y = (float) cluster.getY();
			this.z = (float) cluster.getZ();

			this.pitch = cluster.getRandom().nextFloat() * 0.3F + 0.8F;
		}

		@Override
		public boolean canStartSilent() {
			return true;
		}

		public void tick() {
			if (this.cluster.isAlive()) {
				this.x = (float) this.cluster.getX();
				this.y = (float) this.cluster.getY();
				this.z = (float) this.cluster.getZ();
			} else {
				this.ticksRemoved++;
				if (this.ticksRemoved > 10) {
					this.stop();
				}
			}

			this.volume = Math.max(0.0F, this.volume - ((float) this.ticksRemoved / 10.0F));
		}
	}
}
