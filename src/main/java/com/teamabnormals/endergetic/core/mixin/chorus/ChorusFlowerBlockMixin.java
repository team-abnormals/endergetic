package com.teamabnormals.endergetic.core.mixin.chorus;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.teamabnormals.endergetic.core.other.tags.EEBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChorusFlowerBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * @author SmellyModder (Luke Tonon)
 */
@Mixin(ChorusFlowerBlock.class)
public final class ChorusFlowerBlockMixin {

	@WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z"), method = "canSurvive")
	private boolean canSurvive(BlockState state, Block block, Operation<Boolean> original) {
		return original.call(state, block) || block == Blocks.END_STONE && state.is(EEBlockTags.CHORUS_PLANTABLE);
	}

	@WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z"), method = "randomTick")
	private boolean randomTick(BlockState state, Block block, Operation<Boolean> original) {
		return original.call(state, block) || block == Blocks.END_STONE && state.is(EEBlockTags.CHORUS_PLANTABLE);
	}
}