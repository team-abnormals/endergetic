package com.minecraftabnormals.endergetic.core.mixin;

import java.util.Random;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import com.minecraftabnormals.endergetic.common.world.features.EEFeatures;

import net.minecraft.world.level.block.entity.TheEndGatewayBlockEntity;
import net.minecraft.world.level.block.entity.TheEndPortalBlockEntity;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.feature.configurations.EndGatewayConfiguration;
import net.minecraft.server.level.ServerLevel;

@Mixin(TheEndGatewayBlockEntity.class)
public abstract class EndGatewayTileEntityMixin extends TheEndPortalBlockEntity implements TickableBlockEntity {

	/**
	 * Overrite since not overwriting it would have basically the same effect as a redirect.
	 */
	@Overwrite
	private void createExitPortal(ServerLevel world, BlockPos pos) {
		EEFeatures.ENDERGETIC_GATEWAY.get().configured(EndGatewayConfiguration.knownExit(this.getBlockPos(), false)).place(world, world.getChunkSource().getGenerator(), new Random(), pos);
	}

}