package endergeticexpansion.core;

import endergeticexpansion.common.world.EndergeticDragonFightManager;
import endergeticexpansion.common.world.util.EndergeticLayerUtil;
import endergeticexpansion.core.registry.EEBiomes;
import endergeticexpansion.core.registry.EEBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.biome.provider.EndBiomeProvider;
import net.minecraft.world.end.DragonFightManager;
import net.minecraft.world.gen.SimplexNoiseGenerator;
import net.minecraft.world.gen.layer.Layer;
import net.minecraft.world.server.ServerWorld;

public final class EEHooks {
	private static SimplexNoiseGenerator generator;
	private static Layer noiseBiomeLayer;
	
	public static DragonFightManager createEndergeticDragonFightManager(ServerWorld world, MinecraftServer server) {
		return world.func_230315_m_().func_236046_h_() ? new EndergeticDragonFightManager(world, server.func_240793_aU_().func_230418_z_().func_236221_b_(), server.func_240793_aU_().func_230402_B_()) : null;
	}
	
	public static Layer test(EEHooks hooks, SimplexNoiseGenerator generator) {
		return noiseBiomeLayer;
	}
	
	public static Biome getEndergeticNoiseBiome(long seed, int x, int y, int z) {
		if (noiseBiomeLayer == null) {
			SharedSeedRandom sharedseedrandom = new SharedSeedRandom(seed);
			sharedseedrandom.skip(17292);
			generator = new SimplexNoiseGenerator(sharedseedrandom);
			noiseBiomeLayer = EndergeticLayerUtil.createGenLayers(seed)[1];
		}
		int i = x >> 2;
		int j = z >> 2;
		if ((long) i * (long) i + (long) j * (long) j <= 4096L) {
			return Biomes.THE_END;
		} else {
			float f = EndBiomeProvider.func_235317_a_(generator, i * 2 + 1, j * 2 + 1);
			Biome biome = getBiomeForArea(x, z);
			boolean isChorus = biome == EEBiomes.CHORUS_PLAINS.get();
			if (f > 40.0F) {
				return isChorus ? Biomes.END_HIGHLANDS : biome;
			} else if (f >= 0.0F) {
				return isChorus ? Biomes.END_MIDLANDS : biome;
			} else {
				return f < -20.0F ? Biomes.SMALL_END_ISLANDS : isChorus ? Biomes.END_BARRENS : biome;
			}
		}
	}
	
	public static BlockState getEnderCrystalFireForPlacement(IBlockReader reader, BlockPos pos) {
		return EEBlocks.ENDER_FIRE.getDefaultState();
	}

	private static Biome getBiomeForArea(int x, int z) {
		return noiseBiomeLayer.func_215738_a(x, z);
	}
}