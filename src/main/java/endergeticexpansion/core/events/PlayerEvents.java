package endergeticexpansion.core.events;

import endergeticexpansion.common.capability.balloons.BalloonProvider;
import endergeticexpansion.common.entities.EntityBoofBlock;
import endergeticexpansion.common.entities.EntityPoiseCluster;
import endergeticexpansion.common.entities.bolloom.EntityBolloomBalloon;
import endergeticexpansion.common.entities.bolloom.EntityBolloomFruit;
import endergeticexpansion.common.items.ItemBolloomBalloon;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

//@Mod.EventBusSubscriber(modid = EndergeticExpansion.MOD_ID)
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
	
}
