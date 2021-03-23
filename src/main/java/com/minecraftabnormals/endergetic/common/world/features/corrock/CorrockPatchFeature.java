package com.minecraftabnormals.endergetic.common.world.features.corrock;

import java.util.Random;

import com.minecraftabnormals.endergetic.common.world.configs.CorrockPatchConfig;
import com.minecraftabnormals.endergetic.core.registry.EEBlocks;
import com.mojang.serialization.Codec;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;

public class CorrockPatchFeature extends AbstractCorrockFeature<CorrockPatchConfig> {

	public CorrockPatchFeature(Codec<CorrockPatchConfig> config) {
		super(config);
	}

	public boolean generate(ISeedReader world, ChunkGenerator generator, Random rand, BlockPos pos, CorrockPatchConfig config) {
		BlockPos blockpos = getPos(world, pos, config.shouldSearchDown());
		Block downBlock = world.getBlockState(blockpos.down()).getBlock();
		if (downBlock == CORROCK_BLOCK_BLOCK || downBlock == EEBlocks.EUMUS.get()) {
			int i = 0;
			BlockPos.Mutable mutable = new BlockPos.Mutable();

			float plantFrequency = config.getPlantFrequency();
			for (int j = 0; j < 32; ++j) {
				mutable.setAndOffset(blockpos, rand.nextInt(8) - rand.nextInt(8), rand.nextInt(4) - rand.nextInt(4), rand.nextInt(8) - rand.nextInt(8));
				BlockState state = CORROCK_STATE.getValue();
				if (world.isAirBlock(mutable) && state.isValidPosition(world, mutable)) {
					if (world.getBlockState(mutable.down()).getBlock() == CORROCK_BLOCK_BLOCK || rand.nextFloat() < plantFrequency) {
						world.setBlockState(mutable, state, 2);
						++i;
					}
				}
			}
			return i > 0;
		}
		return false;
	}

	@SuppressWarnings("deprecation")
	private static BlockPos getPos(ISeedReader world, BlockPos pos, boolean searchDown) {
		if (searchDown) {
			BlockPos.Mutable mutable = pos.toMutable();
			for (BlockState blockstate = world.getBlockState(mutable); (blockstate.isAir()) && mutable.getY() > 0; blockstate = world.getBlockState(mutable)) {
				mutable.move(Direction.DOWN);
			}
			return mutable;
		}
		return world.getHeight(Heightmap.Type.WORLD_SURFACE_WG, pos);
	}

}