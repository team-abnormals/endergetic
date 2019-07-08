package endergeticexpansion.common.network.player;

import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;

/*
 * @author SmellyModder(Luke Tonon)
 */
public class MessageCUpdatePlayerMotion {
	private int entityId;
	private double vecX;
	private double vecY;
	private double vecZ;

	public MessageCUpdatePlayerMotion(Vec3d motion, int entityId) {
		this.entityId = entityId;
		this.vecX = motion.getX();
		this.vecY = motion.getY();
		this.vecZ = motion.getZ();
	}
	
	public void serialize(PacketBuffer buf) {
		buf.writeInt(this.entityId);
		buf.writeDouble(this.vecX);
		buf.writeDouble(this.vecY);
		buf.writeDouble(this.vecZ);
	}
	
	public static MessageCUpdatePlayerMotion deserialize(PacketBuffer buf) {
		int entityId = buf.readInt();
		double vecX = buf.readDouble();
		double vecY = buf.readDouble();
		double vecZ = buf.readDouble();
		return new MessageCUpdatePlayerMotion(new Vec3d(vecX, vecY, vecZ), entityId);
	}
	
	public static void handle(MessageCUpdatePlayerMotion message, Supplier<NetworkEvent.Context> contextSupplier) {
		NetworkEvent.Context context = contextSupplier.get();
		Entity entity = Minecraft.getInstance().player.world.getEntityByID(message.entityId);
		if (context.getDirection().getReceptionSide() == LogicalSide.CLIENT) {
			context.enqueueWork(() -> {
				entity.setMotion(new Vec3d(message.vecX, message.vecY, message.vecZ));
			});
			context.setPacketHandled(true);
		}
	}
}
