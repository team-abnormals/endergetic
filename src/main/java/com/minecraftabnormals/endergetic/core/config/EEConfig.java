package com.minecraftabnormals.endergetic.core.config;

import com.minecraftabnormals.abnormals_core.core.annotations.ConfigKey;
import org.apache.commons.lang3.tuple.Pair;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.fml.config.ModConfig;

/**
 * @author SmellyModder(Luke Tonon)
 */
public final class EEConfig {

	public static class Common {

		@ConfigKey("debug_dragon_fight_manager")
		public final ConfigValue<Boolean> debugDragonFightManager;

		Common(ForgeConfigSpec.Builder builder) {
			builder.comment("Common only settings for Endergetic")
					.push("common");

			debugDragonFightManager = builder
					.comment("If The Dragon Fight Manager should debug its portal values; Default: False")
					.translation(makeTranslation("debug_dragon_fight_manager"))
					.define("debugDragonFightManager", false);

			builder.pop();
		}
	}

	private static String makeTranslation(String name) {
		return "endergetic.config." + name;
	}

	public static final ForgeConfigSpec COMMON_SPEC;
	public static final Common COMMON;

	static {
		final Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
		COMMON_SPEC = specPair.getRight();
		COMMON = specPair.getLeft();
	}

	public static class ValuesHolder {
		private static boolean debugDragonFightManager;

		public static void updateCommonValuesFromConfig(ModConfig config) {
			debugDragonFightManager = EEConfig.COMMON.debugDragonFightManager.get();
		}

		public static boolean shouldDebugDragonFightManager() {
			return debugDragonFightManager;
		}
	}

}