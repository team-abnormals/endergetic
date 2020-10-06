package com.minecraftabnormals.endergetic.common.world.features.corrock;

import java.util.Random;
import java.util.function.Supplier;

import com.mojang.serialization.Codec;
import com.minecraftabnormals.endergetic.core.registry.EEBlocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.ChorusFlowerBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.ProbabilityConfig;

public abstract class AbstractCorrockFeature extends Feature<ProbabilityConfig> {
	protected static final Supplier<BlockState> CORROCK = () -> EEBlocks.CORROCK_END.get().getDefaultState();
	protected static final Supplier<BlockState> CORROCK_BLOCK = () -> EEBlocks.CORROCK_END_BLOCK.get().getDefaultState();

	protected static Supplier<BlockState> CORROCK_CROWN(boolean wall) {
		return wall ? () -> EEBlocks.CORROCK_CROWN_END_WALL.get().getDefaultState() : () -> EEBlocks.CORROCK_CROWN_END_STANDING.get().getDefaultState();
	}

	public AbstractCorrockFeature(Codec<ProbabilityConfig> configFactory) {
		super(configFactory);
	}

	protected static class ChorusPlantPart {
		public final BlockPos pos;

		public ChorusPlantPart(BlockPos pos) {
			this.pos = pos;
		}

		public void placeGrowth(IWorld world, Random rand) {
			world.setBlockState(this.pos, CORROCK_BLOCK.get(), 2);
			ChorusFlowerBlock.generatePlant(world, this.pos.up(), rand, 8);
		}
	}
}