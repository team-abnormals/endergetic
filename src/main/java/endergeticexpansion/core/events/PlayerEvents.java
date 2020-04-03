package endergeticexpansion.core.events;

import java.util.List;

import endergeticexpansion.core.EndergeticExpansion;
import net.minecraft.block.Blocks;
import net.minecraft.entity.projectile.PotionEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EndergeticExpansion.MOD_ID)
public class PlayerEvents {
	
	@SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void onThrowableImpact(final ProjectileImpactEvent.Throwable event) {
        ThrowableEntity projectileEntity = event.getThrowable();

        if (projectileEntity instanceof PotionEntity) {
            PotionEntity potionEntity = ((PotionEntity) projectileEntity);
            ItemStack itemstack = potionEntity.getItem();
            Potion potion = PotionUtils.getPotionFromItem(itemstack);
            List<EffectInstance> list = PotionUtils.getEffectsFromStack(itemstack);

            if (potion == Potions.WATER && list.isEmpty() && event.getRayTraceResult() instanceof BlockRayTraceResult) {
            	BlockRayTraceResult blockraytraceresult = (BlockRayTraceResult)event.getRayTraceResult();
            	Direction direction = blockraytraceresult.getFace();
            	BlockPos blockpos = blockraytraceresult.getPos().offset(Direction.DOWN).offset(direction);
            	
        		potionEntity.world.setBlockState(blockpos, Blocks.BRAIN_CORAL_BLOCK.getDefaultState());
        		potionEntity.world.setBlockState(blockpos.offset(direction.getOpposite()), Blocks.BRAIN_CORAL_BLOCK.getDefaultState());
            	for(Direction direction1 : Direction.Plane.HORIZONTAL) {
            		potionEntity.world.setBlockState(blockpos.offset(direction1), Blocks.BRAIN_CORAL_BLOCK.getDefaultState());
            	}
            }
        }
    }
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