package com.teamabnormals.endergetic.core.keybinds;

import com.google.common.collect.Lists;
import com.teamabnormals.endergetic.api.entity.util.EntityMotionHelper;
import com.teamabnormals.endergetic.common.entity.booflo.Booflo;
import com.teamabnormals.endergetic.common.item.BoofloVestItem;
import com.teamabnormals.endergetic.common.network.C2SInflateBoofloVestMessage;
import com.teamabnormals.endergetic.common.network.entity.booflo.C2SSlamMessage;
import com.teamabnormals.endergetic.core.EndergeticExpansion;
import com.teamabnormals.endergetic.core.registry.EEItems;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

import java.util.List;

/**
 * @author SmellyModder(Luke Tonon)
 */
@EventBusSubscriber(modid = EndergeticExpansion.MOD_ID, value = Dist.CLIENT)
public final class KeybindHandler {
	private static final List<KeyMapping> keyBinds = Lists.newArrayList();
	public static final KeyMapping BOOFLO_SLAM = registerKeybind(new KeyMapping("key.endergetic.booflo_slam", 88, "key.categories.gameplay"));

	public static void registerKeys(RegisterKeyMappingsEvent event) {
		for (KeyMapping key : keyBinds) {
			event.register(key);
		}
	}

	private static KeyMapping registerKeybind(KeyMapping keybind) {
		keyBinds.add(keybind);
		return keybind;
	}

	@SubscribeEvent(priority = EventPriority.LOW)
	public static void onKeyPressed(InputEvent.Key event) {
		if (Minecraft.getInstance().screen != null) return;
		Player player = Minecraft.getInstance().player;
		if (player == null) return;

		if (Minecraft.getInstance().options.keyJump.isDown() && !player.getAbilities().flying) {
			ItemStack stack = player.getInventory().getArmor(2);
			if (stack.getItem() == EEItems.BOOFLO_VEST.get() && !player.onGround() && !player.isSpectator()) {
				if (BoofloVestItem.canBoof(stack, player)) {
					EntityMotionHelper.knockbackEntity(player, C2SInflateBoofloVestMessage.HORIZONTAL_BOOST_FORCE, C2SInflateBoofloVestMessage.VERTICAL_BOOST_FORCE, true, true);
					EndergeticExpansion.CHANNEL.sendToServer(new C2SInflateBoofloVestMessage());
				}
			}
		}
		if (player.getVehicle() instanceof Booflo booflo) {
			if (!booflo.onGround()) {
				if (BOOFLO_SLAM.isDown()) {
					if (booflo.isBoofed() && booflo.getBoostPower() <= 0 && booflo.isNoEndimationPlaying()) {
						EndergeticExpansion.CHANNEL.sendToServer(new C2SSlamMessage());
					}
				}
			}
		}
	}
}