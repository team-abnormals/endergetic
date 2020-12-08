package com.minecraftabnormals.endergetic.common.world.placements;

import com.minecraftabnormals.endergetic.core.EndergeticExpansion;
import net.minecraft.world.gen.placement.NoiseDependant;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public final class EEPlacements {
	public static final DeferredRegister<Placement<?>> PLACEMENTS = DeferredRegister.create(ForgeRegistries.DECORATORS, EndergeticExpansion.MOD_ID);

	public static final RegistryObject<NoiseHeightmap32> NOISE_HEIGHTMAP_32 = PLACEMENTS.register("noise_heightmap_32", () -> new NoiseHeightmap32(NoiseDependant.CODEC));
}
