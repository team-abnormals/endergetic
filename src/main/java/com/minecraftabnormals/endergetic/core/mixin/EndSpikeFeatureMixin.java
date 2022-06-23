package com.minecraftabnormals.endergetic.core.mixin;

import java.util.Random;

import net.minecraft.world.level.ServerLevelAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.minecraftabnormals.endergetic.core.registry.EEBlocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.feature.SpikeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.SpikeConfiguration;

@Mixin(SpikeFeature.class)
public final class EndSpikeFeatureMixin {

	@Inject(at = @At("TAIL"), method = "placeSpike")
	private void placeCrystalHolder(ServerLevelAccessor world, Random rand, SpikeConfiguration config, SpikeFeature.EndSpike spike, CallbackInfo info) {
		world.setBlock(new BlockPos(spike.getCenterX(), spike.getHeight(), spike.getCenterZ()), EEBlocks.CRYSTAL_HOLDER.get().defaultBlockState(), 3);
	}

}