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
		if (!purpoid.getMoveControl().hasWanted()) {
			this.notMovingTicks++;
		} else {
			this.notMovingTicks = 0;
		}
		if (this.notMovingTicks >= 20 && !purpoid.isPassenger() && !purpoid.isBoosting() && !purpoid.hasTeleportCooldown() && purpoid.isNoEndimationPlaying()) {
			BlockPos randomPos = this.generateTeleportPos(purpoid, purpoid.getRandom());
			Level world = purpoid.level;
			if (randomPos != null && world.hasChunkAt(randomPos)) {
				AABB collisionBox = purpoid.getDimensions(purpoid.getPose()).makeBoundingBox(randomPos.getX() + 0.5F, randomPos.getY(), randomPos.getZ() + 0.5F);
				if (world.noCollision(collisionBox) && world.isUnobstructed(purpoid, Shapes.create(collisionBox)) && !world.containsAnyLiquid(collisionBox)) {
					this.beginTeleportation(purpoid, randomPos);
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean canContinueToUse() {
		return this.purpoid.getTeleportController().isTeleporting();
	}

	protected void beginTeleportation(PurpoidEntity purpoid, BlockPos pos) {
		purpoid.resetTeleportCooldown();
		purpoid.getTeleportController().beginTeleportation(purpoid, pos, false);
	}

	@Nullable
	abstract BlockPos generateTeleportPos(PurpoidEntity purpoid, RandomSource random);

	@Override
	public boolean requiresUpdateEveryTick() {
		return true;
	}
}
