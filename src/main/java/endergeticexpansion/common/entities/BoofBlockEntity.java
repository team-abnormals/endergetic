package endergeticexpansion.common.entities;

import java.util.List;

import endergeticexpansion.core.registry.EEBlocks;
import endergeticexpansion.core.registry.EEEntities;
import endergeticexpansion.core.registry.other.EETags;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.HandSide;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class BoofBlockEntity extends LivingEntity {
	private static final DataParameter<BlockPos> ORIGIN = EntityDataManager.createKey(BoofBlockEntity.class, DataSerializers.BLOCK_POS);
	private static final DataParameter<Boolean> FOR_PROJECTILE = EntityDataManager.createKey(BoofBlockEntity.class, DataSerializers.BOOLEAN);
	
	public BoofBlockEntity(EntityType<? extends BoofBlockEntity> type, World world) {
		super(EEEntities.BOOF_BLOCK.get(), world);
		this.setNoGravity(true);
	}
	
	public BoofBlockEntity(World world, BlockPos pos) {
		this(EEEntities.BOOF_BLOCK.get(), world);
		this.setPosition(pos.getX() + 0.5F, pos.getY(), pos.getZ() + 0.5F);
		this.setOrigin(pos);
	}
	
	@Override
	protected void registerData() {
		this.getDataManager().register(ORIGIN, BlockPos.ZERO);
		this.getDataManager().register(FOR_PROJECTILE, false);
		super.registerData();
	}
	
	@Override
	public void tick() {
		AxisAlignedBB bb = this.getBoundingBox().grow(0, 0.25F, 0);
		List<Entity> entities = this.getEntityWorld().getEntitiesWithinAABB(Entity.class, bb);
		for(int i = 0; i < entities.size(); i++) {
			Entity entity = entities.get(i);
				
			if(!EETags.EntityTypes.BOOF_BLOCK_RESISTANT.contains(entity.getType())) {
				if(entity instanceof TridentEntity || entity instanceof AbstractArrowEntity) {
					this.setForProjectile(true);
					this.world.setBlockState(getOrigin(), Blocks.AIR.getDefaultState());
					entity.addVelocity(MathHelper.sin((float) (entity.rotationYaw * Math.PI / 180.0F)) * 3 * 0.1F, 0.55D, -MathHelper.cos((float) (entity.rotationYaw * Math.PI / 180.0F)) * 3 * 0.1F);
				} else {
					if(entity.getPosY() - 0.45F >= this.getPosY()) {
						entity.addVelocity(0, this.rand.nextFloat() * 0.05D + 0.35D, 0);
					} else if(entity.getPosY() < this.getPosY() - 1F) {
						entity.addVelocity(0, this.rand.nextFloat() * -0.05D - 0.35D, 0);
					} else {
						float amount = 0.5F;
						Vec3d result = entity.getPositionVec().subtract(this.getPositionVec());
						entity.addVelocity(result.x * amount, this.rand.nextFloat() * 0.45D + 0.25D, result.z * amount);
					}
				}
			}
		}
		
		if(this.ticksExisted >= 10) {
			if(this.world.isAreaLoaded(this.getOrigin(), 1) && this.world.getBlockState(getOrigin()).getBlock() == EEBlocks.BOOF_BLOCK.get() && !this.isForProjectile()) {
				this.world.setBlockState(this.getOrigin(), EEBlocks.BOOF_BLOCK.get().getDefaultState());
			} else if(this.world.isAreaLoaded(this.getOrigin(), 1) && this.isForProjectile()) {
				this.getEntityWorld().setBlockState(this.getOrigin(), EEBlocks.BOOF_BLOCK.get().getDefaultState());
			}
			this.remove();
		}
		
		this.setMotion(Vec3d.ZERO);
		super.tick();
	}
	
	@Override
	public void readAdditional(CompoundNBT nbt) {
		super.readAdditional(nbt);
		this.setOrigin(new BlockPos(nbt.getInt("OriginX"), nbt.getInt("OriginY"), nbt.getInt("OriginZ")));
		this.setForProjectile(nbt.getBoolean("ForProjectile"));
	}
	
	@Override
	public void writeAdditional(CompoundNBT nbt) {
		super.writeAdditional(nbt);
		BlockPos blockpos = this.getOrigin();
		nbt.putInt("OriginX", blockpos.getX());
		nbt.putInt("OriginY", blockpos.getY());
		nbt.putInt("OriginZ", blockpos.getZ());
		nbt.putBoolean("ForProjectile", this.isForProjectile());
	}
	
	@Override
	public boolean isInvulnerable() {
		return true;
	}
	
	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {return false;}
	
	public boolean isForProjectile() {
		return this.getDataManager().get(FOR_PROJECTILE);
	}
	
	@Override
	public boolean canRenderOnFire() {
		return false;
	}
	
	public void setForProjectile(boolean forProjectile) {
		this.getDataManager().set(FOR_PROJECTILE, forProjectile);
	}
	
	public void setOrigin(BlockPos pos) {
		this.getDataManager().set(ORIGIN, pos);
	}
	
	public BlockPos getOrigin() {
		return this.getDataManager().get(ORIGIN);
	}
	
	@Override
	public boolean canBePushed() {
		return false;
	}
	
	@Override
	public boolean isPushedByWater() {
		return false;
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