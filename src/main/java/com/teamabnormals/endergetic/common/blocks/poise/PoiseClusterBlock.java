package com.teamabnormals.endergetic.common.blocks.poise;

import com.teamabnormals.endergetic.client.particles.EEParticles;
import com.teamabnormals.endergetic.common.entities.PoiseClusterEntity;
import com.teamabnormals.endergetic.core.registry.EESounds;

import com.teamabnormals.blueprint.core.util.NetworkUtil;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.Tags;

public class PoiseClusterBlock extends Block {

	public PoiseClusterBlock(Properties properties) {
		super(properties);
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void animateTick(BlockState stateIn, Level worldIn, BlockPos pos, RandomSource rand) {
		if (rand.nextFloat() > 0.05F || !worldIn.getBlockState(pos.above()).getCollisionShape(worldIn, pos.above()).isEmpty())
			return;

		double offsetX = GlowingPoiseStemBlock.makeNegativeRandomly(rand.nextFloat() * 0.25F, rand);
		double offsetZ = GlowingPoiseStemBlock.makeNegativeRandomly(rand.nextFloat() * 0.25F, rand);

		double x = pos.getX() + 0.5D + offsetX;
		double y = pos.getY() + 0.95D + (rand.nextFloat() * 0.05F);
		double z = pos.getZ() + 0.5D + offsetZ;

		worldIn.addParticle(EEParticles.SHORT_POISE_BUBBLE.get(), x, y, z, 0.0D, 0.0D, 0.0D);
	}

	@Override
	public void attack(BlockState state, Level world, BlockPos pos, Player player) {
		ItemStack stack = player.getMainHandItem();
		if (!stack.is(Tags.Items.SHEARS)) {
			if (world.isEmptyBlock(pos.above()) && world.getEntitiesOfClass(PoiseClusterEntity.class, new AABB(pos).move(0, 1, 0)).isEmpty()) {
				if (!world.isClientSide) {
					PoiseClusterEntity cluster = new PoiseClusterEntity(world, pos, pos.getX(), pos.getY(), pos.getZ());
					cluster.setBlocksToMoveUp(10);
					world.addFreshEntity(cluster);

					RandomSource rand = player.getRandom();

					for (int i = 0; i < 8; i++) {
						double offsetX = GlowingPoiseStemBlock.makeNegativeRandomly(rand.nextFloat() * 0.25F, rand);
						double offsetZ = GlowingPoiseStemBlock.makeNegativeRandomly(rand.nextFloat() * 0.25F, rand);

						double x = pos.getX() + 0.5D + offsetX;
						double y = pos.getY() + 0.5D + (rand.nextFloat() * 0.05F);
						double z = pos.getZ() + 0.5D + offsetZ;

						NetworkUtil.spawnParticle("endergetic:short_poise_bubble", x, y, z, GlowingPoiseStemBlock.makeNegativeRandomly((rand.nextFloat() * 0.1F), rand) + 0.025F, (rand.nextFloat() * 0.15F) + 0.1F, GlowingPoiseStemBlock.makeNegativeRandomly((rand.nextFloat() * 0.1F), rand) + 0.025F);
					}
				}
				world.removeBlock(pos, false);
			}
		} else {
			world.destroyBlock(pos, false);
			stack.hurtAndBreak(1, player, (broken) -> broken.broadcastBreakEvent(player.getUsedItemHand()));
			popResource(world, pos, new ItemStack(this.asItem()));
		}
	}

	@Override
	public void onProjectileHit(Level world, BlockState state, BlockHitResult hit, Projectile projectile) {
		BlockPos pos = hit.getBlockPos();
		if (world.isEmptyBlock(pos.above()) && world.getEntitiesOfClass(PoiseClusterEntity.class, new AABB(pos.above())).isEmpty()) {
			if (!world.isClientSide) {
				PoiseClusterEntity cluster = new PoiseClusterEntity(world, pos, pos.getX(), pos.getY(), pos.getZ());
				cluster.setBlocksToMoveUp(10);
				world.addFreshEntity(cluster);

				if (projectile instanceof Arrow) {
					projectile.discard();
				}

				world.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
				world.playSound(null, pos, EESounds.CLUSTER_BREAK.get(), SoundSource.BLOCKS, 0.90F, 0.75F);

				RandomSource rand = cluster.getRandom();
				for (int i = 0; i < 8; i++) {
					double offsetX = GlowingPoiseStemBlock.makeNegativeRandomly(rand.nextFloat() * 0.25F, rand);
					double offsetZ = GlowingPoiseStemBlock.makeNegativeRandomly(rand.nextFloat() * 0.25F, rand);

					double x = pos.getX() + 0.5D + offsetX;
					double y = pos.getY() + 0.5D + (rand.nextFloat() * 0.05F);
					double z = pos.getZ() + 0.5D + offsetZ;

					NetworkUtil.spawnParticle("endergetic:short_poise_bubble", x, y, z, GlowingPoiseStemBlock.makeNegativeRandomly((rand.nextFloat() * 0.1F), rand) + 0.025F, (rand.nextFloat() * 0.15F) + 0.1F, GlowingPoiseStemBlock.makeNegativeRandomly((rand.nextFloat() * 0.1F), rand) + 0.025F);
				}
			}
		}
	}

	@SuppressWarnings("deprecation")
	@OnlyIn(Dist.CLIENT)
	@Override
	public boolean skipRendering(BlockState state, BlockState adjacentBlockState, Direction side) {
		return adjacentBlockState.getBlock() == this || super.skipRendering(state, adjacentBlockState, side);
	}

	@SuppressWarnings("deprecation")
	@Override
	public SoundType getSoundType(BlockState p_220072_1_) {
		return EESounds.EESoundTypes.CLUSTER;
	}

}