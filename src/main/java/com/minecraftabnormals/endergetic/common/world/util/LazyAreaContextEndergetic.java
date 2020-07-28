package com.minecraftabnormals.endergetic.common.world.util;

import net.minecraft.world.gen.IExtendedNoiseRandom;
import net.minecraft.world.gen.LazyAreaLayerContext;
import net.minecraft.world.gen.area.LazyArea;

public class LazyAreaContextEndergetic extends LazyAreaLayerContext implements IExtendedNoiseRandom<LazyArea> {
	private long worldSeed;

	public LazyAreaContextEndergetic(int maxCacheSize, long seed, long seedModifier) {
		super(maxCacheSize, seed, seedModifier);
		this.worldSeed = seed;
	}

	public long getWorldSeed() {
		return worldSeed;
	}
}
