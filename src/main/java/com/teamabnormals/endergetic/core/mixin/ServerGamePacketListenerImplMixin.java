package com.teamabnormals.endergetic.core.mixin;

import com.teamabnormals.endergetic.common.entity.booflo.Booflo;
import com.teamabnormals.endergetic.core.interfaces.BalloonHolder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.vehicle.Boat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerGamePacketListenerImpl.class)
public final class ServerGamePacketListenerImplMixin {
	@Shadow
	public ServerPlayer player;

	@Shadow
	private int aboveGroundVehicleTickCount;

	@Inject(at = @At("TAIL"), method = "tick()V")
	private void preventFlyingKick(CallbackInfo info) {
		Entity ridingEntity = this.player.getVehicle();
		if (ridingEntity instanceof Boat && !((BalloonHolder) ridingEntity).getBalloons().isEmpty()) {
			this.aboveGroundVehicleTickCount = 0;
		} else if (ridingEntity instanceof Booflo) {
			this.aboveGroundVehicleTickCount = 0;
		}
	}
}