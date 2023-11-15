package com.teamabnormals.endergetic.core.data.server;

import com.teamabnormals.endergetic.common.world.biome.modifiers.SmallEndIslandsAmbienceBiomeModifier;
import com.teamabnormals.endergetic.core.EndergeticExpansion;
import com.teamabnormals.endergetic.core.registry.EEEntities;
import com.teamabnormals.endergetic.core.registry.EEFeatures;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.JsonCodecProvider;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class EEBiomeModifierProvider {

	public static JsonCodecProvider<BiomeModifier> create(DataGenerator dataGenerator, ExistingFileHelper existingFileHelper) {
		RegistryAccess access = RegistryAccess.builtinCopy();
		Registry<Biome> biomeRegistry = access.registryOrThrow(Registry.BIOME_REGISTRY);
		Registry<PlacedFeature> placedFeatures = access.registryOrThrow(Registry.PLACED_FEATURE_REGISTRY);

		HolderSet<Biome> endHighlandsOrMidlands = HolderSet.direct(List.of(biomeRegistry.getHolderOrThrow(Biomes.END_HIGHLANDS), biomeRegistry.getHolderOrThrow(Biomes.END_MIDLANDS)));
		HashMap<ResourceLocation, BiomeModifier> modifiers = new HashMap<>();
//		addModifier(modifiers, "add_eetle_spawns", new ForgeBiomeModifiers.AddSpawnsBiomeModifier(endHighlandsOrMidlands, List.of(new MobSpawnSettings.SpawnerData(EEEntities.CHARGER_EETLE.get(), 12, 2, 5), new MobSpawnSettings.SpawnerData(EEEntities.CHARGER_EETLE.get(), 8, 2, 4))));
//
//		addModifier(modifiers, "add_corrock_vegetation", new ForgeBiomeModifiers.AddFeaturesBiomeModifier(endHighlandsOrMidlands, of(placedFeatures, EEFeatures.Placed.CORROCK_PATCH, EEFeatures.Placed.EETLE_EGG_PATCH), GenerationStep.Decoration.VEGETAL_DECORATION));
//
//		addModifier(modifiers, "add_corrock_surface_structures", new ForgeBiomeModifiers.AddFeaturesBiomeModifier(endHighlandsOrMidlands, of(placedFeatures, EEFeatures.Placed.CORROCK_BRANCH, EEFeatures.Placed.CORROCK_TOWER, EEFeatures.Placed.CORROCK_SHELF, EEFeatures.Placed.CORROCK_ARCH, EEFeatures.Placed.EUMUS_PATCH, EEFeatures.Placed.SPECKLED_CORROCK_PATCH), GenerationStep.Decoration.SURFACE_STRUCTURES));
//
//		HolderSet<Biome> endMidlands = HolderSet.direct(biomeRegistry.getHolderOrThrow(Biomes.END_MIDLANDS));
//		addModifier(modifiers, "add_sparse_corrock_branch", new ForgeBiomeModifiers.AddFeaturesBiomeModifier(endMidlands, of(placedFeatures, EEFeatures.Placed.SPARSE_CORROCK_BRANCH), GenerationStep.Decoration.SURFACE_STRUCTURES));

		addModifier(modifiers, "small_end_islands_ambience", SmallEndIslandsAmbienceBiomeModifier.INSTANCE);
		return JsonCodecProvider.forDatapackRegistry(dataGenerator, existingFileHelper, EndergeticExpansion.MOD_ID, RegistryOps.create(JsonOps.INSTANCE, access), ForgeRegistries.Keys.BIOME_MODIFIERS, modifiers);
	}

	private static void addModifier(HashMap<ResourceLocation, BiomeModifier> modifiers, String name, BiomeModifier modifier) {
		modifiers.put(new ResourceLocation(EndergeticExpansion.MOD_ID, name), modifier);
	}

	@SafeVarargs
	@SuppressWarnings("ConstantConditions")
	private static HolderSet<PlacedFeature> of(Registry<PlacedFeature> placedFeatures, RegistryObject<PlacedFeature>... features) {
		return HolderSet.direct(Stream.of(features).map(registryObject -> placedFeatures.getOrCreateHolderOrThrow(registryObject.getKey())).collect(Collectors.toList()));
	}

}
