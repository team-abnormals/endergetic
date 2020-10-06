package com.minecraftabnormals.endergetic.common.network.entity.puffbug;

import java.util.function.Supplier;

import com.teamabnormals.abnormals_core.client.ClientInfo;
import com.minecraftabnormals.endergetic.common.entities.puffbug.PuffBugEntity;

import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;

/**
 * Message that tells the client to rotate a Puff Bug
 *
 * @author - SmellyModder(Luke Tonon)
 */
public class RotateMessage {
	private int entityId;
	private int tickLength;
	private float yaw;
	private float pitch;
	private float roll;

	public RotateMessage(int entityId, int tickLength, float yaw, float pitch, float roll) {
		this.entityId = entityId;
		this.tickLength = tickLength;
		this.yaw = yaw;
		this.pitch = pitch;
		this.roll = roll;
	}

	public void serialize(PacketBuffer buf) {
		buf.writeInt(this.entityId);
		buf.writeInt(this.tickLength);
		buf.writeFloat(this.yaw);
		buf.writeFloat(this.pitch);
		buf.writeFloat(this.roll);
	}

	public static RotateMessage deserialize(PacketBuffer buf) {
		return new RotateMessage(buf.readInt(), buf.readInt(), buf.readFloat(), buf.readFloat(), buf.readFloat());
	}

	public static void handle(RotateMessage message, Supplier<NetworkEvent.Context> ctx) {
		NetworkEvent.Context context = ctx.get();
		if (context.getDirection().getReceptionSide() == LogicalSide.CLIENT) {
			context.enqueueWork(() -> {
				Entity entity = ClientInfo.getClientPlayerWorld().getEntityByID(message.entityId);
				if (entity instanceof PuffBugEntity) {
					((PuffBugEntity) entity).getRotationController().rotate(message.yaw, message.pitch, message.roll, message.tickLength);
				}
			});
			context.setPacketHandled(true);
		}
	}
}