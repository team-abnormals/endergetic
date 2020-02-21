package endergeticexpansion.api.endimator;

import endergeticexpansion.api.endimator.entity.IEndimatedEntity;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * @author - SmellyModder(Luke Tonon)
 * @param <E> - The Entity for the Model; Vanilla needs this by default so it will be used here as well
 */
@OnlyIn(Dist.CLIENT)
public class EndimatorEntityModel<E extends Entity & IEndimatedEntity> extends EntityModel<E> {
	private EndimatorRendererModel scaleController;
	protected Endimator endimator = new Endimator();
	
	public void animateModel(E endimatedEntity, float f, float f1, float f2, float f3, float f4, float f5) {
		this.setRotationAngles(endimatedEntity, f, f1, f2, f3, f4, f5);
	}
	
	public void setDefaultBoxValues() {
		this.boxList.forEach((rendererModel) -> {
			if(rendererModel instanceof EndimatorRendererModel) {
				((EndimatorRendererModel) rendererModel).setDefaultBoxValues();
			}
		});
	}
	
	public void revertBoxesToDefaultValues() {
		this.boxList.forEach((rendererModel) -> {
			if(rendererModel instanceof EndimatorRendererModel) {
				((EndimatorRendererModel) rendererModel).revertToDefaultBoxValues();
			}
		});
	}
	
	public void createScaleController() {
		this.scaleController = new EndimatorRendererModel(this, 0, 0);
		this.scaleController.showModel = false;
		this.scaleController.setRotationPoint(1, 1, 1);
	}
	
	public EndimatorRendererModel getScaleController() {
		return this.scaleController;
	}
}