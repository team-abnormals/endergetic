package com.minecraftabnormals.endergetic.core.mixin;

import com.minecraftabnormals.endergetic.core.registry.other.EETags;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.EnderCrystalEntity;
import net.minecraft.item.EnderCrystalItem;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.end.DragonFightManager;
import net.minecraft.world.server.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnderCrystalItem.class)
public final class EnderCrystalItemMixin {

	@Inject(at = @At("HEAD"), method = "useOn", cancellable = true)
	private void onItemUse(ItemUseContext context, CallbackInfoReturnable<ActionResultType> info) {
		World world = context.getLevel();
		BlockPos blockpos = context.getClickedPos();
		BlockState blockstate = world.getBlockState(blockpos);
		if (!blockstate.getBlock().is(EETags.Blocks.END_CRYSTAL_PLACEABLE)) {
			info.setReturnValue(ActionResultType.FAIL);
		} else {
			BlockPos blockpos1 = blockpos.above();
			if (!world.isEmptyBlock(blockpos1)) {
				info.setReturnValue(ActionResultType.FAIL);
			} else {
				double d0 = blockpos1.getX();
				double d1 = blockpos1.getY();
				double d2 = blockpos1.getZ();
				if (!world.getEntities(null, new AxisAlignedBB(d0, d1, d2, d0 + 1.0D, d1 + 2.0D, d2 + 1.0D)).isEmpty()) {
					info.setReturnValue(ActionResultType.FAIL);
				} else {
					if (world instanceof ServerWorld) {
						EnderCrystalEntity endercrystalentity = new EnderCrystalEntity(world, d0 + 0.5D, d1, d2 + 0.5D);
						endercrystalentity.setShowBottom(false);
						world.addFreshEntity(endercrystalentity);
						DragonFightManager dragonfightmanager = ((ServerWorld) world).dragonFight();
						if (dragonfightmanager != null) {
							dragonfightmanager.tryRespawn();
						}
					}
					context.getItemInHand().shrink(1);
					info.setReturnValue(ActionResultType.sidedSuccess(world.isClientSide));
				}
			}
		}
	}

}
