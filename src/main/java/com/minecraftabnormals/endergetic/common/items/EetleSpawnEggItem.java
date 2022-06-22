package com.minecraftabnormals.endergetic.common.items;

import com.minecraftabnormals.abnormals_core.common.items.AbnormalsSpawnEggItem;
import com.minecraftabnormals.endergetic.core.registry.EEEntities;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;

import javax.annotation.Nullable;

import net.minecraft.item.Item.Properties;

public final class EetleSpawnEggItem extends AbnormalsSpawnEggItem {

	public EetleSpawnEggItem(int primaryColor, int secondaryColor, Properties properties) {
		super(EEEntities.CHARGER_EETLE::get, primaryColor, secondaryColor, properties);
	}

	@Override
	public EntityType<?> getType(@Nullable CompoundNBT compound) {
		if (compound != null && compound.contains("EntityTag", 10)) {
			return super.getType(compound);
		}
		return random.nextFloat() < 0.6F ? EEEntities.CHARGER_EETLE.get() : EEEntities.GLIDER_EETLE.get();
	}

}
