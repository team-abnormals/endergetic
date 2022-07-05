package com.minecraftabnormals.endergetic.core.registry;

import com.minecraftabnormals.endergetic.common.world.biome.modifiers.SmallEndIslandsAmbienceBiomeModifier;
import com.minecraftabnormals.endergetic.core.EndergeticExpansion;
import com.mojang.serialization.Codec;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class EEBiomeModifierSerializers {
	public static final DeferredRegister<Codec<? extends BiomeModifier>> SERIALIZERS = DeferredRegister.create(ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, EndergeticExpansion.MOD_ID);

	public static final RegistryObject<Codec<? extends BiomeModifier>> SMALL_END_ISLANDS_AMBIENCE = SERIALIZERS.register("small_end_islands_ambience", () -> SmallEndIslandsAmbienceBiomeModifier.CODEC);
}
