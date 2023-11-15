package com.teamabnormals.endergetic.core.registry;

import com.teamabnormals.endergetic.common.levelgen.structure.EetleNestStructure;
import com.teamabnormals.endergetic.common.levelgen.structure.structures.EetleNestPieces;
import com.teamabnormals.endergetic.core.EndergeticExpansion;
import com.teamabnormals.endergetic.core.registry.other.tags.EEBiomeTags;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.structure.*;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.Map;

public final class EEStructures {
	public static final DeferredRegister<StructureType<?>> STRUCTURE_TYPES = DeferredRegister.create(Registry.STRUCTURE_TYPE_REGISTRY, EndergeticExpansion.MOD_ID);
	public static final DeferredRegister<Structure> STRUCTURES = DeferredRegister.create(Registry.STRUCTURE_REGISTRY, EndergeticExpansion.MOD_ID);

	public static final RegistryObject<StructureType<EetleNestStructure>> EETLE_NEST_TYPE = STRUCTURE_TYPES.register("eetle_nest", () -> (StructureType<EetleNestStructure>) () -> EetleNestStructure.CODEC);

	public static final RegistryObject<Structure> EETLE_NEST = STRUCTURES.register("eetle_nest", () -> new EetleNestStructure(new Structure.StructureSettings(biomes(EEBiomeTags.HAS_EETLE_NEST), Map.of(MobCategory.MONSTER, new StructureSpawnOverride(StructureSpawnOverride.BoundingBoxType.STRUCTURE, WeightedRandomList.create(new MobSpawnSettings.SpawnerData(EEEntityTypes.CHARGER_EETLE.get(), 12, 3, 7), new MobSpawnSettings.SpawnerData(EEEntityTypes.GLIDER_EETLE.get(), 8, 3, 6)))), GenerationStep.Decoration.RAW_GENERATION, TerrainAdjustment.NONE)));

	public static final class PieceTypes {
		public static final DeferredRegister<StructurePieceType> STRUCTURE_PIECE_TYPES = DeferredRegister.create(Registry.STRUCTURE_PIECE_REGISTRY, EndergeticExpansion.MOD_ID);

		public static final RegistryObject<StructurePieceType.StructureTemplateType> EETLE_NEST = STRUCTURE_PIECE_TYPES.register("eetle_nest", () -> EetleNestPieces.EetleNestPiece::new);
	}

	public static final class Sets {
		public static final DeferredRegister<StructureSet> STRUCTURE_SETS = DeferredRegister.create(Registry.STRUCTURE_SET_REGISTRY, EndergeticExpansion.MOD_ID);

		public static final RegistryObject<StructureSet> EETLE_NEST = STRUCTURE_SETS.register("eetle_nest", () -> new StructureSet(EEStructures.EETLE_NEST.getHolder().get(), new RandomSpreadStructurePlacement(18, 9, RandomSpreadType.TRIANGULAR, 5193657)));
	}

	@SuppressWarnings("deprecation")
	private static HolderSet<Biome> biomes(TagKey<Biome> tagKey) {
		return BuiltinRegistries.BIOME.getOrCreateTag(tagKey);
	}
}
