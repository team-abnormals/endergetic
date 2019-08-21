package endergeticexpansion.common.world.features;

import java.util.Random;
import java.util.function.Function;

import com.mojang.datafixers.Dynamic;

import endergeticexpansion.common.blocks.poise.BlockBolloomBud;
import endergeticexpansion.common.tileentities.TileEntityBolloomBud;
import endergeticexpansion.core.registry.EEBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

/**
 * @author - SmellyModder(Luke Tonon)
 */
public class FeatureBolloomBud extends Feature<NoFeatureConfig> {
	protected static final BlockState BOLLOOM_BUD = EEBlocks.BOLLOOM_BUD.getDefaultState();

	public FeatureBolloomBud(Function<Dynamic<?>, ? extends NoFeatureConfig> configFactoryIn) {
		super(configFactoryIn);
	}

	@Override
	public boolean place(IWorld world, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos pos, NoFeatureConfig config) {
		if(rand.nextFloat() >= 0.60) {
			if(world.getBlockState(pos.down()).getBlock() == EEBlocks.POISE_GRASS_BLOCK && world.getBlockState(pos).getMaterial().isReplaceable() && world.getBlockState(pos.up()).getBlock() != EEBlocks.POISE_GRASS_TALL) {
				world.setBlockState(pos, BOLLOOM_BUD, 2);
				return true;
			}
		} else {
			if(world.getBlockState(pos.down()).getBlock() == EEBlocks.POISE_GRASS_BLOCK && this.isAreaReplacable(world, pos) && world.getBlockState(pos.up()).getBlock() != EEBlocks.POISE_GRASS_TALL) {
				world.setBlockState(pos, BOLLOOM_BUD.with(BlockBolloomBud.OPENED, true), 2);
				this.setBud(world, pos, rand);
				return true;
			}
		}
		return false;
	}
	
	private void setBud(IWorld IWorld, BlockPos pos, Random rand) {
		boolean[] chances = {
			rand.nextBoolean(),
			rand.nextBoolean(),
			rand.nextBoolean(),
			rand.nextBoolean(),
		};
		if(!chances[0] && !chances[1] && !chances[2] && !chances[3]) {
			chances[rand.nextInt(4)] = true;
		}
		if(chances[0]) {
			IWorld.setBlockState(pos, IWorld.getBlockState(pos).with(BlockBolloomBud.HAS_NORTH_FRUIT, true), 2);
		}
		if(chances[1]) {
			IWorld.setBlockState(pos, IWorld.getBlockState(pos).with(BlockBolloomBud.HAS_SOUTH_FRUIT, true), 2);
		}
		if(chances[2]) {
			IWorld.setBlockState(pos, IWorld.getBlockState(pos).with(BlockBolloomBud.HAS_EAST_FRUIT, true), 2);
		}
		if(chances[3]) {
			IWorld.setBlockState(pos, IWorld.getBlockState(pos).with(BlockBolloomBud.HAS_WEST_FRUIT, true), 2);
		}
		((TileEntityBolloomBud)IWorld.getTileEntity(pos)).markForSpawning();
	}
	
	public boolean isAreaReplacable(IWorld world, BlockPos pos) {
		for(int y = pos.getY() + 1; y < pos.getY() + 7; y++) {
			for(int x = pos.getX() - 1; x < pos.getX() + 1; x++) {
				for(int z = pos.getZ() - 1; z < pos.getZ() + 1; z++) {
					if(!world.getBlockState(new BlockPos(x, y, z)).getMaterial().isReplaceable() || !canFitCross(world, pos)) {
						return false;
					}
				}
			}
		}
		return true;
	}
	
	@SuppressWarnings("deprecation")
	public boolean canFitCross(IWorld world, BlockPos pos) {
		if(world.getBlockState(pos.north().up()).getBlock() != EEBlocks.POISE_GRASS_TALL && world.getBlockState(pos.north()).isAir() && world.getBlockState(pos.east().up()).getBlock() != EEBlocks.POISE_GRASS_TALL && world.getBlockState(pos.east()).isAir() && world.getBlockState(pos.south().up()).getBlock() != EEBlocks.POISE_GRASS_TALL && world.getBlockState(pos.south()).isAir() && world.getBlockState(pos.west().up()).getBlock() != EEBlocks.POISE_GRASS_TALL && world.getBlockState(pos.west()).isAir()) {
			return true;
		}
		return false;
	}
}
