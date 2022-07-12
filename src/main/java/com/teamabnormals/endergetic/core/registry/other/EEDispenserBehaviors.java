package com.teamabnormals.endergetic.core.registry.other;

import com.teamabnormals.endergetic.common.blocks.poise.boof.BoofBlock;
import com.teamabnormals.endergetic.common.items.BolloomBalloonItem;
import com.teamabnormals.endergetic.common.items.PuffBugBottleItem;
import com.teamabnormals.endergetic.core.registry.EEBlocks;
import com.teamabnormals.endergetic.core.registry.EEItems;

import net.minecraft.world.level.block.DispenserBlock;

public final class EEDispenserBehaviors {

	public static void registerAll() {
		DispenserBlock.registerBehavior(EEBlocks.BOOF_BLOCK.get().asItem(), new BoofBlock.BoofDispenseBehavior());
		DispenserBlock.registerBehavior(EEItems.BOLLOOM_BALLOON.get(), new BolloomBalloonItem.BalloonDispenseBehavior());
		DispenserBlock.registerBehavior(EEItems.BOLLOOM_BALLOON_RED.get(), new BolloomBalloonItem.BalloonDispenseBehavior());
		DispenserBlock.registerBehavior(EEItems.BOLLOOM_BALLOON_ORANGE.get(), new BolloomBalloonItem.BalloonDispenseBehavior());
		DispenserBlock.registerBehavior(EEItems.BOLLOOM_BALLOON_YELLOW.get(), new BolloomBalloonItem.BalloonDispenseBehavior());
		DispenserBlock.registerBehavior(EEItems.BOLLOOM_BALLOON_LIME.get(), new BolloomBalloonItem.BalloonDispenseBehavior());
		DispenserBlock.registerBehavior(EEItems.BOLLOOM_BALLOON_GREEN.get(), new BolloomBalloonItem.BalloonDispenseBehavior());
		DispenserBlock.registerBehavior(EEItems.BOLLOOM_BALLOON_LIGHT_BLUE.get(), new BolloomBalloonItem.BalloonDispenseBehavior());
		DispenserBlock.registerBehavior(EEItems.BOLLOOM_BALLOON_CYAN.get(), new BolloomBalloonItem.BalloonDispenseBehavior());
		DispenserBlock.registerBehavior(EEItems.BOLLOOM_BALLOON_BLUE.get(), new BolloomBalloonItem.BalloonDispenseBehavior());
		DispenserBlock.registerBehavior(EEItems.BOLLOOM_BALLOON_PINK.get(), new BolloomBalloonItem.BalloonDispenseBehavior());
		DispenserBlock.registerBehavior(EEItems.BOLLOOM_BALLOON_MAGENTA.get(), new BolloomBalloonItem.BalloonDispenseBehavior());
		DispenserBlock.registerBehavior(EEItems.BOLLOOM_BALLOON_PURPLE.get(), new BolloomBalloonItem.BalloonDispenseBehavior());
		DispenserBlock.registerBehavior(EEItems.BOLLOOM_BALLOON_BROWN.get(), new BolloomBalloonItem.BalloonDispenseBehavior());
		DispenserBlock.registerBehavior(EEItems.BOLLOOM_BALLOON_GRAY.get(), new BolloomBalloonItem.BalloonDispenseBehavior());
		DispenserBlock.registerBehavior(EEItems.BOLLOOM_BALLOON_LIGHT_GRAY.get(), new BolloomBalloonItem.BalloonDispenseBehavior());
		DispenserBlock.registerBehavior(EEItems.BOLLOOM_BALLOON_BLACK.get(), new BolloomBalloonItem.BalloonDispenseBehavior());
		DispenserBlock.registerBehavior(EEItems.BOLLOOM_BALLOON_WHITE.get(), new BolloomBalloonItem.BalloonDispenseBehavior());
		DispenserBlock.registerBehavior(EEItems.PUFFBUG_BOTTLE.get(), new PuffBugBottleItem.PuffBugBottleDispenseBehavior());
	}

}