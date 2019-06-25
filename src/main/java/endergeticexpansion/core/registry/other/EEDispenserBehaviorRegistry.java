package endergeticexpansion.core.registry.other;

import endergeticexpansion.core.registry.EEBlocks;
import endergeticexpansion.common.blocks.poise.boof.BlockBoof;
import net.minecraft.block.DispenserBlock;

public class EEDispenserBehaviorRegistry {

	public static void registerAll() {
		DispenserBlock.registerDispenseBehavior(EEBlocks.BOOF_BLOCK.asItem(), new BlockBoof.BoofDispenseBehavior());
	}
	
}
