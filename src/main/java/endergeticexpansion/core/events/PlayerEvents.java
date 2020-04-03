package endergeticexpansion.core.events;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import com.google.common.collect.Maps;

import endergeticexpansion.common.blocks.BlockCorrock;
import endergeticexpansion.common.blocks.BlockCorrockBlock;
import endergeticexpansion.common.blocks.BlockCorrockCrown;
import endergeticexpansion.common.blocks.BlockCorrockCrownStanding;
import endergeticexpansion.common.blocks.BlockCorrockCrownWall;
import endergeticexpansion.core.EndergeticExpansion;
import endergeticexpansion.core.registry.EEBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.projectile.PotionEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.util.Direction;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EndergeticExpansion.MOD_ID)
public class PlayerEvents {
	public static final Map<Supplier<Block>, Supplier<Block>> PETRIFICATION_MAP = Util.make(Maps.newHashMap(), (petrifications) -> {
		petrifications.put(() -> EEBlocks.CORROCK_END.get(), () -> EEBlocks.PETRIFIED_CORROCK_END.get());
		petrifications.put(() -> EEBlocks.CORROCK_NETHER.get(), () -> EEBlocks.PETRIFIED_CORROCK_NETHER.get());
		petrifications.put(() -> EEBlocks.CORROCK_OVERWORLD.get(), () -> EEBlocks.PETRIFIED_CORROCK_OVERWORLD.get());
		petrifications.put(() -> EEBlocks.CORROCK_END_BLOCK.get(), () -> EEBlocks.PETRIFIED_CORROCK_END_BLOCK.get());
		petrifications.put(() -> EEBlocks.CORROCK_NETHER_BLOCK.get(), () -> EEBlocks.PETRIFIED_CORROCK_NETHER_BLOCK.get());
		petrifications.put(() -> EEBlocks.CORROCK_OVERWORLD_BLOCK.get(), () -> EEBlocks.PETRIFIED_CORROCK_OVERWORLD_BLOCK.get());
		petrifications.put(() -> EEBlocks.CORROCK_CROWN_END_STANDING.get(), () -> EEBlocks.PETRIFIED_CORROCK_CROWN_END_STANDING.get());
		petrifications.put(() -> EEBlocks.CORROCK_CROWN_NETHER_STANDING.get(), () -> EEBlocks.PETRIFIED_CORROCK_CROWN_NETHER_STANDING.get());
		petrifications.put(() -> EEBlocks.CORROCK_CROWN_OVERWORLD_STANDING.get(), () -> EEBlocks.PETRIFIED_CORROCK_CROWN_OVERWORLD_STANDING.get());
		petrifications.put(() -> EEBlocks.CORROCK_CROWN_END_WALL.get(), () -> EEBlocks.PETRIFIED_CORROCK_CROWN_END_WALL.get());
		petrifications.put(() -> EEBlocks.CORROCK_CROWN_NETHER_WALL.get(), () -> EEBlocks.PETRIFIED_CORROCK_CROWN_NETHER_WALL.get());
		petrifications.put(() -> EEBlocks.CORROCK_CROWN_OVERWORLD_WALL.get(), () -> EEBlocks.PETRIFIED_CORROCK_CROWN_OVERWORLD_WALL.get());
	});
	
	@SubscribeEvent
	public static void onThrowableImpact(final ProjectileImpactEvent.Throwable event) {
		ThrowableEntity projectileEntity = event.getThrowable();

		if(projectileEntity instanceof PotionEntity) {
			PotionEntity potionEntity = ((PotionEntity) projectileEntity);
			ItemStack itemstack = potionEntity.getItem();
			Potion potion = PotionUtils.getPotionFromItem(itemstack);
			List<EffectInstance> list = PotionUtils.getEffectsFromStack(itemstack);

			if(potion == Potions.WATER && list.isEmpty() && event.getRayTraceResult() instanceof BlockRayTraceResult) {
				World world = potionEntity.world;
				BlockRayTraceResult blockraytraceresult = (BlockRayTraceResult) event.getRayTraceResult();
				Direction direction = blockraytraceresult.getFace();
				BlockPos blockpos = blockraytraceresult.getPos().offset(Direction.DOWN).offset(direction);
            	
				tryToConvertCorrockBlock(world, blockpos);
				tryToConvertCorrockBlock(world, blockpos.offset(direction.getOpposite()));
				for(Direction horizontals : Direction.Plane.HORIZONTAL) {
					tryToConvertCorrockBlock(world, blockpos.offset(horizontals));
				}
			}
		}
	}
	
	private static void tryToConvertCorrockBlock(World world, BlockPos pos) {
		BlockState state = world.getBlockState(pos);
		Block block = state.getBlock();
		if((block instanceof BlockCorrock && !((BlockCorrock) block).petrified) || (block instanceof BlockCorrockBlock && !((BlockCorrockBlock) block).petrified) || (block instanceof BlockCorrockCrown && !((BlockCorrockCrown) block).petrified)) {
			world.setBlockState(pos, convertCorrockBlock(state));
		}
	}
	
	public static BlockState convertCorrockBlock(BlockState state) {
		Block block = state.getBlock();
		for(Map.Entry<Supplier<Block>, Supplier<Block>> entries : PETRIFICATION_MAP.entrySet()) {
			Block petrifiedBlock = entries.getValue().get();
			if(entries.getKey().get() == block) {
				if(block instanceof BlockCorrock) {
					return petrifiedBlock.getDefaultState().with(BlockCorrock.WATERLOGGED, state.get(BlockCorrock.WATERLOGGED));
				} else if(block instanceof BlockCorrockBlock) {
					return petrifiedBlock.getDefaultState();
				} else if(block instanceof BlockCorrockCrownStanding) {
					return petrifiedBlock.getDefaultState()
						.with(BlockCorrockCrownStanding.ROTATION, state.get(BlockCorrockCrownStanding.ROTATION))
						.with(BlockCorrockCrownStanding.UPSIDE_DOWN, state.get(BlockCorrockCrownStanding.UPSIDE_DOWN))
						.with(BlockCorrockCrownStanding.WATERLOGGED, state.get(BlockCorrockCrownStanding.WATERLOGGED));
				}
				return petrifiedBlock.getDefaultState().with(BlockCorrockCrownWall.WATERLOGGED, state.get(BlockCorrockCrownWall.WATERLOGGED)).with(BlockCorrockCrownWall.FACING, state.get(BlockCorrockCrownWall.FACING));
			}
		}
		return null;
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
}