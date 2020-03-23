package endergeticexpansion.core.events;

import endergeticexpansion.core.EndergeticExpansion;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EndergeticExpansion.MOD_ID)
public class PlayerEvents {

//	//@SubscribeEvent
//	public static void onEntityClicked(PlayerInteractEvent.EntityInteract event) {
//		Entity entity = event.getTarget();
//		PlayerEntity player = event.getPlayer();
//		if(event.getItemStack().getItem() instanceof ItemBolloomBalloon && !entity.getEntityWorld().isRemote && !player.isShiftKeyDown()) {
//			if(entity instanceof LivingEntity && !(entity instanceof EntityBolloomFruit) && !(entity instanceof EntityBoofBlock) && !(entity instanceof EntityPoiseCluster)) {
//				entity.getCapability(BalloonProvider.BALLOON_CAP, null)
//				.ifPresent(balloons -> {
//					if(balloons.getBalloonsTied() < 4) {
//						balloons.incrementBalloons(1);
//						EntityBolloomBalloon.addBalloonToEntity(entity);
//						System.out.println(balloons.getBalloonsTied());
//					}
//				});
//			} else if(entity instanceof BoatEntity) {
//				entity.getCapability(BalloonProvider.BALLOON_CAP, null)
//				.ifPresent(balloons -> {
//					if(balloons.getBalloonsTied() < 4) {
//						balloons.incrementBalloons(1);
//						EntityBolloomBalloon.addBalloonToEntity(entity);
//						System.out.println(balloons.getBalloonsTied());
//					}
//				});
//			}
//		}
//	}
//	
//	@SubscribeEvent
//	public static void onPlayerTick(PlayerTickEvent event) {
//		PlayerEntity player = event.player;
//    	ItemStack stack = player.getItemStackFromSlot(EquipmentSlotType.CHEST);
//    	
//    	if(player != null && !stack.isEmpty() && stack.getItem() == EEItems.BOOFLO_VEST.get()) {
//    		if(stack.hasTag() && !player.onGround) {
//    			if(stack.getTag().getBoolean("boofed")) {
//    				player.fallDistance = 0;
//    			}
//    		}
//    		if(event.side == LogicalSide.SERVER) {
//    			if(player.onGround) {
//    				if(stack.hasTag()) {
//    					stack.getTag().putInt("timesBoofed", 0);
//    				}
//    			}
//    		}
//    	}
//	}
	
}