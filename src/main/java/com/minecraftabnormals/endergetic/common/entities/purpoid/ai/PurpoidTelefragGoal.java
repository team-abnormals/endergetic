package com.minecraftabnormals.endergetic.common.entities.purpoid.ai;

import com.minecraftabnormals.endergetic.common.entities.purpoid.PurpoidEntity;
import com.minecraftabnormals.endergetic.common.network.entity.S2CEnablePurpoidFlash;
import com.minecraftabnormals.endergetic.core.EndergeticExpansion;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.PacketDistributor;

import javax.annotation.Nullable;
import java.util.EnumSet;


public class PurpoidTelefragGoal extends Goal {
	private final PurpoidEntity purpoid;
	@Nullable
	private TeleportPattern teleportPattern;

	public PurpoidTelefragGoal(PurpoidEntity purpoid) {
		this.purpoid = purpoid;
		this.setFlags(EnumSet.of(Flag.MOVE, Flag.TARGET));
	}

	@Override
	public boolean canUse() {
		PurpoidEntity purpoid = this.purpoid;
		Entity ridingEntity = purpoid.getVehicle();
		return purpoid.isNoEndimationPlaying() && !purpoid.getTeleportController().isTeleporting() && ridingEntity instanceof LivingEntity && ridingEntity.isAlive() && (!(ridingEntity instanceof Player) || !ridingEntity.isSpectator() && !((Player) ridingEntity).isCreative());
	}

	@Override
	public void start() {
		PurpoidEntity purpoid = this.purpoid;
		this.teleportPattern = new TeleportPattern(purpoid.getRandom());
		purpoid.setTarget((LivingEntity) purpoid.getVehicle());
		purpoid.setAggressive(true);
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
				BlockPos pos = purpoid.blockPosition();
				RandomSource random = purpoid.getRandom();
				Level world = purpoid.level;
				Entity ridingEntity = purpoid.getVehicle();
				if (sky) {
					EntityDimensions size = purpoid.getDimensions(purpoid.getPose());
					for (int i = 0; i < 16; i++) {
						BlockPos randomPos = pos.offset(random.nextInt(32) - random.nextInt(32), random.nextInt(32) + 8, random.nextInt(32) - random.nextInt(32));
						AABB collisionBox = size.makeBoundingBox(randomPos.getX() + 0.5F, randomPos.getY(), randomPos.getZ() + 0.5F);
						if (world.hasChunkAt(randomPos) && world.noCollision(collisionBox) && !world.containsAnyLiquid(collisionBox)) {
							teleportPos = randomPos;
							break;
						}
					}
				} else {
					EntityDimensions size = ridingEntity.getDimensions(ridingEntity.getPose());
					int x = pos.getX();
					int z = pos.getZ();
					for (int i = 0; i < 32; i++) {
						int randomX = x + (random.nextInt(32) - random.nextInt(32));
						int randomZ = z + (random.nextInt(32) - random.nextInt(32));
						BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos(randomX, (int) ridingEntity.getY(), randomZ);
						if (world.hasChunkAt(mutable)) {
							boolean successful = true;
							while (true) {
								int y = mutable.getY();
								if (y > 0 && !world.getBlockState(mutable).getMaterial().blocksMotion()) {
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
								AABB collisionBox = size.makeBoundingBox(mutable.getX() + 0.5F, mutable.getY(), mutable.getZ() + 0.5F);
								if (world.noCollision(collisionBox) && !world.containsAnyLiquid(collisionBox)) {
									teleportPos = mutable;
									break;
								}
							}
						}
					}
				}
				if (teleportPos != null) {
					teleportController.beginTeleportation(purpoid, teleportPos, true);
					ridingEntity.hurt(DamageSource.mobAttack(purpoid), (float) purpoid.getAttributeValue(Attributes.ATTACK_DAMAGE));
					if (ridingEntity instanceof ServerPlayer) {
						EndergeticExpansion.CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) ridingEntity), new S2CEnablePurpoidFlash());
					}
				}
			}
		}
	}

	@Override
	public boolean canContinueToUse() {
		PurpoidEntity purpoid = this.purpoid;
		Entity ridingEntity = purpoid.getVehicle();
		return ridingEntity instanceof LivingEntity && ridingEntity.isAlive() && (!(ridingEntity instanceof Player) || !ridingEntity.isSpectator() && !((Player) ridingEntity).isCreative());
	}

	@Override
	public void stop() {
		this.teleportPattern = null;
		PurpoidEntity purpoid = this.purpoid;
		LivingEntity livingentity = purpoid.getTarget();
		if (!EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(livingentity)) {
			purpoid.setTarget(null);
		}
		purpoid.setAggressive(false);
	}

	@Override
	public boolean requiresUpdateEveryTick() {
		return true;
	}

	static class TeleportPattern {
		private final boolean[] sequence;
		private int index;

		TeleportPattern(RandomSource random) {
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
