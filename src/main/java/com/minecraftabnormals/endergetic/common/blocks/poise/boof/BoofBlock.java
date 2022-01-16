package com.minecraftabnormals.endergetic.common.blocks.poise.boof;

import com.minecraftabnormals.endergetic.common.entities.BoofBlockEntity;
import com.minecraftabnormals.endergetic.common.tileentities.boof.BoofBlockTileEntity;
import com.minecraftabnormals.endergetic.core.registry.EEBlocks;
import com.minecraftabnormals.endergetic.core.registry.EESounds;
import net.minecraft.block.*;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.OptionalDispenseBehavior;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySpawnPlacementRegistry.PlacementType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tags.FluidTags;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class BoofBlock extends ContainerBlock {
	public static final BooleanProperty BOOFED = BooleanProperty.create("boofed");

	public BoofBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(BOOFED, false));
	}

	@Override
	public PathNodeType getAiPathNodeType(BlockState state, IBlockReader world, BlockPos pos, MobEntity entity) {
		return PathNodeType.DAMAGE_CACTUS;
	}

	@Override
	public boolean canCreatureSpawn(BlockState state, IBlockReader world, BlockPos pos, PlacementType type, EntityType<?> entityType) {
		return false;
	}

	public boolean isSignalSource(BlockState state) {
		return state.getValue(BOOFED);
	}

	public int getSignal(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side) {
		return 15;
	}

	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(BOOFED);
	}

	@Override
	public void fallOn(World worldIn, BlockPos pos, Entity entityIn, float fallDistance) {
		if (entityIn.isShiftKeyDown()) {
			super.fallOn(worldIn, pos, entityIn, fallDistance);
		} else {
			entityIn.causeFallDamage(fallDistance, 0.0F);
		}
	}

	@Override
	public void entityInside(BlockState state, World worldIn, BlockPos pos, Entity entity) {
		if (entity instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) entity;
			player.pushthrough = Float.MAX_VALUE;
			player.fallDistance = 0;
		}
	}

	public static void doBoof(World world, BlockPos pos) {
		if (!world.isClientSide) {
			BoofBlockEntity boofBlock = new BoofBlockEntity(world, pos);
			world.addFreshEntity(boofBlock);
			world.playSound(null, pos, EESounds.BOOF_BLOCK_INFLATE.get(), SoundCategory.NEUTRAL, 1.0F, 1.0F);
		}
		world.setBlockAndUpdate(pos, EEBlocks.BOOF_BLOCK.get().defaultBlockState().setValue(BOOFED, true));
	}

	@Override
	public TileEntity newBlockEntity(IBlockReader worldIn) {
		return new BoofBlockTileEntity();
	}

	@Override
	public BlockRenderType getRenderShape(BlockState state) {
		return BlockRenderType.MODEL;
	}

	public static class BoofDispenseBehavior extends OptionalDispenseBehavior {

		@Override
		protected ItemStack execute(IBlockSource source, ItemStack stack) {
			World world = source.getLevel();
			Direction facing = source.getBlockState().getValue(DispenserBlock.FACING);
			BlockPos pos = source.getPos().relative(facing);
			if (world.getBlockState(pos).getMaterial().isReplaceable()) {
				world.setBlockAndUpdate(pos, EEBlocks.BOOF_BLOCK_DISPENSED.get().defaultBlockState().setValue(DispensedBoofBlock.FACING, facing).setValue(DispensedBoofBlock.WATERLOGGED, world.getFluidState(pos).is(FluidTags.WATER)));
				world.playSound(null, pos, EESounds.BOOF_BLOCK_INFLATE.get(), SoundCategory.NEUTRAL, 0.85F, 0.9F + world.random.nextFloat() * 0.15F);
				this.setSuccess(true);
			}
			return stack;
		}

	}
}
