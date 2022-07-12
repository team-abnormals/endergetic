package com.teamabnormals.endergetic.core.registry;

import com.teamabnormals.endergetic.core.EndergeticExpansion;

import com.teamabnormals.blueprint.core.util.registry.SoundSubRegistryHelper;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.common.util.ForgeSoundType;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = EndergeticExpansion.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class EESounds {
	private static final SoundSubRegistryHelper HELPER = EndergeticExpansion.REGISTRY_HELPER.getSoundSubHelper();

	public static final RegistryObject<SoundEvent> KILOBYTE = HELPER.createSoundEvent("music.record.kilobyte");

	public static final RegistryObject<SoundEvent> POISE_FOREST_LOOP = HELPER.createSoundEvent("ambient.poise_forest.loop");
	public static final RegistryObject<SoundEvent> POISE_FOREST_ADDITIONS = HELPER.createSoundEvent("ambient.poise_forest.additions");
	public static final RegistryObject<SoundEvent> POISE_FOREST_MOOD = HELPER.createSoundEvent("ambient.poise_forest.mood");
	public static final RegistryObject<SoundEvent> POISE_FOREST_MUSIC = HELPER.createSoundEvent("music.end.poise_forest");

	public static final RegistryObject<SoundEvent> CLUSTER_PLACE = HELPER.createSoundEvent("block.cluster.place");
	public static final RegistryObject<SoundEvent> CLUSTER_BREAK = HELPER.createSoundEvent("block.cluster.break");
	public static final RegistryObject<SoundEvent> CLUSTER_STEP = HELPER.createSoundEvent("block.cluster.step");
	public static final RegistryObject<SoundEvent> CLUSTER_HIT = HELPER.createSoundEvent("block.cluster.hit");

	public static final RegistryObject<SoundEvent> POISE_BUSH_AMBIENT = HELPER.createSoundEvent("block.poise_bush.ambient");
	public static final RegistryObject<SoundEvent> POISE_BUSH_AMBIENT_LONG = HELPER.createSoundEvent("block.poise_bush.ambient_long");
	public static final RegistryObject<SoundEvent> POISE_CLUSTER_AMBIENT = HELPER.createSoundEvent("block.poise_cluster.ambient");

	public static final RegistryObject<SoundEvent> EETLE_EGG_BREAK = HELPER.createSoundEvent("block.eetle_egg.break");
	public static final RegistryObject<SoundEvent> EETLE_EGG_STEP = HELPER.createSoundEvent("block.eetle_egg.step");
	public static final RegistryObject<SoundEvent> EETLE_EGG_PLACE = HELPER.createSoundEvent("block.eetle_egg.place");
	public static final RegistryObject<SoundEvent> EETLE_EGG_HIT = HELPER.createSoundEvent("block.eetle_egg.hit");
	public static final RegistryObject<SoundEvent> EETLE_EGG_FALL = HELPER.createSoundEvent("block.eetle_egg.fall");
	public static final RegistryObject<SoundEvent> EETLE_EGG_AMBIENT = HELPER.createSoundEvent("block.eetle_egg.ambient");

	public static final RegistryObject<SoundEvent> BOOFLO_VEST_INFLATE = HELPER.createSoundEvent("item.booflo_vest.inflate");

	public static final RegistryObject<SoundEvent> BOOFLO_CROAK = HELPER.createSoundEvent("entity.booflo.croak");
	public static final RegistryObject<SoundEvent> BOOFLO_GROWL = HELPER.createSoundEvent("entity.booflo.growl");
	public static final RegistryObject<SoundEvent> BOOFLO_HOP = HELPER.createSoundEvent("entity.booflo.hop");
	public static final RegistryObject<SoundEvent> BOOFLO_HOP_LAND = HELPER.createSoundEvent("entity.booflo.hop_land");
	public static final RegistryObject<SoundEvent> BOOFLO_SLAM = HELPER.createSoundEvent("entity.booflo.slam");
	public static final RegistryObject<SoundEvent> BOOFLO_INFLATE = HELPER.createSoundEvent("entity.booflo.inflate");
	public static final RegistryObject<SoundEvent> BOOFLO_DEFLATE = HELPER.createSoundEvent("entity.booflo.deflate");
	public static final RegistryObject<SoundEvent> BOOFLO_HURT = HELPER.createSoundEvent("entity.booflo.hurt");
	public static final RegistryObject<SoundEvent> BOOFLO_DEATH = HELPER.createSoundEvent("entity.booflo.death");

	public static final RegistryObject<SoundEvent> PUFFBUG_PUFF = HELPER.createSoundEvent("entity.puffbug.puff");
	public static final RegistryObject<SoundEvent> PUFFBUG_TELEPORT_TO = HELPER.createSoundEvent("entity.puffbug.teleport_to");
	public static final RegistryObject<SoundEvent> PUFFBUG_TELEPORT_FROM = HELPER.createSoundEvent("entity.puffbug.teleport_from");
	public static final RegistryObject<SoundEvent> PUFFBUG_SLEEP = HELPER.createSoundEvent("entity.puffbug.sleep");
	public static final RegistryObject<SoundEvent> PUFFBUG_CREATE_ITEM = HELPER.createSoundEvent("entity.puffbug.create_item");
	public static final RegistryObject<SoundEvent> PUFFBUG_LAUNCH = HELPER.createSoundEvent("entity.puffbug.launch");
	public static final RegistryObject<SoundEvent> PUFFBUG_LAND = HELPER.createSoundEvent("entity.puffbug.land");
	public static final RegistryObject<SoundEvent> PUFFBUG_HURT = HELPER.createSoundEvent("entity.puffbug.hurt");
	public static final RegistryObject<SoundEvent> PUFFBUG_DEATH = HELPER.createSoundEvent("entity.puffbug.death");

	public static final RegistryObject<SoundEvent> LEETLE_AMBIENT = HELPER.createSoundEvent("entity.eetle.leetle_ambient");
	public static final RegistryObject<SoundEvent> LEETLE_HURT = HELPER.createSoundEvent("entity.eetle.leetle_hurt");
	public static final RegistryObject<SoundEvent> LEETLE_DEATH = HELPER.createSoundEvent("entity.eetle.leetle_death");
	public static final RegistryObject<SoundEvent> LEETLE_STEP = HELPER.createSoundEvent("entity.eetle.leetle_step");
	public static final RegistryObject<SoundEvent> LEETLE_TRANSFORM = HELPER.createSoundEvent("entity.eetle.leetle_transform");

	public static final RegistryObject<SoundEvent> BOOF_BLOCK_INFLATE = HELPER.createSoundEvent("entity.boof_block.inflate");

	public static final RegistryObject<SoundEvent> SMALL_END_ISLANDS_LOOP = HELPER.createSoundEvent("ambient.small_end_islands.loop");
	public static final RegistryObject<SoundEvent> SMALL_END_ISLANDS_ADDITIONS = HELPER.createSoundEvent("ambient.small_end_islands.additions");

	public static class EESoundTypes {
		public static final SoundType CLUSTER = new ForgeSoundType(1.0F, 1.0F, CLUSTER_BREAK, CLUSTER_STEP, CLUSTER_PLACE, CLUSTER_HIT, CLUSTER_PLACE);
		public static final SoundType EETLE_EGG = new ForgeSoundType(1.0F, 1.0F, EETLE_EGG_BREAK, EETLE_EGG_STEP, EETLE_EGG_PLACE, EETLE_EGG_HIT, EETLE_EGG_FALL);
	}
}