package endergeticexpansion.common.entities.bolloom;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import endergeticexpansion.core.registry.EEEntities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.item.DyeColor;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;

public class EntityBolloomKnot extends Entity {
	protected BlockPos hangingPosition;
	private static final DataParameter<Integer> BALLOONS_TIED = EntityDataManager.createKey(EntityBolloomKnot.class, DataSerializers.VARINT);
	
	public EntityBolloomKnot(EntityType<? extends EntityBolloomKnot> entityTypeIn, World world) {
		super(entityTypeIn, world);
	}
	
	public EntityBolloomKnot(World world, BlockPos pos) {
		this(EEEntities.BOLLOOM_KNOT, world);
		this.setPosition(pos.getX() + 0.5F, pos.getY() + 0.9F, pos.getZ() + 0.5F);
		this.hangingPosition = pos;
		this.setMotion(Vec3d.ZERO);
		this.forceSpawn = true;
	}
	
	public EntityBolloomKnot(FMLPlayMessages.SpawnEntity spawnEntity, World world) {
		this(EEEntities.BOLLOOM_KNOT, world);
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public void tick() {
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		if (!this.world.isRemote) {
			if(this.getEntityWorld().isAreaLoaded(getHangingPos(), 1)) {
				if (!this.removed && !this.onValidBlock()) {
					this.remove();
				}
			}
		}
		if(this.getBalloonsTied() <= 0) {
			this.remove();
		}
	}
	
	@Nullable
    public static EntityBolloomKnot getKnotForPosition(World worldIn, BlockPos pos) {
        for (EntityBolloomKnot entity : worldIn.getEntitiesWithinAABB(EntityBolloomKnot.class, new AxisAlignedBB(pos))) {
            if (entity.getHangingPos().equals(pos)) {
                return entity;
            }
        }
        return null;
    }
	
	public static void createStartingKnot(World world, BlockPos pos, DyeColor balloonColor) {
		EntityBolloomKnot knot = new EntityBolloomKnot(world, pos);
		EntityBolloomBalloon balloon = new EntityBolloomBalloon(world, knot.getUniqueID(), pos, 0);
		knot.setBalloonsTied(1);
		balloon.setColor(balloonColor);
		world.addEntity(knot);
		world.addEntity(balloon);
	}
	
	public void addBalloon(DyeColor balloonColor) {
		EntityBolloomBalloon balloon = new EntityBolloomBalloon(this.getEntityWorld(), this.getUniqueID(), this.getHangingPos(), 0.1F);
		balloon.setColor(balloonColor);
		world.addEntity(balloon);
		this.setBalloonsTied(this.getBalloonsTied() + 1);
	}
	
	protected boolean onValidBlock() {
		return this.getEntityWorld().getBlockState(this.hangingPosition).getBlock().isIn(BlockTags.FENCES);
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
		this.getDataManager().set(BALLOONS_TIED, nbt.getInt("Ballons_Tied"));
	}

	@Override
	protected void writeAdditional(CompoundNBT nbt) {
		BlockPos blockpos = this.getHangingPos();
		nbt.putInt("TileX", blockpos.getX());
		nbt.putInt("TileY", blockpos.getY());
		nbt.putInt("TileZ", blockpos.getZ());
		nbt.putInt("Ballons_Tied", this.getDataManager().get(BALLOONS_TIED));
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
