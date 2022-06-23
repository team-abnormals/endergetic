package com.minecraftabnormals.endergetic.core.mixin.chorus;

import com.minecraftabnormals.endergetic.core.registry.other.EETags;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChorusFlowerBlock;
import net.minecraft.world.level.block.ChorusPlantBlock;
import net.minecraft.world.level.block.state.BlockState;

/**
 * @author SmellyModder (Luke Tonon)
 */
@Mixin(ChorusFlowerBlock.class)
public final class ChorusFlowerBlockMixin {
	@Shadow
	@Final
	private ChorusPlantBlock plant;

	@Redirect(at = @At(value = "INVOKE", ordinal = 3), method = "canSurvive")
	private boolean isValidPosition(BlockState state, Block block) {
		return state.is(EETags.Blocks.CHORUS_PLANTABLE);
	}

	@Redirect(at = @At(value = "JUMP", opcode = Opcodes.IF_ACMPNE, shift = At.Shift.BEFORE, ordinal = 0), method = "randomTick")
	private Block isEndStone(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
		BlockState blockState = world.getBlockState(pos.below());
		return blockState.is(EETags.Blocks.CHORUS_PLANTABLE) ? Blocks.END_STONE : null;
	}

	@Redirect(at = @At(value = "INVOKE", ordinal = 8), method = "randomTick")
	private Block isEndstone(BlockState state) {
		Block block = state.getBlock();
		if (state.is(EETags.Blocks.CHORUS_PLANTABLE)) {
			return Blocks.END_STONE;
		} else if (block == Blocks.END_STONE) {
			return this.plant;
		}
		return block;
	}
}
