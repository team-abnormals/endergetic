package endergeticexpansion.common.world.biomes;

import endergeticexpansion.common.world.surface.EESurfaceBuilders;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage.Decoration;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.placement.IPlacementConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.surfacebuilders.ConfiguredSurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BiomePoiseForest extends Biome {

	public BiomePoiseForest() {
		super((new Builder()).surfaceBuilder(new ConfiguredSurfaceBuilder<>(EESurfaceBuilders.POISE_SURFACE_BUILDER, SurfaceBuilder.END_STONE_CONFIG)).precipitation(RainType.NONE).category(Category.THEEND).depth(0.1F).scale(0.2F).temperature(0.5F).downfall(0.5F).waterColor(4159204).waterFogColor(329011).parent((String)null));

		this.addFeature(Decoration.SURFACE_STRUCTURES, Biome.createDecoratedFeature(Feature.END_CITY, IFeatureConfig.NO_FEATURE_CONFIG, Placement.NOPE, IPlacementConfig.NO_PLACEMENT_CONFIG));
	}
	
	@OnlyIn(Dist.CLIENT)
	public int getSkyColorByTemp(float currentTemperature) {
		return 0; //TODO: Make sky color purple to match poise? 11665571;
	}
	
}
