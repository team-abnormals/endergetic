package endergeticexpansion.common.entities.booflo.ai;

import java.util.EnumSet;

import endergeticexpansion.common.entities.booflo.EntityBooflo;
import endergeticexpansion.common.entities.booflo.EntityBooflo.GroundMoveHelperController;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.potion.Effects;

public class BoofloFaceRandomGoal extends Goal {
	private final EntityBooflo booflo;
	private float chosenDegrees;
	private int nextRandomizeTime;

	public BoofloFaceRandomGoal(EntityBooflo booflo) {
		this.booflo = booflo;
		this.setMutexFlags(EnumSet.of(Flag.LOOK));
	}

	@Override
	public boolean shouldExecute() {
    	return this.booflo.getAttackTarget() == null && (this.booflo.onGround || this.booflo.isPotionActive(Effects.LEVITATION)) && this.booflo.getMoveHelper() instanceof EntityBooflo.GroundMoveHelperController;
	}

	@Override
	public void tick() {
		if(this.nextRandomizeTime-- <= 0) {
			this.nextRandomizeTime = 30 + this.booflo.getRNG().nextInt(60);
			this.chosenDegrees = this.booflo.getRNG().nextInt(360);
		}

		((GroundMoveHelperController) this.booflo.getMoveHelper()).setDirection(this.chosenDegrees, false);
	}
}
