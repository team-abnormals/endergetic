package com.minecraftabnormals.endergetic.common;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraftforge.common.ToolType;

public final class EEProperties {
	public static final AbstractBlock.Properties FRISBLOOM_STEM = AbstractBlock.Properties.of(Material.PLANT, MaterialColor.TERRACOTTA_PURPLE).strength(0F);
	public static final AbstractBlock.Properties FRISBLOOM_BUD = AbstractBlock.Properties.of(Material.PLANT, MaterialColor.TERRACOTTA_PURPLE).randomTicks().strength(0F);
	public static final AbstractBlock.Properties POISE_CLUSTER = AbstractBlock.Properties.of(Material.GRASS, MaterialColor.COLOR_PINK).noOcclusion().strength(0.15F);
	public static final AbstractBlock.Properties POISE_WOOD = AbstractBlock.Properties.of(Material.WOOD, MaterialColor.TERRACOTTA_PURPLE).sound(SoundType.WOOD).harvestTool(ToolType.AXE).strength(2);
	public static final AbstractBlock.Properties POISE_WOOD_NOT_SOLID = AbstractBlock.Properties.of(Material.WOOD, MaterialColor.TERRACOTTA_PURPLE).sound(SoundType.WOOD).noOcclusion().harvestTool(ToolType.AXE).strength(2, 10);
	public static final AbstractBlock.Properties POISE_LOG_GLOWING = AbstractBlock.Properties.of(Material.WOOD, MaterialColor.TERRACOTTA_PURPLE).sound(SoundType.WOOD).harvestTool(ToolType.AXE).strength(2, 10).lightLevel((state) -> 15);
	public static final AbstractBlock.Properties BOOF_BLOCK = AbstractBlock.Properties.of(Material.WOOL, MaterialColor.TERRACOTTA_YELLOW).sound(SoundType.WOOL).strength(0.85F);
	public static final AbstractBlock.Properties EUMUS = AbstractBlock.Properties.of(Material.DIRT, MaterialColor.TERRACOTTA_PURPLE).strength(0.5F).sound(SoundType.GRAVEL);
	public static final AbstractBlock.Properties POISMOSS_EUMUS = AbstractBlock.Properties.of(Material.GRASS, MaterialColor.COLOR_PURPLE).randomTicks().strength(0.6F).sound(SoundType.GRASS);
	public static final AbstractBlock.Properties EUMUS_BRICKS = AbstractBlock.Properties.of(Material.STONE, MaterialColor.TERRACOTTA_PURPLE).harvestTool(ToolType.PICKAXE).sound(SoundType.STONE).strength(2, 30);
	public static final AbstractBlock.Properties MYSTICAL_OBSIDIAN = AbstractBlock.Properties.of(Material.STONE, MaterialColor.PODZOL).strength(-1.0F, 3600000.0F).noDrops();
	public static final AbstractBlock.Properties ACIDIAN_LANTERN = AbstractBlock.Properties.of(Material.STONE, MaterialColor.PODZOL).strength(50F, 6000.0F).lightLevel(state -> 15).harvestLevel(2).harvestTool(ToolType.PICKAXE);
	public static final AbstractBlock.Properties BOLLOOM_CRATE = AbstractBlock.Properties.of(Material.WOOD, MaterialColor.TERRACOTTA_YELLOW).strength(1.5F).sound(SoundType.WOOD);

	public static AbstractBlock.Properties getCorrockBase(MaterialColor color, boolean isFullBlock) {
		return isFullBlock ? AbstractBlock.Properties.of(Material.CORAL, color).strength(1.5F, 6.0F) : AbstractBlock.Properties.of(Material.WATER_PLANT, color).strength(0F).noCollission();
	}

	public static AbstractBlock.Properties getGlowingCorrockBase(MaterialColor color) {
		return AbstractBlock.Properties.of(Material.WATER_PLANT, color).lightLevel((state) -> 9).strength(0F).noCollission();
	}

	public static AbstractBlock.Properties getPoiseGrass(boolean isPlant) {
		return !isPlant ? AbstractBlock.Properties.of(Material.STONE, MaterialColor.COLOR_PURPLE).strength(3.0F, 9.0F).randomTicks() : AbstractBlock.Properties.of(Material.REPLACEABLE_PLANT, MaterialColor.COLOR_PURPLE).sound(SoundType.GRASS).strength(0F).noCollission();
	}

	public static AbstractBlock.Properties getPoiseWood(boolean ticksRandomly, boolean doesNotBlockMovement) {
		Block.Properties poise = AbstractBlock.Properties.of(Material.WOOD, MaterialColor.TERRACOTTA_PURPLE).sound(SoundType.WOOD).strength(2, 10);
		if (ticksRandomly) {
			poise.randomTicks();
		}
		if (doesNotBlockMovement) {
			poise.noCollission();
		}
		return poise;
	}

	public static AbstractBlock.Properties getPuffBugHive(boolean hanger) {
		return !hanger ? AbstractBlock.Properties.of(Material.GRASS, MaterialColor.WOOL).noOcclusion().noCollission() : AbstractBlock.Properties.of(Material.WOOD, MaterialColor.WOOL).strength(2.5F);
	}
}