package com.minecraftabnormals.endergetic.core.mixin;

import com.minecraftabnormals.endergetic.common.entities.booflo.BoofloEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.damagesource.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public final class LivingEntityMixin {
	@Shadow
	protected int lastHurtByPlayerTime;
	@Shadow
	protected Player lastHurtByPlayer;

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;setLastHurtByMob(Lnet/minecraft/entity/LivingEntity;)V", ordinal = 0), method = "hurt")
	private void tryToAttackAsBoofloOwner(DamageSource source, float amount, CallbackInfoReturnable<Boolean> info) {
		Entity entity = source.getEntity();
		if (entity instanceof BoofloEntity) {
			BoofloEntity booflo = (BoofloEntity) entity;
			if (booflo.isTamed()) {
				this.lastHurtByPlayerTime = 100;
				LivingEntity owner = booflo.getOwner();
				if (owner instanceof Player) {
					this.lastHurtByPlayer = (Player) owner;
				} else {
					this.lastHurtByPlayer = null;
				}
			}
		}
	}
}
