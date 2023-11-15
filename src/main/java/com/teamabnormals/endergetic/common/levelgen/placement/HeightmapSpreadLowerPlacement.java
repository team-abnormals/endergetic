package com.teamabnormals.endergetic.common.levelgen.placement;

import com.mojang.serialization.Codec;
import com.teamabnormals.endergetic.core.registry.EEPlacementModifierTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;

import java.util.stream.Stream;

public class HeightmapSpreadLowerPlacement extends PlacementModifier {
	public static final HeightmapSpreadLowerPlacement INSTANCE = new HeightmapSpreadLowerPlacement();
	public static final Codec<HeightmapSpreadLowerPlacement> CODEC = Codec.unit(INSTANCE);

	private HeightmapSpreadLowerPlacement() {
	}

	@Override
	public Stream<BlockPos> getPositions(PlacementContext context, RandomSource rand, BlockPos pos) {
		int x = pos.getX();
		int z = pos.getZ();
		int height = context.getHeight(Heightmap.Types.MOTION_BLOCKING, x, z);
		return height == 0 ? Stream.of() : Stream.of(new BlockPos(x, Mth.clamp(rand.nextInt((int) (height * 0.85F)), rand.nextInt(10) + 22, 57), z));
	}

	@Override
	public PlacementModifierType<?> type() {
		return EEPlacementModifierTypes.HEIGHTMAP_SPREAD_LOWER.get();
	}
}
