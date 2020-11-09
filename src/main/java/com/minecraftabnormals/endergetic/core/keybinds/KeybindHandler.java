package com.minecraftabnormals.endergetic.core.keybinds;

import java.util.List;

import com.google.common.collect.Lists;
import com.minecraftabnormals.endergetic.common.network.C2SInflateBoofloVestMessage;
import com.minecraftabnormals.endergetic.api.entity.util.EntityMotionHelper;
import com.minecraftabnormals.endergetic.api.util.EndergeticNetworkUtil;
import com.minecraftabnormals.endergetic.common.entities.booflo.BoofloEntity;
import com.minecraftabnormals.endergetic.common.items.BoofloVestItem;
import com.minecraftabnormals.endergetic.core.EndergeticExpansion;
import com.minecraftabnormals.endergetic.core.registry.EEItems;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent.KeyInputEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

/**
 * @author - SmellyModder(Luke Tonon)
 */
@EventBusSubscriber(modid = EndergeticExpansion.MOD_ID, value = Dist.CLIENT)
public final class KeybindHandler {
	private static List<KeyBinding> keyBinds = Lists.newArrayList();
	private static KeyBinding BOOF_VEST = registerKeybind(new KeyBinding("key.endergetic.booflo_vest", 32, "key.categories.movement"));
	private static KeyBinding BOOFLO_INFLATE = registerKeybind(new KeyBinding("key.endergetic.booflo_inflate", 32, "key.categories.gameplay"));
	private static KeyBinding BOOFLO_SLAM = registerKeybind(new KeyBinding("key.endergetic.booflo_slam", 88, "key.categories.gameplay"));

	public static void registerKeys() {
		for (KeyBinding keys : keyBinds) {
			ClientRegistry.registerKeyBinding(keys);
		}
	}

	private static KeyBinding registerKeybind(KeyBinding keybind) {
		keyBinds.add(keybind);
		return keybind;
	}

	@SubscribeEvent(priority = EventPriority.LOW)
	public static void onKeyPressed(KeyInputEvent event) {
		if (Minecraft.getInstance().currentScreen != null) return;
		PlayerEntity player = Minecraft.getInstance().player;
		if (player == null) return;

		if (BOOF_VEST.isPressed() && !player.abilities.isFlying) {
			ItemStack stack = player.inventory.armorItemInSlot(2);
			if (stack.getItem() == EEItems.BOOFLO_VEST.get() && !player.isOnGround() && !player.isSpectator()) {
				if (BoofloVestItem.canBoof(stack, player)) {
					EntityMotionHelper.knockbackEntity(player, 4.0F, 0.75F, true, true);
					EndergeticExpansion.CHANNEL.sendToServer(new C2SInflateBoofloVestMessage());
				}
			}
		}
		if (BOOFLO_INFLATE.isKeyDown()) {
			Entity ridingEntity = player.getRidingEntity();
			if (KeybindHandler.isRidingBooflo(player) && !ridingEntity.isOnGround()) {
				BoofloEntity booflo = (BoofloEntity) ridingEntity;
				if (booflo.isBoofed() && booflo.canPassengerSteer()) {
					if (!booflo.isDelayDecrementing() && !booflo.isDelayExpanding() && booflo.getRideControlDelay() <= 182) {
						if (booflo.getRideControlDelay() >= 182) {
							EndergeticNetworkUtil.setPlayerNotBoosting(booflo.getEntityId());
						} else {
							EndergeticNetworkUtil.incrementBoofloBoostTimer(booflo.getEntityId());
						}
					}
				} else if (!booflo.isBoofed() && booflo.canPassengerSteer()) {
					if (booflo.getRideControlDelay() <= 0) {
						EndergeticNetworkUtil.inflateBooflo(booflo.getEntityId());
					}
				}
			}
		} else {
			if (KeybindHandler.isRidingBooflo(player)) {
				Entity ridingEntity = player.getRidingEntity();
				BoofloEntity booflo = (BoofloEntity) ridingEntity;
				if (booflo.isBoofed()) {
					if (!booflo.isDelayDecrementing() && !booflo.isDelayExpanding() && booflo.wasPlayerBoosting()) {
						EndergeticNetworkUtil.setPlayerNotBoosting(booflo.getEntityId());
					}
				}
			}
		}
		if (BOOFLO_SLAM.isPressed()) {
			Entity ridingEntity = player.getRidingEntity();
			if (KeybindHandler.isRidingBooflo(player)) {
				BoofloEntity booflo = (BoofloEntity) ridingEntity;
				if (booflo.isBoofed() && booflo.getRideControlDelay() <= 0 && booflo.isNoEndimationPlaying()) {
					EndergeticNetworkUtil.slamBooflo(booflo.getEntityId());
				}
			}
		}
	}

	private static boolean isRidingBooflo(PlayerEntity player) {
		return player != null && player.isPassenger() && player.getRidingEntity() instanceof BoofloEntity;
	}
}