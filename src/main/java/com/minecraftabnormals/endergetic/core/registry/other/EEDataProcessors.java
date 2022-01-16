package com.minecraftabnormals.endergetic.core.registry.other;

import com.google.common.collect.Maps;
import com.minecraftabnormals.abnormals_core.common.world.storage.tracking.IDataProcessor;
import com.minecraftabnormals.abnormals_core.common.world.storage.tracking.TrackedData;
import com.minecraftabnormals.abnormals_core.common.world.storage.tracking.TrackedDataManager;
import com.minecraftabnormals.endergetic.common.entities.bolloom.BalloonOrder;
import com.minecraftabnormals.endergetic.core.EndergeticExpansion;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;

import java.util.Map;
import java.util.UUID;

public final class EEDataProcessors {
	private static final IDataProcessor<Map<UUID, BalloonOrder>> ORDER_PROCESSOR = new IDataProcessor<Map<UUID, BalloonOrder>>() {

		@Override
		public CompoundNBT write(Map<UUID, BalloonOrder> map) {
			ListNBT entries = new ListNBT();
			map.forEach((uuid, balloonOrder) -> {
				CompoundNBT entry = new CompoundNBT();
				entry.putUUID("UUID", uuid);
				entry.putInt("Order", balloonOrder.ordinal());
				entries.add(entry);
			});
			CompoundNBT compound = new CompoundNBT();
			compound.put("Entries", entries);
			return compound;
		}

		@Override
		public Map<UUID, BalloonOrder> read(CompoundNBT compound) {
			Map<UUID, BalloonOrder> map = Maps.newHashMap();
			compound.getList("Entries", Constants.NBT.TAG_COMPOUND).forEach(nbt -> {
				CompoundNBT entry = (CompoundNBT) nbt;
				if (entry.contains("UUID", Constants.NBT.TAG_INT_ARRAY) && entry.contains("Order", Constants.NBT.TAG_INT)) {
					map.put(entry.getUUID("UUID"), BalloonOrder.byOrdinal(entry.getInt("Order")));
				}
			});
			return map;
		}

	};

	public static final TrackedData<Map<UUID, BalloonOrder>> ORDER_DATA = TrackedData.Builder.create(ORDER_PROCESSOR, Maps::newHashMap).build();

	public static void registerTrackedData() {
		TrackedDataManager.INSTANCE.registerData(new ResourceLocation(EndergeticExpansion.MOD_ID, "ballooon_order"), ORDER_DATA);
	}
}
