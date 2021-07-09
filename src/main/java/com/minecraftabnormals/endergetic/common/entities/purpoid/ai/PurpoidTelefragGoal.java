package com.minecraftabnormals.endergetic.common.entities.purpoid.ai;

import com.minecraftabnormals.endergetic.common.entities.purpoid.PurpoidEntity;
import com.minecraftabnormals.endergetic.common.entities.purpoid.PurpoidSize;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.Random;

public class PurpoidTelefragGoal extends Goal {
	private final PurpoidEntity purpoid;
	@Nullable
	private TeleportPattern teleportPattern;

	public PurpoidTelefragGoal(PurpoidEntity purpoid) {
		this.purpoid = purpoid;
		this.setMutexFlags(EnumSet.of(Flag.MOVE, Flag.TARGET));
	}

	@Override
	public boolean shouldExecute() {
		PurpoidEntity purpoid = this.purpoid;
		Entity ridingEntity = purpoid.getRidingEntity();
		return purpoid.getSize() == PurpoidSize.NORMAL && purpoid.isNoEndimationPlaying() && !purpoid.getTeleportController().isTeleporting() && ridingEntity instanceof LivingEntity && ridingEntity.isAlive() && (!(ridingEntity instanceof PlayerEntity) || !ridingEntity.isSpectator() && !((PlayerEntity) ridingEntity).isCreative());
	}

	@Override
	public void startExecuting() {
		PurpoidEntity purpoid = this.purpoid;
		this.teleportPattern = new TeleportPattern(purpoid.getRNG());
		purpoid.setAttackTarget((LivingEntity) purpoid.getRidingEntity());
		purpoid.setAggroed(true);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void tick() {
		PurpoidEntity purpoid = this.purpoid;
		if (purpoid.isNoEndimationPlaying()) {
			PurpoidEntity.TeleportController teleportController = purpoid.getTeleportController();
			if (!teleportController.isTeleporting()) {
				boolean sky = this.teleportPattern.next();
				BlockPos teleportPos = null;
				BlockPos pos = purpoid.getPosition();
				Random random = purpoid.getRNG();
				World world = purpoid.world;
				Entity ridingEntity = purpoid.getRidingEntity();
				if (sky) {
					EntitySize size = purpoid.getSize(purpoid.getPose());
					for (int i = 0; i < 16; i++) {
						BlockPos randomPos = pos.add(random.nextInt(32) - random.nextInt(32), random.nextInt(32) + 8, random.nextInt(32) - random.nextInt(32));
						AxisAlignedBB collisionBox = size.func_242285_a(randomPos.getX() + 0.5F, randomPos.getY(), randomPos.getZ() + 0.5F);
						if (world.isBlockLoaded(randomPos) && world.hasNoCollisions(collisionBox) && !world.containsAnyLiquid(collisionBox)) {
							teleportPos = randomPos;
							break;
						}
					}
				} else {
					EntitySize size = ridingEntity.getSize(ridingEntity.getPose());
					int x = pos.getX();
					int z = pos.getZ();
					for (int i = 0; i < 32; i++) {
						int randomX = x + (random.nextInt(32) - random.nextInt(32));
						int randomZ = z + (random.nextInt(32) - random.nextInt(32));
						BlockPos.Mutable mutable = new BlockPos.Mutable(randomX, (int) ridingEntity.getPosY(), randomZ);
						if (world.isBlockLoaded(mutable)) {
							boolean successful = true;
							while (true) {
								int y = mutable.getY();
								if (y > 0 && !world.getBlockState(mutable).getMaterial().blocksMovement()) {
									mutable.setY(y - 1);
								} else if (y <= 0) {
									successful = false;
									break;
								} else {
									break;
								}
							}
							if (successful) {
								mutable.move(Direction.UP);
								AxisAlignedBB collisionBox = size.func_242285_a(mutable.getX() + 0.5F, mutable.getY(), mutable.getZ() + 0.5F);
								if (world.hasNoCollisions(collisionBox) && !world.containsAnyLiquid(collisionBox)) {
									teleportPos = mutable;
									break;
								}
							}
						}
					}
				}
				if (teleportPos != null) {
					teleportController.beginTeleportation(purpoid, teleportPos, true);
					ridingEntity.attackEntityFrom(DamageSource.causeMobDamage(purpoid), (float) purpoid.getAttributeValue(Attributes.ATTACK_DAMAGE));
				}
			}
		}
	}

	@Override
	public boolean shouldContinueExecuting() {
		PurpoidEntity purpoid = this.purpoid;
		Entity ridingEntity = purpoid.getRidingEntity();
		return purpoid.getSize() == PurpoidSize.NORMAL && ridingEntity instanceof LivingEntity && ridingEntity.isAlive() && (!(ridingEntity instanceof PlayerEntity) || !ridingEntity.isSpectator() && !((PlayerEntity) ridingEntity).isCreative());
	}

	@Override
	public void resetTask() {
		this.teleportPattern = null;
		PurpoidEntity purpoid = this.purpoid;
		LivingEntity livingentity = purpoid.getAttackTarget();
		if (!EntityPredicates.CAN_AI_TARGET.test(livingentity)) {
			purpoid.setAttackTarget(null);
		}
		purpoid.setAggroed(false);
	}

	static class TeleportPattern {
		private final boolean[] sequence;
		private int index;

		TeleportPattern(Random random) {
			if (random.nextBoolean()) {
				boolean first = random.nextBoolean();
				boolean second = random.nextBoolean();
				this.sequence = new boolean[] {first, second, first == second ? !first : random.nextBoolean()};
			} else {
				boolean first = random.nextBoolean();
				this.sequence = new boolean[] {first, !first};
			}
		}

		private boolean next() {
			boolean[] sequence = this.sequence;
			return sequence[this.index++ % sequence.length];
		}
	}
}
