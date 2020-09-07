package com.minecraftabnormals.endergetic.common.entities.bolloom;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.minecraftabnormals.endergetic.core.registry.EEEntities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;

public class BolloomKnotEntity extends Entity {
	private BlockPos hangingPosition;
	private static final DataParameter<Integer> BALLOONS_TIED = EntityDataManager.createKey(BolloomKnotEntity.class, DataSerializers.VARINT);
	
	public BolloomKnotEntity(EntityType<? extends BolloomKnotEntity> entityType, World world) {
		super(entityType, world);
	}
	
	public BolloomKnotEntity(World world, BlockPos pos) {
		this(EEEntities.BOLLOOM_KNOT.get(), world);
		this.setPosition(pos.getX() + 0.5F, pos.getY() + 0.9F, pos.getZ() + 0.5F);
		this.hangingPosition = pos;
		this.setMotion(Vector3d.ZERO);
		this.forceSpawn = true;
	}
	
	public BolloomKnotEntity(FMLPlayMessages.SpawnEntity spawnEntity, World world) {
		this(EEEntities.BOLLOOM_KNOT.get(), world);
	}
	
	@Override
	public void tick() {
		this.prevPosX = this.getPosX();
		this.prevPosY = this.getPosY();
		this.prevPosZ = this.getPosZ();
		if (!this.world.isRemote && this.isAlive() && this.world.isAreaLoaded(this.getHangingPos(), 1) && !this.onValidBlock()) {
			this.remove();
		} else if (this.getBalloonsTied() <= 0) {
			this.remove();
		}
	}
	
	@Nullable
    public static BolloomKnotEntity getKnotForPosition(World worldIn, BlockPos pos) {
        for (BolloomKnotEntity entity : worldIn.getEntitiesWithinAABB(BolloomKnotEntity.class, new AxisAlignedBB(pos))) {
            if (entity.getHangingPos().equals(pos)) {
                return entity;
            }
        }
        return null;
    }
	
	public static void createStartingKnot(World world, BlockPos pos, BalloonColor balloonColor) {
		BolloomKnotEntity knot = new BolloomKnotEntity(world, pos);
		BolloomBalloonEntity balloon = new BolloomBalloonEntity(world, knot.getUniqueID(), pos, 0);
		knot.setBalloonsTied(1);
		balloon.setColor(balloonColor);
		world.addEntity(knot);
		world.addEntity(balloon);
	}
	
	public void addBalloon(BalloonColor balloonColor) {
		BolloomBalloonEntity balloon = new BolloomBalloonEntity(this.world, this.getUniqueID(), this.getHangingPos(), 0.1F);
		balloon.setColor(balloonColor);
		this.world.addEntity(balloon);
		this.setBalloonsTied(this.getBalloonsTied() + 1);
	}
	
	private boolean onValidBlock() {
		return this.world.getBlockState(this.hangingPosition).getBlock().isIn(BlockTags.FENCES);
	}
	
	public boolean canBeCollidedWith() {
		return true;
	}
	
	@Override
	protected boolean shouldSetPosAfterLoading() {
		return false;
	}
	
	@Override
	public void setPosition(double x, double y, double z) {
		this.hangingPosition = new BlockPos(x, y, z);
		super.setPosition(x, y, z);
	}
	
	@OnlyIn(Dist.CLIENT)
	public boolean isInRangeToRenderDist(double distance) {
		return distance < 1024.0D;
	}

	@Override
	protected void registerData() {
		this.dataManager.register(BALLOONS_TIED, 0);
	}

	@Override
	protected void readAdditional(CompoundNBT nbt) {
		this.hangingPosition = new BlockPos(nbt.getInt("TileX"), nbt.getInt("TileY"), nbt.getInt("TileZ"));
		this.setBalloonsTied(nbt.getInt("Ballons_Tied"));
	}

	@Override
	protected void writeAdditional(CompoundNBT nbt) {
		BlockPos blockpos = this.getHangingPos();
		nbt.putInt("TileX", blockpos.getX());
		nbt.putInt("TileY", blockpos.getY());
		nbt.putInt("TileZ", blockpos.getZ());
		//Cursed NBT name, rename in 1.16.2
		nbt.putInt("Ballons_Tied", this.getBalloonsTied());
	}
	
	public BlockPos getHangingPos() {
		return this.hangingPosition;
	}
	
	public void setBalloonsTied(int amount) {
		this.getDataManager().set(BALLOONS_TIED, amount);
	}
	
	public int getBalloonsTied() {
		return this.getDataManager().get(BALLOONS_TIED);
	}
	
	public boolean hasMaxBalloons() {
		return this.getDataManager().get(BALLOONS_TIED) > 3;
	}

	@Nonnull
	@Override
	public IPacket<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}
