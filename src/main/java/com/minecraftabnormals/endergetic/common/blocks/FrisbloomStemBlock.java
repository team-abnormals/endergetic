package com.minecraftabnormals.endergetic.common.blocks;

import java.util.Random;

import javax.annotation.Nullable;

import com.minecraftabnormals.endergetic.common.tileentities.FrisbloomStemTileEntity;
import com.minecraftabnormals.endergetic.core.registry.EEBlocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.entity.BlockEntity;
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

public class FrisbloomStemBlock extends Block {
	//The height position of the current stem; 5 layers total
	public static final IntegerProperty LAYER = IntegerProperty.create("layer", 0, 4);
	protected static final VoxelShape SHAPE = Block.box(4.0D, 0.0D, 4.0D, 12.0D, 16.0D, 12.0D);

	public FrisbloomStemBlock(Properties properties) {
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
	public void tick(BlockState state, ServerLevel world, BlockPos pos, Random random) {
		if (!world.isAreaLoaded(pos, 0)) return;
		if (!state.canSurvive(world, pos)) {
			world.destroyBlock(pos, true);
		}
	}

	public boolean canSurvive(BlockState state, LevelReader worldIn, BlockPos pos) {
		BlockState ground = worldIn.getBlockState(pos.below());
		return ground.getBlock() == Blocks.END_STONE || ground.getBlock() == EEBlocks.FRISBLOOM_STEM && !worldIn.getBlockState(pos.above()).getMaterial().isLiquid();
	}

	@Override
	public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
		worldIn.getBlockTicks().scheduleTick(currentPos, this, 60 + worldIn.getRandom().nextInt(40));
		return canSurvive(stateIn, worldIn, currentPos) ? worldIn.getBlockState(currentPos) : Blocks.AIR.defaultBlockState();
	}

	@Override
	@Nullable
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		Level world = context.getLevel();
		BlockPos pos = context.getClickedPos().below();
		if (world.getBlockState(pos).getBlock() == Blocks.END_STONE) {
			return this.defaultBlockState();
		} else if (world.getBlockState(pos).getBlock() == this) {
			return world.getBlockState(pos).setValue(LAYER, world.getBlockState(pos).getValue(LAYER) + 1);
		}
		return null;
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Nullable
	@Override
	public BlockEntity createTileEntity(BlockState state, BlockGetter world) {
		return new FrisbloomStemTileEntity();
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.ENTITYBLOCK_ANIMATED;
	}
}