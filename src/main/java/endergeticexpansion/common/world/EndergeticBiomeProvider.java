package endergeticexpansion.common.world;

import endergeticexpansion.common.world.util.EndergeticLayerUtil;
import endergeticexpansion.core.registry.EEBiomes;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.biome.provider.EndBiomeProvider;
import net.minecraft.world.biome.provider.EndBiomeProviderSettings;
import net.minecraft.world.gen.SimplexNoiseGenerator;
import net.minecraft.world.gen.layer.Layer;

public class EndergeticBiomeProvider extends EndBiomeProvider {
	private final Layer biomeFactoryLayer;
	private final SimplexNoiseGenerator generator;
	private final SharedSeedRandom random;
	
	public EndergeticBiomeProvider(EndBiomeProviderSettings settings) {
		super(settings);
		this.random = new SharedSeedRandom(settings.getSeed());
		this.random.skip(17292);
		this.generator = new SimplexNoiseGenerator(this.random);
		Layer[] alayer = EndergeticLayerUtil.createGenLayers(settings.getSeed(), WorldType.DEFAULT);
		this.biomeFactoryLayer = alayer[1];
	}
	
	@Override
	public Biome getNoiseBiome(int x, int y, int z) {
		int i = x >> 4;
		int j = z >> 4;
		if((long)i * (long)i + (long)j * (long)j <= 4096L) {
			return Biomes.THE_END;
		} else {
			float f = this.func_222365_c(i * 2 + 1, j * 2 + 1);
			if (f >= 0.0F) {
				return this.biomeFactoryLayer.func_215738_a(x, y) == EEBiomes.CHORUS_PLAINS.get() ? super.getNoiseBiome(x, y, z) : this.biomeFactoryLayer.func_215738_a(x, y);
			} else {
				return f < -20.0F ? Biomes.SMALL_END_ISLANDS : this.biomeFactoryLayer.func_215738_a(x, y) == EEBiomes.CHORUS_PLAINS.get() ? super.getNoiseBiome(x, y, z) : this.biomeFactoryLayer.func_215738_a(x, y);
			}
		}
	}

	public float func_222365_c(int p_222365_1_, int p_222365_2_) {
		int i = p_222365_1_ / 2;
		int j = p_222365_2_ / 2;
		int k = p_222365_1_ % 2;
		int l = p_222365_2_ % 2;
		float f = 100.0F - MathHelper.sqrt((float)(p_222365_1_ * p_222365_1_ + p_222365_2_ * p_222365_2_)) * 8.0F;
		f = MathHelper.clamp(f, -100.0F, 80.0F);

		for(int i1 = -12; i1 <= 12; ++i1) {
			for(int j1 = -12; j1 <= 12; ++j1) {
				long k1 = (long)(i + i1);
				long l1 = (long)(j + j1);
				if (k1 * k1 + l1 * l1 > 4096L && this.generator.getValue((double)k1, (double)l1) < (double)-0.9F) {
					float f1 = (MathHelper.abs((float)k1) * 3439.0F + MathHelper.abs((float)l1) * 147.0F) % 13.0F + 9.0F;
					float f2 = (float)(k - i1 * 2);
					float f3 = (float)(l - j1 * 2);
					float f4 = 100.0F - MathHelper.sqrt(f2 * f2 + f3 * f3) * f1;
					f4 = MathHelper.clamp(f4, -100.0F, 80.0F);
					f = Math.max(f, f4);
				}
			}
		}
		return f;
	}
}