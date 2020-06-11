package endergeticexpansion.client.render.entity.layer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.teamabnormals.abnormals_core.client.ACRenderTypes;
import com.teamabnormals.abnormals_core.client.ClientInfo;

import endergeticexpansion.common.entities.booflo.BoofloEntity;
import endergeticexpansion.core.EndergeticExpansion;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class LayerRendererBoofloBracelets<E extends BoofloEntity, M extends EntityModel<E>> extends LayerRenderer<E, M> {
	
	public LayerRendererBoofloBracelets(IEntityRenderer<E, M> entityRenderer) {
		super(entityRenderer);
	}
	
	@Override
	public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, E booflo, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		if(!booflo.isTamed()) return;
		
		ResourceLocation texture = new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/booflo/bracelets/booflo_ring_" + booflo.getBraceletsColor().getTranslationKey() + ".png");
		ClientInfo.MINECRAFT.getTextureManager().bindTexture(texture);

		IVertexBuilder ivertexbuilder = bufferIn.getBuffer(ACRenderTypes.getEmissiveEntity(texture));
		
		this.getEntityModel().setRotationAngles(booflo, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
		this.getEntityModel().render(matrixStackIn, ivertexbuilder, 240, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
	}
	
}