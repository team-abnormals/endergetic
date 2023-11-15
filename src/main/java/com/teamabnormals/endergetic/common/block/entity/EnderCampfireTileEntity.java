package com.teamabnormals.endergetic.common.block.entity;

import com.teamabnormals.endergetic.core.registry.EEBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.CampfireBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class EnderCampfireTileEntity extends CampfireBlockEntity {

	public EnderCampfireTileEntity(BlockPos pos, BlockState state) {
		super(pos, state);
	}

	@Override
	public BlockEntityType<?> getType() {
		return EEBlockEntityTypes.ENDER_CAMPFIRE.get();
	}

}
