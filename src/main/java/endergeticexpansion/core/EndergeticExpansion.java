package endergeticexpansion.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import endergeticexpansion.common.network.entity.MessageCSetVelocity;
import endergeticexpansion.common.network.entity.MessageCUpdatePlayerMotion;
import endergeticexpansion.common.network.entity.MessageSBoofEntity;
import endergeticexpansion.common.network.entity.MessageSSetCooldown;
import endergeticexpansion.common.network.entity.MessageSSetFallDistance;
import endergeticexpansion.common.network.entity.MessageSSetVelocity;
import endergeticexpansion.common.network.item.MessageDamageItem;
import endergeticexpansion.common.network.nbt.MessageCUpdateNBTTag;
import endergeticexpansion.common.network.nbt.MessageSUpdateNBTTag;
import endergeticexpansion.common.tileentities.TileEntityBolloomBud;
import endergeticexpansion.common.tileentities.TileEntityCorrockCrown;
import endergeticexpansion.common.tileentities.TileEntityFrisbloomStem;
import endergeticexpansion.common.tileentities.TileEntityPuffBugHive;
import endergeticexpansion.common.tileentities.boof.TileEntityBoof;
import endergeticexpansion.common.tileentities.boof.TileEntityDispensedBoof;
import endergeticexpansion.core.proxy.ClientProxy;
import endergeticexpansion.core.proxy.CommonProxy;
import endergeticexpansion.core.registry.EEBiomes;
import endergeticexpansion.core.registry.EEBlocks;
import endergeticexpansion.core.registry.EETileEntities;
import endergeticexpansion.core.registry.other.EECapabilities;
import endergeticexpansion.core.registry.other.EEDispenserBehaviorRegistry;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
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
    	
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::preInit);
		FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(TileEntityType.class, this::registerTileEntities);
    }
    
	void preInit(final FMLCommonSetupEvent event) {
		proxy.preInit();
		EEDispenserBehaviorRegistry.registerAll();
		EECapabilities.registerAll();
		EEBiomes.registerBiomeDictionaryTags();
	}
    
    @SubscribeEvent
	@SuppressWarnings("unchecked")
	public void registerTileEntities(RegistryEvent.Register<TileEntityType<?>> event) {
    	event.getRegistry().register(EETileEntities.CORROCK_CROWN = (TileEntityType<TileEntityCorrockCrown>) TileEntityType.Builder.create(TileEntityCorrockCrown::new, 
    		EEBlocks.CORROCK_CROWN_OVERWORLD_STANDING, EEBlocks.CORROCK_CROWN_OVERWORLD_WALL, EEBlocks.CORROCK_CROWN_NETHER_STANDING, EEBlocks.CORROCK_CROWN_NETHER_WALL,
    		EEBlocks.CORROCK_CROWN_END_STANDING, EEBlocks.CORROCK_CROWN_END_WALL)
    		.build(null).setRegistryName(MOD_ID, "corrock_crown"));
    	
    	event.getRegistry().register(EETileEntities.FRISBLOOM_STEM = (TileEntityType<TileEntityFrisbloomStem>) TileEntityType.Builder.create(TileEntityFrisbloomStem::new, EEBlocks.FRISBLOOM_STEM).build(null).setRegistryName(MOD_ID, "frisbloom_stem"));
    
    	event.getRegistry().register(EETileEntities.BOLLOOM_BUD = (TileEntityType<TileEntityBolloomBud>) TileEntityType.Builder.create(TileEntityBolloomBud::new, EEBlocks.BOLLOOM_BUD).build(null).setRegistryName(MOD_ID, "bolloom_bud"));
    
    	event.getRegistry().register(EETileEntities.PUFFBUG_HIVE = (TileEntityType<TileEntityPuffBugHive>) TileEntityType.Builder.create(TileEntityPuffBugHive::new, EEBlocks.PUFFBUG_HIVE).build(null).setRegistryName(MOD_ID, "puffbug_hive"));
    	
    	event.getRegistry().register(EETileEntities.BOOF_BLOCK = (TileEntityType<TileEntityBoof>) TileEntityType.Builder.create(TileEntityBoof::new, EEBlocks.BOOF_BLOCK).build(null).setRegistryName(MOD_ID, "boof_block"));
    	event.getRegistry().register(EETileEntities.BOOF_DISPENSED = (TileEntityType<TileEntityDispensedBoof>) TileEntityType.Builder.create(TileEntityDispensedBoof::new, EEBlocks.BOOF_DISPENSED_BLOCK).build(null).setRegistryName(MOD_ID, "boof_dispensed_block"));
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
    }
    
}
