package com.minecraftabnormals.endergetic.common.items;

import com.minecraftabnormals.endergetic.client.models.armor.BoofloVestModel;
import com.minecraftabnormals.endergetic.core.EndergeticExpansion;
import com.minecraftabnormals.endergetic.core.registry.other.EEArmorMaterials;

import com.teamabnormals.abnormals_core.core.utils.ItemStackUtils;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

/**
 * @author - SmellyModder(Luke Tonon)
 */
public class BoofloVestItem extends ArmorItem {
	private static final String DEFAULT_TEXTURE = EndergeticExpansion.MOD_ID + ":textures/models/armor/booflo_vest.png";
	private static final String BOOFED_TEXTURE = EndergeticExpansion.MOD_ID + ":textures/models/armor/booflo_vest_boofed.png";
	public static final String TICKS_BOOFED_TAG = "ticksBoofed";
	public static final String BOOFED_TAG = "boofed";
	public static final String TIMES_BOOFED_TAG = "timesBoofed";

	public BoofloVestItem(Properties properties) {
		super(EEArmorMaterials.BOOFLO, EquipmentSlotType.CHEST, properties);
	}

	@Override
	public void onArmorTick(ItemStack stack, World world, PlayerEntity player) {
		CompoundNBT tag = stack.getOrCreateTag();
		int ticksBoofed = tag.getInt(TICKS_BOOFED_TAG);
		if (tag.getBoolean(BOOFED_TAG)) {
			ticksBoofed++;
			tag.putInt(TICKS_BOOFED_TAG, ticksBoofed);
		} else {
			tag.putInt(TICKS_BOOFED_TAG, 0);
		}

		if (ticksBoofed >= 10) {
			tag.putBoolean(BOOFED_TAG, false);
		}

		if (tag.getInt(TICKS_BOOFED_TAG) == 10) {
			player.getItemStackFromSlot(EquipmentSlotType.CHEST).damageItem(2, player, (onBroken) -> {
				onBroken.sendBreakAnimation(EquipmentSlotType.CHEST);
			});
		}

		if (player.isOnGround() || (player.isPassenger() && player.getRidingEntity().isOnGround())) {
			tag.putInt(TIMES_BOOFED_TAG, 0);
		}
	}

	@Override
	public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
		ItemStackUtils.fillAfterItemForGroup(this, Items.TURTLE_HELMET, group, items);
	}

	@Override
	public boolean isDamageable() {
		return true;
	}

	public static boolean canBoof(ItemStack stack, PlayerEntity player) {
		return !player.getCooldownTracker().hasCooldown(stack.getItem()) && !stack.getOrCreateTag().getBoolean(BOOFED_TAG);
	}

	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type) {
		return stack.hasTag() && stack.getTag().getBoolean(BOOFED_TAG) ? BOOFED_TEXTURE : DEFAULT_TEXTURE;
	}

	@SuppressWarnings({"unchecked"})
	@Override
	@Nullable
	@OnlyIn(Dist.CLIENT)
	public <A extends BipedModel<?>> A getArmorModel(LivingEntity wearer, ItemStack stack, EquipmentSlotType armorSlot, A _default) {
		return stack.hasTag() && stack.getTag().getBoolean(BOOFED_TAG) ? (A) BoofloVestModel.INSTANCE : null;
	}
}
