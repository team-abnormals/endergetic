package com.minecraftabnormals.endergetic.common.tileentities.boof;

import java.util.List;

import com.minecraftabnormals.endergetic.common.blocks.poise.boof.DispensedBoofBlock;
import com.minecraftabnormals.endergetic.core.registry.EETileEntities;
import com.minecraftabnormals.endergetic.core.registry.other.EETags;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.AABB;
import net.minecraft.util.Mth;

public class DispensedBlockBoofTileEntity extends BlockEntity implements TickableBlockEntity {
	private int ticksExisted;

	public DispensedBlockBoofTileEntity() {
		super(EETileEntities.BOOF_BLOCK_DISPENSED.get());
	}

	@Override
	public void tick() {
		this.ticksExisted++;

		AABB bb = new AABB(this.worldPosition).inflate(0.1F, 0.1F, 0.1F);
		List<Entity> entities = this.level.getEntitiesOfClass(Entity.class, bb, (entity -> !EETags.EntityTypes.BOOF_BLOCK_RESISTANT.contains(entity.getType())));

		Direction facing = this.getBlockState().getValue(DispensedBoofBlock.FACING);
		for (int i = 0; i < entities.size(); i++) {
			Entity entity = entities.get(i);

			if (facing == Direction.UP) {
				entity.push(Mth.sin((float) (entity.yRot * Math.PI / 180.0F)) * 0.1F * 0.1F, 0.25D, -Mth.cos((float) (entity.yRot * Math.PI / 180.0F)) * 0.1F * 0.1F);
			} else if (facing == Direction.DOWN) {
				entity.push(Mth.sin((float) (entity.yRot * Math.PI / 180.0F)) * 0.1F * 0.1F, -0.45D, -Mth.cos((float) (entity.yRot * Math.PI / 180.0F)) * 0.1F * 0.1F);
			} else {
				entity.push(Mth.sin((float) (entity.yRot * Math.PI / 180.0F)) * 3F * 0.1F, 0.45D, -Mth.cos((float) (entity.yRot * Math.PI / 180.0F)) * 3F * 0.1F);
			}
		}

		if (this.ticksExisted >= 10) {
			this.level.setBlockAndUpdate(this.worldPosition, Blocks.AIR.defaultBlockState());
		}
	}
}