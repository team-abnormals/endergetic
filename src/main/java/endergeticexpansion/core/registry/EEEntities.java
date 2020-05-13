package endergeticexpansion.core.registry;

import java.util.Random;

import endergeticexpansion.common.entities.EntityBoofBlock;
import endergeticexpansion.common.entities.EntityPoiseCluster;
import endergeticexpansion.common.entities.bolloom.EntityBolloomBalloon;
import endergeticexpansion.common.entities.bolloom.EntityBolloomFruit;
import endergeticexpansion.common.entities.bolloom.EntityBolloomKnot;
import endergeticexpansion.common.entities.booflo.EntityBooflo;
import endergeticexpansion.common.entities.booflo.EntityBoofloAdolescent;
import endergeticexpansion.common.entities.booflo.EntityBoofloBaby;
import endergeticexpansion.common.entities.puffbug.EntityPuffBug;
import endergeticexpansion.core.EndergeticExpansion;
import endergeticexpansion.core.registry.util.EndergeticRegistryHelper;
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
public class EEEntities {
	private static final EndergeticRegistryHelper HELPER = EndergeticExpansion.REGISTRY_HELPER;
	
	/*
	 * Poise Forest
	 */
	public static final RegistryObject<EntityType<EntityPoiseCluster>> POISE_CLUSTER = HELPER.createLivingEntity("poise_cluster", EntityPoiseCluster::new, EntityClassification.MISC, 1F, 1F);
	public static final RegistryObject<EntityType<EntityBolloomFruit>> BOLLOOM_FRUIT = HELPER.createEntity("bolloom_fruit", EntityBolloomFruit::new, EntityBolloomFruit::new, EntityClassification.MISC, 0.5F, 0.5F);
	public static final RegistryObject<EntityType<EntityBoofBlock>> BOOF_BLOCK = HELPER.createLivingEntity("boof_block", EntityBoofBlock::new, EntityClassification.MISC, 1.6F, 1.6F);
	public static final RegistryObject<EntityType<EntityPuffBug>> PUFF_BUG = HELPER.createLivingEntity("puff_bug", EntityPuffBug::new, EntityClassification.CREATURE, 0.3F, 1.15F);
	public static final RegistryObject<EntityType<EntityBolloomBalloon>> BOLLOOM_BALLOON = HELPER.createEntity("bolloom_balloon", EntityBolloomBalloon::new, EntityBolloomBalloon::new, EntityClassification.MISC, 0.5F, 0.5F);
	public static final RegistryObject<EntityType<EntityBolloomKnot>> BOLLOOM_KNOT = HELPER.createEntity("bolloom_knot", EntityBolloomKnot::new, EntityBolloomKnot::new, EntityClassification.MISC, 0.375F, 0.19F);
	public static final RegistryObject<EntityType<EntityBoofloBaby>> BOOFLO_BABY = HELPER.createLivingEntity("booflo_baby", EntityBoofloBaby::new, EntityClassification.CREATURE, 0.375F, 0.325F);
	public static final RegistryObject<EntityType<EntityBoofloAdolescent>> BOOFLO_ADOLESCENT = HELPER.createLivingEntity("booflo_adolescent", EntityBoofloAdolescent::new, EntityClassification.CREATURE, 0.8F, 0.7F);
	public static final RegistryObject<EntityType<EntityBooflo>> BOOFLO = HELPER.createLivingEntity("booflo", EntityBooflo::new, EntityClassification.CREATURE, 1.3F, 1.3F);
	
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
		public static final EntityClassification END_CREATURE = EntityClassification.create("endergetic:end_creature", "END_CREATURE", 20, false, true);
	}
}