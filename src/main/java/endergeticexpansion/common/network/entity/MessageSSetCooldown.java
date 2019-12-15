package endergeticexpansion.common.network.entity;

import java.util.function.Supplier;

import endergeticexpansion.core.registry.EEItems;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;

public class MessageSSetCooldown {
	private String itemName;
	private int cooldown;
	boolean isVest;

	public MessageSSetCooldown() {}

	public MessageSSetCooldown(ItemStack stack, int cooldown, boolean isVest) {
		if(!stack.isEmpty() && stack.hasTag()){
			itemName = stack.getTranslationKey();
		}
		this.cooldown = cooldown;
		this.isVest = isVest;
	}
	
	public void fromBytes(PacketBuffer buf) {
		this.itemName = buf.readString(Integer.MAX_VALUE / 4);
		this.cooldown = buf.readInt();
		this.isVest = buf.readBoolean();
	}

	public void toBytes(PacketBuffer buf) {
		buf.writeString(this.itemName);
		buf.writeInt(this.cooldown);
		buf.writeBoolean(this.isVest);
	}

	public static void serialize(MessageSSetCooldown message, PacketBuffer packet) {
		message.toBytes(packet);
	}

	public static MessageSSetCooldown deserialize(PacketBuffer packet) {
		MessageSSetCooldown message = new MessageSSetCooldown();

		message.fromBytes(packet);
		return message;
	}
	
	public static void handle(MessageSSetCooldown message, Supplier<NetworkEvent.Context> ctx) {
		if (ctx.get().getDirection().getReceptionSide() == LogicalSide.SERVER) {
			ctx.get().enqueueWork(() -> {
				PlayerEntity player = ctx.get().getSender();
				if(message.isVest) {
					ItemStack vest = player.getItemStackFromSlot(EquipmentSlotType.CHEST);
					if(!vest.isEmpty() && vest.getItem() == EEItems.BOOFLO_VEST.get()) {
						player.getCooldownTracker().setCooldown(vest.getItem(), MessageSSetCooldown.getDelayForBoofedAmount(vest));
					}
				}
			});
			ctx.get().setPacketHandled(true);
		}
	}
	
	public static int getDelayForBoofedAmount(ItemStack stack) {
		if(stack.hasTag()) {
			if(stack.getTag().getInt("timesBoofed") < 5) {
				return 7;
			} else if(stack.getTag().getInt("timesBoofed") > 4) {
				return (int) (0.5 * (stack.getTag().getInt("timesBoofed")) * 20);
			}
		}
		return 20;
	}
}
