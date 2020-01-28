package endergeticexpansion.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import endergeticexpansion.client.particle.EEParticles;
import endergeticexpansion.common.items.EndergeticSpawnEgg;
import endergeticexpansion.common.network.entity.MessageCAnimation;
import endergeticexpansion.common.network.entity.MessageCSetVelocity;
import endergeticexpansion.common.network.entity.MessageCUpdatePlayerMotion;
import endergeticexpansion.common.network.entity.MessageSBoofEntity;
import endergeticexpansion.common.network.entity.MessageSSetCooldown;
import endergeticexpansion.common.network.entity.MessageSSetFallDistance;
import endergeticexpansion.common.network.entity.MessageSSetVelocity;
import endergeticexpansion.common.network.entity.booflo.MessageSIncrementBoostDelay;
import endergeticexpansion.common.network.entity.booflo.MessageSInflate;
import endergeticexpansion.common.network.entity.booflo.MessageSSetPlayerNotBoosting;
import endergeticexpansion.common.network.entity.booflo.MessageSSlam;
import endergeticexpansion.common.network.nbt.MessageCUpdateNBTTag;
import endergeticexpansion.common.network.nbt.MessageSUpdateNBTTag;
import endergeticexpansion.common.network.particle.MessageSpawnParticle;
import endergeticexpansion.common.world.EndOverrideHandler;
import endergeticexpansion.common.world.FeatureOverrideHandler;
import endergeticexpansion.core.proxy.ClientProxy;
import endergeticexpansion.core.proxy.CommonProxy;
import endergeticexpansion.core.registry.EEBiomes;
import endergeticexpansion.core.registry.EEBlocks;
import endergeticexpansion.core.registry.EEEntities;
import endergeticexpansion.core.registry.EEItems;
import endergeticexpansion.core.registry.EESounds;
import endergeticexpansion.core.registry.EETileEntities;
import endergeticexpansion.core.registry.other.EECapabilities;
import endergeticexpansion.core.registry.other.EEDispenserBehaviorRegistry;
import endergeticexpansion.core.registry.other.EEFireInfo;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

@Mod(value = EndergeticExpansion.MOD_ID)
public class EndergeticExpansion {
	public static final String MOD_ID = "endergetic";
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID.toUpperCase());
	public static final String NETWORK_PROTOCOL = "EE1";
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
		
		final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
    	
		EEItems.ITEMS.register(modEventBus);
		EEBlocks.BLOCKS.register(modEventBus);
		EETileEntities.TILE_ENTITY_TYPES.register(modEventBus);
		EEParticles.PARTICLES.register(modEventBus);
		EEEntities.ENTITY_TYPES.register(modEventBus);
		EEBiomes.BIOMES.register(modEventBus);
		EESounds.SOUNDS.register(modEventBus);
		
		DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
			modEventBus.addListener(EventPriority.LOWEST, this::registerItemColors);
		});
		
		FMLJavaModLoadingContext.get().getModEventBus().addListener(EventPriority.LOWEST, this::setupCommon);
	}
	
	void setupCommon(final FMLCommonSetupEvent event) {
		proxy.preInit();
		EEDispenserBehaviorRegistry.registerAll();
		EECapabilities.registerAll();
		EEBiomes.registerBiomeDictionaryTags();
		EEFireInfo.registerFireInfo();
		EndOverrideHandler.overrideEndFactory();
		FeatureOverrideHandler.overrideFeatures();
	}
	
	@OnlyIn(Dist.CLIENT)
	private void registerItemColors(ColorHandlerEvent.Item event) {
		for(RegistryObject<Item> items : EEItems.SPAWN_EGGS) {
			Item item = items.get();
			if(item instanceof EndergeticSpawnEgg) {
				event.getItemColors().register((itemColor, itemsIn) -> {
					return ((EndergeticSpawnEgg) item).getColor(itemsIn);
				}, item);
			}
		}
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
		
		CHANNEL.messageBuilder(MessageSpawnParticle.class, id++)
		.encoder(MessageSpawnParticle::serialize).decoder(MessageSpawnParticle::deserialize)
		.consumer(MessageSpawnParticle::handle)
		.add();
		
		CHANNEL.messageBuilder(MessageSInflate.class, id++)
		.encoder(MessageSInflate::serialize).decoder(MessageSInflate::deserialize)
		.consumer(MessageSInflate::handle)
		.add();
		
		CHANNEL.messageBuilder(MessageSIncrementBoostDelay.class, id++)
		.encoder(MessageSIncrementBoostDelay::serialize).decoder(MessageSIncrementBoostDelay::deserialize)
		.consumer(MessageSIncrementBoostDelay::handle)
		.add();
		
		CHANNEL.messageBuilder(MessageSSetPlayerNotBoosting.class, id++)
		.encoder(MessageSSetPlayerNotBoosting::serialize).decoder(MessageSSetPlayerNotBoosting::deserialize)
		.consumer(MessageSSetPlayerNotBoosting::handle)
		.add();
		
		CHANNEL.messageBuilder(MessageSSlam.class, id++)
		.encoder(MessageSSlam::serialize).decoder(MessageSSlam::deserialize)
		.consumer(MessageSSlam::handle)
		.add();
	}
}