package endergeticexpansion.common.entities.booflo.ai;

import java.util.List;

import endergeticexpansion.common.entities.booflo.EntityBooflo;
import endergeticexpansion.common.entities.booflo.EntityBoofloBaby;
import net.minecraft.entity.ai.goal.Goal;

public class BabyFollowParentGoal extends Goal {
	private final EntityBoofloBaby baby;
	private EntityBooflo parent;
	private final double moveSpeed;
	private int delayCounter;
	
	public BabyFollowParentGoal(EntityBoofloBaby booflo, double speed) {
		this.baby = booflo;
		this.moveSpeed = speed;
	}

	public boolean shouldExecute() {
		List<EntityBooflo> list = this.baby.world.getEntitiesWithinAABB(EntityBooflo.class, this.baby.getBoundingBox().grow(8.0D, 6.0D, 8.0D));
		EntityBooflo booflo = null;
		double d0 = Double.MAX_VALUE;

		for(EntityBooflo booflos : list) {
			double d1 = this.baby.getDistanceSq(booflos);
			if (!(d1 > d0)) {
				d0 = d1;
				booflo = booflos;
			}
		}

		if (booflo == null) {
			return false;
		} else if (d0 < 9.0D) {
			return false;
		} else {
			if(this.baby.getRNG().nextFloat() < 0.25F) {
				this.parent = booflo;
				return true;
			}
			return false;
		}
	}
	
	public boolean shouldContinueExecuting() {
		if (!this.parent.isAlive()) {
			return false;
		} else {
			double d0 = this.baby.getDistanceSq(this.parent);
			return !(d0 < 9.0D) && !(d0 > 256.0D);
		}
	}
	
	public void startExecuting() {
		this.delayCounter = 0;
	}

	public void resetTask() {
		this.parent = null;
	}

	public void tick() {
		if (--this.delayCounter <= 0) {
			this.delayCounter = 10;
			this.baby.getNavigator().tryMoveToEntityLiving(this.parent, this.moveSpeed);
		}
	}
}