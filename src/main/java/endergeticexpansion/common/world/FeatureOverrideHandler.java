package endergeticexpansion.common.world;

import com.google.common.collect.ImmutableList;

import endergeticexpansion.common.world.features.EEFeatures;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.dimension.EndDimension;
import net.minecraft.world.gen.GenerationStage.Decoration;
import net.minecraft.world.gen.feature.EndGatewayConfig;
import net.minecraft.world.gen.feature.EndSpikeFeatureConfig;
import net.minecraft.world.gen.placement.IPlacementConfig;
import net.minecraft.world.gen.placement.Placement;

public class FeatureOverrideHandler {

	public static void overrideFeatures() {
		Biomes.END_HIGHLANDS.getFeatures(Decoration.SURFACE_STRUCTURES).set(0, Biome.createDecoratedFeature(EEFeatures.ENDERGETIC_GATEWAY, EndGatewayConfig.func_214702_a(EndDimension.SPAWN, true), Placement.END_GATEWAY, IPlacementConfig.NO_PLACEMENT_CONFIG));
		Biomes.THE_END.getFeatures(Decoration.SURFACE_STRUCTURES).set(0, Biome.createDecoratedFeature(EEFeatures.ENDERGETIC_END_SPIKE, new EndSpikeFeatureConfig(false, ImmutableList.of(), (BlockPos)null), Placement.NOPE, IPlacementConfig.NO_PLACEMENT_CONFIG));
	}
	
}
