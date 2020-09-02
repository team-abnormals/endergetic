package com.minecraftabnormals.endergetic.core.mixin;

import com.minecraftabnormals.endergetic.common.entities.bolloom.BolloomBalloonEntity;
import com.minecraftabnormals.endergetic.core.interfaces.BalloonHolder;
import net.minecraft.client.multiplayer.ClientChunkProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientWorld.class)
public abstract class MixinClientWorld {

	@Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;isPassenger()Z"), method = "tickEntities")
	private boolean shouldNotTick(Entity entity) {
		if (entity.isPassenger() || entity instanceof BolloomBalloonEntity && ((BolloomBalloonEntity) entity).attachedEntity != null) {
			return true;
		}
		return false;
	}

	@Inject(at = @At(value = "FIELD", target = "Lnet/minecraft/entity/Entity;addedToChunk:Z", ordinal = 1, shift = At.Shift.AFTER), method = "updateEntity")
	private void updateBalloons(Entity entity, CallbackInfo info) {
		BalloonHolder balloonHolder = (BalloonHolder) entity;
		ClientChunkProvider chunkProvider = ((ClientWorld) (Object) this).getChunkProvider();
		for (BolloomBalloonEntity balloon : balloonHolder.getBalloons()) {
			if (!balloon.removed && balloon.attachedEntity == entity) {
				if (chunkProvider.isChunkLoaded(balloon)) {
					balloon.forceSetPosition(balloon.getPosX(), balloon.getPosY(), balloon.getPosZ());
					balloon.prevRotationYaw = balloon.rotationYaw;
					balloon.prevRotationPitch = balloon.rotationPitch;
					if (balloon.addedToChunk) {
						balloon.ticksExisted++;
						balloon.updateAttachedPosition();
					}
					this.callCheckChunk(balloon);
				}
			} else {

			}
		}
	}

	@Invoker
	public abstract void callCheckChunk(Entity entity);

}
