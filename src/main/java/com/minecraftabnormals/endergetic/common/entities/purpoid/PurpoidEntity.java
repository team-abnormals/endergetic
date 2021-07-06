package com.minecraftabnormals.endergetic.common.entities.purpoid;

import com.minecraftabnormals.abnormals_core.core.endimator.Endimation;
import com.minecraftabnormals.abnormals_core.core.endimator.entity.EndimatedEntity;
import com.minecraftabnormals.abnormals_core.core.util.MathUtil;
import com.minecraftabnormals.abnormals_core.core.util.NetworkUtil;
import com.minecraftabnormals.endergetic.api.entity.pathfinding.EndergeticFlyingPathNavigator;
import com.minecraftabnormals.endergetic.client.particles.EEParticles;
import com.minecraftabnormals.endergetic.client.particles.data.CorrockCrownParticleData;
import com.minecraftabnormals.endergetic.common.entities.purpoid.ai.*;
import com.minecraftabnormals.endergetic.core.registry.other.EEDataSerializers;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.LookController;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IndirectEntityDamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.Random;

public class PurpoidEntity extends EndimatedEntity {
	private static final DataParameter<PurpoidSize> SIZE = EntityDataManager.createKey(PurpoidEntity.class, EEDataSerializers.PURPOID_SIZE);
	private static final DataParameter<Integer> BOOSTING_TICKS = EntityDataManager.createKey(PurpoidEntity.class, DataSerializers.VARINT);
	public static final Endimation TELEPORT_TO_ANIMATION = new Endimation(18);
	public static final Endimation TELEPORT_FROM_ANIMATION = new Endimation(10);
	private final TeleportController teleportController = new TeleportController();
	private int teleportCooldown;
	private int restCooldown;
	private Vector3d prevPull = Vector3d.ZERO, pull = Vector3d.ZERO;
	@Nullable
	private BlockPos flowerPos;

	public PurpoidEntity(EntityType<? extends CreatureEntity> type, World world) {
		super(type, world);
		this.resetTeleportCooldown();
		this.resetRestCooldown();
		this.moveController = new PurpoidMoveController(this);
	}

	@Override
	protected void registerData() {
		super.registerData();
		this.dataManager.register(SIZE, PurpoidSize.NORMAL);
		this.dataManager.register(BOOSTING_TICKS, 0);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(1, new PurpoidRestOnFlowerGoal(this));
		this.goalSelector.addGoal(2, new PurpoidTeleportToFlowerGoal(this));
		this.goalSelector.addGoal(2, new PurpoidRandomTeleportGoal(this));
		this.goalSelector.addGoal(3, new PurpoidMoveRandomGoal(this));
	}

	@Override
	public void notifyDataManagerChange(DataParameter<?> key) {
		if (SIZE.equals(key)) {
			this.recalculateSize();
		}
		super.notifyDataManagerChange(key);
	}

	public static AttributeModifierMap.MutableAttribute getAttributes() {
		return MobEntity.func_233666_p_()
				.createMutableAttribute(Attributes.ATTACK_DAMAGE, 4.0F)
				.createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.2F)
				.createMutableAttribute(Attributes.MAX_HEALTH, 25.0F)
				.createMutableAttribute(Attributes.FOLLOW_RANGE, 32.0F)
				.createMutableAttribute(Attributes.KNOCKBACK_RESISTANCE, 0.2F);
	}

	@Override
	public void tick() {
		super.tick();
		World world = this.world;
		if (world.isRemote) {
			this.prevPull = this.pull;
			Vector3d pos = this.getPositionVec();
			this.pull = pos.add(this.pull.subtract(pos).normalize().scale(0.1F));

			if (this.isBoosting() && world.getGameTime() % 4 == 0) {
				double dy = this.pull.getY() - pos.getY();
				CorrockCrownParticleData particleData = this.createParticleData();
				Random random = this.getRNG();
				for (int i = 0; i < 2; i++) {
					world.addParticle(particleData, this.getPosXRandom(0.5D), this.getPosY() + this.getEyeHeight(), this.getPosZRandom(0.5D), MathUtil.makeNegativeRandomly(random.nextDouble() * 0.05F, random), dy * random.nextDouble(), MathUtil.makeNegativeRandomly(random.nextDouble() * 0.05F, random));
				}
			}

			if (!this.isNoEndimationPlaying()) {
				pos = pos.subtract(0.0F, 1.0F, 0.0F);
				this.pull = pos.add(this.pull.subtract(pos).normalize().scale(0.1F));

				int animationTick = this.getAnimationTick();
				if (this.isEndimationPlaying(TELEPORT_TO_ANIMATION) && animationTick == 8 || this.isEndimationPlaying(TELEPORT_FROM_ANIMATION) && animationTick == 4) {
					CorrockCrownParticleData particleData = this.createParticleData();
					Random random = this.getRNG();
					for (int i = 0; i < 12; i++) {
						world.addParticle(particleData, this.getPosXRandom(0.5D), this.getPosYRandom(), this.getPosZRandom(0.5D), MathUtil.makeNegativeRandomly(random.nextFloat() * 0.25F, random), (random.nextFloat() - random.nextFloat()) * 0.3F + 0.1F, MathUtil.makeNegativeRandomly(random.nextFloat() * 0.25F, random));
					}
				}
			}
		} else {
			if (this.hasTeleportCooldown()) {
				this.teleportCooldown--;
			}
			if (this.hasRestCooldown()) {
				this.restCooldown--;
			}
			if (this.isBoosting()) {
				this.setBoostingTicks(this.getBoostingTicks() - 1);
			} else if (this.hasTeleportCooldown() && !this.isResting() && this.rand.nextFloat() <= 0.001F) {
				this.setBoostingTicks(this.rand.nextInt(81) + 80);
			}
			this.teleportController.tick(this);
		}
	}

	@Override
	public void writeAdditional(CompoundNBT compound) {
		super.writeAdditional(compound);
		compound.putInt("Size", this.getSize().ordinal());
		compound.putInt("BoostingTicks", this.getBoostingTicks());
		compound.putInt("TeleportCooldown", this.teleportCooldown);
		compound.putInt("RestCooldown", this.restCooldown);
		if (this.isResting()) {
			compound.put("FlowerPos", NBTUtil.writeBlockPos(this.flowerPos));
		}
	}

	@Override
	public void readAdditional(CompoundNBT compound) {
		super.readAdditional(compound);
		this.setSize(PurpoidSize.values()[MathHelper.clamp(compound.getInt("Size"), 0, 2)], false);
		this.setBoostingTicks(Math.max(0, compound.getInt("BoostingTicks")));
		this.teleportCooldown = Math.max(0, compound.getInt("TeleportCooldown"));
		this.restCooldown = Math.max(0, compound.getInt("RestCooldown"));
		if (compound.contains("FlowerPos", Constants.NBT.TAG_COMPOUND)) {
			this.flowerPos = NBTUtil.readBlockPos(compound.getCompound("FlowerPos"));
		}
	}

	public void setSize(PurpoidSize size, boolean updateHealth) {
		this.dataManager.set(SIZE, size);
		float scale = size.getScale();
		this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(scale * 25.0F);
		this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(scale * 4.0F);
		if (updateHealth) {
			this.setHealth(this.getMaxHealth());
		}
		this.experienceValue = (int) (2 * scale);
	}

	public PurpoidSize getSize() {
		return this.dataManager.get(SIZE);
	}

	public void setBoostingTicks(int boostingTicks) {
		this.dataManager.set(BOOSTING_TICKS, boostingTicks);
	}

	public int getBoostingTicks() {
		return this.dataManager.get(BOOSTING_TICKS);
	}

	public boolean isBoosting() {
		return this.getBoostingTicks() > 0;
	}

	public void resetTeleportCooldown() {
		this.teleportCooldown = this.getRNG().nextInt(2801) + 200;
	}

	public boolean hasTeleportCooldown() {
		return this.teleportCooldown > 0;
	}

	public void resetRestCooldown() {
		this.restCooldown = this.getRNG().nextInt(2001) + 600;
	}

	public boolean hasRestCooldown() {
		return this.restCooldown > 0;
	}

	public void updatePull(Vector3d pos) {
		this.prevPull = this.pull = pos.subtract(0.0F, 1.0F, 0.0F);
	}

	public Vector3d getPull(float partialTicks) {
		return MathUtil.lerp(this.prevPull, this.pull, partialTicks);
	}

	public TeleportController getTeleportController() {
		return this.teleportController;
	}

	public void setFlowerPos(@Nullable BlockPos flowerPos) {
		this.flowerPos = flowerPos;
	}

	@Nullable
	public BlockPos getFlowerPos() {
		return this.flowerPos;
	}

	public boolean isResting() {
		return this.flowerPos != null;
	}

	private CorrockCrownParticleData createParticleData() {
		return new CorrockCrownParticleData(EEParticles.END_CROWN.get(), false, 0.2F * this.getSize().getScale());
	}

	@Override
	public void travel(Vector3d travelVector) {
		if (this.isServerWorld()) {
			this.moveRelative(0.1F, travelVector);
			this.move(MoverType.SELF, this.getMotion());
			this.setMotion(this.getMotion().scale(0.8D));
		} else {
			super.travel(travelVector);
		}
	}

	@Nullable
	@Override
	public ILivingEntityData onInitialSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
		this.setSize(this.rand.nextFloat() <= 0.005F ? PurpoidSize.GIANT : PurpoidSize.NORMAL, true);
		return super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
	}

	@Override
	public void func_233629_a_(LivingEntity entity, boolean p_233629_2_) {
		super.func_233629_a_(entity, true);
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		if (!this.world.isRemote && this.isNoEndimationPlaying()) {
			if (source instanceof IndirectEntityDamageSource) {
				if (this.tryToTeleportRandomly(12)) {
					return true;
				}
			} else if (!(source.getTrueSource() instanceof LivingEntity) && this.rand.nextInt(10) != 0) {
				this.tryToTeleportRandomly(1);
			}
		}
		return super.attackEntityFrom(source, amount);
	}

	@Override
	public PathNavigator createNavigator(World world) {
		return new EndergeticFlyingPathNavigator(this, world);
	}

	@Override
	public boolean onLivingFall(float distance, float damageMultiplier) {
		return false;
	}

	@Override
	public EntitySize getSize(Pose poseIn) {
		return super.getSize(poseIn).scale(this.getSize().getScale());
	}

	@Override
	protected float getStandingEyeHeight(Pose poseIn, EntitySize size) {
		return size.height * 0.5F;
	}

	@Override
	public boolean isWaterSensitive() {
		return true;
	}

	@Override
	public void resetEndimation() {
		Endimation endimation = this.getPlayingEndimation();
		super.resetEndimation();
		if (!this.world.isRemote && endimation == TELEPORT_TO_ANIMATION) {
			NetworkUtil.setPlayingAnimationMessage(this, TELEPORT_FROM_ANIMATION);
		}
	}

	@Override
	public Endimation[] getEndimations() {
		return new Endimation[] {
				TELEPORT_TO_ANIMATION,
				TELEPORT_FROM_ANIMATION
		};
	}

	private boolean tryToTeleportRandomly(int attempts) {
		BlockPos pos = this.getPosition();
		Random random = this.getRNG();
		EntitySize size = this.getSize(this.getPose());
		World world = this.world;
		for (int i = 0; i < attempts; i++) {
			BlockPos randomPos = pos.add(random.nextInt(17) - random.nextInt(17), random.nextInt(17) - random.nextInt(17), random.nextInt(17) - random.nextInt(17));
			AxisAlignedBB collisionBox = size.func_242285_a(randomPos.getX() + 0.5F, randomPos.getY(), randomPos.getZ() + 0.5F);
			if (world.hasNoCollisions(collisionBox) && !world.containsAnyLiquid(collisionBox)) {
				this.teleportController.beginTeleportation(this, randomPos);
				return true;
			}
		}
		return false;
	}

	static class PurpoidMoveController extends MovementController {
		private Vector3d prevPos;
		private int stuckTicks;

		public PurpoidMoveController(PurpoidEntity mob) {
			super(mob);
			this.prevPos = mob.getPositionVec();
		}

		@Override
		public void setMoveTo(double x, double y, double z, double speedIn) {
			super.setMoveTo(x, y, z, speedIn);
			this.stuckTicks = 0;
		}

		@Override
		public void tick() {
			PurpoidEntity purpoid = (PurpoidEntity) this.mob;
			boolean boosting = purpoid.isBoosting();
			if (this.action == Action.MOVE_TO) {
				Vector3d pos = purpoid.getPositionVec();
				double x = pos.getX();
				double z = pos.getZ();
				Vector3d vector3d = new Vector3d(this.posX - x, this.posY - pos.getY(), this.posZ - z);
				double distance = vector3d.length();
				if (distance <= 1.0F) {
					this.action = Action.WAIT;
					purpoid.setMotion(purpoid.getMotion().scale(0.5F));
				} else {
					double dx = vector3d.x;
					double dz = vector3d.z;
					purpoid.rotationYaw = purpoid.renderYawOffset = this.limitAngle(purpoid.rotationYaw, (float)(MathHelper.atan2(dz, dx) * (double)(180F / (float)Math.PI)) - 90.0F, 90.0F);
					float newMoveSpeed = MathHelper.lerp(0.125F, purpoid.getAIMoveSpeed(), (boosting ? 1.25F : 1.0F) * (float)(this.speed * purpoid.getAttributeValue(Attributes.MOVEMENT_SPEED)));
					purpoid.setAIMoveSpeed(newMoveSpeed);
					double normalizedY = vector3d.y / distance;
					purpoid.setMotion(purpoid.getMotion().add(0.0F, newMoveSpeed * normalizedY * 0.1D, 0.0F));
					LookController lookcontroller = purpoid.getLookController();
					double d11 = lookcontroller.getLookPosX();
					double d12 = lookcontroller.getLookPosY();
					double d13 = lookcontroller.getLookPosZ();
					double d8 = x + (dx / distance) * 2.0D;
					double d9 = purpoid.getPosYEye() + normalizedY / distance;
					double d10 = z + (dz / distance) * 2.0D;
					if (!lookcontroller.getIsLooking()) {
						d11 = d8;
						d12 = d9;
						d13 = d10;
					}

					purpoid.getLookController().setLookPosition(MathHelper.lerp(0.125D, d11, d8), MathHelper.lerp(0.125D, d12, d9), MathHelper.lerp(0.125D, d13, d10), 10.0F, 40.0F);

					if (this.prevPos.squareDistanceTo(pos) <= 0.005F) {
						if (++this.stuckTicks >= 60) {
							this.action = Action.WAIT;
						}
					} else {
						this.stuckTicks = 0;
					}
				}
			} else {
				purpoid.setAIMoveSpeed(0.0F);
				if (purpoid.isNoEndimationPlaying() && !purpoid.isResting()) {
					purpoid.setMotion(purpoid.getMotion().add(0.0F, boosting ? 0.025F : 0.01F, 0.0F));
				}
			}
			this.prevPos = purpoid.getPositionVec();
		}

	}

	public static class TeleportController {
		@Nullable
		private BlockPos destination;

		private void tick(PurpoidEntity purpoid) {
			if (purpoid.isEndimationPlaying(TELEPORT_TO_ANIMATION) && purpoid.getAnimationTick() == 10) {
				this.teleportToDestination(purpoid);
			} else if (purpoid.isEndimationPlaying(TELEPORT_FROM_ANIMATION)) {
				purpoid.setMotion(Vector3d.ZERO);
			}
		}

		private void teleportToDestination(PurpoidEntity purpoid) {
			if (this.isTeleporting()) {
				BlockPos destination = this.destination;
				NetworkUtil.teleportEntity(purpoid, destination.getX() + 0.5F, destination.getY(), destination.getZ() + 0.5F);
				this.destination = null;
			}
		}

		public void beginTeleportation(PurpoidEntity purpoid, BlockPos destination) {
			NetworkUtil.setPlayingAnimationMessage(purpoid, PurpoidEntity.TELEPORT_TO_ANIMATION);
			this.destination = destination;
		}

		public boolean isTeleporting() {
			return this.destination != null;
		}
	}
}
