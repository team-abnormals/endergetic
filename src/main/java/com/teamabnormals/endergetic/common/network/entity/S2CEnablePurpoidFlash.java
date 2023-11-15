package com.teamabnormals.endergetic.common.network.entity;

import com.teamabnormals.endergetic.client.events.OverlayEvents;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public final class S2CEnablePurpoidFlash {

	public void serialize(FriendlyByteBuf buf) {
	}

	public static S2CEnablePurpoidFlash deserialize(FriendlyByteBuf buf) {
		return new S2CEnablePurpoidFlash();
	}

	public static void handle(S2CEnablePurpoidFlash message, Supplier<NetworkEvent.Context> ctx) {
		NetworkEvent.Context context = ctx.get();
		if (context.getDirection().getReceptionSide() == LogicalSide.CLIENT) {
			context.enqueueWork(OverlayEvents::enablePurpoidFlash);
		}
		context.setPacketHandled(true);
	}

}
