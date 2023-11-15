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
 * Message that requests the server to slam the Booflo the sending player is riding.
 *
 * @author SmellyModder (Luke Tonon)
 */
public final class C2SSlamMessage {

	public void serialize(FriendlyByteBuf buf) {
	}

	public static C2SSlamMessage deserialize(FriendlyByteBuf buf) {
		return new C2SSlamMessage();
	}

	public static void handle(C2SSlamMessage message, Supplier<NetworkEvent.Context> ctx) {
		NetworkEvent.Context context = ctx.get();
		if (context.getDirection().getReceptionSide() == LogicalSide.SERVER) {
			context.enqueueWork(() -> {
				Player player = context.getSender();
				if (player != null) {
					Entity ridingEntity = player.getVehicle();
					if (ridingEntity instanceof Booflo) {
						Booflo booflo = (Booflo) ridingEntity;
						if (booflo.isBoofed() && booflo.getBoostPower() <= 0 && booflo.isNoEndimationPlaying()) {
							NetworkUtil.setPlayingAnimation(booflo, EEPlayableEndimations.BOOFLO_CHARGE);
							booflo.setBoostExpanding(true);
							booflo.setBoostLocked(true);
						}
					}
				}
			});
			context.setPacketHandled(true);
		}
	}

}