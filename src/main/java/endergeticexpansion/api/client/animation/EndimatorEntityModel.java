package endergeticexpansion.api.client.animation;

import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * @author - SmellyModder(Luke Tonon)
 * @param <E> - The Entity for the Model; Vanilla needs this by default so it will be used here as well
 */
@OnlyIn(Dist.CLIENT)
public class EndimatorEntityModel<E extends LivingEntity> extends EntityModel<E> {
	
	public void setDefaultBoxValues() {
		for(int i = 0; i < this.boxList.size(); i++) {
			((EndimatorRendererModel)this.boxList.get(i)).setDefaultBoxValues();
		}
	}
	
	public void revertBoxesToDefaultValues() {
		for(int i = 0; i < this.boxList.size(); i++) {
			((EndimatorRendererModel)this.boxList.get(i)).revertToDefaultBoxValues();
		}
	}
	
}
