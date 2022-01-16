package com.minecraftabnormals.endergetic.common.tileentities.boof;

import com.minecraftabnormals.endergetic.common.blocks.poise.boof.BoofBlock;
import com.minecraftabnormals.endergetic.common.entities.BoofBlockEntity;
import com.minecraftabnormals.endergetic.core.registry.EEBlocks;
import com.minecraftabnormals.endergetic.core.registry.EESounds;
import com.minecraftabnormals.endergetic.core.registry.EETileEntities;
import com.minecraftabnormals.endergetic.core.registry.other.EETags;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;

public class BoofBlockTileEntity extends TileEntity implements ITickableTileEntity {

	public BoofBlockTileEntity() {
		super(EETileEntities.BOOF_BLOCK.get());
	}

	@Override
	public void tick() {
		if (this.level.isClientSide || this.level.getBlockState(this.worldPosition).getValue(BoofBlock.BOOFED)) return;

		if (!this.level.getEntitiesOfClass(Entity.class, new AxisAlignedBB(this.getBlockPos()).inflate(0.05F), entity -> (!(entity instanceof PlayerEntity) || !entity.isShiftKeyDown()) && !EETags.EntityTypes.BOOF_BLOCK_RESISTANT.contains(entity.getType())).isEmpty()) {
			if (this.level.addFreshEntity(new BoofBlockEntity(this.level, this.worldPosition))) {
				this.level.setBlockAndUpdate(this.worldPosition, EEBlocks.BOOF_BLOCK.get().defaultBlockState().setValue(BoofBlock.BOOFED, true));
				this.level.playSound(null, this.worldPosition, EESounds.BOOF_BLOCK_INFLATE.get(), SoundCategory.NEUTRAL, 1.0F, 1.0F);
			}
		}
	}

}