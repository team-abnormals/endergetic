package com.minecraftabnormals.endergetic.common.world;

import com.minecraftabnormals.endergetic.common.world.features.EEFeatures;

import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.GenerationStage.Decoration;
import net.minecraft.world.gen.feature.EndGatewayConfig;
import net.minecraft.world.gen.placement.IPlacementConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.server.ServerWorld;

public final class EEWorldGenHandler {

	public static void overrideFeatures() {
		Biomes.END_HIGHLANDS.getFeatures(Decoration.SURFACE_STRUCTURES).set(0, EEFeatures.ENDERGETIC_GATEWAY.get().withConfiguration(EndGatewayConfig.func_214702_a(ServerWorld.field_241108_a_, true)).withPlacement(Placement.END_GATEWAY.configure(IPlacementConfig.NO_PLACEMENT_CONFIG)));
	}
	
}