package endergeticexpansion.common.blocks.poise;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import endergeticexpansion.core.registry.EEBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PickaxeItem;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.lighting.LightEngine;
import net.minecraft.world.storage.loot.LootContext.Builder;
import net.minecraftforge.common.ToolType;

public class BlockPoiseEumusGrass extends Block {

	public BlockPoiseEumusGrass(Properties properties) {
		super(properties);
	}
	
	@Override
	public void harvestBlock(World worldIn, PlayerEntity player, BlockPos pos, BlockState state, TileEntity te, ItemStack stack) {
		if(stack.getItem() instanceof PickaxeItem) {
			int i = EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, stack);
			if(i > 0) {
				spawnAsEntity(worldIn, pos, new ItemStack(this));
			} else {
				spawnAsEntity(worldIn, pos, new ItemStack(EEBlocks.EUMUS));
			}
		} else {
			spawnAsEntity(worldIn, pos, new ItemStack(EEBlocks.EUMUS));
		}
	}
	
	@Override
	public List<ItemStack> getDrops(BlockState p_220076_1_, Builder p_220076_2_) {
		ArrayList<ItemStack> dropList = new ArrayList<ItemStack>();
		dropList.add(new ItemStack(EEBlocks.EUMUS));
		return dropList;
	}
	
	@Override
	public ToolType getHarvestTool(BlockState state) {
		return ToolType.SHOVEL;
	}

	@Override
	@SuppressWarnings("deprecation")
	public void tick(BlockState state, World worldIn, BlockPos pos, Random random) {
		if (!worldIn.isRemote) {
            if (!worldIn.isAreaLoaded(pos, 3)) return;
            if (!func_220257_b(state, worldIn, pos)) {
                worldIn.setBlockState(pos, EEBlocks.EUMUS.getDefaultState());
            } else {
            	for (int i = 0; i < 4; ++i) {
            		BlockPos blockpos = pos.add(worldIn.rand.nextInt(3) - 1, worldIn.rand.nextInt(5) - 3, worldIn.rand.nextInt(3) - 1);

                    if (blockpos.getY() >= 0 && blockpos.getY() < 256 && !worldIn.isBlockLoaded(blockpos)) {
                    	return;
                    }

                    BlockState iblockstate = worldIn.getBlockState(blockpos.up());
                    BlockState iblockstate1 = worldIn.getBlockState(blockpos);

                    if (iblockstate1.getBlock() == EEBlocks.EUMUS && func_220257_b(iblockstate, worldIn, blockpos)) {
                        worldIn.setBlockState(blockpos, this.getDefaultState());
                    }
                }
            }
        }
		super.tick(state, worldIn, pos, random);
	}
	
	private static boolean func_220257_b(BlockState p_220257_0_, IWorldReader p_220257_1_, BlockPos p_220257_2_) {
		BlockPos blockpos = p_220257_2_.up();
		BlockState blockstate = p_220257_1_.getBlockState(blockpos);
		int i = LightEngine.func_215613_a(p_220257_1_, p_220257_0_, p_220257_2_, blockstate, blockpos, Direction.UP, blockstate.getOpacity(p_220257_1_, blockpos));
		return i < p_220257_1_.getMaxLightLevel();
	}
	
	@Override
	public BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.CUTOUT_MIPPED;
	}
	
}
