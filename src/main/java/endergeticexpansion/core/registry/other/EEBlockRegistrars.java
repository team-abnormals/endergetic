package endergeticexpansion.core.registry.other;

import endergeticexpansion.core.registry.EEBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.FireBlock;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.fml.ModList;

public class EEBlockRegistrars {
	
	public static void registerFireInfo() {
		if(ModList.get().isLoaded("quark")) {
			setFireInfo(EEBlocks.POISE_VERTICAL_PLANKS.get(), 5, 20);
			setFireInfo(EEBlocks.POISE_VERTICAL_SLAB.get(), 5, 20);
			setFireInfo(EEBlocks.POISE_BOOKSHELF.get(), 30, 20);
		}
		setFireInfo(EEBlocks.POISE_PLANKS.get(), 5, 20);
	}
	
	public static void setupRenderLayers() {
		RenderTypeLookup.setRenderLayer(EEBlocks.CORROCK_END.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(EEBlocks.CORROCK_NETHER.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(EEBlocks.CORROCK_OVERWORLD.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(EEBlocks.ENDER_FIRE, RenderType.getCutout());
		
		RenderTypeLookup.setRenderLayer(EEBlocks.POISMOSS_EUMUS.get(), RenderType.getCutoutMipped());
		RenderTypeLookup.setRenderLayer(EEBlocks.POISMOSS.get(), RenderType.getCutoutMipped());
		RenderTypeLookup.setRenderLayer(EEBlocks.POISE_GRASS.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(EEBlocks.POISE_GRASS_TALL.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(EEBlocks.POISE_CLUSTER.get(), RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(EEBlocks.HIVE_HANGER.get(), RenderType.getCutout());
	}
	
	private static void setFireInfo(Block block, int encouragement, int flammability) {
		((FireBlock) Blocks.FIRE).setFireInfo(block, encouragement, flammability);
	}
	
}