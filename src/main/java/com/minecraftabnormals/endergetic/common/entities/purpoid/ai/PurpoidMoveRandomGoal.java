package com.minecraftabnormals.endergetic.common.entities.purpoid.ai;

import com.minecraftabnormals.endergetic.common.entities.purpoid.PurpoidEntity;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.vector.Vector3d;

import java.util.EnumSet;
import java.util.Random;

public class PurpoidMoveRandomGoal extends Goal {
	private final PurpoidEntity purpoid;
	private int cooldown;
	private double x, y, z;

	public PurpoidMoveRandomGoal(PurpoidEntity purpoid) {
		this.purpoid = purpoid;
		this.setMutexFlags(EnumSet.of(Flag.MOVE));
	}

	@Override
	public boolean shouldExecute() {
		if (this.cooldown > 0) {
			this.cooldown--;
		} else {
			PurpoidEntity purpoid = this.purpoid;
			Random random = purpoid.getRNG();
			Vector3d randomPos = purpoid.getPosY() >= purpoid.world.getSeaLevel() + 30 ? purpoid.getPositionVec().add(new Vector3d(random.nextInt(17) - random.nextInt(17), -random.nextInt(16), random.nextInt(17) - random.nextInt(17))) : RandomPositionGenerator.findRandomTarget(purpoid, 32, 16);
			if (randomPos != null) {
				this.x = randomPos.getX();
				this.y = randomPos.getY();
				this.z = randomPos.getZ();
				return true;
			}
		}
		return false;
	}

	@Override
	public void startExecuting() {
		PurpoidEntity purpoid = this.purpoid;
		purpoid.getMoveHelper().setMoveTo(this.x, this.y, this.z, 1.0F);
		this.cooldown = purpoid.getRNG().nextInt(81) + (int) (40 * purpoid.getSize().getScale());
	}

	@Override
	public boolean shouldContinueExecuting() {
		return this.purpoid.getMoveHelper().isUpdating();
	}

	@Override
	public void resetTask() {
		this.purpoid.getNavigator().clearPath();
	}
}
