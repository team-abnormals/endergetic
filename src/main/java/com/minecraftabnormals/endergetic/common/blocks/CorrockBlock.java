package com.minecraftabnormals.endergetic.common.blocks;

import java.util.Map;
import java.util.OptionalLong;
import java.util.Random;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import com.google.common.collect.Maps;
import com.minecraftabnormals.endergetic.core.events.EntityEvents;
import com.minecraftabnormals.endergetic.core.registry.EEBlocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.biome.IBiomeMagnifier;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ToolType;

public class CorrockBlock extends Block {
	private static final Map<DimensionType, Supplier<Block>> CONVERSIONS = Util.make(Maps.newHashMap(), (conversions) -> {
		conversions.put(DimensionTypeAccessor.OVERWORLD, () -> EEBlocks.CORROCK_OVERWORLD_BLOCK.get());
		conversions.put(DimensionTypeAccessor.THE_NETHER, () -> EEBlocks.CORROCK_NETHER_BLOCK.get());
		conversions.put(DimensionTypeAccessor.THE_END, () -> EEBlocks.CORROCK_END_BLOCK.get());
	});
	public final boolean petrified;
	
	public CorrockBlock(Properties properties, boolean petrified) {
		super(properties);
		this.petrified = petrified;
	}
	
	@Override
	public ToolType getHarvestTool(BlockState state) {
		return ToolType.PICKAXE;
	}
	
	@Override
	public void tick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		if (!this.petrified && !this.isInProperDimension(world)) {
			world.setBlockState(pos, CONVERSIONS.getOrDefault(world.func_230315_m_(), EEBlocks.CORROCK_OVERWORLD_BLOCK).get().getDefaultState());
		}
	}
	
	@Override
	public SoundType getSoundType(BlockState state, IWorldReader world, BlockPos pos, Entity entity) {
		return SoundType.CORAL;
	}
	
	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
		if (!this.isInProperDimension(worldIn.getWorld())) {
			worldIn.getPendingBlockTicks().scheduleTick(currentPos, this, 60 + worldIn.getRandom().nextInt(40));
		}
		
		if (this.isSubmerged(worldIn, currentPos)) {
			return !this.petrified ? EntityEvents.convertCorrockBlock(stateIn) : stateIn;
		}
		
	    return stateIn;
	}
	
	@Nullable
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		if (!this.isInProperDimension(context.getWorld())) {
			context.getWorld().getPendingBlockTicks().scheduleTick(context.getPos(), this, 60 + context.getWorld().getRandom().nextInt(40));
		}
		
		return this.getDefaultState();
	}

	public boolean isInProperDimension(World world) {
		return !this.petrified && CONVERSIONS.getOrDefault(world.func_230315_m_(), EEBlocks.CORROCK_OVERWORLD_BLOCK).get() == this;
	}
	
	public boolean isSubmerged(IWorld world, BlockPos pos) {
		for (Direction offsets : Direction.values()) {
			FluidState fluidState = world.getFluidState(pos.offset(offsets));
			if (!fluidState.isEmpty() && fluidState.isTagged(FluidTags.WATER)) {
				return true;
			}
		}
		return false;
	}
	
	public static class DimensionTypeAccessor extends DimensionType {
		public static final DimensionType OVERWORLD = OVERWORLD_TYPE;
		public static final DimensionType THE_NETHER = NETHER_TYPE;
		public static final DimensionType THE_END = END_TYPE;
		
		private DimensionTypeAccessor(OptionalLong p_i241243_1_, boolean p_i241243_2_, boolean p_i241243_3_, boolean p_i241243_4_, boolean p_i241243_5_, boolean p_i241243_6_, boolean p_i241243_7_, boolean p_i241243_8_, boolean p_i241243_9_, boolean p_i241243_10_, boolean p_i241243_11_, int p_i241243_12_, IBiomeMagnifier p_i241243_13_, ResourceLocation p_i241243_14_, float p_i241243_15_) {
			super(p_i241243_1_, p_i241243_2_, p_i241243_3_, p_i241243_4_, p_i241243_5_, p_i241243_6_, p_i241243_7_, p_i241243_8_, p_i241243_9_, p_i241243_10_, p_i241243_11_, p_i241243_12_, p_i241243_13_, p_i241243_14_, p_i241243_15_);
		}
	}
}
