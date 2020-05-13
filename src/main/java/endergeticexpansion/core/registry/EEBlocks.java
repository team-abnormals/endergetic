package endergeticexpansion.core.registry;

import java.util.concurrent.Callable;

import com.mojang.datafixers.util.Pair;
import com.teamabnormals.abnormals_core.common.blocks.sign.AbnormalsStandingSignBlock;
import com.teamabnormals.abnormals_core.common.blocks.sign.AbnormalsWallSignBlock;
import com.teamabnormals.abnormals_core.common.blocks.wood.AbnormalsLogBlock;
import com.teamabnormals.abnormals_core.common.blocks.wood.AbnormalsWoodButtonBlock;
import com.teamabnormals.abnormals_core.common.blocks.wood.PlanksBlock;
import com.teamabnormals.abnormals_core.common.blocks.wood.StrippedLogBlock;
import com.teamabnormals.abnormals_core.common.blocks.wood.StrippedWoodBlock;
import com.teamabnormals.abnormals_core.common.blocks.wood.WoodBlock;
import com.teamabnormals.abnormals_core.common.blocks.wood.WoodDoorBlock;
import com.teamabnormals.abnormals_core.common.blocks.wood.WoodFenceBlock;
import com.teamabnormals.abnormals_core.common.blocks.wood.WoodFenceGateBlock;
import com.teamabnormals.abnormals_core.common.blocks.wood.WoodPressurePlateBlock;
import com.teamabnormals.abnormals_core.common.blocks.wood.WoodSlabBlock;
import com.teamabnormals.abnormals_core.common.blocks.wood.WoodStairsBlock;
import com.teamabnormals.abnormals_core.common.blocks.wood.WoodTrapDoorBlock;

import endergeticexpansion.client.render.item.EETileEntityItemRenderer;
import endergeticexpansion.common.EEProperties;
import endergeticexpansion.common.blocks.BlockAcidianLantern;
import endergeticexpansion.common.blocks.BlockCorrock;
import endergeticexpansion.common.blocks.BlockCorrockBlock;
import endergeticexpansion.common.blocks.BlockCorrockCrown;
import endergeticexpansion.common.blocks.BlockCorrockCrownStanding;
import endergeticexpansion.common.blocks.BlockCorrockCrownWall;
import endergeticexpansion.common.blocks.BlockEEBookshelf;
import endergeticexpansion.common.blocks.BlockEELadder;
import endergeticexpansion.common.blocks.BlockEndStoneCover;
import endergeticexpansion.common.blocks.BlockEnderFire;
import endergeticexpansion.common.blocks.BlockEumus;
import endergeticexpansion.common.blocks.BlockFrisbloomBud;
import endergeticexpansion.common.blocks.BlockFrisbloomStem;
import endergeticexpansion.common.blocks.BlockRotatable;
import endergeticexpansion.common.blocks.BlockStairsBase;
import endergeticexpansion.common.blocks.BlockVerticalSlab;
import endergeticexpansion.common.blocks.poise.BlockBolloomBud;
import endergeticexpansion.common.blocks.poise.BlockGlowingPoiseLog;
import endergeticexpansion.common.blocks.poise.BlockGlowingPoiseWood;
import endergeticexpansion.common.blocks.poise.BlockPoiseCluster;
import endergeticexpansion.common.blocks.poise.BlockPoiseGrassPlant;
import endergeticexpansion.common.blocks.poise.BlockPoiseGrassPlantTall;
import endergeticexpansion.common.blocks.poise.BlockPoismoss;
import endergeticexpansion.common.blocks.poise.BlockPoismossEumus;
import endergeticexpansion.common.blocks.poise.boof.BlockBoof;
import endergeticexpansion.common.blocks.poise.boof.BlockDispensedBoof;
import endergeticexpansion.common.blocks.poise.hive.BlockHiveHanger;
import endergeticexpansion.common.blocks.poise.hive.BlockPuffBugHive;
import endergeticexpansion.common.tileentities.TileEntityBolloomBud;
import endergeticexpansion.common.tileentities.TileEntityPuffBugHive;
import endergeticexpansion.core.EndergeticExpansion;
import endergeticexpansion.core.registry.util.EndergeticRegistryHelper;
import net.minecraft.block.Block;
import net.minecraft.block.Block.Properties;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowerPotBlock;
import net.minecraft.block.PressurePlateBlock;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.WallBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.ItemGroup;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;

@SuppressWarnings("deprecation")
@Mod.EventBusSubscriber(modid = EndergeticExpansion.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EEBlocks {
	public static final EndergeticRegistryHelper HELPER = EndergeticExpansion.REGISTRY_HELPER;
	
	public static final RegistryObject<Block> CORROCK_OVERWORLD_BLOCK           = HELPER.createBlock("corrock_overworld_block", () -> new BlockCorrockBlock(EEProperties.CORROCK_BASE(MaterialColor.BROWN_TERRACOTTA, true), false), ItemGroup.BUILDING_BLOCKS);
	public static final RegistryObject<Block> PETRIFIED_CORROCK_OVERWORLD_BLOCK = HELPER.createBlock("petrified_corrock_overworld_block", () -> new BlockCorrockBlock(EEProperties.CORROCK_BASE(MaterialColor.BROWN_TERRACOTTA, true), true), null);
	public static final RegistryObject<Block> CORROCK_NETHER_BLOCK              = HELPER.createBlock("corrock_nether_block", () -> new BlockCorrockBlock(EEProperties.CORROCK_BASE(MaterialColor.RED_TERRACOTTA, true), false), ItemGroup.BUILDING_BLOCKS);
	public static final RegistryObject<Block> PETRIFIED_CORROCK_NETHER_BLOCK    = HELPER.createBlock("petrified_corrock_nether_block", () -> new BlockCorrockBlock(EEProperties.CORROCK_BASE(MaterialColor.RED_TERRACOTTA, true), true), null);
	public static final RegistryObject<Block> CORROCK_END_BLOCK                 = HELPER.createBlock("corrock_end_block", () -> new BlockCorrockBlock(EEProperties.CORROCK_BASE(MaterialColor.PURPLE_TERRACOTTA, true), false), ItemGroup.BUILDING_BLOCKS);
	public static final RegistryObject<Block> PETRIFIED_CORROCK_END_BLOCK       = HELPER.createBlock("petrified_corrock_end_block", () -> new BlockCorrockBlock(EEProperties.CORROCK_BASE(MaterialColor.PURPLE_TERRACOTTA, true), true), null);
	public static final RegistryObject<Block> CORROCK_OVERWORLD                 = HELPER.createBlock("corrock_overworld", () -> new BlockCorrock(EEProperties.CORROCK_BASE(MaterialColor.BROWN_TERRACOTTA, false), false), ItemGroup.DECORATIONS);
	public static final RegistryObject<Block> PETRIFIED_CORROCK_OVERWORLD       = HELPER.createBlock("petrified_corrock_overworld", () -> new BlockCorrock(EEProperties.CORROCK_BASE(MaterialColor.BROWN_TERRACOTTA, false), true), null);
	public static final RegistryObject<Block> CORROCK_NETHER                    = HELPER.createBlock("corrock_nether", () -> new BlockCorrock(EEProperties.CORROCK_BASE(MaterialColor.RED_TERRACOTTA, false), false), ItemGroup.DECORATIONS);
	public static final RegistryObject<Block> PETRIFIED_CORROCK_NETHER          = HELPER.createBlock("petrified_corrock_nether", () -> new BlockCorrock(EEProperties.CORROCK_BASE(MaterialColor.RED_TERRACOTTA, false), true), null);
	public static final RegistryObject<Block> CORROCK_END                       = HELPER.createBlock("corrock_end", () -> new BlockCorrock(EEProperties.CORROCK_BASE(MaterialColor.PURPLE_TERRACOTTA, false), false), ItemGroup.DECORATIONS);
	public static final RegistryObject<Block> PETRIFIED_CORROCK_END             = HELPER.createBlock("petrified_corrock_end", () -> new BlockCorrock(EEProperties.CORROCK_BASE(MaterialColor.PURPLE_TERRACOTTA, false), true), null);
	public static final RegistryObject<BlockCorrockCrownWall> CORROCK_CROWN_OVERWORLD_WALL           = HELPER.createBlockNoItem("corrock_crown_wall_overworld", () -> new BlockCorrockCrownWall(EEProperties.CORROCK_BASE_GLOWING(MaterialColor.BROWN), false));
	public static final RegistryObject<BlockCorrockCrownWall> PETRIFIED_CORROCK_CROWN_OVERWORLD_WALL = HELPER.createBlockNoItem("petrified_corrock_crown_wall_overworld", () -> new BlockCorrockCrownWall(EEProperties.CORROCK_BASE_GLOWING(MaterialColor.BROWN), true));
	public static final RegistryObject<BlockCorrockCrown> CORROCK_CROWN_OVERWORLD_STANDING           = HELPER.createCorrockStandingBlock("corrock_crown_standing_overworld", () -> new BlockCorrockCrownStanding(EEProperties.CORROCK_BASE_GLOWING(MaterialColor.BROWN), false), () -> CORROCK_CROWN_OVERWORLD_WALL.get(), ItemGroup.DECORATIONS);
	public static final RegistryObject<BlockCorrockCrown> PETRIFIED_CORROCK_CROWN_OVERWORLD_STANDING = HELPER.createCorrockStandingBlock("petrified_corrock_crown_standing_overworld", () -> new BlockCorrockCrownStanding(EEProperties.CORROCK_BASE_GLOWING(MaterialColor.BROWN), true), () -> PETRIFIED_CORROCK_CROWN_OVERWORLD_WALL.get(), null);
	public static final RegistryObject<BlockCorrockCrownWall> CORROCK_CROWN_NETHER_WALL              = HELPER.createBlockNoItem("corrock_crown_wall_nether", () -> new BlockCorrockCrownWall(EEProperties.CORROCK_BASE_GLOWING(MaterialColor.RED), false));
	public static final RegistryObject<BlockCorrockCrownWall> PETRIFIED_CORROCK_CROWN_NETHER_WALL    = HELPER.createBlockNoItem("petrified_corrock_crown_wall_nether", () -> new BlockCorrockCrownWall(EEProperties.CORROCK_BASE_GLOWING(MaterialColor.RED), true));
	public static final RegistryObject<BlockCorrockCrown> CORROCK_CROWN_NETHER_STANDING              = HELPER.createCorrockStandingBlock("corrock_crown_standing_nether", () -> new BlockCorrockCrownStanding(EEProperties.CORROCK_BASE_GLOWING(MaterialColor.RED), false), () -> CORROCK_CROWN_NETHER_WALL.get(), ItemGroup.DECORATIONS);
	public static final RegistryObject<BlockCorrockCrown> PETRIFIED_CORROCK_CROWN_NETHER_STANDING    = HELPER.createCorrockStandingBlock("petrified_corrock_crown_standing_nether", () -> new BlockCorrockCrownStanding(EEProperties.CORROCK_BASE_GLOWING(MaterialColor.RED), true), () -> PETRIFIED_CORROCK_CROWN_NETHER_WALL.get(), null);
	public static final RegistryObject<BlockCorrockCrownWall> CORROCK_CROWN_END_WALL                 = HELPER.createBlockNoItem("corrock_crown_wall_end", () -> new BlockCorrockCrownWall(EEProperties.CORROCK_BASE_GLOWING(MaterialColor.PURPLE), false));
	public static final RegistryObject<BlockCorrockCrownWall> PETRIFIED_CORROCK_CROWN_END_WALL       = HELPER.createBlockNoItem("petrified_corrock_crown_wall_end", () -> new BlockCorrockCrownWall(EEProperties.CORROCK_BASE_GLOWING(MaterialColor.PURPLE), true));
	public static final RegistryObject<BlockCorrockCrown> CORROCK_CROWN_END_STANDING                 = HELPER.createCorrockStandingBlock("corrock_crown_standing_end", () -> new BlockCorrockCrownStanding(EEProperties.CORROCK_BASE_GLOWING(MaterialColor.PURPLE), false), () -> CORROCK_CROWN_END_WALL.get(), ItemGroup.DECORATIONS);
	public static final RegistryObject<BlockCorrockCrown> PETRIFIED_CORROCK_CROWN_END_STANDING       = HELPER.createCorrockStandingBlock("petrified_corrock_crown_standing_end", () -> new BlockCorrockCrownStanding(EEProperties.CORROCK_BASE_GLOWING(MaterialColor.PURPLE), true), () -> PETRIFIED_CORROCK_CROWN_END_WALL.get(), null);
	public static final RegistryObject<Block> ENDSTONE_COVER                                         = HELPER.createBlockNoItem("endstone_cover", () -> new BlockEndStoneCover(Block.Properties.create(Material.TALL_PLANTS, MaterialColor.PURPLE_TERRACOTTA).hardnessAndResistance(1.5F, 6.0F).doesNotBlockMovement()));
	
	/*
	 * Poise Forest
	 */
	public static final RegistryObject<BlockPoismossEumus> POISMOSS_EUMUS         = HELPER.createBlock("poismoss_eumus", () -> new BlockPoismossEumus(EEProperties.POISMOSS_EUMUS), ItemGroup.BUILDING_BLOCKS);
	public static final RegistryObject<BlockPoismoss> POISMOSS                    = HELPER.createBlock("poise_grass_block", () -> new BlockPoismoss(EEProperties.POISE_GRASS(false)), ItemGroup.BUILDING_BLOCKS);
	public static final RegistryObject<BlockPoiseGrassPlant> POISE_GRASS          = HELPER.createBlock("poise_grass", () -> new BlockPoiseGrassPlant(EEProperties.POISE_GRASS(true)), ItemGroup.DECORATIONS);
	public static final RegistryObject<BlockPoiseGrassPlantTall> POISE_GRASS_TALL = HELPER.createBlock("poise_grass_tall", () -> new BlockPoiseGrassPlantTall(EEProperties.POISE_GRASS(true)), ItemGroup.DECORATIONS);
	public static final RegistryObject<BlockPoiseCluster> POISE_CLUSTER           = HELPER.createBlock("poise_cluster", () -> new BlockPoiseCluster(EEProperties.POISE_CLUSTER.tickRandomly()), ItemGroup.BUILDING_BLOCKS);
	public static final RegistryObject<StrippedLogBlock> POISE_LOG_STRIPPED       = HELPER.createBlock("poise_stem_stripped", () -> new StrippedLogBlock(EEProperties.POISE_WOOD), ItemGroup.BUILDING_BLOCKS);
	public static final RegistryObject<StrippedWoodBlock> POISE_WOOD_STRIPPED     = HELPER.createBlock("poise_wood_stripped", () -> new StrippedWoodBlock(EEProperties.POISE_WOOD), ItemGroup.BUILDING_BLOCKS);
	public static final RegistryObject<AbnormalsLogBlock> POISE_LOG               = HELPER.createBlock("poise_stem", () -> new AbnormalsLogBlock(() -> EEBlocks.POISE_LOG_STRIPPED.get(), MaterialColor.PURPLE_TERRACOTTA, EEProperties.POISE_WOOD), ItemGroup.BUILDING_BLOCKS);
	public static final RegistryObject<WoodBlock> POISE_WOOD                      = HELPER.createBlock("poise_wood", () -> new WoodBlock(() -> EEBlocks.POISE_WOOD_STRIPPED.get(), EEProperties.POISE_WOOD), ItemGroup.BUILDING_BLOCKS);
	public static final RegistryObject<BlockGlowingPoiseLog> POISE_LOG_GLOWING    = HELPER.createBlock("poise_stem_glowing", () -> new BlockGlowingPoiseLog(EEProperties.POISE_LOG_GLOWING), ItemGroup.BUILDING_BLOCKS);
	public static final RegistryObject<BlockGlowingPoiseWood> POISE_WOOD_GLOWING  = HELPER.createBlock("poise_wood_glowing", () -> new BlockGlowingPoiseWood(EEProperties.POISE_LOG_GLOWING), ItemGroup.BUILDING_BLOCKS);
	public static final RegistryObject<Block> POISE_PLANKS                        = HELPER.createBlock("poise_planks", () -> new PlanksBlock(EEProperties.POISE_WOOD), ItemGroup.BUILDING_BLOCKS);
	public static final RegistryObject<WoodDoorBlock> POISE_DOOR                  = HELPER.createTallBlock("poise_door", () -> new WoodDoorBlock(EEProperties.POISE_WOOD_NOT_SOLID), ItemGroup.REDSTONE);
	public static final RegistryObject<WoodSlabBlock> POISE_SLAB                  = HELPER.createBlock("poise_slab", () -> new WoodSlabBlock(EEProperties.POISE_WOOD), ItemGroup.BUILDING_BLOCKS);
	public static final RegistryObject<WoodStairsBlock> POISE_STAIRS              = HELPER.createBlock("poise_stairs", () -> new WoodStairsBlock(POISE_PLANKS.get().getDefaultState(), EEProperties.POISE_WOOD), ItemGroup.BUILDING_BLOCKS);
	public static final RegistryObject<WoodFenceBlock> POISE_FENCE                = HELPER.createBlock("poise_fence", () -> new WoodFenceBlock(EEProperties.POISE_WOOD), ItemGroup.DECORATIONS);
	public static final RegistryObject<WoodFenceGateBlock> POISE_FENCE_GATE       = HELPER.createBlock("poise_fence_gate", () -> new WoodFenceGateBlock(EEProperties.POISE_WOOD), ItemGroup.REDSTONE);
	public static final RegistryObject<WoodPressurePlateBlock> POISE_PRESSURE_PLATE = HELPER.createBlock("poise_pressure_plate", () -> new WoodPressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, EEProperties.POISE_WOOD), ItemGroup.REDSTONE);
	public static final RegistryObject<Block> POISE_BUTTON                        = HELPER.createBlock("poise_button", () -> new AbnormalsWoodButtonBlock(EEProperties.POISE_WOOD_OTHER(false, true)), ItemGroup.REDSTONE);
	public static final RegistryObject<Block> POISE_TRAPDOOR                      = HELPER.createBlock("poise_trapdoor", () -> new WoodTrapDoorBlock(EEProperties.POISE_WOOD_NOT_SOLID), ItemGroup.REDSTONE);
	public static final Pair<RegistryObject<AbnormalsStandingSignBlock>, RegistryObject<AbnormalsWallSignBlock>> POISE_SIGN = HELPER.createSignBlock("poise", MaterialColor.PURPLE_TERRACOTTA);
	public static final RegistryObject<BlockBolloomBud> BOLLOOM_BUD               = HELPER.createBlockWithISTER("bolloom_bud", () -> new BlockBolloomBud(EEProperties.POISE_WOOD_OTHER(true, false)), () -> bolloomBudISTER(), ItemGroup.DECORATIONS);
	public static final RegistryObject<BlockPuffBugHive> PUFFBUG_HIVE             = HELPER.createBlockWithISTER("puffbug_hive", () -> new BlockPuffBugHive(EEProperties.PUFFBUG_HIVE(true)), () -> puffbugHiveISTER(), ItemGroup.DECORATIONS);
	public static final RegistryObject<BlockHiveHanger> HIVE_HANGER               = HELPER.createBlockNoItem("hive_hanger", () -> new BlockHiveHanger(EEProperties.PUFFBUG_HIVE(false)));
	public static final RegistryObject<Block> BOLLOOM_PARTICLE                    = HELPER.createBlockNoItem("bolloom_particle", () -> new Block(EEProperties.POISE_WOOD_OTHER(false, true)));
	public static final RegistryObject<BlockBoof> BOOF_BLOCK                      = HELPER.createBlock("boof_block", () -> new BlockBoof(EEProperties.BOOF_BLOCK), ItemGroup.DECORATIONS);
	public static final RegistryObject<BlockDispensedBoof> BOOF_BLOCK_DISPENSED   = HELPER.createBlockNoItem("boof_dispensed_block", () -> new BlockDispensedBoof(EEProperties.BOOF_BLOCK.doesNotBlockMovement().notSolid().hardnessAndResistance(-1, 3600000.0F)));
	
	/*
	 * Vibra Jungle(Unused)
	 */
	public static Block FRISBLOOM_STEM    = new BlockFrisbloomStem(EEProperties.FRISBLOOM_STEM).setRegistryName(EndergeticExpansion.MOD_ID, "frisbloom_stem");
	public static Block FRISBLOOM_BUD     = new BlockFrisbloomBud(EEProperties.FRISBLOOM_BUD.doesNotBlockMovement()).setRegistryName(EndergeticExpansion.MOD_ID, "frisbloom_seeds");
	
	/*
	 * Misc
	 */
	public static final RegistryObject<BlockEumus> EUMUS                         = HELPER.createBlock("eumus", () -> new BlockEumus(EEProperties.EUMUS), ItemGroup.BUILDING_BLOCKS);
	public static final RegistryObject<Block> EUMUS_BRICKS                       = HELPER.createBlock("eumus_bricks", () -> new Block(EEProperties.EUMUS_BRICKS), ItemGroup.BUILDING_BLOCKS);
	public static final RegistryObject<Block> EUMUS_BRICKS_CHISELED              = HELPER.createBlock("eumus_chiseled_bricks", () -> new Block(EEProperties.EUMUS_BRICKS), ItemGroup.BUILDING_BLOCKS);
	public static final RegistryObject<SlabBlock> EUMUS_BRICK_SLAB               = HELPER.createBlock("eumus_brick_slab", () -> new SlabBlock(EEProperties.EUMUS_BRICKS), ItemGroup.BUILDING_BLOCKS);
	public static final RegistryObject<BlockStairsBase> EUMUS_BRICK_STAIRS       = HELPER.createBlock("eumus_brick_stairs", () -> new BlockStairsBase(() -> EUMUS_BRICKS.get().getDefaultState(), EEProperties.EUMUS_BRICKS), ItemGroup.BUILDING_BLOCKS);
	public static final RegistryObject<WallBlock> EUMUS_BRICK_WALL               = HELPER.createBlock("eumus_brick_wall", () -> new WallBlock(EEProperties.EUMUS_BRICKS), ItemGroup.DECORATIONS);
	public static final RegistryObject<FlowerPotBlock> POISE_BUSH_POT            = HELPER.createBlockNoItem("potted_poise_bush", () -> new FlowerPotBlock(POISE_GRASS.get(), Properties.from(Blocks.POTTED_PINK_TULIP)));
	public static final RegistryObject<Block> MYSTICAL_OBSIDIAN                  = HELPER.createBlock("mystical_obsidian", () -> new Block(EEProperties.MYSTICAL_OBSIDIAN), null);
	public static final RegistryObject<WallBlock> MYSTICAL_OBSIDIAN_WALL         = HELPER.createBlock("mystical_obsidian_wall", () -> new WallBlock(EEProperties.MYSTICAL_OBSIDIAN), null);
	public static final RegistryObject<BlockRotatable> MYSTICAL_OBSIDIAN_RUNE    = HELPER.createBlock("mystical_obsidian_rune", () -> new BlockRotatable(EEProperties.MYSTICAL_OBSIDIAN), null);
	public static final RegistryObject<BlockRotatable> MYSTICAL_OBSIDIAN_ACTIVATION_RUNE = HELPER.createBlock("mystical_obsidian_activation_rune", () -> new BlockRotatable(EEProperties.MYSTICAL_OBSIDIAN), null);
	public static final RegistryObject<BlockRotatable> MYSTICAL_OBSIDIAN_ACTIVATION_RUNE_ACTIVE = HELPER.createBlock("mystical_obsidian_activation_rune_active", () -> new BlockRotatable(EEProperties.MYSTICAL_OBSIDIAN.lightValue(5)), null);
	public static final RegistryObject<BlockAcidianLantern> ACIDIAN_LANTERN      = HELPER.createBlock("acidian_lantern", () ->  new BlockAcidianLantern(EEProperties.ACIDIAN_LANTERN), ItemGroup.DECORATIONS);
	public static final RegistryObject<Block> CRYSTAL_HOLDER                     = HELPER.createBlock("crystal_holder", () -> new Block(EEProperties.MYSTICAL_OBSIDIAN), null);
	public static Block ENDER_FIRE           = new BlockEnderFire(Properties.from(Blocks.FIRE)).setRegistryName(EndergeticExpansion.MOD_ID, "ender_fire");
	
	/*
	 * Compatibility
	 */
	public static final RegistryObject<Block> POISE_VERTICAL_PLANKS = HELPER.createCompatBlock("quark", "poise_vertical_planks", () -> new Block(EEProperties.POISE_WOOD), ItemGroup.BUILDING_BLOCKS);
	public static final RegistryObject<Block> POISE_VERTICAL_SLAB   = HELPER.createCompatBlock("quark", "poise_vertical_slab", () -> new BlockVerticalSlab(EEProperties.POISE_WOOD), ItemGroup.BUILDING_BLOCKS);
	public static final RegistryObject<Block> POISE_BOOKSHELF       = HELPER.createCompatBlock("quark", "poise_bookshelf", () -> new BlockEEBookshelf(Properties.from(Blocks.BOOKSHELF)), ItemGroup.BUILDING_BLOCKS);
	public static final RegistryObject<Block> POISE_LADDER          = HELPER.createCompatBlock("quark", "poise_ladder", () -> new BlockEELadder(Properties.from(Blocks.LADDER)), ItemGroup.DECORATIONS);
	public static final RegistryObject<Block> EUMUS_BRICK_VERTICAL_SLAB = HELPER.createCompatBlock("quark", "eumus_brick_vertical_slab", () -> new BlockVerticalSlab(EEProperties.EUMUS_BRICKS), ItemGroup.BUILDING_BLOCKS);
	
	@OnlyIn(Dist.CLIENT)
	private static Callable<ItemStackTileEntityRenderer> bolloomBudISTER() {
		return () -> new EETileEntityItemRenderer<TileEntity>(TileEntityBolloomBud::new);
	}
	
	@OnlyIn(Dist.CLIENT)
	private static Callable<ItemStackTileEntityRenderer> puffbugHiveISTER() {
		return () -> new EETileEntityItemRenderer<TileEntity>(TileEntityPuffBugHive::new);
	}
	
	@SubscribeEvent
	public static void onRegisterBlocks(RegistryEvent.Register<Block> event) {
		final Block blocks[] = {
			ENDER_FIRE
		};
		event.getRegistry().registerAll(blocks);
	}
}