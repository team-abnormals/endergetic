package endergeticexpansion.common.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.EndRodBlock;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BlockAcidianLantern extends EndRodBlock {
	//DOWN, UP, NORTH, SOUTH, WEST, EAST
	private static final VoxelShape[] SHAPES = new VoxelShape[] {
		VoxelShapes.or(Block.makeCuboidShape(6.0D, 8.0D, 6.0D, 10.0D, 16.0D, 10.0D), Block.makeCuboidShape(4.0D, 0.0D, 4.0D, 12.0D, 8.0D, 12.0D)),
		VoxelShapes.or(Block.makeCuboidShape(6.0D, 0.0D, 6.0D, 10.0D, 8.0D, 10.0D), Block.makeCuboidShape(4.0D, 8.0D, 4.0D, 12.0D, 16.0D, 12.0D)),
		VoxelShapes.or(Block.makeCuboidShape(6.0D, 6.0D, 8.0D, 10.0D, 10.0D, 16.0D), Block.makeCuboidShape(4.0D, 4.0D, 0.0D, 12.0D, 12.0D, 8.0D)),
		VoxelShapes.or(Block.makeCuboidShape(6.0D, 6.0D, 0.0D, 10.0D, 10.0D, 8.0D), Block.makeCuboidShape(4.0D, 4.0D, 8.0D, 12.0D, 12.0D, 16.0D)),
		VoxelShapes.or(Block.makeCuboidShape(8.0D, 6.0D, 6.0D, 16.0D, 10.0D, 10.0D), Block.makeCuboidShape(0.0D, 4.0D, 4.0D, 8.0D, 12.0D, 12.0D)),
		VoxelShapes.or(Block.makeCuboidShape(0.0D, 6.0D, 6.0D, 8.0D, 10.0D, 10.0D), Block.makeCuboidShape(8.0D, 4.0D, 4.0D, 16.0D, 12.0D, 12.0D)),
	};
	
	public BlockAcidianLantern(Properties builder) {
		super(builder);
	}
	
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPES[state.get(FACING).getIndex()];
	}
	
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
		Direction direction = stateIn.get(FACING);
		double offset = direction == Direction.UP ? 0.1D : direction == Direction.DOWN ? 1D : 0.5D;
		double xOffset;
		double zOffset;
		switch(direction) {
			default:
			case NORTH:
				xOffset = 0.5D;
				zOffset = -0.1D;
				break;
			case SOUTH:
				xOffset = 0.5D;
				zOffset = 1.1D;
				break;
			case WEST:
				xOffset = -0.1D;
				zOffset = 0.5D;
				break;
			case EAST:
				xOffset = 1.1D;
				zOffset = 0.5D;
				break;
		}
		if(direction == Direction.UP || direction == Direction.DOWN) {
			xOffset = 0.55D;
			zOffset = 0.55D;
		}
		double d0 = pos.getX() + xOffset - (double)(rand.nextFloat() * 0.1F);
		double d1 = pos.getY() + offset - (double)(rand.nextFloat() * 0.1F);
		double d2 = pos.getZ() + zOffset - (double)(rand.nextFloat() * 0.1F);
		if(rand.nextFloat() <= 0.65F) {
			worldIn.addParticle(ParticleTypes.DRAGON_BREATH, d0, d1 + direction.getYOffset(), d2, rand.nextGaussian() * 0.005D, rand.nextGaussian() * 0.005D, rand.nextGaussian() * 0.005D);
		}
	}
	
	@Override
	public int getLightValue(BlockState state) {
		return 10;
	}
}
