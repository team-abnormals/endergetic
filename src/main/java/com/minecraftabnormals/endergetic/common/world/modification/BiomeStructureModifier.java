package com.minecraftabnormals.endergetic.common.world.modification;

import com.minecraftabnormals.abnormals_core.common.world.modification.BiomeModificationContext;
import com.minecraftabnormals.abnormals_core.common.world.modification.BiomeModifier;
import net.minecraft.util.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.StructureFeature;

import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Supplier;

//TODO: Move to AC
public final class BiomeStructureModifier extends BiomeModifier {

	private BiomeStructureModifier(BiPredicate<RegistryKey<Biome>, Biome> shouldModify, Consumer<BiomeModificationContext> modifier) {
		super(shouldModify, modifier);
	}

	public static BiomeStructureModifier createStructureAdder(BiPredicate<RegistryKey<Biome>, Biome> shouldModify, Supplier<StructureFeature<?, ?>> structureFeatureSupplier) {
		return new BiomeStructureModifier(shouldModify, context -> {
			context.event.getGeneration().withStructure(structureFeatureSupplier.get());
		});
	}

}
