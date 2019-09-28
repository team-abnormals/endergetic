package endergeticexpansion.common.blocks.poise.boof;

import endergeticexpansion.common.entities.EntityBoofBlock;
import endergeticexpansion.common.tileentities.boof.TileEntityBoof;
import endergeticexpansion.core.registry.EEBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.block.DispenserBlock;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.OptionalDispenseBehavior;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.EntitySpawnPlacementRegistry.PlacementType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class BlockBoof extends ContainerBlock {
	public static final BooleanProperty BOOFED = BooleanProperty.create("boofed");

	public BlockBoof(Properties properties) {
		super(properties);
		this.setDefaultState(this.stateContainer.getBaseState().with(BOOFED, false));
	}
	
	@Override
	public PathNodeType getAiPathNodeType(BlockState state, IBlockReader world, BlockPos pos, MobEntity entity) {
		return PathNodeType.DAMAGE_CACTUS;
	}
	
	@Override
	public boolean canCreatureSpawn(BlockState state, IBlockReader world, BlockPos pos, PlacementType type, EntityType<?> entityType) {
		return false;
	}
	
	public boolean canProvidePower(BlockState state) {
		return state.get(BOOFED);
	}
	
	public int getWeakPower(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side) {
		return 15;
	}
	
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(BOOFED);
	}
	
	@Override
	public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance) {
		if (entityIn.isSneaking()) {
			super.onFallenUpon(worldIn, pos, entityIn, fallDistance);
		} else {
			entityIn.fall(fallDistance, 0.0F);
		}
	}
	
	@Override
	public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
		if(entityIn instanceof PlayerEntity) {
			((PlayerEntity)entityIn).entityCollisionReduction = Float.MAX_VALUE;
			((PlayerEntity)entityIn).onGround = false;
			((PlayerEntity)entityIn).fallDistance = 0;
		}
	}
	
	public static void doBoof(World world, BlockPos pos) {
		if(!world.isRemote) {
			EntityBoofBlock boofBlock = new EntityBoofBlock(world, pos);
			world.addEntity(boofBlock);
		}
		world.setBlockState(pos, EEBlocks.BOOF_BLOCK.getDefaultState().with(BOOFED, true));
	}
	
	@Override
	public TileEntity createNewTileEntity(IBlockReader worldIn) {
		return new TileEntityBoof();
	}
	
	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}
	
	public static class BoofDispenseBehavior extends OptionalDispenseBehavior {
		
		@Override
		protected ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
			World world = source.getWorld();
			this.successful = true;
            BlockPos blockpos = source.getBlockPos().offset(source.getBlockState().get(DispenserBlock.FACING));
            BlockState blockstate = world.getBlockState(blockpos);
            if(!blockstate.getMaterial().isReplaceable()) {
    			this.successful = false;
            } else {
            	this.successful = true;
            }
            
            if (this.successful) {
            	IFluidState fluidstate = world.getFluidState(blockpos);
            	if(fluidstate.getFluid() == Fluids.WATER) {
            		world.setBlockState(blockpos, EEBlocks.BOOF_BLOCK_DISPENSED.getDefaultState().with(BlockDispensedBoof.WATERLOGGED, true).with(BlockDispensedBoof.FACING, source.getBlockState().get(DispenserBlock.FACING)));
            	} else {
            		world.setBlockState(blockpos, EEBlocks.BOOF_BLOCK_DISPENSED.getDefaultState().with(BlockDispensedBoof.FACING, source.getBlockState().get(DispenserBlock.FACING)));
            	}
            }
            
            return stack;
		}
		
	}
	
}
