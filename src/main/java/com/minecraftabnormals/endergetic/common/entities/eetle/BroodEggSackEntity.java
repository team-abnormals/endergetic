package com.minecraftabnormals.endergetic.common.entities.eetle;

import com.minecraftabnormals.endergetic.core.registry.EEBlocks;
import com.minecraftabnormals.endergetic.core.registry.EEEntities;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.UUID;

public class BroodEggSackEntity extends Entity {
	private UUID broodUUID;

	public BroodEggSackEntity(EntityType<?> entityType, World world) {
		super(EEEntities.BROOD_EGG_SACK.get(), world);
	}

	public BroodEggSackEntity(World world) {
		super(EEEntities.BROOD_EGG_SACK.get(), world);
	}

	public BroodEggSackEntity(FMLPlayMessages.SpawnEntity spawnEntity, World world) {
		super(EEEntities.BROOD_EGG_SACK.get(), world);
	}

	@Override
	protected void registerData() {
	}

	@Override
	public void tick() {
		World world = this.world;
		BroodEetleEntity broodEetle = this.getBroodEetle(world);
		if (!world.isRemote && (broodEetle == null || !broodEetle.isAlive() || broodEetle.getEggSack(world) != this)) {
			this.remove();
		}
	}

	@Override
	protected void readAdditional(CompoundNBT compound) {
		if (compound.hasUniqueId("BroodUUID")) {
			this.setBroodUUID(compound.getUniqueId("BroodUUID"));
		}
	}

	@Override
	protected void writeAdditional(CompoundNBT compound) {
		UUID broodUUID = this.broodUUID;
		if (broodUUID != null) {
			compound.putUniqueId("BroodUUID", broodUUID);
		}
	}

	public void updatePosition(BroodEetleEntity broodEetle) {
		Vector3d sackPos = getEggPos(broodEetle.getPositionVec(), broodEetle.renderYawOffset, broodEetle.getEggCannonProgressServer(), broodEetle.getEggCannonFlyingProgressServer(), broodEetle.getFlyingRotations().getFlyPitch());
		this.setPosition(sackPos.getX(), sackPos.getY(), sackPos.getZ());
	}

	public void setBroodUUID(UUID uuid) {
		this.broodUUID = uuid;
	}

	@Nullable
	private BroodEetleEntity getBroodEetle(World world) {
		UUID broodUUID = this.broodUUID;
		if (world instanceof ServerWorld && broodUUID != null) {
			Entity entity = ((ServerWorld) world).getEntityByUuid(broodUUID);
			if (entity instanceof BroodEetleEntity) {
				return (BroodEetleEntity) entity;
			}
		}
		return null;
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		World world = this.world;
		if (!world.isRemote) {
			BroodEetleEntity broodEetle = this.getBroodEetle(world);
			if (broodEetle != null && broodEetle.isAlive() && broodEetle.isEggMouthOpen()) {
				Entity trueSource = source.getTrueSource();
				LivingEntity livingEntity = trueSource instanceof LivingEntity ? (LivingEntity) trueSource : null;
				if (livingEntity != null) {
					amount += 0.5F * EnchantmentHelper.getModifierForCreature(livingEntity.getHeldItemMainhand(), CreatureAttribute.ARTHROPOD);
				}
				if (broodEetle.attackEntityFromEggSack(source, amount)) {
					if (livingEntity != null) {
						this.applyEnchantments(livingEntity, broodEetle);
					}
					if (world instanceof ServerWorld) {
						((ServerWorld) world).spawnParticle(new BlockParticleData(ParticleTypes.BLOCK, EEBlocks.EETLE_EGGS.get().getDefaultState()), this.getPosX(), this.getPosY() + (double) this.getHeight() / 1.5D, this.getPosZ(), 15, this.getWidth() / 4.0F, this.getHeight() / 4.0F, this.getWidth() / 4.0F, 0.05D);
					}
					return true;
				}
			}
		}
		return false;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean canBeCollidedWith() {
		return !this.removed;
	}

	@Override
	protected boolean canTriggerWalking() {
		return false;
	}

	@Override
	public boolean onLivingFall(float distance, float damageMultiplier) {
		return false;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean canRenderOnFire() {
		return false;
	}

	@Override
	public IPacket<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	public static Vector3d getEggPos(Vector3d pos, float yaw, float eggCannonProgress, float eggCannonFlyingProgress, float flyPitch) {
		flyPitch = MathHelper.clamp(flyPitch, -30.0F, 20.0F);
		float flyPitchMultiplier = flyPitch >= 0.0F ? 0.0425F : 0.0567F;
		float xOffset = flyPitch < 0.0F ? flyPitch * 0.033F : 0.0F;
		return pos.add(new Vector3d(-1.75F + 0.8F * eggCannonProgress - xOffset, 1.3D + Math.sin(eggCannonProgress * 0.91F) - Math.sin(eggCannonFlyingProgress * 1.2F) + flyPitch * flyPitchMultiplier, 0.0D).rotateYaw(-yaw * ((float)Math.PI / 180F) - ((float)Math.PI / 2F)));
	}
}
