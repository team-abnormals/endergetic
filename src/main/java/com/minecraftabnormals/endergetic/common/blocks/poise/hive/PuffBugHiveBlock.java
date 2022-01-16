package com.minecraftabnormals.endergetic.common.blocks.poise.hive;

import com.minecraftabnormals.endergetic.common.entities.puffbug.PuffBugEntity;
import com.minecraftabnormals.endergetic.common.tileentities.PuffBugHiveTileEntity;
import com.minecraftabnormals.endergetic.core.registry.EEBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.*;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class PuffBugHiveBlock extends Block {
	private static final VoxelShape HIVE_SHAPE = VoxelShapes.or(Block.box(0.0D, 3.0D, 0.0D, 16.0D, 16.0D, 16.0D), Block.box(1.0D, 0.0D, 1.0D, 15.0D, 3.0D, 15.0D));

	public PuffBugHiveBlock(Properties properties) {
		super(properties);
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return HIVE_SHAPE;
	}

	@Override
	public void playerWillDestroy(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		alertPuffBugs(world, pos, player);
		super.playerWillDestroy(world, pos, state, player);
	}

	@Override
	public void wasExploded(World world, BlockPos pos, Explosion explosion) {
		alertPuffBugs(world, pos, explosion.getSourceMob());
		super.wasExploded(world, pos, explosion);
	}

	@Override
	public void onProjectileHit(World world, BlockState state, BlockRayTraceResult hit, ProjectileEntity projectile) {
		alertPuffBugs(world, hit.getBlockPos(), null);
	}

	@Override
	public void attack(BlockState state, World world, BlockPos pos, PlayerEntity player) {
		alertPuffBugs(world, pos, player);
	}

	@Override
	@Nonnull
	public BlockState updateShape(@Nonnull BlockState state, Direction facing, BlockState facingState, IWorld world, BlockPos currentPos, BlockPos facingPos) {
		return !canSurvive(state, world, currentPos) ? alertPuffBugs(world, currentPos, null) : state;
	}

	@Nullable
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		BlockPos blockpos = context.getClickedPos();
		World world = context.getLevel();
		for (Direction enumfacing : context.getNearestLookingDirections()) {
			if (enumfacing == Direction.UP) {
				BlockPos up = blockpos.above();
				if (world.isEmptyBlock(blockpos.below()) && world.getBlockState(up).isFaceSturdy(context.getLevel(), up, Direction.DOWN)) {
					AxisAlignedBB bb = new AxisAlignedBB(context.getClickedPos().below());
					List<Entity> entities = context.getLevel().getEntitiesOfClass(Entity.class, bb);
					if (entities.size() > 0) {
						return null;
					}
					world.setBlockAndUpdate(blockpos.below(), this.defaultBlockState());
					return EEBlocks.HIVE_HANGER.get().defaultBlockState();
				} else {
					return this.defaultBlockState();
				}
			} else {
				return this.defaultBlockState();
			}
		}
		return null;
	}

	@Override
	public boolean canSurvive(BlockState state, IWorldReader world, BlockPos pos) {
		BlockState down = world.getBlockState(pos.below());
		return (world.getBlockState(pos.above()).getBlock() instanceof PuffbugHiveHangerBlock) || down.canOcclude() || down.getBlock() instanceof PuffBugHiveBlock;
	}

	@Override
	public ToolType getHarvestTool(BlockState state) {
		return ToolType.PICKAXE;
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new PuffBugHiveTileEntity();
	}

	@Override
	public BlockRenderType getRenderShape(BlockState state) {
		return BlockRenderType.ENTITYBLOCK_ANIMATED;
	}

	public static BlockState alertPuffBugs(IWorld world, BlockPos pos, @Nullable LivingEntity breaker) {
		if (!world.isClientSide()) {
			TileEntity tile = world.getBlockEntity(pos);
			if (tile instanceof PuffBugHiveTileEntity) {
				PuffBugHiveTileEntity hive = (PuffBugHiveTileEntity) tile;
				if (breaker == null) {
					hive.alertPuffBugs(null);
				} else {
					if (PuffBugEntity.CAN_ANGER.test(breaker)) {
						hive.alertPuffBugs(breaker);
					}
				}
			}
		}
		return Blocks.AIR.defaultBlockState();
	}
}
