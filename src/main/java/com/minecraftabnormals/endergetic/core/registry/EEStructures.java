package com.minecraftabnormals.endergetic.core.registry;

import com.minecraftabnormals.endergetic.common.world.structures.EetleNestStructure;
import com.minecraftabnormals.endergetic.common.world.structures.pieces.EetleNestPieces;
import com.minecraftabnormals.endergetic.core.EndergeticExpansion;
import com.minecraftabnormals.endergetic.core.registry.other.EETags;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSpawnOverride;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.Map;

public final class EEStructures {
	public static final DeferredRegister<StructureType<?>> STRUCTURE_TYPES = DeferredRegister.create(Registry.STRUCTURE_TYPE_REGISTRY, EndergeticExpansion.MOD_ID);
	public static final DeferredRegister<Structure> STRUCTURES = DeferredRegister.create(Registry.STRUCTURE_REGISTRY, EndergeticExpansion.MOD_ID);

	public static final RegistryObject<StructureType<EetleNestStructure>> EETLE_NEST_TYPE = STRUCTURE_TYPES.register("eetle_nest", () -> (StructureType<EetleNestStructure>) () -> EetleNestStructure.CODEC);

	public static final RegistryObject<EetleNestStructure> EETLE_NEST = STRUCTURES.register("eetle_nest", () -> new EetleNestStructure(new Structure.StructureSettings(biomes(EETags.Biomes.HAS_EETLE_NEST), Map.of(MobCategory.MONSTER, new StructureSpawnOverride(StructureSpawnOverride.BoundingBoxType.STRUCTURE, WeightedRandomList.create(new MobSpawnSettings.SpawnerData(EEEntities.CHARGER_EETLE.get(), 12, 3, 7), new MobSpawnSettings.SpawnerData(EEEntities.GLIDER_EETLE.get(), 8, 3, 6)))), GenerationStep.Decoration.RAW_GENERATION, TerrainAdjustment.NONE)));

	public static final class PieceTypes {
		public static final DeferredRegister<StructurePieceType> STRUCTURE_PIECE_TYPES = DeferredRegister.create(Registry.STRUCTURE_PIECE_REGISTRY, EndergeticExpansion.MOD_ID);

		public static final RegistryObject<StructurePieceType.StructureTemplateType> EETLE_NEST = STRUCTURE_PIECE_TYPES.register("eetle_nest", () -> EetleNestPieces.EetleNestPiece::new);
	}

	@SuppressWarnings("deprecation")
	private static HolderSet<Biome> biomes(TagKey<Biome> tagKey) {
		return BuiltinRegistries.BIOME.getOrCreateTag(tagKey);
	}
}
