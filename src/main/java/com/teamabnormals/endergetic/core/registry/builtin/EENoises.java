package com.teamabnormals.endergetic.core.registry.builtin;

import com.teamabnormals.endergetic.core.EndergeticExpansion;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

public final class EENoises {
	public static final ResourceKey<NormalNoise.NoiseParameters> CORROCK = createKey("corrock");
	public static final ResourceKey<NormalNoise.NoiseParameters> CORROCK_TENDRILS = createKey("corrock_tendrils");

	public static void bootstrap(BootstapContext<NormalNoise.NoiseParameters> context) {
		context.register(CORROCK, new NormalNoise.NoiseParameters(-8, 2.0D, 1.0D, 1.0D, 1.0D, 4.0D));
		context.register(CORROCK_TENDRILS, new NormalNoise.NoiseParameters(-4, 1.0D, 1.0D, 0.0D, 1.0D));
	}

	public static ResourceKey<NormalNoise.NoiseParameters> createKey(String name) {
		return ResourceKey.create(Registries.NOISE, new ResourceLocation(EndergeticExpansion.MOD_ID, name));
	}
}
