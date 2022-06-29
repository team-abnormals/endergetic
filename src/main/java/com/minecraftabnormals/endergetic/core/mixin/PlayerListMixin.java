package com.minecraftabnormals.endergetic.core.mixin;

import com.minecraftabnormals.endergetic.common.entities.bolloom.BolloomBalloonEntity;
import com.minecraftabnormals.endergetic.common.network.entity.S2CUpdateBalloonsMessage;
import com.minecraftabnormals.endergetic.core.EndergeticExpansion;
import com.minecraftabnormals.endergetic.core.interfaces.BalloonHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.Connection;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.storage.PlayerDataStorage;
import net.minecraftforge.network.PacketDistributor;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;
import java.util.List;

@Mixin(PlayerList.class)
public final class PlayerListMixin {
	@Shadow
	@Final
	private PlayerDataStorage playerIo;
	@Shadow
	@Final
	private MinecraftServer server;

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/server/players/PlayerList;save(Lnet/minecraft/server/level/ServerPlayer;)V", shift = At.Shift.AFTER), method = "remove")
	private void removeBalloons(ServerPlayer player, CallbackInfo info) {
		List<BolloomBalloonEntity> balloons = ((BalloonHolder) player).getBalloons();
		for (BolloomBalloonEntity balloon : balloons) {
			balloon.setRemoved(Entity.RemovalReason.UNLOADED_WITH_PLAYER);
		}
	}

	@Inject(at = @At("RETURN"), method = "placeNewPlayer")
	private void spawnBalloons(Connection connection, ServerPlayer player, CallbackInfo info) {
		ServerLevel serverWorld = (ServerLevel) player.level;

		CompoundTag compound = this.server.getWorldData().getLoadedPlayerTag();
		if (!(compound != null && this.server.isSingleplayerOwner(player.getGameProfile()))) {
			try {
				File playerDataFile = new File(this.playerIo.getPlayerDataFolder(), player.getStringUUID() + ".dat");
				if (playerDataFile.exists() && playerDataFile.isFile()) {
					compound = NbtIo.readCompressed(playerDataFile);
				}
			} catch (Exception exception) {
				EndergeticExpansion.LOGGER.warn("Failed to load player data for {}", player.getName().getString());
			}
		}

		if (compound != null && compound.contains("Balloons", 9)) {
			ListTag balloonsTag = compound.getList("Balloons", 10);
			if (!balloonsTag.isEmpty()) {
				for (int i = 0; i < balloonsTag.size(); i++) {
					Entity entity = EntityType.loadEntityRecursive(balloonsTag.getCompound(i), serverWorld, (balloon -> !serverWorld.addWithUUID(balloon) ? null : balloon));
					if (entity instanceof BolloomBalloonEntity) {
						((BolloomBalloonEntity) entity).attachToEntity(player);
						EndergeticExpansion.CHANNEL.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), new S2CUpdateBalloonsMessage(player));
					}
				}
			}
		}
	}
}
