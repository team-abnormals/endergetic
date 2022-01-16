package com.minecraftabnormals.endergetic.core.mixin.chorus;

import com.minecraftabnormals.endergetic.core.registry.other.EETags;
import net.minecraft.block.*;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ChorusPlantBlock.class)
public final class ChorusPlantBlockMixin {

	@Inject(at = @At(value = "RETURN", shift = At.Shift.BEFORE), method = "getStateForPlacement(Lnet/minecraft/world/IBlockReader;Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/BlockState;", locals = LocalCapture.CAPTURE_FAILSOFT, cancellable = true)
	private void makePlantConnections(IBlockReader blockReader, BlockPos pos, CallbackInfoReturnable<BlockState> info, Block block, Block block1, Block block2, Block block3, Block block4, Block block5) {
		Block thisBlock = (Block) (Object) this;
		if (block.is(EETags.Blocks.CHORUS_PLANTABLE)) {
			info.setReturnValue(Blocks.CHORUS_PLANT.defaultBlockState().setValue(SixWayBlock.DOWN, true).setValue(SixWayBlock.UP, block1 == thisBlock || block1 == Blocks.CHORUS_FLOWER).setValue(SixWayBlock.NORTH, block2 == thisBlock || block2 == Blocks.CHORUS_FLOWER).setValue(SixWayBlock.EAST, block3 == thisBlock || block3 == Blocks.CHORUS_FLOWER).setValue(SixWayBlock.SOUTH, block4 == thisBlock || block4 == Blocks.CHORUS_FLOWER).setValue(SixWayBlock.WEST, block5 == thisBlock || block5 == Blocks.CHORUS_FLOWER));
		} else if (block == Blocks.END_STONE) {
			info.setReturnValue(Blocks.CHORUS_PLANT.defaultBlockState().setValue(SixWayBlock.DOWN, false).setValue(SixWayBlock.UP, block1 == thisBlock || block1 == Blocks.CHORUS_FLOWER).setValue(SixWayBlock.NORTH, block2 == thisBlock || block2 == Blocks.CHORUS_FLOWER).setValue(SixWayBlock.EAST, block3 == thisBlock || block3 == Blocks.CHORUS_FLOWER).setValue(SixWayBlock.SOUTH, block4 == thisBlock || block4 == Blocks.CHORUS_FLOWER).setValue(SixWayBlock.WEST, block5 == thisBlock || block5 == Blocks.CHORUS_FLOWER));
		}
	}

	@Inject(at = @At(value = "JUMP", ordinal = 1, shift = At.Shift.AFTER), method = "updateShape", cancellable = true)
	private void updateShape(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos, CallbackInfoReturnable<BlockState> info) {
		if (facing == Direction.DOWN && facingState.is(EETags.Blocks.CHORUS_PLANTABLE)) {
			info.setReturnValue(stateIn.setValue(SixWayBlock.PROPERTY_BY_DIRECTION.get(facing), true));
		} else {
			info.setReturnValue(stateIn.setValue(SixWayBlock.PROPERTY_BY_DIRECTION.get(facing), facingState.getBlock() == (Object) this || facingState.is(Blocks.CHORUS_FLOWER)));
		}
	}

	/**
	 * Overwrite moment...
	 * Anyways this'll likely be a Forge PR eventually.
	 * TODO: Make this not overwrite
	 */
	@SuppressWarnings("deprecation")
	@Overwrite
	public boolean canSurvive(BlockState state, IWorldReader worldIn, BlockPos pos) {
		BlockState downState = worldIn.getBlockState(pos.below());
		boolean flag = !worldIn.getBlockState(pos.above()).isAir() && !downState.isAir();

		for (Direction direction : Direction.Plane.HORIZONTAL) {
			BlockPos offset = pos.relative(direction);
			Block block = worldIn.getBlockState(offset).getBlock();
			if (block == (Object) this) {
				if (flag) {
					return false;
				}

				Block offsetDownBlock = worldIn.getBlockState(offset.below()).getBlock();
				if (offsetDownBlock == (Object) this || offsetDownBlock.is(EETags.Blocks.CHORUS_PLANTABLE)) {
					return true;
				}
			}
		}
		Block block = downState.getBlock();
		return block == (Object) this || block.is(EETags.Blocks.CHORUS_PLANTABLE);
	}

}
