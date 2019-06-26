package endergeticexpansion.common.entities;

import javax.annotation.Nullable;

import endergeticexpansion.common.blocks.poise.BlockBolloomBud;
import endergeticexpansion.core.registry.EEBlocks;
import endergeticexpansion.core.registry.EEEntities;
import endergeticexpansion.core.registry.EEItems;
import net.minecraft.block.Block;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.HandSide;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityBolloomFruit extends LivingEntity {
	private static final DataParameter<Float> ORIGINAL_X = EntityDataManager.createKey(EntityBolloomFruit.class, DataSerializers.field_187193_c);
	private static final DataParameter<Float> ORIGINAL_Z = EntityDataManager.createKey(EntityBolloomFruit.class, DataSerializers.field_187193_c);
	private static final DataParameter<Float> ORIGINAL_Y = EntityDataManager.createKey(EntityBolloomFruit.class, DataSerializers.field_187193_c);
	private static final DataParameter<Float> ANGLE = EntityDataManager.createKey(EntityBolloomFruit.class, DataSerializers.field_187193_c);
	private static final DataParameter<Float> DESIRED_ANGLE = EntityDataManager.createKey(EntityBolloomFruit.class, DataSerializers.field_187193_c);
	private static final DataParameter<Integer> VINE_HEIGHT = EntityDataManager.createKey(EntityBolloomFruit.class, DataSerializers.field_187192_b);
	private static final DataParameter<Float> SWAY = EntityDataManager.createKey(EntityBolloomFruit.class, DataSerializers.field_187193_c);
	private static final DataParameter<Boolean> UNTIED = EntityDataManager.createKey(EntityBolloomFruit.class, DataSerializers.field_187198_h);
	private static final DataParameter<Boolean> GROWN = EntityDataManager.createKey(EntityBolloomFruit.class, DataSerializers.field_187198_h);
	private static final DataParameter<BlockPos> ORIGIN = EntityDataManager.createKey(EntityBolloomFruit.class, DataSerializers.field_187200_j);
	private static final DataParameter<Integer> DIRECTION = EntityDataManager.createKey(EntityBolloomFruit.class, DataSerializers.field_187192_b);
	public float prevVineAngle;
	public float prevAngle;
	
	public EntityBolloomFruit(EntityType<? extends EntityBolloomFruit> type, World world) {
		super(EEEntities.BOLLOOM_FRUIT, world);
		this.setNoGravity(true);
	}
	
	// 0 - North, 1 - East...
	public EntityBolloomFruit(World world, BlockPos budPos, BlockPos origin, int height, Direction direction) {
		this(EEEntities.BOLLOOM_FRUIT, world);
		if(direction == Direction.NORTH) {
			this.setPosition(origin.getX() + 0.5, origin.getY() + 1.15F, origin.getZ() + 0.5 + 0.3F);
			this.setOriginalSway(origin.getX() + 0.5F, origin.getZ() + 0.5F + 0.2F);
			this.setDirection(0);
		} else if(direction == Direction.SOUTH) {
			this.setPosition(origin.getX() + 0.5, origin.getY() + 1.15F, origin.getZ() + 0.5 - 0.3F);
			this.setOriginalSway(origin.getX() + 0.5F, origin.getZ() + 0.5F - 0.2F);
			this.setDirection(2);
		} else if(direction == Direction.WEST) {
			this.setPosition(origin.getX() + 0.5 + 0.4F, origin.getY() + 1.15F, origin.getZ() + 0.5);
			this.setOriginalSway(origin.getX() + 0.5F + 0.2F, origin.getZ() + 0.5F);
			this.setDirection(3);
		} else if(direction == Direction.EAST) {
			this.setPosition(origin.getX() + 0.5 + 0.4F, origin.getY() + 1.15F, origin.getZ() + 0.5);
			this.setOriginalSway(origin.getX() + 0.5F - 0.2F, origin.getZ() + 0.5F);
			this.setDirection(1);
		}
		this.getDataManager().set(ORIGIN, budPos);
		this.setVineHeight(height);
		this.getDataManager().set(ORIGINAL_Y, origin.getY() + 1.15F);
		this.prevRenderYawOffset = 180.0F;
		this.renderYawOffset = 180.0F;
		this.rotationYaw = 180.0F;
	}
	
	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		return super.getRenderBoundingBox().grow(35);
	}
	
	@Override
	protected void registerData() {
		super.registerData();
		this.getDataManager().register(ORIGINAL_X, 0F);
		this.getDataManager().register(ORIGINAL_Z, 0F);
		this.getDataManager().register(ORIGINAL_Y, 0F);
		this.getDataManager().register(SWAY, 0F);
		this.getDataManager().register(ANGLE, 0F);
		this.getDataManager().register(DESIRED_ANGLE, 0F);
		this.getDataManager().register(VINE_HEIGHT, 0);
		this.getDataManager().register(DIRECTION, 0);
		this.getDataManager().register(ORIGIN, BlockPos.ZERO);
		this.getDataManager().register(UNTIED, false);
		this.getDataManager().register(GROWN, false);
	}
	
	@Override
	public boolean canBeCollidedWith() {
		return super.canBeCollidedWith() && this.getDataManager().get(GROWN);
	}
	
	@Override
	protected void collideWithEntity(Entity entityIn) {
		if(this.getDataManager().get(GROWN)) {
			super.collideWithEntity(entityIn);
		}
	}
	
	@Override
	public void tick() {
		if(!world.isAreaLoaded(this.getOrigin(), 1)) return;
		this.prevVineAngle = this.getVineAngle();
		this.prevAngle = this.getAngle();
		if(world.isAreaLoaded(this.getOrigin(), 1)) {
			this.dataManager.set(SWAY, (float) Math.sin((float) (2 * Math.PI / 100 * ticksExisted)) * 0.5F);
		}
		if(world.isAreaLoaded(this.getOrigin(), 1)) {
			if(!this.isUntied()) {
				this.setPosition(
					this.getDataManager().get(ORIGINAL_X) + this.dataManager.get(SWAY) * Math.sin(-this.getAngle()),
					this.getSetY(),
					this.getDataManager().get(ORIGINAL_Z) + this.dataManager.get(SWAY) * Math.cos(-this.getAngle())
				);
			} else {
				this.setMotion(Math.sin(this.getVineAngle()) * Math.sin(-this.getAngle()) * 0.05F, Math.toRadians(4), Math.cos(this.getVineAngle()) * Math.cos(-this.getAngle()) * 0.05F);
			}
		}
		
		if (ticksExisted % 45 == 0) {
		    this.getDataManager().set(DESIRED_ANGLE, (float) (rand.nextDouble() * 2 * Math.PI));
		}
		
		if(ticksExisted % 50 == 0 && this.getRNG().nextInt(5) == 0 && !this.isUntied()) {
			this.getDataManager().set(GROWN, true);
		}
		
		if(ticksExisted % 1200 == 0 && this.isUntied()) {
			this.remove();
		}
		
		float dangle = this.getDesiredAngle() - this.getAngle();
		while (dangle > Math.PI) {
		    dangle -= 2 * Math.PI;
		}
		while (dangle <= -Math.PI) {
		    dangle += 2 * Math.PI;
		}
		if (Math.abs(dangle) <= 0.1F) {
		    this.setAngle(this.getAngle() + dangle);
		} else if (dangle > 0) {
		    this.setAngle(this.getAngle() + 0.03F);
		} else {
		    this.setAngle(this.getAngle() - 0.03F);
		}
		
		if(world.isAreaLoaded(this.getOrigin(), 1)) {
			if(this.getEntityWorld().getBlockState(this.getOrigin()).getBlock() != EEBlocks.BOLLOOM_BUD || !this.getEntityWorld().getBlockState(this.getOrigin()).get(BlockBolloomBud.OPENED)) {
				this.setUntied();
			}
			
			if(this.isUntied() && this.getEntityWorld().getBlockState(this.getOrigin()).getBlock() == EEBlocks.BOLLOOM_BUD) {
				if(this.getEntityWorld().getBlockState(this.getOrigin()).get(BlockBolloomBud.OPENED)) {
					if(this.getDirection() == 0) {
						this.getEntityWorld().setBlockState(this.getOrigin(), this.getEntityWorld().getBlockState(this.getOrigin()).with(BlockBolloomBud.HAS_NORTH_FRUIT, false));
					} else if(this.getDirection() == 1) {
						this.getEntityWorld().setBlockState(this.getOrigin(), this.getEntityWorld().getBlockState(this.getOrigin()).with(BlockBolloomBud.HAS_EAST_FRUIT, false));
					} else if(this.getDirection() == 2) {
						this.getEntityWorld().setBlockState(this.getOrigin(), this.getEntityWorld().getBlockState(this.getOrigin()).with(BlockBolloomBud.HAS_SOUTH_FRUIT, false));
					} else {
						this.getEntityWorld().setBlockState(this.getOrigin(), this.getEntityWorld().getBlockState(this.getOrigin()).with(BlockBolloomBud.HAS_WEST_FRUIT, false));
					}
				}
			} else if(!this.isUntied() && this.getEntityWorld().getBlockState(this.getOrigin()).getBlock() == EEBlocks.BOLLOOM_BUD) {
				if(this.getEntityWorld().getBlockState(this.getOrigin()).get(BlockBolloomBud.OPENED)) {
					if(this.getDirection() == 0) {
						this.getEntityWorld().setBlockState(this.getOrigin(), this.getEntityWorld().getBlockState(this.getOrigin()).with(BlockBolloomBud.HAS_NORTH_FRUIT, true));
					} else if(this.getDirection() == 1) {
						this.getEntityWorld().setBlockState(this.getOrigin(), this.getEntityWorld().getBlockState(this.getOrigin()).with(BlockBolloomBud.HAS_EAST_FRUIT, true));
					} else if(this.getDirection() == 2) {
						this.getEntityWorld().setBlockState(this.getOrigin(), this.getEntityWorld().getBlockState(this.getOrigin()).with(BlockBolloomBud.HAS_SOUTH_FRUIT, true));
					} else {
						this.getEntityWorld().setBlockState(this.getOrigin(), this.getEntityWorld().getBlockState(this.getOrigin()).with(BlockBolloomBud.HAS_WEST_FRUIT, true));
					}
				}
			}
		}
		
		if(this.checkForBlocksDown()) {
			this.setUntied();
		}
		
		this.clearActivePotions();
		this.extinguish();
		
		super.tick();
	}
	
	@Override
	public CreatureAttribute getCreatureAttribute() {
		return CreatureAttribute.UNDEAD;
	}
	
	@Override
	public boolean startRiding(Entity entityIn, boolean force) {
		return false;
	}
	
	@Override
	protected float getWaterSlowDown() {
		return 0;
	}
	
	@Override
	public boolean canEntityBeSeen(Entity entityIn) {
		return super.canEntityBeSeen(entityIn) && this.getDataManager().get(GROWN);
	}
	
	@Override
	public boolean attackable() {
		return this.getDataManager().get(GROWN);
	}
	
	@SuppressWarnings("deprecation")
	public boolean checkForBlocksDown() {
		for (int i = 0; i < this.getVineHeight(); i++) {
			BlockPos pos = this.getPosition().down(i);
			if(this.getEntityWorld().isAreaLoaded(pos, 1)) {
				if(!this.getEntityWorld().getBlockState(pos).isAir()) {
					return true;
				}
			}
		}
		return false;
	}
	
	@Override
	public boolean canBreatheUnderwater() {
		return true;
	}
	
	@Override
	public boolean canRenderOnFire() {
		return false;
	}
	
	@Override
	public boolean canBeHitWithPotion() {
		return false;
	}
	
	@Override
	protected void dealFireDamage(int amount) {}
	
	public float getDirection() {
		return this.getDataManager().get(DIRECTION);
	}
	
	public void setDirection(int dir) {
		this.getDataManager().set(DIRECTION, dir);
	}
	
	public float getSetY() {
		return this.getDataManager().get(ORIGINAL_Y);
	}
	
	public boolean isGrown() {
		return this.getDataManager().get(GROWN);
	}
	
	public float getVineAngle() {
		return (float) Math.atan(this.dataManager.get(SWAY) / (this.getVineHeight()));
	}
	
	public void setVineHeight(int height) {
		this.getDataManager().set(VINE_HEIGHT, height);
	}
	
	public int getVineHeight() {
		return this.getDataManager().get(VINE_HEIGHT);
	}
	
	public void setAngle(float degree) {
		this.getDataManager().set(ANGLE, degree);
	}
	
	public float getAngle() {
		return this.getDataManager().get(ANGLE);
	}
	
	public float getDesiredAngle() {
		return this.getDataManager().get(DESIRED_ANGLE);
	}
	
	public BlockPos getOrigin() {
		return this.getDataManager().get(ORIGIN);
	}
	
	public boolean isUntied() {
		return this.getDataManager().get(UNTIED);
	}
	
	public void setOriginalSway(float x, float z) {
		this.getDataManager().set(ORIGINAL_Z, (float) z);
		this.getDataManager().set(ORIGINAL_X, (float) x);
	}
	
	public void setUntied() {
		this.dataManager.set(UNTIED, true);
	}
	
	@Override
	protected void damageEntity(DamageSource damageSrc, float damageAmount) {
		if(!this.getEntityWorld().isRemote && this.ticksExisted > 10) {
			Block.spawnAsEntity(getEntityWorld(), this.getPosition(), new ItemStack(EEItems.BOLLOOM_FRUIT));
		}
		this.remove();
	}
	
	@Override
	public boolean hitByEntity(Entity entityIn) {
		this.remove();
		return false;
	}
	
	@Override
	@Nullable
	protected SoundEvent getDeathSound() {
		return null;
	}
	
	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return SoundEvents.BLOCK_WET_GRASS_BREAK;
	}
	
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
