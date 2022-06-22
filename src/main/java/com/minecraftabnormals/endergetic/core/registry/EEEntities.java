package com.minecraftabnormals.endergetic.core.registry;

import java.util.Random;

import com.minecraftabnormals.endergetic.common.entities.*;
import com.minecraftabnormals.endergetic.common.entities.bolloom.*;
import com.minecraftabnormals.endergetic.common.entities.booflo.*;
import com.minecraftabnormals.endergetic.common.entities.eetle.*;
import com.minecraftabnormals.endergetic.common.entities.puffbug.*;
import com.minecraftabnormals.endergetic.common.entities.purpoid.PurpoidEntity;
import com.minecraftabnormals.endergetic.core.EndergeticExpansion;

import com.minecraftabnormals.endergetic.core.registry.util.EndergeticEntitySubRegistryHelper;
import net.minecraft.block.Block;
import net.minecraft.entity.*;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = EndergeticExpansion.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public final class EEEntities {
	public static final EntityClassification END_CREATURE = EntityClassification.create("endergetic:end_creature", "end_creature", 20, false, true, 128);
	private static final EndergeticEntitySubRegistryHelper HELPER = EndergeticExpansion.REGISTRY_HELPER.getEntitySubHelper();

	public static final RegistryObject<EntityType<PoiseClusterEntity>> POISE_CLUSTER = HELPER.createLivingEntity("poise_cluster", PoiseClusterEntity::new, EntityClassification.MISC, 1F, 1F);
	public static final RegistryObject<EntityType<BolloomFruitEntity>> BOLLOOM_FRUIT = HELPER.createEntity("bolloom_fruit", BolloomFruitEntity::new, BolloomFruitEntity::new, EntityClassification.MISC, 0.5F, 0.5F);
	public static final RegistryObject<EntityType<BoofBlockEntity>> BOOF_BLOCK = HELPER.createEntity("boof_block", BoofBlockEntity::new, BoofBlockEntity::new, EntityClassification.MISC, 1.75F, 1.75F);
	public static final RegistryObject<EntityType<PuffBugEntity>> PUFF_BUG = HELPER.createLivingEntity("puff_bug", PuffBugEntity::new, EntityClassification.CREATURE, 0.3F, 1.15F);
	public static final RegistryObject<EntityType<BolloomBalloonEntity>> BOLLOOM_BALLOON = HELPER.createEntity("bolloom_balloon", BolloomBalloonEntity::new, BolloomBalloonEntity::new, EntityClassification.MISC, 0.5F, 0.5F);
	public static final RegistryObject<EntityType<BolloomKnotEntity>> BOLLOOM_KNOT = HELPER.createEntity("bolloom_knot", BolloomKnotEntity::new, BolloomKnotEntity::new, EntityClassification.MISC, 0.375F, 0.19F);
	public static final RegistryObject<EntityType<BoofloBabyEntity>> BOOFLO_BABY = HELPER.createLivingEntity("booflo_baby", BoofloBabyEntity::new, EntityClassification.CREATURE, 0.375F, 0.325F);
	public static final RegistryObject<EntityType<BoofloAdolescentEntity>> BOOFLO_ADOLESCENT = HELPER.createLivingEntity("booflo_adolescent", BoofloAdolescentEntity::new, EntityClassification.CREATURE, 0.8F, 0.7F);
	public static final RegistryObject<EntityType<BoofloEntity>> BOOFLO = HELPER.createLivingEntity("booflo", BoofloEntity::new, EntityClassification.CREATURE, 1.3F, 1.3F);
	public static final RegistryObject<EntityType<ChargerEetleEntity>> CHARGER_EETLE = HELPER.createLivingEntity("charger_eetle", ChargerEetleEntity::new, EntityClassification.MONSTER, 1.05F, 0.85F);
	public static final RegistryObject<EntityType<GliderEetleEntity>> GLIDER_EETLE = HELPER.createLivingEntity("glider_eetle", GliderEetleEntity::new, EntityClassification.MONSTER, 1.05F, 0.85F);
	public static final RegistryObject<EntityType<BroodEetleEntity>> BROOD_EETLE = HELPER.createLivingEntity("brood_eetle", BroodEetleEntity::new, EntityClassification.MONSTER, 3.4375F, 2.125F);
	public static final RegistryObject<EntityType<EetleEggEntity>> EETLE_EGG = HELPER.createEntity("eetle_egg", EetleEggEntity::new, EetleEggEntity::new, EntityClassification.MISC, 0.98F, 0.98F);
	public static final RegistryObject<EntityType<BroodEggSackEntity>> BROOD_EGG_SACK = HELPER.createUnsummonableEntity("brood_egg_sack", BroodEggSackEntity::new, BroodEggSackEntity::new, EntityClassification.MISC, 1.25F, 1.25F);
	public static final RegistryObject<EntityType<PurpoidEntity>> PURPOID = HELPER.createLivingEntity("purpoid", PurpoidEntity::new, EntityClassification.CREATURE, 1.0F, 1.0F);

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void registerSpawnPlacements(RegistryEvent.Register<EntityType<?>> event) {
		EntitySpawnPlacementRegistry.register(BOOFLO.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING, EEEntities::endIslandCondition);
		EntitySpawnPlacementRegistry.register(BOOFLO_ADOLESCENT.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING, EEEntities::endIslandCondition);
		EntitySpawnPlacementRegistry.register(PUFF_BUG.get(), EntitySpawnPlacementRegistry.PlacementType.NO_RESTRICTIONS, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, EEEntities::endIslandCondition);
		EntitySpawnPlacementRegistry.register(CHARGER_EETLE.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, EEEntities::eetleCondition);
		EntitySpawnPlacementRegistry.register(GLIDER_EETLE.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, EEEntities::eetleCondition);
	}

	@SubscribeEvent
	public static void onEntityAttributesCreated(EntityAttributeCreationEvent event) {
		event.put(BOOFLO.get(), BoofloEntity.registerAttributes().build());
		event.put(BOOFLO_ADOLESCENT.get(), BoofloAdolescentEntity.registerAttributes().build());
		event.put(BOOFLO_BABY.get(), BoofloBabyEntity.registerAttributes().build());
		event.put(PUFF_BUG.get(), PuffBugEntity.registerAttributes().build());
		event.put(POISE_CLUSTER.get(), LivingEntity.createLivingAttributes().build());
		event.put(CHARGER_EETLE.get(), ChargerEetleEntity.registerAttributes().build());
		event.put(GLIDER_EETLE.get(), GliderEetleEntity.registerAttributes().build());
		event.put(BROOD_EETLE.get(), BroodEetleEntity.registerAttributes().build());
		event.put(PURPOID.get(), PurpoidEntity.registerAttributes().build());
	}

	private static boolean eetleCondition(EntityType<? extends MonsterEntity> entityType, IServerWorld world, SpawnReason spawnReason, BlockPos pos, Random random) {
		if (MonsterEntity.checkMonsterSpawnRules(entityType, world, spawnReason, pos, random) || isInfestedCorrockNearby(world, pos)) {
			BlockPos down = pos.below();
			Block downBlock = world.getBlockState(down).getBlock();
			if (downBlock == EEBlocks.CORROCK_END_BLOCK.get() || downBlock == EEBlocks.EUMUS.get() || downBlock == EEBlocks.INFESTED_CORROCK.get()) {
				return true;
			}
			for (Direction direction : Direction.Plane.HORIZONTAL) {
				Block offsetBlock = world.getBlockState(down.relative(direction)).getBlock();
				if (offsetBlock == EEBlocks.CORROCK_END_BLOCK.get() || offsetBlock == EEBlocks.EUMUS.get()) {
					return true;
				}
			}
		}
		return false;
	}

	private static boolean isInfestedCorrockNearby(IServerWorld world, BlockPos pos) {
		int radius = 1;
		BlockPos.Mutable mutable = new BlockPos.Mutable();
		Block infestedCorrock = EEBlocks.INFESTED_CORROCK.get();
		for (int x = -radius; x <= radius; x++) {
			for (int y = -radius; y <= radius; y++) {
				for (int z = -radius; z <= radius; z++) {
					if (world.getBlockState(mutable.setWithOffset(pos, x, y, z)).getBlock() == infestedCorrock) {
						return true;
					}
				}
			}
		}
		return false;
	}

	private static boolean endIslandCondition(EntityType<? extends CreatureEntity> entityType, IWorld world, SpawnReason spawnReason, BlockPos pos, Random random) {
		return pos.getY() >= 40;
	}
}