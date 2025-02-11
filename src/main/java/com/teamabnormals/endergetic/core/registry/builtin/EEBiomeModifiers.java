package com.teamabnormals.endergetic.core.registry.builtin;

import com.teamabnormals.endergetic.common.levelgen.biome.modifiers.SmallEndIslandsAmbienceBiomeModifier;
import com.teamabnormals.endergetic.core.EndergeticExpansion;
import com.teamabnormals.endergetic.core.registry.EEEntityTypes;
import com.teamabnormals.endergetic.core.registry.EEFeatures.EEPlacedFeatures;
import com.teamabnormals.endergetic.core.other.tags.EEBiomeTags;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.GenerationStep.Decoration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers.AddFeaturesBiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers.AddSpawnsBiomeModifier;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class EEBiomeModifiers {

	public static void bootstrap(BootstapContext<BiomeModifier> context) {
		addSpawn(context, "add_eetle_spawns", EEBiomeTags.HAS_EETLE, new MobSpawnSettings.SpawnerData(EEEntityTypes.CHARGER_EETLE.get(), 12, 2, 5), new MobSpawnSettings.SpawnerData(EEEntityTypes.GLIDER_EETLE.get(), 8, 2, 4));
		addSpawn(context, "add_purpoid_spawns", EEBiomeTags.HAS_PURPOID, new MobSpawnSettings.SpawnerData(EEEntityTypes.PURPOID.get(), 12, 1, 6));
		addFeature(context, "add_corrock_vegetation", EEBiomeTags.HAS_CORROCK, GenerationStep.Decoration.VEGETAL_DECORATION, EEPlacedFeatures.CORROCK_PATCH, EEPlacedFeatures.EETLE_EGG_PATCH);
		addFeature(context, "add_corrock_surface_structures", EEBiomeTags.HAS_CORROCK, GenerationStep.Decoration.SURFACE_STRUCTURES, EEPlacedFeatures.CORROCK_BRANCH, EEPlacedFeatures.CORROCK_TOWER, EEPlacedFeatures.CORROCK_SHELF, EEPlacedFeatures.CORROCK_ARCH, EEPlacedFeatures.EUMUS_PATCH, EEPlacedFeatures.SPECKLED_CORROCK_PATCH);
		addFeature(context, "add_sparse_corrock_branch", EEBiomeTags.HAS_SPARSE_CORROCK, GenerationStep.Decoration.SURFACE_STRUCTURES, EEPlacedFeatures.SPARSE_CORROCK_BRANCH);

		register(context, "small_end_islands_ambience", () -> SmallEndIslandsAmbienceBiomeModifier.INSTANCE);
	}

	@SafeVarargs
	private static void addFeature(BootstapContext<BiomeModifier> context, String name, TagKey<Biome> biomes, Decoration step, ResourceKey<PlacedFeature>... features) {
		register(context, "add_feature/" + name, () -> new AddFeaturesBiomeModifier(context.lookup(Registries.BIOME).getOrThrow(biomes), featureSet(context, features), step));
	}

	private static void addSpawn(BootstapContext<BiomeModifier> context, String name, TagKey<Biome> biomes, MobSpawnSettings.SpawnerData... spawns) {
		register(context, "add_spawn/" + name, () -> new AddSpawnsBiomeModifier(context.lookup(Registries.BIOME).getOrThrow(biomes), List.of(spawns)));
	}

	@SafeVarargs
	private static HolderSet<PlacedFeature> featureSet(BootstapContext<?> context, ResourceKey<PlacedFeature>... features) {
		return HolderSet.direct(Stream.of(features).map(placedFeatureKey -> context.lookup(Registries.PLACED_FEATURE).getOrThrow(placedFeatureKey)).collect(Collectors.toList()));
	}

	private static void register(BootstapContext<BiomeModifier> context, String name, Supplier<? extends BiomeModifier> modifier) {
		context.register(ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, new ResourceLocation(EndergeticExpansion.MOD_ID, name)), modifier.get());
	}
}
