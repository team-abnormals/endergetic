package com.teamabnormals.endergetic.core.registry;

import com.mojang.serialization.Codec;
import com.teamabnormals.endergetic.common.levelgen.placement.HeightmapSpreadDoublePlacement;
import com.teamabnormals.endergetic.common.levelgen.placement.HeightmapSpreadLowerPlacement;
import com.teamabnormals.endergetic.common.levelgen.placement.NoiseHeightmap32Placement;
import com.teamabnormals.endergetic.common.levelgen.placement.NoiseRarityFilter;
import com.teamabnormals.endergetic.core.EndergeticExpansion;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public final class EEPlacementModifierTypes {
	public static final DeferredRegister<PlacementModifierType<?>> PLACEMENT_MODIFIER_TYPES = DeferredRegister.create(Registries.PLACEMENT_MODIFIER_TYPE, EndergeticExpansion.MOD_ID);

	public static final RegistryObject<PlacementModifierType<HeightmapSpreadDoublePlacement>> HEIGHTMAP_SPREAD_DOUBLE = register("heightmap_spread_double", HeightmapSpreadDoublePlacement.CODEC);
	public static final RegistryObject<PlacementModifierType<NoiseHeightmap32Placement>> NOISE_HEIGHTMAP_32 = register("noise_heightmap_32", NoiseHeightmap32Placement.CODEC);
	public static final RegistryObject<PlacementModifierType<HeightmapSpreadLowerPlacement>> HEIGHTMAP_SPREAD_LOWER = register("heightmap_spread_lower", HeightmapSpreadLowerPlacement.CODEC);
	public static final RegistryObject<PlacementModifierType<NoiseRarityFilter>> NOISE_RARITY_FILTER = register("noise_rarity_filter", NoiseRarityFilter.CODEC);

	private static <P extends PlacementModifier> RegistryObject<PlacementModifierType<P>> register(String name, Codec<P> codec) {
		return PLACEMENT_MODIFIER_TYPES.register(name, () -> () -> codec);
	}
}
