package com.teamabnormals.endergetic.core.registry.other;

import com.teamabnormals.blueprint.core.util.DataUtil;
import com.teamabnormals.endergetic.common.block.poise.boof.BoofBlock;
import com.teamabnormals.endergetic.common.item.BolloomBalloonItem;
import com.teamabnormals.endergetic.common.item.PuffBugBottleItem;
import com.teamabnormals.endergetic.core.registry.EEBlocks;
import com.teamabnormals.endergetic.core.registry.EEItems;
import net.minecraft.world.level.block.DispenserBlock;

public class EECompat {

	public static void registerCompat() {
		registerFlammables();
		registerDispenserBehaviors();
		registerCompostables();
	}

	private static void registerDispenserBehaviors() {
		DispenserBlock.registerBehavior(EEBlocks.BOOF_BLOCK.get().asItem(), new BoofBlock.BoofDispenseBehavior());
		DispenserBlock.registerBehavior(EEItems.BOLLOOM_BALLOON.get(), new BolloomBalloonItem.BalloonDispenseBehavior());
		DispenserBlock.registerBehavior(EEItems.RED_BOLLOOM_BALLOON.get(), new BolloomBalloonItem.BalloonDispenseBehavior());
		DispenserBlock.registerBehavior(EEItems.ORANGE_BOLLOOM_BALLOON.get(), new BolloomBalloonItem.BalloonDispenseBehavior());
		DispenserBlock.registerBehavior(EEItems.YELLOW_BOLLOOM_BALLOON.get(), new BolloomBalloonItem.BalloonDispenseBehavior());
		DispenserBlock.registerBehavior(EEItems.LIME_BOLLOOM_BALLOON.get(), new BolloomBalloonItem.BalloonDispenseBehavior());
		DispenserBlock.registerBehavior(EEItems.GREEN_BOLLOOM_BALLOON.get(), new BolloomBalloonItem.BalloonDispenseBehavior());
		DispenserBlock.registerBehavior(EEItems.LIGHT_BLUE_BOLLOOM_BALLOON.get(), new BolloomBalloonItem.BalloonDispenseBehavior());
		DispenserBlock.registerBehavior(EEItems.CYAN_BOLLOOM_BALLOON.get(), new BolloomBalloonItem.BalloonDispenseBehavior());
		DispenserBlock.registerBehavior(EEItems.BLUE_BOLLOOM_BALLOON.get(), new BolloomBalloonItem.BalloonDispenseBehavior());
		DispenserBlock.registerBehavior(EEItems.PINK_BOLLOOM_BALLOON.get(), new BolloomBalloonItem.BalloonDispenseBehavior());
		DispenserBlock.registerBehavior(EEItems.MAGENTA_BOLLOOM_BALLOON.get(), new BolloomBalloonItem.BalloonDispenseBehavior());
		DispenserBlock.registerBehavior(EEItems.PURPLE_BOLLOOM_BALLOON.get(), new BolloomBalloonItem.BalloonDispenseBehavior());
		DispenserBlock.registerBehavior(EEItems.BROWN_BOLLOOM_BALLOON.get(), new BolloomBalloonItem.BalloonDispenseBehavior());
		DispenserBlock.registerBehavior(EEItems.GRAY_BOLLOOM_BALLOON.get(), new BolloomBalloonItem.BalloonDispenseBehavior());
		DispenserBlock.registerBehavior(EEItems.LIGHT_GRAY_BOLLOOM_BALLOON.get(), new BolloomBalloonItem.BalloonDispenseBehavior());
		DispenserBlock.registerBehavior(EEItems.BLACK_BOLLOOM_BALLOON.get(), new BolloomBalloonItem.BalloonDispenseBehavior());
		DispenserBlock.registerBehavior(EEItems.WHITE_BOLLOOM_BALLOON.get(), new BolloomBalloonItem.BalloonDispenseBehavior());
		DispenserBlock.registerBehavior(EEItems.PUFFBUG_BOTTLE.get(), new PuffBugBottleItem.PuffBugBottleDispenseBehavior());
	}

	private static void registerCompostables() {
		DataUtil.registerCompostable(EEBlocks.POISE_BUSH.get(), 0.3F);
		DataUtil.registerCompostable(EEBlocks.TALL_POISE_BUSH.get(), 0.5F);
		DataUtil.registerCompostable(EEBlocks.POISE_CLUSTER.get(), 0.85F);
		DataUtil.registerCompostable(EEBlocks.BOLLOOM_BUD.get(), 1.0F);
		DataUtil.registerCompostable(EEItems.BOLLOOM_FRUIT.get(), 0.65F);
		DataUtil.registerCompostable(EEBlocks.BOLLOOM_CRATE.get(), 1.0F);
	}

	private static void registerFlammables() {
		DataUtil.registerFlammable(EEBlocks.POISE_STEM.get(), 5, 5);
		DataUtil.registerFlammable(EEBlocks.POISE_WOOD.get(), 5, 5);
		DataUtil.registerFlammable(EEBlocks.GLOWING_POISE_STEM.get(), 5, 5);
		DataUtil.registerFlammable(EEBlocks.GLOWING_POISE_WOOD.get(), 5, 5);
		DataUtil.registerFlammable(EEBlocks.STRIPPED_POISE_STEM.get(), 5, 5);
		DataUtil.registerFlammable(EEBlocks.STRIPPED_POISE_WOOD.get(), 5, 5);
		DataUtil.registerFlammable(EEBlocks.POISE_PLANKS.get(), 5, 20);
		DataUtil.registerFlammable(EEBlocks.POISE_SLAB.get(), 5, 20);
		DataUtil.registerFlammable(EEBlocks.POISE_STAIRS.get(), 5, 20);
		DataUtil.registerFlammable(EEBlocks.POISE_FENCE.get(), 5, 20);
		DataUtil.registerFlammable(EEBlocks.POISE_FENCE_GATE.get(), 5, 20);
		DataUtil.registerFlammable(EEBlocks.POISE_VERTICAL_PLANKS.get(), 5, 20);
		DataUtil.registerFlammable(EEBlocks.POISE_VERTICAL_SLAB.get(), 5, 20);
		DataUtil.registerFlammable(EEBlocks.POISE_BOOKSHELF.get(), 30, 20);
		DataUtil.registerFlammable(EEBlocks.POISE_BEEHIVE.get(), 5, 20);
		DataUtil.registerFlammable(EEBlocks.BOLLOOM_CRATE.get(), 5, 20);
		DataUtil.registerFlammable(EEBlocks.POISE_POST.get(), 5, 20);
		DataUtil.registerFlammable(EEBlocks.STRIPPED_POISE_POST.get(), 5, 20);
	}

}