package com.teamabnormals.endergetic.core.registry;

import com.mojang.serialization.Codec;
import com.teamabnormals.endergetic.common.levelgen.placement.HeightmapSpreadDoublePlacement;
import com.teamabnormals.endergetic.common.levelgen.placement.HeightmapSpreadLowerPlacement;
import com.teamabnormals.endergetic.common.levelgen.placement.NoiseHeightmap32Placement;
import com.teamabnormals.endergetic.core.EndergeticExpansion;
import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public final class EEPlacementModifierTypes {
	public static final DeferredRegister<PlacementModifierType<?>> PLACEMENT_MODIFIER_TYPES = DeferredRegister.create(Registry.PLACEMENT_MODIFIER_REGISTRY, EndergeticExpansion.MOD_ID);

	public static final RegistryObject<PlacementModifierType<HeightmapSpreadDoublePlacement>> HEIGHTMAP_SPREAD_DOUBLE = register("heightmap_spread_double", HeightmapSpreadDoublePlacement.CODEC);
	public static final RegistryObject<PlacementModifierType<NoiseHeightmap32Placement>> NOISE_HEIGHTMAP_32 = register("noise_heightmap_32", NoiseHeightmap32Placement.CODEC);
	public static final RegistryObject<PlacementModifierType<HeightmapSpreadLowerPlacement>> HEIGHTMAP_SPREAD_LOWER = register("heightmap_spread_lower", HeightmapSpreadLowerPlacement.CODEC);

	private static <P extends PlacementModifier> RegistryObject<PlacementModifierType<P>> register(String name, Codec<P> codec) {
		return PLACEMENT_MODIFIER_TYPES.register(name, () -> (PlacementModifierType<P>) () -> codec);
	}
}
