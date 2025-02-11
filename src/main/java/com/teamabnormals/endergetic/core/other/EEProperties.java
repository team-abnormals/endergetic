package com.teamabnormals.endergetic.core.other;

import com.teamabnormals.blueprint.core.api.WoodTypeRegistryHelper;
import com.teamabnormals.blueprint.core.util.PropertyUtil.WoodSetProperties;
import com.teamabnormals.endergetic.common.block.EetleEggBlock;
import com.teamabnormals.endergetic.common.block.PortaplasmBlock;
import com.teamabnormals.endergetic.core.EndergeticExpansion;
import com.teamabnormals.endergetic.core.registry.EESoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

public final class EEProperties {
	public static final BlockSetType POISE_BLOCK_SET = BlockSetType.register(new BlockSetType(EndergeticExpansion.MOD_ID + ":poise"));
	public static final WoodType POISE_WOOD_TYPE = WoodTypeRegistryHelper.registerWoodType(new WoodType(EndergeticExpansion.MOD_ID + ":poise", POISE_BLOCK_SET));

	public static final WoodSetProperties POISE = WoodSetProperties.builder(MapColor.TERRACOTTA_PURPLE).build();

	public static final Properties POISE_CLUSTER = Properties.of().mapColor(MapColor.COLOR_PINK).noOcclusion().strength(0.15F);
	public static final Properties BOOF_BLOCK = Properties.of().mapColor(MapColor.TERRACOTTA_YELLOW).sound(SoundType.WOOL).strength(0.85F).ignitedByLava();
	public static final Properties EUMUS = Properties.of().mapColor(MapColor.TERRACOTTA_PURPLE).strength(0.5F).sound(SoundType.GRAVEL);
	public static final Properties EUMUS_POISMOSS = Properties.of().mapColor(MapColor.COLOR_PURPLE).randomTicks().strength(0.6F).sound(SoundType.GRASS);
	public static final Properties EUMUS_POISMOSS_PATH = Properties.of().mapColor(MapColor.COLOR_PURPLE).strength(0.6F).sound(SoundType.GRASS).isViewBlocking(EEProperties::blocksVision);
	public static final Properties POISMOSS_PATH = Properties.of().mapColor(MapColor.COLOR_PURPLE).requiresCorrectToolForDrops().strength(3.0F, 9.0F).isViewBlocking(EEProperties::blocksVision);
	public static final Properties EUMUS_BRICKS = Properties.of().mapColor(MapColor.TERRACOTTA_PURPLE).sound(SoundType.STONE).strength(2, 30);
	public static final Properties MYSTICAL_OBSIDIAN = Properties.of().mapColor(MapColor.PODZOL).strength(-1.0F, 3600000.0F).noLootTable();
	public static final Properties ACIDIAN_LANTERN = Properties.of().mapColor(MapColor.PODZOL).strength(50F, 6000.0F).lightLevel(state -> 15).requiresCorrectToolForDrops();
	public static final Properties BOLLOOM_CRATE = Properties.of().mapColor(MapColor.TERRACOTTA_YELLOW).strength(1.5F).sound(SoundType.WOOD).ignitedByLava();
	public static final Properties EETLE_EGG = Properties.of().mapColor(MapColor.COLOR_PURPLE).lightLevel(state -> 7 + state.getValue(EetleEggBlock.SIZE)).strength(1.0F).sound(EESoundEvents.EESoundTypes.EETLE_EGG).pushReaction(PushReaction.DESTROY);
	public static final Properties PORTAPLASM = Properties.of().mapColor(MapColor.COLOR_PURPLE).lightLevel(state -> {
		int phase = state.getValue(PortaplasmBlock.PHASE);
		return phase > 0 || !state.getValue(BlockStateProperties.POWERED) ? phase + 8 : 0;
	}).sound(SoundType.HONEY_BLOCK).noOcclusion();
	public static final Properties INFESTED_CORROCK = Properties.of().mapColor(MapColor.COLOR_PURPLE).lightLevel(state -> 12).randomTicks().strength(1.5F, 6.0F).sound(SoundType.CORAL_BLOCK);
	public static final Properties PETRIFIED_INFESTED_CORROCK = Properties.of().mapColor(MapColor.COLOR_PURPLE).lightLevel(state -> 12).strength(1.5F, 6.0F).sound(SoundType.CORAL_BLOCK);

	public static Properties corrockBase(MapColor color, boolean isFullBlock) {
		return isFullBlock ? Properties.of().mapColor(color).strength(1.5F, 6.0F) : Properties.of().mapColor(color).strength(0F).noCollission().pushReaction(PushReaction.DESTROY);
	}

	public static Properties glowingCorrockBase(MapColor color) {
		return Properties.of().mapColor(color).lightLevel((state) -> 12).strength(0F).noCollission().pushReaction(PushReaction.DESTROY);
	}

	public static Properties poiseGrass(boolean isPlant) {
		return !isPlant ? Properties.of().mapColor(MapColor.COLOR_PURPLE).requiresCorrectToolForDrops().strength(3.0F, 9.0F).randomTicks() : Properties.of().mapColor(MapColor.COLOR_PURPLE).sound(SoundType.GRASS).strength(0F).noCollission().replaceable().pushReaction(PushReaction.DESTROY);
	}

	public static Properties bolloomBud() {
		return Properties.of().mapColor(MapColor.TERRACOTTA_PURPLE).sound(SoundType.WOOD).strength(2, 10);
	}

	public static Properties getPuffBugHive(boolean hive) {
		return !hive ? Properties.of().mapColor(MapColor.WOOL).noOcclusion().noCollission() : Properties.of().mapColor(MapColor.WOOL).requiresCorrectToolForDrops().strength(2.5F);
	}

	private static boolean blocksVision(BlockState state, BlockGetter access, BlockPos pos) {
		return true;
	}
}