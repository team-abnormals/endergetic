package com.minecraftabnormals.endergetic.common.blocks;

import com.minecraftabnormals.endergetic.client.particles.EEParticles;
import com.minecraftabnormals.endergetic.client.particles.data.CorrockCrownParticleData;
import com.minecraftabnormals.endergetic.common.tileentities.CorrockCrownTileEntity;

import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.block.IBucketPickupHandler;
import net.minecraft.block.ILiquidContainer;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.particles.ParticleType;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;

import java.util.function.Supplier;

import net.minecraft.block.AbstractBlock.Properties;

public abstract class CorrockCrownBlock extends ContainerBlock implements IBucketPickupHandler, ILiquidContainer {
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	protected final DimensionalType dimensionalType;
	public final boolean petrified;

	protected CorrockCrownBlock(Properties builder, DimensionalType dimensionalType, boolean petrified) {
		super(builder);
		this.dimensionalType = dimensionalType;
		this.petrified = petrified;
	}

	@Override
	public TileEntity newBlockEntity(IBlockReader worldIn) {
		return new CorrockCrownTileEntity();
	}

	public Fluid takeLiquid(IWorld worldIn, BlockPos pos, BlockState state) {
		if (state.getValue(WATERLOGGED)) {
			worldIn.setBlock(pos, state.setValue(WATERLOGGED, Boolean.FALSE), 3);
			return Fluids.WATER;
		} else {
			return Fluids.EMPTY;
		}
	}

	@SuppressWarnings("deprecation")
	public FluidState getFluidState(BlockState state) {
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}

	public boolean canPlaceLiquid(IBlockReader worldIn, BlockPos pos, BlockState state, Fluid fluidIn) {
		return !state.getValue(WATERLOGGED) && fluidIn == Fluids.WATER;
	}

	public boolean placeLiquid(IWorld worldIn, BlockPos pos, BlockState state, FluidState fluidStateIn) {
		if (!state.getValue(WATERLOGGED) && fluidStateIn.getType() == Fluids.WATER) {
			if (!worldIn.isClientSide()) {
				worldIn.setBlock(pos, state.setValue(WATERLOGGED, Boolean.TRUE), 3);
				worldIn.getLiquidTicks().scheduleTick(pos, fluidStateIn.getType(), fluidStateIn.getType().getTickDelay(worldIn));
			}
			return true;
		} else {
			return false;
		}
	}

	@Override
	public BlockRenderType getRenderShape(BlockState state) {
		return BlockRenderType.ENTITYBLOCK_ANIMATED;
	}

	public enum DimensionalType {
		OVERWORLD(EEParticles.OVERWORLD_CROWN),
		NETHER(EEParticles.NETHER_CROWN),
		END(EEParticles.END_CROWN);

		protected final Supplier<ParticleType<CorrockCrownParticleData>> particle;

		DimensionalType(Supplier<ParticleType<CorrockCrownParticleData>> particle) {
			this.particle = particle;
		}
	}
}