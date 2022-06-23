package com.minecraftabnormals.endergetic.common.world.surfacebuilders;

import java.util.function.Supplier;

import com.minecraftabnormals.endergetic.core.EndergeticExpansion;
import com.minecraftabnormals.endergetic.core.registry.EEBlocks;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.world.level.levelgen.surfacebuilders.ConfiguredSurfaceBuilder;
import net.minecraft.world.level.levelgen.surfacebuilders.SurfaceBuilderConfiguration;
import net.minecraft.world.level.levelgen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.level.levelgen.surfacebuilders.SurfaceBuilderBaseConfiguration;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public final class EESurfaceBuilders {
	public static final DeferredRegister<SurfaceBuilder<?>> SURFACE_BUILDERS = DeferredRegister.create(ForgeRegistries.SURFACE_BUILDERS, EndergeticExpansion.MOD_ID);

	public static final RegistryObject<SurfaceBuilder<SurfaceBuilderBaseConfiguration>> POISE_SURFACE_BUILDER = createSurfaceBuilder("poise_forest", () -> new PoiseForestSurfaceBuilder(SurfaceBuilderBaseConfiguration.CODEC));
	public static final RegistryObject<SurfaceBuilder<SurfaceBuilderBaseConfiguration>> SPARSE_CORROCK_SURFACE_BUILDER = createSurfaceBuilder("sparse_corrock", () -> new SparseCorrockSurfaceBuilder(SurfaceBuilderBaseConfiguration.CODEC));

	private static <S extends SurfaceBuilder<?>> RegistryObject<S> createSurfaceBuilder(String name, Supplier<S> sup) {
		return SURFACE_BUILDERS.register(name, sup);
	}

	public static final class Configs {
		public static final Supplier<SurfaceBuilderBaseConfiguration> END_STONE_CONFIG = () -> new SurfaceBuilderBaseConfiguration(Blocks.END_STONE.defaultBlockState(), Blocks.END_STONE.defaultBlockState(), Blocks.END_STONE.defaultBlockState());
		public static final Supplier<SurfaceBuilderBaseConfiguration> SPECKLED_END_STONE_CONFIG = () -> new SurfaceBuilderBaseConfiguration(EEBlocks.SPECKLED_END_CORROCK.get().defaultBlockState(), Blocks.END_STONE.defaultBlockState(), Blocks.END_STONE.defaultBlockState());
		public static final Supplier<SurfaceBuilderBaseConfiguration> EUMUS_CONFIG = () -> new SurfaceBuilderBaseConfiguration(EEBlocks.EUMUS.get().defaultBlockState(), EEBlocks.EUMUS.get().defaultBlockState(), EEBlocks.EUMUS.get().defaultBlockState());
		public static final Supplier<SurfaceBuilderBaseConfiguration> POISMOSS_CONFIG = () -> new SurfaceBuilderBaseConfiguration(EEBlocks.POISMOSS.get().defaultBlockState(), Blocks.END_STONE.defaultBlockState(), EEBlocks.EUMUS.get().defaultBlockState());
		public static final Supplier<SurfaceBuilderBaseConfiguration> CORROCK_CONFIG = () -> new SurfaceBuilderBaseConfiguration(EEBlocks.CORROCK_END_BLOCK.get().defaultBlockState(), EEBlocks.EUMUS.get().defaultBlockState(), EEBlocks.PETRIFIED_CORROCK_END_BLOCK.get().defaultBlockState());

		public static final ConfiguredSurfaceBuilder<?> POISE_FOREST = POISE_SURFACE_BUILDER.get().configured(SurfaceBuilder.CONFIG_THEEND);
		public static final ConfiguredSurfaceBuilder<?> SPARSE_CORROCK = SPARSE_CORROCK_SURFACE_BUILDER.get().configured(SurfaceBuilder.CONFIG_THEEND);

		private static <SC extends SurfaceBuilderConfiguration> void register(String name, ConfiguredSurfaceBuilder<SC> configuredSurfaceBuilder) {
			Registry.register(BuiltinRegistries.CONFIGURED_SURFACE_BUILDER, new ResourceLocation(EndergeticExpansion.MOD_ID, name), configuredSurfaceBuilder);
		}

		public static void registerConfiguredSurfaceBuilders() {
			register("poise_forest", POISE_FOREST);
			register("sparse_corrock", SPARSE_CORROCK);
		}
	}
}