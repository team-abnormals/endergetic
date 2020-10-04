package com.minecraftabnormals.endergetic.core.mixin;

import com.minecraftabnormals.endergetic.core.interfaces.BalloonHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.play.ClientPlayNetHandler;
import net.minecraft.network.play.server.SChangeGameStatePacket;
import net.minecraft.world.GameType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetHandler.class)
public final class ClientPlayNetHandlerMixin {
	@Shadow
	private Minecraft client;

	@Inject(at = @At("RETURN"), method = "handleChangeGameState")
	private void detachBalloons(SChangeGameStatePacket packet, CallbackInfo info) {
		if (this.client.playerController.getCurrentGameType() == GameType.SPECTATOR) {
			BalloonHolder holder = (BalloonHolder) this.client.player;
			if (!holder.getBalloons().isEmpty()) {
				holder.detachBalloons();
			}
		}
	}
}
