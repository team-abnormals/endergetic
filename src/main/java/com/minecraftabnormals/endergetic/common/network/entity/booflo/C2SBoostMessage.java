package com.minecraftabnormals.endergetic.common.network.entity.booflo;

import com.minecraftabnormals.abnormals_core.core.util.NetworkUtil;
import com.minecraftabnormals.endergetic.common.entities.booflo.BoofloEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Message that requests the server to use up the boost power of the Booflo the sending player is riding.
 *
 * @author SmellyModder (Luke Tonon)
 */
public final class C2SBoostMessage {

	public final void serialize(FriendlyByteBuf buf) {
	}

	public static C2SBoostMessage deserialize(FriendlyByteBuf buf) {
		return new C2SBoostMessage();
	}

	public static void handle(C2SBoostMessage message, Supplier<NetworkEvent.Context> ctx) {
		NetworkEvent.Context context = ctx.get();
		if (context.getDirection().getReceptionSide() == LogicalSide.SERVER) {
			context.enqueueWork(() -> {
				Player player = context.getSender();
				if (player != null) {
					Entity entity = player.getVehicle();
					if (entity instanceof BoofloEntity) {
						BoofloEntity booflo = (BoofloEntity) entity;
						if (booflo.isBoostExpanding() && !booflo.isBoostLocked() && !booflo.isOnGround() && booflo.isBoofed() && booflo.getBoostPower() > 0) {
							NetworkUtil.setPlayingAnimationMessage(booflo, BoofloEntity.INFLATE);
							booflo.playSound(booflo.getInflateSound(), 0.75F, 1.0F);
							booflo.setBoostExpanding(false);
						}
					}
				}
			});
			context.setPacketHandled(true);
		}
	}

}
