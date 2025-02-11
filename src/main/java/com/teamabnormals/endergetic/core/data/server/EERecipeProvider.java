package com.teamabnormals.endergetic.core.data.server;

import com.teamabnormals.blueprint.core.data.server.BlueprintRecipeProvider;
import com.teamabnormals.blueprint.core.other.tags.BlueprintItemTags;
import com.teamabnormals.boatload.core.data.server.BoatloadRecipeProvider;
import com.teamabnormals.clayworks.core.data.server.ClayworksRecipeProvider;
import com.teamabnormals.endergetic.common.entity.bolloom.BalloonColor;
import com.teamabnormals.endergetic.core.EndergeticExpansion;
import com.teamabnormals.endergetic.core.registry.EEItems;
import com.teamabnormals.endergetic.core.other.EEBlockFamilies;
import com.teamabnormals.endergetic.core.other.tags.EEItemTags;
import com.teamabnormals.endergetic.integration.boatload.EEBoatTypes;
import com.teamabnormals.woodworks.core.data.server.WoodworksRecipeProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.RegistryObject;

import java.util.Arrays;
import java.util.function.Consumer;

import static com.teamabnormals.endergetic.core.registry.EEBlocks.*;

public class EERecipeProvider extends BlueprintRecipeProvider {

	public EERecipeProvider(PackOutput output) {
		super(EndergeticExpansion.MOD_ID, output);
	}

	@Override
	public void buildRecipes(Consumer<FinishedRecipe> consumer) {
		storageRecipes(consumer, RecipeCategory.FOOD, EEItems.BOLLOOM_FRUIT.get(), RecipeCategory.BUILDING_BLOCKS, BOLLOOM_CRATE.get());

		SimpleCookingRecipeBuilder.smelting(Ingredient.of(EUMUS.get()), RecipeCategory.MISC, EEItems.EUMUS_BRICK.get(), 0.1F, 200).unlockedBy("has_eumus", has(EUMUS.get())).save(consumer);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, EUMUS_BRICKS.get()).define('#', EEItems.EUMUS_BRICK.get()).pattern("##").pattern("##").unlockedBy("has_eumus_brick", has(EEItems.EUMUS_BRICK.get())).save(consumer);
		generateRecipes(consumer, EEBlockFamilies.EUMUS_BRICKS_FAMILY);
		stonecutterRecipe(consumer, RecipeCategory.BUILDING_BLOCKS, EUMUS_BRICK_SLAB.get(), EUMUS_BRICKS.get(), 2);
		stonecutterRecipe(consumer, RecipeCategory.BUILDING_BLOCKS, EUMUS_BRICK_STAIRS.get(), EUMUS_BRICKS.get());
		stonecutterRecipe(consumer, RecipeCategory.DECORATIONS, EUMUS_BRICK_WALL.get(), EUMUS_BRICKS.get());
		stonecutterRecipe(consumer, RecipeCategory.BUILDING_BLOCKS, CHISELED_EUMUS_BRICKS.get(), EUMUS_BRICKS.get());

		chiseledBuilder(RecipeCategory.BUILDING_BLOCKS, CHISELED_END_STONE_BRICKS.get(), Ingredient.of(Blocks.END_STONE_BRICK_SLAB)).unlockedBy("has_end_stone_brick_slab", has(Blocks.END_STONE_BRICK_SLAB)).save(consumer);
		stonecutterRecipe(consumer, RecipeCategory.BUILDING_BLOCKS, CHISELED_END_STONE_BRICKS.get(), Blocks.END_STONE_BRICKS);
		stonecutterRecipe(consumer, RecipeCategory.BUILDING_BLOCKS, CHISELED_END_STONE_BRICKS.get(), Blocks.END_STONE);

		SimpleCookingRecipeBuilder.smelting(Ingredient.of(Blocks.END_STONE_BRICKS), RecipeCategory.BUILDING_BLOCKS, CRACKED_END_STONE_BRICKS.get(), 0.1F, 200).unlockedBy("has_end_stone_bricks", has(Blocks.END_STONE_BRICKS)).save(consumer);
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(Blocks.PURPUR_BLOCK), RecipeCategory.BUILDING_BLOCKS, CRACKED_PURPUR_BLOCK.get(), 0.1F, 200).unlockedBy("has_purpur_block", has(Blocks.PURPUR_BLOCK)).save(consumer);

		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ENDER_CAMPFIRE.get()).define('L', ItemTags.LOGS).define('S', Items.STICK).define('#', EEItemTags.ENDER_FIRE_BASE_BLOCKS).pattern(" S ").pattern("S#S").pattern("LLL").unlockedBy("has_end_stone", has(EEItemTags.ENDER_FIRE_BASE_BLOCKS)).save(consumer);
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ENDER_TORCH.get(), 4).define('X', Ingredient.of(Items.COAL, Items.CHARCOAL)).define('#', Items.STICK).define('S', EEItemTags.ENDER_FIRE_BASE_BLOCKS).pattern("X").pattern("#").pattern("S").unlockedBy("has_end_stone", has(EEItemTags.ENDER_FIRE_BASE_BLOCKS)).save(consumer);
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ENDER_LANTERN.get()).define('#', ENDER_TORCH.get()).define('X', Items.IRON_NUGGET).pattern("XXX").pattern("X#X").pattern("XXX").unlockedBy("has_ender_torch", has(ENDER_TORCH.get())).save(consumer);

		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ACIDIAN_LANTERN.get()).define('#', Items.DRAGON_BREATH).define('O', Items.OBSIDIAN).pattern("#").pattern("O").unlockedBy("has_dragon_breath", has(Items.DRAGON_BREATH)).save(consumer);

		ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, BOOF_BLOCK.get()).define('#', EEItems.BOOFLO_HIDE.get()).pattern("##").pattern("##").unlockedBy("has_booflo_hide", has(EEItems.BOOFLO_HIDE.get())).save(consumer);
		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, EEItems.BOOFLO_VEST.get()).define('#', EEItems.BOOFLO_HIDE.get()).define('B', BOOF_BLOCK.get()).pattern("# #").pattern("#B#").pattern("###").unlockedBy("has_booflo_hide", has(EEItems.BOOFLO_HIDE.get())).save(consumer);
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, EEItems.BOLLOOM_BALLOON.get(), 3).define('#', EEItems.BOLLOOM_FRUIT.get()).define('S', Items.STRING).pattern("#").pattern("S").pattern("S").group("bolloom_balloon").unlockedBy("has_bolloom_fruit", has(EEItems.BOLLOOM_FRUIT.get())).save(consumer);
		Arrays.stream(BalloonColor.values()).filter(color -> color.color != null).forEach(color -> ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, color.balloonItem.get()).requires(EEItems.BOLLOOM_BALLOON.get()).requires(DyeItem.byColor(color.color)).group("bolloom_balloon").unlockedBy("has_bolloom_balloon", has(EEItems.BOLLOOM_BALLOON.get())).save(consumer));

		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, GLOWING_POISE_WOOD.get(), 8).define('#', POISE_WOOD.get()).define('C', POISE_CLUSTER.get()).pattern("###").pattern("#C#").pattern("###").unlockedBy("has_poise_cluster", has(POISE_CLUSTER.get())).save(consumer, getModConversionRecipeName(GLOWING_POISE_WOOD.get(), POISE_CLUSTER.get()));

		generateRecipes(consumer, EEBlockFamilies.POISE_PLANKS_FAMILY);
		planksFromLogs(consumer, POISE_PLANKS.get(), EEItemTags.POISE_STEMS, 4);
		woodFromLogs(consumer, POISE_WOOD.get(), POISE_STEM.get());
		woodFromLogs(consumer, STRIPPED_POISE_WOOD.get(), STRIPPED_POISE_STEM.get());
		woodFromLogs(consumer, GLOWING_POISE_WOOD.get(), GLOWING_POISE_STEM.get());
		hangingSign(consumer, POISE_HANGING_SIGNS.getFirst().get(), STRIPPED_POISE_STEM.get());

		BoatloadRecipeProvider.boatRecipes(consumer, EEBoatTypes.POISE);
		WoodworksRecipeProvider.baseRecipes(consumer, POISE_PLANKS.get(), POISE_SLAB.get(), POISE_BOARDS.get(), POISE_BOOKSHELF.get(), CHISELED_POISE_BOOKSHELF.get(), POISE_LADDER.get(), POISE_BEEHIVE.get(), POISE_CHEST.get(), TRAPPED_POISE_CHEST.get(), EndergeticExpansion.MOD_ID);
		WoodworksRecipeProvider.sawmillRecipes(consumer, EEBlockFamilies.POISE_PLANKS_FAMILY, EEItemTags.POISE_STEMS, POISE_BOARDS.get(), POISE_LADDER.get(), EndergeticExpansion.MOD_ID);

		ClayworksRecipeProvider.bakingRecipe(consumer, RecipeCategory.BUILDING_BLOCKS, Blocks.END_STONE_BRICKS, CRACKED_END_STONE_BRICKS.get(), 0.1F, 100, EndergeticExpansion.MOD_ID);
		ClayworksRecipeProvider.bakingRecipe(consumer, RecipeCategory.BUILDING_BLOCKS, Blocks.PURPUR_BLOCK, CRACKED_PURPUR_BLOCK.get(), 0.1F, 100, EndergeticExpansion.MOD_ID);
		ClayworksRecipeProvider.bakingRecipe(consumer, RecipeCategory.BUILDING_BLOCKS, EUMUS.get(), EEItems.EUMUS_BRICK.get(), 0.1F, 100, EndergeticExpansion.MOD_ID);
		ClayworksRecipeProvider.bakingRecipe(consumer, RecipeCategory.BUILDING_BLOCKS, EUMUS_BRICKS.get(), CRACKED_EUMUS_BRICKS.get(), 0.1F, 100, EndergeticExpansion.MOD_ID);

		// TODO: Eetle Update
//		foodCookingRecipes(consumer, EETLE_EGG.get(), EEItems.COOKED_EETLE_EGG.get());
//
//		petrifiedCorrockRecipe(consumer, PETRIFIED_INFESTED_CORROCK, INFESTED_CORROCK);
//
//		petrifiedCorrockRecipe(consumer, PETRIFIED_END_CORROCK, END_CORROCK);
//		petrifiedCorrockRecipe(consumer, PETRIFIED_END_CORROCK_BLOCK, END_CORROCK_BLOCK);
//		petrifiedCorrockRecipe(consumer, PETRIFIED_END_CORROCK_CROWN, END_CORROCK_CROWN);
//		petrifiedCorrockRecipe(consumer, PETRIFIED_SPECKLED_END_CORROCK, SPECKLED_END_CORROCK);
//
//		petrifiedCorrockRecipe(consumer, PETRIFIED_NETHER_CORROCK, NETHER_CORROCK);
//		petrifiedCorrockRecipe(consumer, PETRIFIED_NETHER_CORROCK_BLOCK, NETHER_CORROCK_BLOCK);
//		petrifiedCorrockRecipe(consumer, PETRIFIED_NETHER_CORROCK_CROWN, NETHER_CORROCK_CROWN);
//		petrifiedCorrockRecipe(consumer, PETRIFIED_SPECKLED_NETHER_CORROCK, SPECKLED_NETHER_CORROCK);
//
//		petrifiedCorrockRecipe(consumer, PETRIFIED_OVERWORLD_CORROCK, OVERWORLD_CORROCK);
//		petrifiedCorrockRecipe(consumer, PETRIFIED_OVERWORLD_CORROCK_BLOCK, OVERWORLD_CORROCK_BLOCK);
//		petrifiedCorrockRecipe(consumer, PETRIFIED_OVERWORLD_CORROCK_CROWN, OVERWORLD_CORROCK_CROWN);
//		petrifiedCorrockRecipe(consumer, PETRIFIED_SPECKLED_OVERWORLD_CORROCK, SPECKLED_OVERWORLD_CORROCK);

		storageRecipes(consumer, RecipeCategory.REDSTONE, EEItems.PORTAPLASM.get(), RecipeCategory.BUILDING_BLOCKS, PORTAPLASM_BLOCK.get());
	}

	public static void petrifiedCorrockRecipe(Consumer<FinishedRecipe> consumer, RegistryObject<? extends Block> petrifiedCorrock, RegistryObject<? extends Block> corrock) {
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, petrifiedCorrock.get(), 8).define('#', corrock.get()).define('B', BlueprintItemTags.BUCKETS_WATER).pattern("###").pattern("#B#").pattern("###").unlockedBy(getHasName(corrock.get()), has(corrock.get())).save(consumer);
	}
}