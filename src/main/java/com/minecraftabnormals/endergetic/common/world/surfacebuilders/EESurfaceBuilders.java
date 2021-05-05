package com.minecraftabnormals.endergetic.common.world.surfacebuilders;

import java.util.function.Supplier;

import com.minecraftabnormals.endergetic.core.EndergeticExpansion;
import com.minecraftabnormals.endergetic.core.registry.EEBlocks;

import net.minecraft.block.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.surfacebuilders.ConfiguredSurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.ISurfaceBuilderConfig;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public final class EESurfaceBuilders {
	public static final DeferredRegister<SurfaceBuilder<?>> SURFACE_BUILDERS = DeferredRegister.create(ForgeRegistries.SURFACE_BUILDERS, EndergeticExpansion.MOD_ID);

	public static final RegistryObject<SurfaceBuilder<SurfaceBuilderConfig>> POISE_SURFACE_BUILDER = createSurfaceBuilder("poise_forest", () -> new PoiseForestSurfaceBuilder(SurfaceBuilderConfig.field_237203_a_));
	public static final RegistryObject<SurfaceBuilder<SurfaceBuilderConfig>> SPARSE_CORROCK_SURFACE_BUILDER = createSurfaceBuilder("sparse_corrock", () -> new SparseCorrockSurfaceBuilder(SurfaceBuilderConfig.field_237203_a_));

	private static <S extends SurfaceBuilder<?>> RegistryObject<S> createSurfaceBuilder(String name, Supplier<S> sup) {
		return SURFACE_BUILDERS.register(name, sup);
	}

	public static final class Configs {
		public static final Supplier<SurfaceBuilderConfig> END_STONE_CONFIG = () -> new SurfaceBuilderConfig(Blocks.END_STONE.getDefaultState(), Blocks.END_STONE.getDefaultState(), Blocks.END_STONE.getDefaultState());
		public static final Supplier<SurfaceBuilderConfig> SPECKLED_END_STONE_CONFIG = () -> new SurfaceBuilderConfig(EEBlocks.SPECKLED_END_CORROCK.get().getDefaultState(), Blocks.END_STONE.getDefaultState(), Blocks.END_STONE.getDefaultState());
		public static final Supplier<SurfaceBuilderConfig> EUMUS_CONFIG = () -> new SurfaceBuilderConfig(EEBlocks.EUMUS.get().getDefaultState(), EEBlocks.EUMUS.get().getDefaultState(), EEBlocks.EUMUS.get().getDefaultState());
		public static final Supplier<SurfaceBuilderConfig> POISMOSS_CONFIG = () -> new SurfaceBuilderConfig(EEBlocks.POISMOSS.get().getDefaultState(), Blocks.END_STONE.getDefaultState(), EEBlocks.EUMUS.get().getDefaultState());
		public static final Supplier<SurfaceBuilderConfig> CORROCK_CONFIG = () -> new SurfaceBuilderConfig(EEBlocks.CORROCK_END_BLOCK.get().getDefaultState(), EEBlocks.EUMUS.get().getDefaultState(), EEBlocks.PETRIFIED_CORROCK_END_BLOCK.get().getDefaultState());

		public static final ConfiguredSurfaceBuilder<?> POISE_FOREST = POISE_SURFACE_BUILDER.get().func_242929_a(SurfaceBuilder.END_STONE_CONFIG);
		public static final ConfiguredSurfaceBuilder<?> SPARSE_CORROCK = SPARSE_CORROCK_SURFACE_BUILDER.get().func_242929_a(SurfaceBuilder.END_STONE_CONFIG);

		private static <SC extends ISurfaceBuilderConfig> void register(String name, ConfiguredSurfaceBuilder<SC> configuredSurfaceBuilder) {
			Registry.register(WorldGenRegistries.CONFIGURED_SURFACE_BUILDER, new ResourceLocation(EndergeticExpansion.MOD_ID, name), configuredSurfaceBuilder);
		}

		public static void registerConfiguredSurfaceBuilders() {
			register("poise_forest", POISE_FOREST);
			register("sparse_corrock", SPARSE_CORROCK);
		}
	}
}