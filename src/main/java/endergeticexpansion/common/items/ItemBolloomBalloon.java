package endergeticexpansion.common.items;

import endergeticexpansion.common.entities.bolloom.EntityBolloomKnot;
import net.minecraft.block.Block;
import net.minecraft.block.FenceBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemBolloomBalloon extends Item {

	public ItemBolloomBalloon(Properties properties) {
		super(properties);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
		ItemStack itemstack = playerIn.getHeldItem(handIn);
		return new ActionResult<>(ActionResultType.SUCCESS, itemstack);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public ActionResultType onItemUse(ItemUseContext context) {
		BlockPos pos = context.getPos();
		World world = context.getWorld();
		Block block = world.getBlockState(pos).getBlock();
		
		if(!(block instanceof FenceBlock)) {
			return ActionResultType.FAIL;
		} else {
			if(world.getBlockState(pos.up()).isAir() && world.getBlockState(pos.up(2)).isAir() && world.getBlockState(pos.up(3)).isAir()) {
				if(!world.isRemote) {
					this.attachToFence(pos, world, context.getItem());
				}
			} else {
				return ActionResultType.FAIL;
			}
			for (Entity entity : world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(pos))) {
				if(entity instanceof EntityBolloomKnot) {
					if(((EntityBolloomKnot)entity).hasMaxBalloons()) {
						return ActionResultType.FAIL;
					}
				}
	        }
			return ActionResultType.PASS;
		}
	}

	public void attachToFence(BlockPos fencePos, World world, ItemStack stack) {
		for (Entity entity : world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(fencePos))) {
			if(entity instanceof EntityBolloomKnot) {
				if(!((EntityBolloomKnot)entity).hasMaxBalloons()) {
					EntityBolloomKnot setKnot = EntityBolloomKnot.getKnotForPosition(world, fencePos);
					setKnot.addBalloon();
					stack.shrink(1);
				}
			}
        }
		if(EntityBolloomKnot.getKnotForPosition(world, fencePos) == null) {
			EntityBolloomKnot.createStartingKnot(world, fencePos);
			stack.shrink(1);
		}
	}
	
}
