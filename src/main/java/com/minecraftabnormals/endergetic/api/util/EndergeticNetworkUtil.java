package com.minecraftabnormals.endergetic.api.util;

import com.teamabnormals.abnormals_core.client.ClientInfo;
import com.minecraftabnormals.endergetic.common.entities.booflo.BoofloEntity;
import com.minecraftabnormals.endergetic.common.network.entity.*;
import com.minecraftabnormals.endergetic.common.network.entity.booflo.*;
import com.minecraftabnormals.endergetic.common.network.nbt.SUpdateNBTTagMessage;
import com.minecraftabnormals.endergetic.core.EndergeticExpansion;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * @author - SmellyModder(Luke Tonon)
 * This class holds a list of useful network functions
 */
public final class EndergeticNetworkUtil {
	/**
	 * @param stack - The stack that the message will update the nbt for
	 *              Used for updating the server nbt from the client. For example it's used in booflo vest keybinds
	 */
	@OnlyIn(Dist.CLIENT)
	public static void updateSItemNBT(ItemStack stack) {
		EndergeticExpansion.CHANNEL.sendToServer(new SUpdateNBTTagMessage(stack));
	}

	/**
	 * @param stack    - The stack that the message will set the cooldown for
	 * @param cooldown - The amount of time the cooldown will last; measured in ticks. 1 second = 20 ticks
	 * @param isVest   - A boolean that specifies that the cooldown is for the Booflo Vest item
	 *                 Used for updating the client nbt from the server
	 */
	@OnlyIn(Dist.CLIENT)
	public static void setSItemCooldown(ItemStack stack, int cooldown, boolean isVest) {
		EndergeticExpansion.CHANNEL.sendToServer(new SSetCooldownMessage(stack, cooldown, isVest));
	}

	/**
	 * @param velX,  velY, velZ - The velocity values for x, y, and z
	 * @param radius - The radius the blast will affect; measured in blocks
	 *               Used for pushing entities back through the client for the server
	 */
	@OnlyIn(Dist.CLIENT)
	public static void SBoofEntity(float xzForce, float upperForce, int radius) {
		EndergeticExpansion.CHANNEL.sendToServer(new SBoofEntityMessage(xzForce, upperForce, radius));
	}

	/**
	 * @param entityId - Entity ID of the booflo
	 *                 Sends a message to the server to play the animation and inflate the booflo
	 */
	@OnlyIn(Dist.CLIENT)
	public static void inflateBooflo(int entityId) {
		Entity entity = ClientInfo.getClientPlayerWorld().getEntityByID(entityId);
		if (entity instanceof BoofloEntity) {
			BoofloEntity booflo = (BoofloEntity) entity;
			EndergeticExpansion.CHANNEL.sendToServer(new SInflateMessage(entityId));
			booflo.setBoofed(true);
			booflo.setDelayDecrementing(false);
			booflo.setDelayExpanding(true);
		}
	}

	/**
	 * @param entityId - Entity ID of the booflo
	 *                 Sends a message to the server to set the booflo to slam
	 */
	@OnlyIn(Dist.CLIENT)
	public static void slamBooflo(int entityId) {
		Entity entity = ClientInfo.getClientPlayerWorld().getEntityByID(entityId);
		if (entity instanceof BoofloEntity) {
			EndergeticExpansion.CHANNEL.sendToServer(new SSlamMessage(entityId));
		}
	}

	/**
	 * @param entityId - Entity ID of the booflo
	 *                 Sends a message to the server to increment the booflo bar
	 */
	@OnlyIn(Dist.CLIENT)
	public static void incrementBoofloBoostTimer(int entityId) {
		Entity entity = ClientInfo.getClientPlayerWorld().getEntityByID(entityId);
		if (entity instanceof BoofloEntity) {
			EndergeticExpansion.CHANNEL.sendToServer(new SIncrementBoostDelayMessage(entityId));
		}
	}

	/**
	 * @param entityId - Entity ID of the booflo
	 *                 Sends a message to the server to set the player not boosting
	 */
	@OnlyIn(Dist.CLIENT)
	public static void setPlayerNotBoosting(int entityId) {
		Entity entity = ClientInfo.getClientPlayerWorld().getEntityByID(entityId);
		if (entity instanceof BoofloEntity) {
			EndergeticExpansion.CHANNEL.sendToServer(new SSetPlayerNotBoostingMessage(entityId));
		}
	}
}