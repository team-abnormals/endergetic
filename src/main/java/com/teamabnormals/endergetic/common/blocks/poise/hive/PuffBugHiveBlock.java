package com.teamabnormals.endergetic.common.blocks.poise.hive;

import com.teamabnormals.endergetic.core.registry.EETileEntities;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.teamabnormals.endergetic.common.entities.puffbug.PuffBugEntity;
import com.teamabnormals.endergetic.common.tileentities.PuffBugHiveTileEntity;
import com.teamabnormals.endergetic.core.registry.EEBlocks;

import java.util.List;

import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;

public class PuffBugHiveBlock extends BaseEntityBlock {
	private static final VoxelShape HIVE_SHAPE = Shapes.or(Block.box(0.0D, 3.0D, 0.0D, 16.0D, 16.0D, 16.0D), Block.box(1.0D, 0.0D, 1.0D, 15.0D, 3.0D, 15.0D));

	public PuffBugHiveBlock(BlockBehaviour.Properties properties) {
		super(properties);
	}

	@Override
	public RenderShape getRenderShape(BlockState p_49232_) {
		return RenderShape.MODEL;
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		return HIVE_SHAPE;
	}

	@Override
	public void playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
		alertPuffBugs(world, pos, player);
		super.playerWillDestroy(world, pos, state, player);
	}

	@Override
	public void wasExploded(Level world, BlockPos pos, Explosion explosion) {
		alertPuffBugs(world, pos, explosion.getSourceMob());
		super.wasExploded(world, pos, explosion);
	}

	@Override
	public void onProjectileHit(Level world, BlockState state, BlockHitResult hit, Projectile projectile) {
		alertPuffBugs(world, hit.getBlockPos(), null);
	}

	@Override
	public void attack(BlockState state, Level world, BlockPos pos, Player player) {
		alertPuffBugs(world, pos, player);
	}

	@Override
	@Nonnull
	public BlockState updateShape(@Nonnull BlockState state, Direction facing, BlockState facingState, LevelAccessor world, BlockPos currentPos, BlockPos facingPos) {
		return !canSurvive(state, world, currentPos) ? alertPuffBugs(world, currentPos, null) : state;
	}

	@Nullable
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		BlockPos blockpos = context.getClickedPos();
		Level world = context.getLevel();
		for (Direction enumfacing : context.getNearestLookingDirections()) {
			if (enumfacing == Direction.UP) {
				BlockPos up = blockpos.above();
				if (world.isEmptyBlock(blockpos.below()) && world.getBlockState(up).isFaceSturdy(context.getLevel(), up, Direction.DOWN)) {
					AABB bb = new AABB(context.getClickedPos().below());
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
	public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
		BlockState down = world.getBlockState(pos.below());
		return (world.getBlockState(pos.above()).getBlock() instanceof PuffbugHiveHangerBlock) || down.canOcclude() || down.getBlock() instanceof PuffBugHiveBlock;
	}

	public static BlockState alertPuffBugs(LevelAccessor world, BlockPos pos, @Nullable LivingEntity breaker) {
		if (!world.isClientSide()) {
			BlockEntity tile = world.getBlockEntity(pos);
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

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new PuffBugHiveTileEntity(pos, state);
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return createTickerHelper(type, EETileEntities.PUFFBUG_HIVE.get(), PuffBugHiveTileEntity::tick);
	}
}
