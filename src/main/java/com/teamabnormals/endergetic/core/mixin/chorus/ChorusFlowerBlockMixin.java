package com.teamabnormals.endergetic.core.mixin.chorus;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.teamabnormals.endergetic.core.other.tags.EEBlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChorusFlowerBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * @author SmellyModder (Luke Tonon)
 */
@Mixin(value = ChorusFlowerBlock.class, priority = 900)
public final class ChorusFlowerBlockMixin {
    //fixes incompatibility with EP
    @WrapOperation(method = "canSurvive", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z"))
    private boolean canSurvive(BlockState instance, Block block, Operation<Boolean> original) {
        return original.call(instance, block) || instance.is(EEBlockTags.CHORUS_PLANTABLE);
    }

    @WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z"), method = "randomTick")
    private boolean isEndstone(BlockState instance, Block block, Operation<Boolean> original) {
        return original.call(instance, block) || instance.is(EEBlockTags.CHORUS_PLANTABLE);
    }
}