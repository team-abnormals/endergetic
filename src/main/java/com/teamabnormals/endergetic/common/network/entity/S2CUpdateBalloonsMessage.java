package com.teamabnormals.endergetic.common.network.entity;

import com.teamabnormals.endergetic.common.entities.bolloom.BolloomBalloonEntity;
import com.teamabnormals.endergetic.core.EndergeticExpansion;
import com.teamabnormals.endergetic.core.interfaces.BalloonHolder;
import com.teamabnormals.blueprint.client.ClientInfo;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

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
		this.entityId = entity.getId();
		List<BolloomBalloonEntity> balloons = ((BalloonHolder) entity).getBalloons();
		this.balloonIds = new int[balloons.size()];
		for (int i = 0; i < balloons.size(); i++) {
			this.balloonIds[i] = balloons.get(i).getId();
		}
	}

	public void serialize(FriendlyByteBuf buf) {
		buf.writeVarInt(this.entityId);
		buf.writeVarIntArray(this.balloonIds);
	}

	public static S2CUpdateBalloonsMessage deserialize(FriendlyByteBuf buf) {
		return new S2CUpdateBalloonsMessage(buf.readVarInt(), buf.readVarIntArray());
	}

	public static void handle(S2CUpdateBalloonsMessage message, Supplier<NetworkEvent.Context> ctx) {
		NetworkEvent.Context context = ctx.get();
		if (context.getDirection().getReceptionSide() == LogicalSide.CLIENT) {
			context.enqueueWork(() -> {
				Level world = ClientInfo.getClientPlayerLevel();
				Entity entity = world.getEntity(message.entityId);
				if (entity == null) {
					EndergeticExpansion.LOGGER.warn("Received balloons for unknown entity!");
				} else {
					((BalloonHolder) entity).detachBalloons();
					for (int id : message.balloonIds) {
						Entity balloon = world.getEntity(id);
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
