package com.minecraftabnormals.endergetic.common.entities.puffbug;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;
import com.teamabnormals.abnormals_core.core.library.endimator.ControlledEndimation;
import com.teamabnormals.abnormals_core.core.library.endimator.Endimation;
import com.teamabnormals.abnormals_core.core.library.endimator.entity.IEndimatedEntity;
import com.teamabnormals.abnormals_core.core.utils.MathUtils;
import com.teamabnormals.abnormals_core.core.utils.NetworkUtil;
import com.minecraftabnormals.endergetic.api.entity.pathfinding.EndergeticFlyingPathNavigator;
import com.minecraftabnormals.endergetic.api.entity.util.RayTraceHelper;
import com.minecraftabnormals.endergetic.api.util.GenerationUtils;
import com.minecraftabnormals.endergetic.client.particles.EEParticles;
import com.minecraftabnormals.endergetic.common.blocks.poise.BolloomBudBlock;
import com.minecraftabnormals.endergetic.common.entities.booflo.BoofloEntity;
import com.minecraftabnormals.endergetic.common.entities.puffbug.ai.PuffBugAttachToHiveGoal;
import com.minecraftabnormals.endergetic.common.entities.puffbug.ai.PuffBugAttackGoal;
import com.minecraftabnormals.endergetic.common.entities.puffbug.ai.PuffBugBoostGoal;
import com.minecraftabnormals.endergetic.common.entities.puffbug.ai.PuffBugCreateItemGoal;
import com.minecraftabnormals.endergetic.common.entities.puffbug.ai.PuffBugDescentGoal;
import com.minecraftabnormals.endergetic.common.entities.puffbug.ai.PuffBugPollinateGoal;
import com.minecraftabnormals.endergetic.common.entities.puffbug.ai.PuffBugPullOutGoal;
import com.minecraftabnormals.endergetic.common.entities.puffbug.ai.PuffBugRestOnHiveGoal;
import com.minecraftabnormals.endergetic.common.entities.puffbug.ai.PuffBugRotateToFireGoal;
import com.minecraftabnormals.endergetic.common.entities.puffbug.ai.PuffBugTargetAggressorGoal;
import com.minecraftabnormals.endergetic.common.entities.puffbug.ai.PuffBugTeleportToBudGoal;
import com.minecraftabnormals.endergetic.common.entities.puffbug.ai.PuffBugTeleportToRestGoal;
import com.minecraftabnormals.endergetic.common.network.entity.puffbug.RotateMessage;
import com.minecraftabnormals.endergetic.common.tileentities.BolloomBudTileEntity;
import com.minecraftabnormals.endergetic.common.tileentities.PuffBugHiveTileEntity;
import com.minecraftabnormals.endergetic.common.tileentities.BolloomBudTileEntity.BudSide;
import com.minecraftabnormals.endergetic.core.EndergeticExpansion;
import com.minecraftabnormals.endergetic.core.registry.EEBlocks;
import com.minecraftabnormals.endergetic.core.registry.EEEntities;
import com.minecraftabnormals.endergetic.core.registry.EEItems;
import com.minecraftabnormals.endergetic.core.registry.EESounds;
import com.minecraftabnormals.endergetic.core.registry.other.EEDataSerializers;

import net.minecraft.block.BlockState;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.Pose;
import net.minecraft.entity.SpawnReason;
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
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.Direction.Axis;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.PacketDistributor;

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
	private static final DataParameter<Optional<BlockPos>> HIVE_POS = EntityDataManager.createKey(PuffBugEntity.class, DataSerializers.OPTIONAL_BLOCK_POS);
	private static final DataParameter<Direction> ATTACHED_HIVE_SIDE = EntityDataManager.createKey(PuffBugEntity.class, DataSerializers.DIRECTION);
	private static final DataParameter<Optional<Vector3d>> LAUNCH_DIRECTION = EntityDataManager.createKey(PuffBugEntity.class, EEDataSerializers.OPTIONAL_VEC3D);
	private static final DataParameter<Optional<Vector3d>> FIRE_DIRECTION = EntityDataManager.createKey(PuffBugEntity.class, EEDataSerializers.OPTIONAL_VEC3D);
	private static final DataParameter<Boolean> FROM_BOTTLE = EntityDataManager.createKey(PuffBugEntity.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Boolean> INFLATED = EntityDataManager.createKey(PuffBugEntity.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Boolean> BOOSTING = EntityDataManager.createKey(PuffBugEntity.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Integer> COLOR = EntityDataManager.createKey(PuffBugEntity.class, DataSerializers.VARINT);

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
		this.moveController = new PuffBugMoveController(this);
		this.teleportController = new TeleportController(this);
		this.rotationController = new RotationController(this);
		this.experienceValue = 2;
	}

	@Override
	protected void registerData() {
		super.registerData();
		this.getDataManager().register(HIVE_POS, Optional.empty());
		this.getDataManager().register(ATTACHED_HIVE_SIDE, Direction.UP);
		this.getDataManager().register(LAUNCH_DIRECTION, Optional.empty());
		this.getDataManager().register(FIRE_DIRECTION, Optional.empty());
		this.getDataManager().register(COLOR, -1);
		this.getDataManager().register(FROM_BOTTLE, false);
		this.getDataManager().register(INFLATED, true);
		this.getDataManager().register(BOOSTING, false);
	}

	@Override
	public void notifyDataManagerChange(DataParameter<?> key) {
		super.notifyDataManagerChange(key);
		if (key.equals(FIRE_DIRECTION) || key.equals(LAUNCH_DIRECTION)) {
			this.recalculateSize();
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

	@Override
	public void tick() {
		if (this.stuckInBlock) {
			this.setMotion(Vector3d.ZERO);
		}

		if (this.getRidingEntity() instanceof BoofloEntity) {
			this.disableProjectile();
			this.removeLaunchDirection();
			this.setInflated(true);
		}

		super.tick();
		this.endimateTick();
		this.getRotationController().tick();
		this.keepEffectsAbsorbed();

		this.fallDistance = 0;

		Vector3d motion = this.getMotion();

		if (!this.world.isRemote) {
			if (this.teleportCooldown > 0) {
				this.teleportCooldown--;
			}

			if (this.puffCooldown > 0) {
				this.puffCooldown--;
			}

			if (!this.isPassenger() && this.isInflated() && !this.getRotationController().rotating && this.isNoEndimationPlaying()) {
				if (this.isBoosting() && RayTraceHelper.rayTrace(this, 2.0D, 1.0F).getType() != Type.BLOCK || ((this.onGround || this.isInWater()) && this.puffCooldown <= 0 && this.getPollinationPos() == null && this.getLaunchDirection() == null && this.getFireDirection() == null)) {
					NetworkUtil.setPlayingAnimationMessage(this, PUFF_ANIMATION);
					this.playSound(this.getPuffSound(), 0.15F, this.getSoundPitch());
				}
			}

			if (this.isEndimationPlaying(TELEPORT_TO_ANIMATION) && this.getAnimationTick() == 10) {
				this.getTeleportController().bringToDestination();
			} else if (this.isEndimationPlaying(TELEPORT_FROM_ANIMATION)) {
				this.setMotion(Vector3d.ZERO);
			}

			if (this.getHivePos() == null) {
				if (this.world.getGameTime() % 5 == 0 && this.getRNG().nextFloat() <= 0.1F) {
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
							if (this.getAttackTarget() == null && this.getRNG().nextInt(7500) == 0 && !this.isInLove() && !this.getHive().isHiveFull()) {
								for (PuffBugEntity nearbyHiveMembers : this.world.getEntitiesWithinAABB(PuffBugEntity.class, this.getBoundingBox().grow(9.0F), puffbug -> (puffbug.getHive() != null && puffbug.getHive() == this.getHive()) && this.getDistance(puffbug) < 12.0F)) {

									this.setInLove(1000);
									this.world.setEntityState(this, (byte) 18);

									nearbyHiveMembers.setInLove(1000);
									this.world.setEntityState(nearbyHiveMembers, (byte) 18);

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

			if (this.getAttackTarget() != null && !CAN_ANGER.test(this.getAttackTarget())) {
				this.setAttackTarget(null);
			}

			if (this.isProjectile() && (this.handleFluidAcceleration(FluidTags.WATER, 1.0F) || this.handleFluidAcceleration(FluidTags.LAVA, 1.0F))) {
				this.disableProjectile();
			}
		} else {
			this.prevSpin = this.spin;
			Random rand = this.getRNG();

			if (this.isEndimationPlaying(PUFF_ANIMATION) && this.getAnimationTick() == 5) {
				for (int i = 0; i < 3; i++) {
					double offsetX = MathUtils.makeNegativeRandomly(rand.nextFloat() * 0.1F, rand);
					double offsetZ = MathUtils.makeNegativeRandomly(rand.nextFloat() * 0.1F, rand);

					double x = this.getPosX() + offsetX;
					double y = this.getPosY() + (rand.nextFloat() * 0.05F) + 0.7F;
					double z = this.getPosZ() + offsetZ;

					this.world.addParticle(EEParticles.SHORT_POISE_BUBBLE.get(), x, y, z, MathUtils.makeNegativeRandomly((rand.nextFloat() * 0.1F), rand) + 0.05F, (rand.nextFloat() * 0.05F) + 0.025F, MathUtils.makeNegativeRandomly((rand.nextFloat() * 0.1F), rand) + 0.05F);
				}
			} else if (this.isEndimationPlaying(TELEPORT_TO_ANIMATION) && this.getAnimationTick() == 8) {
				for (int i = 0; i < 6; i++) {
					double offsetX = MathUtils.makeNegativeRandomly(rand.nextFloat() * 0.1F, rand);
					double offsetZ = MathUtils.makeNegativeRandomly(rand.nextFloat() * 0.1F, rand);

					double x = this.getPosX() + offsetX;
					double y = this.getPosY() + (rand.nextFloat() * 0.05F) + 0.7F;
					double z = this.getPosZ() + offsetZ;

					this.world.addParticle(EEParticles.SHORT_POISE_BUBBLE.get(), x, y, z, MathUtils.makeNegativeRandomly((rand.nextFloat() * 0.15F), rand) + 0.025F, (rand.nextFloat() * 0.025F) + 0.025F, MathUtils.makeNegativeRandomly((rand.nextFloat() * 0.15F), rand) + 0.025F);
				}
			} else if (this.isEndimationPlaying(TELEPORT_FROM_ANIMATION) && this.getAnimationTick() == 5) {
				for (int i = 0; i < 6; i++) {
					double offsetX = MathUtils.makeNegativeRandomly(rand.nextFloat() * 0.1F, rand);
					double offsetZ = MathUtils.makeNegativeRandomly(rand.nextFloat() * 0.1F, rand);

					double x = this.getPosX() + offsetX;
					double y = this.getPosY() + (rand.nextFloat() * 0.05F) + 0.7F;
					double z = this.getPosZ() + offsetZ;

					this.world.addParticle(EEParticles.SHORT_POISE_BUBBLE.get(), x, y, z, MathUtils.makeNegativeRandomly((rand.nextFloat() * 0.15F), rand) + 0.025F, (rand.nextFloat() * 0.025F) + 0.025F, MathUtils.makeNegativeRandomly((rand.nextFloat() * 0.15F), rand) + 0.025F);
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
				float degrees = (this.getDesiredHiveSide() == Direction.SOUTH ? 0 : this.getDesiredHiveSide().getAxisDirection().getOffset()) * (this.getDesiredHiveSide().getAxis() == Axis.Z ? 180.0F : -90.0F);
				this.rotationYaw = this.prevRotationYaw = degrees;
				this.rotationYawHead = this.prevRotationYawHead = degrees;
				this.renderYawOffset = this.prevRenderYawOffset = degrees;
			}
		}

		if (this.getAttachedHiveSide() != Direction.UP) {
			if (this.getHive() != null) {
				this.setMotion(this.getMotion().mul(1.0F, 0.0F, 1.0F));
				this.getNavigator().clearPath();
				this.setAIMoveSpeed(0.0F);

				this.addVelocity(0.0F, 0.0025F, 0.0F);

				this.rotationYaw = this.prevRotationYaw;
				this.rotationYawHead = this.prevRotationYawHead;
				this.renderYawOffset = this.prevRenderYawOffset;

				if (!this.world.isRemote && !this.isAtCorrectRestLocation(this.getAttachedHiveSide())) {
					this.setAttachedHiveSide(Direction.UP);
				}
			} else {
				if (!this.world.isRemote) {
					this.setAttachedHiveSide(Direction.UP);
				}
			}
		} else {
			if ((this.getLaunchDirection() != null || this.getFireDirection() != null)) {
				this.rotationYaw = 0.0F;
				this.rotationYawHead = 0.0F;
				this.renderYawOffset = 0.0F;

				Vector3d fireDirection = this.getFireDirection();

				if (fireDirection != null) {
					if ((this.world.isRemote && !this.stuckInBlock) || !this.world.isRemote) {
						this.getRotationController().rotate((float) MathHelper.wrapDegrees(fireDirection.getY() - this.rotationYaw), (float) fireDirection.getX() + 90.0F, 0.0F, 5);
					}

					LivingEntity target = this.getAttackTarget();

					if (!this.stuckInBlock) {
						if (!this.world.isRemote && target != null && this.isEndimationPlaying(FLY_ANIMATION)) {
							float seekOffset = target.getPosY() > this.getPosY() ? 0.0F : 0.5F;
							Vector3d targetVecNoScale = new Vector3d(target.getPosX() - this.getPosX(), target.getPosY() - seekOffset - this.getPosY(), target.getPosZ() - this.getPosZ());
							Vector3d targetVec = targetVecNoScale.scale(SEEKING_FACTOR);

							double motionLength = motion.length();
							double targetVecLength = targetVec.length();

							float totalVecLength = MathHelper.sqrt(motionLength * motionLength + targetVecLength * targetVecLength);

							Vector3d newMotion = motion.scale(motionLength / totalVecLength).add(targetVec.scale(targetVecLength / totalVecLength));

							float gravityCompensator = totalVecLength <= 4.0F ? 0.05F : 0.1F;

							this.setMotion(newMotion.scale(0.4F).add(0.0F, gravityCompensator, 0.0F));
						}

						Vector3d newestMotion = this.getMotion();

						float pitch = -((float) (MathHelper.atan2(newestMotion.getY(), (double) MathHelper.sqrt(newestMotion.getX() * newestMotion.getX() + newestMotion.getZ() * newestMotion.getZ())) * (double) (180F / (float) Math.PI)));
						float yaw = (float) (MathHelper.atan2(newestMotion.getZ(), newestMotion.getX()) * (double) (180F / (float) Math.PI)) - 90F;

						this.setFireDirection(pitch, yaw);
					}
				}
			}
		}

		if (this.isProjectile() && !this.isInflated()) {
			BlockPos blockpos = this.getPosition();
			BlockState blockstate = this.world.getBlockState(blockpos);
			if (!blockstate.isAir(this.world, blockpos) && !this.noClip) {
				VoxelShape voxelshape = blockstate.getCollisionShape(this.world, blockpos);
				if (!voxelshape.isEmpty()) {
					for (AxisAlignedBB axisalignedbb : voxelshape.toBoundingBoxList()) {
						if (axisalignedbb.offset(blockpos).contains(this.getPositionVec())) {
							this.stuckInBlock = true;
							break;
						}
					}
				}
			}

			if (this.stuckInBlock && !this.noClip) {
				if (!this.world.isRemote && this.stuckInBlockState != blockstate && this.world.hasNoCollisions(this.getBoundingBox().grow(0.06D))) {
					this.disableProjectile();
				}
				this.setMotion(this.getMotion().mul(0.0F, 1.0F, 0.0F));
			} else {
				Vector3d positionVec = new Vector3d(this.getPosX(), this.getPosY(), this.getPosZ());
				Vector3d endVec = positionVec.add(motion);

				RayTraceResult traceResult = this.world.rayTraceBlocks(new RayTraceContext(positionVec, endVec, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, this));
				EntityRayTraceResult entityTraceResult = this.traceEntity(positionVec, endVec);

				if (entityTraceResult != null) {
					traceResult = entityTraceResult;
				}

				if (traceResult.getType() != Type.MISS) {
					this.onSting(traceResult);
					this.isAirBorne = true;
				}
			}
		}
	}

	@Override
	public void readAdditional(CompoundNBT compound) {
		super.readAdditional(compound);

		this.setAttachedHiveSide(Direction.byIndex(compound.getByte("AttachedHiveSide")));
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
			ItemStack newStackToCreate = ItemStack.read(stackToCreate);
			if (!newStackToCreate.isEmpty()) {
				this.setStackToCreate(newStackToCreate);
			}
		}

		this.rotationController = this.getRotationController().read(this, compound.getCompound("Orientation"));
	}

	@Override
	public void writeAdditional(CompoundNBT compound) {
		super.writeAdditional(compound);

		compound.putByte("AttachedHiveSide", (byte) this.getAttachedHiveSide().getIndex());
		compound.putBoolean("FromBottle", this.isFromBottle());
		compound.putBoolean("IsInflated", this.isInflated());

		compound.putInt("TeleportCooldown", this.teleportCooldown);
		compound.putInt("TicksAwayFromHive", this.ticksAwayFromHive);

		if (this.getHivePos() != null) {
			compound.put("HivePos", NBTUtil.writeBlockPos(this.getHivePos()));
		}

		if (this.hasStackToCreate()) {
			compound.put("ItemStackToCreate", this.getStackToCreate().write(new CompoundNBT()));
		}

		if (this.stuckInBlockState != null) {
			compound.put("StuckInBlockState", NBTUtil.writeBlockState(this.stuckInBlockState));
		}

		compound.put("Orientation", this.getRotationController().write(new CompoundNBT()));
	}

	@Nullable
	public BlockPos getHivePos() {
		return this.getDataManager().get(HIVE_POS).orElse(null);
	}

	public void setHivePos(@Nullable BlockPos pos) {
		this.getDataManager().set(HIVE_POS, Optional.ofNullable(pos));
	}

	@Nullable
	public PuffBugHiveTileEntity getHive() {
		BlockPos hivePos = this.getHivePos();
		if (hivePos != null) {
			try {
				TileEntity tileEntity = this.world.getTileEntity(hivePos).getTileEntity();
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
		return this.dataManager.get(ATTACHED_HIVE_SIDE);
	}

	public void setAttachedHiveSide(Direction side) {
		this.dataManager.set(ATTACHED_HIVE_SIDE, side);
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
		return this.dataManager.get(FROM_BOTTLE);
	}

	public void setFromBottle(boolean value) {
		this.dataManager.set(FROM_BOTTLE, value);
	}

	public boolean isInflated() {
		return this.dataManager.get(INFLATED);
	}

	public void setInflated(boolean inflated) {
		this.dataManager.set(INFLATED, inflated);
	}

	public boolean isBoosting() {
		return this.dataManager.get(BOOSTING);
	}

	public void setBoosting(boolean boosting) {
		this.dataManager.set(BOOSTING, boosting);
	}

	public int getColor() {
		return this.dataManager.get(COLOR);
	}

	public void setColor(int color) {
		this.dataManager.set(COLOR, color);
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
		this.dataManager.set(LAUNCH_DIRECTION, Optional.of(new Vector3d(pitch, yaw, 0.0F)));
	}

	public void setFireDirection(float pitch, float yaw) {
		this.dataManager.set(FIRE_DIRECTION, Optional.of(new Vector3d(pitch, yaw, 0.0F)));
	}

	public void removeLaunchDirection() {
		this.dataManager.set(LAUNCH_DIRECTION, Optional.empty());
	}

	public void removeFireDirection() {
		this.dataManager.set(FIRE_DIRECTION, Optional.empty());
	}

	@Nullable
	public Vector3d getLaunchDirection() {
		return this.dataManager.get(LAUNCH_DIRECTION).orElse(null);
	}

	@Nullable
	public Vector3d getFireDirection() {
		return this.dataManager.get(FIRE_DIRECTION).orElse(null);
	}

	public boolean hasLevitation() {
		return this.isPotionActive(Effects.LEVITATION);
	}

	@Override
	public boolean hasNoGravity() {
		return super.hasNoGravity() || this.getAttachedHiveSide() != Direction.UP;
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
			float pitch = this.isBeingRidden() ? 1.0F : this.rotationPitch;
			float xMotion = -MathHelper.sin(this.rotationYaw * ((float) Math.PI / 180F)) * MathHelper.cos(pitch * ((float) Math.PI / 180F));
			float zMotion = MathHelper.cos(this.rotationYaw * ((float) Math.PI / 180F)) * MathHelper.cos(pitch * ((float) Math.PI / 180F));

			Vector3d motion = new Vector3d(xMotion, 0.65F, zMotion).normalize();

			if (this.getAttackTarget() != null && CAN_ANGER.test(this.getAttackTarget())) {
				motion.scale(2.0F);
			}

			this.addVelocity(motion.getX() * (this.getAttribute(Attributes.MOVEMENT_SPEED).getValue() - 0.1F), motion.getY(), motion.getZ() * (this.getAttribute(Attributes.MOVEMENT_SPEED).getValue() - 0.1F));
		} else if (endimation == TELEPORT_TO_ANIMATION) {
			if (!this.world.isRemote) {
				this.playSound(this.getTeleportSound(false), 0.65F, this.getSoundPitch());
			}
		} else if (endimation == FLY_ANIMATION) {
			this.world.playMovingSound(null, this, EESounds.PUFFBUG_LAUNCH.get(), SoundCategory.HOSTILE, 0.25F, this.getRNG().nextFloat() * 0.35F + 0.75F);
		}
	}

	@Override
	public void onEndimationEnd(Endimation endimation) {
		if (endimation == TELEPORT_TO_ANIMATION) {
			if (!this.world.isRemote) {
				NetworkUtil.setPlayingAnimationMessage(this, TELEPORT_FROM_ANIMATION);
				this.playSound(this.getTeleportSound(true), 0.65F, this.getSoundPitch());
			}
		} else if (endimation == POLLINATE_ANIMATION) {
			this.addPotionEffect(new EffectInstance(Effects.LEVITATION, 3000));
			if (this.getPollinationPos() != null) {
				TileEntity te = this.world.getTileEntity(this.getPollinationPos());
				if (te instanceof BolloomBudTileEntity) {
					BolloomBudTileEntity bud = (BolloomBudTileEntity) te;
					if (bud.canBeOpened()) {
						for (BudSide side : BudSide.values()) {
							BlockPos sidePos = side.offsetPosition(this.getPollinationPos());
							if (this.world.getBlockState(sidePos).getCollisionShape(world, this.getPollinationPos()).isEmpty()) {
								this.world.destroyBlock(sidePos, true);
							}
						}

						this.world.setBlockState(this.getPollinationPos(), world.getBlockState(this.getPollinationPos()).with(BolloomBudBlock.OPENED, true));
						bud.startGrowing(this.getRNG(), bud.calculateFruitMaxHeight(), false);
					}
				}
			}
		}
	}

	@Override
	public void travel(Vector3d moveDirection) {
		if (this.isServerWorld() && this.isInflated()) {
			double gravity = this.hasLevitation() ? -0.005D : 0.005D;
			float speed = this.onGround ? 0.01F : 0.025F;

			this.moveRelative(speed, moveDirection);
			this.move(MoverType.SELF, this.getMotion());
			this.setMotion(this.getMotion().scale(0.75D));
			this.setMotion(this.getMotion().subtract(0, gravity, 0));
		} else {
			if (this.stuckInBlock) {
				this.setMotion(0.0F, this.getMotion().getY(), 0.0F);
			}
			boolean noVerticalMotion = this.isInflated() && (this.getAttachedHiveSide() != Direction.UP || this.getDesiredHiveSide() != null);

			if (noVerticalMotion) {
				this.setMotion(this.getMotion().mul(1.0F, 0.0F, 1.0F));
			}

			super.travel(Vector3d.ZERO);

			if (noVerticalMotion) {
				this.setMotion(this.getMotion().mul(1.0F, 0.0F, 1.0F));
			}

			if (this.isProjectile()) {
				this.setMotion(this.getMotion().subtract(0.0F, 0.005F, 0.0F));
			}

			if (this.stuckInBlock) {
				this.setMotion(0.0F, this.getMotion().getY(), 0.0F);
			}
		}
	}

	public void addToHive(PuffBugHiveTileEntity hive) {
		hive.addBugToHive(this);
		this.setHivePos(hive.getPos());
	}

	@Nullable
	public PuffBugHiveTileEntity findNewNearbyHive() {
		BlockPos pos = this.getPosition();
		double xyDistance = 16.0D;
		for (BlockPos blockpos : BlockPos.getAllInBoxMutable(pos.add(-xyDistance, -6.0D, -xyDistance), pos.add(xyDistance, 6.0D, xyDistance))) {
			if (blockpos.withinDistance(this.getPositionVec(), xyDistance)) {
				if (this.world.getBlockState(blockpos).getBlock() == EEBlocks.PUFFBUG_HIVE.get() && this.world.getTileEntity(blockpos) instanceof PuffBugHiveTileEntity) {
					PuffBugHiveTileEntity hive = (PuffBugHiveTileEntity) this.world.getTileEntity(blockpos);
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
			bottle.setDisplayName(this.getCustomName());
		}

		CompoundNBT nbt = bottle.getOrCreateTag();

		if (this.getColor() != -1) {
			nbt.putInt("ColorTag", this.getColor());
		}

		nbt.putInt("TeleportCooldown", this.teleportCooldown);

		if (!this.getActivePotionEffects().isEmpty()) {
			ListNBT listnbt = new ListNBT();

			for (EffectInstance effectinstance : this.getActivePotionEffects()) {
				listnbt.add(effectinstance.write(new CompoundNBT()));
			}

			nbt.put("CustomPotionEffects", listnbt);
		}

		nbt.putBoolean("IsFromBottle", true);
		nbt.putBoolean("IsChild", this.isChild());
	}

	private void keepEffectsAbsorbed() {
		Iterator<Effect> iterator = this.getActivePotionMap().keySet().iterator();
		while (iterator.hasNext()) {
			Effect effect = iterator.next();
			if (effect != Effects.LEVITATION) {
				EffectInstance effectInstance = this.getActivePotionMap().get(effect);
				this.getActivePotionMap().put(effect, new EffectInstance(effect, 1600, effectInstance.getAmplifier(), effectInstance.isAmbient(), effectInstance.doesShowParticles()));
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

		if (hive == null || (hive != null && !hive.canTeleportTo()) || (this.getAttachedHiveSide() == Direction.UP && Math.sqrt(this.getDistanceSq(Vector3d.copyCentered((pos)))) < 5.0F) || this.isEndimationPlaying(TELEPORT_FROM_ANIMATION)) {
			return;
		}

		for (int x = 0; x < 6; x++) {
			for (int y = 0; y < 6; y++) {
				for (int z = 0; z < 6; z++) {
					positions.setPos(pos.add(x, y, z));

					if (this.world.isAirBlock(positions)) {
						avaliablePositions.add(new BlockPos(positions));
					}
				}
			}
		}

		if (!avaliablePositions.isEmpty() && !this.isProjectile()) {
			this.getTeleportController().destination = GenerationUtils.getClosestPositionToPos(avaliablePositions, this.getPosition());
			this.getNavigator().clearPath();
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
			if (entity.attackEntityFrom(DamageSource.causeMobDamage(this).setProjectile(), (float) this.getAttribute(Attributes.ATTACK_DAMAGE).getValue())) {
				this.setInflated(true);
				this.removeFireDirection();
				this.stuckInBlock = false;

				if (!this.getActivePotionEffects().isEmpty() && entity instanceof LivingEntity) {
					for (EffectInstance effects : this.getActivePotionEffects()) {
						((LivingEntity) entity).addPotionEffect(effects);
					}
					this.clearActivePotions();
				}
			}
		} else {
			BlockRayTraceResult blockraytraceresult = (BlockRayTraceResult) result;
			this.stuckInBlockState = this.world.getBlockState(blockraytraceresult.getPos());
			this.stuckInBlock = true;

			Vector3d end = result.getHitVec();
			this.setPosition(end.getX(), end.getY(), end.getZ());
			this.setMotion(Vector3d.ZERO);

			if (!this.world.isRemote) {
				NetworkUtil.setPlayingAnimationMessage(this, LAND_ANIMATION);
			}

			this.world.playMovingSound(null, this, EESounds.PUFFBUG_LAND.get(), SoundCategory.HOSTILE, 0.5F, this.getSoundPitch());
		}
	}

	@Nullable
	private EntityRayTraceResult traceEntity(Vector3d start, Vector3d end) {
		return ProjectileHelper.rayTraceEntities(this.world, this, start, end, this.getBoundingBox().expand(this.getMotion()).grow(0.5F), (result) -> {
			return !result.isSpectator() && result.isAlive() && !(result instanceof PuffBugEntity);
		});
	}

	public void disableProjectile() {
		this.stuckInBlock = false;
		this.setInflated(true);
		this.removeFireDirection();
		this.world.setEntityState(this, (byte) 38);
	}

	public boolean wantsToRest() {
		return this.ticksAwayFromHive >= 3000;
	}

	public boolean isProjectile() {
		return !(this.getFireDirection() == null && this.getLaunchDirection() == null);
	}

	public boolean isAtCorrectRestLocation(Direction side) {
		TileEntity te = side == Direction.DOWN ? this.isChild() ? this.world.getTileEntity(this.getPosition().up(1)) : this.world.getTileEntity(this.getPosition().up(2)) : this.isChild() ? this.world.getTileEntity(this.getPosition().offset(side.getOpposite())) : this.world.getTileEntity(this.getPosition().up(1).offset(side.getOpposite()));
		if (te != this.getHive()) {
			return false;
		}

		BlockPos hivePos = this.getHivePos();
		switch (side) {
			case UP:
				return false;
			case DOWN:
				float yOffsetDown = this.isChild() ? 0.45F : -0.15F;
				return Vector3d.copyCentered((hivePos.down()).add(0.5F, yOffsetDown, 0.5F)).distanceTo(this.getPositionVec()) < 0.25F;
			default:
				float yOffset = this.isChild() ? 0.2F : -0.2F;
				BlockPos sideOffset = hivePos.offset(side);
				return this.world.isAirBlock(sideOffset.up()) && this.world.isAirBlock(sideOffset.down())
						&& Vector3d.copy(sideOffset).add(this.getTeleportController().getOffsetForDirection(side)[0], yOffset, this.getTeleportController().getOffsetForDirection(side)[1]).distanceTo(this.getPositionVec()) < (this.isChild() ? 0.1F : 0.25F);
		}
	}

	@Override
	protected PathNavigator createNavigator(World worldIn) {
		return new EndergeticFlyingPathNavigator(this, worldIn);
	}

	@Override
	public boolean canBreed() {
		return super.canBreed() && this.isInflated();
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void handleStatusUpdate(byte id) {
		if (id == 38) {
			this.stuckInBlock = false;
		} else {
			super.handleStatusUpdate(id);
		}
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public boolean isInRangeToRenderDist(double distance) {
		double boundingBoxLength = this.getBoundingBox().getAverageEdgeLength() * 2.5D;
		if (Double.isNaN(boundingBoxLength)) {
			boundingBoxLength = 1.0D;
		}

		boundingBoxLength = boundingBoxLength * 64.0D * getRenderDistanceWeight();
		return distance < boundingBoxLength * boundingBoxLength;
	}

	@Override
	public float getBrightness() {
		BlockPos blockpos = new BlockPos(this.getPosX(), this.getPosY() + (double) this.getEyeHeight(), this.getPosZ());
		if (this.stuckInBlock && !this.isInflated()) {
			boolean rotationFlag = true;
			float[] rotations = this.getRotationController().getRotations(1.0F);
			Direction horizontalOffset = Direction.fromAngle(rotations[0]).getOpposite();
			Direction verticalOffset = (rotations[1] <= 180.0F && rotations[1] > 100.0F) ? Direction.UP : Direction.DOWN;

			if (rotations[1] >= 80.0F && rotations[1] <= 100.0F) {
				rotationFlag = false;
			}

			return this.world.isAreaLoaded(blockpos, 0) ? this.world.getLight(rotationFlag ? blockpos.offset(horizontalOffset).offset(verticalOffset) : blockpos.offset(horizontalOffset)) : 0;
		}
		return super.getBrightness();
	}

	@Override
	public boolean onLivingFall(float distance, float damageMultiplier) {
		return false;
	}

	@Override
	protected float getStandingEyeHeight(Pose poseIn, EntitySize size) {
		return this.isProjectile() ? 0.0F : size.height * 0.5F;
	}

	@Override
	protected void updatePotionMetadata() {
		super.updatePotionMetadata();
		Collection<EffectInstance> effects = this.getActivePotionEffects();

		if (!effects.isEmpty()) {
			this.setColor(PotionUtils.getPotionColorFromEffectList(effects));
		} else {
			this.setColor(-1);
		}
	}

	@Override
	public ItemStack getPickedResult(RayTraceResult target) {
		return new ItemStack(EEItems.PUFF_BUG_SPAWN_EGG.get());
	}

	@Override
	public CreatureAttribute getCreatureAttribute() {
		return CreatureAttribute.ARTHROPOD;
	}

	@Override
	public boolean isBreedingItem(ItemStack stack) {
		return stack.getItem() == EEBlocks.TALL_POISE_BUSH.get().asItem();
	}

	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		return super.getRenderBoundingBox().grow(12);
	}

	@Override
	public ActionResultType func_230254_b_(PlayerEntity player, Hand hand) {
		ItemStack itemstack = player.getHeldItem(hand);
		Item item = itemstack.getItem();

		if (!this.isAlive() || this.isAggressive()) return ActionResultType.FAIL;

		if (item == Items.GLASS_BOTTLE) {
			this.playSound(SoundEvents.ITEM_BOTTLE_FILL_DRAGONBREATH, 1.0F, 1.0F);
			itemstack.shrink(1);
			ItemStack bottle = new ItemStack(EEItems.PUFFBUG_BOTTLE.get());
			this.setBottleData(bottle);

			if (itemstack.isEmpty()) {
				player.setHeldItem(hand, bottle);
			} else if (!player.inventory.addItemStackToInventory(bottle)) {
				player.dropItem(bottle, false);
			}

			this.remove();
			return ActionResultType.SUCCESS;
		} else if (!this.hasStackToCreate() && this.hasLevitation()) {
			ItemStack newStackToCreate = item == EEItems.BOLLOOM_FRUIT.get() ? new ItemStack(EEBlocks.BOLLOOM_BUD.get()) : this.isBreedingItem(itemstack) ? new ItemStack(EEBlocks.PUFFBUG_HIVE.get()) : null;
			if (newStackToCreate != null) {
				this.setStackToCreate(newStackToCreate);
				this.consumeItemFromStack(player, itemstack);
				return ActionResultType.CONSUME;
			}
			return ActionResultType.PASS;
		} else {
			return super.func_230254_b_(player, hand);
		}
	}

	@Override
	public ILivingEntityData onInitialSpawn(IWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, ILivingEntityData spawnDataIn, CompoundNBT dataTag) {
		Random rng = this.getRNG();

		if (dataTag != null) {
			int age = dataTag.getBoolean("IsChild") ? -24000 : 0;

			this.setGrowingAge(age);
			this.teleportCooldown = dataTag.getInt("TeleportCooldown");
			this.setFromBottle(dataTag.getBoolean("IsFromBottle"));

			if (dataTag.contains("ColorTag", 3)) {
				this.setColor(dataTag.getInt("ColorTag"));
			}

			if (dataTag.contains("CustomPotionEffects")) {
				for (EffectInstance effectinstance : PotionUtils.getFullEffectsFromTag(dataTag)) {
					this.addPotionEffect(effectinstance);
				}
			}
		}

		if (reason == SpawnReason.STRUCTURE) {
			this.ticksAwayFromHive = rng.nextInt(1500) + 1500;

			if (rng.nextFloat() < 0.1F) {
				this.growingAge = -24000;
			}
		} else if (reason == SpawnReason.NATURAL || reason == SpawnReason.SPAWNER) {
			if (rng.nextFloat() < 0.05F) {
				int swarmSize = rng.nextInt(11) + 10;
				Vector3d centeredPos = Vector3d.copyCentered(this.getPosition());
				for (int i = 0; i < swarmSize; i++) {
					Vector3d spawnPos = centeredPos.add(MathUtils.makeNegativeRandomly(rng.nextFloat() * 5.5F, rng), MathUtils.makeNegativeRandomly(rng.nextFloat() * 2.0F, rng), MathUtils.makeNegativeRandomly(rng.nextFloat() * 5.5F, rng));

					if (this.world.isAirBlock(new BlockPos(spawnPos))) {
						PuffBugEntity swarmChild = EEEntities.PUFF_BUG.get().create(this.world);
						swarmChild.setLocationAndAngles(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ(), 0.0F, 0.0F);
						swarmChild.onInitialSpawn(this.world, this.world.getDifficultyForLocation(new BlockPos(spawnPos)), SpawnReason.EVENT, null, null);
						swarmChild.setGrowingAge(-24000);

						this.world.addEntity(swarmChild);
					}
				}
			}
		}

		return super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
	}

	@Override
	public boolean canDespawn(double distanceToClosestPlayer) {
		return this.getHive() == null && !this.isFromBottle() && !this.hasCustomName();
	}

	@Override
	public boolean preventDespawn() {
		return this.isFromBottle();
	}

	@Override
	protected void collideWithEntity(Entity entity) {
		if (!this.isInflated() && entity instanceof PuffBugEntity) {
			if (entity instanceof PuffBugEntity) {
				return;
			} else {
				if (this.isProjectile()) {
					if (entity.attackEntityFrom(DamageSource.causeMobDamage(this).setProjectile(), 5.0F)) {
						this.setInflated(true);
						this.removeFireDirection();
						this.stuckInBlock = false;

						if (!this.getActivePotionEffects().isEmpty() && entity instanceof LivingEntity) {
							for (EffectInstance effects : this.getActivePotionEffects()) {
								((LivingEntity) entity).addPotionEffect(effects);
							}
							this.clearActivePotions();
						}
					}
				}
			}
		}
		super.collideWithEntity(entity);
	}

	@Override
	public EntitySize getSize(Pose pose) {
		return this.isProjectile() ? this.isChild() ? PROJECTILE_SIZE_CHILD : PROJECTILE_SIZE : super.getSize(pose);
	}

	@Override
	public boolean isInvulnerableTo(DamageSource source) {
		return ((this.isProjectile() || this.isPassenger()) && (source == DamageSource.IN_WALL || source == DamageSource.FLY_INTO_WALL || source == DamageSource.CRAMMING)) || super.isInvulnerableTo(source);
	}

	@Override
	public boolean isOnLadder() {
		return false;
	}

	@Override
	protected boolean canTriggerWalking() {
		return false;
	}

	@Override
	public AgeableEntity createChild(AgeableEntity ageable) {
		return EEEntities.PUFF_BUG.get().create(this.world);
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
				this.puffbug.setAIMoveSpeed(0.0F);
				return;
			}

			if (this.action == MovementController.Action.MOVE_TO && !this.puffbug.getNavigator().noPath()) {
				double xDistance = this.posX - this.puffbug.getPosX();
				double yDistance = this.posY - this.puffbug.getPosY();
				double zDistance = this.posZ - this.puffbug.getPosZ();
				double totalDistance = (double) MathHelper.sqrt(xDistance * xDistance + yDistance * yDistance + zDistance * zDistance);

				double verticalVelocity = yDistance / totalDistance;

				float angle = (float) (MathHelper.atan2(zDistance, xDistance) * (180F / Math.PI)) - 90.0F;

				this.puffbug.rotationYaw = this.limitAngle(this.puffbug.rotationYaw, angle, 20.0F);
				this.puffbug.renderYawOffset = this.puffbug.rotationYaw;

				float speed = (float) (this.speed * this.puffbug.getAttribute(Attributes.FLYING_SPEED).getValue());

				if (verticalVelocity < 0.0F) {
					this.puffbug.setAIMoveSpeed(MathHelper.lerp(0.125F, this.puffbug.getAIMoveSpeed(), speed));
					this.puffbug.setMotion(this.puffbug.getMotion().add(0.0D, (double) this.puffbug.getAIMoveSpeed() * verticalVelocity * 0.05D, 0.0D));
					this.puffbug.setBoosting(false);
				} else {
					this.puffbug.setBoosting(true);
				}
			} else {
				this.puffbug.setAIMoveSpeed(0.0F);
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
			this.world = puffbug.world;
		}

		public void processTeleportation() {
			if (!this.hasNoDestination() && !this.world.isRemote) {
				this.puffbug.getNavigator().clearPath();
				NetworkUtil.setPlayingAnimationMessage(this.puffbug, PuffBugEntity.TELEPORT_TO_ANIMATION);

				this.puffbug.teleportCooldown = this.puffbug.getRNG().nextInt(300) + 1200;
			}
		}

		protected void bringToDestination() {
			if (!this.world.isRemote) {
				Direction side = this.puffbug.getTeleportHiveSide();

				float xOffset = side == null || side == Direction.DOWN ? 0.5F : this.getOffsetForDirection(side)[0];
				float yOffset = side == Direction.DOWN ? this.puffbug.isChild() ? 0.45F : -0.15F : this.puffbug.isChild() ? 0.2F : -0.2F;
				float zOffset = side == null || side == Direction.DOWN ? 0.5F : this.getOffsetForDirection(side)[1];

				if (side == null) {
					NetworkUtil.teleportEntity(this.puffbug, this.destination.getX() + 0.5F, this.destination.getY() + 0.5F, this.destination.getZ() + 0.5F);
				} else {
					NetworkUtil.teleportEntity(this.puffbug, this.destination.getX() + xOffset, this.destination.getY() + yOffset, this.destination.getZ() + zOffset);
				}

				this.destination = null;

				this.puffbug.getNavigator().clearPath();
				this.puffbug.setMotion(Vector3d.ZERO);

				if (side != null) {
					this.puffbug.setDesiredHiveSide(side);
					this.puffbug.setTeleportHiveSide(null);
				}
			}
		}

		@Nullable
		public boolean tryToCreateDesinationTo(BlockPos pos, @Nullable Direction direction) {
			boolean directionFlag = direction != null ? this.world.isAirBlock(pos.offset(direction)) : true;

			if (direction != null && direction != Direction.DOWN) {
				if (!this.world.isAirBlock(pos.up()) || !this.world.isAirBlock(pos.down())) {
					directionFlag = false;
				}
			}

			if (this.puffbug.ticksExisted > 5 && this.world.isAirBlock(pos) && directionFlag && this.hasNoDestination()) {
				if (this.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(pos)).isEmpty()) {
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
					return this.puffbug.isChild() ? new float[]{0.5F, 0.85F} : new float[]{0.5F, 0.75F};
				case SOUTH:
					return this.puffbug.isChild() ? new float[]{0.5F, 0.15F} : new float[]{0.5F, 0.25F};
				case EAST:
					return this.puffbug.isChild() ? new float[]{0.15F, 0.5F} : new float[]{0.25F, 0.5F};
				case WEST:
					return this.puffbug.isChild() ? new float[]{0.85F, 0.5F} : new float[]{0.75F, 0.5F};
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

			if (!this.puffbug.world.isRemote) {
				EndergeticExpansion.CHANNEL.send(PacketDistributor.TRACKING_ENTITY.with(() -> this.puffbug), new RotateMessage(this.puffbug.getEntityId(), tickLength, yaw, pitch, roll));
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