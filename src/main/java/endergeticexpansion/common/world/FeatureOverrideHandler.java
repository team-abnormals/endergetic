package endergeticexpansion.common.world;

import endergeticexpansion.common.world.features.EEFeatures;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.dimension.EndDimension;
import net.minecraft.world.gen.GenerationStage.Decoration;
import net.minecraft.world.gen.feature.EndGatewayConfig;
import net.minecraft.world.gen.placement.IPlacementConfig;
import net.minecraft.world.gen.placement.Placement;

public class FeatureOverrideHandler {

	@SuppressWarnings("unchecked")
	public static void overrideFeatures() {
		Biomes.END_HIGHLANDS.getFeatures(Decoration.SURFACE_STRUCTURES).set(0, Biome.createDecoratedFeature(EEFeatures.ENDERGETIC_GATEWAY, EndGatewayConfig.func_214702_a(EndDimension.SPAWN, true), Placement.END_GATEWAY, IPlacementConfig.NO_PLACEMENT_CONFIG));
	}
	
}
