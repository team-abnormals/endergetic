package com.minecraftabnormals.endergetic.core.mixin;

import java.util.Random;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import com.minecraftabnormals.endergetic.common.world.features.EEFeatures;

import net.minecraft.tileentity.EndGatewayTileEntity;
import net.minecraft.tileentity.EndPortalTileEntity;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.feature.EndGatewayConfig;
import net.minecraft.world.server.ServerWorld;

@Mixin(EndGatewayTileEntity.class)
public abstract class EndGatewayTileEntityMixin extends EndPortalTileEntity implements ITickableTileEntity {

	/**
	 * Overrite since not overwriting it would have basically the same effect as a redirect.
	 */
	@Overwrite
	private void createExitPortal(ServerWorld world, BlockPos pos) {
		EEFeatures.ENDERGETIC_GATEWAY.get().configured(EndGatewayConfig.knownExit(this.getBlockPos(), false)).place(world, world.getChunkSource().getGenerator(), new Random(), pos);
	}

}