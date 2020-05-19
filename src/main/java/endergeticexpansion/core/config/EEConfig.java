package endergeticexpansion.core.config;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.fml.config.ModConfig;

/**
 * @author SmellyModder(Luke Tonon)
 */
public class EEConfig {

	public static class Common {
		public final ConfigValue<Boolean> debugDragonFightManager;
		public static ConfigValue<Boolean> poisonPotatoCompatEnabled;
		public static ConfigValue<Boolean> poisonEffect;
		public static ConfigValue<Double> poisonChance;
		
		Common(ForgeConfigSpec.Builder builder) {
			builder.comment("Common only settings for Endergetic")
			.push("common");
			
			debugDragonFightManager = builder
				.comment("If The Dragon Fight Manager should debug its portal values; Default: False")
				.translation(makeTranslation("debug_dragon_fight_manager"))
				.define("debugDragonFightManager", false);
			
			builder.pop();

			builder.comment("Compatibility with Quark's poisonous potatoes feature")
			.push("poisonousPotatoCompat");
			poisonPotatoCompatEnabled = builder
					.comment("If booflos can be fed a poisonous potato to stunt their growth when Quark is installed; Default: True")
					.translation(makeTranslation("poison_potato_compat_enabled"))
					.define("poisonPotatoCompatEnabled",true);
			poisonEffect = builder
					.comment("If growth stunting should give a booflo poison; Default: True")
					.translation(makeTranslation("poison_effect"))
					.define("poisonEffect",true);
			poisonChance = builder
					.comment("The chance to stunt booflo growth when feeding a poisonous potato; Default: 0.1")
					.translation(makeTranslation("poison_chance"))
					.defineInRange("poisonChance",0.1,0,1);
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
		public static boolean poisonPotatoCompatEnabled;
		public static boolean poisonEffect;
		public static double poisonChance;
		
		public static void updateCommonValuesFromConfig(ModConfig config) {
			debugDragonFightManager = EEConfig.COMMON.debugDragonFightManager.get();
			poisonPotatoCompatEnabled = EEConfig.COMMON.poisonPotatoCompatEnabled.get();
			poisonEffect = EEConfig.COMMON.poisonEffect.get();
			poisonChance = EEConfig.COMMON.poisonChance.get();
		}
		
		public static boolean shouldDebugDragonFightManager() {
			return debugDragonFightManager;
		}

		public static boolean shouldEnablePoisonPotatoCompat() {
			return poisonPotatoCompatEnabled;
		}

		public static boolean shouldPoisonEntity() {
			return poisonEffect;
		}

		public static double poisonEffectChance() {
			return poisonChance;
		}
	}
	
}