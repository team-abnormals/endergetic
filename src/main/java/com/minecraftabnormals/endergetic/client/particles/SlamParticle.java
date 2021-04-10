package com.minecraftabnormals.endergetic.client.particles;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public class SlamParticle extends SpriteTexturedParticle {
	private final BlockState sourceState;
	private BlockPos sourcePos;
	private final float uCoord;
	private final float vCoord;

	@SuppressWarnings("deprecation")
	public SlamParticle(ClientWorld world, double x, double y, double z, double motionX, double motionY, double motionZ, BlockState state) {
		super(world, x, y, z, motionX, motionY, motionZ);
		this.motionX = motionX + (Math.random() * 2.0D - 1.0D) * 0.4F;
		this.motionY = Math.random() * 0.1F;
		this.motionZ = motionZ + (Math.random() * 2.0D - 1.0D) * 0.4F;
		this.sourceState = state;
		this.setSprite(Minecraft.getInstance().getBlockRendererDispatcher().getBlockModelShapes().getTexture(state));
		this.particleGravity = 1.0F;
		this.particleRed = this.particleGreen = this.particleBlue = 0.6F;
		this.particleScale /= 2.0F;
		this.uCoord = this.rand.nextFloat() * 3.0F;
		this.vCoord = this.rand.nextFloat() * 3.0F;
	}

	protected void multiplyColor(@Nullable BlockPos pos) {
		int i = Minecraft.getInstance().getBlockColors().getColor(this.sourceState, this.world, pos, 0);
		this.particleRed *= (float)(i >> 16 & 255) / 255.0F;
		this.particleGreen *= (float)(i >> 8 & 255) / 255.0F;
		this.particleBlue *= (float)(i & 255) / 255.0F;
	}

	public SlamParticle init() {
		this.sourcePos = new BlockPos(this.posX, this.posY, this.posZ);
		if (!this.sourceState.isIn(Blocks.GRASS_BLOCK)) {
			this.multiplyColor(this.sourcePos);
		}
		return this;
	}

	private Particle updateSprite(BlockPos pos) {
		if (pos != null) {
			this.setSprite(Minecraft.getInstance().getBlockRendererDispatcher().getBlockModelShapes().getTexture(this.sourceState, this.world, pos));
		}
		return this;
	}

	@Override
	public void tick() {
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		if (this.age++ >= this.maxAge) {
			this.setExpired();
		} else {
			this.motionY -= 0.04D * this.particleGravity;
			this.move(this.motionX, this.motionY, this.motionZ);
			this.motionX *= 0.98F;
			this.motionY *= 0.98F;
			this.motionZ *= 0.98F;
			if (this.onGround) {
				this.motionX *= 0.95F;
				this.motionZ *= 0.95F;
			}
		}
	}

	@Override
	public IParticleRenderType getRenderType() {
		return IParticleRenderType.TERRAIN_SHEET;
	}

	@Override
	protected float getMinU() {
		return this.sprite.getInterpolatedU((this.uCoord + 1.0F) / 4.0F * 16.0F);
	}

	@Override
	protected float getMaxU() {
		return this.sprite.getInterpolatedU(this.uCoord / 4.0F * 16.0F);
	}

	@Override
	protected float getMinV() {
		return this.sprite.getInterpolatedV(this.vCoord / 4.0F * 16.0F);
	}

	@Override
	protected float getMaxV() {
		return this.sprite.getInterpolatedV((this.vCoord + 1.0F) / 4.0F * 16.0F);
	}

	@SuppressWarnings("deprecation")
	@Override
	public int getBrightnessForRender(float partialTick) {
		int brightnessForRender = super.getBrightnessForRender(partialTick);
		int light = 0;
		if (this.world.isBlockLoaded(this.sourcePos)) {
			light = WorldRenderer.getCombinedLight(this.world, this.sourcePos);
		}
		return brightnessForRender == 0 ? light : brightnessForRender;
	}

	@OnlyIn(Dist.CLIENT)
	public static class Factory implements IParticleFactory<BlockParticleData> {
		@SuppressWarnings("deprecation")
		@Override
		public Particle makeParticle(BlockParticleData data, ClientWorld world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
			BlockState blockstate = data.getBlockState();
			return !blockstate.isAir() && !blockstate.isIn(Blocks.MOVING_PISTON) ? new SlamParticle(world, x, y, z, xSpeed, ySpeed, zSpeed, blockstate).init().updateSprite(data.getPos()) : null;
		}
	}
}
