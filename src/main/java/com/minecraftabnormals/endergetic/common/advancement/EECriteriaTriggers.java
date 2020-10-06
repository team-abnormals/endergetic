package com.minecraftabnormals.endergetic.common.advancement;

import com.teamabnormals.abnormals_core.common.advancement.EmptyTrigger;
import com.minecraftabnormals.endergetic.core.EndergeticExpansion;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = EndergeticExpansion.MOD_ID)
public final class EECriteriaTriggers {
	public static final EmptyTrigger TAME_BOOFLO = CriteriaTriggers.register(new EmptyTrigger(prefix("tamed_booflo")));
	public static final EmptyTrigger UP_UP_AND_AWAY = CriteriaTriggers.register(new EmptyTrigger(prefix("up_up_and_away")));

	private static ResourceLocation prefix(String name) {
		return new ResourceLocation(EndergeticExpansion.MOD_ID, name);
	}
}