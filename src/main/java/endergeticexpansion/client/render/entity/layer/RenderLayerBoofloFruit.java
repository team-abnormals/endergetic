package endergeticexpansion.client.render.entity.layer;

import com.mojang.blaze3d.platform.GlStateManager;

import endergeticexpansion.client.model.booflo.ModelBooflo;
import endergeticexpansion.client.model.booflo.ModelBoofloInflated;
import endergeticexpansion.common.entities.booflo.EntityBooflo;
import endergeticexpansion.core.registry.EEItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@SuppressWarnings("deprecation")
@OnlyIn(Dist.CLIENT)
public class RenderLayerBoofloFruit extends LayerRenderer<EntityBooflo, EntityModel<EntityBooflo>> {
	private final ItemRenderer ITEM_RENDERER = Minecraft.getInstance().getItemRenderer();
	
	public RenderLayerBoofloFruit(IEntityRenderer<EntityBooflo, EntityModel<EntityBooflo>> renderer) {
		super(renderer);
	}
	
	@Override
	public void render(EntityBooflo booflo, float p_212842_2_, float p_212842_3_, float p_212842_4_, float p_212842_5_, float f, float f1, float p_212842_8_) {
		if(booflo.hasCaughtFruit() && booflo.isEndimationPlaying(EntityBooflo.EAT) && booflo.getAnimationTick() > 20) {
			GlStateManager.pushMatrix();
			
			EntityModel<EntityBooflo> boofloModel = this.getBoofloModel(booflo);
			
			if(boofloModel instanceof ModelBooflo) {
				GlStateManager.translatef(((ModelBooflo<EntityBooflo>) (boofloModel)).FruitPos.rotationPointX / 16 - 1.25F, ((ModelBooflo<EntityBooflo>) (boofloModel)).FruitPos.rotationPointY / 16 + 1.45F + this.getFruitPosOffset(booflo), ((ModelBooflo<EntityBooflo>) (boofloModel)).FruitPos.rotationPointZ / 16 - 1.2F);
			}
			
			GlStateManager.rotatef(90F, 1.0F, 0.0F, 0.0F);
			GlStateManager.scalef(1.3F, 1.3F, 1.3F);
			
			this.ITEM_RENDERER.renderItem(new ItemStack(EEItems.BOLLOOM_FRUIT.get()), booflo, ItemCameraTransforms.TransformType.GROUND, false);
			
			GlStateManager.popMatrix();
		}
	}
	
	private float getFruitPosOffset(EntityBooflo booflo) {
		return 0.22F * booflo.FRUIT_HOVER.getAnimationProgress();
	}
	
	private EntityModel<EntityBooflo> getBoofloModel(EntityBooflo booflo) {
		return booflo.isBoofed() ? new ModelBoofloInflated<>() : new ModelBooflo<>();
	}
	
	@Override
	public boolean shouldCombineTextures() {
		return false;
	}
}