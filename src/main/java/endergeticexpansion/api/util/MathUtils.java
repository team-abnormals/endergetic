package endergeticexpansion.api.util;

import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;

public class MathUtils {

	/*
	 * Used to calculate center's with percents of AxisAllignedBBs
	 * To get a default middle value use 0.5D for all multipliers
	 */
    public static Vec3d getCenterAdjusted(AxisAlignedBB bb, double xMultiplier, double yMultiplier, double zMultiplier) {
        return new Vec3d(bb.minX + (bb.maxX - bb.minX) * xMultiplier, bb.minY + (bb.maxY - bb.minY) * yMultiplier, bb.minZ + (bb.maxZ - bb.minZ) * zMultiplier);
    }
	
}
