package com.minecraftabnormals.endergetic.core.mixin;

import com.google.common.collect.Lists;
import com.minecraftabnormals.endergetic.common.entities.bolloom.BolloomBalloonEntity;
import com.minecraftabnormals.endergetic.common.network.entity.S2CUpdateBalloonsMessage;
import com.minecraftabnormals.endergetic.core.EndergeticExpansion;
import com.minecraftabnormals.endergetic.core.interfaces.BalloonHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerEntity;
import net.minecraftforge.fml.network.PacketDistributor;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ServerEntity.class)
public final class TrackedEntityMixin {
	private List<BolloomBalloonEntity> prevBalloons = Lists.newArrayList();

	@Shadow
	@Final
	private Entity entity;

	@Inject(at = @At("HEAD"), method = "sendChanges")
	private void updateBalloons(CallbackInfo info) {
		List<BolloomBalloonEntity> currentBalloons = ((BalloonHolder) this.entity).getBalloons();
		if (!currentBalloons.equals(this.prevBalloons)) {
			this.prevBalloons = currentBalloons;
			EndergeticExpansion.CHANNEL.send(PacketDistributor.TRACKING_ENTITY.with(() -> this.entity), new S2CUpdateBalloonsMessage(this.entity));
		}
	}

	@Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;isPassenger()Z"), method = "sendChanges")
	private boolean redirectPositionUpdate(Entity trackedEntity) {
		return trackedEntity.isPassenger() || trackedEntity instanceof BolloomBalloonEntity && ((BolloomBalloonEntity) trackedEntity).isAttachedToEntity();
	}
}
