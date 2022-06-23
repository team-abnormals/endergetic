package com.minecraftabnormals.endergetic.common.world.placements;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.placement.DecorationContext;
import net.minecraft.world.level.levelgen.placement.EdgeDecorator;
import net.minecraft.world.level.levelgen.feature.configurations.NoneDecoratorConfiguration;

import java.util.Random;
import java.util.stream.Stream;

public class HeightmapSpreadLowerPlacement extends EdgeDecorator<NoneDecoratorConfiguration> {

	public HeightmapSpreadLowerPlacement(Codec<NoneDecoratorConfiguration> codec) {
		super(codec);
	}

	@Override
	protected Heightmap.Types type(NoneDecoratorConfiguration config) {
		return Heightmap.Types.MOTION_BLOCKING;
	}

	public Stream<BlockPos> getPositions(DecorationContext helper, Random rand, NoneDecoratorConfiguration config, BlockPos pos) {
		int x = pos.getX();
		int z = pos.getZ();
		int height = helper.getHeight(this.type(config), x, z);
		return height == 0 ? Stream.of() : Stream.of(new BlockPos(x, Mth.clamp(rand.nextInt((int) (height * 0.85F)), rand.nextInt(10) + 22, 57), z));
	}

}
