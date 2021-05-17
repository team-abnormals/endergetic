package com.minecraftabnormals.endergetic.common.entities.eetle;

import com.minecraftabnormals.abnormals_core.core.endimator.Endimation;
import com.minecraftabnormals.abnormals_core.core.endimator.entity.IEndimatedEntity;
import com.minecraftabnormals.abnormals_core.core.util.NetworkUtil;
import com.minecraftabnormals.endergetic.client.particles.EEParticles;
import com.minecraftabnormals.endergetic.common.blocks.EetleEggsBlock;
import com.minecraftabnormals.endergetic.common.entities.eetle.ai.EetleHurtByTargetGoal;
import com.minecraftabnormals.endergetic.common.tileentities.EetleEggsTileEntity;
import com.minecraftabnormals.endergetic.core.registry.EEBlocks;
import com.minecraftabnormals.endergetic.core.registry.EEItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.tags.BlockTags;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.UUID;

public abstract class AbstractEetleEntity extends MonsterEntity implements IEndimatedEntity {
	private static final DataParameter<Boolean> CHILD = EntityDataManager.createKey(AbstractEetleEntity.class, DataSerializers.BOOLEAN);
	private static final EntitySize LEETLE_SIZE = EntitySize.fixed(0.6F, 0.4375F);
	private static final AttributeModifier LEETLE_HEALTH = new AttributeModifier(UUID.fromString("8a1ea466-4b2d-11eb-ae93-0242ac130002"), "Leetle health decrease", -0.8F, AttributeModifier.Operation.MULTIPLY_BASE);
	private static final Direction[] EGG_DIRECTIONS = Direction.values();
	public static final Endimation GROW_UP = new Endimation(30);
	private final AvoidEntityGoal<PlayerEntity> avoidEntityGoal = new AvoidEntityGoal<>(this, PlayerEntity.class, 12.0F, 1.0F, 1.0F);
	private NearestAttackableTargetGoal<PlayerEntity> attackableTargetGoal;
	private Endimation endimation = BLANK_ANIMATION;
	private int animationTick;
	private int growingAge;
	private int despawnTimer;
	private boolean fromEgg;
	protected int idleDelay;

	protected AbstractEetleEntity(EntityType<? extends AbstractEetleEntity> type, World world) {
		super(type, world);
		this.moveController = new GroundEetleMoveController(this);
		this.stepHeight = 0.5F;
	}

	@Override
	protected void registerData() {
		super.registerData();
		this.dataManager.register(CHILD, false);
	}

	@Override
	protected void registerGoals() {
		this.attackableTargetGoal = new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true);
		this.targetSelector.addGoal(2, this.attackableTargetGoal);
		this.targetSelector.addGoal(1, new EetleHurtByTargetGoal(this));
	}

	@Override
	public void notifyDataManagerChange(DataParameter<?> key) {
		if (CHILD.equals(key)) {
			this.recalculateSize();
		}
		super.notifyDataManagerChange(key);
	}

	@Override
	public void tick() {
		super.tick();
		this.endimateTick();

		if (!this.world.isRemote && this.isAlive()) {
			int age = this.growingAge;
			if (age < 0) {
				this.updateAge(++age);
			} else if (age > 0) {
				this.updateAge(--age);
			}
			if (!this.isChild()) {
				if (this.idleDelay > 0) this.idleDelay--;
				if (this.despawnTimer > 0) {
					int newTime = --this.despawnTimer;
					if (newTime == 0) {
						this.world.setEntityState(this, (byte) 20);
						this.remove();
					} else if (newTime <= 100 && newTime % 10 == 0) {
						LivingEntity attackTarget = this.getAttackTarget();
						if (attackTarget != null && attackTarget.isAlive() && this.getDistanceSq(attackTarget) <= 256.0F && this.canEntityBeSeen(attackTarget)) {
							this.despawnTimer += 105 + this.rand.nextInt(11);
						}
					}
				}
			}
		}
	}

	@Override
	public void writeAdditional(CompoundNBT compound) {
		super.writeAdditional(compound);
		compound.putInt("Age", this.growingAge);
		compound.putInt("DespawnTimer", this.despawnTimer);
		compound.putBoolean("FromEgg", this.fromEgg);
	}

	@Override
	public void readAdditional(CompoundNBT compound) {
		super.readAdditional(compound);
		this.updateAge(compound.getInt("Age"));
		this.despawnTimer = Math.max(0, compound.getInt("DespawnTimer"));
		this.fromEgg = compound.getBoolean("FromEgg");
	}

	@Override
	public boolean isChild() {
		return this.dataManager.get(CHILD);
	}

	@Override
	public void setChild(boolean child) {
		boolean wasChild = this.isChild();
		this.dataManager.set(CHILD, child);
		this.updateGoals(this.goalSelector, this.targetSelector, child);
		if (child) {
			this.experienceValue = 2;
			if (this.world != null && !this.world.isRemote) {
				ModifiableAttributeInstance maxHealth = this.getAttribute(Attributes.MAX_HEALTH);
				if (maxHealth != null) {
					maxHealth.applyNonPersistentModifier(LEETLE_HEALTH);
					this.setHealth(Math.max(this.getHealth(), this.getMaxHealth()));
				}
			}
		} else {
			this.experienceValue = 6;
			if (wasChild) {
				ModifiableAttributeInstance maxHealth = this.getAttribute(Attributes.MAX_HEALTH);
				if (maxHealth != null) {
					maxHealth.removeModifier(LEETLE_HEALTH);
				}
				this.setHealth(Math.min(this.getMaxHealth(), this.getHealth() * 4.3F));
			}
		}
	}

	public void updateAge(int growingAge) {
		int prevAge = this.growingAge;
		this.growingAge = growingAge;
		if (prevAge < 0 && growingAge >= 0 || prevAge >= 0 && growingAge < 0) {
			boolean willBeAdult = growingAge >= 0;
			if (willBeAdult && this.isChild()) {
				NetworkUtil.setPlayingAnimationMessage(this, GROW_UP);
				return;
			} else if (this.isEndimationPlaying(GROW_UP) && !willBeAdult) {
				NetworkUtil.setPlayingAnimationMessage(this, BLANK_ANIMATION);
			}
			this.setChild(!willBeAdult);
		}
	}

	public int getGrowingAge() {
		return this.growingAge;
	}

	public void markFromEgg() {
		this.fromEgg = true;
	}

	@Nullable
	@Override
	public ILivingEntityData onInitialSpawn(IServerWorld world, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnData, @Nullable CompoundNBT dataTag) {
		//Patches of baby eetles will spawn 40% of the time
		if (reason == SpawnReason.NATURAL && this.rand.nextFloat() < 0.4F) {
			this.updateAge(-(20000 + this.rand.nextInt(4001)));
			BlockPos.Mutable mutable = new BlockPos.Mutable();
			int startX = (int) this.getPosX();
			int startZ = (int) this.getPosZ();
			for (int x = -2; x <= 2; x++) {
				for (int z = -2; z <= 2; z++) {
					if (this.rand.nextFloat() < 0.1F) {
						int currentX = startX + x;
						int currentZ = startZ + z;
						mutable.setPos(currentX, world.getHeight(Heightmap.Type.MOTION_BLOCKING, currentX, currentZ), currentZ);
						if (world.isAirBlock(mutable) && Block.hasSolidSideOnTop(world, mutable.down())) {
							EntityType<?> type = this.getType();
							Entity entity = type.create(this.world);
							if (entity instanceof AbstractEetleEntity) {
								AbstractEetleEntity eetle = (AbstractEetleEntity) entity;
								eetle.updateAge(-(20000 + this.rand.nextInt(4001)));
								if (this.world.addEntity(eetle)) {
									entity.setPositionAndRotation(currentX + 0.5F, mutable.getY(), currentZ + 0.5F, this.rand.nextFloat() * 360.0F, 0.0F);
								}
							}
						}
					}
				}
			}
		}
		return super.onInitialSpawn(world, difficultyIn, reason, spawnData, dataTag);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onDeath(DamageSource cause) {
		World world = this.world;
		if (!this.isChild() && this.rand.nextFloat() < calculateEggChance(world, this.getBoundingBox().grow(this.getAttributeValue(Attributes.FOLLOW_RANGE) * 1.25F)) && !this.removed && !this.dead) {
			if (!world.isRemote) {
				BlockPos pos = this.getPosition();
				if (world.getFluidState(pos).isEmpty() && world.getBlockState(pos).getMaterial().isReplaceable()) {
					Random random = this.rand;
					EetleEggsBlock.shuffleDirections(EGG_DIRECTIONS, random);
					BlockState defaultState = EEBlocks.EETLE_EGGS.get().getDefaultState();
					for (Direction direction : EGG_DIRECTIONS) {
						BlockState state = defaultState.with(EetleEggsBlock.FACING, direction);
						if (state.isValidPosition(world, pos)) {
							world.setBlockState(pos, state.with(EetleEggsBlock.SIZE, random.nextInt(2)));
							TileEntity tileEntity = world.getTileEntity(pos);
							if (tileEntity instanceof EetleEggsTileEntity) {
								EetleEggsTileEntity eetleEggsTileEntity = (EetleEggsTileEntity) tileEntity;
								eetleEggsTileEntity.updateHatchDelay(world, random.nextInt(11) + 5);
								eetleEggsTileEntity.bypassSpawningGameRule();
							}
							if (world instanceof ServerWorld) {
								((ServerWorld) world).spawnParticle(EEParticles.EETLE_CROWN.get(), this.getPosX(), this.getPosY() + this.getHeight(), this.getPosZ(), 5, this.getWidth() / 4.0F, this.getHeight() / 4.0F, this.getWidth() / 4.0F, 0.1D);
							}
						}
					}
				}
			}
		}
		super.onDeath(cause);
	}

	protected void updateGoals(GoalSelector goalSelector, GoalSelector targetSelector, boolean child) {
		if (child) {
			goalSelector.addGoal(1, this.avoidEntityGoal);
			targetSelector.removeGoal(this.attackableTargetGoal);
		} else {
			goalSelector.removeGoal(this.avoidEntityGoal);
			targetSelector.addGoal(2, this.attackableTargetGoal);
		}
	}

	public void resetIdleFlapDelay() {
		this.idleDelay = this.rand.nextInt(41) + 30;
	}

	public void applyDespawnTimer() {
		this.despawnTimer = this.rand.nextInt(101) + 600;
	}

	@Override
	public Endimation getPlayingEndimation() {
		return this.endimation;
	}

	@Override
	public void setPlayingEndimation(Endimation endimation) {
		this.onEndimationEnd(this.endimation);
		this.endimation = endimation;
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

	@Override
	public void onEndimationEnd(Endimation endimation) {
		World world = this.world;
		if (endimation == GROW_UP && world instanceof ServerWorld) {
			this.setChild(false);
			((ServerWorld) world).spawnParticle(EEParticles.EETLE_CROWN.get(), this.getPosX(), this.getPosY() + this.getHeight(), this.getPosZ(), 5, this.getWidth() / 4.0F, this.getHeight() / 4.0F, this.getWidth() / 4.0F, 0.1D);
		}
	}

	@Override
	public EntitySize getSize(Pose poseIn) {
		return this.isChild() ? LEETLE_SIZE : super.getSize(poseIn);
	}

	@Override
	protected float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn) {
		return 0.65F;
	}

	@Override
	public boolean isWaterSensitive() {
		return true;
	}

	@Override
	public CreatureAttribute getCreatureAttribute() {
		return CreatureAttribute.ARTHROPOD;
	}

	@Override
	public int getMaxSpawnedInChunk() {
		return 3;
	}

	@Override
	public ItemStack getPickedResult(RayTraceResult target) {
		return new ItemStack(EEItems.EETLE_SPAWN_EGG.get());
	}

	@Override
	public boolean onLivingFall(float distance, float damageMultiplier) {
		return false;
	}

	private static float calculateEggChance(World world, AxisAlignedBB boundingBox) {
		return 0.6F - 0.075F * world.getEntitiesWithinAABB(AbstractEetleEntity.class, boundingBox, eetle -> {
			return eetle.isAlive() && !eetle.isChild();
		}).size();
	}

	protected static class GroundEetleMoveController extends MovementController {

		protected GroundEetleMoveController(MobEntity mob) {
			super(mob);
		}

		@Override
		public void tick() {
			if (this.action == MovementController.Action.MOVE_TO) {
				MobEntity mob = this.mob;
				this.action = MovementController.Action.WAIT;
				double d0 = this.posX - mob.getPosX();
				double d1 = this.posZ - mob.getPosZ();
				double d2 = this.posY - mob.getPosY();
				double d3 = d0 * d0 + d2 * d2 + d1 * d1;
				if (d3 < 2.5000003E-7D) {
					mob.setMoveForward(0.0F);
					return;
				}

				float f9 = (float)(MathHelper.atan2(d1, d0) * (double)(180F / (float)Math.PI)) - 90.0F;
				mob.rotationYaw = this.limitAngle(mob.rotationYaw, f9, 90.0F);
				float moveSpeed = (float)(this.speed * mob.getAttributeValue(Attributes.MOVEMENT_SPEED));
				if (mob instanceof AbstractEetleEntity) {
					AbstractEetleEntity abstractEetleEntity = (AbstractEetleEntity) mob;
					if (abstractEetleEntity.isChild() && abstractEetleEntity.fromEgg) {
						int growingAge = Math.abs(abstractEetleEntity.growingAge);
						moveSpeed *= Math.min(2.0F, 7.0F * growingAge / (growingAge + 300.0F));
					}
				}
				mob.setAIMoveSpeed(moveSpeed);
				BlockPos blockpos = mob.getPosition();
				BlockState blockstate = mob.world.getBlockState(blockpos);
				Block block = blockstate.getBlock();
				VoxelShape voxelshape = blockstate.getCollisionShape(mob.world, blockpos);
				if (d2 > mob.stepHeight && d0 * d0 + d1 * d1 < (double)Math.max(1.0F, mob.getWidth()) || !voxelshape.isEmpty() && mob.getPosY() < voxelshape.getEnd(Direction.Axis.Y) + (double)blockpos.getY() && !block.isIn(BlockTags.DOORS) && !block.isIn(BlockTags.FENCES)) {
					mob.getJumpController().setJumping();
					this.action = MovementController.Action.JUMPING;
				}
				return;
			}
			super.tick();
		}

	}
}
