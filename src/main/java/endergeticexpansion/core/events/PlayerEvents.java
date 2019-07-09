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
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

@Mod.EventBusSubscriber(modid = EndergeticExpansion.MOD_ID)
public class PlayerEvents {

	//@SubscribeEvent
	public static void onEntityClicked(PlayerInteractEvent.EntityInteract event) {
		Entity entity = event.getTarget();
		PlayerEntity player = event.getEntityPlayer();
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
    	ItemStack stack = player.inventory.armorItemInSlot(2);
    	
    	if(!stack.isEmpty() && stack.getItem() == EEItems.BOOFLO_VEST && !player.onGround) {
    		if(stack.hasTag()) {
    			if(stack.getTag().getBoolean("boofed")) {
    				player.fallDistance = 0;
    			}
    		}
    	}
    	
	}
	
}
