package com.minecraftabnormals.endergetic.core.keybinds;

import com.google.common.collect.Lists;
import com.minecraftabnormals.endergetic.api.entity.util.EntityMotionHelper;
import com.minecraftabnormals.endergetic.common.entities.booflo.BoofloEntity;
import com.minecraftabnormals.endergetic.common.items.BoofloVestItem;
import com.minecraftabnormals.endergetic.common.network.C2SInflateBoofloVestMessage;
import com.minecraftabnormals.endergetic.common.network.entity.booflo.C2SSlamMessage;
import com.minecraftabnormals.endergetic.core.EndergeticExpansion;
import com.minecraftabnormals.endergetic.core.registry.EEItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent.KeyInputEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

import java.util.List;

/**
 * @author SmellyModder(Luke Tonon)
 */
@EventBusSubscriber(modid = EndergeticExpansion.MOD_ID, value = Dist.CLIENT)
public final class KeybindHandler {
	private static List<KeyBinding> keyBinds = Lists.newArrayList();
	private static final KeyBinding BOOF_VEST = registerKeybind(new KeyBinding("key.endergetic.booflo_vest", 32, "key.categories.movement"));
	public static final KeyBinding BOOFLO_SLAM = registerKeybind(new KeyBinding("key.endergetic.booflo_slam", 88, "key.categories.gameplay"));

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
		if (Minecraft.getInstance().screen != null) return;
		PlayerEntity player = Minecraft.getInstance().player;
		if (player == null) return;

		if (BOOF_VEST.consumeClick() && !player.abilities.flying) {
			ItemStack stack = player.inventory.getArmor(2);
			if (stack.getItem() == EEItems.BOOFLO_VEST.get() && !player.isOnGround() && !player.isSpectator()) {
				if (BoofloVestItem.canBoof(stack, player)) {
					EntityMotionHelper.knockbackEntity(player, C2SInflateBoofloVestMessage.HORIZONTAL_BOOST_FORCE, C2SInflateBoofloVestMessage.VERTICAL_BOOST_FORCE, true, true);
					EndergeticExpansion.CHANNEL.sendToServer(new C2SInflateBoofloVestMessage());
				}
			}
		}
		if (player.getVehicle() instanceof BoofloEntity) {
			BoofloEntity booflo = (BoofloEntity) player.getVehicle();
			if (!booflo.isOnGround()) {
				if (BOOFLO_SLAM.consumeClick()) {
					if (booflo.isBoofed() && booflo.getBoostPower() <= 0 && booflo.isNoEndimationPlaying()) {
						EndergeticExpansion.CHANNEL.sendToServer(new C2SSlamMessage());
					}
				}
			}
		}
	}
}