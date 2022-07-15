package com.teamabnormals.endergetic.core.registry.util;

import com.teamabnormals.blueprint.core.util.registry.EntitySubRegistryHelper;
import com.teamabnormals.blueprint.core.util.registry.RegistryHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.PlayMessages;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.BiFunction;

public class EndergeticEntitySubRegistryHelper extends EntitySubRegistryHelper {

	public EndergeticEntitySubRegistryHelper(RegistryHelper parent) {
		super(parent, parent.getEntitySubHelper().getDeferredRegister());
	}

	public <E extends Entity> RegistryObject<EntityType<E>> createUnsummonableEntity(String name, EntityType.EntityFactory<E> factory, BiFunction<PlayMessages.SpawnEntity, Level, E> clientFactory, MobCategory entityClassification, float width, float height) {
		return this.deferredRegister.register(name, () -> EntityType.Builder.of(factory, entityClassification).sized(width, height).noSummon().setTrackingRange(64).setShouldReceiveVelocityUpdates(true).setUpdateInterval(1).setCustomClientFactory(clientFactory).build(this.parent.prefix(name).toString()));
	}

	public <E extends Entity> RegistryObject<EntityType<E>> createManuallyUpdatedEntity(String name, EntityType.EntityFactory<E> factory, BiFunction<PlayMessages.SpawnEntity, Level, E> clientFactory, MobCategory entityClassification, float width, float height) {
		return this.deferredRegister.register(name, () -> EntityType.Builder.of(factory, entityClassification).sized(width, height).setTrackingRange(64).setShouldReceiveVelocityUpdates(true).setUpdateInterval(Integer.MAX_VALUE).setCustomClientFactory(clientFactory).build(this.parent.prefix(name).toString()));
	}

}
