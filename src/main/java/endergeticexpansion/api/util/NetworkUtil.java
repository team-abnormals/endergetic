package endergeticexpansion.api.util;

import endergeticexpansion.common.network.item.MessageDamageItem;
import endergeticexpansion.common.network.nbt.MessageCUpdateNBTTag;
import endergeticexpansion.common.network.nbt.MessageSUpdateNBTTag;
import endergeticexpansion.common.network.player.MessageCSetVelocity;
import endergeticexpansion.common.network.player.MessageSSetCooldown;
import endergeticexpansion.common.network.player.MessageSSetVelocity;
import endergeticexpansion.core.EndergeticExpansion;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.PacketDistributor;

/**
 * @author - SmellyModder(Luke Tonon)
 * This class holds a big list of useful network functions. Most are used in the mod
 */
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
	
	/**
	 * @param motion{Vec3d} - The vector motion of the entity
	 * @param id{Integer} - The Player's Entity Id
	 * Used for setting the client side Player Velocity from the server side
	 */
	@OnlyIn(Dist.DEDICATED_SERVER)
	public static void setCPlayerVelocity(Vec3d motion, int id) {
		EndergeticExpansion.CHANNEL.sendToServer(new MessageCSetVelocity(motion, id));
	}
	
	/**
	 * @param motion{Vec3d} - The vector motion of the entity
	 * @param id{Integer} - The Player's Entity Id
	 * Used for setting server side Player Velocity from the client side
	 */
	@OnlyIn(Dist.CLIENT)
	public static void setSPlayerVelocity(Vec3d motion, int id) {
		EndergeticExpansion.CHANNEL.sendToServer(new MessageSSetVelocity(motion, id));
	}
	
	/**
	 * @param stack{itemstack} - The stack do damage
	 * @param amount{Integer} - The amount to damage
	 * Used for damaging items from the client side
	 */
	@OnlyIn(Dist.CLIENT)
	public static void damageItem(ItemStack stack, int amount) {
		EndergeticExpansion.CHANNEL.sendToServer(new MessageDamageItem(stack, amount));
	}
	
}
