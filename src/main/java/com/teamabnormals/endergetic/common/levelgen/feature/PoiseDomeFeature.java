package com.teamabnormals.endergetic.common.levelgen.feature;

import com.mojang.serialization.Codec;
import com.teamabnormals.endergetic.api.util.GenerationUtils;
import com.teamabnormals.endergetic.core.registry.EEBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelSimulatedRW;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class PoiseDomeFeature extends Feature<NoneFeatureConfiguration> {

	public PoiseDomeFeature(Codec<NoneFeatureConfiguration> configFactory) {
		super(configFactory);
	}

	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
		WorldGenLevel world = context.level();
		BlockPos pos = context.origin();
		RandomSource rand = context.random();
		if (world.getBlockState(pos.below()).getBlock() == Blocks.END_STONE && isViableDomeArea(world, pos) && this.isGroundViable(world, pos.below(3), rand)) {
			this.buildDomeBase(world, pos, rand);
			this.buildDome(world, pos, rand);
			this.buildGround(world, pos, rand);
		}
		return false;
	}

	private void buildDomeBase(LevelAccessor world, BlockPos origin, RandomSource rand) {
		for (int x = origin.getX() - 13; x <= origin.getX() + 13; x++) {
			for (int z = origin.getZ() - 2; z <= origin.getZ() + 2; z++) {
				if (x == origin.getX() - 13 || x == origin.getX() + 13) {
					this.setPoiseLog(world, new BlockPos(x, origin.getY(), z), rand);
				} else {
					this.setPoismoss(world, new BlockPos(x, origin.getY(), z));
				}
			}
		}
		for (int x = origin.getX() - 12; x <= origin.getX() + 12; x++) {
			for (int z = origin.getZ() - 5; z <= origin.getZ() + 5; z++) {
				if (x == origin.getX() - 12 || x == origin.getX() + 12) {
					this.setPoiseLog(world, new BlockPos(x, origin.getY(), z), rand);
				} else {
					this.setPoismoss(world, new BlockPos(x, origin.getY(), z));
				}
			}
		}
		for (int x = origin.getX() - 11; x <= origin.getX() + 11; x++) {
			for (int z = origin.getZ() - 7; z <= origin.getZ() + 7; z++) {
				if (x == origin.getX() - 11 || x == origin.getX() + 11) {
					this.setPoiseLog(world, new BlockPos(x, origin.getY(), z), rand);
				} else {
					this.setPoismoss(world, new BlockPos(x, origin.getY(), z));
				}
			}
		}
		for (int x = origin.getX() - 10; x <= origin.getX() + 10; x++) {
			for (int z = origin.getZ() - 8; z <= origin.getZ() + 8; z++) {
				if (x == origin.getX() - 10 || x == origin.getX() + 10) {
					this.setPoiseLog(world, new BlockPos(x, origin.getY(), z), rand);
				} else {
					this.setPoismoss(world, new BlockPos(x, origin.getY(), z));
				}
			}
		}
		for (int x = origin.getX() - 9; x <= origin.getX() + 9; x++) {
			for (int z = origin.getZ() - 9; z <= origin.getZ() + 9; z++) {
				if (x == origin.getX() - 9 || x == origin.getX() + 9) {
					this.setPoiseLog(world, new BlockPos(x, origin.getY(), z), rand);
				} else {
					this.setPoismoss(world, new BlockPos(x, origin.getY(), z));
				}
			}
		}
		for (int x = origin.getX() - 8; x <= origin.getX() + 8; x++) {
			for (int z = origin.getZ() - 10; z <= origin.getZ() + 10; z++) {
				if (x == origin.getX() - 8 || x == origin.getX() + 8) {
					this.setPoiseLog(world, new BlockPos(x, origin.getY(), z), rand);
				} else {
					this.setPoismoss(world, new BlockPos(x, origin.getY(), z));
				}
			}
		}
		for (int x = origin.getX() - 7; x <= origin.getX() + 7; x++) {
			for (int z = origin.getZ() - 11; z <= origin.getZ() + 11; z++) {
				if (x == origin.getX() - 7 || x == origin.getX() + 7 || x == origin.getX() - 6 || x == origin.getX() + 6) {
					this.setPoiseLog(world, new BlockPos(x, origin.getY(), z), rand);
				} else {
					this.setPoismoss(world, new BlockPos(x, origin.getY(), z));
				}
			}
		}
		for (int x = origin.getX() - 5; x <= origin.getX() + 5; x++) {
			for (int z = origin.getZ() - 12; z <= origin.getZ() + 12; z++) {
				if (x <= origin.getX() - 3 || x >= origin.getX() + 3) {
					this.setPoiseLog(world, new BlockPos(x, origin.getY(), z), rand);
				} else {
					this.setPoismoss(world, new BlockPos(x, origin.getY(), z));
				}
			}
		}
		for (int x = 0; x <= 4; x++) {
			this.setPoiseLog(world, origin.north(13).east(2).west(x), rand);
			this.setPoiseLog(world, origin.south(13).east(2).west(x), rand);
		}

		//Doors
		for (int y = 1; y <= 3; y++) {
			this.setPoiseLog(world, origin.north(12).west(3).above(y), rand);
		}
		this.setPoiseLog(world, origin.north(12).west(2).above(4), rand);
		for (int y = 1; y <= 3; y++) {
			this.setPoiseLog(world, origin.north(12).east(3).above(y), rand);
		}
		this.setPoiseLog(world, origin.north(12).east(2).above(4), rand);

		this.setPoiseLog(world, origin.north(12).west().above(5), rand);
		this.setPoiseLog(world, origin.north(12).above(5), rand);
		this.setPoiseLog(world, origin.north(12).east().above(5), rand);
		if (rand.nextFloat() <= 0.25F) {
			if (rand.nextInt(3) == 0) {
				this.setPoiseLog(world, origin.north(12).above(6), rand);
			} else {
				if (rand.nextInt(2) == 0) {
					this.setPoiseLog(world, origin.north(12).west().above(6), rand);
				} else {
					this.setPoiseLog(world, origin.north(12).east().above(6), rand);
				}
			}
		}

		for (int y = 1; y <= 3; y++) {
			this.setPoiseLog(world, origin.east(12).north(3).above(y), rand);
		}
		this.setPoiseLog(world, origin.east(12).north(2).above(4), rand);
		for (int y = 1; y <= 3; y++) {
			this.setPoiseLog(world, origin.east(12).south(3).above(y), rand);
		}
		this.setPoiseLog(world, origin.east(12).south(2).above(4), rand);

		this.setPoiseLog(world, origin.east(12).north().above(5), rand);
		this.setPoiseLog(world, origin.east(12).above(5), rand);
		this.setPoiseLog(world, origin.east(12).south().above(5), rand);
		if (rand.nextFloat() <= 0.25F) {
			if (rand.nextInt(3) == 0) {
				this.setPoiseLog(world, origin.east(12).above(6), rand);
			} else {
				if (rand.nextInt(2) == 0) {
					this.setPoiseLog(world, origin.east(12).north().above(6), rand);
				} else {
					this.setPoiseLog(world, origin.east(12).south().above(6), rand);
				}
			}
		}

		for (int y = 1; y <= 3; y++) {
			this.setPoiseLog(world, origin.south(12).east(3).above(y), rand);
		}
		this.setPoiseLog(world, origin.south(12).east(2).above(4), rand);
		for (int y = 1; y <= 3; y++) {
			this.setPoiseLog(world, origin.south(12).west(3).above(y), rand);
		}
		this.setPoiseLog(world, origin.south(12).west(2).above(4), rand);

		this.setPoiseLog(world, origin.south(12).east().above(5), rand);
		this.setPoiseLog(world, origin.south(12).above(5), rand);
		this.setPoiseLog(world, origin.south(12).west().above(5), rand);
		if (rand.nextFloat() <= 0.25F) {
			if (rand.nextInt(3) == 0) {
				this.setPoiseLog(world, origin.south(12).above(6), rand);
			} else {
				if (rand.nextInt(2) == 0) {
					this.setPoiseLog(world, origin.south(12).east().above(6), rand);
				} else {
					this.setPoiseLog(world, origin.south(12).west().above(6), rand);
				}
			}
		}

		for (int y = 1; y <= 3; y++) {
			this.setPoiseLog(world, origin.west(12).south(3).above(y), rand);
		}
		this.setPoiseLog(world, origin.west(12).south(2).above(4), rand);
		for (int y = 1; y <= 3; y++) {
			this.setPoiseLog(world, origin.west(12).north(3).above(y), rand);
		}
		this.setPoiseLog(world, origin.west(12).north(2).above(4), rand);

		this.setPoiseLog(world, origin.west(12).south().above(5), rand);
		this.setPoiseLog(world, origin.west(12).above(5), rand);
		this.setPoiseLog(world, origin.west(12).north().above(5), rand);
		if (rand.nextFloat() <= 0.25F) {
			if (rand.nextInt(3) == 0) {
				this.setPoiseLog(world, origin.west(12).above(6), rand);
			} else {
				if (rand.nextInt(2) == 0) {
					this.setPoiseLog(world, origin.west(12).south().above(6), rand);
				} else {
					this.setPoiseLog(world, origin.west(12).north().above(6), rand);
				}
			}
		}
		this.buildPoismossCircle(world, world, rand, origin);
	}

	private void buildDome(LevelAccessor world, BlockPos origin, RandomSource rand) {
		/*
		 * North
		 */
		{
			GenerationUtils.fillAreaWithBlockCube(world, origin.above().north(12).west(5).getX(), origin.above().north(12).west(5).getY(), origin.above().north(12).west(5).getZ(), origin.above(4).north(12).west(3).getX(), origin.above(4).north(12).west(3).getY(), origin.above(4).north(12).west(3).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above().north(12).east(3).getX(), origin.above().north(12).east(3).getY(), origin.above().north(12).east(3).getZ(), origin.above(4).north(12).east(5).getX(), origin.above(4).north(12).east(5).getY(), origin.above(4).north(12).east(5).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(5).north(12).west(4).getX(), origin.above(5).north(12).west(4).getY(), origin.above(5).north(12).west(4).getZ(), origin.above(6).north(12).east(4).getX(), origin.above(6).north(12).east(4).getY(), origin.above(6).north(12).east(4).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(7).north(12).west(2).getX(), origin.above(7).north(12).west(2).getY(), origin.above(7).north(12).west(2).getZ(), origin.above(7).north(12).east(2).getX(), origin.above(7).north(12).east(2).getY(), origin.above(7).north(12).east(2).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
		}

		{
			GenerationUtils.fillAreaWithBlockCube(world, origin.above().north(11).west(7).getX(), origin.above().north(11).west(7).getY(), origin.above().north(11).west(7).getZ(), origin.above(5).north(11).west(6).getX(), origin.above(5).north(11).west(6).getY(), origin.above(5).north(11).west(6).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(5).north(11).west(6).getX(), origin.above(5).north(11).west(6).getY(), origin.above(5).north(11).west(6).getZ(), origin.above(7).north(11).west(5).getX(), origin.above(7).north(11).west(5).getY(), origin.above(7).north(11).west(5).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(7).north(11).west(5).getX(), origin.above(7).north(11).west(5).getY(), origin.above(7).north(11).west(5).getZ(), origin.above(8).north(11).west(3).getX(), origin.above(8).north(11).west(3).getY(), origin.above(8).north(11).west(3).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());

			GenerationUtils.fillAreaWithBlockCube(world, origin.above().north(11).east(6).getX(), origin.above().north(11).east(6).getY(), origin.above().north(11).east(6).getZ(), origin.above(5).north(11).east(7).getX(), origin.above(5).north(11).east(7).getY(), origin.above(5).north(11).east(7).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(5).north(11).east(5).getX(), origin.above(5).north(11).east(5).getY(), origin.above(5).north(11).east(5).getZ(), origin.above(7).north(11).east(6).getX(), origin.above(7).north(11).east(6).getY(), origin.above(7).north(11).east(6).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(7).north(11).east(3).getX(), origin.above(7).north(11).east(3).getY(), origin.above(7).north(11).east(3).getZ(), origin.above(8).north(11).east(5).getX(), origin.above(8).north(11).east(5).getY(), origin.above(8).north(11).east(5).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());

			GenerationUtils.fillAreaWithBlockCube(world, origin.above(8).north(11).west(3).getX(), origin.above(8).north(11).west(3).getY(), origin.above(8).north(11).west(3).getZ(), origin.above(9).north(11).east(3).getX(), origin.above(9).north(11).east(3).getY(), origin.above(9).north(11).east(3).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(10).north(11).west().getX(), origin.above(10).north(11).west().getY(), origin.above(10).north(11).west().getZ(), origin.above(11).north(10).east().getX(), origin.above(10).north(11).east().getY(), origin.above(10).north(11).east().getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
		}

		{
			GenerationUtils.fillAreaWithBlockCube(world, origin.above().north(10).west(8).getX(), origin.above().north(10).west(8).getY(), origin.above().north(10).west(8).getZ(), origin.above(6).north(10).west(8).getX(), origin.above(6).north(10).west(8).getY(), origin.above(6).north(10).west(8).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(6).north(10).west(7).getX(), origin.above(6).north(10).west(7).getY(), origin.above(6).north(10).west(7).getZ(), origin.above(8).north(10).west(7).getX(), origin.above(8).north(10).west(7).getY(), origin.above(8).north(10).west(7).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(8).north(10).west(6).getX(), origin.above(8).north(10).west(6).getY(), origin.above(8).north(10).west(6).getZ(), origin.above(9).north(10).west(6).getX(), origin.above(9).north(10).west(6).getY(), origin.above(9).north(10).west(6).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(9).north(10).west(6).getX(), origin.above(9).north(10).west(6).getY(), origin.above(9).north(10).west(6).getZ(), origin.above(9).north(10).west(4).getX(), origin.above(9).north(10).west(4).getY(), origin.above(9).north(10).west(4).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(10).north(10).west(4).getX(), origin.above(10).north(10).west(4).getY(), origin.above(10).north(10).west(4).getZ(), origin.above(10).north(10).west(2).getX(), origin.above(10).north(10).west(2).getY(), origin.above(10).north(10).west(2).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());

			GenerationUtils.fillAreaWithBlockCube(world, origin.above().north(10).east(8).getX(), origin.above().north(10).east(8).getY(), origin.above().north(10).east(8).getZ(), origin.above(6).north(10).east(8).getX(), origin.above(6).north(10).east(8).getY(), origin.above(6).north(10).east(8).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(6).north(10).east(7).getX(), origin.above(6).north(10).east(7).getY(), origin.above(6).north(10).east(7).getZ(), origin.above(8).north(10).east(7).getX(), origin.above(8).north(10).east(7).getY(), origin.above(8).north(10).east(7).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(8).north(10).east(6).getX(), origin.above(8).north(10).east(6).getY(), origin.above(8).north(10).east(6).getZ(), origin.above(9).north(10).east(6).getX(), origin.above(9).north(10).east(6).getY(), origin.above(9).north(10).east(6).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(9).north(10).east(4).getX(), origin.above(9).north(10).east(4).getY(), origin.above(9).north(10).east(4).getZ(), origin.above(9).north(10).east(6).getX(), origin.above(9).north(10).east(6).getY(), origin.above(9).north(10).east(6).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(10).north(10).east(2).getX(), origin.above(10).north(10).east(2).getY(), origin.above(10).north(10).east(2).getZ(), origin.above(10).north(10).east(4).getX(), origin.above(10).north(10).east(4).getY(), origin.above(10).north(10).east(4).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());

			GenerationUtils.fillAreaWithBlockCube(world, origin.above(11).north(10).west(2).getX(), origin.above(11).north(10).west(2).getY(), origin.above(11).north(10).west(2).getZ(), origin.above(11).north(10).east(2).getX(), origin.above(11).north(10).east(2).getY(), origin.above(11).north(10).east(2).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
		}

		{
			GenerationUtils.fillAreaWithBlockCube(world, origin.above().north(9).west(9).getX(), origin.above().north(9).west(9).getY(), origin.above().north(9).west(9).getZ(), origin.above(6).north(9).west(9).getX(), origin.above(6).north(9).west(9).getY(), origin.above(6).north(9).west(9).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(7).north(9).west(8).getX(), origin.above(7).north(9).west(8).getY(), origin.above(7).north(9).west(8).getZ(), origin.above(8).north(9).west(8).getX(), origin.above(8).north(9).west(8).getY(), origin.above(8).north(9).west(8).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(9).north(9).west(7).getX(), origin.above(9).north(9).west(7).getY(), origin.above(9).north(9).west(7).getZ(), origin.above(10).north(9).west(7).getX(), origin.above(10).north(9).west(7).getY(), origin.above(10).north(9).west(7).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(10).north(9).west(7).getX(), origin.above(10).north(9).west(7).getY(), origin.above(10).north(9).west(7).getZ(), origin.above(10).north(9).west(5).getX(), origin.above(10).north(9).west(5).getY(), origin.above(10).north(9).west(5).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(11).north(9).west(5).getX(), origin.above(11).north(9).west(5).getY(), origin.above(11).north(9).west(5).getZ(), origin.above(11).north(9).west(3).getX(), origin.above(11).north(9).west(3).getY(), origin.above(11).north(9).west(3).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());

			GenerationUtils.fillAreaWithBlockCube(world, origin.above().north(9).east(9).getX(), origin.above().north(9).east(9).getY(), origin.above().north(9).east(9).getZ(), origin.above(6).north(9).east(9).getX(), origin.above(6).north(9).east(9).getY(), origin.above(6).north(9).east(9).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(7).north(9).east(8).getX(), origin.above(7).north(9).east(8).getY(), origin.above(7).north(9).east(8).getZ(), origin.above(8).north(9).east(8).getX(), origin.above(8).north(9).east(8).getY(), origin.above(8).north(9).east(8).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(9).north(9).east(7).getX(), origin.above(9).north(9).east(7).getY(), origin.above(9).north(9).east(7).getZ(), origin.above(10).north(9).east(7).getX(), origin.above(10).north(9).east(7).getY(), origin.above(10).north(9).east(7).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(10).north(9).east(5).getX(), origin.above(10).north(9).east(5).getY(), origin.above(10).north(9).east(5).getZ(), origin.above(10).north(9).east(7).getX(), origin.above(10).north(9).east(7).getY(), origin.above(10).north(9).east(7).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(11).north(9).east(3).getX(), origin.above(11).north(9).east(3).getY(), origin.above(11).north(9).east(3).getZ(), origin.above(11).north(9).east(5).getX(), origin.above(11).north(9).east(5).getY(), origin.above(11).north(9).east(5).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());

			GenerationUtils.fillAreaWithBlockCube(world, origin.above(12).north(9).west(3).getX(), origin.above(12).north(9).west(3).getY(), origin.above(12).north(9).west(3).getZ(), origin.above(12).north(9).east(3).getX(), origin.above(12).north(9).east(3).getY(), origin.above(12).north(9).east(3).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
		}

		{
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(9).north(8).west(8).getX(), origin.above(9).north(8).west(8).getY(), origin.above(9).north(8).west(8).getZ(), origin.above(10).north(8).west(8).getX(), origin.above(10).north(8).west(8).getY(), origin.above(10).north(8).west(8).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(11).north(8).west(7).getX(), origin.above(11).north(8).west(7).getY(), origin.above(11).north(8).west(7).getZ(), origin.above(11).north(8).west(6).getX(), origin.above(11).north(8).west(6).getY(), origin.above(11).north(8).west(6).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(12).north(8).west(6).getX(), origin.above(12).north(8).west(6).getY(), origin.above(12).north(8).west(6).getZ(), origin.above(12).north(8).west(4).getX(), origin.above(12).north(8).west(4).getY(), origin.above(12).north(8).west(4).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());

			GenerationUtils.fillAreaWithBlockCube(world, origin.above(9).north(8).east(8).getX(), origin.above(9).north(8).east(8).getY(), origin.above(9).north(8).east(8).getZ(), origin.above(10).north(8).east(8).getX(), origin.above(10).north(8).east(8).getY(), origin.above(10).north(8).east(8).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(11).north(8).east(6).getX(), origin.above(11).north(8).east(6).getY(), origin.above(11).north(8).east(6).getZ(), origin.above(11).north(8).east(7).getX(), origin.above(11).north(8).east(7).getY(), origin.above(11).north(8).east(7).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(12).north(8).east(4).getX(), origin.above(12).north(8).east(4).getY(), origin.above(12).north(8).east(4).getZ(), origin.above(12).north(8).east(6).getX(), origin.above(12).north(8).east(6).getY(), origin.above(12).north(8).east(6).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());

			GenerationUtils.fillAreaWithBlockCube(world, origin.above(13).north(8).west(4).getX(), origin.above(13).north(8).west(4).getY(), origin.above(13).north(8).west(4).getZ(), origin.above(13).north(8).east(4).getX(), origin.above(13).north(8).east(4).getY(), origin.above(13).north(8).east(4).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
		}

		{
			this.setPoiseCluster(world, origin.above(12).north(7).west(7), rand);
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(13).north(7).west(6).getX(), origin.above(13).north(7).west(6).getY(), origin.above(13).north(7).west(6).getZ(), origin.above(13).north(7).west(5).getX(), origin.above(13).north(7).west(5).getY(), origin.above(13).north(7).west(5).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());

			this.setPoiseCluster(world, origin.above(12).north(7).east(7), rand);
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(13).north(7).east(5).getX(), origin.above(13).north(7).east(5).getY(), origin.above(13).north(7).east(5).getZ(), origin.above(13).north(7).east(6).getX(), origin.above(13).north(7).east(6).getY(), origin.above(13).north(7).east(6).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());

			GenerationUtils.fillAreaWithBlockCube(world, origin.above(14).north(7).west(4).getX(), origin.above(14).north(7).west(4).getY(), origin.above(14).north(7).west(4).getZ(), origin.above(14).north(7).east(4).getX(), origin.above(14).north(7).east(4).getY(), origin.above(14).north(7).east(4).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
		}

		{
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(14).north(6).west(6).getX(), origin.above(14).north(6).west(6).getY(), origin.above(14).north(6).west(6).getZ(), origin.above(14).north(6).west(3).getX(), origin.above(14).north(6).west(3).getY(), origin.above(14).north(6).west(3).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(14).north(6).east(3).getX(), origin.above(14).north(6).east(3).getY(), origin.above(14).north(6).east(3).getZ(), origin.above(14).north(6).east(6).getX(), origin.above(14).north(6).east(6).getY(), origin.above(14).north(6).east(6).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());

			GenerationUtils.fillAreaWithBlockCube(world, origin.above(15).north(6).west(3).getX(), origin.above(15).north(6).west(3).getY(), origin.above(15).north(6).west(3).getZ(), origin.above(14).north(6).east(3).getX(), origin.above(14).north(6).east(3).getY(), origin.above(14).north(6).east(3).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
		}

		{
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(15).north(6).west(2).getX(), origin.above(15).north(6).west(2).getY(), origin.above(15).north(6).west(2).getZ(), origin.above(15).north(6).east(2).getX(), origin.above(15).north(6).east(2).getY(), origin.above(15).north(6).east(2).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(15).north(5).west(5).getX(), origin.above(15).north(5).west(5).getY(), origin.above(15).north(5).west(5).getZ(), origin.above(15).north(5).east(5).getX(), origin.above(15).north(5).east(5).getY(), origin.above(15).north(5).east(5).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());

			GenerationUtils.fillAreaWithBlockCube(world, origin.above(15).north(4).west(5).getX(), origin.above(15).north(4).west(5).getY(), origin.above(15).north(4).west(5).getZ(), origin.above(15).north(4).west(2).getX(), origin.above(15).north(4).west(2).getY(), origin.above(15).north(4).west(2).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(15).north(4).east(2).getX(), origin.above(15).north(4).east(2).getY(), origin.above(15).north(4).east(2).getZ(), origin.above(15).north(4).east(5).getX(), origin.above(15).north(4).east(5).getY(), origin.above(15).north(4).east(5).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(16).north(4).west().getX(), origin.above(16).north(4).west().getY(), origin.above(16).north(4).west().getZ(), origin.above(16).north(4).east().getX(), origin.above(16).north(4).east().getY(), origin.above(16).north(4).east().getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
		}

		/*
		 * East
		 */
		{
			GenerationUtils.fillAreaWithBlockCube(world, origin.above().east(12).north(5).getX(), origin.above().east(12).north(5).getY(), origin.above().east(12).north(5).getZ(), origin.above(4).east(12).north(3).getX(), origin.above(4).east(12).north(3).getY(), origin.above(4).east(12).north(3).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above().east(12).south(3).getX(), origin.above().east(12).south(3).getY(), origin.above().east(12).south(3).getZ(), origin.above(4).east(12).south(5).getX(), origin.above(4).east(12).south(5).getY(), origin.above(4).east(12).south(5).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(5).east(12).north(4).getX(), origin.above(5).east(12).north(4).getY(), origin.above(5).east(12).north(4).getZ(), origin.above(6).east(12).south(4).getX(), origin.above(6).east(12).south(4).getY(), origin.above(6).east(12).south(4).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(7).east(12).north(2).getX(), origin.above(7).east(12).north(2).getY(), origin.above(7).east(12).north(2).getZ(), origin.above(7).east(12).south(2).getX(), origin.above(7).east(12).south(2).getY(), origin.above(7).east(12).south(2).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
		}

		{
			GenerationUtils.fillAreaWithBlockCube(world, origin.above().east(11).north(7).getX(), origin.above().east(11).north(7).getY(), origin.above().east(11).north(7).getZ(), origin.above(5).east(11).north(6).getX(), origin.above(5).east(11).north(6).getY(), origin.above(5).east(11).north(6).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(5).east(11).north(6).getX(), origin.above(5).east(11).north(6).getY(), origin.above(5).east(11).north(6).getZ(), origin.above(7).east(11).north(5).getX(), origin.above(7).east(11).north(5).getY(), origin.above(7).east(11).north(5).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(7).east(11).north(5).getX(), origin.above(7).east(11).north(5).getY(), origin.above(7).east(11).north(5).getZ(), origin.above(8).east(11).north(3).getX(), origin.above(8).east(11).north(3).getY(), origin.above(8).east(11).north(3).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());

			GenerationUtils.fillAreaWithBlockCube(world, origin.above().east(11).south(6).getX(), origin.above().east(11).south(6).getY(), origin.above().east(11).south(6).getZ(), origin.above(5).east(11).south(7).getX(), origin.above(5).east(11).south(7).getY(), origin.above(5).east(11).south(7).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(5).east(11).south(5).getX(), origin.above(5).east(11).south(5).getY(), origin.above(5).east(11).south(5).getZ(), origin.above(7).east(11).south(6).getX(), origin.above(7).east(11).south(6).getY(), origin.above(7).east(11).south(6).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(7).east(11).south(3).getX(), origin.above(7).east(11).south(3).getY(), origin.above(7).east(11).south(3).getZ(), origin.above(8).east(11).south(5).getX(), origin.above(8).east(11).south(5).getY(), origin.above(8).east(11).south(5).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());

			GenerationUtils.fillAreaWithBlockCube(world, origin.above(8).east(11).north(3).getX(), origin.above(8).east(11).north(3).getY(), origin.above(8).east(11).north(3).getZ(), origin.above(9).east(11).south(3).getX(), origin.above(9).east(11).south(3).getY(), origin.above(9).east(11).south(3).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			this.setPoiseCluster(world, origin.above(10).east(11).north(), rand);
			this.setPoiseCluster(world, origin.above(10).east(11), rand);
			this.setPoiseCluster(world, origin.above(10).east(11).south(), rand);
		}

		{
			GenerationUtils.fillAreaWithBlockCube(world, origin.above().east(10).north(8).getX(), origin.above().east(10).north(8).getY(), origin.above().east(10).north(8).getZ(), origin.above(6).east(10).north(8).getX(), origin.above(6).east(10).north(8).getY(), origin.above(6).east(10).north(8).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(6).east(10).north(7).getX(), origin.above(6).east(10).north(7).getY(), origin.above(6).east(10).north(7).getZ(), origin.above(8).east(10).north(7).getX(), origin.above(8).east(10).north(7).getY(), origin.above(8).east(10).north(7).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(8).east(10).north(6).getX(), origin.above(8).east(10).north(6).getY(), origin.above(8).east(10).north(6).getZ(), origin.above(9).east(10).north(6).getX(), origin.above(9).east(10).north(6).getY(), origin.above(9).east(10).north(6).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(9).east(10).north(6).getX(), origin.above(9).east(10).north(6).getY(), origin.above(9).east(10).north(6).getZ(), origin.above(9).east(10).north(4).getX(), origin.above(9).east(10).north(4).getY(), origin.above(9).east(10).north(4).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(10).east(10).north(4).getX(), origin.above(10).east(10).north(4).getY(), origin.above(10).east(10).north(4).getZ(), origin.above(10).east(10).north(2).getX(), origin.above(10).east(10).north(2).getY(), origin.above(10).east(10).north(2).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());

			GenerationUtils.fillAreaWithBlockCube(world, origin.above().east(10).south(8).getX(), origin.above().east(10).south(8).getY(), origin.above().east(10).south(8).getZ(), origin.above(6).east(10).south(8).getX(), origin.above(6).east(10).south(8).getY(), origin.above(6).east(10).south(8).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(6).east(10).south(7).getX(), origin.above(6).east(10).south(7).getY(), origin.above(6).east(10).south(7).getZ(), origin.above(8).east(10).south(7).getX(), origin.above(8).east(10).south(7).getY(), origin.above(8).east(10).south(7).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(8).east(10).south(6).getX(), origin.above(8).east(10).south(6).getY(), origin.above(8).east(10).south(6).getZ(), origin.above(9).east(10).south(6).getX(), origin.above(9).east(10).south(6).getY(), origin.above(9).east(10).south(6).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(9).east(10).south(4).getX(), origin.above(9).east(10).south(4).getY(), origin.above(9).east(10).south(4).getZ(), origin.above(9).east(10).south(6).getX(), origin.above(9).east(10).south(6).getY(), origin.above(9).east(10).south(6).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(10).east(10).south(2).getX(), origin.above(10).east(10).south(2).getY(), origin.above(10).east(10).south(2).getZ(), origin.above(10).east(10).south(4).getX(), origin.above(10).east(10).south(4).getY(), origin.above(10).east(10).south(4).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());

			GenerationUtils.fillAreaWithBlockCube(world, origin.above(11).east(10).north(2).getX(), origin.above(11).east(10).north(2).getY(), origin.above(11).east(10).north(2).getZ(), origin.above(11).east(10).south(2).getX(), origin.above(11).east(10).south(2).getY(), origin.above(11).east(10).south(2).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
		}

		{
			GenerationUtils.fillAreaWithBlockCube(world, origin.above().east(9).north(9).getX(), origin.above().east(9).north(9).getY(), origin.above().east(9).north(9).getZ(), origin.above(6).east(9).north(9).getX(), origin.above(6).east(9).north(9).getY(), origin.above(6).east(9).north(9).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(7).east(9).north(8).getX(), origin.above(7).east(9).north(8).getY(), origin.above(7).east(9).north(8).getZ(), origin.above(8).east(9).north(8).getX(), origin.above(8).east(9).north(8).getY(), origin.above(8).east(9).north(8).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(9).east(9).north(7).getX(), origin.above(9).east(9).north(7).getY(), origin.above(9).east(9).north(7).getZ(), origin.above(10).east(9).north(7).getX(), origin.above(10).east(9).north(7).getY(), origin.above(10).east(9).north(7).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(10).east(9).north(7).getX(), origin.above(10).east(9).north(7).getY(), origin.above(10).east(9).north(7).getZ(), origin.above(10).east(9).north(5).getX(), origin.above(10).east(9).north(5).getY(), origin.above(10).east(9).north(5).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(11).east(9).north(5).getX(), origin.above(11).east(9).north(5).getY(), origin.above(11).east(9).north(5).getZ(), origin.above(11).east(9).north(3).getX(), origin.above(11).east(9).north(3).getY(), origin.above(11).east(9).north(3).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());

			GenerationUtils.fillAreaWithBlockCube(world, origin.above().east(9).south(9).getX(), origin.above().east(9).south(9).getY(), origin.above().east(9).south(9).getZ(), origin.above(6).east(9).south(9).getX(), origin.above(6).east(9).south(9).getY(), origin.above(6).east(9).south(9).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(7).east(9).south(8).getX(), origin.above(7).east(9).south(8).getY(), origin.above(7).east(9).south(8).getZ(), origin.above(8).east(9).south(8).getX(), origin.above(8).east(9).south(8).getY(), origin.above(8).east(9).south(8).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(9).east(9).south(7).getX(), origin.above(9).east(9).south(7).getY(), origin.above(9).east(9).south(7).getZ(), origin.above(10).east(9).south(7).getX(), origin.above(10).east(9).south(7).getY(), origin.above(10).east(9).south(7).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(10).east(9).south(5).getX(), origin.above(10).east(9).south(5).getY(), origin.above(10).east(9).south(5).getZ(), origin.above(10).east(9).south(7).getX(), origin.above(10).east(9).south(7).getY(), origin.above(10).east(9).south(7).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(11).east(9).south(3).getX(), origin.above(11).east(9).south(3).getY(), origin.above(11).east(9).south(3).getZ(), origin.above(11).east(9).south(5).getX(), origin.above(11).east(9).south(5).getY(), origin.above(11).east(9).south(5).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());

			GenerationUtils.fillAreaWithBlockCube(world, origin.above(12).east(9).north(3).getX(), origin.above(12).east(9).north(3).getY(), origin.above(12).east(9).north(3).getZ(), origin.above(12).east(9).south(3).getX(), origin.above(12).east(9).south(3).getY(), origin.above(12).east(9).south(3).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
		}

		{
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(9).east(8).north(8).getX(), origin.above(9).east(8).north(8).getY(), origin.above(9).east(8).north(8).getZ(), origin.above(10).east(8).north(8).getX(), origin.above(10).east(8).north(8).getY(), origin.above(10).east(8).north(8).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(11).east(8).north(7).getX(), origin.above(11).east(8).north(7).getY(), origin.above(11).east(8).north(7).getZ(), origin.above(11).east(8).north(6).getX(), origin.above(11).east(8).north(6).getY(), origin.above(11).east(8).north(6).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(12).east(8).north(6).getX(), origin.above(12).east(8).north(6).getY(), origin.above(12).east(8).north(6).getZ(), origin.above(12).east(8).north(4).getX(), origin.above(12).east(8).north(4).getY(), origin.above(12).east(8).north(4).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());

			GenerationUtils.fillAreaWithBlockCube(world, origin.above(9).east(8).south(8).getX(), origin.above(9).east(8).south(8).getY(), origin.above(9).east(8).south(8).getZ(), origin.above(10).east(8).south(8).getX(), origin.above(10).east(8).south(8).getY(), origin.above(10).east(8).south(8).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(11).east(8).south(6).getX(), origin.above(11).east(8).south(6).getY(), origin.above(11).east(8).south(6).getZ(), origin.above(11).east(8).south(7).getX(), origin.above(11).east(8).south(7).getY(), origin.above(11).east(8).south(7).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(12).east(8).south(4).getX(), origin.above(12).east(8).south(4).getY(), origin.above(12).east(8).south(4).getZ(), origin.above(12).east(8).south(6).getX(), origin.above(12).east(8).south(6).getY(), origin.above(12).east(8).south(6).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());

			GenerationUtils.fillAreaWithBlockCube(world, origin.above(13).east(8).north(4).getX(), origin.above(13).east(8).north(4).getY(), origin.above(13).east(8).north(4).getZ(), origin.above(13).east(8).south(4).getX(), origin.above(13).east(8).south(4).getY(), origin.above(13).east(8).south(4).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
		}

		{
			this.setPoiseCluster(world, origin.above(12).east(7).north(7), rand);
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(13).east(7).north(6).getX(), origin.above(13).east(7).north(6).getY(), origin.above(13).east(7).north(6).getZ(), origin.above(13).east(7).north(5).getX(), origin.above(13).east(7).north(5).getY(), origin.above(13).east(7).north(5).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());

			this.setPoiseCluster(world, origin.above(12).east(7).south(7), rand);
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(13).east(7).south(5).getX(), origin.above(13).east(7).south(5).getY(), origin.above(13).east(7).south(5).getZ(), origin.above(13).east(7).south(6).getX(), origin.above(13).east(7).south(6).getY(), origin.above(13).east(7).south(6).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());

			GenerationUtils.fillAreaWithBlockCube(world, origin.above(14).east(7).north(4).getX(), origin.above(14).east(7).north(4).getY(), origin.above(14).east(7).north(4).getZ(), origin.above(14).east(7).south(4).getX(), origin.above(14).east(7).south(4).getY(), origin.above(14).east(7).south(4).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
		}

		{
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(14).east(6).north(6).getX(), origin.above(14).east(6).north(6).getY(), origin.above(14).east(6).north(6).getZ(), origin.above(14).east(6).north(3).getX(), origin.above(14).east(6).north(3).getY(), origin.above(14).east(6).north(3).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(14).east(6).south(3).getX(), origin.above(14).east(6).south(3).getY(), origin.above(14).east(6).south(3).getZ(), origin.above(14).east(6).south(6).getX(), origin.above(14).east(6).south(6).getY(), origin.above(14).east(6).south(6).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());

			GenerationUtils.fillAreaWithBlockCube(world, origin.above(15).east(6).north(3).getX(), origin.above(15).east(6).north(3).getY(), origin.above(15).east(6).north(3).getZ(), origin.above(14).east(6).south(3).getX(), origin.above(14).east(6).south(3).getY(), origin.above(14).east(7).south(3).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
		}

		{
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(15).east(6).north(2).getX(), origin.above(15).east(6).north(2).getY(), origin.above(15).east(6).north(2).getZ(), origin.above(15).east(6).south(2).getX(), origin.above(15).east(6).south(2).getY(), origin.above(15).east(6).south(2).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(15).east(5).north(5).getX(), origin.above(15).east(5).north(5).getY(), origin.above(15).east(5).north(5).getZ(), origin.above(15).east(5).south(5).getX(), origin.above(15).east(5).south(5).getY(), origin.above(15).east(5).south(5).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());

			GenerationUtils.fillAreaWithBlockCube(world, origin.above(15).east(4).north(5).getX(), origin.above(15).east(4).north(5).getY(), origin.above(15).east(4).north(5).getZ(), origin.above(15).east(4).north(2).getX(), origin.above(15).east(4).north(2).getY(), origin.above(15).east(4).north(2).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(15).east(4).south(2).getX(), origin.above(15).east(4).south(2).getY(), origin.above(15).east(4).south(2).getZ(), origin.above(15).east(4).south(5).getX(), origin.above(15).east(4).south(5).getY(), origin.above(15).east(4).south(5).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(16).east(4).north().getX(), origin.above(16).east(4).north().getY(), origin.above(16).east(4).north().getZ(), origin.above(16).east(4).south().getX(), origin.above(16).east(4).south().getY(), origin.above(16).east(4).south().getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
		}

		/*
		 * South
		 */
		{
			GenerationUtils.fillAreaWithBlockCube(world, origin.above().south(12).west(5).getX(), origin.above().south(12).west(5).getY(), origin.above().south(12).west(5).getZ(), origin.above(4).south(12).west(3).getX(), origin.above(4).south(12).west(3).getY(), origin.above(4).south(12).west(3).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above().south(12).east(3).getX(), origin.above().south(12).east(3).getY(), origin.above().south(12).east(3).getZ(), origin.above(4).south(12).east(5).getX(), origin.above(4).south(12).east(5).getY(), origin.above(4).south(12).east(5).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(5).south(12).west(4).getX(), origin.above(5).south(12).west(4).getY(), origin.above(5).south(12).west(4).getZ(), origin.above(6).south(12).east(4).getX(), origin.above(6).south(12).east(4).getY(), origin.above(6).south(12).east(4).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(7).south(12).west(2).getX(), origin.above(7).south(12).west(2).getY(), origin.above(7).south(12).west(2).getZ(), origin.above(7).south(12).east(2).getX(), origin.above(7).south(12).east(2).getY(), origin.above(7).south(12).east(2).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
		}

		{
			GenerationUtils.fillAreaWithBlockCube(world, origin.above().south(11).west(7).getX(), origin.above().south(11).west(7).getY(), origin.above().south(11).west(7).getZ(), origin.above(5).south(11).west(6).getX(), origin.above(5).south(11).west(6).getY(), origin.above(5).south(11).west(6).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(5).south(11).west(6).getX(), origin.above(5).south(11).west(6).getY(), origin.above(5).south(11).west(6).getZ(), origin.above(7).south(11).west(5).getX(), origin.above(7).south(11).west(5).getY(), origin.above(7).south(11).west(5).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(7).south(11).west(5).getX(), origin.above(7).south(11).west(5).getY(), origin.above(7).south(11).west(5).getZ(), origin.above(8).south(11).west(3).getX(), origin.above(8).south(11).west(3).getY(), origin.above(8).south(11).west(3).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());

			GenerationUtils.fillAreaWithBlockCube(world, origin.above().south(11).east(6).getX(), origin.above().south(11).east(6).getY(), origin.above().south(11).east(6).getZ(), origin.above(5).south(11).east(7).getX(), origin.above(5).south(11).east(7).getY(), origin.above(5).south(11).east(7).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(5).south(11).east(5).getX(), origin.above(5).south(11).east(5).getY(), origin.above(5).south(11).east(5).getZ(), origin.above(7).south(11).east(6).getX(), origin.above(7).south(11).east(6).getY(), origin.above(7).south(11).east(6).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(7).south(11).east(3).getX(), origin.above(7).south(11).east(3).getY(), origin.above(7).south(11).east(3).getZ(), origin.above(8).south(11).east(5).getX(), origin.above(8).south(11).east(5).getY(), origin.above(8).south(11).east(5).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());

			GenerationUtils.fillAreaWithBlockCube(world, origin.above(8).south(11).west(3).getX(), origin.above(8).south(11).west(3).getY(), origin.above(8).south(11).west(3).getZ(), origin.above(9).south(11).east(3).getX(), origin.above(9).south(11).east(3).getY(), origin.above(9).south(11).east(3).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(10).south(11).west().getX(), origin.above(10).south(11).west().getY(), origin.above(10).south(11).west().getZ(), origin.above(11).south(10).east().getX(), origin.above(10).south(11).east().getY(), origin.above(10).south(11).east().getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
		}

		{
			GenerationUtils.fillAreaWithBlockCube(world, origin.above().south(10).west(8).getX(), origin.above().south(10).west(8).getY(), origin.above().south(10).west(8).getZ(), origin.above(6).south(10).west(8).getX(), origin.above(6).south(10).west(8).getY(), origin.above(6).south(10).west(8).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(6).south(10).west(7).getX(), origin.above(6).south(10).west(7).getY(), origin.above(6).south(10).west(7).getZ(), origin.above(8).south(10).west(7).getX(), origin.above(8).south(10).west(7).getY(), origin.above(8).south(10).west(7).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(8).south(10).west(6).getX(), origin.above(8).south(10).west(6).getY(), origin.above(8).south(10).west(6).getZ(), origin.above(9).south(10).west(6).getX(), origin.above(9).south(10).west(6).getY(), origin.above(9).south(10).west(6).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(9).south(10).west(6).getX(), origin.above(9).south(10).west(6).getY(), origin.above(9).south(10).west(6).getZ(), origin.above(9).south(10).west(4).getX(), origin.above(9).south(10).west(4).getY(), origin.above(9).south(10).west(4).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(10).south(10).west(4).getX(), origin.above(10).south(10).west(4).getY(), origin.above(10).south(10).west(4).getZ(), origin.above(10).south(10).west(2).getX(), origin.above(10).south(10).west(2).getY(), origin.above(10).south(10).west(2).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());

			GenerationUtils.fillAreaWithBlockCube(world, origin.above().south(10).east(8).getX(), origin.above().south(10).east(8).getY(), origin.above().south(10).east(8).getZ(), origin.above(6).south(10).east(8).getX(), origin.above(6).south(10).east(8).getY(), origin.above(6).south(10).east(8).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(6).south(10).east(7).getX(), origin.above(6).south(10).east(7).getY(), origin.above(6).south(10).east(7).getZ(), origin.above(8).south(10).east(7).getX(), origin.above(8).south(10).east(7).getY(), origin.above(8).south(10).east(7).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(8).south(10).east(6).getX(), origin.above(8).south(10).east(6).getY(), origin.above(8).south(10).east(6).getZ(), origin.above(9).south(10).east(6).getX(), origin.above(9).south(10).east(6).getY(), origin.above(9).south(10).east(6).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(9).south(10).east(4).getX(), origin.above(9).south(10).east(4).getY(), origin.above(9).south(10).east(4).getZ(), origin.above(9).south(10).east(6).getX(), origin.above(9).south(10).east(6).getY(), origin.above(9).south(10).east(6).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(10).south(10).east(2).getX(), origin.above(10).south(10).east(2).getY(), origin.above(10).south(10).east(2).getZ(), origin.above(10).south(10).east(4).getX(), origin.above(10).south(10).east(4).getY(), origin.above(10).south(10).east(4).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());

			GenerationUtils.fillAreaWithBlockCube(world, origin.above(11).south(10).west(2).getX(), origin.above(11).south(10).west(2).getY(), origin.above(11).south(10).west(2).getZ(), origin.above(11).south(10).east(2).getX(), origin.above(11).south(10).east(2).getY(), origin.above(11).south(10).east(2).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
		}

		{
			GenerationUtils.fillAreaWithBlockCube(world, origin.above().south(9).west(9).getX(), origin.above().south(9).west(9).getY(), origin.above().south(9).west(9).getZ(), origin.above(6).south(9).west(9).getX(), origin.above(6).south(9).west(9).getY(), origin.above(6).south(9).west(9).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(7).south(9).west(8).getX(), origin.above(7).south(9).west(8).getY(), origin.above(7).south(9).west(8).getZ(), origin.above(8).south(9).west(8).getX(), origin.above(8).south(9).west(8).getY(), origin.above(8).south(9).west(8).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(9).south(9).west(7).getX(), origin.above(9).south(9).west(7).getY(), origin.above(9).south(9).west(7).getZ(), origin.above(10).south(9).west(7).getX(), origin.above(10).south(9).west(7).getY(), origin.above(10).south(9).west(7).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(10).south(9).west(7).getX(), origin.above(10).south(9).west(7).getY(), origin.above(10).south(9).west(7).getZ(), origin.above(10).south(9).west(5).getX(), origin.above(10).south(9).west(5).getY(), origin.above(10).south(9).west(5).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(11).south(9).west(5).getX(), origin.above(11).south(9).west(5).getY(), origin.above(11).south(9).west(5).getZ(), origin.above(11).south(9).west(3).getX(), origin.above(11).south(9).west(3).getY(), origin.above(11).south(9).west(3).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());

			GenerationUtils.fillAreaWithBlockCube(world, origin.above().south(9).east(9).getX(), origin.above().south(9).east(9).getY(), origin.above().south(9).east(9).getZ(), origin.above(6).south(9).east(9).getX(), origin.above(6).south(9).east(9).getY(), origin.above(6).south(9).east(9).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(7).south(9).east(8).getX(), origin.above(7).south(9).east(8).getY(), origin.above(7).south(9).east(8).getZ(), origin.above(8).south(9).east(8).getX(), origin.above(8).south(9).east(8).getY(), origin.above(8).south(9).east(8).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(9).south(9).east(7).getX(), origin.above(9).south(9).east(7).getY(), origin.above(9).south(9).east(7).getZ(), origin.above(10).south(9).east(7).getX(), origin.above(10).south(9).east(7).getY(), origin.above(10).south(9).east(7).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(10).south(9).east(5).getX(), origin.above(10).south(9).east(5).getY(), origin.above(10).south(9).east(5).getZ(), origin.above(10).south(9).east(7).getX(), origin.above(10).south(9).east(7).getY(), origin.above(10).south(9).east(7).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(11).south(9).east(3).getX(), origin.above(11).south(9).east(3).getY(), origin.above(11).south(9).east(3).getZ(), origin.above(11).south(9).east(5).getX(), origin.above(11).south(9).east(5).getY(), origin.above(11).south(9).east(5).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());

			GenerationUtils.fillAreaWithBlockCube(world, origin.above(12).south(9).west(3).getX(), origin.above(12).south(9).west(3).getY(), origin.above(12).south(9).west(3).getZ(), origin.above(12).south(9).east(3).getX(), origin.above(12).south(9).east(3).getY(), origin.above(12).south(9).east(3).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
		}

		{
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(9).south(8).west(8).getX(), origin.above(9).south(8).west(8).getY(), origin.above(9).south(8).west(8).getZ(), origin.above(10).south(8).west(8).getX(), origin.above(10).south(8).west(8).getY(), origin.above(10).south(8).west(8).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(11).south(8).west(7).getX(), origin.above(11).south(8).west(7).getY(), origin.above(11).south(8).west(7).getZ(), origin.above(11).south(8).west(6).getX(), origin.above(11).south(8).west(6).getY(), origin.above(11).south(8).west(6).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(12).south(8).west(6).getX(), origin.above(12).south(8).west(6).getY(), origin.above(12).south(8).west(6).getZ(), origin.above(12).south(8).west(4).getX(), origin.above(12).south(8).west(4).getY(), origin.above(12).south(8).west(4).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());

			GenerationUtils.fillAreaWithBlockCube(world, origin.above(9).south(8).east(8).getX(), origin.above(9).south(8).east(8).getY(), origin.above(9).south(8).east(8).getZ(), origin.above(10).south(8).east(8).getX(), origin.above(10).south(8).east(8).getY(), origin.above(10).south(8).east(8).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(11).south(8).east(6).getX(), origin.above(11).south(8).east(6).getY(), origin.above(11).south(8).east(6).getZ(), origin.above(11).south(8).east(7).getX(), origin.above(11).south(8).east(7).getY(), origin.above(11).south(8).east(7).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(12).south(8).east(4).getX(), origin.above(12).south(8).east(4).getY(), origin.above(12).south(8).east(4).getZ(), origin.above(12).south(8).east(6).getX(), origin.above(12).south(8).east(6).getY(), origin.above(12).south(8).east(6).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());

			GenerationUtils.fillAreaWithBlockCube(world, origin.above(13).south(8).west(4).getX(), origin.above(13).south(8).west(4).getY(), origin.above(13).south(8).west(4).getZ(), origin.above(13).south(8).east(4).getX(), origin.above(13).south(8).east(4).getY(), origin.above(13).south(8).east(4).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
		}

		{
			this.setPoiseCluster(world, origin.above(12).south(7).west(7), rand);
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(13).south(7).west(6).getX(), origin.above(13).south(7).west(6).getY(), origin.above(13).south(7).west(6).getZ(), origin.above(13).south(7).west(5).getX(), origin.above(13).south(7).west(5).getY(), origin.above(13).south(7).west(5).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());

			this.setPoiseCluster(world, origin.above(12).south(7).east(7), rand);
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(13).south(7).east(5).getX(), origin.above(13).south(7).east(5).getY(), origin.above(13).south(7).east(5).getZ(), origin.above(13).south(7).east(6).getX(), origin.above(13).south(7).east(6).getY(), origin.above(13).south(7).east(6).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());

			GenerationUtils.fillAreaWithBlockCube(world, origin.above(14).south(7).west(4).getX(), origin.above(14).south(7).west(4).getY(), origin.above(14).south(7).west(4).getZ(), origin.above(14).south(7).east(4).getX(), origin.above(14).south(7).east(4).getY(), origin.above(14).south(7).east(4).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
		}

		{
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(14).south(6).west(6).getX(), origin.above(14).south(6).west(6).getY(), origin.above(14).south(6).west(6).getZ(), origin.above(14).south(6).west(3).getX(), origin.above(14).south(6).west(3).getY(), origin.above(14).south(6).west(3).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(14).south(6).east(3).getX(), origin.above(14).south(6).east(3).getY(), origin.above(14).south(6).east(3).getZ(), origin.above(14).south(6).east(6).getX(), origin.above(14).south(6).east(6).getY(), origin.above(14).south(6).east(6).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());

			GenerationUtils.fillAreaWithBlockCube(world, origin.above(15).south(6).west(3).getX(), origin.above(15).south(6).west(3).getY(), origin.above(15).south(6).west(3).getZ(), origin.above(14).south(6).east(3).getX(), origin.above(14).south(6).east(3).getY(), origin.above(14).south(7).east(3).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
		}

		{
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(15).south(6).west(2).getX(), origin.above(15).south(6).west(2).getY(), origin.above(15).south(6).west(2).getZ(), origin.above(15).south(6).east(2).getX(), origin.above(15).south(6).east(2).getY(), origin.above(15).south(6).east(2).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(15).south(5).west(5).getX(), origin.above(15).south(5).west(5).getY(), origin.above(15).south(5).west(5).getZ(), origin.above(15).south(5).east(5).getX(), origin.above(15).south(5).east(5).getY(), origin.above(15).south(5).east(5).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());

			GenerationUtils.fillAreaWithBlockCube(world, origin.above(15).south(4).west(5).getX(), origin.above(15).south(4).west(5).getY(), origin.above(15).south(4).west(5).getZ(), origin.above(15).south(4).west(2).getX(), origin.above(15).south(4).west(2).getY(), origin.above(15).south(4).west(2).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(15).south(4).east(2).getX(), origin.above(15).south(4).east(2).getY(), origin.above(15).south(4).east(2).getZ(), origin.above(15).south(4).east(5).getX(), origin.above(15).south(4).east(5).getY(), origin.above(15).south(4).east(5).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(16).south(4).west().getX(), origin.above(16).south(4).west().getY(), origin.above(16).south(4).west().getZ(), origin.above(16).south(4).east().getX(), origin.above(16).south(4).east().getY(), origin.above(16).south(4).east().getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
		}

		/*
		 * West
		 */
		{
			GenerationUtils.fillAreaWithBlockCube(world, origin.above().west(12).north(5).getX(), origin.above().west(12).north(5).getY(), origin.above().west(12).north(5).getZ(), origin.above(4).west(12).north(3).getX(), origin.above(4).west(12).north(3).getY(), origin.above(4).west(12).north(3).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above().west(12).south(3).getX(), origin.above().west(12).south(3).getY(), origin.above().west(12).south(3).getZ(), origin.above(4).west(12).south(5).getX(), origin.above(4).west(12).south(5).getY(), origin.above(4).west(12).south(5).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(5).west(12).north(4).getX(), origin.above(5).west(12).north(4).getY(), origin.above(5).west(12).north(4).getZ(), origin.above(6).west(12).south(4).getX(), origin.above(6).west(12).south(4).getY(), origin.above(6).west(12).south(4).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(7).west(12).north(2).getX(), origin.above(7).west(12).north(2).getY(), origin.above(7).west(12).north(2).getZ(), origin.above(7).west(12).south(2).getX(), origin.above(7).west(12).south(2).getY(), origin.above(7).west(12).south(2).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
		}

		{
			GenerationUtils.fillAreaWithBlockCube(world, origin.above().west(11).north(7).getX(), origin.above().west(11).north(7).getY(), origin.above().west(11).north(7).getZ(), origin.above(5).west(11).north(6).getX(), origin.above(5).west(11).north(6).getY(), origin.above(5).west(11).north(6).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(5).west(11).north(6).getX(), origin.above(5).west(11).north(6).getY(), origin.above(5).west(11).north(6).getZ(), origin.above(7).west(11).north(5).getX(), origin.above(7).west(11).north(5).getY(), origin.above(7).west(11).north(5).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(7).west(11).north(5).getX(), origin.above(7).west(11).north(5).getY(), origin.above(7).west(11).north(5).getZ(), origin.above(8).west(11).north(3).getX(), origin.above(8).west(11).north(3).getY(), origin.above(8).west(11).north(3).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());

			GenerationUtils.fillAreaWithBlockCube(world, origin.above().west(11).south(6).getX(), origin.above().west(11).south(6).getY(), origin.above().west(11).south(6).getZ(), origin.above(5).west(11).south(7).getX(), origin.above(5).west(11).south(7).getY(), origin.above(5).west(11).south(7).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(5).west(11).south(5).getX(), origin.above(5).west(11).south(5).getY(), origin.above(5).west(11).south(5).getZ(), origin.above(7).west(11).south(6).getX(), origin.above(7).west(11).south(6).getY(), origin.above(7).west(11).south(6).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(7).west(11).south(3).getX(), origin.above(7).west(11).south(3).getY(), origin.above(7).west(11).south(3).getZ(), origin.above(8).west(11).south(5).getX(), origin.above(8).west(11).south(5).getY(), origin.above(8).west(11).south(5).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());

			GenerationUtils.fillAreaWithBlockCube(world, origin.above(8).west(11).north(3).getX(), origin.above(8).west(11).north(3).getY(), origin.above(8).west(11).north(3).getZ(), origin.above(9).west(11).south(3).getX(), origin.above(9).west(11).south(3).getY(), origin.above(9).west(11).south(3).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			this.setPoiseCluster(world, origin.above(10).west(11).north(), rand);
			this.setPoiseCluster(world, origin.above(10).west(11), rand);
			this.setPoiseCluster(world, origin.above(10).west(11).south(), rand);
		}

		{
			GenerationUtils.fillAreaWithBlockCube(world, origin.above().west(10).north(8).getX(), origin.above().west(10).north(8).getY(), origin.above().west(10).north(8).getZ(), origin.above(6).west(10).north(8).getX(), origin.above(6).west(10).north(8).getY(), origin.above(6).west(10).north(8).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(6).west(10).north(7).getX(), origin.above(6).west(10).north(7).getY(), origin.above(6).west(10).north(7).getZ(), origin.above(8).west(10).north(7).getX(), origin.above(8).west(10).north(7).getY(), origin.above(8).west(10).north(7).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(8).west(10).north(6).getX(), origin.above(8).west(10).north(6).getY(), origin.above(8).west(10).north(6).getZ(), origin.above(9).west(10).north(6).getX(), origin.above(9).west(10).north(6).getY(), origin.above(9).west(10).north(6).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(9).west(10).north(6).getX(), origin.above(9).west(10).north(6).getY(), origin.above(9).west(10).north(6).getZ(), origin.above(9).west(10).north(4).getX(), origin.above(9).west(10).north(4).getY(), origin.above(9).west(10).north(4).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(10).west(10).north(4).getX(), origin.above(10).west(10).north(4).getY(), origin.above(10).west(10).north(4).getZ(), origin.above(10).west(10).north(2).getX(), origin.above(10).west(10).north(2).getY(), origin.above(10).west(10).north(2).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());

			GenerationUtils.fillAreaWithBlockCube(world, origin.above().west(10).south(8).getX(), origin.above().west(10).south(8).getY(), origin.above().west(10).south(8).getZ(), origin.above(6).west(10).south(8).getX(), origin.above(6).west(10).south(8).getY(), origin.above(6).west(10).south(8).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(6).west(10).south(7).getX(), origin.above(6).west(10).south(7).getY(), origin.above(6).west(10).south(7).getZ(), origin.above(8).west(10).south(7).getX(), origin.above(8).west(10).south(7).getY(), origin.above(8).west(10).south(7).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(8).west(10).south(6).getX(), origin.above(8).west(10).south(6).getY(), origin.above(8).west(10).south(6).getZ(), origin.above(9).west(10).south(6).getX(), origin.above(9).west(10).south(6).getY(), origin.above(9).west(10).south(6).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(9).west(10).south(4).getX(), origin.above(9).west(10).south(4).getY(), origin.above(9).west(10).south(4).getZ(), origin.above(9).west(10).south(6).getX(), origin.above(9).west(10).south(6).getY(), origin.above(9).west(10).south(6).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(10).west(10).south(2).getX(), origin.above(10).west(10).south(2).getY(), origin.above(10).west(10).south(2).getZ(), origin.above(10).west(10).south(4).getX(), origin.above(10).west(10).south(4).getY(), origin.above(10).west(10).south(4).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());

			GenerationUtils.fillAreaWithBlockCube(world, origin.above(11).west(10).north(2).getX(), origin.above(11).west(10).north(2).getY(), origin.above(11).west(10).north(2).getZ(), origin.above(11).west(10).south(2).getX(), origin.above(11).west(10).south(2).getY(), origin.above(11).west(10).south(2).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
		}

		{
			GenerationUtils.fillAreaWithBlockCube(world, origin.above().west(9).north(9).getX(), origin.above().west(9).north(9).getY(), origin.above().west(9).north(9).getZ(), origin.above(6).west(9).north(9).getX(), origin.above(6).west(9).north(9).getY(), origin.above(6).west(9).north(9).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(7).west(9).north(8).getX(), origin.above(7).west(9).north(8).getY(), origin.above(7).west(9).north(8).getZ(), origin.above(8).west(9).north(8).getX(), origin.above(8).west(9).north(8).getY(), origin.above(8).west(9).north(8).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(9).west(9).north(7).getX(), origin.above(9).west(9).north(7).getY(), origin.above(9).west(9).north(7).getZ(), origin.above(10).west(9).north(7).getX(), origin.above(10).west(9).north(7).getY(), origin.above(10).west(9).north(7).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(10).west(9).north(7).getX(), origin.above(10).west(9).north(7).getY(), origin.above(10).west(9).north(7).getZ(), origin.above(10).west(9).north(5).getX(), origin.above(10).west(9).north(5).getY(), origin.above(10).west(9).north(5).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(11).west(9).north(5).getX(), origin.above(11).west(9).north(5).getY(), origin.above(11).west(9).north(5).getZ(), origin.above(11).west(9).north(3).getX(), origin.above(11).west(9).north(3).getY(), origin.above(11).west(9).north(3).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());

			GenerationUtils.fillAreaWithBlockCube(world, origin.above().west(9).south(9).getX(), origin.above().west(9).south(9).getY(), origin.above().west(9).south(9).getZ(), origin.above(6).west(9).south(9).getX(), origin.above(6).west(9).south(9).getY(), origin.above(6).west(9).south(9).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(7).west(9).south(8).getX(), origin.above(7).west(9).south(8).getY(), origin.above(7).west(9).south(8).getZ(), origin.above(8).west(9).south(8).getX(), origin.above(8).west(9).south(8).getY(), origin.above(8).west(9).south(8).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(9).west(9).south(7).getX(), origin.above(9).west(9).south(7).getY(), origin.above(9).west(9).south(7).getZ(), origin.above(10).west(9).south(7).getX(), origin.above(10).west(9).south(7).getY(), origin.above(10).west(9).south(7).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(10).west(9).south(5).getX(), origin.above(10).west(9).south(5).getY(), origin.above(10).west(9).south(5).getZ(), origin.above(10).west(9).south(7).getX(), origin.above(10).west(9).south(7).getY(), origin.above(10).west(9).south(7).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(11).west(9).south(3).getX(), origin.above(11).west(9).south(3).getY(), origin.above(11).west(9).south(3).getZ(), origin.above(11).west(9).south(5).getX(), origin.above(11).west(9).south(5).getY(), origin.above(11).west(9).south(5).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());

			GenerationUtils.fillAreaWithBlockCube(world, origin.above(12).west(9).north(3).getX(), origin.above(12).west(9).north(3).getY(), origin.above(12).west(9).north(3).getZ(), origin.above(12).west(9).south(3).getX(), origin.above(12).west(9).south(3).getY(), origin.above(12).west(9).south(3).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
		}

		{
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(9).west(8).north(8).getX(), origin.above(9).west(8).north(8).getY(), origin.above(9).west(8).north(8).getZ(), origin.above(10).west(8).north(8).getX(), origin.above(10).west(8).north(8).getY(), origin.above(10).west(8).north(8).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(11).west(8).north(7).getX(), origin.above(11).west(8).north(7).getY(), origin.above(11).west(8).north(7).getZ(), origin.above(11).west(8).north(6).getX(), origin.above(11).west(8).north(6).getY(), origin.above(11).west(8).north(6).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(12).west(8).north(6).getX(), origin.above(12).west(8).north(6).getY(), origin.above(12).west(8).north(6).getZ(), origin.above(12).west(8).north(4).getX(), origin.above(12).west(8).north(4).getY(), origin.above(12).west(8).north(4).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());

			GenerationUtils.fillAreaWithBlockCube(world, origin.above(9).west(8).south(8).getX(), origin.above(9).west(8).south(8).getY(), origin.above(9).west(8).south(8).getZ(), origin.above(10).west(8).south(8).getX(), origin.above(10).west(8).south(8).getY(), origin.above(10).west(8).south(8).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(11).west(8).south(6).getX(), origin.above(11).west(8).south(6).getY(), origin.above(11).west(8).south(6).getZ(), origin.above(11).west(8).south(7).getX(), origin.above(11).west(8).south(7).getY(), origin.above(11).west(8).south(7).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(12).west(8).south(4).getX(), origin.above(12).west(8).south(4).getY(), origin.above(12).west(8).south(4).getZ(), origin.above(12).west(8).south(6).getX(), origin.above(12).west(8).south(6).getY(), origin.above(12).west(8).south(6).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());

			GenerationUtils.fillAreaWithBlockCube(world, origin.above(13).west(8).north(4).getX(), origin.above(13).west(8).north(4).getY(), origin.above(13).west(8).north(4).getZ(), origin.above(13).west(8).south(4).getX(), origin.above(13).west(8).south(4).getY(), origin.above(13).west(8).south(4).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
		}

		{
			this.setPoiseCluster(world, origin.above(12).west(7).north(7), rand);
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(13).west(7).north(6).getX(), origin.above(13).west(7).north(6).getY(), origin.above(13).west(7).north(6).getZ(), origin.above(13).west(7).north(5).getX(), origin.above(13).west(7).north(5).getY(), origin.above(13).west(7).north(5).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());

			this.setPoiseCluster(world, origin.above(12).west(7).south(7), rand);
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(13).west(7).south(5).getX(), origin.above(13).west(7).south(5).getY(), origin.above(13).west(7).south(5).getZ(), origin.above(13).west(7).south(6).getX(), origin.above(13).west(7).south(6).getY(), origin.above(13).west(7).south(6).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());

			GenerationUtils.fillAreaWithBlockCube(world, origin.above(14).west(7).north(4).getX(), origin.above(14).west(7).north(4).getY(), origin.above(14).west(7).north(4).getZ(), origin.above(14).west(7).south(4).getX(), origin.above(14).west(7).south(4).getY(), origin.above(14).west(7).south(4).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
		}

		{
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(14).west(6).north(6).getX(), origin.above(14).west(6).north(6).getY(), origin.above(14).west(6).north(6).getZ(), origin.above(14).west(6).north(3).getX(), origin.above(14).west(6).north(3).getY(), origin.above(14).west(6).north(3).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(14).west(6).south(3).getX(), origin.above(14).west(6).south(3).getY(), origin.above(14).west(6).south(3).getZ(), origin.above(14).west(6).south(6).getX(), origin.above(14).west(6).south(6).getY(), origin.above(14).west(6).south(6).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());

			GenerationUtils.fillAreaWithBlockCube(world, origin.above(15).west(6).north(3).getX(), origin.above(15).west(6).north(3).getY(), origin.above(15).west(6).north(3).getZ(), origin.above(14).west(6).south(3).getX(), origin.above(14).west(6).south(3).getY(), origin.above(14).west(7).south(3).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
		}

		{
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(15).west(6).north(2).getX(), origin.above(15).west(6).north(2).getY(), origin.above(15).west(6).north(2).getZ(), origin.above(15).west(6).south(2).getX(), origin.above(15).west(6).south(2).getY(), origin.above(15).west(6).south(2).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(15).west(5).north(5).getX(), origin.above(15).west(5).north(5).getY(), origin.above(15).west(5).north(5).getZ(), origin.above(15).west(5).south(5).getX(), origin.above(15).west(5).south(5).getY(), origin.above(15).west(5).south(5).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());

			GenerationUtils.fillAreaWithBlockCube(world, origin.above(15).west(4).north(5).getX(), origin.above(15).west(4).north(5).getY(), origin.above(15).west(4).north(5).getZ(), origin.above(15).west(4).north(2).getX(), origin.above(15).west(4).north(2).getY(), origin.above(15).west(4).north(2).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(15).west(4).south(2).getX(), origin.above(15).west(4).south(2).getY(), origin.above(15).west(4).south(2).getZ(), origin.above(15).west(4).south(5).getX(), origin.above(15).west(4).south(5).getY(), origin.above(15).west(4).south(5).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
			GenerationUtils.fillAreaWithBlockCube(world, origin.above(16).west(4).north().getX(), origin.above(16).west(4).north().getY(), origin.above(16).west(4).north().getZ(), origin.above(16).west(4).south().getX(), origin.above(16).west(4).south().getY(), origin.above(16).west(4).south().getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());
		}

		/*
		 * Top
		 */
		GenerationUtils.fillAreaWithBlockCube(world, origin.above(16).north(3).west(3).getX(), origin.above(16).north(3).west(3).getY(), origin.above(16).north(3).west(3).getZ(), origin.above(16).south(3).east(3).getX(), origin.above(16).south(3).east(3).getY(), origin.above(16).south(3).east(3).getZ(), EEBlocks.POISE_CLUSTER.get().defaultBlockState());

		if (rand.nextFloat() <= 0.25F) {
			int i = rand.nextInt(4);
			this.buildDomeHole(world, origin.above(16), rand, true, i);
		}
		boolean[] doSide = {
				rand.nextBoolean(),
				rand.nextBoolean(),
				rand.nextBoolean(),
				rand.nextBoolean()
		};
		if (doSide[0]) {
			this.buildDomeHole(world, origin.above(16), rand, false, 0);
		}
		if (doSide[1]) {
			this.buildDomeHole(world, origin.above(16), rand, false, 1);
		}
		if (doSide[2]) {
			this.buildDomeHole(world, origin.above(16), rand, false, 2);
		}
		if (doSide[3]) {
			this.buildDomeHole(world, origin.above(16), rand, false, 3);
		}

		boolean[] doHangerSide = {
				rand.nextFloat() <= 0.25F,
				rand.nextFloat() <= 0.25F,
				rand.nextFloat() <= 0.25F,
				rand.nextFloat() <= 0.25F,
		};
		if (doHangerSide[0]) {
			if (rand.nextBoolean()) {
				this.buildPoiseHanger(world, origin.above(8).north(10).east(rand.nextInt(3)), rand, 0, false);
			} else {
				this.buildPoiseHanger(world, origin.above(8).north(10).west(rand.nextInt(3)), rand, 0, false);
			}
		}
		if (doHangerSide[1]) {
			if (rand.nextBoolean()) {
				this.buildPoiseHanger(world, origin.above(8).east(10).south(rand.nextInt(3)), rand, 1, false);
			} else {
				this.buildPoiseHanger(world, origin.above(8).east(10).north(rand.nextInt(3)), rand, 1, false);
			}
		}
		if (doHangerSide[2]) {
			if (rand.nextBoolean()) {
				this.buildPoiseHanger(world, origin.above(8).south(10).west(rand.nextInt(3)), rand, 2, false);
			} else {
				this.buildPoiseHanger(world, origin.above(8).south(10).east(rand.nextInt(3)), rand, 2, false);
			}
		}
		if (doHangerSide[3]) {
			if (rand.nextBoolean()) {
				this.buildPoiseHanger(world, origin.above(8).west(10).south(rand.nextInt(3)), rand, 3, false);
			} else {
				this.buildPoiseHanger(world, origin.above(8).west(10).north(rand.nextInt(3)), rand, 3, false);
			}
		}
		if (rand.nextFloat() <= 0.25F) {
			this.buildPoiseHanger(world, origin.above(5).north(9).east(8), rand, 0, true);
		}
		if (rand.nextFloat() <= 0.25F) {
			this.buildPoiseHanger(world, origin.above(5).north(8).west(9), rand, 1, true);
		}
		if (rand.nextFloat() <= 0.25F) {
			this.buildPoiseHanger(world, origin.above(5).south(8).east(9), rand, 2, true);
		}
		if (rand.nextFloat() <= 0.25F) {
			this.buildPoiseHanger(world, origin.above(5).south(9).west(8), rand, 3, true);
		}
	}

	private void buildPoismossCircle(LevelAccessor world, LevelSimulatedRW reader, RandomSource random, BlockPos pos) {
		this.placePoismossCircle(world, reader, pos.west().north());
		this.placePoismossCircle(world, reader, pos.east(2).north());
		this.placePoismossCircle(world, reader, pos.west().south(2));
		this.placePoismossCircle(world, reader, pos.east(2).south(2));

		for (int i = 0; i < 5; ++i) {
			int j = random.nextInt(64);
			int k = j % 8;
			int l = j / 8;
			if (k == 0 || k == 7 || l == 0 || l == 7) {
				this.placePoismossCircle(world, reader, pos.offset(-3 + k, 0, -3 + l));
			}
		}
	}

	private void placePoismossCircle(LevelAccessor world, LevelSimulatedRW reader, BlockPos center) {
		for (int i = -2; i <= 2; ++i) {
			for (int j = -2; j <= 2; ++j) {
				if (Math.abs(i) != 2 || Math.abs(j) != 2) {
					this.placePoismossAt(world, reader, center.offset(i, 0, j));
				}
			}
		}
	}

	private void buildDomeHole(LevelAccessor world, BlockPos pos, RandomSource rand, boolean top, int variant) {
		if (top) {
			switch (variant) {
				case 0:
					this.setPoiseLogUnsafe(world, pos, rand);
					this.setPoiseLogUnsafe(world, pos.north(), rand);
					this.setPoiseLogUnsafe(world, pos.north(2), rand);

					this.setPoiseLogUnsafe(world, pos.north(3).east(), rand);
					this.setPoiseLogUnsafe(world, pos.north(4).east(), rand);

					this.setPoiseLogUnsafe(world, pos.below().north(5).east(2), rand);
					this.setPoiseLogUnsafe(world, pos.below().north(5).east(3), rand);

					this.setPoiseLogUnsafe(world, pos.below(2).north(6).east(4), rand);
					this.setPoiseLogUnsafe(world, pos.below(2).north(6).east(5), rand);
					this.setPoiseLogUnsafe(world, pos.below(2).north(6).east(6), rand);

					this.setPoiseLogUnsafe(world, pos.below(3).north(5).east(7), rand);
					this.setPoiseLogUnsafe(world, pos.below(3).north(4).east(8), rand);
					this.setPoiseLogUnsafe(world, pos.below(3).north(3).east(8), rand);
					this.setPoiseLogUnsafe(world, pos.below(3).north(2).east(8), rand);

					this.setPoiseLogUnsafe(world, pos.below(2).north().east(7), rand);
					this.setPoiseLogUnsafe(world, pos.below(2).east(7), rand);

					this.setPoiseLogUnsafe(world, pos.below().south().east(6), rand);
					this.setPoiseLogUnsafe(world, pos.below().south().east(5), rand);

					this.setPoiseLogUnsafe(world, pos.south(2).east(4), rand);
					this.setPoiseLogUnsafe(world, pos.south(2).east(3), rand);
					this.setPoiseLogUnsafe(world, pos.south(2).east(2), rand);
					this.setPoiseLogUnsafe(world, pos.south().east(), rand);

					//Air
					GenerationUtils.forceFillAreaWithBlockCube(world, pos.north(2).east().getX(), pos.north(2).east().getY(), pos.north(2).east().getZ(), pos.east().getX(), pos.east().getY(), pos.east().getZ(), Blocks.AIR.defaultBlockState());
					GenerationUtils.forceFillAreaWithBlockCube(world, pos.below().north(4).east(2).getX(), pos.below().north(4).east(2).getY(), pos.below().north(4).east(2).getZ(), pos.south().east(4).getX(), pos.south().east(4).getY(), pos.south().east(4).getZ(), Blocks.AIR.defaultBlockState());
					GenerationUtils.forceFillAreaWithBlockCube(world, pos.below(2).north(5).east(4).getX(), pos.below(2).north(5).east(4).getY(), pos.below(2).north(5).east(4).getZ(), pos.below().east(6).getX(), pos.below().east(6).getY(), pos.below().east(6).getZ(), Blocks.AIR.defaultBlockState());
					GenerationUtils.forceFillAreaWithBlockCube(world, pos.below(2).north(4).east(7).getX(), pos.below(2).north(4).east(7).getY(), pos.below(2).north(4).east(7).getZ(), pos.below(2).north(2).east(7).getX(), pos.below(2).north(2).east(7).getY(), pos.below(2).north(2).east(7).getZ(), Blocks.AIR.defaultBlockState());
					break;
				case 1:
					this.setPoiseLogUnsafe(world, pos, rand);
					this.setPoiseLogUnsafe(world, pos.east(), rand);
					this.setPoiseLogUnsafe(world, pos.east(2), rand);

					this.setPoiseLogUnsafe(world, pos.east(3).south(), rand);
					this.setPoiseLogUnsafe(world, pos.east(4).south(), rand);

					this.setPoiseLogUnsafe(world, pos.below().east(5).south(2), rand);
					this.setPoiseLogUnsafe(world, pos.below().east(5).south(3), rand);

					this.setPoiseLogUnsafe(world, pos.below(2).east(6).south(4), rand);
					this.setPoiseLogUnsafe(world, pos.below(2).east(6).south(5), rand);
					this.setPoiseLogUnsafe(world, pos.below(2).east(6).south(6), rand);

					this.setPoiseLogUnsafe(world, pos.below(3).east(5).south(7), rand);
					this.setPoiseLogUnsafe(world, pos.below(3).east(4).south(8), rand);
					this.setPoiseLogUnsafe(world, pos.below(3).east(3).south(8), rand);
					this.setPoiseLogUnsafe(world, pos.below(3).east(2).south(8), rand);

					this.setPoiseLogUnsafe(world, pos.below(2).east().south(7), rand);
					this.setPoiseLogUnsafe(world, pos.below(2).south(7), rand);

					this.setPoiseLogUnsafe(world, pos.below().west().south(6), rand);
					this.setPoiseLogUnsafe(world, pos.below().west().south(5), rand);

					this.setPoiseLogUnsafe(world, pos.west(2).south(4), rand);
					this.setPoiseLogUnsafe(world, pos.west(2).south(3), rand);
					this.setPoiseLogUnsafe(world, pos.west(2).south(2), rand);
					this.setPoiseLogUnsafe(world, pos.west().south(), rand);

					//Air
					GenerationUtils.forceFillAreaReversedWithBlockCube(world, pos.east(2).south().getX(), pos.east(2).south().getY(), pos.east(2).south().getZ(), pos.south().getX(), pos.south().getY(), pos.south().getZ(), Blocks.AIR.defaultBlockState());
					GenerationUtils.forceFillAreaReversedWithBlockCube(world, pos.below().east(4).south(4).getX(), pos.below().east(4).south(4).getY(), pos.below().east(4).south(4).getZ(), pos.west().south(2).getX(), pos.west().south(2).getY(), pos.west().south(2).getZ(), Blocks.AIR.defaultBlockState());
					GenerationUtils.forceFillAreaReversedWithBlockCube(world, pos.below(2).east(5).south(6).getX(), pos.below(2).east(5).south(6).getY(), pos.below(2).east(5).south(6).getZ(), pos.below().south(4).getX(), pos.below().south(4).getY(), pos.below().south(4).getZ(), Blocks.AIR.defaultBlockState());
					GenerationUtils.forceFillAreaReversedWithBlockCube(world, pos.below(2).east(4).south(7).getX(), pos.below(2).east(4).south(7).getY(), pos.below(2).east(4).south(7).getZ(), pos.below(2).east(2).south(7).getX(), pos.below(2).east(2).south(7).getY(), pos.below(2).east(2).south(7).getZ(), Blocks.AIR.defaultBlockState());
					break;
				case 2:
					this.setPoiseLogUnsafe(world, pos, rand);
					this.setPoiseLogUnsafe(world, pos.south(), rand);
					this.setPoiseLogUnsafe(world, pos.south(2), rand);

					this.setPoiseLogUnsafe(world, pos.south(3).west(), rand);
					this.setPoiseLogUnsafe(world, pos.south(4).west(), rand);

					this.setPoiseLogUnsafe(world, pos.below().south(5).west(2), rand);
					this.setPoiseLogUnsafe(world, pos.below().south(5).west(3), rand);

					this.setPoiseLogUnsafe(world, pos.below(2).south(6).west(4), rand);
					this.setPoiseLogUnsafe(world, pos.below(2).south(6).west(5), rand);
					this.setPoiseLogUnsafe(world, pos.below(2).south(6).west(6), rand);

					this.setPoiseLogUnsafe(world, pos.below(3).south(5).west(7), rand);
					this.setPoiseLogUnsafe(world, pos.below(3).south(4).west(8), rand);
					this.setPoiseLogUnsafe(world, pos.below(3).south(3).west(8), rand);
					this.setPoiseLogUnsafe(world, pos.below(3).south(2).west(8), rand);

					this.setPoiseLogUnsafe(world, pos.below(2).south().west(7), rand);
					this.setPoiseLogUnsafe(world, pos.below(2).west(7), rand);

					this.setPoiseLogUnsafe(world, pos.below().north().west(6), rand);
					this.setPoiseLogUnsafe(world, pos.below().north().west(5), rand);

					this.setPoiseLogUnsafe(world, pos.north(2).west(4), rand);
					this.setPoiseLogUnsafe(world, pos.north(2).west(3), rand);
					this.setPoiseLogUnsafe(world, pos.north(2).west(2), rand);
					this.setPoiseLogUnsafe(world, pos.north().west(), rand);

					//Air
					GenerationUtils.forceFillAreaReversedWithBlockCube(world, pos.south(2).west().getX(), pos.south(2).west().getY(), pos.south(2).west().getZ(), pos.west().getX(), pos.west().getY(), pos.west().getZ(), Blocks.AIR.defaultBlockState());
					GenerationUtils.forceFillAreaReversedWithBlockCube(world, pos.below().south(4).west(2).getX(), pos.below().south(4).west(2).getY(), pos.below().south(4).west(2).getZ(), pos.north().west(4).getX(), pos.north().west(4).getY(), pos.north().west(4).getZ(), Blocks.AIR.defaultBlockState());
					GenerationUtils.forceFillAreaReversedWithBlockCube(world, pos.below(2).south(5).west(4).getX(), pos.below(2).south(5).west(4).getY(), pos.below(2).south(5).west(4).getZ(), pos.below().west(6).getX(), pos.below().west(6).getY(), pos.below().west(6).getZ(), Blocks.AIR.defaultBlockState());
					GenerationUtils.forceFillAreaReversedWithBlockCube(world, pos.below(2).south(4).west(7).getX(), pos.below(2).south(4).west(7).getY(), pos.below(2).south(4).west(7).getZ(), pos.below(2).south(2).west(7).getX(), pos.below(2).south(2).west(7).getY(), pos.below(2).south(2).west(7).getZ(), Blocks.AIR.defaultBlockState());
					break;
				case 3:
					this.setPoiseLogUnsafe(world, pos, rand);
					this.setPoiseLogUnsafe(world, pos.west(), rand);
					this.setPoiseLogUnsafe(world, pos.west(2), rand);

					this.setPoiseLogUnsafe(world, pos.west(3).south(), rand);
					this.setPoiseLogUnsafe(world, pos.west(4).south(), rand);

					this.setPoiseLogUnsafe(world, pos.below().west(5).south(2), rand);
					this.setPoiseLogUnsafe(world, pos.below().west(5).south(3), rand);

					this.setPoiseLogUnsafe(world, pos.below(2).west(6).south(4), rand);
					this.setPoiseLogUnsafe(world, pos.below(2).west(6).south(5), rand);
					this.setPoiseLogUnsafe(world, pos.below(2).west(6).south(6), rand);

					this.setPoiseLogUnsafe(world, pos.below(3).west(5).south(7), rand);
					this.setPoiseLogUnsafe(world, pos.below(3).west(4).south(8), rand);
					this.setPoiseLogUnsafe(world, pos.below(3).west(3).south(8), rand);
					this.setPoiseLogUnsafe(world, pos.below(3).west(2).south(8), rand);

					this.setPoiseLogUnsafe(world, pos.below(2).west().south(7), rand);
					this.setPoiseLogUnsafe(world, pos.below(2).south(7), rand);

					this.setPoiseLogUnsafe(world, pos.below().east().south(6), rand);
					this.setPoiseLogUnsafe(world, pos.below().east().south(5), rand);

					this.setPoiseLogUnsafe(world, pos.east(2).south(4), rand);
					this.setPoiseLogUnsafe(world, pos.east(2).south(3), rand);
					this.setPoiseLogUnsafe(world, pos.east(2).south(2), rand);
					this.setPoiseLogUnsafe(world, pos.east().south(), rand);

					//Air
					GenerationUtils.forceFillAreaWithBlockCube(world, pos.west(2).south().getX(), pos.west(2).south().getY(), pos.west(2).south().getZ(), pos.south().getX(), pos.south().getY(), pos.south().getZ(), Blocks.AIR.defaultBlockState());
					GenerationUtils.forceFillAreaWithBlockCube(world, pos.below().west(4).south(2).getX(), pos.below().west(4).south(2).getY(), pos.below().west(4).south(2).getZ(), pos.east().south(4).getX(), pos.east().south(4).getY(), pos.east().south(4).getZ(), Blocks.AIR.defaultBlockState());
					GenerationUtils.forceFillAreaWithBlockCube(world, pos.below(2).west(5).south(4).getX(), pos.below(2).west(5).south(4).getY(), pos.below(2).west(5).south(4).getZ(), pos.below().south(6).getX(), pos.below().south(6).getY(), pos.below().south(6).getZ(), Blocks.AIR.defaultBlockState());
					GenerationUtils.forceFillAreaWithBlockCube(world, pos.below(2).west(4).south(7).getX(), pos.below(2).west(4).south(7).getY(), pos.below(2).west(4).south(7).getZ(), pos.below(2).west(2).south(7).getX(), pos.below(2).west(2).south(7).getY(), pos.below(2).west(2).south(7).getZ(), Blocks.AIR.defaultBlockState());
					break;
			}
		} else {
			switch (variant) {
				case 0:
					this.setPoiseLogUnsafe(world, pos.below(6).north(8).east(8), rand);

					this.setPoiseLogUnsafe(world, pos.below(6).north(9).east(7), rand);

					this.setPoiseLogUnsafe(world, pos.below(7).north(10).east(6), rand);

					this.setPoiseLogUnsafe(world, pos.below(8).north(11).east(6), rand);
					this.setPoiseLogUnsafe(world, pos.below(9).north(11).east(6), rand);

					this.setPoiseLogUnsafe(world, pos.below(10).north(11).east(7), rand);

					this.setPoiseLogUnsafe(world, pos.below(11).north(10).east(8), rand);
					this.setPoiseLogUnsafe(world, pos.below(11).north(9).east(9), rand);

					this.setPoiseLogUnsafe(world, pos.below(10).north(8).east(10), rand);

					this.setPoiseLogUnsafe(world, pos.below(9).north(7).east(10), rand);
					this.setPoiseLogUnsafe(world, pos.below(8).north(7).east(10), rand);

					this.setPoiseLogUnsafe(world, pos.below(7).north(8).east(9), rand);

					//Air
					world.setBlock(pos.below(7).north(8).east(8), Blocks.AIR.defaultBlockState(), 2);
					world.setBlock(pos.below(7).north(9).east(7), Blocks.AIR.defaultBlockState(), 2);

					world.setBlock(pos.below(8).north(8).east(9), Blocks.AIR.defaultBlockState(), 2);
					world.setBlock(pos.below(8).north(9).east(8), Blocks.AIR.defaultBlockState(), 2);
					world.setBlock(pos.below(8).north(10).east(7), Blocks.AIR.defaultBlockState(), 2);
					world.setBlock(pos.below(9).north(8).east(9), Blocks.AIR.defaultBlockState(), 2);
					world.setBlock(pos.below(9).north(9).east(8), Blocks.AIR.defaultBlockState(), 2);
					world.setBlock(pos.below(9).north(10).east(7), Blocks.AIR.defaultBlockState(), 2);

					world.setBlock(pos.below(10).north(9).east(9), Blocks.AIR.defaultBlockState(), 2);
					world.setBlock(pos.below(10).north(10).east(8), Blocks.AIR.defaultBlockState(), 2);

					world.setBlock(pos.below(8).north(10).east(6), Blocks.AIR.defaultBlockState(), 2);
					world.setBlock(pos.below(10).north(10).east(7), Blocks.AIR.defaultBlockState(), 2);
				case 1:
					this.setPoiseLogUnsafe(world, pos.below(6).east(8).south(8), rand);

					this.setPoiseLogUnsafe(world, pos.below(6).east(9).south(7), rand);

					this.setPoiseLogUnsafe(world, pos.below(7).east(10).south(6), rand);

					this.setPoiseLogUnsafe(world, pos.below(8).east(11).south(6), rand);
					this.setPoiseLogUnsafe(world, pos.below(9).east(11).south(6), rand);

					this.setPoiseLogUnsafe(world, pos.below(10).east(11).south(7), rand);

					this.setPoiseLogUnsafe(world, pos.below(11).east(10).south(8), rand);
					this.setPoiseLogUnsafe(world, pos.below(11).east(9).south(9), rand);

					this.setPoiseLogUnsafe(world, pos.below(10).east(8).south(10), rand);

					this.setPoiseLogUnsafe(world, pos.below(9).east(7).south(10), rand);
					this.setPoiseLogUnsafe(world, pos.below(8).east(7).south(10), rand);

					this.setPoiseLogUnsafe(world, pos.below(7).east(8).south(9), rand);

					//Air
					world.setBlock(pos.below(7).east(8).south(8), Blocks.AIR.defaultBlockState(), 2);
					world.setBlock(pos.below(7).east(9).south(7), Blocks.AIR.defaultBlockState(), 2);

					world.setBlock(pos.below(8).east(8).south(9), Blocks.AIR.defaultBlockState(), 2);
					world.setBlock(pos.below(8).east(9).south(8), Blocks.AIR.defaultBlockState(), 2);
					world.setBlock(pos.below(8).east(10).south(7), Blocks.AIR.defaultBlockState(), 2);
					world.setBlock(pos.below(9).east(8).south(9), Blocks.AIR.defaultBlockState(), 2);
					world.setBlock(pos.below(9).east(9).south(8), Blocks.AIR.defaultBlockState(), 2);
					world.setBlock(pos.below(9).east(10).south(7), Blocks.AIR.defaultBlockState(), 2);

					world.setBlock(pos.below(10).east(9).south(9), Blocks.AIR.defaultBlockState(), 2);
					world.setBlock(pos.below(10).east(10).south(8), Blocks.AIR.defaultBlockState(), 2);

					world.setBlock(pos.below(8).east(10).south(6), Blocks.AIR.defaultBlockState(), 2);
					world.setBlock(pos.below(10).east(10).south(7), Blocks.AIR.defaultBlockState(), 2);
				case 2:
					this.setPoiseLogUnsafe(world, pos.below(6).south(8).west(8), rand);

					this.setPoiseLogUnsafe(world, pos.below(6).south(9).west(7), rand);

					this.setPoiseLogUnsafe(world, pos.below(7).south(10).west(6), rand);

					this.setPoiseLogUnsafe(world, pos.below(8).south(11).west(6), rand);
					this.setPoiseLogUnsafe(world, pos.below(9).south(11).west(6), rand);

					this.setPoiseLogUnsafe(world, pos.below(10).south(11).west(7), rand);

					this.setPoiseLogUnsafe(world, pos.below(11).south(10).west(8), rand);
					this.setPoiseLogUnsafe(world, pos.below(11).south(9).west(9), rand);

					this.setPoiseLogUnsafe(world, pos.below(10).south(8).west(10), rand);

					this.setPoiseLogUnsafe(world, pos.below(9).south(7).west(10), rand);
					this.setPoiseLogUnsafe(world, pos.below(8).south(7).west(10), rand);

					this.setPoiseLogUnsafe(world, pos.below(7).south(8).west(9), rand);

					//Air
					world.setBlock(pos.below(7).south(8).west(8), Blocks.AIR.defaultBlockState(), 2);
					world.setBlock(pos.below(7).south(9).west(7), Blocks.AIR.defaultBlockState(), 2);

					world.setBlock(pos.below(8).south(8).west(9), Blocks.AIR.defaultBlockState(), 2);
					world.setBlock(pos.below(8).south(9).west(8), Blocks.AIR.defaultBlockState(), 2);
					world.setBlock(pos.below(8).south(10).west(7), Blocks.AIR.defaultBlockState(), 2);
					world.setBlock(pos.below(9).south(8).west(9), Blocks.AIR.defaultBlockState(), 2);
					world.setBlock(pos.below(9).south(9).west(8), Blocks.AIR.defaultBlockState(), 2);
					world.setBlock(pos.below(9).south(10).west(7), Blocks.AIR.defaultBlockState(), 2);

					world.setBlock(pos.below(10).south(9).west(9), Blocks.AIR.defaultBlockState(), 2);
					world.setBlock(pos.below(10).south(10).west(8), Blocks.AIR.defaultBlockState(), 2);

					world.setBlock(pos.below(8).south(10).west(6), Blocks.AIR.defaultBlockState(), 2);
					world.setBlock(pos.below(10).south(10).west(7), Blocks.AIR.defaultBlockState(), 2);
				case 3:
					this.setPoiseLogUnsafe(world, pos.below(6).west(8).north(8), rand);

					this.setPoiseLogUnsafe(world, pos.below(6).west(9).north(7), rand);

					this.setPoiseLogUnsafe(world, pos.below(7).west(10).north(6), rand);

					this.setPoiseLogUnsafe(world, pos.below(8).west(11).north(6), rand);
					this.setPoiseLogUnsafe(world, pos.below(9).west(11).north(6), rand);

					this.setPoiseLogUnsafe(world, pos.below(10).west(11).north(7), rand);

					this.setPoiseLogUnsafe(world, pos.below(11).west(10).north(8), rand);
					this.setPoiseLogUnsafe(world, pos.below(11).west(9).north(9), rand);

					this.setPoiseLogUnsafe(world, pos.below(10).west(8).north(10), rand);

					this.setPoiseLogUnsafe(world, pos.below(9).west(7).north(10), rand);
					this.setPoiseLogUnsafe(world, pos.below(8).west(7).north(10), rand);

					this.setPoiseLogUnsafe(world, pos.below(7).west(8).north(9), rand);

					//Air
					world.setBlock(pos.below(7).west(8).north(8), Blocks.AIR.defaultBlockState(), 2);
					world.setBlock(pos.below(7).west(9).north(7), Blocks.AIR.defaultBlockState(), 2);

					world.setBlock(pos.below(8).west(8).north(9), Blocks.AIR.defaultBlockState(), 2);
					world.setBlock(pos.below(8).west(9).north(8), Blocks.AIR.defaultBlockState(), 2);
					world.setBlock(pos.below(8).west(10).north(7), Blocks.AIR.defaultBlockState(), 2);
					world.setBlock(pos.below(9).west(8).north(9), Blocks.AIR.defaultBlockState(), 2);
					world.setBlock(pos.below(9).west(9).north(8), Blocks.AIR.defaultBlockState(), 2);
					world.setBlock(pos.below(9).west(10).north(7), Blocks.AIR.defaultBlockState(), 2);

					world.setBlock(pos.below(10).west(9).north(9), Blocks.AIR.defaultBlockState(), 2);
					world.setBlock(pos.below(10).west(10).north(8), Blocks.AIR.defaultBlockState(), 2);

					world.setBlock(pos.below(8).west(10).north(6), Blocks.AIR.defaultBlockState(), 2);
					world.setBlock(pos.below(10).west(10).north(7), Blocks.AIR.defaultBlockState(), 2);
			}
		}
	}

	private void buildGround(LevelAccessor world, BlockPos pos, RandomSource rand) {
		BlockPos origin = pos.below();
		for (int x = origin.getX() - 13; x <= origin.getX() + 13; x++) {
			for (int z = origin.getZ() - 2; z <= origin.getZ() + 2; z++) {
				world.setBlock(new BlockPos(x, origin.getY(), z), Blocks.END_STONE.defaultBlockState(), 2);
				world.setBlock(new BlockPos(x, origin.getY() - 1, z), Blocks.END_STONE.defaultBlockState(), 2);
			}
		}
		for (int x = origin.getX() - 12; x <= origin.getX() + 12; x++) {
			for (int z = origin.getZ() - 5; z <= origin.getZ() + 5; z++) {
				world.setBlock(new BlockPos(x, origin.getY(), z), Blocks.END_STONE.defaultBlockState(), 2);
				world.setBlock(new BlockPos(x, origin.getY() - 1, z), Blocks.END_STONE.defaultBlockState(), 2);
			}
		}
		for (int x = origin.getX() - 11; x <= origin.getX() + 11; x++) {
			for (int z = origin.getZ() - 7; z <= origin.getZ() + 7; z++) {
				world.setBlock(new BlockPos(x, origin.getY(), z), Blocks.END_STONE.defaultBlockState(), 2);
				world.setBlock(new BlockPos(x, origin.getY() - 1, z), Blocks.END_STONE.defaultBlockState(), 2);
			}
		}
		for (int x = origin.getX() - 10; x <= origin.getX() + 10; x++) {
			for (int z = origin.getZ() - 8; z <= origin.getZ() + 8; z++) {
				world.setBlock(new BlockPos(x, origin.getY(), z), Blocks.END_STONE.defaultBlockState(), 2);
				world.setBlock(new BlockPos(x, origin.getY() - 1, z), Blocks.END_STONE.defaultBlockState(), 2);
			}
		}
		for (int x = origin.getX() - 9; x <= origin.getX() + 9; x++) {
			for (int z = origin.getZ() - 9; z <= origin.getZ() + 9; z++) {
				world.setBlock(new BlockPos(x, origin.getY(), z), Blocks.END_STONE.defaultBlockState(), 2);
				world.setBlock(new BlockPos(x, origin.getY() - 1, z), Blocks.END_STONE.defaultBlockState(), 2);
			}
		}
		for (int x = origin.getX() - 8; x <= origin.getX() + 8; x++) {
			for (int z = origin.getZ() - 10; z <= origin.getZ() + 10; z++) {
				world.setBlock(new BlockPos(x, origin.getY(), z), Blocks.END_STONE.defaultBlockState(), 2);
				world.setBlock(new BlockPos(x, origin.getY() - 1, z), Blocks.END_STONE.defaultBlockState(), 2);
			}
		}
		for (int x = origin.getX() - 7; x <= origin.getX() + 7; x++) {
			for (int z = origin.getZ() - 11; z <= origin.getZ() + 11; z++) {
				world.setBlock(new BlockPos(x, origin.getY(), z), Blocks.END_STONE.defaultBlockState(), 2);
				world.setBlock(new BlockPos(x, origin.getY() - 1, z), Blocks.END_STONE.defaultBlockState(), 2);
			}
		}
		for (int x = origin.getX() - 5; x <= origin.getX() + 5; x++) {
			for (int z = origin.getZ() - 12; z <= origin.getZ() + 12; z++) {
				world.setBlock(new BlockPos(x, origin.getY(), z), Blocks.END_STONE.defaultBlockState(), 2);
				world.setBlock(new BlockPos(x, origin.getY() - 1, z), Blocks.END_STONE.defaultBlockState(), 2);
			}
		}
		for (int x = 0; x <= 4; x++) {
			world.setBlock(origin.below().north(13).east(2).west(x), Blocks.END_STONE.defaultBlockState(), 2);
			world.setBlock(origin.north(13).east(2).west(x), Blocks.END_STONE.defaultBlockState(), 2);
			world.setBlock(origin.below().south(13).east(2).west(x), Blocks.END_STONE.defaultBlockState(), 2);
			world.setBlock(origin.south(13).east(2).west(x), Blocks.END_STONE.defaultBlockState(), 2);
		}
	}

	private void buildPoiseHanger(LevelAccessor world, BlockPos pos, RandomSource rand, int direction, boolean corner) {
		if (!corner) {
			switch (direction) {
				case 0:
					this.setPoiseLogHighProb(world, pos, rand);
					this.setPoiseLogHighProb(world, pos.above().south(), rand);
					this.setPoiseLogHighProb(world, pos.above().south(2), rand);

					this.setPoiseCluster(world, pos.above(2).south(), rand);
					this.setPoiseCluster(world, pos.above(2).south(2), rand);
					this.setPoiseCluster(world, pos.above(2).south(3), rand);
					this.setPoiseCluster(world, pos.above(2).south(2).east(), rand);
					this.setPoiseCluster(world, pos.above(2).south(2).west(), rand);

					this.setPoiseCluster(world, pos.south(), rand);
					this.setPoiseCluster(world, pos.south(2), rand);
					this.setPoiseCluster(world, pos.south(3), rand);
					this.setPoiseCluster(world, pos.south(2).east(), rand);
					this.setPoiseCluster(world, pos.south(2).west(), rand);
					this.setPoiseCluster(world, pos.above().south(), rand);
					this.setPoiseCluster(world, pos.above().south(3), rand);
					this.setPoiseCluster(world, pos.above().south(4), rand);

					this.setPoiseCluster(world, pos.above().south(3).east(), rand);
					this.setPoiseCluster(world, pos.above().south(3).west(), rand);

					this.setPoiseCluster(world, pos.above().south(2).east(2), rand);
					this.setPoiseCluster(world, pos.above().south(2).west(2), rand);

					this.setPoiseCluster(world, pos.above().south().east(), rand);
					this.setPoiseCluster(world, pos.above().south().west(), rand);
					break;
				case 1:
					this.setPoiseLogHighProb(world, pos, rand);
					this.setPoiseLogHighProb(world, pos.above().west(), rand);
					this.setPoiseLogHighProb(world, pos.above().west(2), rand);

					this.setPoiseCluster(world, pos.above(2).west(), rand);
					this.setPoiseCluster(world, pos.above(2).west(2), rand);
					this.setPoiseCluster(world, pos.above(2).west(3), rand);
					this.setPoiseCluster(world, pos.above(2).west(2).south(), rand);
					this.setPoiseCluster(world, pos.above(2).west(2).north(), rand);

					this.setPoiseCluster(world, pos.west(), rand);
					this.setPoiseCluster(world, pos.west(2), rand);
					this.setPoiseCluster(world, pos.west(3), rand);
					this.setPoiseCluster(world, pos.west(2).south(), rand);
					this.setPoiseCluster(world, pos.west(2).north(), rand);
					this.setPoiseCluster(world, pos.above().west(), rand);
					this.setPoiseCluster(world, pos.above().west(3), rand);
					this.setPoiseCluster(world, pos.above().west(4), rand);

					this.setPoiseCluster(world, pos.above().west(3).south(), rand);
					this.setPoiseCluster(world, pos.above().west(3).north(), rand);

					this.setPoiseCluster(world, pos.above().west(2).south(2), rand);
					this.setPoiseCluster(world, pos.above().west(2).north(2), rand);

					this.setPoiseCluster(world, pos.above().west().south(), rand);
					this.setPoiseCluster(world, pos.above().west().north(), rand);
					break;
				case 2:
					this.setPoiseLogHighProb(world, pos, rand);
					this.setPoiseLogHighProb(world, pos.above().north(), rand);
					this.setPoiseLogHighProb(world, pos.above().north(2), rand);

					this.setPoiseCluster(world, pos.above(2).north(), rand);
					this.setPoiseCluster(world, pos.above(2).north(2), rand);
					this.setPoiseCluster(world, pos.above(2).north(3), rand);
					this.setPoiseCluster(world, pos.above(2).north(2).west(), rand);
					this.setPoiseCluster(world, pos.above(2).north(2).east(), rand);

					this.setPoiseCluster(world, pos.north(), rand);
					this.setPoiseCluster(world, pos.north(2), rand);
					this.setPoiseCluster(world, pos.north(3), rand);
					this.setPoiseCluster(world, pos.north(2).west(), rand);
					this.setPoiseCluster(world, pos.north(2).east(), rand);
					this.setPoiseCluster(world, pos.above().north(), rand);
					this.setPoiseCluster(world, pos.above().north(3), rand);
					this.setPoiseCluster(world, pos.above().north(4), rand);

					this.setPoiseCluster(world, pos.above().north(3).west(), rand);
					this.setPoiseCluster(world, pos.above().north(3).east(), rand);

					this.setPoiseCluster(world, pos.above().north(2).west(2), rand);
					this.setPoiseCluster(world, pos.above().north(2).east(2), rand);

					this.setPoiseCluster(world, pos.above().north().west(), rand);
					this.setPoiseCluster(world, pos.above().north().east(), rand);
					break;
				case 3:
					this.setPoiseLogHighProb(world, pos, rand);
					this.setPoiseLogHighProb(world, pos.above().east(), rand);
					this.setPoiseLogHighProb(world, pos.above().east(2), rand);

					this.setPoiseCluster(world, pos.above(2).east(), rand);
					this.setPoiseCluster(world, pos.above(2).east(2), rand);
					this.setPoiseCluster(world, pos.above(2).east(3), rand);
					this.setPoiseCluster(world, pos.above(2).east(2).north(), rand);
					this.setPoiseCluster(world, pos.above(2).east(2).south(), rand);

					this.setPoiseCluster(world, pos.east(), rand);
					this.setPoiseCluster(world, pos.east(2), rand);
					this.setPoiseCluster(world, pos.east(3), rand);
					this.setPoiseCluster(world, pos.east(2).north(), rand);
					this.setPoiseCluster(world, pos.east(2).south(), rand);
					this.setPoiseCluster(world, pos.above().east(), rand);
					this.setPoiseCluster(world, pos.above().east(3), rand);
					this.setPoiseCluster(world, pos.above().east(4), rand);

					this.setPoiseCluster(world, pos.above().east(3).north(), rand);
					this.setPoiseCluster(world, pos.above().east(3).south(), rand);

					this.setPoiseCluster(world, pos.above().east(2).north(2), rand);
					this.setPoiseCluster(world, pos.above().east(2).south(2), rand);

					this.setPoiseCluster(world, pos.above().east().north(), rand);
					this.setPoiseCluster(world, pos.above().east().south(), rand);
					break;
			}
		} else {
			switch (direction) {
				case 0:
					this.setPoiseLogHighProb(world, pos, rand);
					this.setPoiseLogHighProb(world, pos.above(), rand);
					this.setPoiseLogHighProb(world, pos.above().south(), rand);
					this.setPoiseLogHighProb(world, pos.above().south().west(), rand);

					this.setPoiseCluster(world, pos.above(2).south().west(), rand);
					this.setPoiseCluster(world, pos.above(2).south(2).west(), rand);
					this.setPoiseCluster(world, pos.above(2).west(), rand);
					this.setPoiseCluster(world, pos.above(2).south().west(2), rand);
					this.setPoiseCluster(world, pos.above(2).south(), rand);

					this.setPoiseCluster(world, pos.above().south(2).west(), rand);
					this.setPoiseCluster(world, pos.above().west(), rand);
					this.setPoiseCluster(world, pos.above().south().west(2), rand);
					this.setPoiseCluster(world, pos.above().south(), rand);

					this.setPoiseCluster(world, pos.south(2).west(), rand);
					this.setPoiseCluster(world, pos.west(), rand);
					this.setPoiseCluster(world, pos.south().west(2), rand);
					this.setPoiseCluster(world, pos.south(), rand);

					this.setPoiseCluster(world, pos.south().west(), rand);
					this.setPoiseCluster(world, pos.above().south(2).west(2), rand);
					this.setPoiseCluster(world, pos.above().south(2), rand);
					this.setPoiseCluster(world, pos.above().west(2), rand);

					this.setPoiseCluster(world, pos.above().south(3).west(), rand);
					this.setPoiseCluster(world, pos.above().north().west(), rand);
					this.setPoiseCluster(world, pos.above().south().west(3), rand);
					this.setPoiseCluster(world, pos.above().south().east(), rand);
					break;
				case 1:
					this.setPoiseLogHighProb(world, pos, rand);
					this.setPoiseLogHighProb(world, pos.above(), rand);
					this.setPoiseLogHighProb(world, pos.above().south(), rand);
					this.setPoiseLogHighProb(world, pos.above().south().east(), rand);

					this.setPoiseCluster(world, pos.above(2).south().east(), rand);
					this.setPoiseCluster(world, pos.above(2).south(2).east(), rand);
					this.setPoiseCluster(world, pos.above(2).east(), rand);
					this.setPoiseCluster(world, pos.above(2).south().east(2), rand);
					this.setPoiseCluster(world, pos.above(2).south(), rand);

					this.setPoiseCluster(world, pos.above().south(2).east(), rand);
					this.setPoiseCluster(world, pos.above().east(), rand);
					this.setPoiseCluster(world, pos.above().south().east(2), rand);
					this.setPoiseCluster(world, pos.above().south(), rand);

					this.setPoiseCluster(world, pos.south(2).east(), rand);
					this.setPoiseCluster(world, pos.east(), rand);
					this.setPoiseCluster(world, pos.south().east(2), rand);
					this.setPoiseCluster(world, pos.south(), rand);

					this.setPoiseCluster(world, pos.south().east(), rand);
					this.setPoiseCluster(world, pos.above().south(2).east(2), rand);
					this.setPoiseCluster(world, pos.above().south(2), rand);
					this.setPoiseCluster(world, pos.above().east(2), rand);

					this.setPoiseCluster(world, pos.above().south(3).east(), rand);
					this.setPoiseCluster(world, pos.above().east().east(), rand);
					this.setPoiseCluster(world, pos.above().south().east(3), rand);
					this.setPoiseCluster(world, pos.above().south().west(), rand);
					break;
				case 2:
					this.setPoiseLogHighProb(world, pos, rand);
					this.setPoiseLogHighProb(world, pos.above(), rand);
					this.setPoiseLogHighProb(world, pos.above().north(), rand);
					this.setPoiseLogHighProb(world, pos.above().north().west(), rand);

					this.setPoiseCluster(world, pos.above(2).north().west(), rand);
					this.setPoiseCluster(world, pos.above(2).north(2).west(), rand);
					this.setPoiseCluster(world, pos.above(2).west(), rand);
					this.setPoiseCluster(world, pos.above(2).north().west(2), rand);
					this.setPoiseCluster(world, pos.above(2).north(), rand);

					this.setPoiseCluster(world, pos.above().north(2).west(), rand);
					this.setPoiseCluster(world, pos.above().west(), rand);
					this.setPoiseCluster(world, pos.above().north().west(2), rand);
					this.setPoiseCluster(world, pos.above().north(), rand);

					this.setPoiseCluster(world, pos.north(2).west(), rand);
					this.setPoiseCluster(world, pos.west(), rand);
					this.setPoiseCluster(world, pos.north().west(2), rand);
					this.setPoiseCluster(world, pos.north(), rand);

					this.setPoiseCluster(world, pos.north().west(), rand);
					this.setPoiseCluster(world, pos.above().north(2).west(2), rand);
					this.setPoiseCluster(world, pos.above().north(2), rand);
					this.setPoiseCluster(world, pos.above().west(2), rand);

					this.setPoiseCluster(world, pos.above().north(3).west(), rand);
					this.setPoiseCluster(world, pos.above().west().west(), rand);
					this.setPoiseCluster(world, pos.above().north().west(3), rand);
					this.setPoiseCluster(world, pos.above().north().east(), rand);
					break;
				case 3:
					this.setPoiseLogHighProb(world, pos, rand);
					this.setPoiseLogHighProb(world, pos.above(), rand);
					this.setPoiseLogHighProb(world, pos.above().north(), rand);
					this.setPoiseLogHighProb(world, pos.above().north().east(), rand);

					this.setPoiseCluster(world, pos.above(2).north().east(), rand);
					this.setPoiseCluster(world, pos.above(2).north(2).east(), rand);
					this.setPoiseCluster(world, pos.above(2).east(), rand);
					this.setPoiseCluster(world, pos.above(2).north().east(2), rand);
					this.setPoiseCluster(world, pos.above(2).north(), rand);

					this.setPoiseCluster(world, pos.above().north(2).east(), rand);
					this.setPoiseCluster(world, pos.above().east(), rand);
					this.setPoiseCluster(world, pos.above().north().east(2), rand);
					this.setPoiseCluster(world, pos.above().north(), rand);

					this.setPoiseCluster(world, pos.north(2).east(), rand);
					this.setPoiseCluster(world, pos.east(), rand);
					this.setPoiseCluster(world, pos.north().east(2), rand);
					this.setPoiseCluster(world, pos.north(), rand);

					this.setPoiseCluster(world, pos.north().east(), rand);
					this.setPoiseCluster(world, pos.above().north(2).east(2), rand);
					this.setPoiseCluster(world, pos.above().north(2), rand);
					this.setPoiseCluster(world, pos.above().east(2), rand);

					this.setPoiseCluster(world, pos.above().north(3).east(), rand);
					this.setPoiseCluster(world, pos.above().east().east(), rand);
					this.setPoiseCluster(world, pos.above().north().east(3), rand);
					this.setPoiseCluster(world, pos.above().north().west(), rand);
					break;
			}
		}
	}

	private boolean isViableDomeArea(LevelAccessor world, BlockPos pos) {
		return GenerationUtils.isAreaReplacable(world, pos.north(13).west(13).getX(), pos.north(13).west(13).getY(), pos.north(13).west(13).getZ(), pos.above(16).south(13).east(13).getX(), pos.above(16).south(13).east(13).getY(), pos.above(16).south(13).east(13).getZ());
	}

	private boolean isGroundViable(LevelAccessor world, BlockPos pos, RandomSource rand) {
		for (int xx = pos.north(13).west(13).getX(); xx <= pos.south(13).east(13).getX(); xx++) {
			for (int zz = pos.north(13).west(13).getZ(); zz <= pos.south(13).east(13).getZ(); zz++) {
				if (!isProperBlock(world, new BlockPos(xx, pos.getY(), zz))) {
					return false;
				}
			}
		}
		return true;
	}

	private boolean isProperBlock(LevelAccessor world, BlockPos pos) {
		return world.getBlockState(pos).getBlock() == Blocks.END_STONE || world.getBlockState(pos).getBlock() == EEBlocks.POISMOSS.get() || world.getBlockState(pos).getBlock() == EEBlocks.EUMUS.get() || world.getBlockState(pos).getBlock() == EEBlocks.EUMUS_POISMOSS.get();
	}

	private void placePoismossAt(LevelAccessor world, LevelSimulatedRW reader, BlockPos pos) {
		BlockPos blockpos = pos.above();
		if (world.getBlockState(blockpos).getBlock() == Blocks.AIR) {
			BlockState newGround = EEBlocks.POISMOSS.get().defaultBlockState();
			world.setBlock(blockpos, newGround, 2);
		}
	}

	private void setPoiseCluster(LevelAccessor world, BlockPos pos, RandomSource rand) {
		if (world.getBlockState(pos).getMaterial().isReplaceable()) {
			world.setBlock(pos, EEBlocks.POISE_CLUSTER.get().defaultBlockState(), 2);
		}
	}

	private void setPoiseLogHighProb(LevelAccessor world, BlockPos pos, RandomSource rand) {
		BlockState logState = rand.nextFloat() <= 0.35F ? EEBlocks.POISE_STEM.get().defaultBlockState() : EEBlocks.GLOWING_POISE_STEM.get().defaultBlockState();
		if (world.getBlockState(pos).getMaterial().isReplaceable()) {
			world.setBlock(pos, logState, 2);
		}
	}

	private void setPoiseLog(LevelAccessor world, BlockPos pos, RandomSource rand) {
		BlockState logState = rand.nextFloat() <= 0.90F ? EEBlocks.POISE_STEM.get().defaultBlockState() : EEBlocks.GLOWING_POISE_STEM.get().defaultBlockState();
		if (world.getBlockState(pos).getMaterial().isReplaceable()) {
			world.setBlock(pos, logState, 2);
		}
	}

	private void setPoiseLogUnsafe(LevelAccessor world, BlockPos pos, RandomSource rand) {
		BlockState logState = rand.nextFloat() <= 0.90F ? EEBlocks.POISE_STEM.get().defaultBlockState() : EEBlocks.GLOWING_POISE_STEM.get().defaultBlockState();
		world.setBlock(pos, logState, 2);
	}

	private void setPoismoss(LevelAccessor world, BlockPos pos) {
		world.setBlock(pos, EEBlocks.POISMOSS.get().defaultBlockState(), 2);
	}

}
