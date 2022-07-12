package com.teamabnormals.endergetic.common.blocks.poise.hive;

import com.teamabnormals.endergetic.common.entities.puffbug.PuffBugEntity;
import com.teamabnormals.endergetic.core.registry.EEBlocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;

public class PuffbugHiveHangerBlock extends Block {
	private static final VoxelShape SHAPE = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 16.0D, 14.0D);

	public PuffbugHiveHangerBlock(Properties properties) {
		super(properties);
	}

	@Override
	public void entityInside(BlockState state, Level worldIn, BlockPos pos, Entity entity) {
		if (!(entity instanceof PuffBugEntity)) {
			entity.makeStuckInBlock(state, new Vec3(0.25D, 0.05D, 0.25D));
		}
	}

	@Override
	public void playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
		if (!world.isClientSide) {
			BlockPos down = pos.below();
			BlockPos doubleDown = down.below();
			BlockState downState = world.getBlockState(down);
			Block block = downState.getBlock();
			if (block == EEBlocks.PUFFBUG_HIVE.get() && !world.getBlockState(doubleDown).canOcclude() && world.getBlockState(doubleDown).getBlock() != EEBlocks.PUFFBUG_HIVE.get()) {
				ItemStack stack = player.getMainHandItem();
				PuffBugHiveBlock.alertPuffBugs(world, down, EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, stack) == 0 ? player : null);
				block.playerDestroy(world, player, down, downState, world.getBlockEntity(down), stack);
			}
		}
		super.playerWillDestroy(world, pos, state, player);
	}

	@Override
	public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level, BlockPos pos, Player player) {
		return new ItemStack(EEBlocks.PUFFBUG_HIVE.get());
	}

	public VoxelShape getShape(BlockState p_220053_1_, BlockGetter p_220053_2_, BlockPos p_220053_3_, CollisionContext p_220053_4_) {
		return SHAPE;
	}

	@Override
	@SuppressWarnings("deprecation")
	public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
		if (worldIn.getBlockState(currentPos.above()).isAir() || worldIn.getBlockState(currentPos.below()).getBlock() != EEBlocks.PUFFBUG_HIVE.get()) {
			return Blocks.AIR.defaultBlockState();
		}
		return super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
	}
}