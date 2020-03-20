package endergeticexpansion.common.blocks.poise;

import javax.annotation.Nullable;

import endergeticexpansion.common.tileentities.TileEntityBolloomBud;
import endergeticexpansion.common.tileentities.TileEntityBolloomBud.BudSide;
import endergeticexpansion.core.registry.EEBlocks;
import endergeticexpansion.core.registry.other.EETags;
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

public class BlockBolloomBud extends Block {
	public static final BooleanProperty OPENED = BooleanProperty.create("opened");
	private static final VoxelShape INSIDE = makeCuboidShape(3.5D, 0.0D, 3.5D, 12.5D, 15.0D, 12.5D);
	protected static final VoxelShape SHAPE = VoxelShapes.combineAndSimplify(makeCuboidShape(1.0D, 0.0D, 1.0D, 15.0D, 15.0D, 15.0D), VoxelShapes.or(INSIDE), IBooleanFunction.ONLY_FIRST);
	protected static final VoxelShape SHAPE_OPENED = Block.makeCuboidShape(1.0D, 0.0D, 1.0D, 15.0D, 3.0D, 15.0D);

	public BlockBolloomBud(Properties properties) {
		super(properties);
		this.setDefaultState(this.stateContainer.getBaseState().with(OPENED, false));
	}
	
	@Override
	public VoxelShape getRenderShape(BlockState state, IBlockReader worldIn, BlockPos pos) {
		return Block.makeCuboidShape(-16.0D, -16.0D, -16.0D, 32.0D, 32.0D, 32.0D);
	}
	
	@Override
	public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
		super.onBlockHarvested(worldIn, pos, state, player);
	}
	
	protected boolean isValidGround(BlockState state, IBlockReader worldIn, BlockPos pos) {
		Block block = state.getBlock();
		return block == Blocks.END_STONE.getBlock() || block.isIn(EETags.Blocks.END_PLANTABLE) || block.isIn(EETags.Blocks.POISE_PLANTABLE);
	}
	
	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld world, BlockPos currentPos, BlockPos facingPos) {
		if(stateIn.isValidPosition(world, currentPos)) {
			boolean opened = stateIn.get(OPENED);
			return this.placePedals(world.getWorld(), currentPos, opened) && opened ? stateIn.with(OPENED, true) : this.resetBud(world, currentPos);
		}
		return Blocks.AIR.getDefaultState();
	}
	
	public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
		BlockPos blockpos = pos.down();
		return this.isValidGround(worldIn.getBlockState(blockpos), worldIn, blockpos) && !isAcrossOrAdjacentToBud(worldIn, pos);
	}
	
	public boolean placePedals(World world, BlockPos pos, boolean opened) {
		if(!world.getBlockState(pos).get(OPENED) && this.canPutDownPedals(world, pos)) {
			if(opened) {
				for(BudSide side : BudSide.values()) {
					BlockPos sidePos = side.offsetPosition(pos);
					if(world.getBlockState(sidePos).getCollisionShape(world, pos).isEmpty()) {
						world.destroyBlock(sidePos, true);
					}
				}
			}
			return true;
		} else if(opened) {
			return false;
		}
		return false;
	}
	
	public static boolean isAcrossOrAdjacentToBud(IWorldReader world, BlockPos pos) {
		Block block = EEBlocks.BOLLOOM_BUD.get();
		for(Direction directions : Direction.values()) {
			if(world.getBlockState(pos.offset(directions, 2)).getBlock() == block) {
				return true;
			}
		}
		
		BlockPos north = pos.offset(Direction.NORTH);
		BlockPos south = pos.offset(Direction.SOUTH);
		
		if(world.getBlockState(north.east()).getBlock() == block || world.getBlockState(south.east()).getBlock() == block || world.getBlockState(north.west()).getBlock() == block || world.getBlockState(south.west()).getBlock() == block) {
			return true;
		}
		return false;
	}
	
	private boolean canPutDownPedals(World world, BlockPos pos) {
		for(BudSide sides : BudSide.values()) {
			BlockPos sidePos = sides.offsetPosition(pos);
			if(!world.getFluidState(sidePos).isEmpty() || !world.getBlockState(sidePos).getCollisionShape(world, sidePos).isEmpty()) {
				return false;
			}
		}
		return true;
	}
	
	private BlockState resetBud(IWorld world, BlockPos pos) {
		if(world.getTileEntity(pos) instanceof TileEntityBolloomBud) {
			((TileEntityBolloomBud) world.getTileEntity(pos)).resetGrowing();
		}
		return this.getDefaultState();
	}
	
	@Override
	public VoxelShape getShape(BlockState p_220053_1_, IBlockReader p_220053_2_, BlockPos p_220053_3_, ISelectionContext p_220053_4_) {
		return p_220053_1_.get(OPENED) ? SHAPE_OPENED : SHAPE;
	}
	
	@OnlyIn(Dist.CLIENT)
	public boolean hasCustomBreakingProgress(BlockState state) {
		return true;
	}
	
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(OPENED);
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}
	
	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new TileEntityBolloomBud();
	}
	
	@Override
	public boolean isSolid(BlockState state) {
		return false;
	}
	
	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.ENTITYBLOCK_ANIMATED;
	}
}