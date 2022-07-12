package com.teamabnormals.endergetic.common.tileentities;

import com.teamabnormals.endergetic.core.registry.EETileEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class CorrockCrownTileEntity extends BlockEntity {

	public CorrockCrownTileEntity(BlockPos pos, BlockState state) {
		super(EETileEntities.CORROCK_CROWN.get(), pos, state);
	}

	@Override
	public AABB getRenderBoundingBox() {
		return super.getRenderBoundingBox().inflate(8.0D);
	}

}