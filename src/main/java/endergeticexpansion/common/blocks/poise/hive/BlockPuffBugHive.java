package endergeticexpansion.common.blocks.poise.hive;

import java.util.List;

import javax.annotation.Nullable;

import endergeticexpansion.common.tileentities.TileEntityPuffBugHive;
import endergeticexpansion.core.registry.EEBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

public class BlockPuffBugHive extends Block {
	public static final BooleanProperty HAS_HANGER = BooleanProperty.create("hanger");

	public BlockPuffBugHive(Properties properties) {
		super(properties);
		this.setDefaultState(this.stateContainer.getBaseState().with(HAS_HANGER, false));
	}
	
	@Override
	public void harvestBlock(World worldIn, PlayerEntity player, BlockPos pos, BlockState state, TileEntity te, ItemStack stack) {
		int i = EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, stack);
		if(i > 0) {
			spawnAsEntity(worldIn, pos, new ItemStack(this));
		}
		if(state.get(HAS_HANGER)) {
			worldIn.destroyBlock(pos.up(), false);
		}
		super.harvestBlock(worldIn, player, pos, state, te, stack);
	}
	
	@Override
	public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
		if(player.isCreative()) {
			if(state.get(HAS_HANGER)) {
				worldIn.destroyBlock(pos.up(), false);
			}
		}
		super.onBlockHarvested(worldIn, pos, state, player);
	}
	
	@Override
	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
		if(stateIn.get(HAS_HANGER)) {
			if(worldIn.getWorld().getBlockState(currentPos.up()) != EEBlocks.HIVE_HANGER.getDefaultState()) {
				return Blocks.AIR.getDefaultState();
			} else {
				return stateIn;
			}
		}
		if(!this.isValidPosition(stateIn, worldIn, currentPos)) {
			return Blocks.AIR.getDefaultState();
		}
		return stateIn;
	}
	
	@Nullable
	@SuppressWarnings("deprecation")
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		BlockPos blockpos = context.getPos();
		for(Direction enumfacing : context.getNearestLookingDirections()) {
			if(enumfacing == Direction.UP) {
				if(context.getWorld().getBlockState(blockpos.down()).isAir() && context.getWorld().getBlockState(blockpos.up()).isSolid()) {
					AxisAlignedBB bb = new AxisAlignedBB(context.getPos().down());
					List<Entity> entities = context.getWorld().getEntitiesWithinAABB(Entity.class, bb);
					if(entities.size() > 0) {
						return null;
					}
					
					context.getWorld().setBlockState(blockpos.down(), getDefaultState().with(HAS_HANGER, true));
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
	
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(HAS_HANGER);
	}
	
}
