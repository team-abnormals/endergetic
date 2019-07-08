package endergeticexpansion.api.util;

import endergeticexpansion.common.network.nbt.MessageCUpdateNBTTag;
import endergeticexpansion.common.network.nbt.MessageSUpdateNBTTag;
import endergeticexpansion.common.network.player.MessageSSetCooldown;
import endergeticexpansion.core.EndergeticExpansion;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.PacketDistributor;

public class NetworkUtil {
	
	/**
	 * @param stack{ItemStack} - The stack that the message will update the nbt for
	 * Used for updating the server nbt from the client. For example it's used in booflo vest keybinds
	 */
	@OnlyIn(Dist.CLIENT)
	public static void updateSItemNBT(ItemStack stack) {
		EndergeticExpansion.CHANNEL.sendToServer(new MessageSUpdateNBTTag(stack));
	}
	
	/**
	 * @param stack{ItemStack} - the stack that the message will update the nbt for
	 * Used for updating the client nbt from the server
	 */
	@OnlyIn(Dist.DEDICATED_SERVER)
	public static void updateCItemNBT(ItemStack stack) {
		EndergeticExpansion.CHANNEL.send(PacketDistributor.SERVER.noArg(), new MessageCUpdateNBTTag(stack));
	}
	
	/**
	 * @param stack{ItemStack} - The stack that the message will set the cooldown for
	 * @param cooldown{Integer} - The amount of time the cooldown will last; measured in ticks. 1 second = 20 ticks
	 * @param isVest{Boolean} - A boolean that specifies that the cooldown is for the Booflo Vest item
	 * Used for updating the client nbt from the server
	 */
	@OnlyIn(Dist.CLIENT)
	public static void setSItemCooldown(ItemStack stack, int cooldown, boolean isVest) {
		EndergeticExpansion.CHANNEL.sendToServer(new MessageSSetCooldown(stack, cooldown, isVest));
	}
	
}
