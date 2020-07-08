package endergeticexpansion.core.registry;

import java.util.Iterator;
import java.util.function.Supplier;

import endergeticexpansion.common.world.biomes.*;
import endergeticexpansion.core.EndergeticExpansion;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.INoiseRandom;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class EEBiomes {
	private static int TOTAL_WEIGHT;
	public static final DeferredRegister<Biome> BIOMES = DeferredRegister.create(ForgeRegistries.BIOMES, EndergeticExpansion.MOD_ID);
	
	public static final RegistryObject<EndergeticBiome> POISE_FOREST  = createEndBiome("poise_forest", PoiseForestBiome::new);
	public static final RegistryObject<EndergeticBiome> CHORUS_PLAINS = createEndBiome("chorus_plains", ChorusPlainsBiome::new);
	public static final RegistryObject<Biome> END_MIDLANDS            = registerBiome("end_midlands", EndergeticMidlandsBiome::new);
	public static final RegistryObject<Biome> END_HIGHLANDS           = registerBiome("end_highlands", EndergeticHighlandsBiome::new);
	
	public static void applyBiomeInfo() {
		BIOMES.getEntries().forEach((biome) -> {
			Biome endBiome = biome.get();
			if(endBiome instanceof EndergeticBiome) {
				if(endBiome != null) {
					BiomeDictionary.addTypes(endBiome, ((EndergeticBiome) endBiome).getBiomeTypes());
					((EndergeticBiome) endBiome).addSpawnsAndFeatures();
				}
			}
		});
	}
	
	private static RegistryObject<EndergeticBiome> createEndBiome(String name, Supplier<EndergeticBiome> supplier) {
		TOTAL_WEIGHT += supplier.get().getWeight();
		return BIOMES.register(name, supplier);
	}
	
	private static RegistryObject<Biome> registerBiome(String name, Supplier<Biome> supplier) {
		return BIOMES.register(name, supplier);
	}
	
	public static Biome getRandomBiome(INoiseRandom context) {
		int weight = context.random(TOTAL_WEIGHT);
		Iterator<RegistryObject<Biome>> iterator = BIOMES.getEntries().iterator();
		Biome biome;
		do {
			biome = iterator.next().get();
			weight -= ((EndergeticBiome) biome).getWeight();
		}
		while(weight >= 0);
		return biome;
	}
}