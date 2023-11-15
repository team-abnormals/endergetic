package com.teamabnormals.endergetic.core.mixin.chorus;

import com.teamabnormals.endergetic.core.registry.other.tags.EEBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChorusFlowerBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * @author SmellyModder (Luke Tonon)
 */
@Mixin(ChorusFlowerBlock.class)
public final class ChorusFlowerBlockMixin {

	@Redirect(at = @At(value = "INVOKE", ordinal = 3), method = "canSurvive")
	private boolean canSurvive(BlockState state, Block block) {
		return state.is(EEBlockTags.CHORUS_PLANTABLE);
	}

	@Redirect(at = @At(value = "INVOKE", ordinal = 8), method = "randomTick")
	private BlockState isEndstone(ServerLevel level, BlockPos pos) {
		BlockState blockState = level.getBlockState(pos);
		return blockState.is(EEBlockTags.CHORUS_PLANTABLE) ? Blocks.END_STONE.defaultBlockState() : blockState;
	}
}
