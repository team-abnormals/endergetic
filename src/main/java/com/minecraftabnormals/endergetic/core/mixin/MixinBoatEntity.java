package com.minecraftabnormals.endergetic.core.mixin;

import java.util.stream.Collectors;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.minecraftabnormals.endergetic.common.entities.bolloom.BolloomBalloonEntity;
import com.minecraftabnormals.endergetic.common.items.BolloomBalloonItem;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.entity.passive.WaterMobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

@Mixin(BoatEntity.class)
public abstract class MixinBoatEntity extends Entity {

	public MixinBoatEntity(EntityType<?> entityType, World world) {
		super(entityType, world);
	}
	
	@Overwrite
	protected boolean canFitPassenger(Entity passenger) {
		return this.getPassengers().stream().filter(entity -> !(entity instanceof BolloomBalloonEntity)).collect(Collectors.toList()).size() < 2 && !this.areEyesInFluid(FluidTags.WATER);
	}
	
	@Inject(at = @At(value = "TAIL"), method = "updateMotion")
	private void handleBalloonMotion(CallbackInfo info) {
		int balloons =  this.getPassengers().stream().filter(entity -> entity instanceof BolloomBalloonEntity).collect(Collectors.toList()).size();
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
			if (!boat.world.isRemote) {
				BolloomBalloonItem.attachToEntity(((BolloomBalloonItem) item).getBalloonColor(), player, boat);
			}
			if (!player.isCreative()) stack.shrink(1);
			info.setReturnValue(ActionResultType.CONSUME);
		}
	}
	
	@Inject(at = @At("HEAD"), method = "updatePassenger", cancellable = true)
	private void addBalloonPositioning(Entity passenger, CallbackInfo info) {
		if (passenger instanceof BolloomBalloonEntity) {
			((BolloomBalloonEntity) passenger).setRidingPosition();
			info.cancel();
		}
	}
	
	@Inject(at = @At("HEAD"), method = "applyEntityCollision")
	private void startRidingWithBalloons(Entity collider, CallbackInfo info) {
		if (!this.world.isRemote && !(this.getControllingPassenger() instanceof PlayerEntity) && !collider.isPassenger() && collider.getWidth() < this.getWidth() && collider instanceof LivingEntity && !(collider instanceof WaterMobEntity) && !(collider instanceof PlayerEntity) && this.canFitPassenger(collider)) {
			collider.startRiding(this);
		}
	}
	
}