package endergeticexpansion.common.network.entity;

import java.util.function.Supplier;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;

public class MessageSSetFallDistance {
	private int entityId;
	private int distance;
	
	public MessageSSetFallDistance(int entityId, int distance) {
		this.entityId = entityId;
		this.distance = distance;
	}
	
	public void serialize(PacketBuffer buf) {
		buf.writeInt(this.entityId);
		buf.writeInt(this.distance);
	}
	
	public static MessageSSetFallDistance deserialize(PacketBuffer buf) {
		int entityId = buf.readInt();
		int distance = buf.readInt();
		return new MessageSSetFallDistance(entityId, distance);
	}
	
	public static void handle(MessageSSetFallDistance message, Supplier<NetworkEvent.Context> ctx) {
		NetworkEvent.Context context = ctx.get();
		PlayerEntity player = ctx.get().getSender();
		Entity entity = player.getEntityWorld().getEntityByID(message.entityId);
		if (context.getDirection().getReceptionSide() == LogicalSide.CLIENT) {
			context.enqueueWork(() -> {
				entity.fallDistance = message.distance;
			});
			context.setPacketHandled(true);
		}
	}
}
