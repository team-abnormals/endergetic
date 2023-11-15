package com.teamabnormals.endergetic.core.registry;

import com.teamabnormals.blueprint.core.util.registry.BiomeSubRegistryHelper;
import com.teamabnormals.endergetic.core.EndergeticExpansion;
import com.teamabnormals.endergetic.core.registry.EEFeatures.EEPlacedFeatures;
import net.minecraft.data.worldgen.placement.EndPlacements;
import net.minecraft.sounds.Musics;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraftforge.fml.common.Mod;

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
								.backgroundMusic(Musics.createGameMusic(EESoundEvents.POISE_FOREST_MUSIC.get()))
								.ambientLoopSound(EESoundEvents.POISE_FOREST_LOOP.get())
								.ambientAdditionsSound(new AmbientAdditionsSettings(EESoundEvents.POISE_FOREST_ADDITIONS.get(), 0.01D))
								.ambientMoodSound(new AmbientMoodSettings(EESoundEvents.POISE_FOREST_MOOD.get(), 6000, 8, 2.0D))
								.build()
				)
				.mobSpawnSettings(
						new MobSpawnSettings.Builder()
								.addSpawn(EEEntityTypes.END_CREATURE, new MobSpawnSettings.SpawnerData(EEEntityTypes.BOOFLO_ADOLESCENT.get(), 5, 1, 2))
								.addSpawn(EEEntityTypes.END_CREATURE, new MobSpawnSettings.SpawnerData(EEEntityTypes.BOOFLO.get(), 15, 1, 3))
								.addSpawn(EEEntityTypes.END_CREATURE, new MobSpawnSettings.SpawnerData(EEEntityTypes.PUFF_BUG.get(), 10, 2, 4))
								.creatureGenerationProbability(0.9F)
								.build()
				)
				.generationSettings(
						new BiomeGenerationSettings.Builder()
								.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, EEPlacedFeatures.POISE_DOME.getHolder().get())
								.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, EEPlacedFeatures.POISE_TREE.getHolder().get())
								.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, EndPlacements.END_GATEWAY_RETURN)
								.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, EEPlacedFeatures.POISE_CLUSTER.getHolder().get())
								.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, EEPlacedFeatures.PUFF_BUG_HIVE.getHolder().get())
								.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, EEPlacedFeatures.BOLLOOM_BUD.getHolder().get())
								.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, EEPlacedFeatures.TALL_POISE_BUSH_PATCH.getHolder().get())
								.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, EEPlacedFeatures.POISE_BUSH_PATCH.getHolder().get())
								.build()
				).build();
	}
}