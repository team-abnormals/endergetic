package endergeticexpansion.common.tileentities;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.Lists;

import endergeticexpansion.common.entities.EntityPuffBug;
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
	private final List<HiveOccupentData> hiveOccupents = Lists.newArrayList();

	public TileEntityPuffBugHive() {
		super(EETileEntities.PUFFBUG_HIVE.get());
	}
	
	@Override
	public void tick() {
		World world = this.world;
		
		if(!world.isRemote && !this.hiveOccupents.isEmpty()) {
			for(int i = 0; i < this.hiveOccupents.size(); i++) {
				HiveOccupentData hiveOccupent = this.hiveOccupents.get(i);
				
				if(hiveOccupent.occupent == null) {
					this.hiveOccupents.remove(i);
				} else {
					hiveOccupent.tick(world);
				}
			};
		}
	}
	
	public void addBugToHive(EntityPuffBug puffBug) {
		if(!this.isHiveFull()) {
			this.hiveOccupents.add(new HiveOccupentData(puffBug.getUniqueID()));
		}
	}
	
	public void alertPuffBugs(@Nullable LivingEntity breaker) {
		this.hiveOccupents.forEach((occupent) -> {
			EntityPuffBug puffBug = occupent.getOccupent(this.world);
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
	
	public int getTotalBugsInHive() {
		return this.hiveOccupents.size();
	}
	
	public boolean isHiveFull() {
		return this.getTotalBugsInHive() >= 5;
	}
	
	@Override
	@Nonnull
	public CompoundNBT write(CompoundNBT compound) {
		compound.put("HiveOccupents", HiveOccupentData.createCompoundList(this));
		return super.write(compound);
	}
	
	@Override
	public void read(CompoundNBT compound) {
		this.hiveOccupents.clear();
		ListNBT occupents = compound.getList("HiveOccupents", 10);
	
		for(int i = 0; i < occupents.size(); i++) {
			CompoundNBT occupent = occupents.getCompound(i);
			String occupentUUID = occupent.contains("OccupentUUID", 8) ? occupent.getString("OccupentUUID") : "";
			
			UUID foundUUID = !occupentUUID.isEmpty() ? UUID.fromString(occupentUUID) : null;
			this.hiveOccupents.add(new HiveOccupentData(foundUUID));
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
	
	static class HiveOccupentData {
		@Nullable
		private UUID occupent;
		
		public HiveOccupentData(@Nullable UUID occupent) {
			this.occupent = occupent;
		}
		
		public void tick(World world) {
			if(this.getOccupent(world) == null) {
				this.occupent = null;
			}
		}
		
		@Nullable
		public EntityPuffBug getOccupent(World world) {
			if(!world.isRemote) {
				Entity entity = ((ServerWorld) world).getEntityByUuid(this.occupent);
				if(entity instanceof EntityPuffBug) {
					return (EntityPuffBug) entity;
				}
			}
			return null;
		}
		
		public static ListNBT createCompoundList(TileEntityPuffBugHive hive) {
			ListNBT listnbt = new ListNBT();

			for(HiveOccupentData occuptentData : hive.hiveOccupents) {
				CompoundNBT compound = new CompoundNBT();
				
				if(occuptentData.occupent == null) {
					compound.putString("OccupentUUID", "");
				} else {
					compound.putString("OccupentUUID", occuptentData.occupent.toString());
				}
				
				listnbt.add(compound);
			}

			return listnbt;
		}
	}
	
	enum HiveSide {
		NORTH(Direction.NORTH, 1),
		EAST(Direction.EAST, 2),
		SOUTH(Direction.SOUTH, 3),
		WEST(Direction.WEST, 4),
		DOWN(Direction.DOWN, 5);
		
		public Direction direction;
		public int id;
		
		HiveSide(Direction direction, int id) {
			this.direction = direction;
			this.id = id;
		}
		
		@Nullable
		public static HiveSide getById(int id) {
			for(HiveSide sides : values()) {
				if(sides.id == id) {
					return sides;
				}
			}
			return null;
		}
	}
}