package com.minecraftabnormals.endergetic.core.mixin;

import java.util.List;
import java.util.UUID;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.minecraftabnormals.endergetic.core.interfaces.BalloonHolder;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.minecraftabnormals.endergetic.common.entities.bolloom.BolloomBalloonEntity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.BoatEntity;

@Mixin(Entity.class)
public final class MixinEntity implements BalloonHolder {
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

	@Inject(at = @At("HEAD"), method = "onRemovedFromWorld()V", remap = false)
	private void onRemovedFromWorld(CallbackInfo info) {
		if ((Object) this instanceof BoatEntity) {
			BolloomBalloonEntity.BALLOONS_ON_BOAT_MAP.remove(this.entityUniqueID);
		}
	}

	@Override
	public List<BolloomBalloonEntity> getBalloons() {
		return this.balloons;
	}

	@Override
	public void attachBalloon(BolloomBalloonEntity balloon) {
		if ((Object) this instanceof BoatEntity) {
			this.balloons.add(getClosestOpenIndex(this.balloons), balloon);
		} else {
			this.balloons.add(balloon);
		}
		balloon.attachedEntity = (Entity) (Object) this;
	}

	@Override
	public void unattachBalloon(BolloomBalloonEntity balloonEntity) {
		if ((Object) this instanceof BoatEntity) {
			this.balloons.set(this.balloons.indexOf(balloonEntity), null);
		} else {
			this.balloons.remove(balloonEntity);
		}
		if (balloonEntity.attachedEntity.equals(this)) {
			balloonEntity.attachedEntity = null;
		}
	}

	@Override
	public void unattachBalloons() {
		ImmutableList.copyOf(this.balloons).forEach(this::unattachBalloon);
	}

	private static int getClosestOpenIndex(List<BolloomBalloonEntity> balloons) {
		for (int i = 0; i < balloons.size(); i++) {
			if (balloons.get(i) != null) {
				return i;
			}
		}
		return balloons.size();
	}
}