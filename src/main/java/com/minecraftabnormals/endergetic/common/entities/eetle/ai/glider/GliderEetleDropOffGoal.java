package com.minecraftabnormals.endergetic.common.entities.eetle.ai.glider;

import com.minecraftabnormals.abnormals_core.common.world.storage.tracking.IDataManager;
import com.minecraftabnormals.endergetic.common.entities.eetle.ChargerEetleEntity;
import com.minecraftabnormals.endergetic.common.entities.eetle.GliderEetleEntity;
import com.minecraftabnormals.endergetic.core.registry.other.EEDataProcessors;
import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.*;

public class GliderEetleDropOffGoal extends Goal {
	private final GliderEetleEntity glider;
	@Nullable
	private Path path;
	@Nullable
	private Vector3d clusterPos;
	@Nullable
	private AxisAlignedBB searchBox;
	private int missingClusterTicks;

	public GliderEetleDropOffGoal(GliderEetleEntity glider) {
		this.glider = glider;
		this.setMutexFlags(EnumSet.of(Flag.MOVE));
	}

	@Override
	public boolean shouldExecute() {
		GliderEetleEntity glider = this.glider;
		if (glider.getRNG().nextFloat() < 0.05F) {
			LivingEntity target = glider.getAttackTarget();
			if (target != null && target.isAlive() && glider.getPassengers().contains(target) && glider.isFlying() && glider.isNoEndimationPlaying()) {
				Pair<BlockPos, BlockPos> clusterPosPair = findLargestClusterAirAndGroundPositions(glider.world, glider.getPosition());
				if (clusterPosPair != null) {
					BlockPos pos = clusterPosPair.getFirst();
					this.path = glider.getNavigator().getPathToPos(pos, 0);
					this.searchBox = new AxisAlignedBB(clusterPosPair.getSecond()).grow(4.0D, 1.0D, 4.0D);
					this.clusterPos = Vector3d.copyCentered(pos);
					return this.path != null;
				}
			}
		}
		return false;
	}

	@Override
	public void startExecuting() {
		this.glider.getNavigator().setPath(this.path, 1.5F);
	}

	@Override
	public boolean shouldContinueExecuting() {
		GliderEetleEntity glider = this.glider;
		LivingEntity target = glider.getAttackTarget();
		if (target != null && target.isAlive() && glider.getPassengers().contains(target) && glider.isFlying() && glider.getNavigator().hasPath()) {
			if (glider.world.getEntitiesWithinAABB(ChargerEetleEntity.class, this.searchBox).size() < 3) {
				this.missingClusterTicks++;
			}
			return this.missingClusterTicks < 10;
		}
		return false;
	}

	@Override
	public void resetTask() {
		this.glider.getNavigator().clearPath();
		this.path = null;
		this.clusterPos = null;
		this.searchBox = null;
		this.missingClusterTicks = 0;
	}

	@Override
	public void tick() {
		GliderEetleEntity glider = this.glider;
		double distance = glider.getPositionVec().squareDistanceTo(this.clusterPos);
		LivingEntity attackTarget = glider.getAttackTarget();
		if (distance <= 2.25D) {
			glider.makeGrounded();
			if (attackTarget instanceof IDataManager) {
				((IDataManager) attackTarget).setValue(EEDataProcessors.CATCHING_COOLDOWN, 40 + glider.getRNG().nextInt(11));
			}
		} else if (distance <= 20.25D) {
			AxisAlignedBB projectedBox = getProjectedBoundingBox(glider);
			if (projectedBox != null && glider.world.getEntitiesWithinAABB(ChargerEetleEntity.class, projectedBox.grow(1.0F, 0.0F, 1.0F)).size() >= 4) {
				glider.makeGrounded();
				if (attackTarget instanceof IDataManager) {
					((IDataManager) attackTarget).setValue(EEDataProcessors.CATCHING_COOLDOWN, 40 + glider.getRNG().nextInt(11));
				}
			}
		}
	}

	@Nullable
	private static AxisAlignedBB getProjectedBoundingBox(GliderEetleEntity glider) {
		BlockPos.Mutable mutable = glider.getPosition().toMutable();
		int startY = mutable.getY();
		World world = glider.world;
		for (int y = 0; y <= 7; y++) {
			mutable.setY(startY - y);
			if (world.isTopSolid(mutable, glider)) {
				AxisAlignedBB bb = glider.getBoundingBox();
				return bb.offset(0.0F, -(bb.minY - mutable.getY()), 0.0F);
			}
		}
		return null;
	}

	@Nullable
	private static Pair<BlockPos, BlockPos> findLargestClusterAirAndGroundPositions(World world, BlockPos origin) {
		BlockPos clusterPos = null;
		int largestCluster = 2;
		Set<ChargerEetleEntity> foundInCluster = new HashSet<>();
		List<ChargerEetleEntity> chargerEetleEntityList = world.getEntitiesWithinAABB(ChargerEetleEntity.class, new AxisAlignedBB(origin).grow(24.0D, 6.0D, 24.0D), Entity::isOnGround);
		List<Pair<Double, Double>> points = new ArrayList<>();
		for (ChargerEetleEntity charger : chargerEetleEntityList) {
			if (!foundInCluster.contains(charger)) {
				int others = 0;
				for (ChargerEetleEntity otherCharger : world.getEntitiesWithinAABB(ChargerEetleEntity.class, new AxisAlignedBB(charger.getPosition()).grow(4.0D, 6.0D, 4.0D))) {
					foundInCluster.add(otherCharger);
					Vector3d pos = otherCharger.getPositionVec();
					points.add(Pair.of(pos.x, pos.z));
					others++;
				}
				if (others > largestCluster) {
					Pair<Double, Double> centroidXZ = computeCentroid(points);
					points.clear();
					clusterPos = new BlockPos(centroidXZ.getFirst(), charger.getPosY(), centroidXZ.getSecond());
				}
			}
		}
		if (clusterPos != null) {
			BlockPos.Mutable airPos = clusterPos.toMutable();
			int startY = airPos.getY();
			for (int y = 0; y <= 7; y++) {
				airPos.setY(startY + y);
				if (!world.isAirBlock(airPos)) {
					airPos.setY(airPos.getY() - 1);
					break;
				}
			}
			return Pair.of(airPos, clusterPos);
		}
		return null;
	}

	private static Pair<Double, Double> computeCentroid(List<Pair<Double, Double>> xzPoints) {
		double x = 0.0F;
		double z = 0.0F;
		for (Pair<Double, Double> pointXZ : xzPoints) {
			x += pointXZ.getFirst();
			z += pointXZ.getSecond();
		}
		double size = xzPoints.size();
		return Pair.of(x / size, z / size);
	}
}
