package com.minecraftabnormals.endergetic.core.registry;

import com.minecraftabnormals.abnormals_core.core.util.registry.EntitySubRegistryHelper;
import com.minecraftabnormals.endergetic.common.entities.BoofBlockEntity;
import com.minecraftabnormals.endergetic.common.entities.PoiseClusterEntity;
import com.minecraftabnormals.endergetic.common.entities.bolloom.BolloomBalloonEntity;
import com.minecraftabnormals.endergetic.common.entities.bolloom.BolloomFruitEntity;
import com.minecraftabnormals.endergetic.common.entities.bolloom.BolloomKnotEntity;
import com.minecraftabnormals.endergetic.common.entities.booflo.BoofloAdolescentEntity;
import com.minecraftabnormals.endergetic.common.entities.booflo.BoofloBabyEntity;
import com.minecraftabnormals.endergetic.common.entities.booflo.BoofloEntity;
import com.minecraftabnormals.endergetic.common.entities.puffbug.PuffBugEntity;
import com.minecraftabnormals.endergetic.core.EndergeticExpansion;
import net.minecraft.entity.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

import java.util.Random;

@EventBusSubscriber(modid = EndergeticExpansion.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public final class EEEntities {
	public static final EntityClassification END_CREATURE = EntityClassification.create("endergetic:end_creature", "end_creature", 20, false, true, 128);
	private static final EntitySubRegistryHelper HELPER = EndergeticExpansion.REGISTRY_HELPER.getEntitySubHelper();

	public static final RegistryObject<EntityType<PoiseClusterEntity>> POISE_CLUSTER = HELPER.createLivingEntity("poise_cluster", PoiseClusterEntity::new, EntityClassification.MISC, 1F, 1F);
	public static final RegistryObject<EntityType<BolloomFruitEntity>> BOLLOOM_FRUIT = HELPER.createEntity("bolloom_fruit", BolloomFruitEntity::new, BolloomFruitEntity::new, EntityClassification.MISC, 0.5F, 0.5F);
	public static final RegistryObject<EntityType<BoofBlockEntity>> BOOF_BLOCK = HELPER.createEntity("boof_block", BoofBlockEntity::new, BoofBlockEntity::new, EntityClassification.MISC, 1.75F, 1.75F);
	public static final RegistryObject<EntityType<PuffBugEntity>> PUFF_BUG = HELPER.createLivingEntity("puff_bug", PuffBugEntity::new, EntityClassification.CREATURE, 0.3F, 1.15F);
	public static final RegistryObject<EntityType<BolloomBalloonEntity>> BOLLOOM_BALLOON = HELPER.createEntity("bolloom_balloon", BolloomBalloonEntity::new, BolloomBalloonEntity::new, EntityClassification.MISC, 0.5F, 0.5F);
	public static final RegistryObject<EntityType<BolloomKnotEntity>> BOLLOOM_KNOT = HELPER.createEntity("bolloom_knot", BolloomKnotEntity::new, BolloomKnotEntity::new, EntityClassification.MISC, 0.375F, 0.19F);
	public static final RegistryObject<EntityType<BoofloBabyEntity>> BOOFLO_BABY = HELPER.createLivingEntity("booflo_baby", BoofloBabyEntity::new, EntityClassification.CREATURE, 0.375F, 0.325F);
	public static final RegistryObject<EntityType<BoofloAdolescentEntity>> BOOFLO_ADOLESCENT = HELPER.createLivingEntity("booflo_adolescent", BoofloAdolescentEntity::new, EntityClassification.CREATURE, 0.8F, 0.7F);
	public static final RegistryObject<EntityType<BoofloEntity>> BOOFLO = HELPER.createLivingEntity("booflo", BoofloEntity::new, EntityClassification.CREATURE, 1.3F, 1.3F);

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void registerSpawnPlacements(RegistryEvent.Register<EntityType<?>> event) {
		EntitySpawnPlacementRegistry.register(BOOFLO.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING, EEEntities::endIslandCondition);
		EntitySpawnPlacementRegistry.register(BOOFLO_ADOLESCENT.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING, EEEntities::endIslandCondition);
		EntitySpawnPlacementRegistry.register(PUFF_BUG.get(), EntitySpawnPlacementRegistry.PlacementType.NO_RESTRICTIONS, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, EEEntities::endIslandCondition);
	}

	@SubscribeEvent
	public static void registerAttributes(EntityAttributeCreationEvent event) {
		event.put(BOOFLO.get(), BoofloEntity.registerAttributes().build());
		event.put(BOOFLO_ADOLESCENT.get(), BoofloAdolescentEntity.registerAttributes().build());
		event.put(BOOFLO_BABY.get(), BoofloBabyEntity.registerAttributes().build());
		event.put(PUFF_BUG.get(), PuffBugEntity.registerAttributes().build());
		event.put(POISE_CLUSTER.get(), LivingEntity.createLivingAttributes().build());
	}

	private static boolean endIslandCondition(EntityType<? extends CreatureEntity> entityType, IWorld world, SpawnReason spawnReason, BlockPos pos, Random random) {
		return pos.getY() >= 40;
	}
}