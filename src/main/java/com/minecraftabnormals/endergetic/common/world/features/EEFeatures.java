package com.minecraftabnormals.endergetic.common.world.features;

import java.util.function.Supplier;

import com.minecraftabnormals.endergetic.common.world.configs.*;
import com.minecraftabnormals.endergetic.common.world.features.corrock.*;
import com.minecraftabnormals.endergetic.common.world.features.corrock.tower.*;
import com.minecraftabnormals.endergetic.common.world.placements.EEPlacements;
import com.minecraftabnormals.endergetic.core.EndergeticExpansion;

import com.mojang.datafixers.util.Pair;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.placement.*;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public final class EEFeatures {
	public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, EndergeticExpansion.MOD_ID);

	public static final RegistryObject<Feature<NoFeatureConfig>> POISE_GRASS = createFeature("poise_bush", () -> new PoiseBushFeature(NoFeatureConfig.field_236558_a_));
	public static final RegistryObject<Feature<NoFeatureConfig>> POISE_TALLGRASS = createFeature("poise_tallgrass", () -> new TallPoiseBushFeature(NoFeatureConfig.field_236558_a_));
	public static final RegistryObject<Feature<NoFeatureConfig>> POISE_CLUSTER = createFeature("poise_cluster", () -> new PoiseClusterFeature(NoFeatureConfig.field_236558_a_));
	public static final RegistryObject<Feature<NoFeatureConfig>> BOLLOOM_BUD = createFeature("bolloom_bud", () -> new BolloomBudFeature(NoFeatureConfig.field_236558_a_));
	public static final RegistryObject<Feature<NoFeatureConfig>> PUFFBUG_HIVE = createFeature("puffbug_hive", () -> new PuffBugHiveFeature(NoFeatureConfig.field_236558_a_));
	public static final RegistryObject<Feature<NoFeatureConfig>> POISE_DOME = createFeature("poise_dome", () -> new PoiseDomeFeature(NoFeatureConfig.field_236558_a_));
	public static final RegistryObject<Feature<NoFeatureConfig>> POISE_TREE = createFeature("poise_tree", () -> new PoiseTreeFeature(NoFeatureConfig.field_236558_a_));

	public static final RegistryObject<Feature<ProbabilityConfig>> CORROCK_PATCH = createFeature("corrock_patch", () -> new CorrockPatchFeature(ProbabilityConfig.CODEC));
	public static final RegistryObject<Feature<SphereReplaceConfig>> GROUND_PATCH = createFeature("ground_patch", () -> new GroundPatchFeature(SphereReplaceConfig.field_236516_a_));
	public static final RegistryObject<Feature<ProbabilityConfig>> CORROCK_BRANCH = createFeature("corrock_branch", () -> new CorrockBranchFeature(ProbabilityConfig.CODEC));
	public static final RegistryObject<Feature<ProbabilityConfig>> SMALL_CORROCK_TOWER = createFeature("small_corrock_tower", () -> new SmallCorrockTowerFeature(ProbabilityConfig.CODEC));
	public static final RegistryObject<Feature<CorrockTowerConfig>> MEDIUM_CORROCK_TOWER = createFeature("medium_corrock_tower", () -> new MediumCorrockTowerFeature(CorrockTowerConfig.CODEC));
	public static final RegistryObject<Feature<CorrockTowerConfig>> LARGE_CORROCK_TOWER = createFeature("large_corrock_tower", () -> new LargeCorrockTowerFeature(CorrockTowerConfig.CODEC));

	public static final RegistryObject<Feature<WeightedFeatureConfig>> WEIGHTED_FEATURES = createFeature("weighted_features", () -> new WeightedMultiFeature(WeightedFeatureConfig.CODEC));
	public static final RegistryObject<Feature<EndGatewayConfig>> ENDERGETIC_GATEWAY = createFeature("gateway", () -> new EndergeticEndGatewayFeature(EndGatewayConfig.field_236522_a_));

	private static <F extends Feature<?>> RegistryObject<F> createFeature(String name, Supplier<F> feature) {
		return FEATURES.register(name, feature);
	}

	public static final class Configured {
		public static final ConfiguredFeature<?, ?> POISE_DOME = EEFeatures.POISE_DOME.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).withPlacement(Placement.COUNT_EXTRA.configure(new AtSurfaceWithExtraConfig(3, 0.02F, 1)));
		public static final ConfiguredFeature<?, ?> POISE_TREE = EEFeatures.POISE_TREE.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).withPlacement(Placement.COUNT_EXTRA.configure(new AtSurfaceWithExtraConfig(2, 0.05F, 1)));
		public static final ConfiguredFeature<?, ?> POISE_CLUSTER = EEFeatures.POISE_CLUSTER.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(EEPlacements.NOISE_HEIGHTMAP_32.get().configure(new NoiseDependant(-0.8D, 4, 22)));
		public static final ConfiguredFeature<?, ?> PUFFBUG_HIVE = EEFeatures.PUFFBUG_HIVE.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(EEPlacements.NOISE_HEIGHTMAP_32.get().configure(new NoiseDependant(-0.8D, 9, 25)));
		public static final ConfiguredFeature<?, ?> BOLLOOM_BUD = EEFeatures.BOLLOOM_BUD.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(EEPlacements.NOISE_HEIGHTMAP_32.get().configure(new NoiseDependant(-0.9D, 90, 90)));
		public static final ConfiguredFeature<?, ?> POISE_GRASS = EEFeatures.POISE_GRASS.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Features.Placements.HEIGHTMAP_SPREAD_DOUBLE_PLACEMENT).withPlacement(Placement.COUNT_NOISE.configure(new NoiseDependant(-0.8D, 5, 10)));
		public static final ConfiguredFeature<?, ?> TALL_POISE_GRASS = EEFeatures.POISE_TALLGRASS.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(EEPlacements.NOISE_HEIGHTMAP_32.get().configure(new NoiseDependant(-0.8D, 0, 7)));
		public static final ConfiguredFeature<?, ?> END_GATEWAY = EEFeatures.ENDERGETIC_GATEWAY.get().withConfiguration(EndGatewayConfig.func_214702_a(ServerWorld.field_241108_a_, true)).withPlacement(Placement.END_GATEWAY.configure(IPlacementConfig.NO_PLACEMENT_CONFIG));
		public static final ConfiguredFeature<?, ?> END_GATEWAY_DELAYED = EEFeatures.ENDERGETIC_GATEWAY.get().withConfiguration(EndGatewayConfig.func_214698_a());
		public static final ConfiguredFeature<?, ?> CORROCK_PATCH = EEFeatures.CORROCK_PATCH.get().withConfiguration(new ProbabilityConfig(0.3F)).withPlacement(Features.Placements.PATCH_PLACEMENT).func_242731_b(8);
		public static final ConfiguredFeature<?, ?> CORROCK_BRANCH = EEFeatures.CORROCK_BRANCH.get().withConfiguration(new ProbabilityConfig(0.35F)).withPlacement(Features.Placements.PATCH_PLACEMENT).func_242731_b(16);
		public static final ConfiguredFeature<?, ?> SMALL_CORROCK_TOWER = EEFeatures.SMALL_CORROCK_TOWER.get().withConfiguration(new ProbabilityConfig(0.25F)).withPlacement(Features.Placements.PATCH_PLACEMENT).func_242731_b(2);
		public static final ConfiguredFeature<?, ?> MEDIUM_CORROCK_TOWER = EEFeatures.MEDIUM_CORROCK_TOWER.get().withConfiguration(new CorrockTowerConfig(3, 4, 0.5F, 0.075F)).withPlacement(Features.Placements.PATCH_PLACEMENT).func_242731_b(2);
		public static final ConfiguredFeature<?, ?> LARGE_CORROCK_TOWER = EEFeatures.LARGE_CORROCK_TOWER.get().withConfiguration(new CorrockTowerConfig(2, 4, 0.5F, 0.1F)).withPlacement(Features.Placements.PATCH_PLACEMENT).func_242731_b(2);
		public static final ConfiguredFeature<?, ?> CORROCK_TOWER = EEFeatures.WEIGHTED_FEATURES.get().withConfiguration(WeightedFeatureConfig.createFromPairs(Pair.of(SMALL_CORROCK_TOWER, 6), Pair.of(MEDIUM_CORROCK_TOWER, 12), Pair.of(LARGE_CORROCK_TOWER, 4))).withPlacement(Features.Placements.PATCH_PLACEMENT).func_242731_b(256);

		private static <FC extends IFeatureConfig> void register(String name, ConfiguredFeature<FC, ?> configuredFeature) {
			Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, new ResourceLocation(EndergeticExpansion.MOD_ID, name), configuredFeature);
		}

		public static void registerConfiguredFeatures() {
			register("poise_dome", POISE_DOME);
			register("poise_tree", POISE_TREE);
			register("poise_cluster", POISE_CLUSTER);
			register("puffbug_hive", PUFFBUG_HIVE);
			register("bolloom_bud", BOLLOOM_BUD);
			register("poise_grass", POISE_GRASS);
			register("tall_poise_grass", TALL_POISE_GRASS);
			register("end_gateway", END_GATEWAY);
			register("end_gateway_delayed", END_GATEWAY_DELAYED);
			register("corrock_patch", CORROCK_PATCH);
			register("corrock_branch", CORROCK_BRANCH);
			register("small_corrock_tower", SMALL_CORROCK_TOWER);
			register("medium_corrock_tower", MEDIUM_CORROCK_TOWER);
			register("large_corrock_tower", LARGE_CORROCK_TOWER);
			register("corrock_tower", CORROCK_TOWER);
		}
	}
}