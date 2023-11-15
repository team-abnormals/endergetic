package com.teamabnormals.endergetic.core.registry;

import com.mojang.serialization.Codec;
import com.teamabnormals.endergetic.common.levelgen.biome.modifiers.SmallEndIslandsAmbienceBiomeModifier;
import com.teamabnormals.endergetic.core.EndergeticExpansion;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class EEBiomeModifierSerializers {
	public static final DeferredRegister<Codec<? extends BiomeModifier>> SERIALIZERS = DeferredRegister.create(ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, EndergeticExpansion.MOD_ID);

	public static final RegistryObject<Codec<? extends BiomeModifier>> SMALL_END_ISLANDS_AMBIENCE = SERIALIZERS.register("small_end_islands_ambience", () -> SmallEndIslandsAmbienceBiomeModifier.CODEC);
}
