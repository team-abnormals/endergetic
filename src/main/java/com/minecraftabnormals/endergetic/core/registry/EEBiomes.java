package com.minecraftabnormals.endergetic.core.registry;

import java.util.Iterator;
import java.util.List;
import java.util.function.BiPredicate;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.minecraftabnormals.abnormals_core.common.world.modification.*;
import com.minecraftabnormals.abnormals_core.core.util.registry.BiomeSubRegistryHelper;
import com.minecraftabnormals.endergetic.common.world.features.EEFeatures;
import com.minecraftabnormals.endergetic.common.world.surfacebuilders.EESurfaceBuilders;
import com.minecraftabnormals.endergetic.core.EndergeticExpansion;

import com.mojang.datafixers.util.Pair;
import net.minecraft.util.RegistryKey;
import net.minecraft.world.biome.*;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.INoiseRandom;
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

		BiomeModificationManager modificationManager = BiomeModificationManager.INSTANCE;
		BiPredicate<RegistryKey<Biome>, Biome> poiseOnly = BiomeModificationPredicates.forBiomeKey(POISE_FOREST.getKey());
		modificationManager.addModifier(BiomeSpawnsModifier.createMultiSpawnAdder(poiseOnly, EEEntities.END_CREATURE,
				Sets.newHashSet(
						new BiomeSpawnsModifier.SpawnInfo(EEEntities.BOOFLO_ADOLESCENT, 5, 1, 2),
						new BiomeSpawnsModifier.SpawnInfo(EEEntities.BOOFLO, 15, 1, 3),
						new BiomeSpawnsModifier.SpawnInfo(EEEntities.PUFF_BUG, 10, 2, 4)
				)
		));
		modificationManager.addModifier(BiomeFeatureModifier.createMultiFeatureAdder(poiseOnly, GenerationStage.Decoration.SURFACE_STRUCTURES,
				Sets.newHashSet(
						() -> EEFeatures.Configs.POISE_DOME,
						() -> EEFeatures.Configs.POISE_TREE,
						() -> EEFeatures.Configs.END_GATEWAY
				)
		));
		modificationManager.addModifier(BiomeFeatureModifier.createMultiFeatureAdder(poiseOnly, GenerationStage.Decoration.VEGETAL_DECORATION,
				Sets.newHashSet(
						() -> EEFeatures.Configs.POISE_CLUSTER,
						() -> EEFeatures.Configs.PUFFBUG_HIVE,
						() -> EEFeatures.Configs.BOLLOOM_BUD,
						() -> EEFeatures.Configs.TALL_POISE_GRASS,
						() -> EEFeatures.Configs.POISE_GRASS
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
								.withSurfaceBuilder(() -> EESurfaceBuilders.Configs.POISE_FOREST)
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
		return HELPER.createBiome("poise_forest", builder::build);
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