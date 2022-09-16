package com.teamabnormals.endergetic.common.entities.purpoid.ai;

import com.teamabnormals.endergetic.common.entities.purpoid.PurpoidEntity;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

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
		purpoid.setRestingPos(pos.below(3));
	}

	@Nullable
	@Override
	protected BlockPos generateTeleportPos(PurpoidEntity purpoid, RandomSource random) {
		BlockPos pos = purpoid.blockPosition();
		int originX = pos.getX();
		int originY = pos.getY();
		int originZ = pos.getZ();
		BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
		Level level = purpoid.level;
		List<BlockPos> teleportPositions = new ArrayList<>();
		for (int x = originX - 12; x <= originX + 12; x++) {
			for (int y = originY - 12; y <= originY + 12; y++) {
				for (int z = originZ - 12; z <= originZ + 12; z++) {
					mutable.set(x, y, z);
					if (level.getBlockState(mutable).getBlock() == Blocks.CHORUS_FLOWER && level.getBlockState(mutable.setY(mutable.getY() + 1)).isAir() && level.getBlockState(mutable.setY(mutable.getY() + 1)).isAir() && level.getBlockState(mutable.setY(mutable.getY() + 1)).isAir()) {
						if (isRangeNotBlockedByEntities(purpoid, mutable.getX(), mutable.getY() - 3, mutable.getZ(), mutable.getY()))
							teleportPositions.add(mutable.immutable());
					}
				}
			}
		}
		return teleportPositions.isEmpty() ? null : teleportPositions.get(random.nextInt(teleportPositions.size()));
	}

	public static boolean isRangeNotBlockedByEntities(PurpoidEntity purpoid, float minX, float minY, float minZ, float maxY) {
		return purpoid.level.getEntitiesOfClass(Entity.class, new AABB(minX, minY, minZ, minX + 1.0D, maxY, minZ + 1.0D), entity -> entity != purpoid).isEmpty();
	}

	@Override
	public boolean requiresUpdateEveryTick() {
		return true;
	}

}
