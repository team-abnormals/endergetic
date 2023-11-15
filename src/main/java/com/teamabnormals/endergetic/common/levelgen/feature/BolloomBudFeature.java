package com.teamabnormals.endergetic.common.levelgen.feature;

import com.mojang.serialization.Codec;
import com.teamabnormals.blueprint.core.util.MathUtil;
import com.teamabnormals.endergetic.api.util.GenerationUtils;
import com.teamabnormals.endergetic.common.block.entity.BolloomBudTileEntity;
import com.teamabnormals.endergetic.common.block.entity.BolloomBudTileEntity.BudSide;
import com.teamabnormals.endergetic.common.block.poise.BolloomBudBlock;
import com.teamabnormals.endergetic.core.registry.EEBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

/**
 * @author - SmellyModder(Luke Tonon)
 */
public class BolloomBudFeature extends Feature<NoneFeatureConfiguration> {
	private static final BlockState BOLLOOM_BUD = EEBlocks.BOLLOOM_BUD.get().defaultBlockState();

	public BolloomBudFeature(Codec<NoneFeatureConfiguration> configFactoryIn) {
		super(configFactoryIn);
	}

	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
		WorldGenLevel world = context.level();
		BlockPos pos = context.origin();
		if (world.isEmptyBlock(pos) && world.isEmptyBlock(pos.above())) {
			RandomSource rand = context.random();
			if (rand.nextFloat() > 0.75) {
				if (isValidGround(world, pos)) {
					world.setBlock(pos, BOLLOOM_BUD, 2);
					return true;
				}
			} else {
				int maxHeight = calculateFruitMaxHeight(world, pos);
				if (isValidGround(world, pos) && canFitCross(world, pos) && GenerationUtils.isAreaAir(world, pos.getX() - 1, pos.getY() + 1, pos.getZ() - 1, pos.getX() + 1, pos.getY() + 2, pos.getZ() + 1) && maxHeight > 1) {
					world.setBlock(pos, BOLLOOM_BUD.setValue(BolloomBudBlock.OPENED, true), 2);
					BlockEntity te = world.getBlockEntity(pos);
					if (te instanceof BolloomBudTileEntity) {
						((BolloomBudTileEntity) te).startGrowing(rand, maxHeight, true);
					}
					return true;
				}
			}
		}
		return false;
	}

	private static boolean isValidGround(LevelAccessor world, BlockPos pos) {
		Block downBlock = world.getBlockState(pos.below()).getBlock();
		return downBlock == EEBlocks.POISMOSS.get() || downBlock == EEBlocks.EUMUS_POISMOSS.get() || downBlock == EEBlocks.EUMUS.get();
	}

	private static int calculateFruitMaxHeight(LevelAccessor world, BlockPos pos) {
		int[] maxHeights = new int[4];

		for (BudSide sides : BudSide.values()) {
			for (int y = 1; y < 7; y++) {
				if (world.isEmptyBlock(sides.offsetPosition(pos.above(y)))) {
					maxHeights[sides.id] = y;
				} else {
					break;
				}
			}
		}

		return MathUtil.getLowestValueInIntArray(maxHeights);
	}

	private static boolean canFitCross(LevelAccessor world, BlockPos pos) {
		for (BudSide sides : BudSide.values()) {
			if (!world.isEmptyBlock(sides.offsetPosition(pos))) {
				return false;
			}
		}
		return !BolloomBudBlock.isAcrossOrAdjacentToBud(world, pos);
	}
}