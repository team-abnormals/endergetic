package endergeticexpansion.api.util;

import org.apache.commons.lang3.ArrayUtils;

import endergeticexpansion.api.endimator.Endimation;
import endergeticexpansion.api.endimator.EndimatedEntity;
import endergeticexpansion.common.network.entity.MessageCAnimation;
import endergeticexpansion.common.network.entity.MessageCSetVelocity;
import endergeticexpansion.common.network.entity.MessageSBoofEntity;
import endergeticexpansion.common.network.entity.MessageSSetCooldown;
import endergeticexpansion.common.network.entity.MessageSSetFallDistance;
import endergeticexpansion.common.network.entity.MessageSSetVelocity;
import endergeticexpansion.common.network.item.MessageDamageItem;
import endergeticexpansion.common.network.nbt.MessageCUpdateNBTTag;
import endergeticexpansion.common.network.nbt.MessageSUpdateNBTTag;
import endergeticexpansion.common.network.particle.MessageSpawnParticle;
import endergeticexpansion.core.EndergeticExpansion;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.PacketDistributor;

/**
 * @author - SmellyModder(Luke Tonon)
 * This class holds a big list of useful network functions. Most are used in the mod
 */
public class NetworkUtil {
	/**
	 * @param stack - The stack that the message will update the nbt for
	 * Used for updating the server nbt from the client. For example it's used in booflo vest keybinds
	 */
	@OnlyIn(Dist.CLIENT)
	public static void updateSItemNBT(ItemStack stack) {
		EndergeticExpansion.CHANNEL.sendToServer(new MessageSUpdateNBTTag(stack));
	}
	
	/**
	 * @param stack - the stack that the message will update the nbt for
	 * Used for updating the client nbt from the server
	 */
	public static void updateCItemNBT(ItemStack stack) {
		EndergeticExpansion.CHANNEL.send(PacketDistributor.SERVER.noArg(), new MessageCUpdateNBTTag(stack));
	}
	
	/**
	 * @param stack - The stack that the message will set the cooldown for
	 * @param cooldown - The amount of time the cooldown will last; measured in ticks. 1 second = 20 ticks
	 * @param isVest - A boolean that specifies that the cooldown is for the Booflo Vest item
	 * Used for updating the client nbt from the server
	 */
	@OnlyIn(Dist.CLIENT)
	public static void setSItemCooldown(ItemStack stack, int cooldown, boolean isVest) {
		EndergeticExpansion.CHANNEL.sendToServer(new MessageSSetCooldown(stack, cooldown, isVest));
	}
	
	/**
	 * @param motion - The vector motion of the entity
	 * @param id - The Player's Entity Id
	 * Used for setting the client side Player Velocity from the server side
	 */
	@OnlyIn(Dist.DEDICATED_SERVER)
	public static void setCVelocity(Vec3d motion, int id) {
		EndergeticExpansion.CHANNEL.sendToServer(new MessageCSetVelocity(motion, id));
	}
	
	/**
	 * @param motion - The vector motion of the entity
	 * @param id - The Player's Entity Id
	 * Used for setting server side Entity Velocity from the client side
	 */
	@OnlyIn(Dist.CLIENT)
	public static void setSVelocity(Vec3d motion, int id) {
		EndergeticExpansion.CHANNEL.sendToServer(new MessageSSetVelocity(motion, id));
	}
	
	/**
	 * @param velX, velY, velZ - The velocity values for x, y, and z
	 * @param radius - The radius the blast will affect; measured in blocks
	 * Used for pushing entities back through the client for the server
	 */
	@OnlyIn(Dist.CLIENT)
	public static void SBoofEntity(double velX, double velY, double velZ, int radius) {
		EndergeticExpansion.CHANNEL.sendToServer(new MessageSBoofEntity(velX, velY, velZ, radius));
	}
	
	/**
	 * @param entityId - The entity's id
	 * @param distance - The height of the fall
	 * Used for setting fall distance in booflo vests
	 */
	@OnlyIn(Dist.CLIENT)
	public static void setSFallDistance(int entityId, int distance) {
		EndergeticExpansion.CHANNEL.sendToServer(new MessageSSetFallDistance(entityId, distance));
	}
	
	/**
	 * @param stack - The stack do damage
	 * @param amount - The amount to damage
	 * Used for damaging items from the client side
	 */
	@OnlyIn(Dist.CLIENT)
	public static void damageItem(ItemStack stack, int amount) {
		EndergeticExpansion.CHANNEL.sendToServer(new MessageDamageItem(stack, amount));
	}
	
	/**
	 * @param name - The registry name of the particle
	 * All other parameters work same as world#addParticle
	 * Used for adding particles to the world from the server side
	 */
	public static void spawnParticle(String name, double posX, double posY, double posZ, double motionX, double motionY, double motionZ) {
		EndergeticExpansion.CHANNEL.send(PacketDistributor.ALL.with(() -> null), new MessageSpawnParticle(name, posX, posY, posZ, motionX, motionY, motionZ));
	}
	
	/**
	 * Sends an animation message to the clients to update an entity's animations
	 * @param entity - The Entity to send the packet for
	 * @param animationToPlay - The animation to play
	 */
	public static void setPlayingAnimationMessage(EndimatedEntity entity, Endimation animationToPlay) {
		if(!entity.isWorldRemote()) {
			EndergeticExpansion.CHANNEL.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), new MessageCAnimation(entity.getEntityId(), ArrayUtils.indexOf(entity.getAnimations(), animationToPlay)));
			entity.setPlayingAnimation(animationToPlay);
		}
	}
}