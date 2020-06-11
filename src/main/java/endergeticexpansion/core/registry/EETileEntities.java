package endergeticexpansion.core.registry;

import endergeticexpansion.common.tileentities.*;
import endergeticexpansion.common.tileentities.boof.*;
import endergeticexpansion.core.EndergeticExpansion;
import endergeticexpansion.core.registry.util.EndergeticRegistryHelper;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EndergeticExpansion.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EETileEntities {
	public static final EndergeticRegistryHelper HELPER = EndergeticExpansion.REGISTRY_HELPER;
	
	public static final RegistryObject<TileEntityType<CorrockCrownTileEntity>> CORROCK_CROWN = HELPER.createTileEntity("corrock_crown", CorrockCrownTileEntity::new, () -> new Block[] {
		EEBlocks.CORROCK_CROWN_OVERWORLD_STANDING.get(), EEBlocks.CORROCK_CROWN_OVERWORLD_WALL.get(), EEBlocks.CORROCK_CROWN_NETHER_STANDING.get(),
		EEBlocks.CORROCK_CROWN_NETHER_WALL.get(), EEBlocks.CORROCK_CROWN_END_STANDING.get(), EEBlocks.CORROCK_CROWN_END_WALL.get(),
		EEBlocks.PETRIFIED_CORROCK_CROWN_OVERWORLD_STANDING.get(), EEBlocks.PETRIFIED_CORROCK_CROWN_OVERWORLD_WALL.get(), EEBlocks.PETRIFIED_CORROCK_CROWN_NETHER_STANDING.get(),
		EEBlocks.PETRIFIED_CORROCK_CROWN_NETHER_WALL.get(), EEBlocks.PETRIFIED_CORROCK_CROWN_END_STANDING.get(), EEBlocks.PETRIFIED_CORROCK_CROWN_END_WALL.get()
	});
	public static final RegistryObject<TileEntityType<EndStoneCoverTileEntity>> ENDSTONE_COVER = HELPER.createTileEntity("endstone_cover", EndStoneCoverTileEntity::new, () -> new Block[] {EEBlocks.ENDSTONE_COVER.get()});
	public static final RegistryObject<TileEntityType<FrisbloomStemTileEntity>> FRISBLOOM_STEM = HELPER.createTileEntity("frisbloom_stem", FrisbloomStemTileEntity::new, () -> new Block[] {EEBlocks.FRISBLOOM_STEM});
	
	/*
	 * Poise Forest
	 */
	public static final RegistryObject<TileEntityType<BolloomBudTileEntity>> BOLLOOM_BUD = HELPER.createTileEntity("bolloom_bud", BolloomBudTileEntity::new, () -> new Block[] {EEBlocks.BOLLOOM_BUD.get()});
	public static final RegistryObject<TileEntityType<PuffBugHiveTileEntity>> PUFFBUG_HIVE = HELPER.createTileEntity("puffbug_hive", PuffBugHiveTileEntity::new, () -> new Block[] {EEBlocks.PUFFBUG_HIVE.get()});
	public static final RegistryObject<TileEntityType<BoofTileEntity>> BOOF_BLOCK = HELPER.createTileEntity("boof_block", BoofTileEntity::new, () -> new Block[] {EEBlocks.BOOF_BLOCK.get()});
	public static final RegistryObject<TileEntityType<DispensedBoofTileEntity>> BOOF_BLOCK_DISPENSED = HELPER.createTileEntity("boof_block_dispensed", DispensedBoofTileEntity::new, () -> new Block[] {EEBlocks.BOOF_BLOCK_DISPENSED.get()});
}