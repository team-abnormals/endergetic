package endergeticexpansion.common.world.features;

import java.util.List;

import com.google.common.collect.Lists;

import endergeticexpansion.core.EndergeticExpansion;
import net.minecraft.world.gen.feature.DoublePlantConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@SuppressWarnings({"rawtypes", "unchecked"})
public class EEFeatures {
	private static List<Feature<?>> features = Lists.newArrayList();
	
	public static final Feature<DoublePlantConfig> POISE_TALLGRASS = registerFeature("poise_tallgrass", new FeatureTallPoiseGrass(DoublePlantConfig::deserialize));
	public static final Feature<NoFeatureConfig> POISE_CLUSTER     = registerFeature("poise_cluster", new FeaturePoiseCluster(NoFeatureConfig::deserialize));
	
	private static Feature registerFeature(String name, Feature feature){
		feature.setRegistryName(EndergeticExpansion.MOD_ID, name);
		features.add(feature);
		return feature;
	}
	
	@SubscribeEvent
	public static void registerFeatures(RegistryEvent.Register<Feature<?>> event) {
		for(Feature features : features) {
			event.getRegistry().register(features);
		}
	}
}
