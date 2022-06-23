package com.minecraftabnormals.endergetic.core.mixin;

import com.minecraftabnormals.endergetic.core.interfaces.BalloonHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.minecraftabnormals.endergetic.common.entities.booflo.BoofloEntity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

@Mixin(ServerGamePacketListenerImpl.class)
public final class ServerPlayNetHandlerMixin {
	@Shadow
	private ServerPlayer player;

	@Shadow
	private int aboveGroundVehicleTickCount;

	@Inject(at = @At("TAIL"), method = "tick()V")
	private void preventFlyingKick(CallbackInfo info) {
		Entity ridingEntity = this.player.getVehicle();
		if (ridingEntity instanceof Boat && !((BalloonHolder) ridingEntity).getBalloons().isEmpty()) {
			this.aboveGroundVehicleTickCount = 0;
		} else if (ridingEntity instanceof BoofloEntity) {
			this.aboveGroundVehicleTickCount = 0;
		}
	}
}