package com.teamabnormals.endergetic.core.registry;

import com.teamabnormals.endergetic.common.entity.BoofBlockEntity;
import com.teamabnormals.endergetic.common.entity.PoiseClusterEntity;
import com.teamabnormals.endergetic.common.entity.bolloom.BolloomBalloon;
import com.teamabnormals.endergetic.common.entity.bolloom.BolloomFruit;
import com.teamabnormals.endergetic.common.entity.bolloom.BolloomKnot;
import com.teamabnormals.endergetic.common.entity.booflo.Booflo;
import com.teamabnormals.endergetic.common.entity.booflo.BoofloAdolescent;
import com.teamabnormals.endergetic.common.entity.booflo.BoofloBaby;
import com.teamabnormals.endergetic.common.entity.eetle.*;
import com.teamabnormals.endergetic.common.entity.puffbug.PuffBug;
import com.teamabnormals.endergetic.common.entity.purpoid.Purpoid;
import com.teamabnormals.endergetic.core.EndergeticExpansion;
import com.teamabnormals.endergetic.core.registry.util.EndergeticEntitySubRegistryHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent.Operation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.registries.RegistryObject;

@EventBusSubscriber(modid = EndergeticExpansion.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public final class EEEntityTypes {
	public static final MobCategory END_CREATURE = MobCategory.create(EndergeticExpansion.MOD_ID + ":end_creature", "end_creature", 20, false, true, 128);
	public static final EndergeticEntitySubRegistryHelper HELPER = EndergeticExpansion.REGISTRY_HELPER.getEntitySubHelper();

	public static final RegistryObject<EntityType<PoiseClusterEntity>> POISE_CLUSTER = HELPER.createLivingEntity("poise_cluster", PoiseClusterEntity::new, MobCategory.MISC, 1F, 1F);
	public static final RegistryObject<EntityType<BolloomFruit>> BOLLOOM_FRUIT = HELPER.createManuallyUpdatedEntity("bolloom_fruit", BolloomFruit::new, BolloomFruit::new, MobCategory.MISC, 0.5F, 0.5F);
	public static final RegistryObject<EntityType<BoofBlockEntity>> BOOF_BLOCK = HELPER.createEntity("boof_block", BoofBlockEntity::new, BoofBlockEntity::new, MobCategory.MISC, 1.75F, 1.75F);
	public static final RegistryObject<EntityType<PuffBug>> PUFF_BUG = HELPER.createLivingEntity("puff_bug", PuffBug::new, MobCategory.CREATURE, 0.3F, 1.15F);
	public static final RegistryObject<EntityType<BolloomBalloon>> BOLLOOM_BALLOON = HELPER.createManuallyUpdatedEntity("bolloom_balloon", BolloomBalloon::new, BolloomBalloon::new, MobCategory.MISC, 0.5F, 0.5F);
	public static final RegistryObject<EntityType<BolloomKnot>> BOLLOOM_KNOT = HELPER.createEntity("bolloom_knot", BolloomKnot::new, BolloomKnot::new, MobCategory.MISC, 0.375F, 0.19F);
	public static final RegistryObject<EntityType<BoofloBaby>> BOOFLO_BABY = HELPER.createLivingEntity("booflo_baby", BoofloBaby::new, MobCategory.CREATURE, 0.375F, 0.325F);
	public static final RegistryObject<EntityType<BoofloAdolescent>> BOOFLO_ADOLESCENT = HELPER.createLivingEntity("booflo_adolescent", BoofloAdolescent::new, MobCategory.CREATURE, 0.8F, 0.7F);
	public static final RegistryObject<EntityType<Booflo>> BOOFLO = HELPER.createLivingEntity("booflo", Booflo::new, MobCategory.CREATURE, 1.3F, 1.3F);
	public static final RegistryObject<EntityType<ChargerEetle>> CHARGER_EETLE = HELPER.createLivingEntity("charger_eetle", ChargerEetle::new, MobCategory.MONSTER, 1.05F, 0.85F);
	public static final RegistryObject<EntityType<GliderEetle>> GLIDER_EETLE = HELPER.createLivingEntity("glider_eetle", GliderEetle::new, MobCategory.MONSTER, 1.05F, 0.85F);
	public static final RegistryObject<EntityType<BroodEetle>> BROOD_EETLE = HELPER.createLivingEntity("brood_eetle", BroodEetle::new, MobCategory.MONSTER, 3.4375F, 2.125F);
	public static final RegistryObject<EntityType<EetleEgg>> EETLE_EGG = HELPER.createEntity("eetle_egg", EetleEgg::new, EetleEgg::new, MobCategory.MISC, 0.98F, 0.98F);
	public static final RegistryObject<EntityType<BroodEggSack>> BROOD_EGG_SACK = HELPER.createUnsummonableEntity("brood_egg_sack", BroodEggSack::new, BroodEggSack::new, MobCategory.MISC, 1.25F, 1.25F);
	public static final RegistryObject<EntityType<Purpoid>> PURPOID = HELPER.createLivingEntity("purpoid", Purpoid::new, MobCategory.CREATURE, 1.0F, 1.0F);

	@SubscribeEvent
	public static void registerAttributes(EntityAttributeCreationEvent event) {
		event.put(BOOFLO.get(), Booflo.registerAttributes().build());
		event.put(BOOFLO_ADOLESCENT.get(), BoofloAdolescent.registerAttributes().build());
		event.put(BOOFLO_BABY.get(), BoofloBaby.registerAttributes().build());
		event.put(PUFF_BUG.get(), PuffBug.registerAttributes().build());
		event.put(POISE_CLUSTER.get(), LivingEntity.createLivingAttributes().build());
		event.put(CHARGER_EETLE.get(), ChargerEetle.registerAttributes().build());
		event.put(GLIDER_EETLE.get(), GliderEetle.registerAttributes().build());
		event.put(BROOD_EETLE.get(), BroodEetle.registerAttributes().build());
		event.put(PURPOID.get(), Purpoid.registerAttributes().build());
	}

	@SubscribeEvent
	public static void registerSpawnPlacements(SpawnPlacementRegisterEvent event) {
		event.register(BOOFLO.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING, EEEntityTypes::endIslandCondition, Operation.OR);
		event.register(BOOFLO_ADOLESCENT.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING, EEEntityTypes::endIslandCondition, Operation.OR);
		event.register(PUFF_BUG.get(), SpawnPlacements.Type.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, EEEntityTypes::endIslandCondition, Operation.OR);
		event.register(CHARGER_EETLE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, EEEntityTypes::eetleCondition, Operation.OR);
		event.register(GLIDER_EETLE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, EEEntityTypes::eetleCondition, Operation.OR);
		event.register(PURPOID.get(), SpawnPlacements.Type.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, EEEntityTypes::purpoidCondition, Operation.OR);
		event.register(EntityType.ENDERMAN, null, null, (entity, level, type, pos, random) -> {
			return !level.getBlockState(pos.below()).is(EEBlocks.END_CORROCK_BLOCK.get());
		}, Operation.AND);
	}

	private static boolean eetleCondition(EntityType<? extends Monster> entityType, ServerLevelAccessor world, MobSpawnType spawnReason, BlockPos pos, RandomSource random) {
		if (Monster.checkMonsterSpawnRules(entityType, world, spawnReason, pos, random) || isInfestedCorrockNearby(world, pos)) {
			BlockPos down = pos.below();
			Block downBlock = world.getBlockState(down).getBlock();
			if (downBlock == EEBlocks.END_CORROCK_BLOCK.get() || downBlock == EEBlocks.EUMUS.get() || downBlock == EEBlocks.INFESTED_CORROCK.get()) {
				return true;
			}
			for (Direction direction : Direction.Plane.HORIZONTAL) {
				Block offsetBlock = world.getBlockState(down.relative(direction)).getBlock();
				if (offsetBlock == EEBlocks.END_CORROCK_BLOCK.get() || offsetBlock == EEBlocks.EUMUS.get()) {
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

	private static boolean purpoidCondition(EntityType<? extends Purpoid> entityType, ServerLevelAccessor level, MobSpawnType type, BlockPos pos, RandomSource random) {
		BlockPos.MutableBlockPos mutable = pos.mutable();
		for (int i = 0; i < 10; i++) {
			mutable.setY(mutable.getY() - 1);
			BlockState state = level.getBlockState(mutable);
			if (state.is(EEBlocks.SPECKLED_END_CORROCK.get()) || state.is(EEBlocks.END_CORROCK_BLOCK.get()))
				return true;
			if (!state.is(Blocks.CHORUS_PLANT) && !state.is(Blocks.CHORUS_FLOWER) && !state.isAir()) return false;
		}
		return false;
	}

	private static boolean endIslandCondition(EntityType<? extends PathfinderMob> entityType, LevelAccessor world, MobSpawnType spawnReason, BlockPos pos, RandomSource random) {
		return pos.getY() >= 40;
	}
}