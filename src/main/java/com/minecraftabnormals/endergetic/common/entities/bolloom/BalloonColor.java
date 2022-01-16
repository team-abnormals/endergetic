package com.minecraftabnormals.endergetic.common.entities.bolloom;

import com.minecraftabnormals.endergetic.core.EndergeticExpansion;
import com.minecraftabnormals.endergetic.core.registry.EEItems;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public enum BalloonColor {
	WHITE(DyeColor.WHITE, EEItems.BOLLOOM_BALLOON_WHITE),
	ORANGE(DyeColor.ORANGE, EEItems.BOLLOOM_BALLOON_ORANGE),
	MAGENTA(DyeColor.MAGENTA, EEItems.BOLLOOM_BALLOON_MAGENTA),
	LIGHT_BLUE(DyeColor.LIGHT_BLUE, EEItems.BOLLOOM_BALLOON_LIGHT_BLUE),
	YELLOW(DyeColor.YELLOW, EEItems.BOLLOOM_BALLOON_YELLOW),
	LIME(DyeColor.LIME, EEItems.BOLLOOM_BALLOON_LIME),
	PINK(DyeColor.PINK, EEItems.BOLLOOM_BALLOON_PINK),
	GRAY(DyeColor.GRAY, EEItems.BOLLOOM_BALLOON_GRAY),
	LIGHT_GRAY(DyeColor.LIGHT_GRAY, EEItems.BOLLOOM_BALLOON_LIGHT_GRAY),
	CYAN(DyeColor.CYAN, EEItems.BOLLOOM_BALLOON_CYAN),
	PURPLE(DyeColor.PURPLE, EEItems.BOLLOOM_BALLOON_PURPLE),
	BLUE(DyeColor.BLUE, EEItems.BOLLOOM_BALLOON_BLUE),
	BROWN(DyeColor.BROWN, EEItems.BOLLOOM_BALLOON_BROWN),
	GREEN(DyeColor.GREEN, EEItems.BOLLOOM_BALLOON_GREEN),
	RED(DyeColor.RED, EEItems.BOLLOOM_BALLOON_RED),
	BLACK(DyeColor.BLACK, EEItems.BOLLOOM_BALLOON_BLACK),
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
