package com.minecraftabnormals.endergetic.core.keybinds;

import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;
import com.minecraftabnormals.endergetic.core.registry.other.EETags;
import com.teamabnormals.abnormals_core.core.utils.MathUtils;
import com.teamabnormals.abnormals_core.core.utils.NetworkUtil;
import com.minecraftabnormals.endergetic.api.entity.util.EntityMotionHelper;
import com.minecraftabnormals.endergetic.api.util.EndergeticNetworkUtil;
import com.minecraftabnormals.endergetic.common.entities.booflo.BoofloEntity;
import com.minecraftabnormals.endergetic.common.items.BoofloVestItem;
import com.minecraftabnormals.endergetic.core.EndergeticExpansion;
import com.minecraftabnormals.endergetic.core.registry.EEItems;
import com.minecraftabnormals.endergetic.core.registry.EESounds;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.MathHelper;
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

		if (BOOF_VEST.isPressed()) {
			Random rand = player.getRNG();
			ItemStack stack = player.inventory.armorItemInSlot(2);

			if (!stack.isEmpty() && stack.getItem() == EEItems.BOOFLO_VEST.get() && !player.isOnGround() && !player.isSpectator()) {
				BoofloVestItem vest = (BoofloVestItem) stack.getItem();
				if (vest.canBoof(stack, player)) {
					CompoundNBT tag = stack.getTag();

					tag.putBoolean(BoofloVestItem.BOOFED_TAG, true);
					tag.putInt(BoofloVestItem.TIMES_BOOFED_TAG, tag.getInt(BoofloVestItem.TIMES_BOOFED_TAG) + 1);
					vest.setDelayForBoofedAmount(stack, player);

					EndergeticNetworkUtil.updateSItemNBT(stack);
					EntityMotionHelper.knockbackEntity(player, 4.0F, 0.75F, true, true);

					for (Entity entity : player.getEntityWorld().getEntitiesWithinAABB(Entity.class, player.getBoundingBox().grow(2.0D))) {
						if (entity != player && !EETags.EntityTypes.BOOF_BLOCK_RESISTANT.contains(entity.getType())) {
							boolean reverse = player.getRidingEntity() == entity;
							EntityMotionHelper.knockbackEntity(entity, 4.0F, 0.75F, reverse, false);
						}
					}

					for (int i = 0; i < 8; i++) {
						double x = player.getPosX() + MathUtils.makeNegativeRandomly(rand.nextFloat() * 0.15F, rand);
						double y = player.getPosY() + (rand.nextFloat() * 0.05F) + 1.25F;
						double z = player.getPosZ() + MathUtils.makeNegativeRandomly(rand.nextFloat() * 0.15F, rand);

						NetworkUtil.spawnParticleC2S2C("endergetic:short_poise_bubble", x, y, z, MathUtils.makeNegativeRandomly((rand.nextFloat() * 0.3F), rand) + 0.025F, (rand.nextFloat() * 0.15F) + 0.1F, MathUtils.makeNegativeRandomly((rand.nextFloat() * 0.3F), rand) + 0.025F);
					}

					player.playSound(EESounds.BOOFLO_VEST_INFLATE.get(), 1.0F, MathHelper.clamp(1.3F - (tag.getInt(BoofloVestItem.TIMES_BOOFED_TAG) * 0.15F), 0.25F, 1.0F));

					EndergeticNetworkUtil.SBoofEntity(4.0F, 0.75F, 2);
				}
			}
		}
		if (BOOFLO_INFLATE.isKeyDown()) {
			Entity ridingEntity = player.getRidingEntity();
			if (KeybindHandler.isRidingBooflo(player) && !((BoofloEntity) ridingEntity).isOnGround()) {
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