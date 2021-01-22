package com.minecraftabnormals.endergetic.common.entities.eetle;

import com.minecraftabnormals.abnormals_core.core.endimator.Endimation;
import com.minecraftabnormals.abnormals_core.core.endimator.entity.IEndimatedEntity;
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
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.UUID;

public abstract class AbstractEetleEntity extends MonsterEntity implements IEndimatedEntity {
	private static final DataParameter<Boolean> CHILD = EntityDataManager.createKey(AbstractEetleEntity.class, DataSerializers.BOOLEAN);
	private static final EntitySize LEETLE_SIZE = EntitySize.fixed(0.6F, 0.4375F);
	private static final AttributeModifier LEETLE_HEALTH = new AttributeModifier(UUID.fromString("8a1ea466-4b2d-11eb-ae93-0242ac130002"), "Leetle health decrease", -0.8F, AttributeModifier.Operation.MULTIPLY_BASE);
	private static final Direction[] EGG_DIRECTIONS = Direction.values();
	private final AvoidEntityGoal<PlayerEntity> avoidEntityGoal = new AvoidEntityGoal<>(this, PlayerEntity.class, 12.0F, 1.0F, 1.0F);
	private NearestAttackableTargetGoal<PlayerEntity> attackableTargetGoal;
	private Endimation endimation = BLANK_ANIMATION;
	private int animationTick;
	private int growingAge;

	protected AbstractEetleEntity(EntityType<? extends AbstractEetleEntity> type, World world) {
		super(type, world);
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
		}
	}

	@Override
	public void writeAdditional(CompoundNBT compound) {
		super.writeAdditional(compound);
		compound.putInt("Age", this.growingAge);
	}

	@Override
	public void readAdditional(CompoundNBT compound) {
		super.readAdditional(compound);
		this.updateAge(compound.getInt("Age"));
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
			this.setChild(growingAge < 0);
		}
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
		if (!this.isChild() && this.rand.nextFloat() < 0.6F && !this.removed && !this.dead) {
			World world = this.world;
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
								((EetleEggsTileEntity) tileEntity).updateHatchDelay(world, random.nextInt(11) + 5);
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
}
