package com.minecraftabnormals.endergetic.common.entities.purpoid.ai;

import com.minecraftabnormals.endergetic.common.entities.purpoid.PurpoidEntity;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.vector.Vector3d;

import java.util.EnumSet;
import java.util.Random;

import net.minecraft.entity.ai.goal.Goal.Flag;

public class PurpoidMoveRandomGoal extends Goal {
	private final PurpoidEntity purpoid;
	private int cooldown;
	private double x, y, z;

	public PurpoidMoveRandomGoal(PurpoidEntity purpoid) {
		this.purpoid = purpoid;
		this.setFlags(EnumSet.of(Flag.MOVE));
	}

	@Override
	public boolean canUse() {
		if (this.cooldown > 0) {
			this.cooldown--;
		} else {
			PurpoidEntity purpoid = this.purpoid;
			Random random = purpoid.getRandom();
			Vector3d randomPos = purpoid.getY() >= purpoid.level.getSeaLevel() + 30 ? purpoid.position().add(new Vector3d(random.nextInt(17) - random.nextInt(17), -random.nextInt(16), random.nextInt(17) - random.nextInt(17))) : RandomPositionGenerator.getPos(purpoid, 32, 16);
			if (randomPos != null) {
				this.x = randomPos.x();
				this.y = randomPos.y();
				this.z = randomPos.z();
				return true;
			}
		}
		return false;
	}

	@Override
	public void start() {
		PurpoidEntity purpoid = this.purpoid;
		purpoid.getMoveControl().setWantedPosition(this.x, this.y, this.z, 1.0F);
		this.cooldown = purpoid.getRandom().nextInt(81) + (int) (40 * purpoid.getSize().getScale());
	}

	@Override
	public boolean canContinueToUse() {
		return this.purpoid.getMoveControl().hasWanted();
	}

	@Override
	public void stop() {
		this.purpoid.getNavigation().stop();
	}
}
