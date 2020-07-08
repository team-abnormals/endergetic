package endergeticexpansion.common.entities.booflo.ai;

import com.teamabnormals.abnormals_core.core.utils.NetworkUtil;

import endergeticexpansion.api.entity.util.DetectionHelper;
import endergeticexpansion.common.entities.booflo.BoofloEntity;
import net.minecraft.block.Block;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.Direction;
import net.minecraft.world.World;

public class BoofloSlamGoal extends Goal {
	private BoofloEntity booflo;
	private World world;
	
	public BoofloSlamGoal(BoofloEntity booflo) {
		this.booflo = booflo;
		this.world = booflo.world;
	}

	@Override
	public boolean shouldExecute() {
		return this.booflo.getPassengers().isEmpty() && this.booflo.isEndimationPlaying(BoofloEntity.SWIM) && !this.booflo.func_233570_aj_() && (this.booflo.hasAggressiveAttackTarget()) && this.isEntityUnder() && this.isSolidUnderTarget();
	}
	
	@Override
	public boolean shouldContinueExecuting() {
		if(!this.isSolidUnderTarget()) {
			NetworkUtil.setPlayingAnimationMessage(this.booflo, BoofloEntity.INFLATE);
			return false;
		}
		return !this.booflo.func_233570_aj_() && this.booflo.hasAggressiveAttackTarget() && this.booflo.isEndimationPlaying(BoofloEntity.CHARGE);
	}
	
	@Override
	public void startExecuting() {
		NetworkUtil.setPlayingAnimationMessage(this.booflo, BoofloEntity.CHARGE);
	}
	
	@Override
	public void tick() {
		this.booflo.getNavigator().clearPath();
		this.booflo.setAIMoveSpeed(0.0F);
		
		this.booflo.rotationPitch = 0.0F;
	}
	
	private boolean isEntityUnder() {
		for(LivingEntity entity : this.world.getEntitiesWithinAABB(LivingEntity.class, DetectionHelper.expandDownwards(this.booflo.getBoundingBox().grow(1.0F), 12.0F))) {
			if(entity == this.booflo.getBoofloAttackTarget()) {
				return true;
			}
		}
		return false;
	}
	
	private boolean isSolidUnderTarget() {
		boolean isSomewhatSolidUnder = false;
		for(int y = 1; y < 4; y++) {
			isSomewhatSolidUnder = !isSomewhatSolidUnder ? this.booflo.getBoofloAttackTarget() != null && Block.hasSolidSide(this.world.getBlockState(this.booflo.getBoofloAttackTarget().func_233580_cy_().down(y)), this.world, this.booflo.getBoofloAttackTarget().func_233580_cy_().down(y), Direction.UP) : true;
		}
		return isSomewhatSolidUnder;
	}
}