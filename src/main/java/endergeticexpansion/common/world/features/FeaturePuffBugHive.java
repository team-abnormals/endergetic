package endergeticexpansion.common.world.features;

import java.util.List;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Supplier;

import com.google.common.collect.Lists;
import com.mojang.datafixers.Dynamic;

import endergeticexpansion.common.entities.puffbug.EntityPuffBug;
import endergeticexpansion.core.registry.EEBlocks;
import endergeticexpansion.core.registry.EEEntities;
import net.minecraft.block.BlockState;
import net.minecraft.entity.SpawnReason;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

public class FeaturePuffBugHive extends Feature<NoFeatureConfig> {
	
	private Supplier<BlockState> HIVE_STATE(boolean hanger) {
		return hanger ? () -> EEBlocks.HIVE_HANGER.get().getDefaultState() : () -> EEBlocks.PUFFBUG_HIVE.get().getDefaultState();
	}

	public FeaturePuffBugHive(Function<Dynamic<?>, ? extends NoFeatureConfig> configFactoryIn) {
		super(configFactoryIn);
	}
	
	@Override
	public boolean place(IWorld world, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos pos, NoFeatureConfig config) {
		if(rand.nextFloat() < 0.1F) return false;
		BlockPos hivePos = pos.down();
		
		if(world.getBlockState(pos.up()).getBlock() == EEBlocks.POISE_LOG.get() || world.getBlockState(pos.up()).getBlock() == EEBlocks.POISE_LOG_GLOWING.get()) {
			if(world.getBlockState(pos).getMaterial().isReplaceable() && world.getBlockState(pos).getMaterial().isReplaceable()) {
				world.setBlockState(pos, this.HIVE_STATE(true).get(), 2);
				world.setBlockState(hivePos, this.HIVE_STATE(false).get(), 2);
				
				this.spawnPuffBugs(world, hivePos, rand);
				return true;
			}
		} else {
			if(world.getBlockState(pos.up()).getBlock() == EEBlocks.POISE_CLUSTER.get() && world.getHeight() > 90) {
				if(world.getBlockState(pos).getMaterial().isReplaceable() && world.getBlockState(pos).getMaterial().isReplaceable()) {
					world.setBlockState(pos, this.HIVE_STATE(true).get(), 2);
					world.setBlockState(hivePos, this.HIVE_STATE(false).get(), 2);
					
					this.spawnPuffBugs(world, hivePos, rand);
					return true;
				}
			}
		}
		return false;
	}
	
	private void spawnPuffBugs(IWorld world, BlockPos pos, Random rand) {
		int timesToRun = rand.nextInt(4) + 2;
		
		for(Direction openDirections : this.getOpenSides(world, pos)) {
			BlockPos offset = pos.offset(openDirections);
			EntityPuffBug puffbug = EEEntities.PUFF_BUG.get().create(world.getWorld());
			puffbug.setLocationAndAngles(offset.getX() + 0.5F, offset.getY() + 0.5F, offset.getZ() + 0.5F, 0.0F, 0.0F);
			puffbug.onInitialSpawn(world, world.getDifficultyForLocation(pos), SpawnReason.STRUCTURE, null, null);
			puffbug.setHivePos(pos);
			world.addEntity(puffbug);
				
			if(timesToRun-- <= 0) break;
		}
	}
	
	private List<Direction> getOpenSides(IWorld world, BlockPos pos) {
		List<Direction> openDirections = Lists.newArrayList();
		for(Direction directions : Direction.values()) {
			if(directions != Direction.UP) {
				BlockPos offsetPos = pos.offset(directions);
				if(world.isAirBlock(offsetPos) && world.isAirBlock(offsetPos.up())) {
					openDirections.add(directions);
				}
			}
		}
		return openDirections;
	}

}
