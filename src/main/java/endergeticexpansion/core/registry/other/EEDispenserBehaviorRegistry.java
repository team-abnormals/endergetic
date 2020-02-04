package endergeticexpansion.core.registry.other;

import endergeticexpansion.common.blocks.poise.boof.BlockBoof;
import endergeticexpansion.common.items.EndergeticSpawnEgg;
import endergeticexpansion.common.items.ItemBolloomBalloon;
import endergeticexpansion.common.items.ItemPuffBugBottle;
import endergeticexpansion.core.registry.EEBlocks;
import endergeticexpansion.core.registry.EEItems;
import net.minecraft.block.DispenserBlock;
import net.minecraft.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.Direction;
import net.minecraftforge.fml.RegistryObject;

public class EEDispenserBehaviorRegistry {
	
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
		DispenserBlock.registerDispenseBehavior(EEBlocks.BOOF_BLOCK.get().asItem(), new BlockBoof.BoofDispenseBehavior());
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
		
		for(RegistryObject<Item> items : EEItems.SPAWN_EGGS) {
    		Item item = items.get();
    		if(item instanceof EndergeticSpawnEgg) {
    			DispenserBlock.registerDispenseBehavior(item, spawnEggItemBehavior);
    		}
    	}
	}
	
}