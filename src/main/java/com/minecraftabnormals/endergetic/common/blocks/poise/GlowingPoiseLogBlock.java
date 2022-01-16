package com.minecraftabnormals.endergetic.common.blocks.poise;

import com.minecraftabnormals.abnormals_core.core.util.MathUtil;
import com.minecraftabnormals.abnormals_core.core.util.item.ItemStackUtil;
import com.minecraftabnormals.endergetic.client.particles.EEParticles;
import net.minecraft.block.BlockState;
import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;
import java.util.function.Supplier;

public class GlowingPoiseLogBlock extends RotatedPillarBlock {
	private final Supplier<Item> fillAfterItem;

	public GlowingPoiseLogBlock(Properties properties, Supplier<Item> fillAfterItem) {
		super(properties);
		this.fillAfterItem = fillAfterItem;
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
		if (rand.nextFloat() > 0.05F) {
			BlockPos up = pos.above();
			if (worldIn.getBlockState(up).getCollisionShape(worldIn, up).isEmpty()) {
				double offsetX = MathUtil.makeNegativeRandomly(rand.nextFloat() * 0.25F, rand);
				double offsetZ = MathUtil.makeNegativeRandomly(rand.nextFloat() * 0.25F, rand);

				double x = pos.getX() + 0.5D + offsetX;
				double y = pos.getY() + 0.95D + (rand.nextFloat() * 0.05F);
				double z = pos.getZ() + 0.5D + offsetZ;

				worldIn.addParticle(EEParticles.SHORT_POISE_BUBBLE.get(), x, y, z, 0.0D, 0.0D, 0.0D);
			}
		}
	}

	//TODO: Convert to item group filling system in AC in 1.16.4
	@Override
	public void fillItemCategory(ItemGroup group, NonNullList<ItemStack> items) {
		ItemStackUtil.fillAfterItemForGroup(this.asItem(), this.fillAfterItem.get(), group, items);
	}

	@Override
	public int getFlammability(BlockState state, IBlockReader world, BlockPos pos, Direction face) {
		return 60;
	}
}