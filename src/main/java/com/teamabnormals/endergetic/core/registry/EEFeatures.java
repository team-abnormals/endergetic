package com.teamabnormals.endergetic.core.registry;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import com.teamabnormals.endergetic.common.levelgen.configs.*;
import com.teamabnormals.endergetic.common.levelgen.feature.*;
import com.teamabnormals.endergetic.common.levelgen.feature.corrock.*;
import com.teamabnormals.endergetic.common.levelgen.feature.corrock.tower.LargeCorrockTowerFeature;
import com.teamabnormals.endergetic.common.levelgen.feature.corrock.tower.MediumCorrockTowerFeature;
import com.teamabnormals.endergetic.common.levelgen.feature.corrock.tower.SmallCorrockTowerFeature;
import com.teamabnormals.endergetic.common.levelgen.placement.HeightmapSpreadDoublePlacement;
import com.teamabnormals.endergetic.common.levelgen.placement.HeightmapSpreadLowerPlacement;
import com.teamabnormals.endergetic.common.levelgen.placement.NoiseHeightmap32Placement;
import com.teamabnormals.endergetic.common.levelgen.placement.NoiseRarityFilter;
import com.teamabnormals.endergetic.core.EndergeticExpansion;
import com.teamabnormals.endergetic.core.registry.builtin.EENoises;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.features.EndFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.*;
import net.minecraft.world.level.levelgen.feature.stateproviders.RuleBasedBlockStateProvider;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

public final class EEFeatures {
	public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, EndergeticExpansion.MOD_ID);

	public static final RegistryObject<Feature<NoneFeatureConfiguration>> POISE_BUSH_PATCH = FEATURES.register("poise_bush_patch", () -> new PoiseBushFeature(NoneFeatureConfiguration.CODEC));
	public static final RegistryObject<Feature<NoneFeatureConfiguration>> TALL_POISE_BUSH_PATCH = FEATURES.register("tall_poise_bush_patch", () -> new TallPoiseBushFeature(NoneFeatureConfiguration.CODEC));
	public static final RegistryObject<Feature<NoneFeatureConfiguration>> POISE_CLUSTER = FEATURES.register("poise_cluster", () -> new PoiseClusterFeature(NoneFeatureConfiguration.CODEC));
	public static final RegistryObject<Feature<NoneFeatureConfiguration>> BOLLOOM_BUD = FEATURES.register("bolloom_bud", () -> new BolloomBudFeature(NoneFeatureConfiguration.CODEC));
	public static final RegistryObject<Feature<NoneFeatureConfiguration>> PUFFBUG_HIVE = FEATURES.register("puffbug_hive", () -> new PuffBugHiveFeature(NoneFeatureConfiguration.CODEC));
	public static final RegistryObject<Feature<NoneFeatureConfiguration>> POISE_DOME = FEATURES.register("poise_dome", () -> new PoiseDomeFeature(NoneFeatureConfiguration.CODEC));
	public static final RegistryObject<Feature<NoneFeatureConfiguration>> POISE_TREE = FEATURES.register("poise_tree", () -> new PoiseTreeFeature(NoneFeatureConfiguration.CODEC));

	public static final RegistryObject<Feature<EndergeticPatchConfig>> CORROCK_PATCH = FEATURES.register("corrock_patch", () -> new CorrockPatchFeature(EndergeticPatchConfig.CODEC));
	public static final RegistryObject<Feature<DiskConfiguration>> CORROCK_GROUND_PATCH = FEATURES.register("corrock_ground_patch", () -> new CorrockGroundPatchFeature(DiskConfiguration.CODEC));
	public static final RegistryObject<Feature<CorrockBranchConfig>> CORROCK_BRANCH = FEATURES.register("corrock_branch", () -> new CorrockBranchFeature(CorrockBranchConfig.CODEC));
	public static final RegistryObject<Feature<ProbabilityFeatureConfiguration>> SMALL_CORROCK_TOWER = FEATURES.register("small_corrock_tower", () -> new SmallCorrockTowerFeature(ProbabilityFeatureConfiguration.CODEC));
	public static final RegistryObject<Feature<CorrockTowerConfig>> MEDIUM_CORROCK_TOWER = FEATURES.register("medium_corrock_tower", () -> new MediumCorrockTowerFeature(CorrockTowerConfig.CODEC));
	public static final RegistryObject<Feature<CorrockTowerConfig>> LARGE_CORROCK_TOWER = FEATURES.register("large_corrock_tower", () -> new LargeCorrockTowerFeature(CorrockTowerConfig.CODEC));
	public static final RegistryObject<Feature<ProbabilityFeatureConfiguration>> CORROCK_SHELF = FEATURES.register("corrock_shelf", () -> new CorrockShelfFeature(ProbabilityFeatureConfiguration.CODEC));
	public static final RegistryObject<Feature<CorrockArchConfig>> CORROCK_ARCH = FEATURES.register("corrock_arch", () -> new CorrockArchFeature(CorrockArchConfig.CODEC));
	public static final RegistryObject<Feature<EndergeticPatchConfig>> EETLE_EGG = FEATURES.register("eetle_egg", () -> new EetleEggPatchFeature(EndergeticPatchConfig.CODEC));
	public static final RegistryObject<Feature<MultiPatchConfig>> EUMUS_PATCH = FEATURES.register("eumus_patch", () -> new EumusPatchFeature(MultiPatchConfig.CODEC));
	public static final RegistryObject<Feature<MultiPatchConfig>> SPECKLED_CORROCK_PATCH = FEATURES.register("speckled_corrock_patch", () -> new SpeckledCorrockPatchFeature(MultiPatchConfig.CODEC));

	public static final RegistryObject<Feature<WeightedFeatureConfig>> WEIGHTED_FEATURES = FEATURES.register("weighted_features", () -> new WeightedMultiFeature(WeightedFeatureConfig.CODEC));
	public static final RegistryObject<Feature<EndGatewayConfiguration>> ENDERGETIC_END_GATEWAY = FEATURES.register("end_gateway", () -> new EndergeticEndGatewayFeature(EndGatewayConfiguration.CODEC));

	public static final class EEConfiguredFeatures {
		public static final ResourceKey<ConfiguredFeature<?, ?>> POISE_DOME = createKey("poise_dome");
		public static final ResourceKey<ConfiguredFeature<?, ?>> POISE_TREE = createKey("poise_tree");
		public static final ResourceKey<ConfiguredFeature<?, ?>> POISE_CLUSTER = createKey("poise_cluster");
		public static final ResourceKey<ConfiguredFeature<?, ?>> PUFF_BUG_HIVE = createKey("puff_bug_hive");
		public static final ResourceKey<ConfiguredFeature<?, ?>> BOLLOOM_BUD = createKey("bolloom_bud");
		public static final ResourceKey<ConfiguredFeature<?, ?>> POISE_BUSH_PATCH = createKey("poise_bush_patch");
		public static final ResourceKey<ConfiguredFeature<?, ?>> TALL_POISE_BUSH_PATCH = createKey("tall_poise_bush_patch");

		public static final ResourceKey<ConfiguredFeature<?, ?>> CORROCK_PATCH = createKey("corrock_patch");
		public static final ResourceKey<ConfiguredFeature<?, ?>> DISK_CORROCK = createKey("disk_corrock");
		public static final ResourceKey<ConfiguredFeature<?, ?>> EXTRA_BRANCH_DECORATIONS_CORROCK_BRANCH = createKey("extra_branch_decorations_corrock_branch");
		public static final ResourceKey<ConfiguredFeature<?, ?>> EXTRA_CROWNS_CORROCK_BRANCH = createKey("extra_crowns_corrock_branch");
		public static final ResourceKey<ConfiguredFeature<?, ?>> SMALL_CORROCK_TOWER = createKey("small_corrock_tower");
		public static final ResourceKey<ConfiguredFeature<?, ?>> MEDIUM_CORROCK_TOWER = createKey("medium_corrock_tower");
		public static final ResourceKey<ConfiguredFeature<?, ?>> LARGE_CORROCK_TOWER = createKey("large_corrock_tower");
		public static final ResourceKey<ConfiguredFeature<?, ?>> CORROCK_TOWER = createKey("corrock_tower");
		public static final ResourceKey<ConfiguredFeature<?, ?>> CORROCK_SHELF = createKey("corrock_shelf");
		public static final ResourceKey<ConfiguredFeature<?, ?>> CORROCK_ARCH = createKey("corrock_arch");
		public static final ResourceKey<ConfiguredFeature<?, ?>> EETLE_EGG_PATCH = createKey("eetle_egg_patch");
		public static final ResourceKey<ConfiguredFeature<?, ?>> EUMUS_PATCH = createKey("eumus_patch");
		public static final ResourceKey<ConfiguredFeature<?, ?>> SPECKLED_CORROCK_PATCH = createKey("speckled_corrock_patch");

		public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> context) {
			HolderGetter<ConfiguredFeature<?, ?>> features = context.lookup(Registries.CONFIGURED_FEATURE);

			register(context, EndFeatures.END_GATEWAY_RETURN, ENDERGETIC_END_GATEWAY.get(), EndGatewayConfiguration.knownExit(ServerLevel.END_SPAWN_POINT, true));
			register(context, EndFeatures.END_GATEWAY_DELAYED, ENDERGETIC_END_GATEWAY.get(), EndGatewayConfiguration.delayedExitSearch());

			register(context, POISE_DOME, EEFeatures.POISE_DOME.get(), FeatureConfiguration.NONE);
			register(context, POISE_TREE, EEFeatures.POISE_TREE.get(), FeatureConfiguration.NONE);
			register(context, POISE_CLUSTER, EEFeatures.POISE_CLUSTER.get(), FeatureConfiguration.NONE);
			register(context, PUFF_BUG_HIVE, EEFeatures.PUFFBUG_HIVE.get(), FeatureConfiguration.NONE);
			register(context, BOLLOOM_BUD, EEFeatures.BOLLOOM_BUD.get(), FeatureConfiguration.NONE);
			register(context, POISE_BUSH_PATCH, EEFeatures.POISE_BUSH_PATCH.get(), FeatureConfiguration.NONE);
			register(context, TALL_POISE_BUSH_PATCH, EEFeatures.TALL_POISE_BUSH_PATCH.get(), FeatureConfiguration.NONE);

			register(context, CORROCK_PATCH, EEFeatures.CORROCK_PATCH.get(), new EndergeticPatchConfig(0.175F, false));
			register(context, DISK_CORROCK, EEFeatures.CORROCK_GROUND_PATCH.get(), new DiskConfiguration(RuleBasedBlockStateProvider.simple(EEBlocks.END_CORROCK_BLOCK.get()), BlockPredicate.matchesBlocks(Blocks.END_STONE), UniformInt.of(2, 3), 3));
			register(context, EXTRA_BRANCH_DECORATIONS_CORROCK_BRANCH, EEFeatures.CORROCK_BRANCH.get(), new CorrockBranchConfig(ImmutableList.of(Blocks.END_STONE.defaultBlockState(), EEBlocks.END_CORROCK_BLOCK.get().defaultBlockState()), 0.4F, 0.5F));
			register(context, EXTRA_CROWNS_CORROCK_BRANCH, EEFeatures.CORROCK_BRANCH.get(), new CorrockBranchConfig(ImmutableList.of(EEBlocks.END_CORROCK_BLOCK.get().defaultBlockState(), EEBlocks.EUMUS.get().defaultBlockState()), 0.5F, 0.4F));
			register(context, SMALL_CORROCK_TOWER, EEFeatures.SMALL_CORROCK_TOWER.get(), new ProbabilityFeatureConfiguration(0.25F));
			register(context, MEDIUM_CORROCK_TOWER, EEFeatures.MEDIUM_CORROCK_TOWER.get(), new CorrockTowerConfig(3, 4, 0.7F, 0.075F));
			register(context, LARGE_CORROCK_TOWER, EEFeatures.LARGE_CORROCK_TOWER.get(), new CorrockTowerConfig(2, 5, 0.9F, 0.1F));
			register(context, CORROCK_TOWER, EEFeatures.WEIGHTED_FEATURES.get(), WeightedFeatureConfig.createFromPairs(Pair.of(features.getOrThrow(SMALL_CORROCK_TOWER), 6), Pair.of(features.getOrThrow(MEDIUM_CORROCK_TOWER), 12), Pair.of(features.getOrThrow(LARGE_CORROCK_TOWER), 4)));
			register(context, CORROCK_SHELF, EEFeatures.CORROCK_SHELF.get(), new ProbabilityFeatureConfiguration(0.75F));
			register(context, CORROCK_ARCH, EEFeatures.CORROCK_ARCH.get(), new CorrockArchConfig(0.1F, 0.25F, 13.0F, 22.0F, 7.0F));
			register(context, EETLE_EGG_PATCH, EEFeatures.EETLE_EGG.get(), new EndergeticPatchConfig(0.75F, false));
			register(context, EUMUS_PATCH, EEFeatures.EUMUS_PATCH.get(), new MultiPatchConfig(2, 3));
			register(context, SPECKLED_CORROCK_PATCH, EEFeatures.SPECKLED_CORROCK_PATCH.get(), new MultiPatchConfig(2, 3));
		}

		public static ResourceKey<ConfiguredFeature<?, ?>> createKey(String name) {
			return ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(EndergeticExpansion.MOD_ID, name));
		}

		public static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(BootstapContext<ConfiguredFeature<?, ?>> context, ResourceKey<ConfiguredFeature<?, ?>> key, F feature, FC config) {
			context.register(key, new ConfiguredFeature<>(feature, config));
		}
	}

	public static final class EEPlacedFeatures {
		public static final ResourceKey<PlacedFeature> POISE_DOME = createKey("poise_dome");
		public static final ResourceKey<PlacedFeature> POISE_TREE = createKey("poise_tree");
		public static final ResourceKey<PlacedFeature> POISE_CLUSTER = createKey("poise_cluster");
		public static final ResourceKey<PlacedFeature> PUFF_BUG_HIVE = createKey("puff_bug_hive");
		public static final ResourceKey<PlacedFeature> BOLLOOM_BUD = createKey("bolloom_bud");
		public static final ResourceKey<PlacedFeature> POISE_BUSH_PATCH = createKey("poise_bush_patch");
		public static final ResourceKey<PlacedFeature> TALL_POISE_BUSH_PATCH = createKey("tall_poise_bush_patch");

		public static final ResourceKey<PlacedFeature> CORROCK_PATCH = createKey("corrock_patch");
		public static final ResourceKey<PlacedFeature> DISK_CORROCK = createKey("disk_corrock");
		public static final ResourceKey<PlacedFeature> SPARSE_CORROCK_BRANCH = createKey("sparse_corrock_branch");
		public static final ResourceKey<PlacedFeature> CORROCK_BRANCH = createKey("corrock_branch");
		public static final ResourceKey<PlacedFeature> CORROCK_TOWER = createKey("corrock_tower");
		public static final ResourceKey<PlacedFeature> CORROCK_SHELF = createKey("corrock_shelf");
		public static final ResourceKey<PlacedFeature> CORROCK_ARCH = createKey("corrock_arch");
		public static final ResourceKey<PlacedFeature> EETLE_EGG_PATCH = createKey("eetle_egg_patch");
		public static final ResourceKey<PlacedFeature> EUMUS_PATCH = createKey("eumus_patch");
		public static final ResourceKey<PlacedFeature> SPECKLED_CORROCK_PATCH = createKey("speckled_corrock");

		public static void bootstrap(BootstapContext<PlacedFeature> context) {
			HolderGetter<NormalNoise.NoiseParameters> noise = context.lookup(Registries.NOISE);

			register(context, POISE_DOME, EEConfiguredFeatures.POISE_DOME, PlacementUtils.countExtra(3, 0.02F, 1), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome());
			register(context, POISE_TREE, EEConfiguredFeatures.POISE_TREE, PlacementUtils.countExtra(2, 0.05F, 1), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome());
			register(context, POISE_CLUSTER, EEConfiguredFeatures.POISE_CLUSTER, new NoiseHeightmap32Placement(-0.8D, 4, 22), BiomeFilter.biome());
			register(context, PUFF_BUG_HIVE, EEConfiguredFeatures.PUFF_BUG_HIVE, new NoiseHeightmap32Placement(-0.8D, 8, 23), BiomeFilter.biome());
			register(context, BOLLOOM_BUD, EEConfiguredFeatures.BOLLOOM_BUD, new NoiseHeightmap32Placement(-0.9D, 90, 90), BiomeFilter.biome());
			register(context, POISE_BUSH_PATCH, EEConfiguredFeatures.POISE_BUSH_PATCH, NoiseThresholdCountPlacement.of(-0.8D, 5, 10), PlacementUtils.FULL_RANGE, BiomeFilter.biome());
			register(context, TALL_POISE_BUSH_PATCH, EEConfiguredFeatures.TALL_POISE_BUSH_PATCH, new NoiseHeightmap32Placement(-0.8D, 2, 8), PlacementUtils.FULL_RANGE, BiomeFilter.biome());

			register(context, CORROCK_PATCH, EEConfiguredFeatures.CORROCK_PATCH, CountPlacement.of(3), InSquarePlacement.spread(), HeightmapSpreadDoublePlacement.MOTION_BLOCKING, BiomeFilter.biome());
			register(context, DISK_CORROCK, EEConfiguredFeatures.DISK_CORROCK, BiomeFilter.biome());
			register(context, SPARSE_CORROCK_BRANCH, EEConfiguredFeatures.EXTRA_BRANCH_DECORATIONS_CORROCK_BRANCH, RarityFilter.onAverageOnceEvery(8), CountPlacement.of(3), InSquarePlacement.spread(), HeightmapSpreadDoublePlacement.MOTION_BLOCKING, BiomeFilter.biome());
			register(context, CORROCK_BRANCH, EEConfiguredFeatures.EXTRA_CROWNS_CORROCK_BRANCH, CountPlacement.of(64), InSquarePlacement.spread(), HeightmapSpreadDoublePlacement.MOTION_BLOCKING, BiomeFilter.biome());
			register(context, CORROCK_TOWER, EEConfiguredFeatures.CORROCK_TOWER, CountPlacement.of(128), InSquarePlacement.spread(), HeightmapSpreadDoublePlacement.MOTION_BLOCKING, BiomeFilter.biome());
			register(context, CORROCK_SHELF, EEConfiguredFeatures.CORROCK_SHELF, new NoiseRarityFilter(noise.getOrThrow(EENoises.CORROCK), 0.5F, 0.1F, 1.0F), CountPlacement.of(8), InSquarePlacement.spread(), HeightmapSpreadLowerPlacement.INSTANCE, BiomeFilter.biome());
			register(context, CORROCK_ARCH, EEConfiguredFeatures.CORROCK_ARCH, CountPlacement.of(26), InSquarePlacement.spread(), HeightmapSpreadDoublePlacement.MOTION_BLOCKING, BiomeFilter.biome());
			register(context, EETLE_EGG_PATCH, EEConfiguredFeatures.EETLE_EGG_PATCH, CountPlacement.of(1), InSquarePlacement.spread(), BiomeFilter.biome());
			register(context, EUMUS_PATCH, EEConfiguredFeatures.EUMUS_PATCH, CountPlacement.of(2), InSquarePlacement.spread(), HeightmapSpreadDoublePlacement.MOTION_BLOCKING, BiomeFilter.biome());
			register(context, SPECKLED_CORROCK_PATCH, EEConfiguredFeatures.SPECKLED_CORROCK_PATCH, RarityFilter.onAverageOnceEvery(2), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome());
		}

		public static ResourceKey<PlacedFeature> createKey(String name) {
			return ResourceKey.create(Registries.PLACED_FEATURE, new ResourceLocation(EndergeticExpansion.MOD_ID, name));
		}

		public static void register(BootstapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, ResourceKey<ConfiguredFeature<?, ?>> feature, List<PlacementModifier> modifiers) {
			context.register(key, new PlacedFeature(context.lookup(Registries.CONFIGURED_FEATURE).getOrThrow(feature), modifiers));
		}

		public static void register(BootstapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, ResourceKey<ConfiguredFeature<?, ?>> feature, PlacementModifier... modifiers) {
			register(context, key, feature, List.of(modifiers));
		}
	}
}