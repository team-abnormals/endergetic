package com.minecraftabnormals.endergetic.common.items;

import com.minecraftabnormals.endergetic.api.util.EndergeticNetworkUtil;
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

/**
 * @author - SmellyModder(Luke Tonon)
 */
public class BoofloVestItem extends ArmorItem {
	private static final String TICKS_BOOFED_TAG = "ticksBoofed";
	public static final String BOOFED_TAG = "boofed";
	public static final String TIMES_BOOFED_TAG = "timesBoofed";

	public BoofloVestItem(Properties properties) {
		super(EEArmorMaterials.BOOFLO, EquipmentSlotType.CHEST, properties);
	}

	@Override
	public void onArmorTick(ItemStack stack, World world, PlayerEntity player) {
		if (this.hasTag(stack)) {
			CompoundNBT tag = stack.getTag();
			int ticksBoofed = tag.getInt(TICKS_BOOFED_TAG);
			if (tag.getBoolean(BOOFED_TAG)) {
				tag.putInt(TICKS_BOOFED_TAG, ticksBoofed + 1);
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
	}

	@Override
	public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
		ItemStackUtils.fillAfterItemForGroup(this, Items.TURTLE_HELMET, group, items);
	}

	@Override
	public boolean isDamageable() {
		return true;
	}

	public boolean hasTag(ItemStack stack) {
		if (!stack.hasTag()) {
			stack.setTag(new CompoundNBT());
			return false;
		}
		return true;
	}

	public boolean canBoof(ItemStack stack, PlayerEntity player) {
		if (this.hasTag(stack)) {
			return !player.getCooldownTracker().hasCooldown(stack.getItem()) && !stack.getTag().getBoolean(BOOFED_TAG);
		}
		return !player.getCooldownTracker().hasCooldown(stack.getItem());
	}

	public void setDelayForBoofedAmount(ItemStack stack, PlayerEntity player) {
		EndergeticNetworkUtil.setSItemCooldown(stack, 100, true);
	}

	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type) {
		if (stack.hasTag()) {
			if (stack.getTag().getBoolean(BOOFED_TAG)) {
				return EndergeticExpansion.MOD_ID + ":textures/models/armor/booflo_vest_boofed.png";
			}
		}
		return EndergeticExpansion.MOD_ID + ":textures/models/armor/booflo_vest.png";
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	@Override
	@OnlyIn(Dist.CLIENT)
	public <A extends BipedModel<?>> A getArmorModel(LivingEntity wearer, ItemStack stack, EquipmentSlotType armorSlot, A _default) {
		if (stack.hasTag()) {
			if (stack.getTag().getBoolean(BOOFED_TAG)) {
				return (A) new BoofloVestModel(wearer, 1.0F);
			}
		}
		return super.getArmorModel(wearer, stack, armorSlot, _default);
	}
}
