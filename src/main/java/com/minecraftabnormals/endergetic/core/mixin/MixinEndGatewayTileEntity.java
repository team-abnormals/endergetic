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
public abstract class MixinEndGatewayTileEntity extends EndPortalTileEntity implements ITickableTileEntity {

	@Overwrite
	private void func_227016_a_(ServerWorld world, BlockPos pos) {
		EEFeatures.ENDERGETIC_GATEWAY.get().withConfiguration(EndGatewayConfig.func_214702_a(this.getPos(), false)).func_236265_a_(world, world.func_241112_a_(), world.getChunkProvider().getChunkGenerator(), new Random(), pos);
	}
	
}