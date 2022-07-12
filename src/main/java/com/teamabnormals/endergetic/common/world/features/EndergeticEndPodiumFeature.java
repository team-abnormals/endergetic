package com.teamabnormals.endergetic.common.world.features;

import com.teamabnormals.endergetic.common.blocks.AcidianLanternBlock;
import com.teamabnormals.endergetic.core.registry.EEBlocks;

import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.levelgen.feature.EndPodiumFeature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

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
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
		BlockPos pos = context.origin();
		WorldGenLevel level = context.level();
		for (BlockPos blockpos : BlockPos.betweenClosed(new BlockPos(pos.getX() - 4, pos.getY() - 1, pos.getZ() - 4), new BlockPos(pos.getX() + 4, pos.getY() + 32, pos.getZ() + 4))) {
			boolean flag = blockpos.closerThan(pos, 2.5D);
			if (flag || blockpos.closerThan(pos, 3.5D)) {
				if (blockpos.getY() < pos.getY()) {
					if (flag) {
						this.setBlockState(level, blockpos.above(), MYSTICAL_OBSIDIAN);
					} else if (blockpos.getY() < pos.getY()) {
						this.setBlockState(level, blockpos.above(), Blocks.END_STONE.defaultBlockState());
					}
				} else if (blockpos.getY() > pos.getY()) {
					this.setBlockState(level, blockpos.above(), Blocks.AIR.defaultBlockState());
				} else if (!flag) {
					this.setBlockState(level, blockpos.above(), MYSTICAL_OBSIDIAN);
				} else if (this.activePortal) {
					this.setBlockState(level, blockpos.above(), Blocks.END_PORTAL.defaultBlockState());
				} else {
					this.setBlockState(level, blockpos.above(), Blocks.AIR.defaultBlockState());
				}
			}
		}

		this.setBlockState(level, pos.above(2).north(2).east(2), MYSTICAL_OBSIDIAN_WALL);
		this.setBlockState(level, pos.above(2).north(2).west(2), MYSTICAL_OBSIDIAN_WALL);
		this.setBlockState(level, pos.above(2).south(2).east(2), MYSTICAL_OBSIDIAN_WALL);
		this.setBlockState(level, pos.above(2).south(2).west(2), MYSTICAL_OBSIDIAN_WALL);

		for (int i = 1; i < 6; i++) {
			if (i > 3) {
				this.setBlockState(level, pos.above(i), MYSTICAL_OBSIDIAN_WALL);
			} else {
				this.setBlockState(level, pos.above(i), MYSTICAL_OBSIDIAN);
			}
		}

		for (int i = 2; i < 6; i++) {
			this.createRuneSide(level, pos, Direction.from3DDataValue(i), this.activePortal);
		}

		if (this.activePortal) {
			this.setBlockState(level, pos.above(3).north(2).east(2), ACIDIAN_LANTERN);
			this.setBlockState(level, pos.above(3).north(2).west(2), ACIDIAN_LANTERN);
			this.setBlockState(level, pos.above(3).south(2).east(2), ACIDIAN_LANTERN);
			this.setBlockState(level, pos.above(3).south(2).west(2), ACIDIAN_LANTERN);
		}

		return true;
	}

	private void createRuneSide(LevelAccessor world, BlockPos pos, Direction direction, boolean active) {
		this.setBlockState(world, pos.relative(direction, 3).relative(direction.getClockWise()).above(), MYSTICAL_OBSIDIAN_RUNE.setValue(HorizontalDirectionalBlock.FACING, direction.getOpposite()));
		this.setBlockState(world, pos.relative(direction, 3).above(), MYSTICAL_OBSIDIAN_ACTIVATION_RUNE(active).setValue(HorizontalDirectionalBlock.FACING, direction));
		this.setBlockState(world, pos.relative(direction, 3).relative(direction.getCounterClockWise()).above(), MYSTICAL_OBSIDIAN_RUNE.setValue(HorizontalDirectionalBlock.FACING, direction));
	}

	private void setBlockState(LevelAccessor world, BlockPos pos, BlockState state) {
		world.setBlock(pos, state, 2);
	}
}