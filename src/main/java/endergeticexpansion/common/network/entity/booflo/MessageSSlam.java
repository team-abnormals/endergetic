package endergeticexpansion.common.network.entity.booflo;

import java.util.function.Supplier;

import endergeticexpansion.api.util.NetworkUtil;
import endergeticexpansion.common.entities.booflo.EntityBooflo;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;

/**
 * Message that tells the server to slam the booflo
 * @author - SmellyModder(Luke Tonon)
 */
public class MessageSSlam {
	private int entityId;
	
	public MessageSSlam(int entityId) {
        this.entityId = entityId;
    }
	
	public void serialize(PacketBuffer buf) {
		buf.writeInt(this.entityId);
	}
	
	public static MessageSSlam deserialize(PacketBuffer buf) {
		return new MessageSSlam(buf.readInt());
	}
	
	public static void handle(MessageSSlam message, Supplier<NetworkEvent.Context> ctx) {
		NetworkEvent.Context context = ctx.get();
		if (context.getDirection().getReceptionSide() == LogicalSide.SERVER) {
			context.enqueueWork(() -> {
				Entity entity = context.getSender().world.getEntityByID(message.entityId);
				if(entity instanceof EntityBooflo) {
					((EntityBooflo) entity).setDelayExpanding(true);
					NetworkUtil.setPlayingAnimationMessage((EntityBooflo) entity, EntityBooflo.CHARGE);
				}
			});
			context.setPacketHandled(true);
		}
	}
}