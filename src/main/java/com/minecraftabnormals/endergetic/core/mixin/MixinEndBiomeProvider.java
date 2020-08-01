package com.minecraftabnormals.endergetic.core.mixin;

import java.util.List;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.minecraftabnormals.endergetic.common.world.util.EndergeticLayerUtil;
import com.minecraftabnormals.endergetic.core.registry.EEBiomes;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.biome.provider.EndBiomeProvider;
import net.minecraft.world.gen.SimplexNoiseGenerator;
import net.minecraft.world.gen.layer.Layer;

@Mixin(EndBiomeProvider.class)
public abstract class MixinEndBiomeProvider extends BiomeProvider {
	private static Layer noiseBiomeLayer;
	
	@Shadow
	@Final
	private SimplexNoiseGenerator generator;
	
	@Shadow(remap = false)
	@Final
	private long field_235315_h_;
	
	protected MixinEndBiomeProvider(List<Biome> p_i231634_1_) {
		super(p_i231634_1_);
	}

	@Inject(at = @At("HEAD"), method = "getNoiseBiome(III)Lnet/minecraft/world/biome/Biome;", cancellable = true)
	private void addEndergeticBiomes(int x, int y, int z, CallbackInfoReturnable<Biome> info) {
		if (noiseBiomeLayer == null) {
			noiseBiomeLayer = EndergeticLayerUtil.createGenLayers(this.field_235315_h_)[1];
		}
		int i = x >> 2;
		int j = z >> 2;
		if ((long) i * (long) i + (long) j * (long) j <= 4096L) {
			info.setReturnValue(Biomes.THE_END);
		} else {
			float f = EndBiomeProvider.func_235317_a_(this.generator, i * 2 + 1, j * 2 + 1);
			Biome biome = noiseBiomeLayer.func_215738_a(x, z);
			boolean isChorus = biome == EEBiomes.CHORUS_PLAINS.get();
			if (f > 40.0F) {
				info.setReturnValue(isChorus ? Biomes.END_HIGHLANDS : biome);
			} else if (f >= 0.0F) {
				info.setReturnValue(isChorus ? Biomes.END_MIDLANDS : biome);
			} else {
				info.setReturnValue(f < -20.0F ? Biomes.SMALL_END_ISLANDS : isChorus ? Biomes.END_BARRENS : biome);
			}
		}
	}
}