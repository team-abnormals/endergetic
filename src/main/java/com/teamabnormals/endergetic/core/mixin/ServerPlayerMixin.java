package com.teamabnormals.endergetic.core.mixin;

import com.teamabnormals.endergetic.core.interfaces.BalloonHolder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayer.class)
public final class ServerPlayerMixin {

	@Inject(at = @At(value = "JUMP", ordinal = 0, shift = At.Shift.AFTER), method = "setGameMode")
	private void detachBalloons(GameType gameType, CallbackInfoReturnable<Boolean> info) {
		BalloonHolder holder = (BalloonHolder) (Object) this;
		if (!holder.getBalloons().isEmpty()) {
			holder.detachBalloons();
		}
	}

}
