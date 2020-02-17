package endergeticexpansion.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import endergeticexpansion.client.particle.EEParticles;
import endergeticexpansion.client.render.entity.*;
import endergeticexpansion.client.render.entity.booflo.*;
import endergeticexpansion.client.render.tile.*;
import endergeticexpansion.common.entities.*;
import endergeticexpansion.common.entities.bolloom.*;
import endergeticexpansion.common.entities.booflo.*;
import endergeticexpansion.common.entities.puffbug.EntityPuffBug;
import endergeticexpansion.common.items.EndergeticSpawnEgg;
import endergeticexpansion.common.network.entity.*;
import endergeticexpansion.common.network.entity.booflo.*;
import endergeticexpansion.common.network.entity.puffbug.MessageRotate;
import endergeticexpansion.common.network.nbt.*;
import endergeticexpansion.common.network.particle.*;
import endergeticexpansion.common.tileentities.*;
import endergeticexpansion.common.tileentities.boof.*;
import endergeticexpansion.common.world.EndOverrideHandler;
import endergeticexpansion.common.world.FeatureOverrideHandler;
import endergeticexpansion.common.world.features.EEFeatures;
import endergeticexpansion.common.world.surfacebuilders.EESurfaceBuilders;
import endergeticexpansion.core.keybinds.KeybindHandler;
import endergeticexpansion.core.registry.*;
import endergeticexpansion.core.registry.other.EECapabilities;
import endergeticexpansion.core.registry.other.EEDispenserBehaviorRegistry;
import endergeticexpansion.core.registry.other.EEFireInfo;
import net.minecraft.client.renderer.entity.EnderCrystalRenderer;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
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
	
	public static final SimpleChannel CHANNEL = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(MOD_ID, "net"))
		.networkProtocolVersion(() -> NETWORK_PROTOCOL)
		.clientAcceptedVersions(NETWORK_PROTOCOL::equals)
		.serverAcceptedVersions(NETWORK_PROTOCOL::equals)
		.simpleChannel();
    
	public EndergeticExpansion() {
		instance = this;
		
		this.setupMessages();
		
		final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
    	
		EEItems.ITEMS.register(modEventBus);
		EEBlocks.BLOCKS.register(modEventBus);
		EETileEntities.TILE_ENTITY_TYPES.register(modEventBus);
		EEParticles.PARTICLES.register(modEventBus);
		EEEntities.ENTITY_TYPES.register(modEventBus);
		EESurfaceBuilders.SURFACE_BUILDERS.register(modEventBus);
		EEFeatures.FEATURES.register(modEventBus);
		EEBiomes.BIOMES.register(modEventBus);
		EESounds.SOUNDS.register(modEventBus);
		
		DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
			modEventBus.addListener(EventPriority.LOWEST, this::registerItemColors);
			modEventBus.addListener(EventPriority.LOWEST, this::setupClient);
		});
		
		modEventBus.addListener(EventPriority.LOWEST, this::setupCommon);
	}
	
	void setupCommon(final FMLCommonSetupEvent event) {
		EEDispenserBehaviorRegistry.registerAll();
		EECapabilities.registerAll();
		EEBiomes.applyBiomeInfo();
		EEFireInfo.registerFireInfo();
		EndOverrideHandler.overrideEndFactory();
		FeatureOverrideHandler.overrideFeatures();
	}
	
	@OnlyIn(Dist.CLIENT)
	void setupClient(final FMLClientSetupEvent event) {
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityFrisbloomStem.class, new RenderTileEntityFrisbloomStem());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCorrockCrown.class, new RenderTileEntityCorrockCrown());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBolloomBud.class, new RenderTileEntityBolloomBud());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPuffBugHive.class, new RenderTileEntityPuffBugHive());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDispensedBoof.class, new RenderTileEntityBoofBlockDispensed());
	
		RenderingRegistry.registerEntityRenderingHandler(EntityBolloomFruit.class, RenderBolloomFruit::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityPoiseCluster.class, RenderPoiseCluster::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityBoofBlock.class, RenderBoofBlock::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityBolloomKnot.class, RenderBolloomKnot::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityBolloomBalloon.class, RenderBolloomBalloon::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityEndergeticBoat.class, RenderEndergeticBoat::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityPuffBug.class, RenderPuffBug::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityBoofloBaby.class, RenderBoofloBaby::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityBoofloAdolescent.class, RenderBoofloAdolescent::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityBooflo.class, RenderBooflo::new);
		
		KeybindHandler.registerKeys();
		
		EnderCrystalRenderer.ENDER_CRYSTAL_TEXTURES = new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/end_crystal.png");
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
		
		CHANNEL.messageBuilder(MessageTeleport.class, id++)
		.encoder(MessageTeleport::serialize).decoder(MessageTeleport::deserialize)
		.consumer(MessageTeleport::handle)
		.add();
		
		CHANNEL.messageBuilder(MessageSpawnParticle.class, id++)
		.encoder(MessageSpawnParticle::serialize).decoder(MessageSpawnParticle::deserialize)
		.consumer(MessageSpawnParticle::handle)
		.add();
		
		CHANNEL.messageBuilder(MessageC2S2CSpawnParticle.class, id++)
		.encoder(MessageC2S2CSpawnParticle::serialize).decoder(MessageC2S2CSpawnParticle::deserialize)
		.consumer(MessageC2S2CSpawnParticle::handle)
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
		
		CHANNEL.messageBuilder(MessageRotate.class, id++)
		.encoder(MessageRotate::serialize).decoder(MessageRotate::deserialize)
		.consumer(MessageRotate::handle)
		.add();
	}
}