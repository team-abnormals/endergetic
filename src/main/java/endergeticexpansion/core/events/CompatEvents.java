package endergeticexpansion.core.events;

import com.teamabnormals.abnormals_core.common.network.particle.MessageC2S2CSpawnParticle;
import com.teamabnormals.abnormals_core.core.library.endimator.entity.EndimatedEntity;
import endergeticexpansion.common.entities.booflo.EntityBooflo;
import endergeticexpansion.common.entities.booflo.EntityBoofloAdolescent;
import endergeticexpansion.common.entities.booflo.EntityBoofloBaby;
import endergeticexpansion.core.EndergeticExpansion;
import endergeticexpansion.core.config.EEConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Items;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * @author Voliant
 * Events for compatibility with other mods.
 */
@Mod.EventBusSubscriber(modid = EndergeticExpansion.MOD_ID)
public class CompatEvents {

    //Savage & Ravage potions of growing/youth
    @SubscribeEvent
    public static void onPotionExpire(PotionEvent.PotionExpiryEvent event) {
        LivingEntity affected = event.getEntityLiving();
        boolean isBabyEffect = event.getPotionEffect().getPotion() == ForgeRegistries.POTIONS.getValue(new ResourceLocation("savageandravage:shrinking"));
        if(isBabyEffect || event.getPotionEffect().getPotion() == ForgeRegistries.POTIONS.getValue(new ResourceLocation("savageandravage:growth"))) {
            if(!isBabyEffect && affected instanceof EntityBoofloBaby) ((EntityBoofloBaby)affected).growUp();
            if(affected instanceof EntityBoofloAdolescent) {
                if(isBabyEffect) {
                    ((EntityBoofloAdolescent) affected).growDown();
                } 
                else {
                    ((EntityBoofloAdolescent) affected).growUp();
                }
            }
            if(isBabyEffect && affected instanceof EntityBooflo) ((EntityBooflo)affected).growDown();
        }

    }

    //Quark potato poisoning
    public static String poisonTag = "endergetic:poisoned_by_potato";

    @SubscribeEvent
    public static void onInteractWithEntity(PlayerInteractEvent.EntityInteract event){
        Entity target = event.getTarget();
        if((target instanceof EntityBoofloBaby || target instanceof EntityBoofloAdolescent) && event.getItemStack().getItem() == Items.POISONOUS_POTATO && EEConfig.ValuesHolder.shouldEnablePoisonPotatoCompat() && ModList.get().isLoaded("quark")) {
            if(!target.getPersistentData().getBoolean(poisonTag) && !event.getWorld().isRemote) {
                //Vec3d pos = target.getPositionVec();
                event.getPlayer().swingArm(event.getHand());
                if(target.world.rand.nextDouble() < EEConfig.ValuesHolder.poisonEffectChance()) {
                    target.playSound(SoundEvents.ENTITY_GENERIC_EAT, 0.5f, 0.25f);
                    //TODO reactivate this if fixed in Quark
                    //if(((LivingEntity)target).isServerWorld()) ((ServerWorld)target.world).spawnParticle(ParticleTypes.ENTITY_EFFECT, pos.x, pos.y, pos.z, 5, 0, 1.0, 0, 0.8);
                    target.getPersistentData().putBoolean(poisonTag, true);
                    if(EEConfig.ValuesHolder.shouldPoisonEntity()) {
                        ((LivingEntity) target).addPotionEffect(new EffectInstance(Effects.POISON, 200));
                    } else {
                        target.playSound(SoundEvents.ENTITY_GENERIC_EAT, 0.5f, 0.5f + target.world.rand.nextFloat() / 2);
                        //if(((LivingEntity)target).isServerWorld()) ((ServerWorld)target.world).spawnParticle(ParticleTypes.SMOKE, pos.x, pos.y, pos.z, 5, 0, 1.0, 0, 0.1);
                    }
                    if(!event.getPlayer().isCreative()) event.getItemStack().shrink(1);
                }
            }

        }

    }

    @SubscribeEvent
    public static void onEntityUpdate(LivingEvent.LivingUpdateEvent event) {
        boolean isBabyBooflo = event.getEntityLiving() instanceof EntityBoofloBaby;
        if((isBabyBooflo || event. getEntityLiving() instanceof EntityBoofloAdolescent) && EEConfig.ValuesHolder.shouldEnablePoisonPotatoCompat() && ModList.get().isLoaded("quark")) {
            if(event.getEntityLiving().getPersistentData().getBoolean(poisonTag)){
                if(isBabyBooflo) {
                    ((EntityBoofloBaby) event.getEntityLiving()).setGrowingAge(-24000);
                } else {
                    ((EntityBoofloAdolescent) event.getEntityLiving()).setGrowingAge(-24000);
                }
            }
        }
    }
}
