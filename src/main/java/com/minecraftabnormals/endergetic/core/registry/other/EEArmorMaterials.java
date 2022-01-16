package com.minecraftabnormals.endergetic.core.registry.other;

import com.minecraftabnormals.abnormals_core.core.api.AbnormalsArmorMaterial;
import com.minecraftabnormals.endergetic.core.EndergeticExpansion;
import com.minecraftabnormals.endergetic.core.registry.EEItems;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;

/**
 * @author - SmellyModder(Luke Tonon)
 */
public final class EEArmorMaterials {
	public static final AbnormalsArmorMaterial BOOFLO_VEST = new AbnormalsArmorMaterial(new ResourceLocation(EndergeticExpansion.MOD_ID, "booflo_vest"), 32, new int[]{3, 3, 3, 3}, 8, SoundEvents.ARMOR_EQUIP_LEATHER, 0.0F, 0.0F, () -> Ingredient.of(EEItems.BOOFLO_HIDE.get()));
}