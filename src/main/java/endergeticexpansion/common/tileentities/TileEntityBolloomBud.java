package endergeticexpansion.common.tileentities;

import endergeticexpansion.core.registry.EETileEntities;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;

public class TileEntityBolloomBud extends TileEntity {

	public TileEntityBolloomBud() {
		super(EETileEntities.BOLLOOM_BUD);
	}
	
	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		return super.getRenderBoundingBox().grow(1.0F);
	}
	
	@Override
	public double getMaxRenderDistanceSquared() {
		return super.getMaxRenderDistanceSquared() * 2;
	}

}
