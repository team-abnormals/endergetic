package com.minecraftabnormals.endergetic.common.tileentities.boof;

import java.util.List;

import com.minecraftabnormals.endergetic.common.blocks.poise.boof.DispensedBoofBlock;
import com.minecraftabnormals.endergetic.core.registry.EETileEntities;
import com.minecraftabnormals.endergetic.core.registry.other.EETags;

import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;

public class DispensedBlockBoofTileEntity extends TileEntity implements ITickableTileEntity {
	private int ticksExisted;

	public DispensedBlockBoofTileEntity() {
		super(EETileEntities.BOOF_BLOCK_DISPENSED.get());
	}

	@Override
	public void tick() {
		this.ticksExisted++;

		AxisAlignedBB bb = new AxisAlignedBB(this.worldPosition).inflate(0.1F, 0.1F, 0.1F);
		List<Entity> entities = this.level.getEntitiesOfClass(Entity.class, bb, (entity -> !EETags.EntityTypes.BOOF_BLOCK_RESISTANT.contains(entity.getType())));

		Direction facing = this.getBlockState().getValue(DispensedBoofBlock.FACING);
		for (int i = 0; i < entities.size(); i++) {
			Entity entity = entities.get(i);

			if (facing == Direction.UP) {
				entity.push(MathHelper.sin((float) (entity.yRot * Math.PI / 180.0F)) * 0.1F * 0.1F, 0.25D, -MathHelper.cos((float) (entity.yRot * Math.PI / 180.0F)) * 0.1F * 0.1F);
			} else if (facing == Direction.DOWN) {
				entity.push(MathHelper.sin((float) (entity.yRot * Math.PI / 180.0F)) * 0.1F * 0.1F, -0.45D, -MathHelper.cos((float) (entity.yRot * Math.PI / 180.0F)) * 0.1F * 0.1F);
			} else {
				entity.push(MathHelper.sin((float) (entity.yRot * Math.PI / 180.0F)) * 3F * 0.1F, 0.45D, -MathHelper.cos((float) (entity.yRot * Math.PI / 180.0F)) * 3F * 0.1F);
			}
		}

		if (this.ticksExisted >= 10) {
			this.level.setBlockAndUpdate(this.worldPosition, Blocks.AIR.defaultBlockState());
		}
	}
}