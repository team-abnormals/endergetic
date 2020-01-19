package endergeticexpansion.common.network.entity.booflo;

import java.util.function.Supplier;

import endergeticexpansion.common.entities.booflo.EntityBooflo;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;

/**
 * Message that tells the server to inflate the booflo
 * @author - SmellyModder(Luke Tonon)
 */
public class MessageSIncrementBoostDelay {
	private int entityId;
	
	public MessageSIncrementBoostDelay(int entityId) {
        this.entityId = entityId;
    }
	
	public void serialize(PacketBuffer buf) {
		buf.writeInt(this.entityId);
	}
	
	public static MessageSIncrementBoostDelay deserialize(PacketBuffer buf) {
		return new MessageSIncrementBoostDelay(buf.readInt());
	}
	
	public static void handle(MessageSIncrementBoostDelay message, Supplier<NetworkEvent.Context> ctx) {
		NetworkEvent.Context context = ctx.get();
		if (context.getDirection().getReceptionSide() == LogicalSide.SERVER) {
			context.enqueueWork(() -> {
				Entity entity = context.getSender().world.getEntityByID(message.entityId);
				if(entity instanceof EntityBooflo) {
					((EntityBooflo) entity).setRideControlDelay(((EntityBooflo) entity).getRideControlDelay() + 10);
					((EntityBooflo) entity).setPlayerBoosting(true);
				}
			});
			context.setPacketHandled(true);
		}
	}
}