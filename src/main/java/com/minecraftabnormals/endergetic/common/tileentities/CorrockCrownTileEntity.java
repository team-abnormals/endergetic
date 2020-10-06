package com.minecraftabnormals.endergetic.common.tileentities;

import com.minecraftabnormals.endergetic.core.registry.EETileEntities;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;

public class CorrockCrownTileEntity extends TileEntity {

	public CorrockCrownTileEntity() {
		super(EETileEntities.CORROCK_CROWN.get());
	}

	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		return super.getRenderBoundingBox().grow(8.0D);
	}

	@Override
	public double getMaxRenderDistanceSquared() {
		return super.getMaxRenderDistanceSquared() * 10;
	}

}