package com.teamabnormals.endergetic.core.data.server;

import com.teamabnormals.blueprint.common.world.modification.chunk.ChunkGeneratorModifierProvider;
import com.teamabnormals.blueprint.common.world.modification.chunk.modifiers.SurfaceRuleModifier;
import com.teamabnormals.blueprint.core.registry.BlueprintSurfaceRules;
import com.teamabnormals.endergetic.core.EndergeticExpansion;
import com.teamabnormals.endergetic.core.registry.EEBiomes;
import com.teamabnormals.endergetic.core.registry.EEBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Noises;

import static net.minecraft.world.level.levelgen.SurfaceRules.*;

public final class EEChunkGeneratorModifierProvider extends ChunkGeneratorModifierProvider {

	public EEChunkGeneratorModifierProvider(DataGenerator dataGenerator) {
		super(dataGenerator, EndergeticExpansion.MOD_ID);
	}

	@Override
	protected void registerEntries() {
		ConditionSource inSlice = new BlueprintSurfaceRules.ModdednessSliceConditionSource(new ResourceLocation(EndergeticExpansion.MOD_ID, "poise_forest"));
		ConditionSource inPoiseForest = isBiome(EEBiomes.POISE_FOREST.getKey());
		RuleSource poismoss = ifTrue(noiseCondition(Noises.SURFACE, 0.43F, Double.MAX_VALUE), sequence(ifTrue(ON_FLOOR, state(EEBlocks.POISMOSS.get().defaultBlockState())), state(Blocks.END_STONE.defaultBlockState())));
		RuleSource eumus = ifTrue(noiseCondition(Noises.SURFACE, 0.35F, 0.43F), ifTrue(UNDER_FLOOR, state(EEBlocks.EUMUS.get().defaultBlockState())));
		RuleSource poismossAndEumus = sequence(poismoss, eumus);
		RuleSource poiseForest = ifTrue(inSlice, ifTrue(inPoiseForest, poismossAndEumus));
		this.entry("poise_forest_surface_rule")
				.selects("the_end")
				.addModifier(new SurfaceRuleModifier(poiseForest, false));

//		RuleSource corrockPlains = SurfaceRules.ifTrue(SurfaceRules.isBiome(Biomes.END_HIGHLANDS, Biomes.END_MIDLANDS), EESurfaceRules.CorrockRuleSource.INSTANCE);
//		this.entry("corrock_plains_surface")
//				.selects("the_end")
//				.addModifier(new SurfaceRuleModifier(corrockPlains, false));
	}

}
