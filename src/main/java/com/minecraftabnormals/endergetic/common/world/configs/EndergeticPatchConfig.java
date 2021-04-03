package com.minecraftabnormals.endergetic.common.world.configs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.IFeatureConfig;

public final class EndergeticPatchConfig implements IFeatureConfig {
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

	@SuppressWarnings("deprecation")
	public static BlockPos getPos(ISeedReader world, BlockPos pos, boolean searchDown) {
		if (searchDown) {
			BlockPos.Mutable mutable = pos.toMutable();
			for (BlockState blockstate = world.getBlockState(mutable); (blockstate.isAir()) && mutable.getY() > 0; blockstate = world.getBlockState(mutable)) {
				mutable.move(Direction.DOWN);
			}
			return mutable;
		}
		return world.getHeight(Heightmap.Type.WORLD_SURFACE_WG, pos);
	}
}
