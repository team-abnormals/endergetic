package com.teamabnormals.endergetic.common.entities.purpoid.ai;

import com.teamabnormals.endergetic.common.entities.purpoid.PurpoidEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;

public class PurpoidRandomTeleportGoal extends AbstractPurpoidTeleportGoal {

	public PurpoidRandomTeleportGoal(PurpoidEntity purpoid) {
		super(purpoid);
	}

	@Override
	public boolean canUse() {
		return this.purpoid.forcedRelativeTeleportingPos == null && super.canUse();
	}

	@Override
	protected BlockPos generateTeleportPos(PurpoidEntity purpoid, RandomSource random) {
		return purpoid.blockPosition().offset(random.nextInt(17) - random.nextInt(17), random.nextInt(17) - random.nextInt(17), random.nextInt(17) - random.nextInt(17));
	}

}
