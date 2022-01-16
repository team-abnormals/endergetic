package com.minecraftabnormals.endergetic.common.items;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class BolloomFruitItem extends Item {

	public BolloomFruitItem(Properties properties) {
		super(properties);
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, World worldIn, LivingEntity eater) {
		ItemStack itemstack = super.finishUsingItem(stack, worldIn, eater);

		if (!worldIn.isClientSide && eater instanceof PlayerEntity) {
			((PlayerEntity) eater).getCooldowns().addCooldown(this, 25);
		}

		return itemstack;
	}

}
