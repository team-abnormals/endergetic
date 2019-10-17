package endergeticexpansion.common.world.features;

import java.util.Random;

import endergeticexpansion.common.blocks.BlockAcidianLantern;
import endergeticexpansion.core.registry.EEBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.feature.EndPodiumFeature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

public class EndergeticEndPodiumFeature extends EndPodiumFeature {
	public static final BlockPos END_PODIUM_LOCATION = BlockPos.ZERO;
	private static final BlockState MYSTICAL_OBSIDIAN = EEBlocks.MYSTICAL_OBSIDIAN.getDefaultState();
	private static final BlockState MYSTICAL_OBSIDIAN_WALL = EEBlocks.MYSTICAL_OBSIDIAN_WALL.getDefaultState();
	private static final BlockState MYSTICAL_OBSIDIAN_RUNE = EEBlocks.MYSTICAL_OBSIDIAN_RUNE.getDefaultState();
	private static final BlockState ACIDIAN_LANTERN = EEBlocks.ACIDIAN_LANTERN.getDefaultState().with(BlockAcidianLantern.FACING, Direction.UP);
	private static final BlockState MYSTICAL_OBSIDIAN_ACTIVATION_RUNE(boolean active) {
		return active ? EEBlocks.MYSTICAL_OBSIDIAN_ACTIVATION_RUNE_ACTIVE.getDefaultState() : EEBlocks.MYSTICAL_OBSIDIAN_ACTIVATION_RUNE.getDefaultState();
	}
	private final boolean activePortal;

	public EndergeticEndPodiumFeature(boolean activePortalIn) {
		super(activePortalIn);
		this.activePortal = activePortalIn;
	}
	
	public boolean place(IWorld worldIn, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos pos, NoFeatureConfig config) {
		for(BlockPos blockpos : BlockPos.getAllInBoxMutable(new BlockPos(pos.getX() - 4, pos.getY() - 1, pos.getZ() - 4), new BlockPos(pos.getX() + 4, pos.getY() + 32, pos.getZ() + 4))) {
			boolean flag = blockpos.withinDistance(pos, 2.5D);
			if(flag || blockpos.withinDistance(pos, 3.5D)) {
				if(blockpos.getY() < pos.getY()) {
					if(flag) {
						this.setBlockState(worldIn, blockpos.up(), MYSTICAL_OBSIDIAN);
					} else if (blockpos.getY() < pos.getY()) {
						this.setBlockState(worldIn, blockpos.up(), Blocks.END_STONE.getDefaultState());
					}
				} else if(blockpos.getY() > pos.getY()) {
					this.setBlockState(worldIn, blockpos.up(), Blocks.AIR.getDefaultState());
				} else if(!flag) {
					this.setBlockState(worldIn, blockpos.up(), MYSTICAL_OBSIDIAN);
				} else if(this.activePortal) {
					this.setBlockState(worldIn, blockpos.up(), Blocks.END_PORTAL.getDefaultState());
				} else {
					this.setBlockState(worldIn, blockpos.up(), Blocks.AIR.getDefaultState());
				}
			}
		}
		
		this.setBlockState(worldIn, pos.up(2).north(2).east(2), MYSTICAL_OBSIDIAN_WALL);
		this.setBlockState(worldIn, pos.up(2).north(2).west(2), MYSTICAL_OBSIDIAN_WALL);
		this.setBlockState(worldIn, pos.up(2).south(2).east(2), MYSTICAL_OBSIDIAN_WALL);
		this.setBlockState(worldIn, pos.up(2).south(2).west(2), MYSTICAL_OBSIDIAN_WALL);
		
		for(int i = 1; i < 6; i++) {
			if(i > 3) {
				this.setBlockState(worldIn, pos.up(i), MYSTICAL_OBSIDIAN_WALL);
			} else {
				this.setBlockState(worldIn, pos.up(i), MYSTICAL_OBSIDIAN);
			}
		}
		
		for(int i = 2; i < 6; i++) {
			this.createRuneSide(worldIn, pos, Direction.byIndex(i), this.activePortal);
		}
		
		if(this.activePortal) {
			this.setBlockState(worldIn, pos.up(3).north(2).east(2), ACIDIAN_LANTERN);
			this.setBlockState(worldIn, pos.up(3).north(2).west(2), ACIDIAN_LANTERN);
			this.setBlockState(worldIn, pos.up(3).south(2).east(2), ACIDIAN_LANTERN);
			this.setBlockState(worldIn, pos.up(3).south(2).west(2), ACIDIAN_LANTERN);
		}
		
		return true;
	}
	
	private void createRuneSide(IWorld world, BlockPos pos, Direction direction, boolean active) {
		this.setBlockState(world, pos.offset(direction, 3).offset(direction.rotateY()).up(), MYSTICAL_OBSIDIAN_RUNE.with(HorizontalBlock.HORIZONTAL_FACING, direction.getOpposite()));
		this.setBlockState(world, pos.offset(direction, 3).up(), MYSTICAL_OBSIDIAN_ACTIVATION_RUNE(active).with(HorizontalBlock.HORIZONTAL_FACING, direction));
		this.setBlockState(world, pos.offset(direction, 3).offset(direction.rotateYCCW()).up(), MYSTICAL_OBSIDIAN_RUNE.with(HorizontalBlock.HORIZONTAL_FACING, direction));
	}
}