package endergeticexpansion.core.registry;

import java.util.concurrent.Callable;

import com.mojang.datafixers.util.Pair;
import com.teamabnormals.abnormals_core.common.blocks.AbnormalsLadderBlock;
import com.teamabnormals.abnormals_core.common.blocks.BookshelfBlock;
import com.teamabnormals.abnormals_core.common.blocks.VerticalSlabBlock;
import com.teamabnormals.abnormals_core.common.blocks.sign.*;
import com.teamabnormals.abnormals_core.common.blocks.wood.*;

import endergeticexpansion.client.render.item.EETileEntityItemRenderer;
import endergeticexpansion.common.EEProperties;
import endergeticexpansion.common.blocks.*;
import endergeticexpansion.common.blocks.poise.*;
import endergeticexpansion.common.blocks.poise.boof.*;
import endergeticexpansion.common.blocks.poise.hive.*;
import endergeticexpansion.common.tileentities.BolloomBudTileEntity;
import endergeticexpansion.common.tileentities.PuffBugHiveTileEntity;
import endergeticexpansion.core.EndergeticExpansion;
import endergeticexpansion.core.registry.util.EndergeticRegistryHelper;
import net.minecraft.block.AbstractBlock.Properties;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowerPotBlock;
import net.minecraft.block.PressurePlateBlock;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.WallBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.ItemGroup;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;

@SuppressWarnings("deprecation")
@Mod.EventBusSubscriber(modid = EndergeticExpansion.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EEBlocks {
	public static final EndergeticRegistryHelper HELPER = EndergeticExpansion.REGISTRY_HELPER;
	
	public static final RegistryObject<Block> CORROCK_OVERWORLD_BLOCK           = HELPER.createBlock("overworld_corrock_block", () -> new CorrockBlock(EEProperties.CORROCK_BASE(MaterialColor.BROWN_TERRACOTTA, true), false), ItemGroup.BUILDING_BLOCKS);
	public static final RegistryObject<Block> PETRIFIED_CORROCK_OVERWORLD_BLOCK = HELPER.createBlock("petrified_overworld_corrock_block", () -> new CorrockBlock(EEProperties.CORROCK_BASE(MaterialColor.BROWN_TERRACOTTA, true), true), null);
	public static final RegistryObject<Block> CORROCK_NETHER_BLOCK              = HELPER.createBlock("nether_corrock_block", () -> new CorrockBlock(EEProperties.CORROCK_BASE(MaterialColor.RED_TERRACOTTA, true), false), ItemGroup.BUILDING_BLOCKS);
	public static final RegistryObject<Block> PETRIFIED_CORROCK_NETHER_BLOCK    = HELPER.createBlock("petrified_nether_corrock_block", () -> new CorrockBlock(EEProperties.CORROCK_BASE(MaterialColor.RED_TERRACOTTA, true), true), null);
	public static final RegistryObject<Block> CORROCK_END_BLOCK                 = HELPER.createBlock("end_corrock_block", () -> new CorrockBlock(EEProperties.CORROCK_BASE(MaterialColor.PURPLE_TERRACOTTA, true), false), ItemGroup.BUILDING_BLOCKS);
	public static final RegistryObject<Block> PETRIFIED_CORROCK_END_BLOCK       = HELPER.createBlock("petrified_end_corrock_block", () -> new CorrockBlock(EEProperties.CORROCK_BASE(MaterialColor.PURPLE_TERRACOTTA, true), true), null);
	public static final RegistryObject<Block> CORROCK_OVERWORLD                 = HELPER.createBlock("overworld_corrock", () -> new CorrockPlantBlock(EEProperties.CORROCK_BASE(MaterialColor.BROWN_TERRACOTTA, false), false), ItemGroup.DECORATIONS);
	public static final RegistryObject<Block> PETRIFIED_CORROCK_OVERWORLD       = HELPER.createBlock("petrified_overworld_corrock", () -> new CorrockPlantBlock(EEProperties.CORROCK_BASE(MaterialColor.BROWN_TERRACOTTA, false), true), null);
	public static final RegistryObject<Block> CORROCK_NETHER                    = HELPER.createBlock("nether_corrock", () -> new CorrockPlantBlock(EEProperties.CORROCK_BASE(MaterialColor.RED_TERRACOTTA, false), false), ItemGroup.DECORATIONS);
	public static final RegistryObject<Block> PETRIFIED_CORROCK_NETHER          = HELPER.createBlock("petrified_nether_corrock", () -> new CorrockPlantBlock(EEProperties.CORROCK_BASE(MaterialColor.RED_TERRACOTTA, false), true), null);
	public static final RegistryObject<Block> CORROCK_END                       = HELPER.createBlock("end_corrock", () -> new CorrockPlantBlock(EEProperties.CORROCK_BASE(MaterialColor.PURPLE_TERRACOTTA, false), false), ItemGroup.DECORATIONS);
	public static final RegistryObject<Block> PETRIFIED_CORROCK_END             = HELPER.createBlock("petrified_end_corrock", () -> new CorrockPlantBlock(EEProperties.CORROCK_BASE(MaterialColor.PURPLE_TERRACOTTA, false), true), null);
	public static final RegistryObject<CorrockCrownWallBlock> CORROCK_CROWN_OVERWORLD_WALL           = HELPER.createBlockNoItem("overworld_wall_corrock_crown", () -> new CorrockCrownWallBlock(EEProperties.CORROCK_BASE_GLOWING(MaterialColor.BROWN), false));
	public static final RegistryObject<CorrockCrownWallBlock> PETRIFIED_CORROCK_CROWN_OVERWORLD_WALL = HELPER.createBlockNoItem("petrified_overworld_wall_corrock_crown", () -> new CorrockCrownWallBlock(EEProperties.CORROCK_BASE_GLOWING(MaterialColor.BROWN), true));
	public static final RegistryObject<CorrockCrownBlock> CORROCK_CROWN_OVERWORLD_STANDING           = HELPER.createCorrockStandingBlock("overworld_corrock_crown", () -> new CorrockCrownStandingBlock(EEProperties.CORROCK_BASE_GLOWING(MaterialColor.BROWN), false), () -> CORROCK_CROWN_OVERWORLD_WALL.get(), ItemGroup.DECORATIONS);
	public static final RegistryObject<CorrockCrownBlock> PETRIFIED_CORROCK_CROWN_OVERWORLD_STANDING = HELPER.createCorrockStandingBlock("petrified_overworld_corrock_crown", () -> new CorrockCrownStandingBlock(EEProperties.CORROCK_BASE_GLOWING(MaterialColor.BROWN), true), () -> PETRIFIED_CORROCK_CROWN_OVERWORLD_WALL.get(), null);
	public static final RegistryObject<CorrockCrownWallBlock> CORROCK_CROWN_NETHER_WALL              = HELPER.createBlockNoItem("nether_wall_corrock_crown", () -> new CorrockCrownWallBlock(EEProperties.CORROCK_BASE_GLOWING(MaterialColor.RED), false));
	public static final RegistryObject<CorrockCrownWallBlock> PETRIFIED_CORROCK_CROWN_NETHER_WALL    = HELPER.createBlockNoItem("petrified_nether_wall_corrock_crown", () -> new CorrockCrownWallBlock(EEProperties.CORROCK_BASE_GLOWING(MaterialColor.RED), true));
	public static final RegistryObject<CorrockCrownBlock> CORROCK_CROWN_NETHER_STANDING              = HELPER.createCorrockStandingBlock("nether_corrock_crown", () -> new CorrockCrownStandingBlock(EEProperties.CORROCK_BASE_GLOWING(MaterialColor.RED), false), () -> CORROCK_CROWN_NETHER_WALL.get(), ItemGroup.DECORATIONS);
	public static final RegistryObject<CorrockCrownBlock> PETRIFIED_CORROCK_CROWN_NETHER_STANDING    = HELPER.createCorrockStandingBlock("petrified_nether_corrock_crown", () -> new CorrockCrownStandingBlock(EEProperties.CORROCK_BASE_GLOWING(MaterialColor.RED), true), () -> PETRIFIED_CORROCK_CROWN_NETHER_WALL.get(), null);
	public static final RegistryObject<CorrockCrownWallBlock> CORROCK_CROWN_END_WALL                 = HELPER.createBlockNoItem("end_wall_corrock_crown", () -> new CorrockCrownWallBlock(EEProperties.CORROCK_BASE_GLOWING(MaterialColor.PURPLE), false));
	public static final RegistryObject<CorrockCrownWallBlock> PETRIFIED_CORROCK_CROWN_END_WALL       = HELPER.createBlockNoItem("petrified_end_wall_corrock_crown", () -> new CorrockCrownWallBlock(EEProperties.CORROCK_BASE_GLOWING(MaterialColor.PURPLE), true));
	public static final RegistryObject<CorrockCrownBlock> CORROCK_CROWN_END_STANDING                 = HELPER.createCorrockStandingBlock("end_corrock_crown", () -> new CorrockCrownStandingBlock(EEProperties.CORROCK_BASE_GLOWING(MaterialColor.PURPLE), false), () -> CORROCK_CROWN_END_WALL.get(), ItemGroup.DECORATIONS);
	public static final RegistryObject<CorrockCrownBlock> PETRIFIED_CORROCK_CROWN_END_STANDING       = HELPER.createCorrockStandingBlock("petrified_end_corrock_crown", () -> new CorrockCrownStandingBlock(EEProperties.CORROCK_BASE_GLOWING(MaterialColor.PURPLE), true), () -> PETRIFIED_CORROCK_CROWN_END_WALL.get(), null);
	public static final RegistryObject<Block> ENDSTONE_COVER                                         = HELPER.createBlockNoItem("endstone_cover", () -> new EndStoneCoverBlock(Block.Properties.create(Material.TALL_PLANTS, MaterialColor.PURPLE_TERRACOTTA).hardnessAndResistance(1.5F, 6.0F).doesNotBlockMovement()));
	
	/*
	 * Poise Forest
	 */
	public static final RegistryObject<PoismossEumusBlock> POISMOSS_EUMUS         = HELPER.createBlock("eumus_poismoss", () -> new PoismossEumusBlock(EEProperties.POISMOSS_EUMUS), ItemGroup.BUILDING_BLOCKS);
	public static final RegistryObject<PoismossBlock> POISMOSS                    = HELPER.createBlock("poismoss", () -> new PoismossBlock(EEProperties.POISE_GRASS(false)), ItemGroup.BUILDING_BLOCKS);
	public static final RegistryObject<PoiseBushBlock> POISE_BUSH                 = HELPER.createBlock("poise_bush", () -> new PoiseBushBlock(EEProperties.POISE_GRASS(true)), ItemGroup.DECORATIONS);
	public static final RegistryObject<PoiseTallBushBlock> POISE_BUSH_TALL        = HELPER.createBlock("tall_poise_bush", () -> new PoiseTallBushBlock(EEProperties.POISE_GRASS(true)), ItemGroup.DECORATIONS);
	public static final RegistryObject<PoiseClusterBlock> POISE_CLUSTER           = HELPER.createBlock("poise_cluster", () -> new PoiseClusterBlock(EEProperties.POISE_CLUSTER.tickRandomly()), ItemGroup.BUILDING_BLOCKS);
	public static final RegistryObject<StrippedLogBlock> POISE_LOG_STRIPPED       = HELPER.createBlock("stripped_poise_stem", () -> new StrippedLogBlock(EEProperties.POISE_WOOD), ItemGroup.BUILDING_BLOCKS);
	public static final RegistryObject<StrippedWoodBlock> POISE_WOOD_STRIPPED     = HELPER.createBlock("stripped_poise_wood", () -> new StrippedWoodBlock(EEProperties.POISE_WOOD), ItemGroup.BUILDING_BLOCKS);
	public static final RegistryObject<AbnormalsLogBlock> POISE_LOG               = HELPER.createBlock("poise_stem", () -> new AbnormalsLogBlock(() -> EEBlocks.POISE_LOG_STRIPPED.get(), EEProperties.POISE_WOOD), ItemGroup.BUILDING_BLOCKS);
	public static final RegistryObject<WoodBlock> POISE_WOOD                      = HELPER.createBlock("poise_wood", () -> new WoodBlock(() -> EEBlocks.POISE_WOOD_STRIPPED.get(), EEProperties.POISE_WOOD), ItemGroup.BUILDING_BLOCKS);
	public static final RegistryObject<GlowingPoiseLogBlock> POISE_LOG_GLOWING    = HELPER.createBlock("glowing_poise_stem", () -> new GlowingPoiseLogBlock(EEProperties.POISE_LOG_GLOWING), ItemGroup.BUILDING_BLOCKS);
	public static final RegistryObject<GlowingPoiseLogBlock> POISE_WOOD_GLOWING   = HELPER.createBlock("glowing_poise_wood", () -> new GlowingPoiseLogBlock(EEProperties.POISE_LOG_GLOWING), ItemGroup.BUILDING_BLOCKS);
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
	public static final RegistryObject<BolloomBudBlock> BOLLOOM_BUD               = HELPER.createBlockWithISTER("bolloom_bud", () -> new BolloomBudBlock(EEProperties.POISE_WOOD_OTHER(true, false)), () -> bolloomBudISTER(), ItemGroup.DECORATIONS);
	public static final RegistryObject<PuffBugHiveBlock> PUFFBUG_HIVE             = HELPER.createBlockWithISTER("puffbug_hive", () -> new PuffBugHiveBlock(EEProperties.PUFFBUG_HIVE(true)), () -> puffbugHiveISTER(), ItemGroup.DECORATIONS);
	public static final RegistryObject<PuffbugHiveHangerBlock> HIVE_HANGER               = HELPER.createBlockNoItem("hive_hanger", () -> new PuffbugHiveHangerBlock(EEProperties.PUFFBUG_HIVE(false)));
	public static final RegistryObject<Block> BOLLOOM_PARTICLE                    = HELPER.createBlockNoItem("bolloom_particle", () -> new Block(EEProperties.POISE_WOOD_OTHER(false, true)));
	public static final RegistryObject<BoofBlock> BOOF_BLOCK                      = HELPER.createBlock("boof_block", () -> new BoofBlock(EEProperties.BOOF_BLOCK), ItemGroup.DECORATIONS);
	public static final RegistryObject<DispensedBoofBlock> BOOF_BLOCK_DISPENSED   = HELPER.createBlockNoItem("dispensed_boof_block", () -> new DispensedBoofBlock(EEProperties.BOOF_BLOCK.doesNotBlockMovement().notSolid().hardnessAndResistance(-1, 3600000.0F)));
	
	/*
	 * Vibra Jungle(Unused)
	 */
	public static Block FRISBLOOM_STEM    = new FrisbloomStemBlock(EEProperties.FRISBLOOM_STEM).setRegistryName(EndergeticExpansion.MOD_ID, "frisbloom_stem");
	public static Block FRISBLOOM_BUD     = new FrisbloomBudBlock(EEProperties.FRISBLOOM_BUD.doesNotBlockMovement()).setRegistryName(EndergeticExpansion.MOD_ID, "frisbloom_seeds");
	
	/*
	 * Misc
	 */
	public static final RegistryObject<EumusBlock> EUMUS                         = HELPER.createBlock("eumus", () -> new EumusBlock(EEProperties.EUMUS), ItemGroup.BUILDING_BLOCKS);
	public static final RegistryObject<Block> EUMUS_BRICKS                       = HELPER.createBlock("eumus_bricks", () -> new Block(EEProperties.EUMUS_BRICKS), ItemGroup.BUILDING_BLOCKS);
	public static final RegistryObject<Block> EUMUS_BRICKS_CHISELED              = HELPER.createBlock("chiseled_eumus_bricks", () -> new Block(EEProperties.EUMUS_BRICKS), ItemGroup.BUILDING_BLOCKS);
	public static final RegistryObject<SlabBlock> EUMUS_BRICK_SLAB               = HELPER.createBlock("eumus_brick_slab", () -> new SlabBlock(EEProperties.EUMUS_BRICKS), ItemGroup.BUILDING_BLOCKS);
	public static final RegistryObject<Block> EUMUS_BRICK_STAIRS                 = HELPER.createBlock("eumus_brick_stairs", () -> new StairsBlock(() -> EUMUS_BRICKS.get().getDefaultState(), EEProperties.EUMUS_BRICKS), ItemGroup.BUILDING_BLOCKS);
	public static final RegistryObject<WallBlock> EUMUS_BRICK_WALL               = HELPER.createBlock("eumus_brick_wall", () -> new WallBlock(EEProperties.EUMUS_BRICKS), ItemGroup.DECORATIONS);
	public static final RegistryObject<FlowerPotBlock> POISE_BUSH_POT            = HELPER.createBlockNoItem("potted_poise_bush", () -> new FlowerPotBlock(POISE_BUSH.get(), Properties.from(Blocks.POTTED_PINK_TULIP)));
	public static final RegistryObject<Block> MYSTICAL_OBSIDIAN                  = HELPER.createBlock("mystical_obsidian", () -> new Block(EEProperties.MYSTICAL_OBSIDIAN), null);
	public static final RegistryObject<WallBlock> MYSTICAL_OBSIDIAN_WALL         = HELPER.createBlock("mystical_obsidian_wall", () -> new WallBlock(EEProperties.MYSTICAL_OBSIDIAN), null);
	public static final RegistryObject<RotatableBlock> MYSTICAL_OBSIDIAN_RUNE    = HELPER.createBlock("mystical_obsidian_rune", () -> new RotatableBlock(EEProperties.MYSTICAL_OBSIDIAN), null);
	public static final RegistryObject<RotatableBlock> MYSTICAL_OBSIDIAN_ACTIVATION_RUNE = HELPER.createBlock("mystical_obsidian_activation_rune", () -> new RotatableBlock(EEProperties.MYSTICAL_OBSIDIAN), null);
	public static final RegistryObject<RotatableBlock> MYSTICAL_OBSIDIAN_ACTIVATION_RUNE_ACTIVE = HELPER.createBlock("activated_mystical_obsidian_activation_rune", () -> new RotatableBlock(EEProperties.MYSTICAL_OBSIDIAN.setLightLevel(state -> 5)), null);
	public static final RegistryObject<AcidianLanternBlock> ACIDIAN_LANTERN      = HELPER.createBlock("acidian_lantern", () ->  new AcidianLanternBlock(EEProperties.ACIDIAN_LANTERN), ItemGroup.DECORATIONS);
	public static final RegistryObject<Block> CRYSTAL_HOLDER                     = HELPER.createBlock("crystal_holder", () -> new Block(EEProperties.MYSTICAL_OBSIDIAN), null);
	public static final RegistryObject<Block> ENDER_FIRE                         = HELPER.createBlock("ender_fire", () -> new EnderFireBlock(Properties.from(Blocks.FIRE)), null);
	
	/*
	 * Compatibility
	 */
	public static final RegistryObject<Block> POISE_VERTICAL_PLANKS = HELPER.createCompatBlock("quark", "vertical_poise_planks", () -> new Block(EEProperties.POISE_WOOD), ItemGroup.BUILDING_BLOCKS);
	public static final RegistryObject<Block> POISE_VERTICAL_SLAB   = HELPER.createCompatBlock("quark", "poise_vertical_slab", () -> new VerticalSlabBlock(EEProperties.POISE_WOOD), ItemGroup.BUILDING_BLOCKS);
	public static final RegistryObject<Block> POISE_BOOKSHELF       = HELPER.createCompatBlock("quark", "poise_bookshelf", () -> new BookshelfBlock(Properties.from(Blocks.BOOKSHELF)), ItemGroup.BUILDING_BLOCKS);
	public static final RegistryObject<Block> POISE_LADDER          = HELPER.createCompatBlock("quark", "poise_ladder", () -> new AbnormalsLadderBlock(Properties.from(Blocks.LADDER)), ItemGroup.DECORATIONS);
	public static final RegistryObject<Block> EUMUS_BRICK_VERTICAL_SLAB = HELPER.createCompatBlock("quark", "eumus_brick_vertical_slab", () -> new VerticalSlabBlock(EEProperties.EUMUS_BRICKS), ItemGroup.BUILDING_BLOCKS);
	
	@OnlyIn(Dist.CLIENT)
	private static Callable<ItemStackTileEntityRenderer> bolloomBudISTER() {
		return () -> new EETileEntityItemRenderer<TileEntity>(BolloomBudTileEntity::new);
	}
	
	@OnlyIn(Dist.CLIENT)
	private static Callable<ItemStackTileEntityRenderer> puffbugHiveISTER() {
		return () -> new EETileEntityItemRenderer<TileEntity>(PuffBugHiveTileEntity::new);
	}
}