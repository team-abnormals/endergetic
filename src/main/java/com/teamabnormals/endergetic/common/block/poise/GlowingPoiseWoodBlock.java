package com.teamabnormals.endergetic.common.block.poise;

import com.teamabnormals.blueprint.common.block.wood.WoodBlock;
import com.teamabnormals.endergetic.core.registry.EEParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.function.Supplier;

public class GlowingPoiseWoodBlock extends WoodBlock {

	public GlowingPoiseWoodBlock(Supplier<Block> strippedBlock, Properties properties) {
		super(strippedBlock, properties);
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource rand) {
		if (rand.nextFloat() > 0.05F) {
			BlockPos up = pos.above();
			if (level.getBlockState(up).getCollisionShape(level, up).isEmpty()) {
				double offsetX = GlowingPoiseStemBlock.makeNegativeRandomly(rand.nextFloat() * 0.25F, rand);
				double offsetZ = GlowingPoiseStemBlock.makeNegativeRandomly(rand.nextFloat() * 0.25F, rand);

				double x = pos.getX() + 0.5D + offsetX;
				double y = pos.getY() + 0.95D + (rand.nextFloat() * 0.05F);
				double z = pos.getZ() + 0.5D + offsetZ;

				level.addParticle(EEParticleTypes.SHORT_POISE_BUBBLE.get(), x, y, z, 0.0D, 0.0D, 0.0D);
			}
		}
	}

}