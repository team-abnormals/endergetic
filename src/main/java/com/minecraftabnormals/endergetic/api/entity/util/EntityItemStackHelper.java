package com.minecraftabnormals.endergetic.api.entity.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public class EntityItemStackHelper {

	public static void consumeItemFromStack(PlayerEntity player, ItemStack stack) {
		if(!player.abilities.isCreativeMode) stack.shrink(1);
	}
	
}