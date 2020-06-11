package endergeticexpansion.common.world.features.corrock;

import java.util.function.Function;
import java.util.function.Supplier;

import com.mojang.datafixers.Dynamic;

import endergeticexpansion.core.registry.EEBlocks;
import net.minecraft.block.BlockState;
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
}