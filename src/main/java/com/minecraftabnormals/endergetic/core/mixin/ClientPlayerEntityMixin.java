package com.minecraftabnormals.endergetic.core.mixin;

import com.minecraftabnormals.endergetic.common.entities.booflo.BoofloEntity;
import com.minecraftabnormals.endergetic.common.network.entity.booflo.C2SBoostMessage;
import com.minecraftabnormals.endergetic.common.network.entity.booflo.C2SInflateMessage;
import com.minecraftabnormals.endergetic.core.EndergeticExpansion;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.util.MovementInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ClientPlayerEntity.class)
public final class ClientPlayerEntityMixin extends AbstractClientPlayerEntity {
	@Shadow
	public MovementInput input;

	private ClientPlayerEntityMixin(ClientWorld world, GameProfile profile) {
		super(world, profile);
	}

	/**
	 * Forge's input event gets fired after the previous input values are updated making it difficult to tell if a key was being pressed prior to the event being fired.
	 *
	 * @param flag wasJumping boolean.
	 */
	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/util/MovementInput;tick(Z)V", shift = At.Shift.AFTER), method = "aiStep", locals = LocalCapture.CAPTURE_FAILSOFT)
	private void onTickMovementInput(CallbackInfo info, boolean flag) {
		Entity ridingEntity = this.getVehicle();
		if (ridingEntity instanceof BoofloEntity) {
			BoofloEntity booflo = (BoofloEntity) ridingEntity;
			if (!booflo.isOnGround()) {
				if (!flag && this.input.jumping) {
					EndergeticExpansion.CHANNEL.sendToServer(new C2SInflateMessage());
				} else if (!this.input.jumping) {
					if (booflo.isBoostExpanding() && booflo.isBoofed() && !booflo.isBoostLocked() && booflo.getBoostPower() > 0) {
						EndergeticExpansion.CHANNEL.sendToServer(new C2SBoostMessage());
					}
				}
			}
		}
	}
}
