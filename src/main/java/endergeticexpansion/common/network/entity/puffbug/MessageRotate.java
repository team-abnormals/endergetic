package endergeticexpansion.common.network.entity.puffbug;

import java.util.function.Supplier;

import endergeticexpansion.api.EndergeticAPI.ClientInfo;
import endergeticexpansion.common.entities.puffbug.EntityPuffBug;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;

/**
 * Message that tells the client to rotate a Puff Bug
 * @author - SmellyModder(Luke Tonon)
 */
public class MessageRotate {
	private int entityId;
	private int tickLength;
	private float yaw;
	private float pitch;
	
	public MessageRotate(int entityId, int tickLength, float yaw, float pitch) {
        this.entityId = entityId;
        this.tickLength = tickLength;
        this.yaw = yaw;
        this.pitch = pitch;
    }
	
	public void serialize(PacketBuffer buf) {
		buf.writeInt(this.entityId);
		buf.writeInt(this.tickLength);
		buf.writeFloat(this.yaw);
		buf.writeFloat(this.pitch);
	}
	
	public static MessageRotate deserialize(PacketBuffer buf) {
		return new MessageRotate(buf.readInt(), buf.readInt(), buf.readFloat(), buf.readFloat());
	}
	
	public static void handle(MessageRotate message, Supplier<NetworkEvent.Context> ctx) {
		NetworkEvent.Context context = ctx.get();
		if (context.getDirection().getReceptionSide() == LogicalSide.CLIENT) {
			context.enqueueWork(() -> {
				Entity entity = ClientInfo.getClientPlayerWorld().getEntityByID(message.entityId);
				if(entity instanceof EntityPuffBug) {
					((EntityPuffBug) entity).getRotationController().rotate(message.yaw, message.pitch, message.tickLength);
				}
			});
			context.setPacketHandled(true);
		}
	}
}