package endergeticexpansion.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import endergeticexpansion.common.network.entity.*;
import endergeticexpansion.common.network.item.*;
import endergeticexpansion.common.network.nbt.*;
import endergeticexpansion.common.world.EndOverrideHandler;
import endergeticexpansion.common.world.FeatureOverrideHandler;
import endergeticexpansion.core.proxy.*;
import endergeticexpansion.core.registry.EEBiomes;
import endergeticexpansion.core.registry.EEEntities;
import endergeticexpansion.core.registry.EEItems;
import endergeticexpansion.core.registry.EESounds;
import endergeticexpansion.core.registry.EETileEntities;
import endergeticexpansion.core.registry.other.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

@Mod(value = EndergeticExpansion.MOD_ID)
public class EndergeticExpansion {
	public static final String MOD_ID = "endergetic";
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID.toUpperCase());
	public static final String NETWORK_PROTOCOL = "1";
	public static EndergeticExpansion instance;
	public static CommonProxy proxy = DistExecutor.runForDist(() -> ClientProxy::new, () -> CommonProxy::new);
	
	public static final SimpleChannel CHANNEL = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(MOD_ID, "net"))
		.networkProtocolVersion(() -> NETWORK_PROTOCOL)
		.clientAcceptedVersions(NETWORK_PROTOCOL::equals)
		.serverAcceptedVersions(NETWORK_PROTOCOL::equals)
		.simpleChannel();
    
	public EndergeticExpansion() {
		instance = this;
		
		proxy.overrideVanillaFields();
		this.setupMessages();
		this.overrideVanillaFields();
		
		final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
    	
		EEItems.ITEMS.register(modEventBus);
		EETileEntities.TILE_ENTITY_TYPES.register(modEventBus);
		EEEntities.ENTITY_TYPES.register(modEventBus);
		EESounds.SOUNDS.register(modEventBus);
		
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::preInit);
	}
    	
	void preInit(final FMLCommonSetupEvent event) {
		proxy.preInit();
		EEDispenserBehaviorRegistry.registerAll();
		EECapabilities.registerAll();
		EEBiomes.registerBiomeDictionaryTags();
	}
    
	void setupMessages() {
		int id = -1;
		CHANNEL.messageBuilder(MessageCUpdatePlayerMotion.class, id++)
		.encoder(MessageCUpdatePlayerMotion::serialize).decoder(MessageCUpdatePlayerMotion::deserialize)
		.consumer(MessageCUpdatePlayerMotion::handle)
		.add();
    	
		CHANNEL.messageBuilder(MessageCUpdateNBTTag.class, id++)
		.encoder(MessageCUpdateNBTTag::serialize).decoder(MessageCUpdateNBTTag::deserialize)
		.consumer(MessageCUpdateNBTTag::handle)
		.add();
    	
		CHANNEL.messageBuilder(MessageSUpdateNBTTag.class, id++)
		.encoder(MessageSUpdateNBTTag::serialize).decoder(MessageSUpdateNBTTag::deserialize)
		.consumer(MessageSUpdateNBTTag::handle)
		.add();
    	
		CHANNEL.messageBuilder(MessageSSetCooldown.class, id++)
		.encoder(MessageSSetCooldown::serialize).decoder(MessageSSetCooldown::deserialize)
		.consumer(MessageSSetCooldown::handle)
		.add();
    	
		CHANNEL.messageBuilder(MessageCSetVelocity.class, id++)
		.encoder(MessageCSetVelocity::serialize).decoder(MessageCSetVelocity::deserialize)
		.consumer(MessageCSetVelocity::handle)
		.add();
    	
		CHANNEL.messageBuilder(MessageSSetVelocity.class, id++)
		.encoder(MessageSSetVelocity::serialize).decoder(MessageSSetVelocity::deserialize)
		.consumer(MessageSSetVelocity::handle)
		.add();
    	
		CHANNEL.messageBuilder(MessageDamageItem.class, id++)
		.encoder(MessageDamageItem::serialize).decoder(MessageDamageItem::deserialize)
		.consumer(MessageDamageItem::handle)
		.add();
    	
		CHANNEL.messageBuilder(MessageSBoofEntity.class, id++)
		.encoder(MessageSBoofEntity::serialize).decoder(MessageSBoofEntity::deserialize)
		.consumer(MessageSBoofEntity::handle)
		.add();
    	
		CHANNEL.messageBuilder(MessageSSetFallDistance.class, id++)
		.encoder(MessageSSetFallDistance::serialize).decoder(MessageSSetFallDistance::deserialize)
		.consumer(MessageSSetFallDistance::handle)
		.add();
    	
		CHANNEL.messageBuilder(MessageCAnimation.class, id++)
		.encoder(MessageCAnimation::serialize).decoder(MessageCAnimation::deserialize)
		.consumer(MessageCAnimation::handle)
		.add();
	}
	
	void overrideVanillaFields() {
		EndOverrideHandler.overrideEndFactory();
		FeatureOverrideHandler.overrideFeatures();
	}
}