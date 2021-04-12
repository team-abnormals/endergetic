package com.minecraftabnormals.endergetic.core.registry.util;

import com.minecraftabnormals.abnormals_core.core.util.registry.EntitySubRegistryHelper;
import com.minecraftabnormals.abnormals_core.core.util.registry.RegistryHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.network.FMLPlayMessages;

import java.util.function.BiFunction;

public class EndergeticEntitySubRegistryHelper extends EntitySubRegistryHelper {

	public EndergeticEntitySubRegistryHelper(RegistryHelper parent) {
		super(parent, parent.getEntitySubHelper().getDeferredRegister());
	}

	public <E extends Entity> RegistryObject<EntityType<E>> createUnsummonableEntity(String name, EntityType.IFactory<E> factory, BiFunction<FMLPlayMessages.SpawnEntity, World, E> clientFactory, EntityClassification entityClassification, float width, float height) {
		return this.deferredRegister.register(name, () -> EntityType.Builder.create(factory, entityClassification).size(width, height).disableSummoning().setTrackingRange(64).setShouldReceiveVelocityUpdates(true).setUpdateInterval(1).setCustomClientFactory(clientFactory).build(this.parent.prefix(name).toString()));
	}

}
