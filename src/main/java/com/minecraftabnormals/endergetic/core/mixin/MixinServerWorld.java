package com.minecraftabnormals.endergetic.core.mixin;

import java.util.List;
import java.util.concurrent.Executor;

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
public class MixinServerWorld {

	@Shadow(remap = false)
	public DragonFightManager field_241105_O_;
	
	@Inject(at = @At("RETURN"), method = "<init>")
	private void replaceDragonFightManager(MinecraftServer server, Executor p_i232604_2_, SaveFormat.LevelSave p_i232604_3_, IServerWorldInfo p_i232604_4_, RegistryKey<World> p_i232604_5_, RegistryKey<DimensionType> p_i232604_6_, DimensionType p_i232604_7_, IChunkStatusListener p_i232604_8_, ChunkGenerator p_i232604_9_, boolean p_i232604_10_, long p_i232604_11_, List<ISpecialSpawner> p_i232604_13_, boolean p_i232604_14_, CallbackInfo info) {
		if (this.field_241105_O_ != null) {
			this.field_241105_O_ = new EndergeticDragonFightManager(this.field_241105_O_.world, server.func_240793_aU_().func_230418_z_().func_236221_b_(), server.func_240793_aU_().func_230402_B_());
		}
	}

}