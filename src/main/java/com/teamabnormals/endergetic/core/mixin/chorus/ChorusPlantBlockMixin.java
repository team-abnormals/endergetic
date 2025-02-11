package com.teamabnormals.endergetic.core.mixin.chorus;

import com.teamabnormals.endergetic.core.other.tags.EEBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.ChorusPlantBlock;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ChorusPlantBlock.class)
public final class ChorusPlantBlockMixin extends PipeBlock {

	public ChorusPlantBlockMixin(float f, Properties properties) {
		super(f, properties);
	}

	@Inject(at = @At(value = "RETURN"), method = "Lnet/minecraft/world/level/block/ChorusPlantBlock;getStateForPlacement(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;", locals = LocalCapture.CAPTURE_FAILSOFT, cancellable = true)
	private void getStateForPlacement(BlockGetter level, BlockPos pos, CallbackInfoReturnable<BlockState> cir) {
		if (level.getBlockState(pos.below()).is(EEBlockTags.CHORUS_PLANTABLE)) {
			cir.setReturnValue(cir.getReturnValue().setValue(PipeBlock.DOWN, true));
		}
	}

	@Inject(at = @At(value = "RETURN"), method = "updateShape", cancellable = true)
	private void updatePostPlacement(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos pos, BlockPos facingPos, CallbackInfoReturnable<BlockState> cir) {
		if (state.canSurvive(level, pos)) {
			if (facing == Direction.DOWN && facingState.is(EEBlockTags.CHORUS_PLANTABLE)) {
				cir.setReturnValue(cir.getReturnValue().setValue(PipeBlock.PROPERTY_BY_DIRECTION.get(facing), true));
			}
		}
	}

	@Inject(at = @At(value = "RETURN"), method = "canSurvive", cancellable = true)
	private void canSurvive(BlockState state, LevelReader level, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
		BlockState downState = level.getBlockState(pos.below());
		boolean flag = !level.getBlockState(pos.above()).isAir() && !downState.isAir();
		boolean canReturn = true;

		for (Direction direction : Direction.Plane.HORIZONTAL) {
			BlockPos offset = pos.relative(direction);
			BlockState block = level.getBlockState(offset);
			if (block.is(this)) {
				if (flag) {
					canReturn = false;
				}

				BlockState offsetDownBlock = level.getBlockState(offset.below());
				if (canReturn && offsetDownBlock.is(EEBlockTags.CHORUS_PLANTABLE)) {
					cir.setReturnValue(true);
				}
			}
		}
		if (canReturn && downState.is(EEBlockTags.CHORUS_PLANTABLE)) {
			cir.setReturnValue(true);
		}
	}
}
