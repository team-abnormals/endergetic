package com.minecraftabnormals.endergetic.common.items;

import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import com.minecraftabnormals.endergetic.common.entities.BoofBlockEntity;
import com.minecraftabnormals.endergetic.common.entities.PoiseClusterEntity;
import com.minecraftabnormals.endergetic.common.entities.bolloom.BolloomBalloonEntity;
import com.minecraftabnormals.endergetic.common.entities.bolloom.BolloomKnotEntity;
import com.minecraftabnormals.endergetic.core.registry.EEEntities;
import com.teamabnormals.abnormals_core.core.utils.EntityUtils;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.FenceBlock;
import net.minecraft.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeMod;

public class BolloomBalloonItem extends Item {
	@Nullable
	private final DyeColor balloonColor;
	
	public BolloomBalloonItem(Properties properties, @Nullable DyeColor balloonColor) {
		super(properties);
		this.balloonColor = balloonColor;
	}
	
	@Nullable
	public DyeColor getBalloonColor() {
		return this.balloonColor;
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
		ItemStack stack = player.getHeldItem(hand);
		if (!world.isRemote && this.canAttachBalloonToTarget(player) && !this.hasEntityTarget(player) && EntityUtils.rayTrace(player, this.getPlayerReach(player), 1.0F).getType() == Type.MISS && !player.isSneaking()) {
			this.attachToEntity(player, player);
			if (!player.isCreative()) stack.shrink(1);
			return ActionResult.resultConsume(stack);
		}
		return ActionResult.resultPass(stack);
	}
	
	@Override
	public ActionResultType onItemUse(ItemUseContext context) {
		BlockPos pos = context.getPos();
		World world = context.getWorld();
		Block block = world.getBlockState(pos).getBlock();
		
		if (block instanceof FenceBlock) {
			if (this.isAirUpwards(world, pos)) {
				if (!world.isRemote) {
					this.attachToFence(pos, world, context.getItem());
				}
			} else {
				return ActionResultType.FAIL;
			}
			for (Entity entity : world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(pos))) {
				if (entity instanceof BolloomKnotEntity) {
					if (((BolloomKnotEntity) entity).hasMaxBalloons()) {
						return ActionResultType.FAIL;
					}
				}
	        }
		}
		return ActionResultType.PASS;
	}
	
	@Override
	public ActionResultType itemInteractionForEntity(ItemStack stack, PlayerEntity player, LivingEntity target, Hand hand) {
		World world = player.world;
		if (!world.isRemote && this.canAttachBalloonToTarget(target)) {
			this.attachToEntity(player, target);
			if (!player.isCreative()) stack.shrink(1);
			return ActionResultType.CONSUME;
		}
		return ActionResultType.PASS;
	}
	
	private boolean canAttachBalloonToTarget(LivingEntity target) {
		return !(target instanceof BoofBlockEntity && target instanceof PoiseClusterEntity) && target.getPassengers().stream().filter(rider -> rider instanceof BolloomBalloonEntity).collect(Collectors.toList()).size() < 6;
	}
	
	private void attachToEntity(PlayerEntity player, LivingEntity target) {
		World world = target.world;
		BolloomBalloonEntity balloon = EEEntities.BOLLOOM_BALLOON.get().create(world);
		balloon.setColor(this.balloonColor);
		balloon.setPosition(target.getPosX() + balloon.getSway() * Math.sin(-balloon.getAngle()), target.getPosY() + balloon.getMountedYOffset() + target.getEyeHeight(), target.getPosZ() + balloon.getSway() * Math.cos(-balloon.getAngle()));
		balloon.setUntied();
		balloon.startRiding(target, true);
		world.addEntity(balloon);
	}

	private void attachToFence(BlockPos fencePos, World world, ItemStack stack) {
		for (Entity entity : world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(fencePos))) {
			if (entity instanceof BolloomKnotEntity) {
				if (!((BolloomKnotEntity) entity).hasMaxBalloons()) {
					BolloomKnotEntity setKnot = BolloomKnotEntity.getKnotForPosition(world, fencePos);
					setKnot.addBalloon(this.getBalloonColor());
					stack.shrink(1);
				}
			}
        }
		if (BolloomKnotEntity.getKnotForPosition(world, fencePos) == null) {
			BolloomKnotEntity.createStartingKnot(world, fencePos, this.getBalloonColor());
			stack.shrink(1);
		}
	}
	
	private boolean isAirUpwards(World world, BlockPos pos) {
		BlockPos.Mutable mutable = new BlockPos.Mutable(pos.getX(), pos.getY(),pos.getZ());
		for (int i = 0; i < 4; i++) {
			if (!world.isAirBlock(mutable.move(0, 1, 0))) {
				return false;
			}
		}
		return true;
	}
	
	private double getPlayerReach(PlayerEntity player) {
		double reach = player.getAttribute(ForgeMod.REACH_DISTANCE.get()).getValue();
		return (player.isCreative() ? reach : reach - 0.5F);
	}
	
	private boolean hasEntityTarget(PlayerEntity player) {
		double distance = this.getPlayerReach(player);
		Vector3d vec3d = player.getEyePosition(1.0F);
		Vector3d vec3d1 = player.getLook(1.0F).scale(distance);
		Vector3d vec3d2 = vec3d.add(vec3d1);
		AxisAlignedBB axisalignedbb = player.getBoundingBox().expand(vec3d1).grow(1.0D);
		double sqrDistance = distance * distance;
		Predicate<Entity> predicate = (p_217727_0_) -> {
			return !p_217727_0_.isSpectator() && p_217727_0_.canBeCollidedWith();
		};
		EntityRayTraceResult entityraytraceresult = ProjectileHelper.rayTraceEntities(player, vec3d, vec3d2, axisalignedbb, predicate, sqrDistance);
		if (entityraytraceresult == null) {
			return false;
		} else {
			return !(vec3d.squareDistanceTo(entityraytraceresult.getHitVec()) > sqrDistance);
		}
	}

	public static class BalloonDispenseBehavior extends DefaultDispenseItemBehavior {
		
		@Override
		protected ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
			BlockPos blockpos = source.getBlockPos().offset(source.getBlockState().get(DispenserBlock.FACING));
			World world = source.getWorld();
			BlockState state = world.getBlockState(blockpos);
			if(state.getMaterial().isReplaceable() && stack.getItem() instanceof BolloomBalloonItem) {
				BolloomBalloonEntity balloon = new BolloomBalloonEntity(world, blockpos);
				balloon.setColor(((BolloomBalloonItem)stack.getItem()).getBalloonColor());
				world.addEntity(balloon);
				stack.shrink(1);
			} else if(!state.getMaterial().isReplaceable() && !state.getBlock().isIn(BlockTags.FENCES)) {
				return super.dispenseStack(source, stack);
			} else if(state.getBlock().isIn(BlockTags.FENCES)) {
				if(BolloomKnotEntity.getKnotForPosition(world, blockpos) == null && stack.getItem() instanceof BolloomBalloonItem) {
					BolloomKnotEntity.createStartingKnot(world, blockpos, ((BolloomBalloonItem)stack.getItem()).getBalloonColor());
					stack.shrink(1);
					return stack;
				} else {
					for (Entity entity : world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(blockpos))) {
						if(entity instanceof BolloomKnotEntity && stack.getItem() instanceof BolloomBalloonItem) {
							if(!((BolloomKnotEntity)entity).hasMaxBalloons()) {
								BolloomKnotEntity setKnot = BolloomKnotEntity.getKnotForPosition(world, blockpos);
								setKnot.addBalloon(((BolloomBalloonItem)stack.getItem()).getBalloonColor());
								stack.shrink(1);
							} else {
								return super.dispenseStack(source, stack);
							}
						}
			        }
				}
			} else {
				return super.dispenseStack(source, stack);
			}
			return stack;
		}
		
	}
	
}
