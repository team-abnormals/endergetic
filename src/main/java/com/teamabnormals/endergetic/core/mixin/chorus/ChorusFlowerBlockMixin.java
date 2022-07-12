package com.teamabnormals.endergetic.core.mixin.chorus;

import com.teamabnormals.endergetic.core.registry.other.EETags;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
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
	private boolean canSurvive(BlockState state, Block block) {
		return state.is(EETags.Blocks.CHORUS_PLANTABLE);
	}

	@Redirect(at = @At(value = "INVOKE", ordinal = 8), method = "randomTick")
	private BlockState isEndstone(ServerLevel level, BlockPos pos) {
		BlockState blockState = level.getBlockState(pos);
		return blockState.is(EETags.Blocks.CHORUS_PLANTABLE) ? Blocks.END_STONE.defaultBlockState() : blockState;
	}
}
