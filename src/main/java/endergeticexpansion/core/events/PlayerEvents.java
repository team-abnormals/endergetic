package endergeticexpansion.core.events;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import com.google.common.collect.Maps;

import endergeticexpansion.common.blocks.CorrockBlock;
import endergeticexpansion.common.blocks.CorrockCrownBlock;
import endergeticexpansion.common.blocks.CorrockCrownStandingBlock;
import endergeticexpansion.common.blocks.CorrockCrownWallBlock;
import endergeticexpansion.common.blocks.CorrockPlantBlock;
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
		if((block instanceof CorrockPlantBlock && !((CorrockPlantBlock) block).petrified) || (block instanceof CorrockBlock && !((CorrockBlock) block).petrified) || (block instanceof CorrockCrownBlock && !((CorrockCrownBlock) block).petrified)) {
			world.setBlockState(pos, convertCorrockBlock(state));
		}
	}
	
	public static BlockState convertCorrockBlock(BlockState state) {
		Block block = state.getBlock();
		for(Map.Entry<Supplier<Block>, Supplier<Block>> entries : PETRIFICATION_MAP.entrySet()) {
			Block petrifiedBlock = entries.getValue().get();
			if(entries.getKey().get() == block) {
				if(block instanceof CorrockPlantBlock) {
					return petrifiedBlock.getDefaultState().with(CorrockPlantBlock.WATERLOGGED, state.get(CorrockPlantBlock.WATERLOGGED));
				} else if(block instanceof CorrockBlock) {
					return petrifiedBlock.getDefaultState();
				} else if(block instanceof CorrockCrownStandingBlock) {
					return petrifiedBlock.getDefaultState()
						.with(CorrockCrownStandingBlock.ROTATION, state.get(CorrockCrownStandingBlock.ROTATION))
						.with(CorrockCrownStandingBlock.UPSIDE_DOWN, state.get(CorrockCrownStandingBlock.UPSIDE_DOWN))
						.with(CorrockCrownStandingBlock.WATERLOGGED, state.get(CorrockCrownStandingBlock.WATERLOGGED));
				}
				return petrifiedBlock.getDefaultState().with(CorrockCrownWallBlock.WATERLOGGED, state.get(CorrockCrownWallBlock.WATERLOGGED)).with(CorrockCrownWallBlock.FACING, state.get(CorrockCrownWallBlock.FACING));
			}
		}
		return null;
	}
	
//	@SubscribeEvent
//	public static void onEntityClicked(PlayerInteractEvent.EntityInteract event) {
//		Entity entity = event.getTarget();
//		PlayerEntity player = event.getPlayer();
//		if(event.getItemStack().getItem() instanceof BolloomBalloonItem && !entity.getEntityWorld().isRemote && !player.isShiftKeyDown()) {
//			if(entity instanceof LivingEntity && !(entity instanceof BoofBlockEntity) && !(entity instanceof PoiseClusterEntity)) {
//				entity.getCapability(BalloonProvider.BALLOON_CAP, null)
//				.ifPresent(balloons -> {
//					if(balloons.getBalloonsTied() < 4) {
//						balloons.incrementBalloons(1);
//						BolloomBalloonEntity.addBalloonToEntity(entity);
//						System.out.println(balloons.getBalloonsTied());
//					}
//				});
//			} else if(entity instanceof BoatEntity) {
//				entity.getCapability(BalloonProvider.BALLOON_CAP, null)
//				.ifPresent(balloons -> {
//					if(balloons.getBalloonsTied() < 4) {
//						balloons.incrementBalloons(1);
//						BolloomBalloonEntity.addBalloonToEntity(entity);
//						System.out.println(balloons.getBalloonsTied());
//					}
//				});
//			}
//		}
//	}	
}