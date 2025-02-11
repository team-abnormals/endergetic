package com.teamabnormals.endergetic.core.mixin;

import com.google.common.collect.Lists;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.teamabnormals.endergetic.common.entity.bolloom.BolloomBalloon;
import com.teamabnormals.endergetic.common.network.entity.S2CUpdateBalloonsMessage;
import com.teamabnormals.endergetic.core.EndergeticExpansion;
import com.teamabnormals.endergetic.core.interfaces.BalloonHolder;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.PacketDistributor;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ServerEntity.class)
public final class ServerEntityMixin {
	private List<BolloomBalloon> prevBalloons = Lists.newArrayList();

	@Shadow
	@Final
	private Entity entity;

	@Inject(at = @At("HEAD"), method = "sendChanges")
	private void updateBalloons(CallbackInfo info) {
		List<BolloomBalloon> currentBalloons = ((BalloonHolder) this.entity).getBalloons();
		if (!currentBalloons.equals(this.prevBalloons)) {
			this.prevBalloons = currentBalloons;
			EndergeticExpansion.CHANNEL.send(PacketDistributor.TRACKING_ENTITY.with(() -> this.entity), new S2CUpdateBalloonsMessage(this.entity));
		}
	}

	@WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;isPassenger()Z"), method = "sendChanges")
	private boolean wrapPositionUpdate(Entity trackedEntity, Operation<Boolean> original) {
		return original.call(trackedEntity) || trackedEntity instanceof BolloomBalloon balloon && balloon.isAttachedToEntity();
	}
}
