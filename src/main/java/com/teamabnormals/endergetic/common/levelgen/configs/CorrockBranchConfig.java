package com.teamabnormals.endergetic.common.levelgen.configs;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

import java.util.List;
import java.util.Set;

public final class CorrockBranchConfig implements FeatureConfiguration {
	public static final Codec<CorrockBranchConfig> CODEC = RecordCodecBuilder.create(instance -> {
		return instance.group(
				BlockState.CODEC.listOf().fieldOf("valid_ground_states").forGetter(config -> ImmutableList.copyOf(config.validGround)),
				Codec.FLOAT.fieldOf("crown_chance").forGetter(config -> config.crownChance),
				Codec.FLOAT.fieldOf("decorated_branch_chance").forGetter(config -> config.decoratedBranchChance)
		).apply(instance, CorrockBranchConfig::new);
	});
	private final Set<BlockState> validGround;
	private final float crownChance;
	private final float decoratedBranchChance;

	public CorrockBranchConfig(List<BlockState> validGround, float crownChance, float decoratedBranchChance) {
		this.validGround = Sets.newHashSet(validGround);
		this.crownChance = crownChance;
		this.decoratedBranchChance = decoratedBranchChance;
	}

	public boolean isValidGround(BlockState state) {
		return this.validGround.contains(state);
	}

	public float getCrownChance() {
		return this.crownChance;
	}

	public float getDecoratedBranchChance() {
		return this.decoratedBranchChance;
	}
}
