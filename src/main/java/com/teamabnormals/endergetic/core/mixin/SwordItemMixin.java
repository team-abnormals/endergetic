package com.teamabnormals.endergetic.core.mixin;

import com.teamabnormals.endergetic.core.registry.EEBlocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SwordItem.class)
public final class SwordItemMixin {

	@Inject(at = @At(value = "HEAD"), method = "getDestroySpeed", cancellable = true)
	private void modifyDestroySpeed(ItemStack stack, BlockState state, CallbackInfoReturnable<Float> info) {
		if (state.getBlock() == EEBlocks.EETLE_EGG.get()) {
			info.setReturnValue(16.0F);
		}
	}

}
