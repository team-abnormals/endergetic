package com.teamabnormals.endergetic.core.registry;

import com.teamabnormals.endergetic.core.EndergeticExpansion;
import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Noises;
import net.minecraft.world.level.levelgen.PositionalRandomFactory;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

public final class EESurfaceRules extends SurfaceRules {
	public static final DeferredRegister<Codec<? extends RuleSource>> RULES = DeferredRegister.create(Registry.RULE_REGISTRY, EndergeticExpansion.MOD_ID);

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
			NormalNoise normalNoise = context.randomState.getOrCreateNoise(Noises.SURFACE);
			PositionalRandomFactory randomFactory = context.randomState.getOrCreateRandomFactory(new ResourceLocation(EndergeticExpansion.MOD_ID, "corrock"));
			BlockState corrock = EEBlocks.CORROCK_END_BLOCK.get().defaultBlockState();
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
						double noise = normalNoise.getValue(x, 0.0F, z);
						if (noise > 0.23F) {
							if (noise <= 0.255F) {
								if ((randomFactory.at(x, 0, z)).nextFloat() < 0.35F) {
									this.lastResult = LastResult.SPECKLED;
								} else {
									this.lastResult = LastResult.CORROCK_EUMUS;
								}
							} else {
								this.lastResult = LastResult.CORROCK_EUMUS;
							}
						} else if (noise > 0.155F && (randomFactory.at(x, 0, z)).nextFloat() <= Math.abs(noise - 0.155F)) {
							this.lastResult = LastResult.SPECKLED;
						} else {
							this.lastResult = LastResult.NOOP;
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
