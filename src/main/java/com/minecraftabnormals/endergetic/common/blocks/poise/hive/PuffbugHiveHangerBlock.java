package com.minecraftabnormals.endergetic.common.blocks.poise.hive;

import com.minecraftabnormals.endergetic.common.entities.puffbug.PuffBugEntity;
import com.minecraftabnormals.endergetic.core.registry.EEBlocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

public class PuffbugHiveHangerBlock extends Block {
	private static final VoxelShape SHAPE = Block.makeCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 16.0D, 14.0D);

	public PuffbugHiveHangerBlock(Properties properties) {
		super(properties);
	}

	@Override
	public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entity) {
		if (!(entity instanceof PuffBugEntity)) {
			entity.setMotionMultiplier(state, new Vector3d(0.25D, 0.05D, 0.25D));
		}
	}

	@Override
	public void onBlockHarvested(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		if (!world.isRemote) {
			BlockPos down = pos.down();
			BlockPos doubleDown = down.down();
			BlockState downState = world.getBlockState(down);
			Block block = downState.getBlock();
			if (block == EEBlocks.PUFFBUG_HIVE.get() && !world.getBlockState(doubleDown).isSolid() && world.getBlockState(doubleDown).getBlock() != EEBlocks.PUFFBUG_HIVE.get()) {
				ItemStack stack = player.getHeldItemMainhand();
				PuffBugHiveBlock.alertPuffBugs(world, down, EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, stack) == 0 ? player : null);
				block.harvestBlock(world, player, down, downState, world.getTileEntity(down), stack);
			}
		}
		super.onBlockHarvested(world, pos, state, player);
	}

	@Override
	public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player) {
		return new ItemStack(EEBlocks.PUFFBUG_HIVE.get());
	}

	public VoxelShape getShape(BlockState p_220053_1_, IBlockReader p_220053_2_, BlockPos p_220053_3_, ISelectionContext p_220053_4_) {
		return SHAPE;
	}

	@Override
	@SuppressWarnings("deprecation")
	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
		if (worldIn.getBlockState(currentPos.up()).isAir() || worldIn.getBlockState(currentPos.down()).getBlock() != EEBlocks.PUFFBUG_HIVE.get()) {
			return Blocks.AIR.getDefaultState();
		}
		return super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
	}
}