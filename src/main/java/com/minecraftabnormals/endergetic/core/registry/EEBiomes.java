package com.minecraftabnormals.endergetic.core.registry;

import com.minecraftabnormals.endergetic.core.EndergeticExpansion;

import com.teamabnormals.blueprint.core.util.registry.BiomeSubRegistryHelper;
import net.minecraft.sounds.Musics;
import net.minecraft.world.level.biome.AmbientAdditionsSettings;
import net.minecraft.world.level.biome.AmbientMoodSettings;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraftforge.fml.common.Mod;

import net.minecraft.world.level.biome.Biome;

@Mod.EventBusSubscriber(modid = EndergeticExpansion.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class EEBiomes {
	private static final BiomeSubRegistryHelper HELPER = EndergeticExpansion.REGISTRY_HELPER.getBiomeSubHelper();

	public static final BiomeSubRegistryHelper.KeyedBiome POISE_FOREST = HELPER.createBiome("poise_forest", EEBiomes::poiseForest);

	public static Biome poiseForest() {
		return new Biome.BiomeBuilder()
				.precipitation(Biome.Precipitation.NONE)
				.temperature(0.5F)
				.downfall(0.5F)
				.specialEffects(
						new BiomeSpecialEffects.Builder()
								.skyColor(0)
								.waterColor(4159204)
								.waterFogColor(329011)
								.fogColor(10518688)
								.backgroundMusic(Musics.createGameMusic(EESounds.POISE_FOREST_MUSIC.get()))
								.ambientLoopSound(EESounds.POISE_FOREST_LOOP.get())
								.ambientAdditionsSound(new AmbientAdditionsSettings(EESounds.POISE_FOREST_ADDITIONS.get(), 0.01D))
								.ambientMoodSound(new AmbientMoodSettings(EESounds.POISE_FOREST_MOOD.get(), 6000, 8, 2.0D))
								.build()
				)
				.mobSpawnSettings(
						new MobSpawnSettings.Builder()
								.addSpawn(EEEntities.END_CREATURE, new MobSpawnSettings.SpawnerData(EEEntities.BOOFLO_ADOLESCENT.get(), 5, 1, 2))
								.addSpawn(EEEntities.END_CREATURE, new MobSpawnSettings.SpawnerData(EEEntities.BOOFLO.get(), 15, 1, 3))
								.addSpawn(EEEntities.END_CREATURE, new MobSpawnSettings.SpawnerData(EEEntities.PUFF_BUG.get(), 10, 2, 4))
								.creatureGenerationProbability(0.9F)
								.build()
				)
				.generationSettings(
						new BiomeGenerationSettings.Builder()
								.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, EEFeatures.Placed.POISE_DOME.getHolder().get())
								.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, EEFeatures.Placed.POISE_TREE.getHolder().get())
								.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, EEFeatures.Placed.END_GATEWAY_RETURN.getHolder().get())
								.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, EEFeatures.Placed.POISE_CLUSTER.getHolder().get())
								.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, EEFeatures.Placed.PUFF_BUG_HIVE.getHolder().get())
								.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, EEFeatures.Placed.BOLLOOM_BUD.getHolder().get())
								.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, EEFeatures.Placed.TALL_POISE_BUSH_PATCH.getHolder().get())
								.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, EEFeatures.Placed.POISE_BUSH_PATCH.getHolder().get())
								.build()
				).build();
	}
}