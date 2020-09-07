package com.minecraftabnormals.endergetic.core.registry;

import java.util.Random;

import com.minecraftabnormals.endergetic.common.entities.*;
import com.minecraftabnormals.endergetic.common.entities.bolloom.*;
import com.minecraftabnormals.endergetic.common.entities.booflo.*;
import com.minecraftabnormals.endergetic.common.entities.puffbug.*;
import com.minecraftabnormals.endergetic.core.EndergeticExpansion;
import com.minecraftabnormals.endergetic.core.registry.util.EndergeticRegistryHelper;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = EndergeticExpansion.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public final class EEEntities {
	private static final EndergeticRegistryHelper HELPER = EndergeticExpansion.REGISTRY_HELPER;
	
	public static final RegistryObject<EntityType<PoiseClusterEntity>> POISE_CLUSTER = HELPER.createLivingEntity("poise_cluster", PoiseClusterEntity::new, EntityClassification.MISC, 1F, 1F);
	public static final RegistryObject<EntityType<BolloomFruitEntity>> BOLLOOM_FRUIT = HELPER.createEntity("bolloom_fruit", BolloomFruitEntity::new, BolloomFruitEntity::new, EntityClassification.MISC, 0.5F, 0.5F);
	public static final RegistryObject<EntityType<BoofBlockEntity>> BOOF_BLOCK = HELPER.createLivingEntity("boof_block", BoofBlockEntity::new, EntityClassification.MISC, 1.6F, 1.6F);
	public static final RegistryObject<EntityType<PuffBugEntity>> PUFF_BUG = HELPER.createLivingEntity("puff_bug", PuffBugEntity::new, EntityClassification.CREATURE, 0.3F, 1.15F);
	public static final RegistryObject<EntityType<BolloomBalloonEntity>> BOLLOOM_BALLOON = HELPER.createEntity("bolloom_balloon", BolloomBalloonEntity::new, BolloomBalloonEntity::new, EntityClassification.MISC, 0.5F, 0.5F);
	public static final RegistryObject<EntityType<BolloomKnotEntity>> BOLLOOM_KNOT = HELPER.createEntity("bolloom_knot", BolloomKnotEntity::new, BolloomKnotEntity::new, EntityClassification.MISC, 0.375F, 0.19F);
	public static final RegistryObject<EntityType<BoofloBabyEntity>> BOOFLO_BABY = HELPER.createLivingEntity("booflo_baby", BoofloBabyEntity::new, EntityClassification.CREATURE, 0.375F, 0.325F);
	public static final RegistryObject<EntityType<BoofloAdolescentEntity>> BOOFLO_ADOLESCENT = HELPER.createLivingEntity("booflo_adolescent", BoofloAdolescentEntity::new, EntityClassification.CREATURE, 0.8F, 0.7F);
	public static final RegistryObject<EntityType<BoofloEntity>> BOOFLO = HELPER.createLivingEntity("booflo", BoofloEntity::new, EntityClassification.CREATURE, 1.3F, 1.3F);
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void registerEntities(RegistryEvent.Register<EntityType<?>> event) {
		EntitySpawnPlacementRegistry.register(BOOFLO.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING, EEEntities::endIslandCondition);
		EntitySpawnPlacementRegistry.register(BOOFLO_ADOLESCENT.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING, EEEntities::endIslandCondition);
		EntitySpawnPlacementRegistry.register(PUFF_BUG.get(), EntitySpawnPlacementRegistry.PlacementType.NO_RESTRICTIONS, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, EEEntities::endIslandCondition);
	}
	
	private static boolean endIslandCondition(EntityType<? extends CreatureEntity> entityType, IWorld world, SpawnReason spawnReason, BlockPos pos, Random random) {
		return pos.getY() >= 40;
	}
	
	public static class EEEntityClassifications {
		public static final EntityClassification END_CREATURE = EntityClassification.create("endergetic:end_creature", "END_CREATURE", 20, false, true, 128);
	}
}