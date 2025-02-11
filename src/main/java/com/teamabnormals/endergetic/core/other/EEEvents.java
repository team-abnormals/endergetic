package com.teamabnormals.endergetic.core.other;

import com.google.common.collect.Maps;
import com.teamabnormals.blueprint.client.ClientInfo;
import com.teamabnormals.blueprint.common.world.storage.tracking.IDataManager;
import com.teamabnormals.blueprint.core.util.EntityUtil;
import com.teamabnormals.endergetic.common.advancement.EECriteriaTriggers;
import com.teamabnormals.endergetic.common.block.*;
import com.teamabnormals.endergetic.common.entity.bolloom.BolloomBalloon;
import com.teamabnormals.endergetic.common.entity.purpoid.Purpoid;
import com.teamabnormals.endergetic.common.item.BolloomBalloonItem;
import com.teamabnormals.endergetic.common.network.entity.S2CUpdateBalloonsMessage;
import com.teamabnormals.endergetic.core.EndergeticExpansion;
import com.teamabnormals.endergetic.core.interfaces.BalloonHolder;
import com.teamabnormals.endergetic.core.registry.EEBlocks;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult.Type;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.network.PacketDistributor;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

@EventBusSubscriber(modid = EndergeticExpansion.MOD_ID)
public final class EEEvents {
	private static final AttributeModifier SLOW_BALLOON = new AttributeModifier(UUID.fromString("eb2242e0-d3be-11ea-87d0-0242ac130003"), "Slow falling acceleration reduction", -0.07, AttributeModifier.Operation.ADDITION);
	private static final AttributeModifier SUPER_SLOW_BALLOON = new AttributeModifier(UUID.fromString("b5c9b111-62b3-40da-b396-f90a138583ad"), "Super slow falling acceleration reduction", -0.075, AttributeModifier.Operation.ADDITION);
	private static final AttributeModifier PURPOID_SLOWFALL = new AttributeModifier(UUID.fromString("6bec3438-1392-426b-9173-618fa9499de5"), "Slow falling acceleration reduction from a Purpoid", -0.07, AttributeModifier.Operation.ADDITION);

	public static final Map<Supplier<Block>, Supplier<Block>> PETRIFICATION_MAP = Util.make(Maps.newHashMap(), (petrifications) -> {
		petrifications.put(EEBlocks.END_CORROCK, EEBlocks.PETRIFIED_END_CORROCK);
		petrifications.put(EEBlocks.NETHER_CORROCK, EEBlocks.PETRIFIED_NETHER_CORROCK);
		petrifications.put(EEBlocks.OVERWORLD_CORROCK, EEBlocks.PETRIFIED_OVERWORLD_CORROCK);
		petrifications.put(EEBlocks.END_CORROCK_BLOCK, EEBlocks.PETRIFIED_END_CORROCK_BLOCK);
		petrifications.put(EEBlocks.NETHER_CORROCK_BLOCK, EEBlocks.PETRIFIED_NETHER_CORROCK_BLOCK);
		petrifications.put(EEBlocks.OVERWORLD_CORROCK_BLOCK, EEBlocks.PETRIFIED_OVERWORLD_CORROCK_BLOCK);
		petrifications.put(EEBlocks.SPECKLED_OVERWORLD_CORROCK, EEBlocks.PETRIFIED_SPECKLED_OVERWORLD_CORROCK);
		petrifications.put(EEBlocks.SPECKLED_NETHER_CORROCK, EEBlocks.PETRIFIED_SPECKLED_NETHER_CORROCK);
		petrifications.put(EEBlocks.SPECKLED_END_CORROCK, EEBlocks.PETRIFIED_SPECKLED_END_CORROCK);
		petrifications.put(EEBlocks.END_CORROCK_CROWN::get, EEBlocks.PETRIFIED_END_CORROCK_CROWN::get);
		petrifications.put(EEBlocks.NETHER_CORROCK_CROWN::get, EEBlocks.PETRIFIED_NETHER_CORROCK_CROWN::get);
		petrifications.put(EEBlocks.OVERWORLD_CORROCK_CROWN::get, EEBlocks.PETRIFIED_OVERWORLD_CORROCK_CROWN::get);
		petrifications.put(EEBlocks.END_WALL_CORROCK_CROWN::get, EEBlocks.PETRIFIED_END_WALL_CORROCK_CROWN::get);
		petrifications.put(EEBlocks.NETHER_WALL_CORROCK_CROWN::get, EEBlocks.PETRIFIED_NETHER_WALL_CORROCK_CROWN::get);
		petrifications.put(EEBlocks.OVERWORLD_WALL_CORROCK_CROWN::get, EEBlocks.PETRIFIED_OVERWORLD_WALL_CORROCK_CROWN::get);
		petrifications.put(EEBlocks.INFESTED_CORROCK, EEBlocks.PETRIFIED_INFESTED_CORROCK);
	});

	@SubscribeEvent
	public static void onThrowableImpact(final ProjectileImpactEvent event) {
		Projectile projectileEntity = event.getProjectile();
		if (projectileEntity instanceof ThrownPotion potionEntity) {
			ItemStack itemstack = potionEntity.getItem();
			Potion potion = PotionUtils.getPotion(itemstack);
			List<MobEffectInstance> list = PotionUtils.getMobEffects(itemstack);

			if (potion == Potions.WATER && list.isEmpty() && event.getRayTraceResult() instanceof BlockHitResult blockraytraceresult) {
				Level world = potionEntity.level();
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
	public static void onLivingTick(LivingEvent.LivingTickEvent event) {
		LivingEntity entity = event.getEntity();
		if (!entity.level().isClientSide) {
			int balloonCount = ((BalloonHolder) entity).getBalloons().size();
			AttributeInstance gravity = entity.getAttribute(ForgeMod.ENTITY_GRAVITY.get());

			if (gravity != null) {
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

				if (isFalling && entity.hasPassenger(e -> e instanceof Purpoid)) {
					entity.fallDistance = 0.0F;
					if (!gravity.hasModifier(PURPOID_SLOWFALL)) gravity.addTransientModifier(PURPOID_SLOWFALL);
				} else if (gravity.hasModifier(PURPOID_SLOWFALL)) {
					gravity.removeModifier(PURPOID_SLOWFALL);
				}

				if (balloonCount > 3) {
					entity.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 2, balloonCount - 4, false, false, false));
					if (entity instanceof ServerPlayer serverPlayer) {
						EECriteriaTriggers.UP_UP_AND_AWAY.trigger(serverPlayer);
					}
				}
			}

			if (entity instanceof IDataManager dataManager) {
				int cooldown = dataManager.getValue(EEDataProcessors.CATCHING_COOLDOWN);
				if (cooldown > 0) {
					dataManager.setValue(EEDataProcessors.CATCHING_COOLDOWN, cooldown - 1);
				}
			}
		}
	}

	@SubscribeEvent
	public static void onEntityTracked(PlayerEvent.StartTracking event) {
		ServerPlayer player = (ServerPlayer) event.getEntity();
		Entity trackingEntity = event.getTarget();
		if (trackingEntity instanceof BolloomBalloon balloon) {
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
		BalloonHolder holder = (BalloonHolder) event.getEntity();
		if (!holder.getBalloons().isEmpty()) {
			holder.detachBalloons();
		}
	}

	@SubscribeEvent
	public static void rightClickBlock(PlayerInteractEvent.RightClickBlock event) {
		Level level = event.getLevel();
		BlockPos pos = event.getPos();
		BlockState state = level.getBlockState(pos);
		Player player = event.getEntity();
		ItemStack stack = event.getItemStack();
		Item item = stack.getItem();
		InteractionHand hand = event.getHand();

		if (event.getFace() != Direction.DOWN && item instanceof ShovelItem && !player.isSpectator()) {
			BlockState newState = state.is(EEBlocks.POISMOSS.get()) ? EEBlocks.POISMOSS_PATH.get().defaultBlockState() : state.is(EEBlocks.EUMUS_POISMOSS.get()) ? EEBlocks.EUMUS_POISMOSS_PATH.get().defaultBlockState() : null;
			if (newState != null && level.isEmptyBlock(pos.above())) {
				level.playSound(player, pos, SoundEvents.SHOVEL_FLATTEN, SoundSource.BLOCKS, 1.0F, 1.0F);
				level.setBlock(pos, newState, 11);
				event.getItemStack().hurtAndBreak(1, player, (damage) -> damage.broadcastBreakEvent(hand));
				event.setCancellationResult(InteractionResult.sidedSuccess(level.isClientSide()));
				event.setCanceled(true);
			}
		}
	}

	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public static void onPlayerSwing(InputEvent.InteractionKeyMappingTriggered event) {
		if (event.isAttack()) {
			LocalPlayer player = ClientInfo.getClientPlayer();
			if (player.getXRot() > -25.0F) return;
			Entity ridingEntity = player.getVehicle();
			if (ridingEntity instanceof Boat && BolloomBalloonItem.hasNoEntityTarget(player) && EntityUtil.rayTrace(player, BolloomBalloonItem.getPlayerReach(player), 1.0F).getType() == Type.MISS) {
				List<BolloomBalloon> balloons = ((BalloonHolder) ridingEntity).getBalloons();
				if (!balloons.isEmpty()) {
					Minecraft.getInstance().gameMode.attack(player, balloons.get(player.getRandom().nextInt(balloons.size())));
					event.setSwingHand(true);
				}
			}
		}
	}

	@SubscribeEvent
	public static void onEntityJoinWorld(EntityJoinLevelEvent event) {
		Entity entity = event.getEntity();
		if (entity.level().isClientSide && entity instanceof Purpoid purpoid) {
			purpoid.updatePull(entity.position());
		}
	}

	private static void tryToConvertCorrockBlock(Level world, BlockPos pos) {
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