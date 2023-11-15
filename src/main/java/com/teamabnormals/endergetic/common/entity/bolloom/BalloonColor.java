package com.teamabnormals.endergetic.common.entity.bolloom;

import com.teamabnormals.endergetic.core.EndergeticExpansion;
import com.teamabnormals.endergetic.core.registry.EEItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public enum BalloonColor {
	WHITE(DyeColor.WHITE, EEItems.WHITE_BOLLOOM_BALLOON),
	ORANGE(DyeColor.ORANGE, EEItems.ORANGE_BOLLOOM_BALLOON),
	MAGENTA(DyeColor.MAGENTA, EEItems.MAGENTA_BOLLOOM_BALLOON),
	LIGHT_BLUE(DyeColor.LIGHT_BLUE, EEItems.LIGHT_BLUE_BOLLOOM_BALLOON),
	YELLOW(DyeColor.YELLOW, EEItems.YELLOW_BOLLOOM_BALLOON),
	LIME(DyeColor.LIME, EEItems.LIME_BOLLOOM_BALLOON),
	PINK(DyeColor.PINK, EEItems.PINK_BOLLOOM_BALLOON),
	GRAY(DyeColor.GRAY, EEItems.GRAY_BOLLOOM_BALLOON),
	LIGHT_GRAY(DyeColor.LIGHT_GRAY, EEItems.LIGHT_GRAY_BOLLOOM_BALLOON),
	CYAN(DyeColor.CYAN, EEItems.CYAN_BOLLOOM_BALLOON),
	PURPLE(DyeColor.PURPLE, EEItems.PURPLE_BOLLOOM_BALLOON),
	BLUE(DyeColor.BLUE, EEItems.BLUE_BOLLOOM_BALLOON),
	BROWN(DyeColor.BROWN, EEItems.BROWN_BOLLOOM_BALLOON),
	GREEN(DyeColor.GREEN, EEItems.GREEN_BOLLOOM_BALLOON),
	RED(DyeColor.RED, EEItems.RED_BOLLOOM_BALLOON),
	BLACK(DyeColor.BLACK, EEItems.BLACK_BOLLOOM_BALLOON),
	DEFAULT(null, EEItems.BOLLOOM_BALLOON);

	@Nullable
	public final DyeColor color;
	public final Supplier<Item> balloonItem;
	public final ResourceLocation texture;

	BalloonColor(@Nullable DyeColor color, Supplier<Item> balloonItem) {
		this.color = color;
		this.balloonItem = balloonItem;
		this.texture = color != null ? new ResourceLocation(EndergeticExpansion.MOD_ID, String.format("textures/entity/balloon/%s_bolloom_balloon.png", color.getName())) : new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/balloon/bolloom_balloon.png");
	}

	public static BalloonColor byOrdinal(int ordinal) {
		for (BalloonColor balloonColor : values()) {
			if (balloonColor.ordinal() == ordinal) {
				return balloonColor;
			}
		}
		return BalloonColor.DEFAULT;
	}

	public static BalloonColor byDyeColor(DyeColor color) {
		for (BalloonColor balloonColor : values()) {
			if (balloonColor.color == color) {
				return balloonColor;
			}
		}
		return BalloonColor.DEFAULT;
	}
}
