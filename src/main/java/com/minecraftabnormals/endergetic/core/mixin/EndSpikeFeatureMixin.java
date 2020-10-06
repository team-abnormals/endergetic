package com.minecraftabnormals.endergetic.core.mixin;

import java.util.Random;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.minecraftabnormals.endergetic.core.registry.EEBlocks;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.feature.EndSpikeFeature;
import net.minecraft.world.gen.feature.EndSpikeFeatureConfig;

@Mixin(EndSpikeFeature.class)
public final class EndSpikeFeatureMixin {

	@Inject(at = @At("TAIL"), method = "placeSpike")
	private void placeCrystalHolder(IWorld world, Random rand, EndSpikeFeatureConfig config, EndSpikeFeature.EndSpike spike, CallbackInfo info) {
		world.setBlockState(new BlockPos(spike.getCenterX(), spike.getHeight(), spike.getCenterZ()), EEBlocks.CRYSTAL_HOLDER.get().getDefaultState(), 3);
	}

}