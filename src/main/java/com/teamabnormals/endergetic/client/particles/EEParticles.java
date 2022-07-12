package com.teamabnormals.endergetic.client.particles;

import com.teamabnormals.endergetic.client.particles.data.CorrockCrownParticleData;
import com.teamabnormals.endergetic.core.EndergeticExpansion;

import com.mojang.serialization.Codec;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.FlameParticle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Function;

public class EEParticles {
	public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, EndergeticExpansion.MOD_ID);

	public static final RegistryObject<SimpleParticleType> POISE_BUBBLE = createBasicParticleType(true, "poise_bubble");
	public static final RegistryObject<SimpleParticleType> SHORT_POISE_BUBBLE = createBasicParticleType(true, "short_poise_bubble");
	public static final RegistryObject<SimpleParticleType> ENDER_FLAME = createBasicParticleType(true, "ender_flame");
	public static final RegistryObject<ParticleType<BlockParticleOption>> FAST_BLOCK = createParticleType("fast_block", BlockParticleOption.DESERIALIZER, BlockParticleOption::codec);
	public static final RegistryObject<ParticleType<CorrockCrownParticleData>> OVERWORLD_CROWN = createParticleType("overworld_crown", CorrockCrownParticleData.DESERIALIZER, CorrockCrownParticleData::codec);
	public static final RegistryObject<ParticleType<CorrockCrownParticleData>> NETHER_CROWN = createParticleType("nether_crown", CorrockCrownParticleData.DESERIALIZER, CorrockCrownParticleData::codec);
	public static final RegistryObject<ParticleType<CorrockCrownParticleData>> END_CROWN = createParticleType("end_crown", CorrockCrownParticleData.DESERIALIZER, CorrockCrownParticleData::codec);

	private static RegistryObject<SimpleParticleType> createBasicParticleType(boolean alwaysShow, String name) {
		return PARTICLES.register(name, () -> new SimpleParticleType(alwaysShow));
	}

	@SuppressWarnings("deprecation")
	private static <T extends ParticleOptions> RegistryObject<ParticleType<T>> createParticleType(String name, ParticleOptions.Deserializer<T> deserializer, Function<ParticleType<T>, Codec<T>> function) {
		return PARTICLES.register(name, () -> new ParticleTypeWithData<>(deserializer, function));
	}

	@OnlyIn(Dist.CLIENT)
	@EventBusSubscriber(modid = EndergeticExpansion.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
	public static class RegisterParticleFactories {

		@OnlyIn(Dist.CLIENT)
		@SubscribeEvent(priority = EventPriority.LOWEST)
		public static void registerParticleTypes(RegisterParticleProvidersEvent event) {
			ParticleEngine particleManager = Minecraft.getInstance().particleEngine;
			if (ENDER_FLAME.isPresent()) {
				particleManager.register(ENDER_FLAME.get(), FlameParticle.Provider::new);
			}
			if (POISE_BUBBLE.isPresent()) {
				particleManager.register(POISE_BUBBLE.get(), PoiseBubbleParticle.Factory::new);
			}
			if (SHORT_POISE_BUBBLE.isPresent()) {
				particleManager.register(SHORT_POISE_BUBBLE.get(), PoiseBubbleParticle.ShortFactory::new);
			}
			if (FAST_BLOCK.isPresent()) {
				particleManager.register(FAST_BLOCK.get(), new FastBlockParticle.Factory());
			}
			if (OVERWORLD_CROWN.isPresent()) {
				particleManager.register(OVERWORLD_CROWN.get(), CorrockCrownParticle.Factory::new);
			}
			if (NETHER_CROWN.isPresent()) {
				particleManager.register(NETHER_CROWN.get(), CorrockCrownParticle.Factory::new);
			}
			if (END_CROWN.isPresent()) {
				particleManager.register(END_CROWN.get(), CorrockCrownParticle.Factory::new);
			}
		}

	}

	static class ParticleTypeWithData<T extends ParticleOptions> extends ParticleType<T> {
		private final Function<ParticleType<T>, Codec<T>> function;

		@SuppressWarnings("deprecation")
		public ParticleTypeWithData(ParticleOptions.Deserializer<T> deserializer, Function<ParticleType<T>, Codec<T>> function) {
			super(false, deserializer);
			this.function = function;
		}

		@Override
		public Codec<T> codec() {
			return this.function.apply(this);
		}
	}
}