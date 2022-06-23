package com.minecraftabnormals.endergetic.common.entities.purpoid.ai;

import com.minecraftabnormals.endergetic.common.entities.purpoid.PurpoidEntity;
import net.minecraft.core.BlockPos;

import java.util.Random;

public class PurpoidRandomTeleportGoal extends AbstractPurpoidTeleportGoal {

	public PurpoidRandomTeleportGoal(PurpoidEntity purpoid) {
		super(purpoid);
	}

	@Override
	BlockPos generateTeleportPos(PurpoidEntity purpoid, Random random) {
		return purpoid.blockPosition().offset(random.nextInt(17) - random.nextInt(17), random.nextInt(17) - random.nextInt(17), random.nextInt(17) - random.nextInt(17));
	}

}
