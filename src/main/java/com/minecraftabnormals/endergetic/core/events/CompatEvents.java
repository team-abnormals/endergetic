package com.minecraftabnormals.endergetic.core.events;

import com.minecraftabnormals.endergetic.common.entities.booflo.BoofloAdolescentEntity;
import com.minecraftabnormals.endergetic.common.entities.booflo.BoofloBabyEntity;
import com.minecraftabnormals.endergetic.common.entities.booflo.BoofloEntity;
import com.minecraftabnormals.endergetic.core.EndergeticExpansion;

import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

/**
 * @author abigailfails
 * Events for compatibility with the Savage & Ravage mod.
 */
@Mod.EventBusSubscriber(modid = EndergeticExpansion.MOD_ID)
public final class CompatEvents {

	private static final ResourceLocation SHRINKING_ID = new ResourceLocation("savageandravage:shrinking");
	private static final ResourceLocation GROWING_ID = new ResourceLocation("savageandravage:growing");

	@SubscribeEvent
	public static void onPotionExpire(PotionEvent.PotionExpiryEvent event) {
		LivingEntity affected = event.getEntityLiving();
		Effect effectType = Objects.requireNonNull(event.getPotionEffect()).getPotion();
		boolean isBabyEffect = effectType == ForgeRegistries.POTIONS.getValue(SHRINKING_ID);
		if (isBabyEffect || effectType == ForgeRegistries.POTIONS.getValue(GROWING_ID)) {
			if (!isBabyEffect && affected instanceof BoofloBabyEntity) ((BoofloBabyEntity) affected).growUp();
			if (affected instanceof BoofloAdolescentEntity) {
				if (isBabyEffect) {
					((BoofloAdolescentEntity) affected).growDown();
				} else {
					((BoofloAdolescentEntity) affected).growUp();
				}
			}
			if (isBabyEffect && affected instanceof BoofloEntity) ((BoofloEntity) affected).growDown();
		}
	}
}
