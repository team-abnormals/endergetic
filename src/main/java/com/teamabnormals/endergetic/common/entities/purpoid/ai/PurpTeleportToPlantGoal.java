package com.teamabnormals.endergetic.common.entities.purpoid.ai;

import com.mojang.datafixers.util.Pair;
import com.teamabnormals.endergetic.common.entities.purpoid.PurpoidEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class PurpTeleportToPlantGoal extends AbstractPurpoidTeleportGoal {
	private Direction side = Direction.DOWN;

	public PurpTeleportToPlantGoal(PurpoidEntity purpoid) {
		super(purpoid);
	}

	@Override
	public boolean canUse() {
		return !this.purpoid.hasRestCooldown() && super.canUse();
	}

	@Override
	protected void beginTeleportation(PurpoidEntity purpoid, BlockPos pos) {
		super.beginTeleportation(purpoid, pos);
		Direction side = this.side;
		purpoid.setRestingPos(pos.relative(side, -3));
		purpoid.setRestingSide(side);
	}

	@Nullable
	@Override
	protected BlockPos generateTeleportPos(PurpoidEntity purpoid, RandomSource random) {
		BlockPos pos = purpoid.blockPosition();
		int sizeXZ = 12;
		if (purpoid.forcedRelativeTeleportingPos != null) {
			pos = purpoid.forcedRelativeTeleportingPos;
			sizeXZ = 3;
		}
		int originX = pos.getX();
		int originY = pos.getY();
		int originZ = pos.getZ();
		BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
		Level level = purpoid.level;
		List<Pair<BlockPos, Direction>> sidedTeleportPositions = new ArrayList<>();
		List<Direction> shuffledHorizontals = Direction.Plane.HORIZONTAL.shuffledCopy(random);
		for (int x = originX - sizeXZ; x <= originX + sizeXZ; x++) {
			for (int y = originY - 12; y <= originY + 12; y++) {
				for (int z = originZ - sizeXZ; z <= originZ + sizeXZ; z++) {
					mutable.set(x, y, z);
					BlockPos chorusPlantPos = mutable.immutable();
					if (level.getBlockState(mutable).getBlock() == Blocks.CHORUS_PLANT) {
						for (Direction direction : shuffledHorizontals) {
							boolean airInPath = true;
							for (int forwardIndex = 0; forwardIndex < 3; forwardIndex++) {
								mutable.move(direction);
								if (!level.getBlockState(mutable).isAir()) {
									airInPath = false;
									break;
								}
							}
							if (airInPath && PurpoidTeleportToFlowerGoal.isRestingPosNotBlocked(purpoid, Vec3.atBottomCenterOf(mutable), chorusPlantPos, direction)) {
								sidedTeleportPositions.add(Pair.of(mutable.immutable(), direction));
								mutable.set(x, y, z);
								break;
							}
							mutable.set(x, y, z);
						}
					}
				}
			}
		}
		if (sidedTeleportPositions.isEmpty()) return null;
		var sidedTeleportPosition = sidedTeleportPositions.get(random.nextInt(sidedTeleportPositions.size()));
		this.side = sidedTeleportPosition.getSecond();
		return sidedTeleportPosition.getFirst();
	}

	@Override
	public boolean requiresUpdateEveryTick() {
		return true;
	}
}
