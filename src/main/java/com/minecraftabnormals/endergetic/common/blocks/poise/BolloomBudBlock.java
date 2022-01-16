package com.minecraftabnormals.endergetic.common.blocks.poise;

import com.minecraftabnormals.endergetic.common.tileentities.BolloomBudTileEntity;
import com.minecraftabnormals.endergetic.common.tileentities.BolloomBudTileEntity.BudSide;
import com.minecraftabnormals.endergetic.core.registry.EEBlocks;
import com.minecraftabnormals.endergetic.core.registry.other.EETags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

public class BolloomBudBlock extends Block {
	public static final BooleanProperty OPENED = BooleanProperty.create("opened");
	private static final VoxelShape INSIDE = box(3.5D, 0.0D, 3.5D, 12.5D, 15.0D, 12.5D);
	protected static final VoxelShape SHAPE = VoxelShapes.join(box(1.0D, 0.0D, 1.0D, 15.0D, 15.0D, 15.0D), VoxelShapes.or(INSIDE), IBooleanFunction.ONLY_FIRST);
	protected static final VoxelShape SHAPE_OPENED = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 3.0D, 15.0D);

	public BolloomBudBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(OPENED, false));
	}

	@Override
	public VoxelShape getOcclusionShape(BlockState state, IBlockReader worldIn, BlockPos pos) {
		return Block.box(-16.0D, -16.0D, -16.0D, 32.0D, 32.0D, 32.0D);
	}

	@Override
	public void playerWillDestroy(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
		super.playerWillDestroy(worldIn, pos, state, player);
	}

	protected boolean isValidGround(BlockState state, IBlockReader worldIn, BlockPos pos) {
		Block block = state.getBlock();
		return block == Blocks.END_STONE.getBlock() || block.is(EETags.Blocks.END_PLANTABLE) || block.is(EETags.Blocks.POISE_PLANTABLE);
	}

	public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, IWorld world, BlockPos currentPos, BlockPos facingPos) {
		if (stateIn.canSurvive(world, currentPos)) {
			boolean opened = stateIn.getValue(OPENED);
			return this.placePedals(world, currentPos, opened) && opened ? stateIn.setValue(OPENED, true) : this.resetBud(world, currentPos);
		}
		return Blocks.AIR.defaultBlockState();
	}

	public boolean canSurvive(BlockState state, IWorldReader worldIn, BlockPos pos) {
		BlockPos blockpos = pos.below();
		return this.isValidGround(worldIn.getBlockState(blockpos), worldIn, blockpos) && !isAcrossOrAdjacentToBud(worldIn, pos);
	}

	public boolean placePedals(IWorld world, BlockPos pos, boolean opened) {
		if (!world.getBlockState(pos).getValue(OPENED) && this.canPutDownPedals(world, pos)) {
			if (opened) {
				for (BudSide side : BudSide.values()) {
					BlockPos sidePos = side.offsetPosition(pos);
					if (world.getBlockState(sidePos).getCollisionShape(world, pos).isEmpty()) {
						world.destroyBlock(sidePos, true);
					}
				}
			}
			return true;
		} else if (opened) {
			return false;
		}
		return false;
	}

	public static boolean isAcrossOrAdjacentToBud(IWorldReader world, BlockPos pos) {
		Block block = EEBlocks.BOLLOOM_BUD.get();
		for (Direction directions : Direction.values()) {
			if (world.getBlockState(pos.relative(directions, 2)).getBlock() == block) {
				return true;
			}
		}

		BlockPos north = pos.relative(Direction.NORTH);
		BlockPos south = pos.relative(Direction.SOUTH);

		if (world.getBlockState(north.east()).getBlock() == block || world.getBlockState(south.east()).getBlock() == block || world.getBlockState(north.west()).getBlock() == block || world.getBlockState(south.west()).getBlock() == block) {
			return true;
		}
		return false;
	}

	private boolean canPutDownPedals(IWorld world, BlockPos pos) {
		for (BudSide sides : BudSide.values()) {
			BlockPos sidePos = sides.offsetPosition(pos);
			if (!world.getFluidState(sidePos).isEmpty() || !world.getBlockState(sidePos).getCollisionShape(world, sidePos).isEmpty()) {
				return false;
			}
		}
		return true;
	}

	private BlockState resetBud(IWorld world, BlockPos pos) {
		if (world.getBlockEntity(pos) instanceof BolloomBudTileEntity) {
			((BolloomBudTileEntity) world.getBlockEntity(pos)).resetGrowing();
		}
		return this.defaultBlockState();
	}

	@Override
	public VoxelShape getShape(BlockState p_220053_1_, IBlockReader p_220053_2_, BlockPos p_220053_3_, ISelectionContext p_220053_4_) {
		return p_220053_1_.getValue(OPENED) ? SHAPE_OPENED : SHAPE;
	}

	@OnlyIn(Dist.CLIENT)
	public boolean hasCustomBreakingProgress(BlockState state) {
		return true;
	}

	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(OPENED);
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new BolloomBudTileEntity();
	}

	@Override
	public BlockRenderType getRenderShape(BlockState state) {
		return BlockRenderType.ENTITYBLOCK_ANIMATED;
	}
}