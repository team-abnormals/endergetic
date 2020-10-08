package com.minecraftabnormals.endergetic.common.entities;

import java.util.List;

import com.minecraftabnormals.endergetic.core.registry.EEBlocks;
import com.minecraftabnormals.endergetic.core.registry.EEEntities;
import com.minecraftabnormals.endergetic.core.registry.other.EETags;

import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;

//TODO: Refactor this a bit, it's kinda weird how some things are done internally.
public class BoofBlockEntity extends Entity {
	private static final DataParameter<BlockPos> ORIGIN = EntityDataManager.createKey(BoofBlockEntity.class, DataSerializers.BLOCK_POS);
	private static final DataParameter<Boolean> FOR_PROJECTILE = EntityDataManager.createKey(BoofBlockEntity.class, DataSerializers.BOOLEAN);

	public BoofBlockEntity(EntityType<? extends BoofBlockEntity> type, World world) {
		super(EEEntities.BOOF_BLOCK.get(), world);
	}

	public BoofBlockEntity(FMLPlayMessages.SpawnEntity spawnEntity, World world) {
		this(EEEntities.BOOF_BLOCK.get(), world);
	}

	public BoofBlockEntity(World world, BlockPos pos) {
		this(EEEntities.BOOF_BLOCK.get(), world);
		this.setPosition(pos.getX() + 0.5F, pos.getY() - 0.375F, pos.getZ() + 0.5F);
		this.setOrigin(pos);
	}

	@Override
	protected void registerData() {
		this.dataManager.register(ORIGIN, BlockPos.ZERO);
		this.dataManager.register(FOR_PROJECTILE, false);
	}

	@Override
	public void tick() {
		AxisAlignedBB bb = this.getBoundingBox().grow(0, 0.25F, 0);
		List<Entity> entities = this.world.getEntitiesWithinAABB(Entity.class, bb, (entity -> !entity.isPassenger()));
		for (Entity entity : entities) {
			if (!EETags.EntityTypes.BOOF_BLOCK_RESISTANT.contains(entity.getType())) {
				if (entity instanceof AbstractArrowEntity) {
					this.setForProjectile(true);
					this.world.setBlockState(getOrigin(), Blocks.AIR.getDefaultState());
					entity.addVelocity(MathHelper.sin((float) (entity.rotationYaw * Math.PI / 180.0F)) * 3 * 0.1F, 0.55D, -MathHelper.cos((float) (entity.rotationYaw * Math.PI / 180.0F)) * 3 * 0.1F);
				} else {
					if (entity.getPosY() - 0.45F >= this.getPosY()) {
						entity.addVelocity(0, this.rand.nextFloat() * 0.05D + 0.35D, 0);
					} else if (entity.getPosY() < this.getPosY() - 1F) {
						entity.addVelocity(0, this.rand.nextFloat() * -0.05D - 0.35D, 0);
					} else {
						float amount = 0.5F;
						Vector3d result = entity.getPositionVec().subtract(this.getPositionVec());
						entity.addVelocity(result.x * amount, this.rand.nextFloat() * 0.45D + 0.25D, result.z * amount);
					}
				}
				if (!(entity instanceof PlayerEntity)) {
					entity.velocityChanged = true;
				}
			}
		}

		if (this.ticksExisted >= 10) {
			if (this.world.isAreaLoaded(this.getOrigin(), 1) && this.world.getBlockState(getOrigin()).getBlock() == EEBlocks.BOOF_BLOCK.get() && !this.isForProjectile()) {
				this.world.setBlockState(this.getOrigin(), EEBlocks.BOOF_BLOCK.get().getDefaultState());
			} else if (this.world.isAreaLoaded(this.getOrigin(), 1) && this.isForProjectile()) {
				this.world.setBlockState(this.getOrigin(), EEBlocks.BOOF_BLOCK.get().getDefaultState());
			}
			this.remove();
		}
	}

	@Override
	public void readAdditional(CompoundNBT nbt) {
		this.setOrigin(new BlockPos(nbt.getInt("OriginX"), nbt.getInt("OriginY"), nbt.getInt("OriginZ")));
		this.setForProjectile(nbt.getBoolean("ForProjectile"));
	}

	@Override
	public void writeAdditional(CompoundNBT nbt) {
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
	public boolean attackEntityFrom(DamageSource source, float amount) {
		return false;
	}

	public boolean isForProjectile() {
		return this.getDataManager().get(FOR_PROJECTILE);
	}

	@Override
	public boolean canRenderOnFire() {
		return false;
	}

	public void setForProjectile(boolean forProjectile) {
		this.getDataManager().set(FOR_PROJECTILE, forProjectile);
	}

	public void setOrigin(BlockPos pos) {
		this.getDataManager().set(ORIGIN, pos);
	}

	public BlockPos getOrigin() {
		return this.getDataManager().get(ORIGIN);
	}

	@Override
	public boolean canBePushed() {
		return false;
	}

	@Override
	public boolean isPushedByWater() {
		return false;
	}

	@Override
	public IPacket<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}