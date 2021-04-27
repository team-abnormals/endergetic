package com.minecraftabnormals.endergetic.core.mixin;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import com.google.common.collect.Lists;
import com.minecraftabnormals.endergetic.common.entities.eetle.GliderEetleEntity;
import com.minecraftabnormals.endergetic.core.interfaces.BalloonHolder;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.minecraftabnormals.endergetic.common.entities.bolloom.BolloomBalloonEntity;

import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public final class EntityMixin implements BalloonHolder {
	private List<BolloomBalloonEntity> balloons = Lists.newArrayList();

	@Shadow
	private UUID entityUniqueID;

	@Shadow
	private World world;

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;setLocationAndAngles(DDDFF)V", shift = At.Shift.AFTER), method = "setPositionAndUpdate")
	private void updateBalloonPositions(double x, double y, double z, CallbackInfo info) {
		ServerWorld world = (ServerWorld) this.world;
		this.balloons.forEach(balloon -> {
			world.chunkCheck(balloon);
			balloon.isPositionDirty = true;
			balloon.updateAttachedPosition();
		});
	}

	@Inject(at = @At(value = "RETURN"), method = "detach")
	private void detach(CallbackInfo info) {
		if (!this.getBalloons().isEmpty()) {
			this.detachBalloons();
		}

		if ((Object) this instanceof BolloomBalloonEntity) {
			((BolloomBalloonEntity) (Object) this).detachFromEntity();
		}
	}

	@Inject(at = @At("HEAD"), method = "isInvulnerableTo", cancellable = true)
	private void preventGliderEetleSuffocationDamage(DamageSource source, CallbackInfoReturnable<Boolean> info) {
		if (source == DamageSource.IN_WALL) {
			Entity entity = ((Entity) (Object) this);
			if (entity.isAlive() && entity.isEntityInsideOpaqueBlock() && entity.getRidingEntity() instanceof GliderEetleEntity) {
				info.setReturnValue(true);
			}
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