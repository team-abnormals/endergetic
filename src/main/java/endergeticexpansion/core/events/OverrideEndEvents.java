package endergeticexpansion.core.events;

import net.minecraftforge.common.DimensionManager;

public class OverrideEndEvents {

	public static void init() {
		DimensionManager.unregisterDimension(1);
	}
	
}
