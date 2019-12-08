package endergeticexpansion.common.blocks.poise.hive;

import endergeticexpansion.api.util.GenerationUtils;
import endergeticexpansion.common.tileentities.TileEntityPuffBugHive;
import endergeticexpansion.core.registry.EEBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;
import java.util.List;

public class BlockPuffBugHive extends Block {
	public BlockPuffBugHive(Properties properties) {
		super(properties);
	}
	
	@Override
	public void harvestBlock(World worldIn, PlayerEntity player, BlockPos pos, BlockState state, TileEntity te, ItemStack stack) {
		if(hasHanger(worldIn, pos)) {
			worldIn.destroyBlock(pos.up(), false);
		}
		super.harvestBlock(worldIn, player, pos, state, te, stack);
	}
	
	@Override
	public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
		if(hasHanger(worldIn, pos) && player.isCreative()) {
			worldIn.destroyBlock(pos.up(), false);
		}
		super.onBlockHarvested(worldIn, pos, state, player);
	}
	
	@Nullable
	@SuppressWarnings("deprecation")
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		BlockPos blockpos = context.getPos();
		for(Direction enumfacing : context.getNearestLookingDirections()) {
			if(enumfacing == Direction.UP) {
				if(context.getWorld().getBlockState(blockpos.down()).isAir() && GenerationUtils.isProperBlock(context.getWorld().getBlockState(blockpos.up()), new Block[] { EEBlocks.POISE_CLUSTER }, true)) {
					AxisAlignedBB bb = new AxisAlignedBB(context.getPos().down());
					List<Entity> entities = context.getWorld().getEntitiesWithinAABB(Entity.class, bb);
					if(entities.size() > 0) {
						return null;
					}
					context.getWorld().setBlockState(blockpos.down(), getDefaultState());
					return EEBlocks.HIVE_HANGER.getDefaultState();
				} else {
					return this.getDefaultState();
				}
			} else {
				return this.getDefaultState();
			}
		}
		return null;
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
		if(worldIn.getBlockState(pos.down()).getMaterial().isReplaceable()) {
			return false;
		}
		return super.isValidPosition(state, worldIn, pos);
	}
	
	@Override
	public ToolType getHarvestTool(BlockState state) {
		return ToolType.PICKAXE;
	}
	
	@Override
	public boolean hasCustomBreakingProgress(BlockState state) {
		return true;
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}
	
	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new TileEntityPuffBugHive();
	}
	
	@Override
	public boolean isSolid(BlockState state) {
		return false;
	}
	
	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.ENTITYBLOCK_ANIMATED;
	}
	
	private static boolean hasHanger(World world, BlockPos pos)
	{
		return world.getBlockState(pos.up()).getBlock() instanceof BlockHiveHanger;
	}
}
