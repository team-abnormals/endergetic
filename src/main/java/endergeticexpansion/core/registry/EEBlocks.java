package endergeticexpansion.core.registry;

import endergeticexpansion.client.render.item.EETileEntityItemRenderer;
import endergeticexpansion.common.EEProperties;
import endergeticexpansion.common.blocks.*;
import endergeticexpansion.common.blocks.poise.*;
import endergeticexpansion.common.blocks.poise.boof.*;
import endergeticexpansion.common.blocks.poise.hive.*;
import endergeticexpansion.core.EndergeticExpansion;
import endergeticexpansion.core.registry.util.RegistryUtils;
import net.minecraft.block.Block;
import net.minecraft.block.Block.Properties;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowerPotBlock;
import net.minecraft.block.PressurePlateBlock;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Rarity;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(modid = EndergeticExpansion.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EEBlocks {
	//Block Init
	public static Block CORROCK_BLOCK_OVERWORLD = new BlockCorrockBlock(EEProperties.CORROCK_BASE(MaterialColor.BROWN_TERRACOTTA, true)).setRegistryName(EndergeticExpansion.MOD_ID, "corrock_overworld_block");
	public static Block CORROCK_BLOCK_NETHER    = new BlockCorrockBlock(EEProperties.CORROCK_BASE(MaterialColor.RED_TERRACOTTA, true)).setRegistryName(EndergeticExpansion.MOD_ID, "corrock_nether_block");
	public static Block CORROCK_BLOCK_END       = new BlockCorrockBlock(EEProperties.CORROCK_BASE(MaterialColor.PURPLE_TERRACOTTA, true)).setRegistryName(EndergeticExpansion.MOD_ID, "corrock_end_block");
	public static Block CORROCK_OVERWORLD       = new BlockCorrock(EEProperties.CORROCK_BASE(MaterialColor.BROWN_TERRACOTTA, false)).setRegistryName(EndergeticExpansion.MOD_ID, "corrock_overworld");
	public static Block CORROCK_NETHER          = new BlockCorrock(EEProperties.CORROCK_BASE(MaterialColor.RED_TERRACOTTA, false)).setRegistryName(EndergeticExpansion.MOD_ID, "corrock_nether");
	public static Block CORROCK_END             = new BlockCorrock(EEProperties.CORROCK_BASE(MaterialColor.PURPLE_TERRACOTTA, false)).setRegistryName(EndergeticExpansion.MOD_ID, "corrock_end");
	public static Block CORROCK_CROWN_OVERWORLD_WALL     = new BlockCorrockCrownWall(EEProperties.CORROCK_BASE_GLOWING(MaterialColor.BROWN)).setRegistryName(EndergeticExpansion.MOD_ID, "corrock_crown_wall_overworld");
	public static Block CORROCK_CROWN_OVERWORLD_STANDING = new BlockCorrockCrownStanding(EEProperties.CORROCK_BASE_GLOWING(MaterialColor.BROWN)).setRegistryName(EndergeticExpansion.MOD_ID, "corrock_crown_standing_overworld");
	public static Block CORROCK_CROWN_NETHER_WALL        = new BlockCorrockCrownWall(EEProperties.CORROCK_BASE_GLOWING(MaterialColor.RED)).setRegistryName(EndergeticExpansion.MOD_ID, "corrock_crown_wall_nether");
	public static Block CORROCK_CROWN_NETHER_STANDING    = new BlockCorrockCrownStanding(EEProperties.CORROCK_BASE_GLOWING(MaterialColor.RED)).setRegistryName(EndergeticExpansion.MOD_ID, "corrock_crown_standing_nether");
	public static Block CORROCK_CROWN_END_WALL           = new BlockCorrockCrownWall(EEProperties.CORROCK_BASE_GLOWING(MaterialColor.PURPLE)).setRegistryName(EndergeticExpansion.MOD_ID, "corrock_crown_wall_end");
	public static Block CORROCK_CROWN_END_STANDING       = new BlockCorrockCrownStanding(EEProperties.CORROCK_BASE_GLOWING(MaterialColor.PURPLE)).setRegistryName(EndergeticExpansion.MOD_ID, "corrock_crown_standing_end");
	
	/*
	 * Poise Forest
	 */
	public static Block POISMOSS_EUMUS       = new BlockPoiseEumusGrass(EEProperties.POISMOSS_EUMUS).setRegistryName(EndergeticExpansion.MOD_ID, "poismoss_eumus");
	public static Block POISE_GRASS_BLOCK    = new BlockPoiseGrass(EEProperties.POISE_GRASS(false)).setRegistryName(EndergeticExpansion.MOD_ID, "poise_grass_block");
	public static Block POISE_GRASS          = new BlockPoiseGrassPlant(EEProperties.POISE_GRASS(true)).setRegistryName(EndergeticExpansion.MOD_ID, "poise_grass");
	public static Block POISE_GRASS_TALL     = new BlockPoiseGrassPlantTall(EEProperties.POISE_GRASS(true)).setRegistryName(EndergeticExpansion.MOD_ID, "poise_grass_tall");
	public static Block POISE_CLUSTER        = new BlockPoiseCluster(EEProperties.POISE_CLUSTER.tickRandomly()).setRegistryName(EndergeticExpansion.MOD_ID, "poise_cluster");
	public static Block POISE_LOG            = new BlockPoiseLog(EEProperties.POISE_WOOD).setRegistryName(EndergeticExpansion.MOD_ID, "poise_log");
	public static Block POISE_WOOD           = new BlockPoiseWood(EEProperties.POISE_WOOD).setRegistryName(EndergeticExpansion.MOD_ID, "poise_wood");
	public static Block POISE_LOG_GLOWING    = new BlockPoiseLog(EEProperties.POISE_LOG_GLOWING).setRegistryName(EndergeticExpansion.MOD_ID, "poise_log_glowing");
	public static Block POISE_WOOD_GLOWING   = new BlockPoiseWood(EEProperties.POISE_LOG_GLOWING).setRegistryName(EndergeticExpansion.MOD_ID, "poise_wood_glowing");
	public static Block POISE_LOG_STRIPPED   = new BlockPoiseLog(EEProperties.POISE_WOOD).setRegistryName(EndergeticExpansion.MOD_ID, "poise_log_stripped");
	public static Block POISE_WOOD_STRIPPED  = new BlockPoiseWood(EEProperties.POISE_WOOD).setRegistryName(EndergeticExpansion.MOD_ID, "poise_wood_stripped");
	public static Block POISE_PLANKS         = new BlockPoisePlanks(EEProperties.POISE_WOOD).setRegistryName(EndergeticExpansion.MOD_ID, "poise_planks");
	public static Block POISE_DOOR           = new BlockPoiseDoor(EEProperties.POISE_WOOD).setRegistryName(EndergeticExpansion.MOD_ID, "poise_door");
	public static Block POISE_SLAB           = new BlockSlabBase(EEProperties.POISE_WOOD).setRegistryName(EndergeticExpansion.MOD_ID, "poise_slab");
	public static Block POISE_STAIRS         = new BlockStairsBase(POISE_PLANKS.getDefaultState(), EEProperties.POISE_WOOD).setRegistryName(EndergeticExpansion.MOD_ID, "poise_stairs");
	public static Block POISE_FENCE          = new BlockFenceBase(EEProperties.POISE_WOOD).setRegistryName(EndergeticExpansion.MOD_ID, "poise_fence");
	public static Block POISE_FENCE_GATE     = new BlockFenceGateBase(EEProperties.POISE_WOOD).setRegistryName(EndergeticExpansion.MOD_ID, "poise_fence_gate");
	public static Block POISE_PRESSURE_PLATE = new BlockPressurePlateBase(PressurePlateBlock.Sensitivity.EVERYTHING, EEProperties.POISE_WOOD).setRegistryName(EndergeticExpansion.MOD_ID, "poise_pressure_plate");
	public static Block POISE_BUTTON         = new BlockButtonBase(EEProperties.POISE_WOOD).setRegistryName(EndergeticExpansion.MOD_ID, "poise_button");
	public static Block POISE_TRAPDOOR       = new BlockTrapdoorBase(EEProperties.POISE_WOOD).setRegistryName(EndergeticExpansion.MOD_ID, "poise_trapdoor");
	public static Block BOLLOOM_BUD          = new BlockBolloomBud(EEProperties.POISE_WOOD.tickRandomly()).setRegistryName(EndergeticExpansion.MOD_ID, "bolloom_bud");
	public static Block PUFFBUG_HIVE         = new BlockPuffBugHive(EEProperties.PUFFBUG_HIVE(true)).setRegistryName(EndergeticExpansion.MOD_ID, "puffbug_hive");
	public static Block HIVE_HANGER          = new BlockHiveHanger(EEProperties.PUFFBUG_HIVE(false)).setRegistryName(EndergeticExpansion.MOD_ID, "hive_hanger");
	public static Block BOLLOOM_PARTICLE     = new Block(EEProperties.POISE_WOOD.doesNotBlockMovement()).setRegistryName(EndergeticExpansion.MOD_ID, "bolloom_particle");
	public static Block BOOF_BLOCK           = new BlockBoof(EEProperties.BOOF_BLOCK).setRegistryName(EndergeticExpansion.MOD_ID, "boof_block");
	public static Block BOOF_DISPENSED_BLOCK = new BlockDispensedBoof(EEProperties.BOOF_BLOCK.doesNotBlockMovement().hardnessAndResistance(-1, 3600000.0F)).setRegistryName(EndergeticExpansion.MOD_ID, "boof_dispensed_block");
	
	/*
	 * Vibra Jungle
	 */
	public static Block FRISBLOOM_STEM    = new BlockFrisbloomStem(EEProperties.FRISBLOOM_STEM).setRegistryName(EndergeticExpansion.MOD_ID, "frisbloom_stem");
	public static Block FRISBLOOM_BUD     = new BlockFrisbloomBud(EEProperties.FRISBLOOM_BUD.doesNotBlockMovement()).setRegistryName(EndergeticExpansion.MOD_ID, "frisbloom_seeds");
	
	/*
	 * Other
	 */
	public static Block EUMUS                = new BlockEumus(EEProperties.EUMUS).setRegistryName(EndergeticExpansion.MOD_ID, "eumus");
	public static Block EUMUS_BRICKS         = new Block(EEProperties.EUMUS_BRICKS).setRegistryName(EndergeticExpansion.MOD_ID, "eumus_bricks");
	public static Block EUMUS_BRICKS_CHISELED = new Block(EEProperties.EUMUS_BRICKS).setRegistryName(EndergeticExpansion.MOD_ID, "eumus_chiseled_bricks");
	public static Block EUMUS_BRICK_SLAB     = new SlabBlock(EEProperties.EUMUS_BRICKS).setRegistryName(EndergeticExpansion.MOD_ID, "eumus_brick_slab");
	public static Block EUMUS_BRICK_STAIRS   = new BlockStairsBase(EUMUS_BRICKS.getDefaultState(), EEProperties.EUMUS_BRICKS).setRegistryName(EndergeticExpansion.MOD_ID, "eumus_brick_stairs");
	public static Block POISE_BUSH_POT       = new FlowerPotBlock(POISE_GRASS, Properties.from(Blocks.POTTED_PINK_TULIP)).setRegistryName(EndergeticExpansion.MOD_ID, "potted_poise_bush");
	
	@SubscribeEvent
	public static void onRegisterBlocks(RegistryEvent.Register<Block> event) {
		final Block blocks[] = {
			FRISBLOOM_STEM, FRISBLOOM_BUD, CORROCK_BLOCK_OVERWORLD, CORROCK_BLOCK_NETHER, CORROCK_BLOCK_END, CORROCK_OVERWORLD, CORROCK_NETHER, CORROCK_END,
			CORROCK_CROWN_OVERWORLD_WALL, CORROCK_CROWN_OVERWORLD_STANDING, CORROCK_CROWN_NETHER_WALL, CORROCK_CROWN_NETHER_STANDING, CORROCK_CROWN_END_WALL, CORROCK_CROWN_END_STANDING,
			EUMUS, POISMOSS_EUMUS, POISE_GRASS_BLOCK, POISE_GRASS, POISE_GRASS_TALL, POISE_CLUSTER, POISE_LOG, POISE_LOG_GLOWING, POISE_LOG_STRIPPED, POISE_WOOD, POISE_WOOD_GLOWING, POISE_WOOD_STRIPPED,
			POISE_PLANKS, POISE_STAIRS, EUMUS_BRICK_STAIRS, POISE_SLAB,  EUMUS_BRICK_SLAB, POISE_DOOR, POISE_FENCE, POISE_FENCE_GATE, POISE_PRESSURE_PLATE, POISE_BUTTON, POISE_TRAPDOOR,
			BOLLOOM_BUD, PUFFBUG_HIVE, HIVE_HANGER, BOLLOOM_PARTICLE, BOOF_BLOCK, BOOF_DISPENSED_BLOCK, EUMUS_BRICKS, EUMUS_BRICKS_CHISELED, POISE_BUSH_POT
		};
		event.getRegistry().registerAll(blocks);
	}
	
	@SubscribeEvent
	public static void onRegisterItemBlocks(RegistryEvent.Register<Item> event) {
		final IForgeRegistry<Item> registry = event.getRegistry();
		
		registry.register(RegistryUtils.createSimpleBlockItem(FRISBLOOM_BUD, ItemGroup.MISC));
		registry.register(RegistryUtils.createSimpleBlockItem(CORROCK_BLOCK_OVERWORLD, ItemGroup.BUILDING_BLOCKS));
		registry.register(RegistryUtils.createSimpleBlockItem(CORROCK_BLOCK_NETHER, ItemGroup.BUILDING_BLOCKS));
		registry.register(RegistryUtils.createSimpleBlockItem(CORROCK_BLOCK_END, ItemGroup.BUILDING_BLOCKS));
		registry.register(RegistryUtils.createSimpleBlockItem(CORROCK_OVERWORLD, ItemGroup.DECORATIONS));
		registry.register(RegistryUtils.createSimpleBlockItem(CORROCK_NETHER, ItemGroup.DECORATIONS));
		registry.register(RegistryUtils.createSimpleBlockItem(CORROCK_END, ItemGroup.DECORATIONS));
		registry.register(RegistryUtils.createWallOrFloorItemUpsideDownAllowed(CORROCK_CROWN_OVERWORLD_STANDING, CORROCK_CROWN_OVERWORLD_WALL, ItemGroup.DECORATIONS));
		registry.register(RegistryUtils.createWallOrFloorItemUpsideDownAllowed(CORROCK_CROWN_NETHER_STANDING, CORROCK_CROWN_NETHER_WALL, ItemGroup.DECORATIONS));
		registry.register(RegistryUtils.createWallOrFloorItemUpsideDownAllowed(CORROCK_CROWN_END_STANDING, CORROCK_CROWN_END_WALL, ItemGroup.DECORATIONS));
		
		registry.register(RegistryUtils.createSimpleBlockItem(EUMUS, ItemGroup.BUILDING_BLOCKS));
		registry.register(RegistryUtils.createSimpleBlockItem(POISMOSS_EUMUS, ItemGroup.BUILDING_BLOCKS));
		registry.register(RegistryUtils.createSimpleBlockItem(POISE_GRASS_BLOCK, ItemGroup.BUILDING_BLOCKS));
		registry.register(RegistryUtils.createSimpleBlockItem(POISE_GRASS, ItemGroup.DECORATIONS));
		registry.register(RegistryUtils.createSimpleBlockItem(POISE_GRASS_TALL, ItemGroup.DECORATIONS));
		registry.register(RegistryUtils.createSimpleBlockItem(POISE_CLUSTER, ItemGroup.BUILDING_BLOCKS));
		registry.register(RegistryUtils.createSimpleBlockItem(POISE_LOG, ItemGroup.BUILDING_BLOCKS));
		registry.register(RegistryUtils.createSimpleBlockItem(POISE_LOG_GLOWING, ItemGroup.BUILDING_BLOCKS));
		registry.register(RegistryUtils.createSimpleBlockItem(POISE_LOG_STRIPPED, ItemGroup.BUILDING_BLOCKS));
		registry.register(RegistryUtils.createSimpleBlockItem(POISE_WOOD, ItemGroup.BUILDING_BLOCKS));
		registry.register(RegistryUtils.createSimpleBlockItem(POISE_WOOD_GLOWING, ItemGroup.BUILDING_BLOCKS));
		registry.register(RegistryUtils.createSimpleBlockItem(POISE_WOOD_STRIPPED, ItemGroup.BUILDING_BLOCKS));
		
		registry.register(RegistryUtils.createSimpleBlockItem(POISE_PLANKS, ItemGroup.BUILDING_BLOCKS));
		registry.register(RegistryUtils.createSimpleBlockItem(POISE_SLAB, ItemGroup.BUILDING_BLOCKS));
		registry.register(RegistryUtils.createSimpleBlockItem(POISE_STAIRS, ItemGroup.BUILDING_BLOCKS));
		registry.register(RegistryUtils.createSimpleBlockItem(POISE_FENCE, ItemGroup.DECORATIONS));
		registry.register(RegistryUtils.createSimpleBlockItem(POISE_FENCE_GATE, ItemGroup.REDSTONE));
		registry.register(RegistryUtils.createTallItemBlock(POISE_DOOR, ItemGroup.REDSTONE));
		registry.register(RegistryUtils.createSimpleBlockItem(POISE_PRESSURE_PLATE, ItemGroup.REDSTONE));
		registry.register(RegistryUtils.createSimpleBlockItem(POISE_BUTTON, ItemGroup.REDSTONE));
		registry.register(RegistryUtils.createSimpleBlockItem(POISE_TRAPDOOR, ItemGroup.REDSTONE));
		
		registry.register(RegistryUtils.createSimpleBlockItem(EUMUS_BRICKS, ItemGroup.BUILDING_BLOCKS));
		registry.register(RegistryUtils.createSimpleBlockItem(EUMUS_BRICKS_CHISELED, ItemGroup.BUILDING_BLOCKS));
		registry.register(RegistryUtils.createSimpleBlockItem(EUMUS_BRICK_SLAB, ItemGroup.BUILDING_BLOCKS));
		registry.register(RegistryUtils.createSimpleBlockItem(EUMUS_BRICK_STAIRS, ItemGroup.BUILDING_BLOCKS));
		
		Item.Properties bollom = (new Item.Properties()).group(ItemGroup.DECORATIONS).rarity(Rarity.RARE).setTEISR(() -> EETileEntityItemRenderer::new);
		registry.register(new BlockItem(BOLLOOM_BUD, bollom).setRegistryName(BOLLOOM_BUD.getRegistryName()));
		
		Item.Properties hive = (new Item.Properties()).group(ItemGroup.DECORATIONS).rarity(Rarity.RARE).setTEISR(() -> EETileEntityItemRenderer::new);
		registry.register(new BlockItem(PUFFBUG_HIVE, hive).setRegistryName(PUFFBUG_HIVE.getRegistryName()));
		
		registry.register(RegistryUtils.createSimpleBlockItem(BOOF_BLOCK, ItemGroup.DECORATIONS));
	}
	
}
