package com.teamabnormals.endergetic.core.registry.other.tags;

import com.teamabnormals.blueprint.core.util.TagUtil;
import com.teamabnormals.endergetic.core.EndergeticExpansion;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

public class EEBiomeTags {
	public static final TagKey<Biome> HAS_EETLE_NEST = biomeTag("has_structure/eetle_nest");

	private static TagKey<Biome> biomeTag(String name) {
		return TagUtil.biomeTag(EndergeticExpansion.MOD_ID, name);
	}
}
