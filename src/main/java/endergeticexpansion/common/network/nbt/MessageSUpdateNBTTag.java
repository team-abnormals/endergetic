package endergeticexpansion.common.network.nbt;

import java.util.function.Supplier;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class MessageSUpdateNBTTag {
	private CompoundNBT tag;
	private String itemName;

	public MessageSUpdateNBTTag() {}

	public MessageSUpdateNBTTag(ItemStack stack) {
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

	public static void serialize(MessageSUpdateNBTTag message, PacketBuffer packet) {
		message.toBytes(packet);
	}

	public static MessageSUpdateNBTTag deserialize(PacketBuffer packet) {
		MessageSUpdateNBTTag message = new MessageSUpdateNBTTag();

		message.fromBytes(packet);
		return message;
	}

	public static void handle(MessageSUpdateNBTTag message, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			PlayerEntity player = ctx.get().getSender();
			if(!player.inventory.getCurrentItem().isEmpty() && player.inventory.getCurrentItem().getItem().getTranslationKey().equals(message.itemName))
				player.inventory.getCurrentItem().setTag(message.tag);
		});

		ctx.get().setPacketHandled(true);
	}
}
