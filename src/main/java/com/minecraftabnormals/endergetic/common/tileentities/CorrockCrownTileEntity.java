package com.minecraftabnormals.endergetic.common.tileentities;

import com.minecraftabnormals.endergetic.core.registry.EETileEntities;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;

public class CorrockCrownTileEntity extends BlockEntity {

	public CorrockCrownTileEntity() {
		super(EETileEntities.CORROCK_CROWN.get());
	}

	@Override
	public AABB getRenderBoundingBox() {
		return super.getRenderBoundingBox().inflate(8.0D);
	}

	@Override
	public double getViewDistance() {
		return super.getViewDistance() * 10;
	}

}