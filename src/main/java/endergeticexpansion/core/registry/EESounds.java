package endergeticexpansion.core.registry;

import java.util.List;

import com.google.common.collect.Lists;

import endergeticexpansion.core.EndergeticExpansion;
import net.minecraft.block.SoundType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ObjectHolder;

@EventBusSubscriber(modid = EndergeticExpansion.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
@ObjectHolder(EndergeticExpansion.MOD_ID)
public class EESounds {
	private static List<SoundEvent> sounds = Lists.newArrayList();
	public static final SoundEvent CLUSTER_PLACE = createSound("block.cluster.place");
	public static final SoundEvent CLUSTER_BREAK = createSound("block.cluster.break");
	public static final SoundEvent CLUSTER_STEP = createSound("block.cluster.step");
	public static final SoundEvent CLUSTER_HIT = createSound("block.cluster.hit");
	
	public static SoundEvent createSound(String name) {
		ResourceLocation location = new ResourceLocation(EndergeticExpansion.MOD_ID, name);
		SoundEvent event = new SoundEvent(location);
		event.setRegistryName(name);
		ForgeRegistries.SOUND_EVENTS.register(event);
		return event;
	}
	
	@SubscribeEvent
    public static void registerSound(RegistryEvent.Register<SoundEvent> event) {
        for (SoundEvent sound : sounds) {
            event.getRegistry().register(sound);
        }
    }
	
	public static class EESoundTypes {
		public static final SoundType CLUSTER = new SoundType(1.0F, 1.0F, EESounds.CLUSTER_BREAK, EESounds.CLUSTER_STEP, EESounds.CLUSTER_PLACE, EESounds.CLUSTER_HIT, EESounds.CLUSTER_PLACE);
	}
	
}
