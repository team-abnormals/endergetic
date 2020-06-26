package endergeticexpansion.common.world.features.corrock;

import java.util.Random;
import java.util.function.Function;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import com.mojang.datafixers.Dynamic;
import com.teamabnormals.abnormals_core.core.library.GenerationPiece;

import endergeticexpansion.core.registry.EEBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChorusFlowerBlock;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.ProbabilityConfig;

public abstract class AbstractCorrockFeature extends Feature<ProbabilityConfig> {
	protected final Supplier<BlockState> CORROCK = () -> EEBlocks.CORROCK_END.get().getDefaultState();
	protected final Supplier<BlockState> CORROCK_BLOCK = () -> EEBlocks.CORROCK_END_BLOCK.get().getDefaultState();
	protected final Supplier<BlockState> CORROCK_CROWN(boolean wall) {
		return wall ? () -> EEBlocks.CORROCK_CROWN_END_WALL.get().getDefaultState() : () -> EEBlocks.CORROCK_CROWN_END_STANDING.get().getDefaultState();
	}

	public AbstractCorrockFeature(Function<Dynamic<?>, ? extends ProbabilityConfig> configFactory) {
		super(configFactory);
	}
	
	protected class ChorusPlantPart {
		public final GenerationPiece piece;
		private final Direction facing;
		public final BlockPos pos;
		
		public ChorusPlantPart(GenerationPiece piece, BlockPos pos, @Nullable Direction facing) {
			this.piece = piece;
			this.pos = pos;
			this.facing = facing;
		}
		
		public void placeCoveredGrowth(IWorld world, Random rand) {
			world.setBlockState(this.pos, Blocks.END_STONE.getDefaultState(), 2);
			world.setBlockState(this.pos.offset(this.facing), EEBlocks.ENDSTONE_COVER.get().getDefaultState().with(HorizontalBlock.HORIZONTAL_FACING, this.facing.getOpposite()), 2);
			ChorusFlowerBlock.generatePlant(world, this.pos.up(), rand, 8);
		}
		
		public void placeGrowth(IWorld world, Random rand) {
			world.setBlockState(this.pos, Blocks.END_STONE.getDefaultState(), 2);
			ChorusFlowerBlock.generatePlant(world, this.pos.up(), rand, 8);
		}
	}
}