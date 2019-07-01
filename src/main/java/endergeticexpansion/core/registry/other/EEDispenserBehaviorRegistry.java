package endergeticexpansion.core.registry.other;

import endergeticexpansion.core.registry.EEBlocks;
import endergeticexpansion.core.registry.EEItems;
import endergeticexpansion.common.blocks.poise.boof.BlockBoof;
import endergeticexpansion.common.items.ItemBolloomBalloon;
import net.minecraft.block.DispenserBlock;

public class EEDispenserBehaviorRegistry {

	public static void registerAll() {
		DispenserBlock.registerDispenseBehavior(EEBlocks.BOOF_BLOCK.asItem(), new BlockBoof.BoofDispenseBehavior());
		DispenserBlock.registerDispenseBehavior(EEItems.BOLLOOM_BALLOON, new ItemBolloomBalloon.BalloonDispenseBehavior());
	}
	
}
