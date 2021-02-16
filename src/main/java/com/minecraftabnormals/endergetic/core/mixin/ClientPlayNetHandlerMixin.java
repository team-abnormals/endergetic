package com.minecraftabnormals.endergetic.core.mixin;

import com.minecraftabnormals.endergetic.common.entities.booflo.BoofloEntity;
import com.minecraftabnormals.endergetic.common.entities.eetle.GliderEetleEntity;
import com.minecraftabnormals.endergetic.core.interfaces.BalloonHolder;
import com.minecraftabnormals.endergetic.core.keybinds.KeybindHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.play.ClientPlayNetHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.server.SChangeGameStatePacket;
import net.minecraft.network.play.server.SSetPassengersPacket;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
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
	@Shadow
	private ClientWorld world;

	@Inject(at = @At("RETURN"), method = "handleChangeGameState")
	private void detachBalloons(SChangeGameStatePacket packet, CallbackInfo info) {
		if (this.client.playerController.getCurrentGameType() == GameType.SPECTATOR) {
			BalloonHolder holder = (BalloonHolder) this.client.player;
			if (!holder.getBalloons().isEmpty()) {
				holder.detachBalloons();
			}
		}
	}

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/IngameGui;setOverlayMessage(Lnet/minecraft/util/text/ITextComponent;Z)V", shift = At.Shift.AFTER), method = "handleSetPassengers")
	private void setBoofloRidingOverlayMessage(SSetPassengersPacket packetIn, CallbackInfo info) {
		Entity entity = this.world.getEntityByID(packetIn.getEntityId());
		if (entity instanceof BoofloEntity) {
			this.client.ingameGUI.setOverlayMessage(new TranslationTextComponent("overlay.mount.booflo", this.client.gameSettings.keyBindSneak.func_238171_j_(), KeybindHandler.BOOFLO_SLAM.func_238171_j_()), false);
		} else if (entity instanceof GliderEetleEntity) {
			this.client.ingameGUI.setOverlayMessage(StringTextComponent.EMPTY, false);
		}
	}
}
