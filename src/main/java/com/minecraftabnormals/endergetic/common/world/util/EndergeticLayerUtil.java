package com.minecraftabnormals.endergetic.common.world.util;

import java.util.function.LongFunction;

import com.google.common.collect.ImmutableList;
import com.minecraftabnormals.endergetic.common.world.EndergeticGenLayerBiome;

import net.minecraft.world.gen.IExtendedNoiseRandom;
import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.area.IArea;
import net.minecraft.world.gen.area.IAreaFactory;
import net.minecraft.world.gen.area.LazyArea;
import net.minecraft.world.gen.layer.Layer;
import net.minecraft.world.gen.layer.LayerUtil;
import net.minecraft.world.gen.layer.SmoothLayer;
import net.minecraft.world.gen.layer.ZoomLayer;
import net.minecraft.world.gen.layer.traits.IAreaTransformer0;
import net.minecraft.world.gen.layer.traits.IAreaTransformer1;

public final class EndergeticLayerUtil {

	public static <T extends IArea, C extends IExtendedNoiseRandom<T>> IAreaFactory<T> createBiomeFactory(IAreaFactory<T> landFactory, LongFunction<C> contextFactory) {
		IAreaFactory<T> biomeFactory = EndergeticGenLayerBiome.INSTANCE.apply(contextFactory.apply(1L));
		biomeFactory = LayerUtil.repeat(100L, ZoomLayer.NORMAL, biomeFactory, 2, contextFactory);
		return biomeFactory;
	}
	
	public static <T extends IArea, C extends IExtendedNoiseRandom<T>> ImmutableList<IAreaFactory<T>> createAreaFactories(LongFunction<C> contextFactory) {
		IAreaFactory<T> landFactory = GenLayerLand.INSTANCE.apply(contextFactory.apply(1L));
		
		IAreaFactory<T> biomesFactory = createBiomeFactory(landFactory, contextFactory);
		
		for (int i = 0; i < 3; i++) {
			biomesFactory = ZoomLayer.NORMAL.apply(contextFactory.apply(1000L + (long) i), biomesFactory);
		}
		
		biomesFactory = SmoothLayer.INSTANCE.apply(contextFactory.apply(100L), biomesFactory);
		
		IAreaFactory<T> voroniZoomBiomesFactory = VoroniZoomLayer.INSTANCE.apply(contextFactory.apply(10L), biomesFactory);
		return ImmutableList.of(biomesFactory, voroniZoomBiomesFactory, biomesFactory);
	}
	
	public static Layer[] createGenLayers(long seed) {
		ImmutableList<IAreaFactory<LazyArea>> factoryList = createAreaFactories((seedModifier) -> new LazyAreaContextEndergetic(25, seed, seedModifier));
		Layer biomesLayer = new Layer(factoryList.get(0));
		Layer voroniZoomBiomesLayer = new Layer(factoryList.get(1));
		Layer biomesLayer2 = new Layer(factoryList.get(2));
		return new Layer[] {
			biomesLayer,
			voroniZoomBiomesLayer,
			biomesLayer2
		};
	}
	
	public enum GenLayerLand implements IAreaTransformer0 {
		INSTANCE;

		public int apply(INoiseRandom random, int x, int z) {
			return 1;
		}
	}
	
	public enum VoroniZoomLayer implements IAreaTransformer1 {
		INSTANCE;

		public int apply(IExtendedNoiseRandom<?> p_215728_1_, IArea p_215728_2_, int p_215728_3_, int p_215728_4_) {
			int i = p_215728_3_ - 2;
			int j = p_215728_4_ - 2;
			int k = i >> 2;
			int l = j >> 2;
			int i1 = k << 2;
			int j1 = l << 2;
			p_215728_1_.setPosition((long)i1, (long)j1);
			double d0 = ((double)p_215728_1_.random(1024) / 1024.0D - 0.5D) * 3.6D;
			double d1 = ((double)p_215728_1_.random(1024) / 1024.0D - 0.5D) * 3.6D;
			p_215728_1_.setPosition((long)(i1 + 4), (long)j1);
			double d2 = ((double)p_215728_1_.random(1024) / 1024.0D - 0.5D) * 3.6D + 4.0D;
			double d3 = ((double)p_215728_1_.random(1024) / 1024.0D - 0.5D) * 3.6D;
			p_215728_1_.setPosition((long)i1, (long)(j1 + 4));
			double d4 = ((double)p_215728_1_.random(1024) / 1024.0D - 0.5D) * 3.6D;
			double d5 = ((double)p_215728_1_.random(1024) / 1024.0D - 0.5D) * 3.6D + 4.0D;
			p_215728_1_.setPosition((long)(i1 + 4), (long)(j1 + 4));
			double d6 = ((double)p_215728_1_.random(1024) / 1024.0D - 0.5D) * 3.6D + 4.0D;
			double d7 = ((double)p_215728_1_.random(1024) / 1024.0D - 0.5D) * 3.6D + 4.0D;
			int k1 = i & 3;
			int l1 = j & 3;
			double d8 = ((double)l1 - d1) * ((double)l1 - d1) + ((double)k1 - d0) * ((double)k1 - d0);
			double d9 = ((double)l1 - d3) * ((double)l1 - d3) + ((double)k1 - d2) * ((double)k1 - d2);
			double d10 = ((double)l1 - d5) * ((double)l1 - d5) + ((double)k1 - d4) * ((double)k1 - d4);
			double d11 = ((double)l1 - d7) * ((double)l1 - d7) + ((double)k1 - d6) * ((double)k1 - d6);
			if (d8 < d9 && d8 < d10 && d8 < d11) {
				return p_215728_2_.getValue(this.getOffsetX(i1), this.getOffsetZ(j1));
			} else if (d9 < d8 && d9 < d10 && d9 < d11) {
				return p_215728_2_.getValue(this.getOffsetX(i1 + 4), this.getOffsetZ(j1)) & 255;
			} else {
				return d10 < d8 && d10 < d9 && d10 < d11 ? p_215728_2_.getValue(this.getOffsetX(i1), this.getOffsetZ(j1 + 4)) : p_215728_2_.getValue(this.getOffsetX(i1 + 4), this.getOffsetZ(j1 + 4)) & 255;
			}
		}

		@Override
		public int getOffsetX(int x) {
			return x >> 2;
		}

		@Override
		public int getOffsetZ(int z) {
			return z >> 2;
		}
	}

}