package com.minecraftabnormals.endergetic.core.mixin;

import net.minecraft.util.RandomSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import com.minecraftabnormals.endergetic.core.registry.EEFeatures;

import net.minecraft.world.level.block.entity.TheEndGatewayBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.feature.configurations.EndGatewayConfiguration;
import net.minecraft.server.level.ServerLevel;

@Mixin(TheEndGatewayBlockEntity.class)
public abstract class EndGatewayTileEntityMixin {

	/**
	 * Overwrite since not overwriting it would have basically the same effect as a redirect.
	 */
	@Overwrite
	private static void spawnGatewayPortal(ServerLevel level, BlockPos pos, EndGatewayConfiguration configuration) {
		EEFeatures.ENDERGETIC_GATEWAY.get().place(configuration, level, level.getChunkSource().getGenerator(), RandomSource.create(), pos);
	}

}