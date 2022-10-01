package com.teamabnormals.endergetic.common.entities.purpoid.ai;

import com.teamabnormals.endergetic.common.entities.purpoid.PurpoidEntity;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.EnumSet;

public abstract class AbstractPurpoidTeleportGoal extends Goal {
	protected final PurpoidEntity purpoid;
	private int notMovingTicks;

	protected AbstractPurpoidTeleportGoal(PurpoidEntity purpoid) {
		this.purpoid = purpoid;
		this.setFlags(EnumSet.of(Goal.Flag.MOVE));
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean canUse() {
		PurpoidEntity purpoid = this.purpoid;
		this.notMovingTicks = !purpoid.getMoveControl().hasWanted() ? this.notMovingTicks + 1 : 0;
		boolean canUse = false;
		if ((purpoid.forcedRelativeTeleportingPos != null || (this.notMovingTicks >= 20 && !purpoid.isBoosting() && !purpoid.hasTeleportCooldown())) && !purpoid.isPassenger() && purpoid.isNoEndimationPlaying()) {
			BlockPos teleportPos = this.generateTeleportPos(purpoid, purpoid.getRandom());
			Level level = purpoid.level;
			if (teleportPos != null && level.hasChunkAt(teleportPos)) {
				AABB collisionBox = purpoid.getDimensions(purpoid.getPose()).makeBoundingBox(teleportPos.getX() + 0.5F, teleportPos.getY(), teleportPos.getZ() + 0.5F);
				if (level.noCollision(collisionBox) && level.isUnobstructed(purpoid, Shapes.create(collisionBox)) && !level.containsAnyLiquid(collisionBox)) {
					this.beginTeleportation(purpoid, teleportPos);
					canUse = true;
				}
			}
		}
		purpoid.forcedRelativeTeleportingPos = null;
		return canUse;
	}

	@Override
	public boolean canContinueToUse() {
		return this.purpoid.getTeleportController().isTeleporting();
	}

	@Override
	public boolean requiresUpdateEveryTick() {
		return true;
	}

	protected void beginTeleportation(PurpoidEntity purpoid, BlockPos pos) {
		purpoid.resetTeleportCooldown();
		purpoid.getTeleportController().beginTeleportation(purpoid, pos, false);
	}

	@Nullable
	protected abstract BlockPos generateTeleportPos(PurpoidEntity purpoid, RandomSource random);
}
