package endergeticexpansion.common.tileentities;

import javax.annotation.Nullable;

import endergeticexpansion.core.registry.EETileEntities;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;

public class TileEntityPuffBugHive extends TileEntity {
	private int bugsAtHive;

	public TileEntityPuffBugHive() {
		super(EETileEntities.PUFFBUG_HIVE);
	}
	
	public void incrementBugsAtHive() {
		this.bugsAtHive++;
	}
	
	public void decrementBugsAtHive() {
		this.bugsAtHive--;
	}
	
	public int getBugsAtHive() {
		return this.bugsAtHive;
	}
	
	public boolean isHiveFull() {
		return this.getBugsAtHive() >= 5;
	}
	
	@Override
	public CompoundNBT write(CompoundNBT compound) {
		super.write(compound);
		compound.putInt("BugsAtHive", this.getBugsAtHive());
		return compound;
	}
	
	@Override
	public void read(CompoundNBT compound) {
		super.read(compound);
		this.bugsAtHive = compound.getInt("BugsAtHive");
	}
	
	@Nullable
	public SUpdateTileEntityPacket getUpdatePacket() {
		return new SUpdateTileEntityPacket(this.pos, 9, this.getUpdateTag());
	}
	
	public CompoundNBT getUpdateTag() {
		return this.write(new CompoundNBT());
	}
	
	public boolean onlyOpsCanSetNbt() {
		return true;
	}
	
	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		return super.getRenderBoundingBox().grow(1084);
	}
	
	@Override
	public double getMaxRenderDistanceSquared() {
		return 16384.0D;
	}
}
