package endergeticexpansion.core.registry;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import endergeticexpansion.common.world.biomes.BiomeChorusPlains;
import endergeticexpansion.common.world.biomes.BiomePoiseForest;
import endergeticexpansion.common.world.biomes.EndergeticBiome;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.INoiseRandom;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class EEBiomes {
	public static final List<EndergeticBiome> BIOMES = Lists.newArrayList();
	private static final Map<Biome, Integer> WEIGHTS = Maps.newHashMap();
	
	public static final Biome POISE_FOREST = registerBiome(new BiomePoiseForest(), "poise_forest", 6);
	public static final Biome CHORUS_PLAINS = registerBiome(new BiomeChorusPlains(), "chorus_plains", 15);

	public static void registerBiomeDictionaryTags() {
		for(EndergeticBiome biomes : BIOMES) {
			if(biomes.getBiomeTypes() != null) BiomeDictionary.addTypes(biomes, biomes.getBiomeTypes());
		}
	}
	
	@SubscribeEvent
	public static void registerBiomes(RegistryEvent.Register<Biome> event) {
		 for(Biome biomes : BIOMES) {
			 event.getRegistry().register(biomes);
		 }
	}
	
	private static EndergeticBiome registerBiome(EndergeticBiome biome, String registryName, int weight) {
		biome.setRegistryName(registryName);
		BIOMES.add(biome);
		WEIGHTS.put(biome, weight);
		return biome;
	}
	
	public static Biome getRandomBiome(INoiseRandom context) {
		int totalWeight = 0;
		for(int index = 0; index < BIOMES.size(); index++) {
			totalWeight += WEIGHTS.get(BIOMES.toArray()[index]);
		}
		int weight = context.random(totalWeight);
		Iterator<EndergeticBiome> iterator = BIOMES.iterator();
		Biome biome;
		do {
			biome = iterator.next();
			weight -= WEIGHTS.get(biome);
		}
		while(weight >= 0);
		return biome;
	}
}
