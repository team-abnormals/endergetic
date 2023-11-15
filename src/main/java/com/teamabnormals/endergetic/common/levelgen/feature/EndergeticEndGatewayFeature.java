package com.teamabnormals.endergetic.common.levelgen.feature;

import com.mojang.serialization.Codec;
import com.teamabnormals.endergetic.api.util.GenerationUtils;
import com.teamabnormals.endergetic.common.block.AcidianLanternBlock;
import com.teamabnormals.endergetic.core.registry.EEBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.TheEndGatewayBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WallSide;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.EndGatewayConfiguration;

import java.util.function.Supplier;

public class EndergeticEndGatewayFeature extends Feature<EndGatewayConfiguration> {
	private static final Supplier<BlockState> MYSTICAL_OBSIDIAN = () -> EEBlocks.MYSTICAL_OBSIDIAN.get().defaultBlockState();
	private static final Supplier<BlockState> MYSTICAL_OBSIDIAN_WALL = () -> EEBlocks.MYSTICAL_OBSIDIAN_WALL.get().defaultBlockState();
	private static final Supplier<BlockState> ACIDIAN_LANTERN = () -> EEBlocks.ACIDIAN_LANTERN.get().defaultBlockState();

	public EndergeticEndGatewayFeature(Codec<EndGatewayConfiguration> config) {
		super(config);
	}

	public boolean place(FeaturePlaceContext<EndGatewayConfiguration> context) {
		WorldGenLevel level = context.level();
		BlockPos pos = context.origin();
		if (!GenerationUtils.isAreaReplacable(level, pos.getX() - 1, pos.getY() - 4, pos.getZ() - 1, pos.getX() + 1, pos.getY() + 4, pos.getZ() + 1))
			return false;

		EndGatewayConfiguration config = context.config();
		for (BlockPos blockpos : BlockPos.betweenClosed(pos.offset(-1, -2, -1), pos.offset(1, 2, 1))) {
			boolean flag = blockpos.getX() == pos.getX();
			boolean flag1 = blockpos.getY() == pos.getY();
			boolean flag2 = blockpos.getZ() == pos.getZ();
			boolean flag3 = Math.abs(blockpos.getY() - pos.getY()) == 2;
			if (flag && flag1 && flag2) {
				BlockPos blockpos1 = blockpos.immutable();
				level.setBlock(blockpos1, Blocks.END_GATEWAY.defaultBlockState(), 2);
				config.getExit().ifPresent((p_214624_3_) -> {
					BlockEntity tileentity = level.getBlockEntity(blockpos1);
					if (tileentity instanceof TheEndGatewayBlockEntity endgatewaytileentity) {
						endgatewaytileentity.setExitPosition(p_214624_3_, config.isExitExact());
						tileentity.setChanged();
					}
				});
			} else if (flag1) {
				level.setBlock(blockpos, Blocks.AIR.defaultBlockState(), 2);
			} else if (flag3 && flag && flag2) {
				level.setBlock(blockpos, MYSTICAL_OBSIDIAN.get(), 2);
			} else if ((flag || flag2) && !flag3) {
				level.setBlock(blockpos, MYSTICAL_OBSIDIAN.get(), 2);
			} else {
				level.setBlock(blockpos, Blocks.AIR.defaultBlockState(), 2);
			}
		}

		level.setBlock(pos.below(3), MYSTICAL_OBSIDIAN_WALL.get(), 2);
		level.setBlock(pos.above(3), MYSTICAL_OBSIDIAN_WALL.get(), 2);

		level.setBlock(pos.north().east().above(), MYSTICAL_OBSIDIAN_WALL.get().setValue(WallBlock.SOUTH_WALL, WallSide.LOW).setValue(WallBlock.WEST_WALL, WallSide.LOW), 2);
		level.setBlock(pos.north().west().above(), MYSTICAL_OBSIDIAN_WALL.get().setValue(WallBlock.SOUTH_WALL, WallSide.LOW).setValue(WallBlock.EAST_WALL, WallSide.LOW), 2);
		level.setBlock(pos.south().east().above(), MYSTICAL_OBSIDIAN_WALL.get().setValue(WallBlock.NORTH_WALL, WallSide.LOW).setValue(WallBlock.WEST_WALL, WallSide.LOW), 2);
		level.setBlock(pos.south().west().above(), MYSTICAL_OBSIDIAN_WALL.get().setValue(WallBlock.NORTH_WALL, WallSide.LOW).setValue(WallBlock.EAST_WALL, WallSide.LOW), 2);

		level.setBlock(pos.above(4), ACIDIAN_LANTERN.get().setValue(AcidianLanternBlock.FACING, Direction.UP), 2);
		level.setBlock(pos.below(4), ACIDIAN_LANTERN.get().setValue(AcidianLanternBlock.FACING, Direction.DOWN), 2);
		return true;
	}
}