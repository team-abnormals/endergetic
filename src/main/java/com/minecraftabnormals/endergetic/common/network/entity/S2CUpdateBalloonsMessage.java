package com.minecraftabnormals.endergetic.common.network.entity;

import com.minecraftabnormals.abnormals_core.client.ClientInfo;
import com.minecraftabnormals.endergetic.common.entities.bolloom.BolloomBalloonEntity;
import com.minecraftabnormals.endergetic.core.EndergeticExpansion;
import com.minecraftabnormals.endergetic.core.interfaces.BalloonHolder;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.List;
import java.util.function.Supplier;

public final class S2CUpdateBalloonsMessage {
	private int entityId;
	private int[] balloonIds;

	private S2CUpdateBalloonsMessage(int entityId, int[] balloonIds) {
		this.entityId = entityId;
		this.balloonIds = balloonIds;
	}

	public S2CUpdateBalloonsMessage(Entity entity) {
		this.entityId = entity.getEntityId();
		List<BolloomBalloonEntity> balloons = ((BalloonHolder) entity).getBalloons();
		this.balloonIds = new int[balloons.size()];
		for (int i = 0; i < balloons.size(); i++) {
			this.balloonIds[i] = balloons.get(i).getEntityId();
		}
	}

	public void serialize(PacketBuffer buf) {
		buf.writeVarInt(this.entityId);
		buf.writeVarIntArray(this.balloonIds);
	}

	public static S2CUpdateBalloonsMessage deserialize(PacketBuffer buf) {
		return new S2CUpdateBalloonsMessage(buf.readVarInt(), buf.readVarIntArray());
	}

	public static void handle(S2CUpdateBalloonsMessage message, Supplier<NetworkEvent.Context> ctx) {
		NetworkEvent.Context context = ctx.get();
		if (context.getDirection().getReceptionSide() == LogicalSide.CLIENT) {
			context.enqueueWork(() -> {
				World world = ClientInfo.getClientPlayerWorld();
				Entity entity = world.getEntityByID(message.entityId);
				if (entity == null) {
					EndergeticExpansion.LOGGER.warn("Received balloons for unknown entity!");
				} else {
					((BalloonHolder) entity).detachBalloons();
					for (int id : message.balloonIds) {
						Entity balloon = world.getEntityByID(id);
						if (balloon instanceof BolloomBalloonEntity) {
							((BolloomBalloonEntity) balloon).attachToEntity(entity);
						}
					}
				}
			});
		}
		context.setPacketHandled(true);
	}
}
