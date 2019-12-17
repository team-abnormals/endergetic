package endergeticexpansion.common.world.biomes;

import javax.annotation.Nullable;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.BiomeDictionary;

public abstract class EndergeticBiome extends Biome {

	public EndergeticBiome(Builder biomeBuilder) {
		super(biomeBuilder);
	}
	
	public int getWeight() {
		return 0;
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public int getSkyColorByTemp(float currentTemperature) {
		return 0;
	}
	
	/*
	 * Unused getter, will be used for the end's sky later
	 */
	@OnlyIn(Dist.CLIENT)
	public int getSkyColor() {
		return 0;
	}
	
	@Nullable
	public BiomeDictionary.Type[] getBiomeTypes() {
		return null;
	}
	
}
