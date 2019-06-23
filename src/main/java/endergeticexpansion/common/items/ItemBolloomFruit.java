package endergeticexpansion.common.items;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemBolloomFruit extends Item {

	public ItemBolloomFruit(Properties properties) {
		super(properties);
	}

	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
		ItemStack itemstack = super.onItemUseFinish(stack, worldIn, entityLiving);
		
		if (!worldIn.isRemote) {
			if (entityLiving instanceof PlayerEntity) {
	            ((PlayerEntity)entityLiving).getCooldownTracker().setCooldown(this, 25);
	         }
		}
		
		return itemstack;
	}
	
}
