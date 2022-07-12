package com.teamabnormals.endergetic.client.particles;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public class FastBlockParticle extends TextureSheetParticle {
	private final BlockState sourceState;
	private BlockPos sourcePos;
	private final float uCoord;
	private final float vCoord;

	@SuppressWarnings("deprecation")
	public FastBlockParticle(ClientLevel world, double x, double y, double z, double motionX, double motionY, double motionZ, BlockState state) {
		super(world, x, y, z, motionX, motionY, motionZ);
		this.xd = motionX + (Math.random() * 2.0D - 1.0D) * 0.4F;
		this.yd = Math.random() * 0.1F;
		this.zd = motionZ + (Math.random() * 2.0D - 1.0D) * 0.4F;
		this.sourceState = state;
		this.setSprite(Minecraft.getInstance().getBlockRenderer().getBlockModelShaper().getParticleIcon(state));
		this.gravity = 1.0F;
		this.rCol = this.gCol = this.bCol = 0.6F;
		this.quadSize /= 2.0F;
		this.uCoord = this.random.nextFloat() * 3.0F;
		this.vCoord = this.random.nextFloat() * 3.0F;
	}

	protected void multiplyColor(@Nullable BlockPos pos) {
		int i = Minecraft.getInstance().getBlockColors().getColor(this.sourceState, this.level, pos, 0);
		this.rCol *= (float)(i >> 16 & 255) / 255.0F;
		this.gCol *= (float)(i >> 8 & 255) / 255.0F;
		this.bCol *= (float)(i & 255) / 255.0F;
	}

	public FastBlockParticle init() {
		this.sourcePos = new BlockPos(this.x, this.y, this.z);
		if (!this.sourceState.is(Blocks.GRASS_BLOCK)) {
			this.multiplyColor(this.sourcePos);
		}
		return this;
	}

	private Particle updateSprite(BlockPos pos) {
		if (pos != null) {
			this.setSprite(Minecraft.getInstance().getBlockRenderer().getBlockModelShaper().getTexture(this.sourceState, this.level, pos));
		}
		return this;
	}

	@Override
	public void tick() {
		this.xo = this.x;
		this.yo = this.y;
		this.zo = this.z;
		if (this.age++ >= this.lifetime) {
			this.remove();
		} else {
			this.yd -= 0.04D * this.gravity;
			this.move(this.xd, this.yd, this.zd);
			this.xd *= 0.98F;
			this.yd *= 0.98F;
			this.zd *= 0.98F;
			if (this.onGround) {
				this.xd *= 0.95F;
				this.zd *= 0.95F;
			}
		}
	}

	@Override
	public ParticleRenderType getRenderType() {
		return ParticleRenderType.TERRAIN_SHEET;
	}

	@Override
	protected float getU0() {
		return this.sprite.getU((this.uCoord + 1.0F) / 4.0F * 16.0F);
	}

	@Override
	protected float getU1() {
		return this.sprite.getU(this.uCoord / 4.0F * 16.0F);
	}

	@Override
	protected float getV0() {
		return this.sprite.getV(this.vCoord / 4.0F * 16.0F);
	}

	@Override
	protected float getV1() {
		return this.sprite.getV((this.vCoord + 1.0F) / 4.0F * 16.0F);
	}

	@SuppressWarnings("deprecation")
	@Override
	public int getLightColor(float partialTick) {
		int brightnessForRender = super.getLightColor(partialTick);
		int light = 0;
		if (this.level.hasChunkAt(this.sourcePos)) {
			light = LevelRenderer.getLightColor(this.level, this.sourcePos);
		}
		return brightnessForRender == 0 ? light : brightnessForRender;
	}

	public static class Factory implements ParticleProvider<BlockParticleOption> {
		@Override
		public Particle createParticle(BlockParticleOption data, ClientLevel world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
			BlockState blockstate = data.getState();
			return !blockstate.isAir() && !blockstate.is(Blocks.MOVING_PISTON) ? new FastBlockParticle(world, x, y, z, xSpeed, ySpeed, zSpeed, blockstate).init().updateSprite(data.getPos()) : null;
		}
	}
}
