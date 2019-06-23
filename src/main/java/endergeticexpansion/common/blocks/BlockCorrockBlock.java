package endergeticexpansion.common.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import endergeticexpansion.core.registry.EEBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.entity.Entity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.EndDimension;
import net.minecraft.world.dimension.NetherDimension;
import net.minecraft.world.dimension.OverworldDimension;
import net.minecraft.world.storage.loot.LootContext.Builder;

public class BlockCorrockBlock extends Block {

	public BlockCorrockBlock(Properties properties) {
		super(properties);
	}
	
	@Override
	public List<ItemStack> getDrops(BlockState p_220076_1_, Builder p_220076_2_) {
		ArrayList<ItemStack> dropList = new ArrayList<ItemStack>();
		dropList.add(new ItemStack(this));
		return dropList;
	}
	
	@Override
	public void tick(BlockState state, World worldIn, BlockPos pos, Random random) {
		if(!this.isInProperDimension(worldIn) && !this.isSubmerged(worldIn, pos)) {
			worldIn.setBlockState(pos, this.getCorrockBlockForDimension(worldIn.getDimension()));
		}
	}
	
	@Override
	public SoundType getSoundType(BlockState state, IWorldReader world, BlockPos pos, Entity entity) {
		return SoundType.CORAL;
	}
	
	@SuppressWarnings("deprecation")
	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
		if (!this.isInProperDimension(worldIn.getWorld())) {
			worldIn.getPendingBlockTicks().scheduleTick(currentPos, this, 60 + worldIn.getRandom().nextInt(40));
		}
		
	    return super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
	}
	
	@Nullable
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		if (!this.isInProperDimension(context.getWorld())) {
			context.getWorld().getPendingBlockTicks().scheduleTick(context.getPos(), this, 60 + context.getWorld().getRandom().nextInt(40));
		}
		
		return this.getDefaultState();
	}

	public boolean isInProperDimension(World world) {
		if(this.getDefaultState().getBlock() == EEBlocks.CORROCK_BLOCK_OVERWORLD) {
			return (world.getDimension() instanceof OverworldDimension);
		}
		else if(this.getDefaultState().getBlock() == EEBlocks.CORROCK_BLOCK_NETHER) {
			return (world.getDimension() instanceof NetherDimension);
		}
		else if(this.getDefaultState().getBlock() == EEBlocks.CORROCK_BLOCK_END) {
			return (world.getDimension() instanceof EndDimension);
		}
		return false;
	}
	
	public BlockState getCorrockBlockForDimension(Dimension dimension) {
		switch(dimension.getType().getId()) {
			case 0:
			return EEBlocks.CORROCK_BLOCK_OVERWORLD.getDefaultState();
			case 1:
			return EEBlocks.CORROCK_BLOCK_END.getDefaultState();
			case -1:
			return EEBlocks.CORROCK_BLOCK_NETHER.getDefaultState();
		}
		return null;
	}
	
	public boolean isSubmerged(World worldIn, BlockPos pos) {
		if(worldIn.getBlockState(pos.up()).getBlock() == Blocks.WATER || worldIn.getBlockState(pos.down()).getBlock() == Blocks.WATER
			|| worldIn.getBlockState(pos.north()).getBlock() == Blocks.WATER || worldIn.getBlockState(pos.east()).getBlock() == Blocks.WATER
			|| worldIn.getBlockState(pos.south()).getBlock() == Blocks.WATER || worldIn.getBlockState(pos.west()).getBlock() == Blocks.WATER) {
			return true;
		}
		return false;
	}
}
