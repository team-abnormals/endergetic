package com.teamabnormals.endergetic.core.mixin;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.teamabnormals.endergetic.common.entities.bolloom.BalloonOrder;
import com.teamabnormals.endergetic.common.entities.bolloom.BolloomBalloonEntity;
import com.teamabnormals.endergetic.core.interfaces.BalloonHolder;
import com.teamabnormals.endergetic.core.interfaces.CustomBalloonPositioner;
import com.teamabnormals.endergetic.core.registry.other.EEDataProcessors;
import com.teamabnormals.blueprint.common.world.storage.tracking.IDataManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.teamabnormals.endergetic.common.items.BolloomBalloonItem;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;

import java.util.*;
import java.util.stream.Collectors;

@Mixin(Boat.class)
public abstract class BoatEntityMixin extends Entity implements CustomBalloonPositioner {
	private static final ResourceLocation LARGE_BOAT_NAME = new ResourceLocation("boatload", "large_boat");

	private BoatEntityMixin(EntityType<?> entityType, Level world) {
		super(entityType, world);
	}

	@Override
	public void onBalloonAttached(BolloomBalloonEntity balloon) {
		if (!balloon.level.isClientSide && !balloon.hasModifiedBoatOrder) {
			Map<UUID, BalloonOrder> orderMap = Maps.newHashMap(((IDataManager) this).getValue(EEDataProcessors.ORDER_DATA));
			orderMap.put(balloon.getUUID(), getClosestOpenOrder(orderMap));
			((IDataManager) this).setValue(EEDataProcessors.ORDER_DATA, orderMap);
			balloon.hasModifiedBoatOrder = true;
		}
	}

	@Override
	public void onBalloonDetached(BolloomBalloonEntity balloon) {
		if (!balloon.level.isClientSide) {
			Map<UUID, BalloonOrder> orderMap = Maps.newHashMap(((IDataManager) this).getValue(EEDataProcessors.ORDER_DATA));
			orderMap.remove(balloon.getUUID());
			((IDataManager) this).setValue(EEDataProcessors.ORDER_DATA, orderMap);
		}
	}

	@Override
	public void updateAttachedPosition(BolloomBalloonEntity balloon) {
		Map<UUID, BalloonOrder> orderMap = ((IDataManager) this).getValue(EEDataProcessors.ORDER_DATA);
		if (!orderMap.containsKey(balloon.getUUID())) return;
		BalloonOrder balloonOrder = orderMap.get(balloon.getUUID());
		Vec3 attachedOffset = (new Vec3(this.getType() == ForgeRegistries.ENTITY_TYPES.getValue(LARGE_BOAT_NAME) ? balloonOrder.largeX : balloonOrder.normalX, 0.0D, balloonOrder.normalZ)).yRot((float) (-this.getYRot() * (Math.PI / 180F) - (Math.PI / 2F)));
		balloon.setPos(this.getX() + attachedOffset.x() + balloon.getSway() * Math.sin(-balloon.getVineYRot()), this.getY() + balloon.getPassengersRidingOffset() + balloon.getEyeHeight(), this.getZ() + attachedOffset.z() + balloon.getSway() * Math.cos(-balloon.getVineYRot()));
	}

	private static BalloonOrder getClosestOpenOrder(Map<UUID, BalloonOrder> orderMap) {
		Set<Integer> missingNumbers = Sets.newHashSet();
		Set<Integer> orders = BalloonOrder.toOrdinalSet(Sets.newHashSet(orderMap.values()));
		for (int i = 0; i < 4; i++) {
			if (!orders.contains(i)) {
				missingNumbers.add(i);
			}
		}
		return BalloonOrder.byOrdinal(missingNumbers.isEmpty() ? orders.size() : missingNumbers.stream().sorted(Comparator.comparingInt(Math::abs)).collect(Collectors.toList()).get(0));
	}

	@Inject(at = @At(value = "TAIL"), method = "floatBoat")
	private void handleBalloonMotion(CallbackInfo info) {
		int balloons = ((BalloonHolder) this).getBalloons().size();
		if (balloons > 0) {
			Vec3 motion = this.getDeltaMovement();
			if (motion.y() <= 0.0F && balloons <= 2) {
				this.setDeltaMovement(motion.multiply(1.0F, 0.85F / balloons, 1.0F));
			} else if (balloons > 2) {
				float boost = (balloons / 2.0F) * 0.045F;
				this.setDeltaMovement(new Vec3(motion.x(), Mth.clamp(motion.y() + boost, -1.0F, boost), motion.z()));
			}
		}
	}

	@Inject(at = @At("HEAD"), method = "interact", cancellable = true)
	private void addBalloonAttachingInteraction(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> info) {
		ItemStack stack = player.getItemInHand(hand);
		Item item = stack.getItem();
		if (item instanceof BolloomBalloonItem && BolloomBalloonItem.canAttachBalloonToTarget(this)) {
			player.swing(hand, true);
			if (!this.level.isClientSide) {
				BolloomBalloonItem.attachToEntity(((BolloomBalloonItem) item).getBalloonColor(), this);
			}
			if (!player.isCreative()) stack.shrink(1);
			info.setReturnValue(InteractionResult.CONSUME);
		}
	}
}
