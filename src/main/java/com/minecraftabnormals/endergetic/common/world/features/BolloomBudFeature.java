package com.minecraftabnormals.endergetic.common.world.features;

import java.util.Random;
import java.util.function.Supplier;

import com.mojang.serialization.Codec;
import com.minecraftabnormals.abnormals_core.core.utils.MathUtils;
import com.minecraftabnormals.endergetic.api.util.GenerationUtils;
import com.minecraftabnormals.endergetic.common.blocks.poise.BolloomBudBlock;
import com.minecraftabnormals.endergetic.common.tileentities.BolloomBudTileEntity;
import com.minecraftabnormals.endergetic.common.tileentities.BolloomBudTileEntity.BudSide;
import com.minecraftabnormals.endergetic.core.registry.EEBlocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.StructureManager;

/**
 * @author - SmellyModder(Luke Tonon)
 */
public class BolloomBudFeature extends Feature<NoFeatureConfig> {
	protected static final Supplier<BlockState> BOLLOOM_BUD = () -> EEBlocks.BOLLOOM_BUD.get().getDefaultState();

	public BolloomBudFeature(Codec<NoFeatureConfig> configFactoryIn) {
		super(configFactoryIn);
	}

	@Override
	public boolean func_230362_a_(ISeedReader world, StructureManager manager, ChunkGenerator generator, Random rand, BlockPos pos, NoFeatureConfig config) {
		if(rand.nextFloat() > 0.75) {
			if(this.isValidPos(world, pos)) {
				world.setBlockState(pos, BOLLOOM_BUD.get(), 2);
				return true;
			}
		} else {
			int maxHeight = this.calculateFruitMaxHeight(world, pos);
			if(this.isValidPos(world, pos) && this.canFitCross(world, pos) && GenerationUtils.isAreaAir(world, pos.getX() - 1, pos.getY() + 1, pos.getZ() - 1, pos.getX() + 1, pos.getY() + 2, pos.getZ() + 1) && maxHeight > 1) {
				world.setBlockState(pos, BOLLOOM_BUD.get().with(BolloomBudBlock.OPENED, true), 2);
				TileEntity te = world.getTileEntity(pos);
				if(te instanceof BolloomBudTileEntity) {
					((BolloomBudTileEntity) te).startGrowing(rand, maxHeight, true);
				}
				return true;
			}
		}
		return false;
	}
	
	private boolean isValidPos(IWorld world, BlockPos pos) {
		Block block = world.getBlockState(pos.down()).getBlock();
		return world.isAirBlock(pos) && world.isAirBlock(pos.up()) && block == EEBlocks.POISMOSS.get() || block == EEBlocks.POISMOSS_EUMUS.get() || block == EEBlocks.EUMUS.get();
	}
	
	private int calculateFruitMaxHeight(IWorld world, BlockPos pos) {
		int[] maxHeights = new int[4];
		
		for(BudSide sides : BudSide.values()) {
			for(int y = 1; y < 7; y++) {
				if(world.isAirBlock(sides.offsetPosition(pos.up(y)))) {
					maxHeights[sides.id] = y;
					continue;
				} else {
					break;
				}
			}
		}
		
		return MathUtils.getLowestValueInIntArray(maxHeights);
	}
	
	private boolean canFitCross(IWorld world, BlockPos pos) {
		for(BudSide sides : BudSide.values()) {
			if(!world.isAirBlock(sides.offsetPosition(pos))) {
				return false;
			}
		}
		return !BolloomBudBlock.isAcrossOrAdjacentToBud(world, pos);
	}
}