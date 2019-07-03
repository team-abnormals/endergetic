package endergeticexpansion.core.registry.other;

import endergeticexpansion.common.capability.balloons.BalloonProvider;
import endergeticexpansion.common.capability.balloons.BalloonStorage;
import endergeticexpansion.common.capability.balloons.BalloonStorageManager;
import endergeticexpansion.common.capability.balloons.IBalloonStorage;
import endergeticexpansion.core.EndergeticExpansion;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EndergeticExpansion.MOD_ID)
public class EECapabilities {
	public static final ResourceLocation BALLOON_CAP = new ResourceLocation(EndergeticExpansion.MOD_ID, "balloons");

	@SubscribeEvent
	public static void onAttachEntityCapabilities(AttachCapabilitiesEvent<Entity> event) {
		event.addCapability(BALLOON_CAP, new BalloonProvider());
	}
	
	public static void registerAll() {
		CapabilityManager.INSTANCE.register(IBalloonStorage.class, new BalloonStorage(), BalloonStorageManager::new);
		EndergeticExpansion.LOGGER.info("Endergetic Capabilites Registered!");
	}
	
}
