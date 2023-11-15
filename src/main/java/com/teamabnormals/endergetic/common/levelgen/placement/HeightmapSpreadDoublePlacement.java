package com.teamabnormals.endergetic.common.levelgen.placement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teamabnormals.endergetic.core.registry.EEPlacementModifierTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;

import java.util.stream.Stream;

public final class HeightmapSpreadDoublePlacement extends PlacementModifier {
	public static final Codec<HeightmapSpreadDoublePlacement> CODEC = RecordCodecBuilder.create((instance) -> {
		return instance.group(Heightmap.Types.CODEC.fieldOf("heightmap").forGetter((placement) -> {
			return placement.heightmap;
		})).apply(instance, HeightmapSpreadDoublePlacement::new);
	});
	public static final HeightmapSpreadDoublePlacement MOTION_BLOCKING = new HeightmapSpreadDoublePlacement(Heightmap.Types.MOTION_BLOCKING);

	private final Heightmap.Types heightmap;

	public HeightmapSpreadDoublePlacement(Heightmap.Types heightmap) {
		this.heightmap = heightmap;
	}

	@Override
	public Stream<BlockPos> getPositions(PlacementContext context, RandomSource random, BlockPos pos) {
		int x = pos.getX();
		int z = pos.getZ();
		int height = context.getHeight(this.heightmap, x, z);
		return height == 0 ? Stream.of() : Stream.of(new BlockPos(x, random.nextInt(height * 2), z));
	}

	@Override
	public PlacementModifierType<?> type() {
		return EEPlacementModifierTypes.HEIGHTMAP_SPREAD_DOUBLE.get();
	}
}
