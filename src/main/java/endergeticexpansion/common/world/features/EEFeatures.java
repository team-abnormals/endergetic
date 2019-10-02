package endergeticexpansion.common.world.features;

import java.util.List;

import com.google.common.collect.Lists;

import endergeticexpansion.core.EndergeticExpansion;
import net.minecraft.world.gen.feature.DoublePlantConfig;
import net.minecraft.world.gen.feature.EndGatewayConfig;
import net.minecraft.world.gen.feature.EndSpikeFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.GrassFeatureConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@SuppressWarnings({"rawtypes", "unchecked"})
public class EEFeatures {
	private static List<Feature<?>> features = Lists.newArrayList();
	
	public static final Feature<GrassFeatureConfig> POISE_GRASS    = registerFeature("poise_grass", new FeaturePoiseGrass(GrassFeatureConfig::deserialize));
	public static final Feature<DoublePlantConfig> POISE_TALLGRASS = registerFeature("poise_tallgrass", new FeatureTallPoiseGrass(DoublePlantConfig::deserialize));
	public static final Feature<NoFeatureConfig> POISE_CLUSTER     = registerFeature("poise_cluster", new FeaturePoiseCluster(NoFeatureConfig::deserialize));
	public static final Feature<NoFeatureConfig> BOLLOOM_BUD       = registerFeature("bolloom_bud", new FeatureBolloomBud(NoFeatureConfig::deserialize));
	public static final Feature<NoFeatureConfig> PUFFBUG_HIVE      = registerFeature("puffbug_hive", new FeaturePuffBugHive(NoFeatureConfig::deserialize));
	public static final Feature<NoFeatureConfig> POISE_DOME        = registerFeature("poise_dome", new FeaturePoiseDome(NoFeatureConfig::deserialize));
	public static final Feature<NoFeatureConfig> POISE_TREE        = registerFeature("poise_tree", new FeaturePoiseTree(NoFeatureConfig::deserialize));
	
	public static final Feature ENDERGETIC_GATEWAY = registerFeature("end_gateway", new EndergeticEndGatewayFeature(EndGatewayConfig::deserialize));
	public static final Feature<EndSpikeFeatureConfig> ENDERGETIC_END_SPIKE = registerFeature("end_spike", new EndergeticEndSpikeFeature(EndSpikeFeatureConfig::deserialize));
	
	private static Feature registerFeature(String name, Feature feature){
		feature.setRegistryName(EndergeticExpansion.MOD_ID, name);
		features.add(feature);
		return feature;
	}
	
	@SuppressWarnings("unused")
	private static Structure<?> registerStructure(String name, Structure<?> structure){
		structure.setRegistryName(EndergeticExpansion.MOD_ID, name);
		features.add(structure);
		Feature.STRUCTURES.put(name, structure);
		return structure;
	}
	
	@SubscribeEvent
	public static void registerFeatures(RegistryEvent.Register<Feature<?>> event) {
		for(Feature features : features) {
			event.getRegistry().register(features);
		}
	}
}
