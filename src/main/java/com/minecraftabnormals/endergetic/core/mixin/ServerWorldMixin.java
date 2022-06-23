package com.minecraftabnormals.endergetic.core.mixin;

import java.util.List;
import java.util.concurrent.Executor;

import com.minecraftabnormals.endergetic.common.entities.bolloom.BolloomBalloonEntity;
import com.minecraftabnormals.endergetic.core.interfaces.BalloonHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerChunkCache;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.minecraftabnormals.endergetic.common.world.EndergeticDragonFightManager;

import net.minecraft.server.MinecraftServer;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.progress.ChunkProgressListener;
import net.minecraft.world.level.dimension.end.EndDragonFight;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.CustomSpawner;
import net.minecraft.world.level.storage.ServerLevelData;
import net.minecraft.world.level.storage.LevelStorageSource;

@Mixin(ServerLevel.class)
public final class ServerWorldMixin {
	@Shadow
	public EndDragonFight dragonFight;

	@Inject(at = @At("RETURN"), method = "<init>")
	private void replaceDragonFightManager(MinecraftServer server, Executor p_i232604_2_, LevelStorageSource.LevelStorageAccess p_i232604_3_, ServerLevelData p_i232604_4_, ResourceKey<Level> p_i232604_5_, DimensionType p_i232604_7_, ChunkProgressListener p_i232604_8_, ChunkGenerator p_i232604_9_, boolean p_i232604_10_, long p_i232604_11_, List<CustomSpawner> p_i232604_13_, boolean p_i232604_14_, CallbackInfo info) {
		if (this.dragonFight != null) {
			this.dragonFight = new EndergeticDragonFightManager(this.dragonFight.level, server.getWorldData().worldGenSettings().seed(), server.getWorldData().endDragonFightData());
		}
	}

	@SuppressWarnings("deprecation")
	@Inject(at = @At(value = "FIELD", target = "Lnet/minecraft/entity/Entity;inChunk:Z", ordinal = 1, shift = At.Shift.AFTER), method = "tickNonPassenger")
	private void updateBalloons(Entity entity, CallbackInfo info) {
		BalloonHolder balloonHolder = (BalloonHolder) entity;
		ServerChunkCache chunkProvider = ((ServerLevel) (Object) this).getChunkSource();
		for (BolloomBalloonEntity balloon : balloonHolder.getBalloons()) {
			if (!balloon.removed && balloon.getAttachedEntity() == entity) {
				if (chunkProvider.isEntityTickingChunk(balloon)) {
					balloon.setPosAndOldPos(balloon.getX(), balloon.getY(), balloon.getZ());
					balloon.yRotO = balloon.yRot;
					balloon.xRotO = balloon.xRot;
					if (balloon.inChunk) {
						balloon.tickCount++;
						balloon.updateAttachedPosition();
					}
					((ServerLevel) (Object) this).updateChunkPos(balloon);
				}
			} else {
				balloon.detachFromEntity();
			}
		}
	}

	@SuppressWarnings("deprecation")
	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;rideTick()V", shift = At.Shift.AFTER), method = "tickPassenger")
	private void updateEntityRiddenBalloons(Entity ridingEntity, Entity passenger, CallbackInfo info) {
		BalloonHolder balloonHolder = (BalloonHolder) passenger;
		ServerChunkCache chunkProvider = ((ServerLevel) (Object) this).getChunkSource();
		for (BolloomBalloonEntity balloon : balloonHolder.getBalloons()) {
			if (!balloon.removed && balloon.getAttachedEntity() == passenger) {
				if (chunkProvider.isEntityTickingChunk(balloon)) {
					balloon.setPosAndOldPos(balloon.getX(), balloon.getY(), balloon.getZ());
					balloon.yRotO = balloon.yRot;
					balloon.xRotO = balloon.xRot;
					if (balloon.inChunk) {
						balloon.tickCount++;
						balloon.updateAttachedPosition();
					}
					((ServerLevel) (Object) this).updateChunkPos(balloon);
				}
			} else {
				balloon.detachFromEntity();
			}
		}
	}
}