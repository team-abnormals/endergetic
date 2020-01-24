package endergeticexpansion.common.entities.bolloom;
import javax.annotation.Nullable;

import endergeticexpansion.common.blocks.poise.BlockBolloomBud;
import endergeticexpansion.core.registry.EEBlocks;
import endergeticexpansion.core.registry.EEEntities;
import endergeticexpansion.core.registry.EEItems;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.Direction.Axis;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraft.world.World;

public class EntityBolloomFruit extends Entity {
	private static final DataParameter<BlockPos> BUD_POS = EntityDataManager.createKey(EntityBolloomFruit.class, DataSerializers.BLOCK_POS);
	private static final DataParameter<Float> ORIGINAL_X = EntityDataManager.createKey(EntityBolloomFruit.class, DataSerializers.FLOAT);
	private static final DataParameter<Float> ORIGINAL_Z = EntityDataManager.createKey(EntityBolloomFruit.class, DataSerializers.FLOAT);
	private static final DataParameter<Float> ORIGINAL_Y = EntityDataManager.createKey(EntityBolloomFruit.class, DataSerializers.FLOAT);
	private static final DataParameter<Float> ANGLE = EntityDataManager.createKey(EntityBolloomFruit.class, DataSerializers.FLOAT);
	private static final DataParameter<Float> DESIRED_ANGLE = EntityDataManager.createKey(EntityBolloomFruit.class, DataSerializers.FLOAT);
	private static final DataParameter<Float> SWAY = EntityDataManager.createKey(EntityBolloomFruit.class, DataSerializers.FLOAT);
	private static final DataParameter<Integer> VINE_HEIGHT = EntityDataManager.createKey(EntityBolloomFruit.class, DataSerializers.VARINT);
	private static final DataParameter<Integer> TICKS_EXISTED = EntityDataManager.createKey(EntityBolloomFruit.class, DataSerializers.VARINT); //Vanilla's ticksExisted isn't synced between server and client
	private static final DataParameter<Boolean> UNTIED = EntityDataManager.createKey(EntityBolloomFruit.class, DataSerializers.BOOLEAN);
	public float prevVineAngle;
	public float prevAngle;
	
	public EntityBolloomFruit(EntityType<? extends EntityBolloomFruit> type, World world) {
		super(EEEntities.BOLLOOM_FRUIT.get(), world);
		this.setNoGravity(true);
	}
	
	public EntityBolloomFruit(FMLPlayMessages.SpawnEntity spawnEntity, World world) {
		this(EEEntities.BOLLOOM_FRUIT.get(), world);
	}
	
	public EntityBolloomFruit(World world, BlockPos budPos, BlockPos origin, int height, Direction direction) {
		this(EEEntities.BOLLOOM_FRUIT.get(), world);
		
		float xPos = origin.getX() + 0.5F + (direction.getAxis() == Axis.Z ? 0.0F : -0.2F * direction.getAxisDirection().getOffset());
		float zPos = origin.getZ() + 0.5F + (direction.getAxis() == Axis.X ? 0.0F : -0.2F * direction.getAxisDirection().getOffset());
		float yPos = origin.getY() + 1.15F;
		
		this.setPosition(xPos, yPos, zPos);
		this.setOriginalPositions(xPos, yPos, zPos);
		this.setOrigin(budPos);
		this.setVineHeight(height);
		
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
	}

	@Override
	public void writeAdditional(CompoundNBT nbt) {
		nbt.putLong("BudPosition", this.getDataManager().get(BUD_POS).toLong());
		nbt.putBoolean("Untied", this.isUntied());
		nbt.putFloat("OriginalPosX", this.getOriginalPos()[0]);
		nbt.putFloat("OriginalPosY", this.getOriginalPos()[1]);
		nbt.putFloat("OriginalPosZ", this.getOriginalPos()[2]);
		nbt.putInt("VineHeight", this.getVineHeight());
	}

	@Override
	public void readAdditional(CompoundNBT nbt) {
		this.getDataManager().set(BUD_POS, BlockPos.fromLong(nbt.getLong("BudPosition")));
		this.setUntied(nbt.getBoolean("Untied"));
		this.setOriginalPositions(nbt.getFloat("OriginalPosX"), nbt.getFloat("OriginalPosY"), nbt.getFloat("OriginalPosZ"));
		this.setVineHeight(nbt.getInt("VineHeight"));
	}
	
	@Override
	protected void registerData() {
		this.getDataManager().register(ORIGINAL_X, 0F);
		this.getDataManager().register(ORIGINAL_Z, 0F);
		this.getDataManager().register(ORIGINAL_Y, 0F);
		this.getDataManager().register(ANGLE, 0F);
		this.getDataManager().register(SWAY, 0F);
		this.getDataManager().register(DESIRED_ANGLE, 0F);
		this.getDataManager().register(VINE_HEIGHT, 0);
		this.getDataManager().register(BUD_POS, BlockPos.ZERO);
		this.getDataManager().register(UNTIED, false);
		this.getDataManager().register(TICKS_EXISTED, 0);
	}
	
	@Override
	public void tick() {
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		
		this.prevVineAngle = this.getVineAngle();
		this.prevAngle = this.getAngle();
		
		if(this.world.isAreaLoaded(this.getOrigin(), 1)) {
			this.setSway((float) Math.sin((2 * Math.PI / 100 * getTicksExisted())) * 0.5F);
		}
		
		if(this.world.isAreaLoaded(this.getOrigin(), 1)) {
			if(!this.isUntied()) {
				this.setPosition(
					this.getOriginalPos()[0] + this.getSway() * Math.sin(-this.getAngle()),
					this.getOriginalY(),
					this.getOriginalPos()[2] + this.getSway() * Math.cos(-this.getAngle())
				);
			} else {
				this.move(MoverType.SELF, this.getMotion());
				this.setMotion(Math.sin(this.getAngle()) * Math.cos(this.getAngle()) * 0.05F, Math.toRadians(4), Math.cos(this.getVineAngle()) * Math.cos(-this.getAngle()) * 0.05F);
			}
		}
		
		if(!this.world.isRemote) {
			if(this.getTicksExisted() % 45 == 0) {
				this.setDesiredAngle((float) (this.rand.nextDouble() * 2 * Math.PI));
			}
			
			if(this.posY >= this.world.getDimension().getSeaLevel() * 2 && this.rand.nextFloat() <= 0.10F && this.isUntied()) {
				this.remove();
			}
			
			float dangle = this.getDesiredAngle() - this.getAngle();
			while(dangle > Math.PI) {
			    dangle -= 2 * Math.PI;
			}
			while(dangle <= -Math.PI) {
			    dangle += 2 * Math.PI;
			}
			if(Math.abs(dangle) <= 0.1F) {
			    this.setAngle(this.getAngle() + dangle);
			} else if(dangle > 0) {
			    this.setAngle(this.getAngle() + 0.03F);
			} else {
			    this.setAngle(this.getAngle() - 0.03F);
			}
		}
		
		if(this.world.isAreaLoaded(this.getOrigin(), 1)) {
			if(this.getEntityWorld().getBlockState(this.getOrigin()).getBlock() != EEBlocks.BOLLOOM_BUD || !this.getEntityWorld().getBlockState(this.getOrigin()).get(BlockBolloomBud.OPENED)) {
				this.setUntied(true);
			}
		}
		
		if(!this.isOpenPathBelowFruit()) {
			this.setUntied(true);
		}
		
		this.extinguish();
		this.incrementTicksExisted();
	}
	
	@SuppressWarnings("deprecation")
	public boolean isOpenPathBelowFruit() {
		for (int i = 0; i < this.getVineHeight(); i++) {
			BlockPos pos = this.getPosition().down(i);
			if(this.getEntityWorld().isAreaLoaded(pos, 1)) {
				if(!this.getEntityWorld().getBlockState(pos).isAir()) {
					return false;
				}
			}
		}
		return true;
	}
	
	private void doParticles() {
		if(this.world instanceof ServerWorld) {
			((ServerWorld)this.world).spawnParticle(new BlockParticleData(ParticleTypes.BLOCK, EEBlocks.BOLLOOM_PARTICLE.getDefaultState()), this.posX, this.posY + (double)this.getHeight() / 1.5D, this.posZ, 10, (double)(this.getWidth() / 4.0F), (double)(this.getHeight() / 4.0F), (double)(this.getWidth() / 4.0F), 0.05D);
		}
	}
	
	public float getOriginalY() {
		return this.getDataManager().get(ORIGINAL_Y);
	}
	
	public void setOriginalPositions(float x, float y, float z) {
		this.dataManager.set(ORIGINAL_X, x);
		this.dataManager.set(ORIGINAL_Y, y);
		this.dataManager.set(ORIGINAL_Z, z);
	}
	
	public int getTicksExisted() {
		return this.getDataManager().get(TICKS_EXISTED);
	}
	
	public void setTicksExisted(int ticks) {
		this.getDataManager().set(TICKS_EXISTED, ticks);
	}

	public void incrementTicksExisted() {
		this.getDataManager().set(TICKS_EXISTED, this.getTicksExisted() + 1);
	}

	public float getVineAngle() {
		return (float) Math.atan(this.getSway() / (this.getVineHeight()));
	}
	
	public void setVineHeight(int height) {
		this.getDataManager().set(VINE_HEIGHT, height);
	}
	
	public int getVineHeight() {
		return this.getDataManager().get(VINE_HEIGHT);
	}
	
	public void setSway(float degree) {
		this.getDataManager().set(SWAY, degree);
	}
	
	public float getSway() {
		return this.getDataManager().get(SWAY);
	}
	
	public void setAngle(float degree) {
		this.getDataManager().set(ANGLE, degree);
	}
	
	public float getAngle() {
		return this.getDataManager().get(ANGLE);
	}
	
	public void setDesiredAngle(float angle) {
		this.getDataManager().set(DESIRED_ANGLE, angle);
	}
	
	public float getDesiredAngle() {
		return this.getDataManager().get(DESIRED_ANGLE);
	}
	
	public BlockPos getOrigin() {
		return this.getDataManager().get(BUD_POS);
	}
	
	public void setOrigin(BlockPos budPos) {
		this.getDataManager().set(BUD_POS, budPos);
	}
	
	public boolean isUntied() {
		return this.getDataManager().get(UNTIED);
	}
	
	public float[] getOriginalPos() {
		return new float[] { this.getDataManager().get(ORIGINAL_X), this.getDataManager().get(ORIGINAL_Y), this.getDataManager().get(ORIGINAL_Z) };
	}
	
	public void setUntied(boolean untied) {
		this.dataManager.set(UNTIED, untied);
	}
	
	@OnlyIn(Dist.CLIENT)
	public float[] getVineAnimation(float partialTicks) {
		return new float[] {
			MathHelper.lerp(partialTicks, this.prevVineAngle, this.getVineAngle()),
			MathHelper.lerp(partialTicks, this.prevAngle, this.getAngle()),
		};
	}
	
	public void onBroken(@Nullable Entity brokenEntity, boolean dropFruit) {
		if(dropFruit) {
			Block.spawnAsEntity(this.world, this.getPosition(), new ItemStack(EEItems.BOLLOOM_FRUIT.get()));
		}
		this.playSound(SoundEvents.BLOCK_WET_GRASS_BREAK, 1.0F, 1.0F);
		this.doParticles();
	}
	
	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		if (this.isInvulnerableTo(source)) {
			return false;
		} else {
			if (this.isAlive() && !this.world.isRemote) {
				this.remove();
				this.markVelocityChanged();
				this.onBroken(source.getTrueSource(), true);
			}
			return true;
		}
	}
	
	@Override
	public boolean isInvulnerableTo(DamageSource source) {
		return this.isInvulnerable() && source != DamageSource.OUT_OF_WORLD && source != DamageSource.CRAMMING;
	}
	
	@Override
	public boolean hitByEntity(Entity entityIn) {
		return entityIn instanceof PlayerEntity ? this.attackEntityFrom(DamageSource.causePlayerDamage((PlayerEntity) entityIn), 0.0F) : false;
	}
	
	@Override
	public void applyEntityCollision(Entity entityIn) {
		if (entityIn instanceof EntityBolloomFruit) {
			if (entityIn.getBoundingBox().minY < this.getBoundingBox().maxY) {
				super.applyEntityCollision(entityIn);
			}
		} else if (entityIn.posY >= this.getBoundingBox().minY) {
			super.applyEntityCollision(entityIn);
		}
	}
	
	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		return super.getRenderBoundingBox().grow(5);
	}
	
	@Override
	public boolean isInRangeToRenderDist(double distance) {
		return true;
	}
	
	@Override
	public boolean isInRangeToRender3d(double x, double y, double z) {
		return true;
	}
	
	@Override
	public boolean canBePushed() {
		return this.isAlive();
	}
	
	@Override
	public boolean canBeCollidedWith() {
		return true;
	}
	
	@Nullable
	public AxisAlignedBB getCollisionBoundingBox() {
		return this.getBoundingBox();
	}
	
	@Nullable
	public AxisAlignedBB getCollisionBox(Entity entityIn) {
		return entityIn.canBePushed() ? entityIn.getBoundingBox() : null;
	}
	
	@Override
	public boolean canRenderOnFire() {
		return false;
	}
	
	@Override
	public IPacket<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}