package endergeticexpansion.common.world.biomes;

import net.minecraft.world.biome.Biome;

import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraftforge.common.BiomeDictionary.Type;

/*
  The Dummy class to trick the end's biome provider
 */
public class BiomeChorusPlains extends EndergeticBiome {
	
	public BiomeChorusPlains() {
		super((new Biome.Builder()).surfaceBuilder(SurfaceBuilder.DEFAULT, SurfaceBuilder.END_STONE_CONFIG).precipitation(Biome.RainType.NONE).category(Biome.Category.THEEND).depth(0.1F).scale(0.2F).temperature(0.5F).downfall(0.5F).waterColor(4159204).waterFogColor(329011).parent((String)null));
	}
	
	@Override
	public Type[] getBiomeTypes() {
		return new Type[] {
			Type.END
		};
	}
}
