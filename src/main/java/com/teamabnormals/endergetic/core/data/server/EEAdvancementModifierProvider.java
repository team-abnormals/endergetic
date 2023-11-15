package com.teamabnormals.endergetic.core.data.server;

import com.teamabnormals.blueprint.common.advancement.modification.AdvancementModifierProvider;
import com.teamabnormals.blueprint.common.advancement.modification.modifiers.CriteriaModifier;
import com.teamabnormals.endergetic.common.advancement.EECriteriaTriggers;
import com.teamabnormals.endergetic.core.EndergeticExpansion;
import com.teamabnormals.endergetic.core.registry.EEEntityTypes;
import com.teamabnormals.endergetic.core.registry.EEItems;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.BredAnimalsTrigger;
import net.minecraft.advancements.critereon.ConsumeItemTrigger;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.KilledTrigger;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;

public final class EEAdvancementModifierProvider extends AdvancementModifierProvider {
	private static final EntityType<?>[] BREEDABLE_ANIMALS = new EntityType[]{EEEntityTypes.PUFF_BUG.get()};
	private static final EntityType<?>[] MOBS_TO_KILL = new EntityType[]{EEEntityTypes.BOOFLO.get(), EEEntityTypes.PUFF_BUG.get()};

	public EEAdvancementModifierProvider(DataGenerator dataGenerator) {
		super(dataGenerator, EndergeticExpansion.MOD_ID);
	}

	@Override
	protected void registerEntries() {
		CriteriaModifier.Builder balancedDiet = CriteriaModifier.builder(this.modId);
		balancedDiet.addCriterion("bolloom_fruit", ConsumeItemTrigger.TriggerInstance.usedItem(EEItems.BOLLOOM_FRUIT.get()));
		this.entry("husbandry/balanced_diet")
				.selects("husbandry/balanced_diet")
				.addModifier(balancedDiet.requirements(RequirementsStrategy.AND).build());

		CriteriaModifier.Builder breedAllAnimals = CriteriaModifier.builder(this.modId);
		breedAllAnimals.addCriterion("booflo", EECriteriaTriggers.BRED_BOOFLO.createInstance());
		for (EntityType<?> entityType : BREEDABLE_ANIMALS) {
			breedAllAnimals.addCriterion(ForgeRegistries.ENTITY_TYPES.getKey(entityType).getPath(), BredAnimalsTrigger.TriggerInstance.bredAnimals(EntityPredicate.Builder.entity().of(entityType)));
		}
		this.entry("husbandry/bred_all_animals")
				.selects("husbandry/bred_all_animals")
				.addModifier(breedAllAnimals.requirements(RequirementsStrategy.AND).build());


		CriteriaModifier.Builder breedAnAnimal = CriteriaModifier.builder(this.modId);
		breedAnAnimal.addCriterion("bred_booflo", EECriteriaTriggers.BRED_BOOFLO.createInstance());
		breedAnAnimal.addIndexedRequirements(0, false, "bred_booflo");
		this.entry("husbandry/breed_an_animal")
				.selects("husbandry/breed_an_animal")
				.addModifier(breedAnAnimal.build());

		CriteriaModifier.Builder killAMob = CriteriaModifier.builder(this.modId);
		CriteriaModifier.Builder killAllMobs = CriteriaModifier.builder(this.modId);
		ArrayList<String> names = new ArrayList<>();
		for (EntityType<?> entityType : MOBS_TO_KILL) {
			String name = ForgeRegistries.ENTITY_TYPES.getKey(entityType).getPath();
			KilledTrigger.TriggerInstance triggerInstance = KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(entityType));
			killAMob.addCriterion(name, triggerInstance);
			killAllMobs.addCriterion(name, triggerInstance);
			names.add(name);
		}

		this.entry("adventure/kill_a_mob")
				.selects("adventure/kill_a_mob")
				.addModifier(killAMob.addIndexedRequirements(0, false, names.toArray(new String[0])).build());
		this.entry("adventure/kill_all_mobs")
				.selects("adventure/kill_all_mobs")
				.addModifier(killAllMobs.requirements(RequirementsStrategy.AND).build());
	}

}
