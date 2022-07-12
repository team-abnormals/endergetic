package com.teamabnormals.endergetic.common.blocks;

import java.util.Random;

import com.teamabnormals.endergetic.client.particles.EEParticles;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.TorchBlock;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class EnderTorchBlock extends TorchBlock {

	public EnderTorchBlock(Properties properties) {
		super(properties, ParticleTypes.FLAME);
	}

	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState stateIn, Level worldIn, BlockPos pos, Random rand) {
		double d0 = (double) pos.getX() + 0.5D;
		double d1 = (double) pos.getY() + 0.7D;
		double d2 = (double) pos.getZ() + 0.5D;
		worldIn.addParticle(ParticleTypes.SMOKE, d0, d1, d2, 0.0D, 0.0D, 0.0D);
		worldIn.addParticle(EEParticles.ENDER_FLAME.get(), d0, d1, d2, 0.0D, 0.0D, 0.0D);
	}

}	
