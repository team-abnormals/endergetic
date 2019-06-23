package endergeticexpansion.common.blocks;

import java.util.Random;

import javax.annotation.Nullable;

import endergeticexpansion.core.registry.EEBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.dimension.Dimension;

public class BlockCorrockCrownStanding extends BlockCorrockCrown {
	public static final IntegerProperty ROTATION = BlockStateProperties.ROTATION_0_15;
	public static final BooleanProperty UPSIDE_DOWN = BooleanProperty.create("upside_down");
	
	public BlockCorrockCrownStanding(Properties properties) {
		super(properties);
		this.setDefaultState(this.stateContainer.getBaseState().with(ROTATION, Integer.valueOf(0)).with(WATERLOGGED, Boolean.valueOf(false)).with(UPSIDE_DOWN, false));
	}
	
	@Override
	public void tick(BlockState state, World worldIn, BlockPos pos, Random random) {
		if(!this.isInProperDimension(worldIn) && !this.isSubmerged(worldIn, pos)) {
			worldIn.setBlockState(pos, this.getCorrockBlockForDimension(worldIn.getDimension())
				.with(ROTATION, worldIn.getBlockState(pos).get(ROTATION))
				.with(UPSIDE_DOWN, worldIn.getBlockState(pos).get(UPSIDE_DOWN)));
		}
		
		if(worldIn.getBlockState(pos).get(UPSIDE_DOWN) && !worldIn.getBlockState(pos.up()).isSolid()) {
			worldIn.destroyBlock(pos, true);
		} else if(!worldIn.getBlockState(pos).get(UPSIDE_DOWN) && !worldIn.getBlockState(pos.down()).isSolid()) {
			worldIn.destroyBlock(pos, true);
		}
	}
	
	public BlockState getCorrockBlockForDimension(Dimension dimension) {
		switch(dimension.getType().getId()) {
			case 0:
			return EEBlocks.CORROCK_CROWN_OVERWORLD_STANDING.getDefaultState();
			case 1:
			return EEBlocks.CORROCK_CROWN_END_STANDING.getDefaultState();
			case -1:
			return EEBlocks.CORROCK_CROWN_NETHER_STANDING.getDefaultState();
		}
		return null;
	}
	
	public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
		return state.get(UPSIDE_DOWN) ? worldIn.getBlockState(pos.up()).getMaterial().isSolid() : worldIn.getBlockState(pos.down()).getMaterial().isSolid();
	}
	
	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
		if (!this.isInProperDimension(worldIn.getWorld())) {
			worldIn.getPendingBlockTicks().scheduleTick(currentPos, this, 60 + worldIn.getRandom().nextInt(40));
		}
		
		return this.isValidPosition(stateIn, worldIn, currentPos) ? stateIn : Blocks.AIR.getDefaultState();
	}

	@Nullable
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		IFluidState ifluidstate = context.getWorld().getFluidState(context.getPos());
		Direction direction = context.getFace();
		if (!this.isInProperDimension(context.getWorld())) {
			context.getWorld().getPendingBlockTicks().scheduleTick(context.getPos(), this, 60 + context.getWorld().getRandom().nextInt(40));
		}
		if(context.getWorld().getBlockState(context.getPos().down()).getBlock() instanceof BlockCorrockCrownStanding) {
			return null;
		}
		return direction == Direction.UP ? 
			this.getDefaultState().with(ROTATION, Integer.valueOf(MathHelper.floor((double)((180.0F + context.getPlacementYaw()) * 16.0F / 360.0F) + 0.5D) & 15)).with(WATERLOGGED, Boolean.valueOf(ifluidstate.getFluid() == Fluids.WATER))
			: this.getDefaultState().with(ROTATION, Integer.valueOf(MathHelper.floor((double)((180.0F + context.getPlacementYaw()) * 16.0F / 360.0F) + 0.5D) & 15)).with(UPSIDE_DOWN, true).with(WATERLOGGED, Boolean.valueOf(ifluidstate.getFluid() == Fluids.WATER));
	}
	
	public BlockState rotate(BlockState state, Rotation rot) {
		return state.with(ROTATION, Integer.valueOf(rot.rotate(state.get(ROTATION), 16)));
	}

	public BlockState mirror(BlockState state, Mirror mirrorIn) {
		return state.with(ROTATION, Integer.valueOf(mirrorIn.mirrorRotation(state.get(ROTATION), 16)));
	}
	
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(ROTATION, WATERLOGGED, UPSIDE_DOWN);
	}
}
