package com.teamabnormals.endergetic.common.levelgen.feature;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.teamabnormals.endergetic.common.entity.puffbug.PuffBug;
import com.teamabnormals.endergetic.core.registry.EEBlocks;
import com.teamabnormals.endergetic.core.registry.EEEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import java.util.List;

public class PuffBugHiveFeature extends Feature<NoneFeatureConfiguration> {

	public PuffBugHiveFeature(Codec<NoneFeatureConfiguration> configFactoryIn) {
		super(configFactoryIn);
	}

	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
		WorldGenLevel level = context.level();
		BlockPos pos = context.origin();
		BlockPos hivePos = pos.below();
		if (level.getBlockState(pos.above()).getBlock() == EEBlocks.POISE_STEM.get() || level.getBlockState(pos.above()).getBlock() == EEBlocks.GLOWING_POISE_STEM.get()) {
			if (level.getBlockState(pos).getMaterial().isReplaceable() && level.getBlockState(pos).getMaterial().isReplaceable()) {
				level.setBlock(pos, EEBlocks.HIVE_HANGER.get().defaultBlockState(), 2);
				level.setBlock(hivePos, EEBlocks.PUFFBUG_HIVE.get().defaultBlockState(), 2);
				spawnPuffBugs(level, hivePos, context.random());
				return true;
			}
		} else {
			if (level.getBlockState(pos.above()).getBlock() == EEBlocks.POISE_CLUSTER.get() && level.getMaxBuildHeight() > 90) {
				if (level.getBlockState(pos).getMaterial().isReplaceable() && level.getBlockState(pos).getMaterial().isReplaceable()) {
					level.setBlock(pos, EEBlocks.HIVE_HANGER.get().defaultBlockState(), 2);
					level.setBlock(hivePos, EEBlocks.PUFFBUG_HIVE.get().defaultBlockState(), 2);
					spawnPuffBugs(level, hivePos, context.random());
					return true;
				}
			}
		}
		return false;
	}

	private static void spawnPuffBugs(WorldGenLevel level, BlockPos pos, RandomSource rand) {
		if (!level.getLevel().getGameRules().getBoolean(GameRules.RULE_DOMOBSPAWNING)) return;
		int maxPuffBugs = rand.nextInt(4) + 2;

		List<Direction> openSides = getOpenSides(level, pos);
		for (Direction openSide : openSides) {
			BlockPos offset = pos.relative(openSide);
			PuffBug puffbug = EEEntityTypes.PUFF_BUG.get().create(level.getLevel());
			if (puffbug != null) {
				puffbug.moveTo(offset.getX() + 0.5F, offset.getY() + 0.5F, offset.getZ() + 0.5F, 0.0F, 0.0F);
				puffbug.finalizeSpawn(level, level.getCurrentDifficultyAt(pos), MobSpawnType.STRUCTURE, null, null);
				puffbug.setHivePos(pos);
				level.addFreshEntity(puffbug);
			}
			if (maxPuffBugs-- <= 0) break;
		}
	}

	private static List<Direction> getOpenSides(LevelAccessor level, BlockPos pos) {
		List<Direction> openDirections = Lists.newArrayList();
		for (Direction directions : Direction.values()) {
			if (directions != Direction.UP) {
				BlockPos offsetPos = pos.relative(directions);
				if (level.isEmptyBlock(offsetPos) && level.isEmptyBlock(offsetPos.above())) {
					openDirections.add(directions);
				}
			}
		}
		return openDirections;
	}

}
