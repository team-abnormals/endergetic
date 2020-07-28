package com.minecraftabnormals.endergetic.core.registry.other;

import com.minecraftabnormals.endergetic.common.blocks.poise.boof.BoofBlock;
import com.minecraftabnormals.endergetic.common.items.BolloomBalloonItem;
import com.minecraftabnormals.endergetic.common.items.PuffBugBottleItem;
import com.minecraftabnormals.endergetic.core.EndergeticExpansion;
import com.minecraftabnormals.endergetic.core.registry.EEBlocks;
import com.minecraftabnormals.endergetic.core.registry.EEItems;

import net.minecraft.block.DispenserBlock;
import net.minecraft.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.Direction;

public class EEDispenserBehaviors {
	
	static DefaultDispenseItemBehavior spawnEggItemBehavior = new DefaultDispenseItemBehavior() {
    	
        public ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
           Direction direction = source.getBlockState().get(DispenserBlock.FACING);
           EntityType<?> entitytype = ((SpawnEggItem)stack.getItem()).getType(stack.getTag());
           entitytype.spawn(source.getWorld(), stack, (PlayerEntity)null, source.getBlockPos().offset(direction), SpawnReason.DISPENSER, direction != Direction.UP, false);
           stack.shrink(1);
           return stack;
        }
        
    };


	public static void registerAll() {
		DispenserBlock.registerDispenseBehavior(EEBlocks.BOOF_BLOCK.get().asItem(), new BoofBlock.BoofDispenseBehavior());
		DispenserBlock.registerDispenseBehavior(EEItems.BOLLOOM_BALLOON.get(), new BolloomBalloonItem.BalloonDispenseBehavior());
		DispenserBlock.registerDispenseBehavior(EEItems.BOLLOOM_BALLOON_RED.get(), new BolloomBalloonItem.BalloonDispenseBehavior());
		DispenserBlock.registerDispenseBehavior(EEItems.BOLLOOM_BALLOON_ORANGE.get(), new BolloomBalloonItem.BalloonDispenseBehavior());
		DispenserBlock.registerDispenseBehavior(EEItems.BOLLOOM_BALLOON_YELLOW.get(), new BolloomBalloonItem.BalloonDispenseBehavior());
		DispenserBlock.registerDispenseBehavior(EEItems.BOLLOOM_BALLOON_LIME.get(), new BolloomBalloonItem.BalloonDispenseBehavior());
		DispenserBlock.registerDispenseBehavior(EEItems.BOLLOOM_BALLOON_GREEN.get(), new BolloomBalloonItem.BalloonDispenseBehavior());
		DispenserBlock.registerDispenseBehavior(EEItems.BOLLOOM_BALLOON_LIGHT_BLUE.get(), new BolloomBalloonItem.BalloonDispenseBehavior());
		DispenserBlock.registerDispenseBehavior(EEItems.BOLLOOM_BALLOON_CYAN.get(), new BolloomBalloonItem.BalloonDispenseBehavior());
		DispenserBlock.registerDispenseBehavior(EEItems.BOLLOOM_BALLOON_BLUE.get(), new BolloomBalloonItem.BalloonDispenseBehavior());
		DispenserBlock.registerDispenseBehavior(EEItems.BOLLOOM_BALLOON_PINK.get(), new BolloomBalloonItem.BalloonDispenseBehavior());
		DispenserBlock.registerDispenseBehavior(EEItems.BOLLOOM_BALLOON_MAGENTA.get(), new BolloomBalloonItem.BalloonDispenseBehavior());
		DispenserBlock.registerDispenseBehavior(EEItems.BOLLOOM_BALLOON_PURPLE.get(), new BolloomBalloonItem.BalloonDispenseBehavior());
		DispenserBlock.registerDispenseBehavior(EEItems.BOLLOOM_BALLOON_BROWN.get(), new BolloomBalloonItem.BalloonDispenseBehavior());
		DispenserBlock.registerDispenseBehavior(EEItems.BOLLOOM_BALLOON_GRAY.get(), new BolloomBalloonItem.BalloonDispenseBehavior());
		DispenserBlock.registerDispenseBehavior(EEItems.BOLLOOM_BALLOON_LIGHT_GRAY.get(), new BolloomBalloonItem.BalloonDispenseBehavior());
		DispenserBlock.registerDispenseBehavior(EEItems.BOLLOOM_BALLOON_BLACK.get(), new BolloomBalloonItem.BalloonDispenseBehavior());
		DispenserBlock.registerDispenseBehavior(EEItems.PUFFBUG_BOTTLE.get(), new PuffBugBottleItem.PuffBugBottleDispenseBehavior());
		EndergeticExpansion.REGISTRY_HELPER.processSpawnEggDispenseBehaviors();
	}
	
}