package endergeticexpansion.api.endimator;

import java.util.List;

import com.google.common.collect.Lists;

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
public abstract class EndimatorEntityModel<E extends Entity & IEndimatedEntity> extends EntityModel<E> {
	protected List<EndimatorModelRenderer> cuboids = Lists.newArrayList();
	private EndimatorModelRenderer scaleController;
	protected Endimator endimator = new Endimator();
	protected E entity;
	
	public void animateModel(E endimatedEntity, float f, float f1, float f2, float f3, float f4, float f5) {}
	
	@Override
	public void setRotationAngles(E entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.entity = entity;
	}
	
	public void addCuboid(EndimatorModelRenderer cuboid) {
		this.cuboids.add(cuboid);
	}
	
	public void setDefaultBoxValues() {
		this.cuboids.forEach((rendererModel) -> {
			if(rendererModel instanceof EndimatorModelRenderer) {
				((EndimatorModelRenderer) rendererModel).setDefaultBoxValues();
			}
		});
	}
	
	public void revertBoxesToDefaultValues() {
		this.cuboids.forEach((rendererModel) -> {
			if(rendererModel instanceof EndimatorModelRenderer) {
				((EndimatorModelRenderer) rendererModel).revertToDefaultBoxValues();
			}
		});
	}
	
	public void createScaleController() {
		this.scaleController = new EndimatorModelRenderer(this, 0, 0);
		this.scaleController.showModel = false;
		this.scaleController.setRotationPoint(1, 1, 1);
	}
	
	public EndimatorModelRenderer getScaleController() {
		return this.scaleController;
	}
}