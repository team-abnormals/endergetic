package endergeticexpansion.common.entities.booflo.ai;

import endergeticexpansion.api.entity.util.AdvancedAxisAllignedBB;
import endergeticexpansion.api.util.NetworkUtil;
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
		return this.booflo.isAnimationPlaying(EntityBooflo.SWIM) && !this.booflo.onGround && this.booflo.hasAggressiveAttackTarget() && this.isEntityUnder() && this.isSolidUnderTarget();
	}
	
	@Override
	public boolean shouldContinueExecuting() {
		if(!this.isSolidUnderTarget()) {
			NetworkUtil.setPlayingAnimationMessage(this.booflo, EntityBooflo.INFLATE);
			return false;
		}
		return !this.booflo.onGround && this.booflo.hasAggressiveAttackTarget() && this.booflo.isAnimationPlaying(EntityBooflo.CHARGE);
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
		
		if(this.booflo.isAnimationPlaying(EntityBooflo.CHARGE) && this.booflo.getAnimationTick() >= 15) {
			this.booflo.addVelocity(0.0F, -0.2F, 0.0F);
		}
	}
	
	private boolean isEntityUnder() {
		for(LivingEntity entity : this.world.getEntitiesWithinAABB(LivingEntity.class, AdvancedAxisAllignedBB.expandDownwards(this.booflo.getBoundingBox().grow(1.0F), 12.0F))) {
			if(entity == this.booflo.getBoofloAttackTarget()) {
				return true;
			}
		}
		return false;
	}
	
	private boolean isSolidUnderTarget() {
		return this.booflo.getBoofloAttackTarget() != null && Block.hasSolidSide(this.world.getBlockState(this.booflo.getBoofloAttackTarget().getPosition().down()), this.world, this.booflo.getBoofloAttackTarget().getPosition().down(), Direction.UP);
	}
}