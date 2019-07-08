package endergeticexpansion.common.network.nbt;

import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class MessageCUpdateNBTTag {
	private CompoundNBT tag;
	private String itemName;

	public MessageCUpdateNBTTag() {}

	public MessageCUpdateNBTTag(ItemStack stack) {
		if(!stack.isEmpty() && stack.hasTag()){
			tag = stack.getTag();
			itemName = stack.getTranslationKey();
		}
	}

	public void fromBytes(PacketBuffer buf) {
		tag = buf.readCompoundTag();
		itemName = buf.readString(Integer.MAX_VALUE / 4);
	}

	public void toBytes(PacketBuffer buf) {
		buf.writeCompoundTag(tag);
		buf.writeString(itemName);
	}

	public static void serialize(MessageCUpdateNBTTag message, PacketBuffer packet) {
		message.toBytes(packet);
	}

	public static MessageCUpdateNBTTag deserialize(PacketBuffer packet) {
		MessageCUpdateNBTTag message = new MessageCUpdateNBTTag();

		message.fromBytes(packet);
		return message;
	}

	public static void handle(MessageCUpdateNBTTag message, Supplier<NetworkEvent.Context> ctx) {
		if(!Minecraft.getInstance().player.inventory.getCurrentItem().isEmpty() && Minecraft.getInstance().player.inventory.getCurrentItem().getItem().getTranslationKey().equals(message.itemName)){
			Minecraft.getInstance().player.inventory.getCurrentItem().setTag(message.tag);
		}

		ctx.get().setPacketHandled(true);
	}
}
