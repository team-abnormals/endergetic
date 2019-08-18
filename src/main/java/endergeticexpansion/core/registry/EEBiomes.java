package endergeticexpansion.core.registry;

import java.util.List;

import com.google.common.collect.Lists;

import endergeticexpansion.common.world.biomes.BiomePoiseForest;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class EEBiomes {
	private static List<Biome> biomes = Lists.newArrayList();
	public static final Biome POISE_FOREST = registerBiome(new BiomePoiseForest(), "poise_forest", false);

	public static void registerBiomeDictionaryTags() {
		BiomeDictionary.addTypes(POISE_FOREST, Type.MAGICAL, Type.LUSH, Type.END);
	}
	
	@SubscribeEvent
	public static void registerBiomes(RegistryEvent.Register<Biome> event) {
		 for(Biome biomes : biomes) {
			 event.getRegistry().register(biomes);
		 }
	}
	
	private static Biome registerBiome(Biome biome, String registryName, boolean spawnable) {
		biome.setRegistryName(registryName);
		biomes.add(biome);
		if(spawnable) {
			BiomeManager.addSpawnBiome(biome);
		}
		return biome;
	}
}
