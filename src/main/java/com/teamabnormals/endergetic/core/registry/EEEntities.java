package com.teamabnormals.endergetic.core.registry;

import com.teamabnormals.endergetic.common.entities.*;
import com.teamabnormals.endergetic.common.entities.bolloom.*;
import com.teamabnormals.endergetic.common.entities.booflo.*;
import com.teamabnormals.endergetic.common.entities.eetle.*;
import com.teamabnormals.endergetic.common.entities.puffbug.*;
import com.teamabnormals.endergetic.common.entities.purpoid.PurpoidEntity;
import com.teamabnormals.endergetic.core.EndergeticExpansion;

import com.teamabnormals.endergetic.core.registry.util.EndergeticEntitySubRegistryHelper;
import net.minecraft.core.Registry;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryObject;

@EventBusSubscriber(modid = EndergeticExpansion.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public final class EEEntities {
	public static final MobCategory END_CREATURE = MobCategory.create("endergetic:end_creature", "end_creature", 20, false, true, 128);
	public static final EndergeticEntitySubRegistryHelper HELPER = EndergeticExpansion.REGISTRY_HELPER.getEntitySubHelper();

	public static final RegistryObject<EntityType<PoiseClusterEntity>> POISE_CLUSTER = HELPER.createLivingEntity("poise_cluster", PoiseClusterEntity::new, MobCategory.MISC, 1F, 1F);
	public static final RegistryObject<EntityType<BolloomFruitEntity>> BOLLOOM_FRUIT = HELPER.createEntity("bolloom_fruit", BolloomFruitEntity::new, BolloomFruitEntity::new, MobCategory.MISC, 0.5F, 0.5F);
	public static final RegistryObject<EntityType<BoofBlockEntity>> BOOF_BLOCK = HELPER.createEntity("boof_block", BoofBlockEntity::new, BoofBlockEntity::new, MobCategory.MISC, 1.75F, 1.75F);
	public static final RegistryObject<EntityType<PuffBugEntity>> PUFF_BUG = HELPER.createLivingEntity("puff_bug", PuffBugEntity::new, MobCategory.CREATURE, 0.3F, 1.15F);
	public static final RegistryObject<EntityType<BolloomBalloonEntity>> BOLLOOM_BALLOON = HELPER.createEntity("bolloom_balloon", BolloomBalloonEntity::new, BolloomBalloonEntity::new, MobCategory.MISC, 0.5F, 0.5F);
	public static final RegistryObject<EntityType<BolloomKnotEntity>> BOLLOOM_KNOT = HELPER.createEntity("bolloom_knot", BolloomKnotEntity::new, BolloomKnotEntity::new, MobCategory.MISC, 0.375F, 0.19F);
	public static final RegistryObject<EntityType<BoofloBabyEntity>> BOOFLO_BABY = HELPER.createLivingEntity("booflo_baby", BoofloBabyEntity::new, MobCategory.CREATURE, 0.375F, 0.325F);
	public static final RegistryObject<EntityType<BoofloAdolescentEntity>> BOOFLO_ADOLESCENT = HELPER.createLivingEntity("booflo_adolescent", BoofloAdolescentEntity::new, MobCategory.CREATURE, 0.8F, 0.7F);
	public static final RegistryObject<EntityType<BoofloEntity>> BOOFLO = HELPER.createLivingEntity("booflo", BoofloEntity::new, MobCategory.CREATURE, 1.3F, 1.3F);
	public static final RegistryObject<EntityType<ChargerEetleEntity>> CHARGER_EETLE = HELPER.createLivingEntity("charger_eetle", ChargerEetleEntity::new, MobCategory.MONSTER, 1.05F, 0.85F);
	public static final RegistryObject<EntityType<GliderEetleEntity>> GLIDER_EETLE = HELPER.createLivingEntity("glider_eetle", GliderEetleEntity::new, MobCategory.MONSTER, 1.05F, 0.85F);
	public static final RegistryObject<EntityType<BroodEetleEntity>> BROOD_EETLE = HELPER.createLivingEntity("brood_eetle", BroodEetleEntity::new, MobCategory.MONSTER, 3.4375F, 2.125F);
	public static final RegistryObject<EntityType<EetleEggEntity>> EETLE_EGG = HELPER.createEntity("eetle_egg", EetleEggEntity::new, EetleEggEntity::new, MobCategory.MISC, 0.98F, 0.98F);
	public static final RegistryObject<EntityType<BroodEggSackEntity>> BROOD_EGG_SACK = HELPER.createUnsummonableEntity("brood_egg_sack", BroodEggSackEntity::new, BroodEggSackEntity::new, MobCategory.MISC, 1.25F, 1.25F);
	public static final RegistryObject<EntityType<PurpoidEntity>> PURPOID = HELPER.createLivingEntity("purpoid", PurpoidEntity::new, MobCategory.CREATURE, 1.0F, 1.0F);

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void registerSpawnPlacements(RegisterEvent event) {
		if (event.getRegistryKey() == Registry.ENTITY_TYPE_REGISTRY) {
			SpawnPlacements.register(BOOFLO.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING, EEEntities::endIslandCondition);
			SpawnPlacements.register(BOOFLO_ADOLESCENT.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING, EEEntities::endIslandCondition);
			SpawnPlacements.register(PUFF_BUG.get(), SpawnPlacements.Type.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, EEEntities::endIslandCondition);
			SpawnPlacements.register(CHARGER_EETLE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, EEEntities::eetleCondition);
			SpawnPlacements.register(GLIDER_EETLE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, EEEntities::eetleCondition);
		}
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

	private static boolean eetleCondition(EntityType<? extends Monster> entityType, ServerLevelAccessor world, MobSpawnType spawnReason, BlockPos pos, RandomSource random) {
		if (Monster.checkMonsterSpawnRules(entityType, world, spawnReason, pos, random) || isInfestedCorrockNearby(world, pos)) {
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

	private static boolean isInfestedCorrockNearby(ServerLevelAccessor world, BlockPos pos) {
		int radius = 1;
		BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
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

	private static boolean endIslandCondition(EntityType<? extends PathfinderMob> entityType, LevelAccessor world, MobSpawnType spawnReason, BlockPos pos, RandomSource random) {
		return pos.getY() >= 40;
	}
}