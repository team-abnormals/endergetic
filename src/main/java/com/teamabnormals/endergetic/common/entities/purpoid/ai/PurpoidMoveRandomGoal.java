package com.teamabnormals.endergetic.common.entities.purpoid.ai;

import com.teamabnormals.endergetic.common.entities.purpoid.PurpoidEntity;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ai.util.HoverRandomPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

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
			RandomSource random = purpoid.getRandom();
			Vec3 randomPos;
			if (purpoid.getY() >= purpoid.level.getSeaLevel() + 30) {
				randomPos = purpoid.position().add(new Vec3(random.nextInt(17) - random.nextInt(17), -random.nextInt(16), random.nextInt(17) - random.nextInt(17)));
			} else {
				Vec3 view = purpoid.getViewVector(0.0F);
				randomPos = HoverRandomPos.getPos(purpoid, 32, 16, view.x, view.z, ((float)Math.PI / 2F), 3, 1);
			}
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
