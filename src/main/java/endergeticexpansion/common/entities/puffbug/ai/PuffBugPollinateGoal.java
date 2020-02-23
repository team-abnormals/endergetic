package endergeticexpansion.common.entities.puffbug.ai;

import endergeticexpansion.api.util.NetworkUtil;
import endergeticexpansion.common.entities.puffbug.EntityPuffBug;
import endergeticexpansion.common.tileentities.TileEntityBolloomBud;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class PuffBugPollinateGoal extends Goal {
	private EntityPuffBug puffbug;
	private World world;
	private float originalPosX, originalPosY, originalPosZ;
	
	public PuffBugPollinateGoal(EntityPuffBug puffbug) {
		this.puffbug = puffbug;
		this.world = this.puffbug.world;
	}

	@Override
	public boolean shouldExecute() {
		if(this.puffbug.getPollinationPos() != null) {
			TileEntity te = this.world.getTileEntity(this.puffbug.getPollinationPos());
			if(te instanceof TileEntityBolloomBud && ((TileEntityBolloomBud) te).canBeOpened()) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean shouldContinueExecuting() {
		if(this.puffbug.getPollinationPos() != null) {
			TileEntity te = this.world.getTileEntity(this.puffbug.getPollinationPos());
			if(!(te instanceof TileEntityBolloomBud && ((TileEntityBolloomBud) te).canBeOpened())) {
				return false;
			}
		} else {
			return false;
		}
		return
			!this.puffbug.hasLevitation() &&
			this.puffbug.isEndimationPlaying(EntityPuffBug.POLLINATE_ANIMATION) &&
			this.puffbug.posX == this.originalPosX &&
			this.puffbug.posZ == this.originalPosZ &&
			Math.abs(this.originalPosY - this.puffbug.posY) < 0.5F
		;
	}
	
	@Override
	public void tick() {
		this.puffbug.getRotationController().rotate(0.0F, 180.0F, 0.0F, 20);
		this.puffbug.puffCooldown = 10;
		
		this.puffbug.setBoosting(false);
		this.puffbug.setAIMoveSpeed(0.0F);
		this.puffbug.getNavigator().clearPath();
	}
	
	@Override
	public void startExecuting() {
		this.puffbug.setBoosting(false);
		this.puffbug.setAIMoveSpeed(0.0F);
		this.puffbug.getNavigator().clearPath();
		
		this.originalPosX = (float) this.puffbug.posX;
		this.originalPosY = (float) this.puffbug.posY;
		this.originalPosZ = (float) this.puffbug.posZ;
		
		NetworkUtil.setPlayingAnimationMessage(this.puffbug, EntityPuffBug.POLLINATE_ANIMATION);
	}
	
	@Override
	public void resetTask() {
		this.puffbug.setPollinationPos(null);
		this.originalPosX = this.originalPosY = this.originalPosZ = 0.0F;
		
		NetworkUtil.setPlayingAnimationMessage(this.puffbug, EntityPuffBug.BLANK_ANIMATION);
	}
}