package com.minecraftabnormals.endergetic.common.world.features;

import java.util.Random;
import java.util.function.Supplier;

import com.minecraftabnormals.endergetic.api.util.GenerationUtils;
import com.minecraftabnormals.endergetic.common.blocks.AcidianLanternBlock;
import com.minecraftabnormals.endergetic.core.registry.EEBlocks;
import com.mojang.serialization.Codec;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.WallBlock;
import net.minecraft.block.WallHeight;
import net.minecraft.tileentity.EndGatewayTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.EndGatewayConfig;
import net.minecraft.world.gen.feature.Feature;

public class EndergeticEndGatewayFeature extends Feature<EndGatewayConfig> {
	private static final Supplier<BlockState> MYSTICAL_OBSIDIAN = () -> EEBlocks.MYSTICAL_OBSIDIAN.get().getDefaultState();
	private static final Supplier<BlockState> MYSTICAL_OBSIDIAN_WALL = () -> EEBlocks.MYSTICAL_OBSIDIAN_WALL.get().getDefaultState();
	private static final Supplier<BlockState> ACIDIAN_LANTERN = () -> EEBlocks.ACIDIAN_LANTERN.get().getDefaultState();

	public EndergeticEndGatewayFeature(Codec<EndGatewayConfig> config) {
		super(config);
	}

	public boolean generate(ISeedReader worldIn, ChunkGenerator generator, Random rand, BlockPos pos, EndGatewayConfig config) {
		if (!GenerationUtils.isAreaReplacable(worldIn, pos.getX() - 1, pos.getY() - 4, pos.getZ() - 1, pos.getX() + 1, pos.getY() + 4, pos.getZ() + 1))
			return false;

		for (BlockPos blockpos : BlockPos.getAllInBoxMutable(pos.add(-1, -2, -1), pos.add(1, 2, 1))) {
			boolean flag = blockpos.getX() == pos.getX();
			boolean flag1 = blockpos.getY() == pos.getY();
			boolean flag2 = blockpos.getZ() == pos.getZ();
			boolean flag3 = Math.abs(blockpos.getY() - pos.getY()) == 2;
			if (flag && flag1 && flag2) {
				BlockPos blockpos1 = blockpos.toImmutable();
				worldIn.setBlockState(blockpos1, Blocks.END_GATEWAY.getDefaultState(), 2);
				config.func_214700_b().ifPresent((p_214624_3_) -> {
					TileEntity tileentity = worldIn.getTileEntity(blockpos1);
					if (tileentity instanceof EndGatewayTileEntity) {
						EndGatewayTileEntity endgatewaytileentity = (EndGatewayTileEntity) tileentity;
						endgatewaytileentity.setExitPortal(p_214624_3_, config.func_214701_c());
						tileentity.markDirty();
					}
				});
			} else if (flag1) {
				worldIn.setBlockState(blockpos, Blocks.AIR.getDefaultState(), 2);
			} else if (flag3 && flag && flag2) {
				worldIn.setBlockState(blockpos, MYSTICAL_OBSIDIAN.get(), 2);
			} else if ((flag || flag2) && !flag3) {
				worldIn.setBlockState(blockpos, MYSTICAL_OBSIDIAN.get(), 2);
			} else {
				worldIn.setBlockState(blockpos, Blocks.AIR.getDefaultState(), 2);
			}
		}

		worldIn.setBlockState(pos.down(3), MYSTICAL_OBSIDIAN_WALL.get(), 2);
		worldIn.setBlockState(pos.up(3), MYSTICAL_OBSIDIAN_WALL.get(), 2);

		worldIn.setBlockState(pos.north().east().up(), MYSTICAL_OBSIDIAN_WALL.get().with(WallBlock.WALL_HEIGHT_SOUTH, WallHeight.LOW).with(WallBlock.WALL_HEIGHT_WEST, WallHeight.LOW), 2);
		worldIn.setBlockState(pos.north().west().up(), MYSTICAL_OBSIDIAN_WALL.get().with(WallBlock.WALL_HEIGHT_SOUTH, WallHeight.LOW).with(WallBlock.WALL_HEIGHT_EAST, WallHeight.LOW), 2);
		worldIn.setBlockState(pos.south().east().up(), MYSTICAL_OBSIDIAN_WALL.get().with(WallBlock.WALL_HEIGHT_NORTH, WallHeight.LOW).with(WallBlock.WALL_HEIGHT_WEST, WallHeight.LOW), 2);
		worldIn.setBlockState(pos.south().west().up(), MYSTICAL_OBSIDIAN_WALL.get().with(WallBlock.WALL_HEIGHT_NORTH, WallHeight.LOW).with(WallBlock.WALL_HEIGHT_EAST, WallHeight.LOW), 2);

		worldIn.setBlockState(pos.up(4), ACIDIAN_LANTERN.get().with(AcidianLanternBlock.FACING, Direction.UP), 2);
		worldIn.setBlockState(pos.down(4), ACIDIAN_LANTERN.get().with(AcidianLanternBlock.FACING, Direction.DOWN), 2);
		return true;
	}
}