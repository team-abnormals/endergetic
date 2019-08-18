package endergeticexpansion.common.world.surface;

import endergeticexpansion.core.EndergeticExpansion;
import endergeticexpansion.core.registry.EEBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EndergeticExpansion.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EESurfaceBuilders {
	/*
	 * Blockstates for surface builder configs
	 */
	public static final BlockState END_STONE = Blocks.END_STONE.getDefaultState();
	public static final BlockState EUMUS     = EEBlocks.EUMUS.getDefaultState();
	public static final BlockState POISMOSS  = EEBlocks.POISE_GRASS_BLOCK.getDefaultState();
	
	/*
	 * Surface Builder Configs
	 */
	public static final SurfaceBuilderConfig END_STONE_CONFIG = new SurfaceBuilderConfig(END_STONE, END_STONE, END_STONE);
	public static final SurfaceBuilderConfig EUMUS_CONFIG     = new SurfaceBuilderConfig(EUMUS, END_STONE, EUMUS);
	public static final SurfaceBuilderConfig POISMOSS_CONFIG  = new SurfaceBuilderConfig(POISMOSS, END_STONE, EUMUS);
	
	/*
	 * Surface Builders
	 */
	public static final SurfaceBuilder<SurfaceBuilderConfig> POISE_SURFACE_BUILDER = registerSurfaceBuilder("poise_forest", new SurfaceBuilderPoiseForest(SurfaceBuilderConfig::deserialize));
	
	private static SurfaceBuilder<SurfaceBuilderConfig> registerSurfaceBuilder(String name, SurfaceBuilder<SurfaceBuilderConfig> builder) {
		builder.setRegistryName(EndergeticExpansion.MOD_ID, name);
		return builder;
	}
	
	@SubscribeEvent
	public static void registerSurfaceBuilders(RegistryEvent.Register<SurfaceBuilder<?>> event) {
		event.getRegistry().registerAll(
			POISE_SURFACE_BUILDER
		);
	}
}
