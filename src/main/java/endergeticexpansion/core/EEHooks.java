package endergeticexpansion.core;

import java.util.Map;

import endergeticexpansion.common.blocks.EnderFireBlock;
import endergeticexpansion.common.world.util.EndergeticLayerUtil;
import endergeticexpansion.core.registry.EEBiomes;
import endergeticexpansion.core.registry.EEBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FireBlock;
import net.minecraft.block.SixWayBlock;
import net.minecraft.block.SoulFireBlock;
import net.minecraft.state.BooleanProperty;
import net.minecraft.util.Direction;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.biome.provider.EndBiomeProvider;
import net.minecraft.world.gen.SimplexNoiseGenerator;
import net.minecraft.world.gen.layer.Layer;

public final class EEHooks {
	private static final Map<Direction, BooleanProperty> FACING_TO_PROPERTY_MAP = SixWayBlock.FACING_TO_PROPERTY_MAP.entrySet().stream().filter((direction) -> {
		return direction.getKey() != Direction.DOWN;
	}).collect(Util.toMapCollector());
	private static SimplexNoiseGenerator generator;
	private static Layer noiseBiomeLayer;
	
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
			Biome biome = noiseBiomeLayer.func_215738_a(x, z);
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
	
	public static BlockState getFireForPlacement(IBlockReader reader, BlockPos pos) {
		BlockPos blockpos = pos.down();
		Block block = reader.getBlockState(blockpos).getBlock();
		if (SoulFireBlock.func_235577_c_(block)) {
			return Blocks.SOUL_FIRE.getDefaultState();
		} else if (EnderFireBlock.isEnderFireBase(block)) {
			return EEBlocks.ENDER_FIRE.get().getDefaultState();
		}
		return getNormalFirePlacement(reader, pos);
	}
	
	private static BlockState getNormalFirePlacement(IBlockReader reader, BlockPos pos) {
		BlockPos downPos = pos.down();
		BlockState blockstate = reader.getBlockState(downPos);
		FireBlock fire = (FireBlock) Blocks.FIRE;
		if (!fire.canCatchFire(reader, pos, Direction.UP) && !Block.hasSolidSide(blockstate, reader, downPos, Direction.UP)) {
			BlockState blockstate1 = fire.getDefaultState();

			for(Direction direction : Direction.values()) {
				BooleanProperty booleanproperty = FACING_TO_PROPERTY_MAP.get(direction);
				if (booleanproperty != null) {
					blockstate1 = blockstate1.with(booleanproperty, Boolean.valueOf(fire.canCatchFire(reader, pos.offset(direction), direction.getOpposite())));
				}
			}

			return blockstate1;
		} else {
			return fire.getDefaultState();
		}
	}
	
	public static BlockState getEnderCrystalFireForPlacement(IBlockReader reader, BlockPos pos) {
		return EEBlocks.ENDER_FIRE.get().getDefaultState();
	}
}