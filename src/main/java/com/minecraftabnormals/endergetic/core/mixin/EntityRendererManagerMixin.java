package com.minecraftabnormals.endergetic.core.mixin;

import com.minecraftabnormals.endergetic.common.entities.eetle.BroodEetleEntity;
import com.minecraftabnormals.endergetic.common.entities.eetle.BroodEggSackEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRendererManager.class)
public final class EntityRendererManagerMixin {

	@Inject(at = @At(value = "JUMP", shift = At.Shift.BEFORE, ordinal = 0), method = "renderHitbox")
	private void renderEggSackBoundingBox(MatrixStack matrixStack, IVertexBuilder bufferIn, Entity entity, float partialTicks, CallbackInfo info) {
		if (entity instanceof BroodEetleEntity) {
			BroodEggSackEntity eggSackEntity = ((BroodEetleEntity) entity).getEggSack(entity.level);
			if (eggSackEntity != null) {
				matrixStack.pushPose();
				double x = -MathHelper.lerp(partialTicks, entity.xOld, entity.getX());
				double y = -MathHelper.lerp(partialTicks, entity.yOld, entity.getY());
				double z = -MathHelper.lerp(partialTicks, entity.zOld, entity.getZ());
				BroodEetleEntity broodEetleEntity = (BroodEetleEntity) entity;
				Vector3d eggSackPos = BroodEggSackEntity.getEggPos(new Vector3d(-x, -y, -z), MathHelper.lerp(partialTicks, broodEetleEntity.yBodyRotO, broodEetleEntity.yBodyRot), broodEetleEntity.getEggCannonProgress(), broodEetleEntity.getEggCannonFlyingProgress(), broodEetleEntity.getFlyingRotations().getRenderFlyPitch(), broodEetleEntity.isOnLastHealthStage());
				matrixStack.translate(x + eggSackPos.x, y + eggSackPos.y, z + eggSackPos.z);
				AxisAlignedBB axisalignedbb = eggSackEntity.getBoundingBox().move(-eggSackEntity.getX(), -eggSackEntity.getY(), -eggSackEntity.getZ());
				WorldRenderer.renderLineBox(matrixStack, bufferIn, axisalignedbb, 0.25F, 1.0F, 0.0F, 1.0F);
				matrixStack.popPose();
			}
		}
	}

	@Inject(at = @At(value = "HEAD"), method = "renderHitbox", cancellable = true)
	private void cancelDefaultEggSackBoundingBox(MatrixStack matrixStack, IVertexBuilder bufferIn, Entity entity, float partialTicks, CallbackInfo info) {
		if (entity instanceof BroodEggSackEntity) {
			info.cancel();
		}
	}

}
