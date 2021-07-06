package com.minecraftabnormals.endergetic.client.particles;

import com.minecraftabnormals.endergetic.client.particles.data.CorrockCrownParticleData;
import com.minecraftabnormals.endergetic.core.EndergeticExpansion;

import com.mojang.serialization.Codec;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.FlameParticle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Function;

public class EEParticles {
	public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, EndergeticExpansion.MOD_ID);

	public static final RegistryObject<BasicParticleType> POISE_BUBBLE = createBasicParticleType(true, "poise_bubble");
	public static final RegistryObject<BasicParticleType> SHORT_POISE_BUBBLE = createBasicParticleType(true, "short_poise_bubble");
	public static final RegistryObject<BasicParticleType> ENDER_FLAME = createBasicParticleType(true, "ender_flame");
	public static final RegistryObject<ParticleType<BlockParticleData>> FAST_BLOCK = createParticleType("fast_block", BlockParticleData.DESERIALIZER, BlockParticleData::func_239800_a_);
	public static final RegistryObject<ParticleType<CorrockCrownParticleData>> OVERWORLD_CROWN = createParticleType("overworld_crown", CorrockCrownParticleData.DESERIALIZER, CorrockCrownParticleData::codec);
	public static final RegistryObject<ParticleType<CorrockCrownParticleData>> NETHER_CROWN = createParticleType("nether_crown", CorrockCrownParticleData.DESERIALIZER, CorrockCrownParticleData::codec);
	public static final RegistryObject<ParticleType<CorrockCrownParticleData>> END_CROWN = createParticleType("end_crown", CorrockCrownParticleData.DESERIALIZER, CorrockCrownParticleData::codec);

	private static RegistryObject<BasicParticleType> createBasicParticleType(boolean alwaysShow, String name) {
		return PARTICLES.register(name, () -> new BasicParticleType(alwaysShow));
	}

	@SuppressWarnings("deprecation")
	private static <T extends IParticleData> RegistryObject<ParticleType<T>> createParticleType(String name, IParticleData.IDeserializer<T> deserializer, Function<ParticleType<T>, Codec<T>> function) {
		return PARTICLES.register(name, () -> new ParticleTypeWithData<>(deserializer, function));
	}

	@OnlyIn(Dist.CLIENT)
	@EventBusSubscriber(modid = EndergeticExpansion.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
	public static class RegisterParticleFactories {

		@OnlyIn(Dist.CLIENT)
		@SubscribeEvent(priority = EventPriority.LOWEST)
		public static void registerParticleTypes(ParticleFactoryRegisterEvent event) {
			ParticleManager particleManager = Minecraft.getInstance().particles;
			if (ENDER_FLAME.isPresent()) {
				particleManager.registerFactory(ENDER_FLAME.get(), FlameParticle.Factory::new);
			}
			if (POISE_BUBBLE.isPresent()) {
				particleManager.registerFactory(POISE_BUBBLE.get(), PoiseBubbleParticle.Factory::new);
			}
			if (SHORT_POISE_BUBBLE.isPresent()) {
				particleManager.registerFactory(SHORT_POISE_BUBBLE.get(), PoiseBubbleParticle.ShortFactory::new);
			}
			if (FAST_BLOCK.isPresent()) {
				particleManager.registerFactory(FAST_BLOCK.get(), new FastBlockParticle.Factory());
			}
			if (OVERWORLD_CROWN.isPresent()) {
				particleManager.registerFactory(OVERWORLD_CROWN.get(), CorrockCrownParticle.Factory::new);
			}
			if (NETHER_CROWN.isPresent()) {
				particleManager.registerFactory(NETHER_CROWN.get(), CorrockCrownParticle.Factory::new);
			}
			if (END_CROWN.isPresent()) {
				particleManager.registerFactory(END_CROWN.get(), CorrockCrownParticle.Factory::new);
			}
		}

	}

	static class ParticleTypeWithData<T extends IParticleData> extends ParticleType<T> {
		private final Function<ParticleType<T>, Codec<T>> function;

		@SuppressWarnings("deprecation")
		public ParticleTypeWithData(IParticleData.IDeserializer<T> deserializer, Function<ParticleType<T>, Codec<T>> function) {
			super(false, deserializer);
			this.function = function;
		}

		@Override
		public Codec<T> func_230522_e_() {
			return this.function.apply(this);
		}
	}
}