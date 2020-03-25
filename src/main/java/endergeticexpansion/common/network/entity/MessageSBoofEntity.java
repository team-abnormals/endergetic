package endergeticexpansion.common.network.entity;

import java.util.function.Supplier;

import endergeticexpansion.api.entity.util.EntityMotionHelper;
import endergeticexpansion.common.entities.EntityBoofBlock;
import endergeticexpansion.common.entities.EntityPoiseCluster;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemFrameEntity;
import net.minecraft.entity.item.PaintingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkEvent.Context;

public class MessageSBoofEntity {
	private float xzForce;
	private float yForce;
	private int radius;
	
	public MessageSBoofEntity(float xzForce, float upperForce, int radius) {
		this.xzForce = xzForce;
		this.yForce = upperForce;
		this.radius = radius;
	}
	
	public void serialize(PacketBuffer buf) {
		buf.writeFloat(this.xzForce);
		buf.writeFloat(this.yForce);
		buf.writeInt(this.radius);
	}
	
	public static MessageSBoofEntity deserialize(PacketBuffer buf) {
		float xzForce = buf.readFloat();
		float yForce = buf.readFloat();
		int radius = buf.readInt();
		return new MessageSBoofEntity(xzForce, yForce, radius);
	}
	
	public static void handle(MessageSBoofEntity message, Supplier<NetworkEvent.Context> ctx) {
		Context context = ctx.get();
		if(context.getDirection().getReceptionSide() == LogicalSide.SERVER) {
			context.enqueueWork(() -> {
				PlayerEntity player = ctx.get().getSender();
				
				for(Entity entity : player.getEntityWorld().getEntitiesWithinAABB(Entity.class, player.getBoundingBox().grow(message.radius))) {
					if(entity != player &&
	        			!(entity instanceof EntityBoofBlock) &&
	        			!(entity instanceof PaintingEntity) &&
	        			!(entity instanceof EntityPoiseCluster) &&
	        			!(entity instanceof ItemFrameEntity)
	        		) {
	    				boolean reverse = player.getRidingEntity() == entity;
	    				EntityMotionHelper.knockbackEntity(entity, (float) message.xzForce, (float) message.yForce, reverse, false);
	    			}
				}
			});
		}
		context.setPacketHandled(true);
	}
}