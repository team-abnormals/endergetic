package com.minecraftabnormals.endergetic.client.particles;

import com.minecraftabnormals.endergetic.client.particles.data.CorrockCrownParticleData;
import net.minecraft.client.particle.*;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Optional;

@OnlyIn(Dist.CLIENT)
public class CorrockCrownParticle extends TextureSheetParticle {
	private final SpriteSet animatedSprite;
	private final float rotSpeed;

	public CorrockCrownParticle(SpriteSet animatedSprite, ClientLevel world, double x, double y, double z, double motionX, double motionY, double motionZ, boolean eetle, Optional<Float> scale) {
		super(world, x, y, z);
		scale.ifPresent(value -> this.quadSize = value + value * 0.1F * (this.random.nextFloat() * 0.5F + 0.5F));
		float size = (float) ((eetle ? 0.5D : 0.3D) + Math.random() * 0.4D);
		this.setSize(size, size);
		this.xd = motionX + motionX * ((float)Math.random() - 0.5F) * 0.2F;
		this.yd = motionY + (eetle ? 0.05F : 0.0F);
		this.zd = motionZ + motionZ * ((float)Math.random() - 0.5F) * 0.2F;
		this.animatedSprite = animatedSprite;
		this.gravity = eetle ? 0.8F : (float) Math.random() * 0.08F;
		this.roll = (float) Math.random() * ((float)Math.PI * 2.0F);
		this.lifetime = (int) (Math.random() * 20 + (eetle ? 20 : 40));
		this.rotSpeed = ((float)Math.random() - 0.5F) * (eetle ? 0.1F : 0.075F);
		this.setSpriteFromAge(animatedSprite);
	}

	@Override
	public void tick() {
		super.tick();
		this.oRoll = this.roll;
		this.roll += (float)Math.PI * this.rotSpeed * 2.0F;

		if (this.onGround) {
			this.oRoll = this.roll = 0.0F;
		}

		float particleAngle = this.roll;
		this.xd += Math.cos(particleAngle) * 0.00175D;
		this.yd *= 1.025D;
		this.zd += Math.sin(particleAngle) * 0.00175D;

		if (this.isAlive()) {
			this.setSpriteFromAge(this.animatedSprite);
		}
	}

	@Override
	public int getLightColor(float partialTick) {
		float ageFactor = Mth.clamp(this.lifetime / (((this.age + (this.lifetime * 0.5F)) + partialTick)), 0.0F, 1.0F);
		int brightnessForRender = super.getLightColor(partialTick);
		int j = brightnessForRender & 255;
		int k = brightnessForRender >> 16 & 255;
		j += (int) (ageFactor * 240.0F);
		if (j > 240) {
			j = 240;
		}
		return j | k << 16;
	}

	@Override
	public ParticleRenderType getRenderType() {
		return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
	}

	public static class Factory implements ParticleProvider<CorrockCrownParticleData> {
		private SpriteSet animatedSprite;

		public Factory(SpriteSet animatedSprite) {
			this.animatedSprite = animatedSprite;
		}

		@Override
		public Particle createParticle(CorrockCrownParticleData data, ClientLevel world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
			return new CorrockCrownParticle(this.animatedSprite, world, x, y, z, xSpeed, ySpeed, zSpeed, data.isEetle(), data.getScale());
		}
	}
}
