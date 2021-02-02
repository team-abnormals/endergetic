package com.minecraftabnormals.endergetic.common.world.placements;

import com.minecraftabnormals.endergetic.core.EndergeticExpansion;
import net.minecraft.world.gen.placement.NoPlacementConfig;
import net.minecraft.world.gen.placement.NoiseDependant;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public final class EEPlacements {
	public static final DeferredRegister<Placement<?>> PLACEMENTS = DeferredRegister.create(ForgeRegistries.DECORATORS, EndergeticExpansion.MOD_ID);

	public static final RegistryObject<NoiseHeightmap32Placement> NOISE_HEIGHTMAP_32 = PLACEMENTS.register("noise_heightmap_32", () -> new NoiseHeightmap32Placement(NoiseDependant.CODEC));
	public static final RegistryObject<HeightmapSpreadLowerPlacement> HEIGHTMAP_SPREAD_LOWER = PLACEMENTS.register("heightmap_spread_lower", () -> new HeightmapSpreadLowerPlacement(NoPlacementConfig.CODEC));
}
