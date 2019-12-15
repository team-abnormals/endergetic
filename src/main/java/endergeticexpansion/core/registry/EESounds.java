package endergeticexpansion.core.registry;

import endergeticexpansion.core.EndergeticExpansion;
import net.minecraft.block.SoundType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class EESounds {
	public static final DeferredRegister<SoundEvent> SOUNDS = new DeferredRegister<>(ForgeRegistries.SOUND_EVENTS, EndergeticExpansion.MOD_ID);
	
	public static final RegistryObject<SoundEvent> CLUSTER_PLACE = SOUNDS.register("block.cluster.place", () -> new SoundEvent(new ResourceLocation(EndergeticExpansion.MOD_ID, "block.cluster.place")));
	public static final RegistryObject<SoundEvent> CLUSTER_BREAK = SOUNDS.register("block.cluster.break", () -> new SoundEvent(new ResourceLocation(EndergeticExpansion.MOD_ID, "block.cluster.break")));
	public static final RegistryObject<SoundEvent> CLUSTER_STEP = SOUNDS.register("block.cluster.step", () -> new SoundEvent(new ResourceLocation(EndergeticExpansion.MOD_ID, "block.cluster.step")));
	public static final RegistryObject<SoundEvent> CLUSTER_HIT = SOUNDS.register("block.cluster.hit", () -> new SoundEvent(new ResourceLocation(EndergeticExpansion.MOD_ID, "block.cluster.hit")));
	
	public static class EESoundTypes {
		public static final SoundType CLUSTER = new SoundType(1.0F, 1.0F, EESounds.CLUSTER_BREAK.get(), EESounds.CLUSTER_STEP.get(), EESounds.CLUSTER_PLACE.get(), EESounds.CLUSTER_HIT.get(), EESounds.CLUSTER_PLACE.get());
	}
}
