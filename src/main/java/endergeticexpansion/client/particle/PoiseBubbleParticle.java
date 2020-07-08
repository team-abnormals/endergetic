package endergeticexpansion.client.particle;

import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.SpriteTexturedParticle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class PoiseBubbleParticle extends SpriteTexturedParticle {
	protected final IAnimatedSprite animatedSprite;
	private float angle;
	
	public PoiseBubbleParticle(IAnimatedSprite animatedSprite, ClientWorld world, double posX, double posY, double posZ, double motionX, double motionY, double motionZ, boolean isShort) {
		super(world, posX, posY, posZ, motionX, motionY, motionZ);
		this.motionX = motionX;
		this.motionY = isShort ? motionY + (rand.nextDouble() * 0.05D + 0.055F) : motionY + (rand.nextDouble() * 0.05D + 0.075F);
		this.motionZ = motionZ;
		this.angle = (float) Math.random() * ((float) Math.PI * 2F);
		this.animatedSprite = animatedSprite;
		this.maxAge = isShort ? rand.nextInt(30) + 30 : rand.nextInt(50) + 50;
		this.particleScale = isShort ? 0.1F * (this.rand.nextFloat() * 0.5F + 0.5F) * 2.0F : 0.2F * (this.rand.nextFloat() * 0.5F + 0.5F) * 2.0F;
		this.selectSpriteWithAge(animatedSprite);
	}
	
	@Override
	public void tick() {
		if(this.age % 5 == 0) {
			this.angle = (float) Math.random() * ((float) Math.PI * 2F);
		}
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		if(this.age++ >= this.maxAge) {
			this.setExpired();
		} else {
			this.motionY -= 0.04D * this.particleGravity;
			this.move(this.motionX, this.motionY, this.motionZ);
			this.motionX += Math.cos(this.angle) * 0.0005;
	        this.motionZ += Math.sin(this.angle) * 0.0005;
			this.motionY *= 0.98D;
		}
		this.particleAlpha -= (this.maxAge / 10000F);
		this.selectSpriteWithAge(this.animatedSprite);
	}
	
	@Override
	public int getBrightnessForRender(float partialTick) {
		float f = this.maxAge / (((this.age + (this.maxAge * 0.5F)) + partialTick));
		f = MathHelper.clamp(f, 0F, 0.5F);
		int i = super.getBrightnessForRender(partialTick);
		int j = i & 255;
		int k = i >> 16 & 255;
		j += (int) (f * 15f * 16f);
		if(j > 240) {
			j = 240;
		}
		return j | k << 16;
	}

	@Override
	public IParticleRenderType getRenderType() {
		return IParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
	}
	
	public static class Factory implements IParticleFactory<BasicParticleType> {
		private IAnimatedSprite animatedSprite;

		public Factory(IAnimatedSprite animatedSprite) {
			this.animatedSprite = animatedSprite;
		}

		@Override
		public Particle makeParticle(BasicParticleType type, ClientWorld world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
			return new PoiseBubbleParticle(this.animatedSprite, world, x, y, z, xSpeed, ySpeed, zSpeed, false);
		}
	}
	
	public static class ShortFactory implements IParticleFactory<BasicParticleType> {
		private IAnimatedSprite animatedSprite;

		public ShortFactory(IAnimatedSprite animatedSprite) {
			this.animatedSprite = animatedSprite;
		}

		@Override
		public Particle makeParticle(BasicParticleType type, ClientWorld world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
			return new PoiseBubbleParticle(this.animatedSprite, world, x, y, z, xSpeed, ySpeed, zSpeed, true);
		}
	}
}