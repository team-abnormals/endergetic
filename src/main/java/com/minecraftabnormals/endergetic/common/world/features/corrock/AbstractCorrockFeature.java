package com.minecraftabnormals.endergetic.common.world.features.corrock;

import com.minecraftabnormals.abnormals_core.core.util.GenerationPiece;
import com.minecraftabnormals.endergetic.common.blocks.CorrockCrownStandingBlock;
import com.minecraftabnormals.endergetic.core.registry.EEBlocks;
import com.mojang.serialization.Codec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChorusFlowerBlock;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.util.Direction;
import net.minecraft.util.LazyValue;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

public abstract class AbstractCorrockFeature<FC extends IFeatureConfig> extends Feature<FC> {
	protected static final Block CORROCK_BLOCK = EEBlocks.CORROCK_END.get();
	protected static final Block CORROCK_BLOCK_BLOCK = EEBlocks.CORROCK_END_BLOCK.get();
	protected static final LazyValue<BlockState> CORROCK_STATE = new LazyValue<>(() -> EEBlocks.CORROCK_END.get().defaultBlockState());
	protected static final LazyValue<BlockState> CORROCK_BLOCK_STATE = new LazyValue<>(() -> EEBlocks.CORROCK_END_BLOCK.get().defaultBlockState());

	protected static Supplier<BlockState> getCorrockCrown(boolean wall) {
		return wall ? () -> EEBlocks.CORROCK_CROWN_END_WALL.get().defaultBlockState() : () -> EEBlocks.CORROCK_CROWN_END_STANDING.get().defaultBlockState();
	}

	protected static BlockState getCorrockCrownWall(Direction facing) {
		return EEBlocks.CORROCK_CROWN_END_WALL.get().defaultBlockState().setValue(HorizontalBlock.FACING, facing);
	}

	protected static BlockState getCorrockCrownStanding(int rotation) {
		return EEBlocks.CORROCK_CROWN_END_STANDING.get().defaultBlockState().setValue(CorrockCrownStandingBlock.ROTATION, rotation);
	}

	public AbstractCorrockFeature(Codec<FC> configFactory) {
		super(configFactory);
	}

	protected static boolean isNotCloseToAnotherGrowth(List<ChorusPlantPart> growths, BlockPos pos) {
		for (ChorusPlantPart part : growths) {
			if (MathHelper.sqrt(part.pos.distSqr(pos)) < 2.0F) {
				return false;
			}
		}
		return true;
	}

	protected static boolean tryToFillWithCorrockBlock(ISeedReader world, int x1, int y1, int z1, int x2, int y2, int z2, List<BlockPos> positions) {
		BlockPos.Mutable mutable = new BlockPos.Mutable();
		for (int yy = y1; yy <= y2; yy++) {
			for (int xx = x1; xx <= x2; xx++) {
				for (int zz = z1; zz <= z2; zz++) {
					if (world.isEmptyBlock(mutable.set(xx, yy, zz))) {
						positions.add(mutable.immutable());
					} else {
						return false;
					}
				}
			}
		}
		return true;
	}

	protected static boolean tryToPlaceCorrockBlock(ISeedReader world, BlockPos pos, List<BlockPos> positions) {
		if (world.isEmptyBlock(pos)) {
			positions.add(pos.immutable());
			return true;
		}
		return false;
	}

	protected static boolean tryToPlaceCorrockBlockWithCrown(ISeedReader world, Random rand, BlockPos pos, List<BlockPos> positions, Direction direction, GenerationPiece crowns, @Nullable List<BlockPos> corners, float crownChance) {
		if (world.isEmptyBlock(pos)) {
			BlockPos immutable = pos.immutable();
			positions.add(immutable);
			if (corners != null) {
				corners.add(immutable);
			}
			if (rand.nextFloat() < crownChance) {
				if (rand.nextBoolean()) {
					BlockPos up = pos.above();
					if (world.isEmptyBlock(up)) {
						crowns.addBlockPiece(getCorrockCrownStanding(rand.nextInt(16)), up);
					}
				} else {
					BlockPos offset = pos.relative(direction);
					if (world.isEmptyBlock(offset)) {
						crowns.addBlockPiece(getCorrockCrownWall(direction), offset);
					}
				}
			}
			return true;
		}
		return false;
	}

	protected static boolean tryToPlaceCrownedCorrockSquare(ISeedReader world, Random rand, int y, int x1, int z1, int x2, int z2, List<BlockPos> positions, Direction direction, GenerationPiece crowns, float crownChance) {
		BlockPos.Mutable mutable = new BlockPos.Mutable();
		for (int x = x1; x <= x2; x++) {
			for (int z = z1; z <= z2; z++) {
				if (!tryToPlaceCorrockBlockWithCrown(world, rand, mutable.set(x, y, z), positions, direction, crowns, null, crownChance)) {
					return false;
				}
			}
		}
		return true;
	}

	protected static class ChorusPlantPart {
		public final BlockPos pos;

		public ChorusPlantPart(BlockPos pos) {
			this.pos = pos;
		}

		public void placeGrowth(IWorld world, Random rand) {
			world.setBlock(this.pos, CORROCK_BLOCK_STATE.get(), 2);
			ChorusFlowerBlock.generatePlant(world, this.pos.above(), rand, 8);
		}
	}
}