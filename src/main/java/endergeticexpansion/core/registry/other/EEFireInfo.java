package endergeticexpansion.core.registry.other;

import endergeticexpansion.core.registry.EEBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.FireBlock;
import net.minecraftforge.fml.ModList;

public class EEFireInfo {
	
	public static void registerFireInfo() {
		if(ModList.get().isLoaded("quark")) {
			setFireInfo(EEBlocks.POISE_VERTICAL_PLANKS.get(), 5, 20);
			setFireInfo(EEBlocks.POISE_VERTICAL_SLAB.get(), 5, 20);
			setFireInfo(EEBlocks.POISE_BOOKSHELF.get(), 30, 20);
		}
		setFireInfo(EEBlocks.POISE_PLANKS, 5, 20);
	}
	
	private static void setFireInfo(Block block, int encouragement, int flammability) {
		((FireBlock) Blocks.FIRE).setFireInfo(block, encouragement, flammability);
	}
	
}