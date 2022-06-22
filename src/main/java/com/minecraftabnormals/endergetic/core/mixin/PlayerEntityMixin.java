package com.minecraftabnormals.endergetic.core.mixin;

import com.minecraftabnormals.endergetic.common.entities.bolloom.BolloomBalloonEntity;
import com.minecraftabnormals.endergetic.common.entities.eetle.GliderEetleEntity;
import com.minecraftabnormals.endergetic.core.interfaces.BalloonHolder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.world.Difficulty;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(PlayerEntity.class)
public final class PlayerEntityMixin {

	@Inject(at = @At("RETURN"), method = "addAdditionalSaveData")
	private void writeBalloons(CompoundNBT compound, CallbackInfo info) {
		List<BolloomBalloonEntity> balloons = ((BalloonHolder) (Object) this).getBalloons();
		if (!balloons.isEmpty()) {
			ListNBT balloonsTag = new ListNBT();

			for (BolloomBalloonEntity balloon : balloons) {
				CompoundNBT compoundnbt = new CompoundNBT();
				if (balloon.saveAsPassenger(compoundnbt)) {
					balloonsTag.add(compoundnbt);
				}
			}

			if (!balloonsTag.isEmpty()) {
				compound.put("Balloons", balloonsTag);
			}
		}
	}

	@Inject(at = @At("HEAD"), method = "wantsToStopRiding", cancellable = true)
	private void preventGliderEetleDismount(CallbackInfoReturnable<Boolean> info) {
		PlayerEntity player = (PlayerEntity) (Object) this;
		if (player.isShiftKeyDown() && player.isAlive() && !player.isSpectator() && !player.isCreative() && player.level.getDifficulty() != Difficulty.PEACEFUL) {
			Entity entity = player.getVehicle();
			if (entity instanceof GliderEetleEntity) {
				GliderEetleEntity glider = (GliderEetleEntity) entity;
				if (!glider.isBaby() && glider.isAlive() && glider.isFlying()) {
					info.setReturnValue(false);
				}
			}
		}
	}

}
