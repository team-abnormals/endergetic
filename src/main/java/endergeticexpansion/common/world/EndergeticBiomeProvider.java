package endergeticexpansion.common.world;

import endergeticexpansion.common.world.util.EndergeticLayerUtil;
import endergeticexpansion.core.registry.EEBiomes;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.biome.provider.EndBiomeProvider;
import net.minecraft.world.biome.provider.EndBiomeProviderSettings;
import net.minecraft.world.gen.layer.Layer;

public class EndergeticBiomeProvider extends EndBiomeProvider {
	private final Layer noiseBiomeLayer;
	private final SharedSeedRandom random;
	
	public EndergeticBiomeProvider(EndBiomeProviderSettings settings) {
		super(settings);
		this.random = new SharedSeedRandom(settings.getSeed());
		this.random.skip(17292);
		Layer[] alayer = EndergeticLayerUtil.createGenLayers(settings.getSeed(), WorldType.DEFAULT);
		this.noiseBiomeLayer = alayer[1];
	}
	
	@Override
	public Biome getNoiseBiome(int x, int y, int z) {
		int i = x >> 2;
		int j = z >> 2;
		if((long)i * (long)i + (long)j * (long)j <= 4096L) {
			return Biomes.THE_END;
		} else {
			float f = this.func_222365_c(i * 2 + 1, j * 2 + 1);
			if (f >= 0.0F) {
				return this.isAreaChorus(x, z) ? super.getNoiseBiome(x, y, z) : this.getBiomeForArea(x, z);
			} else {
				return f < -20.0F ? Biomes.SMALL_END_ISLANDS : this.isAreaChorus(x, z) ? super.getNoiseBiome(x, y, z) : this.getBiomeForArea(x, z);
			}
		}
	}
	
	private boolean isAreaChorus(int x, int z) {
		return this.getBiomeForArea(x, z) == EEBiomes.CHORUS_PLAINS.get();
	}
	
	private Biome getBiomeForArea(int x, int z) {
		return this.noiseBiomeLayer.func_215738_a(x, z);
	}
}