package com.minecraftabnormals.endergetic.api.entity.util;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class EntityItemStackHelper {

	public static void consumeItemFromStack(Player player, ItemStack stack) {
		if (!player.getAbilities().instabuild) stack.shrink(1);
	}

}