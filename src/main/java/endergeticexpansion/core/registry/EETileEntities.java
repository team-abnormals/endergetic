package endergeticexpansion.core.registry;

import endergeticexpansion.common.tileentities.TileEntityBolloomBud;
import endergeticexpansion.common.tileentities.TileEntityCorrockCrown;
import endergeticexpansion.common.tileentities.TileEntityEndStoneCover;
import endergeticexpansion.common.tileentities.TileEntityFrisbloomStem;
import endergeticexpansion.common.tileentities.TileEntityPuffBugHive;
import endergeticexpansion.common.tileentities.boof.TileEntityBoof;
import endergeticexpansion.common.tileentities.boof.TileEntityDispensedBoof;
import endergeticexpansion.core.EndergeticExpansion;
import endergeticexpansion.core.registry.util.EndergeticRegistryHelper;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EndergeticExpansion.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EETileEntities {
	public static final EndergeticRegistryHelper HELPER = EndergeticExpansion.REGISTRY_HELPER;
	
	public static final RegistryObject<TileEntityType<TileEntityCorrockCrown>> CORROCK_CROWN = HELPER.createTileEntity("corrock_crown", TileEntityCorrockCrown::new, () -> new Block[] {
		EEBlocks.CORROCK_CROWN_OVERWORLD_STANDING.get(), EEBlocks.CORROCK_CROWN_OVERWORLD_WALL.get(), EEBlocks.CORROCK_CROWN_NETHER_STANDING.get(),
		EEBlocks.CORROCK_CROWN_NETHER_WALL.get(), EEBlocks.CORROCK_CROWN_END_STANDING.get(), EEBlocks.CORROCK_CROWN_END_WALL.get(),
		EEBlocks.PETRIFIED_CORROCK_CROWN_OVERWORLD_STANDING.get(), EEBlocks.PETRIFIED_CORROCK_CROWN_OVERWORLD_WALL.get(), EEBlocks.PETRIFIED_CORROCK_CROWN_NETHER_STANDING.get(),
		EEBlocks.PETRIFIED_CORROCK_CROWN_NETHER_WALL.get(), EEBlocks.PETRIFIED_CORROCK_CROWN_END_STANDING.get(), EEBlocks.PETRIFIED_CORROCK_CROWN_END_WALL.get()
	});
	public static final RegistryObject<TileEntityType<TileEntityEndStoneCover>> ENDSTONE_COVER = HELPER.createTileEntity("endstone_cover", TileEntityEndStoneCover::new, () -> new Block[] {EEBlocks.ENDSTONE_COVER.get()});
	public static final RegistryObject<TileEntityType<TileEntityFrisbloomStem>> FRISBLOOM_STEM = HELPER.createTileEntity("frisbloom_stem", TileEntityFrisbloomStem::new, () -> new Block[] {EEBlocks.FRISBLOOM_STEM});
	
	/*
	 * Poise Forest
	 */
	public static final RegistryObject<TileEntityType<TileEntityBolloomBud>> BOLLOOM_BUD = HELPER.createTileEntity("bolloom_bud", TileEntityBolloomBud::new, () -> new Block[] {EEBlocks.BOLLOOM_BUD.get()});
	public static final RegistryObject<TileEntityType<TileEntityPuffBugHive>> PUFFBUG_HIVE = HELPER.createTileEntity("puffbug_hive", TileEntityPuffBugHive::new, () -> new Block[] {EEBlocks.PUFFBUG_HIVE.get()});
	public static final RegistryObject<TileEntityType<TileEntityBoof>> BOOF_BLOCK = HELPER.createTileEntity("boof_block", TileEntityBoof::new, () -> new Block[] {EEBlocks.BOOF_BLOCK.get()});
	public static final RegistryObject<TileEntityType<TileEntityDispensedBoof>> BOOF_BLOCK_DISPENSED = HELPER.createTileEntity("boof_block_dispensed", TileEntityDispensedBoof::new, () -> new Block[] {EEBlocks.BOOF_BLOCK_DISPENSED.get()});
}