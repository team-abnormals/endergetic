package com.minecraftabnormals.endergetic.core.registry;

import net.minecraft.item.Food;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;

public final class EEFoods {
	public static final Food BOLLOOM_FRUIT = new Food.Builder().nutrition(2).saturationMod(0.3F).effect(() -> new EffectInstance(Effects.LEVITATION, 125, 0), 1.0F).alwaysEat().build();
}
