package com.minecraftabnormals.endergetic.common.tileentities.boof;

import com.minecraftabnormals.endergetic.common.blocks.poise.boof.BoofBlock;
import com.minecraftabnormals.endergetic.common.entities.BoofBlockEntity;
import com.minecraftabnormals.endergetic.core.registry.EEBlocks;
import com.minecraftabnormals.endergetic.core.registry.EESounds;
import com.minecraftabnormals.endergetic.core.registry.EETileEntities;
import com.minecraftabnormals.endergetic.core.registry.other.EETags;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class BoofBlockTileEntity extends BlockEntity {

	public BoofBlockTileEntity(BlockPos pos, BlockState state) {
		super(EETileEntities.BOOF_BLOCK.get(), pos, state);
	}

	public static void tick(Level level, BlockPos pos, BlockState state, BoofBlockTileEntity boofBlock) {
		if (level.isClientSide || state.getValue(BoofBlock.BOOFED)) return;

		if (!level.getEntitiesOfClass(Entity.class, new AABB(pos).inflate(0.05F), entity -> (!(entity instanceof Player) || !entity.isShiftKeyDown()) && !entity.getType().is(EETags.EntityTypes.BOOF_BLOCK_RESISTANT)).isEmpty()) {
			if (level.addFreshEntity(new BoofBlockEntity(level, pos))) {
				level.setBlockAndUpdate(pos, EEBlocks.BOOF_BLOCK.get().defaultBlockState().setValue(BoofBlock.BOOFED, true));
				level.playSound(null, pos, EESounds.BOOF_BLOCK_INFLATE.get(), SoundSource.NEUTRAL, 1.0F, 1.0F);
			}
		}
	}

}