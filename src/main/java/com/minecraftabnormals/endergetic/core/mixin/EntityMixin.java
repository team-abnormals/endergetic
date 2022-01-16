package com.minecraftabnormals.endergetic.core.mixin;

import com.google.common.collect.Lists;
import com.minecraftabnormals.endergetic.common.entities.bolloom.BolloomBalloonEntity;
import com.minecraftabnormals.endergetic.core.interfaces.BalloonHolder;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Mixin(Entity.class)
public final class EntityMixin implements BalloonHolder {
	private List<BolloomBalloonEntity> balloons = Lists.newArrayList();

	@Shadow
	private UUID uuid;

	@Shadow
	private World level;

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;moveTo(DDDFF)V", shift = At.Shift.AFTER), method = "teleportTo")
	private void updateBalloonPositions(double x, double y, double z, CallbackInfo info) {
		ServerWorld level = (ServerWorld) this.level;
		this.balloons.forEach(balloon -> {
			level.updateChunkPos(balloon);
			balloon.forceChunkAddition = true;
			balloon.updateAttachedPosition();
		});
	}

	@Inject(at = @At(value = "RETURN"), method = "unRide")
	private void detach(CallbackInfo info) {
		if (!this.getBalloons().isEmpty()) {
			this.detachBalloons();
		}

		if ((Object) this instanceof BolloomBalloonEntity) {
			((BolloomBalloonEntity) (Object) this).detachFromEntity();
		}
	}

	@Override
	public List<BolloomBalloonEntity> getBalloons() {
		return this.balloons.isEmpty() ? Collections.emptyList() : Lists.newArrayList(this.balloons);
	}

	@Override
	public void attachBalloon(BolloomBalloonEntity balloon) {
		this.balloons.add(balloon);
	}

	@Override
	public void detachBalloon(BolloomBalloonEntity balloonEntity) {
		this.balloons.remove(balloonEntity);
	}

	@Override
	public void detachBalloons() {
		this.getBalloons().forEach(BolloomBalloonEntity::detachFromEntity);
	}
}