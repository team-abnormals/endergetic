package com.minecraftabnormals.endergetic.core;

import java.util.Collections;

import com.minecraftabnormals.endergetic.core.registry.other.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.minecraftabnormals.endergetic.client.particles.EEParticles;
import com.minecraftabnormals.endergetic.client.renderers.entity.*;
import com.minecraftabnormals.endergetic.client.renderers.entity.booflo.*;
import com.minecraftabnormals.endergetic.client.renderers.tile.*;
import com.minecraftabnormals.endergetic.common.network.*;
import com.minecraftabnormals.endergetic.common.network.entity.*;
import com.minecraftabnormals.endergetic.common.network.entity.booflo.*;
import com.minecraftabnormals.endergetic.common.network.entity.puffbug.*;
import com.minecraftabnormals.endergetic.common.world.EEWorldGenHandler;
import com.minecraftabnormals.endergetic.common.world.features.EEFeatures;
import com.minecraftabnormals.endergetic.common.world.surfacebuilders.EESurfaceBuilders;
import com.minecraftabnormals.endergetic.core.config.EEConfig;
import com.minecraftabnormals.endergetic.core.keybinds.KeybindHandler;
import com.minecraftabnormals.endergetic.core.registry.EEBiomes;
import com.minecraftabnormals.endergetic.core.registry.EEEntities;
import com.minecraftabnormals.endergetic.core.registry.EESounds;
import com.minecraftabnormals.endergetic.core.registry.EETileEntities;
import com.minecraftabnormals.endergetic.core.registry.util.EndergeticRegistryHelper;
import com.teamabnormals.abnormals_core.core.library.api.AmbienceMusicPlayer;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EnderCrystalRenderer;
import net.minecraft.client.renderer.tileentity.CampfireTileEntityRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DeferredWorkQueue;
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

@SuppressWarnings("deprecation")
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
		EEDataProcessors.registerTrackedData();

		final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

		REGISTRY_HELPER.getDeferredItemRegister().register(modEventBus);
		REGISTRY_HELPER.getDeferredBlockRegister().register(modEventBus);
		REGISTRY_HELPER.getDeferredSoundRegister().register(modEventBus);
		REGISTRY_HELPER.getDeferredTileEntityRegister().register(modEventBus);
		REGISTRY_HELPER.getDeferredEntityRegister().register(modEventBus);
		EEParticles.PARTICLES.register(modEventBus);
		EESurfaceBuilders.SURFACE_BUILDERS.register(modEventBus);
		EEFeatures.FEATURES.register(modEventBus);
		EEBiomes.BIOMES.register(modEventBus);

		modEventBus.addListener((ModConfig.ModConfigEvent event) -> {
			final ModConfig config = event.getConfig();
			if (config.getSpec() == EEConfig.COMMON_SPEC) {
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
		DeferredWorkQueue.runLater(() -> {
			EEDispenserBehaviors.registerAll();
			EELootInjectors.registerLootInjectors();
			EEBiomes.applyBiomeInfo();
			EEFlammables.registerFlammables();
			EECompostables.registerCompostables();
			EEWorldGenHandler.overrideFeatures();
			EEEntityAttributes.putAttributes();
		});
	}

	@OnlyIn(Dist.CLIENT)
	void setupClient(final FMLClientSetupEvent event) {
		EERenderLayers.setupRenderLayers();

		ClientRegistry.bindTileEntityRenderer(EETileEntities.FRISBLOOM_STEM.get(), FrisbloomStemTileEntityRenderer::new);
		ClientRegistry.bindTileEntityRenderer(EETileEntities.CORROCK_CROWN.get(), CorrockCrownTileEntityRenderer::new);
		ClientRegistry.bindTileEntityRenderer(EETileEntities.BOLLOOM_BUD.get(), BolloomBudTileEntityRenderer::new);
		ClientRegistry.bindTileEntityRenderer(EETileEntities.PUFFBUG_HIVE.get(), PuffBugHiveTileEntityRenderer::new);
		ClientRegistry.bindTileEntityRenderer(EETileEntities.BOOF_BLOCK_DISPENSED.get(), DispensedBoofBlockTileEntityRenderer::new);
		ClientRegistry.bindTileEntityRenderer(EETileEntities.ENDER_CAMPFIRE.get(), CampfireTileEntityRenderer::new);

		RenderingRegistry.registerEntityRenderingHandler(EEEntities.BOLLOOM_FRUIT.get(), BolloomFruitRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(EEEntities.POISE_CLUSTER.get(), PoiseClusterRender::new);
		RenderingRegistry.registerEntityRenderingHandler(EEEntities.BOOF_BLOCK.get(), BoofBlockRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(EEEntities.BOLLOOM_KNOT.get(), BolloomKnotRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(EEEntities.BOLLOOM_BALLOON.get(), BolloomBalloonRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(EEEntities.PUFF_BUG.get(), PuffBugRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(EEEntities.BOOFLO_BABY.get(), BoofloBabyRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(EEEntities.BOOFLO_ADOLESCENT.get(), BoofloAdolescentRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(EEEntities.BOOFLO.get(), BoofloRenderer::new);

		KeybindHandler.registerKeys();

		AmbienceMusicPlayer.registerBiomeAmbientSoundPlayer(Collections.singletonList(EEBiomes.POISE_FOREST::get), EESounds.POISE_FOREST_LOOP, EESounds.POISE_FOREST_ADDITIONS, EESounds.POISE_FOREST_MOOD);
		EnderCrystalRenderer.field_229046_e_ = RenderType.getEntityCutoutNoCull(new ResourceLocation(MOD_ID, "textures/entity/end_crystal.png"));
	}

	@OnlyIn(Dist.CLIENT)
	private void registerItemColors(ColorHandlerEvent.Item event) {
		REGISTRY_HELPER.processSpawnEggColors(event);
	}

	private void setupMessages() {
		int id = -1;
		CHANNEL.registerMessage(id++, C2SInflateMessage.class, C2SInflateMessage::serialize, C2SInflateMessage::deserialize, C2SInflateMessage::handle);
		CHANNEL.registerMessage(id++, C2SBoostMessage.class, C2SBoostMessage::serialize, C2SBoostMessage::deserialize, C2SBoostMessage::handle);
		CHANNEL.registerMessage(id++, C2SSlamMessage.class, C2SSlamMessage::serialize, C2SSlamMessage::deserialize, C2SSlamMessage::handle);
		CHANNEL.registerMessage(id++, RotateMessage.class, RotateMessage::serialize, RotateMessage::deserialize, RotateMessage::handle);
		CHANNEL.registerMessage(id++, S2CUpdateBalloonsMessage.class, S2CUpdateBalloonsMessage::serialize, S2CUpdateBalloonsMessage::deserialize, S2CUpdateBalloonsMessage::handle);
		CHANNEL.registerMessage(id++, C2SInflateBoofloVestMessage.class, C2SInflateBoofloVestMessage::serialize, C2SInflateBoofloVestMessage::deserialize, C2SInflateBoofloVestMessage::handle);
	}
}