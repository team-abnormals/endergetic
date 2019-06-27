package endergeticexpansion.common.entities;

import java.util.List;

import endergeticexpansion.core.registry.EEBlocks;
import endergeticexpansion.core.registry.EEEntities;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemFrameEntity;
import net.minecraft.entity.item.PaintingEntity;
import net.minecraft.entity.monster.ShulkerEntity;
import net.minecraft.entity.monster.VexEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
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

public class EntityBoofBlock extends LivingEntity {
	private static final DataParameter<BlockPos> ORIGIN = EntityDataManager.createKey(EntityBoofBlock.class, DataSerializers.BLOCK_POS);
	private static final DataParameter<Boolean> FOR_PROJECTILE = EntityDataManager.createKey(EntityBoofBlock.class, DataSerializers.BOOLEAN);
	
	public EntityBoofBlock(EntityType<? extends EntityBoofBlock> type, World world) {
		super(EEEntities.BOOF_BLOCK, world);
		this.setNoGravity(true);
	}
	
	public EntityBoofBlock(World world, BlockPos pos) {
		this(EEEntities.BOOF_BLOCK, world);
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
		int entityCount = entities.size();
		boolean hasEntity = entityCount > 0;
		if(hasEntity) {
			for(int i = 0; i < entities.size(); i++) {
				Entity entity = entities.get(i);
				
				if(!(entity instanceof EntityBoofBlock) &&
					!(entity instanceof ShulkerEntity) &&
					!(entity instanceof TridentEntity) &&
					!(entity instanceof AbstractArrowEntity) &&
					!(entity instanceof PaintingEntity) &&
					!(entity instanceof VexEntity) &&
					!(entity instanceof EntityPoiseCluster) &&
					!(entity instanceof ItemFrameEntity)
				) {
					if(entity.posY - 0.45F >= this.posY) {
						entity.addVelocity(0, this.rand.nextFloat() * 0.05D + 0.35D, 0);
					} else if(entity.posY < this.posY - 1F) {
						entity.addVelocity(0, this.rand.nextFloat() * -0.05D - 0.35D, 0);
					} else {
						float amount = 0.5F;
						Vec3d result = entity.getPositionVec().subtract(this.getPositionVec());
						entity.addVelocity(result.x * amount, this.rand.nextFloat() * 0.45D + 0.25D, result.z * amount);
					}
				} else if((entity instanceof TridentEntity) || (entity instanceof AbstractArrowEntity)) {
					this.setForProjectile();
					this.getEntityWorld().setBlockState(getOrigin(), Blocks.AIR.getDefaultState());
					entity.addVelocity(MathHelper.sin((float) (entity.rotationYaw * Math.PI / 180.0F)) * 3 * 0.1F, 0.55D, -MathHelper.cos((float) (entity.rotationYaw * Math.PI / 180.0F)) * 3 * 0.1F);
				}
			}
		}
		if(this.ticksExisted >= 10) {
			if(this.getEntityWorld().isAreaLoaded(this.getOrigin(), 1) && this.getEntityWorld().getBlockState(getOrigin()).getBlock() == EEBlocks.BOOF_BLOCK && !this.isForProjectile()) {
				this.getEntityWorld().setBlockState(getOrigin(), EEBlocks.BOOF_BLOCK.getDefaultState());
			} else if(this.getEntityWorld().isAreaLoaded(this.getOrigin(), 1) && this.isForProjectile()) {
				this.getEntityWorld().setBlockState(getOrigin(), EEBlocks.BOOF_BLOCK.getDefaultState());
			}
			this.remove();
		}
		this.setMotion(Vec3d.ZERO);
		super.tick();
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
	
	public void setForProjectile() {
		this.getDataManager().set(FOR_PROJECTILE, true);
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
