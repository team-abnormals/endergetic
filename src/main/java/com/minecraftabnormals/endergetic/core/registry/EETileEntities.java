package com.minecraftabnormals.endergetic.core.registry;

import com.minecraftabnormals.abnormals_core.core.util.registry.TileEntitySubRegistryHelper;
import com.minecraftabnormals.endergetic.common.tileentities.*;
import com.minecraftabnormals.endergetic.common.tileentities.boof.BoofBlockTileEntity;
import com.minecraftabnormals.endergetic.common.tileentities.boof.DispensedBlockBoofTileEntity;
import com.minecraftabnormals.endergetic.core.EndergeticExpansion;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EndergeticExpansion.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class EETileEntities {
	private static final TileEntitySubRegistryHelper HELPER = EndergeticExpansion.REGISTRY_HELPER.getTileEntitySubHelper();

	public static final RegistryObject<TileEntityType<CorrockCrownTileEntity>> CORROCK_CROWN = HELPER.createTileEntity("corrock_crown", CorrockCrownTileEntity::new, () -> new Block[]{
			EEBlocks.CORROCK_CROWN_OVERWORLD_STANDING.get(), EEBlocks.CORROCK_CROWN_OVERWORLD_WALL.get(), EEBlocks.CORROCK_CROWN_NETHER_STANDING.get(),
			EEBlocks.CORROCK_CROWN_NETHER_WALL.get(), EEBlocks.CORROCK_CROWN_END_STANDING.get(), EEBlocks.CORROCK_CROWN_END_WALL.get(),
			EEBlocks.PETRIFIED_CORROCK_CROWN_OVERWORLD_STANDING.get(), EEBlocks.PETRIFIED_CORROCK_CROWN_OVERWORLD_WALL.get(), EEBlocks.PETRIFIED_CORROCK_CROWN_NETHER_STANDING.get(),
			EEBlocks.PETRIFIED_CORROCK_CROWN_NETHER_WALL.get(), EEBlocks.PETRIFIED_CORROCK_CROWN_END_STANDING.get(), EEBlocks.PETRIFIED_CORROCK_CROWN_END_WALL.get()
	});
	public static final RegistryObject<TileEntityType<FrisbloomStemTileEntity>> FRISBLOOM_STEM = HELPER.createTileEntity("frisbloom_stem", FrisbloomStemTileEntity::new, () -> new Block[]{EEBlocks.FRISBLOOM_STEM});
	public static final RegistryObject<TileEntityType<EnderCampfireTileEntity>> ENDER_CAMPFIRE = HELPER.createTileEntity("ender_campfire", EnderCampfireTileEntity::new, () -> new Block[]{EEBlocks.ENDER_CAMPFIRE.get()});

	/*
	 * Poise Forest
	 */
	public static final RegistryObject<TileEntityType<BolloomBudTileEntity>> BOLLOOM_BUD = HELPER.createTileEntity("bolloom_bud", BolloomBudTileEntity::new, () -> new Block[]{EEBlocks.BOLLOOM_BUD.get()});
	public static final RegistryObject<TileEntityType<PuffBugHiveTileEntity>> PUFFBUG_HIVE = HELPER.createTileEntity("puffbug_hive", PuffBugHiveTileEntity::new, () -> new Block[]{EEBlocks.PUFFBUG_HIVE.get()});
	public static final RegistryObject<TileEntityType<BoofBlockTileEntity>> BOOF_BLOCK = HELPER.createTileEntity("boof_block", BoofBlockTileEntity::new, () -> new Block[]{EEBlocks.BOOF_BLOCK.get()});
	public static final RegistryObject<TileEntityType<DispensedBlockBoofTileEntity>> BOOF_BLOCK_DISPENSED = HELPER.createTileEntity("boof_block_dispensed", DispensedBlockBoofTileEntity::new, () -> new Block[]{EEBlocks.BOOF_BLOCK_DISPENSED.get()});
}