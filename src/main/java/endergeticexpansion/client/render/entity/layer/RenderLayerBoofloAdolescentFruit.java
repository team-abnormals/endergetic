package endergeticexpansion.client.render.entity.layer;

import com.mojang.blaze3d.matrix.MatrixStack;

import endergeticexpansion.client.model.booflo.ModelAdolescentBooflo;
import endergeticexpansion.common.entities.booflo.EntityBoofloAdolescent;
import endergeticexpansion.core.registry.EEItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@SuppressWarnings("deprecation")
@OnlyIn(Dist.CLIENT)
public class RenderLayerBoofloAdolescentFruit extends LayerRenderer<EntityBoofloAdolescent, ModelAdolescentBooflo<EntityBoofloAdolescent>> {
	
	public RenderLayerBoofloAdolescentFruit(IEntityRenderer<EntityBoofloAdolescent, ModelAdolescentBooflo<EntityBoofloAdolescent>> renderer) {
		super(renderer);
	}
	
	@Override
	public void render(MatrixStack matrixStack, IRenderTypeBuffer bufferIn, int packedLightIn, EntityBoofloAdolescent adolescent, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		if(adolescent.hasFruit()) {
			matrixStack.push();
			matrixStack.translate(0.0F, 1.1F, -0.2F);
			
			matrixStack.rotate(Vector3f.XP.rotationDegrees(90.0F));
			
			Minecraft.getInstance().getFirstPersonRenderer().renderItemSide(adolescent, new ItemStack(EEItems.BOLLOOM_FRUIT.get()), ItemCameraTransforms.TransformType.GROUND, false, matrixStack, bufferIn, packedLightIn);
			matrixStack.pop();
		}
	}
	
}