package endergeticexpansion.common.entities.booflo.ai;

import java.util.EnumSet;

import endergeticexpansion.api.util.NetworkUtil;
import endergeticexpansion.common.entities.booflo.EntityBooflo;
import endergeticexpansion.common.entities.booflo.EntityBooflo.GroundMoveHelperController;
import endergeticexpansion.core.registry.EEItems;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

public class BoofloTemptGoal extends Goal {
	private static final EntityPredicate SHOULD_FOLLOW = (new EntityPredicate()).setDistance(10.0D).allowFriendlyFire().allowInvulnerable();
	private EntityBooflo booflo;
	private PlayerEntity tempter;
	
	public BoofloTemptGoal(EntityBooflo booflo) {
		this.booflo = booflo;
		this.setMutexFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
	}

	@Override
	public boolean shouldExecute() {
		this.tempter = this.booflo.world.getClosestPlayer(SHOULD_FOLLOW, this.booflo);
		if(this.tempter == null) {
			return false;
		} else {
			return !this.booflo.isTamed() && !this.booflo.hasCaughtFruit() && !this.booflo.isInLove() && this.booflo.getMoveHelper() instanceof GroundMoveHelperController && !this.booflo.isBoofed() && this.booflo.onGround && this.isTemptedBy(this.tempter.getHeldItemMainhand()) || this.isTemptedBy(this.tempter.getHeldItemOffhand());
		}
	}
	
	@Override
	public boolean shouldContinueExecuting() {
		this.tempter = this.booflo.world.getClosestPlayer(SHOULD_FOLLOW, this.booflo);
		if(this.tempter == null) {
			return false;
		} else {
			return this.booflo.getMoveHelper() instanceof GroundMoveHelperController && !this.booflo.isTamed() && !this.booflo.isInLove() && !this.booflo.isBoofed();
		}
	}
	
	@Override
	public void tick() {
		if(this.booflo.hopDelay == 0 && this.booflo.isAnimationPlaying(EntityBooflo.BLANK_ANIMATION)) {
			NetworkUtil.setPlayingAnimationMessage(this.booflo, EntityBooflo.HOP);
		}
		
		((GroundMoveHelperController) this.booflo.getMoveHelper()).setSpeed(1.0D);
		
		double dx = this.tempter.posX - this.booflo.posX;
		double dz = this.tempter.posZ - this.booflo.posZ;
		
		float angle = (float) (MathHelper.atan2(dz, dx) * (double) (180F / Math.PI)) - 90.0F;
		
		((GroundMoveHelperController) this.booflo.getMoveHelper()).setDirection(angle, false);
		
		this.booflo.getNavigator().tryMoveToEntityLiving(this.booflo, 1.0D);
	}
	
	@Override
	public void resetTask() {
		this.tempter = null;
	}
	
	private boolean isTemptedBy(ItemStack stack) {
		return stack.getItem() == EEItems.BOLLOOM_FRUIT.get();
	}
}