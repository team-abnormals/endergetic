package endergeticexpansion.common;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraftforge.common.ToolType;

public class EEProperties {
	public static final Block.Properties FRISBLOOM_STEM    = Block.Properties.create(Material.PLANTS, MaterialColor.PURPLE_TERRACOTTA).hardnessAndResistance(0F);
	public static final Block.Properties FRISBLOOM_BUD     = Block.Properties.create(Material.PLANTS, MaterialColor.PURPLE_TERRACOTTA).tickRandomly().hardnessAndResistance(0F);
	
	public static final Block.Properties CORROCK_BASE(MaterialColor color, boolean isFullBlock) {
		return isFullBlock ? Block.Properties.create(Material.CORAL, color).hardnessAndResistance(1.5F, 6.0F) : Block.Properties.create(Material.OCEAN_PLANT, color).hardnessAndResistance(0F).doesNotBlockMovement();
	}
	
	public static final Block.Properties CORROCK_BASE_GLOWING(MaterialColor color) {
		return Block.Properties.create(Material.OCEAN_PLANT, color).lightValue(4).hardnessAndResistance(0F).doesNotBlockMovement();
	}
	
	public static final Block.Properties POISE_GRASS(boolean isPlant) {
		//Fire you say? Well trust me its safe
		return !isPlant ? Block.Properties.create(Material.ROCK, EEMaterialColors.POISMOSS).hardnessAndResistance(3.0F, 9.0F).tickRandomly() : Block.Properties.create(Material.FIRE, EEMaterialColors.POISMOSS).sound(SoundType.PLANT).hardnessAndResistance(0F).doesNotBlockMovement();
	}
	
	public static final Block.Properties POISE_WOOD_OTHER(boolean ticksRandomly, boolean doesNotBlockMovement) {
		Block.Properties poise = Block.Properties.create(Material.WOOD, EEMaterialColors.POISE_PURPLE).sound(SoundType.WOOD).hardnessAndResistance(2, 10);
		if(ticksRandomly) {
			poise.tickRandomly();
		}
		if(doesNotBlockMovement) {
			poise.doesNotBlockMovement();
		}
		return poise;
	}
	
	public static final Block.Properties POISE_CLUSTER     = Block.Properties.create(Material.ORGANIC, EEMaterialColors.POISE_PINK);
	public static final Block.Properties POISE_WOOD        = Block.Properties.create(Material.WOOD, EEMaterialColors.POISE_PURPLE).sound(SoundType.WOOD).harvestTool(ToolType.AXE).hardnessAndResistance(2, 10);
	public static final Block.Properties POISE_LOG_GLOWING = Block.Properties.create(Material.WOOD, EEMaterialColors.POISE_PURPLE).sound(SoundType.WOOD).harvestTool(ToolType.AXE).hardnessAndResistance(2, 10).lightValue(15);
	public static final Block.Properties BOOF_BLOCK        = Block.Properties.create(Material.WOOL, MaterialColor.YELLOW_TERRACOTTA).sound(SoundType.CLOTH).hardnessAndResistance(0.85F);
	public static final Block.Properties EUMUS             = Block.Properties.create(Material.EARTH, EEMaterialColors.EUMUS).hardnessAndResistance(0.5F).sound(SoundType.GROUND);
	public static final Block.Properties POISMOSS_EUMUS    = Block.Properties.create(Material.ORGANIC, EEMaterialColors.POISMOSS).tickRandomly().hardnessAndResistance(0.6F).sound(SoundType.PLANT);
	public static final Block.Properties EUMUS_BRICKS      = Block.Properties.create(Material.ROCK, EEMaterialColors.EUMUS).harvestTool(ToolType.PICKAXE).sound(SoundType.STONE).hardnessAndResistance(2, 30);
	public static final Block.Properties MYSTICAL_OBSIDIAN = Block.Properties.create(Material.ROCK, MaterialColor.OBSIDIAN).hardnessAndResistance(-1.0F, 3600000.0F).noDrops();
	public static final Block.Properties ACIDIAN_LANTERN   = Block.Properties.create(Material.ROCK, MaterialColor.OBSIDIAN).hardnessAndResistance(50F, 6000.0F).harvestLevel(2).harvestTool(ToolType.PICKAXE);
	
	public static final Block.Properties PUFFBUG_HIVE(boolean hanger) {
		return !hanger ? Block.Properties.create(Material.ORGANIC, MaterialColor.WOOL).doesNotBlockMovement() : Block.Properties.create(Material.WOOD, MaterialColor.WOOL).hardnessAndResistance(2.5F);
	}
	
}
