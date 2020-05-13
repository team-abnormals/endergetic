package endergeticexpansion.common.entities.booflo.ai;

import com.teamabnormals.abnormals_core.core.utils.NetworkUtil;

import endergeticexpansion.api.entity.util.DetectionHelper;
import endergeticexpansion.common.entities.booflo.EntityBooflo;
import net.minecraft.block.Block;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.Direction;
import net.minecraft.world.World;

public class BoofloSlamGoal extends Goal {
	private EntityBooflo booflo;
	private World world;
	
	public BoofloSlamGoal(EntityBooflo booflo) {
		this.booflo = booflo;
		this.world = booflo.world;
	}

	@Override
	public boolean shouldExecute() {
		return this.booflo.getPassengers().isEmpty() && this.booflo.isEndimationPlaying(EntityBooflo.SWIM) && !this.booflo.onGround && (this.booflo.hasAggressiveAttackTarget()) && this.isEntityUnder() && this.isSolidUnderTarget();
	}
	
	@Override
	public boolean shouldContinueExecuting() {
		if(!this.isSolidUnderTarget()) {
			NetworkUtil.setPlayingAnimationMessage(this.booflo, EntityBooflo.INFLATE);
			return false;
		}
		return !this.booflo.onGround && this.booflo.hasAggressiveAttackTarget() && this.booflo.isEndimationPlaying(EntityBooflo.CHARGE);
	}
	
	@Override
	public void startExecuting() {
		NetworkUtil.setPlayingAnimationMessage(this.booflo, EntityBooflo.CHARGE);
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
			isSomewhatSolidUnder = !isSomewhatSolidUnder ? this.booflo.getBoofloAttackTarget() != null && Block.hasSolidSide(this.world.getBlockState(this.booflo.getBoofloAttackTarget().getPosition().down(y)), this.world, this.booflo.getBoofloAttackTarget().getPosition().down(y), Direction.UP) : true;
		}
		return isSomewhatSolidUnder;
	}
}