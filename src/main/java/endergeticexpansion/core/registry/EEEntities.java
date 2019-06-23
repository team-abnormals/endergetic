package endergeticexpansion.core.registry;

import java.util.List;

import com.google.common.base.CaseFormat;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import endergeticexpansion.common.entities.EntityBolloomFruit;
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

@Mod.EventBusSubscriber(modid = EndergeticExpansion.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EEEntities {
	@SuppressWarnings("rawtypes")
	private static List<EntityType> entities = Lists.newArrayList();
	
	/*
	 * Poise Forest
	 */
	public static final EntityType<EntityPoiseCluster> POISE_CLUSTER = createEntity(EntityPoiseCluster.class, EntityPoiseCluster::new, 1F, 1F, 0x000000, 0xFFFFFF);
	public static final EntityType<EntityBolloomFruit> BOLLOOM_FRUIT = createEntity(EntityBolloomFruit.class, EntityBolloomFruit::new, 0.5F, 0.50F, 0x000000, 0xFFFFFF);
	
	private static <T extends Entity> EntityType<T> createEntity(Class<T> entityClass, EntityType.IFactory<T> factory, float width, float height, int eggPrimary, int eggSecondary) {
        ResourceLocation location = new ResourceLocation(EndergeticExpansion.MOD_ID, classToString(entityClass));
        EntityType<T> entity = EntityType.Builder.create(factory, EntityClassification.MISC).size(width, height).setTrackingRange(64).setShouldReceiveVelocityUpdates(true).setUpdateInterval(1).build(location.toString());
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
    }
}
