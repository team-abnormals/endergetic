package com.teamabnormals.endergetic.common.entities.puffbug;

import java.util.*;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;
import com.teamabnormals.endergetic.api.entity.pathfinding.EndergeticFlyingPathNavigator;
import com.teamabnormals.endergetic.api.entity.util.RayTraceHelper;
import com.teamabnormals.endergetic.api.util.GenerationUtils;
import com.teamabnormals.endergetic.api.util.TemporaryMathUtil;
import com.teamabnormals.endergetic.client.particles.EEParticles;
import com.teamabnormals.endergetic.common.blocks.poise.BolloomBudBlock;
import com.teamabnormals.endergetic.common.entities.booflo.BoofloEntity;
import com.teamabnormals.endergetic.common.entities.puffbug.ai.PuffBugAttachToHiveGoal;
import com.teamabnormals.endergetic.common.entities.puffbug.ai.PuffBugAttackGoal;
import com.teamabnormals.endergetic.common.entities.puffbug.ai.PuffBugBoostGoal;
import com.teamabnormals.endergetic.common.entities.puffbug.ai.PuffBugCreateItemGoal;
import com.teamabnormals.endergetic.common.entities.puffbug.ai.PuffBugDescentGoal;
import com.teamabnormals.endergetic.common.entities.puffbug.ai.PuffBugPollinateGoal;
import com.teamabnormals.endergetic.common.entities.puffbug.ai.PuffBugPullOutGoal;
import com.teamabnormals.endergetic.common.entities.puffbug.ai.PuffBugRestOnHiveGoal;
import com.teamabnormals.endergetic.common.entities.puffbug.ai.PuffBugRotateToFireGoal;
import com.teamabnormals.endergetic.common.entities.puffbug.ai.PuffBugTargetAggressorGoal;
import com.teamabnormals.endergetic.common.entities.puffbug.ai.PuffBugTeleportToBudGoal;
import com.teamabnormals.endergetic.common.entities.puffbug.ai.PuffBugTeleportToRestGoal;
import com.teamabnormals.endergetic.common.network.entity.puffbug.RotateMessage;
import com.teamabnormals.endergetic.common.tileentities.BolloomBudTileEntity;
import com.teamabnormals.endergetic.common.tileentities.PuffBugHiveTileEntity;
import com.teamabnormals.endergetic.common.tileentities.BolloomBudTileEntity.BudSide;
import com.teamabnormals.endergetic.core.EndergeticExpansion;
import com.teamabnormals.endergetic.core.registry.EEBlocks;
import com.teamabnormals.endergetic.core.registry.EEEntities;
import com.teamabnormals.endergetic.core.registry.EEItems;
import com.teamabnormals.endergetic.core.registry.EESounds;
import com.teamabnormals.endergetic.core.registry.other.EEDataSerializers;

import com.teamabnormals.endergetic.core.registry.other.EEPlayableEndimations;
import com.teamabnormals.blueprint.core.endimator.Endimatable;
import com.teamabnormals.blueprint.core.endimator.PlayableEndimation;
import com.teamabnormals.blueprint.core.endimator.TimedEndimation;
import com.teamabnormals.blueprint.core.util.NetworkUtil;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.InteractionHand;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.HitResult.Type;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import net.minecraftforge.network.PacketDistributor;

public class PuffBugEntity extends Animal implements Endimatable {
	public static final Predicate<LivingEntity> CAN_ANGER = (entity) -> {
		if (entity instanceof Player) {
			return entity.isAlive() && !entity.isSpectator() && !((Player) entity).isCreative();
		}
		return !(entity instanceof PuffBugEntity) && entity.isAlive() && !entity.isSpectator() && !entity.isInvisible();
	};
	public static final float SEEKING_FACTOR = 1.1F;
	public static final EntityDimensions PROJECTILE_SIZE = EntityDimensions.fixed(0.5F, 0.5F);
	public static final EntityDimensions PROJECTILE_SIZE_CHILD = EntityDimensions.fixed(0.26325F, 0.26325F);
	private static final EntityDataAccessor<Optional<BlockPos>> HIVE_POS = SynchedEntityData.defineId(PuffBugEntity.class, EntityDataSerializers.OPTIONAL_BLOCK_POS);
	private static final EntityDataAccessor<Direction> ATTACHED_HIVE_SIDE = SynchedEntityData.defineId(PuffBugEntity.class, EntityDataSerializers.DIRECTION);
	private static final EntityDataAccessor<Optional<Vec3>> LAUNCH_DIRECTION = SynchedEntityData.defineId(PuffBugEntity.class, EEDataSerializers.OPTIONAL_VEC3D);
	private static final EntityDataAccessor<Optional<Vec3>> FIRE_DIRECTION = SynchedEntityData.defineId(PuffBugEntity.class, EEDataSerializers.OPTIONAL_VEC3D);
	private static final EntityDataAccessor<Boolean> FROM_BOTTLE = SynchedEntityData.defineId(PuffBugEntity.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> INFLATED = SynchedEntityData.defineId(PuffBugEntity.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> BOOSTING = SynchedEntityData.defineId(PuffBugEntity.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Integer> COLOR = SynchedEntityData.defineId(PuffBugEntity.class, EntityDataSerializers.INT);

	public final TimedEndimation HIVE_LANDING = new TimedEndimation(20, 0);
	public final TimedEndimation HIVE_SLEEP = new TimedEndimation(25, 0);

	private TeleportController teleportController;
	private RotationController rotationController;

	@Nullable
	private BlockPos budPos, pollinationPos;
	@Nullable
	private Direction teleportHiveSide, desiredHiveSide;

	@Nullable
	private ItemStack stackToCreate;
	@Nullable
	public BlockState stuckInBlockState;
	public boolean stuckInBlock;

	public float prevSpin, spin;
	public int teleportCooldown;
	public int ticksAwayFromHive;
	public int puffCooldown;

	public PuffBugEntity(EntityType<? extends PuffBugEntity> type, Level worldIn) {
		super(type, worldIn);
		this.moveControl = new PuffBugMoveController(this);
		this.teleportController = new TeleportController(this);
		this.rotationController = new RotationController(this);
		this.xpReward = 2;
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.getEntityData().define(HIVE_POS, Optional.empty());
		this.getEntityData().define(ATTACHED_HIVE_SIDE, Direction.UP);
		this.getEntityData().define(LAUNCH_DIRECTION, Optional.empty());
		this.getEntityData().define(FIRE_DIRECTION, Optional.empty());
		this.getEntityData().define(COLOR, -1);
		this.getEntityData().define(FROM_BOTTLE, false);
		this.getEntityData().define(INFLATED, true);
		this.getEntityData().define(BOOSTING, false);
	}

	@Override
	public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
		super.onSyncedDataUpdated(key);
		if (key.equals(FIRE_DIRECTION) || key.equals(LAUNCH_DIRECTION)) {
			this.refreshDimensions();
		}
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new PuffBugPullOutGoal(this));
		this.goalSelector.addGoal(0, new PuffBugRestOnHiveGoal(this));
		this.goalSelector.addGoal(1, new PuffBugAttachToHiveGoal(this));
		this.goalSelector.addGoal(1, new PuffBugRotateToFireGoal(this));
		this.goalSelector.addGoal(2, new PuffBugCreateItemGoal(this));
		this.goalSelector.addGoal(2, new PuffBugAttackGoal(this));
		this.goalSelector.addGoal(3, new PuffBugTeleportToRestGoal(this));
		this.goalSelector.addGoal(4, new PuffBugPollinateGoal(this));
		this.goalSelector.addGoal(5, new PuffBugDescentGoal(this));
		this.goalSelector.addGoal(6, new PuffBugTeleportToBudGoal(this));
		this.goalSelector.addGoal(7, new BreedGoal(this, 1.0F));
		this.goalSelector.addGoal(8, new FollowParentGoal(this, 1.5F));
		this.goalSelector.addGoal(9, new PuffBugBoostGoal(this));

		this.targetSelector.addGoal(2, new PuffBugTargetAggressorGoal(this));
	}

	public static AttributeSupplier.Builder registerAttributes() {
		return Mob.createMobAttributes()
				.add(Attributes.ATTACK_DAMAGE, 5.0F)
				.add(Attributes.FLYING_SPEED, 0.75F)
				.add(Attributes.ATTACK_KNOCKBACK, 0.15F)
				.add(Attributes.MAX_HEALTH, 8.0F)
				.add(Attributes.FOLLOW_RANGE, 16.0F);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void tick() {
		if (this.stuckInBlock) {
			this.setDeltaMovement(Vec3.ZERO);
		}

		if (this.getVehicle() instanceof BoofloEntity) {
			this.disableProjectile();
			this.removeLaunchDirection();
			this.setInflated(true);
		}

		super.tick();
		this.getRotationController().tick();
		this.keepEffectsAbsorbed();

		this.fallDistance = 0;

		Vec3 motion = this.getDeltaMovement();

		if (!this.level.isClientSide) {
			if (this.teleportCooldown > 0) {
				this.teleportCooldown--;
			}

			if (this.puffCooldown > 0) {
				this.puffCooldown--;
			}

			if (!this.isPassenger() && this.isInflated() && !this.getRotationController().rotating && this.isNoEndimationPlaying()) {
				if (this.isBoosting() && RayTraceHelper.rayTrace(this, 2.0D, 1.0F).getType() != Type.BLOCK || ((this.onGround || this.isInWater()) && this.puffCooldown <= 0 && this.getPollinationPos() == null && this.getLaunchDirection() == null && this.getFireDirection() == null)) {
					NetworkUtil.setPlayingAnimation(this, EEPlayableEndimations.PUFF_BUG_PUFF);
					this.playSound(this.getPuffSound(), 0.15F, this.getVoicePitch());
				}
			}

			if (this.isEndimationPlaying(EEPlayableEndimations.PUFF_BUG_TELEPORT_TO) && this.getAnimationTick() == 10 && !this.getTeleportController().hasNoDestination()) {
				this.getTeleportController().bringToDestination();
			} else if (this.isEndimationPlaying(EEPlayableEndimations.PUFF_BUG_TELEPORT_FROM)) {
				this.setDeltaMovement(Vec3.ZERO);
			}

			if (this.getHivePos() == null) {
				if (this.level.getGameTime() % 5 == 0 && this.getRandom().nextFloat() <= 0.1F) {
					PuffBugHiveTileEntity hive = this.findNewNearbyHive();
					if (hive != null) {
						this.addToHive(hive);
						if (this.isNoEndimationPlaying()) {
							NetworkUtil.setPlayingAnimation(this, EEPlayableEndimations.PUFF_BUG_CLAIM_HIVE);
						}
					}
				}
				this.ticksAwayFromHive = 0;
			} else {
				if (this.getHive() == null) {
					this.setHivePos(null);
				} else {
					if (this.getAttachedHiveSide() == Direction.UP) {
						this.ticksAwayFromHive++;

						if (this.ticksAwayFromHive < 1500) {
							if (this.getTarget() == null && this.getRandom().nextInt(7500) == 0 && !this.isInLove() && !this.getHive().isHiveFull()) {
								for (PuffBugEntity nearbyHiveMembers : this.level.getEntitiesOfClass(PuffBugEntity.class, this.getBoundingBox().inflate(9.0F), puffbug -> (puffbug.getHive() != null && puffbug.getHive() == this.getHive()) && this.distanceTo(puffbug) < 12.0F)) {

									this.setInLoveTime(1000);
									this.level.broadcastEntityEvent(this, (byte) 18);

									nearbyHiveMembers.setInLoveTime(1000);
									this.level.broadcastEntityEvent(nearbyHiveMembers, (byte) 18);

									if (this.isInLove() || nearbyHiveMembers.isInLove()) break;
								}
							}
						}
					} else {
						this.ticksAwayFromHive = 0;
					}
				}
			}

			if (this.getAttachedHiveSide() != Direction.UP) {
				this.setInflated(false);
			} else if (!this.isInflated() && this.getFireDirection() == null) {
				this.setInflated(true);
			}

			if (this.getTarget() != null && !CAN_ANGER.test(this.getTarget())) {
				this.setTarget(null);
			}

			if (this.isProjectile() && (this.updateFluidHeightAndDoFluidPushing(FluidTags.WATER, 1.0F) || this.updateFluidHeightAndDoFluidPushing(FluidTags.LAVA, 1.0F))) {
				this.disableProjectile();
			}
		} else {
			this.prevSpin = this.spin;
			RandomSource rand = this.getRandom();

			if (this.isEndimationPlaying(EEPlayableEndimations.PUFF_BUG_PUFF) && this.getAnimationTick() == 5) {
				for (int i = 0; i < 3; i++) {
					double offsetX = TemporaryMathUtil.makeNegativeRandomly(rand.nextFloat() * 0.1F, rand);
					double offsetZ = TemporaryMathUtil.makeNegativeRandomly(rand.nextFloat() * 0.1F, rand);

					double x = this.getX() + offsetX;
					double y = this.getY() + (rand.nextFloat() * 0.05F) + 0.7F;
					double z = this.getZ() + offsetZ;

					this.level.addParticle(EEParticles.SHORT_POISE_BUBBLE.get(), x, y, z, TemporaryMathUtil.makeNegativeRandomly((rand.nextFloat() * 0.1F), rand) + 0.05F, (rand.nextFloat() * 0.05F) + 0.025F, TemporaryMathUtil.makeNegativeRandomly((rand.nextFloat() * 0.1F), rand) + 0.05F);
				}
			} else if (this.isEndimationPlaying(EEPlayableEndimations.PUFF_BUG_TELEPORT_TO) && this.getAnimationTick() == 8) {
				for (int i = 0; i < 6; i++) {
					double offsetX = TemporaryMathUtil.makeNegativeRandomly(rand.nextFloat() * 0.1F, rand);
					double offsetZ = TemporaryMathUtil.makeNegativeRandomly(rand.nextFloat() * 0.1F, rand);

					double x = this.getX() + offsetX;
					double y = this.getY() + (rand.nextFloat() * 0.05F) + 0.7F;
					double z = this.getZ() + offsetZ;

					this.level.addParticle(EEParticles.SHORT_POISE_BUBBLE.get(), x, y, z, TemporaryMathUtil.makeNegativeRandomly((rand.nextFloat() * 0.15F), rand) + 0.025F, (rand.nextFloat() * 0.025F) + 0.025F, TemporaryMathUtil.makeNegativeRandomly((rand.nextFloat() * 0.15F), rand) + 0.025F);
				}
			} else if (this.isEndimationPlaying(EEPlayableEndimations.PUFF_BUG_TELEPORT_FROM) && this.getAnimationTick() == 5) {
				for (int i = 0; i < 6; i++) {
					double offsetX = TemporaryMathUtil.makeNegativeRandomly(rand.nextFloat() * 0.1F, rand);
					double offsetZ = TemporaryMathUtil.makeNegativeRandomly(rand.nextFloat() * 0.1F, rand);

					double x = this.getX() + offsetX;
					double y = this.getY() + (rand.nextFloat() * 0.05F) + 0.7F;
					double z = this.getZ() + offsetZ;

					this.level.addParticle(EEParticles.SHORT_POISE_BUBBLE.get(), x, y, z, TemporaryMathUtil.makeNegativeRandomly((rand.nextFloat() * 0.15F), rand) + 0.025F, (rand.nextFloat() * 0.025F) + 0.025F, TemporaryMathUtil.makeNegativeRandomly((rand.nextFloat() * 0.15F), rand) + 0.025F);
				}
			}

			this.HIVE_LANDING.tick();
			this.HIVE_SLEEP.tick();

			this.HIVE_LANDING.setDecrementing(this.getAttachedHiveSide() == Direction.UP);

			if (this.HIVE_LANDING.isMaxed()) {
				if (this.HIVE_SLEEP.isDecrementing() && this.HIVE_SLEEP.getTick() == 0) {
					this.HIVE_SLEEP.setDecrementing(false);
				} else if (this.HIVE_SLEEP.isMaxed()) {
					this.HIVE_SLEEP.setDecrementing(true);
				}
			} else {
				this.HIVE_SLEEP.setDecrementing(true);
			}

			if (this.isProjectile() && this.isEndimationPlaying(EEPlayableEndimations.PUFF_BUG_FLY)) {
				this.spin = this.spin + 55.0F;
			} else {
				this.spin = 0.0F;
			}
		}

		if (this.getDesiredHiveSide() != null) {
			if (this.getHive() != null && this.getDesiredHiveSide() != Direction.DOWN) {
				float degrees = (this.getDesiredHiveSide() == Direction.SOUTH ? 0 : this.getDesiredHiveSide().getAxisDirection().getStep()) * (this.getDesiredHiveSide().getAxis() == Axis.Z ? 180.0F : -90.0F);
				this.setYRot(this.yRotO = degrees);
				this.yHeadRot = this.yHeadRotO = degrees;
				this.yBodyRot = this.yBodyRotO = degrees;
			}
		}

		if (this.getAttachedHiveSide() != Direction.UP) {
			if (this.getHive() != null) {
				this.setDeltaMovement(this.getDeltaMovement().multiply(1.0F, 0.0F, 1.0F));
				this.getNavigation().stop();
				this.setSpeed(0.0F);

				this.push(0.0F, 0.0025F, 0.0F);

				this.setYRot(this.yRotO);
				this.yHeadRot = this.yHeadRotO;
				this.yBodyRot = this.yBodyRotO;

				if (!this.level.isClientSide && !this.isAtCorrectRestLocation(this.getAttachedHiveSide())) {
					this.setAttachedHiveSide(Direction.UP);
				}
			} else {
				if (!this.level.isClientSide) {
					this.setAttachedHiveSide(Direction.UP);
				}
			}
		} else {
			if ((this.getLaunchDirection() != null || this.getFireDirection() != null)) {
				this.setYRot(0.0F);
				this.yHeadRot = 0.0F;
				this.yBodyRot = 0.0F;

				Vec3 fireDirection = this.getFireDirection();

				if (fireDirection != null) {
					if ((this.level.isClientSide && !this.stuckInBlock) || !this.level.isClientSide) {
						this.getRotationController().rotate((float) Mth.wrapDegrees(fireDirection.y() - this.getYRot()), (float) fireDirection.x() + 90.0F, 0.0F, 5);
					}

					LivingEntity target = this.getTarget();

					if (!this.stuckInBlock) {
						if (!this.level.isClientSide && target != null && this.isEndimationPlaying(EEPlayableEndimations.PUFF_BUG_FLY)) {
							float seekOffset = target.getY() > this.getY() ? 0.0F : 0.5F;
							Vec3 targetVecNoScale = new Vec3(target.getX() - this.getX(), target.getY() - seekOffset - this.getY(), target.getZ() - this.getZ());
							Vec3 targetVec = targetVecNoScale.scale(SEEKING_FACTOR);

							double motionLength = motion.length();
							double targetVecLength = targetVec.length();

							float totalVecLength = Mth.sqrt((float) (motionLength * motionLength + targetVecLength * targetVecLength));

							Vec3 newMotion = motion.scale(motionLength / totalVecLength).add(targetVec.scale(targetVecLength / totalVecLength));

							float gravityCompensator = totalVecLength <= 4.0F ? 0.05F : 0.1F;

							this.setDeltaMovement(newMotion.scale(0.4F).add(0.0F, gravityCompensator, 0.0F));
						}

						Vec3 newestMotion = this.getDeltaMovement();

						float pitch = -((float) (Mth.atan2(newestMotion.y(), Mth.sqrt((float) (newestMotion.x() * newestMotion.x() + newestMotion.z() * newestMotion.z()))) * (double) (180F / (float) Math.PI)));
						float yaw = (float) (Mth.atan2(newestMotion.z(), newestMotion.x()) * (double) (180F / (float) Math.PI)) - 90F;

						this.setFireDirection(pitch, yaw);
					}
				}
			}
		}

		if (this.isProjectile() && !this.isInflated()) {
			BlockPos blockpos = this.blockPosition();
			BlockState blockstate = this.level.getBlockState(blockpos);
			if (!blockstate.isAir() && !this.noPhysics) {
				VoxelShape voxelshape = blockstate.getCollisionShape(this.level, blockpos);
				if (!voxelshape.isEmpty()) {
					for (AABB axisalignedbb : voxelshape.toAabbs()) {
						if (axisalignedbb.move(blockpos).contains(this.position())) {
							this.stuckInBlock = true;
							break;
						}
					}
				}
			}

			if (this.stuckInBlock && !this.noPhysics) {
				if (!this.level.isClientSide && this.stuckInBlockState != blockstate && this.level.noCollision(this.getBoundingBox().inflate(0.06D))) {
					this.disableProjectile();
				}
				this.setDeltaMovement(this.getDeltaMovement().multiply(0.0F, 1.0F, 0.0F));
			} else {
				Vec3 positionVec = new Vec3(this.getX(), this.getY(), this.getZ());
				Vec3 endVec = positionVec.add(motion);

				HitResult traceResult = this.level.clip(new ClipContext(positionVec, endVec, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
				EntityHitResult entityTraceResult = this.traceEntity(positionVec, endVec);

				if (entityTraceResult != null) {
					traceResult = entityTraceResult;
				}

				if (traceResult.getType() != Type.MISS) {
					this.onSting(traceResult);
					this.hasImpulse = true;
				}
			}
		}
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);

		this.setAttachedHiveSide(Direction.from3DDataValue(compound.getByte("AttachedHiveSide")));
		this.setFromBottle(compound.getBoolean("FromBottle"));

		this.teleportCooldown = compound.getInt("TeleportCooldown");
		this.ticksAwayFromHive = compound.getInt("TicksAwayFromHive");

		if (compound.contains("IsInflated")) {
			this.setInflated(compound.getBoolean("IsInflated"));
		}

		if (compound.contains("HivePos", 10)) {
			this.setHivePos(NbtUtils.readBlockPos(compound.getCompound("HivePos")));
		}

		if (compound.contains("StuckInBlockState", 10)) {
			this.stuckInBlockState = NbtUtils.readBlockState(compound.getCompound("StuckInBlockState"));
		}

		CompoundTag stackToCreate = compound.getCompound("ItemStackToCreate");

		if (stackToCreate != null) {
			ItemStack newStackToCreate = ItemStack.of(stackToCreate);
			if (!newStackToCreate.isEmpty()) {
				this.setStackToCreate(newStackToCreate);
			}
		}

		this.rotationController = this.getRotationController().read(this, compound.getCompound("Orientation"));
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);

		compound.putByte("AttachedHiveSide", (byte) this.getAttachedHiveSide().get3DDataValue());
		compound.putBoolean("FromBottle", this.isFromBottle());
		compound.putBoolean("IsInflated", this.isInflated());

		compound.putInt("TeleportCooldown", this.teleportCooldown);
		compound.putInt("TicksAwayFromHive", this.ticksAwayFromHive);

		if (this.getHivePos() != null) {
			compound.put("HivePos", NbtUtils.writeBlockPos(this.getHivePos()));
		}

		if (this.hasStackToCreate()) {
			compound.put("ItemStackToCreate", this.getStackToCreate().save(new CompoundTag()));
		}

		if (this.stuckInBlockState != null) {
			compound.put("StuckInBlockState", NbtUtils.writeBlockState(this.stuckInBlockState));
		}

		compound.put("Orientation", this.getRotationController().write(new CompoundTag()));
	}

	@Nullable
	public BlockPos getHivePos() {
		return this.getEntityData().get(HIVE_POS).orElse(null);
	}

	public void setHivePos(@Nullable BlockPos pos) {
		this.getEntityData().set(HIVE_POS, Optional.ofNullable(pos));
	}

	@Nullable
	public PuffBugHiveTileEntity getHive() {
		BlockPos hivePos = this.getHivePos();
		if (hivePos != null) {
			try {
				BlockEntity tileEntity = this.level.getBlockEntity(hivePos);
				if (tileEntity instanceof PuffBugHiveTileEntity) {
					return (PuffBugHiveTileEntity) tileEntity;
				}
			} catch (NullPointerException e) {
				return null;
			}
		}
		return null;
	}

	@Nullable
	public BlockPos getBudPos() {
		return this.budPos;
	}

	public void setBudPos(@Nullable BlockPos pos) {
		this.budPos = pos;
	}

	@Nullable
	public BlockPos getPollinationPos() {
		return this.pollinationPos;
	}

	public void setPollinationPos(@Nullable BlockPos pos) {
		this.pollinationPos = pos;
	}

	public boolean canAttachToSide(Direction direction) {
		if (direction == Direction.UP) {
			return false;
		}
		return this.getHive() != null && PuffBugHiveTileEntity.HiveOccupantData.isHiveSideEmpty(this.getHive(), direction);
	}

	/*
	 * Up is considered null
	 */
	public Direction getAttachedHiveSide() {
		return this.entityData.get(ATTACHED_HIVE_SIDE);
	}

	public void setAttachedHiveSide(@Nullable Direction side) {
		this.entityData.set(ATTACHED_HIVE_SIDE, side == null ? Direction.UP : side);
	}

	@Nullable
	public Direction getTeleportHiveSide() {
		return this.teleportHiveSide;
	}

	public void setTeleportHiveSide(@Nullable Direction side) {
		this.teleportHiveSide = side;
	}

	@Nullable
	public Direction getDesiredHiveSide() {
		return this.desiredHiveSide;
	}

	public void setDesiredHiveSide(@Nullable Direction side) {
		this.desiredHiveSide = side;
	}

	public boolean isFromBottle() {
		return this.entityData.get(FROM_BOTTLE);
	}

	public void setFromBottle(boolean value) {
		this.entityData.set(FROM_BOTTLE, value);
	}

	public boolean isInflated() {
		return this.entityData.get(INFLATED);
	}

	public void setInflated(boolean inflated) {
		this.entityData.set(INFLATED, inflated);
	}

	public boolean isBoosting() {
		return this.entityData.get(BOOSTING);
	}

	public void setBoosting(boolean boosting) {
		this.entityData.set(BOOSTING, boosting);
	}

	public int getColor() {
		return this.entityData.get(COLOR);
	}

	public void setColor(int color) {
		this.entityData.set(COLOR, color);
	}

	public void setStackToCreate(@Nullable ItemStack stack) {
		this.stackToCreate = stack;
	}

	@Nullable
	public ItemStack getStackToCreate() {
		return this.stackToCreate;
	}

	public boolean hasStackToCreate() {
		return this.getStackToCreate() != null;
	}

	public void setLaunchDirection(float pitch, float yaw) {
		this.entityData.set(LAUNCH_DIRECTION, Optional.of(new Vec3(pitch, yaw, 0.0F)));
	}

	public void setFireDirection(float pitch, float yaw) {
		this.entityData.set(FIRE_DIRECTION, Optional.of(new Vec3(pitch, yaw, 0.0F)));
	}

	public void removeLaunchDirection() {
		this.entityData.set(LAUNCH_DIRECTION, Optional.empty());
	}

	public void removeFireDirection() {
		this.entityData.set(FIRE_DIRECTION, Optional.empty());
	}

	@Nullable
	public Vec3 getLaunchDirection() {
		return this.entityData.get(LAUNCH_DIRECTION).orElse(null);
	}

	@Nullable
	public Vec3 getFireDirection() {
		return this.entityData.get(FIRE_DIRECTION).orElse(null);
	}

	public boolean hasLevitation() {
		return this.hasEffect(MobEffects.LEVITATION);
	}

	@Override
	public boolean isNoGravity() {
		return super.isNoGravity() || this.getAttachedHiveSide() != Direction.UP;
	}

	public TeleportController getTeleportController() {
		return this.teleportController;
	}

	public RotationController getRotationController() {
		return this.rotationController;
	}


	@Override
	public void onEndimationStart(PlayableEndimation endimation, PlayableEndimation oldEndimation) {
		if (endimation == EEPlayableEndimations.PUFF_BUG_PUFF) {
			float pitch = this.isVehicle() ? 1.0F : this.getXRot();
			float xMotion = -Mth.sin(this.getYRot() * ((float) Math.PI / 180F)) * Mth.cos(pitch * ((float) Math.PI / 180F));
			float zMotion = Mth.cos(this.getYRot() * ((float) Math.PI / 180F)) * Mth.cos(pitch * ((float) Math.PI / 180F));

			Vec3 motion = new Vec3(xMotion, 0.65F, zMotion).normalize();

			if (this.getTarget() != null && CAN_ANGER.test(this.getTarget())) {
				motion.scale(2.0F);
			}

			this.push(motion.x() * (this.getAttribute(Attributes.MOVEMENT_SPEED).getValue() - 0.1F), motion.y(), motion.z() * (this.getAttribute(Attributes.MOVEMENT_SPEED).getValue() - 0.1F));
		} else if (endimation == EEPlayableEndimations.PUFF_BUG_TELEPORT_TO) {
			if (!this.level.isClientSide) {
				this.playSound(this.getTeleportSound(false), 0.65F, this.getVoicePitch());
			}
		} else if (endimation == EEPlayableEndimations.PUFF_BUG_FLY) {
			this.level.playSound(null, this, EESounds.PUFFBUG_LAUNCH.get(), SoundSource.HOSTILE, 0.25F, this.getRandom().nextFloat() * 0.35F + 0.75F);
		}
	}

	@Override
	public void onEndimationEnd(PlayableEndimation endimation, PlayableEndimation newEndimation) {
		if (newEndimation != EEPlayableEndimations.PUFF_BUG_TELEPORT_FROM && endimation == EEPlayableEndimations.PUFF_BUG_TELEPORT_TO) {
			if (!this.level.isClientSide) {
				NetworkUtil.setPlayingAnimation(this, EEPlayableEndimations.PUFF_BUG_TELEPORT_FROM);
				this.playSound(this.getTeleportSound(true), 0.65F, this.getVoicePitch());
			}
		} else if (endimation == EEPlayableEndimations.PUFF_BUG_POLLINATE) {
			this.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 3000));
			if (this.getPollinationPos() != null) {
				BlockEntity te = this.level.getBlockEntity(this.getPollinationPos());
				if (te instanceof BolloomBudTileEntity) {
					BolloomBudTileEntity bud = (BolloomBudTileEntity) te;
					if (bud.canBeOpened()) {
						for (BudSide side : BudSide.values()) {
							BlockPos sidePos = side.offsetPosition(this.getPollinationPos());
							if (this.level.getBlockState(sidePos).getCollisionShape(level, this.getPollinationPos()).isEmpty()) {
								this.level.destroyBlock(sidePos, true);
							}
						}

						this.level.setBlockAndUpdate(this.getPollinationPos(), level.getBlockState(this.getPollinationPos()).setValue(BolloomBudBlock.OPENED, true));
						bud.startGrowing(this.getRandom(), bud.calculateFruitMaxHeight(), false);
					}
				}
			}
		}
	}

	@Override
	public void travel(Vec3 moveDirection) {
		if (this.isEffectiveAi() && this.isInflated()) {
			double gravity = this.hasLevitation() ? -0.005D : 0.005D;
			float speed = this.onGround ? 0.01F : 0.025F;

			this.moveRelative(speed, moveDirection);
			this.move(MoverType.SELF, this.getDeltaMovement());
			this.setDeltaMovement(this.getDeltaMovement().scale(0.75D));
			this.setDeltaMovement(this.getDeltaMovement().subtract(0, gravity, 0));
		} else {
			if (this.stuckInBlock) {
				this.setDeltaMovement(0.0F, this.getDeltaMovement().y(), 0.0F);
			}
			boolean noVerticalMotion = this.isInflated() && (this.getAttachedHiveSide() != Direction.UP || this.getDesiredHiveSide() != null);

			if (noVerticalMotion) {
				this.setDeltaMovement(this.getDeltaMovement().multiply(1.0F, 0.0F, 1.0F));
			}

			super.travel(Vec3.ZERO);

			if (noVerticalMotion) {
				this.setDeltaMovement(this.getDeltaMovement().multiply(1.0F, 0.0F, 1.0F));
			}

			if (this.isProjectile()) {
				this.setDeltaMovement(this.getDeltaMovement().subtract(0.0F, 0.005F, 0.0F));
			}

			if (this.stuckInBlock) {
				this.setDeltaMovement(0.0F, this.getDeltaMovement().y(), 0.0F);
			}
		}
	}

	public void addToHive(PuffBugHiveTileEntity hive) {
		hive.addBugToHive(this);
		this.setHivePos(hive.getBlockPos());
	}

	@Nullable
	public PuffBugHiveTileEntity findNewNearbyHive() {
		BlockPos pos = this.blockPosition();
		double xyDistance = 16.0D;
		for (BlockPos blockpos : BlockPos.betweenClosed(pos.offset(-xyDistance, -6.0D, -xyDistance), pos.offset(xyDistance, 6.0D, xyDistance))) {
			if (blockpos.closerToCenterThan(this.position(), xyDistance)) {
				if (this.level.getBlockState(blockpos).getBlock() == EEBlocks.PUFFBUG_HIVE.get() && this.level.getBlockEntity(blockpos) instanceof PuffBugHiveTileEntity) {
					PuffBugHiveTileEntity hive = (PuffBugHiveTileEntity) this.level.getBlockEntity(blockpos);
					if (!hive.isHiveFull() && this.getHive() == null) {
						return hive;
					}
				}
			}
		}
		return null;
	}

	protected void setBottleData(ItemStack bottle) {
		if (this.hasCustomName()) {
			bottle.setHoverName(this.getCustomName());
		}

		CompoundTag nbt = bottle.getOrCreateTag();

		if (this.getColor() != -1) {
			nbt.putInt("ColorTag", this.getColor());
		}

		nbt.putInt("TeleportCooldown", this.teleportCooldown);

		if (!this.getActiveEffects().isEmpty()) {
			ListTag listnbt = new ListTag();

			for (MobEffectInstance effectinstance : this.getActiveEffects()) {
				listnbt.add(effectinstance.save(new CompoundTag()));
			}

			nbt.put("CustomPotionEffects", listnbt);
		}

		nbt.putBoolean("IsFromBottle", true);
		nbt.putBoolean("IsChild", this.isBaby());
	}

	private void keepEffectsAbsorbed() {
		Map<MobEffect, MobEffectInstance> activePotionMap = this.getActiveEffectsMap();
		Iterator<MobEffect> iterator = activePotionMap.keySet().iterator();
		while (iterator.hasNext()) {
			MobEffect effect = iterator.next();
			if (effect != MobEffects.LEVITATION) {
				MobEffectInstance effectInstance = activePotionMap.get(effect);
				activePotionMap.put(effect, new MobEffectInstance(effect, effectInstance.getDuration() + 1, effectInstance.getAmplifier(), effectInstance.isAmbient(), effectInstance.isVisible()));
			}
		}
	}

	/**
	 * Looks for an open position near the hive, used for alerted Puff Bugs
	 */
	public void tryToTeleportToHive(BlockPos pos) {
		BlockPos.MutableBlockPos positions = new BlockPos.MutableBlockPos();
		List<BlockPos> avaliablePositions = Lists.newArrayList();
		PuffBugHiveTileEntity hive = this.getHive();

		if (hive == null || (hive != null && !hive.canTeleportTo()) || (this.getAttachedHiveSide() == Direction.UP && Math.sqrt(this.distanceToSqr(Vec3.atCenterOf((pos)))) < 5.0F) || this.isEndimationPlaying(EEPlayableEndimations.PUFF_BUG_TELEPORT_FROM)) {
			return;
		}

		for (int x = 0; x < 6; x++) {
			for (int y = 0; y < 6; y++) {
				for (int z = 0; z < 6; z++) {
					positions.set(pos.offset(x, y, z));

					if (this.level.isEmptyBlock(positions)) {
						avaliablePositions.add(new BlockPos(positions));
					}
				}
			}
		}

		if (!avaliablePositions.isEmpty() && !this.isProjectile()) {
			this.getTeleportController().destination = GenerationUtils.getClosestPositionToPos(avaliablePositions, this.blockPosition());
			this.getNavigation().stop();
			if (!this.isEndimationPlaying(EEPlayableEndimations.PUFF_BUG_TELEPORT_TO)) {
				this.getTeleportController().processTeleportation();
			}
		}
	}

	private void onSting(HitResult result) {
		HitResult.Type resultType = result.getType();
		if (resultType == HitResult.Type.ENTITY) {
			EntityHitResult entityResult = (EntityHitResult) result;
			Entity entity = entityResult.getEntity();
			if (entity.hurt(DamageSource.mobAttack(this).setProjectile(), (float) this.getAttribute(Attributes.ATTACK_DAMAGE).getValue())) {
				this.setInflated(true);
				this.removeFireDirection();
				this.stuckInBlock = false;

				if (!this.getActiveEffects().isEmpty() && entity instanceof LivingEntity) {
					for (MobEffectInstance effects : this.getActiveEffects()) {
						((LivingEntity) entity).addEffect(effects);
					}
					this.removeAllEffects();
				}
			}
		} else {
			BlockHitResult blockraytraceresult = (BlockHitResult) result;
			this.stuckInBlockState = this.level.getBlockState(blockraytraceresult.getBlockPos());
			this.stuckInBlock = true;

			Vec3 end = result.getLocation();
			this.setPos(end.x(), end.y(), end.z());
			this.setDeltaMovement(Vec3.ZERO);

			if (!this.level.isClientSide) {
				NetworkUtil.setPlayingAnimation(this, EEPlayableEndimations.PUFF_BUG_LAND);
			}

			this.level.playSound(null, this, EESounds.PUFFBUG_LAND.get(), SoundSource.HOSTILE, 0.5F, this.getVoicePitch());
		}
	}

	@Nullable
	private EntityHitResult traceEntity(Vec3 start, Vec3 end) {
		return ProjectileUtil.getEntityHitResult(this.level, this, start, end, this.getBoundingBox().expandTowards(this.getDeltaMovement()).inflate(0.5F), (result) -> {
			return !result.isSpectator() && result.isAlive() && !(result instanceof PuffBugEntity);
		});
	}

	public void disableProjectile() {
		this.stuckInBlock = false;
		this.setInflated(true);
		this.removeFireDirection();
		this.level.broadcastEntityEvent(this, (byte) 38);
	}

	public boolean wantsToRest() {
		return this.ticksAwayFromHive >= 3000;
	}

	public boolean isProjectile() {
		return !(this.getFireDirection() == null && this.getLaunchDirection() == null);
	}

	public boolean isAtCorrectRestLocation(Direction side) {
		BlockEntity te = side == Direction.DOWN ? this.isBaby() ? this.level.getBlockEntity(this.blockPosition().above(1)) : this.level.getBlockEntity(this.blockPosition().above(2)) : this.isBaby() ? this.level.getBlockEntity(this.blockPosition().relative(side.getOpposite())) : this.level.getBlockEntity(this.blockPosition().above(1).relative(side.getOpposite()));
		if (te != this.getHive()) {
			return false;
		}

		BlockPos hivePos = this.getHivePos();
		switch (side) {
			case UP:
				return false;
			case DOWN:
				float yOffsetDown = this.isBaby() ? 0.45F : -0.15F;
				return Vec3.atCenterOf((hivePos.below()).offset(0.5F, yOffsetDown, 0.5F)).distanceTo(this.position()) < 0.25F;
			default:
				float yOffset = this.isBaby() ? 0.2F : -0.2F;
				BlockPos sideOffset = hivePos.relative(side);
				return this.level.isEmptyBlock(sideOffset.above()) && this.level.isEmptyBlock(sideOffset.below())
						&& Vec3.atLowerCornerOf(sideOffset).add(this.getTeleportController().getOffsetForDirection(side)[0], yOffset, this.getTeleportController().getOffsetForDirection(side)[1]).distanceTo(this.position()) < (this.isBaby() ? 0.1F : 0.25F);
		}
	}

	@Override
	protected PathNavigation createNavigation(Level worldIn) {
		return new EndergeticFlyingPathNavigator(this, worldIn);
	}

	@Override
	public boolean canBreed() {
		return super.canBreed() && this.isInflated();
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void handleEntityEvent(byte id) {
		if (id == 38) {
			this.stuckInBlock = false;
		} else {
			super.handleEntityEvent(id);
		}
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public boolean shouldRenderAtSqrDistance(double distance) {
		double boundingBoxLength = this.getBoundingBox().getSize() * 2.5D;
		if (Double.isNaN(boundingBoxLength)) {
			boundingBoxLength = 1.0D;
		}

		boundingBoxLength = boundingBoxLength * 64.0D * getViewScale();
		return distance < boundingBoxLength * boundingBoxLength;
	}

	@Override
	public Vec3 getLightProbePosition(float partialTicks) {
		BlockPos blockpos = new BlockPos(this.getX(), this.getY() + (double) this.getEyeHeight(), this.getZ());
		if (this.stuckInBlock && !this.isInflated()) {
			boolean rotationFlag = true;
			float[] rotations = this.getRotationController().getRotations(1.0F);
			Direction horizontalOffset = Direction.fromYRot(rotations[0]).getOpposite();
			Direction verticalOffset = (rotations[1] <= 180.0F && rotations[1] > 100.0F) ? Direction.UP : Direction.DOWN;

			if (rotations[1] >= 80.0F && rotations[1] <= 100.0F) {
				rotationFlag = false;
			}

			return this.level.isAreaLoaded(blockpos, 0) ? Vec3.atCenterOf((rotationFlag ? blockpos.relative(horizontalOffset).relative(verticalOffset) : blockpos.relative(horizontalOffset))) : super.getLightProbePosition(partialTicks);
		}
		return super.getLightProbePosition(partialTicks);
	}

	@Override
	public boolean causeFallDamage(float distance, float damageMultiplier, DamageSource source) {
		return false;
	}

	@Override
	protected float getStandingEyeHeight(Pose poseIn, EntityDimensions size) {
		return this.isProjectile() ? 0.0F : size.height * 0.5F;
	}

	@Override
	protected void updateInvisibilityStatus() {
		super.updateInvisibilityStatus();
		Collection<MobEffectInstance> effects = this.getActiveEffects();

		if (!effects.isEmpty()) {
			this.setColor(PotionUtils.getColor(effects));
		} else {
			this.setColor(-1);
		}
	}

	@Override
	public ItemStack getPickedResult(HitResult target) {
		return new ItemStack(EEItems.PUFF_BUG_SPAWN_EGG.get());
	}

	@Override
	public MobType getMobType() {
		return MobType.ARTHROPOD;
	}

	@Override
	public boolean isFood(ItemStack stack) {
		return stack.getItem() == EEBlocks.TALL_POISE_BUSH.get().asItem();
	}

	@Override
	public AABB getBoundingBoxForCulling() {
		return super.getBoundingBoxForCulling().inflate(12);
	}

	@Override
	public InteractionResult mobInteract(Player player, InteractionHand hand) {
		ItemStack itemstack = player.getItemInHand(hand);
		Item item = itemstack.getItem();

		if (!this.isAlive() || this.isAggressive()) return InteractionResult.FAIL;

		if (item == Items.GLASS_BOTTLE) {
			this.playSound(SoundEvents.BOTTLE_FILL_DRAGONBREATH, 1.0F, 1.0F);
			itemstack.shrink(1);
			ItemStack bottle = new ItemStack(EEItems.PUFFBUG_BOTTLE.get());
			this.setBottleData(bottle);

			if (itemstack.isEmpty()) {
				player.setItemInHand(hand, bottle);
			} else if (!player.getInventory().add(bottle)) {
				player.drop(bottle, false);
			}

			this.discard();
			return InteractionResult.SUCCESS;
		} else if (!this.hasStackToCreate() && this.hasLevitation()) {
			ItemStack newStackToCreate = item == EEItems.BOLLOOM_FRUIT.get() ? new ItemStack(EEBlocks.BOLLOOM_BUD.get()) : this.isFood(itemstack) ? new ItemStack(EEBlocks.PUFFBUG_HIVE.get()) : null;
			if (newStackToCreate != null) {
				this.setStackToCreate(newStackToCreate);
				this.usePlayerItem(player, hand, itemstack);
				return InteractionResult.sidedSuccess(this.level.isClientSide);
			}
			return InteractionResult.PASS;
		} else {
			return super.mobInteract(player, hand);
		}
	}

	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
		RandomSource rng = this.getRandom();

		if (dataTag != null) {
			int age = dataTag.getBoolean("IsChild") ? -24000 : 0;

			this.setAge(age);
			this.teleportCooldown = dataTag.getInt("TeleportCooldown");
			this.setFromBottle(dataTag.getBoolean("IsFromBottle"));

			if (dataTag.contains("ColorTag", 3)) {
				this.setColor(dataTag.getInt("ColorTag"));
			}

			if (dataTag.contains("CustomPotionEffects")) {
				for (MobEffectInstance effectinstance : PotionUtils.getCustomEffects(dataTag)) {
					this.addEffect(effectinstance);
				}
			}
		}

		if (reason == MobSpawnType.STRUCTURE) {
			this.ticksAwayFromHive = rng.nextInt(1500) + 1500;

			if (rng.nextFloat() < 0.1F) {
				this.age = -24000;
			}
		} else if (reason == MobSpawnType.NATURAL || reason == MobSpawnType.SPAWNER) {
			if (rng.nextFloat() < 0.05F) {
				int swarmSize = rng.nextInt(11) + 10;
				Vec3 centeredPos = Vec3.atCenterOf(this.blockPosition());
				for (int i = 0; i < swarmSize; i++) {
					Vec3 spawnPos = centeredPos.add(TemporaryMathUtil.makeNegativeRandomly(rng.nextFloat() * 5.5F, rng), TemporaryMathUtil.makeNegativeRandomly(rng.nextFloat() * 2.0F, rng), TemporaryMathUtil.makeNegativeRandomly(rng.nextFloat() * 5.5F, rng));

					if (this.level.isEmptyBlock(new BlockPos(spawnPos))) {
						PuffBugEntity swarmChild = EEEntities.PUFF_BUG.get().create(this.level);
						swarmChild.moveTo(spawnPos.x(), spawnPos.y(), spawnPos.z(), 0.0F, 0.0F);
						swarmChild.finalizeSpawn(worldIn, this.level.getCurrentDifficultyAt(new BlockPos(spawnPos)), MobSpawnType.EVENT, null, null);
						swarmChild.setAge(-24000);

						this.level.addFreshEntity(swarmChild);
					}
				}
			}
		}
		return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
	}

	@Override
	public boolean removeWhenFarAway(double distanceToClosestPlayer) {
		return this.getHive() == null && !this.isFromBottle() && !this.hasCustomName();
	}

	@Override
	public boolean requiresCustomPersistence() {
		return super.requiresCustomPersistence() || this.isFromBottle();
	}

	@Override
	protected void doPush(Entity entity) {
		if (!this.isInflated() && !(entity instanceof PuffBugEntity)) {
			if (this.isProjectile()) {
				if (entity.hurt(DamageSource.mobAttack(this).setProjectile(), 5.0F)) {
					this.setInflated(true);
					this.removeFireDirection();
					this.stuckInBlock = false;

					if (!this.getActiveEffects().isEmpty() && entity instanceof LivingEntity) {
						for (MobEffectInstance effects : this.getActiveEffects()) {
							((LivingEntity) entity).addEffect(effects);
						}
						this.removeAllEffects();
					}
				}
			}
		}
		super.doPush(entity);
	}

	@Override
	public EntityDimensions getDimensions(Pose pose) {
		return this.isProjectile() ? this.isBaby() ? PROJECTILE_SIZE_CHILD : PROJECTILE_SIZE : super.getDimensions(pose);
	}

	@Override
	public boolean isInvulnerableTo(DamageSource source) {
		return ((this.isProjectile() || this.isPassenger()) && (source == DamageSource.IN_WALL || source == DamageSource.FLY_INTO_WALL || source == DamageSource.CRAMMING)) || super.isInvulnerableTo(source);
	}

	@Override
	public boolean onClimbable() {
		return false;
	}

	@Override
	protected MovementEmission getMovementEmission() {
		return MovementEmission.SOUNDS;
	}

	@Nullable
	@Override
	public AgeableMob getBreedOffspring(ServerLevel p_146743_, AgeableMob p_146744_) {
		return EEEntities.PUFF_BUG.get().create(this.level);
	}

	public SoundEvent getPuffSound() {
		return EESounds.PUFFBUG_PUFF.get();
	}

	public SoundEvent getTeleportSound(boolean from) {
		return from ? EESounds.PUFFBUG_TELEPORT_FROM.get() : EESounds.PUFFBUG_TELEPORT_TO.get();
	}

	public SoundEvent getSleepSound() {
		return EESounds.PUFFBUG_SLEEP.get();
	}

	public SoundEvent getItemCreationSound() {
		return EESounds.PUFFBUG_CREATE_ITEM.get();
	}

	public SoundEvent getLaunchSound() {
		return EESounds.PUFFBUG_LAUNCH.get();
	}

	public SoundEvent getLandSound() {
		return EESounds.PUFFBUG_LAND.get();
	}

	@Override
	protected SoundEvent getDeathSound() {
		return EESounds.PUFFBUG_DEATH.get();
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSource) {
		return EESounds.PUFFBUG_HURT.get();
	}

	static class PuffBugMoveController extends MoveControl {
		private final PuffBugEntity puffbug;

		PuffBugMoveController(PuffBugEntity puffbug) {
			super(puffbug);
			this.puffbug = puffbug;
		}

		public void tick() {
			if (!this.puffbug.isInflated()) {
				this.puffbug.setSpeed(0.0F);
				return;
			}

			if (this.operation == MoveControl.Operation.MOVE_TO && !this.puffbug.getNavigation().isDone()) {
				double xDistance = this.wantedX - this.puffbug.getX();
				double yDistance = this.wantedY - this.puffbug.getY();
				double zDistance = this.wantedZ - this.puffbug.getZ();
				double totalDistance = Mth.sqrt((float) (xDistance * xDistance + yDistance * yDistance + zDistance * zDistance));

				double verticalVelocity = yDistance / totalDistance;

				float angle = (float) (Mth.atan2(zDistance, xDistance) * (180F / Math.PI)) - 90.0F;

				this.puffbug.setYRot(this.rotlerp(this.puffbug.getYRot(), angle, 20.0F));
				this.puffbug.yBodyRot = this.puffbug.getYRot();

				float speed = (float) (this.speedModifier * this.puffbug.getAttribute(Attributes.FLYING_SPEED).getValue());

				if (verticalVelocity < 0.0F) {
					this.puffbug.setSpeed(Mth.lerp(0.125F, this.puffbug.getSpeed(), speed));
					this.puffbug.setDeltaMovement(this.puffbug.getDeltaMovement().add(0.0D, (double) this.puffbug.getSpeed() * verticalVelocity * 0.05D, 0.0D));
					this.puffbug.setBoosting(false);
				} else {
					this.puffbug.setBoosting(true);
				}
			} else {
				this.puffbug.setSpeed(0.0F);
				this.puffbug.setBoosting(false);
			}
		}
	}

	public static class TeleportController {
		private PuffBugEntity puffbug;
		private Level world;
		@Nullable
		private BlockPos destination;

		TeleportController(PuffBugEntity puffbug) {
			this.puffbug = puffbug;
			this.world = puffbug.level;
		}

		public void processTeleportation() {
			if (!this.hasNoDestination() && !this.world.isClientSide) {
				this.puffbug.getNavigation().stop();
				NetworkUtil.setPlayingAnimation(this.puffbug, EEPlayableEndimations.PUFF_BUG_TELEPORT_TO);

				this.puffbug.teleportCooldown = this.puffbug.getRandom().nextInt(300) + 1200;
			}
		}

		protected void bringToDestination() {
			if (!this.world.isClientSide) {
				Direction side = this.puffbug.getTeleportHiveSide();

				float xOffset = side == null || side == Direction.DOWN ? 0.5F : this.getOffsetForDirection(side)[0];
				float yOffset = side == Direction.DOWN ? this.puffbug.isBaby() ? 0.45F : -0.15F : this.puffbug.isBaby() ? 0.2F : -0.2F;
				float zOffset = side == null || side == Direction.DOWN ? 0.5F : this.getOffsetForDirection(side)[1];

				if (side == null) {
					NetworkUtil.teleportEntity(this.puffbug, this.destination.getX() + 0.5F, this.destination.getY() + 0.5F, this.destination.getZ() + 0.5F);
				} else {
					NetworkUtil.teleportEntity(this.puffbug, this.destination.getX() + xOffset, this.destination.getY() + yOffset, this.destination.getZ() + zOffset);
				}

				this.destination = null;

				this.puffbug.getNavigation().stop();
				this.puffbug.setDeltaMovement(Vec3.ZERO);

				if (side != null) {
					this.puffbug.setDesiredHiveSide(side);
					this.puffbug.setTeleportHiveSide(null);
				}
			}
		}

		public boolean tryToCreateDesinationTo(BlockPos pos, @Nullable Direction direction) {
			boolean directionFlag = direction == null || this.world.isEmptyBlock(pos.relative(direction));

			if (direction != null && direction != Direction.DOWN) {
				if (!this.world.isEmptyBlock(pos.above()) || !this.world.isEmptyBlock(pos.below())) {
					directionFlag = false;
				}
			}

			if (this.puffbug.tickCount > 5 && this.world.isEmptyBlock(pos) && directionFlag && this.hasNoDestination()) {
				if (this.world.getEntitiesOfClass(Entity.class, new AABB(pos)).isEmpty()) {
					this.destination = pos;
					return true;
				}
			}
			return false;
		}

		public boolean hasNoDestination() {
			return this.destination == null;
		}

		public boolean canTeleport() {
			return this.destination == null && this.puffbug.teleportCooldown <= 0;
		}

		protected float[] getOffsetForDirection(Direction side) {
			switch (side) {
				default:
				case NORTH:
					return this.puffbug.isBaby() ? new float[]{0.5F, 0.85F} : new float[]{0.5F, 0.75F};
				case SOUTH:
					return this.puffbug.isBaby() ? new float[]{0.5F, 0.15F} : new float[]{0.5F, 0.25F};
				case EAST:
					return this.puffbug.isBaby() ? new float[]{0.15F, 0.5F} : new float[]{0.25F, 0.5F};
				case WEST:
					return this.puffbug.isBaby() ? new float[]{0.85F, 0.5F} : new float[]{0.75F, 0.5F};
			}
		}
	}

	public static class RotationController {
		private PuffBugEntity puffbug;
		private float prevYaw, yaw, startingYaw;
		private float prevPitch, pitch, startingPitch;
		private float setYaw, setPitch;
		private int tickLength;
		private int ticksSinceNotRotating;
		public boolean rotating;

		RotationController(PuffBugEntity puffbug) {
			this.puffbug = puffbug;
		}

		protected void tick() {
			this.prevYaw = this.yaw;
			this.prevPitch = this.pitch;

			if (!this.rotating) {
				this.ticksSinceNotRotating++;

				if (this.ticksSinceNotRotating > 5) {
					if (this.setYaw != 0.0F) {
						this.startingYaw = this.yaw;
					}

					if (this.setPitch != 0.0F) {
						this.startingPitch = this.pitch;
						if (this.puffbug.isNoEndimationPlaying()) {
							NetworkUtil.setPlayingAnimation(this.puffbug, EEPlayableEndimations.PUFF_BUG_ROTATE);
						}
					}

					this.setYaw = 0.0F;
					this.setPitch = 0.0F;
					this.tickLength = this.puffbug.isPassenger() ? 1 : 20;
				}
			}

			this.yaw = this.clamp((this.setYaw - this.startingYaw) <= 0, this.yaw + ((this.setYaw - this.startingYaw) / this.tickLength), this.startingYaw, this.setYaw);
			this.pitch = this.clamp((this.setPitch - this.startingPitch) <= 0, this.pitch + ((this.setPitch - this.startingPitch) / this.tickLength), this.startingPitch, this.setPitch);

			this.rotating = false;
		}

		private float clamp(boolean invert, float num, float min, float max) {
			if (invert) {
				return num > max ? num : max;
			} else {
				if (num < min) {
					return min;
				} else {
					return num > max ? max : num;
				}
			}
		}

		public void rotate(float yaw, float pitch, float roll, int tickLength) {
			if (this.setYaw != yaw) {
				this.startingYaw = this.yaw;
			}

			if (this.setPitch != pitch) {
				this.startingPitch = this.pitch;
				if (tickLength >= 20 && this.puffbug.isNoEndimationPlaying()) {
					NetworkUtil.setPlayingAnimation(this.puffbug, EEPlayableEndimations.PUFF_BUG_ROTATE);
				}
			}

			this.setYaw = yaw;
			this.setPitch = pitch;
			this.tickLength = tickLength;
			this.rotating = true;
			this.ticksSinceNotRotating = 0;

			if (!this.puffbug.level.isClientSide) {
				EndergeticExpansion.CHANNEL.send(PacketDistributor.TRACKING_ENTITY.with(() -> this.puffbug), new RotateMessage(this.puffbug.getId(), tickLength, yaw, pitch, roll));
			}
		}

		protected CompoundTag write(CompoundTag compound) {
			compound.putFloat("Yaw", this.yaw);
			compound.putFloat("Pitch", this.pitch);
			compound.putFloat("SetYaw", this.setYaw);
			compound.putFloat("SetPitch", this.setPitch);
			compound.putFloat("StartingYaw", this.startingYaw);
			compound.putFloat("StartingPitch", this.startingPitch);
			compound.putInt("TickLength", this.tickLength);
			compound.putBoolean("Rotating", this.rotating);
			return compound;
		}

		protected RotationController read(PuffBugEntity puffbug, CompoundTag compound) {
			RotationController rotationController = new RotationController(puffbug);

			rotationController.yaw = rotationController.prevYaw = compound.getFloat("Yaw");
			rotationController.pitch = rotationController.prevPitch = compound.getFloat("Pitch");
			rotationController.setYaw = compound.getFloat("SetYaw");
			rotationController.setPitch = compound.getFloat("SetPitch");
			rotationController.startingYaw = compound.getFloat("StartingYaw");
			rotationController.startingPitch = compound.getFloat("StartingPitch");
			rotationController.tickLength = compound.getInt("TickLength");
			rotationController.rotating = compound.getBoolean("Rotating");

			return rotationController;
		}

		public float[] getRotations(float ptc) {
			return new float[]{Mth.lerp(ptc, this.prevYaw, this.yaw), Mth.lerp(ptc, this.prevPitch, this.pitch)};
		}
	}
}