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
		DispenserBlock.registerDispenseBehavior(EEItems.BOLLOOM_BALLOON_RED, new ItemBolloomBalloon.BalloonDispenseBehavior());
		DispenserBlock.registerDispenseBehavior(EEItems.BOLLOOM_BALLOON_ORANGE, new ItemBolloomBalloon.BalloonDispenseBehavior());
		DispenserBlock.registerDispenseBehavior(EEItems.BOLLOOM_BALLOON_YELLOW, new ItemBolloomBalloon.BalloonDispenseBehavior());
		DispenserBlock.registerDispenseBehavior(EEItems.BOLLOOM_BALLOON_LIME, new ItemBolloomBalloon.BalloonDispenseBehavior());
		DispenserBlock.registerDispenseBehavior(EEItems.BOLLOOM_BALLOON_GREEN, new ItemBolloomBalloon.BalloonDispenseBehavior());
		DispenserBlock.registerDispenseBehavior(EEItems.BOLLOOM_BALLOON_LIGHT_BLUE, new ItemBolloomBalloon.BalloonDispenseBehavior());
		DispenserBlock.registerDispenseBehavior(EEItems.BOLLOOM_BALLOON_CYAN, new ItemBolloomBalloon.BalloonDispenseBehavior());
		DispenserBlock.registerDispenseBehavior(EEItems.BOLLOOM_BALLOON_BLUE, new ItemBolloomBalloon.BalloonDispenseBehavior());
		DispenserBlock.registerDispenseBehavior(EEItems.BOLLOOM_BALLOON_PINK, new ItemBolloomBalloon.BalloonDispenseBehavior());
		DispenserBlock.registerDispenseBehavior(EEItems.BOLLOOM_BALLOON_MAGENTA, new ItemBolloomBalloon.BalloonDispenseBehavior());
		DispenserBlock.registerDispenseBehavior(EEItems.BOLLOOM_BALLOON_PURPLE, new ItemBolloomBalloon.BalloonDispenseBehavior());
		DispenserBlock.registerDispenseBehavior(EEItems.BOLLOOM_BALLOON_BROWN, new ItemBolloomBalloon.BalloonDispenseBehavior());
		DispenserBlock.registerDispenseBehavior(EEItems.BOLLOOM_BALLOON_GRAY, new ItemBolloomBalloon.BalloonDispenseBehavior());
		DispenserBlock.registerDispenseBehavior(EEItems.BOLLOOM_BALLOON_LIGHT_GRAY, new ItemBolloomBalloon.BalloonDispenseBehavior());
		DispenserBlock.registerDispenseBehavior(EEItems.BOLLOOM_BALLOON_BLACK, new ItemBolloomBalloon.BalloonDispenseBehavior());
	}
	
}
