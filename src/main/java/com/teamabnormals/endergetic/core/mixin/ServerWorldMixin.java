package com.teamabnormals.endergetic.core.mixin;

import java.util.List;
import java.util.concurrent.Executor;

import com.teamabnormals.endergetic.common.entities.bolloom.BolloomBalloonEntity;
import com.teamabnormals.endergetic.core.interfaces.BalloonHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.entity.EntityTickList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.teamabnormals.endergetic.common.world.EndergeticDragonFightManager;

import net.minecraft.server.MinecraftServer;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.progress.ChunkProgressListener;
import net.minecraft.world.level.dimension.end.EndDragonFight;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.CustomSpawner;
import net.minecraft.world.level.storage.ServerLevelData;
import net.minecraft.world.level.storage.LevelStorageSource;

@Mixin(ServerLevel.class)
public final class ServerWorldMixin {
	@Shadow
	@Final
	EntityTickList entityTickList;

	@Shadow
	public EndDragonFight dragonFight;

	@Inject(at = @At("RETURN"), method = "<init>")
	private void replaceDragonFightManager(MinecraftServer server, Executor p_i232604_2_, LevelStorageSource.LevelStorageAccess p_i232604_3_, ServerLevelData p_i232604_4_, ResourceKey<Level> p_i232604_5_, LevelStem p_i232604_7_, ChunkProgressListener p_i232604_8_, boolean p_215006_, long p_215007_, List<CustomSpawner> p_215008_, boolean p_215009_, CallbackInfo info) {
		if (this.dragonFight != null) {
			this.dragonFight = new EndergeticDragonFightManager(this.dragonFight.level, server.getWorldData().worldGenSettings().seed(), server.getWorldData().endDragonFightData());
		}
	}

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiling/ProfilerFiller;pop()V", ordinal = 0, shift = At.Shift.AFTER), method = "tickNonPassenger")
	private void updateBalloons(Entity entity, CallbackInfo info) {
		BalloonHolder balloonHolder = (BalloonHolder) entity;
		for (BolloomBalloonEntity balloon : balloonHolder.getBalloons()) {
			if (!balloon.isRemoved() && balloon.getAttachedEntity() == entity) {
				if (this.entityTickList.contains(balloon)) {
					balloon.setOldPosAndRot();
					balloon.tickCount++;
					balloon.updateAttachedPosition();
				}
			} else {
				balloon.detachFromEntity();
			}
		}
	}

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiling/ProfilerFiller;pop()V", shift = At.Shift.AFTER), method = "tickPassenger")
	private void updatePassengerBalloons(Entity ridingEntity, Entity passenger, CallbackInfo info) {
		BalloonHolder balloonHolder = (BalloonHolder) passenger;
		for (BolloomBalloonEntity balloon : balloonHolder.getBalloons()) {
			if (!balloon.isRemoved() && balloon.getAttachedEntity() == passenger) {
				if (this.entityTickList.contains(balloon)) {
					balloon.setOldPosAndRot();
					balloon.tickCount++;
					balloon.updateAttachedPosition();
				}
			} else {
				balloon.detachFromEntity();
			}
		}
	}
}