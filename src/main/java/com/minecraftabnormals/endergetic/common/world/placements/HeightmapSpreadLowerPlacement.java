package com.minecraftabnormals.endergetic.common.world.placements;

import com.mojang.serialization.Codec;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.WorldDecoratingHelper;
import net.minecraft.world.gen.placement.HeightmapBasedPlacement;
import net.minecraft.world.gen.placement.NoPlacementConfig;

import java.util.Random;
import java.util.stream.Stream;

public class HeightmapSpreadLowerPlacement extends HeightmapBasedPlacement<NoPlacementConfig> {

	public HeightmapSpreadLowerPlacement(Codec<NoPlacementConfig> codec) {
		super(codec);
	}

	@Override
	protected Heightmap.Type func_241858_a(NoPlacementConfig config) {
		return Heightmap.Type.MOTION_BLOCKING;
	}

	public Stream<BlockPos> getPositions(WorldDecoratingHelper helper, Random rand, NoPlacementConfig config, BlockPos pos) {
		int x = pos.getX();
		int z = pos.getZ();
		int height = helper.func_242893_a(this.func_241858_a(config), x, z);
		return height == 0 ? Stream.of() : Stream.of(new BlockPos(x, Math.min(60, rand.nextInt((int) (height * 0.85F))), z));
	}

}
