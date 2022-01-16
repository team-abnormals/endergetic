package com.minecraftabnormals.endergetic.core.registry.other;

import com.minecraftabnormals.endergetic.core.registry.EEEntities;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;

public final class EEEntityAttributes {

	public static void putAttributes() {
		GlobalEntityTypeAttributes.put(EEEntities.BOOFLO.get(),
				MobEntity.createMobAttributes()
						.add(Attributes.ATTACK_DAMAGE, 7.0F)
						.add(Attributes.MAX_HEALTH, 40.0F)
						.add(Attributes.MOVEMENT_SPEED, 1.05F)
						.add(Attributes.ARMOR, 4.0F)
						.add(Attributes.FOLLOW_RANGE, 22.0F)
						.add(Attributes.KNOCKBACK_RESISTANCE, 0.6F)
						.build()
		);
		GlobalEntityTypeAttributes.put(EEEntities.BOOFLO_ADOLESCENT.get(),
				MobEntity.createMobAttributes()
						.add(Attributes.MAX_HEALTH, 10.0F)
						.add(Attributes.MOVEMENT_SPEED, 1.7F)
						.add(Attributes.FOLLOW_RANGE, 25.0F)
						.build()
		);
		GlobalEntityTypeAttributes.put(EEEntities.BOOFLO_BABY.get(),
				MobEntity.createMobAttributes()
						.add(Attributes.MAX_HEALTH, 5.0F)
						.add(Attributes.MOVEMENT_SPEED, 0.85F)
						.add(Attributes.FOLLOW_RANGE, 18.0F)
						.build()
		);
		GlobalEntityTypeAttributes.put(EEEntities.PUFF_BUG.get(),
				MobEntity.createMobAttributes()
						.add(Attributes.ATTACK_DAMAGE, 5.0F)
						.add(Attributes.FLYING_SPEED, 0.75F)
						.add(Attributes.ATTACK_KNOCKBACK, 0.15F)
						.add(Attributes.MAX_HEALTH, 8.0F)
						.add(Attributes.FOLLOW_RANGE, 16.0F)
						.build()
		);
		GlobalEntityTypeAttributes.put(EEEntities.POISE_CLUSTER.get(),
				LivingEntity.createLivingAttributes().build()
		);
	}

}