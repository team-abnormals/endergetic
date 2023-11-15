package com.teamabnormals.endergetic.core.registry.other.tags;

import com.teamabnormals.blueprint.core.util.TagUtil;
import com.teamabnormals.endergetic.core.EndergeticExpansion;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class EEBlockTags {
	public static final TagKey<Block> END_PLANTABLE = blockTag("end_plantable");
	public static final TagKey<Block> POISE_PLANTABLE = blockTag("poise_plantable");
	public static final TagKey<Block> ENDER_FIRE_BASE_BLOCKS = blockTag("ender_fire_base_blocks");
	public static final TagKey<Block> END_CRYSTAL_PLACEABLE = blockTag("end_crystal_placeable");
	public static final TagKey<Block> CHORUS_PLANTABLE = blockTag("chorus_plantable");

	private static TagKey<Block> blockTag(String name) {
		return TagUtil.blockTag(EndergeticExpansion.MOD_ID, name);
	}
}
