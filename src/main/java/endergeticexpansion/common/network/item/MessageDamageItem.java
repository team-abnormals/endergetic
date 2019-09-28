package endergeticexpansion.common.network.item;

import java.util.function.Supplier;

import endergeticexpansion.core.registry.EEItems;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;

public class MessageDamageItem {
	private ItemStack itemstack;
	private int amount;
	private boolean isVest;

	public MessageDamageItem() {}

	public MessageDamageItem(ItemStack stack, int amount) {
		this.itemstack = stack;
		this.amount = amount;
		this.isVest = stack.getItem() == EEItems.BOOFLO_VEST ? true : false;
	}

	public void serialize(PacketBuffer buf) {
		buf.writeItemStack(this.itemstack);
		buf.writeInt(this.amount);
	}

	public static MessageDamageItem deserialize(PacketBuffer buf) {
		ItemStack stack = buf.readItemStack();
		int amount = buf.readInt();
		return new MessageDamageItem(stack, amount);
	}

	public static void handle(MessageDamageItem message, Supplier<NetworkEvent.Context> ctx) {
		if(ctx.get().getDirection().getReceptionSide() == LogicalSide.SERVER) {
			ctx.get().enqueueWork(() -> {
				PlayerEntity player = ctx.get().getSender();
				ItemStack vest = player.getItemStackFromSlot(EquipmentSlotType.CHEST);
				if(message.isVest) {
					if(!vest.isEmpty() && vest.getItem() == EEItems.BOOFLO_VEST) {
						vest.damageItem(10, player, (onBroken) -> {
							onBroken.sendBreakAnimation(EquipmentSlotType.CHEST);
						});
					}
				} else {
					if(!player.inventory.getCurrentItem().isEmpty() && player.inventory.getCurrentItem().getItem() == message.itemstack.getItem())
						player.inventory.getCurrentItem().damageItem(1, player, (p_213341_0_) -> {
							p_213341_0_.sendBreakAnimation(EquipmentSlotType.MAINHAND);
						});
				}
			});
			ctx.get().setPacketHandled(true);
		}
	}
}