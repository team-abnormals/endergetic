package com.minecraftabnormals.endergetic.core.registry;

import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

public final class EEFoods {
	public static final FoodProperties BOLLOOM_FRUIT = new FoodProperties.Builder().nutrition(2).saturationMod(0.3F).effect(() -> new MobEffectInstance(MobEffects.LEVITATION, 125, 0), 1.0F).alwaysEat().build();
}
