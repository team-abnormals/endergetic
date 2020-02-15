package endergeticexpansion.common.tileentities;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.Lists;

import endergeticexpansion.common.entities.puffbug.EntityPuffBug;
import endergeticexpansion.core.registry.EETileEntities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class TileEntityPuffBugHive extends TileEntity implements ITickableTileEntity {
	private final List<HiveOccupantData> hiveOccupants = Lists.newArrayList();

	public TileEntityPuffBugHive() {
		super(EETileEntities.PUFFBUG_HIVE.get());
	}
	
	@Override
	public void tick() {
		World world = this.world;
		
		if(!world.isRemote && !this.hiveOccupants.isEmpty()) {
			for(int i = 0; i < this.hiveOccupants.size(); i++) {
				HiveOccupantData hiveOccupant = this.hiveOccupants.get(i);
				
				if(hiveOccupant.Occupant == null) {
					this.hiveOccupants.remove(i);
				} else {
					hiveOccupant.tick(world);
				}
			};
		}
	}
	
	public void addBugToHive(EntityPuffBug puffBug) {
		if(!this.isHiveFull()) {
			this.hiveOccupants.add(new HiveOccupantData(puffBug.getUniqueID()));
		}
	}
	
	public void alertPuffBugs(@Nullable LivingEntity breaker) {
		this.hiveOccupants.forEach((Occupant) -> {
			EntityPuffBug puffBug = Occupant.getOccupant(this.world);
			if(puffBug != null) {
				if(breaker == null) {
					LivingEntity target = this.world.func_225318_b(LivingEntity.class, new EntityPredicate().setDistance(16.0D).setCustomPredicate(EntityPuffBug.CAN_ANGER), puffBug, puffBug.posX, puffBug.posY + puffBug.getEyeHeight(), puffBug.posZ, new AxisAlignedBB(this.pos).grow(16.0D));
					if(target != null) {
						puffBug.setAttackTarget(target);
					}
				} else {
					puffBug.setAttackTarget(breaker);
					puffBug.setHivePos(null);
				}
			}
		});
	}
	
	public List<HiveOccupantData> getHiveOccupants() {
		return this.hiveOccupants;
	}
	
	public int getTotalBugsInHive() {
		return this.hiveOccupants.size();
	}
	
	public boolean isHiveFull() {
		return this.getTotalBugsInHive() >= 5;
	}
	
	@Override
	@Nonnull
	public CompoundNBT write(CompoundNBT compound) {
		compound.put("HiveOccupants", HiveOccupantData.createCompoundList(this));
		return super.write(compound);
	}
	
	@Override
	public void read(CompoundNBT compound) {
		this.hiveOccupants.clear();
		ListNBT Occupants = compound.getList("HiveOccupants", 10);
	
		for(int i = 0; i < Occupants.size(); i++) {
			CompoundNBT Occupant = Occupants.getCompound(i);
			String OccupantUUID = Occupant.contains("OccupantUUID", 8) ? Occupant.getString("OccupantUUID") : "";
			
			UUID foundUUID = !OccupantUUID.isEmpty() ? UUID.fromString(OccupantUUID) : null;
			this.hiveOccupants.add(new HiveOccupantData(foundUUID));
		}
		
		super.read(compound);
	}
	
	@Nullable
	public SUpdateTileEntityPacket getUpdatePacket() {
		return new SUpdateTileEntityPacket(this.pos, 9, this.getUpdateTag());
	}
	
	@Nonnull
	@Override
	public CompoundNBT getUpdateTag() {
		return this.write(new CompoundNBT());
	}
	
	public boolean onlyOpsCanSetNbt() {
		return true;
	}
	
	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		return super.getRenderBoundingBox().grow(1084);
	}
	
	@Override
	public double getMaxRenderDistanceSquared() {
		return 16384.0D;
	}
	
	public static class HiveOccupantData {
		@Nullable
		private UUID Occupant;
		
		public HiveOccupantData(@Nullable UUID Occupant) {
			this.Occupant = Occupant;
		}
		
		public void tick(World world) {
			if(this.getOccupant(world) == null) {
				this.Occupant = null;
			}
		}
		
		@Nullable
		public EntityPuffBug getOccupant(World world) {
			if(!world.isRemote) {
				Entity entity = ((ServerWorld) world).getEntityByUuid(this.Occupant);
				if(entity instanceof EntityPuffBug) {
					return (EntityPuffBug) entity;
				}
			}
			return null;
		}
		
		public static ListNBT createCompoundList(TileEntityPuffBugHive hive) {
			ListNBT listnbt = new ListNBT();

			for(HiveOccupantData occuptentData : hive.hiveOccupants) {
				CompoundNBT compound = new CompoundNBT();
				
				if(occuptentData.Occupant == null) {
					compound.putString("OccupantUUID", "");
				} else {
					compound.putString("OccupantUUID", occuptentData.Occupant.toString());
				}
				
				listnbt.add(compound);
			}

			return listnbt;
		}
		
		public static boolean isHiveSideEmpty(TileEntityPuffBugHive hive, Direction direction) {
			for(int i = 0; i < hive.getTotalBugsInHive(); i++) {
				EntityPuffBug bug = hive.getHiveOccupants().get(i).getOccupant(hive.world);
				if(bug != null && bug.getAttachedHiveSide() == direction) {
					return false;
				}
			}
			return true;
		}
	}
}