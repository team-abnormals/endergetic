package com.teamabnormals.endergetic.core.registry.other;

import com.teamabnormals.blueprint.core.api.BlueprintArmorMaterial;
import com.teamabnormals.endergetic.core.EndergeticExpansion;
import com.teamabnormals.endergetic.core.registry.EEItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.crafting.Ingredient;

/**
 * @author - SmellyModder(Luke Tonon)
 */
public final class EEArmorMaterials {
	public static final BlueprintArmorMaterial BOOFLO_VEST = new BlueprintArmorMaterial(new ResourceLocation(EndergeticExpansion.MOD_ID, "booflo_vest"), 32, new int[]{3, 3, 3, 3}, 8, () -> SoundEvents.ARMOR_EQUIP_LEATHER, 0.0F, 0.0F, () -> Ingredient.of(EEItems.BOOFLO_HIDE.get()));
}