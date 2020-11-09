package com.minecraftabnormals.endergetic.api.util;

import com.minecraftabnormals.endergetic.common.entities.booflo.BoofloEntity;
import com.minecraftabnormals.endergetic.common.network.entity.booflo.*;
import com.minecraftabnormals.endergetic.core.EndergeticExpansion;
import com.teamabnormals.abnormals_core.client.ClientInfo;
import net.minecraft.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * This class holds a list of useful network functions
 *
 * @author SmellyModder(Luke Tonon)
 */
public final class EndergeticNetworkUtil {
	/**
	 * Sends a message to the server to play the animation and inflate the booflo
	 *
	 * @param entityId Entity ID of the booflo
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
	 * Sends a message to the server to set the booflo to slam
	 *
	 * @param entityId Entity ID of the booflo
	 */
	@OnlyIn(Dist.CLIENT)
	public static void slamBooflo(int entityId) {
		Entity entity = ClientInfo.getClientPlayerWorld().getEntityByID(entityId);
		if (entity instanceof BoofloEntity) {
			EndergeticExpansion.CHANNEL.sendToServer(new SSlamMessage(entityId));
		}
	}

	/**
	 * Sends a message to the server to increment the booflo bar
	 *
	 * @param entityId Entity ID of the booflo
	 */
	@OnlyIn(Dist.CLIENT)
	public static void incrementBoofloBoostTimer(int entityId) {
		Entity entity = ClientInfo.getClientPlayerWorld().getEntityByID(entityId);
		if (entity instanceof BoofloEntity) {
			EndergeticExpansion.CHANNEL.sendToServer(new SIncrementBoostDelayMessage(entityId));
		}
	}

	/**
	 * Sends a message to the server to set the player not boosting
	 *
	 * @param entityId Entity ID of the booflo
	 */
	@OnlyIn(Dist.CLIENT)
	public static void setPlayerNotBoosting(int entityId) {
		Entity entity = ClientInfo.getClientPlayerWorld().getEntityByID(entityId);
		if (entity instanceof BoofloEntity) {
			EndergeticExpansion.CHANNEL.sendToServer(new SSetPlayerNotBoostingMessage(entityId));
		}
	}
}