package endergeticexpansion.common.entities.bolloom;

import java.util.Optional;
import java.util.UUID;

import javax.annotation.Nullable;

import endergeticexpansion.core.registry.EEEntities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MoverType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ServerWorld;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class EntityBolloomBalloon extends Entity {
	private static final DataParameter<Float> ORIGINAL_X = EntityDataManager.createKey(EntityBolloomBalloon.class, DataSerializers.FLOAT);
	private static final DataParameter<Float> ORIGINAL_Z = EntityDataManager.createKey(EntityBolloomBalloon.class, DataSerializers.FLOAT);
	private static final DataParameter<Float> ORIGINAL_Y = EntityDataManager.createKey(EntityBolloomBalloon.class, DataSerializers.FLOAT);
	private static final DataParameter<Float> ANGLE = EntityDataManager.createKey(EntityBolloomBalloon.class, DataSerializers.FLOAT);
	private static final DataParameter<Float> DESIRED_ANGLE = EntityDataManager.createKey(EntityBolloomBalloon.class, DataSerializers.FLOAT);
	private static final DataParameter<Float> SWAY = EntityDataManager.createKey(EntityBolloomBalloon.class, DataSerializers.FLOAT);
	private static final DataParameter<Boolean> UNTIED = EntityDataManager.createKey(EntityBolloomBalloon.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Optional<UUID>> KNOT_UNIQUE_ID = EntityDataManager.<Optional<UUID>>createKey(EntityBolloomBalloon.class, DataSerializers.OPTIONAL_UNIQUE_ID);
	private static final DataParameter<Integer> TICKSEXISTED = EntityDataManager.createKey(EntityBolloomBalloon.class, DataSerializers.VARINT); //Vanilla's ticksExisted isn't synced between server and client
	public float prevVineAngle;
	public float prevAngle;
	
	public EntityBolloomBalloon(EntityType<? extends EntityBolloomBalloon> entityType, World world) {
		super(entityType, world);
	}
	
	public EntityBolloomBalloon(World world, UUID ownerKnot, BlockPos pos) {
		this(EEEntities.ObjectEntites.BOLLOOM_BALLOON, world);
		this.setPosition(pos.getX() + 0.5F, pos.getY() + 3, pos.getZ() + 0.5F);
		this.setOriginalPos(pos.getX() + 0.5F, pos.getY() + 3, pos.getZ() + 0.5F);
		this.setKnotId(ownerKnot);
	}
	
	@Override
	public void tick() {
		super.tick();
		this.move(MoverType.SELF, this.getMotion());
		this.prevVineAngle = this.getVineAngle();
		this.prevAngle = this.getAngle();
		if(!world.isRemote) {
			if(this.getKnot() == null) {
				this.setUntied();
			}
		}
		if(world.isAreaLoaded(this.getPosition(), 1)) {
			this.dataManager.set(SWAY, (float) Math.sin((2 * Math.PI / 100 * this.getTicksExisted())) * 0.5F);
		}
		if(world.isAreaLoaded(this.getPosition(), 1)) {
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
		if(!world.isRemote) {
			if (this.getTicksExisted() % 45 == 0) {
			    this.getDataManager().set(DESIRED_ANGLE, (float) (rand.nextDouble() * 2 * Math.PI));
			}
			
			if(this.getTicksExisted() % 1200 == 0 && this.isUntied()) {
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
		}
		this.incrementTicksExisted();
	}
	
	@Override
	protected void registerData() {
		this.getDataManager().register(KNOT_UNIQUE_ID, Optional.empty());
		this.getDataManager().register(ORIGINAL_X, 0F);
		this.getDataManager().register(ORIGINAL_Z, 0F);
		this.getDataManager().register(ORIGINAL_Y, 0F);
		this.getDataManager().register(UNTIED, false);
		this.getDataManager().register(ANGLE, 0F);
		this.getDataManager().register(SWAY, 0F);
		this.getDataManager().register(DESIRED_ANGLE, 0F);
		this.getDataManager().register(TICKSEXISTED, 0);
	}

	@Override
	protected void writeAdditional(CompoundNBT nbt) {
		if(this.getKnotId() != null) {
			nbt.put("KnotUUID", NBTUtil.writeUniqueId(this.getKnotId()));
		}
		nbt.putBoolean("UNTIED", this.getDataManager().get(UNTIED));
		nbt.putFloat("ORIGINAL_X", this.getDataManager().get(ORIGINAL_X));
		nbt.putFloat("ORIGINAL_Y", this.getDataManager().get(ORIGINAL_Y));
		nbt.putFloat("ORIGINAL_Z", this.getDataManager().get(ORIGINAL_Z));
	}
	
	@Override
	protected void readAdditional(CompoundNBT nbt) {
		this.setKnotId(nbt.getUniqueId("KnotUUID"));
		this.getDataManager().set(UNTIED, nbt.getBoolean("UNTIED"));
		this.getDataManager().set(ORIGINAL_X, nbt.getFloat("ORIGINAL_X"));
		this.getDataManager().set(ORIGINAL_Y, nbt.getFloat("ORIGINAL_Y"));
		this.getDataManager().set(ORIGINAL_Z, nbt.getFloat("ORIGINAL_Z"));
	}
	
	@Nullable
	public Entity getKnot() {
		return ((ServerWorld)world).getEntityByUuid(getKnotId());
	}
	
	@Nullable
    public UUID getKnotId() {
        return this.dataManager.get(KNOT_UNIQUE_ID).orElse((UUID)null);
    }

    public void setKnotId(@Nullable UUID knotUUID) {
        this.dataManager.set(KNOT_UNIQUE_ID, Optional.ofNullable(knotUUID));
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
    
    public float getVineAngle() {
		return (float) Math.atan(this.dataManager.get(SWAY) / 2F);
	}
    
    public void setUntied() {
		this.getDataManager().set(UNTIED, true);
	}
    
    public boolean isUntied() {
		return this.getDataManager().get(UNTIED);
	}
    
    public float getSetY() {
		return this.getDataManager().get(ORIGINAL_Y);
	}
    
    public void setOriginalPos(float x, float y, float z) {
		this.getDataManager().set(ORIGINAL_X, (float) x);
		this.getDataManager().set(ORIGINAL_Y, (float) y);
		this.getDataManager().set(ORIGINAL_Z, (float) z);
	}
    
    public int getTicksExisted() {
		return this.getDataManager().get(TICKSEXISTED);
	}

	public void incrementTicksExisted() {
		this.getDataManager().set(TICKSEXISTED, getTicksExisted() + 1);
	}
    
    @Override
    protected boolean canTriggerWalking() {
    	return false;
    }
    
    @Override
	public boolean canBePushed() {
		return true;
	}
    
    @Override
	public AxisAlignedBB getRenderBoundingBox() {
		return super.getRenderBoundingBox().grow(5);
	}

	@Override
	public IPacket<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

}
