package endergeticexpansion.common.world.biomes;

import endergeticexpansion.common.world.features.EEFeatures;
import endergeticexpansion.common.world.surface.EESurfaceBuilders;
import endergeticexpansion.core.registry.EEBlocks;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage.Decoration;
import net.minecraft.world.gen.feature.DoublePlantConfig;
import net.minecraft.world.gen.feature.GrassFeatureConfig;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.placement.AtSurfaceWithExtraConfig;
import net.minecraft.world.gen.placement.NoiseDependant;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.surfacebuilders.ConfiguredSurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BiomePoiseForest extends Biome {

	public BiomePoiseForest() {
		super((new Builder()).surfaceBuilder(new ConfiguredSurfaceBuilder<>(EESurfaceBuilders.POISE_SURFACE_BUILDER, SurfaceBuilder.END_STONE_CONFIG)).precipitation(RainType.NONE).category(Category.THEEND).depth(0.1F).scale(0.2F).temperature(0.5F).downfall(0.5F).waterColor(4159204).waterFogColor(329011).parent((String)null));
		
		this.addFeature(Decoration.SURFACE_STRUCTURES, createDecoratedFeature(EEFeatures.POISE_CLUSTER, IFeatureConfig.NO_FEATURE_CONFIG, Placement.NOISE_HEIGHTMAP_32, new NoiseDependant(-0.8D, 4, 22)));
		this.addFeature(Decoration.SURFACE_STRUCTURES, createDecoratedFeature(EEFeatures.POISE_TREE, IFeatureConfig.NO_FEATURE_CONFIG, Placement.COUNT_EXTRA_HEIGHTMAP, new AtSurfaceWithExtraConfig(2, 0.05F, 1)));
		
		this.addFeature(Decoration.VEGETAL_DECORATION, createDecoratedFeature(EEFeatures.PUFFBUG_HIVE, IFeatureConfig.NO_FEATURE_CONFIG, Placement.NOISE_HEIGHTMAP_32, new NoiseDependant(-0.8D, 9, 25)));
		this.addFeature(Decoration.VEGETAL_DECORATION, createDecoratedFeature(EEFeatures.BOLLOOM_BUD, IFeatureConfig.NO_FEATURE_CONFIG, Placement.NOISE_HEIGHTMAP_32, new NoiseDependant(-0.8D, 10, 40)));
		this.addFeature(Decoration.VEGETAL_DECORATION, createDecoratedFeature(EEFeatures.POISE_TALLGRASS, new DoublePlantConfig(EEBlocks.POISE_GRASS_TALL.getDefaultState()), Placement.NOISE_HEIGHTMAP_32, new NoiseDependant(-0.8D, 0, 7)));
		this.addFeature(Decoration.VEGETAL_DECORATION, createDecoratedFeature(EEFeatures.POISE_GRASS, new GrassFeatureConfig(EEBlocks.POISE_GRASS.getDefaultState()), Placement.NOISE_HEIGHTMAP_DOUBLE, new NoiseDependant(-0.8D, 5, 10)));
	}
	
	@OnlyIn(Dist.CLIENT)
	public int getSkyColorByTemp(float currentTemperature) {
		return 0; //TODO: Make sky color purple to match poise? 11665571;
	}
	
}
