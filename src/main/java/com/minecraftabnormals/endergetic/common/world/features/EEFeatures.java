package com.minecraftabnormals.endergetic.common.world.features;

import java.util.function.Supplier;

import com.minecraftabnormals.endergetic.common.world.features.corrock.*;
import com.minecraftabnormals.endergetic.core.EndergeticExpansion;

import net.minecraft.world.gen.feature.EndGatewayConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.ProbabilityConfig;
import net.minecraft.world.gen.feature.SphereReplaceConfig;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class EEFeatures {
	public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, EndergeticExpansion.MOD_ID);
	
	public static final RegistryObject<Feature<NoFeatureConfig>> POISE_GRASS       = createFeature("poise_bush", () -> new PoiseBushFeature(NoFeatureConfig.field_236558_a_));
	public static final RegistryObject<Feature<NoFeatureConfig>> POISE_TALLGRASS   = createFeature("poise_tallgrass", () -> new TallPoiseBushFeature(NoFeatureConfig.field_236558_a_));
	public static final RegistryObject<Feature<NoFeatureConfig>> POISE_CLUSTER     = createFeature("poise_cluster", () -> new PoiseClusterFeature(NoFeatureConfig.field_236558_a_));
	public static final RegistryObject<Feature<NoFeatureConfig>> BOLLOOM_BUD       = createFeature("bolloom_bud", () -> new BolloomBudFeature(NoFeatureConfig.field_236558_a_));
	public static final RegistryObject<Feature<NoFeatureConfig>> PUFFBUG_HIVE      = createFeature("puffbug_hive", () -> new PuffBugHiveFeature(NoFeatureConfig.field_236558_a_));
	public static final RegistryObject<Feature<NoFeatureConfig>> POISE_DOME        = createFeature("poise_dome", () -> new PoiseDomeFeature(NoFeatureConfig.field_236558_a_));
	public static final RegistryObject<Feature<NoFeatureConfig>> POISE_TREE        = createFeature("poise_tree", () -> new PoiseTreeFeature(NoFeatureConfig.field_236558_a_));
	
	public static final RegistryObject<Feature<NoFeatureConfig>> CORROCK_PATCH     = createFeature("corrock_patch", () -> new CorrockPatchFeature(NoFeatureConfig.field_236558_a_));
	public static final RegistryObject<Feature<SphereReplaceConfig>> GROUND_PATCH  = createFeature("ground_patch", () -> new GroundPatchFeature(SphereReplaceConfig.field_236516_a_));
	public static final RegistryObject<Feature<ProbabilityConfig>> CORROCK_BRANCH  = createFeature("corrock_branch", () -> new CorrockBranchFeature(ProbabilityConfig.field_236576_b_));
	public static final RegistryObject<Feature<ProbabilityConfig>> CORROCK_TOWER   = createFeature("corrock_tower", () -> new CorrockTowerFeature(ProbabilityConfig.field_236576_b_));
	
	public static final Feature<EndGatewayConfig> ENDERGETIC_GATEWAY = new EndergeticEndGatewayFeature(EndGatewayConfig.field_236522_a_);
	
	private static <F extends Feature<?>> RegistryObject<F> createFeature(String name, Supplier<F> feature) {
		return FEATURES.register(name, feature);
	}
}