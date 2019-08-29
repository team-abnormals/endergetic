package endergeticexpansion.common.world.util;

import java.util.function.LongFunction;

import com.google.common.collect.ImmutableList;

import endergeticexpansion.common.world.EndergeticGenLayerBiome;
import net.minecraft.world.WorldType;
import net.minecraft.world.gen.IExtendedNoiseRandom;
import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.area.IArea;
import net.minecraft.world.gen.area.IAreaFactory;
import net.minecraft.world.gen.area.LazyArea;
import net.minecraft.world.gen.layer.Layer;
import net.minecraft.world.gen.layer.LayerUtil;
import net.minecraft.world.gen.layer.SmoothLayer;
import net.minecraft.world.gen.layer.VoroniZoomLayer;
import net.minecraft.world.gen.layer.ZoomLayer;
import net.minecraft.world.gen.layer.traits.IAreaTransformer0;

public class EndergeticLayerUtil {

	public static <T extends IArea, C extends IExtendedNoiseRandom<T>> IAreaFactory<T> createBiomeFactory(IAreaFactory<T> landFactory, LongFunction<C> contextFactory) {
		IAreaFactory<T> biomeFactory = EndergeticGenLayerBiome.INSTANCE.apply(contextFactory.apply(1L));
		biomeFactory = LayerUtil.repeat(200L, ZoomLayer.NORMAL, biomeFactory, 2, contextFactory);
		return biomeFactory;
	}
	
	public static <T extends IArea, C extends IExtendedNoiseRandom<T>> ImmutableList<IAreaFactory<T>> createAreaFactories(WorldType worldType, LongFunction<C> contextFactory) {
		IAreaFactory<T> landFactory = GenLayerLand.INSTANCE.apply(contextFactory.apply(1L));
		
		IAreaFactory<T> biomesFactory = createBiomeFactory(landFactory, contextFactory);
		
		biomesFactory = ZoomLayer.NORMAL.apply(contextFactory.apply(1000L), biomesFactory);
		biomesFactory = ZoomLayer.NORMAL.apply(contextFactory.apply(1001L), biomesFactory);
		biomesFactory = ZoomLayer.NORMAL.apply(contextFactory.apply(1002L), biomesFactory);
		biomesFactory = ZoomLayer.NORMAL.apply(contextFactory.apply(1003L), biomesFactory);
		biomesFactory = ZoomLayer.NORMAL.apply(contextFactory.apply(1004L), biomesFactory);
	        
		biomesFactory = SmoothLayer.INSTANCE.apply(contextFactory.apply(100L), biomesFactory);
		
		IAreaFactory<T> voroniZoomBiomesFactory = VoroniZoomLayer.INSTANCE.apply(contextFactory.apply(10L), biomesFactory);
		return ImmutableList.of(biomesFactory, voroniZoomBiomesFactory, biomesFactory);
	}
	
	public static Layer[] createGenLayers(long seed, WorldType worldType) {
		ImmutableList<IAreaFactory<LazyArea>> factoryList = createAreaFactories(worldType, (seedModifier) -> {
			return new LazyAreaContextEndergetic(25, seed, seedModifier);
		});
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
	
}
