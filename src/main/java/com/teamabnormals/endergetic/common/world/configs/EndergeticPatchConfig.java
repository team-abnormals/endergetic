package com.teamabnormals.endergetic.common.world.configs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public final class EndergeticPatchConfig implements FeatureConfiguration {
	public static final Codec<EndergeticPatchConfig> CODEC = RecordCodecBuilder.create(instance -> {
		return instance.group(
				Codec.FLOAT.fieldOf("frequency").forGetter(config -> config.frequency),
				Codec.BOOL.fieldOf("search_down").forGetter(config -> config.searchDown)
		).apply(instance, EndergeticPatchConfig::new);
	});
	private final float frequency;
	private final boolean searchDown;

	public EndergeticPatchConfig(float frequency, boolean searchDown) {
		this.frequency = frequency;
		this.searchDown = searchDown;
	}

	public float getFrequency() {
		return this.frequency;
	}

	public boolean shouldSearchDown() {
		return this.searchDown;
	}

	public static BlockPos getPos(WorldGenLevel world, BlockPos pos, boolean searchDown) {
		if (searchDown) {
			BlockPos.MutableBlockPos mutable = pos.mutable();
			for (BlockState blockstate = world.getBlockState(mutable); (blockstate.isAir()) && mutable.getY() > 0; blockstate = world.getBlockState(mutable)) {
				mutable.move(Direction.DOWN);
			}
			return mutable;
		}
		return world.getHeightmapPos(Heightmap.Types.WORLD_SURFACE_WG, pos);
	}
}
