package endergeticexpansion.core.registry;

import java.util.concurrent.Callable;

import endergeticexpansion.client.render.item.EETileEntityItemRenderer;
import endergeticexpansion.common.EEProperties;
import endergeticexpansion.common.blocks.*;
import endergeticexpansion.common.blocks.poise.*;
import endergeticexpansion.common.blocks.poise.boof.*;
import endergeticexpansion.common.blocks.poise.hive.*;
import endergeticexpansion.common.tileentities.*;
import endergeticexpansion.core.EndergeticExpansion;
import endergeticexpansion.core.registry.util.RegistryUtils;
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
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@SuppressWarnings("deprecation")
@Mod.EventBusSubscriber(modid = EndergeticExpansion.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EEBlocks {
	public static final DeferredRegister<Block> BLOCKS = new DeferredRegister<>(ForgeRegistries.BLOCKS, EndergeticExpansion.MOD_ID);
	
	public static final RegistryObject<Block> CORROCK_OVERWORLD_BLOCK           = RegistryUtils.createBlock("corrock_overworld_block", () -> new BlockCorrockBlock(EEProperties.CORROCK_BASE(MaterialColor.BROWN_TERRACOTTA, true), false), ItemGroup.BUILDING_BLOCKS);
	public static final RegistryObject<Block> PETRIFIED_CORROCK_OVERWORLD_BLOCK = RegistryUtils.createBlock("petrified_corrock_overworld_block", () -> new BlockCorrockBlock(EEProperties.CORROCK_BASE(MaterialColor.BROWN_TERRACOTTA, true), true), null);
	public static final RegistryObject<Block> CORROCK_NETHER_BLOCK              = RegistryUtils.createBlock("corrock_nether_block", () -> new BlockCorrockBlock(EEProperties.CORROCK_BASE(MaterialColor.RED_TERRACOTTA, true), false), ItemGroup.BUILDING_BLOCKS);
	public static final RegistryObject<Block> PETRIFIED_CORROCK_NETHER_BLOCK    = RegistryUtils.createBlock("petrified_corrock_nether_block", () -> new BlockCorrockBlock(EEProperties.CORROCK_BASE(MaterialColor.RED_TERRACOTTA, true), true), null);
	public static final RegistryObject<Block> CORROCK_END_BLOCK                 = RegistryUtils.createBlock("corrock_end_block", () -> new BlockCorrockBlock(EEProperties.CORROCK_BASE(MaterialColor.PURPLE_TERRACOTTA, true), false), ItemGroup.BUILDING_BLOCKS);
	public static final RegistryObject<Block> PETRIFIED_CORROCK_END_BLOCK       = RegistryUtils.createBlock("petrified_corrock_end_block", () -> new BlockCorrockBlock(EEProperties.CORROCK_BASE(MaterialColor.PURPLE_TERRACOTTA, true), true), null);
	public static final RegistryObject<Block> CORROCK_OVERWORLD                 = RegistryUtils.createBlock("corrock_overworld", () -> new BlockCorrock(EEProperties.CORROCK_BASE(MaterialColor.BROWN_TERRACOTTA, false), false), ItemGroup.DECORATIONS);
	public static final RegistryObject<Block> PETRIFIED_CORROCK_OVERWORLD       = RegistryUtils.createBlock("petrified_corrock_overworld", () -> new BlockCorrock(EEProperties.CORROCK_BASE(MaterialColor.BROWN_TERRACOTTA, false), true), null);
	public static final RegistryObject<Block> CORROCK_NETHER                    = RegistryUtils.createBlock("corrock_nether", () -> new BlockCorrock(EEProperties.CORROCK_BASE(MaterialColor.RED_TERRACOTTA, false), false), ItemGroup.DECORATIONS);
	public static final RegistryObject<Block> PETRIFIED_CORROCK_NETHER          = RegistryUtils.createBlock("petrified_corrock_nether", () -> new BlockCorrock(EEProperties.CORROCK_BASE(MaterialColor.RED_TERRACOTTA, false), true), null);
	public static final RegistryObject<Block> CORROCK_END                       = RegistryUtils.createBlock("corrock_end", () -> new BlockCorrock(EEProperties.CORROCK_BASE(MaterialColor.PURPLE_TERRACOTTA, false), false), ItemGroup.DECORATIONS);
	public static final RegistryObject<Block> PETRIFIED_CORROCK_END             = RegistryUtils.createBlock("petrified_corrock_end", () -> new BlockCorrock(EEProperties.CORROCK_BASE(MaterialColor.PURPLE_TERRACOTTA, false), true), null);
	public static final RegistryObject<BlockCorrockCrownWall> CORROCK_CROWN_OVERWORLD_WALL           = RegistryUtils.createBlockNoItem("corrock_crown_wall_overworld", () -> new BlockCorrockCrownWall(EEProperties.CORROCK_BASE_GLOWING(MaterialColor.BROWN), false));
	public static final RegistryObject<BlockCorrockCrownWall> PETRIFIED_CORROCK_CROWN_OVERWORLD_WALL = RegistryUtils.createBlockNoItem("petrified_corrock_crown_wall_overworld", () -> new BlockCorrockCrownWall(EEProperties.CORROCK_BASE_GLOWING(MaterialColor.BROWN), true));
	public static final RegistryObject<BlockCorrockCrown> CORROCK_CROWN_OVERWORLD_STANDING           = RegistryUtils.createCorrockStandingBlock("corrock_crown_standing_overworld", () -> new BlockCorrockCrownStanding(EEProperties.CORROCK_BASE_GLOWING(MaterialColor.BROWN), false), () -> CORROCK_CROWN_OVERWORLD_WALL.get(), ItemGroup.DECORATIONS);
	public static final RegistryObject<BlockCorrockCrown> PETRIFIED_CORROCK_CROWN_OVERWORLD_STANDING = RegistryUtils.createCorrockStandingBlock("petrified_corrock_crown_standing_overworld", () -> new BlockCorrockCrownStanding(EEProperties.CORROCK_BASE_GLOWING(MaterialColor.BROWN), true), () -> PETRIFIED_CORROCK_CROWN_OVERWORLD_WALL.get(), null);
	public static final RegistryObject<BlockCorrockCrownWall> CORROCK_CROWN_NETHER_WALL              = RegistryUtils.createBlockNoItem("corrock_crown_wall_nether", () -> new BlockCorrockCrownWall(EEProperties.CORROCK_BASE_GLOWING(MaterialColor.RED), false));
	public static final RegistryObject<BlockCorrockCrownWall> PETRIFIED_CORROCK_CROWN_NETHER_WALL    = RegistryUtils.createBlockNoItem("petrified_corrock_crown_wall_nether", () -> new BlockCorrockCrownWall(EEProperties.CORROCK_BASE_GLOWING(MaterialColor.RED), true));
	public static final RegistryObject<BlockCorrockCrown> CORROCK_CROWN_NETHER_STANDING              = RegistryUtils.createCorrockStandingBlock("corrock_crown_standing_nether", () -> new BlockCorrockCrownStanding(EEProperties.CORROCK_BASE_GLOWING(MaterialColor.RED), false), () -> CORROCK_CROWN_NETHER_WALL.get(), ItemGroup.DECORATIONS);
	public static final RegistryObject<BlockCorrockCrown> PETRIFIED_CORROCK_CROWN_NETHER_STANDING    = RegistryUtils.createCorrockStandingBlock("petrified_corrock_crown_standing_nether", () -> new BlockCorrockCrownStanding(EEProperties.CORROCK_BASE_GLOWING(MaterialColor.RED), true), () -> PETRIFIED_CORROCK_CROWN_NETHER_WALL.get(), null);
	public static final RegistryObject<BlockCorrockCrownWall> CORROCK_CROWN_END_WALL                 = RegistryUtils.createBlockNoItem("corrock_crown_wall_end", () -> new BlockCorrockCrownWall(EEProperties.CORROCK_BASE_GLOWING(MaterialColor.PURPLE), false));
	public static final RegistryObject<BlockCorrockCrownWall> PETRIFIED_CORROCK_CROWN_END_WALL       = RegistryUtils.createBlockNoItem("petrified_corrock_crown_wall_end", () -> new BlockCorrockCrownWall(EEProperties.CORROCK_BASE_GLOWING(MaterialColor.PURPLE), true));
	public static final RegistryObject<BlockCorrockCrown> CORROCK_CROWN_END_STANDING                 = RegistryUtils.createCorrockStandingBlock("corrock_crown_standing_end", () -> new BlockCorrockCrownStanding(EEProperties.CORROCK_BASE_GLOWING(MaterialColor.PURPLE), false), () -> CORROCK_CROWN_END_WALL.get(), ItemGroup.DECORATIONS);
	public static final RegistryObject<BlockCorrockCrown> PETRIFIED_CORROCK_CROWN_END_STANDING       = RegistryUtils.createCorrockStandingBlock("petrified_corrock_crown_standing_end", () -> new BlockCorrockCrownStanding(EEProperties.CORROCK_BASE_GLOWING(MaterialColor.PURPLE), true), () -> PETRIFIED_CORROCK_CROWN_END_WALL.get(), null);
	public static final RegistryObject<Block> ENDSTONE_COVER                                         = RegistryUtils.createBlockNoItem("endstone_cover", () -> new BlockEndStoneCover(Block.Properties.create(Material.TALL_PLANTS, MaterialColor.PURPLE_TERRACOTTA).hardnessAndResistance(1.5F, 6.0F).doesNotBlockMovement()));
	
	/*
	 * Poise Forest
	 */
	public static final RegistryObject<BlockPoismossEumus> POISMOSS_EUMUS         = RegistryUtils.createBlock("poismoss_eumus", () -> new BlockPoismossEumus(EEProperties.POISMOSS_EUMUS), ItemGroup.BUILDING_BLOCKS);
	public static final RegistryObject<BlockPoismoss> POISMOSS                    = RegistryUtils.createBlock("poise_grass_block", () -> new BlockPoismoss(EEProperties.POISE_GRASS(false)), ItemGroup.BUILDING_BLOCKS);
	public static final RegistryObject<BlockPoiseGrassPlant> POISE_GRASS          = RegistryUtils.createBlock("poise_grass", () -> new BlockPoiseGrassPlant(EEProperties.POISE_GRASS(true)), ItemGroup.DECORATIONS);
	public static final RegistryObject<BlockPoiseGrassPlantTall> POISE_GRASS_TALL = RegistryUtils.createBlock("poise_grass_tall", () -> new BlockPoiseGrassPlantTall(EEProperties.POISE_GRASS(true)), ItemGroup.DECORATIONS);
	public static final RegistryObject<BlockPoiseCluster> POISE_CLUSTER           = RegistryUtils.createBlock("poise_cluster", () -> new BlockPoiseCluster(EEProperties.POISE_CLUSTER.tickRandomly()), ItemGroup.BUILDING_BLOCKS);
	public static final RegistryObject<BlockLogBase> POISE_LOG_STRIPPED           = RegistryUtils.createBlock("poise_stem_stripped", () -> new BlockLogBase(EEProperties.POISE_WOOD, null), ItemGroup.BUILDING_BLOCKS);
	public static final RegistryObject<BlockLogBase> POISE_WOOD_STRIPPED          = RegistryUtils.createBlock("poise_wood_stripped", () -> new BlockLogBase(EEProperties.POISE_WOOD, null), ItemGroup.BUILDING_BLOCKS);
	public static final RegistryObject<BlockLogBase> POISE_LOG                    = RegistryUtils.createBlock("poise_stem", () -> new BlockLogBase(EEProperties.POISE_WOOD, () -> EEBlocks.POISE_LOG_STRIPPED.get()), ItemGroup.BUILDING_BLOCKS);
	public static final RegistryObject<BlockLogBase> POISE_WOOD                   = RegistryUtils.createBlock("poise_wood", () -> new BlockLogBase(EEProperties.POISE_WOOD, () -> EEBlocks.POISE_WOOD_STRIPPED.get()), ItemGroup.BUILDING_BLOCKS);
	public static final RegistryObject<BlockGlowingPoiseLog> POISE_LOG_GLOWING    = RegistryUtils.createBlock("poise_stem_glowing", () -> new BlockGlowingPoiseLog(EEProperties.POISE_LOG_GLOWING), ItemGroup.BUILDING_BLOCKS);
	public static final RegistryObject<BlockGlowingPoiseWood> POISE_WOOD_GLOWING  = RegistryUtils.createBlock("poise_wood_glowing", () -> new BlockGlowingPoiseWood(EEProperties.POISE_LOG_GLOWING), ItemGroup.BUILDING_BLOCKS);
	public static final RegistryObject<Block> POISE_PLANKS                        = RegistryUtils.createBlock("poise_planks", () -> new Block(EEProperties.POISE_WOOD), ItemGroup.BUILDING_BLOCKS);
	public static final RegistryObject<BlockDoorBase> POISE_DOOR                  = RegistryUtils.createTallBlock("poise_door", () -> new BlockDoorBase(EEProperties.POISE_WOOD_NOT_SOLID), ItemGroup.REDSTONE);
	public static final RegistryObject<BlockSlabBase> POISE_SLAB                  = RegistryUtils.createBlock("poise_slab", () -> new BlockSlabBase(EEProperties.POISE_WOOD), ItemGroup.BUILDING_BLOCKS);
	public static final RegistryObject<BlockStairsBase> POISE_STAIRS              = RegistryUtils.createBlock("poise_stairs", () -> new BlockStairsBase(() -> POISE_PLANKS.get().getDefaultState(), EEProperties.POISE_WOOD), ItemGroup.BUILDING_BLOCKS);
	public static final RegistryObject<BlockFenceBase> POISE_FENCE                = RegistryUtils.createBlock("poise_fence", () -> new BlockFenceBase(EEProperties.POISE_WOOD), ItemGroup.DECORATIONS);
	public static final RegistryObject<BlockFenceGateBase> POISE_FENCE_GATE       = RegistryUtils.createBlock("poise_fence_gate", () -> new BlockFenceGateBase(EEProperties.POISE_WOOD), ItemGroup.REDSTONE);
	public static final RegistryObject<BlockPressurePlateBase> POISE_PRESSURE_PLATE = RegistryUtils.createBlock("poise_pressure_plate", () -> new BlockPressurePlateBase(PressurePlateBlock.Sensitivity.EVERYTHING, EEProperties.POISE_WOOD), ItemGroup.REDSTONE);
	public static final RegistryObject<BlockButtonBase> POISE_BUTTON              = RegistryUtils.createBlock("poise_button", () -> new BlockButtonBase(EEProperties.POISE_WOOD_OTHER(false, true)), ItemGroup.REDSTONE);
	public static final RegistryObject<BlockTrapdoorBase> POISE_TRAPDOOR          = RegistryUtils.createBlock("poise_trapdoor", () -> new BlockTrapdoorBase(EEProperties.POISE_WOOD_NOT_SOLID), ItemGroup.REDSTONE);
	public static final RegistryObject<BlockBolloomBud> BOLLOOM_BUD               = RegistryUtils.createBlockWithISTER("bolloom_bud", () -> new BlockBolloomBud(EEProperties.POISE_WOOD_OTHER(true, false)), () -> bolloomBudISTER(), ItemGroup.DECORATIONS);
	public static final RegistryObject<BlockPuffBugHive> PUFFBUG_HIVE             = RegistryUtils.createBlockWithISTER("puffbug_hive", () -> new BlockPuffBugHive(EEProperties.PUFFBUG_HIVE(true)), () -> puffbugHiveISTER(), ItemGroup.DECORATIONS);
	public static final RegistryObject<BlockHiveHanger> HIVE_HANGER               = RegistryUtils.createBlockNoItem("hive_hanger", () -> new BlockHiveHanger(EEProperties.PUFFBUG_HIVE(false)));
	public static final RegistryObject<Block> BOLLOOM_PARTICLE                    = RegistryUtils.createBlockNoItem("bolloom_particle", () -> new Block(EEProperties.POISE_WOOD_OTHER(false, true)));
	public static final RegistryObject<BlockBoof> BOOF_BLOCK                      = RegistryUtils.createBlock("boof_block", () -> new BlockBoof(EEProperties.BOOF_BLOCK), ItemGroup.DECORATIONS);
	public static final RegistryObject<BlockDispensedBoof> BOOF_BLOCK_DISPENSED   = RegistryUtils.createBlockNoItem("boof_dispensed_block", () -> new BlockDispensedBoof(EEProperties.BOOF_BLOCK.doesNotBlockMovement().notSolid().hardnessAndResistance(-1, 3600000.0F)));
	
	/*
	 * Vibra Jungle(Unused)
	 */
	public static Block FRISBLOOM_STEM    = new BlockFrisbloomStem(EEProperties.FRISBLOOM_STEM).setRegistryName(EndergeticExpansion.MOD_ID, "frisbloom_stem");
	public static Block FRISBLOOM_BUD     = new BlockFrisbloomBud(EEProperties.FRISBLOOM_BUD.doesNotBlockMovement()).setRegistryName(EndergeticExpansion.MOD_ID, "frisbloom_seeds");
	
	/*
	 * Misc
	 */
	public static final RegistryObject<BlockEumus> EUMUS                         = RegistryUtils.createBlock("eumus", () -> new BlockEumus(EEProperties.EUMUS), ItemGroup.BUILDING_BLOCKS);
	public static final RegistryObject<Block> EUMUS_BRICKS                       = RegistryUtils.createBlock("eumus_bricks", () -> new Block(EEProperties.EUMUS_BRICKS), ItemGroup.BUILDING_BLOCKS);
	public static final RegistryObject<Block> EUMUS_BRICKS_CHISELED              = RegistryUtils.createBlock("eumus_chiseled_bricks", () -> new Block(EEProperties.EUMUS_BRICKS), ItemGroup.BUILDING_BLOCKS);
	public static final RegistryObject<SlabBlock> EUMUS_BRICK_SLAB               = RegistryUtils.createBlock("eumus_brick_slab", () -> new SlabBlock(EEProperties.EUMUS_BRICKS), ItemGroup.BUILDING_BLOCKS);
	public static final RegistryObject<BlockStairsBase> EUMUS_BRICK_STAIRS       = RegistryUtils.createBlock("eumus_brick_stairs", () -> new BlockStairsBase(() -> EUMUS_BRICKS.get().getDefaultState(), EEProperties.EUMUS_BRICKS), ItemGroup.BUILDING_BLOCKS);
	public static final RegistryObject<WallBlock> EUMUS_BRICK_WALL               = RegistryUtils.createBlock("eumus_brick_wall", () -> new WallBlock(EEProperties.EUMUS_BRICKS), ItemGroup.DECORATIONS);
	public static final RegistryObject<FlowerPotBlock> POISE_BUSH_POT            = RegistryUtils.createBlockNoItem("potted_poise_bush", () -> new FlowerPotBlock(POISE_GRASS.get(), Properties.from(Blocks.POTTED_PINK_TULIP)));
	public static final RegistryObject<Block> MYSTICAL_OBSIDIAN                  = RegistryUtils.createBlock("mystical_obsidian", () -> new Block(EEProperties.MYSTICAL_OBSIDIAN), null);
	public static final RegistryObject<WallBlock> MYSTICAL_OBSIDIAN_WALL         = RegistryUtils.createBlock("mystical_obsidian_wall", () -> new WallBlock(EEProperties.MYSTICAL_OBSIDIAN), null);
	public static final RegistryObject<BlockRotatable> MYSTICAL_OBSIDIAN_RUNE    = RegistryUtils.createBlock("mystical_obsidian_rune", () -> new BlockRotatable(EEProperties.MYSTICAL_OBSIDIAN), null);
	public static final RegistryObject<BlockRotatable> MYSTICAL_OBSIDIAN_ACTIVATION_RUNE = RegistryUtils.createBlock("mystical_obsidian_activation_rune", () -> new BlockRotatable(EEProperties.MYSTICAL_OBSIDIAN), null);
	public static final RegistryObject<BlockRotatable> MYSTICAL_OBSIDIAN_ACTIVATION_RUNE_ACTIVE = RegistryUtils.createBlock("mystical_obsidian_activation_rune_active", () -> new BlockRotatable(EEProperties.MYSTICAL_OBSIDIAN.lightValue(5)), null);
	public static final RegistryObject<BlockAcidianLantern> ACIDIAN_LANTERN      = RegistryUtils.createBlock("acidian_lantern", () ->  new BlockAcidianLantern(EEProperties.ACIDIAN_LANTERN), ItemGroup.DECORATIONS);
	public static final RegistryObject<Block> CRYSTAL_HOLDER                     = RegistryUtils.createBlock("crystal_holder", () -> new Block(EEProperties.MYSTICAL_OBSIDIAN), null);
	public static Block ENDER_FIRE           = new BlockEnderFire(Properties.from(Blocks.FIRE)).setRegistryName(EndergeticExpansion.MOD_ID, "ender_fire");
	
	/*
	 * Compatibility
	 */
	public static final RegistryObject<Block> POISE_VERTICAL_PLANKS = RegistryUtils.createCompatBlock("poise_vertical_planks", "quark", () -> new Block(EEProperties.POISE_WOOD), ItemGroup.BUILDING_BLOCKS);
	public static final RegistryObject<Block> POISE_VERTICAL_SLAB   = RegistryUtils.createCompatBlock("poise_vertical_slab", "quark", () -> new BlockVerticalSlab(EEProperties.POISE_WOOD), ItemGroup.BUILDING_BLOCKS);
	public static final RegistryObject<Block> POISE_BOOKSHELF       = RegistryUtils.createCompatBlock("poise_bookshelf", "quark", () -> new BlockEEBookshelf(Properties.from(Blocks.BOOKSHELF)), ItemGroup.BUILDING_BLOCKS);
	public static final RegistryObject<Block> POISE_LADDER          = RegistryUtils.createCompatBlock("poise_ladder", "quark", () -> new BlockEELadder(Properties.from(Blocks.LADDER)), ItemGroup.DECORATIONS);
	public static final RegistryObject<Block> EUMUS_BRICK_VERTICAL_SLAB = RegistryUtils.createCompatBlock("eumus_brick_vertical_slab", "quark", () -> new BlockVerticalSlab(EEProperties.EUMUS_BRICKS), ItemGroup.BUILDING_BLOCKS);
	
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