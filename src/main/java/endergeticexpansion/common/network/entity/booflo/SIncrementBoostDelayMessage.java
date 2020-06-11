package endergeticexpansion.common.network.entity.booflo;

import java.util.function.Supplier;

import endergeticexpansion.common.entities.booflo.BoofloEntity;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;

/**
 * Message that tells the server to inflate the booflo
 * @author - SmellyModder(Luke Tonon)
 */
public class SIncrementBoostDelayMessage {
	private int entityId;
	
	public SIncrementBoostDelayMessage(int entityId) {
        this.entityId = entityId;
    }
	
	public void serialize(PacketBuffer buf) {
		buf.writeInt(this.entityId);
	}
	
	public static SIncrementBoostDelayMessage deserialize(PacketBuffer buf) {
		return new SIncrementBoostDelayMessage(buf.readInt());
	}
	
	public static void handle(SIncrementBoostDelayMessage message, Supplier<NetworkEvent.Context> ctx) {
		NetworkEvent.Context context = ctx.get();
		if (context.getDirection().getReceptionSide() == LogicalSide.SERVER) {
			context.enqueueWork(() -> {
				Entity entity = context.getSender().world.getEntityByID(message.entityId);
				if(entity instanceof BoofloEntity) {
					((BoofloEntity) entity).setRideControlDelay(((BoofloEntity) entity).getRideControlDelay() + 10);
					((BoofloEntity) entity).setPlayerBoosting(true);
				}
			});
			context.setPacketHandled(true);
		}
	}
}