package endergeticexpansion.common.world.features;

import java.util.List;
import java.util.Random;

import com.mojang.serialization.Codec;

import endergeticexpansion.core.registry.EEBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.PaneBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.EnderCrystalEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.EndSpikeFeature;
import net.minecraft.world.gen.feature.EndSpikeFeatureConfig;
import net.minecraft.world.gen.feature.structure.StructureManager;

public class EndergeticEndSpikeFeature extends EndSpikeFeature {

	public EndergeticEndSpikeFeature(Codec<EndSpikeFeatureConfig> config) {
		super(config);
	}
	
	@Override
	public boolean func_230362_a_(ISeedReader worldIn, StructureManager manager, ChunkGenerator generator, Random rand, BlockPos pos, EndSpikeFeatureConfig config) {
		List<EndSpikeFeature.EndSpike> list = config.getSpikes();
		if(list.isEmpty()) {
			list = func_236356_a_(worldIn);
		}

		for(EndSpikeFeature.EndSpike endspikefeature$endspike : list) {
			if(endspikefeature$endspike.doesStartInChunk(pos)) {
				this.createSpike(worldIn, rand, config, endspikefeature$endspike);
			}
		}
		
		return true;
	}
	
	private void createSpike(IWorld p_214553_1_, Random p_214553_2_, EndSpikeFeatureConfig p_214553_3_, EndSpikeFeature.EndSpike p_214553_4_) {
		int i = p_214553_4_.getRadius();

		for(BlockPos blockpos : BlockPos.getAllInBoxMutable(new BlockPos(p_214553_4_.getCenterX() - i, 0, p_214553_4_.getCenterZ() - i), new BlockPos(p_214553_4_.getCenterX() + i, p_214553_4_.getHeight() + 10, p_214553_4_.getCenterZ() + i))) {
			if(blockpos.withinDistance(new BlockPos(p_214553_4_.getCenterX(), blockpos.getY(), p_214553_4_.getCenterZ()), (double)i) && blockpos.getY() < p_214553_4_.getHeight()) {
				this.setBlockState(p_214553_1_, blockpos, Blocks.OBSIDIAN.getDefaultState());
			} else if (blockpos.getY() > 65) {
				this.setBlockState(p_214553_1_, blockpos, Blocks.AIR.getDefaultState());
			}
		}

		if(p_214553_4_.isGuarded()) {
			BlockPos.Mutable blockpos$mutableblockpos = new BlockPos.Mutable();

			for(int k = -2; k <= 2; ++k) {
				for(int l = -2; l <= 2; ++l) {
	            	for(int i1 = 0; i1 <= 3; ++i1) {
	            		boolean flag = MathHelper.abs(k) == 2;
	            		boolean flag1 = MathHelper.abs(l) == 2;
	            		boolean flag2 = i1 == 3;
	            		if(flag || flag1 || flag2) {
	            			boolean flag3 = k == -2 || k == 2 || flag2;
	            			boolean flag4 = l == -2 || l == 2 || flag2;
	            			BlockState blockstate = Blocks.IRON_BARS.getDefaultState().with(PaneBlock.NORTH, Boolean.valueOf(flag3 && l != -2)).with(PaneBlock.SOUTH, Boolean.valueOf(flag3 && l != 2)).with(PaneBlock.WEST, Boolean.valueOf(flag4 && k != -2)).with(PaneBlock.EAST, Boolean.valueOf(flag4 && k != 2));
	            			this.setBlockState(p_214553_1_, blockpos$mutableblockpos.setPos(p_214553_4_.getCenterX() + k, p_214553_4_.getHeight() + i1, p_214553_4_.getCenterZ() + l), blockstate);
	            		}
	            	}
				}
			}
		}

		EnderCrystalEntity endercrystalentity = EntityType.END_CRYSTAL.create(p_214553_1_.getWorld());
		endercrystalentity.setBeamTarget(p_214553_3_.getCrystalBeamTarget());
		endercrystalentity.setInvulnerable(p_214553_3_.isCrystalInvulnerable());
		endercrystalentity.setLocationAndAngles((double)((float)p_214553_4_.getCenterX() + 0.5F), (double)(p_214553_4_.getHeight() + 1), (double)((float)p_214553_4_.getCenterZ() + 0.5F), p_214553_2_.nextFloat() * 360.0F, 0.0F);
		p_214553_1_.addEntity(endercrystalentity);
		this.setBlockState(p_214553_1_, new BlockPos(p_214553_4_.getCenterX(), p_214553_4_.getHeight(), p_214553_4_.getCenterZ()), EEBlocks.CRYSTAL_HOLDER.get().getDefaultState());
	}
	
	private void setBlockState(IWorld world, BlockPos pos, BlockState state) {
		world.setBlockState(pos, state, 2);
	}
}