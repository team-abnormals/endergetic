package com.minecraftabnormals.endergetic.common.entities.eetle;

import com.minecraftabnormals.endergetic.core.registry.EEBlocks;
import com.minecraftabnormals.endergetic.core.registry.EEEntities;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class BroodEggSackEntity extends Entity {
	private static final EntitySize FLYING_SIZE = EntitySize.fixed(1.5F, 1.5F);
	private static final EntitySize EXPOSED_SIZE = EntitySize.fixed(1.5F, 1.75F);
	private static final DataParameter<Integer> BROOD_ID = EntityDataManager.createKey(BroodEggSackEntity.class, DataSerializers.VARINT);

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
		this.dataManager.register(BROOD_ID, -1);
	}

	@Override
	public void notifyDataManagerChange(DataParameter<?> key) {
		super.notifyDataManagerChange(key);
		if (BROOD_ID.equals(key)) {
			this.recalculateSize();
		}
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
		if (compound.contains("BroodID", Constants.NBT.TAG_INT)) {
			this.setBroodID(compound.getInt("BroodID"));
		}
	}

	@Override
	protected void writeAdditional(CompoundNBT compound) {
		int broodID = this.getBroodID();
		if (broodID >= 0) {
			compound.putInt("BroodID", broodID);
		}
	}


	public void updatePosition(BroodEetleEntity broodEetle) {
		Vector3d sackPos = getEggPos(broodEetle.getPositionVec(), broodEetle.renderYawOffset, broodEetle.getEggCannonProgressServer(), broodEetle.getEggCannonFlyingProgressServer(), broodEetle.getFlyingRotations().getFlyPitch(), broodEetle.isOnLastHealthStage());
		this.setPosition(sackPos.getX(), sackPos.getY(), sackPos.getZ());
	}

	public void setBroodID(int id) {
		this.dataManager.set(BROOD_ID, Math.max(-1, id));
	}

	private int getBroodID() {
		return this.dataManager.get(BROOD_ID);
	}

	@Nullable
	private BroodEetleEntity getBroodEetle(World world) {
		int broodID = this.getBroodID();
		if (broodID >= 0) {
			Entity entity = world.getEntityByID(broodID);
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
			if (broodEetle != null && broodEetle.isAlive() && (broodEetle.isEggMouthOpen() || broodEetle.isOnLastHealthStage())) {
				Entity trueSource = source.getTrueSource();
				LivingEntity livingEntity = trueSource instanceof LivingEntity ? (LivingEntity) trueSource : null;
				if (livingEntity != null) {
					amount += 0.25F * EnchantmentHelper.getModifierForCreature(livingEntity.getHeldItemMainhand(), CreatureAttribute.ARTHROPOD);
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
	public EntitySize getSize(Pose pose) {
		BroodEetleEntity broodEetleEntity = this.getBroodEetle(this.world);
		if (broodEetleEntity != null) {
			if (broodEetleEntity.isFlying()) {
				return FLYING_SIZE;
			} else if (broodEetleEntity.isOnLastHealthStage()) {
				return EXPOSED_SIZE;
			}
		}
		return super.getSize(pose);
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

	public static Vector3d getEggPos(Vector3d pos, float yaw, float eggCannonProgress, float eggCannonFlyingProgress, float flyPitch, boolean exposed) {
		flyPitch = MathHelper.clamp(flyPitch, -30.0F, 20.0F);
		float flyPitchMultiplier = flyPitch >= 0.0F ? 0.0425F : 0.0567F;
		float xOffset = flyPitch < 0.0F ? flyPitch * 0.033F : 0.0F;
		return pos.add(new Vector3d(-1.75F + 0.8F * eggCannonProgress - xOffset, 1.3D + Math.sin(eggCannonProgress * 0.91F) - Math.sin(eggCannonFlyingProgress * 1.2F) + flyPitch * flyPitchMultiplier - (exposed ? (eggCannonProgress == 0.0F ? 0.2F : eggCannonProgress * 0.75F) : 0.0F), 0.0D).rotateYaw(-yaw * ((float)Math.PI / 180F) - ((float)Math.PI / 2F)));
	}
}
