package com.minecraftabnormals.endergetic.core.events;

import com.minecraftabnormals.endergetic.common.entities.booflo.BoofloAdolescentEntity;
import com.minecraftabnormals.endergetic.common.entities.booflo.BoofloBabyEntity;
import com.minecraftabnormals.endergetic.common.entities.booflo.BoofloEntity;
import com.minecraftabnormals.endergetic.core.EndergeticExpansion;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * @author tessdotcpp
 * Events for compatibility with the Savage & Ravage mod.
 */
@Mod.EventBusSubscriber(modid = EndergeticExpansion.MOD_ID)
public class CompatEvents {
    @SubscribeEvent
    public static void onPotionExpire(PotionEvent.PotionExpiryEvent event) {
        LivingEntity affected = event.getEntityLiving();
        boolean isBabyEffect = event.getPotionEffect().getPotion() == ForgeRegistries.POTIONS.getValue(new ResourceLocation("savageandravage:shrinking"));
        if(isBabyEffect || event.getPotionEffect().getPotion() == ForgeRegistries.POTIONS.getValue(new ResourceLocation("savageandravage:growth"))) {
            if(!isBabyEffect && affected instanceof BoofloBabyEntity) ((BoofloBabyEntity)affected).growUp();
            if(affected instanceof BoofloAdolescentEntity) {
                if(isBabyEffect) {
                    ((BoofloAdolescentEntity) affected).growDown();
                } 
                else {
                    ((BoofloAdolescentEntity) affected).growUp();
                }
            }
            if(isBabyEffect && affected instanceof BoofloEntity) ((BoofloEntity)affected).growDown();
        }

    }
}
