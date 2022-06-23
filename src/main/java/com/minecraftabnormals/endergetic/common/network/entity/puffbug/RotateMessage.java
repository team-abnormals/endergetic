package com.minecraftabnormals.endergetic.common.network.entity.puffbug;

import com.minecraftabnormals.endergetic.common.entities.puffbug.PuffBugEntity;

import com.teamabnormals.blueprint.client.ClientInfo;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

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

	public void serialize(FriendlyByteBuf buf) {
		buf.writeInt(this.entityId);
		buf.writeInt(this.tickLength);
		buf.writeFloat(this.yaw);
		buf.writeFloat(this.pitch);
		buf.writeFloat(this.roll);
	}

	public static RotateMessage deserialize(FriendlyByteBuf buf) {
		return new RotateMessage(buf.readInt(), buf.readInt(), buf.readFloat(), buf.readFloat(), buf.readFloat());
	}

	public static void handle(RotateMessage message, Supplier<NetworkEvent.Context> ctx) {
		NetworkEvent.Context context = ctx.get();
		if (context.getDirection().getReceptionSide() == LogicalSide.CLIENT) {
			context.enqueueWork(() -> {
				Entity entity = ClientInfo.getClientPlayerLevel().getEntity(message.entityId);
				if (entity instanceof PuffBugEntity) {
					((PuffBugEntity) entity).getRotationController().rotate(message.yaw, message.pitch, message.roll, message.tickLength);
				}
			});
			context.setPacketHandled(true);
		}
	}
}