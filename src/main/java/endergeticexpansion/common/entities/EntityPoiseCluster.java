package endergeticexpansion.common.entities;

import java.util.List;

import javax.annotation.Nullable;

import endergeticexpansion.api.util.MathUtils;
import endergeticexpansion.api.util.NetworkUtil;
import endergeticexpansion.core.registry.EEBlocks;
import endergeticexpansion.core.registry.EEEntities;
import endergeticexpansion.core.registry.EESounds;
import net.minecraft.block.Block;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.HandSide;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class EntityPoiseCluster extends LivingEntity {
	private static final DataParameter<BlockPos> ORIGIN = EntityDataManager.createKey(EntityPoiseCluster.class, DataSerializers.BLOCK_POS);
	private static final DataParameter<Integer> BLOCKS_TO_MOVE_UP = EntityDataManager.createKey(EntityPoiseCluster.class, DataSerializers.VARINT);
	private static final DataParameter<Integer> TIMES_HIT = EntityDataManager.createKey(EntityPoiseCluster.class, DataSerializers.VARINT);
	private static final DataParameter<Boolean> ASCEND = EntityDataManager.createKey(EntityPoiseCluster.class, DataSerializers.BOOLEAN);
	
	public EntityPoiseCluster(EntityType<? extends EntityPoiseCluster> cluster, World worldIn) {
		super(EEEntities.POISE_CLUSTER.get(), worldIn);
	}
	
	/*
	 * Constructor used for spawning the entity
	 */
	public EntityPoiseCluster(World worldIn, BlockPos origin, double x, double y, double z) {
		this(EEEntities.POISE_CLUSTER.get(), worldIn);
		this.setHealth(100);
		this.setOrigin(new BlockPos(origin));
		// Subtracts 0.5 on x and z due to block positions being whole
		this.setPosition(x + 0.5D, y, z + 0.5D);
		this.setNoGravity(true);
		this.prevRenderYawOffset = 180.0F;
		this.renderYawOffset = 180.0F;
		this.rotationYaw = 180.0F;
	}
	
	@Override
	protected void registerData() {
		super.registerData();
		this.dataManager.register(ORIGIN, BlockPos.ZERO);
		this.dataManager.register(BLOCKS_TO_MOVE_UP, 0);
		this.dataManager.register(TIMES_HIT, 0);
		this.dataManager.register(ASCEND, true);
	}
	
	@Override
	public void writeAdditional(CompoundNBT nbt) {
		super.writeAdditional(nbt);
		nbt.putLong("ORIGIN", this.getDataManager().get(ORIGIN).toLong());
		nbt.putInt("BLOCKS_TO_MOVE_UP", this.getDataManager().get(BLOCKS_TO_MOVE_UP));
		nbt.putInt("TIMES_HIT", this.getDataManager().get(TIMES_HIT));
		nbt.putBoolean("ASCEND", this.getDataManager().get(ASCEND));
	}
	
	@Override
	public void readAdditional(CompoundNBT nbt) {
		super.readAdditional(nbt);
		this.getDataManager().set(ORIGIN, BlockPos.fromLong(nbt.getLong("ORIGIN")));
		this.getDataManager().set(BLOCKS_TO_MOVE_UP, nbt.getInt("BLOCKS_TO_MOVE_UP"));
		this.getDataManager().set(TIMES_HIT, nbt.getInt("TIMES_HIT"));
		this.getDataManager().set(ASCEND, nbt.getBoolean("ASCEND"));
	}
	
	/*
	 * Used to make the entity's motion shoot up/down on the y-axis to reach it's position
	 */
	@Override
	@SuppressWarnings("deprecation")
	public void tick() {
		if(Math.ceil(this.posY) < (this.getOrigin().getY() + this.getBlocksToMoveUp()) && this.dataManager.get(ASCEND)) {
			this.setMotion(0, Math.toRadians(3), 0);
		}
		
		if(Math.ceil(this.posY) >= this.getOrigin().getY() + this.getBlocksToMoveUp()) {
			this.dataManager.set(ASCEND, false);
			this.setBlocksToMoveUp(0);
		}
		
		if(!this.isAscending()) {
			if(this.posY > this.getOrigin().getY()) {
				this.setMotion(0, -Math.toRadians(3), 0);
			} else if(Math.ceil(this.posY) == this.getOrigin().getY()) {
				for(int i = 0; i < 8; i++) {
					double offsetX = MathUtils.makeNegativeRandomly(rand.nextFloat() * 0.25F, rand);
					double offsetZ = MathUtils.makeNegativeRandomly(rand.nextFloat() * 0.25F, rand);
				
					double x = this.getOrigin().getX() + 0.5D + offsetX;
					double y = this.getOrigin().getY() + 0.5D + (rand.nextFloat() * 0.05F);
					double z = this.getOrigin().getZ() + 0.5D + offsetZ;
				
					if(this.isServerWorld()) {
						NetworkUtil.spawnParticle("endergetic:short_poise_bubble", x, y, z, MathUtils.makeNegativeRandomly((rand.nextFloat() * 0.1F), rand) + 0.025F, (rand.nextFloat() * 0.15F) + 0.1F, MathUtils.makeNegativeRandomly((rand.nextFloat() * 0.1F), rand) + 0.025F);
					}
				}
				
				this.getEntityWorld().playSound(posX, posY, posZ, SoundEvents.BLOCK_CHORUS_FLOWER_DEATH, SoundCategory.BLOCKS, 1, 1, true);
				this.getEntityWorld().setBlockState(getOrigin(), EEBlocks.POISE_CLUSTER.getDefaultState());
				this.remove();
			}
			
			if(!this.getEntityWorld().getBlockState(new BlockPos(Math.ceil(this.posX) - 0.5F, (Math.ceil(this.posY) - 1), Math.ceil(this.posZ) - 0.5F)).isAir()) {
				BlockPos pos = new BlockPos(posX, posY, posZ);
				
				for(int i = 0; i < 8; i++) {
					double offsetX = MathUtils.makeNegativeRandomly(rand.nextFloat() * 0.25F, rand);
					double offsetZ = MathUtils.makeNegativeRandomly(rand.nextFloat() * 0.25F, rand);
				
					double x = pos.getX() + 0.5D + offsetX;
					double y = pos.getY() + 0.5D + (rand.nextFloat() * 0.05F);
					double z = pos.getZ() + 0.5D + offsetZ;
				
					if(this.isServerWorld()) {
						NetworkUtil.spawnParticle("endergetic:short_poise_bubble", x, y, z, MathUtils.makeNegativeRandomly((rand.nextFloat() * 0.1F), rand) + 0.025F, (rand.nextFloat() * 0.15F) + 0.1F, MathUtils.makeNegativeRandomly((rand.nextFloat() * 0.1F), rand) + 0.025F);
					}
				}
				
				this.getEntityWorld().playSound(posX, posY, posZ, SoundEvents.BLOCK_CHORUS_FLOWER_DEATH, SoundCategory.BLOCKS, 3F, 1, false);
				this.getEntityWorld().setBlockState(pos, EEBlocks.POISE_CLUSTER.getDefaultState());
				this.remove();
			}
		}
		
		if(this.isAscending() && this.prevPosY == posY && this.ticksExisted % 25 == 0 && Math.ceil(this.posY) > this.getOrigin().getY() + this.getBlocksToMoveUp()) {
			this.dataManager.set(ASCEND, false);
			this.setBlocksToMoveUp(0);
		}
		
		/*
		 * Used to make it try to move down if there is another entity of itself above it
		 */
		AxisAlignedBB bb = this.getBoundingBox().offset(0, 1, 0);
		List<Entity> entities = this.getEntityWorld().getEntitiesWithinAABB(Entity.class, bb);
		int entityCount = entities.size();
		boolean hasEntity = entityCount > 0;
		if(hasEntity && isAscending()) {
			
			for(int i = 0; i < entities.size(); i++) {
				Entity entity = entities.get(i);
				
				if(entity instanceof EntityPoiseCluster) {
					this.dataManager.set(ASCEND, false);
					this.setBlocksToMoveUp(0);
				}
				
				if(entity instanceof PlayerEntity) {
					((PlayerEntity)entity).fallDistance = 0;
				}
				
			}
			
		}
		
		/*
		 * Tell it to being moving down if a block is blocking its way up at its position above
		 */
		if(this.isAscending()) {
			
			if(this.getEntityWorld().getGameTime() % 20 == 10 && !this.getEntityWorld().getBlockState(new BlockPos(Math.ceil(this.posX) - 0.5F, (Math.ceil(this.posY) + 1), Math.ceil(this.posZ) - 0.5F)).isAir()) {
				this.dataManager.set(ASCEND, false);
				this.setBlocksToMoveUp(0);
			}
			
		}
		
		//Prevents going over max height
		if(this.getBlocksToMoveUp() > 30) {
			this.setBlocksToMoveUp(30);
		}
		
		this.clearActivePotions();
		this.extinguish();
		
		if(this.getHealth() != 0) this.setHealth(100);
		
		if(this.getMotion().length() > 0 && this.isAscending()) {
			AxisAlignedBB clusterBB = this.getBoundingBox().offset(0.0F, 0.01F, 0.0F);
			List<Entity> entitiesAbove = this.world.getEntitiesWithinAABBExcludingEntity(null, clusterBB);
			if(!entitiesAbove.isEmpty()) {
				for(int i = 0; i < entitiesAbove.size(); i++) {
					Entity entity = entitiesAbove.get(i);
					if(!(entity instanceof EntityPoiseCluster) && entity.getPushReaction() != PushReaction.IGNORE) {
						AxisAlignedBB entityBB = entity.getBoundingBox();
						double distanceMotion = (clusterBB.maxY - entityBB.minY) + (entity instanceof PlayerEntity ? 0.0225F : 0.02F);

						entity.move(MoverType.SELF, new Vec3d(0.0F, distanceMotion, 0.0F));
						entity.onGround = true;
					}
				}
			}
		}
		
		super.tick();
	}
	
	@Override
	public boolean hitByEntity(Entity entityIn) {
		this.dataManager.set(TIMES_HIT, this.dataManager.get(TIMES_HIT) + 1);
		if(this.dataManager.get(TIMES_HIT) == 3) {
			if(!this.getEntityWorld().isRemote) {
				Block.spawnAsEntity(getEntityWorld(), this.getPosition(), new ItemStack(EEBlocks.POISE_CLUSTER));
			}
			this.remove();
			return true;
		}
		this.dataManager.set(ASCEND, true);
		this.setBlocksToMoveUp((int) (Math.ceil(this.posY) - this.getOrigin().getY()) + 10);
		return false;
	}
	
	@Override
	protected void damageEntity(DamageSource damageSrc, float damageAmount) {
		if(damageSrc.isProjectile()) {
			this.dataManager.set(TIMES_HIT, this.dataManager.get(TIMES_HIT) + 1);
			
			if(this.dataManager.get(TIMES_HIT) == 3) {
				if(!this.getEntityWorld().isRemote) {
					Block.spawnAsEntity(getEntityWorld(), this.getPosition(), new ItemStack(EEBlocks.POISE_CLUSTER));
				}
				this.remove();
			}
			
			if((int) (Math.ceil(this.posY) - this.getOrigin().getY()) + 10 < 30) {
				this.dataManager.set(ASCEND, true);
				this.setBlocksToMoveUp((int) (Math.ceil(this.posY) - this.getOrigin().getY()) + 10);
			} else {
				this.remove();
			}
		}
		
		super.damageEntity(damageSrc, damageAmount);
	}
	
	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return EESounds.CLUSTER_BREAK.get();
	}
	
	@Override
	public void fall(float distance, float damageMultiplier) {}
	
	public void setBlocksToMoveUp(int value) {
		this.dataManager.set(BLOCKS_TO_MOVE_UP, value);
	}
	
	public int getBlocksToMoveUp() {
		return this.dataManager.get(BLOCKS_TO_MOVE_UP);
	}
	
	/*
	 * Used to tell if the entity is ascending or descending
	 */
	public boolean isAscending() {
		return this.dataManager.get(ASCEND);
	}
	
	@Override
	public void knockBack(Entity entityIn, float strength, double xRatio, double zRatio) {}
	
	@Override
	public void livingTick() {
		this.prevRenderYawOffset = 180.0F;
		this.renderYawOffset = 180.0F;
		this.rotationYaw = 180.0F;
		super.livingTick();
	}
	
	@Override
	public CreatureAttribute getCreatureAttribute() {
		return CreatureAttribute.ILLAGER;
	}
	
	public void setOrigin(BlockPos pos) {
		this.dataManager.set(ORIGIN, pos);
	}

	public BlockPos getOrigin() {
		return this.dataManager.get(ORIGIN);
	}
	
	@OnlyIn(Dist.CLIENT)
	@Override
	public boolean canRenderOnFire() {
		return false;
	}
	
	@Nullable
	public AxisAlignedBB getCollisionBox(Entity entityIn) {
		return entityIn.canBePushed() ? entityIn.getBoundingBox() : null;
	}
	
	@Nullable
	public AxisAlignedBB getCollisionBoundingBox() {
		return this.getBoundingBox();
	}
	
	@Override
	protected float getWaterSlowDown() {
		return 0;
	}
	
	@Override
	public boolean isPushedByWater() {
		return false;
	}
	
	@Override
	public boolean canBePushed() {
		return true;
	}

	@Override
	protected boolean canTriggerWalking() {
		return false;
	}
	
	@Override
	public boolean startRiding(Entity entityIn, boolean force) {
		return false;
	}
	
	@Override
	public boolean canBreatheUnderwater() {
		return true;
	}
	
	@Override
	protected void dealFireDamage(int amount) {}
	
	@Override
	public Iterable<ItemStack> getArmorInventoryList() {
		return NonNullList.withSize(4, ItemStack.EMPTY);
	}

	@Override
	public ItemStack getItemStackFromSlot(EquipmentSlotType slotIn) {
		return ItemStack.EMPTY;
	}

	@Override
	public void setItemStackToSlot(EquipmentSlotType slotIn, ItemStack stack) {}

	@Override
	public HandSide getPrimaryHand() {
		return HandSide.RIGHT;
	}
}
