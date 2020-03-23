package endergeticexpansion.common.blocks;

import java.util.ArrayList;
import java.util.List;

import endergeticexpansion.common.tileentities.TileEntityCorrockCrown;
import endergeticexpansion.core.registry.EEBlocks;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ContainerBlock;
import net.minecraft.block.IBucketPickupHandler;
import net.minecraft.block.ILiquidContainer;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.dimension.EndDimension;
import net.minecraft.world.dimension.NetherDimension;
import net.minecraft.world.dimension.OverworldDimension;
import net.minecraft.world.storage.loot.LootContext.Builder;

public abstract class BlockCorrockCrown extends ContainerBlock implements IBucketPickupHandler, ILiquidContainer {
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	
	protected BlockCorrockCrown(Properties builder) {
		super(builder);
	}
	
	@Override
	public List<ItemStack> getDrops(BlockState p_220076_1_, Builder p_220076_2_) {
		ArrayList<ItemStack> dropList = new ArrayList<ItemStack>();
		dropList.add(new ItemStack(this));
		return dropList;
	}

	@SuppressWarnings("deprecation")
	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
		if (stateIn.get(WATERLOGGED)) {
			worldIn.getPendingFluidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickRate(worldIn));
		}
		if (!this.isInProperDimension(worldIn.getWorld())) {
			worldIn.getPendingBlockTicks().scheduleTick(currentPos, this, 60 + worldIn.getRandom().nextInt(40));
		}
		return super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
	}
	
	public boolean isInProperDimension(World world) {
		if(this.getDefaultState().getBlock() == EEBlocks.CORROCK_CROWN_OVERWORLD_STANDING.get() || this.getDefaultState().getBlock() == EEBlocks.CORROCK_CROWN_OVERWORLD_WALL.get()) {
			return (world.getDimension() instanceof OverworldDimension);
		}
		else if(this.getDefaultState().getBlock() == EEBlocks.CORROCK_CROWN_NETHER_STANDING.get() || this.getDefaultState().getBlock() == EEBlocks.CORROCK_CROWN_NETHER_WALL.get()) {
			return (world.getDimension() instanceof NetherDimension);
		}
		else if(this.getDefaultState().getBlock() == EEBlocks.CORROCK_CROWN_END_STANDING.get() || this.getDefaultState().getBlock() == EEBlocks.CORROCK_CROWN_END_WALL.get()) {
			return (world.getDimension() instanceof EndDimension);
		}
		return false;
	}
	
	public boolean isSubmerged(World worldIn, BlockPos pos) {
		if(worldIn.getBlockState(pos.up()).getBlock() == Blocks.WATER || worldIn.getBlockState(pos.down()).getBlock() == Blocks.WATER
			|| worldIn.getBlockState(pos.north()).getBlock() == Blocks.WATER || worldIn.getBlockState(pos.east()).getBlock() == Blocks.WATER
			|| worldIn.getBlockState(pos.south()).getBlock() == Blocks.WATER || worldIn.getBlockState(pos.west()).getBlock() == Blocks.WATER) {
			return true;
		}
		return false;
	}

	@Override
	public TileEntity createNewTileEntity(IBlockReader worldIn) {
		return new TileEntityCorrockCrown();
	}
	
	public Fluid pickupFluid(IWorld worldIn, BlockPos pos, BlockState state) {
		if (state.get(WATERLOGGED)) {
			worldIn.setBlockState(pos, state.with(WATERLOGGED, Boolean.valueOf(false)), 3);
			return Fluids.WATER;
		} else {
			return Fluids.EMPTY;
		}
	}

	@SuppressWarnings("deprecation")
	public IFluidState getFluidState(BlockState state) {
		return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
	}

	public boolean canContainFluid(IBlockReader worldIn, BlockPos pos, BlockState state, Fluid fluidIn) {
		return !state.get(WATERLOGGED) && fluidIn == Fluids.WATER;
	}

	public boolean receiveFluid(IWorld worldIn, BlockPos pos, BlockState state, IFluidState fluidStateIn) {
		if (!state.get(WATERLOGGED) && fluidStateIn.getFluid() == Fluids.WATER) {
			if (!worldIn.isRemote()) {
				worldIn.setBlockState(pos, state.with(WATERLOGGED, Boolean.valueOf(true)), 3);
	            worldIn.getPendingFluidTicks().scheduleTick(pos, fluidStateIn.getFluid(), fluidStateIn.getFluid().getTickRate(worldIn));
			}
			
	        return true;
		} else {
			return false;
		}
	}
	
	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.ENTITYBLOCK_ANIMATED;
	}
}