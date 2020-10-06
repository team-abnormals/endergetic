package com.minecraftabnormals.endergetic.common;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraftforge.common.ToolType;

public final class EEProperties {
	public static final Block.Properties FRISBLOOM_STEM    = Block.Properties.create(Material.PLANTS, MaterialColor.PURPLE_TERRACOTTA).hardnessAndResistance(0F);
	public static final Block.Properties FRISBLOOM_BUD     = Block.Properties.create(Material.PLANTS, MaterialColor.PURPLE_TERRACOTTA).tickRandomly().hardnessAndResistance(0F);
	
	public static Block.Properties CORROCK_BASE(MaterialColor color, boolean isFullBlock) {
		return isFullBlock ? Block.Properties.create(Material.CORAL, color).hardnessAndResistance(1.5F, 6.0F) : Block.Properties.create(Material.OCEAN_PLANT, color).hardnessAndResistance(0F).doesNotBlockMovement();
	}
	
	public static Block.Properties CORROCK_BASE_GLOWING(MaterialColor color) {
		return Block.Properties.create(Material.OCEAN_PLANT, color).setLightLevel((state) -> 9).hardnessAndResistance(0F).doesNotBlockMovement();
	}
	
	public static Block.Properties POISE_GRASS(boolean isPlant) {
		return !isPlant ? Block.Properties.create(Material.ROCK, EEMaterialColors.POISMOSS).hardnessAndResistance(3.0F, 9.0F).tickRandomly() : Block.Properties.create(Material.FIRE, EEMaterialColors.POISMOSS).sound(SoundType.PLANT).hardnessAndResistance(0F).doesNotBlockMovement();
	}
	
	public static Block.Properties POISE_WOOD_OTHER(boolean ticksRandomly, boolean doesNotBlockMovement) {
		Block.Properties poise = Block.Properties.create(Material.WOOD, EEMaterialColors.POISE_PURPLE).sound(SoundType.WOOD).hardnessAndResistance(2, 10);
		if(ticksRandomly) {
			poise.tickRandomly();
		}
		if(doesNotBlockMovement) {
			poise.doesNotBlockMovement();
		}
		return poise;
	}
	
	public static final Block.Properties POISE_CLUSTER     = Block.Properties.create(Material.ORGANIC, EEMaterialColors.POISE_PINK).notSolid().hardnessAndResistance(0.15F);
	public static final Block.Properties POISE_WOOD        = AbstractBlock.Properties.create(Material.WOOD, EEMaterialColors.POISE_PURPLE).sound(SoundType.WOOD).harvestTool(ToolType.AXE).hardnessAndResistance(2);
	public static final Block.Properties POISE_WOOD_NOT_SOLID = Block.Properties.create(Material.WOOD, EEMaterialColors.POISE_PURPLE).sound(SoundType.WOOD).notSolid().harvestTool(ToolType.AXE).hardnessAndResistance(2, 10);
	public static final Block.Properties POISE_LOG_GLOWING = Block.Properties.create(Material.WOOD, EEMaterialColors.POISE_PURPLE).sound(SoundType.WOOD).harvestTool(ToolType.AXE).hardnessAndResistance(2, 10).setLightLevel((state) -> 15);
	public static final Block.Properties BOOF_BLOCK        = Block.Properties.create(Material.WOOL, MaterialColor.YELLOW_TERRACOTTA).sound(SoundType.CLOTH).hardnessAndResistance(0.85F);
	public static final Block.Properties EUMUS             = Block.Properties.create(Material.EARTH, EEMaterialColors.EUMUS).hardnessAndResistance(0.5F).sound(SoundType.GROUND);
	public static final Block.Properties POISMOSS_EUMUS    = Block.Properties.create(Material.ORGANIC, EEMaterialColors.POISMOSS).tickRandomly().hardnessAndResistance(0.6F).sound(SoundType.PLANT);
	public static final Block.Properties EUMUS_BRICKS      = Block.Properties.create(Material.ROCK, EEMaterialColors.EUMUS).harvestTool(ToolType.PICKAXE).sound(SoundType.STONE).hardnessAndResistance(2, 30);
	public static final Block.Properties MYSTICAL_OBSIDIAN = Block.Properties.create(Material.ROCK, MaterialColor.OBSIDIAN).hardnessAndResistance(-1.0F, 3600000.0F).noDrops();
	public static final Block.Properties ACIDIAN_LANTERN   = Block.Properties.create(Material.ROCK, MaterialColor.OBSIDIAN).hardnessAndResistance(50F, 6000.0F).setLightLevel(state -> 15).harvestLevel(2).harvestTool(ToolType.PICKAXE);
	public static final Block.Properties BOLLOOM_CRATE     = Block.Properties.create(Material.WOOD, MaterialColor.YELLOW_TERRACOTTA).hardnessAndResistance(1.5F).sound(SoundType.WOOD);
	public static Block.Properties PUFFBUG_HIVE(boolean hanger) {
		return !hanger ? Block.Properties.create(Material.ORGANIC, MaterialColor.WOOL).notSolid().doesNotBlockMovement() : Block.Properties.create(Material.WOOD, MaterialColor.WOOL).hardnessAndResistance(2.5F);
	}
}