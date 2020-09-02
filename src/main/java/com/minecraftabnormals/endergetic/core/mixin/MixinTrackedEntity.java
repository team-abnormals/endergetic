package com.minecraftabnormals.endergetic.core.mixin;

import com.google.common.collect.Lists;
import com.minecraftabnormals.endergetic.common.entities.bolloom.BolloomBalloonEntity;
import com.minecraftabnormals.endergetic.common.network.entity.S2CUpdateBalloons;
import com.minecraftabnormals.endergetic.core.EndergeticExpansion;
import com.minecraftabnormals.endergetic.core.interfaces.BalloonHolder;
import net.minecraft.entity.Entity;
import net.minecraft.world.TrackedEntity;
import net.minecraftforge.fml.network.PacketDistributor;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(TrackedEntity.class)
public final class MixinTrackedEntity {
	private List<BolloomBalloonEntity> prevBalloons = Lists.newArrayList();

	@Shadow
	@Final
	private Entity trackedEntity;

	@Inject(at = @At("HEAD"), method = "tick")
	private void updateBalloons(CallbackInfo info) {
		List<BolloomBalloonEntity> currentBalloons = ((BalloonHolder) this.trackedEntity).getBalloons();
		if (!currentBalloons.equals(this.prevBalloons)) {
			this.prevBalloons = currentBalloons;
			EndergeticExpansion.CHANNEL.send(PacketDistributor.TRACKING_ENTITY.with(() -> this.trackedEntity), new S2CUpdateBalloons(this.trackedEntity));
		}
	}

	@Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;isPassenger()Z"), method = "tick")
	private boolean redirectPositionUpdate(Entity trackedEntity) {
		return trackedEntity.isPassenger() || trackedEntity instanceof BolloomBalloonEntity && ((BolloomBalloonEntity) trackedEntity).attachedEntity != null;
	}
}
