package com.minecraftabnormals.endergetic.common.network.entity;

import java.util.function.Supplier;

import com.minecraftabnormals.endergetic.common.entities.bolloom.BolloomBalloonEntity;
import com.minecraftabnormals.endergetic.core.interfaces.CustomBalloonPositioner;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkEvent.Context;

public final class S2CDetachCustomPositionBalloon {
	public final int boatId, balloonId;
	
	public S2CDetachCustomPositionBalloon(int boatId, int balloonId) {
		this.boatId = boatId;
		this.balloonId = balloonId;
	}
	
	public void serialize(PacketBuffer buf) {
		buf.writeInt(this.boatId);
		buf.writeInt(this.balloonId);
	}
	
	public static S2CDetachCustomPositionBalloon deserialize(PacketBuffer buf) {
		return new S2CDetachCustomPositionBalloon(buf.readInt(), buf.readInt());
	}
	
	public static void handle(S2CDetachCustomPositionBalloon message, Supplier<NetworkEvent.Context> ctx) {
		Context context = ctx.get();
		if (context.getDirection().getReceptionSide() == LogicalSide.CLIENT) {
			context.enqueueWork(() -> {
				ClientWorld world = Minecraft.getInstance().world;
				Entity boat = world.getEntityByID(message.boatId);
				if (boat instanceof CustomBalloonPositioner) {
					Entity balloon = world.getEntityByID(message.balloonId);
					if (balloon instanceof BolloomBalloonEntity) {
						((CustomBalloonPositioner) boat).onBalloonDetachedClient((BolloomBalloonEntity) balloon);
					}
				}
			});
		}
		context.setPacketHandled(true);
	}
}