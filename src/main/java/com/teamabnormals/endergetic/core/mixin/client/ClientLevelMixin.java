package com.teamabnormals.endergetic.core.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.teamabnormals.endergetic.common.entity.bolloom.BolloomBalloon;
import com.teamabnormals.endergetic.core.interfaces.BalloonHolder;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.entity.EntityTickList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientLevel.class)
public abstract class ClientLevelMixin {
	@Shadow
	@Final
	EntityTickList tickingEntities;

	@WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;isPassenger()Z"), method = "*(Lnet/minecraft/world/entity/Entity;)V")
	private boolean shouldNotTick(Entity entity, Operation<Boolean> original) {
		return original.call(entity) || entity instanceof BolloomBalloon balloon && balloon.isAttachedToEntity();
	}

	@Inject(at = @At(value = "RETURN"), method = "tickNonPassenger")
	private void updateBalloons(Entity entity, CallbackInfo info) {
		BalloonHolder balloonHolder = (BalloonHolder) entity;
		for (BolloomBalloon balloon : balloonHolder.getBalloons()) {
			if (!balloon.isRemoved() && balloon.getAttachedEntity() == entity) {
				if (this.tickingEntities.contains(balloon)) {
					balloon.setOldPosAndRot();
					balloon.tickCount++;
					balloon.updateAttachedPosition();
				}
			} else {
				balloon.detachFromEntity();
			}
		}
	}

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;rideTick()V", shift = At.Shift.AFTER), method = "tickPassenger")
	private void updatePassengerBalloons(Entity ridingEntity, Entity passenger, CallbackInfo info) {
		BalloonHolder balloonHolder = (BalloonHolder) passenger;
		for (BolloomBalloon balloon : balloonHolder.getBalloons()) {
			if (!balloon.isRemoved() && balloon.getAttachedEntity() == passenger) {
				if (this.tickingEntities.contains(balloon)) {
					balloon.setOldPosAndRot();
					balloon.tickCount++;
					balloon.updateAttachedPosition();
				}
			} else {
				balloon.detachFromEntity();
			}
		}
	}
}
