package com.minecraftabnormals.endergetic.common.blocks;

import java.util.Random;

import com.minecraftabnormals.endergetic.core.registry.EEBlocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class FrisbloomBudBlock extends Block {
	public static final IntegerProperty LAYER = IntegerProperty.create("layers_total", 0, 3);
	protected static final VoxelShape SHAPE = Block.box(4.0D, 0.0D, 4.0D, 12.0D, 16.0D, 12.0D);

	public FrisbloomBudBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any()
				.setValue(LAYER, 0)
		);
	}

	@Override
	public VoxelShape getShape(BlockState p_220053_1_, BlockGetter p_220053_2_, BlockPos p_220053_3_, CollisionContext p_220053_4_) {
		return SHAPE;
	}

	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(LAYER);
	}

	@Override
	public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
		return canSurvive(stateIn, worldIn, currentPos) ? worldIn.getBlockState(currentPos) : Blocks.AIR.defaultBlockState();
	}

	@Override
	public void tick(BlockState state, ServerLevel world, BlockPos pos, Random random) {
		if (!world.isAreaLoaded(pos, 1)) return;

		if (random.nextInt(2) == 1 && world.isEmptyBlock(pos.above())) {
			this.grow(state, world, pos, random);
		}

		if (state.getValue(LAYER) == 3) {
			world.setBlockAndUpdate(pos, EEBlocks.FRISBLOOM_STEM.defaultBlockState().setValue(FrisbloomStemBlock.LAYER, 4));
		}
	}

	public void grow(BlockState state, Level worldIn, BlockPos pos, Random random) {
		if (state.getValue(LAYER) == 0) {
			worldIn.setBlockAndUpdate(pos, EEBlocks.FRISBLOOM_STEM.defaultBlockState().setValue(FrisbloomStemBlock.LAYER, 1));
			worldIn.setBlock(pos.above(), this.defaultBlockState().setValue(LAYER, this.getProbabilityForLayer(random, 1)), 2);
		} else if (state.getValue(LAYER) == 1) {
			worldIn.setBlockAndUpdate(pos, EEBlocks.FRISBLOOM_STEM.defaultBlockState().setValue(FrisbloomStemBlock.LAYER, 2));
			worldIn.setBlock(pos.above(), this.defaultBlockState().setValue(LAYER, this.getProbabilityForLayer(random, 2)), 2);
		} else if (state.getValue(LAYER) == 2) {
			worldIn.setBlockAndUpdate(pos, EEBlocks.FRISBLOOM_STEM.defaultBlockState().setValue(FrisbloomStemBlock.LAYER, 3));
			worldIn.setBlock(pos.above(), this.defaultBlockState().setValue(LAYER, this.getProbabilityForLayer(random, 3)), 2);
		}
	}

	public int getProbabilityForLayer(Random random, int layer) {
		if (layer == 1) {
			return random.nextInt(3) == 1 ? 1 : 0;
		} else if (layer == 1) {
			return random.nextInt(2) == 1 ? 2 : 1;
		}
		return random.nextInt(2) == 1 ? 3 : 2;
	}

	public boolean canSurvive(BlockState state, LevelReader worldIn, BlockPos pos) {
		BlockState ground = worldIn.getBlockState(pos.below());
		return state.getValue(LAYER) == 0 ? ground.getBlock() == Blocks.END_STONE : ground.getBlock() == EEBlocks.FRISBLOOM_STEM;
	}

	@Override
	public SoundType getSoundType(BlockState state, LevelReader world, BlockPos pos, Entity entity) {
		return SoundType.GRASS;
	}
}