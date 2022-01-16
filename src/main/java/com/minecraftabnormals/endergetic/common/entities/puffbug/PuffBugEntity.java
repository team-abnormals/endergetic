package com.minecraftabnormals.endergetic.common.entities.puffbug;

import com.google.common.collect.Lists;
import com.minecraftabnormals.abnormals_core.core.endimator.ControlledEndimation;
import com.minecraftabnormals.abnormals_core.core.endimator.Endimation;
import com.minecraftabnormals.abnormals_core.core.endimator.entity.IEndimatedEntity;
import com.minecraftabnormals.abnormals_core.core.util.MathUtil;
import com.minecraftabnormals.abnormals_core.core.util.NetworkUtil;
import com.minecraftabnormals.endergetic.api.entity.pathfinding.EndergeticFlyingPathNavigator;
import com.minecraftabnormals.endergetic.api.entity.util.RayTraceHelper;
import com.minecraftabnormals.endergetic.api.util.GenerationUtils;
import com.minecraftabnormals.endergetic.client.particles.EEParticles;
import com.minecraftabnormals.endergetic.common.blocks.poise.BolloomBudBlock;
import com.minecraftabnormals.endergetic.common.entities.booflo.BoofloEntity;
import com.minecraftabnormals.endergetic.common.entities.puffbug.ai.*;
import com.minecraftabnormals.endergetic.common.network.entity.puffbug.RotateMessage;
import com.minecraftabnormals.endergetic.common.tileentities.BolloomBudTileEntity;
import com.minecraftabnormals.endergetic.common.tileentities.BolloomBudTileEntity.BudSide;
import com.minecraftabnormals.endergetic.common.tileentities.PuffBugHiveTileEntity;
import com.minecraftabnormals.endergetic.core.EndergeticExpansion;
import com.minecraftabnormals.endergetic.core.registry.EEBlocks;
import com.minecraftabnormals.endergetic.core.registry.EEEntities;
import com.minecraftabnormals.endergetic.core.registry.EEItems;
import com.minecraftabnormals.endergetic.core.registry.EESounds;
import com.minecraftabnormals.endergetic.core.registry.other.EEDataSerializers;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.BreedGoal;
import net.minecraft.entity.ai.goal.FollowParentGoal;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.potion.PotionUtils;
import net.minecraft.tags.FluidTags;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.Direction.Axis;
import net.minecraft.util.math.*;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.PacketDistributor;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Predicate;

public class PuffBugEntity extends AnimalEntity implements IEndimatedEntity {
	public static final Predicate<LivingEntity> CAN_ANGER = (entity) -> {
		if (entity instanceof PlayerEntity) {
			return entity.isAlive() && !entity.isSpectator() && !((PlayerEntity) entity).isCreative();
		}
		return !(entity instanceof PuffBugEntity) && entity.isAlive() && !entity.isSpectator() && !entity.isInvisible();
	};
	public static final float SEEKING_FACTOR = 1.1F;
	public static final EntitySize PROJECTILE_SIZE = EntitySize.fixed(0.5F, 0.5F);
	public static final EntitySize PROJECTILE_SIZE_CHILD = EntitySize.fixed(0.26325F, 0.26325F);
	private static final DataParameter<Optional<BlockPos>> HIVE_POS = EntityDataManager.defineId(PuffBugEntity.class, DataSerializers.OPTIONAL_BLOCK_POS);
	private static final DataParameter<Direction> ATTACHED_HIVE_SIDE = EntityDataManager.defineId(PuffBugEntity.class, DataSerializers.DIRECTION);
	private static final DataParameter<Optional<Vector3d>> LAUNCH_DIRECTION = EntityDataManager.defineId(PuffBugEntity.class, EEDataSerializers.OPTIONAL_VEC3D);
	private static final DataParameter<Optional<Vector3d>> FIRE_DIRECTION = EntityDataManager.defineId(PuffBugEntity.class, EEDataSerializers.OPTIONAL_VEC3D);
	private static final DataParameter<Boolean> FROM_BOTTLE = EntityDataManager.defineId(PuffBugEntity.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Boolean> INFLATED = EntityDataManager.defineId(PuffBugEntity.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Boolean> BOOSTING = EntityDataManager.defineId(PuffBugEntity.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Integer> COLOR = EntityDataManager.defineId(PuffBugEntity.class, DataSerializers.INT);

	public static final Endimation CLAIM_HIVE_ANIMATION = new Endimation(20);
	public static final Endimation PUFF_ANIMATION = new Endimation(20);
	public static final Endimation TELEPORT_TO_ANIMATION = new Endimation(15);
	public static final Endimation TELEPORT_FROM_ANIMATION = new Endimation(10);
	public static final Endimation ROTATE_ANIMATION = new Endimation(20);
	public static final Endimation POLLINATE_ANIMATION = new Endimation(120);
	public static final Endimation MAKE_ITEM_ANIMATION = new Endimation(100);
	public static final Endimation FLY_ANIMATION = new Endimation(25);
	public static final Endimation LAND_ANIMATION = new Endimation(20);
	public static final Endimation PULL_ANIMATION = new Endimation(25);

	public final ControlledEndimation HIVE_LANDING = new ControlledEndimation(20, 0);
	public final ControlledEndimation HIVE_SLEEP = new ControlledEndimation(25, 0);

	private TeleportController teleportController;
	private RotationController rotationController;
	private Endimation endimation = BLANK_ANIMATION;

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
	private int animationTick;
	public int teleportCooldown;
	public int ticksAwayFromHive;
	public int puffCooldown;

	public PuffBugEntity(EntityType<? extends PuffBugEntity> type, World worldIn) {
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
	public void onSyncedDataUpdated(DataParameter<?> key) {
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

	@SuppressWarnings("deprecation")
	@Override
	public void tick() {
		if (this.stuckInBlock) {
			this.setDeltaMovement(Vector3d.ZERO);
		}

		if (this.getVehicle() instanceof BoofloEntity) {
			this.disableProjectile();
			this.removeLaunchDirection();
			this.setInflated(true);
		}

		super.tick();
		this.endimateTick();
		this.getRotationController().tick();
		this.keepEffectsAbsorbed();

		this.fallDistance = 0;

		Vector3d motion = this.getDeltaMovement();

		if (!this.level.isClientSide) {
			if (this.teleportCooldown > 0) {
				this.teleportCooldown--;
			}

			if (this.puffCooldown > 0) {
				this.puffCooldown--;
			}

			if (!this.isPassenger() && this.isInflated() && !this.getRotationController().rotating && this.isNoEndimationPlaying()) {
				if (this.isBoosting() && RayTraceHelper.rayTrace(this, 2.0D, 1.0F).getType() != Type.BLOCK || ((this.onGround || this.isInWater()) && this.puffCooldown <= 0 && this.getPollinationPos() == null && this.getLaunchDirection() == null && this.getFireDirection() == null)) {
					NetworkUtil.setPlayingAnimationMessage(this, PUFF_ANIMATION);
					this.playSound(this.getPuffSound(), 0.15F, this.getVoicePitch());
				}
			}

			if (this.isEndimationPlaying(TELEPORT_TO_ANIMATION) && this.getAnimationTick() == 10) {
				this.getTeleportController().bringToDestination();
			} else if (this.isEndimationPlaying(TELEPORT_FROM_ANIMATION)) {
				this.setDeltaMovement(Vector3d.ZERO);
			}

			if (this.getHivePos() == null) {
				if (this.level.getGameTime() % 5 == 0 && this.getRandom().nextFloat() <= 0.1F) {
					PuffBugHiveTileEntity hive = this.findNewNearbyHive();
					if (hive != null) {
						this.addToHive(hive);
						if (this.isNoEndimationPlaying()) {
							NetworkUtil.setPlayingAnimationMessage(this, CLAIM_HIVE_ANIMATION);
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
			Random rand = this.getRandom();

			if (this.isEndimationPlaying(PUFF_ANIMATION) && this.getAnimationTick() == 5) {
				for (int i = 0; i < 3; i++) {
					double offsetX = MathUtil.makeNegativeRandomly(rand.nextFloat() * 0.1F, rand);
					double offsetZ = MathUtil.makeNegativeRandomly(rand.nextFloat() * 0.1F, rand);

					double x = this.getX() + offsetX;
					double y = this.getY() + (rand.nextFloat() * 0.05F) + 0.7F;
					double z = this.getZ() + offsetZ;

					this.level.addParticle(EEParticles.SHORT_POISE_BUBBLE.get(), x, y, z, MathUtil.makeNegativeRandomly((rand.nextFloat() * 0.1F), rand) + 0.05F, (rand.nextFloat() * 0.05F) + 0.025F, MathUtil.makeNegativeRandomly((rand.nextFloat() * 0.1F), rand) + 0.05F);
				}
			} else if (this.isEndimationPlaying(TELEPORT_TO_ANIMATION) && this.getAnimationTick() == 8) {
				for (int i = 0; i < 6; i++) {
					double offsetX = MathUtil.makeNegativeRandomly(rand.nextFloat() * 0.1F, rand);
					double offsetZ = MathUtil.makeNegativeRandomly(rand.nextFloat() * 0.1F, rand);

					double x = this.getX() + offsetX;
					double y = this.getY() + (rand.nextFloat() * 0.05F) + 0.7F;
					double z = this.getZ() + offsetZ;

					this.level.addParticle(EEParticles.SHORT_POISE_BUBBLE.get(), x, y, z, MathUtil.makeNegativeRandomly((rand.nextFloat() * 0.15F), rand) + 0.025F, (rand.nextFloat() * 0.025F) + 0.025F, MathUtil.makeNegativeRandomly((rand.nextFloat() * 0.15F), rand) + 0.025F);
				}
			} else if (this.isEndimationPlaying(TELEPORT_FROM_ANIMATION) && this.getAnimationTick() == 5) {
				for (int i = 0; i < 6; i++) {
					double offsetX = MathUtil.makeNegativeRandomly(rand.nextFloat() * 0.1F, rand);
					double offsetZ = MathUtil.makeNegativeRandomly(rand.nextFloat() * 0.1F, rand);

					double x = this.getX() + offsetX;
					double y = this.getY() + (rand.nextFloat() * 0.05F) + 0.7F;
					double z = this.getZ() + offsetZ;

					this.level.addParticle(EEParticles.SHORT_POISE_BUBBLE.get(), x, y, z, MathUtil.makeNegativeRandomly((rand.nextFloat() * 0.15F), rand) + 0.025F, (rand.nextFloat() * 0.025F) + 0.025F, MathUtil.makeNegativeRandomly((rand.nextFloat() * 0.15F), rand) + 0.025F);
				}
			}

			this.HIVE_LANDING.update();
			this.HIVE_SLEEP.update();

			this.HIVE_LANDING.tick();
			this.HIVE_SLEEP.tick();

			this.HIVE_LANDING.setDecrementing(this.getAttachedHiveSide() == Direction.UP);

			if (this.HIVE_LANDING.isAtMax()) {
				if (this.HIVE_SLEEP.isDecrementing() && this.HIVE_SLEEP.getTick() == 0) {
					this.HIVE_SLEEP.setDecrementing(false);
				} else if (this.HIVE_SLEEP.isAtMax()) {
					this.HIVE_SLEEP.setDecrementing(true);
				}
			} else {
				this.HIVE_SLEEP.setDecrementing(true);
			}

			if (this.isProjectile() && this.isEndimationPlaying(FLY_ANIMATION)) {
				this.spin = this.spin + 55.0F;
			} else {
				this.spin = 0.0F;
			}
		}

		if (this.getDesiredHiveSide() != null) {
			if (this.getHive() != null && this.getDesiredHiveSide() != Direction.DOWN) {
				float degrees = (this.getDesiredHiveSide() == Direction.SOUTH ? 0 : this.getDesiredHiveSide().getAxisDirection().getStep()) * (this.getDesiredHiveSide().getAxis() == Axis.Z ? 180.0F : -90.0F);
				this.yRot = this.yRotO = degrees;
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

				this.yRot = this.yRotO;
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
				this.yRot = 0.0F;
				this.yHeadRot = 0.0F;
				this.yBodyRot = 0.0F;

				Vector3d fireDirection = this.getFireDirection();

				if (fireDirection != null) {
					if ((this.level.isClientSide && !this.stuckInBlock) || !this.level.isClientSide) {
						this.getRotationController().rotate((float) MathHelper.wrapDegrees(fireDirection.y() - this.yRot), (float) fireDirection.x() + 90.0F, 0.0F, 5);
					}

					LivingEntity target = this.getTarget();

					if (!this.stuckInBlock) {
						if (!this.level.isClientSide && target != null && this.isEndimationPlaying(FLY_ANIMATION)) {
							float seekOffset = target.getY() > this.getY() ? 0.0F : 0.5F;
							Vector3d targetVecNoScale = new Vector3d(target.getX() - this.getX(), target.getY() - seekOffset - this.getY(), target.getZ() - this.getZ());
							Vector3d targetVec = targetVecNoScale.scale(SEEKING_FACTOR);

							double motionLength = motion.length();
							double targetVecLength = targetVec.length();

							float totalVecLength = MathHelper.sqrt(motionLength * motionLength + targetVecLength * targetVecLength);

							Vector3d newMotion = motion.scale(motionLength / totalVecLength).add(targetVec.scale(targetVecLength / totalVecLength));

							float gravityCompensator = totalVecLength <= 4.0F ? 0.05F : 0.1F;

							this.setDeltaMovement(newMotion.scale(0.4F).add(0.0F, gravityCompensator, 0.0F));
						}

						Vector3d newestMotion = this.getDeltaMovement();

						float pitch = -((float) (MathHelper.atan2(newestMotion.y(), (double) MathHelper.sqrt(newestMotion.x() * newestMotion.x() + newestMotion.z() * newestMotion.z())) * (double) (180F / (float) Math.PI)));
						float yaw = (float) (MathHelper.atan2(newestMotion.z(), newestMotion.x()) * (double) (180F / (float) Math.PI)) - 90F;

						this.setFireDirection(pitch, yaw);
					}
				}
			}
		}

		if (this.isProjectile() && !this.isInflated()) {
			BlockPos blockpos = this.blockPosition();
			BlockState blockstate = this.level.getBlockState(blockpos);
			if (!blockstate.isAir(this.level, blockpos) && !this.noPhysics) {
				VoxelShape voxelshape = blockstate.getCollisionShape(this.level, blockpos);
				if (!voxelshape.isEmpty()) {
					for (AxisAlignedBB axisalignedbb : voxelshape.toAabbs()) {
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
				Vector3d positionVec = new Vector3d(this.getX(), this.getY(), this.getZ());
				Vector3d endVec = positionVec.add(motion);

				RayTraceResult traceResult = this.level.clip(new RayTraceContext(positionVec, endVec, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, this));
				EntityRayTraceResult entityTraceResult = this.traceEntity(positionVec, endVec);

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
	public void readAdditionalSaveData(CompoundNBT compound) {
		super.readAdditionalSaveData(compound);

		this.setAttachedHiveSide(Direction.from3DDataValue(compound.getByte("AttachedHiveSide")));
		this.setFromBottle(compound.getBoolean("FromBottle"));

		this.teleportCooldown = compound.getInt("TeleportCooldown");
		this.ticksAwayFromHive = compound.getInt("TicksAwayFromHive");

		if (compound.contains("IsInflated")) {
			this.setInflated(compound.getBoolean("IsInflated"));
		}

		if (compound.contains("HivePos", 10)) {
			this.setHivePos(NBTUtil.readBlockPos(compound.getCompound("HivePos")));
		}

		if (compound.contains("StuckInBlockState", 10)) {
			this.stuckInBlockState = NBTUtil.readBlockState(compound.getCompound("StuckInBlockState"));
		}

		CompoundNBT stackToCreate = compound.getCompound("ItemStackToCreate");

		if (stackToCreate != null) {
			ItemStack newStackToCreate = ItemStack.of(stackToCreate);
			if (!newStackToCreate.isEmpty()) {
				this.setStackToCreate(newStackToCreate);
			}
		}

		this.rotationController = this.getRotationController().read(this, compound.getCompound("Orientation"));
	}

	@Override
	public void addAdditionalSaveData(CompoundNBT compound) {
		super.addAdditionalSaveData(compound);

		compound.putByte("AttachedHiveSide", (byte) this.getAttachedHiveSide().get3DDataValue());
		compound.putBoolean("FromBottle", this.isFromBottle());
		compound.putBoolean("IsInflated", this.isInflated());

		compound.putInt("TeleportCooldown", this.teleportCooldown);
		compound.putInt("TicksAwayFromHive", this.ticksAwayFromHive);

		if (this.getHivePos() != null) {
			compound.put("HivePos", NBTUtil.writeBlockPos(this.getHivePos()));
		}

		if (this.hasStackToCreate()) {
			compound.put("ItemStackToCreate", this.getStackToCreate().save(new CompoundNBT()));
		}

		if (this.stuckInBlockState != null) {
			compound.put("StuckInBlockState", NBTUtil.writeBlockState(this.stuckInBlockState));
		}

		compound.put("Orientation", this.getRotationController().write(new CompoundNBT()));
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
				TileEntity tileEntity = this.level.getBlockEntity(hivePos).getTileEntity();
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

	public void setAttachedHiveSide(Direction side) {
		this.entityData.set(ATTACHED_HIVE_SIDE, side);
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
		this.entityData.set(LAUNCH_DIRECTION, Optional.of(new Vector3d(pitch, yaw, 0.0F)));
	}

	public void setFireDirection(float pitch, float yaw) {
		this.entityData.set(FIRE_DIRECTION, Optional.of(new Vector3d(pitch, yaw, 0.0F)));
	}

	public void removeLaunchDirection() {
		this.entityData.set(LAUNCH_DIRECTION, Optional.empty());
	}

	public void removeFireDirection() {
		this.entityData.set(FIRE_DIRECTION, Optional.empty());
	}

	@Nullable
	public Vector3d getLaunchDirection() {
		return this.entityData.get(LAUNCH_DIRECTION).orElse(null);
	}

	@Nullable
	public Vector3d getFireDirection() {
		return this.entityData.get(FIRE_DIRECTION).orElse(null);
	}

	public boolean hasLevitation() {
		return this.hasEffect(Effects.LEVITATION);
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
	public Endimation[] getEndimations() {
		return new Endimation[]{
				CLAIM_HIVE_ANIMATION,
				PUFF_ANIMATION,
				TELEPORT_TO_ANIMATION,
				TELEPORT_FROM_ANIMATION,
				ROTATE_ANIMATION,
				POLLINATE_ANIMATION,
				MAKE_ITEM_ANIMATION,
				FLY_ANIMATION,
				LAND_ANIMATION,
				PULL_ANIMATION
		};
	}

	@Override
	public void onEndimationStart(Endimation endimation) {
		if (endimation == PUFF_ANIMATION) {
			float pitch = this.isVehicle() ? 1.0F : this.xRot;
			float xMotion = -MathHelper.sin(this.yRot * ((float) Math.PI / 180F)) * MathHelper.cos(pitch * ((float) Math.PI / 180F));
			float zMotion = MathHelper.cos(this.yRot * ((float) Math.PI / 180F)) * MathHelper.cos(pitch * ((float) Math.PI / 180F));

			Vector3d motion = new Vector3d(xMotion, 0.65F, zMotion).normalize();

			if (this.getTarget() != null && CAN_ANGER.test(this.getTarget())) {
				motion.scale(2.0F);
			}

			this.push(motion.x() * (this.getAttribute(Attributes.MOVEMENT_SPEED).getValue() - 0.1F), motion.y(), motion.z() * (this.getAttribute(Attributes.MOVEMENT_SPEED).getValue() - 0.1F));
		} else if (endimation == TELEPORT_TO_ANIMATION) {
			if (!this.level.isClientSide) {
				this.playSound(this.getTeleportSound(false), 0.65F, this.getVoicePitch());
			}
		} else if (endimation == FLY_ANIMATION) {
			this.level.playSound(null, this, EESounds.PUFFBUG_LAUNCH.get(), SoundCategory.HOSTILE, 0.25F, this.getRandom().nextFloat() * 0.35F + 0.75F);
		}
	}

	@Override
	public void onEndimationEnd(Endimation endimation) {
		if (endimation == TELEPORT_TO_ANIMATION) {
			if (!this.level.isClientSide) {
				NetworkUtil.setPlayingAnimationMessage(this, TELEPORT_FROM_ANIMATION);
				this.playSound(this.getTeleportSound(true), 0.65F, this.getVoicePitch());
			}
		} else if (endimation == POLLINATE_ANIMATION) {
			this.addEffect(new EffectInstance(Effects.LEVITATION, 3000));
			if (this.getPollinationPos() != null) {
				TileEntity te = this.level.getBlockEntity(this.getPollinationPos());
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
	public void travel(Vector3d moveDirection) {
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

			super.travel(Vector3d.ZERO);

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
			if (blockpos.closerThan(this.position(), xyDistance)) {
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

		CompoundNBT nbt = bottle.getOrCreateTag();

		if (this.getColor() != -1) {
			nbt.putInt("ColorTag", this.getColor());
		}

		nbt.putInt("TeleportCooldown", this.teleportCooldown);

		if (!this.getActiveEffects().isEmpty()) {
			ListNBT listnbt = new ListNBT();

			for (EffectInstance effectinstance : this.getActiveEffects()) {
				listnbt.add(effectinstance.save(new CompoundNBT()));
			}

			nbt.put("CustomPotionEffects", listnbt);
		}

		nbt.putBoolean("IsFromBottle", true);
		nbt.putBoolean("IsChild", this.isBaby());
	}

	private void keepEffectsAbsorbed() {
		Iterator<Effect> iterator = this.getActiveEffectsMap().keySet().iterator();
		while (iterator.hasNext()) {
			Effect effect = iterator.next();
			if (effect != Effects.LEVITATION) {
				EffectInstance effectInstance = this.getActiveEffectsMap().get(effect);
				this.getActiveEffectsMap().put(effect, new EffectInstance(effect, 1600, effectInstance.getAmplifier(), effectInstance.isAmbient(), effectInstance.isVisible()));
			}
		}
	}

	/**
	 * Looks for an open position near the hive, used for alerted Puff Bugs
	 */
	public void tryToTeleportToHive(BlockPos pos) {
		BlockPos.Mutable positions = new BlockPos.Mutable();
		List<BlockPos> avaliablePositions = Lists.newArrayList();
		PuffBugHiveTileEntity hive = this.getHive();

		if (hive == null || (hive != null && !hive.canTeleportTo()) || (this.getAttachedHiveSide() == Direction.UP && Math.sqrt(this.distanceToSqr(Vector3d.atCenterOf((pos)))) < 5.0F) || this.isEndimationPlaying(TELEPORT_FROM_ANIMATION)) {
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
			if (!this.isEndimationPlaying(TELEPORT_TO_ANIMATION)) {
				this.getTeleportController().processTeleportation();
			}
		}
	}

	private void onSting(RayTraceResult result) {
		RayTraceResult.Type resultType = result.getType();
		if (resultType == RayTraceResult.Type.ENTITY) {
			EntityRayTraceResult entityResult = (EntityRayTraceResult) result;
			Entity entity = entityResult.getEntity();
			if (entity.hurt(DamageSource.mobAttack(this).setProjectile(), (float) this.getAttribute(Attributes.ATTACK_DAMAGE).getValue())) {
				this.setInflated(true);
				this.removeFireDirection();
				this.stuckInBlock = false;

				if (!this.getActiveEffects().isEmpty() && entity instanceof LivingEntity) {
					for (EffectInstance effects : this.getActiveEffects()) {
						((LivingEntity) entity).addEffect(effects);
					}
					this.removeAllEffects();
				}
			}
		} else {
			BlockRayTraceResult blockraytraceresult = (BlockRayTraceResult) result;
			this.stuckInBlockState = this.level.getBlockState(blockraytraceresult.getBlockPos());
			this.stuckInBlock = true;

			Vector3d end = result.getLocation();
			this.setPos(end.x(), end.y(), end.z());
			this.setDeltaMovement(Vector3d.ZERO);

			if (!this.level.isClientSide) {
				NetworkUtil.setPlayingAnimationMessage(this, LAND_ANIMATION);
			}

			this.level.playSound(null, this, EESounds.PUFFBUG_LAND.get(), SoundCategory.HOSTILE, 0.5F, this.getVoicePitch());
		}
	}

	@Nullable
	private EntityRayTraceResult traceEntity(Vector3d start, Vector3d end) {
		return ProjectileHelper.getEntityHitResult(this.level, this, start, end, this.getBoundingBox().expandTowards(this.getDeltaMovement()).inflate(0.5F), (result) -> {
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
		TileEntity te = side == Direction.DOWN ? this.isBaby() ? this.level.getBlockEntity(this.blockPosition().above(1)) : this.level.getBlockEntity(this.blockPosition().above(2)) : this.isBaby() ? this.level.getBlockEntity(this.blockPosition().relative(side.getOpposite())) : this.level.getBlockEntity(this.blockPosition().above(1).relative(side.getOpposite()));
		if (te != this.getHive()) {
			return false;
		}

		BlockPos hivePos = this.getHivePos();
		switch (side) {
			case UP:
				return false;
			case DOWN:
				float yOffsetDown = this.isBaby() ? 0.45F : -0.15F;
				return Vector3d.atCenterOf((hivePos.below()).offset(0.5F, yOffsetDown, 0.5F)).distanceTo(this.position()) < 0.25F;
			default:
				float yOffset = this.isBaby() ? 0.2F : -0.2F;
				BlockPos sideOffset = hivePos.relative(side);
				return this.level.isEmptyBlock(sideOffset.above()) && this.level.isEmptyBlock(sideOffset.below())
						&& Vector3d.atLowerCornerOf(sideOffset).add(this.getTeleportController().getOffsetForDirection(side)[0], yOffset, this.getTeleportController().getOffsetForDirection(side)[1]).distanceTo(this.position()) < (this.isBaby() ? 0.1F : 0.25F);
		}
	}

	@Override
	protected PathNavigator createNavigation(World worldIn) {
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
	public float getBrightness() {
		BlockPos blockpos = new BlockPos(this.getX(), this.getY() + (double) this.getEyeHeight(), this.getZ());
		if (this.stuckInBlock && !this.isInflated()) {
			boolean rotationFlag = true;
			float[] rotations = this.getRotationController().getRotations(1.0F);
			Direction horizontalOffset = Direction.fromYRot(rotations[0]).getOpposite();
			Direction verticalOffset = (rotations[1] <= 180.0F && rotations[1] > 100.0F) ? Direction.UP : Direction.DOWN;

			if (rotations[1] >= 80.0F && rotations[1] <= 100.0F) {
				rotationFlag = false;
			}

			return this.level.isAreaLoaded(blockpos, 0) ? this.level.getMaxLocalRawBrightness(rotationFlag ? blockpos.relative(horizontalOffset).relative(verticalOffset) : blockpos.relative(horizontalOffset)) : 0;
		}
		return super.getBrightness();
	}

	@Override
	public boolean causeFallDamage(float distance, float damageMultiplier) {
		return false;
	}

	@Override
	protected float getStandingEyeHeight(Pose poseIn, EntitySize size) {
		return this.isProjectile() ? 0.0F : size.height * 0.5F;
	}

	@Override
	protected void updateInvisibilityStatus() {
		super.updateInvisibilityStatus();
		Collection<EffectInstance> effects = this.getActiveEffects();

		if (!effects.isEmpty()) {
			this.setColor(PotionUtils.getColor(effects));
		} else {
			this.setColor(-1);
		}
	}

	@Override
	public ItemStack getPickedResult(RayTraceResult target) {
		return new ItemStack(EEItems.PUFF_BUG_SPAWN_EGG.get());
	}

	@Override
	public CreatureAttribute getMobType() {
		return CreatureAttribute.ARTHROPOD;
	}

	@Override
	public boolean isFood(ItemStack stack) {
		return stack.getItem() == EEBlocks.TALL_POISE_BUSH.get().asItem();
	}

	@Override
	public AxisAlignedBB getBoundingBoxForCulling() {
		return super.getBoundingBoxForCulling().inflate(12);
	}

	@Override
	public ActionResultType mobInteract(PlayerEntity player, Hand hand) {
		ItemStack itemstack = player.getItemInHand(hand);
		Item item = itemstack.getItem();

		if (!this.isAlive() || this.isAggressive()) return ActionResultType.FAIL;

		if (item == Items.GLASS_BOTTLE) {
			this.playSound(SoundEvents.BOTTLE_FILL_DRAGONBREATH, 1.0F, 1.0F);
			itemstack.shrink(1);
			ItemStack bottle = new ItemStack(EEItems.PUFFBUG_BOTTLE.get());
			this.setBottleData(bottle);

			if (itemstack.isEmpty()) {
				player.setItemInHand(hand, bottle);
			} else if (!player.inventory.add(bottle)) {
				player.drop(bottle, false);
			}

			this.remove();
			return ActionResultType.SUCCESS;
		} else if (!this.hasStackToCreate() && this.hasLevitation()) {
			ItemStack newStackToCreate = item == EEItems.BOLLOOM_FRUIT.get() ? new ItemStack(EEBlocks.BOLLOOM_BUD.get()) : this.isFood(itemstack) ? new ItemStack(EEBlocks.PUFFBUG_HIVE.get()) : null;
			if (newStackToCreate != null) {
				this.setStackToCreate(newStackToCreate);
				this.usePlayerItem(player, itemstack);
				return ActionResultType.sidedSuccess(this.level.isClientSide);
			}
			return ActionResultType.PASS;
		} else {
			return super.mobInteract(player, hand);
		}
	}

	@Override
	public ILivingEntityData finalizeSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
		Random rng = this.getRandom();

		if (dataTag != null) {
			int age = dataTag.getBoolean("IsChild") ? -24000 : 0;

			this.setAge(age);
			this.teleportCooldown = dataTag.getInt("TeleportCooldown");
			this.setFromBottle(dataTag.getBoolean("IsFromBottle"));

			if (dataTag.contains("ColorTag", 3)) {
				this.setColor(dataTag.getInt("ColorTag"));
			}

			if (dataTag.contains("CustomPotionEffects")) {
				for (EffectInstance effectinstance : PotionUtils.getCustomEffects(dataTag)) {
					this.addEffect(effectinstance);
				}
			}
		}

		if (reason == SpawnReason.STRUCTURE) {
			this.ticksAwayFromHive = rng.nextInt(1500) + 1500;

			if (rng.nextFloat() < 0.1F) {
				this.age = -24000;
			}
		} else if (reason == SpawnReason.NATURAL || reason == SpawnReason.SPAWNER) {
			if (rng.nextFloat() < 0.05F) {
				int swarmSize = rng.nextInt(11) + 10;
				Vector3d centeredPos = Vector3d.atCenterOf(this.blockPosition());
				for (int i = 0; i < swarmSize; i++) {
					Vector3d spawnPos = centeredPos.add(MathUtil.makeNegativeRandomly(rng.nextFloat() * 5.5F, rng), MathUtil.makeNegativeRandomly(rng.nextFloat() * 2.0F, rng), MathUtil.makeNegativeRandomly(rng.nextFloat() * 5.5F, rng));

					if (this.level.isEmptyBlock(new BlockPos(spawnPos))) {
						PuffBugEntity swarmChild = EEEntities.PUFF_BUG.get().create(this.level);
						swarmChild.moveTo(spawnPos.x(), spawnPos.y(), spawnPos.z(), 0.0F, 0.0F);
						swarmChild.finalizeSpawn(worldIn, this.level.getCurrentDifficultyAt(new BlockPos(spawnPos)), SpawnReason.EVENT, null, null);
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
						for (EffectInstance effects : this.getActiveEffects()) {
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
	public EntitySize getDimensions(Pose pose) {
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
	protected boolean isMovementNoisy() {
		return false;
	}

	@Nullable
	@Override
	public AgeableEntity getBreedOffspring(ServerWorld world, AgeableEntity ageableEntity) {
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

	@Override
	public Endimation getPlayingEndimation() {
		return this.endimation;
	}

	@Override
	public void setPlayingEndimation(Endimation endimationToPlay) {
		this.endimation = endimationToPlay;
		this.setAnimationTick(0);
	}

	@Override
	public int getAnimationTick() {
		return this.animationTick;
	}

	@Override
	public void setAnimationTick(int animationTick) {
		this.animationTick = animationTick;
	}

	static class PuffBugMoveController extends MovementController {
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

			if (this.operation == MovementController.Action.MOVE_TO && !this.puffbug.getNavigation().isDone()) {
				double xDistance = this.wantedX - this.puffbug.getX();
				double yDistance = this.wantedY - this.puffbug.getY();
				double zDistance = this.wantedZ - this.puffbug.getZ();
				double totalDistance = (double) MathHelper.sqrt(xDistance * xDistance + yDistance * yDistance + zDistance * zDistance);

				double verticalVelocity = yDistance / totalDistance;

				float angle = (float) (MathHelper.atan2(zDistance, xDistance) * (180F / Math.PI)) - 90.0F;

				this.puffbug.yRot = this.rotlerp(this.puffbug.yRot, angle, 20.0F);
				this.puffbug.yBodyRot = this.puffbug.yRot;

				float speed = (float) (this.speedModifier * this.puffbug.getAttribute(Attributes.FLYING_SPEED).getValue());

				if (verticalVelocity < 0.0F) {
					this.puffbug.setSpeed(MathHelper.lerp(0.125F, this.puffbug.getSpeed(), speed));
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
		private World world;
		@Nullable
		private BlockPos destination;

		TeleportController(PuffBugEntity puffbug) {
			this.puffbug = puffbug;
			this.world = puffbug.level;
		}

		public void processTeleportation() {
			if (!this.hasNoDestination() && !this.world.isClientSide) {
				this.puffbug.getNavigation().stop();
				NetworkUtil.setPlayingAnimationMessage(this.puffbug, PuffBugEntity.TELEPORT_TO_ANIMATION);

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
				this.puffbug.setDeltaMovement(Vector3d.ZERO);

				if (side != null) {
					this.puffbug.setDesiredHiveSide(side);
					this.puffbug.setTeleportHiveSide(null);
				}
			}
		}

		@Nullable
		public boolean tryToCreateDesinationTo(BlockPos pos, @Nullable Direction direction) {
			boolean directionFlag = direction != null ? this.world.isEmptyBlock(pos.relative(direction)) : true;

			if (direction != null && direction != Direction.DOWN) {
				if (!this.world.isEmptyBlock(pos.above()) || !this.world.isEmptyBlock(pos.below())) {
					directionFlag = false;
				}
			}

			if (this.puffbug.tickCount > 5 && this.world.isEmptyBlock(pos) && directionFlag && this.hasNoDestination()) {
				if (this.world.getEntitiesOfClass(Entity.class, new AxisAlignedBB(pos)).isEmpty()) {
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
							NetworkUtil.setPlayingAnimationMessage(this.puffbug, PuffBugEntity.ROTATE_ANIMATION);
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
					NetworkUtil.setPlayingAnimationMessage(this.puffbug, PuffBugEntity.ROTATE_ANIMATION);
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

		protected CompoundNBT write(CompoundNBT compound) {
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

		protected RotationController read(PuffBugEntity puffbug, CompoundNBT compound) {
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
			return new float[]{MathHelper.lerp(ptc, this.prevYaw, this.yaw), MathHelper.lerp(ptc, this.prevPitch, this.pitch)};
		}
	}
}