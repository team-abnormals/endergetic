package com.teamabnormals.endergetic.core.data.server;

import com.teamabnormals.blueprint.core.Blueprint;
import com.teamabnormals.blueprint.core.api.conditions.QuarkFlagRecipeCondition;
import com.teamabnormals.endergetic.core.registry.EEBlocks;
import com.teamabnormals.endergetic.core.registry.EEItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.ConditionalRecipe;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;

import java.util.function.Consumer;

public class EERecipeProvider extends RecipeProvider {
	public static final ModLoadedCondition BOATLOAD_LOADED = new ModLoadedCondition("boatload");

	public EERecipeProvider(DataGenerator generator) {
		super(generator);
	}

	@Override
	public void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
		woodenBoat(consumer, EEItems.POISE_BOAT.getFirst().get(), EEBlocks.POISE_PLANKS.get());
		chestBoat(consumer, EEItems.POISE_BOAT.getSecond().get(), EEItems.POISE_BOAT.getFirst().get());
		conditionalRecipe(consumer, BOATLOAD_LOADED, ShapelessRecipeBuilder.shapeless(EEItems.POISE_FURNACE_BOAT.get()).requires(Blocks.FURNACE).requires(EEItems.POISE_BOAT.getFirst().get()).group("furnace_boat").unlockedBy("has_boat", has(ItemTags.BOATS)));
		conditionalRecipe(consumer, BOATLOAD_LOADED, ShapedRecipeBuilder.shaped(EEItems.LARGE_POISE_BOAT.get()).define('#', EEBlocks.POISE_PLANKS.get()).define('B', EEItems.POISE_BOAT.getFirst().get()).pattern("#B#").pattern("###").group("large_boat").unlockedBy("has_boat", has(ItemTags.BOATS)));
	}

	public static QuarkFlagRecipeCondition quarkFlag(String flag) {
		return new QuarkFlagRecipeCondition(new ResourceLocation(Blueprint.MOD_ID, "quark_flag"), flag);
	}

	private static void chestBoat(Consumer<FinishedRecipe> consumer, ItemLike chestBoat, ItemLike boat) {
		ShapelessRecipeBuilder.shapeless(chestBoat).requires(Tags.Items.CHESTS_WOODEN).requires(boat).group("chest_boat").unlockedBy("has_boat", has(ItemTags.BOATS)).save(consumer);
	}

	public static void conditionalRecipe(Consumer<FinishedRecipe> consumer, ICondition condition, RecipeBuilder recipe) {
		conditionalRecipe(consumer, condition, recipe, RecipeBuilder.getDefaultRecipeId(recipe.getResult()));
	}

	public static void conditionalRecipe(Consumer<FinishedRecipe> consumer, ICondition condition, RecipeBuilder recipe, ResourceLocation id) {
		ConditionalRecipe.builder().addCondition(condition).addRecipe(consumer1 -> recipe.save(consumer1, id)).generateAdvancement(new ResourceLocation(id.getNamespace(), "recipes/" + recipe.getResult().getItemCategory().getRecipeFolderName() + "/" + id.getPath())).build(consumer, id);
	}
}