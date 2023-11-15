package com.teamabnormals.endergetic.common.block.entity;

import com.teamabnormals.endergetic.core.registry.EEBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class CorrockCrownTileEntity extends BlockEntity {

	public CorrockCrownTileEntity(BlockPos pos, BlockState state) {
		super(EEBlockEntityTypes.CORROCK_CROWN.get(), pos, state);
	}

	@Override
	public AABB getRenderBoundingBox() {
		return INFINITE_EXTENT_AABB;
	}

}