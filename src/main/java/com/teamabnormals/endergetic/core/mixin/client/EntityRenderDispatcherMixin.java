package com.teamabnormals.endergetic.core.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.teamabnormals.endergetic.common.entity.eetle.BroodEetle;
import com.teamabnormals.endergetic.common.entity.eetle.BroodEggSack;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderDispatcher.class)
public final class EntityRenderDispatcherMixin {

	@Inject(at = @At(value = "JUMP", shift = At.Shift.BEFORE, ordinal = 0), method = "renderHitbox")
	private static void renderEggSackBoundingBox(PoseStack poseStack, VertexConsumer bufferIn, Entity entity, float partialTicks, CallbackInfo info) {
		if (entity instanceof BroodEetle) {
			BroodEggSack eggSackEntity = ((BroodEetle) entity).getEggSack(entity.level);
			if (eggSackEntity != null) {
				poseStack.pushPose();
				double x = -Mth.lerp(partialTicks, entity.xOld, entity.getX());
				double y = -Mth.lerp(partialTicks, entity.yOld, entity.getY());
				double z = -Mth.lerp(partialTicks, entity.zOld, entity.getZ());
				BroodEetle broodEetle = (BroodEetle) entity;
				Vec3 eggSackPos = BroodEggSack.getEggPos(new Vec3(-x, -y, -z), Mth.lerp(partialTicks, broodEetle.yBodyRotO, broodEetle.yBodyRot), broodEetle.getEggCannonProgress(), broodEetle.getEggCannonFlyingProgress(), broodEetle.getFlyingRotations().getRenderFlyPitch(), broodEetle.isOnLastHealthStage());
				poseStack.translate(x + eggSackPos.x, y + eggSackPos.y, z + eggSackPos.z);
				AABB axisalignedbb = eggSackEntity.getBoundingBox().move(-eggSackEntity.getX(), -eggSackEntity.getY(), -eggSackEntity.getZ());
				LevelRenderer.renderLineBox(poseStack, bufferIn, axisalignedbb, 0.25F, 1.0F, 0.0F, 1.0F);
				poseStack.popPose();
			}
		}
	}

	@Inject(at = @At(value = "HEAD"), method = "renderHitbox", cancellable = true)
	private static void cancelDefaultEggSackBoundingBox(PoseStack poseStack, VertexConsumer vertexConsumer, Entity entity, float partialTicks, CallbackInfo info) {
		if (entity instanceof BroodEggSack) {
			info.cancel();
		}
	}

}
