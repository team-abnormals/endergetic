package com.minecraftabnormals.endergetic.core.mixin;

import java.util.UUID;
import java.util.stream.Collectors;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.minecraftabnormals.endergetic.common.entities.bolloom.BolloomBalloonEntity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.BoatEntity;

@Mixin(Entity.class)
public final class MixinEntity {
	@Shadow
	private UUID entityUniqueID;
	
	@Inject(at = @At("HEAD"), method = "onRemovedFromWorld()V", remap = false)
	private void onRemovedFromWorld(CallbackInfo info) {
		if ((Object) this instanceof BoatEntity) {
			BolloomBalloonEntity.BALLOONS_ON_BOAT_MAP.remove(this.entityUniqueID);
		}
	}
	
	@Inject(at = @At("HEAD"), method = "canFitPassenger", cancellable = true)
	private void canFitPassenger(Entity passenger, CallbackInfoReturnable<Boolean> info) {
		if ((Object) this instanceof LivingEntity) {
			info.setReturnValue(((Entity) (Object) this).getPassengers().stream().filter(entity -> !(entity instanceof BolloomBalloonEntity)).collect(Collectors.toList()).size() < 1);
		}
	}
}