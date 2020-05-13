package endergeticexpansion.common.network.entity.booflo;

import java.util.function.Supplier;

import com.teamabnormals.abnormals_core.core.utils.NetworkUtil;

import endergeticexpansion.common.entities.booflo.EntityBooflo;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;

/**
 * Message that tells the server to set player no longer boosting on the booflo
 * @author - SmellyModder(Luke Tonon)
 */
public class MessageSSetPlayerNotBoosting {
	private int entityId;
	
	public MessageSSetPlayerNotBoosting(int entityId) {
        this.entityId = entityId;
    }
	
	public void serialize(PacketBuffer buf) {
		buf.writeInt(this.entityId);
	}
	
	public static MessageSSetPlayerNotBoosting deserialize(PacketBuffer buf) {
		return new MessageSSetPlayerNotBoosting(buf.readInt());
	}
	
	public static void handle(MessageSSetPlayerNotBoosting message, Supplier<NetworkEvent.Context> ctx) {
		NetworkEvent.Context context = ctx.get();
		if (context.getDirection().getReceptionSide() == LogicalSide.SERVER) {
			context.enqueueWork(() -> {
				Entity entity = context.getSender().world.getEntityByID(message.entityId);
				if(entity instanceof EntityBooflo) {
					EntityBooflo booflo = (EntityBooflo) entity;
					booflo.setPlayerBoosting(false);
					booflo.setDelayDecrementing(true);
					booflo.playSound(booflo.getInflateSound(), 0.75F, 1.0F);
					booflo.setBoostPower(MathHelper.clamp(booflo.getRideControlDelay() * 0.01F, 0.35F, 1.82F));
					NetworkUtil.setPlayingAnimationMessage(booflo, EntityBooflo.INFLATE);
				}
			});
			context.setPacketHandled(true);
		}
	}
}