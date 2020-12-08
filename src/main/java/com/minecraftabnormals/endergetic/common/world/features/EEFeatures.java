package com.minecraftabnormals.endergetic.common.world.features;

import java.util.function.Supplier;

import com.minecraftabnormals.endergetic.common.world.features.corrock.*;
import com.minecraftabnormals.endergetic.common.world.placements.EEPlacements;
import com.minecraftabnormals.endergetic.core.EndergeticExpansion;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.placement.AtSurfaceWithExtraConfig;
import net.minecraft.world.gen.placement.IPlacementConfig;
import net.minecraft.world.gen.placement.NoiseDependant;
import net.minecraft.world.gen.placement.Placement;
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

	public static final RegistryObject<Feature<NoFeatureConfig>> CORROCK_PATCH = createFeature("corrock_patch", () -> new CorrockPatchFeature(NoFeatureConfig.field_236558_a_));
	public static final RegistryObject<Feature<SphereReplaceConfig>> GROUND_PATCH = createFeature("ground_patch", () -> new GroundPatchFeature(SphereReplaceConfig.field_236516_a_));
	public static final RegistryObject<Feature<ProbabilityConfig>> CORROCK_BRANCH = createFeature("corrock_branch", () -> new CorrockBranchFeature(ProbabilityConfig.CODEC));
	public static final RegistryObject<Feature<ProbabilityConfig>> CORROCK_TOWER = createFeature("corrock_tower", () -> new CorrockTowerFeature(ProbabilityConfig.CODEC));

	public static final RegistryObject<Feature<EndGatewayConfig>> ENDERGETIC_GATEWAY = createFeature("gateway", () -> new EndergeticEndGatewayFeature(EndGatewayConfig.field_236522_a_));

	private static <F extends Feature<?>> RegistryObject<F> createFeature(String name, Supplier<F> feature) {
		return FEATURES.register(name, feature);
	}

	public static final class Configs {
		public static final ConfiguredFeature<?, ?> POISE_DOME = EEFeatures.POISE_DOME.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).withPlacement(Placement.COUNT_EXTRA.configure(new AtSurfaceWithExtraConfig(3, 0.02F, 1)));
		public static final ConfiguredFeature<?, ?> POISE_TREE = EEFeatures.POISE_TREE.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).withPlacement(Placement.COUNT_EXTRA.configure(new AtSurfaceWithExtraConfig(2, 0.05F, 1)));
		public static final ConfiguredFeature<?, ?> POISE_CLUSTER = EEFeatures.POISE_CLUSTER.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(EEPlacements.NOISE_HEIGHTMAP_32.get().configure(new NoiseDependant(-0.8D, 4, 22)));
		public static final ConfiguredFeature<?, ?> PUFFBUG_HIVE = EEFeatures.PUFFBUG_HIVE.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(EEPlacements.NOISE_HEIGHTMAP_32.get().configure(new NoiseDependant(-0.8D, 9, 25)));
		public static final ConfiguredFeature<?, ?> BOLLOOM_BUD = EEFeatures.BOLLOOM_BUD.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(EEPlacements.NOISE_HEIGHTMAP_32.get().configure(new NoiseDependant(-0.9D, 90, 90)));
		public static final ConfiguredFeature<?, ?> POISE_GRASS = EEFeatures.POISE_GRASS.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Features.Placements.HEIGHTMAP_SPREAD_DOUBLE_PLACEMENT).withPlacement(Placement.COUNT_NOISE.configure(new NoiseDependant(-0.8D, 5, 10)));
		public static final ConfiguredFeature<?, ?> TALL_POISE_GRASS = EEFeatures.POISE_TALLGRASS.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(EEPlacements.NOISE_HEIGHTMAP_32.get().configure(new NoiseDependant(-0.8D, 0, 7)));
		public static final ConfiguredFeature<?, ?> END_GATEWAY = EEFeatures.ENDERGETIC_GATEWAY.get().withConfiguration(EndGatewayConfig.func_214702_a(ServerWorld.field_241108_a_, true)).withPlacement(Placement.END_GATEWAY.configure(IPlacementConfig.NO_PLACEMENT_CONFIG));
		public static final ConfiguredFeature<?, ?> END_GATEWAY_DELAYED = EEFeatures.ENDERGETIC_GATEWAY.get().withConfiguration(EndGatewayConfig.func_214698_a());

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
		}
	}
}