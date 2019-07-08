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
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent.KeyInputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;

/*
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
        	if(!stack.isEmpty() && stack.getItem() == EEItems.BOOFLO_VEST && !player.onGround) {
        		if(((ItemBoofloVest)stack.getItem()).canBoof(stack, player)) {
        			stack.getTag().putBoolean("boofed", true);
        			stack.getTag().putInt("timesBoofed", stack.getTag().getInt("timesBoofed") + 1);
        			((ItemBoofloVest)stack.getItem()).setDelayForBoofedAmount(stack, player);
        			NetworkUtil.updateSItemNBT(stack);
        		}
        	}
        }
    }
    
    public boolean hasTag(ItemStack stack) {
		if(!stack.hasTag()) {
			stack.setTag(new CompoundNBT());
			return false;
		}
		return true;
	}
}
