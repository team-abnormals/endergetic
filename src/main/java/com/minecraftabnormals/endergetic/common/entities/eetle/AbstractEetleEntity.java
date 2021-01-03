package com.minecraftabnormals.endergetic.common.entities.eetle;

import com.minecraftabnormals.abnormals_core.core.endimator.Endimation;
import com.minecraftabnormals.abnormals_core.core.endimator.entity.IEndimatedEntity;
import com.minecraftabnormals.endergetic.core.registry.EEItems;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.UUID;

public abstract class AbstractEetleEntity extends MonsterEntity implements IEndimatedEntity {
	private static final DataParameter<Boolean> CHILD = EntityDataManager.createKey(AbstractEetleEntity.class, DataSerializers.BOOLEAN);
	private static final EntitySize LEETLE_SIZE = EntitySize.fixed(0.6F, 0.4375F);
	private static final AttributeModifier LEETLE_HEALTH = new AttributeModifier(UUID.fromString("8a1ea466-4b2d-11eb-ae93-0242ac130002"), "Leetle health decrease", -0.8F, AttributeModifier.Operation.MULTIPLY_BASE);
	private final AvoidEntityGoal<PlayerEntity> avoidEntityGoal;
	private Endimation endimation = BLANK_ANIMATION;
	private int animationTick;

	protected AbstractEetleEntity(EntityType<? extends AbstractEetleEntity> type, World world) {
		super(type, world);
		this.avoidEntityGoal = new AvoidEntityGoal<>(this, PlayerEntity.class, 12.0F, 1.0F, 1.0F);
	}

	@Override
	protected void registerData() {
		super.registerData();
		this.dataManager.register(CHILD, false);
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
	}

	@Override
	public void writeAdditional(CompoundNBT compound) {
		super.writeAdditional(compound);
		compound.putBoolean("IsChild", this.isChild());
	}

	@Override
	public void readAdditional(CompoundNBT compound) {
		super.readAdditional(compound);
		this.setChild(compound.getBoolean("IsChild"));
	}

	@Override
	public boolean isChild() {
		return this.dataManager.get(CHILD);
	}

	@Override
	public void setChild(boolean child) {
		this.dataManager.set(CHILD, child);
		if (child) {
			this.experienceValue = 2;
			this.goalSelector.addGoal(1, this.avoidEntityGoal);
			if (this.world != null && !this.world.isRemote) {
				ModifiableAttributeInstance maxHealth = this.getAttribute(Attributes.MAX_HEALTH);
				if (maxHealth != null) {
					maxHealth.applyNonPersistentModifier(LEETLE_HEALTH);
					this.setHealth(Math.max(this.getHealth(), this.getMaxHealth()));
				}
			}
		} else {
			this.experienceValue = 6;
			this.goalSelector.removeGoal(this.avoidEntityGoal);
			ModifiableAttributeInstance maxHealth = this.getAttribute(Attributes.MAX_HEALTH);
			if (maxHealth != null) {
				maxHealth.removeModifier(LEETLE_HEALTH);
			}
		}
	}

	@Nullable
	@Override
	public ILivingEntityData onInitialSpawn(IServerWorld world, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnData, @Nullable CompoundNBT dataTag) {
		//TODO: Move to custom child chance condition
		if (this.rand.nextFloat() < 0.25F) {
			this.setChild(true);
		}
		return super.onInitialSpawn(world, difficultyIn, reason, spawnData, dataTag);
	}

	public float getClimbingOffset() {
		return this.isChild() ? 0.053125F : 0.425F;
	}

	@Override
	public EntitySize getSize(Pose poseIn) {
		return this.isChild() ? LEETLE_SIZE : super.getSize(poseIn);
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
