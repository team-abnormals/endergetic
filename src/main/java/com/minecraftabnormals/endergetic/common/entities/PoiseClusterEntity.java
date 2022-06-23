package com.minecraftabnormals.endergetic.common.entities;

import java.util.List;

import javax.annotation.Nullable;

import com.minecraftabnormals.abnormals_core.core.util.MathUtil;
import com.minecraftabnormals.abnormals_core.core.util.NetworkUtil;
import com.minecraftabnormals.endergetic.core.registry.EEBlocks;
import com.minecraftabnormals.endergetic.core.registry.EEEntities;
import com.minecraftabnormals.endergetic.core.registry.EESounds;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.core.NonNullList;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.HitResult.Type;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class PoiseClusterEntity extends LivingEntity {
	private static final int MAX_BLOCKS_TO_MOVE_UP = 30;
	private static final EntityDataAccessor<BlockPos> ORIGIN = SynchedEntityData.defineId(PoiseClusterEntity.class, EntityDataSerializers.BLOCK_POS);
	private static final EntityDataAccessor<Integer> BLOCKS_TO_MOVE_UP = SynchedEntityData.defineId(PoiseClusterEntity.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Integer> TIMES_HIT = SynchedEntityData.defineId(PoiseClusterEntity.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Boolean> ASCEND = SynchedEntityData.defineId(PoiseClusterEntity.class, EntityDataSerializers.BOOLEAN);
	private boolean playedSound;

	public PoiseClusterEntity(EntityType<? extends PoiseClusterEntity> cluster, Level worldIn) {
		super(EEEntities.POISE_CLUSTER.get(), worldIn);
	}

	public PoiseClusterEntity(Level worldIn, BlockPos origin, double x, double y, double z) {
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
	public void addAdditionalSaveData(CompoundTag nbt) {
		super.addAdditionalSaveData(nbt);
		nbt.putLong("OriginPos", this.getOrigin().asLong());
		nbt.putInt("BlocksToMoveUp", this.getBlocksToMoveUp());
		nbt.putInt("TimesHit", this.getTimesHit());
		nbt.putBoolean("IsAscending", this.isAscending());
	}

	@Override
	public void readAdditionalSaveData(CompoundTag nbt) {
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
		AABB bb = this.getBoundingBox().move(0, 1, 0);
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
	protected float getStandingEyeHeight(Pose poseIn, EntityDimensions size) {
		return size.height;
	}

	private boolean isBlockBlockingPath(boolean down) {
		Vec3 eyePos = down ? this.position() : this.getEyePosition(1.0F);
		return this.level.clip(new ClipContext(
				eyePos,
				eyePos.add(this.getDeltaMovement()),
				ClipContext.Block.OUTLINE,
				ClipContext.Fluid.ANY,
				this
		)).getType() != Type.MISS;
	}

	private void moveEntitiesUp() {
		if (this.getDeltaMovement().length() > 0 && this.isAscending()) {
			AABB clusterBB = this.getBoundingBox().move(0.0F, 0.01F, 0.0F);
			List<Entity> entitiesAbove = this.level.getEntities(this, clusterBB);
			if (!entitiesAbove.isEmpty()) {
				for (Entity entity : entitiesAbove) {
					if (!entity.isPassenger() && !(entity instanceof PoiseClusterEntity) && entity.getPistonPushReaction() != PushReaction.IGNORE) {
						AABB entityBB = entity.getBoundingBox();
						double distanceMotion = (clusterBB.maxY - entityBB.minY) + (entity instanceof Player ? 0.0225F : 0.02F);

						if (entity instanceof Player) {
							entity.move(MoverType.PISTON, new Vec3(0.0F, distanceMotion, 0.0F));
						} else {
							entity.move(MoverType.SELF, new Vec3(0.0F, distanceMotion, 0.0F));
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
	public MobType getMobType() {
		return MobType.ILLAGER;
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
	public AABB getCollisionBox(Entity entityIn) {
		return entityIn.isPushable() ? entityIn.getBoundingBox() : null;
	}

	@Nullable
	public AABB getCollisionBoundingBox() {
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
	public ItemStack getItemBySlot(EquipmentSlot slotIn) {
		return ItemStack.EMPTY;
	}

	@Override
	public void setItemSlot(EquipmentSlot slotIn, ItemStack stack) {
	}

	@Override
	public HumanoidArm getMainArm() {
		return HumanoidArm.RIGHT;
	}

	@OnlyIn(Dist.CLIENT)
	private static class PoiseClusterSound extends AbstractTickableSoundInstance {
		private final PoiseClusterEntity cluster;
		private int ticksRemoved;

		private PoiseClusterSound(PoiseClusterEntity cluster) {
			super(EESounds.POISE_CLUSTER_AMBIENT.get(), SoundSource.NEUTRAL);
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
