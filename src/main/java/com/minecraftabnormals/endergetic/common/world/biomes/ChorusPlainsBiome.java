package com.minecraftabnormals.endergetic.common.world.biomes;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeAmbience;
import net.minecraft.world.biome.MoodSoundAmbience;
import net.minecraft.world.gen.surfacebuilders.ConfiguredSurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraftforge.common.BiomeDictionary.Type;

/*
  The Dummy biome for the end's biome provider
 */
public class ChorusPlainsBiome extends EndergeticBiome {
	
	public ChorusPlainsBiome() {
		super((new Biome.Builder()).surfaceBuilder(SurfaceBuilder.DEFAULT, SurfaceBuilder.END_STONE_CONFIG).precipitation(Biome.RainType.NONE).category(Biome.Category.THEEND).depth(0.1F).scale(0.2F).temperature(0.5F).downfall(0.5F).func_235097_a_((new BiomeAmbience.Builder()).func_235246_b_(4159204).func_235248_c_(329011).func_235239_a_(10518688).func_235243_a_(MoodSoundAmbience.field_235027_b_).func_235238_a_()).parent((String)null), () -> new ConfiguredSurfaceBuilder<>(SurfaceBuilder.DEFAULT, SurfaceBuilder.END_STONE_CONFIG));
	}
	
	@Override
	public int getWeight() {
		return 15;
	}
	
	@Override
	public Type[] getBiomeTypes() {
		return new Type[] {
			Type.END
		};
	}
	
}
