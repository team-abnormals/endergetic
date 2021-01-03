package com.minecraftabnormals.endergetic.common.blocks;

import java.util.Map;
import java.util.OptionalLong;
import java.util.Random;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import com.google.common.collect.Maps;
import com.minecraftabnormals.endergetic.core.events.EntityEvents;
import com.minecraftabnormals.endergetic.core.registry.EEBlocks;

import com.minecraftabnormals.endergetic.core.registry.EEEntities;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ToolType;

public class CorrockBlock extends Block {
	private static final Map<DimensionType, Supplier<Block>> CONVERSIONS = Util.make(Maps.newHashMap(), (conversions) -> {
		conversions.put(DimensionTypeAccessor.OVERWORLD, EEBlocks.CORROCK_OVERWORLD_BLOCK);
		conversions.put(DimensionTypeAccessor.THE_NETHER, EEBlocks.CORROCK_NETHER_BLOCK);
		conversions.put(DimensionTypeAccessor.THE_END, EEBlocks.CORROCK_END_BLOCK);
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
		if (this.shouldConvert(world)) {
			world.setBlockState(pos, CONVERSIONS.getOrDefault(world.getDimensionType(), EEBlocks.CORROCK_OVERWORLD_BLOCK).get().getDefaultState());
		}
	}

	@Override
	public SoundType getSoundType(BlockState state, IWorldReader world, BlockPos pos, Entity entity) {
		return SoundType.CORAL;
	}

	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
		if (this.shouldConvert(worldIn)) {
			worldIn.getPendingBlockTicks().scheduleTick(currentPos, this, 60 + worldIn.getRandom().nextInt(40));
		}

		if (this.isSubmerged(worldIn, currentPos)) {
			return !this.petrified ? EntityEvents.convertCorrockBlock(stateIn) : stateIn;
		}

		return stateIn;
	}

	@Nullable
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		if (this.shouldConvert(context.getWorld())) {
			context.getWorld().getPendingBlockTicks().scheduleTick(context.getPos(), this, 60 + context.getWorld().getRandom().nextInt(40));
		}
		return this.getDefaultState();
	}

	protected boolean shouldConvert(IWorld world) {
		return !this.petrified && CONVERSIONS.getOrDefault(world.getDimensionType(), EEBlocks.CORROCK_OVERWORLD_BLOCK).get() != this;
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

	@Override
	public boolean canCreatureSpawn(BlockState state, IBlockReader world, BlockPos pos, EntitySpawnPlacementRegistry.PlacementType type, @Nullable EntityType<?> entityType) {
		return entityType == EEEntities.CHARGER_EETLE.get() || super.canCreatureSpawn(state, world, pos, type, entityType);
	}

	public static class DimensionTypeAccessor extends DimensionType {
		public static final DimensionType OVERWORLD = OVERWORLD_TYPE;
		public static final DimensionType THE_NETHER = NETHER_TYPE;
		public static final DimensionType THE_END = END_TYPE;

		protected DimensionTypeAccessor(OptionalLong fixedTime, boolean hasSkyLight, boolean hasCeiling, boolean ultrawarm, boolean natural, double coordinateScale, boolean piglinSafe, boolean bedWorks, boolean respawnAnchorWorks, boolean hasRaids, int logicalHeight, ResourceLocation infiniburn, ResourceLocation effects, float ambientLight) {
			super(fixedTime, hasSkyLight, hasCeiling, ultrawarm, natural, coordinateScale, piglinSafe, bedWorks, respawnAnchorWorks, hasRaids, logicalHeight, infiniburn, effects, ambientLight);
		}
	}
}
