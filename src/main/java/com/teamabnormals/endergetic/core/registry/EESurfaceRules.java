package com.teamabnormals.endergetic.core.registry;

import com.mojang.serialization.Codec;
import com.teamabnormals.endergetic.core.EndergeticExpansion;
import com.teamabnormals.endergetic.core.registry.builtin.EENoises;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.PositionalRandomFactory;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

public final class EESurfaceRules extends SurfaceRules {
	public static final DeferredRegister<Codec<? extends RuleSource>> RULES = DeferredRegister.create(Registries.MATERIAL_RULE, EndergeticExpansion.MOD_ID);

	public static final RegistryObject<Codec<CorrockRuleSource>> CORROCK = RULES.register("corrock", CorrockRuleSource.CODEC::codec);

	public enum CorrockRuleSource implements SurfaceRules.RuleSource {
		INSTANCE;

		public static final KeyDispatchDataCodec<CorrockRuleSource> CODEC = KeyDispatchDataCodec.of(Codec.unit(INSTANCE));

		@Override
		public KeyDispatchDataCodec<? extends RuleSource> codec() {
			return CODEC;
		}

		@Override
		public SurfaceRule apply(Context context) {
			NormalNoise corrockNoise = context.randomState.getOrCreateNoise(EENoises.CORROCK);
			NormalNoise tendrilsNoise = context.randomState.getOrCreateNoise(EENoises.CORROCK_TENDRILS);
			PositionalRandomFactory randomFactory = context.randomState.getOrCreateRandomFactory(new ResourceLocation(EndergeticExpansion.MOD_ID, "corrock"));
			BlockState corrock = EEBlocks.END_CORROCK_BLOCK.get().defaultBlockState();
			BlockState eumus = EEBlocks.EUMUS.get().defaultBlockState();
			BlockState speckledCorrock = EEBlocks.SPECKLED_END_CORROCK.get().defaultBlockState();

			class CorrockRule implements SurfaceRule {
				private long lastUpdateXZ = context.lastUpdateXZ - 1L;
				private LastResult lastResult = LastResult.NOOP;

				@Nullable
				@Override
				public BlockState tryApply(int x, int y, int z) {
					if (context.lastUpdateXZ != this.lastUpdateXZ) {
						this.lastUpdateXZ = context.lastUpdateXZ;
						double corrockNoiseValue = corrockNoise.getValue(x, 0.0F, z);
						if (corrockNoiseValue < 0.5F) {
							this.lastResult = LastResult.NOOP;
						} else {
							double tendrilNoiseValue = Math.min(Math.abs(tendrilsNoise.getValue(x, 0.0D, z)), 1.0D);
							double tendrilThreshold = corrockNoiseValue < 0.7D ? 0.125F : 1.5D * (corrockNoiseValue - 0.7D) + 0.125D;
							double tendrilProgress = tendrilThreshold - tendrilNoiseValue;
							if (tendrilProgress >= 0.0D) {
								this.lastResult = LastResult.CORROCK_EUMUS;
							} else if (tendrilProgress >= -0.05D || (tendrilProgress >= -0.1D && randomFactory.at(x, 0, z).nextFloat() <= -5.0D * tendrilProgress)) {
								this.lastResult = LastResult.SPECKLED;
							} else {
								this.lastResult = LastResult.NOOP;
							}
						}
					}

					if (this.lastResult == LastResult.SPECKLED) {
						if (context.stoneDepthAbove <= 1) {
							return speckledCorrock;
						}
					} else if (this.lastResult == LastResult.CORROCK_EUMUS) {
						if (context.stoneDepthAbove <= 1) {
							return corrock;
						} else if (context.stoneDepthAbove <= 1 + context.surfaceDepth) {
							return eumus;
						}
					}

					return null;
				}

				enum LastResult {
					SPECKLED,
					CORROCK_EUMUS,
					NOOP
				}
			}

			return new CorrockRule();
		}
	}

}
