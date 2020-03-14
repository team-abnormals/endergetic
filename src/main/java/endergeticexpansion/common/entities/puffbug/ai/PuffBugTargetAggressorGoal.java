package endergeticexpansion.common.entities.puffbug.ai;

import endergeticexpansion.common.entities.puffbug.EntityPuffBug;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;

public class PuffBugTargetAggressorGoal extends HurtByTargetGoal {
	
	public PuffBugTargetAggressorGoal(EntityPuffBug puffbug) {
		super(puffbug);
		this.setCallsForHelp(new Class[] {EntityPuffBug.class});
	}
	
	@Override
	public void startExecuting() {
		super.startExecuting();
		
		for(EntityPuffBug bugs : this.goalOwner.world.getEntitiesWithinAABB(EntityPuffBug.class, this.goalOwner.getBoundingBox().grow(16.0D), (puffbug) -> puffbug.getAttackTarget() == null)) {
			bugs.setAttackTarget(this.goalOwner.getAttackTarget());
		}
	}

}
