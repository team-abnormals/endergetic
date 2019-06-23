package endergeticexpansion.common;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;

public class EEProperties {
	public static final Block.Properties FRISBLOOM_STEM    = Block.Properties.create(Material.PLANTS, MaterialColor.PURPLE_TERRACOTTA).hardnessAndResistance(0F);
	public static final Block.Properties FRISBLOOM_BUD     = Block.Properties.create(Material.PLANTS, MaterialColor.PURPLE_TERRACOTTA).tickRandomly().hardnessAndResistance(0F);
	
	public static final Block.Properties CORROCK_BASE(MaterialColor color, boolean isFullBlock) {
		return isFullBlock ? Block.Properties.create(Material.CORAL, color).hardnessAndResistance(1.5F, 6.0F) : Block.Properties.create(Material.OCEAN_PLANT, color).hardnessAndResistance(0F).doesNotBlockMovement();
	}
	
	public static final Block.Properties CORROCK_BASE_GLOWING(MaterialColor color) {
		return Block.Properties.create(Material.OCEAN_PLANT, color).lightValue(4).hardnessAndResistance(0F).doesNotBlockMovement();
	}
	
	@SuppressWarnings("static-access")
	public static final Block.Properties POISE_GRASS(boolean isPlant) {
		return !isPlant ? Block.Properties.create(Material.ROCK, MaterialColor.PURPLE).from(Blocks.END_STONE).tickRandomly() : Block.Properties.create(Material.PLANTS, MaterialColor.PURPLE_TERRACOTTA).sound(SoundType.PLANT).hardnessAndResistance(0F).doesNotBlockMovement();
	}
	
	public static final Block.Properties POISE_CLUSTER     = Block.Properties.create(Material.ORGANIC, MaterialColor.PINK_TERRACOTTA);
	public static final Block.Properties POISE_WOOD        = Block.Properties.create(Material.WOOD, MaterialColor.PURPLE_TERRACOTTA).sound(SoundType.WOOD).hardnessAndResistance(2, 10);
	public static final Block.Properties POISE_LOG_GLOWING = Block.Properties.create(Material.WOOD, MaterialColor.PURPLE_TERRACOTTA).sound(SoundType.WOOD).hardnessAndResistance(2, 10).lightValue(15);
	public static final Block.Properties BOOF_BLOCK        = Block.Properties.create(Material.WOOL, MaterialColor.YELLOW_TERRACOTTA).sound(SoundType.CLOTH).hardnessAndResistance(0.85F);
	
	public static final Block.Properties PUFFBUG_HIVE(boolean hanger) {
		return !hanger ? Block.Properties.create(Material.ORGANIC, MaterialColor.WOOL).doesNotBlockMovement() : Block.Properties.create(Material.WOOD, MaterialColor.WOOL).hardnessAndResistance(2.5F);
	}
	
}
