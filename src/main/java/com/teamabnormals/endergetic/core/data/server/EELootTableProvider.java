package com.teamabnormals.endergetic.core.data.server;

import com.google.common.collect.ImmutableList;
import com.teamabnormals.endergetic.common.block.EetleEggBlock;
import com.teamabnormals.endergetic.core.EndergeticExpansion;
import com.teamabnormals.endergetic.core.registry.EEBlocks;
import com.teamabnormals.endergetic.core.registry.EEEntityTypes;
import com.teamabnormals.endergetic.core.registry.EEItems;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.LootingEnchantFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceWithLootingCondition;
import net.minecraft.world.level.storage.loot.providers.number.BinomialDistributionGenerator;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.teamabnormals.endergetic.core.registry.EEBlocks.*;

public class EELootTableProvider extends LootTableProvider {

	public EELootTableProvider(PackOutput output) {
		super(output, BuiltInLootTables.all(), ImmutableList.of(
				new SubProviderEntry(EEBlockLoot::new, LootContextParamSets.BLOCK),
				new SubProviderEntry(EEEntityLoot::new, LootContextParamSets.ENTITY)
		));
	}

	@Override
	protected void validate(Map<ResourceLocation, LootTable> map, ValidationContext context) {
	}

	private static class EEBlockLoot extends BlockLootSubProvider {

		protected EEBlockLoot() {
			super(Set.of(), FeatureFlags.REGISTRY.allFlags());
		}

		@Override
		public void generate() {
			this.add(ENDER_FIRE.get(), noDrop());
			this.dropSelf(ENDER_LANTERN.get());
			this.dropSelf(ENDER_TORCH.get());
			this.dropOther(ENDER_WALL_TORCH.get(), ENDER_TORCH.get());
			this.add(ENDER_CAMPFIRE.get(), (block) -> createSilkTouchDispatchTable(block, applyExplosionCondition(block, LootItem.lootTableItem(Items.COPPER_BLOCK).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))))));

			this.add(EETLE_EGG.get(), block -> {
				return LootTable.lootTable()
						.withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(this.applyExplosionDecay(block, LootItem.lootTableItem(block).apply(List.of(1, 2), (size) -> SetItemCountFunction.setCount(ConstantValue.exactly((float) size + 1)).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(EetleEggBlock.SIZE, size)))))).when(HAS_SILK_TOUCH))
						.withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(this.applyExplosionDecay(block, LootItem.lootTableItem(block).apply(List.of(0, 1, 2), (size) -> SetItemCountFunction.setCount(BinomialDistributionGenerator.binomial(size + 1, 0.25F)).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(EetleEggBlock.SIZE, size)))))).when(HAS_SILK_TOUCH));
			});

			this.dropWhenSilkTouch(PUFFBUG_HIVE.get());
			this.add(HIVE_HANGER.get(), noDrop());

			this.dropSelf(ACIDIAN_LANTERN.get());

			this.dropSelf(BOLLOOM_BUD.get());
			this.dropSelf(BOLLOOM_CRATE.get());
			this.dropSelf(BOOF_BLOCK.get());

			this.dropSelf(POISE_CLUSTER.get());
			this.add(POISE_BUSH.get(), BlockLootSubProvider::createShearsOnlyDrop);
			this.add(TALL_POISE_BUSH.get(), this::createTallPoiseBushDrops);
			this.add(POISMOSS.get(), block -> this.createSingleItemTableWithSilkTouch(block, Blocks.END_STONE));
			this.dropOther(POISMOSS_PATH.get(), Blocks.END_STONE);

			this.dropSelf(EUMUS.get());
			this.add(EUMUS_POISMOSS.get(), (block) -> this.createSingleItemTableWithSilkTouch(block, EUMUS.get()));
			this.dropOther(EUMUS_POISMOSS_PATH.get(), EUMUS_POISMOSS.get());

			this.dropSelf(EUMUS_BRICKS.get());
			this.dropSelf(EUMUS_BRICK_STAIRS.get());
			this.dropSelf(EUMUS_BRICK_WALL.get());
			this.add(EUMUS_BRICK_SLAB.get(), this::createSlabItemTable);
			this.dropSelf(CHISELED_EUMUS_BRICKS.get());
			this.dropSelf(CRACKED_EUMUS_BRICKS.get());

			this.dropSelf(CHISELED_END_STONE_BRICKS.get());
			this.dropSelf(CRACKED_END_STONE_BRICKS.get());
			this.dropSelf(CRACKED_PURPUR_BLOCK.get());

			this.dropSelf(POISE_PLANKS.get());
			this.dropSelf(POISE_STEM.get());
			this.dropSelf(POISE_WOOD.get());
			this.dropSelf(STRIPPED_POISE_STEM.get());
			this.dropSelf(STRIPPED_POISE_WOOD.get());
			this.dropSelf(GLOWING_POISE_STEM.get());
			this.dropSelf(GLOWING_POISE_WOOD.get());
			this.dropSelf(POISE_SIGNS.getFirst().get());
			this.dropSelf(POISE_HANGING_SIGNS.getFirst().get());
			this.dropSelf(POISE_PRESSURE_PLATE.get());
			this.dropSelf(POISE_TRAPDOOR.get());
			this.dropSelf(POISE_BUTTON.get());
			this.dropSelf(POISE_STAIRS.get());
			this.dropSelf(POISE_FENCE.get());
			this.dropSelf(POISE_FENCE_GATE.get());
			this.dropSelf(POISE_BOARDS.get());

			this.dropSelf(POISE_LADDER.get());
			this.add(POISE_SLAB.get(), this::createSlabItemTable);
			this.add(POISE_DOOR.get(), this::createDoorTable);
			this.add(POISE_BEEHIVE.get(), BlockLootSubProvider::createBeeHiveDrop);
			this.add(POISE_CHEST.get(), this::createNameableBlockEntityTable);
			this.add(TRAPPED_POISE_CHEST.get(), this::createNameableBlockEntityTable);
			this.add(POISE_BOOKSHELF.get(), (block) -> createSingleItemTableWithSilkTouch(block, Items.BOOK, ConstantValue.exactly(3.0F)));
			this.dropWhenSilkTouch(CHISELED_POISE_BOOKSHELF.get());

			this.dropPottedContents(POTTED_POISE_BUSH.get());
			this.dropPottedContents(POTTED_TALL_POISE_BUSH.get());

			this.dropSelf(INFESTED_CORROCK.get());
			this.dropSelf(PETRIFIED_INFESTED_CORROCK.get());
			this.dropSelf(PORTAPLASM_BLOCK.get());

			this.dropWhenSilkTouch(END_CORROCK.get());
			this.dropSelf(END_CORROCK_BLOCK.get());
			this.dropSelf(END_CORROCK_CROWN.get());
			this.dropSelf(END_WALL_CORROCK_CROWN.get());
			this.dropSelf(SPECKLED_END_CORROCK.get());
			this.dropWhenSilkTouch(PETRIFIED_END_CORROCK.get());
			this.dropSelf(PETRIFIED_END_CORROCK_BLOCK.get());
			this.dropSelf(PETRIFIED_END_CORROCK_CROWN.get());
			this.dropSelf(PETRIFIED_END_WALL_CORROCK_CROWN.get());
			this.dropSelf(PETRIFIED_SPECKLED_END_CORROCK.get());

			this.dropWhenSilkTouch(NETHER_CORROCK.get());
			this.dropSelf(NETHER_CORROCK_BLOCK.get());
			this.dropSelf(NETHER_CORROCK_CROWN.get());
			this.dropSelf(NETHER_WALL_CORROCK_CROWN.get());
			this.dropSelf(SPECKLED_NETHER_CORROCK.get());
			this.dropWhenSilkTouch(PETRIFIED_NETHER_CORROCK.get());
			this.dropSelf(PETRIFIED_NETHER_CORROCK_BLOCK.get());
			this.dropSelf(PETRIFIED_NETHER_CORROCK_CROWN.get());
			this.dropSelf(PETRIFIED_NETHER_WALL_CORROCK_CROWN.get());
			this.dropSelf(PETRIFIED_SPECKLED_NETHER_CORROCK.get());

			this.dropWhenSilkTouch(OVERWORLD_CORROCK.get());
			this.dropSelf(OVERWORLD_CORROCK_BLOCK.get());
			this.dropSelf(OVERWORLD_CORROCK_CROWN.get());
			this.dropSelf(OVERWORLD_WALL_CORROCK_CROWN.get());
			this.dropSelf(SPECKLED_OVERWORLD_CORROCK.get());
			this.dropWhenSilkTouch(PETRIFIED_OVERWORLD_CORROCK.get());
			this.dropSelf(PETRIFIED_OVERWORLD_CORROCK_BLOCK.get());
			this.dropSelf(PETRIFIED_OVERWORLD_CORROCK_CROWN.get());
			this.dropSelf(PETRIFIED_OVERWORLD_WALL_CORROCK_CROWN.get());
			this.dropSelf(PETRIFIED_SPECKLED_OVERWORLD_CORROCK.get());
		}

		@Override
		public Iterable<Block> getKnownBlocks() {
			return ForgeRegistries.BLOCKS.getValues().stream().filter(block -> ForgeRegistries.BLOCKS.getKey(block).getNamespace().equals(EndergeticExpansion.MOD_ID)).collect(Collectors.toSet());
		}

		protected LootTable.Builder createTallPoiseBushDrops(Block block) {
			return LootTable.lootTable().withPool(this.applyExplosionCondition(block, LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(block).when(HAS_SHEARS).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.LOWER))))));
		}
	}

	private static class EEEntityLoot extends EntityLootSubProvider {

		protected EEEntityLoot() {
			super(FeatureFlags.REGISTRY.allFlags());
		}

		@Override
		public void generate() {
			this.add(EEEntityTypes.BOOFLO.get(), LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(EEItems.BOOFLO_HIDE.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F)))).when(LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(0.2F, 0.2F))));
			this.add(EEEntityTypes.BOOFLO_ADOLESCENT.get(), LootTable.lootTable());
			this.add(EEEntityTypes.BOOFLO_BABY.get(), LootTable.lootTable());
			this.add(EEEntityTypes.BROOD_EETLE.get(), LootTable.lootTable());
			this.add(EEEntityTypes.PUFF_BUG.get(), LootTable.lootTable());

			this.add(EEEntityTypes.PURPOID.get(), new ResourceLocation(EndergeticExpansion.MOD_ID, "entities/purp"), LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(EEItems.PORTAPLASM.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 1.0F))))));
			this.add(EEEntityTypes.PURPOID.get(), LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(EEItems.PORTAPLASM.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))).apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 2.0F))))));
			this.add(EEEntityTypes.PURPOID.get(), new ResourceLocation(EndergeticExpansion.MOD_ID, "entities/purpazoid"), LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(EEItems.PORTAPLASM.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 8.0F))).apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(1.0F, 3.0F))))));

			this.add(EEEntityTypes.CHARGER_EETLE.get(), LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(EEBlocks.EETLE_EGG.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F)))).when(LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(0.2F, 0.1F))));
			this.add(EEEntityTypes.GLIDER_EETLE.get(), LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(EEBlocks.EETLE_EGG.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F)))).when(LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(0.2F, 0.1F))));
		}

		@Override
		public Stream<EntityType<?>> getKnownEntityTypes() {
			return ForgeRegistries.ENTITY_TYPES.getValues().stream().filter(entity -> ForgeRegistries.ENTITY_TYPES.getKey(entity).getNamespace().equals(EndergeticExpansion.MOD_ID));
		}
	}
}