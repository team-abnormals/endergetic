package com.minecraftabnormals.endergetic.common.world.features;

import com.minecraftabnormals.endergetic.core.registry.EEBlocks;
import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

import java.util.Random;

/**
 * @author - SmellyModder(Luke Tonon)
 */
public class PoiseClusterFeature extends Feature<NoFeatureConfig> {
	private static final BlockState GLOWING_POISE_LOG = EEBlocks.GLOWING_POISE_WOOD.get().defaultBlockState();
	private static final BlockState POISE_CLUSTER = EEBlocks.POISE_CLUSTER.get().defaultBlockState();

	public PoiseClusterFeature(Codec<NoFeatureConfig> configFactoryIn) {
		super(configFactoryIn);
	}

	@Override
	public boolean place(ISeedReader world, ChunkGenerator generator, Random rand, BlockPos pos, NoFeatureConfig config) {
		if (world.getBlockState(pos).getMaterial().isReplaceable() && world.getBlockState(pos.below()).getBlock() == EEBlocks.POISMOSS.get()) {
			this.createGlob(rand.nextInt(12), world, pos, rand);
			return true;
		}
		return false;
	}

	private void createGlob(int variation, IWorld world, BlockPos pos, Random rand) {
		this.setBlockIfReplacable(world, pos, GLOWING_POISE_LOG);
		this.setBlockIfReplacable(world, pos.north(), POISE_CLUSTER);
		this.setBlockIfReplacable(world, pos.east(), POISE_CLUSTER);
		this.setBlockIfReplacable(world, pos.south(), POISE_CLUSTER);
		this.setBlockIfReplacable(world, pos.west(), POISE_CLUSTER);
		this.setBlockIfReplacable(world, pos.above(), POISE_CLUSTER);

		this.setBlockIfReplacable(world, pos.north().above(), POISE_CLUSTER);
		this.setBlockIfReplacable(world, pos.east().above(), POISE_CLUSTER);
		this.setBlockIfReplacable(world, pos.south().above(), POISE_CLUSTER);
		this.setBlockIfReplacable(world, pos.west().above(), POISE_CLUSTER);

		this.setBlockIfReplacable(world, pos.north(2), POISE_CLUSTER);
		this.setBlockIfReplacable(world, pos.east(2), POISE_CLUSTER);
		this.setBlockIfReplacable(world, pos.south(2), POISE_CLUSTER);
		this.setBlockIfReplacable(world, pos.west(2), POISE_CLUSTER);

		this.setBlockIfReplacable(world, pos.north().east(), POISE_CLUSTER);
		this.setBlockIfReplacable(world, pos.north().west(), POISE_CLUSTER);
		this.setBlockIfReplacable(world, pos.south().east(), POISE_CLUSTER);
		this.setBlockIfReplacable(world, pos.south().west(), POISE_CLUSTER);

		if (variation <= 2) {
			this.setBlockIfReplacable(world, pos.above().above(), POISE_CLUSTER);
		} else if (variation <= 7) {
			if (rand.nextInt(2) == 1) this.setBlockIfReplacable(world, pos.north().west().above(), POISE_CLUSTER);
			if (rand.nextInt(2) == 1) this.setBlockIfReplacable(world, pos.north().east().above(), POISE_CLUSTER);
			if (rand.nextInt(2) == 1) this.setBlockIfReplacable(world, pos.south().west().above(), POISE_CLUSTER);
			if (rand.nextInt(2) == 1) this.setBlockIfReplacable(world, pos.south().east().above(), POISE_CLUSTER);

			if (rand.nextInt(2) == 1) this.setBlockIfReplacable(world, pos.north(2).west(), POISE_CLUSTER);
			if (rand.nextInt(2) == 1) this.setBlockIfReplacable(world, pos.north(2).east(), POISE_CLUSTER);
			if (rand.nextInt(2) == 1) this.setBlockIfReplacable(world, pos.south(2).west(), POISE_CLUSTER);
			if (rand.nextInt(2) == 1) this.setBlockIfReplacable(world, pos.west(2).east(), POISE_CLUSTER);
		} else if (variation <= 10) {
			int i = rand.nextInt(4);
			if (i == 0) {
				this.setBlockIfReplacable(world, pos.north().above().east(), POISE_CLUSTER);
				this.setBlockIfReplacable(world, pos.north().east(2), POISE_CLUSTER);
				this.setBlockIfReplacable(world, pos.north(2).east(), POISE_CLUSTER);
				this.setBlockIfReplacable(world, pos.north(2).east(2), POISE_CLUSTER);
			} else if (i == 1) {
				this.setBlockIfReplacable(world, pos.east().above().south(), POISE_CLUSTER);
				this.setBlockIfReplacable(world, pos.east().south(2), POISE_CLUSTER);
				this.setBlockIfReplacable(world, pos.east(2).south(), POISE_CLUSTER);
				this.setBlockIfReplacable(world, pos.east(2).south(2), POISE_CLUSTER);
			} else if (i == 2) {
				this.setBlockIfReplacable(world, pos.south().above().west(), POISE_CLUSTER);
				this.setBlockIfReplacable(world, pos.south().west(2), POISE_CLUSTER);
				this.setBlockIfReplacable(world, pos.south(2).west(), POISE_CLUSTER);
				this.setBlockIfReplacable(world, pos.south(2).west(2), POISE_CLUSTER);
			} else if (i == 3) {
				this.setBlockIfReplacable(world, pos.west().above().north(), POISE_CLUSTER);
				this.setBlockIfReplacable(world, pos.west().north(2), POISE_CLUSTER);
				this.setBlockIfReplacable(world, pos.west(2).north(), POISE_CLUSTER);
				this.setBlockIfReplacable(world, pos.west(2).north(2), POISE_CLUSTER);
			}
		}
	}

	private void setBlockIfReplacable(IWorld world, BlockPos pos, BlockState newState) {
		if (world.getBlockState(pos).getMaterial().isReplaceable() && world.getBlockState(pos.above()).getBlock() != EEBlocks.TALL_POISE_BUSH.get() && world.getBlockState(pos.below()).getBlock() != EEBlocks.TALL_POISE_BUSH.get()) {
			world.setBlock(pos, newState, 2);
		}
	}
}
