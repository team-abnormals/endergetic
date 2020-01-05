package endergeticexpansion.common.blocks.poise;

import java.util.List;
import java.util.Random;

import endergeticexpansion.api.util.MathUtils;
import endergeticexpansion.api.util.NetworkUtil;
import endergeticexpansion.client.particle.EEParticles;
import endergeticexpansion.common.entities.EntityPoiseCluster;
import endergeticexpansion.core.registry.EESounds;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShearsItem;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BlockPoiseCluster extends Block {
	
	public BlockPoiseCluster(Properties properties) {
		super(properties);
	}
	
	@OnlyIn(Dist.CLIENT)
	@Override
	public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
		if(rand.nextFloat() > 0.05F || !worldIn.getBlockState(pos.up()).getCollisionShape(worldIn, pos.up()).isEmpty()) return;
		
		double offsetX = MathUtils.makeNegativeRandomly(rand.nextFloat() * 0.25F, rand);
		double offsetZ = MathUtils.makeNegativeRandomly(rand.nextFloat() * 0.25F, rand);
		
		double x = pos.getX() + 0.5D + offsetX;
		double y = pos.getY() + 0.95D + (rand.nextFloat() * 0.05F);
		double z = pos.getZ() + 0.5D + offsetZ;
		
		worldIn.addParticle(EEParticles.SHORT_POISE_BUBBLE.get(), x, y, z, 0.0D, 0.0D, 0.0D);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void harvestBlock(World worldIn, PlayerEntity player, BlockPos pos, BlockState state, TileEntity te, ItemStack stack) {
		if(stack.getItem() instanceof ShearsItem) {
			super.harvestBlock(worldIn, player, pos, state, te, stack);
		} else {
			AxisAlignedBB bb = new AxisAlignedBB(pos).offset(0, 1, 0);
			List<Entity> entities = worldIn.getEntitiesWithinAABB(Entity.class, bb);
			boolean isBlocked = false;
			for(int i = 0; i < entities.size(); i++) {
				Entity entity = entities.get(i);
				
				if(entity instanceof EntityPoiseCluster) {
					isBlocked = true;
				}
			}
			if(worldIn.getBlockState(pos.up()).isAir() && !isBlocked) {
				if (!worldIn.isRemote) {
					EntityPoiseCluster cluster = new EntityPoiseCluster(worldIn, pos, pos.getX(), pos.getY(), pos.getZ());
					cluster.setBlocksToMoveUp(10);
					worldIn.addEntity(cluster);
					
					Random rand = player.getRNG();
					
					for(int i = 0; i < 8; i++) {
						double offsetX = MathUtils.makeNegativeRandomly(rand.nextFloat() * 0.25F, rand);
						double offsetZ = MathUtils.makeNegativeRandomly(rand.nextFloat() * 0.25F, rand);
					
						double x = pos.getX() + 0.5D + offsetX;
						double y = pos.getY() + 0.5D + (rand.nextFloat() * 0.05F);
						double z = pos.getZ() + 0.5D + offsetZ;
					
						NetworkUtil.spawnParticle("endergetic:short_poise_bubble", x, y, z, MathUtils.makeNegativeRandomly((rand.nextFloat() * 0.1F), rand) + 0.025F, (rand.nextFloat() * 0.15F) + 0.1F, MathUtils.makeNegativeRandomly((rand.nextFloat() * 0.1F), rand) + 0.025F);
					}
				}
				worldIn.removeBlock(pos, false);
			} else {
				worldIn.setBlockState(pos, getDefaultState());
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
		if(entityIn instanceof AbstractArrowEntity || entityIn instanceof TridentEntity) {
			AxisAlignedBB bb = new AxisAlignedBB(pos).offset(0, 1, 0);
			List<Entity> entities = worldIn.getEntitiesWithinAABB(Entity.class, bb);
			boolean isBlocked = false;
			for(int i = 0; i < entities.size(); i++) {
				Entity entity = entities.get(i);
				
				if(entity instanceof EntityPoiseCluster) {
					isBlocked = true;
				}
			}
			if(worldIn.getBlockState(pos.up()).isAir() && !isBlocked) {
				if(!worldIn.isRemote) {
					EntityPoiseCluster cluster = new EntityPoiseCluster(worldIn, pos, pos.getX(), pos.getY(), pos.getZ());
					cluster.setBlocksToMoveUp(10);
					worldIn.addEntity(cluster);
					entityIn.remove();
					worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 2);
					worldIn.playSound(null, pos, EESounds.CLUSTER_BREAK.get(), SoundCategory.BLOCKS, 0.90F, 0.75F);
					
					Random rand = new Random();
					
					for(int i = 0; i < 8; i++) {
						double offsetX = MathUtils.makeNegativeRandomly(rand.nextFloat() * 0.25F, rand);
						double offsetZ = MathUtils.makeNegativeRandomly(rand.nextFloat() * 0.25F, rand);
					
						double x = pos.getX() + 0.5D + offsetX;
						double y = pos.getY() + 0.5D + (rand.nextFloat() * 0.05F);
						double z = pos.getZ() + 0.5D + offsetZ;
					
						NetworkUtil.spawnParticle("endergetic:short_poise_bubble", x, y, z, MathUtils.makeNegativeRandomly((rand.nextFloat() * 0.1F), rand) + 0.025F, (rand.nextFloat() * 0.15F) + 0.1F, MathUtils.makeNegativeRandomly((rand.nextFloat() * 0.1F), rand) + 0.025F);
					}
				}
			} else {
				worldIn.setBlockState(pos, getDefaultState());
			}
		}
		super.onEntityCollision(state, worldIn, pos, entityIn);
	}
	
	@Override
	public BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.TRANSLUCENT;
	}
	
	@SuppressWarnings("deprecation")
	@OnlyIn(Dist.CLIENT)
	public boolean isSideInvisible(BlockState state, BlockState adjacentBlockState, Direction side) {
		return adjacentBlockState.getBlock() == this ? true : super.isSideInvisible(state, adjacentBlockState, side);
	}
	
	@Override
	public SoundType getSoundType(BlockState p_220072_1_) {
		return EESounds.EESoundTypes.CLUSTER;
	}
	
}
