package com.minecraftabnormals.endergetic.core.mixin;

import com.minecraftabnormals.endergetic.common.entities.bolloom.BolloomBalloonEntity;
import com.minecraftabnormals.endergetic.common.network.entity.S2CUpdateBalloons;
import com.minecraftabnormals.endergetic.core.EndergeticExpansion;
import com.minecraftabnormals.endergetic.core.interfaces.BalloonHolder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.server.management.PlayerList;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.PacketDistributor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(PlayerList.class)
public final class MixinPlayerList {

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
		CompoundNBT compound = ((PlayerList) (Object) this).readPlayerDataFromFile(player);
		if (compound != null && compound.contains("Balloons", 9)) {
			ListNBT balloonsTag = compound.getList("Balloons", 10);
			if (!balloonsTag.isEmpty()) {
				for (int i = 0; i < balloonsTag.size(); i++) {
					Entity entity = EntityType.func_220335_a(balloonsTag.getCompound(i), serverWorld, (balloon -> !serverWorld.summonEntity(balloon) ? null : balloon));
					if (entity instanceof BolloomBalloonEntity) {
						((BolloomBalloonEntity) entity).attachToEntity(player);
						EndergeticExpansion.CHANNEL.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), new S2CUpdateBalloons(player));
					}
				}
			}
		}
	}

}