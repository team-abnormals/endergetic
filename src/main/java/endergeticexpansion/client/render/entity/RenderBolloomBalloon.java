package endergeticexpansion.client.render.entity;

import com.mojang.blaze3d.platform.GlStateManager;

import endergeticexpansion.client.model.bolloom.ModelBolloomBalloon;
import endergeticexpansion.common.entities.bolloom.EntityBolloomBalloon;
import endergeticexpansion.core.EndergeticExpansion;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
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
	public void doRender(EntityBolloomBalloon entity, double x, double y, double z, float entityYaw, float partialTicks) {
		float angle1Old = entity.prevVineAngle;
        float angle2Old = entity.prevAngle;
        float angle1New = entity.getVineAngle();
        float angle2New = entity.getAngle();
        float angle1 = angle1Old * (1 - partialTicks) + angle1New * partialTicks;
        float angle2 = angle2Old * (1 - partialTicks) + angle2New * partialTicks;
        model.x_string.rotateAngleX = angle1;
        model.x_string.rotateAngleY = angle2;
		GlStateManager.pushMatrix();
		this.bindTexture(this.getEntityTexture(entity));
		GlStateManager.translated(x, y + 1.5F, z);
		GlStateManager.rotatef(180.0F, 1.0F, 0.0F, 0.0F);
		model.render(entity, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0625f);
		GlStateManager.popMatrix();
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}
	
	@Override
	protected ResourceLocation getEntityTexture(EntityBolloomBalloon balloon) {
		return balloon.getColor() == null ? DEFAULT_TEXTURE : COLORS[balloon.getColor().getId()];
	}
	
}
