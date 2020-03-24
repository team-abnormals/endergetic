package endergeticexpansion.client.render.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import endergeticexpansion.client.model.ModelEndergeticBoat;
import endergeticexpansion.common.entities.EntityEndergeticBoat;
import endergeticexpansion.core.EndergeticExpansion;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderEndergeticBoat extends EntityRenderer<EntityEndergeticBoat> {
	private static final ResourceLocation[] BOAT_TEXTURES = new ResourceLocation[] {
		new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/boat/poise_boat.png"),
	};
	protected final ModelEndergeticBoat model = new ModelEndergeticBoat();

	public RenderEndergeticBoat(EntityRendererManager renderManagerIn) {
		super(renderManagerIn);
		this.shadowSize = 0.8F;
	}

    @Override
    public void render(EntityEndergeticBoat entity, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
    	matrixStackIn.push();
    	matrixStackIn.translate(0.0D, 0.375D, 0.0D);
    	matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180.0F - entityYaw));
    	float f = (float)entity.getTimeSinceHit() - partialTicks;
    	float f1 = entity.getDamageTaken() - partialTicks;
    	if (f1 < 0.0F) {
    		f1 = 0.0F;
    	}

    	if (f > 0.0F) {
    		matrixStackIn.rotate(Vector3f.XP.rotationDegrees(MathHelper.sin(f) * f * f1 / 10.0F * (float)entity.getForwardDirection()));
    	}

    	float f2 = entity.getRockingAngle(partialTicks);
    	if(!MathHelper.epsilonEquals(f2, 0.0F)) {
    		matrixStackIn.rotate(new Quaternion(new Vector3f(1.0F, 0.0F, 1.0F), entity.getRockingAngle(partialTicks), true));
    	}

    	matrixStackIn.scale(-1.0F, -1.0F, 1.0F);
    	matrixStackIn.rotate(Vector3f.YP.rotationDegrees(90.0F));
    	this.model.setRotationAngles(entity, partialTicks, 0.0F, -0.1F, 0.0F, 0.0F);
    	IVertexBuilder ivertexbuilder = bufferIn.getBuffer(this.model.getRenderType(this.getEntityTexture(entity)));
    	this.model.render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
    	IVertexBuilder ivertexbuilder1 = bufferIn.getBuffer(RenderType.getWaterMask());
    	this.model.func_228245_c_().render(matrixStackIn, ivertexbuilder1, packedLightIn, OverlayTexture.NO_OVERLAY);
    	matrixStackIn.pop();
    	super.render(entity, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    @Override
	public ResourceLocation getEntityTexture(EntityEndergeticBoat entity) {
        return BOAT_TEXTURES[entity.getBoatModel().ordinal()];
    }
}