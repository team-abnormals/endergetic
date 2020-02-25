package endergeticexpansion.common.network.nbt;

import java.util.function.Supplier;

import endergeticexpansion.core.registry.EEItems;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkEvent.Context;

public class MessageSUpdateNBTTag {
	private CompoundNBT tag;
	private String itemName;
	private boolean isVest;

	public MessageSUpdateNBTTag() {}

	public MessageSUpdateNBTTag(ItemStack stack) {
		if(!stack.isEmpty() && stack.hasTag()){
			tag = stack.getTag();
			itemName = stack.getTranslationKey();
		}
		isVest = stack.getItem() == EEItems.BOOFLO_VEST.get() ? true : false;
	}

	public void fromBytes(PacketBuffer buf) {
		tag = buf.readCompoundTag();
		itemName = buf.readString(Integer.MAX_VALUE / 4);
		isVest = buf.readBoolean();
	}

	public void toBytes(PacketBuffer buf) {
		buf.writeCompoundTag(tag);
		buf.writeString(itemName);
		buf.writeBoolean(isVest);
	}

	public static void serialize(MessageSUpdateNBTTag message, PacketBuffer packet) {
		message.toBytes(packet);
	}

	public static MessageSUpdateNBTTag deserialize(PacketBuffer packet) {
		MessageSUpdateNBTTag message = new MessageSUpdateNBTTag();

		message.fromBytes(packet);
		return message;
	}

	public static boolean handle(MessageSUpdateNBTTag message, Supplier<NetworkEvent.Context> ctx) {
		Context context = ctx.get();
		if(context.getDirection().getReceptionSide() == LogicalSide.SERVER) {
			context.enqueueWork(() -> {
				PlayerEntity player = ctx.get().getSender();
				ItemStack vest = player.getItemStackFromSlot(EquipmentSlotType.CHEST);
				if(message.isVest) {
					if(!vest.isEmpty() && vest.getItem() == EEItems.BOOFLO_VEST.get()) {
						vest.setTag(message.tag);
					}
				} else {
					if(!player.inventory.getCurrentItem().isEmpty() && player.inventory.getCurrentItem().getItem().getTranslationKey().equals(message.itemName))
						player.inventory.getCurrentItem().setTag(message.tag);
				}
			});
		}
		
		return true;
	}
}
