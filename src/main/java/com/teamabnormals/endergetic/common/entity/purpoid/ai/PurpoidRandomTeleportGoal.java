package com.teamabnormals.endergetic.common.entity.purpoid.ai;

import com.teamabnormals.endergetic.common.entity.purpoid.Purpoid;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;

public class PurpoidRandomTeleportGoal extends AbstractPurpoidTeleportGoal {

	public PurpoidRandomTeleportGoal(Purpoid purpoid) {
		super(purpoid);
	}

	@Override
	public boolean canUse() {
		return this.purpoid.forcedRelativeTeleportingPos == null && super.canUse();
	}

	@Override
	protected BlockPos generateTeleportPos(Purpoid purpoid, RandomSource random) {
		return purpoid.blockPosition().offset(random.nextInt(17) - random.nextInt(17), random.nextInt(17) - random.nextInt(17), random.nextInt(17) - random.nextInt(17));
	}

}
