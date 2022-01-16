package com.minecraftabnormals.endergetic.common.world.features;

import com.minecraftabnormals.endergetic.common.blocks.AcidianLanternBlock;
import com.minecraftabnormals.endergetic.core.registry.EEBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.EndPodiumFeature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

import java.util.Random;

public class EndergeticEndPodiumFeature extends EndPodiumFeature {
	public static final BlockPos END_PODIUM_LOCATION = BlockPos.ZERO;
	private static final BlockState MYSTICAL_OBSIDIAN = EEBlocks.MYSTICAL_OBSIDIAN.get().defaultBlockState();
	private static final BlockState MYSTICAL_OBSIDIAN_WALL = EEBlocks.MYSTICAL_OBSIDIAN_WALL.get().defaultBlockState();
	private static final BlockState MYSTICAL_OBSIDIAN_RUNE = EEBlocks.MYSTICAL_OBSIDIAN_RUNE.get().defaultBlockState();
	private static final BlockState ACIDIAN_LANTERN = EEBlocks.ACIDIAN_LANTERN.get().defaultBlockState().setValue(AcidianLanternBlock.FACING, Direction.UP);

	private static BlockState MYSTICAL_OBSIDIAN_ACTIVATION_RUNE(boolean active) {
		return active ? EEBlocks.ACTIVATED_MYSTICAL_OBSIDIAN_ACTIVATION_RUNE.get().defaultBlockState() : EEBlocks.MYSTICAL_OBSIDIAN_ACTIVATION_RUNE.get().defaultBlockState();
	}

	private final boolean activePortal;

	public EndergeticEndPodiumFeature(boolean activePortalIn) {
		super(activePortalIn);
		this.activePortal = activePortalIn;
	}

	@Override
	public boolean place(ISeedReader worldIn, ChunkGenerator generator, Random rand, BlockPos pos, NoFeatureConfig config) {
		for (BlockPos blockpos : BlockPos.betweenClosed(new BlockPos(pos.getX() - 4, pos.getY() - 1, pos.getZ() - 4), new BlockPos(pos.getX() + 4, pos.getY() + 32, pos.getZ() + 4))) {
			boolean flag = blockpos.closerThan(pos, 2.5D);
			if (flag || blockpos.closerThan(pos, 3.5D)) {
				if (blockpos.getY() < pos.getY()) {
					if (flag) {
						this.setBlockState(worldIn, blockpos.above(), MYSTICAL_OBSIDIAN);
					} else if (blockpos.getY() < pos.getY()) {
						this.setBlockState(worldIn, blockpos.above(), Blocks.END_STONE.defaultBlockState());
					}
				} else if (blockpos.getY() > pos.getY()) {
					this.setBlockState(worldIn, blockpos.above(), Blocks.AIR.defaultBlockState());
				} else if (!flag) {
					this.setBlockState(worldIn, blockpos.above(), MYSTICAL_OBSIDIAN);
				} else if (this.activePortal) {
					this.setBlockState(worldIn, blockpos.above(), Blocks.END_PORTAL.defaultBlockState());
				} else {
					this.setBlockState(worldIn, blockpos.above(), Blocks.AIR.defaultBlockState());
				}
			}
		}

		this.setBlockState(worldIn, pos.above(2).north(2).east(2), MYSTICAL_OBSIDIAN_WALL);
		this.setBlockState(worldIn, pos.above(2).north(2).west(2), MYSTICAL_OBSIDIAN_WALL);
		this.setBlockState(worldIn, pos.above(2).south(2).east(2), MYSTICAL_OBSIDIAN_WALL);
		this.setBlockState(worldIn, pos.above(2).south(2).west(2), MYSTICAL_OBSIDIAN_WALL);

		for (int i = 1; i < 6; i++) {
			if (i > 3) {
				this.setBlockState(worldIn, pos.above(i), MYSTICAL_OBSIDIAN_WALL);
			} else {
				this.setBlockState(worldIn, pos.above(i), MYSTICAL_OBSIDIAN);
			}
		}

		for (int i = 2; i < 6; i++) {
			this.createRuneSide(worldIn, pos, Direction.from3DDataValue(i), this.activePortal);
		}

		if (this.activePortal) {
			this.setBlockState(worldIn, pos.above(3).north(2).east(2), ACIDIAN_LANTERN);
			this.setBlockState(worldIn, pos.above(3).north(2).west(2), ACIDIAN_LANTERN);
			this.setBlockState(worldIn, pos.above(3).south(2).east(2), ACIDIAN_LANTERN);
			this.setBlockState(worldIn, pos.above(3).south(2).west(2), ACIDIAN_LANTERN);
		}

		return true;
	}

	private void createRuneSide(IWorld world, BlockPos pos, Direction direction, boolean active) {
		this.setBlockState(world, pos.relative(direction, 3).relative(direction.getClockWise()).above(), MYSTICAL_OBSIDIAN_RUNE.setValue(HorizontalBlock.FACING, direction.getOpposite()));
		this.setBlockState(world, pos.relative(direction, 3).above(), MYSTICAL_OBSIDIAN_ACTIVATION_RUNE(active).setValue(HorizontalBlock.FACING, direction));
		this.setBlockState(world, pos.relative(direction, 3).relative(direction.getCounterClockWise()).above(), MYSTICAL_OBSIDIAN_RUNE.setValue(HorizontalBlock.FACING, direction));
	}

	private void setBlockState(IWorld world, BlockPos pos, BlockState state) {
		world.setBlock(pos, state, 2);
	}
}