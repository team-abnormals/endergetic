package com.minecraftabnormals.endergetic.core.mixin;

import com.minecraftabnormals.endergetic.common.entities.bolloom.BolloomBalloonEntity;
import com.minecraftabnormals.endergetic.core.interfaces.BalloonHolder;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(PlayerEntity.class)
public final class MixinPlayerEntity {

	@Inject(at = @At("RETURN"), method = "writeAdditional")
	private void writeBalloons(CompoundNBT compound, CallbackInfo info) {
		List<BolloomBalloonEntity> balloons = ((BalloonHolder) (Object) this).getBalloons();
		if (!balloons.isEmpty()) {
			ListNBT balloonsTag = new ListNBT();

			for (BolloomBalloonEntity balloon : balloons) {
				CompoundNBT compoundnbt = new CompoundNBT();
				if (balloon.writeUnlessRemoved(compoundnbt)) {
					balloonsTag.add(compoundnbt);
				}
			}

			if (!balloonsTag.isEmpty()) {
				compound.put("Balloons", balloonsTag);
			}
		}
	}

}
