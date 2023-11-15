package com.teamabnormals.endergetic.common.entity.eetle.ai.glider;

import com.mojang.datafixers.util.Pair;
import com.teamabnormals.blueprint.common.world.storage.tracking.IDataManager;
import com.teamabnormals.endergetic.common.entity.eetle.ChargerEetle;
import com.teamabnormals.endergetic.common.entity.eetle.GliderEetle;
import com.teamabnormals.endergetic.core.registry.other.EEDataProcessors;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.*;

public class GliderEetleDropOffGoal extends Goal {
	private final GliderEetle glider;
	@Nullable
	private Path path;
	@Nullable
	private Vec3 clusterPos;
	@Nullable
	private AABB searchBox;
	private int missingClusterTicks;

	public GliderEetleDropOffGoal(GliderEetle glider) {
		this.glider = glider;
		this.setFlags(EnumSet.of(Flag.MOVE));
	}

	@Override
	public boolean canUse() {
		GliderEetle glider = this.glider;
		if (glider.getRandom().nextFloat() < 0.05F) {
			LivingEntity target = glider.getTarget();
			if (target != null && target.isAlive() && glider.getPassengers().contains(target) && glider.isFlying() && glider.isNoEndimationPlaying()) {
				Pair<BlockPos, BlockPos> clusterPosPair = findLargestClusterAirAndGroundPositions(glider.level, glider.blockPosition());
				if (clusterPosPair != null) {
					BlockPos pos = clusterPosPair.getFirst();
					this.path = glider.getNavigation().createPath(pos, 0);
					this.searchBox = new AABB(clusterPosPair.getSecond()).inflate(4.0D, 1.0D, 4.0D);
					this.clusterPos = Vec3.atCenterOf(pos);
					return this.path != null;
				}
			}
		}
		return false;
	}

	@Override
	public void start() {
		this.glider.getNavigation().moveTo(this.path, 1.5F);
	}

	@Override
	public boolean canContinueToUse() {
		GliderEetle glider = this.glider;
		LivingEntity target = glider.getTarget();
		if (target != null && target.isAlive() && glider.getPassengers().contains(target) && glider.isFlying() && glider.getNavigation().isInProgress()) {
			if (glider.level.getEntitiesOfClass(ChargerEetle.class, this.searchBox).size() < 3) {
				this.missingClusterTicks++;
			}
			return this.missingClusterTicks < 10;
		}
		return false;
	}

	@Override
	public void stop() {
		this.glider.getNavigation().stop();
		this.path = null;
		this.clusterPos = null;
		this.searchBox = null;
		this.missingClusterTicks = 0;
	}

	@Override
	public void tick() {
		GliderEetle glider = this.glider;
		double distance = glider.position().distanceToSqr(this.clusterPos);
		LivingEntity attackTarget = glider.getTarget();
		if (distance <= 2.25D) {
			glider.makeGrounded();
			if (attackTarget instanceof IDataManager) {
				((IDataManager) attackTarget).setValue(EEDataProcessors.CATCHING_COOLDOWN, 40 + glider.getRandom().nextInt(11));
			}
		} else if (distance <= 20.25D) {
			AABB projectedBox = getProjectedBoundingBox(glider);
			if (projectedBox != null && glider.level.getEntitiesOfClass(ChargerEetle.class, projectedBox.inflate(1.0F, 0.0F, 1.0F)).size() >= 4) {
				glider.makeGrounded();
				if (attackTarget instanceof IDataManager) {
					((IDataManager) attackTarget).setValue(EEDataProcessors.CATCHING_COOLDOWN, 40 + glider.getRandom().nextInt(11));
				}
			}
		}
	}

	@Override
	public boolean requiresUpdateEveryTick() {
		return true;
	}

	@Nullable
	private static AABB getProjectedBoundingBox(GliderEetle glider) {
		BlockPos.MutableBlockPos mutable = glider.blockPosition().mutable();
		int startY = mutable.getY();
		Level world = glider.level;
		for (int y = 0; y <= 7; y++) {
			mutable.setY(startY - y);
			if (world.loadedAndEntityCanStandOn(mutable, glider)) {
				AABB bb = glider.getBoundingBox();
				return bb.move(0.0F, -(bb.minY - mutable.getY()), 0.0F);
			}
		}
		return null;
	}

	@Nullable
	private static Pair<BlockPos, BlockPos> findLargestClusterAirAndGroundPositions(Level world, BlockPos origin) {
		BlockPos clusterPos = null;
		int largestCluster = 2;
		Set<ChargerEetle> foundInCluster = new HashSet<>();
		List<ChargerEetle> chargerEetleEntityList = world.getEntitiesOfClass(ChargerEetle.class, new AABB(origin).inflate(24.0D, 6.0D, 24.0D), Entity::isOnGround);
		List<Pair<Double, Double>> points = new ArrayList<>();
		for (ChargerEetle charger : chargerEetleEntityList) {
			if (!foundInCluster.contains(charger)) {
				int others = 0;
				for (ChargerEetle otherCharger : world.getEntitiesOfClass(ChargerEetle.class, new AABB(charger.blockPosition()).inflate(4.0D, 6.0D, 4.0D))) {
					foundInCluster.add(otherCharger);
					Vec3 pos = otherCharger.position();
					points.add(Pair.of(pos.x, pos.z));
					others++;
				}
				if (others > largestCluster) {
					Pair<Double, Double> centroidXZ = computeCentroid(points);
					points.clear();
					clusterPos = new BlockPos(centroidXZ.getFirst(), charger.getY(), centroidXZ.getSecond());
				}
			}
		}
		if (clusterPos != null) {
			BlockPos.MutableBlockPos airPos = clusterPos.mutable();
			int startY = airPos.getY();
			for (int y = 0; y <= 7; y++) {
				airPos.setY(startY + y);
				if (!world.isEmptyBlock(airPos)) {
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
