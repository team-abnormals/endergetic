package com.minecraftabnormals.endergetic.core.mixin;

import com.minecraftabnormals.endergetic.core.interfaces.BalloonHolder;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.GameType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public final class ServerPlayerEntityMixin {

	@Inject(at = @At(value = "JUMP", ordinal = 0, shift = At.Shift.AFTER), method = "setGameType")
	private void detachBalloons(GameType gameType, CallbackInfo info) {
		BalloonHolder holder = (BalloonHolder) (Object) this;
		if (!holder.getBalloons().isEmpty()) {
			holder.detachBalloons();
		}
	}

}
