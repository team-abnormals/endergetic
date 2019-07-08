package endergeticexpansion.core.keybinds;

import java.util.List;

import com.google.common.collect.Lists;

import endergeticexpansion.api.util.NetworkUtil;
import endergeticexpansion.common.items.ItemBoofloVest;
import endergeticexpansion.core.EndergeticExpansion;
import endergeticexpansion.core.registry.EEItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent.KeyInputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;

/**
 * @author - SmellyModder(Luke Tonon)
 */
@Mod.EventBusSubscriber(modid = EndergeticExpansion.MOD_ID, value = Dist.CLIENT)
public class KeybindHandler {
    private static List<KeyBinding> keyBinds = Lists.newArrayList();
    public static KeyBinding BOOF = new KeyBinding("key.endergetic.boof", 32, EndergeticExpansion.MOD_ID);
    
    public static void registerKeys() {
        for(KeyBinding keys : keyBinds) {
            ClientRegistry.registerKeyBinding(keys);
        }
    }
    
    @SubscribeEvent
    public static void onKeyPressed(KeyInputEvent event) {
        if(BOOF.isPressed()) {
        	PlayerEntity player = Minecraft.getInstance().player;
        	
        	ItemStack stack = player.inventory.armorItemInSlot(2);
        	if(!stack.isEmpty() && stack.getItem() == EEItems.BOOFLO_VEST && !KeybindHandler.isPlayerOnGroundReal(player) && !player.onGround && Minecraft.getInstance().currentScreen == null) {
        		if(((ItemBoofloVest)stack.getItem()).canBoof(stack, player)) {
        			player.fallDistance = player.fallDistance - 2;
        			stack.getTag().putBoolean("boofed", true);
        			stack.getTag().putInt("timesBoofed", stack.getTag().getInt("timesBoofed") + 1);
        			((ItemBoofloVest)stack.getItem()).setDelayForBoofedAmount(stack, player);
        			NetworkUtil.damageItem(stack, 1);
        			NetworkUtil.updateSItemNBT(stack);
        			
        			double[] vars = {4D, player.rotationYaw, 3.141593D, 180D};
        			player.setVelocity(-MathHelper.sin((float) (vars[1] * vars[2] / vars[3])) * vars[0] * 0.1D, 0.75D, MathHelper.cos((float) (vars[1] * vars[2] / vars[3])) * vars[0] * 0.1D);
        			NetworkUtil.setSPlayerVelocity(new Vec3d(-MathHelper.sin((float) (vars[1] * vars[2] / vars[3])) * vars[0] * 0.75D, 1.0D, MathHelper.cos((float) (vars[1] * vars[2] / vars[3])) * vars[0] * 0.1D), player.getEntityId());
        		}
        	}
        }
    }
    
    public static boolean isPlayerOnGroundReal(PlayerEntity player) {
    	if(player.getEntityWorld().getBlockState(player.getPosition().down()).isSolid()) {
    		return true;
    	}
    	return false;
    }
    
}
