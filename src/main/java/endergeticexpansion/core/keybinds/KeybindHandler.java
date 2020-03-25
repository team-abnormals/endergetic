package endergeticexpansion.core.keybinds;

import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;

import endergeticexpansion.api.entity.util.EntityMotionHelper;
import endergeticexpansion.api.util.MathUtils;
import endergeticexpansion.api.util.NetworkUtil;
import endergeticexpansion.common.entities.EntityBoofBlock;
import endergeticexpansion.common.entities.EntityPoiseCluster;
import endergeticexpansion.common.entities.booflo.EntityBooflo;
import endergeticexpansion.common.items.ItemBoofloVest;
import endergeticexpansion.core.EndergeticExpansion;
import endergeticexpansion.core.registry.EEItems;
import endergeticexpansion.core.registry.EESounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemFrameEntity;
import net.minecraft.entity.item.PaintingEntity;
import net.minecraft.entity.monster.ShulkerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent.KeyInputEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

/**
 * @author - SmellyModder(Luke Tonon)
 */
@EventBusSubscriber(modid = EndergeticExpansion.MOD_ID, value = Dist.CLIENT)
public class KeybindHandler {
	private static List<KeyBinding> keyBinds = Lists.newArrayList();
	public static KeyBinding BOOF_VEST = registerKeybind(new KeyBinding("key.endergetic.boof_vest", 32, "key.categories.movement"));
	public static KeyBinding BOOFLO_INFLATE = registerKeybind(new KeyBinding("key.endergetic.booflo_inflate", 32, "key.categories.gameplay"));
	public static KeyBinding BOOFLO_SLAM = registerKeybind(new KeyBinding("key.endergetic.booflo_slam", 88, "key.categories.gameplay"));
	
	public static void registerKeys() {
		for(KeyBinding keys : keyBinds) {
			ClientRegistry.registerKeyBinding(keys);
		}
	}
	
	private static KeyBinding registerKeybind(KeyBinding keybind) {
		keyBinds.add(keybind);
		return keybind;
	}
    	
	@SubscribeEvent(priority = EventPriority.LOW)
	public static void onKeyPressed(KeyInputEvent event) {
		if(BOOF_VEST.isPressed()) {
			PlayerEntity player = Minecraft.getInstance().player;
			Random rand = player.getRNG();
			ItemStack stack = player.inventory.armorItemInSlot(2);
        	
			if(!stack.isEmpty() && stack.getItem() == EEItems.BOOFLO_VEST.get() && !player.onGround && Minecraft.getInstance().currentScreen == null && !player.isSpectator()) {
				ItemBoofloVest vest = (ItemBoofloVest) stack.getItem();
				if(vest.canBoof(stack, player)) {
					CompoundNBT tag = stack.getTag();
    			
					tag.putBoolean("boofed", true);
					tag.putInt("timesBoofed", tag.getInt("timesBoofed") + 1);
					vest.setDelayForBoofedAmount(stack, player);
        			
					NetworkUtil.updateSItemNBT(stack);
					EntityMotionHelper.knockbackEntity(player, 4.0F, 0.75F, true, true);
        			
					for(Entity entity : player.getEntityWorld().getEntitiesWithinAABB(Entity.class, player.getBoundingBox().grow(2.0D))) {
        				if(entity != player &&
        					!(entity instanceof EntityBoofBlock) &&
        					!(entity instanceof ShulkerEntity) &&
        					!(entity instanceof PaintingEntity) &&
        					!(entity instanceof EntityPoiseCluster) &&
        					!(entity instanceof ItemFrameEntity)
        				) {
        					boolean reverse = player.getRidingEntity() == entity;
        					EntityMotionHelper.knockbackEntity(entity, 4.0F, 0.75F, reverse, false);
        				}
        			}
        			
        			for(int i = 0; i < 8; i++) {
        				double offsetX = MathUtils.makeNegativeRandomly(rand.nextFloat() * 0.15F, rand);
        				double offsetZ = MathUtils.makeNegativeRandomly(rand.nextFloat() * 0.15F, rand);
        			
        				double x = player.getPosX() + offsetX;
        				double y = player.getPosY() + (rand.nextFloat() * 0.05F) + 1.25F;
        				double z = player.getPosZ() + offsetZ;
        			
        				NetworkUtil.spawnParticleC2S2C("endergetic:short_poise_bubble", x, y, z, MathUtils.makeNegativeRandomly((rand.nextFloat() * 0.3F), rand) + 0.025F, (rand.nextFloat() * 0.15F) + 0.1F, MathUtils.makeNegativeRandomly((rand.nextFloat() * 0.3F), rand) + 0.025F);
        			}
        			
        			player.playSound(EESounds.BOOFLO_VEST_INFLATE.get(), 1.0F, MathHelper.clamp(1.3F - (tag.getInt("timesBoofed") * 0.15F), 0.25F, 1.0F));
        			
        			NetworkUtil.SBoofEntity(4.0F, 0.75F, 2);
        		}
        	}
    	}
		if(BOOFLO_INFLATE.isKeyDown() && Minecraft.getInstance().currentScreen == null) {
			PlayerEntity player = Minecraft.getInstance().player;
			Entity ridingEntity = player.getRidingEntity();
			if(KeybindHandler.checkRidden(player) && !((EntityBooflo) ridingEntity).isOnGround()) {
				EntityBooflo booflo = (EntityBooflo) ridingEntity;
				if(booflo.isBoofed() && booflo.canPassengerSteer()) {
					if(!booflo.isDelayDecrementing() && !booflo.isDelayExpanding() && booflo.getRideControlDelay() <= 182) {
						if(booflo.getRideControlDelay() >= 182) {
							NetworkUtil.setPlayerNotBoosting(booflo.getEntityId());
						} else {
							NetworkUtil.incrementBoofloBoostTimer(booflo.getEntityId());
						}
					}
				} else if(!booflo.isBoofed() && booflo.canPassengerSteer()) {
					if(booflo.getRideControlDelay() <= 0) {
						NetworkUtil.inflateBooflo(booflo.getEntityId());
					}
				}
			}
		} else {
			PlayerEntity player = Minecraft.getInstance().player;
			if(KeybindHandler.checkRidden(player) && Minecraft.getInstance().currentScreen == null) {
				Entity ridingEntity = player.getRidingEntity();
				EntityBooflo booflo = (EntityBooflo) ridingEntity;
				if(booflo.isBoofed()) {
					if(!booflo.isDelayDecrementing() && !booflo.isDelayExpanding() && booflo.wasPlayerBoosting()) {
						NetworkUtil.setPlayerNotBoosting(booflo.getEntityId());
					}
				}
			}
		}
		if(BOOFLO_SLAM.isPressed() && Minecraft.getInstance().currentScreen == null) {
			PlayerEntity player = Minecraft.getInstance().player;
			Entity ridingEntity = player.getRidingEntity();
			if(KeybindHandler.checkRidden(player)) {
				EntityBooflo booflo = (EntityBooflo) ridingEntity;
				if(booflo.isBoofed()) {
					if(booflo.getRideControlDelay() <= 0 && booflo.isNoEndimationPlaying()) {
						NetworkUtil.slamBooflo(booflo.getEntityId());
					}
				}
			}
		}
	}
	
	private static boolean checkRidden(PlayerEntity player) {
		return player != null && player.isPassenger() && player.getRidingEntity() instanceof EntityBooflo;
	}
}