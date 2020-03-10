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
	
	public static final RegistryObject<SoundEvent> POISE_FOREST_LOOP = createSoundEvent("ambient.poise_forest.loop");
	public static final RegistryObject<SoundEvent> POISE_FOREST_ADDITIONS = createSoundEvent("ambient.poise_forest.additions");
	public static final RegistryObject<SoundEvent> POISE_FOREST_MOOD = createSoundEvent("ambient.poise_forest.mood");
	
	public static final RegistryObject<SoundEvent> CLUSTER_PLACE   = createSoundEvent("block.cluster.place");
	public static final RegistryObject<SoundEvent> CLUSTER_BREAK   = createSoundEvent("block.cluster.break");
	public static final RegistryObject<SoundEvent> CLUSTER_STEP    = createSoundEvent("block.cluster.step");
	public static final RegistryObject<SoundEvent> CLUSTER_HIT     = createSoundEvent("block.cluster.hit");
	
	public static final RegistryObject<SoundEvent> POISE_BUSH_AMBIENT = createSoundEvent("block.poise_bush.ambient");
	public static final RegistryObject<SoundEvent> POISE_BUSH_AMBIENT_LONG = createSoundEvent("block.poise_bush.ambient_long");
	public static final RegistryObject<SoundEvent> POISE_CLUSTER_AMBIENT = createSoundEvent("block.poise_cluster.ambient");
	
	public static final RegistryObject<SoundEvent> BOOFLO_VEST_INFLATE = createSoundEvent("item.booflo_vest.inflate");
	
	public static final RegistryObject<SoundEvent> BOOFLO_CROAK    = createSoundEvent("entity.booflo.croak");
	public static final RegistryObject<SoundEvent> BOOFLO_GROWL    = createSoundEvent("entity.booflo.growl");
	public static final RegistryObject<SoundEvent> BOOFLO_HOP      = createSoundEvent("entity.booflo.hop");
	public static final RegistryObject<SoundEvent> BOOFLO_HOP_LAND = createSoundEvent("entity.booflo.hop_land");
	public static final RegistryObject<SoundEvent> BOOFLO_SLAM     = createSoundEvent("entity.booflo.slam");
	public static final RegistryObject<SoundEvent> BOOFLO_INFLATE  = createSoundEvent("entity.booflo.inflate");
	public static final RegistryObject<SoundEvent> BOOFLO_DEFLATE  = createSoundEvent("entity.booflo.deflate");
	public static final RegistryObject<SoundEvent> BOOFLO_HURT     = createSoundEvent("entity.booflo.hurt");
	public static final RegistryObject<SoundEvent> BOOFLO_DEATH    = createSoundEvent("entity.booflo.death");
	
	public static final RegistryObject<SoundEvent> PUFFBUG_PUFF          = createSoundEvent("entity.puffbug.puff");
	public static final RegistryObject<SoundEvent> PUFFBUG_TELEPORT_TO   = createSoundEvent("entity.puffbug.teleport_to");
	public static final RegistryObject<SoundEvent> PUFFBUG_TELEPORT_FROM = createSoundEvent("entity.puffbug.teleport_from");
	public static final RegistryObject<SoundEvent> PUFFBUG_POLLINATE     = createSoundEvent("entity.puffbug.pollinate");
	public static final RegistryObject<SoundEvent> PUFFBUG_SLEEP         = createSoundEvent("entity.puffbug.sleep");
	public static final RegistryObject<SoundEvent> PUFFBUG_CREATE_HIVE   = createSoundEvent("entity.puffbug.create_hive");
	public static final RegistryObject<SoundEvent> PUFFBUG_LAUNCH        = createSoundEvent("entity.puffbug.launch");
	public static final RegistryObject<SoundEvent> PUFFBUG_LAND          = createSoundEvent("entity.puffbug.land");
	public static final RegistryObject<SoundEvent> PUFFBUG_HURT          = createSoundEvent("entity.puffbug.hurt");
	public static final RegistryObject<SoundEvent> PUFFBUG_DEATH         = createSoundEvent("entity.puffbug.death");
	
	public static final RegistryObject<SoundEvent> BOOF_BLOCK_INFLATE    = createSoundEvent("entity.boof_block.inflate");
	
	public static class EESoundTypes {
		public static final SoundType CLUSTER = new SoundType(1.0F, 1.0F, EESounds.CLUSTER_BREAK.get(), EESounds.CLUSTER_STEP.get(), EESounds.CLUSTER_PLACE.get(), EESounds.CLUSTER_HIT.get(), EESounds.CLUSTER_PLACE.get());
	}
	
	private static RegistryObject<SoundEvent> createSoundEvent(String name) {
		return SOUNDS.register(name, () -> new SoundEvent(new ResourceLocation(EndergeticExpansion.MOD_ID, name)));
	}
}