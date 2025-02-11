package com.teamabnormals.endergetic.core.data.server;

import com.teamabnormals.blueprint.core.registry.BlueprintDataPackRegistries;
import com.teamabnormals.endergetic.core.EndergeticExpansion;
import com.teamabnormals.endergetic.core.registry.EEFeatures.EEConfiguredFeatures;
import com.teamabnormals.endergetic.core.registry.EEFeatures.EEPlacedFeatures;
import com.teamabnormals.endergetic.core.registry.EEStructureTypes.EEStructureSets;
import com.teamabnormals.endergetic.core.registry.EEStructureTypes.EEStructures;
import com.teamabnormals.endergetic.core.registry.builtin.*;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class EEDatapackBuiltinEntriesProvider extends DatapackBuiltinEntriesProvider {

	public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
			.add(Registries.CONFIGURED_FEATURE, EEConfiguredFeatures::bootstrap)
			.add(Registries.PLACED_FEATURE, EEPlacedFeatures::bootstrap)
			.add(Registries.BIOME, EEBiomes::bootstrap)
			.add(Registries.DAMAGE_TYPE, EEDamageTypes::bootstrap)
			.add(Registries.NOISE, EENoises::bootstrap)
			.add(Registries.STRUCTURE, EEStructures::bootstrap)
			.add(Registries.STRUCTURE_SET, EEStructureSets::bootstrap)
			.add(BlueprintDataPackRegistries.MODDED_BIOME_SLICES, EEBiomeSlices::bootstrap)
			.add(ForgeRegistries.Keys.BIOME_MODIFIERS, EEBiomeModifiers::bootstrap);

	public EEDatapackBuiltinEntriesProvider(PackOutput output, CompletableFuture<Provider> provider) {
		super(output, provider, BUILDER, Set.of(EndergeticExpansion.MOD_ID, "minecraft"));
	}
}