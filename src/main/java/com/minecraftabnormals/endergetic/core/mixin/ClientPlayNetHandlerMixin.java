package com.minecraftabnormals.endergetic.core.mixin;

import com.minecraftabnormals.endergetic.common.entities.booflo.BoofloEntity;
import com.minecraftabnormals.endergetic.common.entities.eetle.GliderEetleEntity;
import com.minecraftabnormals.endergetic.core.interfaces.BalloonHolder;
import com.minecraftabnormals.endergetic.core.keybinds.KeybindHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.network.protocol.game.ClientboundSetPassengersPacket;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.GameType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public final class ClientPlayNetHandlerMixin {
	@Shadow
	private Minecraft minecraft;
	@Shadow
	private ClientLevel level;

	@Inject(at = @At("RETURN"), method = "handleGameEvent")
	private void detachBalloons(ClientboundGameEventPacket packet, CallbackInfo info) {
		if (this.minecraft.gameMode.getPlayerMode() == GameType.SPECTATOR) {
			BalloonHolder holder = (BalloonHolder) this.minecraft.player;
			if (!holder.getBalloons().isEmpty()) {
				holder.detachBalloons();
			}
		}
	}

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/IngameGui;setOverlayMessage(Lnet/minecraft/util/text/ITextComponent;Z)V", shift = At.Shift.AFTER), method = "handleSetEntityPassengersPacket")
	private void setBoofloRidingOverlayMessage(ClientboundSetPassengersPacket packetIn, CallbackInfo info) {
		Entity entity = this.level.getEntity(packetIn.getVehicle());
		if (entity instanceof BoofloEntity) {
			this.minecraft.gui.setOverlayMessage(new TranslatableComponent("overlay.mount.booflo", this.minecraft.options.keyShift.getTranslatedKeyMessage(), KeybindHandler.BOOFLO_SLAM.getTranslatedKeyMessage()), false);
		} else if (entity instanceof GliderEetleEntity) {
			this.minecraft.gui.setOverlayMessage(TextComponent.EMPTY, false);
		}
	}
}
