package endergeticexpansion.core;

import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.teamabnormals.abnormals_core.core.library.api.AmbienceMusicPlayer;

import endergeticexpansion.client.particle.EEParticles;
import endergeticexpansion.client.render.entity.RenderBolloomBalloon;
import endergeticexpansion.client.render.entity.RenderBolloomFruit;
import endergeticexpansion.client.render.entity.RenderBolloomKnot;
import endergeticexpansion.client.render.entity.RenderBoofBlock;
import endergeticexpansion.client.render.entity.RenderPoiseCluster;
import endergeticexpansion.client.render.entity.RenderPuffBug;
import endergeticexpansion.client.render.entity.booflo.RenderBooflo;
import endergeticexpansion.client.render.entity.booflo.RenderBoofloAdolescent;
import endergeticexpansion.client.render.entity.booflo.RenderBoofloBaby;
import endergeticexpansion.client.render.tile.BolloomBudTileEntityRenderer;
import endergeticexpansion.client.render.tile.BoofBlockTileEntityRenderer;
import endergeticexpansion.client.render.tile.CorrockCrownTileEntityRenderer;
import endergeticexpansion.client.render.tile.EndStoneCoverTileEntityRenderer;
import endergeticexpansion.client.render.tile.FrisbloomStemTileEntityRenderer;
import endergeticexpansion.client.render.tile.PuffBugHiveTileEntityRenderer;
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
import endergeticexpansion.common.network.entity.puffbug.MessageRotate;
import endergeticexpansion.common.network.nbt.MessageCUpdateNBTTag;
import endergeticexpansion.common.network.nbt.MessageSUpdateNBTTag;
import endergeticexpansion.common.world.EEWorldGenHandler;
import endergeticexpansion.common.world.EndOverrideHandler;
import endergeticexpansion.common.world.features.EEFeatures;
import endergeticexpansion.common.world.surfacebuilders.EESurfaceBuilders;
import endergeticexpansion.core.config.EEConfig;
import endergeticexpansion.core.keybinds.KeybindHandler;
import endergeticexpansion.core.registry.EEBiomes;
import endergeticexpansion.core.registry.EEEntities;
import endergeticexpansion.core.registry.EESounds;
import endergeticexpansion.core.registry.EETileEntities;
import endergeticexpansion.core.registry.other.EEBlockRegistrars;
import endergeticexpansion.core.registry.other.EEDataSerializers;
import endergeticexpansion.core.registry.other.EEDispenserBehaviors;
import endergeticexpansion.core.registry.util.EndergeticRegistryHelper;
import net.minecraft.client.renderer.entity.EnderCrystalRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
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
	public static final EndergeticRegistryHelper REGISTRY_HELPER = new EndergeticRegistryHelper(MOD_ID);
	
	public static final SimpleChannel CHANNEL = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(MOD_ID, "net"))
		.networkProtocolVersion(() -> NETWORK_PROTOCOL)
		.clientAcceptedVersions(NETWORK_PROTOCOL::equals)
		.serverAcceptedVersions(NETWORK_PROTOCOL::equals)
		.simpleChannel();
    
	public EndergeticExpansion() {
		instance = this;
		
		this.setupMessages();
		EEDataSerializers.registerSerializers();
		
		final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
    	
		REGISTRY_HELPER.getDeferredItemRegister().register(modEventBus);
		REGISTRY_HELPER.getDeferredBlockRegister().register(modEventBus);
		REGISTRY_HELPER.getDeferredSoundRegister().register(modEventBus);
		REGISTRY_HELPER.getDeferredTileEntityRegister().register(modEventBus);
		EEParticles.PARTICLES.register(modEventBus);
		REGISTRY_HELPER.getDeferredEntityRegister().register(modEventBus);
		EESurfaceBuilders.SURFACE_BUILDERS.register(modEventBus);
		EEFeatures.FEATURES.register(modEventBus);
		EEBiomes.BIOMES.register(modEventBus);
		
		modEventBus.addListener((ModConfig.ModConfigEvent event) -> {
			final ModConfig config = event.getConfig();
			if(config.getSpec() == EEConfig.COMMON_SPEC) {
				EEConfig.ValuesHolder.updateCommonValuesFromConfig(config);
			}
		});
		
		DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
			modEventBus.addListener(EventPriority.LOWEST, this::registerItemColors);
			modEventBus.addListener(EventPriority.LOWEST, this::setupClient);
		});
		
		modEventBus.addListener(EventPriority.LOWEST, this::setupCommon);
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, EEConfig.COMMON_SPEC);
	}
	
	void setupCommon(final FMLCommonSetupEvent event) {
		EEDispenserBehaviors.registerAll();
		//EECapabilities.registerAll();
		EEBiomes.applyBiomeInfo();
		EEBlockRegistrars.registerFireInfo();
		EndOverrideHandler.overrideEndFactory();
		//EEWorldGenHandler.addFeaturesToVanillaBiomes();
		EEWorldGenHandler.overrideFeatures();
	}
	
	@OnlyIn(Dist.CLIENT)
	void setupClient(final FMLClientSetupEvent event) {
		EEBlockRegistrars.setupRenderLayers();
		
		ClientRegistry.bindTileEntityRenderer(EETileEntities.FRISBLOOM_STEM.get(), FrisbloomStemTileEntityRenderer::new);
		ClientRegistry.bindTileEntityRenderer(EETileEntities.CORROCK_CROWN.get(), CorrockCrownTileEntityRenderer::new);
		ClientRegistry.bindTileEntityRenderer(EETileEntities.ENDSTONE_COVER.get(), EndStoneCoverTileEntityRenderer::new);
		ClientRegistry.bindTileEntityRenderer(EETileEntities.BOLLOOM_BUD.get(), BolloomBudTileEntityRenderer::new);
		ClientRegistry.bindTileEntityRenderer(EETileEntities.PUFFBUG_HIVE.get(), PuffBugHiveTileEntityRenderer::new);
		ClientRegistry.bindTileEntityRenderer(EETileEntities.BOOF_BLOCK_DISPENSED.get(), BoofBlockTileEntityRenderer::new);
	
		RenderingRegistry.registerEntityRenderingHandler(EEEntities.BOLLOOM_FRUIT.get(), RenderBolloomFruit::new);
		RenderingRegistry.registerEntityRenderingHandler(EEEntities.POISE_CLUSTER.get(), RenderPoiseCluster::new);
		RenderingRegistry.registerEntityRenderingHandler(EEEntities.BOOF_BLOCK.get(), RenderBoofBlock::new);
		RenderingRegistry.registerEntityRenderingHandler(EEEntities.BOLLOOM_KNOT.get(), RenderBolloomKnot::new);
		RenderingRegistry.registerEntityRenderingHandler(EEEntities.BOLLOOM_BALLOON.get(), RenderBolloomBalloon::new);
		RenderingRegistry.registerEntityRenderingHandler(EEEntities.PUFF_BUG.get(), RenderPuffBug::new);
		RenderingRegistry.registerEntityRenderingHandler(EEEntities.BOOFLO_BABY.get(), RenderBoofloBaby::new);
		RenderingRegistry.registerEntityRenderingHandler(EEEntities.BOOFLO_ADOLESCENT.get(), RenderBoofloAdolescent::new);
		RenderingRegistry.registerEntityRenderingHandler(EEEntities.BOOFLO.get(), RenderBooflo::new);
		
		KeybindHandler.registerKeys();
		
		AmbienceMusicPlayer.registerBiomeAmbientSoundPlayer(Arrays.asList(() -> EEBiomes.POISE_FOREST.get()), () -> EESounds.POISE_FOREST_LOOP.get(), () -> EESounds.POISE_FOREST_ADDITIONS.get(), () -> EESounds.POISE_FOREST_MOOD.get());
		EnderCrystalRenderer.ENDER_CRYSTAL_TEXTURES = new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/end_crystal.png");
	}
	
	
	@OnlyIn(Dist.CLIENT)
	private void registerItemColors(ColorHandlerEvent.Item event) {
		REGISTRY_HELPER.processSpawnEggColors(event);
	}
    
	private void setupMessages() {
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