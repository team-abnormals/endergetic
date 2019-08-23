package endergeticexpansion.common.tileentities;

import endergeticexpansion.core.registry.EETileEntities;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;

public class TileEntityPuffBugHive extends TileEntity {

	public TileEntityPuffBugHive() {
		super(EETileEntities.PUFFBUG_HIVE);
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
