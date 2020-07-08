package endergeticexpansion.core.registry.other;

import endergeticexpansion.core.registry.EEEntities;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;

public class EEEntityAttributes {
	
	public static void putAttributes() {
		GlobalEntityTypeAttributes.put(EEEntities.BOOFLO.get(),
			MobEntity.func_233666_p_()
				.func_233815_a_(Attributes.ATTACK_DAMAGE, 7.0F)
				.func_233815_a_(Attributes.MAX_HEALTH, 40.0F)
				.func_233815_a_(Attributes.MOVEMENT_SPEED, 1.05F)
				.func_233815_a_(Attributes.ARMOR, 4.0F)
				.func_233815_a_(Attributes.FOLLOW_RANGE, 22.0F)
				.func_233815_a_(Attributes.KNOCKBACK_RESISTANCE, 0.6F)
			.func_233813_a_()
		);
		GlobalEntityTypeAttributes.put(EEEntities.BOOFLO_ADOLESCENT.get(),
			MobEntity.func_233666_p_()
				.func_233815_a_(Attributes.MAX_HEALTH, 10.0F)
				.func_233815_a_(Attributes.MOVEMENT_SPEED, 1.7F)
				.func_233815_a_(Attributes.FOLLOW_RANGE, 25.0F)
			.func_233813_a_()
		);
		GlobalEntityTypeAttributes.put(EEEntities.BOOFLO_BABY.get(),
			MobEntity.func_233666_p_()
				.func_233815_a_(Attributes.MAX_HEALTH, 5.0F)
				.func_233815_a_(Attributes.MOVEMENT_SPEED, 0.85F)
				.func_233815_a_(Attributes.FOLLOW_RANGE, 18.0F)
			.func_233813_a_()
		);
		GlobalEntityTypeAttributes.put(EEEntities.PUFF_BUG.get(),
			MobEntity.func_233666_p_()
				.func_233815_a_(Attributes.ATTACK_DAMAGE, 5.0F)
				.func_233815_a_(Attributes.FLYING_SPEED, 0.75F)
				.func_233815_a_(Attributes.ATTACK_KNOCKBACK, 0.15F)
				.func_233815_a_(Attributes.MAX_HEALTH, 8.0F)
				.func_233815_a_(Attributes.FOLLOW_RANGE, 16.0F)
			.func_233813_a_()
		);
	}
	
}