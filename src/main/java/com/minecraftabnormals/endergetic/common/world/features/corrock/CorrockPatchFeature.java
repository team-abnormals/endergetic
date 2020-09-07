package com.minecraftabnormals.endergetic.common.world.features.corrock;

import java.util.Random;
import java.util.function.Supplier;

import com.minecraftabnormals.endergetic.core.registry.EEBlocks;
import com.mojang.serialization.Codec;

import net.minecraft.block.BlockState;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.StructureManager;

public class CorrockPatchFeature extends Feature<NoFeatureConfig> {
	private static final Supplier<BlockState> CORROCK = () -> EEBlocks.CORROCK_END.get().getDefaultState();
	
	public CorrockPatchFeature(Codec<NoFeatureConfig> config) {
		super(config);
	}

	@SuppressWarnings("deprecation")
	public boolean func_230362_a_(ISeedReader world, StructureManager manager, ChunkGenerator generator, Random rand, BlockPos pos, NoFeatureConfig config) {
		for(BlockState blockstate = world.getBlockState(pos); (blockstate.isAir() || blockstate.isIn(BlockTags.LEAVES)) && pos.getY() > 0; blockstate = world.getBlockState(pos)) {
			pos = pos.down();
		}
		if (world.getBlockState(pos).getBlock() != EEBlocks.CORROCK_END_BLOCK.get()) return false;
		int i = 0;
		
		for (int j = 0; j < 128; ++j) {
			BlockPos blockpos = pos.add(rand.nextInt(8) - rand.nextInt(8), rand.nextInt(6) - rand.nextInt(6), rand.nextInt(8) - rand.nextInt(8));
			if (world.isAirBlock(blockpos) && CORROCK.get().isValidPosition(world, blockpos)) {
				boolean chance = world.getBlockState(blockpos.down()).getBlock() == EEBlocks.CORROCK_END_BLOCK.get() ? rand.nextFloat() < 0.85F : rand.nextFloat() < 0.25F;
				if (chance) {
					world.setBlockState(blockpos, CORROCK.get(), 2);
					i++;
				}
			}
		}
		return i > 0;
	}
}