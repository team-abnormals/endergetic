package com.minecraftabnormals.endergetic.common.network.entity;

import java.util.UUID;
import java.util.function.Supplier;

import com.minecraftabnormals.endergetic.common.entities.bolloom.BolloomBalloonEntity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkEvent.Context;

public class S2CRemoveBalloonFromOrderMap {
	public final int boatId, balloonId;
	
	public S2CRemoveBalloonFromOrderMap(int boatId, int balloonId) {
		this.boatId = boatId;
		this.balloonId = balloonId;
	}
	
	public void serialize(PacketBuffer buf) {
		buf.writeInt(this.boatId);
		buf.writeInt(this.balloonId);
	}
	
	public static S2CRemoveBalloonFromOrderMap deserialize(PacketBuffer buf) {
		return new S2CRemoveBalloonFromOrderMap(buf.readInt(), buf.readInt());
	}
	
	public static void handle(S2CRemoveBalloonFromOrderMap message, Supplier<NetworkEvent.Context> ctx) {
		Context context = ctx.get();
		if(context.getDirection().getReceptionSide() == LogicalSide.CLIENT) {
			context.enqueueWork(() -> {
				ClientWorld world = Minecraft.getInstance().world;
				UUID boatUUID = world.getEntityByID(message.boatId).getUniqueID();
				UUID uuid = world.getEntityByID(message.balloonId).getUniqueID();
				if (BolloomBalloonEntity.BALLOONS_ON_BOAT_MAP.containsKey(boatUUID) && BolloomBalloonEntity.BALLOONS_ON_BOAT_MAP.get(boatUUID).containsKey(uuid)) {
					BolloomBalloonEntity.BALLOONS_ON_BOAT_MAP.get(boatUUID).remove(uuid);
				}
			});
		}
		context.setPacketHandled(true);
	}
}