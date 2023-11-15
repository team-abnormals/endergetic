package com.teamabnormals.endergetic.core.registry;

import com.teamabnormals.blueprint.core.util.registry.BlockEntitySubRegistryHelper;
import com.teamabnormals.endergetic.common.block.entity.*;
import com.teamabnormals.endergetic.common.block.entity.boof.BoofBlockTileEntity;
import com.teamabnormals.endergetic.common.block.entity.boof.DispensedBlockBoofTileEntity;
import com.teamabnormals.endergetic.core.EndergeticExpansion;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.registries.RegistryObject;

@EventBusSubscriber(modid = EndergeticExpansion.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public final class EEBlockEntityTypes {
	private static final BlockEntitySubRegistryHelper HELPER = EndergeticExpansion.REGISTRY_HELPER.getBlockEntitySubHelper();

	public static final RegistryObject<BlockEntityType<CorrockCrownTileEntity>> CORROCK_CROWN = HELPER.createBlockEntity("corrock_crown", CorrockCrownTileEntity::new, () -> new Block[]{
			EEBlocks.CORROCK_CROWN_OVERWORLD_STANDING.get(), EEBlocks.CORROCK_CROWN_OVERWORLD_WALL.get(), EEBlocks.CORROCK_CROWN_NETHER_STANDING.get(),
			EEBlocks.CORROCK_CROWN_NETHER_WALL.get(), EEBlocks.CORROCK_CROWN_END_STANDING.get(), EEBlocks.CORROCK_CROWN_END_WALL.get(),
			EEBlocks.PETRIFIED_CORROCK_CROWN_OVERWORLD_STANDING.get(), EEBlocks.PETRIFIED_CORROCK_CROWN_OVERWORLD_WALL.get(), EEBlocks.PETRIFIED_CORROCK_CROWN_NETHER_STANDING.get(),
			EEBlocks.PETRIFIED_CORROCK_CROWN_NETHER_WALL.get(), EEBlocks.PETRIFIED_CORROCK_CROWN_END_STANDING.get(), EEBlocks.PETRIFIED_CORROCK_CROWN_END_WALL.get()
	});
	public static final RegistryObject<BlockEntityType<EnderCampfireTileEntity>> ENDER_CAMPFIRE = HELPER.createBlockEntity("ender_campfire", EnderCampfireTileEntity::new, () -> new Block[]{EEBlocks.ENDER_CAMPFIRE.get()});
	public static final RegistryObject<BlockEntityType<EetleEggTileEntity>> EETLE_EGG = HELPER.createBlockEntity("eetle_egg", EetleEggTileEntity::new, () -> new Block[]{EEBlocks.EETLE_EGG.get()});

	public static final RegistryObject<BlockEntityType<BolloomBudTileEntity>> BOLLOOM_BUD = HELPER.createBlockEntity("bolloom_bud", BolloomBudTileEntity::new, () -> new Block[]{EEBlocks.BOLLOOM_BUD.get()});
	public static final RegistryObject<BlockEntityType<PuffBugHiveTileEntity>> PUFFBUG_HIVE = HELPER.createBlockEntity("puffbug_hive", PuffBugHiveTileEntity::new, () -> new Block[]{EEBlocks.PUFFBUG_HIVE.get()});
	public static final RegistryObject<BlockEntityType<BoofBlockTileEntity>> BOOF_BLOCK = HELPER.createBlockEntity("boof_block", BoofBlockTileEntity::new, () -> new Block[]{EEBlocks.BOOF_BLOCK.get()});
	public static final RegistryObject<BlockEntityType<DispensedBlockBoofTileEntity>> BOOF_BLOCK_DISPENSED = HELPER.createBlockEntity("boof_block_dispensed", DispensedBlockBoofTileEntity::new, () -> new Block[]{EEBlocks.BOOF_BLOCK_DISPENSED.get()});
}