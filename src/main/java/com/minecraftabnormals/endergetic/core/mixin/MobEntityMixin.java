package com.minecraftabnormals.endergetic.core.mixin;

import com.minecraftabnormals.endergetic.common.entities.eetle.GliderEetleEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.controller.MovementController;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MobEntity.class)
public final class MobEntityMixin {
	@Shadow
	protected MovementController moveController;

	/**
	 * Vanilla doesn't check for instanceof on certain mobs when casting to their custom {@link MovementController}.
	 */
	@Inject(at = @At("HEAD"), method = "getMoveHelper", cancellable = true)
	private void getMoveHelper(CallbackInfoReturnable<MovementController> info) {
		MobEntity thisMob = (MobEntity) (Object) this;
		if (thisMob.isPassenger() && thisMob.getRidingEntity() instanceof GliderEetleEntity) {
			info.setReturnValue(this.moveController);
		}
	}
}
