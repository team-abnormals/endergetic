package com.minecraftabnormals.endergetic.core;

import com.minecraftabnormals.endergetic.client.models.BoofBlockDispenserModel;
import com.minecraftabnormals.endergetic.client.models.BoofBlockModel;
import com.minecraftabnormals.endergetic.client.models.PoiseClusterModel;
import com.minecraftabnormals.endergetic.client.models.bolloom.BolloomBalloonModel;
import com.minecraftabnormals.endergetic.client.models.bolloom.BolloomBudModel;
import com.minecraftabnormals.endergetic.client.models.bolloom.BolloomFruitModel;
import com.minecraftabnormals.endergetic.client.models.bolloom.BolloomKnotModel;
import com.minecraftabnormals.endergetic.client.models.booflo.AdolescentBoofloModel;
import com.minecraftabnormals.endergetic.client.models.booflo.BoofloBabyModel;
import com.minecraftabnormals.endergetic.client.models.booflo.BoofloModel;
import com.minecraftabnormals.endergetic.client.models.corrock.CorrockCrownStandingModel;
import com.minecraftabnormals.endergetic.client.models.corrock.CorrockCrownWallModel;
import com.minecraftabnormals.endergetic.client.models.eetle.BroodEetleModel;
import com.minecraftabnormals.endergetic.client.models.eetle.ChargerEetleModel;
import com.minecraftabnormals.endergetic.client.models.eetle.GliderEetleModel;
import com.minecraftabnormals.endergetic.client.models.eetle.LeetleModel;
import com.minecraftabnormals.endergetic.client.models.eetle.eggs.LargeEetleEggModel;
import com.minecraftabnormals.endergetic.client.models.eetle.eggs.MediumEetleEggModel;
import com.minecraftabnormals.endergetic.client.models.eetle.eggs.SmallEetleEggModel;
import com.minecraftabnormals.endergetic.client.models.puffbug.PuffBugHiveModel;
import com.minecraftabnormals.endergetic.client.models.puffbug.PuffBugModel;
import com.minecraftabnormals.endergetic.client.models.purpoid.PurpoidGelModel;
import com.minecraftabnormals.endergetic.client.models.purpoid.PurpoidModel;
import com.minecraftabnormals.endergetic.core.data.server.EEAdvancementModifierProvider;
import com.minecraftabnormals.endergetic.core.data.server.EEChunkGeneratorModifierProvider;
import com.minecraftabnormals.endergetic.core.data.server.EELootModifierProvider;
import com.minecraftabnormals.endergetic.core.data.server.tags.EEBiomeTagsProvider;
import com.minecraftabnormals.endergetic.core.registry.*;
import com.minecraftabnormals.endergetic.core.registry.other.*;
import com.minecraftabnormals.endergetic.core.registry.util.EndergeticBlockSubRegistryHelper;
import com.minecraftabnormals.endergetic.core.registry.util.EndergeticEntitySubRegistryHelper;
import com.minecraftabnormals.endergetic.core.registry.util.EndergeticItemSubRegistryHelper;
import com.teamabnormals.blueprint.core.util.BiomeUtil;
import com.teamabnormals.blueprint.core.util.registry.RegistryHelper;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.minecraftabnormals.endergetic.client.particles.EEParticles;
import com.minecraftabnormals.endergetic.client.renderers.entity.*;
import com.minecraftabnormals.endergetic.client.renderers.entity.booflo.*;
import com.minecraftabnormals.endergetic.client.renderers.entity.eetle.*;
import com.minecraftabnormals.endergetic.client.renderers.tile.*;
import com.minecraftabnormals.endergetic.common.network.*;
import com.minecraftabnormals.endergetic.common.network.entity.*;
import com.minecraftabnormals.endergetic.common.network.entity.booflo.*;
import com.minecraftabnormals.endergetic.common.network.entity.puffbug.*;
import com.minecraftabnormals.endergetic.core.config.EEConfig;
import com.minecraftabnormals.endergetic.core.keybinds.KeybindHandler;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EndCrystalRenderer;
import net.minecraft.client.renderer.blockentity.CampfireRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(value = EndergeticExpansion.MOD_ID)
public class EndergeticExpansion {
	public static final String MOD_ID = "endergetic";
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID.toUpperCase());
	public static final String NETWORK_PROTOCOL = "EE1";
	public static EndergeticExpansion instance;
	public static final RegistryHelper REGISTRY_HELPER = RegistryHelper.create(MOD_ID, helper -> {
		helper.putSubHelper(ForgeRegistries.ITEMS, new EndergeticItemSubRegistryHelper(helper));
		helper.putSubHelper(ForgeRegistries.BLOCKS, new EndergeticBlockSubRegistryHelper(helper));
		helper.putSubHelper(ForgeRegistries.ENTITIES, new EndergeticEntitySubRegistryHelper(helper));
	});

	public static final SimpleChannel CHANNEL = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(MOD_ID, "net"))
			.networkProtocolVersion(() -> NETWORK_PROTOCOL)
			.clientAcceptedVersions(NETWORK_PROTOCOL::equals)
			.serverAcceptedVersions(NETWORK_PROTOCOL::equals)
			.simpleChannel();

	public EndergeticExpansion() {
		instance = this;

		this.setupMessages();
		EEDataProcessors.registerTrackedData();

		final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

		REGISTRY_HELPER.register(modEventBus);
		EEParticles.PARTICLES.register(modEventBus);
		EESurfaceRules.RULES.register(modEventBus);
		EEPlacementModifierTypes.PLACEMENT_MODIFIER_TYPES.register(modEventBus);
		EEFeatures.FEATURES.register(modEventBus);
		EEFeatures.Configured.CONFIGURED_FEATURES.register(modEventBus);
		EEFeatures.Placed.PLACED_FEATURES.register(modEventBus);
		EEStructures.STRUCTURE_TYPES.register(modEventBus);
		EEStructures.STRUCTURES.register(modEventBus);
		EEStructures.PieceTypes.STRUCTURE_PIECE_TYPES.register(modEventBus);
		EEDataSerializers.SERIALIZERS.register(modEventBus);

		modEventBus.addListener((ModConfigEvent event) -> {
			final ModConfig config = event.getConfig();
			if (config.getSpec() == EEConfig.COMMON_SPEC) {
				EEConfig.ValuesHolder.updateCommonValuesFromConfig(config);
			}
		});

		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
			modEventBus.addListener(EventPriority.LOWEST, this::setupClient);
			modEventBus.addListener(this::registerLayerDefinitions);
			modEventBus.addListener(this::registerRenderers);
		});

		modEventBus.addListener(EventPriority.LOWEST, this::setupCommon);
		modEventBus.addListener(this::dataSetup);
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, EEConfig.COMMON_SPEC);
	}

	void setupCommon(final FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {
			EEDispenserBehaviors.registerAll();
			EEFlammables.registerFlammables();
			EECompostables.registerCompostables();
		});
	}

	private void dataSetup(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		boolean includeServer = event.includeServer();

		generator.addProvider(includeServer, new EEChunkGeneratorModifierProvider(generator));
		generator.addProvider(includeServer, new EEAdvancementModifierProvider(generator));
		generator.addProvider(includeServer, new EELootModifierProvider(generator));

		ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
		generator.addProvider(includeServer, new EEBiomeTagsProvider(generator, existingFileHelper));
	}

	private static void modifyBiomes() {
		//TODO: Replace with Forge's biome modifiers
//		BiomeModificationManager modificationManager = BiomeModificationManager.INSTANCE;
//		BiPredicate<ResourceKey<Biome>, Biome> highlandsOnly = BiomeModificationPredicates.forBiomeKey(Biomes.END_HIGHLANDS);
//		BiPredicate<ResourceKey<Biome>, Biome> highlandsOrMidlands = highlandsOnly.or(BiomeModificationPredicates.forBiomeKey(Biomes.END_MIDLANDS));
//		modificationManager.addModifier(BiomeFeatureModifier.createFeatureReplacer(highlandsOnly, EnumSet.of(GenerationStep.Decoration.SURFACE_STRUCTURES), () -> Feature.END_GATEWAY, () -> EEFeatures.Configured.END_GATEWAY));
//		modificationManager.addModifier(BiomeSurfaceBuilderModifier.surfaceBuilderReplacer(highlandsOrMidlands, () -> EESurfaceBuilders.Configs.SPARSE_CORROCK));
//		modificationManager.addModifier(BiomeFeatureModifier.createMultiFeatureAdder(highlandsOrMidlands, GenerationStep.Decoration.VEGETAL_DECORATION, Sets.newHashSet(
//				() -> EEFeatures.Configured.CORROCK_PATCH,
//				() -> EEFeatures.Configured.EETLE_EGG
//		)));
//		modificationManager.addModifier(BiomeFeatureModifier.createMultiFeatureAdder(highlandsOrMidlands, GenerationStep.Decoration.SURFACE_STRUCTURES, Sets.newHashSet(
//				() -> EEFeatures.Configured.CORROCK_BRANCH,
//				() -> EEFeatures.Configured.CORROCK_TOWER,
//				() -> EEFeatures.Configured.CORROCK_SHELF,
//				() -> EEFeatures.Configured.CORROCK_ARCH,
//				() -> EEFeatures.Configured.EUMUS_PATCH,
//				() -> EEFeatures.Configured.SPECKLED_CORROCK_PATCH
//		)));
//		modificationManager.addModifier(BiomeStructureModifier.createStructureAdder(highlandsOnly, () -> EEStructures.Configured.EETLE_NEST));
//		modificationManager.addModifier(BiomeSpawnsModifier.createMultiSpawnAdder(highlandsOrMidlands, MobCategory.MONSTER, Sets.newHashSet(
//				new BiomeSpawnsModifier.SpawnInfo(EEEntities.CHARGER_EETLE, 12, 2, 5),
//				new BiomeSpawnsModifier.SpawnInfo(EEEntities.GLIDER_EETLE, 8, 2, 4)
//		)));
//		modificationManager.addModifier(BiomeSpawnsModifier.createSpawnCost(highlandsOrMidlands, EEEntities.CHARGER_EETLE::get, 0.8D, 1.0D));
//		modificationManager.addModifier(BiomeSpawnsModifier.createSpawnCost(highlandsOrMidlands, EEEntities.GLIDER_EETLE::get, 0.8D, 1.0D));
//		modificationManager.addModifier(BiomeFeatureModifier.createFeatureAdder(BiomeModificationPredicates.forBiomeKey(Biomes.END_MIDLANDS), GenerationStep.Decoration.SURFACE_STRUCTURES, () -> EEFeatures.Configured.SPARSE_CORROCK_BRANCH));
//
//		modificationManager.addModifier(BiomeAmbienceModifier.createAmbienceReplacer(BiomeModificationPredicates.forBiomeKey(Biomes.SMALL_END_ISLANDS), () -> {
//			return new BiomeSpecialEffects.Builder().waterColor(4159204).waterFogColor(329011).fogColor(10518688).skyColor(0)
//					.ambientLoopSound(EESounds.SMALL_END_ISLANDS_LOOP.get())
//					.ambientAdditionsSound(new AmbientAdditionsSettings(EESounds.SMALL_END_ISLANDS_ADDITIONS.get(), 0.0111D))
//					.build();
//		}));
	}

	private void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(CorrockCrownStandingModel.LOCATION, CorrockCrownStandingModel::createLayerDefinition);
		event.registerLayerDefinition(CorrockCrownWallModel.LOCATION, CorrockCrownWallModel::createLayerDefinition);
		event.registerLayerDefinition(BolloomBudModel.LOCATION, BolloomBudModel::createLayerDefinition);
		event.registerLayerDefinition(PuffBugHiveModel.LOCATION, PuffBugHiveModel::createLayerDefinition);
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
		event.registerLayerDefinition(PurpoidGelModel.LOCATION, PurpoidGelModel::createLayerDefinition);
		event.registerLayerDefinition(PurpoidModel.LOCATION, PurpoidModel::createLayerDefinition);
	}

	private void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerBlockEntityRenderer(EETileEntities.CORROCK_CROWN.get(), CorrockCrownTileEntityRenderer::new);
		event.registerBlockEntityRenderer(EETileEntities.BOLLOOM_BUD.get(), BolloomBudTileEntityRenderer::new);
		event.registerBlockEntityRenderer(EETileEntities.PUFFBUG_HIVE.get(), PuffBugHiveTileEntityRenderer::new);
		event.registerBlockEntityRenderer(EETileEntities.BOOF_BLOCK_DISPENSED.get(), DispensedBoofBlockTileEntityRenderer::new);
		event.registerBlockEntityRenderer(EETileEntities.ENDER_CAMPFIRE.get(), CampfireRenderer::new);
		event.registerBlockEntityRenderer(EETileEntities.EETLE_EGG.get(), EetleEggTileEntityRenderer::new);

		event.registerEntityRenderer(EEEntities.BOLLOOM_FRUIT.get(), BolloomFruitRenderer::new);
		event.registerEntityRenderer(EEEntities.POISE_CLUSTER.get(), PoiseClusterRender::new);
		event.registerEntityRenderer(EEEntities.BOOF_BLOCK.get(), BoofBlockRenderer::new);
		event.registerEntityRenderer(EEEntities.BOLLOOM_KNOT.get(), BolloomKnotRenderer::new);
		event.registerEntityRenderer(EEEntities.BOLLOOM_BALLOON.get(), BolloomBalloonRenderer::new);
		event.registerEntityRenderer(EEEntities.PUFF_BUG.get(), PuffBugRenderer::new);
		event.registerEntityRenderer(EEEntities.BOOFLO_BABY.get(), BoofloBabyRenderer::new);
		event.registerEntityRenderer(EEEntities.BOOFLO_ADOLESCENT.get(), BoofloAdolescentRenderer::new);
		event.registerEntityRenderer(EEEntities.BOOFLO.get(), BoofloRenderer::new);
		event.registerEntityRenderer(EEEntities.CHARGER_EETLE.get(), ChargerEetleRenderer::new);
		event.registerEntityRenderer(EEEntities.GLIDER_EETLE.get(), GliderEetleRenderer::new);
		event.registerEntityRenderer(EEEntities.BROOD_EETLE.get(), BroodEetleRenderer::new);
		event.registerEntityRenderer(EEEntities.EETLE_EGG.get(), EetleEggRenderer::new);
		event.registerEntityRenderer(EEEntities.BROOD_EGG_SACK.get(), BroodEggSackRenderer::new);
		event.registerEntityRenderer(EEEntities.PURPOID.get(), PurpoidRenderer::new);
	}

	@OnlyIn(Dist.CLIENT)
	void setupClient(final FMLClientSetupEvent event) {
		EERenderLayers.setupRenderLayers();
		KeybindHandler.registerKeys();
		EndCrystalRenderer.RENDER_TYPE = RenderType.entityCutoutNoCull(new ResourceLocation(MOD_ID, "textures/entity/end_crystal.png"));
		BiomeUtil.markEndBiomeCustomMusic(EEBiomes.POISE_FOREST.getKey());
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