package endergeticexpansion.common.world.biomes;

import java.util.Random;
import java.util.function.Supplier;

import javax.annotation.Nonnull;

import net.minecraft.block.BlockState;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.surfacebuilders.ConfiguredSurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.ISurfaceBuilderConfig;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.BiomeDictionary.Type;

public abstract class EndergeticBiome extends Biome {
	protected final Supplier<ConfiguredSurfaceBuilder<?>> surfaceBuilder;

	public EndergeticBiome(Builder biomeBuilder, Supplier<ConfiguredSurfaceBuilder<?>> surfaceBuilder) {
		super(biomeBuilder);
		this.surfaceBuilder = surfaceBuilder;
	}
	
	@Override
	public void buildSurface(Random random, IChunk chunkIn, int x, int z, int startHeight, double noise, BlockState defaultBlock, BlockState defaultFluid, int seaLevel, long seed) {
		this.surfaceBuilder.get().setSeed(seed);
		this.surfaceBuilder.get().buildSurface(random, chunkIn, this, x, z, startHeight, noise, defaultBlock, defaultFluid, seaLevel, seed);
	}
	
	@Override
	public ConfiguredSurfaceBuilder<?> getSurfaceBuilder() {
		return this.surfaceBuilder.get();
	}
	
	@Override
	public ISurfaceBuilderConfig getSurfaceBuilderConfig() {
		return this.surfaceBuilder.get().getConfig();
	}
	
	/**
	 * Used for adding spawns and features to the biome using DeferredRegister; gets called in common setup
	 */
	public void addSpawnsAndFeatures() {}
	
	public int getWeight() {
		return 0;
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public int getSkyColorByTemp(float currentTemperature) {
		return 0;
	}
	
	@OnlyIn(Dist.CLIENT)
	public int getSkyColor() {
		return 0;
	}
	
	@Nonnull
	public Type[] getBiomeTypes() {
		return new Type[] {Type.END};
	}
}