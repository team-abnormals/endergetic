package com.minecraftabnormals.endergetic.core.registry.other;

import com.minecraftabnormals.endergetic.core.registry.EEItems;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;

/**
 * @author - SmellyModder(Luke Tonon)
 */
public enum EEArmorMaterials implements IArmorMaterial {
	BOOFLO("booflo_vest", 32, new int[] {3, 3, 3, 3}, 8, EEItems.BOOFLO_HIDE.get(), SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 0.0f);
	
	private static final int[] MAX_DAMAGE_ARRAY = new int[] {13, 15, 16, 11};
	private String name;
	private SoundEvent equipSound;
	private Item repairItem;
	private int[] damageReductionAmounts;
	private int enchantability;
	private int maxDamageFactor;
	private float toughness;
	
	private EEArmorMaterials(String name, int maxDamageFactor, int[] damageReductionAmounts, int enchantability, Item repairItem, SoundEvent equipSound, float toughness) {
		this.name = name;
		this.equipSound = equipSound;
		this.maxDamageFactor = maxDamageFactor;
		this.enchantability = enchantability;
		this.repairItem = repairItem;
		this.damageReductionAmounts = damageReductionAmounts;
		this.toughness = toughness;
	}

	@Override
	public int getDamageReductionAmount(EquipmentSlotType slot) {
		return this.damageReductionAmounts[slot.getIndex()];
	}

	public int getDurability(EquipmentSlotType slotIn) {
		return MAX_DAMAGE_ARRAY[slotIn.getIndex()] * this.maxDamageFactor;
	}

	@Override
	public int getEnchantability() {
		return this.enchantability;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public Ingredient getRepairMaterial() {
		return Ingredient.fromItems(this.repairItem);
	}

	@Override
	public SoundEvent getSoundEvent() {
		return this.equipSound;
	}

	@Override
	public float getToughness() {
		return this.toughness;
	}

	@Override
	public float getKnockbackResistance() {
		return 0.0F;
	}
}