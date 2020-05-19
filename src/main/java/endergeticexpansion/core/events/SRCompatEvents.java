package endergeticexpansion.core.events;

import endergeticexpansion.common.entities.booflo.EntityBooflo;
import endergeticexpansion.common.entities.booflo.EntityBoofloAdolescent;
import endergeticexpansion.common.entities.booflo.EntityBoofloBaby;
import endergeticexpansion.core.EndergeticExpansion;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * @author Voliant
 * Events for compatibility with the Savage & Ravage mod.
 */
@Mod.EventBusSubscriber(modid = EndergeticExpansion.MOD_ID)
public class SRCompatEvents {
    @SubscribeEvent
    public static void onPotionExpire(PotionEvent.PotionExpiryEvent event) {
        LivingEntity affected = event.getEntityLiving();
        boolean isBabyEffect = event.getPotionEffect().getPotion() == ForgeRegistries.POTIONS.getValue(new ResourceLocation("savageandravage:shrinking"));
        
        if(isBabyEffect || event.getPotionEffect().getPotion() == ForgeRegistries.POTIONS.getValue(new ResourceLocation("savageandravage:growth"))) {
            
            if(!isBabyEffect && affected instanceof EntityBoofloBaby) ((EntityBoofloBaby)affected).growUp();
            
            if(affected instanceof EntityBoofloAdolescent) {
                
                if (isBabyEffect) {
                    ((EntityBoofloAdolescent) affected).growDown();
                } 
                else {
                    ((EntityBoofloAdolescent) affected).growUp();
               }
                
            }
            
            if(isBabyEffect && affected instanceof EntityBooflo) ((EntityBooflo)affected).growDown();
        }

    }
}
