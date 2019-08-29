package endergeticexpansion.core.registry;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import endergeticexpansion.common.world.biomes.BiomeChorusPlains;
import endergeticexpansion.common.world.biomes.BiomePoiseForest;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.INoiseRandom;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class EEBiomes {
	private static List<Biome> biomes = Lists.newArrayList();
	private static Map<Biome, Integer> biomeWeights = Maps.newHashMap();
	
	public static final Biome POISE_FOREST = registerBiome(new BiomePoiseForest(), "poise_forest", 7);
	public static final Biome CHORUS_PLAINS = registerBiome(new BiomeChorusPlains(), "chorus_plains", 14);

	public static void registerBiomeDictionaryTags() {
		BiomeDictionary.addTypes(POISE_FOREST, Type.MAGICAL, Type.LUSH, Type.END);
		BiomeDictionary.addTypes(CHORUS_PLAINS, Type.END);
	}
	
	@SubscribeEvent
	public static void registerBiomes(RegistryEvent.Register<Biome> event) {
		 for(Biome biomes : biomes) {
			 event.getRegistry().register(biomes);
		 }
	}
	
	private static Biome registerBiome(Biome biome, String registryName, int weight) {
		biome.setRegistryName(registryName);
		biomes.add(biome);
		biomeWeights.put(biome, weight);
		return biome;
	}
	
	public static Biome getRandomBiome(INoiseRandom context) {
		int totalWeight = 0;
		for(int index = 0; index < biomes.size(); index++) {
			totalWeight += biomeWeights.get(biomes.toArray()[index]);
		}
		int weight = context.random(totalWeight);
		Iterator<Biome> iterator = biomes.iterator();
		Biome biome;
		do {
			biome = iterator.next();
			weight -= biomeWeights.get(biome);
		}
		while(weight >= 0);
		return biome;
	}
}
