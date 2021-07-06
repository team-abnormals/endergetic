package com.minecraftabnormals.endergetic.client.particles;

import com.minecraftabnormals.endergetic.client.particles.data.CorrockCrownParticleData;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Optional;

@OnlyIn(Dist.CLIENT)
public class CorrockCrownParticle extends SpriteTexturedParticle {
	private final IAnimatedSprite animatedSprite;
	private final float rotSpeed;

	public CorrockCrownParticle(IAnimatedSprite animatedSprite, ClientWorld world, double x, double y, double z, double motionX, double motionY, double motionZ, boolean eetle, Optional<Float> scale) {
		super(world, x, y, z);
		scale.ifPresent(value -> this.particleScale = value + value * 0.1F * (this.rand.nextFloat() * 0.5F + 0.5F));
		float size = (float) ((eetle ? 0.5D : 0.3D) + Math.random() * 0.4D);
		this.setSize(size, size);
		this.motionX = motionX + motionX * ((float)Math.random() - 0.5F) * 0.2F;
		this.motionY = motionY + (eetle ? 0.05F : 0.0F);
		this.motionZ = motionZ + motionZ * ((float)Math.random() - 0.5F) * 0.2F;
		this.animatedSprite = animatedSprite;
		this.particleGravity = eetle ? 0.8F : (float) Math.random() * 0.08F;
		this.particleAngle = (float) Math.random() * ((float)Math.PI * 2.0F);
		this.maxAge = (int) (Math.random() * 20 + (eetle ? 20 : 40));
		this.rotSpeed = ((float)Math.random() - 0.5F) * (eetle ? 0.1F : 0.075F);
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
		float ageFactor = MathHelper.clamp(this.maxAge / (((this.age + (this.maxAge * 0.5F)) + partialTick)), 0.0F, 1.0F);
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

	public static class Factory implements IParticleFactory<CorrockCrownParticleData> {
		private IAnimatedSprite animatedSprite;

		public Factory(IAnimatedSprite animatedSprite) {
			this.animatedSprite = animatedSprite;
		}

		@Override
		public Particle makeParticle(CorrockCrownParticleData data, ClientWorld world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
			return new CorrockCrownParticle(this.animatedSprite, world, x, y, z, xSpeed, ySpeed, zSpeed, data.isEetle(), data.getScale());
		}
	}
}
