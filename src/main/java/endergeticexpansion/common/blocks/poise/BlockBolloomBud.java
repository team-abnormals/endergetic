package endergeticexpansion.common.blocks.poise;

import java.util.Random;

import javax.annotation.Nullable;

import endergeticexpansion.api.util.MathUtils;
import endergeticexpansion.client.particle.EEParticles;
import endergeticexpansion.common.tileentities.TileEntityBolloomBud;
import endergeticexpansion.core.registry.other.EETags;
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
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BlockBolloomBud extends Block {
	public static final BooleanProperty OPENED = BooleanProperty.create("opened");
	private static final VoxelShape INSIDE = makeCuboidShape(3.5D, 0.0D, 3.5D, 12.5D, 15.0D, 12.5D);
	protected static final VoxelShape SHAPE = VoxelShapes.combineAndSimplify(makeCuboidShape(1.0D, 0.0D, 1.0D, 15.0D, 15.0D, 15.0D), VoxelShapes.or(INSIDE), IBooleanFunction.ONLY_FIRST);
	protected static final VoxelShape SHAPE_OPENED = Block.makeCuboidShape(1.0D, 0.0D, 1.0D, 15.0D, 3.0D, 15.0D);

	public BlockBolloomBud(Properties properties) {
		super(properties);
		this.setDefaultState(this.stateContainer.getBaseState().with(OPENED, false));
	}
	
	@Override
	public VoxelShape getRenderShape(BlockState state, IBlockReader worldIn, BlockPos pos) {
		return Block.makeCuboidShape(-16.0D, -16.0D, -16.0D, 32.0D, 32.0D, 32.0D);
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
		return block == Blocks.END_STONE.getBlock() || block.isIn(EETags.Blocks.END_PLANTABLE) || block.isIn(EETags.Blocks.POISE_PLANTABLE);
	}
	
	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld world, BlockPos currentPos, BlockPos facingPos) {
		if(stateIn.isValidPosition(world, currentPos)) {
			return this.placePedals(world.getWorld(), currentPos) && stateIn.get(OPENED) ? stateIn.with(OPENED, true) : this.resetBud(world, currentPos);
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
			world.setBlockState(pos, getDefaultState().with(OPENED, true));
			
			if(world.getTileEntity(pos) instanceof TileEntityBolloomBud) {
				((TileEntityBolloomBud) world.getTileEntity(pos)).startGrowing(false);
			}
			
			if(world.isRemote) {
				Random rand = player.getRNG();
				
				for(int i = 0; i < 8; i++) {
					double offsetX = MathUtils.makeNegativeRandomly(rand.nextFloat() * 0.25F, rand);
					double offsetZ = MathUtils.makeNegativeRandomly(rand.nextFloat() * 0.25F, rand);
				
					double x = pos.getX() + 0.5D + offsetX;
					double y = pos.getY() + 0.5D + (rand.nextFloat() * 0.05F);
					double z = pos.getZ() + 0.5D + offsetZ;
				
					world.addParticle(EEParticles.SHORT_POISE_BUBBLE.get(), x, y, z, MathUtils.makeNegativeRandomly((rand.nextFloat() * 0.1F), rand) + 0.025F, (rand.nextFloat() * 0.15F) + 0.1F, MathUtils.makeNegativeRandomly((rand.nextFloat() * 0.1F), rand) + 0.025F);
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
	
	private BlockState resetBud(IWorld world, BlockPos pos) {
		if(world.getTileEntity(pos) instanceof TileEntityBolloomBud) {
			((TileEntityBolloomBud) world.getTileEntity(pos)).resetGrowing();
		}
		return this.getDefaultState();
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
		builder.add(OPENED);
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