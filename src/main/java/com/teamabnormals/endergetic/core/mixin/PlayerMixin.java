package com.teamabnormals.endergetic.core.mixin;

import com.teamabnormals.endergetic.common.entity.bolloom.BolloomBalloon;
import com.teamabnormals.endergetic.common.entity.eetle.GliderEetle;
import com.teamabnormals.endergetic.core.interfaces.BalloonHolder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(Player.class)
public final class PlayerMixin {

	@Inject(at = @At("RETURN"), method = "addAdditionalSaveData")
	private void writeBalloons(CompoundTag compound, CallbackInfo info) {
		List<BolloomBalloon> balloons = ((BalloonHolder) (Object) this).getBalloons();
		if (!balloons.isEmpty()) {
			ListTag balloonsTag = new ListTag();

			for (BolloomBalloon balloon : balloons) {
				CompoundTag compoundnbt = new CompoundTag();
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
		Player player = (Player) (Object) this;
		if (player.isShiftKeyDown() && player.isAlive() && !player.isSpectator() && !player.isCreative() && player.level.getDifficulty() != Difficulty.PEACEFUL) {
			Entity entity = player.getVehicle();
			if (entity instanceof GliderEetle glider) {
				if (!glider.isBaby() && glider.isAlive() && glider.isFlying()) {
					info.setReturnValue(false);
				}
			}
		}
	}

}
