package endergeticexpansion.core.registry;

import com.google.common.collect.Sets;

import endergeticexpansion.common.tileentities.*;
import endergeticexpansion.common.tileentities.boof.*;
import endergeticexpansion.core.EndergeticExpansion;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class EETileEntities {
	public static final DeferredRegister<TileEntityType<?>> TILE_ENTITY_TYPES = new DeferredRegister<>(ForgeRegistries.TILE_ENTITIES, EndergeticExpansion.MOD_ID);
	
	public static final RegistryObject<TileEntityType<TileEntityCorrockCrown>> CORROCK_CROWN = TILE_ENTITY_TYPES.register("corrock_crown", () -> new TileEntityType<>(TileEntityCorrockCrown::new, Sets.newHashSet(
		EEBlocks.CORROCK_CROWN_OVERWORLD_STANDING.get(), EEBlocks.CORROCK_CROWN_OVERWORLD_WALL.get(), EEBlocks.CORROCK_CROWN_NETHER_STANDING.get(),
		EEBlocks.CORROCK_CROWN_NETHER_WALL.get(), EEBlocks.CORROCK_CROWN_END_STANDING.get(), EEBlocks.CORROCK_CROWN_END_WALL.get(),
		EEBlocks.PETRIFIED_CORROCK_CROWN_OVERWORLD_STANDING.get(), EEBlocks.PETRIFIED_CORROCK_CROWN_OVERWORLD_WALL.get(), EEBlocks.PETRIFIED_CORROCK_CROWN_NETHER_STANDING.get(),
		EEBlocks.PETRIFIED_CORROCK_CROWN_NETHER_WALL.get(), EEBlocks.PETRIFIED_CORROCK_CROWN_END_STANDING.get(), EEBlocks.PETRIFIED_CORROCK_CROWN_END_WALL.get()), null
	));
	public static final RegistryObject<TileEntityType<TileEntityEndStoneCover>> ENDSTONE_COVER = TILE_ENTITY_TYPES.register("endstone_cover", () -> new TileEntityType<>(TileEntityEndStoneCover::new, Sets.newHashSet(EEBlocks.ENDSTONE_COVER.get()), null));
	public static final RegistryObject<TileEntityType<TileEntityFrisbloomStem>> FRISBLOOM_STEM = TILE_ENTITY_TYPES.register("frisbloom_stem", () -> new TileEntityType<>(TileEntityFrisbloomStem::new, Sets.newHashSet(EEBlocks.FRISBLOOM_STEM), null));
	
	/*
	 * Poise Forest
	 */
	public static final RegistryObject<TileEntityType<TileEntityBolloomBud>> BOLLOOM_BUD = TILE_ENTITY_TYPES.register("bolloom_bud", () -> new TileEntityType<>(TileEntityBolloomBud::new, Sets.newHashSet(EEBlocks.BOLLOOM_BUD.get()), null));
	public static final RegistryObject<TileEntityType<TileEntityPuffBugHive>> PUFFBUG_HIVE = TILE_ENTITY_TYPES.register("puffbug_hive", () -> new TileEntityType<>(TileEntityPuffBugHive::new, Sets.newHashSet(EEBlocks.PUFFBUG_HIVE.get()), null));
	public static final RegistryObject<TileEntityType<TileEntityBoof>> BOOF_BLOCK = TILE_ENTITY_TYPES.register("boof_block", () -> new TileEntityType<>(TileEntityBoof::new, Sets.newHashSet(EEBlocks.BOOF_BLOCK.get()), null));
	public static final RegistryObject<TileEntityType<TileEntityDispensedBoof>> BOOF_BLOCK_DISPENSED = TILE_ENTITY_TYPES.register("boof_block_dispensed", () -> new TileEntityType<>(TileEntityDispensedBoof::new, Sets.newHashSet(EEBlocks.BOOF_BLOCK_DISPENSED.get()), null));
}