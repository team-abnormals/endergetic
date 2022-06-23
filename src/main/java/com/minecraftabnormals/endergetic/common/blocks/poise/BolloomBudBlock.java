package com.minecraftabnormals.endergetic.common.blocks.poise;

import com.minecraftabnormals.endergetic.common.tileentities.BolloomBudTileEntity;
import com.minecraftabnormals.endergetic.common.tileentities.BolloomBudTileEntity.BudSide;
import com.minecraftabnormals.endergetic.core.registry.EEBlocks;
import com.minecraftabnormals.endergetic.core.registry.EETileEntities;
import com.minecraftabnormals.endergetic.core.registry.other.EETags;

import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.Nullable;

public class BolloomBudBlock extends BaseEntityBlock {
	public static final BooleanProperty OPENED = BooleanProperty.create("opened");
	private static final VoxelShape INSIDE = box(3.5D, 0.0D, 3.5D, 12.5D, 15.0D, 12.5D);
	protected static final VoxelShape SHAPE = Shapes.join(box(1.0D, 0.0D, 1.0D, 15.0D, 15.0D, 15.0D), Shapes.or(INSIDE), BooleanOp.ONLY_FIRST);
	protected static final VoxelShape SHAPE_OPENED = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 3.0D, 15.0D);

	public BolloomBudBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(OPENED, false));
	}

	@Override
	public VoxelShape getOcclusionShape(BlockState state, BlockGetter worldIn, BlockPos pos) {
		return Block.box(-16.0D, -16.0D, -16.0D, 32.0D, 32.0D, 32.0D);
	}

	@Override
	public void playerWillDestroy(Level worldIn, BlockPos pos, BlockState state, Player player) {
		super.playerWillDestroy(worldIn, pos, state, player);
	}

	protected boolean isValidGround(BlockState state, BlockGetter getter, BlockPos pos) {
		return state.is(Blocks.END_STONE) || state.is(EETags.Blocks.END_PLANTABLE) || state.is(EETags.Blocks.POISE_PLANTABLE);
	}

	@Override
	public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor world, BlockPos currentPos, BlockPos facingPos) {
		if (stateIn.canSurvive(world, currentPos)) {
			boolean opened = stateIn.getValue(OPENED);
			return this.placePedals(world, currentPos, opened) && opened ? stateIn.setValue(OPENED, true) : this.resetBud(world, currentPos);
		}
		return Blocks.AIR.defaultBlockState();
	}

	@Override
	public boolean canSurvive(BlockState state, LevelReader worldIn, BlockPos pos) {
		BlockPos blockpos = pos.below();
		return this.isValidGround(worldIn.getBlockState(blockpos), worldIn, blockpos) && !isAcrossOrAdjacentToBud(worldIn, pos);
	}

	public boolean placePedals(LevelAccessor world, BlockPos pos, boolean opened) {
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

	public static boolean isAcrossOrAdjacentToBud(LevelReader world, BlockPos pos) {
		Block block = EEBlocks.BOLLOOM_BUD.get();
		for (Direction directions : Direction.values()) {
			if (world.getBlockState(pos.relative(directions, 2)).getBlock() == block) {
				return true;
			}
		}

		BlockPos north = pos.relative(Direction.NORTH);
		BlockPos south = pos.relative(Direction.SOUTH);
		return world.getBlockState(north.east()).getBlock() == block || world.getBlockState(south.east()).getBlock() == block || world.getBlockState(north.west()).getBlock() == block || world.getBlockState(south.west()).getBlock() == block;
	}

	private boolean canPutDownPedals(LevelAccessor world, BlockPos pos) {
		for (BudSide sides : BudSide.values()) {
			BlockPos sidePos = sides.offsetPosition(pos);
			if (!world.getFluidState(sidePos).isEmpty() || !world.getBlockState(sidePos).getCollisionShape(world, sidePos).isEmpty()) {
				return false;
			}
		}
		return true;
	}

	private BlockState resetBud(LevelAccessor world, BlockPos pos) {
		if (world.getBlockEntity(pos) instanceof BolloomBudTileEntity) {
			((BolloomBudTileEntity) world.getBlockEntity(pos)).resetGrowing();
		}
		return this.defaultBlockState();
	}

	@Override
	public VoxelShape getShape(BlockState p_220053_1_, BlockGetter p_220053_2_, BlockPos p_220053_3_, CollisionContext p_220053_4_) {
		return p_220053_1_.getValue(OPENED) ? SHAPE_OPENED : SHAPE;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(OPENED);
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new BolloomBudTileEntity(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return createTickerHelper(type, EETileEntities.BOLLOOM_BUD.get(), BolloomBudTileEntity::tick);
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.ENTITYBLOCK_ANIMATED;
	}
}