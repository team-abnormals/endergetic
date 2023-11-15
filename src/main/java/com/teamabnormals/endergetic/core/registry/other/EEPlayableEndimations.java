package com.teamabnormals.endergetic.core.registry.other;

import com.teamabnormals.blueprint.core.endimator.PlayableEndimation;
import com.teamabnormals.blueprint.core.endimator.PlayableEndimation.LoopType;
import com.teamabnormals.blueprint.core.endimator.PlayableEndimationManager;
import com.teamabnormals.endergetic.core.EndergeticExpansion;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EndergeticExpansion.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class EEPlayableEndimations {
	public static final PlayableEndimation ADOLESCENT_BOOFLO_BOOF = register("adolescent_booflo/boof", 10, LoopType.NONE);
	public static final PlayableEndimation ADOLESCENT_BOOFLO_EATING = register("adolescent_booflo/eating", 10, LoopType.NONE);

	public static final PlayableEndimation BOOFLO_CROAK = register("booflo/croak", 55, LoopType.NONE);
	public static final PlayableEndimation BOOFLO_HOP = register("booflo/hop", 25, LoopType.NONE);
	public static final PlayableEndimation BOOFLO_HURT = register("booflo/hurt", 15, LoopType.NONE);
	public static final PlayableEndimation BOOFLO_BIRTH = register("booflo/birth", 140, LoopType.NONE);
	public static final PlayableEndimation BOOFLO_INFLATE = register("booflo/inflate", 10, LoopType.NONE);
	public static final PlayableEndimation BOOFLO_SWIM = register("booflo/swim", 20, LoopType.NONE);
	public static final PlayableEndimation BOOFLO_EAT = register("booflo/eat", 160, LoopType.NONE);
	public static final PlayableEndimation BOOFLO_CHARGE = register("booflo/charge", 75, LoopType.NONE);
	public static final PlayableEndimation BOOFLO_SLAM = register("booflo/slam", 10, LoopType.NONE);
	public static final PlayableEndimation BOOFLO_GROWL = register("booflo/growl", 60, LoopType.NONE);

	public static final PlayableEndimation PUFF_BUG_CLAIM_HIVE = register("puff_bug/claim_hive", 20, LoopType.NONE);
	public static final PlayableEndimation PUFF_BUG_PUFF = register("puff_bug/puff", 20, LoopType.NONE);
	public static final PlayableEndimation PUFF_BUG_TELEPORT_TO = register("puff_bug/teleport_to", 15, LoopType.NONE);
	public static final PlayableEndimation PUFF_BUG_TELEPORT_FROM = register("puff_bug/teleport_from", 10, LoopType.NONE);
	public static final PlayableEndimation PUFF_BUG_ROTATE = register("puff_bug/rotate", 20, LoopType.NONE);
	public static final PlayableEndimation PUFF_BUG_POLLINATE = register("puff_bug/pollinate", 120, LoopType.NONE);
	public static final PlayableEndimation PUFF_BUG_MAKE_ITEM = register("puff_bug/make_item", 100, LoopType.NONE);
	//TODO: Does this need to be an PlayableEndimation actually?
	public static final PlayableEndimation PUFF_BUG_FLY = register("puff_bug/fly", 25, LoopType.NONE);
	public static final PlayableEndimation PUFF_BUG_LAND = register("puff_bug/land", 20, LoopType.NONE);
	public static final PlayableEndimation PUFF_BUG_PULL = register("puff_bug/pull", 25, LoopType.NONE);

	public static final PlayableEndimation EETLE_GROW_UP = register("eetle/grow_up", 30, LoopType.NONE);

	public static final PlayableEndimation CHARGER_EETLE_ATTACK = register("eetle/charger_attack", 10, LoopType.NONE);
	public static final PlayableEndimation CHARGER_EETLE_CATAPULT = register("eetle/charger_catapult", 16, LoopType.NONE);
	public static final PlayableEndimation CHARGER_EETLE_FLAP = register("eetle/charger_flap", 20, LoopType.NONE);

	public static final PlayableEndimation GLIDER_EETLE_FLAP = register("eetle/glider_flap", 22, LoopType.NONE);
	public static final PlayableEndimation GLIDER_EETLE_MUNCH = register("eetle/glider_munch", 25, LoopType.NONE);

	public static final PlayableEndimation BROOD_EETLE_FLAP = register("eetle/brood_flap", 22, LoopType.NONE);
	public static final PlayableEndimation BROOD_EETLE_MUNCH = register("eetle/brood_munch", 25, LoopType.NONE);
	public static final PlayableEndimation BROOD_EETLE_ATTACK = register("eetle/brood_attack", 12, LoopType.NONE);
	public static final PlayableEndimation BROOD_EETLE_SLAM = register("eetle/brood_slam", 20, LoopType.NONE);
	public static final PlayableEndimation BROOD_EETLE_LAUNCH = register("eetle/brood_launch", 18, LoopType.NONE);
	public static final PlayableEndimation BROOD_EETLE_DROP_EGGS = register("eetle/brood_drop_eggs", 18, LoopType.NONE);
	public static final PlayableEndimation BROOD_EETLE_AIR_CHARGE = register("eetle/brood_air_charge", 80, LoopType.NONE);
	public static final PlayableEndimation BROOD_EETLE_AIR_SLAM = register("eetle/brood_air_slam", 11, LoopType.NONE);
	public static final PlayableEndimation BROOD_EETLE_DEATH_LEFT = register("eetle/brood_death_left", 115, LoopType.NONE);
	public static final PlayableEndimation BROOD_EETLE_DEATH_RIGHT = register("eetle/brood_death_right", 115, LoopType.NONE);

	public static final PlayableEndimation PURPOID_TELEPORT_TO = register("purpoid/teleport_to", 18, LoopType.NONE);
	public static final PlayableEndimation PURPOID_FAST_TELEPORT_TO = register("purpoid/fast_teleport_to", 15, LoopType.NONE);
	public static final PlayableEndimation PURPOID_TELEPORT_FROM = register("purpoid/teleport_from", 10, LoopType.NONE);
	public static final PlayableEndimation PURPOID_TELEFRAG = register("purpoid/telefrag", 10, LoopType.NONE);
	public static final PlayableEndimation PURPOID_DEATH = register("purpoid/death", 20, LoopType.NONE);
	public static final PlayableEndimation PURPOID_SQUIRT_ATTACK = register("purpoid/squirt_attack", 65, LoopType.NONE);

	private static PlayableEndimation register(String name, int duration, PlayableEndimation.LoopType loopType) {
		return PlayableEndimationManager.INSTANCE.registerPlayableEndimation(new PlayableEndimation(new ResourceLocation(EndergeticExpansion.MOD_ID, name), duration, loopType));
	}
}
