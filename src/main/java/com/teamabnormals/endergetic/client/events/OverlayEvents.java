package com.teamabnormals.endergetic.client.events;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.teamabnormals.endergetic.common.entity.booflo.Booflo;
import com.teamabnormals.endergetic.common.entity.eetle.GliderEetle;
import com.teamabnormals.endergetic.core.EndergeticExpansion;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = EndergeticExpansion.MOD_ID, value = Dist.CLIENT)
public final class OverlayEvents {
	private static final Minecraft MC = Minecraft.getInstance();
	private static boolean purpoidFlash;
	private static int prevPurpoidFlashTime = 0, purpoidFlashTime = 0;

	@SubscribeEvent
	public static void onClientTick(TickEvent.ClientTickEvent event) {
		if (event.phase == TickEvent.Phase.START) {
			prevPurpoidFlashTime = purpoidFlashTime;
			if (purpoidFlash) {
				if (++purpoidFlashTime >= 5) {
					purpoidFlash = false;
				}
			} else if (purpoidFlashTime > 0) {
				purpoidFlashTime--;
			}
		}
	}

	@SubscribeEvent
	public static void renderOverlays(RenderGuiOverlayEvent.Pre event) {
		LocalPlayer player = MC.player;
		if (player != null) {
			if (!MC.options.hideGui) {
				ResourceLocation overlayID = event.getOverlay().id();
				if (overlayID == VanillaGuiOverlay.EXPERIENCE_BAR.id()) {
					if (player.isPassenger() && player.getVehicle() instanceof Booflo) {
						event.setCanceled(true);

						int scaledWidth = event.getWindow().getGuiScaledWidth();
						int scaledHeight = event.getWindow().getGuiScaledHeight();
						int top = scaledHeight - 32 + 3;
						int left = scaledWidth / 2 - 91;
						int progress = ((Booflo) player.getVehicle()).getBoostPower();

						PoseStack stack = event.getPoseStack();
						stack.pushPose();
						RenderSystem.setShaderTexture(0, new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/gui/booflo_bar.png"));

						OverlayEvents.drawTexture(stack, left, top, 0, 0, 182, 5);
						if (progress > 0) {
							OverlayEvents.drawTexture(stack, left, top, 0, 5, progress, 10);
						}

						stack.popPose();
					}
				} else if (overlayID == VanillaGuiOverlay.MOUNT_HEALTH.id() && player.level.getDifficulty() != Difficulty.PEACEFUL && !player.isSpectator() && !player.isCreative() && player.isPassenger() && player.getVehicle() instanceof GliderEetle) {
					event.setCanceled(true);
				} else if (overlayID == VanillaGuiOverlay.VIGNETTE.id() && MC.options.getCameraType() == CameraType.FIRST_PERSON) {
					float purpoidFlashProgress = Mth.lerp(event.getPartialTick(), prevPurpoidFlashTime, purpoidFlashTime) * 0.2F;
					if (purpoidFlashProgress > 0.0F) {
						PoseStack stack = event.getPoseStack();
						stack.pushPose();
						RenderSystem.disableDepthTest();
						RenderSystem.depthMask(false);
						RenderSystem.enableBlend();
						RenderSystem.defaultBlendFunc();
						RenderSystem.setShader(GameRenderer::getPositionTexShader);
						RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, purpoidFlashProgress);
						RenderSystem.setShaderTexture(0, new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/gui/overlay/purpoid_flash.png"));
						Tesselator tessellator = Tesselator.getInstance();
						BufferBuilder bufferbuilder = tessellator.getBuilder();
						bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
						Window mainWindow = MC.getWindow();
						int scaledWidth = mainWindow.getGuiScaledWidth();
						int scaledHeight = mainWindow.getGuiScaledHeight();
						bufferbuilder.vertex(0.0D, scaledHeight, -90.0D).uv(0.0F, 1.0F).endVertex();
						bufferbuilder.vertex(scaledWidth, scaledHeight, -90.0D).uv(1.0F, 1.0F).endVertex();
						bufferbuilder.vertex(scaledWidth, 0.0D, -90.0D).uv(1.0F, 0.0F).endVertex();
						bufferbuilder.vertex(0.0D, 0.0D, -90.0D).uv(0.0F, 0.0F).endVertex();
						tessellator.end();
						RenderSystem.depthMask(true);
						RenderSystem.enableDepthTest();
						RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
						stack.popPose();
					}
				}
			}
		}
	}

	private static void drawTexture(PoseStack stack, int posX, int posY, int p_blit_3_, int p_blit_4_, int p_blit_5_, int p_blit_6_) {
		GuiComponent.blit(stack, posX, posY, -90, (float) p_blit_3_, (float) p_blit_4_, p_blit_5_, p_blit_6_, 256, 256);
	}

	public static void enablePurpoidFlash() {
		OverlayEvents.purpoidFlash = true;
	}
}