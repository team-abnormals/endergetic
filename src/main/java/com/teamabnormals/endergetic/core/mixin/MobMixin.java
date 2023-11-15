package com.teamabnormals.endergetic.core.mixin;

import com.teamabnormals.endergetic.common.entity.eetle.GliderEetle;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.MoveControl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Mob.class)
public final class MobMixin {
	@Shadow
	protected MoveControl moveControl;

	/**
	 * Vanilla doesn't check for instanceof on certain mobs when casting to their custom {@link MoveControl}.
	 */
	@Inject(at = @At("HEAD"), method = "getMoveControl", cancellable = true)
	private void getMoveHelper(CallbackInfoReturnable<MoveControl> info) {
		Mob thisMob = (Mob) (Object) this;
		if (thisMob.isPassenger() && thisMob.getVehicle() instanceof GliderEetle) {
			info.setReturnValue(this.moveControl);
		}
	}
}
