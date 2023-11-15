package com.teamabnormals.endergetic.core.mixin;

import com.google.common.collect.Lists;
import com.teamabnormals.endergetic.common.entity.bolloom.BolloomBalloon;
import com.teamabnormals.endergetic.common.entity.eetle.GliderEetle;
import com.teamabnormals.endergetic.core.interfaces.BalloonHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collections;
import java.util.List;

@Mixin(Entity.class)
public final class EntityMixin implements BalloonHolder {
	private List<BolloomBalloon> balloons = Lists.newArrayList();

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;moveTo(DDDFF)V", shift = At.Shift.AFTER), method = "teleportTo")
	private void updateBalloonPositions(double x, double y, double z, CallbackInfo info) {
		this.balloons.forEach(BolloomBalloon::updateAttachedPosition);
	}

	@Inject(at = @At(value = "RETURN"), method = "unRide")
	private void detach(CallbackInfo info) {
		if (!this.getBalloons().isEmpty()) {
			this.detachBalloons();
		}

		if ((Object) this instanceof BolloomBalloon balloon) {
			balloon.detachFromEntity();
		}
	}

	@Inject(at = @At("HEAD"), method = "isInvulnerableTo", cancellable = true)
	private void preventGliderEetleSuffocationDamage(DamageSource source, CallbackInfoReturnable<Boolean> info) {
		if (source == DamageSource.IN_WALL) {
			Entity entity = ((Entity) (Object) this);
			if (entity.isAlive() && entity.isInWall() && entity.getVehicle() instanceof GliderEetle) {
				info.setReturnValue(true);
			}
		}
	}

	@Override
	public List<BolloomBalloon> getBalloons() {
		return this.balloons.isEmpty() ? Collections.emptyList() : Lists.newArrayList(this.balloons);
	}

	@Override
	public void attachBalloon(BolloomBalloon balloon) {
		this.balloons.add(balloon);
	}

	@Override
	public void detachBalloon(BolloomBalloon balloonEntity) {
		this.balloons.remove(balloonEntity);
	}

	@Override
	public void detachBalloons() {
		this.getBalloons().forEach(BolloomBalloon::detachFromEntity);
	}
}