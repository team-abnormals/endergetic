package com.minecraftabnormals.endergetic.client.events;

import com.minecraftabnormals.endergetic.common.entities.booflo.BoofloEntity;
import com.minecraftabnormals.endergetic.common.entities.eetle.GliderEetleEntity;
import com.minecraftabnormals.endergetic.core.EndergeticExpansion;
import com.mojang.blaze3d.vertex.PoseStack;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.gui.GuiComponent;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.client.CameraType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.client.gui.IIngameOverlay;
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
	public static void renderOverlays(RenderGameOverlayEvent.PreLayer event) {
		LocalPlayer player = MC.player;
		if (player != null) {
			if (!MC.options.hideGui) {
				IIngameOverlay overlay = event.getOverlay();
				if (overlay == ForgeIngameGui.EXPERIENCE_BAR_ELEMENT) {
					if (player.isPassenger() && player.getVehicle() instanceof BoofloEntity) {
						event.setCanceled(true);

						int scaledWidth = event.getWindow().getGuiScaledWidth();
						int scaledHeight = event.getWindow().getGuiScaledHeight();
						int top = scaledHeight - 32 + 3;
						int left = scaledWidth / 2 - 91;
						int progress = ((BoofloEntity) player.getVehicle()).getBoostPower();

						PoseStack stack = event.getPoseStack();
						stack.pushPose();
						RenderSystem.setShaderTexture(0, new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/gui/booflo_bar.png"));

						OverlayEvents.drawTexture(stack, left, top, 0, 0, 182, 5);
						if (progress > 0) {
							OverlayEvents.drawTexture(stack, left, top, 0, 5, progress, 10);
						}

						stack.popPose();
					}
				} else if (overlay == ForgeIngameGui.MOUNT_HEALTH_ELEMENT && player.level.getDifficulty() != Difficulty.PEACEFUL && !player.isSpectator() && !player.isCreative() && player.isPassenger() && player.getVehicle() instanceof GliderEetleEntity) {
					event.setCanceled(true);
				} else if (overlay == ForgeIngameGui.VIGNETTE_ELEMENT && MC.options.getCameraType() == CameraType.FIRST_PERSON) {
					float purpoidFlashProgress = Mth.lerp(event.getPartialTick(), prevPurpoidFlashTime, purpoidFlashTime) * 0.2F;
					if (purpoidFlashProgress > 0.0F) {
						PoseStack stack = event.getPoseStack();
						stack.pushPose();
						RenderSystem.setShaderTexture(0, new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/gui/overlay/purpoid_flash.png"));
						RenderSystem.enableBlend();
						RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
						RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, purpoidFlashProgress);
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