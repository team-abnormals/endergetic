package com.teamabnormals.endergetic.core.mixin.client;

import com.teamabnormals.endergetic.common.entity.booflo.Booflo;
import com.teamabnormals.endergetic.common.entity.eetle.GliderEetle;
import com.teamabnormals.endergetic.core.interfaces.BalloonHolder;
import com.teamabnormals.endergetic.core.keybinds.KeybindHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.network.protocol.game.ClientboundSetPassengersPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.GameType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public final class ClientPacketListenerMixin {
	@Shadow
	private Minecraft minecraft;
	@Shadow
	private ClientLevel level;

	@Inject(at = @At("RETURN"), method = "handleGameEvent")
	private void detachBalloons(ClientboundGameEventPacket packet, CallbackInfo info) {
		if (packet.getEvent() == ClientboundGameEventPacket.CHANGE_GAME_MODE && this.minecraft.gameMode.getPlayerMode() == GameType.SPECTATOR) {
			BalloonHolder holder = (BalloonHolder) this.minecraft.player;
			if (!holder.getBalloons().isEmpty()) {
				holder.detachBalloons();
			}
		}
	}

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;setOverlayMessage(Lnet/minecraft/network/chat/Component;Z)V", shift = At.Shift.AFTER), method = "handleSetEntityPassengersPacket")
	private void setBoofloRidingOverlayMessage(ClientboundSetPassengersPacket packet, CallbackInfo info) {
		Entity entity = this.level.getEntity(packet.getVehicle());
		if (entity instanceof Booflo) {
			this.minecraft.gui.setOverlayMessage(Component.translatable("overlay.mount.booflo", this.minecraft.options.keyShift.getTranslatedKeyMessage(), KeybindHandler.BOOFLO_SLAM.getTranslatedKeyMessage()), false);
		} else if (entity instanceof GliderEetle) {
			this.minecraft.gui.setOverlayMessage(Component.empty(), false);
		}
	}
}
