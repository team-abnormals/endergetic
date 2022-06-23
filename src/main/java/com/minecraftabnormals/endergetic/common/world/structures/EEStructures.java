package com.minecraftabnormals.endergetic.common.world.structures;

import com.minecraftabnormals.endergetic.common.world.structures.pieces.EetleNestPieces;
import com.minecraftabnormals.endergetic.core.EndergeticExpansion;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.StructurePieceType;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.StructureFeatureConfiguration;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = EndergeticExpansion.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class EEStructures {
	public static final DeferredRegister<StructureFeature<?>> STRUCTURES = DeferredRegister.create(ForgeRegistries.STRUCTURE_FEATURES, EndergeticExpansion.MOD_ID);

	public static final RegistryObject<StructureFeature<NoneFeatureConfiguration>> EETLE_NEST = STRUCTURES.register("eetle_nest", () -> new EetleNestStructure(NoneFeatureConfiguration.CODEC));

	public static final class Configured {
		public static final ConfiguredStructureFeature<?, ?> EETLE_NEST = EEStructures.EETLE_NEST.get().configured(FeatureConfiguration.NONE);

		private static <FC extends FeatureConfiguration> void register(String name, ConfiguredStructureFeature<FC, ?> stuctureFeature) {
			Registry.register(BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE, new ResourceLocation(EndergeticExpansion.MOD_ID, name), stuctureFeature);
		}

		public static void registerConfiguredStructures() {
			register("eetle_nest", EETLE_NEST);
		}
	}

	public static final class PieceTypes {
		public static final StructurePieceType EETLE_NEST = StructurePieceType.setPieceId(EetleNestPieces.EetleNestPiece::new, "eetle_nest");
	}

	public static void setupStructureInfo() {
		StructureFeature<NoneFeatureConfiguration> eetleNest = EETLE_NEST.get();
		StructureFeature.STRUCTURES_REGISTRY.put("eetle_nest", eetleNest);
		BuiltinRegistries.NOISE_GENERATOR_SETTINGS.forEach(dimensionSettings -> dimensionSettings.structureSettings().structureConfig().put(eetleNest, new StructureFeatureConfiguration(18, 9, 5193657)));
	}
}
