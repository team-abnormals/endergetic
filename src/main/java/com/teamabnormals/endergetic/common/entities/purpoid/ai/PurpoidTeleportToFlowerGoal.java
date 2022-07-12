package com.teamabnormals.endergetic.common.entities.purpoid.ai;

import com.teamabnormals.endergetic.common.entities.purpoid.PurpoidEntity;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class PurpoidTeleportToFlowerGoal extends AbstractPurpoidTeleportGoal {

	public PurpoidTeleportToFlowerGoal(PurpoidEntity purpoid) {
		super(purpoid);
	}

	@Override
	public boolean canUse() {
		return !this.purpoid.hasRestCooldown() && super.canUse();
	}

	@Override
	protected void beginTeleportation(PurpoidEntity purpoid, BlockPos pos) {
		super.beginTeleportation(purpoid, pos);
		purpoid.setFlowerPos(pos.below());
	}

	@Nullable
	@Override
	BlockPos generateTeleportPos(PurpoidEntity purpoid, RandomSource random) {
		BlockPos pos = purpoid.blockPosition();
		int originX = pos.getX();
		int originY = pos.getY();
		int originZ = pos.getZ();
		BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
		Level world = purpoid.level;
		List<BlockPos> flowerPositions = new ArrayList<>();
		for (int x = originX - 8; x <= originX + 8; x++) {
			for (int y = originY - 12; y <= originY + 12; y++) {
				for (int z = originZ - 8; z <= originZ + 8; z++) {
					mutable.set(x, y, z);
					if (world.getBlockState(mutable).getBlock() == Blocks.CHORUS_FLOWER) {
						flowerPositions.add(mutable.above());
					}
				}
			}
		}
		return flowerPositions.isEmpty() ? null : flowerPositions.get(random.nextInt(flowerPositions.size()));
	}

	@Override
	public boolean requiresUpdateEveryTick() {
		return true;
	}

}
