package com.minecraftabnormals.endergetic.core.registry;

import com.google.common.collect.ImmutableList;
import com.minecraftabnormals.endergetic.common.world.configs.*;
import com.minecraftabnormals.endergetic.common.world.features.*;
import com.minecraftabnormals.endergetic.common.world.features.corrock.*;
import com.minecraftabnormals.endergetic.common.world.features.corrock.tower.*;
import com.minecraftabnormals.endergetic.common.world.placements.HeightmapSpreadLowerPlacement;
import com.minecraftabnormals.endergetic.common.world.placements.NoiseHeightmap32Placement;
import com.minecraftabnormals.endergetic.core.EndergeticExpansion;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.Registry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.stateproviders.RuleBasedBlockStateProvider;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.NoiseThresholdCountPlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.RandomOffsetPlacement;
import net.minecraft.world.level.levelgen.placement.RarityFilter;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.DiskConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.EndGatewayConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.ProbabilityFeatureConfiguration;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

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
	public static final RegistryObject<Feature<EndGatewayConfiguration>> ENDERGETIC_GATEWAY = FEATURES.register("gateway", () -> new EndergeticEndGatewayFeature(EndGatewayConfiguration.CODEC));

	public static final class Configured {
		public static final DeferredRegister<ConfiguredFeature<?, ?>> CONFIGURED_FEATURES = DeferredRegister.create(Registry.CONFIGURED_FEATURE_REGISTRY, EndergeticExpansion.MOD_ID);

		public static final RegistryObject<ConfiguredFeature<NoneFeatureConfiguration, ?>> POISE_DOME = register("poise_dome", () -> new ConfiguredFeature<>(EEFeatures.POISE_DOME.get(), FeatureConfiguration.NONE));
		public static final RegistryObject<ConfiguredFeature<NoneFeatureConfiguration, ?>> POISE_TREE = register("poise_tree", () -> new ConfiguredFeature<>(EEFeatures.POISE_TREE.get(), FeatureConfiguration.NONE));
		public static final RegistryObject<ConfiguredFeature<NoneFeatureConfiguration, ?>> POISE_CLUSTER = register("poise_cluster", () -> new ConfiguredFeature<>( EEFeatures.POISE_CLUSTER.get(), FeatureConfiguration.NONE));
		public static final RegistryObject<ConfiguredFeature<NoneFeatureConfiguration, ?>> PUFF_BUG_HIVE = register("puff_bug_hive", () -> new ConfiguredFeature<>(EEFeatures.PUFFBUG_HIVE.get(), FeatureConfiguration.NONE));
		public static final RegistryObject<ConfiguredFeature<NoneFeatureConfiguration, ?>> BOLLOOM_BUD = register("bolloom_bud", () -> new ConfiguredFeature<>(EEFeatures.BOLLOOM_BUD.get(), FeatureConfiguration.NONE));
		public static final RegistryObject<ConfiguredFeature<NoneFeatureConfiguration, ?>> POISE_BUSH_PATCH = register("poise_bush_patch", () -> new ConfiguredFeature<>(EEFeatures.POISE_BUSH_PATCH.get(), FeatureConfiguration.NONE));
		public static final RegistryObject<ConfiguredFeature<NoneFeatureConfiguration, ?>> TALL_POISE_BUSH_PATCH = register("tall_poise_bush_patch", () -> new ConfiguredFeature<>(EEFeatures.TALL_POISE_BUSH_PATCH.get(), FeatureConfiguration.NONE));

		public static final RegistryObject<ConfiguredFeature<EndGatewayConfiguration, ?>> END_GATEWAY_RETURN = register("end_gateway_return", () -> new ConfiguredFeature<>(EEFeatures.ENDERGETIC_GATEWAY.get(), EndGatewayConfiguration.knownExit(ServerLevel.END_SPAWN_POINT, true)));
		public static final RegistryObject<ConfiguredFeature<EndGatewayConfiguration, ?>> END_GATEWAY_DELAYED = register("end_gateway_delayed", () -> new ConfiguredFeature<>(EEFeatures.ENDERGETIC_GATEWAY.get(), EndGatewayConfiguration.delayedExitSearch()));

		public static final RegistryObject<ConfiguredFeature<EndergeticPatchConfig, ?>> CORROCK_PATCH = register("corrock_patch", () -> new ConfiguredFeature<>(EEFeatures.CORROCK_PATCH.get(), new EndergeticPatchConfig(0.175F, false)));
		public static final RegistryObject<ConfiguredFeature<DiskConfiguration, ?>> DISK_CORROCK = register("disk_corrock", () -> new ConfiguredFeature<>(EEFeatures.CORROCK_GROUND_PATCH.get(), new DiskConfiguration(RuleBasedBlockStateProvider.simple(EEBlocks.CORROCK_END_BLOCK.get()), BlockPredicate.matchesBlocks(Blocks.END_STONE), UniformInt.of(2, 3), 3)));
		public static final RegistryObject<ConfiguredFeature<CorrockBranchConfig, ?>> EXTRA_BRANCH_DECORATIONS_CORROCK_BRANCH = register("extra_branch_decorations_corrock_branch", () -> new ConfiguredFeature<>(EEFeatures.CORROCK_BRANCH.get(), new CorrockBranchConfig(ImmutableList.of(Blocks.END_STONE.defaultBlockState(), EEBlocks.CORROCK_END_BLOCK.get().defaultBlockState()), 0.4F, 0.5F)));
		public static final RegistryObject<ConfiguredFeature<CorrockBranchConfig, ?>> EXTRA_CROWNS_CORROCK_BRANCH = register("extra_crowns_corrock_branch", () -> new ConfiguredFeature<>(EEFeatures.CORROCK_BRANCH.get(), new CorrockBranchConfig(ImmutableList.of(EEBlocks.CORROCK_END_BLOCK.get().defaultBlockState(), EEBlocks.EUMUS.get().defaultBlockState()), 0.5F, 0.4F)));
		public static final RegistryObject<ConfiguredFeature<ProbabilityFeatureConfiguration, ?>> SMALL_CORROCK_TOWER = register("small_corrock_tower", () -> new ConfiguredFeature<>(EEFeatures.SMALL_CORROCK_TOWER.get(), new ProbabilityFeatureConfiguration(0.25F)));
		public static final RegistryObject<ConfiguredFeature<CorrockTowerConfig, ?>> MEDIUM_CORROCK_TOWER = register("medium_corrock_tower", () -> new ConfiguredFeature<>(EEFeatures.MEDIUM_CORROCK_TOWER.get(), new CorrockTowerConfig(3, 4, 0.7F, 0.075F)));
		public static final RegistryObject<ConfiguredFeature<CorrockTowerConfig, ?>> LARGE_CORROCK_TOWER = register("large_corrock_tower", () -> new ConfiguredFeature<>(EEFeatures.LARGE_CORROCK_TOWER.get(), new CorrockTowerConfig(2, 5, 0.9F, 0.1F)));
		public static final RegistryObject<ConfiguredFeature<WeightedFeatureConfig, ?>> CORROCK_TOWER = register("corrock_tower", () -> new ConfiguredFeature<>(EEFeatures.WEIGHTED_FEATURES.get(), WeightedFeatureConfig.createFromPairs(Pair.of(SMALL_CORROCK_TOWER.getHolder().get(), 6), Pair.of(MEDIUM_CORROCK_TOWER.getHolder().get(), 12), Pair.of(LARGE_CORROCK_TOWER.getHolder().get(), 4))));
		public static final RegistryObject<ConfiguredFeature<ProbabilityFeatureConfiguration, ?>> CORROCK_SHELF = register("corrock_shelf", () -> new ConfiguredFeature<>(EEFeatures.CORROCK_SHELF.get(), new ProbabilityFeatureConfiguration(0.75F)));
		public static final RegistryObject<ConfiguredFeature<CorrockArchConfig, ?>> CORROCK_ARCH = register("corrock_arch", () -> new ConfiguredFeature<>(EEFeatures.CORROCK_ARCH.get(), new CorrockArchConfig(0.1F, 0.25F, 13.0F, 22.0F, 7.0F)));
		public static final RegistryObject<ConfiguredFeature<EndergeticPatchConfig, ?>> EETLE_EGG_PATCH = register("eetle_egg_patch", () -> new ConfiguredFeature<>(EEFeatures.EETLE_EGG.get(), new EndergeticPatchConfig(0.75F, false)));
		public static final RegistryObject<ConfiguredFeature<MultiPatchConfig, ?>> EUMUS_PATCH = register("eumus_patch", () -> new ConfiguredFeature<>(EEFeatures.EUMUS_PATCH.get(), new MultiPatchConfig(2, 3)));
		public static final RegistryObject<ConfiguredFeature<MultiPatchConfig, ?>> SPECKLED_CORROCK_PATCH = register("speckled_corrock_patch", () -> new ConfiguredFeature<>(EEFeatures.SPECKLED_CORROCK_PATCH.get(), new MultiPatchConfig(2, 3)));

		private static <FC extends FeatureConfiguration, F extends Feature<FC>> RegistryObject<ConfiguredFeature<FC, ?>> register(String name, Supplier<ConfiguredFeature<FC, F>> feature) {
			return CONFIGURED_FEATURES.register(name, feature);
		}
	}

	public static final class Placed {
		public static final DeferredRegister<PlacedFeature> PLACED_FEATURES = DeferredRegister.create(Registry.PLACED_FEATURE_REGISTRY, EndergeticExpansion.MOD_ID);

		public static final RegistryObject<PlacedFeature> POISE_DOME = register("poise_dome", Configured.POISE_DOME, PlacementUtils.countExtra(3, 0.02F, 1), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome());
		public static final RegistryObject<PlacedFeature> POISE_TREE = register("poise_tree", Configured.POISE_TREE, PlacementUtils.countExtra(2, 0.05F, 1), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome());
		public static final RegistryObject<PlacedFeature> POISE_CLUSTER = register("poise_cluster", Configured.POISE_CLUSTER, new NoiseHeightmap32Placement(-0.8D, 4, 22), BiomeFilter.biome());
		public static final RegistryObject<PlacedFeature> PUFF_BUG_HIVE = register("puff_bug_hive", Configured.PUFF_BUG_HIVE, new NoiseHeightmap32Placement(-0.8D, 9, 25), BiomeFilter.biome());
		public static final RegistryObject<PlacedFeature> BOLLOOM_BUD = register("bolloom_bud", Configured.BOLLOOM_BUD, new NoiseHeightmap32Placement(-0.9D, 90, 90), BiomeFilter.biome());
		public static final RegistryObject<PlacedFeature> POISE_BUSH_PATCH = register("poise_bush_patch", Configured.POISE_BUSH_PATCH, NoiseThresholdCountPlacement.of(-0.8D, 5, 10), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome());
		public static final RegistryObject<PlacedFeature> TALL_POISE_BUSH_PATCH = register("tall_poise_bush_patch", Configured.TALL_POISE_BUSH_PATCH, new NoiseHeightmap32Placement(-0.8D, 0, 7), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome());

		public static final RegistryObject<PlacedFeature> END_GATEWAY_RETURN = register("end_gateway_return", Configured.END_GATEWAY_RETURN, RarityFilter.onAverageOnceEvery(700), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, RandomOffsetPlacement.vertical(UniformInt.of(3, 9)), BiomeFilter.biome());

		public static final RegistryObject<PlacedFeature> CORROCK_PATCH = register("corrock_patch", Configured.CORROCK_PATCH, CountPlacement.of(3), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome());
		public static final RegistryObject<PlacedFeature> DISK_CORROCK = register("disk_corrock", Configured.DISK_CORROCK, BiomeFilter.biome());
		public static final RegistryObject<PlacedFeature> SPARSE_CORROCK_BRANCH = register("sparse_corrock_branch", Configured.EXTRA_BRANCH_DECORATIONS_CORROCK_BRANCH, CountPlacement.of(5), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome());
		public static final RegistryObject<PlacedFeature> CORROCK_BRANCH = register("corrock_branch", Configured.EXTRA_CROWNS_CORROCK_BRANCH, CountPlacement.of(64), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome());
		public static final RegistryObject<PlacedFeature> CORROCK_TOWER = register("corrock_tower", Configured.CORROCK_TOWER, CountPlacement.of(128), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome());
		public static final RegistryObject<PlacedFeature> CORROCK_SHELF = register("corrock_shelf", Configured.CORROCK_SHELF, CountPlacement.of(8), InSquarePlacement.spread(), HeightmapSpreadLowerPlacement.INSTANCE, BiomeFilter.biome());
		public static final RegistryObject<PlacedFeature> CORROCK_ARCH = register("corrock_arch", Configured.CORROCK_ARCH, CountPlacement.of(26), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome());
		public static final RegistryObject<PlacedFeature> EETLE_EGG_PATCH = register("eetle_egg_patch", Configured.EETLE_EGG_PATCH, CountPlacement.of(1), BiomeFilter.biome());
		public static final RegistryObject<PlacedFeature> EUMUS_PATCH = register("eumus_patch", Configured.EUMUS_PATCH, CountPlacement.of(2), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome());
		public static final RegistryObject<PlacedFeature> SPECKLED_CORROCK_PATCH = register("speckled_corrock", Configured.SPECKLED_CORROCK_PATCH, RarityFilter.onAverageOnceEvery(2), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome());
		
		@SuppressWarnings("unchecked")
		private static RegistryObject<PlacedFeature> register(String name, RegistryObject<? extends ConfiguredFeature<?, ?>> feature, PlacementModifier... placementModifiers) {
			return PLACED_FEATURES.register(name, () -> new PlacedFeature((Holder<ConfiguredFeature<?, ?>>) feature.getHolder().get(), ImmutableList.copyOf(placementModifiers)));
		}
	}
}