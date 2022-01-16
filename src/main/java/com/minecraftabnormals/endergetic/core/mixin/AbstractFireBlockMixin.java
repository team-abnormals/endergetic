package com.minecraftabnormals.endergetic.core.mixin;

import com.minecraftabnormals.endergetic.common.blocks.EnderFireBlock;
import com.minecraftabnormals.endergetic.core.registry.EEBlocks;
import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractFireBlock.class)
public final class AbstractFireBlockMixin extends Block {

	private AbstractFireBlockMixin(Properties properties) {
		super(properties);
	}

	@Inject(at = @At("HEAD"), method = "getState(Lnet/minecraft/world/IBlockReader;Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/BlockState;", cancellable = true)
	private static void enderFirePlacement(IBlockReader reader, BlockPos pos, CallbackInfoReturnable<BlockState> info) {
		if (EnderFireBlock.isEnderFireBase(reader.getBlockState(pos.below()).getBlock())) {
			info.setReturnValue(EEBlocks.ENDER_FIRE.get().defaultBlockState());
		}
	}

}