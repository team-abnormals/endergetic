package endergeticexpansion.core.registry.other;

import endergeticexpansion.core.registry.EEBlocks;
import endergeticexpansion.core.registry.EEItems;
import endergeticexpansion.common.blocks.poise.boof.BlockBoof;
import endergeticexpansion.common.items.ItemBolloomBalloon;
import endergeticexpansion.common.items.ItemPuffBugBottle;
import net.minecraft.block.DispenserBlock;

public class EEDispenserBehaviorRegistry {

	public static void registerAll() {
		DispenserBlock.registerDispenseBehavior(EEBlocks.BOOF_BLOCK.asItem(), new BlockBoof.BoofDispenseBehavior());
		DispenserBlock.registerDispenseBehavior(EEItems.BOLLOOM_BALLOON.get(), new ItemBolloomBalloon.BalloonDispenseBehavior());
		DispenserBlock.registerDispenseBehavior(EEItems.BOLLOOM_BALLOON_RED.get(), new ItemBolloomBalloon.BalloonDispenseBehavior());
		DispenserBlock.registerDispenseBehavior(EEItems.BOLLOOM_BALLOON_ORANGE.get(), new ItemBolloomBalloon.BalloonDispenseBehavior());
		DispenserBlock.registerDispenseBehavior(EEItems.BOLLOOM_BALLOON_YELLOW.get(), new ItemBolloomBalloon.BalloonDispenseBehavior());
		DispenserBlock.registerDispenseBehavior(EEItems.BOLLOOM_BALLOON_LIME.get(), new ItemBolloomBalloon.BalloonDispenseBehavior());
		DispenserBlock.registerDispenseBehavior(EEItems.BOLLOOM_BALLOON_GREEN.get(), new ItemBolloomBalloon.BalloonDispenseBehavior());
		DispenserBlock.registerDispenseBehavior(EEItems.BOLLOOM_BALLOON_LIGHT_BLUE.get(), new ItemBolloomBalloon.BalloonDispenseBehavior());
		DispenserBlock.registerDispenseBehavior(EEItems.BOLLOOM_BALLOON_CYAN.get(), new ItemBolloomBalloon.BalloonDispenseBehavior());
		DispenserBlock.registerDispenseBehavior(EEItems.BOLLOOM_BALLOON_BLUE.get(), new ItemBolloomBalloon.BalloonDispenseBehavior());
		DispenserBlock.registerDispenseBehavior(EEItems.BOLLOOM_BALLOON_PINK.get(), new ItemBolloomBalloon.BalloonDispenseBehavior());
		DispenserBlock.registerDispenseBehavior(EEItems.BOLLOOM_BALLOON_MAGENTA.get(), new ItemBolloomBalloon.BalloonDispenseBehavior());
		DispenserBlock.registerDispenseBehavior(EEItems.BOLLOOM_BALLOON_PURPLE.get(), new ItemBolloomBalloon.BalloonDispenseBehavior());
		DispenserBlock.registerDispenseBehavior(EEItems.BOLLOOM_BALLOON_BROWN.get(), new ItemBolloomBalloon.BalloonDispenseBehavior());
		DispenserBlock.registerDispenseBehavior(EEItems.BOLLOOM_BALLOON_GRAY.get(), new ItemBolloomBalloon.BalloonDispenseBehavior());
		DispenserBlock.registerDispenseBehavior(EEItems.BOLLOOM_BALLOON_LIGHT_GRAY.get(), new ItemBolloomBalloon.BalloonDispenseBehavior());
		DispenserBlock.registerDispenseBehavior(EEItems.BOLLOOM_BALLOON_BLACK.get(), new ItemBolloomBalloon.BalloonDispenseBehavior());
		DispenserBlock.registerDispenseBehavior(EEItems.PUFFBUG_BOTTLE.get(), new ItemPuffBugBottle.PuffBugBottleDispenseBehavior());
	}
	
}
