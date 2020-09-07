package com.minecraftabnormals.endergetic.common.network.entity;

import java.util.function.Supplier;

import com.minecraftabnormals.endergetic.api.entity.util.EntityMotionHelper;
import com.minecraftabnormals.endergetic.core.registry.other.EETags;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkEvent.Context;

public class SBoofEntityMessage {
	private float xzForce;
	private float yForce;
	private int radius;
	
	public SBoofEntityMessage(float xzForce, float upperForce, int radius) {
		this.xzForce = xzForce;
		this.yForce = upperForce;
		this.radius = radius;
	}
	
	public void serialize(PacketBuffer buf) {
		buf.writeFloat(this.xzForce);
		buf.writeFloat(this.yForce);
		buf.writeInt(this.radius);
	}
	
	public static SBoofEntityMessage deserialize(PacketBuffer buf) {
		float xzForce = buf.readFloat();
		float yForce = buf.readFloat();
		int radius = buf.readInt();
		return new SBoofEntityMessage(xzForce, yForce, radius);
	}
	
	public static void handle(SBoofEntityMessage message, Supplier<NetworkEvent.Context> ctx) {
		Context context = ctx.get();
		if (context.getDirection().getReceptionSide() == LogicalSide.SERVER) {
			context.enqueueWork(() -> {
				PlayerEntity player = ctx.get().getSender();
				
				for (Entity entity : player.getEntityWorld().getEntitiesWithinAABB(Entity.class, player.getBoundingBox().grow(message.radius))) {
					if (entity != player && !EETags.EntityTypes.BOOF_BLOCK_RESISTANT.contains(entity.getType())) {
	    				boolean reverse = player.getRidingEntity() == entity;
	    				EntityMotionHelper.knockbackEntity(entity, (float) message.xzForce, (float) message.yForce, reverse, false);
	    			}
				}
			});
		}
		context.setPacketHandled(true);
	}
}