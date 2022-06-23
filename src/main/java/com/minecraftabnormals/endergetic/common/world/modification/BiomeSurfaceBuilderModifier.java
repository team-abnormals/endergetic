package com.minecraftabnormals.endergetic.common.world.modification;

import com.minecraftabnormals.abnormals_core.common.world.modification.BiomeModificationContext;
import com.minecraftabnormals.abnormals_core.common.world.modification.BiomeModifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.surfacebuilders.ConfiguredSurfaceBuilder;

import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class BiomeSurfaceBuilderModifier extends BiomeModifier {

	private BiomeSurfaceBuilderModifier(BiPredicate<ResourceKey<Biome>, Biome> shouldModify, Consumer<BiomeModificationContext> modifier) {
		super(shouldModify, modifier);
	}

	public static BiomeSurfaceBuilderModifier surfaceBuilderReplacer(BiPredicate<ResourceKey<Biome>, Biome> shouldModify, Supplier<ConfiguredSurfaceBuilder<?>> configuredSurfaceBuilder) {
		return new BiomeSurfaceBuilderModifier(shouldModify, context -> {
			context.event.getGeneration().surfaceBuilder(configuredSurfaceBuilder);
		});
	}

}
