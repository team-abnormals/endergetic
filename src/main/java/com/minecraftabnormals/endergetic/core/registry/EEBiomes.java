package com.minecraftabnormals.endergetic.core.registry;

import java.util.Iterator;
import java.util.List;
import java.util.function.BiPredicate;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.minecraftabnormals.abnormals_core.common.world.modification.BiomeFeatureModifier;
import com.minecraftabnormals.abnormals_core.common.world.modification.BiomeModificationManager;
import com.minecraftabnormals.abnormals_core.common.world.modification.BiomeModificationPredicates;
import com.minecraftabnormals.abnormals_core.common.world.modification.BiomeSpawnsModifier;
import com.minecraftabnormals.abnormals_core.core.util.registry.BiomeSubRegistryHelper;
import com.minecraftabnormals.endergetic.common.world.features.EEFeatures;
import com.minecraftabnormals.endergetic.common.world.placements.NoiseHeightmap32;
import com.minecraftabnormals.endergetic.common.world.surfacebuilders.EESurfaceBuilders;
import com.minecraftabnormals.endergetic.core.EndergeticExpansion;

import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.EntityClassification;
import net.minecraft.util.RegistryKey;
import net.minecraft.world.biome.*;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.feature.EndGatewayConfig;
import net.minecraft.world.gen.feature.Features;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.placement.AtSurfaceWithExtraConfig;
import net.minecraft.world.gen.placement.IPlacementConfig;
import net.minecraft.world.gen.placement.NoiseDependant;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.surfacebuilders.ConfiguredSurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EndergeticExpansion.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class EEBiomes {
	private static int TOTAL_WEIGHT;
	private static final List<Pair<RegistryKey<Biome>, Integer>> BIOME_WEIGHTS = Lists.newArrayList();
	private static final BiomeSubRegistryHelper HELPER = EndergeticExpansion.REGISTRY_HELPER.getBiomeSubHelper();

	public static final BiomeSubRegistryHelper.KeyedBiome POISE_FOREST = createPoiseForest();
	public static final BiomeSubRegistryHelper.KeyedBiome CHORUS_PLAINS = HELPER.createBiome("chorus_plains", BiomeMaker::makeEndMidlandsBiome);

	@SuppressWarnings("unchecked")
	public static void setupBiomeInfo() {
		BiomeDictionary.addTypes(POISE_FOREST.getKey(), BiomeDictionary.Type.END, BiomeDictionary.Type.MAGICAL, BiomeDictionary.Type.FOREST);
		BiomeDictionary.addTypes(CHORUS_PLAINS.getKey(), BiomeDictionary.Type.END);
		addEndBiome(POISE_FOREST.getKey(), 6);
		addEndBiome(CHORUS_PLAINS.getKey(), 15);

		//TODO: Possibly move to json?
		BiomeModificationManager modificationManager = BiomeModificationManager.INSTANCE;
		BiPredicate<RegistryKey<Biome>, Biome> poiseOnly = BiomeModificationPredicates.forBiomeKey(POISE_FOREST.getKey());
		modificationManager.addModifier(BiomeSpawnsModifier.createMultiSpawnAdder(poiseOnly, EntityClassification.CREATURE,
				Sets.newHashSet(
						new BiomeSpawnsModifier.SpawnInfo(() -> EEEntities.BOOFLO_ADOLESCENT.get(), 5, 1, 2),
						new BiomeSpawnsModifier.SpawnInfo(() -> EEEntities.BOOFLO.get(), 15, 1, 3),
						new BiomeSpawnsModifier.SpawnInfo(() -> EEEntities.PUFF_BUG.get(), 10, 2, 4)
				)
		));
		modificationManager.addModifier(BiomeFeatureModifier.createMultiFeatureAdder(poiseOnly, GenerationStage.Decoration.SURFACE_STRUCTURES,
				Sets.newHashSet(
						() -> EEFeatures.POISE_DOME.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).withPlacement(Placement.COUNT_EXTRA.configure(new AtSurfaceWithExtraConfig(3, 0.02F, 1))),
						() -> EEFeatures.POISE_TREE.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).withPlacement(Placement.COUNT_EXTRA.configure(new AtSurfaceWithExtraConfig(2, 0.05F, 1))),
						() -> EEFeatures.ENDERGETIC_GATEWAY.get().withConfiguration(EndGatewayConfig.func_214702_a(ServerWorld.field_241108_a_, true)).withPlacement(Placement.END_GATEWAY.configure(IPlacementConfig.NO_PLACEMENT_CONFIG))
				)
		));
		modificationManager.addModifier(BiomeFeatureModifier.createMultiFeatureAdder(poiseOnly, GenerationStage.Decoration.VEGETAL_DECORATION,
				Sets.newHashSet(
						() -> EEFeatures.POISE_CLUSTER.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(NoiseHeightmap32.INSTANCE.configure(new NoiseDependant(-0.8D, 4, 22))),
						() -> EEFeatures.PUFFBUG_HIVE.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(NoiseHeightmap32.INSTANCE.configure(new NoiseDependant(-0.8D, 9, 25))),
						() -> EEFeatures.BOLLOOM_BUD.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(NoiseHeightmap32.INSTANCE.configure(new NoiseDependant(-0.9D, 90, 90))),
						() -> EEFeatures.POISE_TALLGRASS.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(NoiseHeightmap32.INSTANCE.configure(new NoiseDependant(-0.8D, 0, 7))),
						() -> EEFeatures.POISE_GRASS.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Features.Placements.HEIGHTMAP_SPREAD_DOUBLE_PLACEMENT).withPlacement(Placement.COUNT_NOISE.configure(new NoiseDependant(-0.8D, 5, 10)))
				)
		));
	}

	/**
	 * Adds a new end biome with weight.
	 * <p>This method is safe to call during parallel mod loading.</p>
	 *
	 * @param key The {@link RegistryKey} of the {@link Biome} to add.
	 * @param weight The weight for the biome.
	 */
	public static synchronized void addEndBiome(RegistryKey<Biome> key, int weight) {
		TOTAL_WEIGHT += weight;
		BIOME_WEIGHTS.add(new Pair<>(key, weight));
	}

	public static BiomeSubRegistryHelper.KeyedBiome createPoiseForest() {
		Biome.Builder builder = new Biome.Builder();
		builder
				.withGenerationSettings(
						new BiomeGenerationSettings.Builder()
								.withSurfaceBuilder(() -> new ConfiguredSurfaceBuilder<>(EESurfaceBuilders.POISE_SURFACE_BUILDER.get(), SurfaceBuilder.END_STONE_CONFIG))
								.build()
				)
				.withMobSpawnSettings(
						new MobSpawnInfo.Builder()
								.withCreatureSpawnProbability(0.9F)
								.copy()
				)
				.setEffects(
						new BiomeAmbience.Builder()
								.withSkyColor(0)
								.setWaterColor(4159204)
								.setWaterFogColor(329011)
								.setFogColor(10518688)
								.setMoodSound(MoodSoundAmbience.DEFAULT_CAVE)
								.build()
				)
				.precipitation(Biome.RainType.NONE)
				.category(Biome.Category.THEEND)
				.depth(0.1F)
				.scale(0.2F)
				.temperature(0.5F)
				.downfall(0.5F);
		Biome biome = builder.build();
		return HELPER.createBiome("poise_forest", () -> biome);
	}

	public static RegistryKey<Biome> getRandomBiome(INoiseRandom context) {
		Iterator<Pair<RegistryKey<Biome>, Integer>> iterator = BIOME_WEIGHTS.iterator();
		RegistryKey<Biome> biome;
		int randomTotal = context.random(TOTAL_WEIGHT);
		do {
			Pair<RegistryKey<Biome>, Integer> entry = iterator.next();
			biome = entry.getFirst();
			randomTotal -= entry.getSecond();
		}
		while (randomTotal >= 0);
		return biome;
	}
}