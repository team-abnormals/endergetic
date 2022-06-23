package com.minecraftabnormals.endergetic.common.tileentities.boof;

import com.minecraftabnormals.endergetic.common.blocks.poise.boof.BoofBlock;
import com.minecraftabnormals.endergetic.common.entities.BoofBlockEntity;
import com.minecraftabnormals.endergetic.core.registry.EEBlocks;
import com.minecraftabnormals.endergetic.core.registry.EESounds;
import com.minecraftabnormals.endergetic.core.registry.EETileEntities;
import com.minecraftabnormals.endergetic.core.registry.other.EETags;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.phys.AABB;

public class BoofBlockTileEntity extends BlockEntity implements TickableBlockEntity {

	public BoofBlockTileEntity() {
		super(EETileEntities.BOOF_BLOCK.get());
	}

	@Override
	public void tick() {
		if (this.level.isClientSide || this.level.getBlockState(this.worldPosition).getValue(BoofBlock.BOOFED)) return;

		if (!this.level.getEntitiesOfClass(Entity.class, new AABB(this.getBlockPos()).inflate(0.05F), entity -> (!(entity instanceof Player) || !entity.isShiftKeyDown()) && !EETags.EntityTypes.BOOF_BLOCK_RESISTANT.contains(entity.getType())).isEmpty()) {
			if (this.level.addFreshEntity(new BoofBlockEntity(this.level, this.worldPosition))) {
				this.level.setBlockAndUpdate(this.worldPosition, EEBlocks.BOOF_BLOCK.get().defaultBlockState().setValue(BoofBlock.BOOFED, true));
				this.level.playSound(null, this.worldPosition, EESounds.BOOF_BLOCK_INFLATE.get(), SoundSource.NEUTRAL, 1.0F, 1.0F);
			}
		}
	}

}