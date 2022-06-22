package com.minecraftabnormals.endergetic.core.mixin;

import java.util.List;
import java.util.concurrent.Executor;

import com.minecraftabnormals.endergetic.common.entities.bolloom.BolloomBalloonEntity;
import com.minecraftabnormals.endergetic.core.interfaces.BalloonHolder;
import net.minecraft.entity.Entity;
import net.minecraft.world.server.ServerChunkProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.minecraftabnormals.endergetic.common.world.EndergeticDragonFightManager;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.RegistryKey;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.chunk.listener.IChunkStatusListener;
import net.minecraft.world.end.DragonFightManager;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.spawner.ISpecialSpawner;
import net.minecraft.world.storage.IServerWorldInfo;
import net.minecraft.world.storage.SaveFormat;

@Mixin(ServerWorld.class)
public final class ServerWorldMixin {
	@Shadow
	public DragonFightManager dragonFight;

	@Inject(at = @At("RETURN"), method = "<init>")
	private void replaceDragonFightManager(MinecraftServer server, Executor p_i232604_2_, SaveFormat.LevelSave p_i232604_3_, IServerWorldInfo p_i232604_4_, RegistryKey<World> p_i232604_5_, DimensionType p_i232604_7_, IChunkStatusListener p_i232604_8_, ChunkGenerator p_i232604_9_, boolean p_i232604_10_, long p_i232604_11_, List<ISpecialSpawner> p_i232604_13_, boolean p_i232604_14_, CallbackInfo info) {
		if (this.dragonFight != null) {
			this.dragonFight = new EndergeticDragonFightManager(this.dragonFight.level, server.getWorldData().worldGenSettings().seed(), server.getWorldData().endDragonFightData());
		}
	}

	@SuppressWarnings("deprecation")
	@Inject(at = @At(value = "FIELD", target = "Lnet/minecraft/entity/Entity;inChunk:Z", ordinal = 1, shift = At.Shift.AFTER), method = "tickNonPassenger")
	private void updateBalloons(Entity entity, CallbackInfo info) {
		BalloonHolder balloonHolder = (BalloonHolder) entity;
		ServerChunkProvider chunkProvider = ((ServerWorld) (Object) this).getChunkSource();
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
					((ServerWorld) (Object) this).updateChunkPos(balloon);
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
		ServerChunkProvider chunkProvider = ((ServerWorld) (Object) this).getChunkSource();
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
					((ServerWorld) (Object) this).updateChunkPos(balloon);
				}
			} else {
				balloon.detachFromEntity();
			}
		}
	}
}