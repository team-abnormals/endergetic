package com.teamabnormals.endergetic.common.entity.purpoid;

import com.teamabnormals.blueprint.core.endimator.Endimatable;
import com.teamabnormals.blueprint.core.endimator.PlayableEndimation;
import com.teamabnormals.blueprint.core.util.MathUtil;
import com.teamabnormals.blueprint.core.util.NetworkUtil;
import com.teamabnormals.endergetic.api.entity.pathfinding.EndergeticFlyingPathNavigator;
import com.teamabnormals.endergetic.client.particle.data.CorrockCrownParticleData;
import com.teamabnormals.endergetic.common.entity.purpoid.ai.*;
import com.teamabnormals.endergetic.core.EndergeticExpansion;
import com.teamabnormals.endergetic.core.registry.EEParticleTypes;
import com.teamabnormals.endergetic.core.other.EEDataSerializers;
import com.teamabnormals.endergetic.core.other.EEPlayableEndimations;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.game.ClientboundSetPassengersPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.*;

public class Purpoid extends PathfinderMob implements Endimatable {
	private static final EntityDataAccessor<PurpoidSize> SIZE = SynchedEntityData.defineId(Purpoid.class, EEDataSerializers.PURPOID_SIZE);
	private static final EntityDataAccessor<Integer> BOOSTING_TICKS = SynchedEntityData.defineId(Purpoid.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Integer> STUN_TIMER = SynchedEntityData.defineId(Purpoid.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Boolean> APPLY_ROTATION_SNAPS = SynchedEntityData.defineId(Purpoid.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Optional<BlockPos>> RESTING_POS = SynchedEntityData.defineId(Purpoid.class, EntityDataSerializers.OPTIONAL_BLOCK_POS);
	private static final EntityDataAccessor<Direction> RESTING_SIDE = SynchedEntityData.defineId(Purpoid.class, EntityDataSerializers.DIRECTION);
	private static final EntityDataAccessor<Integer> SHIELDED_MOMMY_ID = SynchedEntityData.defineId(Purpoid.class, EntityDataSerializers.INT);
	private static final ResourceLocation PURP_LOOT_TABLE = new ResourceLocation(EndergeticExpansion.MOD_ID, "entities/purp");
	private static final ResourceLocation PURPAZOID_LOOT_TABLE = new ResourceLocation(EndergeticExpansion.MOD_ID, "entities/purpazoid");
	private final TeleportController teleportController = new TeleportController();
	private int growingAge;
	private int teleportCooldown;
	private int restCooldown;
	private int despawnTimer;
	private float restOntoProgressO, restOntoProgress;
	private boolean wantsToFlee;
	private Vec3 prevPull = Vec3.ZERO, pull = Vec3.ZERO;
	@Nullable
	public BlockPos forcedRelativeTeleportingPos;
	public final Set<UUID> revengeTargets = new HashSet<>();
	private final ArrayList<UUID> shielders = new ArrayList<>(3);
	private PurpoidTelefragGoal telefragGoal;
	private PurpoidMoveNearTargetGoal moveNearTargetGoal;
	private PurpoidAttackGoal attackGoal;
	private PurpoidRestGoal restGoal;
	private PurpoidTeleportToFlowerGoal teleportToFlowerGoal;
	private PurpTeleportToPlantGoal teleportToPlantGoal;
	private PurpFollowPurpoidsGoal followPurpoidsGoal;
	private final PurpazoidSquirtPurpsGoal squirtPurpsGoal = new PurpazoidSquirtPurpsGoal(this);

	public Purpoid(EntityType<? extends PathfinderMob> type, Level world) {
		super(type, world);
		this.resetTeleportCooldown();
		this.resetRestCooldown();
		this.moveControl = new PurpoidMoveController(this);
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(SIZE, PurpoidSize.NORMAL);
		this.entityData.define(BOOSTING_TICKS, 0);
		this.entityData.define(STUN_TIMER, 0);
		this.entityData.define(APPLY_ROTATION_SNAPS, true);
		this.entityData.define(RESTING_POS, Optional.empty());
		this.entityData.define(RESTING_SIDE, Direction.DOWN);
		this.entityData.define(SHIELDED_MOMMY_ID, -1);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new PurpoidStunnedGoal(this));
		this.goalSelector.addGoal(1, this.telefragGoal = new PurpoidTelefragGoal(this));
		this.goalSelector.addGoal(2, this.moveNearTargetGoal = new PurpoidMoveNearTargetGoal(this));
		this.goalSelector.addGoal(2, this.attackGoal = new PurpoidAttackGoal(this));
		this.goalSelector.addGoal(3, this.restGoal = new PurpoidRestGoal(this));
		this.goalSelector.addGoal(4, this.teleportToFlowerGoal = new PurpoidTeleportToFlowerGoal(this));
		this.goalSelector.addGoal(4, this.teleportToPlantGoal = new PurpTeleportToPlantGoal(this));
		this.goalSelector.addGoal(4, new PurpoidRandomTeleportGoal(this));
		this.goalSelector.addGoal(5, this.followPurpoidsGoal = new PurpFollowPurpoidsGoal(this));
		this.goalSelector.addGoal(6, new PurpoidMoveRandomGoal(this));

		this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
	}

	@Override
	public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
		if (SIZE.equals(key)) this.refreshDimensions();
		super.onSyncedDataUpdated(key);
	}

	public static AttributeSupplier.Builder registerAttributes() {
		return Mob.createMobAttributes()
				.add(Attributes.ATTACK_DAMAGE, 5.0F)
				.add(Attributes.MOVEMENT_SPEED, 0.2F)
				.add(Attributes.MAX_HEALTH, 25.0F)
				.add(Attributes.FOLLOW_RANGE, 32.0F)
				.add(Attributes.KNOCKBACK_RESISTANCE, 0.2F);
	}

	@Override
	public void tick() {
		this.noPhysics = this.getSize() == PurpoidSize.NORMAL && this.restOntoProgress >= 0.6F;
		super.tick();
		this.noPhysics = false;
		Level level = this.level();
		if (level.isClientSide) {
			this.restOntoProgressO = this.restOntoProgress;
			this.prevPull = this.pull;
			Vec3 pos = this.position();
			this.pull = pos.add(this.pull.subtract(pos).normalize().scale(0.25F));

			if ((this.isBoosting() || this.isEndimationPlaying(EEPlayableEndimations.PURPOID_SQUIRT_ATTACK) || this.getStunTimer() > 0) && level.getGameTime() % 4 == 0) {
				double dy = this.pull.y() - pos.y();
				CorrockCrownParticleData particleData = this.createParticleData();
				RandomSource random = this.getRandom();
				for (int i = 0; i < 2; i++) {
					level.addParticle(particleData, this.getRandomX(0.5D), this.getY() + this.getEyeHeight(), this.getRandomZ(0.5D), MathUtil.makeNegativeRandomly(random.nextDouble() * 0.05F, random), dy * random.nextDouble(), MathUtil.makeNegativeRandomly(random.nextDouble() * 0.05F, random));
				}
			}

			if (!this.isDeadOrDying() && (this.isEndimationPlaying(EEPlayableEndimations.PURPOID_TELEPORT_TO) || this.isEndimationPlaying(EEPlayableEndimations.PURPOID_FAST_TELEPORT_TO) || this.isEndimationPlaying(EEPlayableEndimations.PURPOID_TELEPORT_FROM) || this.isPassenger() || this.getStunTimer() > 0) && this.shouldApplyRotationSnaps()) {
				pos = pos.subtract(0.0F, 1.0F, 0.0F);
				this.pull = pos.add(this.pull.subtract(pos).normalize().scale(0.1F));
			} else {
				float restOntoProgressO = this.restOntoProgressO;
				if (restOntoProgressO > 0.0F) {
					Direction restingSide = this.getRestingSide();
					if (restingSide != Direction.DOWN) {
						restOntoProgressO *= restOntoProgressO;
						pos = pos.subtract(restOntoProgressO * restingSide.getStepX(), restOntoProgressO * restingSide.getStepY(), restOntoProgressO * restingSide.getStepZ());
						this.pull = pos.add(this.pull.subtract(pos).normalize().scale(0.1F));
					}
				}
			}

			int animationTick = this.getAnimationTick();
			if ((this.isEndimationPlaying(EEPlayableEndimations.PURPOID_TELEPORT_TO) || this.isEndimationPlaying(EEPlayableEndimations.PURPOID_FAST_TELEPORT_TO)) && animationTick == 7 || this.isEndimationPlaying(EEPlayableEndimations.PURPOID_TELEPORT_FROM) && animationTick == 4 || this.isEndimationPlaying(EEPlayableEndimations.PURPOID_TELEFRAG) && animationTick == 2) {
				this.burstParticles();
			}
		} else {
			if (this.isAlive()) {
				if (this.getSize() != PurpoidSize.PURPAZOID) {
					int age = this.growingAge;
					if (age < 0) {
						this.updateAge(++age);
					} else if (age > 0) {
						this.updateAge(--age);
					}

					if (this.despawnTimer > 0 && --this.despawnTimer == 0) {
						level.broadcastEntityEvent(this, (byte) 1);
						this.discard();
						return;
					}
				} else {
					int tickCount = this.tickCount;

					if (tickCount >= 40) {
						var shieldersIterator = this.shielders.iterator();
						int id = this.getId();
						double x = this.getX();
						double z = this.getZ();
						while (shieldersIterator.hasNext()) {
							if (((ServerLevel) level).getEntity(shieldersIterator.next()) instanceof Purpoid purpoid) {
								int shieldedMommyID = purpoid.getIDOfShieldedMommy();
								boolean purpDoesNotShield = shieldedMommyID < 0;
								if ((purpDoesNotShield || shieldedMommyID == id) && purpoid.isAlive() && purpoid.isBaby() && Mth.square(purpoid.getX() - x) + Mth.square(purpoid.getZ() - z) <= 4096.0D) {
									if (purpDoesNotShield) purpoid.setShieldedMommyId(id);
									continue;
								} else {
									purpoid.setShieldedMommyId(-1);
								}
							}
							shieldersIterator.remove();
						}

						int shieldedMommyID = this.getIDOfShieldedMommy();
						if (shieldedMommyID >= 0) {
							Entity shieldedMommy = level.getEntity(shieldedMommyID);
							if (!(shieldedMommy instanceof Purpoid mommy) || !mommy.shielders.contains(this.uuid)) {
								this.setShieldedMommyId(-1);
							}
						}
					}

					if (tickCount % 20 == 0) {
						AABB searchBox = this.getBoundingBox().inflate(32.0F);
						var nearbyRevengeTargets = level.getEntitiesOfClass(LivingEntity.class, searchBox, entity -> TargetingConditions.DEFAULT.test(this, entity) && this.revengeTargets.contains(entity.getUUID()));
						if (!nearbyRevengeTargets.isEmpty()) {
							var nearbyPassivePurps = level.getEntitiesOfClass(Purpoid.class, searchBox, purpoid -> {
								if (!purpoid.isBaby()) return false;
								LivingEntity purpoidTarget = purpoid.getTarget();
								return (purpoidTarget == null || !purpoidTarget.isAlive()) && !purpoid.isResting();
							});
							RandomSource random = this.getRandom();
							for (Purpoid purpoid : nearbyPassivePurps) {
								purpoid.setTarget(nearbyRevengeTargets.get(random.nextInt(nearbyRevengeTargets.size())));
							}
						}
					}
				}
			}

			if (this.wantsToFlee && this.isNoEndimationPlaying() && !this.getTeleportController().isTeleporting()) {
				BlockPos pos = this.blockPosition();
				int purpoidBlockX = pos.getX();
				int purpoidBlockZ = pos.getZ();
				int levelHeight = level.getHeight();
				RandomSource random = this.getRandom();
				EntityDimensions size = this.getDimensions(this.getPose());
				List<BlockPos> possiblePositions = new ArrayList<>();
				BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
				for (int relX = -16; relX <= 16; relX++) {
					int x = purpoidBlockX + relX;
					double xMiddle = x + 0.5D;
					for (int relZ = -16; relZ <= 16; relZ++) {
						int z = purpoidBlockZ + relZ;
						int heightAtXZ = level.getHeight(Heightmap.Types.MOTION_BLOCKING, x, z);
						if (!Block.canSupportRigidBlock(level, mutableBlockPos.set(x, heightAtXZ - 1, z))) continue;
						double zMiddle = z + 0.5D;
						int minColumnHeight = Math.min(heightAtXZ + 4, levelHeight);
						int y = Math.min(minColumnHeight + 4 + random.nextInt(9), levelHeight);
						while (y >= minColumnHeight) {
							if (level.noCollision(size.makeBoundingBox(xMiddle, y, zMiddle))) {
								possiblePositions.add(new BlockPos(x, y, z));
								break;
							}
							y--;
						}
					}
				}
				if (!possiblePositions.isEmpty()) {
					this.teleportController.beginTeleportation(this, possiblePositions.get(random.nextInt(possiblePositions.size())), false);
					this.wantsToFlee = false;
				} else if (this.tryToTeleportRandomly(0, 24, 32)) {
					this.wantsToFlee = false;
				}
			}

			if (this.hasTeleportCooldown()) this.teleportCooldown--;
			if (this.hasRestCooldown()) this.restCooldown--;

			if (this.isBoosting()) {
				this.setBoostingTicks(this.getBoostingTicks() - 1);
			} else if (!this.isPassenger() && this.hasTeleportCooldown() && !this.isResting() && this.random.nextFloat() <= 0.001F) {
				this.setBoostingTicks(this.random.nextInt(81) + 80);
			}

			int stunTimer = this.getStunTimer();
			if (stunTimer > 0) this.setStunTimer(stunTimer - 1);

			this.teleportController.tick(this);
		}

		BlockPos restingPos = this.getRestingPos();
		if (restingPos != null && !(this.isEndimationPlaying(EEPlayableEndimations.PURPOID_TELEPORT_TO) || this.isEndimationPlaying(EEPlayableEndimations.PURPOID_TELEPORT_FROM))) {
			Direction side = this.getRestingSide();
			Direction.Axis axis = side.getAxis();
			double chosenTestingPos = axis.choose(restingPos.getX(), restingPos.getY(), restingPos.getZ());
			if (side.getAxis().getPlane() == Direction.Plane.HORIZONTAL && side.getAxisDirection() == Direction.AxisDirection.POSITIVE) {
				chosenTestingPos += 1.0D;
			}
			this.restOntoProgress = (float) (1.0F - Mth.clamp(Math.abs(chosenTestingPos - axis.choose(this.getX(), this.getY(), this.getZ())) / 3.0D, 0.0D, 1.0D));
		} else if (this.getStunTimer() > 0) {
			this.restOntoProgress = Math.min(this.restOntoProgress + 0.025F, 1.0F);
		} else {
			this.restOntoProgress = Math.max(this.restOntoProgress - 0.025F, 0.0F);
		}

		if (this.isDeadOrDying()) {
			if (!this.isEndimationPlaying(EEPlayableEndimations.PURPOID_DEATH) && !level.isClientSide) {
				NetworkUtil.setPlayingAnimation(this, EEPlayableEndimations.PURPOID_DEATH);
			}
			if (++this.deathTime >= 20) {
				if (!level.isClientSide) {
					this.discard();
				} else {
					CorrockCrownParticleData particleData = this.createParticleData();
					RandomSource random = this.getRandom();
					for (int i = 0; i < 12; ++i) {
						level.addParticle(particleData, this.getRandomX(1.0D), this.getRandomY(), this.getRandomZ(1.0D), MathUtil.makeNegativeRandomly(random.nextFloat() * 0.25F, random), (random.nextFloat() - random.nextFloat()) * 0.3F + 0.1F, MathUtil.makeNegativeRandomly(random.nextFloat() * 0.25F, random));
					}
					for (int i = 0; i < 20; ++i) {
						level.addParticle(ParticleTypes.POOF, this.getRandomX(1.0D), this.getRandomY(), this.getRandomZ(1.0D), this.random.nextGaussian() * 0.02D, this.random.nextGaussian() * 0.02D, this.random.nextGaussian() * 0.02D);
					}
				}
			}
		}
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		compound.putInt("Size", this.getSize().ordinal());
		compound.putInt("Age", this.growingAge);
		compound.putInt("BoostingTicks", this.getBoostingTicks());
		compound.putInt("StunTimer", this.getStunTimer());
		compound.putInt("TeleportCooldown", this.teleportCooldown);
		compound.putInt("RestCooldown", this.restCooldown);
		compound.putInt("DespawnTimer", this.despawnTimer);
		var revengeTargets = this.revengeTargets;
		if (!revengeTargets.isEmpty()) {
			ListTag revengeTargetsTag = new ListTag();
			for (UUID uuid : revengeTargets) revengeTargetsTag.add(NbtUtils.createUUID(uuid));
			compound.put("RevengeTargets", revengeTargetsTag);
		}
		var shielders = this.shielders;
		if (!shielders.isEmpty()) {
			ListTag shieldersTag = new ListTag();
			for (UUID uuid : shielders) shieldersTag.add(NbtUtils.createUUID(uuid));
			compound.put("Shielders", shieldersTag);
		}
		if (this.isResting()) {
			compound.put("RestingPos", NbtUtils.writeBlockPos(this.getRestingPos()));
		}
		compound.putInt("RestingSide", this.getRestingSide().get3DDataValue());
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		this.setSize(PurpoidSize.values()[Mth.clamp(compound.getInt("Size"), 0, 2)], false);
		this.updateAge(compound.getInt("Age"));
		this.setBoostingTicks(Math.max(0, compound.getInt("BoostingTicks")));
		this.setStunTimer(Math.max(0, compound.getInt("StunTimer")));
		if (compound.contains("TeleportCooldown", 3)) {
			this.teleportCooldown = Math.max(0, compound.getInt("TeleportCooldown"));
		}
		if (compound.contains("RestCooldown", 3)) {
			this.restCooldown = Math.max(0, compound.getInt("RestCooldown"));
		}
		this.despawnTimer = Math.max(0, compound.getInt("DespawnTimer"));
		var revengeTargets = this.revengeTargets;
		for (Tag tag : compound.getList("RevengeTargets", 11)) revengeTargets.add(NbtUtils.loadUUID(tag));
		var shielders = this.shielders;
		for (Tag tag : compound.getList("Shielders", 11)) shielders.add(NbtUtils.loadUUID(tag));
		if (compound.contains("RestingPos", 10)) {
			this.setRestingPos(NbtUtils.readBlockPos(compound.getCompound("RestingPos")));
		}
		this.setRestingSide(Direction.from3DDataValue(compound.getInt("RestingSide")));
	}

	public void setSize(PurpoidSize size, boolean updateHealth) {
		this.entityData.set(SIZE, size);
		this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(25.0F * size.getHealthMultiplier());
		if (updateHealth) this.setHealth(this.getMaxHealth());
		GoalSelector goalSelector = this.goalSelector;
		if (size == PurpoidSize.PURPAZOID) {
			goalSelector.addGoal(0, this.squirtPurpsGoal);
			goalSelector.removeGoal(this.moveNearTargetGoal);
			goalSelector.removeGoal(this.attackGoal);
			goalSelector.removeGoal(this.restGoal);
			goalSelector.removeGoal(this.teleportToFlowerGoal);
			goalSelector.removeGoal(this.teleportToPlantGoal);
			goalSelector.removeGoal(this.telefragGoal);
			goalSelector.removeGoal(this.followPurpoidsGoal);
		} else {
			if (size == PurpoidSize.NORMAL) {
				goalSelector.addGoal(1, this.telefragGoal);
				goalSelector.addGoal(4, this.teleportToFlowerGoal);
				goalSelector.removeGoal(this.teleportToPlantGoal);
				goalSelector.removeGoal(this.followPurpoidsGoal);
			} else {
				goalSelector.addGoal(4, this.teleportToPlantGoal);
				goalSelector.addGoal(5, this.followPurpoidsGoal);
				goalSelector.removeGoal(this.telefragGoal);
				goalSelector.removeGoal(this.teleportToFlowerGoal);
			}
			goalSelector.addGoal(2, this.moveNearTargetGoal);
			goalSelector.addGoal(2, this.attackGoal);
			goalSelector.addGoal(3, this.restGoal);
			goalSelector.removeGoal(this.squirtPurpsGoal);
		}
		this.xpReward = (int) (2 * size.getScale());
	}

	public PurpoidSize getSize() {
		return this.entityData.get(SIZE);
	}

	public void setBoostingTicks(int boostingTicks) {
		this.entityData.set(BOOSTING_TICKS, boostingTicks);
	}

	public int getBoostingTicks() {
		return this.entityData.get(BOOSTING_TICKS);
	}

	public void setStunTimer(int stunTimer) {
		this.entityData.set(STUN_TIMER, stunTimer);
	}

	public int getStunTimer() {
		return this.entityData.get(STUN_TIMER);
	}

	public void setShouldApplyRotationSnaps(boolean shouldApplyRotationSnaps) {
		this.entityData.set(APPLY_ROTATION_SNAPS, shouldApplyRotationSnaps);
	}

	public boolean shouldApplyRotationSnaps() {
		return this.entityData.get(APPLY_ROTATION_SNAPS);
	}

	public void updateAge(int growingAge) {
		int prevAge = this.growingAge;
		this.growingAge = growingAge;
		if (prevAge < 0 && growingAge >= 0 || prevAge >= 0 && growingAge < 0) {
			this.setSize(growingAge < 0 ? PurpoidSize.PURP : PurpoidSize.NORMAL, true);
		}
	}

	public boolean isBoosting() {
		return this.getBoostingTicks() > 0;
	}

	public void resetTeleportCooldown() {
		this.teleportCooldown = this.getRandom().nextInt(2801) + 200;
	}

	public boolean hasTeleportCooldown() {
		return this.teleportCooldown > 0;
	}

	public void allowRest() {
		this.restCooldown = 0;
	}

	public void resetRestCooldown() {
		this.restCooldown = this.getRandom().nextInt(1001) + 600;
	}

	public boolean hasRestCooldown() {
		return this.restCooldown > 0;
	}

	public void randomizeDespawnTimer() {
		this.despawnTimer = 1000 + this.random.nextInt(101);
	}

	public boolean wantsToFlee() {
		return this.wantsToFlee;
	}

	public float getRestOntoProgress() {
		return this.restOntoProgress;
	}

	public float getRestOntoAnimationProgress(float partialTicks) {
		return Mth.lerp(partialTicks, this.restOntoProgressO, this.restOntoProgress);
	}

	public void updatePull(Vec3 pos) {
		this.prevPull = this.pull = pos.subtract(0.0F, 1.0F, 0.0F);
	}

	public Vec3 getPull(float partialTicks) {
		return MathUtil.lerp(this.prevPull, this.pull, partialTicks);
	}

	@Nullable
	public Purpoid getLeader() {
		return this.followPurpoidsGoal.leader;
	}

	public TeleportController getTeleportController() {
		return this.teleportController;
	}

	public void setRestingPos(@Nullable BlockPos pos) {
		this.entityData.set(RESTING_POS, Optional.ofNullable(pos));
	}

	@Nullable
	public BlockPos getRestingPos() {
		return this.entityData.get(RESTING_POS).orElse(null);
	}

	public boolean isResting() {
		return this.getRestingPos() != null;
	}

	public void setRestingSide(Direction side) {
		this.entityData.set(RESTING_SIDE, side);
	}

	public Direction getRestingSide() {
		return this.entityData.get(RESTING_SIDE);
	}

	public void setShieldedMommyId(int id) {
		this.entityData.set(SHIELDED_MOMMY_ID, id);
	}

	public int getIDOfShieldedMommy() {
		return this.entityData.get(SHIELDED_MOMMY_ID);
	}

	public void addShielder(Purpoid shielder) {
		this.shielders.add(shielder.uuid);
		shielder.setShieldedMommyId(this.getId());
	}

	public boolean needsMoreShielders() {
		return this.shielders.size() < 3;
	}

	@Override
	public boolean isBaby() {
		return this.getSize() == PurpoidSize.PURP;
	}

	@Override
	public float getScale() {
		return this.getSize().getScale();
	}

	private CorrockCrownParticleData createParticleData() {
		return new CorrockCrownParticleData(EEParticleTypes.END_CROWN.get(), false, 0.2F * this.getSize().getScale());
	}

	@Override
	public void travel(Vec3 travelVector) {
		if (this.isEffectiveAi()) {
			this.moveRelative(0.1F, travelVector);
			this.move(MoverType.SELF, this.getDeltaMovement());
			this.setDeltaMovement(this.getDeltaMovement().scale(0.8D));
		} else {
			super.travel(travelVector);
		}
	}

	@Nullable
	@Override
	@SuppressWarnings("deprecation")
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag dataTag) {
		RandomSource random = this.random;
		if (random.nextFloat() < 0.5F) {
			this.updateAge(-24000);
		} else if (random.nextFloat() < 0.005F) {
			this.setSize(PurpoidSize.PURPAZOID, true);
		} else if (random.nextFloat() < 0.75F) {
			this.allowRest();
		}
		return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnData, dataTag);
	}

	@Override
	public void calculateEntityAnimation(boolean p_233629_2_) {
		super.calculateEntityAnimation(true);
	}

	@Override
	public double getMyRidingOffset() {
		Entity ridingEntity = this.getVehicle();
		if (ridingEntity != null) {
			return ridingEntity.getBoundingBox().maxY - (ridingEntity.getY() + ridingEntity.getPassengersRidingOffset());
		}
		return super.getMyRidingOffset();
	}

	@Override
	public boolean startRiding(Entity entity, boolean force) {
		boolean riding = super.startRiding(entity, force);
		if (entity instanceof ServerPlayer) {
			((ServerPlayer) entity).connection.send(new ClientboundSetPassengersPacket(entity));
		}
		return riding;
	}

	@Override
	public void stopRiding() {
		Entity entity = this.getVehicle();
		super.stopRiding();
		if (entity instanceof ServerPlayer) {
			((ServerPlayer) entity).connection.send(new ClientboundSetPassengersPacket(entity));
		}
	}

	@Override
	protected InteractionResult mobInteract(Player player, InteractionHand hand) {
		if (player.getMainHandItem().isEmpty() && !this.isPassenger() && this.getSize() == PurpoidSize.NORMAL && this.isAlive()) {
			this.startRiding(player);
			this.setTarget(player);
			return InteractionResult.SUCCESS;
		}
		return super.mobInteract(player, hand);
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		if (!this.level().isClientSide) {
			Entity ridingEntity = this.getVehicle();
			if (ridingEntity != null && source.getEntity() == ridingEntity) {
				this.stopRiding();
				return super.hurt(source, amount);
			}
			if (this.getSize() == PurpoidSize.PURPAZOID) {
				this.wantsToFlee = true;
				this.setStunTimer(0);
				if (source.isIndirect() && this.isNoEndimationPlaying() && !this.getTeleportController().isTeleporting() && this.tryToTeleportRandomly(2, 16, 12)) {
					this.wantsToFlee = false;
					return true;
				}
				var shielders = this.shielders;
				if (!shielders.isEmpty() && !source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
					UUID randomShielderUUID = shielders.get(this.random.nextInt(shielders.size()));
					if (randomShielderUUID != null && ((ServerLevel) this.level()).getEntity(randomShielderUUID) instanceof Purpoid shielder) {
						shielder.hurt(source, amount);
						return false;
					}
				}
				return super.hurt(source, amount);
			} else if (this.isNoEndimationPlaying() && !this.getTeleportController().isTeleporting()) {
				if (source.isIndirect()) {
					if (this.tryToTeleportRandomly(2, 16, 12)) return true;
				} else if (!(source.getEntity() instanceof LivingEntity)) {
					this.tryToTeleportRandomly(2, 16, 4);
				}
			}
		}
		return super.hurt(source, amount);
	}

	@Override
	public void setTarget(@Nullable LivingEntity entity) {
		super.setTarget(entity);
		//Target may change when the LivingSetAttackTargetEvent event gets fired, so we must check after
		LivingEntity target = this.getTarget();
		if (target != null) {
			this.revengeTargets.add(target.getUUID());
		}
	}

	@Override
	public PathNavigation createNavigation(Level world) {
		return new EndergeticFlyingPathNavigator(this, world);
	}

	@Override
	public void handleEntityEvent(byte id) {
		if (id == 1) {
			this.burstParticles();
			Level level = this.level();
			RandomSource random = this.random;
			for (int i = 0; i < 5; ++i) {
				level.addParticle(ParticleTypes.POOF, this.getRandomX(1.0D), this.getRandomY(), this.getRandomZ(1.0D), random.nextGaussian() * 0.02D, random.nextGaussian() * 0.02D, random.nextGaussian() * 0.02D);
			}
		} else {
			super.handleEntityEvent(id);
		}
	}

	@Override
	public boolean causeFallDamage(float distance, float damageMultiplier, DamageSource source) {
		return false;
	}

	@Override
	protected void tickDeath() {
	}

	@Override
	protected void checkFallDamage(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
	}

	@Override
	public boolean onClimbable() {
		return false;
	}

	@Override
	protected float getStandingEyeHeight(Pose poseIn, EntityDimensions size) {
		return size.height * 0.5F;
	}

	@Override
	public boolean isSensitiveToWater() {
		return true;
	}

	@Override
	public boolean canRiderInteract() {
		return true;
	}

	@Override
	protected boolean shouldDropLoot() {
		return true;
	}

	@Override
	protected ResourceLocation getDefaultLootTable() {
		switch (this.getSize()) {
			default -> {
				return super.getDefaultLootTable();
			}
			case PURP -> {
				return PURP_LOOT_TABLE;
			}
			case PURPAZOID -> {
				return PURPAZOID_LOOT_TABLE;
			}
		}
	}

	@Override
	public void onEndimationStart(PlayableEndimation endimation, PlayableEndimation oldEndimation) {
		if (endimation == EEPlayableEndimations.PURPOID_DEATH) {
			this.deathTime = 0;
		}
	}

	@Override
	public void onEndimationEnd(PlayableEndimation endimation, PlayableEndimation newEndimation) {
		if (!this.level().isClientSide && newEndimation != EEPlayableEndimations.PURPOID_TELEPORT_FROM && (endimation == EEPlayableEndimations.PURPOID_TELEPORT_TO || endimation == EEPlayableEndimations.PURPOID_FAST_TELEPORT_TO)) {
			NetworkUtil.setPlayingAnimation(this, EEPlayableEndimations.PURPOID_TELEPORT_FROM);
		}
	}

	private void burstParticles() {
		CorrockCrownParticleData particleData = this.createParticleData();
		Level level = this.level();
		RandomSource random = this.getRandom();
		for (int i = 0; i < 12; i++) {
			level.addParticle(particleData, this.getRandomX(0.5D), this.getRandomY(), this.getRandomZ(0.5D), MathUtil.makeNegativeRandomly(random.nextFloat() * 0.25F, random), (random.nextFloat() - random.nextFloat()) * 0.3F + 0.1F, MathUtil.makeNegativeRandomly(random.nextFloat() * 0.25F, random));
		}
	}

	private boolean tryToTeleportRandomly(int minDistance, int maxDistance, int attempts) {
		BlockPos pos = this.blockPosition();
		RandomSource random = this.getRandom();
		EntityDimensions size = this.getDimensions(this.getPose());
		Level world = this.level();
		int upperBound = maxDistance - minDistance + 1;
		for (int i = 0; i < attempts; i++) {
			BlockPos randomPos = pos.offset((int) MathUtil.makeNegativeRandomly(minDistance + random.nextInt(upperBound), random), (int) MathUtil.makeNegativeRandomly(minDistance + random.nextInt(upperBound), random), (int) MathUtil.makeNegativeRandomly(minDistance + random.nextInt(upperBound), random));
			AABB collisionBox = size.makeBoundingBox(randomPos.getX() + 0.5F, randomPos.getY(), randomPos.getZ() + 0.5F);
			if (world.noCollision(collisionBox) && !world.containsAnyLiquid(collisionBox)) {
				this.teleportController.beginTeleportation(this, randomPos, true);
				return true;
			}
		}
		return false;
	}

	static class PurpoidMoveController extends MoveControl {
		private Vec3 prevPos;
		private int stuckTicks;

		public PurpoidMoveController(Purpoid mob) {
			super(mob);
			this.prevPos = mob.position();
		}

		@Override
		public void setWantedPosition(double x, double y, double z, double speedIn) {
			super.setWantedPosition(x, y, z, speedIn);
			this.stuckTicks = 0;
		}

		@Override
		public void tick() {
			Purpoid purpoid = (Purpoid) this.mob;
			boolean boosting = purpoid.isBoosting();
			if (this.operation == Operation.MOVE_TO) {
				Vec3 pos = purpoid.position();
				double x = pos.x();
				double z = pos.z();
				Vec3 vector3d = new Vec3(this.wantedX - x, this.wantedY - pos.y(), this.wantedZ - z);
				double distance = vector3d.length();
				if (distance <= 0.2F * purpoid.getSize().getScale()) {
					this.operation = Operation.WAIT;
				} else {
					double dx = vector3d.x;
					double dz = vector3d.z;
					purpoid.setYRot(purpoid.yBodyRot = this.rotlerp(purpoid.getYRot(), (float) (Mth.atan2(dz, dx) * (double) (180F / (float) Math.PI)) - 90.0F, 90.0F));
					float newMoveSpeed = Mth.lerp(0.125F, purpoid.getSpeed(), (boosting ? 1.25F : 1.0F) * (float) (this.speedModifier * purpoid.getAttributeValue(Attributes.MOVEMENT_SPEED)));
					purpoid.setSpeed(newMoveSpeed);
					double normalizedY = vector3d.y / distance;
					purpoid.setDeltaMovement(purpoid.getDeltaMovement().add(0.0F, newMoveSpeed * normalizedY * 0.1D, 0.0F));
					LookControl lookControl = purpoid.getLookControl();
					double d11 = lookControl.getWantedX();
					double d12 = lookControl.getWantedY();
					double d13 = lookControl.getWantedZ();
					double d8 = x + (dx / distance) * 2.0D;
					double d9 = purpoid.getEyeY() + normalizedY / distance;
					double d10 = z + (dz / distance) * 2.0D;
					if (!lookControl.isLookingAtTarget()) {
						d11 = d8;
						d12 = d9;
						d13 = d10;
					}

					lookControl.setLookAt(Mth.lerp(0.125D, d11, d8), Mth.lerp(0.125D, d12, d9), Mth.lerp(0.125D, d13, d10), 10.0F, 40.0F);

					if (this.prevPos.distanceToSqr(pos) <= 0.005F) {
						if (++this.stuckTicks >= 60) {
							this.operation = Operation.WAIT;
						}
					} else {
						this.stuckTicks = 0;
					}
				}
			} else {
				purpoid.setSpeed(0.0F);
				if (purpoid.isNoEndimationPlaying() && !purpoid.isResting() && purpoid.getStunTimer() <= 0) {
					purpoid.setDeltaMovement(purpoid.getDeltaMovement().add(0.0F, boosting ? 0.025F : 0.01F, 0.0F));
				}
			}
			this.prevPos = purpoid.position();
		}

	}

	public static class TeleportController {
		@Nullable
		private Vec3 destination;

		private void tick(Purpoid purpoid) {
			if ((purpoid.isEndimationPlaying(EEPlayableEndimations.PURPOID_TELEPORT_TO) || purpoid.isEndimationPlaying(EEPlayableEndimations.PURPOID_FAST_TELEPORT_TO)) && purpoid.getAnimationTick() == 10) {
				this.teleportToDestination(purpoid);
			} else if (purpoid.isEndimationPlaying(EEPlayableEndimations.PURPOID_TELEPORT_FROM)) {
				purpoid.setDeltaMovement(Vec3.ZERO);
			}
		}

		private void teleportToDestination(Purpoid purpoid) {
			Vec3 destination = this.destination;
			if (destination != null) {
				Entity ridingEntity = purpoid.getVehicle();
				if (ridingEntity != null) {
					ridingEntity.teleportToWithTicket(destination.x, destination.y, destination.z);
				} else {
					NetworkUtil.teleportEntity(purpoid, destination.x, destination.y, destination.z);
				}
				this.destination = null;
			}
		}

		public void beginTeleportation(Purpoid purpoid, Vec3 destination, boolean fast) {
			NetworkUtil.setPlayingAnimation(purpoid, fast ? EEPlayableEndimations.PURPOID_FAST_TELEPORT_TO : EEPlayableEndimations.PURPOID_TELEPORT_TO);
			this.destination = destination;
		}

		public void beginTeleportation(Purpoid purpoid, BlockPos destination, boolean fast) {
			this.beginTeleportation(purpoid, new Vec3(destination.getX() + 0.5D, purpoid.isBaby() ? destination.getY() + 0.25F : destination.getY(), destination.getZ() + 0.5D), fast);
		}

		public boolean isTeleporting() {
			return this.destination != null;
		}
	}
}
