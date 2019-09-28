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
import endergeticexpansion.core.registry.other.*;
import net.minecraft.util.ResourceLocation;
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
		
		this.setupMessages();
		proxy.overrideVanillaFields();
		this.overrideVanillaFields();
    	
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::preInit);
	}
    	
	void preInit(final FMLCommonSetupEvent event) {
		proxy.preInit();
		EEDispenserBehaviorRegistry.registerAll();
		EECapabilities.registerAll();
		EEBiomes.registerBiomeDictionaryTags();
	}
    
	void setupMessages() {
		CHANNEL.messageBuilder(MessageCUpdatePlayerMotion.class, 0)
    	.encoder(MessageCUpdatePlayerMotion::serialize).decoder(MessageCUpdatePlayerMotion::deserialize)
    	.consumer(MessageCUpdatePlayerMotion::handle)
    	.add();
    	
    	CHANNEL.messageBuilder(MessageCUpdateNBTTag.class, 1)
    	.encoder(MessageCUpdateNBTTag::serialize).decoder(MessageCUpdateNBTTag::deserialize)
    	.consumer(MessageCUpdateNBTTag::handle)
    	.add();
    	
    	CHANNEL.messageBuilder(MessageSUpdateNBTTag.class, 2)
    	.encoder(MessageSUpdateNBTTag::serialize).decoder(MessageSUpdateNBTTag::deserialize)
    	.consumer(MessageSUpdateNBTTag::handle)
    	.add();
    	
    	CHANNEL.messageBuilder(MessageSSetCooldown.class, 3)
    	.encoder(MessageSSetCooldown::serialize).decoder(MessageSSetCooldown::deserialize)
    	.consumer(MessageSSetCooldown::handle)
    	.add();
    	
    	CHANNEL.messageBuilder(MessageCSetVelocity.class, 4)
    	.encoder(MessageCSetVelocity::serialize).decoder(MessageCSetVelocity::deserialize)
    	.consumer(MessageCSetVelocity::handle)
    	.add();
    	
    	CHANNEL.messageBuilder(MessageSSetVelocity.class, 5)
    	.encoder(MessageSSetVelocity::serialize).decoder(MessageSSetVelocity::deserialize)
    	.consumer(MessageSSetVelocity::handle)
    	.add();
    	
    	CHANNEL.messageBuilder(MessageDamageItem.class, 6)
    	.encoder(MessageDamageItem::serialize).decoder(MessageDamageItem::deserialize)
    	.consumer(MessageDamageItem::handle)
    	.add();
    	
    	CHANNEL.messageBuilder(MessageSBoofEntity.class, 7)
    	.encoder(MessageSBoofEntity::serialize).decoder(MessageSBoofEntity::deserialize)
    	.consumer(MessageSBoofEntity::handle)
    	.add();
    	
    	CHANNEL.messageBuilder(MessageSSetFallDistance.class, 8)
    	.encoder(MessageSSetFallDistance::serialize).decoder(MessageSSetFallDistance::deserialize)
    	.consumer(MessageSSetFallDistance::handle)
    	.add();
    	
    	CHANNEL.messageBuilder(MessageCAnimation.class, 9)
    	.encoder(MessageCAnimation::serialize).decoder(MessageCAnimation::deserialize)
    	.consumer(MessageCAnimation::handle)
    	.add();
	}
	
	void overrideVanillaFields() {
		EndOverrideHandler.overrideEndFactory();
		FeatureOverrideHandler.overrideFeatures();
	}
}