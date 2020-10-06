package com.minecraftabnormals.endergetic.common.network.entity.booflo;

import java.util.function.Supplier;

import com.teamabnormals.abnormals_core.core.utils.NetworkUtil;
import com.minecraftabnormals.endergetic.common.entities.booflo.BoofloEntity;

import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;

/**
 * Message that tells the server to inflate the booflo
 *
 * @author - SmellyModder(Luke Tonon)
 */
public class SInflateMessage {
	private int entityId;

	public SInflateMessage(int entityId) {
		this.entityId = entityId;
	}

	public void serialize(PacketBuffer buf) {
		buf.writeInt(this.entityId);
	}

	public static SInflateMessage deserialize(PacketBuffer buf) {
		return new SInflateMessage(buf.readInt());
	}

	public static void handle(SInflateMessage message, Supplier<NetworkEvent.Context> ctx) {
		NetworkEvent.Context context = ctx.get();
		if (context.getDirection().getReceptionSide() == LogicalSide.SERVER) {
			context.enqueueWork(() -> {
				Entity entity = context.getSender().world.getEntityByID(message.entityId);
				if (entity instanceof BoofloEntity) {
					BoofloEntity booflo = (BoofloEntity) entity;
					booflo.setBoofed(true);
					booflo.setDelayExpanding(true);
					booflo.setDelayDecrementing(false);
					NetworkUtil.setPlayingAnimationMessage(booflo, BoofloEntity.INFLATE);
				}
			});
			context.setPacketHandled(true);
		}
	}
}