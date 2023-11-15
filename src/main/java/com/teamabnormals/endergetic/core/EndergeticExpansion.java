package com.teamabnormals.endergetic.core;

import com.teamabnormals.blueprint.core.util.BiomeUtil;
import com.teamabnormals.blueprint.core.util.registry.RegistryHelper;
import com.teamabnormals.endergetic.client.model.BoofBlockDispenserModel;
import com.teamabnormals.endergetic.client.model.BoofBlockModel;
import com.teamabnormals.endergetic.client.model.PoiseClusterModel;
import com.teamabnormals.endergetic.client.model.PuffBugModel;
import com.teamabnormals.endergetic.client.model.bolloom.BolloomBalloonModel;
import com.teamabnormals.endergetic.client.model.bolloom.BolloomBudModel;
import com.teamabnormals.endergetic.client.model.bolloom.BolloomFruitModel;
import com.teamabnormals.endergetic.client.model.bolloom.BolloomKnotModel;
import com.teamabnormals.endergetic.client.model.booflo.AdolescentBoofloModel;
import com.teamabnormals.endergetic.client.model.booflo.BoofloBabyModel;
import com.teamabnormals.endergetic.client.model.booflo.BoofloModel;
import com.teamabnormals.endergetic.client.model.corrock.CorrockCrownStandingModel;
import com.teamabnormals.endergetic.client.model.corrock.CorrockCrownWallModel;
import com.teamabnormals.endergetic.client.model.eetle.BroodEetleModel;
import com.teamabnormals.endergetic.client.model.eetle.ChargerEetleModel;
import com.teamabnormals.endergetic.client.model.eetle.GliderEetleModel;
import com.teamabnormals.endergetic.client.model.eetle.LeetleModel;
import com.teamabnormals.endergetic.client.model.eetle.eggs.LargeEetleEggModel;
import com.teamabnormals.endergetic.client.model.eetle.eggs.MediumEetleEggModel;
import com.teamabnormals.endergetic.client.model.eetle.eggs.SmallEetleEggModel;
import com.teamabnormals.endergetic.client.model.purpoid.PurpModel;
import com.teamabnormals.endergetic.client.model.purpoid.PurpazoidModel;
import com.teamabnormals.endergetic.client.model.purpoid.PurpoidGelModel;
import com.teamabnormals.endergetic.client.model.purpoid.PurpoidModel;
import com.teamabnormals.endergetic.client.renderer.block.BolloomBudTileEntityRenderer;
import com.teamabnormals.endergetic.client.renderer.block.CorrockCrownTileEntityRenderer;
import com.teamabnormals.endergetic.client.renderer.block.DispensedBoofBlockTileEntityRenderer;
import com.teamabnormals.endergetic.client.renderer.block.EetleEggTileEntityRenderer;
import com.teamabnormals.endergetic.client.renderer.entity.*;
import com.teamabnormals.endergetic.client.renderer.entity.booflo.BoofloAdolescentRenderer;
import com.teamabnormals.endergetic.client.renderer.entity.booflo.BoofloBabyRenderer;
import com.teamabnormals.endergetic.client.renderer.entity.booflo.BoofloRenderer;
import com.teamabnormals.endergetic.client.renderer.entity.eetle.*;
import com.teamabnormals.endergetic.common.network.C2SInflateBoofloVestMessage;
import com.teamabnormals.endergetic.common.network.entity.S2CEnablePurpoidFlash;
import com.teamabnormals.endergetic.common.network.entity.S2CUpdateBalloonsMessage;
import com.teamabnormals.endergetic.common.network.entity.booflo.C2SBoostMessage;
import com.teamabnormals.endergetic.common.network.entity.booflo.C2SInflateMessage;
import com.teamabnormals.endergetic.common.network.entity.booflo.C2SSlamMessage;
import com.teamabnormals.endergetic.common.network.entity.puffbug.RotateMessage;
import com.teamabnormals.endergetic.core.data.client.EEEndimationProvider;
import com.teamabnormals.endergetic.core.data.server.*;
import com.teamabnormals.endergetic.core.data.server.tags.EEBiomeTagsProvider;
import com.teamabnormals.endergetic.core.data.server.tags.EEBlockTagsProvider;
import com.teamabnormals.endergetic.core.data.server.tags.EEItemTagsProvider;
import com.teamabnormals.endergetic.core.keybinds.KeybindHandler;
import com.teamabnormals.endergetic.core.registry.*;
import com.teamabnormals.endergetic.core.registry.EEFeatures.EEConfiguredFeatures;
import com.teamabnormals.endergetic.core.registry.EEFeatures.EEPlacedFeatures;
import com.teamabnormals.endergetic.core.registry.other.EEClientCompat;
import com.teamabnormals.endergetic.core.registry.other.EECompat;
import com.teamabnormals.endergetic.core.registry.other.EEDataProcessors;
import com.teamabnormals.endergetic.core.registry.other.EEDataSerializers;
import com.teamabnormals.endergetic.core.registry.util.EndergeticBlockSubRegistryHelper;
import com.teamabnormals.endergetic.core.registry.util.EndergeticEntitySubRegistryHelper;
import com.teamabnormals.endergetic.core.registry.util.EndergeticItemSubRegistryHelper;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.CampfireRenderer;
import net.minecraft.client.renderer.entity.EndCrystalRenderer;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(value = EndergeticExpansion.MOD_ID)
public class EndergeticExpansion {
	public static final String MOD_ID = "endergetic";
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID.toUpperCase());
	public static final String NETWORK_PROTOCOL = "EE1";
	public static EndergeticExpansion instance;
	public static final RegistryHelper REGISTRY_HELPER = RegistryHelper.create(MOD_ID, helper -> {
		helper.putSubHelper(ForgeRegistries.ITEMS, new EndergeticItemSubRegistryHelper(helper));
		helper.putSubHelper(ForgeRegistries.BLOCKS, new EndergeticBlockSubRegistryHelper(helper));
		helper.putSubHelper(ForgeRegistries.ENTITY_TYPES, new EndergeticEntitySubRegistryHelper(helper));
	});

	public static final SimpleChannel CHANNEL = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(MOD_ID, "net"))
			.networkProtocolVersion(() -> NETWORK_PROTOCOL)
			.clientAcceptedVersions(NETWORK_PROTOCOL::equals)
			.serverAcceptedVersions(NETWORK_PROTOCOL::equals)
			.simpleChannel();

	public EndergeticExpansion() {
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		ModLoadingContext context = ModLoadingContext.get();
		MinecraftForge.EVENT_BUS.register(this);

		instance = this;

		this.setupMessages();
		EEDataProcessors.registerTrackedData();

		REGISTRY_HELPER.register(bus);
		EEParticleTypes.PARTICLES.register(bus);
		EESurfaceRules.RULES.register(bus);
		EEPlacementModifierTypes.PLACEMENT_MODIFIER_TYPES.register(bus);
		EEFeatures.FEATURES.register(bus);
		EEConfiguredFeatures.CONFIGURED_FEATURES.register(bus);
		EEPlacedFeatures.PLACED_FEATURES.register(bus);
		EEStructures.STRUCTURE_TYPES.register(bus);
		EEStructures.STRUCTURES.register(bus);
		EEStructures.PieceTypes.STRUCTURE_PIECE_TYPES.register(bus);
		EEStructures.Sets.STRUCTURE_SETS.register(bus);
		EEDataSerializers.SERIALIZERS.register(bus);
		EEBiomeModifierSerializers.SERIALIZERS.register(bus);

		bus.addListener((ModConfigEvent event) -> {
			final ModConfig config = event.getConfig();
			if (config.getSpec() == EEConfig.COMMON_SPEC) {
				EEConfig.ValuesHolder.updateCommonValuesFromConfig(config);
			}
		});

		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
			bus.addListener(EventPriority.LOWEST, this::clientSetup);
			bus.addListener(KeybindHandler::registerKeys);
			bus.addListener(this::registerLayerDefinitions);
			bus.addListener(this::registerRenderers);
		});

		bus.addListener(EventPriority.LOWEST, this::commonSetup);
		bus.addListener(this::dataSetup);
		context.registerConfig(ModConfig.Type.COMMON, EEConfig.COMMON_SPEC);
	}

	private void commonSetup(FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {
			EECompat.registerCompat();
		});
	}

	private void clientSetup(FMLClientSetupEvent event) {
		EEClientCompat.registerClientCompat();
		EndCrystalRenderer.RENDER_TYPE = RenderType.entityCutoutNoCull(new ResourceLocation(MOD_ID, "textures/entity/end_crystal.png"));
		BiomeUtil.markEndBiomeCustomMusic(EEBiomes.POISE_FOREST.getKey());
	}

	private void dataSetup(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		ExistingFileHelper helper = event.getExistingFileHelper();

		boolean includeServer = event.includeServer();
		generator.addProvider(includeServer, new EEChunkGeneratorModifierProvider(generator));
		generator.addProvider(includeServer, new EERecipeProvider(generator));
		generator.addProvider(includeServer, new EEAdvancementModifierProvider(generator));
		generator.addProvider(includeServer, new EELootModifierProvider(generator));
		EEBlockTagsProvider blockTags = new EEBlockTagsProvider(generator, helper);
		generator.addProvider(includeServer, blockTags);
		generator.addProvider(includeServer, new EEItemTagsProvider(generator, blockTags, helper));
		generator.addProvider(includeServer, new EEBiomeTagsProvider(generator, helper));
		generator.addProvider(includeServer, EEBiomeModifierProvider.create(generator, helper));

		boolean includeClient = event.includeClient();
		generator.addProvider(includeClient, new EEEndimationProvider(generator));
	}

	private void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(CorrockCrownStandingModel.LOCATION, CorrockCrownStandingModel::createLayerDefinition);
		event.registerLayerDefinition(CorrockCrownWallModel.LOCATION, CorrockCrownWallModel::createLayerDefinition);
		event.registerLayerDefinition(BolloomBudModel.LOCATION, BolloomBudModel::createLayerDefinition);
		event.registerLayerDefinition(BoofBlockDispenserModel.LOCATION, BoofBlockDispenserModel::createLayerDefinition);
		event.registerLayerDefinition(SmallEetleEggModel.LOCATION, SmallEetleEggModel::createLayerDefinition);
		event.registerLayerDefinition(MediumEetleEggModel.LOCATION, MediumEetleEggModel::createLayerDefinition);
		event.registerLayerDefinition(LargeEetleEggModel.LOCATION, LargeEetleEggModel::createLayerDefinition);

		event.registerLayerDefinition(BolloomFruitModel.LOCATION, BolloomFruitModel::createLayerDefinition);
		event.registerLayerDefinition(PoiseClusterModel.LOCATION, PoiseClusterModel::createLayerDefinition);
		event.registerLayerDefinition(BoofBlockModel.LOCATION, BoofBlockModel::createLayerDefinition);
		event.registerLayerDefinition(BolloomKnotModel.LOCATION, BolloomKnotModel::createLayerDefinition);
		event.registerLayerDefinition(BolloomBalloonModel.LOCATION, BolloomBalloonModel::createLayerDefinition);
		event.registerLayerDefinition(PuffBugModel.LOCATION, PuffBugModel::createLayerDefinition);
		event.registerLayerDefinition(BoofloBabyModel.LOCATION, BoofloBabyModel::createLayerDefinition);
		event.registerLayerDefinition(AdolescentBoofloModel.LOCATION, AdolescentBoofloModel::createLayerDefinition);
		event.registerLayerDefinition(BoofloModel.LOCATION, BoofloModel::createLayerDefinition);
		event.registerLayerDefinition(LeetleModel.LOCATION, LeetleModel::createLayerDefinition);
		event.registerLayerDefinition(ChargerEetleModel.LOCATION, ChargerEetleModel::createLayerDefinition);
		event.registerLayerDefinition(GliderEetleModel.LOCATION, GliderEetleModel::createLayerDefinition);
		event.registerLayerDefinition(BroodEetleModel.LOCATION, BroodEetleModel::createLayerDefinition);
		event.registerLayerDefinition(PurpoidGelModel.PURPOID_LOCATION, PurpoidGelModel::createPurpoidLayerDefinition);
		event.registerLayerDefinition(PurpoidGelModel.PURP_LOCATION, PurpoidGelModel::createPurpLayerDefinition);
		event.registerLayerDefinition(PurpoidGelModel.PURPAZOID_LOCATION, PurpoidGelModel::createPurpazoidLayerDefinition);
		event.registerLayerDefinition(PurpoidModel.LOCATION, PurpoidModel::createLayerDefinition);
		event.registerLayerDefinition(PurpModel.LOCATION, PurpModel::createLayerDefinition);
		event.registerLayerDefinition(PurpazoidModel.LOCATION, PurpazoidModel::createLayerDefinition);
	}

	private void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerBlockEntityRenderer(EEBlockEntityTypes.CORROCK_CROWN.get(), CorrockCrownTileEntityRenderer::new);
		event.registerBlockEntityRenderer(EEBlockEntityTypes.BOLLOOM_BUD.get(), BolloomBudTileEntityRenderer::new);
		event.registerBlockEntityRenderer(EEBlockEntityTypes.BOOF_BLOCK_DISPENSED.get(), DispensedBoofBlockTileEntityRenderer::new);
		event.registerBlockEntityRenderer(EEBlockEntityTypes.ENDER_CAMPFIRE.get(), CampfireRenderer::new);
		event.registerBlockEntityRenderer(EEBlockEntityTypes.EETLE_EGG.get(), EetleEggTileEntityRenderer::new);

		event.registerEntityRenderer(EEEntityTypes.BOLLOOM_FRUIT.get(), BolloomFruitRenderer::new);
		event.registerEntityRenderer(EEEntityTypes.POISE_CLUSTER.get(), PoiseClusterRender::new);
		event.registerEntityRenderer(EEEntityTypes.BOOF_BLOCK.get(), BoofBlockRenderer::new);
		event.registerEntityRenderer(EEEntityTypes.BOLLOOM_KNOT.get(), BolloomKnotRenderer::new);
		event.registerEntityRenderer(EEEntityTypes.BOLLOOM_BALLOON.get(), BolloomBalloonRenderer::new);
		event.registerEntityRenderer(EEEntityTypes.PUFF_BUG.get(), PuffBugRenderer::new);
		event.registerEntityRenderer(EEEntityTypes.BOOFLO_BABY.get(), BoofloBabyRenderer::new);
		event.registerEntityRenderer(EEEntityTypes.BOOFLO_ADOLESCENT.get(), BoofloAdolescentRenderer::new);
		event.registerEntityRenderer(EEEntityTypes.BOOFLO.get(), BoofloRenderer::new);
		event.registerEntityRenderer(EEEntityTypes.CHARGER_EETLE.get(), ChargerEetleRenderer::new);
		event.registerEntityRenderer(EEEntityTypes.GLIDER_EETLE.get(), GliderEetleRenderer::new);
		event.registerEntityRenderer(EEEntityTypes.BROOD_EETLE.get(), BroodEetleRenderer::new);
		event.registerEntityRenderer(EEEntityTypes.EETLE_EGG.get(), EetleEggRenderer::new);
		event.registerEntityRenderer(EEEntityTypes.BROOD_EGG_SACK.get(), BroodEggSackRenderer::new);
		event.registerEntityRenderer(EEEntityTypes.PURPOID.get(), PurpoidRenderer::new);
	}

	private void setupMessages() {
		int id = -1;
		CHANNEL.registerMessage(id++, C2SInflateMessage.class, C2SInflateMessage::serialize, C2SInflateMessage::deserialize, C2SInflateMessage::handle);
		CHANNEL.registerMessage(id++, C2SBoostMessage.class, C2SBoostMessage::serialize, C2SBoostMessage::deserialize, C2SBoostMessage::handle);
		CHANNEL.registerMessage(id++, C2SSlamMessage.class, C2SSlamMessage::serialize, C2SSlamMessage::deserialize, C2SSlamMessage::handle);
		CHANNEL.registerMessage(id++, RotateMessage.class, RotateMessage::serialize, RotateMessage::deserialize, RotateMessage::handle);
		CHANNEL.registerMessage(id++, S2CUpdateBalloonsMessage.class, S2CUpdateBalloonsMessage::serialize, S2CUpdateBalloonsMessage::deserialize, S2CUpdateBalloonsMessage::handle);
		CHANNEL.registerMessage(id++, C2SInflateBoofloVestMessage.class, C2SInflateBoofloVestMessage::serialize, C2SInflateBoofloVestMessage::deserialize, C2SInflateBoofloVestMessage::handle);
		CHANNEL.registerMessage(id++, S2CEnablePurpoidFlash.class, S2CEnablePurpoidFlash::serialize, S2CEnablePurpoidFlash::deserialize, S2CEnablePurpoidFlash::handle);
	}
}