package endergeticexpansion.common.blocks.poise.hive;

import endergeticexpansion.common.entities.EntityPuffBug;
import endergeticexpansion.common.tileentities.TileEntityPuffBugHive;
import endergeticexpansion.core.registry.EEBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.*;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class BlockPuffBugHive extends Block {
	
	public BlockPuffBugHive(Properties properties) {
		super(properties);
	}
	
	@Override
	public void harvestBlock(World worldIn, PlayerEntity player, BlockPos pos, BlockState state, TileEntity te, ItemStack stack) {
		destroyBlock(worldIn, pos, player);
		super.harvestBlock(worldIn, player, pos, state, te, stack);
	}
	
	@Override
	public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
		destroyBlock(worldIn, pos, player);
		super.onBlockHarvested(worldIn, pos, state, player);
	}
	
	@Override
	public void onExplosionDestroy(World world, BlockPos pos, Explosion explosion) {
		destroyBlock(world, pos, explosion.getExplosivePlacedBy());
		super.onExplosionDestroy(world, pos, explosion);
	}
	
	@Override
	public void onProjectileCollision(World world, BlockState state, BlockRayTraceResult hit, Entity projectile) {
		TileEntity tileEntity = world.getTileEntity(hit.getPos());
		if(tileEntity instanceof TileEntityPuffBugHive) {
			((TileEntityPuffBugHive) tileEntity).alertPuffBugs(null);
		}
	}
	
	@Override
	@Nonnull
	public BlockState updatePostPlacement(@Nonnull BlockState state, Direction facing, BlockState facingState, IWorld world, BlockPos currentPos, BlockPos facingPos) {
		return !isValidPosition(state, world, currentPos) ? destroyBlock(world, currentPos, null) : state;
	}
	
	@Nullable
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		BlockPos blockpos = context.getPos();
		World world = context.getWorld();
		for(Direction enumfacing : context.getNearestLookingDirections()) {
			if(enumfacing == Direction.UP) {
				if(world.isAirBlock(blockpos.down()) && Block.hasSolidSide(world.getBlockState(blockpos.up()), world, blockpos, Direction.DOWN)) {
					AxisAlignedBB bb = new AxisAlignedBB(context.getPos().down());
					List<Entity> entities = context.getWorld().getEntitiesWithinAABB(Entity.class, bb);
					if(entities.size() > 0) {
						return null;
					}
					world.setBlockState(blockpos.down(), this.getDefaultState());
					return EEBlocks.HIVE_HANGER.get().getDefaultState();
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
		BlockState down = worldIn.getBlockState(pos.down());
		return (hasHanger(worldIn, pos) || down.isSolid() || down.getBlock() instanceof BlockPuffBugHive) && super.isValidPosition(state, worldIn, pos);
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
	
	private static boolean hasHanger(IWorldReader world, BlockPos pos) {
		return world.getBlockState(pos.up()).getBlock() instanceof BlockHiveHanger;
	}
	
	private static BlockState destroyBlock(IWorld world, BlockPos pos, @Nullable LivingEntity breaker) {
		if(hasHanger(world, pos)) {
			world.destroyBlock(pos.up(), false);
		}
		
		TileEntity tile = world.getTileEntity(pos);
		if(tile instanceof TileEntityPuffBugHive) {
			TileEntityPuffBugHive hive = (TileEntityPuffBugHive) tile;
			if(breaker != null && EntityPuffBug.CAN_ANGER.test(breaker)) {
				hive.alertPuffBugs(breaker);
			}
		}
		return Blocks.AIR.getDefaultState();
	}
}
