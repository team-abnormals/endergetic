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
		setRenderLayer(EEBlocks.CORROCK_END.get(), RenderType.getCutout());
		setRenderLayer(EEBlocks.CORROCK_NETHER.get(), RenderType.getCutout());
		setRenderLayer(EEBlocks.CORROCK_OVERWORLD.get(), RenderType.getCutout());
		setRenderLayer(EEBlocks.PETRIFIED_CORROCK_END.get(), RenderType.getCutout());
		setRenderLayer(EEBlocks.PETRIFIED_CORROCK_NETHER.get(), RenderType.getCutout());
		setRenderLayer(EEBlocks.PETRIFIED_CORROCK_OVERWORLD.get(), RenderType.getCutout());
		setRenderLayer(EEBlocks.ENDER_FIRE, RenderType.getCutout());
		
		setRenderLayer(EEBlocks.POISE_BUSH_POT.get(), RenderType.getCutout());
		setRenderLayer(EEBlocks.POISE_DOOR.get(), RenderType.getCutout());
		setRenderLayer(EEBlocks.POISE_TRAPDOOR.get(), RenderType.getCutout());
		setRenderLayer(EEBlocks.POISE_LADDER.get(), RenderType.getCutout());
		setRenderLayer(EEBlocks.POISMOSS_EUMUS.get(), RenderType.getCutoutMipped());
		setRenderLayer(EEBlocks.POISMOSS.get(), RenderType.getCutoutMipped());
		setRenderLayer(EEBlocks.POISE_BUSH.get(), RenderType.getCutout());
		setRenderLayer(EEBlocks.POISE_BUSH_TALL.get(), RenderType.getCutout());
		setRenderLayer(EEBlocks.POISE_CLUSTER.get(), RenderType.getTranslucent());
		setRenderLayer(EEBlocks.HIVE_HANGER.get(), RenderType.getCutout());
	}
	
	private static void setFireInfo(Block block, int encouragement, int flammability) {
		((FireBlock) Blocks.FIRE).setFireInfo(block, encouragement, flammability);
	}
	
	private static synchronized void setRenderLayer(Block block, RenderType type) {
		RenderTypeLookup.setRenderLayer(block, type::equals);
	}
}