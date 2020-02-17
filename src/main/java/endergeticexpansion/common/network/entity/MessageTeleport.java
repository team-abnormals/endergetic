package endergeticexpansion.common.network.entity;

import java.util.function.Supplier;

import endergeticexpansion.api.EndergeticAPI.ClientInfo;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;

public class MessageTeleport {
	private int entityId;
	private double posX, posY, posZ;
	
	public MessageTeleport(int entityID, double posX, double posY, double posZ) {
		this.entityId = entityID;
		this.posX = posX;
		this.posY = posY;
		this.posZ = posZ;
	}
	
	public void serialize(PacketBuffer buf) {
		buf.writeInt(this.entityId);
		buf.writeDouble(this.posX);
		buf.writeDouble(this.posY);
		buf.writeDouble(this.posZ);
	}
	
	public static MessageTeleport deserialize(PacketBuffer buf) {
		int entityId = buf.readInt();
		return new MessageTeleport(entityId, buf.readDouble(), buf.readDouble(), buf.readDouble());
	}
	
	public static void handle(MessageTeleport message, Supplier<NetworkEvent.Context> ctx) {
		NetworkEvent.Context context = ctx.get();
		Entity entity = ClientInfo.getClientPlayerWorld().getEntityByID(message.entityId);
		if(context.getDirection().getReceptionSide() == LogicalSide.CLIENT) {
			context.enqueueWork(() -> {
				entity.setLocationAndAngles(message.posX, message.posY, message.posZ, entity.rotationYaw, entity.rotationPitch);
			});
			context.setPacketHandled(true);
		}
	}
}