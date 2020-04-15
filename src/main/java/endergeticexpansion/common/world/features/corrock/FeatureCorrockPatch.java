package endergeticexpansion.common.world.features.corrock;

import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import com.mojang.datafixers.Dynamic;

import endergeticexpansion.api.generation.IAddToBiomes;
import endergeticexpansion.common.world.features.EEFeatures;
import endergeticexpansion.core.registry.EEBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.GenerationStage.Decoration;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.placement.NoiseDependant;
import net.minecraft.world.gen.placement.Placement;

public class FeatureCorrockPatch extends Feature<NoFeatureConfig> implements IAddToBiomes {
	private static final Supplier<BlockState> CORROCK = () -> EEBlocks.CORROCK_END.get().getDefaultState();
	
	public FeatureCorrockPatch(Function<Dynamic<?>, ? extends NoFeatureConfig> config) {
		super(config);
	}

	@SuppressWarnings("deprecation")
	public boolean place(IWorld world, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos pos, NoFeatureConfig config) {
		for(BlockState blockstate = world.getBlockState(pos); (blockstate.isAir() || blockstate.isIn(BlockTags.LEAVES)) && pos.getY() > 0; blockstate = world.getBlockState(pos)) {
			pos = pos.down();
		}
		if(world.getBlockState(pos).getBlock() != EEBlocks.CORROCK_END_BLOCK.get()) return false;
		int i = 0;
		
		for(int j = 0; j < 128; ++j) {
			BlockPos blockpos = pos.add(rand.nextInt(8) - rand.nextInt(8), rand.nextInt(6) - rand.nextInt(6), rand.nextInt(8) - rand.nextInt(8));
			if(world.isAirBlock(blockpos) && CORROCK.get().isValidPosition(world, blockpos)) {
				boolean chance = world.getBlockState(blockpos.down()).getBlock() == EEBlocks.CORROCK_END_BLOCK.get() ? rand.nextFloat() < 0.85F : rand.nextFloat() < 0.25F;
				if(chance) {
					world.setBlockState(blockpos, CORROCK.get(), 2);
					i++;
				}
			}
		}
		return i > 0;
	}
	
	@Override
	public Consumer<Biome> processBiomeAddition() {
		return biome -> {
			if(IAddToBiomes.isInChorusBiome(biome)) {
				biome.addFeature(Decoration.VEGETAL_DECORATION, EEFeatures.CORROCK_PATCH.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Placement.NOISE_HEIGHTMAP_DOUBLE.configure(new NoiseDependant(-0.8D, 7, 8))));
			}
		};
	}
}