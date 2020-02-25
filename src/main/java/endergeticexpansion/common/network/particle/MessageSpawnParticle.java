package endergeticexpansion.common.network.particle;

import java.util.function.Supplier;

import endergeticexpansion.api.EndergeticAPI.ClientInfo;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Message for telling the client to spawn particles
 * @author - SmellyModder(Luke Tonon)
 */
public class MessageSpawnParticle {
	public String particleName;
	public double posX, posY, posZ;
	public double motionX, motionY, motionZ;
	
	public MessageSpawnParticle(String particleName, double posX, double posY, double posZ, double motionX, double motionY, double motionZ) {
		this.particleName = particleName;
		this.posX = posX;
		this.posY = posY;
		this.posZ = posZ;
		this.motionX = motionX;
		this.motionY = motionY;
		this.motionZ = motionZ;
	}
	
	public void serialize(PacketBuffer buf) {
		buf.writeString(this.particleName);
		buf.writeDouble(this.posX);
		buf.writeDouble(this.posY);
		buf.writeDouble(this.posZ);
		buf.writeDouble(this.motionX);
		buf.writeDouble(this.motionY);
		buf.writeDouble(this.motionZ);
	}
	
	public static MessageSpawnParticle deserialize(PacketBuffer buf) {
		String particleName = buf.readString();
		double posX = buf.readDouble();
		double posY = buf.readDouble();
		double posZ = buf.readDouble();
		double motionX = buf.readDouble();
		double motionY = buf.readDouble();
		double motionZ = buf.readDouble();
		return new MessageSpawnParticle(particleName, posX, posY, posZ, motionX, motionY, motionZ);
	}
	
	public static boolean handle(MessageSpawnParticle message, Supplier<NetworkEvent.Context> ctx) {
		NetworkEvent.Context context = ctx.get();
		if(context.getDirection().getReceptionSide() == LogicalSide.CLIENT) {
			context.enqueueWork(() -> {
				World world = ClientInfo.getClientPlayerWorld();
				BasicParticleType particleType = (BasicParticleType) ForgeRegistries.PARTICLE_TYPES.getValue(new ResourceLocation(message.particleName));
				
				if(particleType != null) {
					world.addParticle(particleType, message.posX, message.posY, message.posZ, message.motionX, message.motionY, message.motionZ);
				}
			});
		}
		
		return true;
	}
}