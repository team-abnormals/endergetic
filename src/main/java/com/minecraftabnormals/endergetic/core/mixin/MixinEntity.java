package com.minecraftabnormals.endergetic.core.mixin;

import java.util.UUID;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.minecraftabnormals.endergetic.common.entities.bolloom.BolloomBalloonEntity;

import net.minecraft.entity.Entity;
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
}