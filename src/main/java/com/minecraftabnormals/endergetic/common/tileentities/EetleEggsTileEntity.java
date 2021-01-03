package com.minecraftabnormals.endergetic.common.tileentities;

import com.minecraftabnormals.endergetic.core.registry.EETileEntities;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.Random;

public class EetleEggsTileEntity extends TileEntity implements ITickableTileEntity {
	private static final Random RANDOM = new Random();
	private final SackGrowth[] sackGrowths;

	public EetleEggsTileEntity() {
		super(EETileEntities.EETLE_EGGS.get());
		this.sackGrowths = new SackGrowth[] {
				new SackGrowth(),
				new SackGrowth(),
				new SackGrowth(),
				new SackGrowth()
		};
	}

	@Override
	public void tick() {
		World world = this.getWorld();
		if (world != null && world.isRemote) {
			for (SackGrowth growth : this.sackGrowths) {
				growth.tick();
			}
		}
	}

	@Override
	public double getMaxRenderDistanceSquared() {
		return 128.0D;
	}

	public SackGrowth[] getSackGrowths() {
		return this.sackGrowths;
	}

	public static class SackGrowth {
		private int cooldown;
		private float prevGrowth;
		private float growth;

		public SackGrowth() {
			this.cooldown += RANDOM.nextInt(21) + 5;
		}

		private void tick() {
			this.prevGrowth = this.growth;
			if (this.cooldown > 0) {
				this.cooldown--;
				this.growth = Math.max(0.0F, this.growth - 0.0075F);
			} else {
				this.growth += 0.0075;
				if (this.growth >= 0.15F) {
					this.cooldown += RANDOM.nextInt(36) + 25;
				}
			}
		}

		public float getGrowth(float partialTicks) {
			return 1.0F + MathHelper.lerp(partialTicks, this.prevGrowth, this.growth);
		}

		public float getGrowthMultiplied(float partialTicks, float multiplier) {
			return 1.0F + multiplier * MathHelper.lerp(partialTicks, this.prevGrowth, this.growth);
		}
	}
}
