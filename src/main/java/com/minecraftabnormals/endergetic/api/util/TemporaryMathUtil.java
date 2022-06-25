package com.minecraftabnormals.endergetic.api.util;

import net.minecraft.util.RandomSource;

//TODO: Fix Blueprint's MathUtil
@Deprecated
public final class TemporaryMathUtil {

	public static double makeNegativeRandomly(double value, RandomSource rand) {
		return rand.nextBoolean() ? -value : value;
	}


	public static double makeNegativeRandomlyWithFavoritism(double value, RandomSource rand, float chance) {
		return rand.nextFloat() < chance ? -value : value;
	}

}
