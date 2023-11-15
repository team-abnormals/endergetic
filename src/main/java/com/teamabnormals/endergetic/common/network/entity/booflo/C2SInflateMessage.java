package com.teamabnormals.endergetic.common.network.entity.booflo;

import com.teamabnormals.blueprint.core.util.NetworkUtil;
import com.teamabnormals.endergetic.common.entity.booflo.Booflo;
import com.teamabnormals.endergetic.core.registry.other.EEPlayableEndimations;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Message that requests the server to begin charging boost power for the Booflo the sending player is riding.
 *
 * @author SmellyModder (Luke Tonon)
 */
public final class C2SInflateMessage {

	public void serialize(FriendlyByteBuf buf) {
	}

	public static C2SInflateMessage deserialize(FriendlyByteBuf buf) {
		return new C2SInflateMessage();
	}

	public static void handle(C2SInflateMessage message, Supplier<NetworkEvent.Context> ctx) {
		NetworkEvent.Context context = ctx.get();
		if (context.getDirection().getReceptionSide() == LogicalSide.SERVER) {
			context.enqueueWork(() -> {
				Player player = context.getSender();
				if (player != null) {
					Entity entity = player.getVehicle();
					if (entity instanceof Booflo) {
						Booflo booflo = (Booflo) entity;
						if (!booflo.isOnGround() && !booflo.isBoostLocked() && booflo.getBoostPower() <= 0) {
							if (!booflo.isBoofed()) {
								booflo.setBoostLocked(true);
								booflo.setBoofed(true);
								NetworkUtil.setPlayingAnimation(booflo, EEPlayableEndimations.BOOFLO_INFLATE);
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