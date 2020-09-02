package com.minecraftabnormals.endergetic.core.mixin;

import com.minecraftabnormals.endergetic.core.interfaces.BalloonHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.minecraftabnormals.endergetic.common.items.BolloomBalloonItem;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

@Mixin(BoatEntity.class)
public abstract class MixinBoatEntity extends Entity {

	private MixinBoatEntity(EntityType<?> entityType, World world) {
		super(entityType, world);
	}
	
	@Inject(at = @At(value = "TAIL"), method = "updateMotion")
	private void handleBalloonMotion(CallbackInfo info) {
		int balloons = ((BalloonHolder) this).getBalloons().size();
		if (balloons > 0) {
			Vector3d motion = this.getMotion();
			if (motion.getY() <= 0.0F && balloons <= 2) {
				this.setMotion(motion.mul(1.0F, 0.85F / balloons, 1.0F));
			} else if (balloons > 2) {
				float boost = (balloons / 2) * 0.045F;
				this.setMotion(new Vector3d(motion.getX(), MathHelper.clamp(motion.getY() + boost, -1.0F, boost), motion.getZ()));
			}
		}
	}

	@Inject(at = @At("HEAD"), method = "processInitialInteract", cancellable = true)
	private void addBalloonAttachingInteraction(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResultType> info) {
		ItemStack stack = player.getHeldItem(hand);
		Entity boat = this.getEntity();
		Item item = stack.getItem();
		if (item instanceof BolloomBalloonItem && BolloomBalloonItem.canAttachBalloonToTarget(boat)) {
			player.swing(hand, true);
			if (!boat.world.isRemote) {
				BolloomBalloonItem.attachToEntity(((BolloomBalloonItem) item).getBalloonColor(), boat);
			}
			if (!player.isCreative()) stack.shrink(1);
			info.setReturnValue(ActionResultType.CONSUME);
		}
	}
	
}