package endergeticexpansion.common.tileentities;

import java.lang.reflect.Field;
import java.util.List;

import endergeticexpansion.common.blocks.poise.BlockBoof;
import endergeticexpansion.common.entities.EntityBolloomFruit;
import endergeticexpansion.common.entities.EntityBoofBlock;
import endergeticexpansion.core.registry.EETileEntities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;

public class TileEntityBoof extends TileEntity implements ITickableTileEntity {
	
	public TileEntityBoof() {
		super(EETileEntities.BOOF_BLOCK);
	}

	@Override
	public void tick() {
		AxisAlignedBB bb = new AxisAlignedBB(this.getPos()).grow(0.05F, 0.05F, 0.05F);
		List<Entity> entities = world.getEntitiesWithinAABB(Entity.class, bb);
		
		for(int i = 0; i < entities.size(); i++) {
			Entity entity = entities.get(i);
			
			if(!(entity instanceof EntityBoofBlock) && 
				!(entity instanceof FishingBobberEntity) &&
				!(entity instanceof AbstractArrowEntity) &&
				!(entity instanceof EntityBolloomFruit) &&
				!world.getBlockState(pos).get(BlockBoof.BOOFED)) {
				
				if(entity instanceof PlayerEntity) {
					if(!((PlayerEntity)entity).isSneaking()){
						BlockBoof.doBoof(world, pos);
					}
				} else {
					BlockBoof.doBoof(world, pos);
				}
			} else if((entity instanceof AbstractArrowEntity)) {
				/*
				 * NOTE: MUST CHANGE inGround to field_70254_i when decompiling into a jar
				 */
				Field inGround = findField(AbstractArrowEntity.class, "inGround");
				try {
					inGround.set(entity, Boolean.FALSE);
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
				BlockBoof.doBoof(getWorld(), getPos());
			}
		}
	}
	
	private static Field findField(Class<?> clazz, String name) {
        try {
            Field f = clazz.getDeclaredField(name);
            f.setAccessible(true);
            return f;
        } catch (Exception e) {
            throw new UnableToFindFieldException(e);
        }
    }
	
	@SuppressWarnings("serial")
	public static class UnableToFindFieldException extends RuntimeException
    {
        private UnableToFindFieldException(Exception e)
        {
            super(e);
        }
    }
	
}
