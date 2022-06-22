package com.minecraftabnormals.endergetic.core.events;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

import com.google.common.collect.Maps;
import com.minecraftabnormals.abnormals_core.client.ClientInfo;
import com.minecraftabnormals.abnormals_core.common.world.storage.tracking.IDataManager;
import com.minecraftabnormals.abnormals_core.core.util.EntityUtil;
import com.minecraftabnormals.endergetic.common.advancement.EECriteriaTriggers;
import com.minecraftabnormals.endergetic.common.blocks.*;
import com.minecraftabnormals.endergetic.common.entities.bolloom.BolloomBalloonEntity;
import com.minecraftabnormals.endergetic.common.entities.purpoid.PurpoidEntity;
import com.minecraftabnormals.endergetic.common.items.BolloomBalloonItem;
import com.minecraftabnormals.endergetic.common.network.entity.S2CUpdateBalloonsMessage;
import com.minecraftabnormals.endergetic.core.EndergeticExpansion;
import com.minecraftabnormals.endergetic.core.interfaces.BalloonHolder;
import com.minecraftabnormals.endergetic.core.registry.EEBlocks;

import com.minecraftabnormals.endergetic.core.registry.other.EEDataProcessors;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.PotionEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShovelItem;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;

@Mod.EventBusSubscriber(modid = EndergeticExpansion.MOD_ID)
public final class EntityEvents {
	private static final AttributeModifier SLOW_BALLOON = new AttributeModifier(UUID.fromString("eb2242e0-d3be-11ea-87d0-0242ac130003"), "Slow falling acceleration reduction", -0.07, AttributeModifier.Operation.ADDITION);
	private static final AttributeModifier SUPER_SLOW_BALLOON = new AttributeModifier(UUID.fromString("b5c9b111-62b3-40da-b396-f90a138583ad"), "Super slow falling acceleration reduction", -0.075, AttributeModifier.Operation.ADDITION);
	private static final AttributeModifier PURPOID_SLOWFALL = new AttributeModifier(UUID.fromString("6bec3438-1392-426b-9173-618fa9499de5"), "Slow falling acceleration reduction from a Purpoid", -0.07, AttributeModifier.Operation.ADDITION);

	public static final Map<Supplier<Block>, Supplier<Block>> PETRIFICATION_MAP = Util.make(Maps.newHashMap(), (petrifications) -> {
		petrifications.put(EEBlocks.CORROCK_END, EEBlocks.PETRIFIED_CORROCK_END);
		petrifications.put(EEBlocks.CORROCK_NETHER, EEBlocks.PETRIFIED_CORROCK_NETHER);
		petrifications.put(EEBlocks.CORROCK_OVERWORLD, EEBlocks.PETRIFIED_CORROCK_OVERWORLD);
		petrifications.put(EEBlocks.CORROCK_END_BLOCK, EEBlocks.PETRIFIED_CORROCK_END_BLOCK);
		petrifications.put(EEBlocks.CORROCK_NETHER_BLOCK, EEBlocks.PETRIFIED_CORROCK_NETHER_BLOCK);
		petrifications.put(EEBlocks.CORROCK_OVERWORLD_BLOCK, EEBlocks.PETRIFIED_CORROCK_OVERWORLD_BLOCK);
		petrifications.put(EEBlocks.SPECKLED_OVERWORLD_CORROCK, EEBlocks.PETRIFIED_SPECKLED_OVERWORLD_CORROCK);
		petrifications.put(EEBlocks.SPECKLED_NETHER_CORROCK, EEBlocks.PETRIFIED_SPECKLED_NETHER_CORROCK);
		petrifications.put(EEBlocks.SPECKLED_END_CORROCK, EEBlocks.PETRIFIED_SPECKLED_END_CORROCK);
		petrifications.put(EEBlocks.CORROCK_CROWN_END_STANDING::get, EEBlocks.PETRIFIED_CORROCK_CROWN_END_STANDING::get);
		petrifications.put(EEBlocks.CORROCK_CROWN_NETHER_STANDING::get, EEBlocks.PETRIFIED_CORROCK_CROWN_NETHER_STANDING::get);
		petrifications.put(EEBlocks.CORROCK_CROWN_OVERWORLD_STANDING::get, EEBlocks.PETRIFIED_CORROCK_CROWN_OVERWORLD_STANDING::get);
		petrifications.put(EEBlocks.CORROCK_CROWN_END_WALL::get, EEBlocks.PETRIFIED_CORROCK_CROWN_END_WALL::get);
		petrifications.put(EEBlocks.CORROCK_CROWN_NETHER_WALL::get, EEBlocks.PETRIFIED_CORROCK_CROWN_NETHER_WALL::get);
		petrifications.put(EEBlocks.CORROCK_CROWN_OVERWORLD_WALL::get, EEBlocks.PETRIFIED_CORROCK_CROWN_OVERWORLD_WALL::get);
		petrifications.put(EEBlocks.INFESTED_CORROCK, EEBlocks.PETRIFIED_INFESTED_CORROCK);
	});

	@SubscribeEvent
	public static void onThrowableImpact(final ProjectileImpactEvent.Throwable event) {
		ThrowableEntity projectileEntity = event.getThrowable();

		if (projectileEntity instanceof PotionEntity) {
			PotionEntity potionEntity = ((PotionEntity) projectileEntity);
			ItemStack itemstack = potionEntity.getItem();
			Potion potion = PotionUtils.getPotion(itemstack);
			List<EffectInstance> list = PotionUtils.getMobEffects(itemstack);

			if (potion == Potions.WATER && list.isEmpty() && event.getRayTraceResult() instanceof BlockRayTraceResult) {
				World world = potionEntity.level;
				BlockRayTraceResult blockraytraceresult = (BlockRayTraceResult) event.getRayTraceResult();
				Direction direction = blockraytraceresult.getDirection();
				BlockPos blockpos = blockraytraceresult.getBlockPos().relative(Direction.DOWN).relative(direction);

				tryToConvertCorrockBlock(world, blockpos);
				tryToConvertCorrockBlock(world, blockpos.relative(direction.getOpposite()));
				for (Direction faces : Direction.values()) {
					tryToConvertCorrockBlock(world, blockpos.relative(faces));
				}
			}
		}
	}

	@SubscribeEvent
	public static void onLivingTick(LivingUpdateEvent event) {
		LivingEntity entity = event.getEntityLiving();
		if (!entity.level.isClientSide) {
			int balloonCount = ((BalloonHolder) entity).getBalloons().size();
			ModifiableAttributeInstance gravity = entity.getAttribute(ForgeMod.ENTITY_GRAVITY.get());
			boolean hasABalloon = balloonCount > 0;
			if (hasABalloon) entity.fallDistance = 0.0F;
			boolean isFalling = entity.getDeltaMovement().y <= 0.0D;

			if (isFalling && balloonCount < 3 && hasABalloon) {
				if (!gravity.hasModifier(SLOW_BALLOON)) gravity.addTransientModifier(SLOW_BALLOON);
			} else if (gravity.hasModifier(SLOW_BALLOON)) {
				gravity.removeModifier(SLOW_BALLOON);
			}

			if (isFalling && balloonCount == 3) {
				if (!gravity.hasModifier(SUPER_SLOW_BALLOON)) gravity.addTransientModifier(SUPER_SLOW_BALLOON);
			} else if (gravity.hasModifier(SUPER_SLOW_BALLOON)) {
				gravity.removeModifier(SUPER_SLOW_BALLOON);
			}

			if (isFalling && entity.hasPassenger(PurpoidEntity.class)) {
				entity.fallDistance = 0.0F;
				if (!gravity.hasModifier(PURPOID_SLOWFALL)) gravity.addTransientModifier(PURPOID_SLOWFALL);
			} else if (gravity.hasModifier(PURPOID_SLOWFALL)) {
				gravity.removeModifier(PURPOID_SLOWFALL);
			}

			if (balloonCount > 3) {
				entity.addEffect(new EffectInstance(Effects.LEVITATION, 2, balloonCount - 4, false, false, false));
				if (entity instanceof ServerPlayerEntity) {
					EECriteriaTriggers.UP_UP_AND_AWAY.trigger((ServerPlayerEntity) entity);
				}
			}

			if (entity instanceof IDataManager) {
				IDataManager dataManager = (IDataManager) entity;
				int cooldown = dataManager.getValue(EEDataProcessors.CATCHING_COOLDOWN);
				if (cooldown > 0) {
					dataManager.setValue(EEDataProcessors.CATCHING_COOLDOWN, cooldown - 1);
				}
			}
		}
	}

	@SubscribeEvent
	public static void onEntityTracked(PlayerEvent.StartTracking event) {
		ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
		Entity trackingEntity = event.getTarget();
		if (trackingEntity instanceof BolloomBalloonEntity) {
			BolloomBalloonEntity balloon = (BolloomBalloonEntity) trackingEntity;
			Entity attachedEntity = balloon.getAttachedEntity();
			if (attachedEntity != null) {
				EndergeticExpansion.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new S2CUpdateBalloonsMessage(attachedEntity));
			}
		} else {
			EndergeticExpansion.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new S2CUpdateBalloonsMessage(trackingEntity));
		}
	}

	@SubscribeEvent
	public static void onPlayerChangeDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
		BalloonHolder holder = (BalloonHolder) event.getPlayer();
		if (!holder.getBalloons().isEmpty()) {
			holder.detachBalloons();
		}
	}

	@SubscribeEvent
	public static void rightClickBlock(PlayerInteractEvent.RightClickBlock event) {
		World world = event.getWorld();
		BlockPos pos = event.getPos();
		BlockState state = world.getBlockState(pos);
		PlayerEntity player = event.getPlayer();
		ItemStack stack = event.getItemStack();
		Item item = stack.getItem();
		Hand hand = event.getHand();

		if (event.getFace() != Direction.DOWN && item instanceof ShovelItem && !player.isSpectator()) {
			BlockState newState = state.is(EEBlocks.POISMOSS.get()) ? EEBlocks.POISMOSS_PATH.get().defaultBlockState() : state.is(EEBlocks.EUMUS_POISMOSS.get()) ? EEBlocks.EUMUS_POISMOSS_PATH.get().defaultBlockState() : null;
			if (newState != null && world.isEmptyBlock(pos.above())) {
				world.playSound(player, pos, SoundEvents.SHOVEL_FLATTEN, SoundCategory.BLOCKS, 1.0F, 1.0F);
				world.setBlock(pos, newState, 11);
				event.getItemStack().hurtAndBreak(1, player, (damage) -> damage.broadcastBreakEvent(hand));
				event.setCancellationResult(ActionResultType.sidedSuccess(world.isClientSide()));
				event.setCanceled(true);
			}
		}
	}

	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public static void onPlayerSwing(InputEvent.ClickInputEvent event) {
		if (event.isAttack()) {
			ClientPlayerEntity player = ClientInfo.getClientPlayer();
			if (player.xRot > -25.0F) return;
			Entity ridingEntity = player.getVehicle();
			if (ridingEntity instanceof BoatEntity && BolloomBalloonItem.hasNoEntityTarget(player) && EntityUtil.rayTrace(player, BolloomBalloonItem.getPlayerReach(player), 1.0F).getType() == Type.MISS) {
				List<BolloomBalloonEntity> balloons = ((BalloonHolder) ridingEntity).getBalloons();
				if (!balloons.isEmpty()) {
					Minecraft.getInstance().gameMode.attack(player, balloons.get(player.getRandom().nextInt(balloons.size())));
					event.setSwingHand(true);
				}
			}
		}
	}

	@SubscribeEvent
	public static void onEntityJoinWorld(EntityJoinWorldEvent event) {
		Entity entity = event.getEntity();
		if (entity.level.isClientSide && entity instanceof PurpoidEntity) {
			((PurpoidEntity) entity).updatePull(entity.position());
		}
	}

	private static void tryToConvertCorrockBlock(World world, BlockPos pos) {
		BlockState state = world.getBlockState(pos);
		Block block = state.getBlock();
		if ((block instanceof CorrockPlantBlock && !((CorrockPlantBlock) block).petrified) || block instanceof CorrockBlock || block instanceof SpeckledCorrockBlock || block instanceof InfestedCorrockBlock || (block instanceof CorrockCrownBlock && !((CorrockCrownBlock) block).petrified)) {
			BlockState convertedState = convertCorrockBlock(state);
			if (convertedState != null) {
				world.setBlockAndUpdate(pos, convertedState);
			}
		}
	}

	public static BlockState convertCorrockBlock(BlockState state) {
		Block block = state.getBlock();
		for (Map.Entry<Supplier<Block>, Supplier<Block>> entries : PETRIFICATION_MAP.entrySet()) {
			Block petrifiedBlock = entries.getValue().get();
			if (entries.getKey().get() == block) {
				if (block instanceof CorrockPlantBlock) {
					return petrifiedBlock.defaultBlockState().setValue(CorrockPlantBlock.WATERLOGGED, state.getValue(CorrockPlantBlock.WATERLOGGED));
				} else if (block instanceof CorrockBlock || block instanceof SpeckledCorrockBlock || block instanceof InfestedCorrockBlock) {
					return petrifiedBlock.defaultBlockState();
				} else if (block instanceof CorrockCrownStandingBlock) {
					return petrifiedBlock.defaultBlockState()
							.setValue(CorrockCrownStandingBlock.ROTATION, state.getValue(CorrockCrownStandingBlock.ROTATION))
							.setValue(CorrockCrownStandingBlock.UPSIDE_DOWN, state.getValue(CorrockCrownStandingBlock.UPSIDE_DOWN))
							.setValue(CorrockCrownStandingBlock.WATERLOGGED, state.getValue(CorrockCrownStandingBlock.WATERLOGGED));
				}
				return petrifiedBlock.defaultBlockState().setValue(CorrockCrownWallBlock.WATERLOGGED, state.getValue(CorrockCrownWallBlock.WATERLOGGED)).setValue(CorrockCrownWallBlock.FACING, state.getValue(CorrockCrownWallBlock.FACING));
			}
		}
		return null;
	}
}