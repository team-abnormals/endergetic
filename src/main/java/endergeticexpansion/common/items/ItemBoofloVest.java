package endergeticexpansion.common.items;

import java.util.List;

import endergeticexpansion.api.util.NetworkUtil;
import endergeticexpansion.core.EndergeticExpansion;
import endergeticexpansion.core.registry.other.EEArmorMaterials;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ItemBoofloVest extends ArmorItem {

	public ItemBoofloVest(Properties properties) {
		super(EEArmorMaterials.BOOFLO, EquipmentSlotType.CHEST, properties);
	}
	
	@Override
	public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		if(entityIn instanceof PlayerEntity) {
			if(((PlayerEntity)entityIn).onGround && !((PlayerEntity)entityIn).isElytraFlying()) {
				if(this.hasTag(stack)) {
					stack.getTag().putInt("timesBoofed", 0);
				}
			}
		}
		if(this.hasTag(stack)) {
			if(stack.getTag().getBoolean("boofed")) {
				stack.getTag().putInt("ticksBoofed", stack.getTag().getInt("ticksBoofed") + 1);
			} else {
				stack.getTag().putInt("ticksBoofed", 0);
			}
			if(stack.getTag().getInt("ticksBoofed") >= 10) {
				stack.getTag().putBoolean("boofed", false);
			}
		}
	}
	
	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		if (hasTag(stack)) {
			tooltip.add(new TranslationTextComponent("hmm yess", stack.getTag().getInt("timesBoofed")));
		}
	}
	
	public boolean hasTag(ItemStack stack) {
		if(!stack.hasTag()) {
			stack.setTag(new CompoundNBT());
			return false;
		}
		return true;
	}
	
	public boolean canBoof(ItemStack stack, PlayerEntity player) {
		if(this.hasTag(stack)) {
			return !player.getCooldownTracker().hasCooldown(stack.getItem()) && !stack.getTag().getBoolean("boofed");
		}
		return !player.getCooldownTracker().hasCooldown(stack.getItem());
	}
	
	public void setDelayForBoofedAmount(ItemStack stack, PlayerEntity player) {
		if(stack.hasTag()) {
			if(stack.getTag().getInt("timesBoofed") == 1) {
				NetworkUtil.setSItemCooldown(stack, 10, true);
			}
			else if(stack.getTag().getInt("timesBoofed") == 2) {
				NetworkUtil.setSItemCooldown(stack, 20, true);
			}
			else if(stack.getTag().getInt("timesBoofed") == 3) {
				NetworkUtil.setSItemCooldown(stack, 40, true);
			} 
			else if(stack.getTag().getInt("timesBoofed") >= 4) {
				NetworkUtil.setSItemCooldown(stack, 100, true);
			}
		}
	}
	
	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type) {
		if(stack.hasTag()) {
			if(stack.getTag().getBoolean("boofed")) {
				return EndergeticExpansion.MOD_ID + ":textures/models/armor/booflo_vest_boofed.png";
			}
		}
		return EndergeticExpansion.MOD_ID + ":textures/models/armor/booflo_vest.png";
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public <A extends BipedModel<?>> A getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlotType armorSlot, A _default) {
		return super.getArmorModel(entityLiving, itemStack, armorSlot, _default);
	}
	
}
