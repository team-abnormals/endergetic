package endergeticexpansion.client.render.entity.layer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import endergeticexpansion.common.entities.puffbug.EntityPuffBug;
import endergeticexpansion.core.EndergeticExpansion;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderLayerPuffBugGlow <T extends EntityPuffBug, M extends EntityModel<T>> extends LayerRenderer<T, M> { 
	private static final ResourceLocation[] TEXTURES = new ResourceLocation[] {
		new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/puffbug/puffbug_inflated_overlay.png"),
		new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/puffbug/puffbug_inflated_overlay_grayscale.png"),
		new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/puffbug/puffbug_inflated_levitation_overlay.png"),
	};
	
	public RenderLayerPuffBugGlow(IEntityRenderer<T, M> entityRenderer) {
		super(entityRenderer);
	}
	
	@Override
	public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, T puffbug, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		if(!puffbug.isInflated()) return;
		
		boolean colorFlag = puffbug.getColor() != -1 && !isLeviationOnlyEffect(puffbug);
		
		float[] rgb = new float[] {
			colorFlag ? (float) ((puffbug.getColor() >> 16 & 255) / 255.0D) * 1.5F : 1.0F,
			colorFlag ? (float) ((puffbug.getColor() >> 8 & 255) / 255.0D) * 1.5F : 1.0F,
			colorFlag ? (float) ((puffbug.getColor() & 255) / 255.0D) * 1.5F : 1.0F,
		};
		
		IVertexBuilder ivertexbuilder = bufferIn.getBuffer(RenderType.getEyes(this.getTexture(puffbug)));
		this.getEntityModel().render(matrixStackIn, ivertexbuilder, 15728640, OverlayTexture.NO_OVERLAY, rgb[0], rgb[1], rgb[2], 1.0F);
	}
	
	private ResourceLocation getTexture(EntityPuffBug puffbug) {
		if(this.isLeviationOnlyEffect(puffbug)) {
			return TEXTURES[2];
		}
		return puffbug.getColor() != -1 ? TEXTURES[1] : TEXTURES[0];
	}
	
	private boolean isLeviationOnlyEffect(EntityPuffBug bug) {
		if(bug.getColor() == 13565951) {
			return true;
		}
		return false;
	}
}