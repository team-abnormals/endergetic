package com.teamabnormals.endergetic.common.entity.eetle;

import com.teamabnormals.endergetic.core.registry.EEBlocks;
import com.teamabnormals.endergetic.core.registry.EEEntityTypes;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;

import javax.annotation.Nullable;

public class BroodEggSack extends Entity {
	private static final EntityDimensions FLYING_SIZE = EntityDimensions.fixed(1.5F, 1.5F);
	private static final EntityDimensions EXPOSED_SIZE = EntityDimensions.fixed(1.5F, 1.75F);
	private static final EntityDataAccessor<Integer> BROOD_ID = SynchedEntityData.defineId(BroodEggSack.class, EntityDataSerializers.INT);

	public BroodEggSack(EntityType<?> entityType, Level world) {
		super(EEEntityTypes.BROOD_EGG_SACK.get(), world);
	}

	public BroodEggSack(Level world) {
		super(EEEntityTypes.BROOD_EGG_SACK.get(), world);
	}

	public BroodEggSack(PlayMessages.SpawnEntity spawnEntity, Level world) {
		super(EEEntityTypes.BROOD_EGG_SACK.get(), world);
	}

	@Override
	protected void defineSynchedData() {
		this.entityData.define(BROOD_ID, -1);
	}

	@Override
	public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
		super.onSyncedDataUpdated(key);
		if (BROOD_ID.equals(key)) {
			this.refreshDimensions();
		}
	}

	@Override
	public void tick() {
		Level world = this.level;
		BroodEetle broodEetle = this.getBroodEetle(world);
		if (!world.isClientSide && (broodEetle == null || !broodEetle.isAlive() || broodEetle.getEggSack(world) != this)) {
			this.discard();
		}
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag compound) {
		if (compound.contains("BroodID", 3)) {
			this.setBroodID(compound.getInt("BroodID"));
		}
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag compound) {
		int broodID = this.getBroodID();
		if (broodID >= 0) {
			compound.putInt("BroodID", broodID);
		}
	}


	public void updatePosition(BroodEetle broodEetle) {
		Vec3 sackPos = getEggPos(broodEetle.position(), broodEetle.yBodyRot, broodEetle.getEggCannonProgressServer(), broodEetle.getEggCannonFlyingProgressServer(), broodEetle.getFlyingRotations().getFlyPitch(), broodEetle.isOnLastHealthStage());
		this.setPos(sackPos.x(), sackPos.y(), sackPos.z());
	}

	public void setBroodID(int id) {
		this.entityData.set(BROOD_ID, Math.max(-1, id));
	}

	private int getBroodID() {
		return this.entityData.get(BROOD_ID);
	}

	@Nullable
	private BroodEetle getBroodEetle(Level world) {
		int broodID = this.getBroodID();
		if (broodID >= 0) {
			Entity entity = world.getEntity(broodID);
			if (entity instanceof BroodEetle) {
				return (BroodEetle) entity;
			}
		}
		return null;
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		Level world = this.level;
		if (!world.isClientSide) {
			BroodEetle broodEetle = this.getBroodEetle(world);
			if (broodEetle != null && broodEetle.isAlive() && (broodEetle.isEggMouthOpen() || broodEetle.isOnLastHealthStage())) {
				Entity trueSource = source.getEntity();
				LivingEntity livingEntity = trueSource instanceof LivingEntity ? (LivingEntity) trueSource : null;
				if (livingEntity != null) {
					amount += 0.25F * EnchantmentHelper.getDamageBonus(livingEntity.getMainHandItem(), MobType.ARTHROPOD);
				}
				if (broodEetle.attackEntityFromEggSack(source, amount)) {
					if (livingEntity != null) {
						this.doEnchantDamageEffects(livingEntity, broodEetle);
					}
					if (world instanceof ServerLevel) {
						((ServerLevel) world).sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, EEBlocks.EETLE_EGG.get().defaultBlockState()), this.getX(), this.getY() + (double) this.getBbHeight() / 1.5D, this.getZ(), 15, this.getBbWidth() / 4.0F, this.getBbHeight() / 4.0F, this.getBbWidth() / 4.0F, 0.05D);
					}
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean isPickable() {
		return !this.isRemoved();
	}

	@Override
	protected MovementEmission getMovementEmission() {
		return MovementEmission.NONE;
	}

	@Override
	public boolean causeFallDamage(float distance, float damageMultiplier, DamageSource source) {
		return false;
	}

	@Override
	public EntityDimensions getDimensions(Pose pose) {
		BroodEetle broodEetle = this.getBroodEetle(this.level);
		if (broodEetle != null) {
			if (broodEetle.isFlying()) {
				return FLYING_SIZE;
			} else if (broodEetle.isOnLastHealthStage()) {
				return EXPOSED_SIZE;
			}
		}
		return super.getDimensions(pose);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean displayFireAnimation() {
		return false;
	}

	@Override
	public Packet<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	public static Vec3 getEggPos(Vec3 pos, float yaw, float eggCannonProgress, float eggCannonFlyingProgress, float flyPitch, boolean exposed) {
		flyPitch = Mth.clamp(flyPitch, -30.0F, 20.0F);
		float flyPitchMultiplier = flyPitch >= 0.0F ? 0.0425F : 0.0567F;
		float xOffset = flyPitch < 0.0F ? flyPitch * 0.033F : 0.0F;
		return pos.add(new Vec3(-1.75F + 0.8F * eggCannonProgress - xOffset, 1.3D + Math.sin(eggCannonProgress * 0.91F) - Math.sin(eggCannonFlyingProgress * 1.2F) + flyPitch * flyPitchMultiplier - (exposed ? (eggCannonProgress == 0.0F ? 0.2F : eggCannonProgress * 0.75F) : 0.0F), 0.0D).yRot(-yaw * ((float) Math.PI / 180F) - ((float) Math.PI / 2F)));
	}
}
