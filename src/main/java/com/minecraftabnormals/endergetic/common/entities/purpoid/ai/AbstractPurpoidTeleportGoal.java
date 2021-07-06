package com.minecraftabnormals.endergetic.common.entities.purpoid.ai;

import com.minecraftabnormals.endergetic.common.entities.purpoid.PurpoidEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.Random;

public abstract class AbstractPurpoidTeleportGoal extends Goal {
	protected final PurpoidEntity purpoid;
	private int notMovingTicks;

	protected AbstractPurpoidTeleportGoal(PurpoidEntity purpoid) {
		this.purpoid = purpoid;
		this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean shouldExecute() {
		PurpoidEntity purpoid = this.purpoid;
		if (!purpoid.getMoveHelper().isUpdating()) {
			this.notMovingTicks++;
		} else {
			this.notMovingTicks = 0;
		}
		if (this.notMovingTicks >= 20 && !purpoid.isBoosting() && !purpoid.hasTeleportCooldown() && purpoid.isNoEndimationPlaying()) {
			BlockPos randomPos = this.generateTeleportPos(purpoid, purpoid.getRNG());
			World world = purpoid.world;
			if (randomPos != null && world.isBlockLoaded(randomPos)) {
				AxisAlignedBB collisionBox = purpoid.getSize(purpoid.getPose()).func_242285_a(randomPos.getX() + 0.5F, randomPos.getY(), randomPos.getZ() + 0.5F);
				if (world.hasNoCollisions(collisionBox) && world.checkNoEntityCollision(purpoid, VoxelShapes.create(collisionBox)) && !world.containsAnyLiquid(collisionBox)) {
					this.beginTeleportation(purpoid, randomPos);
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean shouldContinueExecuting() {
		return this.purpoid.getTeleportController().isTeleporting();
	}

	protected void beginTeleportation(PurpoidEntity purpoid, BlockPos pos) {
		purpoid.resetTeleportCooldown();
		purpoid.getTeleportController().beginTeleportation(purpoid, pos);
	}

	@Nullable
	abstract BlockPos generateTeleportPos(PurpoidEntity purpoid, Random random);
}
