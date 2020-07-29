package com.minecraftabnormals.endergetic.common.advancement;

import com.teamabnormals.abnormals_core.common.advancement.EmptyTrigger;
import com.minecraftabnormals.endergetic.core.EndergeticExpansion;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = EndergeticExpansion.MOD_ID)
public class EECriteriaTriggers {
	public static final EmptyTrigger TAME_BOOFLO = CriteriaTriggers.register(new EmptyTrigger(prefix("tamed_booflo")));
	
	private static ResourceLocation prefix(String name) {
		return new ResourceLocation(EndergeticExpansion.MOD_ID, name);
	}
}