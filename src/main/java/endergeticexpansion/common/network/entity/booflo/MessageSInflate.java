package endergeticexpansion.common.network.entity.booflo;

import java.util.function.Supplier;

import com.teamabnormals.abnormals_core.core.utils.NetworkUtil;

import endergeticexpansion.common.entities.booflo.EntityBooflo;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;

/**
 * Message that tells the server to inflate the booflo
 * @author - SmellyModder(Luke Tonon)
 */
public class MessageSInflate {
	private int entityId;
	
	public MessageSInflate(int entityId) {
        this.entityId = entityId;
    }
	
	public void serialize(PacketBuffer buf) {
		buf.writeInt(this.entityId);
	}
	
	public static MessageSInflate deserialize(PacketBuffer buf) {
		return new MessageSInflate(buf.readInt());
	}
	
	public static void handle(MessageSInflate message, Supplier<NetworkEvent.Context> ctx) {
		NetworkEvent.Context context = ctx.get();
		if (context.getDirection().getReceptionSide() == LogicalSide.SERVER) {
			context.enqueueWork(() -> {
				Entity entity = context.getSender().world.getEntityByID(message.entityId);
				if(entity instanceof EntityBooflo) {
					EntityBooflo booflo = (EntityBooflo) entity;
					booflo.setBoofed(true);
					booflo.setDelayExpanding(true);
					booflo.setDelayDecrementing(false);
					NetworkUtil.setPlayingAnimationMessage(booflo, EntityBooflo.INFLATE);
				}
			});
			context.setPacketHandled(true);
		}
	}
}