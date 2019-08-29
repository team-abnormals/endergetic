package endergeticexpansion.common.world;

import endergeticexpansion.core.registry.EEBiomes;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.layer.traits.IAreaTransformer0;
import net.minecraft.world.gen.layer.traits.IDimOffset0Transformer;

public enum EndergeticGenLayerBiome implements IAreaTransformer0, IDimOffset0Transformer {
	INSTANCE;

	@SuppressWarnings("deprecation")
	@Override
	public int apply(INoiseRandom context, int x, int z) {
		return Registry.BIOME.getId(EEBiomes.getRandomBiome(context));
	}
	
}
