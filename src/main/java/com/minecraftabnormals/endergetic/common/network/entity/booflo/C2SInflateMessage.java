package com.minecraftabnormals.endergetic.common.network.entity.booflo;

import com.minecraftabnormals.endergetic.common.entities.booflo.BoofloEntity;
import com.teamabnormals.abnormals_core.core.utils.NetworkUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Message that requests the server to begin charging boost power for the Booflo the sending player is riding.
 *
 * @author SmellyModder (Luke Tonon)
 */
public final class C2SInflateMessage {

	public final void serialize(PacketBuffer buf) {
	}

	public static C2SInflateMessage deserialize(PacketBuffer buf) {
		return new C2SInflateMessage();
	}

	public static void handle(C2SInflateMessage message, Supplier<NetworkEvent.Context> ctx) {
		NetworkEvent.Context context = ctx.get();
		if (context.getDirection().getReceptionSide() == LogicalSide.SERVER) {
			context.enqueueWork(() -> {
				PlayerEntity player = context.getSender();
				if (player != null) {
					Entity entity = player.getRidingEntity();
					if (entity instanceof BoofloEntity) {
						BoofloEntity booflo = (BoofloEntity) entity;
						if (!booflo.isOnGround() && !booflo.isBoostLocked() && booflo.getBoostPower() <= 0) {
							if (!booflo.isBoofed()) {
								booflo.setBoostLocked(true);
								booflo.setBoofed(true);
								NetworkUtil.setPlayingAnimationMessage(booflo, BoofloEntity.INFLATE);
							}
							booflo.setBoostExpanding(true);
						}
					}
				}
			});
			context.setPacketHandled(true);
		}
	}
}