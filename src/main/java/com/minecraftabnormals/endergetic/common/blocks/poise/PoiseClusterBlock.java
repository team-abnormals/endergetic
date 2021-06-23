package com.minecraftabnormals.endergetic.common.blocks.poise;

import java.util.Random;

import com.minecraftabnormals.abnormals_core.core.util.MathUtil;
import com.minecraftabnormals.abnormals_core.core.util.NetworkUtil;
import com.minecraftabnormals.endergetic.client.particles.EEParticles;
import com.minecraftabnormals.endergetic.common.entities.PoiseClusterEntity;
import com.minecraftabnormals.endergetic.core.registry.EESounds;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.Tags;

public class PoiseClusterBlock extends Block {

	public PoiseClusterBlock(Properties properties) {
		super(properties);
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
		if (rand.nextFloat() > 0.05F || !worldIn.getBlockState(pos.up()).getCollisionShape(worldIn, pos.up()).isEmpty())
			return;

		double offsetX = MathUtil.makeNegativeRandomly(rand.nextFloat() * 0.25F, rand);
		double offsetZ = MathUtil.makeNegativeRandomly(rand.nextFloat() * 0.25F, rand);

		double x = pos.getX() + 0.5D + offsetX;
		double y = pos.getY() + 0.95D + (rand.nextFloat() * 0.05F);
		double z = pos.getZ() + 0.5D + offsetZ;

		worldIn.addParticle(EEParticles.SHORT_POISE_BUBBLE.get(), x, y, z, 0.0D, 0.0D, 0.0D);
	}

	@Override
	public void onBlockClicked(BlockState state, World world, BlockPos pos, PlayerEntity player) {
		ItemStack stack = player.getHeldItemMainhand();
		Item item = stack.getItem();
		if (!item.isIn(Tags.Items.SHEARS)) {
			if (world.isAirBlock(pos.up()) && world.getEntitiesWithinAABB(PoiseClusterEntity.class, new AxisAlignedBB(pos).offset(0, 1, 0)).isEmpty()) {
				if (!world.isRemote) {
					PoiseClusterEntity cluster = new PoiseClusterEntity(world, pos, pos.getX(), pos.getY(), pos.getZ());
					cluster.setBlocksToMoveUp(10);
					world.addEntity(cluster);

					Random rand = player.getRNG();

					for (int i = 0; i < 8; i++) {
						double offsetX = MathUtil.makeNegativeRandomly(rand.nextFloat() * 0.25F, rand);
						double offsetZ = MathUtil.makeNegativeRandomly(rand.nextFloat() * 0.25F, rand);

						double x = pos.getX() + 0.5D + offsetX;
						double y = pos.getY() + 0.5D + (rand.nextFloat() * 0.05F);
						double z = pos.getZ() + 0.5D + offsetZ;

						NetworkUtil.spawnParticle("endergetic:short_poise_bubble", x, y, z, MathUtil.makeNegativeRandomly((rand.nextFloat() * 0.1F), rand) + 0.025F, (rand.nextFloat() * 0.15F) + 0.1F, MathUtil.makeNegativeRandomly((rand.nextFloat() * 0.1F), rand) + 0.025F);
					}
				}
				world.removeBlock(pos, false);
			}
		} else {
			world.destroyBlock(pos, false);
			stack.damageItem(1, player, (broken) -> broken.sendBreakAnimation(player.getActiveHand()));
			spawnAsEntity(world, pos, new ItemStack(this.asItem()));
		}
	}

	@Override
	public void onProjectileCollision(World world, BlockState state, BlockRayTraceResult hit, ProjectileEntity projectile) {
		BlockPos pos = hit.getPos();
		if (world.isAirBlock(pos.up()) && world.getEntitiesWithinAABB(PoiseClusterEntity.class, new AxisAlignedBB(pos.up())).isEmpty()) {
			if (!world.isRemote) {
				PoiseClusterEntity cluster = new PoiseClusterEntity(world, pos, pos.getX(), pos.getY(), pos.getZ());
				cluster.setBlocksToMoveUp(10);
				world.addEntity(cluster);

				if (projectile instanceof ArrowEntity) {
					projectile.remove();
				}

				world.setBlockState(pos, Blocks.AIR.getDefaultState(), 2);
				world.playSound(null, pos, EESounds.CLUSTER_BREAK.get(), SoundCategory.BLOCKS, 0.90F, 0.75F);

				Random rand = new Random();
				for (int i = 0; i < 8; i++) {
					double offsetX = MathUtil.makeNegativeRandomly(rand.nextFloat() * 0.25F, rand);
					double offsetZ = MathUtil.makeNegativeRandomly(rand.nextFloat() * 0.25F, rand);

					double x = pos.getX() + 0.5D + offsetX;
					double y = pos.getY() + 0.5D + (rand.nextFloat() * 0.05F);
					double z = pos.getZ() + 0.5D + offsetZ;

					NetworkUtil.spawnParticle("endergetic:short_poise_bubble", x, y, z, MathUtil.makeNegativeRandomly((rand.nextFloat() * 0.1F), rand) + 0.025F, (rand.nextFloat() * 0.15F) + 0.1F, MathUtil.makeNegativeRandomly((rand.nextFloat() * 0.1F), rand) + 0.025F);
				}
			}
		}
	}

	@SuppressWarnings("deprecation")
	@OnlyIn(Dist.CLIENT)
	public boolean isSideInvisible(BlockState state, BlockState adjacentBlockState, Direction side) {
		return adjacentBlockState.getBlock() == this || super.isSideInvisible(state, adjacentBlockState, side);
	}

	@SuppressWarnings("deprecation")
	@Override
	public SoundType getSoundType(BlockState p_220072_1_) {
		return EESounds.EESoundTypes.CLUSTER;
	}

}