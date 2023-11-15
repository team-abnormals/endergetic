package com.teamabnormals.endergetic.core.mixin.chorus;

import com.teamabnormals.endergetic.core.registry.other.tags.EEBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChorusPlantBlock;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ChorusPlantBlock.class)
public final class ChorusPlantBlockMixin {

	@Inject(at = @At(value = "RETURN", shift = At.Shift.BEFORE), method = "Lnet/minecraft/world/level/block/ChorusPlantBlock;getStateForPlacement(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;", locals = LocalCapture.CAPTURE_FAILSOFT, cancellable = true)
	private void makePlantConnections(BlockGetter blockReader, BlockPos pos, CallbackInfoReturnable<BlockState> info, BlockState block, BlockState block1, BlockState block2, BlockState block3, BlockState block4, BlockState block5) {
		Block thisBlock = (Block) (Object) this;
		if (block.is(EEBlockTags.CHORUS_PLANTABLE)) {
			info.setReturnValue(Blocks.CHORUS_PLANT.defaultBlockState().setValue(PipeBlock.DOWN, true).setValue(PipeBlock.UP, block1.is(thisBlock) || block1.is(Blocks.CHORUS_FLOWER)).setValue(PipeBlock.NORTH, block2.is(thisBlock) || block2.is(Blocks.CHORUS_FLOWER)).setValue(PipeBlock.EAST, block3.is(thisBlock) || block3.is(Blocks.CHORUS_FLOWER)).setValue(PipeBlock.SOUTH, block4.is(thisBlock) || block4.is(Blocks.CHORUS_FLOWER)).setValue(PipeBlock.WEST, block5.is(thisBlock) || block5.is(Blocks.CHORUS_FLOWER)));
		} else if (block.is(Blocks.END_STONE)) {
			info.setReturnValue(Blocks.CHORUS_PLANT.defaultBlockState().setValue(PipeBlock.DOWN, false).setValue(PipeBlock.UP, block1.is(thisBlock) || block1.is(Blocks.CHORUS_FLOWER)).setValue(PipeBlock.NORTH, block2.is(thisBlock) || block2.is(Blocks.CHORUS_FLOWER)).setValue(PipeBlock.EAST, block3.is(thisBlock) || block3.is(Blocks.CHORUS_FLOWER)).setValue(PipeBlock.SOUTH, block4.is(thisBlock) || block4.is(Blocks.CHORUS_FLOWER)).setValue(PipeBlock.WEST, block5.is(thisBlock) || block5.is(Blocks.CHORUS_FLOWER)));
		}
	}

	@Inject(at = @At(value = "JUMP", ordinal = 1, shift = At.Shift.AFTER), method = "updateShape", cancellable = true)
	private void updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor levelAccessor, BlockPos currentPos, BlockPos facingPos, CallbackInfoReturnable<BlockState> info) {
		if (facing == Direction.DOWN && facingState.is(EEBlockTags.CHORUS_PLANTABLE)) {
			info.setReturnValue(stateIn.setValue(PipeBlock.PROPERTY_BY_DIRECTION.get(facing), true));
		} else {
			info.setReturnValue(stateIn.setValue(PipeBlock.PROPERTY_BY_DIRECTION.get(facing), facingState.getBlock() == (Object) this || facingState.is(Blocks.CHORUS_FLOWER)));
		}
	}

	/**
	 * Overwrite moment...
	 * Anyways this'll likely be a Forge PR eventually.
	 * TODO: Make this not overwrite
	 */
	@Overwrite
	public boolean canSurvive(BlockState state, LevelReader worldIn, BlockPos pos) {
		BlockState downState = worldIn.getBlockState(pos.below());
		boolean flag = !worldIn.getBlockState(pos.above()).isAir() && !downState.isAir();

		for (Direction direction : Direction.Plane.HORIZONTAL) {
			BlockPos offset = pos.relative(direction);
			Block block = worldIn.getBlockState(offset).getBlock();
			if (block == (Object) this) {
				if (flag) {
					return false;
				}

				BlockState offsetDownBlock = worldIn.getBlockState(offset.below());
				if (offsetDownBlock.is((Block) (Object) this) || offsetDownBlock.is(EEBlockTags.CHORUS_PLANTABLE)) {
					return true;
				}
			}
		}
		return downState.is((Block) (Object) this) || downState.is(EEBlockTags.CHORUS_PLANTABLE);
	}

}
