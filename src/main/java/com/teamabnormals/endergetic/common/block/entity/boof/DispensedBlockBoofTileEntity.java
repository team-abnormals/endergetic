package com.teamabnormals.endergetic.common.block.entity.boof;

import com.teamabnormals.endergetic.common.block.poise.boof.DispensedBoofBlock;
import com.teamabnormals.endergetic.core.registry.EEBlockEntityTypes;
import com.teamabnormals.endergetic.core.registry.other.tags.EEEntityTypeTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class DispensedBlockBoofTileEntity extends BlockEntity {
	private int ticksExisted;

	public DispensedBlockBoofTileEntity(BlockPos pos, BlockState state) {
		super(EEBlockEntityTypes.BOOF_BLOCK_DISPENSED.get(), pos, state);
	}

	public static void tick(Level level, BlockPos pos, BlockState state, DispensedBlockBoofTileEntity dispensed) {
		dispensed.ticksExisted++;

		AABB bb = new AABB(pos).inflate(0.1F, 0.1F, 0.1F);
		List<Entity> entities = level.getEntitiesOfClass(Entity.class, bb, entity -> !entity.getType().is(EEEntityTypeTags.BOOF_BLOCK_RESISTANT));

		Direction facing = state.getValue(DispensedBoofBlock.FACING);
		for (Entity entity : entities) {
			if (facing == Direction.UP) {
				entity.push(Mth.sin((float) (entity.getYRot() * Math.PI / 180.0F)) * 0.1F * 0.1F, 0.25D, -Mth.cos((float) (entity.getYRot() * Math.PI / 180.0F)) * 0.1F * 0.1F);
			} else if (facing == Direction.DOWN) {
				entity.push(Mth.sin((float) (entity.getYRot() * Math.PI / 180.0F)) * 0.1F * 0.1F, -0.45D, -Mth.cos((float) (entity.getYRot() * Math.PI / 180.0F)) * 0.1F * 0.1F);
			} else {
				entity.push(Mth.sin((float) (entity.getYRot() * Math.PI / 180.0F)) * 3F * 0.1F, 0.45D, -Mth.cos((float) (entity.getYRot() * Math.PI / 180.0F)) * 3F * 0.1F);
			}
		}

		if (dispensed.ticksExisted >= 10) {
			level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
		}
	}
}