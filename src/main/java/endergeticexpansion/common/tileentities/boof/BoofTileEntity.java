package endergeticexpansion.common.tileentities.boof;

import java.util.List;

import endergeticexpansion.common.blocks.poise.boof.BoofBlock;
import endergeticexpansion.core.registry.EETileEntities;
import endergeticexpansion.core.registry.other.EETags;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

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
			
			if(!EETags.EntityTypes.BOOF_BLOCK_RESISTANT.contains(entity.getType()) && !(this.world.getBlockState(this.pos).get(BoofBlock.BOOFED))) {
				if(entity instanceof PlayerEntity) {
					if(!((PlayerEntity) entity).isSneaking()){
						BoofBlock.doBoof(this.world, this.pos);
					}
					((PlayerEntity) entity).fallDistance = 0;
				} else if(entity instanceof AbstractArrowEntity) {
					ObfuscationReflectionHelper.setPrivateValue(AbstractArrowEntity.class, (AbstractArrowEntity) entity, false, "field_70254_i");
					BoofBlock.doBoof(this.world, this.pos);
				} else {
					BoofBlock.doBoof(this.world, this.pos);
				}
			}
		}
	}
	
}