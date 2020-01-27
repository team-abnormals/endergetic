package endergeticexpansion.common.network.item;

import java.util.function.Supplier;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;

public class MessageDamageItem {
	private ItemStack itemstack;
	private int amount;
	private EquipmentSlotType slotType;

	public MessageDamageItem(ItemStack stack, EquipmentSlotType slotType, int amount) {
		this.itemstack = stack;
		this.amount = amount;
		this.slotType = slotType;
	}

	public void serialize(PacketBuffer buf) {
		buf.writeItemStack(this.itemstack);
		buf.writeInt(this.amount);
		buf.writeString(this.slotType.getName());
	}

	public static MessageDamageItem deserialize(PacketBuffer buf) {
		ItemStack stack = buf.readItemStack();
		int amount = buf.readInt();
		EquipmentSlotType slotType = EquipmentSlotType.fromString(buf.readString());
		return new MessageDamageItem(stack, slotType, amount);
	}

	public static void handle(MessageDamageItem message, Supplier<NetworkEvent.Context> ctx) {
		NetworkEvent.Context context = ctx.get();
		if(context.getDirection().getReceptionSide() == LogicalSide.SERVER) {
			context.enqueueWork(() -> {
				PlayerEntity player = context.getSender();
				player.getItemStackFromSlot(message.slotType).damageItem(1, player, (onBroken) -> {
					onBroken.sendBreakAnimation(message.slotType);
				});
			});
			context.setPacketHandled(true);
		}
	}
}