package endergeticexpansion.common.entities.puffbug.ai;

import javax.annotation.Nullable;

import endergeticexpansion.common.entities.puffbug.EntityPuffBug;
import endergeticexpansion.common.tileentities.TileEntityBolloomBud;
import endergeticexpansion.core.registry.EEBlocks;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PuffBugDescentGoal extends Goal {
	private EntityPuffBug puffbug;
	private World world;
	@Nullable
	private BlockPos budPos;
	private float originalPosX, originalPosZ;
	private int ticksPassed;
	
	public PuffBugDescentGoal(EntityPuffBug puffbug) {
		this.puffbug = puffbug;
		this.world = puffbug.world;
	}
	
	@Override
	public boolean shouldExecute() {
		return this.isBolloomBudUnder() && this.puffbug.getBudPos() != null;
	}
	
	@Override
	public boolean shouldContinueExecuting() {
		TileEntity te = this.world.getTileEntity(this.budPos);
		if(!(te instanceof TileEntityBolloomBud && ((TileEntityBolloomBud) te).canBeOpened())) {
			return false;
		}
		return this.puffbug.posX == this.originalPosX && this.puffbug.posZ == this.originalPosZ && !this.puffbug.onGround;
	}
	
	@Override
	public void startExecuting() {
		TileEntity te = this.world.getTileEntity(this.puffbug.getBudPos());
		if(te instanceof TileEntityBolloomBud) {
			((TileEntityBolloomBud) te).setTeleportingBug(null);
		}
		
		this.puffbug.setBudPos(null);
		this.puffbug.setBoosting(false);
		this.puffbug.setAIMoveSpeed(0.0F);
		this.puffbug.getNavigator().clearPath();
		
		this.originalPosX = (float) this.puffbug.posX;
		this.originalPosZ = (float) this.puffbug.posZ;
	}

	@Override
	public void tick() {
		this.ticksPassed++;
		
		if(this.ticksPassed > 20) {
			this.puffbug.getRotationController().rotate(0.0F, 180.0F, 20);
		}
		
		this.puffbug.setBoosting(false);
		this.puffbug.setAIMoveSpeed(0.0F);
		this.puffbug.getNavigator().clearPath();
	}
	
	@Override
	public void resetTask() {
		this.budPos = null;
		this.originalPosX = 0.0F;
		this.originalPosZ = 0.0F;
		this.puffbug.setBoosting(false);
	}
	
	private boolean isBolloomBudUnder() {
		BlockPos pos = new BlockPos(this.puffbug.posX - 0.5F, this.puffbug.posY - 0.5F, this.puffbug.posZ - 0.5F);
		
		if(this.world.getBlockState(pos.down()).getCollisionShape(this.world, pos.down()).isEmpty() && this.world.getBlockState(pos.down(2)).getBlock() == EEBlocks.BOLLOOM_BUD.get()) {
			this.budPos = pos.down(2);
			return true;
		}
		
		return false;
	}
}