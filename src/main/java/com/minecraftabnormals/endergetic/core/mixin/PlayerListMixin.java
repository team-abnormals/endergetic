package com.minecraftabnormals.endergetic.core.mixin;

import com.minecraftabnormals.endergetic.common.entities.bolloom.BolloomBalloonEntity;
import com.minecraftabnormals.endergetic.common.network.entity.S2CUpdateBalloonsMessage;
import com.minecraftabnormals.endergetic.core.EndergeticExpansion;
import com.minecraftabnormals.endergetic.core.interfaces.BalloonHolder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.PlayerData;
import net.minecraftforge.fml.network.PacketDistributor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;
import java.util.List;

@Mixin(PlayerList.class)
public final class PlayerListMixin {
	private PlayerData playerDataManager;
	private MinecraftServer server;

	@Inject(at = @At("RETURN"), method = "<init>")
	private void initialize(MinecraftServer p_i231425_1_, DynamicRegistries.Impl p_i231425_2_, PlayerData p_i231425_3_, int p_i231425_4_, CallbackInfo info) {
		playerDataManager = p_i231425_3_;
		server = p_i231425_1_;
	}

	@SuppressWarnings("deprecation")
	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/server/management/PlayerList;writePlayerData(Lnet/minecraft/entity/player/ServerPlayerEntity;)V", shift = At.Shift.AFTER), method = "playerLoggedOut")
	private void removeBalloons(ServerPlayerEntity player, CallbackInfo info) {
		List<BolloomBalloonEntity> balloons = ((BalloonHolder) player).getBalloons();
		if (!balloons.isEmpty()) {
			ServerWorld serverWorld = (ServerWorld) player.world;
			for (BolloomBalloonEntity balloon : balloons) {
				serverWorld.removeEntity(balloon);
				balloon.removed = true;
			}
			serverWorld.getChunk(player.chunkCoordX, player.chunkCoordZ).markDirty();
		}
	}

	@Inject(at = @At("RETURN"), method = "initializeConnectionToPlayer")
	private void spawnBalloons(NetworkManager netManager, ServerPlayerEntity player, CallbackInfo info) {
		ServerWorld serverWorld = (ServerWorld) player.world;

		CompoundNBT compound = this.server.getServerConfiguration().getHostPlayerNBT();
		if (!(compound != null && player.getName().getString().equals(this.server.getServerOwner()))) {
			try {
				File file1 = new File(this.playerDataManager.getPlayerDataFolder(), player.getCachedUniqueIdString() + ".dat");
				if (file1.exists() && file1.isFile()) {
					compound = CompressedStreamTools.readCompressed(file1);
				}
			} catch (Exception var4) {
				EndergeticExpansion.LOGGER.warn("Failed to load player data for {}", player.getName().getString());
			}
		}

		if (compound != null && compound.contains("Balloons", 9)) {
			ListNBT balloonsTag = compound.getList("Balloons", 10);
			if (!balloonsTag.isEmpty()) {
				for (int i = 0; i < balloonsTag.size(); i++) {
					Entity entity = EntityType.loadEntityAndExecute(balloonsTag.getCompound(i), serverWorld, (balloon -> !serverWorld.summonEntity(balloon) ? null : balloon));
					if (entity instanceof BolloomBalloonEntity) {
						((BolloomBalloonEntity) entity).attachToEntity(player);
						EndergeticExpansion.CHANNEL.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), new S2CUpdateBalloonsMessage(player));
					}
				}
			}
		}
	}

}