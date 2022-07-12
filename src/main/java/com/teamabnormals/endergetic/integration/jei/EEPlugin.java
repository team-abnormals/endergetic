package com.teamabnormals.endergetic.integration.jei;

//import com.teamabnormals.endergetic.core.EndergeticExpansion;
//import com.teamabnormals.endergetic.core.registry.EEBlocks;
//import mezz.jei.api.IModPlugin;
//import mezz.jei.api.JeiPlugin;
//import mezz.jei.api.constants.RecipeTypes;
//import mezz.jei.api.registration.IRecipeCatalystRegistration;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.resources.ResourceLocation;
//
//@JeiPlugin
//public class EEPlugin implements IModPlugin {
//
//	@Override
//	public ResourceLocation getPluginUid() {
//		return new ResourceLocation(EndergeticExpansion.MOD_ID, EndergeticExpansion.MOD_ID);
//	}
//
//	@Override
//	public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
//		registration.addRecipeCatalyst(new ItemStack(EEBlocks.ENDER_CAMPFIRE.get()), RecipeTypes.CAMPFIRE_COOKING);
//	}
//
//}