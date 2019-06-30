package endergeticexpansion.common.blocks.poise;

import java.util.Random;

import javax.annotation.Nullable;

import endergeticexpansion.common.entities.bolloom.EntityBolloomFruit;
import endergeticexpansion.common.tileentities.TileEntityBolloomBud;
import endergeticexpansion.core.registry.EEBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BlockBolloomBud extends Block {
	public static final BooleanProperty OPENED = BooleanProperty.create("opened");
	public static final BooleanProperty HAS_NORTH_FRUIT = BooleanProperty.create("north_fruit");
	public static final BooleanProperty HAS_EAST_FRUIT = BooleanProperty.create("east_fruit");
	public static final BooleanProperty HAS_SOUTH_FRUIT = BooleanProperty.create("south_fruit");
	public static final BooleanProperty HAS_WEST_FRUIT = BooleanProperty.create("west_fruit");
	protected static final VoxelShape SHAPE = Block.makeCuboidShape(1.0D, 0.0D, 1.0D, 15.0D, 15.0D, 15.0D);
	protected static final VoxelShape SHAPE_OPENED = Block.makeCuboidShape(1.0D, 0.0D, 1.0D, 15.0D, 3.0D, 15.0D);

	public BlockBolloomBud(Properties properties) {
		super(properties);
		this.setDefaultState(this.stateContainer.getBaseState()
			.with(OPENED, false)
			.with(HAS_NORTH_FRUIT, false)
			.with(HAS_EAST_FRUIT, false)
			.with(HAS_SOUTH_FRUIT, false)
			.with(HAS_WEST_FRUIT, false)
		);
	}
	
	@Override
	public VoxelShape getRenderShape(BlockState state, IBlockReader worldIn, BlockPos pos) {
		return Block.makeCuboidShape(-16.0D, -16.0D, -16.0D, 32.0D, 32.0D, 32.0D);
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public void tick(BlockState state, World worldIn, BlockPos pos, Random random) {
		if(state.get(OPENED)) {
			worldIn.setBlockState(pos, state
				.with(HAS_NORTH_FRUIT, false)
				.with(HAS_EAST_FRUIT, false)
				.with(HAS_SOUTH_FRUIT, false)
				.with(HAS_WEST_FRUIT, false));
		}
		
		if(state.get(OPENED) && !state.get(HAS_NORTH_FRUIT) && !state.get(HAS_EAST_FRUIT)
			&& !state.get(HAS_SOUTH_FRUIT) && !state.get(HAS_WEST_FRUIT)) {
			worldIn.setBlockState(pos, getDefaultState());
		}
		
		super.tick(state, worldIn, pos, random);
	}
	
	@Override
	public int tickRate(IWorldReader worldIn) {
		return 40;
	}
	
	@Override
	public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
		super.onBlockHarvested(worldIn, pos, state, player);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
		super.onReplaced(state, worldIn, pos, newState, isMoving);
	}
	
	protected boolean isValidGround(BlockState state, IBlockReader worldIn, BlockPos pos) {
		Block block = state.getBlock();
		return block == EEBlocks.POISE_GRASS_BLOCK || block == Blocks.END_STONE || block == EEBlocks.EUMUS || block == EEBlocks.POISMOSS_EUMUS;
	}
	
	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
		worldIn.getPendingBlockTicks().scheduleTick(currentPos, this, 1);
		if(stateIn.isValidPosition(worldIn, currentPos)) {
			return this.placePedals(worldIn.getWorld(), currentPos) && stateIn.get(OPENED) ? stateIn.with(OPENED, true) : this.getDefaultState();
		}
		return Blocks.AIR.getDefaultState();
	}
	
	public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
		BlockPos blockpos = pos.down();
		return this.isValidGround(worldIn.getBlockState(blockpos), worldIn, blockpos) && !this.isAcrossOrAdjacentToBud(worldIn, pos);
	}
	
	@SuppressWarnings("deprecation")
	public boolean placePedals(World world, BlockPos pos) {
		if(!world.getBlockState(pos).get(OPENED) && world.getBlockState(pos.north()).isAir() && world.getBlockState(pos.south()).isAir() && world.getBlockState(pos.east()).isAir() && world.getBlockState(pos.west()).isAir()) {
			return true;
		} else if(world.getBlockState(pos).get(OPENED)) {
			return false;
		}
		return false;
	}
	
	@Override
	public boolean onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand p_220051_5_, BlockRayTraceResult p_220051_6_) {
		if(this.placePedals(world, pos)) {
			world.setBlockState(pos, getDefaultState()
				.with(HAS_NORTH_FRUIT, true)
				.with(HAS_EAST_FRUIT, true)
				.with(HAS_SOUTH_FRUIT, true)
				.with(HAS_WEST_FRUIT, true)
				.with(OPENED, true));
			int heighte = player.getRNG().nextInt(7) + 1;
			int heightn = player.getRNG().nextInt(7) + 1;
			int heights = player.getRNG().nextInt(7) + 1;
			int heightw = player.getRNG().nextInt(7) + 1;
			boolean[] chances = {
				player.getRNG().nextBoolean(),
				player.getRNG().nextBoolean(),
				player.getRNG().nextBoolean(),
				player.getRNG().nextBoolean(),
			};
			if(!chances[0] && !chances[1] && !chances[2] && !chances[3]) {
				chances[player.getRNG().nextInt(4)] = true;
			}
			if(!world.isRemote) {
				if(chances[0]) {
					EntityBolloomFruit fruitN = new EntityBolloomFruit(world, pos, pos.north().up(heightn - 1), heightn, Direction.NORTH);
					world.addEntity(fruitN);
				}
				if(chances[1]) {
					EntityBolloomFruit fruitS = new EntityBolloomFruit(world, pos, pos.south().up(heights - 1), heights, Direction.SOUTH);
					world.addEntity(fruitS);
				}
				if(chances[2]) {
					EntityBolloomFruit fruitE = new EntityBolloomFruit(world, pos, pos.east().up(heighte - 1), heighte, Direction.EAST);
					world.addEntity(fruitE);
				}
				if(chances[3]) {
					EntityBolloomFruit fruitW = new EntityBolloomFruit(world, pos, pos.west().up(heightw - 1), heightw, Direction.WEST);
					world.addEntity(fruitW);
				}
			}
		}
		return true;
	}
	
	public boolean isAcrossOrAdjacentToBud(IWorldReader world, BlockPos pos) {
		if(world.getBlockState(pos.north(2)).getBlock() == this || world.getBlockState(pos.east(2)).getBlock() == this
			|| world.getBlockState(pos.south(2)).getBlock() == this || world.getBlockState(pos.west(2)).getBlock() == this
			|| world.getBlockState(pos.north().east()).getBlock() == this || world.getBlockState(pos.north().west()).getBlock() == this
			|| world.getBlockState(pos.south().east()).getBlock() == this || world.getBlockState(pos.south().west()).getBlock() == this) {
			return true;
		}
		return false;
	}
	
	@Override
	public VoxelShape getShape(BlockState p_220053_1_, IBlockReader p_220053_2_, BlockPos p_220053_3_, ISelectionContext p_220053_4_) {
		return p_220053_1_.get(OPENED) ? SHAPE_OPENED : SHAPE;
	}
	
	@OnlyIn(Dist.CLIENT)
	public boolean hasCustomBreakingProgress(BlockState state) {
		return true;
	}
	
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(OPENED, HAS_NORTH_FRUIT, HAS_EAST_FRUIT, HAS_SOUTH_FRUIT, HAS_WEST_FRUIT);
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}
	
	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new TileEntityBolloomBud();
	}
	
	@Override
	public boolean isSolid(BlockState state) {
		return false;
	}
	
	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.ENTITYBLOCK_ANIMATED;
	}
	
}
