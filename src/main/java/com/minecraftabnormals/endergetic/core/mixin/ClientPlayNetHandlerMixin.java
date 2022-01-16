package com.minecraftabnormals.endergetic.core.mixin;

import com.minecraftabnormals.endergetic.common.entities.booflo.BoofloEntity;
import com.minecraftabnormals.endergetic.core.interfaces.BalloonHolder;
import com.minecraftabnormals.endergetic.core.keybinds.KeybindHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.play.ClientPlayNetHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.play.server.SChangeGameStatePacket;
import net.minecraft.network.play.server.SSetPassengersPacket;
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
	private Minecraft minecraft;
	@Shadow
	private ClientWorld level;

	@Inject(at = @At("RETURN"), method = "handleGameEvent")
	private void detachBalloons(SChangeGameStatePacket packet, CallbackInfo info) {
		if (this.minecraft.gameMode.getPlayerMode() == GameType.SPECTATOR) {
			BalloonHolder holder = (BalloonHolder) this.minecraft.player;
			if (!holder.getBalloons().isEmpty()) {
				holder.detachBalloons();
			}
		}
	}

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/IngameGui;setOverlayMessage(Lnet/minecraft/util/text/ITextComponent;Z)V", shift = At.Shift.AFTER), method = "handleSetEntityPassengersPacket")
	private void setBoofloRidingOverlayMessage(SSetPassengersPacket packetIn, CallbackInfo info) {
		if (this.level.getEntity(packetIn.getVehicle()) instanceof BoofloEntity) {
			this.minecraft.gui.setOverlayMessage(new TranslationTextComponent("overlay.mount.booflo", this.minecraft.options.keyShift.getTranslatedKeyMessage(), KeybindHandler.BOOFLO_SLAM.getTranslatedKeyMessage()), false);
		}
	}
}
