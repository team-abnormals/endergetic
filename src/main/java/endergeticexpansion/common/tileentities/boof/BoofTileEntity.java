package endergeticexpansion.common.tileentities.boof;

import java.lang.reflect.Field;
import java.util.List;

import endergeticexpansion.common.blocks.poise.boof.BoofBlock;
import endergeticexpansion.common.entities.BoofBlockEntity;
import endergeticexpansion.common.entities.bolloom.BolloomBalloonEntity;
import endergeticexpansion.common.entities.bolloom.BolloomFruitEntity;
import endergeticexpansion.core.registry.EETileEntities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemFrameEntity;
import net.minecraft.entity.item.PaintingEntity;
import net.minecraft.entity.monster.ShulkerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;

public class BoofTileEntity extends TileEntity implements ITickableTileEntity {
	
	public BoofTileEntity() {
		super(EETileEntities.BOOF_BLOCK.get());
	}

	@Override
	public void tick() {
		AxisAlignedBB bb = new AxisAlignedBB(this.getPos()).grow(0.05F);
		List<Entity> entities = this.world.getEntitiesWithinAABB(Entity.class, bb);
		
		for(int i = 0; i < entities.size(); i++) {
			Entity entity = entities.get(i);
			
			if(!(entity instanceof BoofBlockEntity) && 
				!(entity instanceof FishingBobberEntity) &&
				!(entity instanceof AbstractArrowEntity) &&
				!(entity instanceof BolloomFruitEntity) &&
				!(entity instanceof PaintingEntity) &&
				!(entity instanceof ItemFrameEntity) &&
				!(entity instanceof ShulkerEntity) &&
				!(entity instanceof BolloomBalloonEntity) &&
				!(this.world.getBlockState(this.pos).get(BoofBlock.BOOFED))) {
				if(entity instanceof PlayerEntity) {
					if(!((PlayerEntity)entity).isShiftKeyDown()){
						BoofBlock.doBoof(this.world, this.pos);
					}
					((PlayerEntity)entity).fallDistance = 0;
				} else {
					BoofBlock.doBoof(this.world, this.pos);
				}
			} else if((entity instanceof AbstractArrowEntity)) {
				String fieldName = this.isDeveloperWorkspace() ? "inGround" : "field_70254_i";
				Field inGround = findField(AbstractArrowEntity.class, fieldName);
				try {
					inGround.set(entity, Boolean.FALSE);
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
				BoofBlock.doBoof(this.world, this.pos);
			}
		}
	}
	
	public boolean isDeveloperWorkspace() {
	    final String target = System.getenv().get("target");
	    if (target == null) {
	        return false;
	    }
	    return target.contains("userdev");
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
	public static class UnableToFindFieldException extends RuntimeException {
        private UnableToFindFieldException(Exception e) {
            super(e);
        }
	}
	
}