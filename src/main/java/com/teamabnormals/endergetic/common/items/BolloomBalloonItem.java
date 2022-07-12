package com.teamabnormals.endergetic.common.items;

import java.util.Optional;
import java.util.function.Predicate;

import com.teamabnormals.endergetic.common.entities.bolloom.BalloonColor;
import com.teamabnormals.endergetic.common.entities.bolloom.BolloomBalloonEntity;
import com.teamabnormals.endergetic.common.entities.bolloom.BolloomKnotEntity;
import com.teamabnormals.endergetic.core.interfaces.BalloonHolder;
import com.teamabnormals.endergetic.core.registry.EEEntities;
import com.teamabnormals.endergetic.core.registry.other.EETags;

import com.teamabnormals.blueprint.core.util.EntityUtil;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.BlockSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult.Type;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeMod;

public class BolloomBalloonItem extends Item {
	private final BalloonColor balloonColor;

	public BolloomBalloonItem(Properties properties, BalloonColor balloonColor) {
		super(properties);
		this.balloonColor = balloonColor;
	}

	public BalloonColor getBalloonColor() {
		return this.balloonColor;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (!world.isClientSide && hasNoEntityTarget(player) && EntityUtil.rayTrace(player, getPlayerReach(player), 1.0F).getType() == Type.MISS && !player.isShiftKeyDown()) {
			Entity ridingEntity = player.getVehicle();
			boolean isRidingBoat = ridingEntity instanceof Boat;
			if (isRidingBoat && canAttachBalloonToTarget(ridingEntity)) {
				attachToEntity(this.balloonColor, ridingEntity);
				player.swing(hand, true);
				if (!player.isCreative()) stack.shrink(1);
				return InteractionResultHolder.consume(stack);
			}

			if (!isRidingBoat && canAttachBalloonToTarget(player)) {
				attachToEntity(this.balloonColor, player);
				player.swing(hand, true);
				if (!player.isCreative()) stack.shrink(1);
				return InteractionResultHolder.consume(stack);
			}
		}
		return InteractionResultHolder.pass(stack);
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		BlockPos pos = context.getClickedPos();
		Level world = context.getLevel();
		Block block = world.getBlockState(pos).getBlock();

		if (block instanceof FenceBlock) {
			if (this.isAirUpwards(world, pos)) {
				if (!world.isClientSide) {
					ItemStack stack = context.getItemInHand();
					if (this.attachToFence(pos, world, stack)) {
						stack.shrink(1);
						return InteractionResult.SUCCESS;
					}
				}
			} else {
				return InteractionResult.FAIL;
			}
			for (Entity entity : world.getEntitiesOfClass(Entity.class, new AABB(pos))) {
				if (entity instanceof BolloomKnotEntity) {
					if (((BolloomKnotEntity) entity).hasMaxBalloons()) {
						return InteractionResult.FAIL;
					}
				}
			}
		}
		return InteractionResult.PASS;
	}

	@Override
	public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
		Level world = player.level;
		if (!world.isClientSide && canAttachBalloonToTarget(target)) {
			player.swing(hand, true);
			attachToEntity(this.balloonColor, target);
			if (!player.isCreative()) stack.shrink(1);
			return InteractionResult.CONSUME;
		}
		return InteractionResult.PASS;
	}

	public static boolean canAttachBalloonToTarget(Entity target) {
		return !target.getType().is(EETags.EntityTypes.NOT_BALLOON_ATTACHABLE) && ((BalloonHolder) target).getBalloons().size() < (target instanceof Boat ? 4 : 6);
	}

	public static void attachToEntity(BalloonColor color, Entity target) {
		Level world = target.level;
		BolloomBalloonEntity balloon = EEEntities.BOLLOOM_BALLOON.get().create(world);
		if (balloon != null) {
			balloon.setColor(color);
			balloon.attachToEntity(target);
			balloon.updateAttachedPosition();
			balloon.setUntied(true);
			world.addFreshEntity(balloon);
		}
	}

	private boolean attachToFence(BlockPos fencePos, Level world, ItemStack stack) {
		BolloomKnotEntity setKnot = BolloomKnotEntity.getKnotForPosition(world, fencePos);
		if (setKnot != null && !setKnot.hasMaxBalloons()) {
			setKnot.addBalloon(this.getBalloonColor());
			return true;
		} else if (setKnot == null) {
			BolloomKnotEntity.createStartingKnot(world, fencePos, this.getBalloonColor());
			return true;
		}
		return false;
	}

	private boolean isAirUpwards(Level world, BlockPos pos) {
		BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos(pos.getX(), pos.getY(), pos.getZ());
		for (int i = 0; i < 4; i++) {
			if (!world.isEmptyBlock(mutable.move(0, 1, 0))) {
				return false;
			}
		}
		return true;
	}

	public static double getPlayerReach(Player player) {
		double reach = player.getAttribute(ForgeMod.REACH_DISTANCE.get()).getValue();
		return (player.isCreative() ? reach : reach - 0.5F);
	}

	public static boolean hasNoEntityTarget(Player player) {
		double distance = getPlayerReach(player);
		Vec3 vec3d = player.getEyePosition(1.0F);
		Vec3 vec3d1 = player.getViewVector(1.0F).scale(distance);
		Vec3 vec3d2 = vec3d.add(vec3d1);
		AABB axisalignedbb = player.getBoundingBox().expandTowards(vec3d1).inflate(1.0D);
		double sqrDistance = distance * distance;
		Predicate<Entity> predicate = (entity) -> !entity.isSpectator() && entity.isPickable();
		EntityHitResult entityraytraceresult = rayTraceEntities(player, vec3d, vec3d2, axisalignedbb, predicate, sqrDistance);
		if (entityraytraceresult == null) {
			return true;
		} else {
			return vec3d.distanceToSqr(entityraytraceresult.getLocation()) > sqrDistance;
		}
	}

	private static EntityHitResult rayTraceEntities(Entity shooter, Vec3 startVec, Vec3 endVec, AABB boundingBox, Predicate<Entity> filter, double distance) {
		Level world = shooter.level;
		double d0 = distance;
		Entity entity = null;
		Vec3 vector3d = null;

		for (Entity entity1 : world.getEntities(shooter, boundingBox, filter)) {
			AABB axisalignedbb = entity1.getBoundingBox().inflate(entity1.getPickRadius());
			Optional<Vec3> optional = axisalignedbb.clip(startVec, endVec);
			if (axisalignedbb.contains(startVec)) {
				if (d0 >= 0.0D) {
					entity = entity1;
					vector3d = optional.orElse(startVec);
					d0 = 0.0D;
				}
			} else if (optional.isPresent()) {
				Vec3 vector3d1 = optional.get();
				double d1 = startVec.distanceToSqr(vector3d1);
				if (d1 < d0 || d0 == 0.0D) {
					if (entity1.getRootVehicle() == shooter.getRootVehicle() && !entity1.canRiderInteract()) {
						if (d0 == 0.0D) {
							entity = entity1;
							vector3d = vector3d1;
						}
					} else {
						entity = entity1;
						vector3d = vector3d1;
						d0 = d1;
					}
				}
			}
		}
		return entity == null ? null : new EntityHitResult(entity, vector3d);
	}

	public static class BalloonDispenseBehavior extends DefaultDispenseItemBehavior {

		@Override
		protected ItemStack execute(BlockSource source, ItemStack stack) {
			BlockPos blockpos = source.getPos().relative(source.getBlockState().getValue(DispenserBlock.FACING));
			Level world = source.getLevel();
			BlockState state = world.getBlockState(blockpos);

			for (Entity entity : world.getEntitiesOfClass(Entity.class, new AABB(blockpos))) {
				if (!world.isClientSide && (entity instanceof LivingEntity || entity instanceof Boat) && canAttachBalloonToTarget(entity)) {
					attachToEntity(((BolloomBalloonItem) stack.getItem()).getBalloonColor(), entity);
					stack.shrink(1);
					return stack;
				}
			}

			if (state.getMaterial().isReplaceable()) {
				BolloomBalloonEntity balloon = new BolloomBalloonEntity(world, blockpos);
				balloon.setColor(((BolloomBalloonItem) stack.getItem()).getBalloonColor());
				world.addFreshEntity(balloon);
				stack.shrink(1);
			} else if (!state.getMaterial().isReplaceable() && !state.is(BlockTags.FENCES)) {
				return super.execute(source, stack);
			} else if (state.is(BlockTags.FENCES)) {
				BolloomKnotEntity setKnot = BolloomKnotEntity.getKnotForPosition(world, blockpos);
				if (setKnot == null) {
					BolloomKnotEntity.createStartingKnot(world, blockpos, ((BolloomBalloonItem) stack.getItem()).getBalloonColor());
					stack.shrink(1);
					return stack;
				} else {
					if (!setKnot.hasMaxBalloons()) {
						setKnot.addBalloon(((BolloomBalloonItem) stack.getItem()).getBalloonColor());
						stack.shrink(1);
					} else {
						return super.execute(source, stack);
					}
				}
			} else {
				return super.execute(source, stack);
			}
			return stack;
		}

	}
}