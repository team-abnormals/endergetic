package com.teamabnormals.endergetic.common.entity.purpoid.ai;

import com.teamabnormals.blueprint.core.util.MathUtil;
import com.teamabnormals.endergetic.common.entity.purpoid.Purpoid;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.HoverRandomPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class PurpoidMoveRandomGoal extends Goal {
	private final Purpoid purpoid;
	private int cooldown;
	private double x, y, z;

	public PurpoidMoveRandomGoal(Purpoid purpoid) {
		this.purpoid = purpoid;
		this.setFlags(EnumSet.of(Flag.MOVE));
	}

	@Override
	public boolean canUse() {
		if (this.cooldown > 0) {
			this.cooldown--;
		} else {
			Purpoid purpoid = this.purpoid;
			Level level = purpoid.level();
			int blockX = purpoid.getBlockX();
			int blockZ = purpoid.getBlockZ();
			int heightBelow = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, blockX, blockZ);
			boolean isAirBelow = level.getBlockState(new BlockPos(blockX, heightBelow, blockZ)).isAir();
			int blockY = purpoid.getBlockY();
			Vec3 randomPos;
			if ((!isAirBelow && blockY - heightBelow > 15) || (isAirBelow && blockY > level.getSeaLevel() + 15)) {
				RandomSource random = purpoid.getRandom();
				randomPos = purpoid.position().add(new Vec3(MathUtil.makeNegativeRandomly(random.nextInt(17), random), -random.nextInt(32), MathUtil.makeNegativeRandomly(random.nextInt(17), random)));
			} else {
				Vec3 view = purpoid.getViewVector(0.0F);
				randomPos = HoverRandomPos.getPos(purpoid, 32, 16, view.x, view.z, ((float) Math.PI / 2F), 3, 1);
			}
			if (randomPos != null) {
				this.x = randomPos.x();
				this.y = randomPos.y();
				this.z = randomPos.z();
				return true;
			}
		}
		return false;
	}

	@Override
	public void start() {
		Purpoid purpoid = this.purpoid;
		purpoid.getMoveControl().setWantedPosition(this.x, this.y, this.z, 1.0F);
		this.cooldown = purpoid.getRandom().nextInt(81) + (int) (40 * purpoid.getSize().getScale());
	}

	@Override
	public boolean canContinueToUse() {
		return this.purpoid.getMoveControl().hasWanted();
	}

	@Override
	public void stop() {
		this.purpoid.getNavigation().stop();
	}
}
