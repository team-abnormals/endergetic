package com.teamabnormals.endergetic.common.block.entity.boof;

import com.teamabnormals.endergetic.common.block.poise.boof.BoofBlock;
import com.teamabnormals.endergetic.common.entity.BoofBlockEntity;
import com.teamabnormals.endergetic.core.registry.EEBlockEntityTypes;
import com.teamabnormals.endergetic.core.registry.EEBlocks;
import com.teamabnormals.endergetic.core.registry.EESoundEvents;
import com.teamabnormals.endergetic.core.registry.other.tags.EEEntityTypeTags;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class BoofBlockTileEntity extends BlockEntity {

	public BoofBlockTileEntity(BlockPos pos, BlockState state) {
		super(EEBlockEntityTypes.BOOF_BLOCK.get(), pos, state);
	}

	public static void tick(Level level, BlockPos pos, BlockState state, BoofBlockTileEntity boofBlock) {
		if (level.isClientSide || state.getValue(BoofBlock.BOOFED)) return;

		if (!level.getEntitiesOfClass(Entity.class, new AABB(pos).inflate(0.05F), entity -> (!(entity instanceof Player) || !entity.isShiftKeyDown()) && !entity.getType().is(EEEntityTypeTags.BOOF_BLOCK_RESISTANT)).isEmpty()) {
			if (level.addFreshEntity(new BoofBlockEntity(level, pos))) {
				level.setBlockAndUpdate(pos, EEBlocks.BOOF_BLOCK.get().defaultBlockState().setValue(BoofBlock.BOOFED, true));
				level.playSound(null, pos, EESoundEvents.BOOF_BLOCK_INFLATE.get(), SoundSource.NEUTRAL, 1.0F, 1.0F);
			}
		}
	}

}