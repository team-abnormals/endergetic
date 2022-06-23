package com.minecraftabnormals.endergetic.core.registry.util;

import com.minecraftabnormals.abnormals_core.core.util.registry.EntitySubRegistryHelper;
import com.minecraftabnormals.abnormals_core.core.util.registry.RegistryHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.network.FMLPlayMessages;

import java.util.function.BiFunction;

public class EndergeticEntitySubRegistryHelper extends EntitySubRegistryHelper {

	public EndergeticEntitySubRegistryHelper(RegistryHelper parent) {
		super(parent, parent.getEntitySubHelper().getDeferredRegister());
	}

	public <E extends Entity> RegistryObject<EntityType<E>> createUnsummonableEntity(String name, EntityType.EntityFactory<E> factory, BiFunction<FMLPlayMessages.SpawnEntity, Level, E> clientFactory, MobCategory entityClassification, float width, float height) {
		return this.deferredRegister.register(name, () -> EntityType.Builder.of(factory, entityClassification).sized(width, height).noSummon().setTrackingRange(64).setShouldReceiveVelocityUpdates(true).setUpdateInterval(1).setCustomClientFactory(clientFactory).build(this.parent.prefix(name).toString()));
	}

}
