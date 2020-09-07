package com.minecraftabnormals.endergetic.common.blocks;

import java.util.Random;

import com.minecraftabnormals.endergetic.core.registry.EEBlocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.entity.Entity;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class FrisbloomBudBlock extends Block {
	public static final IntegerProperty LAYER = IntegerProperty.create("layers_total", 0, 3);
	protected static final VoxelShape SHAPE = Block.makeCuboidShape(4.0D, 0.0D, 4.0D, 12.0D, 16.0D, 12.0D);
	
	public FrisbloomBudBlock(Properties properties) {
		super(properties);
		this.setDefaultState(this.stateContainer.getBaseState()
			.with(LAYER, 0)
		);
	}
	
	@Override
	public VoxelShape getShape(BlockState p_220053_1_, IBlockReader p_220053_2_, BlockPos p_220053_3_, ISelectionContext p_220053_4_) {
		return SHAPE;
	}
	
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(LAYER);
	}
	
	@Override
	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
		return isValidPosition(stateIn, worldIn, currentPos) ? worldIn.getBlockState(currentPos) : Blocks.AIR.getDefaultState();
	}
	
	@Override
	public void tick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		if (!world.isAreaLoaded(pos, 1)) return;
		
		if (random.nextInt(2) == 1 && world.isAirBlock(pos.up())) {
			this.grow(state, world, pos, random);
		}
		
		if (state.get(LAYER) == 3) {
			world.setBlockState(pos, EEBlocks.FRISBLOOM_STEM.getDefaultState().with(FrisbloomStemBlock.LAYER, 4));
		}
	}
	
	public void grow(BlockState state, World worldIn, BlockPos pos, Random random) {
		if (state.get(LAYER) == 0) {
			worldIn.setBlockState(pos, EEBlocks.FRISBLOOM_STEM.getDefaultState().with(FrisbloomStemBlock.LAYER, 1));
			worldIn.setBlockState(pos.up(), this.getDefaultState().with(LAYER, this.getProbabilityForLayer(random, 1)), 2);
		} else if (state.get(LAYER) == 1) {
			worldIn.setBlockState(pos, EEBlocks.FRISBLOOM_STEM.getDefaultState().with(FrisbloomStemBlock.LAYER, 2));
			worldIn.setBlockState(pos.up(), this.getDefaultState().with(LAYER, this.getProbabilityForLayer(random, 2)), 2);
		} else if (state.get(LAYER) == 2) {
			worldIn.setBlockState(pos, EEBlocks.FRISBLOOM_STEM.getDefaultState().with(FrisbloomStemBlock.LAYER, 3));
			worldIn.setBlockState(pos.up(), this.getDefaultState().with(LAYER, this.getProbabilityForLayer(random, 3)), 2);
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
	
	public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
		BlockState ground = worldIn.getBlockState(pos.down());
		return state.get(LAYER) == 0 ? ground.getBlock() == Blocks.END_STONE : ground.getBlock() == EEBlocks.FRISBLOOM_STEM;
	}
	
	@Override
	public SoundType getSoundType(BlockState state, IWorldReader world, BlockPos pos, Entity entity) {
		return SoundType.PLANT;
	}
}