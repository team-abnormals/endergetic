package com.minecraftabnormals.endergetic.common.entities.purpoid.ai;

import com.minecraftabnormals.endergetic.common.entities.purpoid.PurpoidEntity;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PurpoidTeleportToFlowerGoal extends AbstractPurpoidTeleportGoal {

	public PurpoidTeleportToFlowerGoal(PurpoidEntity purpoid) {
		super(purpoid);
	}

	@Override
	public boolean shouldExecute() {
		return !this.purpoid.hasRestCooldown() && super.shouldExecute();
	}

	@Override
	protected void beginTeleportation(PurpoidEntity purpoid, BlockPos pos) {
		super.beginTeleportation(purpoid, pos);
		purpoid.setFlowerPos(pos.down());
	}

	@Nullable
	@Override
	BlockPos generateTeleportPos(PurpoidEntity purpoid, Random random) {
		BlockPos pos = purpoid.getPosition();
		int originX = pos.getX();
		int originY = pos.getY();
		int originZ = pos.getZ();
		BlockPos.Mutable mutable = new BlockPos.Mutable();
		World world = purpoid.world;
		List<BlockPos> flowerPositions = new ArrayList<>();
		for (int x = originX - 8; x <= originX + 8; x++) {
			for (int y = originY - 12; y <= originY + 12; y++) {
				for (int z = originZ - 8; z <= originZ + 8; z++) {
					mutable.setPos(x, y, z);
					if (world.getBlockState(mutable).getBlock() == Blocks.CHORUS_FLOWER) {
						flowerPositions.add(mutable.up());
					}
				}
			}
		}
		return flowerPositions.isEmpty() ? null : flowerPositions.get(random.nextInt(flowerPositions.size()));
	}

}
