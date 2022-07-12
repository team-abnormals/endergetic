package com.teamabnormals.endergetic.common.blocks.poise;

import com.teamabnormals.endergetic.client.particles.EEParticles;
import com.teamabnormals.blueprint.common.block.wood.LogBlock;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.util.function.Supplier;

public class GlowingPoiseStemBlock extends LogBlock {

	public GlowingPoiseStemBlock(Supplier<Block> strippedBlock, Properties properties) {
		super(strippedBlock, properties);
	}

	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource rand) {
		if (rand.nextFloat() > 0.05F) {
			BlockPos up = pos.above();
			if (level.getBlockState(up).getCollisionShape(level, up).isEmpty()) {
				double offsetX = makeNegativeRandomly(rand.nextFloat() * 0.25F, rand);
				double offsetZ = makeNegativeRandomly(rand.nextFloat() * 0.25F, rand);

				double x = pos.getX() + 0.5D + offsetX;
				double y = pos.getY() + 0.95D + (rand.nextFloat() * 0.05F);
				double z = pos.getZ() + 0.5D + offsetZ;

				level.addParticle(EEParticles.SHORT_POISE_BUBBLE.get(), x, y, z, 0.0D, 0.0D, 0.0D);
			}
		}
	}

	static double makeNegativeRandomly(double value, RandomSource rand) {
		return rand.nextBoolean() ? -value : value;
	}

}