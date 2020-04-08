package endergeticexpansion.api.generation;

import java.util.List;
import java.util.function.BiPredicate;

import com.google.common.collect.Lists;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

public class GenerationPiece {
	private final List<BlockPart> blockPieces = Lists.newArrayList();
	private final BiPredicate<IWorld, BlockPart> blockPlaceCondition;
	
	public GenerationPiece(BiPredicate<IWorld, BlockPart> blockPlaceCondition) {
		this.blockPlaceCondition = blockPlaceCondition;
	}
	
	public void addBlockPiece(BlockState state, BlockPos pos) {
		this.blockPieces.add(new BlockPart(state, pos));
	}
	
	public boolean canPlace(IWorld world) {
		for(BlockPart blocks : this.blockPieces) {
			if(!this.blockPlaceCondition.test(world, blocks)) {
				return false;
			}
		}
		return true;
	}
	
	public void place(IWorld world) {
		for(BlockPart blocks : this.blockPieces) {
			world.setBlockState(blocks.pos, blocks.state, 2);
		}
	}
	
	public class BlockPart {
		public final BlockState state;
		public final BlockPos pos;
		
		public BlockPart(BlockState state, BlockPos pos) {
			this.state = state;
			this.pos = pos;
		}
	}
}