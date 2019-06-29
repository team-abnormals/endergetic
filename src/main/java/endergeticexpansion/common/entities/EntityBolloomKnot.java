package endergeticexpansion.common.entities;

import javax.annotation.Nonnull;

import endergeticexpansion.core.registry.EEEntities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

public class EntityBolloomKnot extends Entity {

	public EntityBolloomKnot(EntityType<? extends EntityBolloomKnot> entityTypeIn, World world) {
		super(entityTypeIn, world);
	}
	
	public EntityBolloomKnot(World world, double x, double y, double z) {
		this(EEEntities.ObjectHolders.BOLLOOM_KNOT, world);
		this.setPosition(x, y, z);
		this.setMotion(Vec3d.ZERO);
		this.forceSpawn = true;
	}
	
	public boolean canBeCollidedWith() {
		return true;
	}
	
	@Override
	public boolean processInitialInteract(PlayerEntity player, Hand hand) {
		if (this.world.isRemote) {
	         return true;
		} else {
			this.remove();
			return true;
		}
	}
	
	@Override
	public void tick() {
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
	}
	
	@OnlyIn(Dist.CLIENT)
	public boolean isInRangeToRenderDist(double distance) {
		return distance < 1024.0D;
	}

	@Override
	protected void registerData() {}

	@Override
	protected void readAdditional(CompoundNBT compound) {}

	@Override
	protected void writeAdditional(CompoundNBT compound) {}

	@Nonnull
	@Override
	public IPacket<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

}
