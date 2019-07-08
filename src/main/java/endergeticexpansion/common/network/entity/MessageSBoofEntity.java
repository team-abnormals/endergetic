package endergeticexpansion.common.network.entity;

import java.util.List;
import java.util.function.Supplier;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;

public class MessageSBoofEntity {
	private double velX;
	private double velY;
	private double velZ;
	private int radius;
	
	public MessageSBoofEntity(double velX, double velY, double velZ, int radius) {
		this.velX = velX;
		this.velY = velY;
		this.velZ = velZ;
		this.radius = radius;
	}
	
	public void serialize(PacketBuffer buf) {
		buf.writeDouble(this.velX);
		buf.writeDouble(this.velY);
		buf.writeDouble(this.velZ);
		buf.writeInt(this.radius);
	}
	
	public static MessageSBoofEntity deserialize(PacketBuffer buf) {
		double velX = buf.readDouble();
		double velY = buf.readDouble();
		double velZ = buf.readDouble();
		int radius = buf.readInt();
		return new MessageSBoofEntity(velX, velY, velZ, radius);
	}
	
	public static void handle(MessageSBoofEntity message, Supplier<NetworkEvent.Context> ctx) {
		if (ctx.get().getDirection().getReceptionSide() == LogicalSide.SERVER) {
			ctx.get().enqueueWork(() -> {
				PlayerEntity player = ctx.get().getSender();
				
				AxisAlignedBB bb = player.getBoundingBox().grow(message.radius);
    			List<Entity> entities = player.getEntityWorld().getEntitiesWithinAABB(Entity.class, bb);
    			
    			for(int i = 0; i < entities.size(); i++) {
    				Entity entity = entities.get(i);
    				
    				if(entity.getEntityId() != player.getEntityId()) {
    					entity.addVelocity(MathHelper.sin((float) (entity.rotationYaw * Math.PI / 180)) * message.velX * 0.1D, message.velY, -MathHelper.cos((float) (entity.rotationYaw * Math.PI / 180)) * message.velZ * 0.1D);
    				}
    			}
			});
		}
		
		ctx.get().setPacketHandled(true);
	}
}
