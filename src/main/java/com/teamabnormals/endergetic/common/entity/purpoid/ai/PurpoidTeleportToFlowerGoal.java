package com.teamabnormals.endergetic.common.entity.purpoid.ai;

import com.teamabnormals.endergetic.common.entity.purpoid.Purpoid;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class PurpoidTeleportToFlowerGoal extends AbstractPurpoidTeleportGoal {

	public PurpoidTeleportToFlowerGoal(Purpoid purpoid) {
		super(purpoid);
		this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
	}

	@Override
	public boolean canUse() {
		return !this.purpoid.hasRestCooldown() && super.canUse();
	}

	@Override
	protected void beginTeleportation(Purpoid purpoid, BlockPos pos) {
		super.beginTeleportation(purpoid, pos);
		BlockPos restingPos = pos.below(3);
		purpoid.setRestingPos(restingPos);
		purpoid.setRestingSide(Direction.UP);
		var nearbyPurpoids = purpoid.level.getEntitiesOfClass(Purpoid.class, purpoid.getBoundingBox().inflate(12.0D, 12.0D, 12.0D));
		for (Purpoid nearbyPurpoid : nearbyPurpoids) {
			Purpoid leaderOfNearbyPurpoid = nearbyPurpoid.getLeader();
			if ((leaderOfNearbyPurpoid == purpoid || leaderOfNearbyPurpoid == null && nearbyPurpoid.distanceToSqr(purpoid) <= 9.0F) && nearbyPurpoid.isBaby() && !nearbyPurpoid.getTeleportController().isTeleporting() && !nearbyPurpoid.isResting()) {
				nearbyPurpoid.forcedRelativeTeleportingPos = restingPos;
				nearbyPurpoid.getNavigation().stop();
				nearbyPurpoid.setDeltaMovement(Vec3.ZERO);
				nearbyPurpoid.allowRest();
			}
		}
	}

	@Nullable
	@Override
	protected BlockPos generateTeleportPos(Purpoid purpoid, RandomSource random) {
		BlockPos pos = purpoid.blockPosition();
		int originX = pos.getX();
		int originY = pos.getY();
		int originZ = pos.getZ();
		BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
		Level level = purpoid.level;
		List<BlockPos> teleportPositions = new ArrayList<>();
		for (int x = originX - 16; x <= originX + 16; x++) {
			for (int y = originY - 16; y <= originY + 16; y++) {
				for (int z = originZ - 16; z <= originZ + 16; z++) {
					mutable.set(x, y, z);
					if (level.getBlockState(mutable).getBlock() == Blocks.CHORUS_FLOWER && level.getBlockState(mutable.setY(mutable.getY() + 1)).isAir() && level.getBlockState(mutable.setY(mutable.getY() + 1)).isAir() && level.getBlockState(mutable.setY(mutable.getY() + 1)).isAir()) {
						if (isRestingPosNotBlocked(purpoid, Vec3.atBottomCenterOf(mutable), mutable.below(3), Direction.UP))
							teleportPositions.add(mutable.immutable());
					}
				}
			}
		}
		return teleportPositions.isEmpty() ? null : teleportPositions.get(random.nextInt(teleportPositions.size()));
	}

	public static boolean isRestingPosNotBlocked(Purpoid purpoid, Vec3 pos, BlockPos restingPos, Direction side) {
		Level level = purpoid.level;
		VoxelShape restingPosShape = level.getBlockState(restingPos).getCollisionShape(level, restingPos).move(restingPos.getX(), restingPos.getY(), restingPos.getZ());
		AABB purpoidCollisionBox = purpoid.getDimensions(purpoid.getPose()).makeBoundingBox(pos.x, pos.y, pos.z);
		double sideCoordinate;
		double purpoidCollisionBoxSideCoordinate;
		Direction.Axis axis = side.getAxis();
		if (side.getAxisDirection() == Direction.AxisDirection.POSITIVE) {
			sideCoordinate = restingPosShape.max(axis);
			purpoidCollisionBoxSideCoordinate = purpoidCollisionBox.min(axis);
		} else {
			sideCoordinate = restingPosShape.min(axis);
			purpoidCollisionBoxSideCoordinate = purpoidCollisionBox.max(axis);
		}
		double expand = sideCoordinate - purpoidCollisionBoxSideCoordinate;
		AABB detectionBox = purpoidCollisionBox.expandTowards(expand * side.getStepX(), expand * side.getStepY(), expand * side.getStepZ());
		for (VoxelShape voxelShape : level.getBlockCollisions(purpoid, detectionBox)) {
			if (!voxelShape.isEmpty()) {
				return false;
			}
		}
		return level.isUnobstructed(purpoid, Shapes.create(purpoidCollisionBox.minmax(restingPosShape.bounds())));
	}

	@Override
	public boolean requiresUpdateEveryTick() {
		return true;
	}

}
