package endergeticexpansion.common.tileentities;

import java.util.Random;

import endergeticexpansion.common.blocks.poise.BlockBolloomBud;
import endergeticexpansion.common.entities.bolloom.EntityBolloomFruit;
import endergeticexpansion.core.registry.EETileEntities;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;

public class TileEntityBolloomBud extends TileEntity implements ITickableTileEntity {
	boolean markedForSpawning;

	public TileEntityBolloomBud() {
		super(EETileEntities.BOLLOOM_BUD.get());
	}
	
	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		return super.getRenderBoundingBox().grow(1.0F);
	}
	
	@Override
	public double getMaxRenderDistanceSquared() {
		return super.getMaxRenderDistanceSquared() * 2;
	}
	
	@Override
	public void tick() {
		if(this.isMarkedForSpawning() && !world.isRemote()) {
			Random rand = new Random();
			int heighte = rand.nextInt(7) + 1;
			int heightn = rand.nextInt(7) + 1;
			int heights = rand.nextInt(7) + 1;
			int heightw = rand.nextInt(7) + 1;
			if(this.getBlockState().get(BlockBolloomBud.HAS_NORTH_FRUIT)) {
				EntityBolloomFruit fruitN = new EntityBolloomFruit(world, pos, pos.north().up(heightn - 1), heightn, Direction.NORTH);
				fruitN.getDataManager().set(EntityBolloomFruit.GROWN, true);
				world.addEntity(fruitN);
			}
			if(this.getBlockState().get(BlockBolloomBud.HAS_SOUTH_FRUIT)) {
				EntityBolloomFruit fruitS = new EntityBolloomFruit(world, pos, pos.south().up(heights - 1), heights, Direction.SOUTH);
				fruitS.getDataManager().set(EntityBolloomFruit.GROWN, true);
				world.addEntity(fruitS);
			}
			if(this.getBlockState().get(BlockBolloomBud.HAS_EAST_FRUIT)) {
				EntityBolloomFruit fruitE = new EntityBolloomFruit(world, pos, pos.east().up(heighte - 1), heighte, Direction.EAST);
				fruitE.getDataManager().set(EntityBolloomFruit.GROWN, true);
				world.addEntity(fruitE);
			}
			if(this.getBlockState().get(BlockBolloomBud.HAS_WEST_FRUIT)) {
				EntityBolloomFruit fruitW = new EntityBolloomFruit(world, pos, pos.west().up(heightw - 1), heightw, Direction.WEST);
				fruitW.getDataManager().set(EntityBolloomFruit.GROWN, true);
				world.addEntity(fruitW);
			}
			this.markedForSpawning = false;
		}
	}
	
	@Override
	public void read(CompoundNBT compound) {
		super.read(compound);
		this.markedForSpawning = compound.getBoolean("MarkedForSpawning");
	}
	
	@Override
	public CompoundNBT write(CompoundNBT compound) {
		super.write(compound);
		compound.putBoolean("MarkedForSpawning", this.isMarkedForSpawning());
		return compound;
	}
	
	public SUpdateTileEntityPacket getUpdatePacket() {
		return new SUpdateTileEntityPacket(this.pos, 11, this.getUpdateTag());
	}
	
	public boolean isMarkedForSpawning() {
		return this.markedForSpawning;
	}
	
	public void markForSpawning() {
		this.markedForSpawning = true;
	}
}
