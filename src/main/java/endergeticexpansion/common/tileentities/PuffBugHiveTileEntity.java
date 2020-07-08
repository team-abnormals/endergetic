package endergeticexpansion.common.tileentities;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.Lists;

import endergeticexpansion.api.entity.util.DetectionHelper;
import endergeticexpansion.common.entities.puffbug.PuffBugEntity;
import endergeticexpansion.core.registry.EETileEntities;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class PuffBugHiveTileEntity extends TileEntity implements ITickableTileEntity {
	private final List<HiveOccupantData> hiveOccupants = Lists.newArrayList();
	private int ticksTillResetTeleport;
	private int teleportCooldown;
	private boolean shouldReset;

	public PuffBugHiveTileEntity() {
		super(EETileEntities.PUFFBUG_HIVE.get());
	}
	
	@Override
	public void tick() {
		World world = this.world;
		
		if(!world.isRemote && !this.hiveOccupants.isEmpty()) {
			if(this.ticksTillResetTeleport > 0) {
				this.ticksTillResetTeleport--;
			} else if(this.shouldReset) {
				this.hiveOccupants.forEach(occupent -> occupent.teleportSide = null);
				this.shouldReset = false;
			}
			
			if(this.teleportCooldown > 0) {
				this.teleportCooldown--;
			}
			
			for(int i = 0; i < this.hiveOccupants.size(); i++) {
				HiveOccupantData hiveOccupant = this.hiveOccupants.get(i);
				
				if(hiveOccupant.occupant == null) {
					this.hiveOccupants.remove(i);
				} else {
					hiveOccupant.tick(world);
				}
			};
		}
	}
	
	public void addBugToHive(PuffBugEntity puffBug) {
		if(!this.isHiveFull()) {
			this.hiveOccupants.add(new HiveOccupantData(puffBug.getUniqueID()));
		}
	}
	
	public void alertPuffBugs(@Nullable LivingEntity breaker) {
		this.hiveOccupants.forEach((Occupant) -> {
			PuffBugEntity puffBug = Occupant.getOccupant(this.world);
			BlockPos hivePos = this.pos;
			if(puffBug != null) {
				if(puffBug.getAttackTarget() == null) {
					puffBug.setAttachedHiveSide(Direction.UP);
					puffBug.tryToTeleportToHive(hivePos);
				}
				
				if(breaker == null) {
					LivingEntity target = DetectionHelper.getClosestEntity(this.world.getEntitiesWithinAABB(LivingEntity.class, new AxisAlignedBB(hivePos).grow(12.0D), PuffBugEntity.CAN_ANGER), hivePos.getX(), hivePos.getY(), hivePos.getZ());
					if(target != null && puffBug.getAttackTarget() == null) {
						puffBug.setAttackTarget(target);
					}
				} else {
					puffBug.setAttackTarget(breaker);
					puffBug.setHivePos(null);
				}
			}
		});
		
		this.addTeleportCooldown();
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
	
	public boolean canTeleportTo() {
		return this.teleportCooldown <= 0;
	}
	
	public void addTeleportCooldown() {
		this.teleportCooldown = 500;
	}
	
	@Nullable
	private HiveOccupantData getOccupentByUUID(UUID uuid) {
		for(HiveOccupantData occupents : this.hiveOccupants) {
			if(occupents.occupant == uuid) {
				return occupents;
			}
		}
		return null;
	}
	
	public void setBeingTeleportedToBy(PuffBugEntity puffbug, Direction side) {
		HiveOccupantData occupentData = this.getOccupentByUUID(puffbug.getUniqueID());
		if(occupentData != null) {
			occupentData.teleportSide = side;
			this.ticksTillResetTeleport = 250;
			this.shouldReset = true;
		}
	}
	
	public boolean isSideBeingTeleportedTo(Direction side) {
		for(HiveOccupantData occupents : this.hiveOccupants) {
			if(occupents.teleportSide == side) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	@Nonnull
	public CompoundNBT write(CompoundNBT compound) {
		compound.put("HiveOccupants", HiveOccupantData.createCompoundList(this));
		
		compound.putInt("TeleportCooldown", this.teleportCooldown);
		
		return super.write(compound);
	}
	
	@Override
	public void read(BlockState state, CompoundNBT compound) {
		this.hiveOccupants.clear();
		ListNBT Occupants = compound.getList("HiveOccupants", 10);
	
		for(int i = 0; i < Occupants.size(); i++) {
			CompoundNBT Occupant = Occupants.getCompound(i);
			String OccupantUUID = Occupant.contains("OccupantUUID", 8) ? Occupant.getString("OccupantUUID") : "";
			
			UUID foundUUID = !OccupantUUID.isEmpty() ? UUID.fromString(OccupantUUID) : null;
			this.hiveOccupants.add(new HiveOccupantData(foundUUID));
		}
		
		this.teleportCooldown = compound.getInt("TeleportCooldown");
		
		super.read(state, compound);
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
		private UUID occupant;
		@Nullable
		private Direction teleportSide;
		
		public HiveOccupantData(@Nullable UUID occupant) {
			this.occupant = occupant;
		}
		
		public void tick(World world) {
			if(this.getOccupant(world) == null) {
				this.occupant = null;
			}
		}
		
		@Nullable
		public PuffBugEntity getOccupant(World world) {
			if(!world.isRemote) {
				Entity entity = ((ServerWorld) world).getEntityByUuid(this.occupant);
				if(entity instanceof PuffBugEntity) {
					return (PuffBugEntity) entity;
				}
			}
			return null;
		}
		
		public static ListNBT createCompoundList(PuffBugHiveTileEntity hive) {
			ListNBT listnbt = new ListNBT();

			for(HiveOccupantData occuptentData : hive.hiveOccupants) {
				CompoundNBT compound = new CompoundNBT();
				
				if(occuptentData.occupant == null) {
					compound.putString("OccupantUUID", "");
				} else {
					compound.putString("OccupantUUID", occuptentData.occupant.toString());
				}
				
				listnbt.add(compound);
			}

			return listnbt;
		}
		
		public static boolean isHiveSideEmpty(PuffBugHiveTileEntity hive, Direction direction) {
			for(int i = 0; i < hive.getTotalBugsInHive(); i++) {
				PuffBugEntity bug = hive.getHiveOccupants().get(i).getOccupant(hive.world);
				if(bug != null && bug.getAttachedHiveSide() == direction) {
					return false;
				}
			}
			return true;
		}
	}
}