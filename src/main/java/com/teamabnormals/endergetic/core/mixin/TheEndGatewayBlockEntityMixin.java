package com.teamabnormals.endergetic.core.mixin;

import com.teamabnormals.endergetic.core.registry.EEFeatures;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.entity.TheEndGatewayBlockEntity;
import net.minecraft.world.level.levelgen.feature.configurations.EndGatewayConfiguration;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(TheEndGatewayBlockEntity.class)
public abstract class TheEndGatewayBlockEntityMixin {

	/**
	 * Overwrite since not overwriting it would have basically the same effect as a redirect.
	 */
	@Overwrite
	private static void spawnGatewayPortal(ServerLevel level, BlockPos pos, EndGatewayConfiguration configuration) {
		EEFeatures.ENDERGETIC_END_GATEWAY.get().place(configuration, level, level.getChunkSource().getGenerator(), RandomSource.create(), pos);
	}

}