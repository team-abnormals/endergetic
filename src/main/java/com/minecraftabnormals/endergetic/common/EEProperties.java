package com.minecraftabnormals.endergetic.common;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraftforge.common.ToolType;

public final class EEProperties {
	public static final AbstractBlock.Properties FRISBLOOM_STEM    = AbstractBlock.Properties.create(Material.PLANTS, MaterialColor.PURPLE_TERRACOTTA).hardnessAndResistance(0F);
	public static final AbstractBlock.Properties FRISBLOOM_BUD     = AbstractBlock.Properties.create(Material.PLANTS, MaterialColor.PURPLE_TERRACOTTA).tickRandomly().hardnessAndResistance(0F);
	public static final AbstractBlock.Properties POISE_CLUSTER     = AbstractBlock.Properties.create(Material.ORGANIC, EEMaterialColors.POISE_PINK).notSolid().hardnessAndResistance(0.15F);
	public static final AbstractBlock.Properties POISE_WOOD        = AbstractBlock.Properties.create(Material.WOOD, EEMaterialColors.POISE_PURPLE).sound(SoundType.WOOD).harvestTool(ToolType.AXE).hardnessAndResistance(2);
	public static final AbstractBlock.Properties POISE_WOOD_NOT_SOLID = AbstractBlock.Properties.create(Material.WOOD, EEMaterialColors.POISE_PURPLE).sound(SoundType.WOOD).notSolid().harvestTool(ToolType.AXE).hardnessAndResistance(2, 10);
	public static final AbstractBlock.Properties POISE_LOG_GLOWING = AbstractBlock.Properties.create(Material.WOOD, EEMaterialColors.POISE_PURPLE).sound(SoundType.WOOD).harvestTool(ToolType.AXE).hardnessAndResistance(2, 10).setLightLevel((state) -> 15);
	public static final AbstractBlock.Properties BOOF_BLOCK        = AbstractBlock.Properties.create(Material.WOOL, MaterialColor.YELLOW_TERRACOTTA).sound(SoundType.CLOTH).hardnessAndResistance(0.85F);
	public static final AbstractBlock.Properties EUMUS             = AbstractBlock.Properties.create(Material.EARTH, EEMaterialColors.EUMUS).hardnessAndResistance(0.5F).sound(SoundType.GROUND);
	public static final AbstractBlock.Properties POISMOSS_EUMUS    = AbstractBlock.Properties.create(Material.ORGANIC, EEMaterialColors.POISMOSS).tickRandomly().hardnessAndResistance(0.6F).sound(SoundType.PLANT);
	public static final AbstractBlock.Properties EUMUS_BRICKS      = AbstractBlock.Properties.create(Material.ROCK, EEMaterialColors.EUMUS).harvestTool(ToolType.PICKAXE).sound(SoundType.STONE).hardnessAndResistance(2, 30);
	public static final AbstractBlock.Properties MYSTICAL_OBSIDIAN = AbstractBlock.Properties.create(Material.ROCK, MaterialColor.OBSIDIAN).hardnessAndResistance(-1.0F, 3600000.0F).noDrops();
	public static final AbstractBlock.Properties ACIDIAN_LANTERN   = AbstractBlock.Properties.create(Material.ROCK, MaterialColor.OBSIDIAN).hardnessAndResistance(50F, 6000.0F).setLightLevel(state -> 15).harvestLevel(2).harvestTool(ToolType.PICKAXE);
	public static final AbstractBlock.Properties BOLLOOM_CRATE     = AbstractBlock.Properties.create(Material.WOOD, MaterialColor.YELLOW_TERRACOTTA).hardnessAndResistance(1.5F).sound(SoundType.WOOD);

	public static AbstractBlock.Properties CORROCK_BASE(MaterialColor color, boolean isFullBlock) {
		return isFullBlock ? AbstractBlock.Properties.create(Material.CORAL, color).hardnessAndResistance(1.5F, 6.0F) : AbstractBlock.Properties.create(Material.OCEAN_PLANT, color).hardnessAndResistance(0F).doesNotBlockMovement();
	}

	public static AbstractBlock.Properties CORROCK_BASE_GLOWING(MaterialColor color) {
		return AbstractBlock.Properties.create(Material.OCEAN_PLANT, color).setLightLevel((state) -> 9).hardnessAndResistance(0F).doesNotBlockMovement();
	}

	public static AbstractBlock.Properties POISE_GRASS(boolean isPlant) {
		return !isPlant ? AbstractBlock.Properties.create(Material.ROCK, EEMaterialColors.POISMOSS).hardnessAndResistance(3.0F, 9.0F).tickRandomly() : AbstractBlock.Properties.create(Material.FIRE, EEMaterialColors.POISMOSS).sound(SoundType.PLANT).hardnessAndResistance(0F).doesNotBlockMovement();
	}

	public static AbstractBlock.Properties POISE_WOOD_OTHER(boolean ticksRandomly, boolean doesNotBlockMovement) {
		Block.Properties poise = AbstractBlock.Properties.create(Material.WOOD, EEMaterialColors.POISE_PURPLE).sound(SoundType.WOOD).hardnessAndResistance(2, 10);
		if(ticksRandomly) {
			poise.tickRandomly();
		}
		if(doesNotBlockMovement) {
			poise.doesNotBlockMovement();
		}
		return poise;
	}

	public static AbstractBlock.Properties PUFFBUG_HIVE(boolean hanger) {
		return !hanger ? AbstractBlock.Properties.create(Material.ORGANIC, MaterialColor.WOOL).notSolid().doesNotBlockMovement() : AbstractBlock.Properties.create(Material.WOOD, MaterialColor.WOOL).hardnessAndResistance(2.5F);
	}
}