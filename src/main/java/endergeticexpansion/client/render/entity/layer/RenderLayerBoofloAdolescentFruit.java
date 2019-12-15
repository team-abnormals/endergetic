package endergeticexpansion.client.render.entity.layer;

import com.mojang.blaze3d.platform.GlStateManager;

import endergeticexpansion.client.model.booflo.ModelAdolescentBooflo;
import endergeticexpansion.common.entities.booflo.EntityBoofloAdolescent;
import endergeticexpansion.core.registry.EEItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@SuppressWarnings("deprecation")
@OnlyIn(Dist.CLIENT)
public class RenderLayerBoofloAdolescentFruit extends LayerRenderer<EntityBoofloAdolescent, ModelAdolescentBooflo<EntityBoofloAdolescent>> {
	private final ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
	
	public RenderLayerBoofloAdolescentFruit(IEntityRenderer<EntityBoofloAdolescent, ModelAdolescentBooflo<EntityBoofloAdolescent>> renderer) {
		super(renderer);
	}
	
	
	@Override
	public void render(EntityBoofloAdolescent booflo, float p_212842_2_, float p_212842_3_, float p_212842_4_, float p_212842_5_, float f, float f1, float p_212842_8_) {
		if(booflo.hasFruit()) {
			GlStateManager.pushMatrix();
			
			GlStateManager.translatef((this.getEntityModel()).Jaw.rotationPointX / 16.0F, (this.getEntityModel()).Jaw.rotationPointY / 16.0F, (this.getEntityModel()).Jaw.rotationPointZ / 16.0F - 0.5F);
			GlStateManager.rotatef(90F, 1.0F, 0.0F, 0.0F);
			
			this.itemRenderer.renderItem(new ItemStack(EEItems.BOLLOOM_FRUIT.get()), booflo, ItemCameraTransforms.TransformType.GROUND, false);
			
			GlStateManager.popMatrix();
		}
	}
	
	@Override
	public boolean shouldCombineTextures() {
		return false;
	}
}
