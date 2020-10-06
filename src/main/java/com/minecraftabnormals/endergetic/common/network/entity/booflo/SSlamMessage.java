package com.minecraftabnormals.endergetic.common.network.entity.booflo;

import java.util.function.Supplier;

import com.teamabnormals.abnormals_core.core.utils.NetworkUtil;
import com.minecraftabnormals.endergetic.common.entities.booflo.BoofloEntity;

import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;

/**
 * Message that tells the server to slam the booflo
 *
 * @author - SmellyModder(Luke Tonon)
 */
public class SSlamMessage {
	private int entityId;

	public SSlamMessage(int entityId) {
		this.entityId = entityId;
	}

	public void serialize(PacketBuffer buf) {
		buf.writeInt(this.entityId);
	}

	public static SSlamMessage deserialize(PacketBuffer buf) {
		return new SSlamMessage(buf.readInt());
	}

	public static void handle(SSlamMessage message, Supplier<NetworkEvent.Context> ctx) {
		NetworkEvent.Context context = ctx.get();
		if (context.getDirection().getReceptionSide() == LogicalSide.SERVER) {
			context.enqueueWork(() -> {
				Entity entity = context.getSender().world.getEntityByID(message.entityId);
				if (entity instanceof BoofloEntity) {
					((BoofloEntity) entity).setDelayExpanding(true);
					NetworkUtil.setPlayingAnimationMessage((BoofloEntity) entity, BoofloEntity.CHARGE);
				}
			});
			context.setPacketHandled(true);
		}
	}
}