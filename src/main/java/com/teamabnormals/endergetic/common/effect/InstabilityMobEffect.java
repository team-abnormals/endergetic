package com.teamabnormals.endergetic.common.effect;

import com.teamabnormals.endergetic.common.network.entity.S2CEnablePurpoidFlash;
import com.teamabnormals.endergetic.core.EndergeticExpansion;
import com.teamabnormals.endergetic.core.other.tags.EEEntityTypeTags;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.network.PacketDistributor;

public class InstabilityMobEffect extends MobEffect {

	public InstabilityMobEffect() {
		super(MobEffectCategory.NEUTRAL, 0x9868C4);
	}

	@Override
	public void applyEffectTick(LivingEntity entity, int amplifier) {
		if (entity.getType().is(EEEntityTypeTags.TELEPORT_IMMUNE)) return;
		double x = entity.getX();
		double y = entity.getY();
		double z = entity.getZ();
		RandomSource random = entity.getRandom();
		for (int i = 0; i < 32; i++) {
			double randomX = x + (random.nextDouble() - 0.5D) * 16.0D;
			double randomY = y + (random.nextInt(17) - 8);
			double randomZ = z + (random.nextDouble() - 0.5D) * 32.0D;
			if (entity.randomTeleport(randomX, randomY, randomZ, false)) {
				if (entity instanceof ServerPlayer player) {
					EndergeticExpansion.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new S2CEnablePurpoidFlash());
				}
				break;
			}
		}
	}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) {
		return duration % (amplifier > 1 ? 60 : 20) == 0;
	}

}
