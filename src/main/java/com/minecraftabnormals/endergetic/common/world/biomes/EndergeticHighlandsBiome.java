package com.minecraftabnormals.endergetic.common.world.biomes;

import com.minecraftabnormals.endergetic.common.world.features.EEFeatures;
import com.minecraftabnormals.endergetic.common.world.surfacebuilders.EESurfaceBuilders;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeAmbience;
import net.minecraft.world.biome.DefaultBiomeFeatures;
import net.minecraft.world.biome.MoodSoundAmbience;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.GenerationStage.Decoration;
import net.minecraft.world.gen.feature.EndGatewayConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.ProbabilityConfig;
import net.minecraft.world.gen.placement.IPlacementConfig;
import net.minecraft.world.gen.placement.NoiseDependant;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.placement.TopSolidWithNoiseConfig;
import net.minecraft.world.gen.surfacebuilders.ConfiguredSurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.server.ServerWorld;

public class EndergeticHighlandsBiome extends EndergeticBiome {

	public EndergeticHighlandsBiome() {
		super((new Biome.Builder()).surfaceBuilder(SurfaceBuilder.DEFAULT, SurfaceBuilder.END_STONE_CONFIG).precipitation(Biome.RainType.NONE).category(Biome.Category.THEEND).depth(0.1F).scale(0.2F).temperature(0.5F).downfall(0.5F).func_235097_a_((new BiomeAmbience.Builder()).setWaterColor(4159204).setWaterFogColor(329011).setFogColor(10518688).setMoodSound(MoodSoundAmbience.field_235027_b_).build()).parent(null), () -> new ConfiguredSurfaceBuilder<>(EESurfaceBuilders.SPARSE_CORROCK_SURFACE_BUILDER.get(), SurfaceBuilder.END_STONE_CONFIG));
		this.func_235063_a_(DefaultBiomeFeatures.END_CITY);
		this.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.CHORUS_PLANT.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Placement.CHORUS_PLANT.configure(IPlacementConfig.NO_PLACEMENT_CONFIG)));
		this.addSpawn(EntityClassification.MONSTER, new Biome.SpawnListEntry(EntityType.ENDERMAN, 10, 4, 4));
	}

	@Override
	public void addSpawnsAndFeatures() {
		this.addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, EEFeatures.ENDERGETIC_GATEWAY.get().withConfiguration(EndGatewayConfig.func_214702_a(ServerWorld.field_241108_a_, true)).withPlacement(Placement.END_GATEWAY.configure(IPlacementConfig.NO_PLACEMENT_CONFIG)));
		this.addFeature(Decoration.SURFACE_STRUCTURES, EEFeatures.CORROCK_TOWER.get().withConfiguration(new ProbabilityConfig(0.25F)).withPlacement(Placement.TOP_SOLID_HEIGHTMAP_NOISE_BIASED.configure(new TopSolidWithNoiseConfig(2, 5.0D, 0.0D, Heightmap.Type.WORLD_SURFACE_WG))));
		this.addFeature(Decoration.SURFACE_STRUCTURES, EEFeatures.CORROCK_BRANCH.get().withConfiguration(new ProbabilityConfig(0.25F)).withPlacement(Placement.TOP_SOLID_HEIGHTMAP_NOISE_BIASED.configure(new TopSolidWithNoiseConfig(2, 5.0D, 0.0D, Heightmap.Type.WORLD_SURFACE_WG))));
		this.addFeature(Decoration.VEGETAL_DECORATION, EEFeatures.CORROCK_PATCH.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Placement.NOISE_HEIGHTMAP_DOUBLE.configure(new NoiseDependant(-0.8D, 5, 10))));
	}

}