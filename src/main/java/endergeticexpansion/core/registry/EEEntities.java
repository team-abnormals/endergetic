package endergeticexpansion.core.registry;

import java.util.List;
import java.util.function.BiFunction;

import com.google.common.collect.Lists;

import endergeticexpansion.common.entities.*;
import endergeticexpansion.common.entities.bolloom.*;
import endergeticexpansion.common.entities.booflo.EntityBoofloBaby;
import endergeticexpansion.core.EndergeticExpansion;
import endergeticexpansion.core.registry.util.RegistryUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.FMLPlayMessages;

@Mod.EventBusSubscriber(modid = EndergeticExpansion.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
@SuppressWarnings("rawtypes")
public class EEEntities {
	private static List<EntityType> entities = Lists.newArrayList();
	private static List<Item> spawnEggs = Lists.newArrayList();
	
	/*
	 * Poise Forest
	 */
	public static final EntityType<EntityPoiseCluster> POISE_CLUSTER = createEntity(EntityPoiseCluster::new, EntityClassification.MISC, "poise_cluster", 1F, 1F, 0, 0);
	public static final EntityType<EntityBolloomFruit> BOLLOOM_FRUIT = createEntity(EntityBolloomFruit::new, EntityClassification.MISC, "bolloom_fruit", 0.5F, 0.5F, 0, 0);
	public static final EntityType<EntityBoofBlock> BOOF_BLOCK = createEntity(EntityBoofBlock::new, EntityClassification.MISC, "boof_block", 1.6F, 1.6F, 0, 0);
	public static final EntityType<EntityPuffBug> PUFF_BUG = createEntity(EntityPuffBug::new, EntityClassification.CREATURE, "puff_bug", 0.3F, 1.0F, 15660724, 16610303);
	public static final EntityType<EntityEndergeticBoat> BOAT = createBasicEntity(EntityEndergeticBoat::new, EntityEndergeticBoat::new, EntityClassification.MISC, "boat", 1.375F, 0.5625F);
	public static final EntityType<EntityBolloomBalloon> BOLLOOM_BALLOON = createBasicEntity(EntityBolloomBalloon::new, EntityBolloomBalloon::new, EntityClassification.MISC, "bolloom_balloon", 0.5F, 0.5F);
	public static final EntityType<EntityBolloomKnot> BOLLOOM_KNOT = createBasicEntity(EntityBolloomKnot::new, EntityBolloomKnot::new, EntityClassification.MISC, "bolloom_knot", 0.375F, 0.19F);
	public static final EntityType<EntityBoofloBaby> BOOFLO_BABY = createEntity(EntityBoofloBaby::new, EntityClassification.CREATURE, "booflo_baby", 0.375F, 0.325F, 0, 0);
	
	private static <T extends Entity> EntityType<T> createEntity(EntityType.IFactory<T> factory, EntityClassification entityClassification, String name, float width, float height, int eggPrimary, int eggSecondary) {
		ResourceLocation location = new ResourceLocation(EndergeticExpansion.MOD_ID, name);
		EntityType<T> entity = EntityType.Builder.create(factory, entityClassification)
        	.size(width, height)
        	.setTrackingRange(64)
        	.setShouldReceiveVelocityUpdates(true)
        	.setUpdateInterval(3)
        	.build(location.toString()
        );
		entity.setRegistryName(location);
		entities.add(entity);
		if(eggPrimary != 0 && eggSecondary != 0) {
			spawnEggs.add(RegistryUtils.createSpawnEggForEntity(entity, eggPrimary, eggSecondary, ItemGroup.MISC));
		}
        
		return entity;
	}
	
	private static <T extends Entity> EntityType<T> createBasicEntity(EntityType.IFactory<T> factory, BiFunction<FMLPlayMessages.SpawnEntity, World, T> clientFactory, EntityClassification entityClassification, String name, float width, float height) {
		ResourceLocation location = new ResourceLocation(EndergeticExpansion.MOD_ID, name);
		EntityType<T> entity = EntityType.Builder.create(factory, entityClassification)
			.size(width, height)
        	.setTrackingRange(64)
        	.setShouldReceiveVelocityUpdates(true)
        	.setUpdateInterval(3)
        	.setCustomClientFactory(clientFactory)
        	.build(location.toString()
        );
		entity.setRegistryName(location);
		entities.add(entity);

		return entity;
	}
    
	@SubscribeEvent
	public static void registerEntities(RegistryEvent.Register<EntityType<?>> event) {
		for (EntityType entity : entities) {
			event.getRegistry().register(entity);
		}
    }
	
	@SubscribeEvent
	public static void registerSpawnEggs(RegistryEvent.Register<Item> event) {
		for (Item spawnEgg : spawnEggs) {
			event.getRegistry().register(spawnEgg);
		}
	}
}
