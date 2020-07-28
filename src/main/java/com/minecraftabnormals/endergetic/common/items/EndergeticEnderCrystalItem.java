package com.minecraftabnormals.endergetic.common.items;

import java.util.List;

import com.minecraftabnormals.endergetic.core.registry.other.EETags;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EnderCrystalEntity;
import net.minecraft.item.EnderCrystalItem;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.end.DragonFightManager;
import net.minecraft.world.server.ServerWorld;

public class EndergeticEnderCrystalItem extends EnderCrystalItem {

	public EndergeticEnderCrystalItem(Properties props) {
		super(props);
	}
	
	@Override
	public ActionResultType onItemUse(ItemUseContext context) {
		World world = context.getWorld();
		BlockPos blockpos = context.getPos();
		BlockState blockstate = world.getBlockState(blockpos);
		if(!blockstate.getBlock().isIn(EETags.Blocks.END_CRYSTAL_PLACEABLE)) {
			return ActionResultType.FAIL;
		} else {
			BlockPos blockpos1 = blockpos.up();
			if (!world.isAirBlock(blockpos1)) {
				return ActionResultType.FAIL;
			} else {
				double d0 = (double)blockpos1.getX();
				double d1 = (double)blockpos1.getY();
				double d2 = (double)blockpos1.getZ();
				List<Entity> list = world.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB(d0, d1, d2, d0 + 1.0D, d1 + 2.0D, d2 + 1.0D));
				if (!list.isEmpty()) {
					return ActionResultType.FAIL;
				} else {
					if (!world.isRemote) {
						EnderCrystalEntity endercrystalentity = new EnderCrystalEntity(world, d0 + 0.5D, d1, d2 + 0.5D);
						endercrystalentity.setShowBottom(false);
						world.addEntity(endercrystalentity);
						DragonFightManager dragonfightmanager = ((ServerWorld) world).func_241110_C_();
						if (dragonfightmanager != null) {
							dragonfightmanager.tryRespawnDragon();
						}
					}
				}
				context.getItem().shrink(1);
				return ActionResultType.SUCCESS;
			}
		}
	}

}
