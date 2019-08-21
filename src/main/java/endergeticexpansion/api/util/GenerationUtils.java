package endergeticexpansion.api.util;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.IWorldGenerationBaseReader;

public class GenerationUtils {

	@SuppressWarnings("deprecation")
	public static boolean isAir(IWorldGenerationBaseReader worldIn, BlockPos pos) {
		if (!(worldIn instanceof net.minecraft.world.IBlockReader))
		return worldIn.hasBlockState(pos, BlockState::isAir);
		else return worldIn.hasBlockState(pos, state -> state.isAir((net.minecraft.world.IBlockReader)worldIn, pos));
	}
	
}
