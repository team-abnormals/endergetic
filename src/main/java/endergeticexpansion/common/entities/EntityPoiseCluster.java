package endergeticexpansion.common.entities;

import java.util.List;

import javax.annotation.Nullable;

import endergeticexpansion.core.registry.EEBlocks;
import endergeticexpansion.core.registry.EEEntities;
import net.minecraft.block.Block;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.HandSide;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class EntityPoiseCluster extends LivingEntity {
	private static final DataParameter<BlockPos> ORIGIN = EntityDataManager.createKey(EntityPoiseCluster.class, DataSerializers.BLOCK_POS);
	private static final DataParameter<Integer> BLOCKS_TO_MOVE_UP = EntityDataManager.createKey(EntityPoiseCluster.class, DataSerializers.VARINT);
	private static final DataParameter<Integer> TIMES_HIT = EntityDataManager.createKey(EntityPoiseCluster.class, DataSerializers.VARINT);
	private static final DataParameter<Boolean> ASCEND = EntityDataManager.createKey(EntityPoiseCluster.class, DataSerializers.BOOLEAN);
	
	public EntityPoiseCluster(EntityType<? extends EntityPoiseCluster> cluster, World worldIn) {
		super(EEEntities.POISE_CLUSTER, worldIn);
	}
	
	/*
	 * Constructor used for spawning the entity
	 */
	public EntityPoiseCluster(World worldIn, BlockPos origin, double x, double y, double z) {
		this(EEEntities.POISE_CLUSTER, worldIn);
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
	
	public void setBlocksToMoveUp(int value) {
		this.dataManager.set(BLOCKS_TO_MOVE_UP, value);
	}
	
	public int getBlocksToMoveUp() {
		return this.dataManager.get(BLOCKS_TO_MOVE_UP);
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
				this.getEntityWorld().playSound(posX, posY, posZ, SoundEvents.BLOCK_CHORUS_FLOWER_DEATH, SoundCategory.BLOCKS, 1, 1, true);
				this.getEntityWorld().setBlockState(getOrigin(), EEBlocks.POISE_CLUSTER.getDefaultState());
				this.remove();
			}
			
			if(!this.getEntityWorld().getBlockState(new BlockPos(Math.ceil(this.posX) - 0.5F, (Math.ceil(this.posY) - 1), Math.ceil(this.posZ) - 0.5F)).isAir()) {
				this.getEntityWorld().playSound(posX, posY, posZ, SoundEvents.BLOCK_CHORUS_FLOWER_DEATH, SoundCategory.BLOCKS, 3F, 1, false);
				this.getEntityWorld().setBlockState(new BlockPos(posX, posY, posZ), EEBlocks.POISE_CLUSTER.getDefaultState());
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
	public void fall(float distance, float damageMultiplier) {}
	
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
	public float getCollisionBorderSize() {
		return 0.0F;
	}
	
	@Override
	public void applyEntityCollision(Entity entityIn) {}

	@Override
	public double getYOffset() {
		return -0.1F;
	}
	
	@Override
	public CreatureAttribute getCreatureAttribute() {
		return CreatureAttribute.field_223222_a_;
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
