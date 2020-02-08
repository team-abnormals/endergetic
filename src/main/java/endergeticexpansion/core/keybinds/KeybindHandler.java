package endergeticexpansion.core.keybinds;

import java.util.List;

import com.google.common.collect.Lists;

import endergeticexpansion.api.util.NetworkUtil;
import endergeticexpansion.common.entities.EntityBoofBlock;
import endergeticexpansion.common.entities.EntityPoiseCluster;
import endergeticexpansion.common.entities.bolloom.EntityBolloomBalloon;
import endergeticexpansion.common.entities.bolloom.EntityBolloomFruit;
import endergeticexpansion.common.entities.booflo.EntityBooflo;
import endergeticexpansion.common.items.ItemBoofloVest;
import endergeticexpansion.core.EndergeticExpansion;
import endergeticexpansion.core.registry.EEItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemFrameEntity;
import net.minecraft.entity.item.PaintingEntity;
import net.minecraft.entity.monster.ShulkerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
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
    		ItemStack stack = player.inventory.armorItemInSlot(2);
        	
    		if(!stack.isEmpty() && stack.getItem() == EEItems.BOOFLO_VEST.get() && !player.onGround && Minecraft.getInstance().currentScreen == null && !player.isSpectator()) {
        		if(((ItemBoofloVest)stack.getItem()).canBoof(stack, player)) {
        			stack.getTag().putBoolean("boofed", true);
        			stack.getTag().putInt("timesBoofed", stack.getTag().getInt("timesBoofed") + 1);
        			((ItemBoofloVest) stack.getItem()).setDelayForBoofedAmount(stack, player);
        			NetworkUtil.updateSItemNBT(stack);
        			
        			double[] vars = {4D, player.rotationYaw, Math.PI, 180D};
        			player.setVelocity(-MathHelper.sin((float) (vars[1] * vars[2] / vars[3])) * vars[0] * 0.1D, 0.75D, MathHelper.cos((float) (vars[1] * vars[2] / vars[3])) * vars[0] * 0.1D);
        			
        			AxisAlignedBB bb = player.getBoundingBox().grow(2.0D);
        			List<Entity> entities = player.getEntityWorld().getEntitiesWithinAABB(Entity.class, bb);
        			for(int i = 0; i < entities.size(); i++) {
        				Entity entity = entities.get(i);
        				
        				if(entity.getEntityId() != player.getEntityId() &&
        					!(entity instanceof EntityBoofBlock) &&
        					!(entity instanceof ShulkerEntity) &&
        					!(entity instanceof PaintingEntity) &&
        					!(entity instanceof EntityPoiseCluster) &&
        					!(entity instanceof ItemFrameEntity)
        				) {
        					if(entity instanceof EntityBolloomFruit) {
        						if(((EntityBolloomFruit)entity).isUntied()) {
        							entity.addVelocity(MathHelper.sin((float) (entity.rotationYaw * vars[2] / vars[3])) * vars[0] * 0.1F, 0.75D, -MathHelper.cos((float) (entity.rotationYaw * vars[2] / vars[3])) * vars[0] * 0.1F);
        						}
        					} else if(entity instanceof EntityBolloomBalloon) {
        						if(((EntityBolloomBalloon)entity).isUntied()) {
        							entity.addVelocity(MathHelper.sin((float) (entity.rotationYaw * vars[2] / vars[3])) * vars[0] * 0.1F, 0.75D, -MathHelper.cos((float) (entity.rotationYaw * vars[2] / vars[3])) * vars[0] * 0.1F);
        						}
        					} else {
        						entity.addVelocity(MathHelper.sin((float) (entity.rotationYaw * vars[2] / vars[3])) * vars[0] * 0.1F, 0.75D, -MathHelper.cos((float) (entity.rotationYaw * vars[2] / vars[3])) * vars[0] * 0.1F);
        					}
        				}
        			}
        			NetworkUtil.SBoofEntity(4.0D, 0.75D, 4.0D, 2);
        		}
        	}
    	}
		if(BOOFLO_INFLATE.isPressed() && Minecraft.getInstance().currentScreen == null) {
			PlayerEntity player = Minecraft.getInstance().player;
			Entity ridingEntity = player.getRidingEntity();
			if(KeybindHandler.checkRidden(player) && !((EntityBooflo) ridingEntity).isOnGround()) {
				EntityBooflo booflo = (EntityBooflo) ridingEntity;
				if(!booflo.isBoofed() && booflo.canPassengerSteer()) {
					if(booflo.getRideControlDelay() <= 0) {
						NetworkUtil.inflateBooflo(booflo.getEntityId());
					}
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
	
	public static boolean checkRidden(PlayerEntity player) {
		return player != null && player.isPassenger() && player.getRidingEntity() instanceof EntityBooflo;
	}
    
	public static boolean isPlayerOnGroundReal(PlayerEntity player) {
		if(player.getEntityWorld().getBlockState(player.getPosition().down()).isSolid()) {
			return true;
    	}
		return false;
	}
}