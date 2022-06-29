package com.minecraftabnormals.endergetic.core.mixin;

import com.minecraftabnormals.endergetic.core.registry.other.EETags;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.item.EndCrystalItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.end.EndDragonFight;
import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EndCrystalItem.class)
public final class EnderCrystalItemMixin {

	@Inject(at = @At("HEAD"), method = "useOn", cancellable = true)
	private void useOn(UseOnContext context, CallbackInfoReturnable<InteractionResult> info) {
		Level world = context.getLevel();
		BlockPos blockpos = context.getClickedPos();
		BlockState blockstate = world.getBlockState(blockpos);
		if (!blockstate.is(EETags.Blocks.END_CRYSTAL_PLACEABLE)) {
			info.setReturnValue(InteractionResult.FAIL);
		} else {
			BlockPos blockpos1 = blockpos.above();
			if (!world.isEmptyBlock(blockpos1)) {
				info.setReturnValue(InteractionResult.FAIL);
			} else {
				double d0 = blockpos1.getX();
				double d1 = blockpos1.getY();
				double d2 = blockpos1.getZ();
				if (!world.getEntities(null, new AABB(d0, d1, d2, d0 + 1.0D, d1 + 2.0D, d2 + 1.0D)).isEmpty()) {
					info.setReturnValue(InteractionResult.FAIL);
				} else {
					if (world instanceof ServerLevel) {
						EndCrystal endercrystalentity = new EndCrystal(world, d0 + 0.5D, d1, d2 + 0.5D);
						endercrystalentity.setShowBottom(false);
						world.addFreshEntity(endercrystalentity);
						EndDragonFight dragonfightmanager = ((ServerLevel) world).dragonFight();
						if (dragonfightmanager != null) {
							dragonfightmanager.tryRespawn();
						}
					}
					context.getItemInHand().shrink(1);
					info.setReturnValue(InteractionResult.sidedSuccess(world.isClientSide));
				}
			}
		}
	}

}
