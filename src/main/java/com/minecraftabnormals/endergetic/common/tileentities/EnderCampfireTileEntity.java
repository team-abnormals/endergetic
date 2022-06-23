package com.minecraftabnormals.endergetic.common.tileentities;

import com.minecraftabnormals.endergetic.core.registry.EETileEntities;

import net.minecraft.world.level.block.entity.CampfireBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class EnderCampfireTileEntity extends CampfireBlockEntity {

	public EnderCampfireTileEntity() {
		super();
	}

	public BlockEntityType<?> getType() {
		return EETileEntities.ENDER_CAMPFIRE.get();
	}
}
