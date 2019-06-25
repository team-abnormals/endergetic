package endergeticexpansion.common.tileentities.boof;

import java.util.List;

import endergeticexpansion.common.blocks.poise.boof.BlockDispensedBoof;
import endergeticexpansion.core.registry.EETileEntities;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;

public class TileEntityDispensedBoof extends TileEntity implements ITickableTileEntity {
	int ticksExisted;
	public TileEntityDispensedBoof() {
		super(EETileEntities.BOOF_DISPENSED);
	}

	@Override
	public void tick() {
		ticksExisted++;
		AxisAlignedBB bb = new AxisAlignedBB(this.getPos()).grow(0.1F, 0.1F, 0.1F);
		List<Entity> entities = world.getEntitiesWithinAABB(Entity.class, bb);
		for(int i = 0; i < entities.size(); i++) {
			Entity entity = entities.get(i);
			
			if(this.getBlockState().get(BlockDispensedBoof.FACING) == Direction.UP) {
				entity.addVelocity(MathHelper.sin((float) (entity.rotationYaw * Math.PI / 180.0F)) * 0.1F * 0.1F, 0.25D, -MathHelper.cos((float) (entity.rotationYaw * Math.PI / 180.0F)) * 0.1F * 0.1F);
			} else if(this.getBlockState().get(BlockDispensedBoof.FACING) == Direction.DOWN) {
				entity.addVelocity(MathHelper.sin((float) (entity.rotationYaw * Math.PI / 180.0F)) * 0.1F * 0.1F, -0.45D, -MathHelper.cos((float) (entity.rotationYaw * Math.PI / 180.0F)) * 0.1F * 0.1F);
			} else {
				entity.addVelocity(MathHelper.sin((float) (entity.rotationYaw * Math.PI / 180.0F)) * 3F * 0.1F, 0.45D, -MathHelper.cos((float) (entity.rotationYaw * Math.PI / 180.0F)) * 3F * 0.1F);
			}
		}
		if(ticksExisted >= 10) {
			this.getWorld().setBlockState(getPos(), Blocks.AIR.getDefaultState());
		}
	}

}
