package com.minecraftabnormals.endergetic.core.registry;

import java.util.function.BiPredicate;

import com.google.common.collect.Sets;
import com.minecraftabnormals.abnormals_core.common.world.modification.*;
import com.minecraftabnormals.abnormals_core.core.util.BiomeUtil;
import com.minecraftabnormals.abnormals_core.core.util.registry.BiomeSubRegistryHelper;
import com.minecraftabnormals.endergetic.common.world.features.EEFeatures;
import com.minecraftabnormals.endergetic.common.world.surfacebuilders.EESurfaceBuilders;
import com.minecraftabnormals.endergetic.core.EndergeticExpansion;

import net.minecraft.sounds.Musics;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.biome.*;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.common.Mod;

import net.minecraft.world.level.biome.AmbientAdditionsSettings;
import net.minecraft.world.level.biome.AmbientMoodSettings;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.MobSpawnSettings;

@Mod.EventBusSubscriber(modid = EndergeticExpansion.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class EEBiomes {
	private static final BiomeSubRegistryHelper HELPER = EndergeticExpansion.REGISTRY_HELPER.getBiomeSubHelper();

	public static final BiomeSubRegistryHelper.KeyedBiome POISE_FOREST = createPoiseForest();

	@SuppressWarnings("unchecked")
	public static void setupBiomeInfo() {
		BiomeDictionary.addTypes(POISE_FOREST.getKey(), BiomeDictionary.Type.END, BiomeDictionary.Type.MAGICAL, BiomeDictionary.Type.FOREST);
		BiomeUtil.addEndBiome(POISE_FOREST.getKey(), 6);

		BiomeModificationManager modificationManager = BiomeModificationManager.INSTANCE;
		BiPredicate<ResourceKey<Biome>, Biome> poiseOnly = BiomeModificationPredicates.forBiomeKey(POISE_FOREST.getKey());
		modificationManager.addModifier(BiomeSpawnsModifier.createMultiSpawnAdder(poiseOnly, EEEntities.END_CREATURE,
				Sets.newHashSet(
						new BiomeSpawnsModifier.SpawnInfo(EEEntities.BOOFLO_ADOLESCENT, 5, 1, 2),
						new BiomeSpawnsModifier.SpawnInfo(EEEntities.BOOFLO, 15, 1, 3),
						new BiomeSpawnsModifier.SpawnInfo(EEEntities.PUFF_BUG, 10, 2, 4)
				)
		));
		modificationManager.addModifier(BiomeFeatureModifier.createMultiFeatureAdder(poiseOnly, GenerationStep.Decoration.SURFACE_STRUCTURES,
				Sets.newHashSet(
						() -> EEFeatures.Configured.POISE_DOME,
						() -> EEFeatures.Configured.POISE_TREE,
						() -> EEFeatures.Configured.END_GATEWAY
				)
		));
		modificationManager.addModifier(BiomeFeatureModifier.createMultiFeatureAdder(poiseOnly, GenerationStep.Decoration.VEGETAL_DECORATION,
				Sets.newHashSet(
						() -> EEFeatures.Configured.POISE_CLUSTER,
						() -> EEFeatures.Configured.PUFFBUG_HIVE,
						() -> EEFeatures.Configured.BOLLOOM_BUD,
						() -> EEFeatures.Configured.TALL_POISE_GRASS,
						() -> EEFeatures.Configured.POISE_GRASS
				)
		));
		modificationManager.addModifier(BiomeAmbienceModifier.createAmbienceReplacer(poiseOnly, () -> new BiomeSpecialEffects.Builder()
				.skyColor(0)
				.waterColor(4159204)
				.waterFogColor(329011)
				.fogColor(10518688)
				.backgroundMusic(Musics.createGameMusic(EESounds.POISE_FOREST_MUSIC.get()))
				.ambientLoopSound(EESounds.POISE_FOREST_LOOP.get())
				.ambientAdditionsSound(new AmbientAdditionsSettings(EESounds.POISE_FOREST_ADDITIONS.get(), 0.01D))
				.ambientMoodSound(new AmbientMoodSettings(EESounds.POISE_FOREST_MOOD.get(), 6000, 8, 2.0D))
				.build()));
	}

	public static BiomeSubRegistryHelper.KeyedBiome createPoiseForest() {
		Biome.BiomeBuilder builder = new Biome.BiomeBuilder();
		builder
				.generationSettings(
						new BiomeGenerationSettings.Builder()
								.surfaceBuilder(() -> EESurfaceBuilders.Configs.POISE_FOREST)
								.build()
				)
				.mobSpawnSettings(
						new MobSpawnSettings.Builder()
								.creatureGenerationProbability(0.9F)
								.build()
				)
				.specialEffects(
						new BiomeSpecialEffects.Builder()
								.skyColor(0)
								.waterColor(4159204)
								.waterFogColor(329011)
								.fogColor(10518688)
								.ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
								.build()
				)
				.precipitation(Biome.Precipitation.NONE)
				.biomeCategory(Biome.BiomeCategory.THEEND)
				.depth(0.1F)
				.scale(0.2F)
				.temperature(0.5F)
				.downfall(0.5F);
		return HELPER.createBiome("poise_forest", builder::build);
	}
}