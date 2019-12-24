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
import net.minecraft.block.WallBlock;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Rarity;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

@SuppressWarnings("deprecation")
@Mod.EventBusSubscriber(modid = EndergeticExpansion.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EEBlocks {
	public static final DeferredRegister<Block> BLOCKS = new DeferredRegister<>(ForgeRegistries.BLOCKS, EndergeticExpansion.MOD_ID);
	
	public static final RegistryObject<Block> CORROCK_BLOCK_OVERWORLD = RegistryUtils.createBlock("corrock_overworld_block", () -> new BlockCorrockBlock(EEProperties.CORROCK_BASE(MaterialColor.BROWN_TERRACOTTA, true)), ItemGroup.BUILDING_BLOCKS);
	public static final RegistryObject<Block> CORROCK_BLOCK_NETHER    = RegistryUtils.createBlock("corrock_nether_block", () -> new BlockCorrockBlock(EEProperties.CORROCK_BASE(MaterialColor.RED_TERRACOTTA, true)), ItemGroup.BUILDING_BLOCKS);
	public static final RegistryObject<Block> CORROCK_BLOCK_END       = RegistryUtils.createBlock("corrock_end_block", () -> new BlockCorrockBlock(EEProperties.CORROCK_BASE(MaterialColor.PURPLE_TERRACOTTA, true)), ItemGroup.BUILDING_BLOCKS);
	public static final RegistryObject<Block> CORROCK_OVERWORLD       = RegistryUtils.createBlock("corrock_overworld", () -> new BlockCorrock(EEProperties.CORROCK_BASE(MaterialColor.BROWN_TERRACOTTA, false)), ItemGroup.DECORATIONS);
	public static final RegistryObject<Block> CORROCK_NETHER          = RegistryUtils.createBlock("corrock_nether", () -> new BlockCorrock(EEProperties.CORROCK_BASE(MaterialColor.RED_TERRACOTTA, false)), ItemGroup.DECORATIONS);
	public static final RegistryObject<Block> CORROCK_END             = RegistryUtils.createBlock("corrock_end", () -> new BlockCorrock(EEProperties.CORROCK_BASE(MaterialColor.PURPLE_TERRACOTTA, false)), ItemGroup.DECORATIONS);
	public static final RegistryObject<BlockCorrockCrownWall> CORROCK_CROWN_OVERWORLD_WALL     = RegistryUtils.createBlockNoItem("corrock_crown_wall_overworld", () -> new BlockCorrockCrownWall(EEProperties.CORROCK_BASE_GLOWING(MaterialColor.BROWN)));
	public static final RegistryObject<BlockCorrockCrown> CORROCK_CROWN_OVERWORLD_STANDING     = RegistryUtils.createCorrockStandingBlock("corrock_crown_standing_overworld", () -> new BlockCorrockCrownStanding(EEProperties.CORROCK_BASE_GLOWING(MaterialColor.BROWN)), () -> CORROCK_CROWN_OVERWORLD_WALL.get(), ItemGroup.DECORATIONS);
	public static final RegistryObject<BlockCorrockCrownWall> CORROCK_CROWN_NETHER_WALL        = RegistryUtils.createBlockNoItem("corrock_crown_wall_nether", () -> new BlockCorrockCrownWall(EEProperties.CORROCK_BASE_GLOWING(MaterialColor.RED)));
	public static final RegistryObject<BlockCorrockCrown> CORROCK_CROWN_NETHER_STANDING        = RegistryUtils.createCorrockStandingBlock("corrock_crown_standing_nether", () -> new BlockCorrockCrownStanding(EEProperties.CORROCK_BASE_GLOWING(MaterialColor.RED)), () -> CORROCK_CROWN_NETHER_WALL.get(), ItemGroup.DECORATIONS);
	public static final RegistryObject<BlockCorrockCrownWall> CORROCK_CROWN_END_WALL           = RegistryUtils.createBlockNoItem("corrock_crown_wall_end", () -> new BlockCorrockCrownWall(EEProperties.CORROCK_BASE_GLOWING(MaterialColor.PURPLE)));
	public static final RegistryObject<BlockCorrockCrown> CORROCK_CROWN_END_STANDING           = RegistryUtils.createCorrockStandingBlock("corrock_crown_standing_end", () -> new BlockCorrockCrownStanding(EEProperties.CORROCK_BASE_GLOWING(MaterialColor.PURPLE)), () -> CORROCK_CROWN_END_WALL.get(), ItemGroup.DECORATIONS);
	
	/*
	 * Poise Forest
	 */
	public static final RegistryObject<BlockPoiseEumusGrass> POISMOSS_EUMUS                    = RegistryUtils.createBlock("poismoss_eumus", () -> new BlockPoiseEumusGrass(EEProperties.POISMOSS_EUMUS), ItemGroup.BUILDING_BLOCKS);
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
	public static Block BOOF_BLOCK_DISPENSED = new BlockDispensedBoof(EEProperties.BOOF_BLOCK.doesNotBlockMovement().hardnessAndResistance(-1, 3600000.0F)).setRegistryName(EndergeticExpansion.MOD_ID, "boof_dispensed_block");
	
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
	public static Block EUMUS_BRICK_WALL     = new WallBlock(EEProperties.EUMUS_BRICKS).setRegistryName(EndergeticExpansion.MOD_ID, "eumus_brick_wall");
	public static Block POISE_BUSH_POT       = new FlowerPotBlock(POISE_GRASS, Properties.from(Blocks.POTTED_PINK_TULIP)).setRegistryName(EndergeticExpansion.MOD_ID, "potted_poise_bush"); //Why... is this deprecated
	public static Block MYSTICAL_OBSIDIAN    = new Block(EEProperties.MYSTICAL_OBSIDIAN).setRegistryName(EndergeticExpansion.MOD_ID, "mystical_obsidian");
	public static Block MYSTICAL_OBSIDIAN_WALL = new WallBlock(EEProperties.MYSTICAL_OBSIDIAN).setRegistryName(EndergeticExpansion.MOD_ID, "mystical_obsidian_wall");
	public static Block MYSTICAL_OBSIDIAN_RUNE = new BlockRotatable(EEProperties.MYSTICAL_OBSIDIAN).setRegistryName(EndergeticExpansion.MOD_ID, "mystical_obsidian_rune");
	public static Block MYSTICAL_OBSIDIAN_ACTIVATION_RUNE = new BlockRotatable(EEProperties.MYSTICAL_OBSIDIAN).setRegistryName(EndergeticExpansion.MOD_ID, "mystical_obsidian_activation_rune");
	public static Block MYSTICAL_OBSIDIAN_ACTIVATION_RUNE_ACTIVE = new BlockRotatable(EEProperties.MYSTICAL_OBSIDIAN.lightValue(5)).setRegistryName(EndergeticExpansion.MOD_ID, "mystical_obsidian_activation_rune_active");
	public static Block ACIDIAN_LANTERN      = new BlockAcidianLantern(EEProperties.ACIDIAN_LANTERN).setRegistryName(EndergeticExpansion.MOD_ID, "acidian_lantern");
	public static Block CRYSTAL_HOLDER       = new Block(EEProperties.MYSTICAL_OBSIDIAN).setRegistryName(EndergeticExpansion.MOD_ID, "crystal_holder");
	public static Block ENDER_FIRE           = new BlockEnderFire(Properties.from(Blocks.FIRE)).setRegistryName(EndergeticExpansion.MOD_ID, "ender_fire");
	
	/*
	 * Compatibility
	 */
	public static final RegistryObject<Block> POISE_VERTICAL_PLANKS = RegistryUtils.createCompatBlock("poise_vertical_planks", "quark", () -> new BlockPoisePlanks(EEProperties.POISE_WOOD), ItemGroup.BUILDING_BLOCKS);
	public static final RegistryObject<Block> POISE_VERTICAL_SLAB   = RegistryUtils.createCompatBlock("poise_vertical_slab", "quark", () -> new BlockVerticalSlab(EEProperties.POISE_WOOD), ItemGroup.BUILDING_BLOCKS);
	public static final RegistryObject<Block> POISE_BOOKSHELF       = RegistryUtils.createCompatBlock("poise_bookshelf", "quark", () -> new BlockEEBookshelf(Properties.from(Blocks.BOOKSHELF)), ItemGroup.BUILDING_BLOCKS);
	public static final RegistryObject<Block> POISE_LADDER          = RegistryUtils.createCompatBlock("poise_ladder", "quark", () -> new BlockEELadder(Properties.from(Blocks.LADDER)), ItemGroup.DECORATIONS);
	
	@SubscribeEvent
	public static void onRegisterBlocks(RegistryEvent.Register<Block> event) {
		final Block blocks[] = {
			EUMUS, POISE_GRASS_BLOCK, POISE_GRASS, POISE_GRASS_TALL, POISE_CLUSTER, POISE_LOG, POISE_LOG_GLOWING, POISE_LOG_STRIPPED, POISE_WOOD, POISE_WOOD_GLOWING, POISE_WOOD_STRIPPED,
			POISE_PLANKS, POISE_STAIRS, EUMUS_BRICK_STAIRS, POISE_SLAB,  EUMUS_BRICK_SLAB, POISE_DOOR, POISE_FENCE, POISE_FENCE_GATE, POISE_PRESSURE_PLATE, POISE_BUTTON, POISE_TRAPDOOR,
			BOLLOOM_BUD, PUFFBUG_HIVE, HIVE_HANGER, BOLLOOM_PARTICLE, BOOF_BLOCK, BOOF_BLOCK_DISPENSED, EUMUS_BRICKS, EUMUS_BRICKS_CHISELED, EUMUS_BRICK_WALL, POISE_BUSH_POT,
			MYSTICAL_OBSIDIAN, MYSTICAL_OBSIDIAN_WALL, MYSTICAL_OBSIDIAN_RUNE, MYSTICAL_OBSIDIAN_ACTIVATION_RUNE, MYSTICAL_OBSIDIAN_ACTIVATION_RUNE_ACTIVE,
			ACIDIAN_LANTERN, CRYSTAL_HOLDER, ENDER_FIRE
		};
		event.getRegistry().registerAll(blocks);
	}
	
	@SubscribeEvent
	public static void onRegisterItemBlocks(RegistryEvent.Register<Item> event) {
		final IForgeRegistry<Item> registry = event.getRegistry();
		
		//registry.register(RegistryUtils.createSimpleBlockItem(FRISBLOOM_BUD, ItemGroup.MISC));
		
		registry.register(RegistryUtils.createSimpleBlockItem(POISE_GRASS_BLOCK, ItemGroup.BUILDING_BLOCKS));
		registry.register(RegistryUtils.createSimpleBlockItem(EUMUS, ItemGroup.BUILDING_BLOCKS));
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
		
		registry.register(RegistryUtils.createNoTabBlockItem(MYSTICAL_OBSIDIAN_WALL));
		registry.register(RegistryUtils.createSimpleBlockItem(EUMUS_BRICK_WALL, ItemGroup.DECORATIONS));
		
		registry.register(RegistryUtils.createNoTabBlockItem(MYSTICAL_OBSIDIAN));
		
		registry.register(RegistryUtils.createNoTabBlockItem(MYSTICAL_OBSIDIAN_RUNE));
		registry.register(RegistryUtils.createNoTabBlockItem(MYSTICAL_OBSIDIAN_ACTIVATION_RUNE));
		registry.register(RegistryUtils.createNoTabBlockItem(MYSTICAL_OBSIDIAN_ACTIVATION_RUNE_ACTIVE));
		registry.register(RegistryUtils.createSimpleBlockItem(ACIDIAN_LANTERN, ItemGroup.DECORATIONS));
		registry.register(RegistryUtils.createNoTabBlockItem(CRYSTAL_HOLDER));
		
		Item.Properties bollom = (new Item.Properties()).group(ItemGroup.DECORATIONS).rarity(Rarity.RARE).setTEISR(() -> EETileEntityItemRenderer::new);
		registry.register(new BlockItem(BOLLOOM_BUD, bollom).setRegistryName(BOLLOOM_BUD.getRegistryName()));
		
		Item.Properties hive = (new Item.Properties()).group(ItemGroup.DECORATIONS).rarity(Rarity.RARE).setTEISR(() -> EETileEntityItemRenderer::new);
		registry.register(new BlockItem(PUFFBUG_HIVE, hive).setRegistryName(PUFFBUG_HIVE.getRegistryName()));
		
		registry.register(RegistryUtils.createSimpleBlockItem(BOOF_BLOCK, ItemGroup.DECORATIONS));
	}
	
}
