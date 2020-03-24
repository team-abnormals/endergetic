package endergeticexpansion.client.render.entity.layer;

import com.mojang.blaze3d.matrix.MatrixStack;

import endergeticexpansion.common.entities.booflo.EntityBooflo;
import endergeticexpansion.core.registry.EEItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@SuppressWarnings("deprecation")
@OnlyIn(Dist.CLIENT)
public class RenderLayerBoofloFruit extends LayerRenderer<EntityBooflo, EntityModel<EntityBooflo>> {
	
	public RenderLayerBoofloFruit(IEntityRenderer<EntityBooflo, EntityModel<EntityBooflo>> renderer) {
		super(renderer);
	}
	
	@Override
	public void render(MatrixStack matrixStack, IRenderTypeBuffer bufferIn, int packedLightIn, EntityBooflo booflo, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		if(booflo.hasCaughtFruit() && !booflo.isBoofed() && booflo.isEndimationPlaying(EntityBooflo.EAT) && booflo.getAnimationTick() > 20) {
			matrixStack.push();
			
			Vec3d fruitPos = (new Vec3d(-1.25D, 0.0D, 0.0D)).rotateYaw(-booflo.rotationYaw * ((float)Math.PI / 180F) - ((float)Math.PI / 2F));
			matrixStack.translate(fruitPos.getX(), fruitPos.getY() + 1.15F + this.getFruitPosOffset(booflo), fruitPos.getZ());
			
			matrixStack.rotate(Vector3f.XP.rotationDegrees(90.0F));
			
			matrixStack.scale(1.3F, 1.3F, 1.3F);
			
			Minecraft.getInstance().getFirstPersonRenderer().renderItemSide(booflo, new ItemStack(EEItems.BOLLOOM_FRUIT.get()), ItemCameraTransforms.TransformType.GROUND, false, matrixStack, bufferIn, packedLightIn);
			matrixStack.pop();
		}
	}
	
	private float getFruitPosOffset(EntityBooflo booflo) {
		return 0.22F * booflo.FRUIT_HOVER.getAnimationProgress();
	}
	
}