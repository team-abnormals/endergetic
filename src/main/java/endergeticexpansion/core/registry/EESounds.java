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
	
	public static final RegistryObject<SoundEvent> CLUSTER_PLACE  = createSoundEvent("block.cluster.place");
	public static final RegistryObject<SoundEvent> CLUSTER_BREAK  = createSoundEvent("block.cluster.break");
	public static final RegistryObject<SoundEvent> CLUSTER_STEP   = createSoundEvent("block.cluster.step");
	public static final RegistryObject<SoundEvent> CLUSTER_HIT    = createSoundEvent("block.cluster.hit");
	
	public static final RegistryObject<SoundEvent> BOOFLO_CROAK   = createSoundEvent("entity.booflo.croak");
	public static final RegistryObject<SoundEvent> BOOFLO_INFLATE = createSoundEvent("entity.booflo.inflate");
	public static final RegistryObject<SoundEvent> BOOFLO_DEFLATE = createSoundEvent("entity.booflo.deflate");
	public static final RegistryObject<SoundEvent> BOOFLO_HURT    = createSoundEvent("entity.booflo.hurt");
	public static final RegistryObject<SoundEvent> BOOFLO_DEATH   = createSoundEvent("entity.booflo.death");
	
	public static class EESoundTypes {
		public static final SoundType CLUSTER = new SoundType(1.0F, 1.0F, EESounds.CLUSTER_BREAK.get(), EESounds.CLUSTER_STEP.get(), EESounds.CLUSTER_PLACE.get(), EESounds.CLUSTER_HIT.get(), EESounds.CLUSTER_PLACE.get());
	}
	
	private static RegistryObject<SoundEvent> createSoundEvent(String name) {
		return SOUNDS.register(name, () -> new SoundEvent(new ResourceLocation(EndergeticExpansion.MOD_ID, name)));
	}
}
