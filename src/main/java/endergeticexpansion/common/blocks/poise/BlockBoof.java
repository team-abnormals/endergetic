package endergeticexpansion.common.blocks.poise;

import endergeticexpansion.common.entities.EntityBoofBlock;
import endergeticexpansion.common.tileentities.TileEntityBoof;
import endergeticexpansion.core.registry.EEBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.entity.Entity;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class BlockBoof extends ContainerBlock {
	public static final BooleanProperty BOOFED = BooleanProperty.create("boofed");

	public BlockBoof(Properties properties) {
		super(properties);
		this.setDefaultState(this.stateContainer.getBaseState().with(BOOFED, false));
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
	
	public static void doBoof(World world, BlockPos pos) {
		if(!world.isRemote) {
			EntityBoofBlock boofBlock = new EntityBoofBlock(world, pos);
			world.func_217376_c(boofBlock);
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
	
}
