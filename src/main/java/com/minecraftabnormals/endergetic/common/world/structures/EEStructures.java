package com.minecraftabnormals.endergetic.common.world.structures;

import com.minecraftabnormals.endergetic.common.world.structures.pieces.EetleNestPieces;
import com.minecraftabnormals.endergetic.core.EndergeticExpansion;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = EndergeticExpansion.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class EEStructures {
	public static final DeferredRegister<Structure<?>> STRUCTURES = DeferredRegister.create(ForgeRegistries.STRUCTURE_FEATURES, EndergeticExpansion.MOD_ID);

	public static final RegistryObject<Structure<NoFeatureConfig>> EETLE_NEST = STRUCTURES.register("eetle_nest", () -> new EetleNestStructure(NoFeatureConfig.field_236558_a_));

	public static final class Configured {
		public static final StructureFeature<?, ?> EETLE_NEST = EEStructures.EETLE_NEST.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG);

		private static <FC extends IFeatureConfig> void register(String name, StructureFeature<FC, ?> stuctureFeature) {
			Registry.register(WorldGenRegistries.CONFIGURED_STRUCTURE_FEATURE, new ResourceLocation(EndergeticExpansion.MOD_ID, name), stuctureFeature);
		}

		public static void registerConfiguredStructures() {
			register("eetle_nest", EETLE_NEST);
		}
	}

	public static final class PieceTypes {
		public static final IStructurePieceType EETLE_NEST_PARENT = IStructurePieceType.register(EetleNestPieces.EetleNestParentPiece::new, "eetle_nest_parent");
		public static final IStructurePieceType EETLE_NEST_CORE = IStructurePieceType.register(EetleNestPieces.EetleNestCorePiece::new, "eetle_nest_core");
	}

	public static void setupStructureInfo() {
		Structure<NoFeatureConfig> eetleNest = EETLE_NEST.get();
		Structure.NAME_STRUCTURE_BIMAP.put("eetle_nest", eetleNest);
		WorldGenRegistries.NOISE_SETTINGS.forEach(dimensionSettings -> dimensionSettings.getStructures().func_236195_a_().put(eetleNest, new StructureSeparationSettings(18, 10,10387313)));
	}
}
