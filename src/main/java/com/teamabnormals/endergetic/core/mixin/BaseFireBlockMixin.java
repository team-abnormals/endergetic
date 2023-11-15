package com.teamabnormals.endergetic.core.mixin;

import com.teamabnormals.endergetic.common.block.EnderFireBlock;
import com.teamabnormals.endergetic.core.registry.EEBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BaseFireBlock.class)
public final class BaseFireBlockMixin extends Block {

	private BaseFireBlockMixin(Properties properties) {
		super(properties);
	}

	@Inject(at = @At("HEAD"), method = "getState(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;", cancellable = true)
	private static void enderFirePlacement(BlockGetter getter, BlockPos pos, CallbackInfoReturnable<BlockState> info) {
		if (EnderFireBlock.isEnderFireBase(getter.getBlockState(pos.below()))) {
			info.setReturnValue(EEBlocks.ENDER_FIRE.get().defaultBlockState());
		}
	}

}