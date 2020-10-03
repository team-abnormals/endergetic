package com.minecraftabnormals.endergetic.common.world.features.corrock;

import java.util.Random;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import com.mojang.serialization.Codec;
import com.minecraftabnormals.endergetic.core.registry.EEBlocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChorusFlowerBlock;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.util.Direction;
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
		private final Direction facing;
		public final BlockPos pos;
		
		public ChorusPlantPart(BlockPos pos, @Nullable Direction facing) {
			this.pos = pos;
			this.facing = facing;
		}

		public void placeGrowth(IWorld world, Random rand) {
			world.setBlockState(this.pos, Blocks.END_STONE.getDefaultState(), 2);
			if (this.facing != null) {
				world.setBlockState(this.pos.offset(this.facing), EEBlocks.ENDSTONE_COVER.get().getDefaultState().with(HorizontalBlock.HORIZONTAL_FACING, this.facing.getOpposite()), 2);
			}
			ChorusFlowerBlock.generatePlant(world, this.pos.up(), rand, 8);
		}
	}
}