package com.minecraftabnormals.endergetic.core.registry.other;

import com.minecraftabnormals.endergetic.core.registry.EEEntities;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;

public final class EEEntityAttributes {

	public static void putAttributes() {
		GlobalEntityTypeAttributes.put(EEEntities.BOOFLO.get(),
				MobEntity.func_233666_p_()
						.createMutableAttribute(Attributes.ATTACK_DAMAGE, 7.0F)
						.createMutableAttribute(Attributes.MAX_HEALTH, 40.0F)
						.createMutableAttribute(Attributes.MOVEMENT_SPEED, 1.05F)
						.createMutableAttribute(Attributes.ARMOR, 4.0F)
						.createMutableAttribute(Attributes.FOLLOW_RANGE, 22.0F)
						.createMutableAttribute(Attributes.KNOCKBACK_RESISTANCE, 0.6F)
						.create()
		);
		GlobalEntityTypeAttributes.put(EEEntities.BOOFLO_ADOLESCENT.get(),
				MobEntity.func_233666_p_()
						.createMutableAttribute(Attributes.MAX_HEALTH, 10.0F)
						.createMutableAttribute(Attributes.MOVEMENT_SPEED, 1.7F)
						.createMutableAttribute(Attributes.FOLLOW_RANGE, 25.0F)
						.create()
		);
		GlobalEntityTypeAttributes.put(EEEntities.BOOFLO_BABY.get(),
				MobEntity.func_233666_p_()
						.createMutableAttribute(Attributes.MAX_HEALTH, 5.0F)
						.createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.85F)
						.createMutableAttribute(Attributes.FOLLOW_RANGE, 18.0F)
						.create()
		);
		GlobalEntityTypeAttributes.put(EEEntities.PUFF_BUG.get(),
				MobEntity.func_233666_p_()
						.createMutableAttribute(Attributes.ATTACK_DAMAGE, 5.0F)
						.createMutableAttribute(Attributes.FLYING_SPEED, 0.75F)
						.createMutableAttribute(Attributes.ATTACK_KNOCKBACK, 0.15F)
						.createMutableAttribute(Attributes.MAX_HEALTH, 8.0F)
						.createMutableAttribute(Attributes.FOLLOW_RANGE, 16.0F)
						.create()
		);
		GlobalEntityTypeAttributes.put(EEEntities.POISE_CLUSTER.get(),
				LivingEntity.registerAttributes().create()
		);
		GlobalEntityTypeAttributes.put(EEEntities.CHARGER_EETLE.get(),
				MobEntity.func_233666_p_()
						.createMutableAttribute(Attributes.ATTACK_DAMAGE, 6.0F)
						.createMutableAttribute(Attributes.ATTACK_KNOCKBACK, 1.0F)
						.createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.2F)
						.createMutableAttribute(Attributes.ARMOR, 4.0F)
						.createMutableAttribute(Attributes.MAX_HEALTH, 30.0F)
						.createMutableAttribute(Attributes.FOLLOW_RANGE, 32.0F)
						.create()
		);
	}

}