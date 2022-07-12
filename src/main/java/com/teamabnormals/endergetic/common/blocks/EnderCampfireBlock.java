package com.teamabnormals.endergetic.common.blocks;

import java.util.Random;

import com.teamabnormals.endergetic.common.tileentities.EnderCampfireTileEntity;
import com.teamabnormals.endergetic.core.registry.EETileEntities;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.CampfireBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import org.jetbrains.annotations.Nullable;

public class EnderCampfireBlock extends CampfireBlock {

	public EnderCampfireBlock(Properties properties) {
		super(false, 3, properties);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new EnderCampfireTileEntity(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level p_152755_, BlockState p_152756_, BlockEntityType<T> p_152757_) {
		if (p_152755_.isClientSide) {
			return p_152756_.getValue(LIT) ? createTickerHelper(p_152757_, EETileEntities.ENDER_CAMPFIRE.get(), CampfireBlockEntity::particleTick) : null;
		} else {
			return p_152756_.getValue(LIT) ? createTickerHelper(p_152757_, EETileEntities.ENDER_CAMPFIRE.get(), CampfireBlockEntity::cookTick) : createTickerHelper(p_152757_, BlockEntityType.CAMPFIRE, CampfireBlockEntity::cooldownTick);
		}
	}

	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState stateIn, Level worldIn, BlockPos pos, Random rand) {
		if (stateIn.getValue(LIT)) {
			if (rand.nextInt(10) == 0) {
				worldIn.playLocalSound((float) pos.getX() + 0.5F, (float) pos.getY() + 0.5F, (float) pos.getZ() + 0.5F, SoundEvents.CAMPFIRE_CRACKLE, SoundSource.BLOCKS, 0.5F + rand.nextFloat(),
						rand.nextFloat() * 0.7F + 0.6F, false);
			}
		}
	}

}
