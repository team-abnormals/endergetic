package com.teamabnormals.endergetic.common.tileentities;

import com.teamabnormals.endergetic.core.registry.EETileEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.CampfireBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class EnderCampfireTileEntity extends CampfireBlockEntity {

	public EnderCampfireTileEntity(BlockPos pos, BlockState state) {
		super(pos, state);
	}

	@Override
	public BlockEntityType<?> getType() {
		return EETileEntities.ENDER_CAMPFIRE.get();
	}

}
