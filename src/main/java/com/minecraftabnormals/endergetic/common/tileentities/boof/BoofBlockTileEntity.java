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
		if (this.world.isRemote || this.world.getBlockState(this.pos).get(BoofBlock.BOOFED)) return;

		if (!this.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(this.getPos()).grow(0.05F), entity -> (!(entity instanceof PlayerEntity) || !entity.isSneaking()) && !EETags.EntityTypes.BOOF_BLOCK_RESISTANT.contains(entity.getType())).isEmpty()) {
			if (this.world.addEntity(new BoofBlockEntity(this.world, this.pos))) {
				this.world.setBlockState(this.pos, EEBlocks.BOOF_BLOCK.get().getDefaultState().with(BoofBlock.BOOFED, true));
				this.world.playSound(null, this.pos, EESounds.BOOF_BLOCK_INFLATE.get(), SoundCategory.NEUTRAL, 1.0F, 1.0F);
			}
		}
	}

}