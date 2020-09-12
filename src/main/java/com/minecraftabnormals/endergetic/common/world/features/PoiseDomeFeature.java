package com.minecraftabnormals.endergetic.common.world.features;

import java.util.Random;

import com.minecraftabnormals.endergetic.api.util.GenerationUtils;
import com.minecraftabnormals.endergetic.core.registry.EEBlocks;
import com.mojang.serialization.Codec;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.IWorldGenerationReader;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.StructureManager;

public class PoiseDomeFeature extends Feature<NoFeatureConfig> {

	public PoiseDomeFeature(Codec<NoFeatureConfig> configFactory) {
		super(configFactory);
	}

	@Override
	public boolean func_230362_a_(ISeedReader world, StructureManager manager, ChunkGenerator generator, Random rand, BlockPos pos, NoFeatureConfig config) {
		if (world.getBlockState(pos.down()).getBlock() == Blocks.END_STONE && isViableDomeArea(world, pos) && this.isGroundViable(world, pos.down(3), rand)) {
			this.buildDomeBase(world, pos, rand);
			this.buildDome(world, pos, rand);
			this.buildGround(world, pos, rand);
		}
		return false;
	}
	
	private void buildDomeBase(IWorld world, BlockPos pos, Random rand) {
		BlockPos origin = pos;
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
			this.setPoiseLog(world, origin.north(12).west(3).up(y), rand);
		}
		this.setPoiseLog(world, origin.north(12).west(2).up(4), rand);
		for (int y = 1; y <= 3; y++) {
			this.setPoiseLog(world, origin.north(12).east(3).up(y), rand);
		}
		this.setPoiseLog(world, origin.north(12).east(2).up(4), rand);
		
		this.setPoiseLog(world, origin.north(12).west().up(5), rand);
		this.setPoiseLog(world, origin.north(12).up(5), rand);
		this.setPoiseLog(world, origin.north(12).east().up(5), rand);
		if (rand.nextFloat() <= 0.25F) {
			if (rand.nextInt(3) == 0) {
				this.setPoiseLog(world, origin.north(12).up(6), rand);
			} else {
				if (rand.nextInt(2) == 0) {
					this.setPoiseLog(world, origin.north(12).west().up(6), rand);
				} else {
					this.setPoiseLog(world, origin.north(12).east().up(6), rand);
				}
			}
		}
		
		for (int y = 1; y <= 3; y++) {
			this.setPoiseLog(world, origin.east(12).north(3).up(y), rand);
		}
		this.setPoiseLog(world, origin.east(12).north(2).up(4), rand);
		for (int y = 1; y <= 3; y++) {
			this.setPoiseLog(world, origin.east(12).south(3).up(y), rand);
		}
		this.setPoiseLog(world, origin.east(12).south(2).up(4), rand);
		
		this.setPoiseLog(world, origin.east(12).north().up(5), rand);
		this.setPoiseLog(world, origin.east(12).up(5), rand);
		this.setPoiseLog(world, origin.east(12).south().up(5), rand);
		if (rand.nextFloat() <= 0.25F) {
			if (rand.nextInt(3) == 0) {
				this.setPoiseLog(world, origin.east(12).up(6), rand);
			} else {
				if (rand.nextInt(2) == 0) {
					this.setPoiseLog(world, origin.east(12).north().up(6), rand);
				} else {
					this.setPoiseLog(world, origin.east(12).south().up(6), rand);
				}
			}
		}
		
		for (int y = 1; y <= 3; y++) {
			this.setPoiseLog(world, origin.south(12).east(3).up(y), rand);
		}
		this.setPoiseLog(world, origin.south(12).east(2).up(4), rand);
		for (int y = 1; y <= 3; y++) {
			this.setPoiseLog(world, origin.south(12).west(3).up(y), rand);
		}
		this.setPoiseLog(world, origin.south(12).west(2).up(4), rand);
		
		this.setPoiseLog(world, origin.south(12).east().up(5), rand);
		this.setPoiseLog(world, origin.south(12).up(5), rand);
		this.setPoiseLog(world, origin.south(12).west().up(5), rand);
		if (rand.nextFloat() <= 0.25F) {
			if (rand.nextInt(3) == 0) {
				this.setPoiseLog(world, origin.south(12).up(6), rand);
			} else {
				if (rand.nextInt(2) == 0) {
					this.setPoiseLog(world, origin.south(12).east().up(6), rand);
				} else {
					this.setPoiseLog(world, origin.south(12).west().up(6), rand);
				}
			}
		}
		
		for (int y = 1; y <= 3; y++) {
			this.setPoiseLog(world, origin.west(12).south(3).up(y), rand);
		}
		this.setPoiseLog(world, origin.west(12).south(2).up(4), rand);
		for (int y = 1; y <= 3; y++) {
			this.setPoiseLog(world, origin.west(12).north(3).up(y), rand);
		}
		this.setPoiseLog(world, origin.west(12).north(2).up(4), rand);
		
		this.setPoiseLog(world, origin.west(12).south().up(5), rand);
		this.setPoiseLog(world, origin.west(12).up(5), rand);
		this.setPoiseLog(world, origin.west(12).north().up(5), rand);
		if (rand.nextFloat() <= 0.25F) {
			if (rand.nextInt(3) == 0) {
				this.setPoiseLog(world, origin.west(12).up(6), rand);
			} else {
				if (rand.nextInt(2) == 0) {
					this.setPoiseLog(world, origin.west(12).south().up(6), rand);
				} else {
					this.setPoiseLog(world, origin.west(12).north().up(6), rand);
				}
			}
		}
		this.buildPoismossCircle(world, world, rand, origin);
	}
	
	private void buildDome(IWorld world, BlockPos origin, Random rand) {
		/*
		 * North
		 */
		{
		GenerationUtils.fillAreaWithBlockCube(world, origin.up().north(12).west(5).getX(), origin.up().north(12).west(5).getY(), origin.up().north(12).west(5).getZ(), origin.up(4).north(12).west(3).getX(), origin.up(4).north(12).west(3).getY(), origin.up(4).north(12).west(3).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up().north(12).east(3).getX(), origin.up().north(12).east(3).getY(), origin.up().north(12).east(3).getZ(), origin.up(4).north(12).east(5).getX(), origin.up(4).north(12).east(5).getY(), origin.up(4).north(12).east(5).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(5).north(12).west(4).getX(), origin.up(5).north(12).west(4).getY(), origin.up(5).north(12).west(4).getZ(), origin.up(6).north(12).east(4).getX(), origin.up(6).north(12).east(4).getY(), origin.up(6).north(12).east(4).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(7).north(12).west(2).getX(), origin.up(7).north(12).west(2).getY(), origin.up(7).north(12).west(2).getZ(), origin.up(7).north(12).east(2).getX(), origin.up(7).north(12).east(2).getY(), origin.up(7).north(12).east(2).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		}
		
		{
		GenerationUtils.fillAreaWithBlockCube(world, origin.up().north(11).west(7).getX(), origin.up().north(11).west(7).getY(), origin.up().north(11).west(7).getZ(), origin.up(5).north(11).west(6).getX(), origin.up(5).north(11).west(6).getY(), origin.up(5).north(11).west(6).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(5).north(11).west(6).getX(), origin.up(5).north(11).west(6).getY(), origin.up(5).north(11).west(6).getZ(), origin.up(7).north(11).west(5).getX(), origin.up(7).north(11).west(5).getY(), origin.up(7).north(11).west(5).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(7).north(11).west(5).getX(), origin.up(7).north(11).west(5).getY(), origin.up(7).north(11).west(5).getZ(), origin.up(8).north(11).west(3).getX(), origin.up(8).north(11).west(3).getY(), origin.up(8).north(11).west(3).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		
		GenerationUtils.fillAreaWithBlockCube(world, origin.up().north(11).east(6).getX(), origin.up().north(11).east(6).getY(), origin.up().north(11).east(6).getZ(), origin.up(5).north(11).east(7).getX(), origin.up(5).north(11).east(7).getY(), origin.up(5).north(11).east(7).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(5).north(11).east(5).getX(), origin.up(5).north(11).east(5).getY(), origin.up(5).north(11).east(5).getZ(), origin.up(7).north(11).east(6).getX(), origin.up(7).north(11).east(6).getY(), origin.up(7).north(11).east(6).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(7).north(11).east(3).getX(), origin.up(7).north(11).east(3).getY(), origin.up(7).north(11).east(3).getZ(), origin.up(8).north(11).east(5).getX(), origin.up(8).north(11).east(5).getY(), origin.up(8).north(11).east(5).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(8).north(11).west(3).getX(), origin.up(8).north(11).west(3).getY(), origin.up(8).north(11).west(3).getZ(), origin.up(9).north(11).east(3).getX(), origin.up(9).north(11).east(3).getY(), origin.up(9).north(11).east(3).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(10).north(11).west().getX(), origin.up(10).north(11).west().getY(), origin.up(10).north(11).west().getZ(), origin.up(11).north(10).east().getX(), origin.up(10).north(11).east().getY(), origin.up(10).north(11).east().getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		}
		
		{
		GenerationUtils.fillAreaWithBlockCube(world, origin.up().north(10).west(8).getX(), origin.up().north(10).west(8).getY(), origin.up().north(10).west(8).getZ(), origin.up(6).north(10).west(8).getX(), origin.up(6).north(10).west(8).getY(), origin.up(6).north(10).west(8).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(6).north(10).west(7).getX(), origin.up(6).north(10).west(7).getY(), origin.up(6).north(10).west(7).getZ(), origin.up(8).north(10).west(7).getX(), origin.up(8).north(10).west(7).getY(), origin.up(8).north(10).west(7).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(8).north(10).west(6).getX(), origin.up(8).north(10).west(6).getY(), origin.up(8).north(10).west(6).getZ(), origin.up(9).north(10).west(6).getX(), origin.up(9).north(10).west(6).getY(), origin.up(9).north(10).west(6).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(9).north(10).west(6).getX(), origin.up(9).north(10).west(6).getY(), origin.up(9).north(10).west(6).getZ(), origin.up(9).north(10).west(4).getX(), origin.up(9).north(10).west(4).getY(), origin.up(9).north(10).west(4).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(10).north(10).west(4).getX(), origin.up(10).north(10).west(4).getY(), origin.up(10).north(10).west(4).getZ(), origin.up(10).north(10).west(2).getX(), origin.up(10).north(10).west(2).getY(), origin.up(10).north(10).west(2).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		
		GenerationUtils.fillAreaWithBlockCube(world, origin.up().north(10).east(8).getX(), origin.up().north(10).east(8).getY(), origin.up().north(10).east(8).getZ(), origin.up(6).north(10).east(8).getX(), origin.up(6).north(10).east(8).getY(), origin.up(6).north(10).east(8).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(6).north(10).east(7).getX(), origin.up(6).north(10).east(7).getY(), origin.up(6).north(10).east(7).getZ(), origin.up(8).north(10).east(7).getX(), origin.up(8).north(10).east(7).getY(), origin.up(8).north(10).east(7).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(8).north(10).east(6).getX(), origin.up(8).north(10).east(6).getY(), origin.up(8).north(10).east(6).getZ(), origin.up(9).north(10).east(6).getX(), origin.up(9).north(10).east(6).getY(), origin.up(9).north(10).east(6).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(9).north(10).east(4).getX(), origin.up(9).north(10).east(4).getY(), origin.up(9).north(10).east(4).getZ(), origin.up(9).north(10).east(6).getX(), origin.up(9).north(10).east(6).getY(), origin.up(9).north(10).east(6).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(10).north(10).east(2).getX(), origin.up(10).north(10).east(2).getY(), origin.up(10).north(10).east(2).getZ(), origin.up(10).north(10).east(4).getX(), origin.up(10).north(10).east(4).getY(), origin.up(10).north(10).east(4).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(11).north(10).west(2).getX(), origin.up(11).north(10).west(2).getY(), origin.up(11).north(10).west(2).getZ(), origin.up(11).north(10).east(2).getX(), origin.up(11).north(10).east(2).getY(), origin.up(11).north(10).east(2).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		}
		
		{
		GenerationUtils.fillAreaWithBlockCube(world, origin.up().north(9).west(9).getX(), origin.up().north(9).west(9).getY(), origin.up().north(9).west(9).getZ(), origin.up(6).north(9).west(9).getX(), origin.up(6).north(9).west(9).getY(), origin.up(6).north(9).west(9).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(7).north(9).west(8).getX(), origin.up(7).north(9).west(8).getY(), origin.up(7).north(9).west(8).getZ(), origin.up(8).north(9).west(8).getX(), origin.up(8).north(9).west(8).getY(), origin.up(8).north(9).west(8).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(9).north(9).west(7).getX(), origin.up(9).north(9).west(7).getY(), origin.up(9).north(9).west(7).getZ(), origin.up(10).north(9).west(7).getX(), origin.up(10).north(9).west(7).getY(), origin.up(10).north(9).west(7).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(10).north(9).west(7).getX(), origin.up(10).north(9).west(7).getY(), origin.up(10).north(9).west(7).getZ(), origin.up(10).north(9).west(5).getX(), origin.up(10).north(9).west(5).getY(), origin.up(10).north(9).west(5).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(11).north(9).west(5).getX(), origin.up(11).north(9).west(5).getY(), origin.up(11).north(9).west(5).getZ(), origin.up(11).north(9).west(3).getX(), origin.up(11).north(9).west(3).getY(), origin.up(11).north(9).west(3).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		
		GenerationUtils.fillAreaWithBlockCube(world, origin.up().north(9).east(9).getX(), origin.up().north(9).east(9).getY(), origin.up().north(9).east(9).getZ(), origin.up(6).north(9).east(9).getX(), origin.up(6).north(9).east(9).getY(), origin.up(6).north(9).east(9).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(7).north(9).east(8).getX(), origin.up(7).north(9).east(8).getY(), origin.up(7).north(9).east(8).getZ(), origin.up(8).north(9).east(8).getX(), origin.up(8).north(9).east(8).getY(), origin.up(8).north(9).east(8).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(9).north(9).east(7).getX(), origin.up(9).north(9).east(7).getY(), origin.up(9).north(9).east(7).getZ(), origin.up(10).north(9).east(7).getX(), origin.up(10).north(9).east(7).getY(), origin.up(10).north(9).east(7).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(10).north(9).east(5).getX(), origin.up(10).north(9).east(5).getY(), origin.up(10).north(9).east(5).getZ(), origin.up(10).north(9).east(7).getX(), origin.up(10).north(9).east(7).getY(), origin.up(10).north(9).east(7).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(11).north(9).east(3).getX(), origin.up(11).north(9).east(3).getY(), origin.up(11).north(9).east(3).getZ(), origin.up(11).north(9).east(5).getX(), origin.up(11).north(9).east(5).getY(), origin.up(11).north(9).east(5).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(12).north(9).west(3).getX(), origin.up(12).north(9).west(3).getY(), origin.up(12).north(9).west(3).getZ(), origin.up(12).north(9).east(3).getX(), origin.up(12).north(9).east(3).getY(), origin.up(12).north(9).east(3).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		}
		
		{
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(9).north(8).west(8).getX(), origin.up(9).north(8).west(8).getY(), origin.up(9).north(8).west(8).getZ(), origin.up(10).north(8).west(8).getX(), origin.up(10).north(8).west(8).getY(), origin.up(10).north(8).west(8).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(11).north(8).west(7).getX(), origin.up(11).north(8).west(7).getY(), origin.up(11).north(8).west(7).getZ(), origin.up(11).north(8).west(6).getX(), origin.up(11).north(8).west(6).getY(), origin.up(11).north(8).west(6).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(12).north(8).west(6).getX(), origin.up(12).north(8).west(6).getY(), origin.up(12).north(8).west(6).getZ(), origin.up(12).north(8).west(4).getX(), origin.up(12).north(8).west(4).getY(), origin.up(12).north(8).west(4).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(9).north(8).east(8).getX(), origin.up(9).north(8).east(8).getY(), origin.up(9).north(8).east(8).getZ(), origin.up(10).north(8).east(8).getX(), origin.up(10).north(8).east(8).getY(), origin.up(10).north(8).east(8).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(11).north(8).east(6).getX(), origin.up(11).north(8).east(6).getY(), origin.up(11).north(8).east(6).getZ(), origin.up(11).north(8).east(7).getX(), origin.up(11).north(8).east(7).getY(), origin.up(11).north(8).east(7).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(12).north(8).east(4).getX(), origin.up(12).north(8).east(4).getY(), origin.up(12).north(8).east(4).getZ(), origin.up(12).north(8).east(6).getX(), origin.up(12).north(8).east(6).getY(), origin.up(12).north(8).east(6).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(13).north(8).west(4).getX(), origin.up(13).north(8).west(4).getY(), origin.up(13).north(8).west(4).getZ(), origin.up(13).north(8).east(4).getX(), origin.up(13).north(8).east(4).getY(), origin.up(13).north(8).east(4).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		}
		
		{
		this.setPoiseCluster(world, origin.up(12).north(7).west(7), rand);
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(13).north(7).west(6).getX(), origin.up(13).north(7).west(6).getY(), origin.up(13).north(7).west(6).getZ(), origin.up(13).north(7).west(5).getX(), origin.up(13).north(7).west(5).getY(), origin.up(13).north(7).west(5).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		
		this.setPoiseCluster(world, origin.up(12).north(7).east(7), rand);
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(13).north(7).east(5).getX(), origin.up(13).north(7).east(5).getY(), origin.up(13).north(7).east(5).getZ(), origin.up(13).north(7).east(6).getX(), origin.up(13).north(7).east(6).getY(), origin.up(13).north(7).east(6).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(14).north(7).west(4).getX(), origin.up(14).north(7).west(4).getY(), origin.up(14).north(7).west(4).getZ(), origin.up(14).north(7).east(4).getX(), origin.up(14).north(7).east(4).getY(), origin.up(14).north(7).east(4).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		}
		
		{
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(14).north(6).west(6).getX(), origin.up(14).north(6).west(6).getY(), origin.up(14).north(6).west(6).getZ(), origin.up(14).north(6).west(3).getX(), origin.up(14).north(6).west(3).getY(), origin.up(14).north(6).west(3).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(14).north(6).east(3).getX(), origin.up(14).north(6).east(3).getY(), origin.up(14).north(6).east(3).getZ(), origin.up(14).north(6).east(6).getX(), origin.up(14).north(6).east(6).getY(), origin.up(14).north(6).east(6).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(15).north(6).west(3).getX(), origin.up(15).north(6).west(3).getY(), origin.up(15).north(6).west(3).getZ(), origin.up(14).north(6).east(3).getX(), origin.up(14).north(6).east(3).getY(), origin.up(14).north(6).east(3).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		}
		
		{
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(15).north(6).west(2).getX(), origin.up(15).north(6).west(2).getY(), origin.up(15).north(6).west(2).getZ(), origin.up(15).north(6).east(2).getX(), origin.up(15).north(6).east(2).getY(), origin.up(15).north(6).east(2).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(15).north(5).west(5).getX(), origin.up(15).north(5).west(5).getY(), origin.up(15).north(5).west(5).getZ(), origin.up(15).north(5).east(5).getX(), origin.up(15).north(5).east(5).getY(), origin.up(15).north(5).east(5).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(15).north(4).west(5).getX(), origin.up(15).north(4).west(5).getY(), origin.up(15).north(4).west(5).getZ(), origin.up(15).north(4).west(2).getX(), origin.up(15).north(4).west(2).getY(), origin.up(15).north(4).west(2).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(15).north(4).east(2).getX(), origin.up(15).north(4).east(2).getY(), origin.up(15).north(4).east(2).getZ(), origin.up(15).north(4).east(5).getX(), origin.up(15).north(4).east(5).getY(), origin.up(15).north(4).east(5).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(16).north(4).west().getX(), origin.up(16).north(4).west().getY(), origin.up(16).north(4).west().getZ(), origin.up(16).north(4).east().getX(), origin.up(16).north(4).east().getY(), origin.up(16).north(4).east().getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		}
		
		/*
		 * East
		 */
		{
		GenerationUtils.fillAreaWithBlockCube(world, origin.up().east(12).north(5).getX(), origin.up().east(12).north(5).getY(), origin.up().east(12).north(5).getZ(), origin.up(4).east(12).north(3).getX(), origin.up(4).east(12).north(3).getY(), origin.up(4).east(12).north(3).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up().east(12).south(3).getX(), origin.up().east(12).south(3).getY(), origin.up().east(12).south(3).getZ(), origin.up(4).east(12).south(5).getX(), origin.up(4).east(12).south(5).getY(), origin.up(4).east(12).south(5).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(5).east(12).north(4).getX(), origin.up(5).east(12).north(4).getY(), origin.up(5).east(12).north(4).getZ(), origin.up(6).east(12).south(4).getX(), origin.up(6).east(12).south(4).getY(), origin.up(6).east(12).south(4).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(7).east(12).north(2).getX(), origin.up(7).east(12).north(2).getY(), origin.up(7).east(12).north(2).getZ(), origin.up(7).east(12).south(2).getX(), origin.up(7).east(12).south(2).getY(), origin.up(7).east(12).south(2).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		}
		
		{
		GenerationUtils.fillAreaWithBlockCube(world, origin.up().east(11).north(7).getX(), origin.up().east(11).north(7).getY(), origin.up().east(11).north(7).getZ(), origin.up(5).east(11).north(6).getX(), origin.up(5).east(11).north(6).getY(), origin.up(5).east(11).north(6).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(5).east(11).north(6).getX(), origin.up(5).east(11).north(6).getY(), origin.up(5).east(11).north(6).getZ(), origin.up(7).east(11).north(5).getX(), origin.up(7).east(11).north(5).getY(), origin.up(7).east(11).north(5).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(7).east(11).north(5).getX(), origin.up(7).east(11).north(5).getY(), origin.up(7).east(11).north(5).getZ(), origin.up(8).east(11).north(3).getX(), origin.up(8).east(11).north(3).getY(), origin.up(8).east(11).north(3).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		
		GenerationUtils.fillAreaWithBlockCube(world, origin.up().east(11).south(6).getX(), origin.up().east(11).south(6).getY(), origin.up().east(11).south(6).getZ(), origin.up(5).east(11).south(7).getX(), origin.up(5).east(11).south(7).getY(), origin.up(5).east(11).south(7).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(5).east(11).south(5).getX(), origin.up(5).east(11).south(5).getY(), origin.up(5).east(11).south(5).getZ(), origin.up(7).east(11).south(6).getX(), origin.up(7).east(11).south(6).getY(), origin.up(7).east(11).south(6).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(7).east(11).south(3).getX(), origin.up(7).east(11).south(3).getY(), origin.up(7).east(11).south(3).getZ(), origin.up(8).east(11).south(5).getX(), origin.up(8).east(11).south(5).getY(), origin.up(8).east(11).south(5).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(8).east(11).north(3).getX(), origin.up(8).east(11).north(3).getY(), origin.up(8).east(11).north(3).getZ(), origin.up(9).east(11).south(3).getX(), origin.up(9).east(11).south(3).getY(), origin.up(9).east(11).south(3).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		this.setPoiseCluster(world, origin.up(10).east(11).north(), rand);
		this.setPoiseCluster(world, origin.up(10).east(11), rand);
		this.setPoiseCluster(world, origin.up(10).east(11).south(), rand);
		}
		
		{
		GenerationUtils.fillAreaWithBlockCube(world, origin.up().east(10).north(8).getX(), origin.up().east(10).north(8).getY(), origin.up().east(10).north(8).getZ(), origin.up(6).east(10).north(8).getX(), origin.up(6).east(10).north(8).getY(), origin.up(6).east(10).north(8).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(6).east(10).north(7).getX(), origin.up(6).east(10).north(7).getY(), origin.up(6).east(10).north(7).getZ(), origin.up(8).east(10).north(7).getX(), origin.up(8).east(10).north(7).getY(), origin.up(8).east(10).north(7).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(8).east(10).north(6).getX(), origin.up(8).east(10).north(6).getY(), origin.up(8).east(10).north(6).getZ(), origin.up(9).east(10).north(6).getX(), origin.up(9).east(10).north(6).getY(), origin.up(9).east(10).north(6).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(9).east(10).north(6).getX(), origin.up(9).east(10).north(6).getY(), origin.up(9).east(10).north(6).getZ(), origin.up(9).east(10).north(4).getX(), origin.up(9).east(10).north(4).getY(), origin.up(9).east(10).north(4).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(10).east(10).north(4).getX(), origin.up(10).east(10).north(4).getY(), origin.up(10).east(10).north(4).getZ(), origin.up(10).east(10).north(2).getX(), origin.up(10).east(10).north(2).getY(), origin.up(10).east(10).north(2).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
			
		GenerationUtils.fillAreaWithBlockCube(world, origin.up().east(10).south(8).getX(), origin.up().east(10).south(8).getY(), origin.up().east(10).south(8).getZ(), origin.up(6).east(10).south(8).getX(), origin.up(6).east(10).south(8).getY(), origin.up(6).east(10).south(8).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(6).east(10).south(7).getX(), origin.up(6).east(10).south(7).getY(), origin.up(6).east(10).south(7).getZ(), origin.up(8).east(10).south(7).getX(), origin.up(8).east(10).south(7).getY(), origin.up(8).east(10).south(7).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(8).east(10).south(6).getX(), origin.up(8).east(10).south(6).getY(), origin.up(8).east(10).south(6).getZ(), origin.up(9).east(10).south(6).getX(), origin.up(9).east(10).south(6).getY(), origin.up(9).east(10).south(6).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(9).east(10).south(4).getX(), origin.up(9).east(10).south(4).getY(), origin.up(9).east(10).south(4).getZ(), origin.up(9).east(10).south(6).getX(), origin.up(9).east(10).south(6).getY(), origin.up(9).east(10).south(6).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(10).east(10).south(2).getX(), origin.up(10).east(10).south(2).getY(), origin.up(10).east(10).south(2).getZ(), origin.up(10).east(10).south(4).getX(), origin.up(10).east(10).south(4).getY(), origin.up(10).east(10).south(4).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(11).east(10).north(2).getX(), origin.up(11).east(10).north(2).getY(), origin.up(11).east(10).north(2).getZ(), origin.up(11).east(10).south(2).getX(), origin.up(11).east(10).south(2).getY(), origin.up(11).east(10).south(2).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		}
		
		{
		GenerationUtils.fillAreaWithBlockCube(world, origin.up().east(9).north(9).getX(), origin.up().east(9).north(9).getY(), origin.up().east(9).north(9).getZ(), origin.up(6).east(9).north(9).getX(), origin.up(6).east(9).north(9).getY(), origin.up(6).east(9).north(9).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(7).east(9).north(8).getX(), origin.up(7).east(9).north(8).getY(), origin.up(7).east(9).north(8).getZ(), origin.up(8).east(9).north(8).getX(), origin.up(8).east(9).north(8).getY(), origin.up(8).east(9).north(8).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(9).east(9).north(7).getX(), origin.up(9).east(9).north(7).getY(), origin.up(9).east(9).north(7).getZ(), origin.up(10).east(9).north(7).getX(), origin.up(10).east(9).north(7).getY(), origin.up(10).east(9).north(7).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(10).east(9).north(7).getX(), origin.up(10).east(9).north(7).getY(), origin.up(10).east(9).north(7).getZ(), origin.up(10).east(9).north(5).getX(), origin.up(10).east(9).north(5).getY(), origin.up(10).east(9).north(5).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(11).east(9).north(5).getX(), origin.up(11).east(9).north(5).getY(), origin.up(11).east(9).north(5).getZ(), origin.up(11).east(9).north(3).getX(), origin.up(11).east(9).north(3).getY(), origin.up(11).east(9).north(3).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
				
		GenerationUtils.fillAreaWithBlockCube(world, origin.up().east(9).south(9).getX(), origin.up().east(9).south(9).getY(), origin.up().east(9).south(9).getZ(), origin.up(6).east(9).south(9).getX(), origin.up(6).east(9).south(9).getY(), origin.up(6).east(9).south(9).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(7).east(9).south(8).getX(), origin.up(7).east(9).south(8).getY(), origin.up(7).east(9).south(8).getZ(), origin.up(8).east(9).south(8).getX(), origin.up(8).east(9).south(8).getY(), origin.up(8).east(9).south(8).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(9).east(9).south(7).getX(), origin.up(9).east(9).south(7).getY(), origin.up(9).east(9).south(7).getZ(), origin.up(10).east(9).south(7).getX(), origin.up(10).east(9).south(7).getY(), origin.up(10).east(9).south(7).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(10).east(9).south(5).getX(), origin.up(10).east(9).south(5).getY(), origin.up(10).east(9).south(5).getZ(), origin.up(10).east(9).south(7).getX(), origin.up(10).east(9).south(7).getY(), origin.up(10).east(9).south(7).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(11).east(9).south(3).getX(), origin.up(11).east(9).south(3).getY(), origin.up(11).east(9).south(3).getZ(), origin.up(11).east(9).south(5).getX(), origin.up(11).east(9).south(5).getY(), origin.up(11).east(9).south(5).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
			
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(12).east(9).north(3).getX(), origin.up(12).east(9).north(3).getY(), origin.up(12).east(9).north(3).getZ(), origin.up(12).east(9).south(3).getX(), origin.up(12).east(9).south(3).getY(), origin.up(12).east(9).south(3).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		}
		
		{
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(9).east(8).north(8).getX(), origin.up(9).east(8).north(8).getY(), origin.up(9).east(8).north(8).getZ(), origin.up(10).east(8).north(8).getX(), origin.up(10).east(8).north(8).getY(), origin.up(10).east(8).north(8).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(11).east(8).north(7).getX(), origin.up(11).east(8).north(7).getY(), origin.up(11).east(8).north(7).getZ(), origin.up(11).east(8).north(6).getX(), origin.up(11).east(8).north(6).getY(), origin.up(11).east(8).north(6).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(12).east(8).north(6).getX(), origin.up(12).east(8).north(6).getY(), origin.up(12).east(8).north(6).getZ(), origin.up(12).east(8).north(4).getX(), origin.up(12).east(8).north(4).getY(), origin.up(12).east(8).north(4).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
					
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(9).east(8).south(8).getX(), origin.up(9).east(8).south(8).getY(), origin.up(9).east(8).south(8).getZ(), origin.up(10).east(8).south(8).getX(), origin.up(10).east(8).south(8).getY(), origin.up(10).east(8).south(8).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(11).east(8).south(6).getX(), origin.up(11).east(8).south(6).getY(), origin.up(11).east(8).south(6).getZ(), origin.up(11).east(8).south(7).getX(), origin.up(11).east(8).south(7).getY(), origin.up(11).east(8).south(7).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(12).east(8).south(4).getX(), origin.up(12).east(8).south(4).getY(), origin.up(12).east(8).south(4).getZ(), origin.up(12).east(8).south(6).getX(), origin.up(12).east(8).south(6).getY(), origin.up(12).east(8).south(6).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
					
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(13).east(8).north(4).getX(), origin.up(13).east(8).north(4).getY(), origin.up(13).east(8).north(4).getZ(), origin.up(13).east(8).south(4).getX(), origin.up(13).east(8).south(4).getY(), origin.up(13).east(8).south(4).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		}
		
		{
		this.setPoiseCluster(world, origin.up(12).east(7).north(7), rand);
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(13).east(7).north(6).getX(), origin.up(13).east(7).north(6).getY(), origin.up(13).east(7).north(6).getZ(), origin.up(13).east(7).north(5).getX(), origin.up(13).east(7).north(5).getY(), origin.up(13).east(7).north(5).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
			
		this.setPoiseCluster(world, origin.up(12).east(7).south(7), rand);
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(13).east(7).south(5).getX(), origin.up(13).east(7).south(5).getY(), origin.up(13).east(7).south(5).getZ(), origin.up(13).east(7).south(6).getX(), origin.up(13).east(7).south(6).getY(), origin.up(13).east(7).south(6).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
			
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(14).east(7).north(4).getX(), origin.up(14).east(7).north(4).getY(), origin.up(14).east(7).north(4).getZ(), origin.up(14).east(7).south(4).getX(), origin.up(14).east(7).south(4).getY(), origin.up(14).east(7).south(4).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		}
		
		{
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(14).east(6).north(6).getX(), origin.up(14).east(6).north(6).getY(), origin.up(14).east(6).north(6).getZ(), origin.up(14).east(6).north(3).getX(), origin.up(14).east(6).north(3).getY(), origin.up(14).east(6).north(3).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(14).east(6).south(3).getX(), origin.up(14).east(6).south(3).getY(), origin.up(14).east(6).south(3).getZ(), origin.up(14).east(6).south(6).getX(), origin.up(14).east(6).south(6).getY(), origin.up(14).east(6).south(6).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(15).east(6).north(3).getX(), origin.up(15).east(6).north(3).getY(), origin.up(15).east(6).north(3).getZ(), origin.up(14).east(6).south(3).getX(), origin.up(14).east(6).south(3).getY(), origin.up(14).east(7).south(3).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		}
		
		{
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(15).east(6).north(2).getX(), origin.up(15).east(6).north(2).getY(), origin.up(15).east(6).north(2).getZ(), origin.up(15).east(6).south(2).getX(), origin.up(15).east(6).south(2).getY(), origin.up(15).east(6).south(2).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(15).east(5).north(5).getX(), origin.up(15).east(5).north(5).getY(), origin.up(15).east(5).north(5).getZ(), origin.up(15).east(5).south(5).getX(), origin.up(15).east(5).south(5).getY(), origin.up(15).east(5).south(5).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(15).east(4).north(5).getX(), origin.up(15).east(4).north(5).getY(), origin.up(15).east(4).north(5).getZ(), origin.up(15).east(4).north(2).getX(), origin.up(15).east(4).north(2).getY(), origin.up(15).east(4).north(2).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(15).east(4).south(2).getX(), origin.up(15).east(4).south(2).getY(), origin.up(15).east(4).south(2).getZ(), origin.up(15).east(4).south(5).getX(), origin.up(15).east(4).south(5).getY(), origin.up(15).east(4).south(5).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(16).east(4).north().getX(), origin.up(16).east(4).north().getY(), origin.up(16).east(4).north().getZ(), origin.up(16).east(4).south().getX(), origin.up(16).east(4).south().getY(), origin.up(16).east(4).south().getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		}
		
		/*
		 * South
		 */
		{
		GenerationUtils.fillAreaWithBlockCube(world, origin.up().south(12).west(5).getX(), origin.up().south(12).west(5).getY(), origin.up().south(12).west(5).getZ(), origin.up(4).south(12).west(3).getX(), origin.up(4).south(12).west(3).getY(), origin.up(4).south(12).west(3).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up().south(12).east(3).getX(), origin.up().south(12).east(3).getY(), origin.up().south(12).east(3).getZ(), origin.up(4).south(12).east(5).getX(), origin.up(4).south(12).east(5).getY(), origin.up(4).south(12).east(5).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(5).south(12).west(4).getX(), origin.up(5).south(12).west(4).getY(), origin.up(5).south(12).west(4).getZ(), origin.up(6).south(12).east(4).getX(), origin.up(6).south(12).east(4).getY(), origin.up(6).south(12).east(4).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(7).south(12).west(2).getX(), origin.up(7).south(12).west(2).getY(), origin.up(7).south(12).west(2).getZ(), origin.up(7).south(12).east(2).getX(), origin.up(7).south(12).east(2).getY(), origin.up(7).south(12).east(2).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		}
		
		{
		GenerationUtils.fillAreaWithBlockCube(world, origin.up().south(11).west(7).getX(), origin.up().south(11).west(7).getY(), origin.up().south(11).west(7).getZ(), origin.up(5).south(11).west(6).getX(), origin.up(5).south(11).west(6).getY(), origin.up(5).south(11).west(6).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(5).south(11).west(6).getX(), origin.up(5).south(11).west(6).getY(), origin.up(5).south(11).west(6).getZ(), origin.up(7).south(11).west(5).getX(), origin.up(7).south(11).west(5).getY(), origin.up(7).south(11).west(5).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(7).south(11).west(5).getX(), origin.up(7).south(11).west(5).getY(), origin.up(7).south(11).west(5).getZ(), origin.up(8).south(11).west(3).getX(), origin.up(8).south(11).west(3).getY(), origin.up(8).south(11).west(3).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		
		GenerationUtils.fillAreaWithBlockCube(world, origin.up().south(11).east(6).getX(), origin.up().south(11).east(6).getY(), origin.up().south(11).east(6).getZ(), origin.up(5).south(11).east(7).getX(), origin.up(5).south(11).east(7).getY(), origin.up(5).south(11).east(7).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(5).south(11).east(5).getX(), origin.up(5).south(11).east(5).getY(), origin.up(5).south(11).east(5).getZ(), origin.up(7).south(11).east(6).getX(), origin.up(7).south(11).east(6).getY(), origin.up(7).south(11).east(6).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(7).south(11).east(3).getX(), origin.up(7).south(11).east(3).getY(), origin.up(7).south(11).east(3).getZ(), origin.up(8).south(11).east(5).getX(), origin.up(8).south(11).east(5).getY(), origin.up(8).south(11).east(5).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(8).south(11).west(3).getX(), origin.up(8).south(11).west(3).getY(), origin.up(8).south(11).west(3).getZ(), origin.up(9).south(11).east(3).getX(), origin.up(9).south(11).east(3).getY(), origin.up(9).south(11).east(3).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(10).south(11).west().getX(), origin.up(10).south(11).west().getY(), origin.up(10).south(11).west().getZ(), origin.up(11).south(10).east().getX(), origin.up(10).south(11).east().getY(), origin.up(10).south(11).east().getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		}
		
		{
		GenerationUtils.fillAreaWithBlockCube(world, origin.up().south(10).west(8).getX(), origin.up().south(10).west(8).getY(), origin.up().south(10).west(8).getZ(), origin.up(6).south(10).west(8).getX(), origin.up(6).south(10).west(8).getY(), origin.up(6).south(10).west(8).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(6).south(10).west(7).getX(), origin.up(6).south(10).west(7).getY(), origin.up(6).south(10).west(7).getZ(), origin.up(8).south(10).west(7).getX(), origin.up(8).south(10).west(7).getY(), origin.up(8).south(10).west(7).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(8).south(10).west(6).getX(), origin.up(8).south(10).west(6).getY(), origin.up(8).south(10).west(6).getZ(), origin.up(9).south(10).west(6).getX(), origin.up(9).south(10).west(6).getY(), origin.up(9).south(10).west(6).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(9).south(10).west(6).getX(), origin.up(9).south(10).west(6).getY(), origin.up(9).south(10).west(6).getZ(), origin.up(9).south(10).west(4).getX(), origin.up(9).south(10).west(4).getY(), origin.up(9).south(10).west(4).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(10).south(10).west(4).getX(), origin.up(10).south(10).west(4).getY(), origin.up(10).south(10).west(4).getZ(), origin.up(10).south(10).west(2).getX(), origin.up(10).south(10).west(2).getY(), origin.up(10).south(10).west(2).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
			
		GenerationUtils.fillAreaWithBlockCube(world, origin.up().south(10).east(8).getX(), origin.up().south(10).east(8).getY(), origin.up().south(10).east(8).getZ(), origin.up(6).south(10).east(8).getX(), origin.up(6).south(10).east(8).getY(), origin.up(6).south(10).east(8).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(6).south(10).east(7).getX(), origin.up(6).south(10).east(7).getY(), origin.up(6).south(10).east(7).getZ(), origin.up(8).south(10).east(7).getX(), origin.up(8).south(10).east(7).getY(), origin.up(8).south(10).east(7).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(8).south(10).east(6).getX(), origin.up(8).south(10).east(6).getY(), origin.up(8).south(10).east(6).getZ(), origin.up(9).south(10).east(6).getX(), origin.up(9).south(10).east(6).getY(), origin.up(9).south(10).east(6).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(9).south(10).east(4).getX(), origin.up(9).south(10).east(4).getY(), origin.up(9).south(10).east(4).getZ(), origin.up(9).south(10).east(6).getX(), origin.up(9).south(10).east(6).getY(), origin.up(9).south(10).east(6).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(10).south(10).east(2).getX(), origin.up(10).south(10).east(2).getY(), origin.up(10).south(10).east(2).getZ(), origin.up(10).south(10).east(4).getX(), origin.up(10).south(10).east(4).getY(), origin.up(10).south(10).east(4).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(11).south(10).west(2).getX(), origin.up(11).south(10).west(2).getY(), origin.up(11).south(10).west(2).getZ(), origin.up(11).south(10).east(2).getX(), origin.up(11).south(10).east(2).getY(), origin.up(11).south(10).east(2).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		}
		
		{
		GenerationUtils.fillAreaWithBlockCube(world, origin.up().south(9).west(9).getX(), origin.up().south(9).west(9).getY(), origin.up().south(9).west(9).getZ(), origin.up(6).south(9).west(9).getX(), origin.up(6).south(9).west(9).getY(), origin.up(6).south(9).west(9).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(7).south(9).west(8).getX(), origin.up(7).south(9).west(8).getY(), origin.up(7).south(9).west(8).getZ(), origin.up(8).south(9).west(8).getX(), origin.up(8).south(9).west(8).getY(), origin.up(8).south(9).west(8).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(9).south(9).west(7).getX(), origin.up(9).south(9).west(7).getY(), origin.up(9).south(9).west(7).getZ(), origin.up(10).south(9).west(7).getX(), origin.up(10).south(9).west(7).getY(), origin.up(10).south(9).west(7).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(10).south(9).west(7).getX(), origin.up(10).south(9).west(7).getY(), origin.up(10).south(9).west(7).getZ(), origin.up(10).south(9).west(5).getX(), origin.up(10).south(9).west(5).getY(), origin.up(10).south(9).west(5).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(11).south(9).west(5).getX(), origin.up(11).south(9).west(5).getY(), origin.up(11).south(9).west(5).getZ(), origin.up(11).south(9).west(3).getX(), origin.up(11).south(9).west(3).getY(), origin.up(11).south(9).west(3).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
				
		GenerationUtils.fillAreaWithBlockCube(world, origin.up().south(9).east(9).getX(), origin.up().south(9).east(9).getY(), origin.up().south(9).east(9).getZ(), origin.up(6).south(9).east(9).getX(), origin.up(6).south(9).east(9).getY(), origin.up(6).south(9).east(9).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(7).south(9).east(8).getX(), origin.up(7).south(9).east(8).getY(), origin.up(7).south(9).east(8).getZ(), origin.up(8).south(9).east(8).getX(), origin.up(8).south(9).east(8).getY(), origin.up(8).south(9).east(8).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(9).south(9).east(7).getX(), origin.up(9).south(9).east(7).getY(), origin.up(9).south(9).east(7).getZ(), origin.up(10).south(9).east(7).getX(), origin.up(10).south(9).east(7).getY(), origin.up(10).south(9).east(7).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(10).south(9).east(5).getX(), origin.up(10).south(9).east(5).getY(), origin.up(10).south(9).east(5).getZ(), origin.up(10).south(9).east(7).getX(), origin.up(10).south(9).east(7).getY(), origin.up(10).south(9).east(7).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(11).south(9).east(3).getX(), origin.up(11).south(9).east(3).getY(), origin.up(11).south(9).east(3).getZ(), origin.up(11).south(9).east(5).getX(), origin.up(11).south(9).east(5).getY(), origin.up(11).south(9).east(5).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
				
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(12).south(9).west(3).getX(), origin.up(12).south(9).west(3).getY(), origin.up(12).south(9).west(3).getZ(), origin.up(12).south(9).east(3).getX(), origin.up(12).south(9).east(3).getY(), origin.up(12).south(9).east(3).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		}
		
		{
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(9).south(8).west(8).getX(), origin.up(9).south(8).west(8).getY(), origin.up(9).south(8).west(8).getZ(), origin.up(10).south(8).west(8).getX(), origin.up(10).south(8).west(8).getY(), origin.up(10).south(8).west(8).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(11).south(8).west(7).getX(), origin.up(11).south(8).west(7).getY(), origin.up(11).south(8).west(7).getZ(), origin.up(11).south(8).west(6).getX(), origin.up(11).south(8).west(6).getY(), origin.up(11).south(8).west(6).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(12).south(8).west(6).getX(), origin.up(12).south(8).west(6).getY(), origin.up(12).south(8).west(6).getZ(), origin.up(12).south(8).west(4).getX(), origin.up(12).south(8).west(4).getY(), origin.up(12).south(8).west(4).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
						
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(9).south(8).east(8).getX(), origin.up(9).south(8).east(8).getY(), origin.up(9).south(8).east(8).getZ(), origin.up(10).south(8).east(8).getX(), origin.up(10).south(8).east(8).getY(), origin.up(10).south(8).east(8).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(11).south(8).east(6).getX(), origin.up(11).south(8).east(6).getY(), origin.up(11).south(8).east(6).getZ(), origin.up(11).south(8).east(7).getX(), origin.up(11).south(8).east(7).getY(), origin.up(11).south(8).east(7).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(12).south(8).east(4).getX(), origin.up(12).south(8).east(4).getY(), origin.up(12).south(8).east(4).getZ(), origin.up(12).south(8).east(6).getX(), origin.up(12).south(8).east(6).getY(), origin.up(12).south(8).east(6).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
						
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(13).south(8).west(4).getX(), origin.up(13).south(8).west(4).getY(), origin.up(13).south(8).west(4).getZ(), origin.up(13).south(8).east(4).getX(), origin.up(13).south(8).east(4).getY(), origin.up(13).south(8).east(4).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		}
		
		{
		this.setPoiseCluster(world, origin.up(12).south(7).west(7), rand);
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(13).south(7).west(6).getX(), origin.up(13).south(7).west(6).getY(), origin.up(13).south(7).west(6).getZ(), origin.up(13).south(7).west(5).getX(), origin.up(13).south(7).west(5).getY(), origin.up(13).south(7).west(5).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
				
		this.setPoiseCluster(world, origin.up(12).south(7).east(7), rand);
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(13).south(7).east(5).getX(), origin.up(13).south(7).east(5).getY(), origin.up(13).south(7).east(5).getZ(), origin.up(13).south(7).east(6).getX(), origin.up(13).south(7).east(6).getY(), origin.up(13).south(7).east(6).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
				
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(14).south(7).west(4).getX(), origin.up(14).south(7).west(4).getY(), origin.up(14).south(7).west(4).getZ(), origin.up(14).south(7).east(4).getX(), origin.up(14).south(7).east(4).getY(), origin.up(14).south(7).east(4).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		}
		
		{
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(14).south(6).west(6).getX(), origin.up(14).south(6).west(6).getY(), origin.up(14).south(6).west(6).getZ(), origin.up(14).south(6).west(3).getX(), origin.up(14).south(6).west(3).getY(), origin.up(14).south(6).west(3).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(14).south(6).east(3).getX(), origin.up(14).south(6).east(3).getY(), origin.up(14).south(6).east(3).getZ(), origin.up(14).south(6).east(6).getX(), origin.up(14).south(6).east(6).getY(), origin.up(14).south(6).east(6).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(15).south(6).west(3).getX(), origin.up(15).south(6).west(3).getY(), origin.up(15).south(6).west(3).getZ(), origin.up(14).south(6).east(3).getX(), origin.up(14).south(6).east(3).getY(), origin.up(14).south(7).east(3).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		}
		
		{
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(15).south(6).west(2).getX(), origin.up(15).south(6).west(2).getY(), origin.up(15).south(6).west(2).getZ(), origin.up(15).south(6).east(2).getX(), origin.up(15).south(6).east(2).getY(), origin.up(15).south(6).east(2).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(15).south(5).west(5).getX(), origin.up(15).south(5).west(5).getY(), origin.up(15).south(5).west(5).getZ(), origin.up(15).south(5).east(5).getX(), origin.up(15).south(5).east(5).getY(), origin.up(15).south(5).east(5).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
			
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(15).south(4).west(5).getX(), origin.up(15).south(4).west(5).getY(), origin.up(15).south(4).west(5).getZ(), origin.up(15).south(4).west(2).getX(), origin.up(15).south(4).west(2).getY(), origin.up(15).south(4).west(2).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(15).south(4).east(2).getX(), origin.up(15).south(4).east(2).getY(), origin.up(15).south(4).east(2).getZ(), origin.up(15).south(4).east(5).getX(), origin.up(15).south(4).east(5).getY(), origin.up(15).south(4).east(5).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(16).south(4).west().getX(), origin.up(16).south(4).west().getY(), origin.up(16).south(4).west().getZ(), origin.up(16).south(4).east().getX(), origin.up(16).south(4).east().getY(), origin.up(16).south(4).east().getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		}
		
		/*
		 * West
		 */
		{
		GenerationUtils.fillAreaWithBlockCube(world, origin.up().west(12).north(5).getX(), origin.up().west(12).north(5).getY(), origin.up().west(12).north(5).getZ(), origin.up(4).west(12).north(3).getX(), origin.up(4).west(12).north(3).getY(), origin.up(4).west(12).north(3).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up().west(12).south(3).getX(), origin.up().west(12).south(3).getY(), origin.up().west(12).south(3).getZ(), origin.up(4).west(12).south(5).getX(), origin.up(4).west(12).south(5).getY(), origin.up(4).west(12).south(5).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(5).west(12).north(4).getX(), origin.up(5).west(12).north(4).getY(), origin.up(5).west(12).north(4).getZ(), origin.up(6).west(12).south(4).getX(), origin.up(6).west(12).south(4).getY(), origin.up(6).west(12).south(4).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(7).west(12).north(2).getX(), origin.up(7).west(12).north(2).getY(), origin.up(7).west(12).north(2).getZ(), origin.up(7).west(12).south(2).getX(), origin.up(7).west(12).south(2).getY(), origin.up(7).west(12).south(2).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		}
		
		{
		GenerationUtils.fillAreaWithBlockCube(world, origin.up().west(11).north(7).getX(), origin.up().west(11).north(7).getY(), origin.up().west(11).north(7).getZ(), origin.up(5).west(11).north(6).getX(), origin.up(5).west(11).north(6).getY(), origin.up(5).west(11).north(6).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(5).west(11).north(6).getX(), origin.up(5).west(11).north(6).getY(), origin.up(5).west(11).north(6).getZ(), origin.up(7).west(11).north(5).getX(), origin.up(7).west(11).north(5).getY(), origin.up(7).west(11).north(5).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(7).west(11).north(5).getX(), origin.up(7).west(11).north(5).getY(), origin.up(7).west(11).north(5).getZ(), origin.up(8).west(11).north(3).getX(), origin.up(8).west(11).north(3).getY(), origin.up(8).west(11).north(3).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		
		GenerationUtils.fillAreaWithBlockCube(world, origin.up().west(11).south(6).getX(), origin.up().west(11).south(6).getY(), origin.up().west(11).south(6).getZ(), origin.up(5).west(11).south(7).getX(), origin.up(5).west(11).south(7).getY(), origin.up(5).west(11).south(7).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(5).west(11).south(5).getX(), origin.up(5).west(11).south(5).getY(), origin.up(5).west(11).south(5).getZ(), origin.up(7).west(11).south(6).getX(), origin.up(7).west(11).south(6).getY(), origin.up(7).west(11).south(6).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(7).west(11).south(3).getX(), origin.up(7).west(11).south(3).getY(), origin.up(7).west(11).south(3).getZ(), origin.up(8).west(11).south(5).getX(), origin.up(8).west(11).south(5).getY(), origin.up(8).west(11).south(5).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(8).west(11).north(3).getX(), origin.up(8).west(11).north(3).getY(), origin.up(8).west(11).north(3).getZ(), origin.up(9).west(11).south(3).getX(), origin.up(9).west(11).south(3).getY(), origin.up(9).west(11).south(3).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		this.setPoiseCluster(world, origin.up(10).west(11).north(), rand);
		this.setPoiseCluster(world, origin.up(10).west(11), rand);
		this.setPoiseCluster(world, origin.up(10).west(11).south(), rand);
		}
		
		{
		GenerationUtils.fillAreaWithBlockCube(world, origin.up().west(10).north(8).getX(), origin.up().west(10).north(8).getY(), origin.up().west(10).north(8).getZ(), origin.up(6).west(10).north(8).getX(), origin.up(6).west(10).north(8).getY(), origin.up(6).west(10).north(8).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(6).west(10).north(7).getX(), origin.up(6).west(10).north(7).getY(), origin.up(6).west(10).north(7).getZ(), origin.up(8).west(10).north(7).getX(), origin.up(8).west(10).north(7).getY(), origin.up(8).west(10).north(7).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(8).west(10).north(6).getX(), origin.up(8).west(10).north(6).getY(), origin.up(8).west(10).north(6).getZ(), origin.up(9).west(10).north(6).getX(), origin.up(9).west(10).north(6).getY(), origin.up(9).west(10).north(6).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(9).west(10).north(6).getX(), origin.up(9).west(10).north(6).getY(), origin.up(9).west(10).north(6).getZ(), origin.up(9).west(10).north(4).getX(), origin.up(9).west(10).north(4).getY(), origin.up(9).west(10).north(4).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(10).west(10).north(4).getX(), origin.up(10).west(10).north(4).getY(), origin.up(10).west(10).north(4).getZ(), origin.up(10).west(10).north(2).getX(), origin.up(10).west(10).north(2).getY(), origin.up(10).west(10).north(2).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
				
		GenerationUtils.fillAreaWithBlockCube(world, origin.up().west(10).south(8).getX(), origin.up().west(10).south(8).getY(), origin.up().west(10).south(8).getZ(), origin.up(6).west(10).south(8).getX(), origin.up(6).west(10).south(8).getY(), origin.up(6).west(10).south(8).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(6).west(10).south(7).getX(), origin.up(6).west(10).south(7).getY(), origin.up(6).west(10).south(7).getZ(), origin.up(8).west(10).south(7).getX(), origin.up(8).west(10).south(7).getY(), origin.up(8).west(10).south(7).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(8).west(10).south(6).getX(), origin.up(8).west(10).south(6).getY(), origin.up(8).west(10).south(6).getZ(), origin.up(9).west(10).south(6).getX(), origin.up(9).west(10).south(6).getY(), origin.up(9).west(10).south(6).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(9).west(10).south(4).getX(), origin.up(9).west(10).south(4).getY(), origin.up(9).west(10).south(4).getZ(), origin.up(9).west(10).south(6).getX(), origin.up(9).west(10).south(6).getY(), origin.up(9).west(10).south(6).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(10).west(10).south(2).getX(), origin.up(10).west(10).south(2).getY(), origin.up(10).west(10).south(2).getZ(), origin.up(10).west(10).south(4).getX(), origin.up(10).west(10).south(4).getY(), origin.up(10).west(10).south(4).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
			
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(11).west(10).north(2).getX(), origin.up(11).west(10).north(2).getY(), origin.up(11).west(10).north(2).getZ(), origin.up(11).west(10).south(2).getX(), origin.up(11).west(10).south(2).getY(), origin.up(11).west(10).south(2).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		}
		
		{
		GenerationUtils.fillAreaWithBlockCube(world, origin.up().west(9).north(9).getX(), origin.up().west(9).north(9).getY(), origin.up().west(9).north(9).getZ(), origin.up(6).west(9).north(9).getX(), origin.up(6).west(9).north(9).getY(), origin.up(6).west(9).north(9).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(7).west(9).north(8).getX(), origin.up(7).west(9).north(8).getY(), origin.up(7).west(9).north(8).getZ(), origin.up(8).west(9).north(8).getX(), origin.up(8).west(9).north(8).getY(), origin.up(8).west(9).north(8).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(9).west(9).north(7).getX(), origin.up(9).west(9).north(7).getY(), origin.up(9).west(9).north(7).getZ(), origin.up(10).west(9).north(7).getX(), origin.up(10).west(9).north(7).getY(), origin.up(10).west(9).north(7).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(10).west(9).north(7).getX(), origin.up(10).west(9).north(7).getY(), origin.up(10).west(9).north(7).getZ(), origin.up(10).west(9).north(5).getX(), origin.up(10).west(9).north(5).getY(), origin.up(10).west(9).north(5).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(11).west(9).north(5).getX(), origin.up(11).west(9).north(5).getY(), origin.up(11).west(9).north(5).getZ(), origin.up(11).west(9).north(3).getX(), origin.up(11).west(9).north(3).getY(), origin.up(11).west(9).north(3).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
			
		GenerationUtils.fillAreaWithBlockCube(world, origin.up().west(9).south(9).getX(), origin.up().west(9).south(9).getY(), origin.up().west(9).south(9).getZ(), origin.up(6).west(9).south(9).getX(), origin.up(6).west(9).south(9).getY(), origin.up(6).west(9).south(9).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(7).west(9).south(8).getX(), origin.up(7).west(9).south(8).getY(), origin.up(7).west(9).south(8).getZ(), origin.up(8).west(9).south(8).getX(), origin.up(8).west(9).south(8).getY(), origin.up(8).west(9).south(8).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(9).west(9).south(7).getX(), origin.up(9).west(9).south(7).getY(), origin.up(9).west(9).south(7).getZ(), origin.up(10).west(9).south(7).getX(), origin.up(10).west(9).south(7).getY(), origin.up(10).west(9).south(7).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(10).west(9).south(5).getX(), origin.up(10).west(9).south(5).getY(), origin.up(10).west(9).south(5).getZ(), origin.up(10).west(9).south(7).getX(), origin.up(10).west(9).south(7).getY(), origin.up(10).west(9).south(7).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(11).west(9).south(3).getX(), origin.up(11).west(9).south(3).getY(), origin.up(11).west(9).south(3).getZ(), origin.up(11).west(9).south(5).getX(), origin.up(11).west(9).south(5).getY(), origin.up(11).west(9).south(5).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(12).west(9).north(3).getX(), origin.up(12).west(9).north(3).getY(), origin.up(12).west(9).north(3).getZ(), origin.up(12).west(9).south(3).getX(), origin.up(12).west(9).south(3).getY(), origin.up(12).west(9).south(3).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		}
		
		{
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(9).west(8).north(8).getX(), origin.up(9).west(8).north(8).getY(), origin.up(9).west(8).north(8).getZ(), origin.up(10).west(8).north(8).getX(), origin.up(10).west(8).north(8).getY(), origin.up(10).west(8).north(8).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(11).west(8).north(7).getX(), origin.up(11).west(8).north(7).getY(), origin.up(11).west(8).north(7).getZ(), origin.up(11).west(8).north(6).getX(), origin.up(11).west(8).north(6).getY(), origin.up(11).west(8).north(6).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(12).west(8).north(6).getX(), origin.up(12).west(8).north(6).getY(), origin.up(12).west(8).north(6).getZ(), origin.up(12).west(8).north(4).getX(), origin.up(12).west(8).north(4).getY(), origin.up(12).west(8).north(4).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
			
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(9).west(8).south(8).getX(), origin.up(9).west(8).south(8).getY(), origin.up(9).west(8).south(8).getZ(), origin.up(10).west(8).south(8).getX(), origin.up(10).west(8).south(8).getY(), origin.up(10).west(8).south(8).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(11).west(8).south(6).getX(), origin.up(11).west(8).south(6).getY(), origin.up(11).west(8).south(6).getZ(), origin.up(11).west(8).south(7).getX(), origin.up(11).west(8).south(7).getY(), origin.up(11).west(8).south(7).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(12).west(8).south(4).getX(), origin.up(12).west(8).south(4).getY(), origin.up(12).west(8).south(4).getZ(), origin.up(12).west(8).south(6).getX(), origin.up(12).west(8).south(6).getY(), origin.up(12).west(8).south(6).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
			
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(13).west(8).north(4).getX(), origin.up(13).west(8).north(4).getY(), origin.up(13).west(8).north(4).getZ(), origin.up(13).west(8).south(4).getX(), origin.up(13).west(8).south(4).getY(), origin.up(13).west(8).south(4).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		}
		
		{
		this.setPoiseCluster(world, origin.up(12).west(7).north(7), rand);
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(13).west(7).north(6).getX(), origin.up(13).west(7).north(6).getY(), origin.up(13).west(7).north(6).getZ(), origin.up(13).west(7).north(5).getX(), origin.up(13).west(7).north(5).getY(), origin.up(13).west(7).north(5).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
					
		this.setPoiseCluster(world, origin.up(12).west(7).south(7), rand);
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(13).west(7).south(5).getX(), origin.up(13).west(7).south(5).getY(), origin.up(13).west(7).south(5).getZ(), origin.up(13).west(7).south(6).getX(), origin.up(13).west(7).south(6).getY(), origin.up(13).west(7).south(6).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
					
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(14).west(7).north(4).getX(), origin.up(14).west(7).north(4).getY(), origin.up(14).west(7).north(4).getZ(), origin.up(14).west(7).south(4).getX(), origin.up(14).west(7).south(4).getY(), origin.up(14).west(7).south(4).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		}
		
		{
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(14).west(6).north(6).getX(), origin.up(14).west(6).north(6).getY(), origin.up(14).west(6).north(6).getZ(), origin.up(14).west(6).north(3).getX(), origin.up(14).west(6).north(3).getY(), origin.up(14).west(6).north(3).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(14).west(6).south(3).getX(), origin.up(14).west(6).south(3).getY(), origin.up(14).west(6).south(3).getZ(), origin.up(14).west(6).south(6).getX(), origin.up(14).west(6).south(6).getY(), origin.up(14).west(6).south(6).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
			
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(15).west(6).north(3).getX(), origin.up(15).west(6).north(3).getY(), origin.up(15).west(6).north(3).getZ(), origin.up(14).west(6).south(3).getX(), origin.up(14).west(6).south(3).getY(), origin.up(14).west(7).south(3).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		}
		
		{
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(15).west(6).north(2).getX(), origin.up(15).west(6).north(2).getY(), origin.up(15).west(6).north(2).getZ(), origin.up(15).west(6).south(2).getX(), origin.up(15).west(6).south(2).getY(), origin.up(15).west(6).south(2).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(15).west(5).north(5).getX(), origin.up(15).west(5).north(5).getY(), origin.up(15).west(5).north(5).getZ(), origin.up(15).west(5).south(5).getX(), origin.up(15).west(5).south(5).getY(), origin.up(15).west(5).south(5).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
				
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(15).west(4).north(5).getX(), origin.up(15).west(4).north(5).getY(), origin.up(15).west(4).north(5).getZ(), origin.up(15).west(4).north(2).getX(), origin.up(15).west(4).north(2).getY(), origin.up(15).west(4).north(2).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(15).west(4).south(2).getX(), origin.up(15).west(4).south(2).getY(), origin.up(15).west(4).south(2).getZ(), origin.up(15).west(4).south(5).getX(), origin.up(15).west(4).south(5).getY(), origin.up(15).west(4).south(5).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(16).west(4).north().getX(), origin.up(16).west(4).north().getY(), origin.up(16).west(4).north().getZ(), origin.up(16).west(4).south().getX(), origin.up(16).west(4).south().getY(), origin.up(16).west(4).south().getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		}
		
		/*
		 * Top
		 */
		GenerationUtils.fillAreaWithBlockCube(world, origin.up(16).north(3).west(3).getX(), origin.up(16).north(3).west(3).getY(), origin.up(16).north(3).west(3).getZ(), origin.up(16).south(3).east(3).getX(), origin.up(16).south(3).east(3).getY(), origin.up(16).south(3).east(3).getZ(), EEBlocks.POISE_CLUSTER.get().getDefaultState());
		
		if (rand.nextFloat() <= 0.25F) {
			int i = rand.nextInt(4);
			this.buildDomeHole(world, origin.up(16), rand, true, i);
		}
		boolean[] doSide = {
			rand.nextBoolean(),
			rand.nextBoolean(),
			rand.nextBoolean(),
			rand.nextBoolean()
		};
		if (doSide[0]) {
			this.buildDomeHole(world, origin.up(16), rand, false, 0);
		}
		if (doSide[1]) {
			this.buildDomeHole(world, origin.up(16), rand, false, 1);
		}
		if (doSide[2]) {
			this.buildDomeHole(world, origin.up(16), rand, false, 2);
		}
		if (doSide[3]) {
			this.buildDomeHole(world, origin.up(16), rand, false, 3);
		}
		
		boolean[] doHangerSide = {
			rand.nextFloat() <= 0.25F ? true : false,
			rand.nextFloat() <= 0.25F ? true : false,
			rand.nextFloat() <= 0.25F ? true : false,
			rand.nextFloat() <= 0.25F ? true : false,
		};
		if (doHangerSide[0]) {
			if (rand.nextBoolean()) {
				this.buildPoiseHanger(world, origin.up(8).north(10).east(rand.nextInt(3)), rand, 0, false);
			} else {
				this.buildPoiseHanger(world, origin.up(8).north(10).west(rand.nextInt(3)), rand, 0, false);
			}
		}
		if (doHangerSide[1]) {
			if (rand.nextBoolean()) {
				this.buildPoiseHanger(world, origin.up(8).east(10).south(rand.nextInt(3)), rand, 1, false);
			} else {
				this.buildPoiseHanger(world, origin.up(8).east(10).north(rand.nextInt(3)), rand, 1, false);
			}
		}
		if (doHangerSide[2]) {
			if (rand.nextBoolean()) {
				this.buildPoiseHanger(world, origin.up(8).south(10).west(rand.nextInt(3)), rand, 2, false);
			} else {
				this.buildPoiseHanger(world, origin.up(8).south(10).east(rand.nextInt(3)), rand, 2, false);
			}
		}
		if (doHangerSide[3]) {
			if (rand.nextBoolean()) {
				this.buildPoiseHanger(world, origin.up(8).west(10).south(rand.nextInt(3)), rand, 3, false);
			} else {
				this.buildPoiseHanger(world, origin.up(8).west(10).north(rand.nextInt(3)), rand, 3, false);
			}
		}
		if (rand.nextFloat() <= 0.25F) {
			this.buildPoiseHanger(world, origin.up(5).north(9).east(8), rand, 0, true);
		}
		if (rand.nextFloat() <= 0.25F) {
			this.buildPoiseHanger(world, origin.up(5).north(8).west(9), rand, 1, true);
		}
		if (rand.nextFloat() <= 0.25F) {
			this.buildPoiseHanger(world, origin.up(5).south(8).east(9), rand, 2, true);
		}
		if (rand.nextFloat() <= 0.25F) {
			this.buildPoiseHanger(world, origin.up(5).south(9).west(8), rand, 3, true);
		}
	}
	
	private void buildPoismossCircle(IWorld world, IWorldGenerationReader reader, Random random, BlockPos pos) {
		this.placePoismossCircle(world, reader, pos.west().north());
		this.placePoismossCircle(world, reader, pos.east(2).north());
		this.placePoismossCircle(world, reader, pos.west().south(2));
		this.placePoismossCircle(world, reader, pos.east(2).south(2));

		for (int i = 0; i < 5; ++i) {
			int j = random.nextInt(64);
			int k = j % 8;
			int l = j / 8;
			if (k == 0 || k == 7 || l == 0 || l == 7) {
				this.placePoismossCircle(world, reader, pos.add(-3 + k, 0, -3 + l));
			}
		}
	}
	
	private void placePoismossCircle(IWorld world, IWorldGenerationReader reader, BlockPos center) {
		for (int i = -2; i <= 2; ++i) {
			for (int j = -2; j <= 2; ++j) {
				if (Math.abs(i) != 2 || Math.abs(j) != 2) {
					this.placePoismossAt(world, reader, center.add(i, 0, j));
				}
			}
		}
	}
	
	private void buildDomeHole(IWorld world, BlockPos pos, Random rand, boolean top, int variant) {
		if (top) {
			switch (variant) {
				case 0:
					this.setPoiseLogUnsafe(world, pos, rand);
					this.setPoiseLogUnsafe(world, pos.north(), rand);
					this.setPoiseLogUnsafe(world, pos.north(2), rand);
					
					this.setPoiseLogUnsafe(world, pos.north(3).east(), rand);
					this.setPoiseLogUnsafe(world, pos.north(4).east(), rand);
					
					this.setPoiseLogUnsafe(world, pos.down().north(5).east(2), rand);
					this.setPoiseLogUnsafe(world, pos.down().north(5).east(3), rand);
					
					this.setPoiseLogUnsafe(world, pos.down(2).north(6).east(4), rand);
					this.setPoiseLogUnsafe(world, pos.down(2).north(6).east(5), rand);
					this.setPoiseLogUnsafe(world, pos.down(2).north(6).east(6), rand);
					
					this.setPoiseLogUnsafe(world, pos.down(3).north(5).east(7), rand);
					this.setPoiseLogUnsafe(world, pos.down(3).north(4).east(8), rand);
					this.setPoiseLogUnsafe(world, pos.down(3).north(3).east(8), rand);
					this.setPoiseLogUnsafe(world, pos.down(3).north(2).east(8), rand);
					
					this.setPoiseLogUnsafe(world, pos.down(2).north().east(7), rand);
					this.setPoiseLogUnsafe(world, pos.down(2).east(7), rand);
					
					this.setPoiseLogUnsafe(world, pos.down().south().east(6), rand);
					this.setPoiseLogUnsafe(world, pos.down().south().east(5), rand);
					
					this.setPoiseLogUnsafe(world, pos.south(2).east(4), rand);
					this.setPoiseLogUnsafe(world, pos.south(2).east(3), rand);
					this.setPoiseLogUnsafe(world, pos.south(2).east(2), rand);
					this.setPoiseLogUnsafe(world, pos.south().east(), rand);
					
					//Air
					GenerationUtils.forceFillAreaWithBlockCube(world, pos.north(2).east().getX(), pos.north(2).east().getY(), pos.north(2).east().getZ(), pos.east().getX(), pos.east().getY(), pos.east().getZ(), Blocks.AIR.getDefaultState());
					GenerationUtils.forceFillAreaWithBlockCube(world, pos.down().north(4).east(2).getX(), pos.down().north(4).east(2).getY(), pos.down().north(4).east(2).getZ(), pos.south().east(4).getX(), pos.south().east(4).getY(), pos.south().east(4).getZ(), Blocks.AIR.getDefaultState());
					GenerationUtils.forceFillAreaWithBlockCube(world, pos.down(2).north(5).east(4).getX(), pos.down(2).north(5).east(4).getY(), pos.down(2).north(5).east(4).getZ(), pos.down().east(6).getX(), pos.down().east(6).getY(), pos.down().east(6).getZ(), Blocks.AIR.getDefaultState());
					GenerationUtils.forceFillAreaWithBlockCube(world, pos.down(2).north(4).east(7).getX(), pos.down(2).north(4).east(7).getY(), pos.down(2).north(4).east(7).getZ(), pos.down(2).north(2).east(7).getX(), pos.down(2).north(2).east(7).getY(), pos.down(2).north(2).east(7).getZ(), Blocks.AIR.getDefaultState());
					break;
				case 1:
					this.setPoiseLogUnsafe(world, pos, rand);
					this.setPoiseLogUnsafe(world, pos.east(), rand);
					this.setPoiseLogUnsafe(world, pos.east(2), rand);
					
					this.setPoiseLogUnsafe(world, pos.east(3).south(), rand);
					this.setPoiseLogUnsafe(world, pos.east(4).south(), rand);
					
					this.setPoiseLogUnsafe(world, pos.down().east(5).south(2), rand);
					this.setPoiseLogUnsafe(world, pos.down().east(5).south(3), rand);
					
					this.setPoiseLogUnsafe(world, pos.down(2).east(6).south(4), rand);
					this.setPoiseLogUnsafe(world, pos.down(2).east(6).south(5), rand);
					this.setPoiseLogUnsafe(world, pos.down(2).east(6).south(6), rand);
					
					this.setPoiseLogUnsafe(world, pos.down(3).east(5).south(7), rand);
					this.setPoiseLogUnsafe(world, pos.down(3).east(4).south(8), rand);
					this.setPoiseLogUnsafe(world, pos.down(3).east(3).south(8), rand);
					this.setPoiseLogUnsafe(world, pos.down(3).east(2).south(8), rand);
					
					this.setPoiseLogUnsafe(world, pos.down(2).east().south(7), rand);
					this.setPoiseLogUnsafe(world, pos.down(2).south(7), rand);
					
					this.setPoiseLogUnsafe(world, pos.down().west().south(6), rand);
					this.setPoiseLogUnsafe(world, pos.down().west().south(5), rand);
					
					this.setPoiseLogUnsafe(world, pos.west(2).south(4), rand);
					this.setPoiseLogUnsafe(world, pos.west(2).south(3), rand);
					this.setPoiseLogUnsafe(world, pos.west(2).south(2), rand);
					this.setPoiseLogUnsafe(world, pos.west().south(), rand);
					
					//Air
					GenerationUtils.forceFillAreaReversedWithBlockCube(world, pos.east(2).south().getX(), pos.east(2).south().getY(), pos.east(2).south().getZ(), pos.south().getX(), pos.south().getY(), pos.south().getZ(), Blocks.AIR.getDefaultState());
					GenerationUtils.forceFillAreaReversedWithBlockCube(world, pos.down().east(4).south(4).getX(), pos.down().east(4).south(4).getY(), pos.down().east(4).south(4).getZ(), pos.west().south(2).getX(), pos.west().south(2).getY(), pos.west().south(2).getZ(), Blocks.AIR.getDefaultState());
					GenerationUtils.forceFillAreaReversedWithBlockCube(world, pos.down(2).east(5).south(6).getX(), pos.down(2).east(5).south(6).getY(), pos.down(2).east(5).south(6).getZ(), pos.down().south(4).getX(), pos.down().south(4).getY(), pos.down().south(4).getZ(), Blocks.AIR.getDefaultState());
					GenerationUtils.forceFillAreaReversedWithBlockCube(world, pos.down(2).east(4).south(7).getX(), pos.down(2).east(4).south(7).getY(), pos.down(2).east(4).south(7).getZ(), pos.down(2).east(2).south(7).getX(), pos.down(2).east(2).south(7).getY(), pos.down(2).east(2).south(7).getZ(), Blocks.AIR.getDefaultState());
					break;
				case 2:
					this.setPoiseLogUnsafe(world, pos, rand);
					this.setPoiseLogUnsafe(world, pos.south(), rand);
					this.setPoiseLogUnsafe(world, pos.south(2), rand);
					
					this.setPoiseLogUnsafe(world, pos.south(3).west(), rand);
					this.setPoiseLogUnsafe(world, pos.south(4).west(), rand);
					
					this.setPoiseLogUnsafe(world, pos.down().south(5).west(2), rand);
					this.setPoiseLogUnsafe(world, pos.down().south(5).west(3), rand);
					
					this.setPoiseLogUnsafe(world, pos.down(2).south(6).west(4), rand);
					this.setPoiseLogUnsafe(world, pos.down(2).south(6).west(5), rand);
					this.setPoiseLogUnsafe(world, pos.down(2).south(6).west(6), rand);
					
					this.setPoiseLogUnsafe(world, pos.down(3).south(5).west(7), rand);
					this.setPoiseLogUnsafe(world, pos.down(3).south(4).west(8), rand);
					this.setPoiseLogUnsafe(world, pos.down(3).south(3).west(8), rand);
					this.setPoiseLogUnsafe(world, pos.down(3).south(2).west(8), rand);
					
					this.setPoiseLogUnsafe(world, pos.down(2).south().west(7), rand);
					this.setPoiseLogUnsafe(world, pos.down(2).west(7), rand);
					
					this.setPoiseLogUnsafe(world, pos.down().north().west(6), rand);
					this.setPoiseLogUnsafe(world, pos.down().north().west(5), rand);
					
					this.setPoiseLogUnsafe(world, pos.north(2).west(4), rand);
					this.setPoiseLogUnsafe(world, pos.north(2).west(3), rand);
					this.setPoiseLogUnsafe(world, pos.north(2).west(2), rand);
					this.setPoiseLogUnsafe(world, pos.north().west(), rand);
					
					//Air
					GenerationUtils.forceFillAreaReversedWithBlockCube(world, pos.south(2).west().getX(), pos.south(2).west().getY(), pos.south(2).west().getZ(), pos.west().getX(), pos.west().getY(), pos.west().getZ(), Blocks.AIR.getDefaultState());
					GenerationUtils.forceFillAreaReversedWithBlockCube(world, pos.down().south(4).west(2).getX(), pos.down().south(4).west(2).getY(), pos.down().south(4).west(2).getZ(), pos.north().west(4).getX(), pos.north().west(4).getY(), pos.north().west(4).getZ(), Blocks.AIR.getDefaultState());
					GenerationUtils.forceFillAreaReversedWithBlockCube(world, pos.down(2).south(5).west(4).getX(), pos.down(2).south(5).west(4).getY(), pos.down(2).south(5).west(4).getZ(), pos.down().west(6).getX(), pos.down().west(6).getY(), pos.down().west(6).getZ(), Blocks.AIR.getDefaultState());
					GenerationUtils.forceFillAreaReversedWithBlockCube(world, pos.down(2).south(4).west(7).getX(), pos.down(2).south(4).west(7).getY(), pos.down(2).south(4).west(7).getZ(), pos.down(2).south(2).west(7).getX(), pos.down(2).south(2).west(7).getY(), pos.down(2).south(2).west(7).getZ(), Blocks.AIR.getDefaultState());
					break;
				case 3:
					this.setPoiseLogUnsafe(world, pos, rand);
					this.setPoiseLogUnsafe(world, pos.west(), rand);
					this.setPoiseLogUnsafe(world, pos.west(2), rand);
					
					this.setPoiseLogUnsafe(world, pos.west(3).south(), rand);
					this.setPoiseLogUnsafe(world, pos.west(4).south(), rand);
					
					this.setPoiseLogUnsafe(world, pos.down().west(5).south(2), rand);
					this.setPoiseLogUnsafe(world, pos.down().west(5).south(3), rand);
					
					this.setPoiseLogUnsafe(world, pos.down(2).west(6).south(4), rand);
					this.setPoiseLogUnsafe(world, pos.down(2).west(6).south(5), rand);
					this.setPoiseLogUnsafe(world, pos.down(2).west(6).south(6), rand);
					
					this.setPoiseLogUnsafe(world, pos.down(3).west(5).south(7), rand);
					this.setPoiseLogUnsafe(world, pos.down(3).west(4).south(8), rand);
					this.setPoiseLogUnsafe(world, pos.down(3).west(3).south(8), rand);
					this.setPoiseLogUnsafe(world, pos.down(3).west(2).south(8), rand);
					
					this.setPoiseLogUnsafe(world, pos.down(2).west().south(7), rand);
					this.setPoiseLogUnsafe(world, pos.down(2).south(7), rand);
					
					this.setPoiseLogUnsafe(world, pos.down().east().south(6), rand);
					this.setPoiseLogUnsafe(world, pos.down().east().south(5), rand);
					
					this.setPoiseLogUnsafe(world, pos.east(2).south(4), rand);
					this.setPoiseLogUnsafe(world, pos.east(2).south(3), rand);
					this.setPoiseLogUnsafe(world, pos.east(2).south(2), rand);
					this.setPoiseLogUnsafe(world, pos.east().south(), rand);
					
					//Air
					GenerationUtils.forceFillAreaWithBlockCube(world, pos.west(2).south().getX(), pos.west(2).south().getY(), pos.west(2).south().getZ(), pos.south().getX(), pos.south().getY(), pos.south().getZ(), Blocks.AIR.getDefaultState());
					GenerationUtils.forceFillAreaWithBlockCube(world, pos.down().west(4).south(2).getX(), pos.down().west(4).south(2).getY(), pos.down().west(4).south(2).getZ(), pos.east().south(4).getX(), pos.east().south(4).getY(), pos.east().south(4).getZ(), Blocks.AIR.getDefaultState());
					GenerationUtils.forceFillAreaWithBlockCube(world, pos.down(2).west(5).south(4).getX(), pos.down(2).west(5).south(4).getY(), pos.down(2).west(5).south(4).getZ(), pos.down().south(6).getX(), pos.down().south(6).getY(), pos.down().south(6).getZ(), Blocks.AIR.getDefaultState());
					GenerationUtils.forceFillAreaWithBlockCube(world, pos.down(2).west(4).south(7).getX(), pos.down(2).west(4).south(7).getY(), pos.down(2).west(4).south(7).getZ(), pos.down(2).west(2).south(7).getX(), pos.down(2).west(2).south(7).getY(), pos.down(2).west(2).south(7).getZ(), Blocks.AIR.getDefaultState());
					break;
			}
		} else {
			switch (variant) {
				case 0:
					this.setPoiseLogUnsafe(world, pos.down(6).north(8).east(8), rand);
					
					this.setPoiseLogUnsafe(world, pos.down(6).north(9).east(7), rand);
					
					this.setPoiseLogUnsafe(world, pos.down(7).north(10).east(6), rand);
					
					this.setPoiseLogUnsafe(world, pos.down(8).north(11).east(6), rand);
					this.setPoiseLogUnsafe(world, pos.down(9).north(11).east(6), rand);
					
					this.setPoiseLogUnsafe(world, pos.down(10).north(11).east(7), rand);
					
					this.setPoiseLogUnsafe(world, pos.down(11).north(10).east(8), rand);
					this.setPoiseLogUnsafe(world, pos.down(11).north(9).east(9), rand);
					
					this.setPoiseLogUnsafe(world, pos.down(10).north(8).east(10), rand);
					
					this.setPoiseLogUnsafe(world, pos.down(9).north(7).east(10), rand);
					this.setPoiseLogUnsafe(world, pos.down(8).north(7).east(10), rand);
					
					this.setPoiseLogUnsafe(world, pos.down(7).north(8).east(9), rand);
					
					//Air
					world.setBlockState(pos.down(7).north(8).east(8), Blocks.AIR.getDefaultState(), 2);
					world.setBlockState(pos.down(7).north(9).east(7), Blocks.AIR.getDefaultState(), 2);
					
					world.setBlockState(pos.down(8).north(8).east(9), Blocks.AIR.getDefaultState(), 2);
					world.setBlockState(pos.down(8).north(9).east(8), Blocks.AIR.getDefaultState(), 2);
					world.setBlockState(pos.down(8).north(10).east(7), Blocks.AIR.getDefaultState(), 2);
					world.setBlockState(pos.down(9).north(8).east(9), Blocks.AIR.getDefaultState(), 2);
					world.setBlockState(pos.down(9).north(9).east(8), Blocks.AIR.getDefaultState(), 2);
					world.setBlockState(pos.down(9).north(10).east(7), Blocks.AIR.getDefaultState(), 2);
					
					world.setBlockState(pos.down(10).north(9).east(9), Blocks.AIR.getDefaultState(), 2);
					world.setBlockState(pos.down(10).north(10).east(8), Blocks.AIR.getDefaultState(), 2);
					
					world.setBlockState(pos.down(8).north(10).east(6), Blocks.AIR.getDefaultState(), 2);
					world.setBlockState(pos.down(10).north(10).east(7), Blocks.AIR.getDefaultState(), 2);
				case 1:
					this.setPoiseLogUnsafe(world, pos.down(6).east(8).south(8), rand);
					
					this.setPoiseLogUnsafe(world, pos.down(6).east(9).south(7), rand);
					
					this.setPoiseLogUnsafe(world, pos.down(7).east(10).south(6), rand);
					
					this.setPoiseLogUnsafe(world, pos.down(8).east(11).south(6), rand);
					this.setPoiseLogUnsafe(world, pos.down(9).east(11).south(6), rand);
					
					this.setPoiseLogUnsafe(world, pos.down(10).east(11).south(7), rand);
					
					this.setPoiseLogUnsafe(world, pos.down(11).east(10).south(8), rand);
					this.setPoiseLogUnsafe(world, pos.down(11).east(9).south(9), rand);
					
					this.setPoiseLogUnsafe(world, pos.down(10).east(8).south(10), rand);
					
					this.setPoiseLogUnsafe(world, pos.down(9).east(7).south(10), rand);
					this.setPoiseLogUnsafe(world, pos.down(8).east(7).south(10), rand);
					
					this.setPoiseLogUnsafe(world, pos.down(7).east(8).south(9), rand);
					
					//Air
					world.setBlockState(pos.down(7).east(8).south(8), Blocks.AIR.getDefaultState(), 2);
					world.setBlockState(pos.down(7).east(9).south(7), Blocks.AIR.getDefaultState(), 2);
					
					world.setBlockState(pos.down(8).east(8).south(9), Blocks.AIR.getDefaultState(), 2);
					world.setBlockState(pos.down(8).east(9).south(8), Blocks.AIR.getDefaultState(), 2);
					world.setBlockState(pos.down(8).east(10).south(7), Blocks.AIR.getDefaultState(), 2);
					world.setBlockState(pos.down(9).east(8).south(9), Blocks.AIR.getDefaultState(), 2);
					world.setBlockState(pos.down(9).east(9).south(8), Blocks.AIR.getDefaultState(), 2);
					world.setBlockState(pos.down(9).east(10).south(7), Blocks.AIR.getDefaultState(), 2);
					
					world.setBlockState(pos.down(10).east(9).south(9), Blocks.AIR.getDefaultState(), 2);
					world.setBlockState(pos.down(10).east(10).south(8), Blocks.AIR.getDefaultState(), 2);
					
					world.setBlockState(pos.down(8).east(10).south(6), Blocks.AIR.getDefaultState(), 2);
					world.setBlockState(pos.down(10).east(10).south(7), Blocks.AIR.getDefaultState(), 2);
				case 2:
					this.setPoiseLogUnsafe(world, pos.down(6).south(8).west(8), rand);
					
					this.setPoiseLogUnsafe(world, pos.down(6).south(9).west(7), rand);
					
					this.setPoiseLogUnsafe(world, pos.down(7).south(10).west(6), rand);
					
					this.setPoiseLogUnsafe(world, pos.down(8).south(11).west(6), rand);
					this.setPoiseLogUnsafe(world, pos.down(9).south(11).west(6), rand);
					
					this.setPoiseLogUnsafe(world, pos.down(10).south(11).west(7), rand);
					
					this.setPoiseLogUnsafe(world, pos.down(11).south(10).west(8), rand);
					this.setPoiseLogUnsafe(world, pos.down(11).south(9).west(9), rand);
					
					this.setPoiseLogUnsafe(world, pos.down(10).south(8).west(10), rand);
					
					this.setPoiseLogUnsafe(world, pos.down(9).south(7).west(10), rand);
					this.setPoiseLogUnsafe(world, pos.down(8).south(7).west(10), rand);
					
					this.setPoiseLogUnsafe(world, pos.down(7).south(8).west(9), rand);
					
					//Air
					world.setBlockState(pos.down(7).south(8).west(8), Blocks.AIR.getDefaultState(), 2);
					world.setBlockState(pos.down(7).south(9).west(7), Blocks.AIR.getDefaultState(), 2);
					
					world.setBlockState(pos.down(8).south(8).west(9), Blocks.AIR.getDefaultState(), 2);
					world.setBlockState(pos.down(8).south(9).west(8), Blocks.AIR.getDefaultState(), 2);
					world.setBlockState(pos.down(8).south(10).west(7), Blocks.AIR.getDefaultState(), 2);
					world.setBlockState(pos.down(9).south(8).west(9), Blocks.AIR.getDefaultState(), 2);
					world.setBlockState(pos.down(9).south(9).west(8), Blocks.AIR.getDefaultState(), 2);
					world.setBlockState(pos.down(9).south(10).west(7), Blocks.AIR.getDefaultState(), 2);
					
					world.setBlockState(pos.down(10).south(9).west(9), Blocks.AIR.getDefaultState(), 2);
					world.setBlockState(pos.down(10).south(10).west(8), Blocks.AIR.getDefaultState(), 2);
					
					world.setBlockState(pos.down(8).south(10).west(6), Blocks.AIR.getDefaultState(), 2);
					world.setBlockState(pos.down(10).south(10).west(7), Blocks.AIR.getDefaultState(), 2);
				case 3:
					this.setPoiseLogUnsafe(world, pos.down(6).west(8).north(8), rand);
					
					this.setPoiseLogUnsafe(world, pos.down(6).west(9).north(7), rand);
					
					this.setPoiseLogUnsafe(world, pos.down(7).west(10).north(6), rand);
					
					this.setPoiseLogUnsafe(world, pos.down(8).west(11).north(6), rand);
					this.setPoiseLogUnsafe(world, pos.down(9).west(11).north(6), rand);
					
					this.setPoiseLogUnsafe(world, pos.down(10).west(11).north(7), rand);
					
					this.setPoiseLogUnsafe(world, pos.down(11).west(10).north(8), rand);
					this.setPoiseLogUnsafe(world, pos.down(11).west(9).north(9), rand);
					
					this.setPoiseLogUnsafe(world, pos.down(10).west(8).north(10), rand);
					
					this.setPoiseLogUnsafe(world, pos.down(9).west(7).north(10), rand);
					this.setPoiseLogUnsafe(world, pos.down(8).west(7).north(10), rand);
					
					this.setPoiseLogUnsafe(world, pos.down(7).west(8).north(9), rand);
					
					//Air
					world.setBlockState(pos.down(7).west(8).north(8), Blocks.AIR.getDefaultState(), 2);
					world.setBlockState(pos.down(7).west(9).north(7), Blocks.AIR.getDefaultState(), 2);
					
					world.setBlockState(pos.down(8).west(8).north(9), Blocks.AIR.getDefaultState(), 2);
					world.setBlockState(pos.down(8).west(9).north(8), Blocks.AIR.getDefaultState(), 2);
					world.setBlockState(pos.down(8).west(10).north(7), Blocks.AIR.getDefaultState(), 2);
					world.setBlockState(pos.down(9).west(8).north(9), Blocks.AIR.getDefaultState(), 2);
					world.setBlockState(pos.down(9).west(9).north(8), Blocks.AIR.getDefaultState(), 2);
					world.setBlockState(pos.down(9).west(10).north(7), Blocks.AIR.getDefaultState(), 2);
					
					world.setBlockState(pos.down(10).west(9).north(9), Blocks.AIR.getDefaultState(), 2);
					world.setBlockState(pos.down(10).west(10).north(8), Blocks.AIR.getDefaultState(), 2);
					
					world.setBlockState(pos.down(8).west(10).north(6), Blocks.AIR.getDefaultState(), 2);
					world.setBlockState(pos.down(10).west(10).north(7), Blocks.AIR.getDefaultState(), 2);
			}
		}
	}
	
	private void buildGround(IWorld world, BlockPos pos, Random rand) {
		BlockPos origin = pos.down();
		for (int x = origin.getX() - 13; x <= origin.getX() + 13; x++) {
			for (int z = origin.getZ() - 2; z <= origin.getZ() + 2; z++) {
				world.setBlockState(new BlockPos(x, origin.getY(), z), Blocks.END_STONE.getDefaultState(), 2);
				world.setBlockState(new BlockPos(x, origin.getY() - 1, z), Blocks.END_STONE.getDefaultState(), 2);
			}
		}
		for (int x = origin.getX() - 12; x <= origin.getX() + 12; x++) {
			for (int z = origin.getZ() - 5; z <= origin.getZ() + 5; z++) {
				world.setBlockState(new BlockPos(x, origin.getY(), z), Blocks.END_STONE.getDefaultState(), 2);
				world.setBlockState(new BlockPos(x, origin.getY() - 1, z), Blocks.END_STONE.getDefaultState(), 2);
			}
		}
		for (int x = origin.getX() - 11; x <= origin.getX() + 11; x++) {
			for (int z = origin.getZ() - 7; z <= origin.getZ() + 7; z++) {
				world.setBlockState(new BlockPos(x, origin.getY(), z), Blocks.END_STONE.getDefaultState(), 2);
				world.setBlockState(new BlockPos(x, origin.getY() - 1, z), Blocks.END_STONE.getDefaultState(), 2);
			}
		}
		for (int x = origin.getX() - 10; x <= origin.getX() + 10; x++) {
			for (int z = origin.getZ() - 8; z <= origin.getZ() + 8; z++) {
				world.setBlockState(new BlockPos(x, origin.getY(), z), Blocks.END_STONE.getDefaultState(), 2);
				world.setBlockState(new BlockPos(x, origin.getY() - 1, z), Blocks.END_STONE.getDefaultState(), 2);
			}
		}
		for (int x = origin.getX() - 9; x <= origin.getX() + 9; x++) {
			for (int z = origin.getZ() - 9; z <= origin.getZ() + 9; z++) {
				world.setBlockState(new BlockPos(x, origin.getY(), z), Blocks.END_STONE.getDefaultState(), 2);
				world.setBlockState(new BlockPos(x, origin.getY() - 1, z), Blocks.END_STONE.getDefaultState(), 2);
			}
		}
		for (int x = origin.getX() - 8; x <= origin.getX() + 8; x++) {
			for (int z = origin.getZ() - 10; z <= origin.getZ() + 10; z++) {
				world.setBlockState(new BlockPos(x, origin.getY(), z), Blocks.END_STONE.getDefaultState(), 2);
				world.setBlockState(new BlockPos(x, origin.getY() - 1, z), Blocks.END_STONE.getDefaultState(), 2);
			}
		}
		for (int x = origin.getX() - 7; x <= origin.getX() + 7; x++) {
			for (int z = origin.getZ() - 11; z <= origin.getZ() + 11; z++) {
				world.setBlockState(new BlockPos(x, origin.getY(), z), Blocks.END_STONE.getDefaultState(), 2);
				world.setBlockState(new BlockPos(x, origin.getY() - 1, z), Blocks.END_STONE.getDefaultState(), 2);
			}
		}
		for (int x = origin.getX() - 5; x <= origin.getX() + 5; x++) {
			for (int z = origin.getZ() - 12; z <= origin.getZ() + 12; z++) {
				world.setBlockState(new BlockPos(x, origin.getY(), z), Blocks.END_STONE.getDefaultState(), 2);
				world.setBlockState(new BlockPos(x, origin.getY() - 1, z), Blocks.END_STONE.getDefaultState(), 2);
			}
		}
		for (int x = 0; x <= 4; x++) {
			world.setBlockState(origin.down().north(13).east(2).west(x), Blocks.END_STONE.getDefaultState(), 2);
			world.setBlockState(origin.north(13).east(2).west(x), Blocks.END_STONE.getDefaultState(), 2);
			world.setBlockState(origin.down().south(13).east(2).west(x), Blocks.END_STONE.getDefaultState(), 2);
			world.setBlockState(origin.south(13).east(2).west(x), Blocks.END_STONE.getDefaultState(), 2);
		}
	}
	
	private void buildPoiseHanger(IWorld world, BlockPos pos, Random rand, int direction, boolean corner) {
		if (!corner) {
			switch(direction) {
				case 0:
					this.setPoiseLogHighProb(world, pos, rand);
					this.setPoiseLogHighProb(world, pos.up().south(), rand);
					this.setPoiseLogHighProb(world, pos.up().south(2), rand);
					
					this.setPoiseCluster(world, pos.up(2).south(), rand);
					this.setPoiseCluster(world, pos.up(2).south(2), rand);
					this.setPoiseCluster(world, pos.up(2).south(3), rand);
					this.setPoiseCluster(world, pos.up(2).south(2).east(), rand);
					this.setPoiseCluster(world, pos.up(2).south(2).west(), rand);
					
					this.setPoiseCluster(world, pos.south(), rand);
					this.setPoiseCluster(world, pos.south(2), rand);
					this.setPoiseCluster(world, pos.south(3), rand);
					this.setPoiseCluster(world, pos.south(2).east(), rand);
					this.setPoiseCluster(world, pos.south(2).west(), rand);
					this.setPoiseCluster(world, pos.up().south(), rand);
					this.setPoiseCluster(world, pos.up().south(3), rand);
					this.setPoiseCluster(world, pos.up().south(4), rand);
					
					this.setPoiseCluster(world, pos.up().south(3).east(), rand);
					this.setPoiseCluster(world, pos.up().south(3).west(), rand);
					
					this.setPoiseCluster(world, pos.up().south(2).east(2), rand);
					this.setPoiseCluster(world, pos.up().south(2).west(2), rand);
					
					this.setPoiseCluster(world, pos.up().south().east(), rand);
					this.setPoiseCluster(world, pos.up().south().west(), rand);
					break;
				case 1:
					this.setPoiseLogHighProb(world, pos, rand);
					this.setPoiseLogHighProb(world, pos.up().west(), rand);
					this.setPoiseLogHighProb(world, pos.up().west(2), rand);
					
					this.setPoiseCluster(world, pos.up(2).west(), rand);
					this.setPoiseCluster(world, pos.up(2).west(2), rand);
					this.setPoiseCluster(world, pos.up(2).west(3), rand);
					this.setPoiseCluster(world, pos.up(2).west(2).south(), rand);
					this.setPoiseCluster(world, pos.up(2).west(2).north(), rand);
					
					this.setPoiseCluster(world, pos.west(), rand);
					this.setPoiseCluster(world, pos.west(2), rand);
					this.setPoiseCluster(world, pos.west(3), rand);
					this.setPoiseCluster(world, pos.west(2).south(), rand);
					this.setPoiseCluster(world, pos.west(2).north(), rand);
					this.setPoiseCluster(world, pos.up().west(), rand);
					this.setPoiseCluster(world, pos.up().west(3), rand);
					this.setPoiseCluster(world, pos.up().west(4), rand);
					
					this.setPoiseCluster(world, pos.up().west(3).south(), rand);
					this.setPoiseCluster(world, pos.up().west(3).north(), rand);
					
					this.setPoiseCluster(world, pos.up().west(2).south(2), rand);
					this.setPoiseCluster(world, pos.up().west(2).north(2), rand);
					
					this.setPoiseCluster(world, pos.up().west().south(), rand);
					this.setPoiseCluster(world, pos.up().west().north(), rand);
					break;
				case 2:
					this.setPoiseLogHighProb(world, pos, rand);
					this.setPoiseLogHighProb(world, pos.up().north(), rand);
					this.setPoiseLogHighProb(world, pos.up().north(2), rand);
					
					this.setPoiseCluster(world, pos.up(2).north(), rand);
					this.setPoiseCluster(world, pos.up(2).north(2), rand);
					this.setPoiseCluster(world, pos.up(2).north(3), rand);
					this.setPoiseCluster(world, pos.up(2).north(2).west(), rand);
					this.setPoiseCluster(world, pos.up(2).north(2).east(), rand);
					
					this.setPoiseCluster(world, pos.north(), rand);
					this.setPoiseCluster(world, pos.north(2), rand);
					this.setPoiseCluster(world, pos.north(3), rand);
					this.setPoiseCluster(world, pos.north(2).west(), rand);
					this.setPoiseCluster(world, pos.north(2).east(), rand);
					this.setPoiseCluster(world, pos.up().north(), rand);
					this.setPoiseCluster(world, pos.up().north(3), rand);
					this.setPoiseCluster(world, pos.up().north(4), rand);
					
					this.setPoiseCluster(world, pos.up().north(3).west(), rand);
					this.setPoiseCluster(world, pos.up().north(3).east(), rand);
					
					this.setPoiseCluster(world, pos.up().north(2).west(2), rand);
					this.setPoiseCluster(world, pos.up().north(2).east(2), rand);
					
					this.setPoiseCluster(world, pos.up().north().west(), rand);
					this.setPoiseCluster(world, pos.up().north().east(), rand);
					break;
				case 3:
					this.setPoiseLogHighProb(world, pos, rand);
					this.setPoiseLogHighProb(world, pos.up().east(), rand);
					this.setPoiseLogHighProb(world, pos.up().east(2), rand);
					
					this.setPoiseCluster(world, pos.up(2).east(), rand);
					this.setPoiseCluster(world, pos.up(2).east(2), rand);
					this.setPoiseCluster(world, pos.up(2).east(3), rand);
					this.setPoiseCluster(world, pos.up(2).east(2).north(), rand);
					this.setPoiseCluster(world, pos.up(2).east(2).south(), rand);
					
					this.setPoiseCluster(world, pos.east(), rand);
					this.setPoiseCluster(world, pos.east(2), rand);
					this.setPoiseCluster(world, pos.east(3), rand);
					this.setPoiseCluster(world, pos.east(2).north(), rand);
					this.setPoiseCluster(world, pos.east(2).south(), rand);
					this.setPoiseCluster(world, pos.up().east(), rand);
					this.setPoiseCluster(world, pos.up().east(3), rand);
					this.setPoiseCluster(world, pos.up().east(4), rand);
					
					this.setPoiseCluster(world, pos.up().east(3).north(), rand);
					this.setPoiseCluster(world, pos.up().east(3).south(), rand);
					
					this.setPoiseCluster(world, pos.up().east(2).north(2), rand);
					this.setPoiseCluster(world, pos.up().east(2).south(2), rand);
					
					this.setPoiseCluster(world, pos.up().east().north(), rand);
					this.setPoiseCluster(world, pos.up().east().south(), rand);
					break;
			}
		} else {
			switch (direction) {
				case 0:
					this.setPoiseLogHighProb(world, pos, rand);
					this.setPoiseLogHighProb(world, pos.up(), rand);
					this.setPoiseLogHighProb(world, pos.up().south(), rand);
					this.setPoiseLogHighProb(world, pos.up().south().west(), rand);
					
					this.setPoiseCluster(world, pos.up(2).south().west(), rand);
					this.setPoiseCluster(world, pos.up(2).south(2).west(), rand);
					this.setPoiseCluster(world, pos.up(2).west(), rand);
					this.setPoiseCluster(world, pos.up(2).south().west(2), rand);
					this.setPoiseCluster(world, pos.up(2).south(), rand);
					
					this.setPoiseCluster(world, pos.up().south(2).west(), rand);
					this.setPoiseCluster(world, pos.up().west(), rand);
					this.setPoiseCluster(world, pos.up().south().west(2), rand);
					this.setPoiseCluster(world, pos.up().south(), rand);
					
					this.setPoiseCluster(world, pos.south(2).west(), rand);
					this.setPoiseCluster(world, pos.west(), rand);
					this.setPoiseCluster(world, pos.south().west(2), rand);
					this.setPoiseCluster(world, pos.south(), rand);
					
					this.setPoiseCluster(world, pos.south().west(), rand);
					this.setPoiseCluster(world, pos.up().south(2).west(2), rand);
					this.setPoiseCluster(world, pos.up().south(2), rand);
					this.setPoiseCluster(world, pos.up().west(2), rand);
					
					this.setPoiseCluster(world, pos.up().south(3).west(), rand);
					this.setPoiseCluster(world, pos.up().north().west(), rand);
					this.setPoiseCluster(world, pos.up().south().west(3), rand);
					this.setPoiseCluster(world, pos.up().south().east(), rand);
					break;
				case 1:
					this.setPoiseLogHighProb(world, pos, rand);
					this.setPoiseLogHighProb(world, pos.up(), rand);
					this.setPoiseLogHighProb(world, pos.up().south(), rand);
					this.setPoiseLogHighProb(world, pos.up().south().east(), rand);
					
					this.setPoiseCluster(world, pos.up(2).south().east(), rand);
					this.setPoiseCluster(world, pos.up(2).south(2).east(), rand);
					this.setPoiseCluster(world, pos.up(2).east(), rand);
					this.setPoiseCluster(world, pos.up(2).south().east(2), rand);
					this.setPoiseCluster(world, pos.up(2).south(), rand);
					
					this.setPoiseCluster(world, pos.up().south(2).east(), rand);
					this.setPoiseCluster(world, pos.up().east(), rand);
					this.setPoiseCluster(world, pos.up().south().east(2), rand);
					this.setPoiseCluster(world, pos.up().south(), rand);
					
					this.setPoiseCluster(world, pos.south(2).east(), rand);
					this.setPoiseCluster(world, pos.east(), rand);
					this.setPoiseCluster(world, pos.south().east(2), rand);
					this.setPoiseCluster(world, pos.south(), rand);
					
					this.setPoiseCluster(world, pos.south().east(), rand);
					this.setPoiseCluster(world, pos.up().south(2).east(2), rand);
					this.setPoiseCluster(world, pos.up().south(2), rand);
					this.setPoiseCluster(world, pos.up().east(2), rand);
					
					this.setPoiseCluster(world, pos.up().south(3).east(), rand);
					this.setPoiseCluster(world, pos.up().east().east(), rand);
					this.setPoiseCluster(world, pos.up().south().east(3), rand);
					this.setPoiseCluster(world, pos.up().south().west(), rand);
					break;
				case 2:
					this.setPoiseLogHighProb(world, pos, rand);
					this.setPoiseLogHighProb(world, pos.up(), rand);
					this.setPoiseLogHighProb(world, pos.up().north(), rand);
					this.setPoiseLogHighProb(world, pos.up().north().west(), rand);
					
					this.setPoiseCluster(world, pos.up(2).north().west(), rand);
					this.setPoiseCluster(world, pos.up(2).north(2).west(), rand);
					this.setPoiseCluster(world, pos.up(2).west(), rand);
					this.setPoiseCluster(world, pos.up(2).north().west(2), rand);
					this.setPoiseCluster(world, pos.up(2).north(), rand);
					
					this.setPoiseCluster(world, pos.up().north(2).west(), rand);
					this.setPoiseCluster(world, pos.up().west(), rand);
					this.setPoiseCluster(world, pos.up().north().west(2), rand);
					this.setPoiseCluster(world, pos.up().north(), rand);
					
					this.setPoiseCluster(world, pos.north(2).west(), rand);
					this.setPoiseCluster(world, pos.west(), rand);
					this.setPoiseCluster(world, pos.north().west(2), rand);
					this.setPoiseCluster(world, pos.north(), rand);
					
					this.setPoiseCluster(world, pos.north().west(), rand);
					this.setPoiseCluster(world, pos.up().north(2).west(2), rand);
					this.setPoiseCluster(world, pos.up().north(2), rand);
					this.setPoiseCluster(world, pos.up().west(2), rand);
					
					this.setPoiseCluster(world, pos.up().north(3).west(), rand);
					this.setPoiseCluster(world, pos.up().west().west(), rand);
					this.setPoiseCluster(world, pos.up().north().west(3), rand);
					this.setPoiseCluster(world, pos.up().north().east(), rand);
					break;
				case 3:
					this.setPoiseLogHighProb(world, pos, rand);
					this.setPoiseLogHighProb(world, pos.up(), rand);
					this.setPoiseLogHighProb(world, pos.up().north(), rand);
					this.setPoiseLogHighProb(world, pos.up().north().east(), rand);
					
					this.setPoiseCluster(world, pos.up(2).north().east(), rand);
					this.setPoiseCluster(world, pos.up(2).north(2).east(), rand);
					this.setPoiseCluster(world, pos.up(2).east(), rand);
					this.setPoiseCluster(world, pos.up(2).north().east(2), rand);
					this.setPoiseCluster(world, pos.up(2).north(), rand);
					
					this.setPoiseCluster(world, pos.up().north(2).east(), rand);
					this.setPoiseCluster(world, pos.up().east(), rand);
					this.setPoiseCluster(world, pos.up().north().east(2), rand);
					this.setPoiseCluster(world, pos.up().north(), rand);
					
					this.setPoiseCluster(world, pos.north(2).east(), rand);
					this.setPoiseCluster(world, pos.east(), rand);
					this.setPoiseCluster(world, pos.north().east(2), rand);
					this.setPoiseCluster(world, pos.north(), rand);
					
					this.setPoiseCluster(world, pos.north().east(), rand);
					this.setPoiseCluster(world, pos.up().north(2).east(2), rand);
					this.setPoiseCluster(world, pos.up().north(2), rand);
					this.setPoiseCluster(world, pos.up().east(2), rand);
					
					this.setPoiseCluster(world, pos.up().north(3).east(), rand);
					this.setPoiseCluster(world, pos.up().east().east(), rand);
					this.setPoiseCluster(world, pos.up().north().east(3), rand);
					this.setPoiseCluster(world, pos.up().north().west(), rand);
					break;
			}
		}
	}
	
	private boolean isViableDomeArea(IWorld world, BlockPos pos) {
		return GenerationUtils.isAreaReplacable(world, pos.north(13).west(13).getX(), pos.north(13).west(13).getY(), pos.north(13).west(13).getZ(), pos.up(16).south(13).east(13).getX(), pos.up(16).south(13).east(13).getY(), pos.up(16).south(13).east(13).getZ());
	}
	
	private boolean isGroundViable(IWorld world, BlockPos pos, Random rand) {
		for (int xx = pos.north(13).west(13).getX(); xx <= pos.south(13).east(13).getX(); xx++) {
			for (int zz = pos.north(13).west(13).getZ(); zz <= pos.south(13).east(13).getZ(); zz++) {
				if (!isProperBlock(world, new BlockPos(xx, pos.getY(), zz))) {
					return false;
				}
			}
		}
		return true;
	}
	
	private boolean isProperBlock(IWorld world, BlockPos pos) {
		return world.getBlockState(pos).getBlock() == Blocks.END_STONE || world.getBlockState(pos).getBlock() == EEBlocks.POISMOSS.get() || world.getBlockState(pos).getBlock() == EEBlocks.EUMUS.get() || world.getBlockState(pos).getBlock() == EEBlocks.POISMOSS_EUMUS.get();
	}
	
	private void placePoismossAt(IWorld world, IWorldGenerationReader reader, BlockPos pos) {
		BlockPos blockpos = pos.up();
		if (world.getBlockState(blockpos).getBlock() == Blocks.AIR) {
			BlockState newGround = EEBlocks.POISMOSS.get().getDefaultState();
			world.setBlockState(blockpos, newGround, 2);
		}
	}
	
	private void setPoiseCluster(IWorld world, BlockPos pos, Random rand) {
		if (world.getBlockState(pos).getMaterial().isReplaceable()) {
			world.setBlockState(pos, EEBlocks.POISE_CLUSTER.get().getDefaultState(), 2);
		}
	}
	
	private void setPoiseLogHighProb(IWorld world, BlockPos pos, Random rand) {
		BlockState logState = rand.nextFloat() <= 0.35F ? EEBlocks.POISE_LOG.get().getDefaultState() : EEBlocks.POISE_LOG_GLOWING.get().getDefaultState();
		if (world.getBlockState(pos).getMaterial().isReplaceable()) {
			world.setBlockState(pos, logState, 2);
		}
	}
	
	private void setPoiseLog(IWorld world, BlockPos pos, Random rand) {
		BlockState logState = rand.nextFloat() <= 0.90F ? EEBlocks.POISE_LOG.get().getDefaultState() : EEBlocks.POISE_LOG_GLOWING.get().getDefaultState();
		if (world.getBlockState(pos).getMaterial().isReplaceable()) {
			world.setBlockState(pos, logState, 2);
		}
	}
	
	private void setPoiseLogUnsafe(IWorld world, BlockPos pos, Random rand) {
		BlockState logState = rand.nextFloat() <= 0.90F ? EEBlocks.POISE_LOG.get().getDefaultState() : EEBlocks.POISE_LOG_GLOWING.get().getDefaultState();
		world.setBlockState(pos, logState, 2);
	}
	
	private void setPoismoss(IWorld world, BlockPos pos) {
		world.setBlockState(pos, EEBlocks.POISMOSS.get().getDefaultState(), 2);
	}

}
