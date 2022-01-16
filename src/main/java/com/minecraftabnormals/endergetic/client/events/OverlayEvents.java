package com.minecraftabnormals.endergetic.client.events;

import com.minecraftabnormals.endergetic.common.entities.booflo.BoofloEntity;
import com.minecraftabnormals.endergetic.core.EndergeticExpansion;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = EndergeticExpansion.MOD_ID, value = Dist.CLIENT)
public final class OverlayEvents {
	private static final Minecraft MC = Minecraft.getInstance();

	@SubscribeEvent
	public static void renderOverlays(RenderGameOverlayEvent.Pre event) {
		ClientPlayerEntity player = MC.player;
		if (!MC.options.hideGui && event.getType() == ElementType.EXPERIENCE) {
			if (player.isPassenger() && player.getVehicle() instanceof BoofloEntity) {
				event.setCanceled(true);

				int scaledWidth = event.getWindow().getGuiScaledWidth();
				int scaledHeight = event.getWindow().getGuiScaledHeight();
				int top = scaledHeight - 32 + 3;
				int left = scaledWidth / 2 - 91;
				int progress = ((BoofloEntity) player.getVehicle()).getBoostPower();

				MatrixStack stack = event.getMatrixStack();
				stack.pushPose();
				MC.textureManager.bind(new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/gui/booflo_bar.png"));

				OverlayEvents.drawTexture(stack, left, top, 0, 0, 182, 5);
				if (progress > 0) {
					OverlayEvents.drawTexture(stack, left, top, 0, 5, progress, 10);
				}

				stack.popPose();
			}
		}
	}

	private static void drawTexture(MatrixStack stack, int posX, int posY, int p_blit_3_, int p_blit_4_, int p_blit_5_, int p_blit_6_) {
		AbstractGui.blit(stack, posX, posY, -90, (float) p_blit_3_, (float) p_blit_4_, p_blit_5_, p_blit_6_, 256, 256);
	}
}