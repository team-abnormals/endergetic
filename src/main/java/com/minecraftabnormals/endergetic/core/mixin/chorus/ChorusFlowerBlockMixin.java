package com.minecraftabnormals.endergetic.core.mixin.chorus;

import com.minecraftabnormals.endergetic.core.registry.other.EETags;
import net.minecraft.block.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Random;

/**
 * @author SmellyModder (Luke Tonon)
 */
@Mixin(ChorusFlowerBlock.class)
public final class ChorusFlowerBlockMixin {
	@Shadow
	@Final
	private ChorusPlantBlock plantBlock;

	@Redirect(at = @At(value = "INVOKE", ordinal = 3), method = "isValidPosition")
	private boolean isValidPosition(BlockState state, Block block) {
		return state.isIn(EETags.Blocks.CHORUS_PLANTABLE);
	}

	@Redirect(at = @At(value = "JUMP", opcode = Opcodes.IF_ACMPNE, shift = At.Shift.BEFORE, ordinal = 0), method = "randomTick")
	private Block isEndStone(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		Block block = world.getBlockState(pos.down()).getBlock();
		return block.isIn(EETags.Blocks.CHORUS_PLANTABLE) ? Blocks.END_STONE : null;
	}

	@Redirect(at = @At(value = "INVOKE", ordinal = 8), method = "randomTick")
	private Block isEndstone(BlockState state) {
		Block block = state.getBlock();
		if (block.isIn(EETags.Blocks.CHORUS_PLANTABLE)) {
			return Blocks.END_STONE;
		} else if (block == Blocks.END_STONE) {
			return this.plantBlock;
		}
		return block;
	}
}
