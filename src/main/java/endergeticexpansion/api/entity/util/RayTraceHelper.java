package endergeticexpansion.api.entity.util;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;

public class RayTraceHelper {

	public static RayTraceResult rayTrace(Entity entity, double distance, float delta) {
		return entity.world.rayTraceBlocks(new RayTraceContext(
			entity.getEyePosition(delta),
			entity.getEyePosition(delta).add(entity.getLook(delta).scale(distance)),
			RayTraceContext.BlockMode.OUTLINE,
			RayTraceContext.FluidMode.NONE,
			entity
		));
	}
	
}