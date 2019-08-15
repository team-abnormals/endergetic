package endergeticexpansion.core.events;

import endergeticexpansion.common.capability.balloons.BalloonProvider;
import endergeticexpansion.common.entities.EntityBoofBlock;
import endergeticexpansion.common.entities.EntityPoiseCluster;
import endergeticexpansion.common.entities.bolloom.EntityBolloomBalloon;
import endergeticexpansion.common.entities.bolloom.EntityBolloomFruit;
import endergeticexpansion.common.items.ItemBolloomBalloon;
import endergeticexpansion.core.EndergeticExpansion;
import endergeticexpansion.core.registry.EEItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EndergeticExpansion.MOD_ID)
public class PlayerEvents {

	//@SubscribeEvent
	public static void onEntityClicked(PlayerInteractEvent.EntityInteract event) {
		Entity entity = event.getTarget();
		PlayerEntity player = event.getPlayer();
		if(event.getItemStack().getItem() instanceof ItemBolloomBalloon && !entity.getEntityWorld().isRemote && !player.isSneaking()) {
			if(entity instanceof LivingEntity && !(entity instanceof EntityBolloomFruit) && !(entity instanceof EntityBoofBlock) && !(entity instanceof EntityPoiseCluster)) {
				entity.getCapability(BalloonProvider.BALLOON_CAP, null)
				.ifPresent(balloons -> {
					if(balloons.getBalloonsTied() < 4) {
						balloons.incrementBalloons(1);
						EntityBolloomBalloon.addBalloonToEntity(entity);
						System.out.println(balloons.getBalloonsTied());
					}
				});
			} else if(entity instanceof BoatEntity) {
				entity.getCapability(BalloonProvider.BALLOON_CAP, null)
				.ifPresent(balloons -> {
					if(balloons.getBalloonsTied() < 4) {
						balloons.incrementBalloons(1);
						EntityBolloomBalloon.addBalloonToEntity(entity);
						System.out.println(balloons.getBalloonsTied());
					}
				});
			}
		}
	}
	
	@SubscribeEvent
	public static void onPlayerTick(PlayerTickEvent event) {
		PlayerEntity player = event.player;
    	ItemStack stack = player.getItemStackFromSlot(EquipmentSlotType.CHEST);
    	
    	if(player != null && !stack.isEmpty() && stack.getItem() == EEItems.BOOFLO_VEST) {
    		if(stack.hasTag() && !player.onGround) {
    			if(stack.getTag().getBoolean("boofed")) {
    				player.fallDistance = 0;
    			}
    		}
    		if(event.side == LogicalSide.SERVER) {
    			if(player.onGround) {
    				if(stack.hasTag()) {
    					stack.getTag().putInt("timesBoofed", 0);
    				}
    			}
    		}
    	}
	}
	
}
