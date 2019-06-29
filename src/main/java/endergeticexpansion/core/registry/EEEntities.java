package endergeticexpansion.core.registry;

import java.util.List;

import com.google.common.base.CaseFormat;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import endergeticexpansion.common.entities.EntityBolloomFruit;
import endergeticexpansion.common.entities.EntityBolloomKnot;
import endergeticexpansion.common.entities.EntityBoofBlock;
import endergeticexpansion.common.entities.EntityPoiseCluster;
import endergeticexpansion.core.EndergeticExpansion;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

@Mod.EventBusSubscriber(modid = EndergeticExpansion.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EEEntities {
	@SuppressWarnings("rawtypes")
	private static List<EntityType> entities = Lists.newArrayList();
	
	/*
	 * Poise Forest
	 */
	public static final EntityType<EntityPoiseCluster> POISE_CLUSTER = createEntity(EntityPoiseCluster.class, EntityPoiseCluster::new, EntityClassification.MISC, 1F, 1F, 0x000000, 0xFFFFFF);
	public static final EntityType<EntityBolloomFruit> BOLLOOM_FRUIT = createEntity(EntityBolloomFruit.class, EntityBolloomFruit::new, EntityClassification.MISC, 0.5F, 0.50F, 0x000000, 0xFFFFFF);
	public static final EntityType<EntityBoofBlock> BOOF_BLOCK = createEntity(EntityBoofBlock.class, EntityBoofBlock::new, EntityClassification.MISC, 1.6F, 1.6F, 0x000000, 0xFFFFFF);
	
	private static <T extends Entity> EntityType<T> createEntity(Class<T> entityClass, EntityType.IFactory<T> factory, EntityClassification entityClassification, float width, float height, int eggPrimary, int eggSecondary) {
        ResourceLocation location = new ResourceLocation(EndergeticExpansion.MOD_ID, classToString(entityClass));
        EntityType<T> entity = EntityType.Builder.create(factory, entityClassification).size(width, height).setTrackingRange(64).setShouldReceiveVelocityUpdates(true).setUpdateInterval(3).build(location.toString());
        entity.setRegistryName(location);
        entities.add(entity);

        return entity;
    }

    private static String classToString(Class<? extends Entity> entityClass) {
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, entityClass.getSimpleName()).replace("entity_", "");
    }
    
    @SuppressWarnings("rawtypes")
	@SubscribeEvent(priority = EventPriority.LOWEST)
    public static void registerEntities(RegistryEvent.Register<EntityType<?>> event) {
    	for (EntityType entity : entities) {
    		Preconditions.checkNotNull(entity.getRegistryName(), "registryName");
            event.getRegistry().register(entity);
    	}
    	
    	event.getRegistry().register(EntityType.Builder.<EntityBolloomKnot>create(EntityBolloomKnot::new, EntityClassification.MISC)
    			.setShouldReceiveVelocityUpdates(true)
    			.setUpdateInterval(1)
                .setTrackingRange(128)
                .size(0.375F, 0.19F)
                .setCustomClientFactory((spawnEntity, world) -> ObjectHolders.BOLLOOM_KNOT.create(world))
                .build("endergetic:bolloom_knot")
                .setRegistryName("endergetic:bolloom_knot"));
    }
    
    @ObjectHolder(value = EndergeticExpansion.MOD_ID)
    public static class ObjectHolders {
    	public static final EntityType<EntityBolloomKnot> BOLLOOM_KNOT = null;
    }
}
