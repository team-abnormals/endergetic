package endergeticexpansion.api.entity.util;

import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public class AdvancedAxisAllignedBB extends AxisAlignedBB {

	public AdvancedAxisAllignedBB(BlockPos pos) {
		super(pos);
	}
	
	public static AxisAlignedBB expandDownwards(AxisAlignedBB bb, double y) {
		double d0 = bb.minX;
		double d1 = bb.minY - y;
		double d2 = bb.minZ;
		double d3 = bb.maxX;
		double d4 = bb.maxY;
		double d5 = bb.maxZ;
		return new AxisAlignedBB(d0, d1, d2, d3, d4, d5);
	}
	
	public static AxisAlignedBB checkOnGround(AxisAlignedBB bb) {
		double d0 = bb.minX;
		double d1 = bb.minY - 0.07F;
		double d2 = bb.minZ;
		double d3 = bb.maxX;
		double d4 = bb.maxY - 1.0F;
		double d5 = bb.maxZ;
		return new AxisAlignedBB(d0, d1, d2, d3, d4, d5);
	}

}