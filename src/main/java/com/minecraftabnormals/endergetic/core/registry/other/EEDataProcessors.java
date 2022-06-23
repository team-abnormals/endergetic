package com.minecraftabnormals.endergetic.core.registry.other;

import com.google.common.collect.Maps;
import com.minecraftabnormals.endergetic.common.entities.bolloom.BalloonOrder;
import com.minecraftabnormals.endergetic.core.EndergeticExpansion;
import com.teamabnormals.blueprint.common.world.storage.tracking.DataProcessors;
import com.teamabnormals.blueprint.common.world.storage.tracking.IDataProcessor;
import com.teamabnormals.blueprint.common.world.storage.tracking.SyncType;
import com.teamabnormals.blueprint.common.world.storage.tracking.TrackedData;
import com.teamabnormals.blueprint.common.world.storage.tracking.TrackedDataManager;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;
import java.util.UUID;

public final class EEDataProcessors {
	private static final IDataProcessor<Map<UUID, BalloonOrder>> ORDER_PROCESSOR = new IDataProcessor<>() {

		@Override
		public CompoundTag write(Map<UUID, BalloonOrder> map) {
			ListTag entries = new ListTag();
			map.forEach((uuid, balloonOrder) -> {
				CompoundTag entry = new CompoundTag();
				entry.putUUID("UUID", uuid);
				entry.putInt("Order", balloonOrder.ordinal());
				entries.add(entry);
			});
			CompoundTag compound = new CompoundTag();
			compound.put("Entries", entries);
			return compound;
		}

		@Override
		public Map<UUID, BalloonOrder> read(CompoundTag compound) {
			Map<UUID, BalloonOrder> map = Maps.newHashMap();
			compound.getList("Entries", 10).forEach(nbt -> {
				CompoundTag entry = (CompoundTag) nbt;
				if (entry.contains("UUID", 11) && entry.contains("Order", 3)) {
					map.put(entry.getUUID("UUID"), BalloonOrder.byOrdinal(entry.getInt("Order")));
				}
			});
			return map;
		}

	};

	public static final TrackedData<Map<UUID, BalloonOrder>> ORDER_DATA = TrackedData.Builder.create(ORDER_PROCESSOR, Maps::newHashMap).build();
	public static final TrackedData<Integer> CATCHING_COOLDOWN = TrackedData.Builder.create(DataProcessors.INT, () -> 0).setSyncType(SyncType.NOPE).build();

	public static void registerTrackedData() {
		TrackedDataManager.INSTANCE.registerData(new ResourceLocation(EndergeticExpansion.MOD_ID, "ballooon_order"), ORDER_DATA);
		TrackedDataManager.INSTANCE.registerData(new ResourceLocation(EndergeticExpansion.MOD_ID, "catching_cooldown"), CATCHING_COOLDOWN);
	}
}
