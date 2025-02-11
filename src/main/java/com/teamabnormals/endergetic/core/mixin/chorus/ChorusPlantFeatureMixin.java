package com.teamabnormals.endergetic.core.mixin.chorus;

import com.teamabnormals.endergetic.core.other.tags.EEBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.ChorusFlowerBlock;
import net.minecraft.world.level.levelgen.feature.ChorusPlantFeature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChorusPlantFeature.class)
public final class ChorusPlantFeatureMixin {

	@Inject(at = @At("HEAD"), method = "place", cancellable = true)
	private void allowPlacementOnTag(FeaturePlaceContext<NoneFeatureConfiguration> context, CallbackInfoReturnable<Boolean> info) {
		WorldGenLevel level = context.level();
		BlockPos pos = context.origin();
		if (level.isEmptyBlock(pos) && level.getBlockState(pos.below()).is(EEBlockTags.CHORUS_PLANTABLE)) {
			ChorusFlowerBlock.generatePlant(level, pos, context.random(), 8);
			info.setReturnValue(true);
		}
	}
}
