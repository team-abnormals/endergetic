package com.teamabnormals.endergetic.common.item;

import com.teamabnormals.endergetic.core.registry.EEEntityTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.ForgeSpawnEggItem;

import javax.annotation.Nullable;

public final class EetleSpawnEggItem extends ForgeSpawnEggItem {

	public EetleSpawnEggItem(int primaryColor, int secondaryColor, Properties properties) {
		super(EEEntityTypes.CHARGER_EETLE, primaryColor, secondaryColor, properties);
	}

	@Override
	public EntityType<?> getType(@Nullable CompoundTag compound) {
		if (compound != null && compound.contains("EntityTag", 10)) {
			return super.getType(compound);
		}
		return Math.random() < 0.6F ? EEEntityTypes.CHARGER_EETLE.get() : EEEntityTypes.GLIDER_EETLE.get();
	}

}
