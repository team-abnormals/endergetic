package com.teamabnormals.endergetic.common.entities;

import java.util.List;

import com.teamabnormals.endergetic.core.registry.EEBlocks;
import com.teamabnormals.endergetic.core.registry.EEEntities;
import com.teamabnormals.endergetic.core.registry.other.EETags;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;

//TODO: Refactor this a bit, it's kinda weird how some things are done internally.
public class BoofBlockEntity extends Entity {
	private static final EntityDataAccessor<BlockPos> ORIGIN = SynchedEntityData.defineId(BoofBlockEntity.class, EntityDataSerializers.BLOCK_POS);
	private static final EntityDataAccessor<Boolean> FOR_PROJECTILE = SynchedEntityData.defineId(BoofBlockEntity.class, EntityDataSerializers.BOOLEAN);

	public BoofBlockEntity(EntityType<? extends BoofBlockEntity> type, Level world) {
		super(EEEntities.BOOF_BLOCK.get(), world);
	}

	public BoofBlockEntity(PlayMessages.SpawnEntity spawnEntity, Level world) {
		this(EEEntities.BOOF_BLOCK.get(), world);
	}

	public BoofBlockEntity(Level world, BlockPos pos) {
		this(EEEntities.BOOF_BLOCK.get(), world);
		this.setPos(pos.getX() + 0.5F, pos.getY() - 0.375F, pos.getZ() + 0.5F);
		this.setOrigin(pos);
	}

	@Override
	protected void defineSynchedData() {
		this.entityData.define(ORIGIN, BlockPos.ZERO);
		this.entityData.define(FOR_PROJECTILE, false);
	}

	@Override
	public void tick() {
		AABB bb = this.getBoundingBox().inflate(0, 0.25F, 0);
		List<Entity> entities = this.level.getEntitiesOfClass(Entity.class, bb, (entity -> !entity.isPassenger()));
		for (Entity entity : entities) {
			if (!entity.getType().is(EETags.EntityTypes.BOOF_BLOCK_RESISTANT)) {
				if (entity instanceof AbstractArrow) {
					this.setForProjectile(true);
					this.level.setBlockAndUpdate(getOrigin(), Blocks.AIR.defaultBlockState());
					entity.push(Mth.sin((float) (entity.getYRot() * Math.PI / 180.0F)) * 3 * 0.1F, 0.55D, -Mth.cos((float) (entity.getYRot() * Math.PI / 180.0F)) * 3 * 0.1F);
				} else {
					if (entity.getY() - 0.45F >= this.getY()) {
						entity.push(0, this.random.nextFloat() * 0.05D + 0.35D, 0);
					} else if (entity.getY() < this.getY() - 1F) {
						entity.push(0, this.random.nextFloat() * -0.05D - 0.35D, 0);
					} else {
						float amount = 0.5F;
						Vec3 result = entity.position().subtract(this.position());
						entity.push(result.x * amount, this.random.nextFloat() * 0.45D + 0.25D, result.z * amount);
					}
				}
				if (!(entity instanceof Player)) {
					entity.hurtMarked = true;
				}
			}
		}

		if (this.tickCount >= 10) {
			if (this.level.isAreaLoaded(this.getOrigin(), 1) && this.level.getBlockState(getOrigin()).getBlock() == EEBlocks.BOOF_BLOCK.get() && !this.isForProjectile()) {
				this.level.setBlockAndUpdate(this.getOrigin(), EEBlocks.BOOF_BLOCK.get().defaultBlockState());
			} else if (this.level.isAreaLoaded(this.getOrigin(), 1) && this.isForProjectile()) {
				this.level.setBlockAndUpdate(this.getOrigin(), EEBlocks.BOOF_BLOCK.get().defaultBlockState());
			}
			this.discard();
		}
	}

	@Override
	public void readAdditionalSaveData(CompoundTag nbt) {
		this.setOrigin(new BlockPos(nbt.getInt("OriginX"), nbt.getInt("OriginY"), nbt.getInt("OriginZ")));
		this.setForProjectile(nbt.getBoolean("ForProjectile"));
	}

	@Override
	public void addAdditionalSaveData(CompoundTag nbt) {
		BlockPos blockpos = this.getOrigin();
		nbt.putInt("OriginX", blockpos.getX());
		nbt.putInt("OriginY", blockpos.getY());
		nbt.putInt("OriginZ", blockpos.getZ());
		nbt.putBoolean("ForProjectile", this.isForProjectile());
	}

	@Override
	public boolean isAlive() {
		return false;
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		return false;
	}

	public boolean isForProjectile() {
		return this.getEntityData().get(FOR_PROJECTILE);
	}

	@Override
	public boolean displayFireAnimation() {
		return false;
	}

	public void setForProjectile(boolean forProjectile) {
		this.getEntityData().set(FOR_PROJECTILE, forProjectile);
	}

	public void setOrigin(BlockPos pos) {
		this.getEntityData().set(ORIGIN, pos);
	}

	public BlockPos getOrigin() {
		return this.getEntityData().get(ORIGIN);
	}

	@Override
	public boolean isPushable() {
		return false;
	}

	@Override
	public boolean isPushedByFluid() {
		return false;
	}

	@Override
	public Packet<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}