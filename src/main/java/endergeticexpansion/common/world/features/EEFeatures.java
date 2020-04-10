package endergeticexpansion.common.world.features;

import java.util.function.Supplier;

import endergeticexpansion.common.world.features.corrock.*;
import endergeticexpansion.core.EndergeticExpansion;
import net.minecraft.world.gen.feature.EndGatewayConfig;
import net.minecraft.world.gen.feature.EndSpikeFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.ProbabilityConfig;
import net.minecraft.world.gen.feature.SphereReplaceConfig;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class EEFeatures {
	public static final DeferredRegister<Feature<?>> FEATURES = new DeferredRegister<>(ForgeRegistries.FEATURES, EndergeticExpansion.MOD_ID);
	
	public static final RegistryObject<Feature<NoFeatureConfig>> POISE_GRASS       = createFeature("poise_grass", () -> new FeaturePoiseGrass(NoFeatureConfig::deserialize));
	public static final RegistryObject<Feature<NoFeatureConfig>> POISE_TALLGRASS   = createFeature("poise_tallgrass", () -> new FeatureTallPoiseGrass(NoFeatureConfig::deserialize));
	public static final RegistryObject<Feature<NoFeatureConfig>> POISE_CLUSTER     = createFeature("poise_cluster", () -> new FeaturePoiseCluster(NoFeatureConfig::deserialize));
	public static final RegistryObject<Feature<NoFeatureConfig>> BOLLOOM_BUD       = createFeature("bolloom_bud", () -> new FeatureBolloomBud(NoFeatureConfig::deserialize));
	public static final RegistryObject<Feature<NoFeatureConfig>> PUFFBUG_HIVE      = createFeature("puffbug_hive", () -> new FeaturePuffBugHive(NoFeatureConfig::deserialize));
	public static final RegistryObject<Feature<NoFeatureConfig>> POISE_DOME        = createFeature("poise_dome", () -> new FeaturePoiseDome(NoFeatureConfig::deserialize));
	public static final RegistryObject<Feature<NoFeatureConfig>> POISE_TREE        = createFeature("poise_tree", () -> new FeaturePoiseTree(NoFeatureConfig::deserialize));
	
	public static final RegistryObject<Feature<NoFeatureConfig>> CORROCK_PATCH     = createFeature("corrock_patch", () -> new FeatureCorrockPatch(NoFeatureConfig::deserialize));
	public static final RegistryObject<Feature<SphereReplaceConfig>> CORROCK_GROUND_PATCH = createFeature("corrock_ground_patch", () -> new FeatureCorrockGroundPatch(SphereReplaceConfig::deserialize));
	public static final RegistryObject<Feature<ProbabilityConfig>> CORROCK_BRANCH  = createFeature("corrock_branch", () -> new FeatureCorrockBranch(ProbabilityConfig::deserialize));
	
	public static final Feature<EndGatewayConfig> ENDERGETIC_GATEWAY =  new EndergeticEndGatewayFeature(EndGatewayConfig::deserialize);
	public static final Feature<EndSpikeFeatureConfig> ENDERGETIC_END_SPIKE =  new EndergeticEndSpikeFeature(EndSpikeFeatureConfig::deserialize);
	
	private static <F extends Feature<?>> RegistryObject<F> createFeature(String name, Supplier<F> feature) {
		return FEATURES.register(name, feature);
	}
}