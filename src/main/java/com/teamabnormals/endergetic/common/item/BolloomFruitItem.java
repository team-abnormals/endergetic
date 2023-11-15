package com.teamabnormals.endergetic.common.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class BolloomFruitItem extends Item {

	public BolloomFruitItem(Properties properties) {
		super(properties);
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level worldIn, LivingEntity eater) {
		ItemStack itemstack = super.finishUsingItem(stack, worldIn, eater);

		if (!worldIn.isClientSide && eater instanceof Player) {
			((Player) eater).getCooldowns().addCooldown(this, 25);
		}

		return itemstack;
	}

}
