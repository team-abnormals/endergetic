package com.minecraftabnormals.endergetic.client.particles;

import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CorrockCrownParticle extends SpriteTexturedParticle {
	private final IAnimatedSprite animatedSprite;
	private final float rotSpeed;

	public CorrockCrownParticle(IAnimatedSprite animatedSprite, ClientWorld world, double x, double y, double z, double motionX, double motionY, double motionZ) {
		super(world, x, y, z);
		float size = (float) (0.3D + Math.random() * 0.4D);
		this.setSize(size, size);
		this.motionX = motionX + motionX * ((float)Math.random() - 0.5F) * 0.2F;
		this.motionY = motionY;
		this.motionZ = motionZ + motionZ * ((float)Math.random() - 0.5F) * 0.2F;
		this.animatedSprite = animatedSprite;
		this.particleGravity = (float) Math.random() * 0.08F;
		this.particleAngle = (float) Math.random() * ((float)Math.PI * 2.0F);
		this.maxAge = (int) (Math.random() * 20 + 40);
		this.rotSpeed = ((float)Math.random() - 0.5F) * 0.075F;
		this.selectSpriteWithAge(animatedSprite);
	}

	@Override
	public void tick() {
		super.tick();
		this.prevParticleAngle = this.particleAngle;
		this.particleAngle += (float)Math.PI * this.rotSpeed * 2.0F;

		if (this.onGround) {
			this.prevParticleAngle = this.particleAngle = 0.0F;
		}

		float particleAngle = this.particleAngle;
		this.motionX += Math.cos(particleAngle) * 0.00175D;
		this.motionY *= 1.025D;
		this.motionZ += Math.sin(particleAngle) * 0.00175D;

		if (this.isAlive()) {
			this.selectSpriteWithAge(this.animatedSprite);
		}
	}

	@Override
	public int getBrightnessForRender(float partialTick) {
		float ageFactor = MathHelper.clamp(this.maxAge / (((this.age + (this.maxAge * 0.5F)) + partialTick)), 0.0F, 0.5F);
		int brightnessForRender = super.getBrightnessForRender(partialTick);
		int j = brightnessForRender & 255;
		int k = brightnessForRender >> 16 & 255;
		j += (int) (ageFactor * 240.0F);
		if (j > 240) {
			j = 240;
		}
		return j | k << 16;
	}

	@Override
	public IParticleRenderType getRenderType() {
		return IParticleRenderType.PARTICLE_SHEET_OPAQUE;
	}

	public static class Factory implements IParticleFactory<BasicParticleType> {
		private IAnimatedSprite animatedSprite;

		public Factory(IAnimatedSprite animatedSprite) {
			this.animatedSprite = animatedSprite;
		}

		@Override
		public Particle makeParticle(BasicParticleType type, ClientWorld world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
			return new CorrockCrownParticle(this.animatedSprite, world, x, y, z, xSpeed, ySpeed, zSpeed);
		}
	}
}
