package com.minecraftabnormals.endergetic.common.blocks;

import java.util.Random;

import com.minecraftabnormals.endergetic.common.tileentities.EnderCampfireTileEntity;

import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import net.minecraft.block.AbstractBlock.Properties;

public class EnderCampfireBlock extends CampfireBlock {

	public EnderCampfireBlock(Properties properties) {
		super(false, 3, properties);
	}

	public TileEntity newBlockEntity(IBlockReader worldIn) {
		return new EnderCampfireTileEntity();
	}

	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
		if (stateIn.getValue(LIT)) {
			if (rand.nextInt(10) == 0) {
				worldIn.playLocalSound((double) ((float) pos.getX() + 0.5F), (double) ((float) pos.getY() + 0.5F), (double) ((float) pos.getZ() + 0.5F), SoundEvents.CAMPFIRE_CRACKLE, SoundCategory.BLOCKS, 0.5F + rand.nextFloat(),
						rand.nextFloat() * 0.7F + 0.6F, false);
			}
		}
	}
}
