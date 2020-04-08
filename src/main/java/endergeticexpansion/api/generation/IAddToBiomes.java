package endergeticexpansion.api.generation;

import java.util.function.Consumer;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;

/**
 * @author SmellyModder(Luke Tonon)
 * Simple interface used for adding features to vanilla biomes
 */
public interface IAddToBiomes {
	Consumer<Biome> processBiomeAddition();
	
	static boolean isInChorusBiome(Biome biome) {
		return biome == Biomes.END_MIDLANDS || biome == Biomes.END_HIGHLANDS;
	}
}