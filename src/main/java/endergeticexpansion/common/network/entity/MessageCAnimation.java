package endergeticexpansion.common.network.entity;

import java.util.function.Supplier;

import endergeticexpansion.api.EndergeticAPI.ClientInfo;
import endergeticexpansion.api.endimator.entity.EndimatedEntity;
import endergeticexpansion.api.endimator.entity.IEndimatedEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;

/**
 * Message for telling the client to begin playing an animation
 * @author - SmellyModder(Luke Tonon)
 */
public class MessageCAnimation {
	private int entityId;
	private int animationIndex;
	
	public MessageCAnimation(int entityID, int animationIndex) {
        this.entityId = entityID;
        this.animationIndex = animationIndex;
    }
	
	public void serialize(PacketBuffer buf) {
		buf.writeInt(this.entityId);
		buf.writeInt(this.animationIndex);
	}
	
	public static MessageCAnimation deserialize(PacketBuffer buf) {
		int entityId = buf.readInt();
		int animationIndex = buf.readInt();
		return new MessageCAnimation(entityId, animationIndex);
	}
	
	public static void handle(MessageCAnimation message, Supplier<NetworkEvent.Context> ctx) {
		NetworkEvent.Context context = ctx.get();
		IEndimatedEntity endimatedEntity = (EndimatedEntity) ClientInfo.getClientPlayerWorld().getEntityByID(message.entityId);
		if (context.getDirection().getReceptionSide() == LogicalSide.CLIENT) {
			context.enqueueWork(() -> {
				if(endimatedEntity != null) {
					if(message.animationIndex == -1) {
						endimatedEntity.setPlayingEndimation(IEndimatedEntity.BLANK_ANIMATION);
					} else {
						endimatedEntity.setPlayingEndimation(endimatedEntity.getEndimations()[message.animationIndex]);
					}
				}
			});
			context.setPacketHandled(true);
		}
	}
}