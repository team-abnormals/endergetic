package com.teamabnormals.endergetic.core.mixin;

import com.teamabnormals.endergetic.core.registry.EEBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.levelgen.feature.SpikeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.SpikeConfiguration;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SpikeFeature.class)
public final class SpikeFeatureMixin {

	@Inject(at = @At("TAIL"), method = "placeSpike")
	private void placeCrystalHolder(ServerLevelAccessor level, RandomSource rand, SpikeConfiguration config, SpikeFeature.EndSpike spike, CallbackInfo info) {
		level.setBlock(new BlockPos(spike.getCenterX(), spike.getHeight(), spike.getCenterZ()), EEBlocks.CRYSTAL_HOLDER.get().defaultBlockState(), 3);
	}

}