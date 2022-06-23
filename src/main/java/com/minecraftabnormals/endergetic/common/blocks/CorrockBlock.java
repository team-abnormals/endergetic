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
import net.minecraft.block.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.tags.FluidTags;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.world.*;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.common.ToolType;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.dimension.DimensionType;

public class CorrockBlock extends Block implements BonemealableBlock {
	private static final Map<DimensionType, Supplier<Block>> CONVERSIONS = Util.make(Maps.newHashMap(), (conversions) -> {
		conversions.put(DimensionTypeAccessor.OVERWORLD, EEBlocks.CORROCK_OVERWORLD_BLOCK);
		conversions.put(DimensionTypeAccessor.THE_NETHER, EEBlocks.CORROCK_NETHER_BLOCK);
		conversions.put(DimensionTypeAccessor.THE_END, EEBlocks.CORROCK_END_BLOCK);
	});
	private final Supplier<Block> speckledBlock;
	private final Supplier<Block> plantBlock;

	public CorrockBlock(Properties properties, Supplier<Block> speckledBlock, Supplier<Block> plantBlock, boolean petrified) {
		super(properties);
		this.speckledBlock = speckledBlock;
		this.plantBlock = plantBlock;
	}

	@Override
	public ToolType getHarvestTool(BlockState state) {
		return ToolType.PICKAXE;
	}

	@Override
	public void tick(BlockState state, ServerLevel world, BlockPos pos, Random random) {
		if (this.shouldConvert(world)) {
			world.setBlockAndUpdate(pos, CONVERSIONS.getOrDefault(world.dimensionType(), EEBlocks.CORROCK_OVERWORLD_BLOCK).get().defaultBlockState());
		}
	}

	@Override
	public SoundType getSoundType(BlockState state, LevelReader world, BlockPos pos, Entity entity) {
		return SoundType.CORAL_BLOCK;
	}

	public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
		if (this.shouldConvert(worldIn)) {
			worldIn.getBlockTicks().scheduleTick(currentPos, this, 60 + worldIn.getRandom().nextInt(40));
		}

		if (isSubmerged(worldIn, currentPos)) {
			return EntityEvents.convertCorrockBlock(stateIn);
		}

		return stateIn;
	}

	@Nullable
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		if (this.shouldConvert(context.getLevel())) {
			context.getLevel().getBlockTicks().scheduleTick(context.getClickedPos(), this, 60 + context.getLevel().getRandom().nextInt(40));
		}
		return this.defaultBlockState();
	}

	protected boolean shouldConvert(LevelAccessor world) {
		return CONVERSIONS.getOrDefault(world.dimensionType(), EEBlocks.CORROCK_OVERWORLD_BLOCK).get() != this;
	}

	public static boolean isSubmerged(LevelAccessor world, BlockPos pos) {
		for (Direction offsets : Direction.values()) {
			FluidState fluidState = world.getFluidState(pos.relative(offsets));
			if (!fluidState.isEmpty() && fluidState.is(FluidTags.WATER)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean canCreatureSpawn(BlockState state, BlockGetter world, BlockPos pos, SpawnPlacements.Type type, @Nullable EntityType<?> entityType) {
		return entityType == EEEntities.CHARGER_EETLE.get() || super.canCreatureSpawn(state, world, pos, type, entityType);
	}

	@Override
	public boolean isValidBonemealTarget(BlockGetter worldIn, BlockPos pos, BlockState state, boolean isClient) {
		return true;
	}

	@Override
	public boolean isBonemealSuccess(Level worldIn, Random rand, BlockPos pos, BlockState state) {
		return true;
	}

	@Override
	public void performBonemeal(ServerLevel world, Random rand, BlockPos pos, BlockState state) {
		int radius = 2;
		BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
		Block speckledBlock = this.speckledBlock.get();
		BlockState speckledState = speckledBlock.defaultBlockState();
		BlockState plantState = this.plantBlock.get().defaultBlockState();
		for (int x = -radius; x <= radius; x++) {
			for (int z = -radius; z <= radius; z++) {
				mutable.setWithOffset(pos, x, 0, z);
				Block block = this.findHighestSpreadableBlock(world, mutable, speckledBlock);
				boolean thisBlock = block == this;
				boolean isSpeckled = block == speckledBlock;
				if (block == Blocks.END_STONE || thisBlock || isSpeckled) {
					int distanceSq = x * x + z * z - rand.nextInt(2);
					if (distanceSq <= 1) {
						this.placeSpreadBlock(world, mutable, this.defaultBlockState(), plantState, rand, false);
					} else if (distanceSq <= 4) {
						boolean notSpeckled = isSpeckled || thisBlock;
						this.placeSpreadBlock(world, mutable, notSpeckled ? this.defaultBlockState() : speckledState, plantState, rand, !notSpeckled);
					}
				}
			}
		}
	}

	private void placeSpreadBlock(ServerLevel world, BlockPos pos, BlockState state, BlockState plantState, Random random, boolean speckled) {
		world.setBlockAndUpdate(pos, state);
		if (random.nextFloat() < (speckled ? 0.1F : 0.2F)) {
			BlockPos up = pos.above();
			if (world.isEmptyBlock(up)) {
				world.setBlockAndUpdate(up, plantState);
			}
		}
	}

	@Nullable
	private Block findHighestSpreadableBlock(ServerLevel world, BlockPos.MutableBlockPos mutable, Block speckledBlock) {
		int originY = mutable.getY();
		for (int y = 1; y > -2; y--) {
			mutable.setY(originY + y);
			Block block = world.getBlockState(mutable).getBlock();
			if (block == Blocks.END_STONE || block == this || block == speckledBlock) {
				return block;
			}
		}
		return null;
	}

	public static final class DimensionTypeAccessor extends DimensionType {
		public static final DimensionType OVERWORLD = DEFAULT_OVERWORLD;
		public static final DimensionType THE_NETHER = DEFAULT_NETHER;
		public static final DimensionType THE_END = DEFAULT_END;

		protected DimensionTypeAccessor(OptionalLong fixedTime, boolean hasSkyLight, boolean hasCeiling, boolean ultrawarm, boolean natural, double coordinateScale, boolean piglinSafe, boolean bedWorks, boolean respawnAnchorWorks, boolean hasRaids, int logicalHeight, ResourceLocation infiniburn, ResourceLocation effects, float ambientLight) {
			super(fixedTime, hasSkyLight, hasCeiling, ultrawarm, natural, coordinateScale, piglinSafe, bedWorks, respawnAnchorWorks, hasRaids, logicalHeight, infiniburn, effects, ambientLight);
		}
	}
}
