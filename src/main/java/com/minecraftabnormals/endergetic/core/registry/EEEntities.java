package com.minecraftabnormals.endergetic.core.registry;

import java.util.Random;

import com.minecraftabnormals.abnormals_core.core.util.registry.EntitySubRegistryHelper;
import com.minecraftabnormals.endergetic.common.entities.*;
import com.minecraftabnormals.endergetic.common.entities.bolloom.*;
import com.minecraftabnormals.endergetic.common.entities.booflo.*;
import com.minecraftabnormals.endergetic.common.entities.eetle.*;
import com.minecraftabnormals.endergetic.common.entities.puffbug.*;
import com.minecraftabnormals.endergetic.core.EndergeticExpansion;

import net.minecraft.block.Block;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

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
	public static final RegistryObject<EntityType<ChargerEetleEntity>> CHARGER_EETLE = HELPER.createLivingEntity("charger_eetle", ChargerEetleEntity::new, EntityClassification.MONSTER, 1.0F, 0.85F);

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void registerSpawnPlacements(RegistryEvent.Register<EntityType<?>> event) {
		EntitySpawnPlacementRegistry.register(BOOFLO.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING, EEEntities::endIslandCondition);
		EntitySpawnPlacementRegistry.register(BOOFLO_ADOLESCENT.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING, EEEntities::endIslandCondition);
		EntitySpawnPlacementRegistry.register(PUFF_BUG.get(), EntitySpawnPlacementRegistry.PlacementType.NO_RESTRICTIONS, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, EEEntities::endIslandCondition);
		EntitySpawnPlacementRegistry.register(CHARGER_EETLE.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, EEEntities::eetleCondition);
	}

	private static boolean eetleCondition(EntityType<? extends MonsterEntity> entityType, IServerWorld world, SpawnReason spawnReason, BlockPos pos, Random random) {
		if (MonsterEntity.canMonsterSpawnInLight(entityType, world, spawnReason, pos, random)) {
			BlockPos down = pos.down();
			Block downBlock = world.getBlockState(down).getBlock();
			if (downBlock == EEBlocks.CORROCK_END_BLOCK.get() || downBlock == EEBlocks.EUMUS.get()) {
				return true;
			}
			for (Direction direction : Direction.Plane.HORIZONTAL) {
				Block offsetBlock = world.getBlockState(down.offset(direction)).getBlock();
				if (offsetBlock == EEBlocks.CORROCK_END_BLOCK.get() || offsetBlock == EEBlocks.EUMUS.get()) {
					return true;
				}
			}
		}
		return false;
	}

	private static boolean endIslandCondition(EntityType<? extends CreatureEntity> entityType, IWorld world, SpawnReason spawnReason, BlockPos pos, Random random) {
		return pos.getY() >= 40;
	}
}