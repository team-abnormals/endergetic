package com.minecraftabnormals.endergetic.common.blocks.poise;

import com.minecraftabnormals.abnormals_core.common.blocks.wood.WoodBlock;
import com.minecraftabnormals.abnormals_core.core.util.MathUtil;
import com.minecraftabnormals.endergetic.client.particles.EEParticles;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;
import java.util.function.Supplier;

public class GlowingPoiseWoodBlock extends WoodBlock {

	public GlowingPoiseWoodBlock(Supplier<Block> strippedBlock, Properties properties) {
		super(strippedBlock, properties);
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
		if (rand.nextFloat() > 0.05F) {
			BlockPos up = pos.up();
			if (worldIn.getBlockState(up).getCollisionShape(worldIn, up).isEmpty()) {
				double offsetX = MathUtil.makeNegativeRandomly(rand.nextFloat() * 0.25F, rand);
				double offsetZ = MathUtil.makeNegativeRandomly(rand.nextFloat() * 0.25F, rand);

				double x = pos.getX() + 0.5D + offsetX;
				double y = pos.getY() + 0.95D + (rand.nextFloat() * 0.05F);
				double z = pos.getZ() + 0.5D + offsetZ;

				worldIn.addParticle(EEParticles.SHORT_POISE_BUBBLE.get(), x, y, z, 0.0D, 0.0D, 0.0D);
			}
		}
	}

}