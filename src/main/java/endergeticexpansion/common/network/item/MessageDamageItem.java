package endergeticexpansion.common.network.item;

import java.util.function.Supplier;

import endergeticexpansion.core.registry.EEItems;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class MessageDamageItem {
	private String itemName;
	private int amount;
	private boolean isVest;

	public MessageDamageItem() {}

	public MessageDamageItem(ItemStack stack, int amount) {
		if(!stack.isEmpty() && stack.hasTag()){
			itemName = stack.getTranslationKey();
		}
		this.amount = amount;
		isVest = stack.getItem() == EEItems.BOOFLO_VEST ? true : false;
	}

	public void fromBytes(PacketBuffer buf) {
		this.itemName = buf.readString(Integer.MAX_VALUE / 4);
		this.amount = buf.readInt();
		this.isVest = buf.readBoolean();
	}

	public void toBytes(PacketBuffer buf) {
		buf.writeString(itemName);
		buf.writeInt(amount);
		buf.writeBoolean(isVest);
	}

	public static void serialize(MessageDamageItem message, PacketBuffer packet) {
		message.toBytes(packet);
	}

	public static MessageDamageItem deserialize(PacketBuffer packet) {
		MessageDamageItem message = new MessageDamageItem();

		message.fromBytes(packet);
		return message;
	}

	public static void handle(MessageDamageItem message, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			PlayerEntity player = ctx.get().getSender();
			if(message.isVest) {
				if(!player.inventory.armorItemInSlot(2).isEmpty() && player.inventory.armorItemInSlot(2).getItem() == EEItems.BOOFLO_VEST) {
					player.inventory.armorItemInSlot(2).damageItem(1, player, (p_213341_0_) -> {
						p_213341_0_.sendBreakAnimation(EquipmentSlotType.CHEST);
					});
				}
			} else {
				if(!player.inventory.getCurrentItem().isEmpty() && player.inventory.getCurrentItem().getItem().getTranslationKey().equals(message.itemName))
					player.inventory.getCurrentItem().damageItem(1, player, (p_213341_0_) -> {
						p_213341_0_.sendBreakAnimation(EquipmentSlotType.MAINHAND);
					});
			}
		});

		ctx.get().setPacketHandled(true);
	}
}
