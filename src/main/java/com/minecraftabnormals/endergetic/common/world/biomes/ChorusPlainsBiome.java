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
		super((new Biome.Builder()).surfaceBuilder(SurfaceBuilder.DEFAULT, SurfaceBuilder.END_STONE_CONFIG).precipitation(Biome.RainType.NONE).category(Biome.Category.THEEND).depth(0.1F).scale(0.2F).temperature(0.5F).downfall(0.5F).func_235097_a_((new BiomeAmbience.Builder()).setWaterColor(4159204).setWaterFogColor(329011).setFogColor(10518688).setMoodSound(MoodSoundAmbience.field_235027_b_).build()).parent(null), () -> new ConfiguredSurfaceBuilder<>(SurfaceBuilder.DEFAULT, SurfaceBuilder.END_STONE_CONFIG));
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
