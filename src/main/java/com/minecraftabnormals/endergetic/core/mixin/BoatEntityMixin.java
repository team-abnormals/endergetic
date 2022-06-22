package com.minecraftabnormals.endergetic.core.mixin;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.minecraftabnormals.abnormals_core.common.world.storage.tracking.IDataManager;
import com.minecraftabnormals.endergetic.common.entities.bolloom.BalloonOrder;
import com.minecraftabnormals.endergetic.common.entities.bolloom.BolloomBalloonEntity;
import com.minecraftabnormals.endergetic.core.interfaces.BalloonHolder;
import com.minecraftabnormals.endergetic.core.interfaces.CustomBalloonPositioner;
import com.minecraftabnormals.endergetic.core.registry.other.EEDataProcessors;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
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

import java.util.*;
import java.util.stream.Collectors;

@Mixin(BoatEntity.class)
public abstract class BoatEntityMixin extends Entity implements CustomBalloonPositioner {
	private static final ResourceLocation LARGE_BOAT_NAME = new ResourceLocation("extraboats", "large_boat");

	private BoatEntityMixin(EntityType<?> entityType, World world) {
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
		Vector3d attachedOffset = (new Vector3d(this.getType() == ForgeRegistries.ENTITIES.getValue(LARGE_BOAT_NAME) ? balloonOrder.largeX : balloonOrder.normalX, 0.0D, balloonOrder.normalZ)).yRot((float) (-this.yRot * (Math.PI / 180F) - (Math.PI / 2F)));
		balloon.setPos(this.getX() + attachedOffset.x() + balloon.getSway() * Math.sin(-balloon.getAngle()), this.getY() + balloon.getPassengersRidingOffset() + balloon.getEyeHeight(), this.getZ() + attachedOffset.z() + balloon.getSway() * Math.cos(-balloon.getAngle()));
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
			Vector3d motion = this.getDeltaMovement();
			if (motion.y() <= 0.0F && balloons <= 2) {
				this.setDeltaMovement(motion.multiply(1.0F, 0.85F / balloons, 1.0F));
			} else if (balloons > 2) {
				float boost = (balloons / 2.0F) * 0.045F;
				this.setDeltaMovement(new Vector3d(motion.x(), MathHelper.clamp(motion.y() + boost, -1.0F, boost), motion.z()));
			}
		}
	}

	@Inject(at = @At("HEAD"), method = "interact", cancellable = true)
	private void addBalloonAttachingInteraction(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResultType> info) {
		ItemStack stack = player.getItemInHand(hand);
		Entity boat = this.getEntity();
		Item item = stack.getItem();
		if (item instanceof BolloomBalloonItem && BolloomBalloonItem.canAttachBalloonToTarget(boat)) {
			player.swing(hand, true);
			if (!boat.level.isClientSide) {
				BolloomBalloonItem.attachToEntity(((BolloomBalloonItem) item).getBalloonColor(), boat);
			}
			if (!player.isCreative()) stack.shrink(1);
			info.setReturnValue(ActionResultType.CONSUME);
		}
	}
}