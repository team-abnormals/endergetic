package endergeticexpansion.client.render.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import endergeticexpansion.client.model.bolloom.ModelBolloomBalloon;
import endergeticexpansion.common.entities.bolloom.EntityBolloomBalloon;
import endergeticexpansion.core.EndergeticExpansion;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;

public class RenderBolloomBalloon extends EntityRenderer<EntityBolloomBalloon> {
	public static final ResourceLocation DEFAULT_TEXTURE = new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/balloon/bolloom_balloon.png");
	public static final ResourceLocation[] COLORS = new ResourceLocation[] {
		new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/balloon/bolloom_balloon_white.png"),
		new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/balloon/bolloom_balloon_orange.png"),
		new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/balloon/bolloom_balloon_magenta.png"),
		new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/balloon/bolloom_balloon_light_blue.png"),
		new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/balloon/bolloom_balloon_yellow.png"),
		new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/balloon/bolloom_balloon_lime.png"),
		new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/balloon/bolloom_balloon_pink.png"),
		new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/balloon/bolloom_balloon_gray.png"),
		new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/balloon/bolloom_balloon_light_gray.png"),
		new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/balloon/bolloom_balloon_cyan.png"),
		new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/balloon/bolloom_balloon_purple.png"),
		new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/balloon/bolloom_balloon_blue.png"),
		new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/balloon/bolloom_balloon_brown.png"),
		new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/balloon/bolloom_balloon_green.png"),
		new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/balloon/bolloom_balloon_red.png"),
		new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/balloon/bolloom_balloon_black.png"),
	};

	public ModelBolloomBalloon<EntityBolloomBalloon> model;
	
	public RenderBolloomBalloon(EntityRendererManager p_i46179_1_) {
		super(p_i46179_1_);
		model = new ModelBolloomBalloon<EntityBolloomBalloon>();
	}
	
	@Override
	public void render(EntityBolloomBalloon entity, float entityYaw, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer bufferIn, int packedLightIn) {
		float[] angles = entity.getVineAnimation(partialTicks);
		model.x_string.rotateAngleX = angles[0];
		model.x_string.rotateAngleY = angles[1];
		matrixStack.push();
		matrixStack.translate(0.0F, 1.5F, 0.0F);
		matrixStack.rotate(Vector3f.XP.rotationDegrees(180.0F));
		IVertexBuilder ivertexbuilder = bufferIn.getBuffer(this.model.getRenderType(this.getEntityTexture(entity)));
    	this.model.render(matrixStack, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
		matrixStack.pop();
		
		super.render(entity, entityYaw, partialTicks, matrixStack, bufferIn, packedLightIn);
	}
	
	@Override
	public ResourceLocation getEntityTexture(EntityBolloomBalloon balloon) {
		return balloon.getColor() == null ? DEFAULT_TEXTURE : COLORS[balloon.getColor().getId()];
	}
}