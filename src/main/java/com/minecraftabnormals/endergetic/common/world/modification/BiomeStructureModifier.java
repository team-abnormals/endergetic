package com.minecraftabnormals.endergetic.common.world.modification;

import com.minecraftabnormals.abnormals_core.common.world.modification.BiomeModificationContext;
import com.minecraftabnormals.abnormals_core.common.world.modification.BiomeModifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;

import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Supplier;

//TODO: Move to AC
public final class BiomeStructureModifier extends BiomeModifier {

	private BiomeStructureModifier(BiPredicate<ResourceKey<Biome>, Biome> shouldModify, Consumer<BiomeModificationContext> modifier) {
		super(shouldModify, modifier);
	}

	public static BiomeStructureModifier createStructureAdder(BiPredicate<ResourceKey<Biome>, Biome> shouldModify, Supplier<ConfiguredStructureFeature<?, ?>> structureFeatureSupplier) {
		return new BiomeStructureModifier(shouldModify, context -> {
			context.event.getGeneration().addStructureStart(structureFeatureSupplier.get());
		});
	}

}
