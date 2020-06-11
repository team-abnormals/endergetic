package endergeticexpansion.common.blocks;

import java.util.Map;
import java.util.Random;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import com.google.common.collect.Maps;

import endergeticexpansion.core.events.PlayerEvents;
import endergeticexpansion.core.registry.EEBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ToolType;

public class CorrockBlock extends Block {
	private static final Map<DimensionType, Supplier<Block>> CONVERSIONS = Util.make(Maps.newHashMap(), (conversions) -> {
		conversions.put(DimensionType.OVERWORLD, () -> EEBlocks.CORROCK_OVERWORLD_BLOCK.get());
		conversions.put(DimensionType.THE_NETHER, () -> EEBlocks.CORROCK_NETHER_BLOCK.get());
		conversions.put(DimensionType.THE_END, () -> EEBlocks.CORROCK_END_BLOCK.get());
	});
	public final boolean petrified;
	
	public CorrockBlock(Properties properties, boolean petrified) {
		super(properties);
		this.petrified = petrified;
	}
	
	@Override
	public ToolType getHarvestTool(BlockState state) {
		return ToolType.PICKAXE;
	}
	
	@Override
	public void tick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		if(!this.petrified && !this.isInProperDimension(world)) {
			world.setBlockState(pos, CONVERSIONS.getOrDefault(world.getDimension().getType(), EEBlocks.CORROCK_OVERWORLD_BLOCK).get().getDefaultState());
		}
	}
	
	@Override
	public SoundType getSoundType(BlockState state, IWorldReader world, BlockPos pos, Entity entity) {
		return SoundType.CORAL;
	}
	
	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
		if (!this.isInProperDimension(worldIn.getWorld())) {
			worldIn.getPendingBlockTicks().scheduleTick(currentPos, this, 60 + worldIn.getRandom().nextInt(40));
		}
		
		if(this.isSubmerged(worldIn, currentPos)) {
			return !this.petrified ? PlayerEvents.convertCorrockBlock(stateIn) : stateIn;
		}
		
	    return stateIn;
	}
	
	@Nullable
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		if (!this.isInProperDimension(context.getWorld())) {
			context.getWorld().getPendingBlockTicks().scheduleTick(context.getPos(), this, 60 + context.getWorld().getRandom().nextInt(40));
		}
		
		return this.getDefaultState();
	}

	public boolean isInProperDimension(World world) {
		return !this.petrified && CONVERSIONS.getOrDefault(world.getDimension().getType(), EEBlocks.CORROCK_OVERWORLD_BLOCK).get() == this;
	}
	
	public boolean isSubmerged(IWorld world, BlockPos pos) {
		for(Direction offsets : Direction.values()) {
			IFluidState fluidState = world.getFluidState(pos.offset(offsets));
			if(!fluidState.isEmpty() && fluidState.isTagged(FluidTags.WATER)) {
				return true;
			}
		}
		return false;
	}
}
