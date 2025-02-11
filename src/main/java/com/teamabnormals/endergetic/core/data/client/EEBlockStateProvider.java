package com.teamabnormals.endergetic.core.data.client;

import com.teamabnormals.blueprint.core.data.client.BlueprintBlockStateProvider;
import com.teamabnormals.endergetic.common.block.PortaplasmBlock;
import com.teamabnormals.endergetic.core.EndergeticExpansion;
import com.teamabnormals.endergetic.core.other.EEBlockFamilies;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.common.data.ExistingFileHelper;

import static com.teamabnormals.endergetic.core.registry.EEBlocks.*;

public class EEBlockStateProvider extends BlueprintBlockStateProvider {

	public EEBlockStateProvider(PackOutput output, ExistingFileHelper helper) {
		super(output, EndergeticExpansion.MOD_ID, helper);
	}

	@Override
	protected void registerStatesAndModels() {
		this.blockFamily(EEBlockFamilies.EUMUS_BRICKS_FAMILY);
		this.blockFamily(EEBlockFamilies.POISE_PLANKS_FAMILY);

		this.block(BOOF_BLOCK);
		this.directionalBlock(BOLLOOM_CRATE);

		this.block(EUMUS.get());
		this.cubeColumnBlock(CHISELED_EUMUS_BRICKS);
		this.cubeColumnBlock(CHISELED_END_STONE_BRICKS);
		this.block(CRACKED_EUMUS_BRICKS);
		this.block(CRACKED_END_STONE_BRICKS);
		this.block(CRACKED_PURPUR_BLOCK);

		this.logBlocks(POISE_STEM, POISE_WOOD);
		this.logBlocks(STRIPPED_POISE_STEM, STRIPPED_POISE_WOOD);
		this.logBlocks(GLOWING_POISE_STEM, GLOWING_POISE_WOOD);
		this.hangingSignBlocks(STRIPPED_POISE_STEM, POISE_HANGING_SIGNS);

		this.crossBlockWithPot(POISE_BUSH, POTTED_POISE_BUSH);

		this.woodworksBlocks(POISE_PLANKS, POISE_BOARDS, POISE_LADDER, POISE_BOOKSHELF, POISE_BEEHIVE, POISE_CHEST, TRAPPED_POISE_CHEST);
		this.chiseledBookshelfBlock(CHISELED_POISE_BOOKSHELF, BOTTOM_BOOKSHELF_POSITIONS);

		var models = this.models();
		var portaplasm = models.getExistingFile(this.modLoc("block/portaplasm_block"));
		var portaplasmDeactivated = models.getExistingFile(this.modLoc("block/portaplasm_block_deactivated"));
		var portaplasmTransition = models.getExistingFile(this.modLoc("block/portaplasm_block_transition"));
		var portaplasmActivated = models.getExistingFile(this.modLoc("block/portaplasm_block_activated"));
		this.getVariantBuilder(PORTAPLASM_BLOCK.get()).forAllStates(state -> {
			int phase = state.getValue(PortaplasmBlock.PHASE);
			return ConfiguredModel.builder().modelFile(
				phase == 1 ? portaplasmTransition :
				phase == 2 ? portaplasmActivated :
				phase == 3 ? portaplasmTransition :
				state.getValue(BlockStateProperties.POWERED) ? portaplasmDeactivated : portaplasm
			).build();
		});
	}
}