package com.minecraftabnormals.endergetic.core.mixin.chorus;

import com.minecraftabnormals.endergetic.core.registry.other.EETags;
import net.minecraft.block.*;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.ChorusPlantFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

import net.minecraft.world.level.block.ChorusFlowerBlock;

@Mixin(ChorusPlantFeature.class)
public final class ChorusPlantFeatureMixin {

	@Inject(at = @At("HEAD"), method = "place", cancellable = true)
	private void allowPlacementOnTag(WorldGenLevel seedReader, ChunkGenerator p_230362_3_, Random p_230362_4_, BlockPos pos, NoneFeatureConfiguration p_230362_6_, CallbackInfoReturnable<Boolean> info) {
		if (seedReader.isEmptyBlock(pos) && seedReader.getBlockState(pos.below()).is(EETags.Blocks.CHORUS_PLANTABLE)) {
			ChorusFlowerBlock.generatePlant(seedReader, pos, p_230362_4_, 8);
			info.setReturnValue(true);
		}
	}

}
