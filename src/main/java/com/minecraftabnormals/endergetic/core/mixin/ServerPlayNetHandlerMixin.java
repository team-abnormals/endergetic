package com.minecraftabnormals.endergetic.core.mixin;

import com.minecraftabnormals.endergetic.common.entities.booflo.BoofloEntity;
import com.minecraftabnormals.endergetic.core.interfaces.BalloonHolder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.play.ServerPlayNetHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetHandler.class)
public final class ServerPlayNetHandlerMixin {
	@Shadow
	private ServerPlayerEntity player;

	@Shadow
	private int aboveGroundVehicleTickCount;

	@Inject(at = @At("TAIL"), method = "tick()V")
	private void preventFlyingKick(CallbackInfo info) {
		Entity ridingEntity = this.player.getVehicle();
		if (ridingEntity instanceof BoatEntity && !((BalloonHolder) ridingEntity).getBalloons().isEmpty()) {
			this.aboveGroundVehicleTickCount = 0;
		} else if (ridingEntity instanceof BoofloEntity) {
			this.aboveGroundVehicleTickCount = 0;
		}
	}
}