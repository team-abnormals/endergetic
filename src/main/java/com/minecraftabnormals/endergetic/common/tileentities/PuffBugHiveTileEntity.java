package com.minecraftabnormals.endergetic.common.tileentities;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.Lists;
import com.minecraftabnormals.endergetic.api.entity.util.DetectionHelper;
import com.minecraftabnormals.endergetic.common.entities.puffbug.PuffBugEntity;
import com.minecraftabnormals.endergetic.core.registry.EETileEntities;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;

public class PuffBugHiveTileEntity extends BlockEntity implements TickableBlockEntity {
	private final List<HiveOccupantData> hiveOccupants = Lists.newArrayList();
	private int ticksTillResetTeleport;
	private int teleportCooldown;
	private boolean shouldReset;

	public PuffBugHiveTileEntity() {
		super(EETileEntities.PUFFBUG_HIVE.get());
	}

	@Override
	public void tick() {
		Level world = this.level;

		if (!world.isClientSide && !this.hiveOccupants.isEmpty()) {
			if (this.ticksTillResetTeleport > 0) {
				this.ticksTillResetTeleport--;
			} else if (this.shouldReset) {
				this.hiveOccupants.forEach(occupent -> occupent.teleportSide = null);
				this.shouldReset = false;
			}

			if (this.teleportCooldown > 0) {
				this.teleportCooldown--;
			}

			for (int i = 0; i < this.hiveOccupants.size(); i++) {
				HiveOccupantData hiveOccupant = this.hiveOccupants.get(i);

				if (hiveOccupant.occupant == null) {
					this.hiveOccupants.remove(i);
				} else {
					hiveOccupant.tick(world);
				}
			}
			;
		}
	}

	public void addBugToHive(PuffBugEntity puffBug) {
		if (!this.isHiveFull()) {
			this.hiveOccupants.add(new HiveOccupantData(puffBug.getUUID()));
		}
	}

	public void alertPuffBugs(@Nullable LivingEntity breaker) {
		this.hiveOccupants.forEach((Occupant) -> {
			PuffBugEntity puffBug = Occupant.getOccupant(this.level);
			BlockPos hivePos = this.worldPosition;
			if (puffBug != null) {
				if (puffBug.getTarget() == null) {
					puffBug.setAttachedHiveSide(Direction.UP);
					puffBug.tryToTeleportToHive(hivePos);
				}

				if (breaker == null) {
					LivingEntity target = DetectionHelper.getClosestEntity(this.level.getEntitiesOfClass(LivingEntity.class, new AABB(hivePos).inflate(12.0D), PuffBugEntity.CAN_ANGER), hivePos.getX(), hivePos.getY(), hivePos.getZ());
					if (target != null && puffBug.getTarget() == null) {
						puffBug.setTarget(target);
					}
				} else {
					puffBug.setTarget(breaker);
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
		for (HiveOccupantData occupents : this.hiveOccupants) {
			if (occupents.occupant == uuid) {
				return occupents;
			}
		}
		return null;
	}

	public void setBeingTeleportedToBy(PuffBugEntity puffbug, Direction side) {
		HiveOccupantData occupentData = this.getOccupentByUUID(puffbug.getUUID());
		if (occupentData != null) {
			occupentData.teleportSide = side;
			this.ticksTillResetTeleport = 250;
			this.shouldReset = true;
		}
	}

	public boolean isSideBeingTeleportedTo(Direction side) {
		for (HiveOccupantData occupents : this.hiveOccupants) {
			if (occupents.teleportSide == side) {
				return true;
			}
		}
		return false;
	}

	@Override
	@Nonnull
	public CompoundTag save(CompoundTag compound) {
		compound.put("HiveOccupants", HiveOccupantData.createCompoundList(this));

		compound.putInt("TeleportCooldown", this.teleportCooldown);

		return super.save(compound);
	}

	@Override
	public void load(BlockState state, CompoundTag compound) {
		this.hiveOccupants.clear();
		ListTag Occupants = compound.getList("HiveOccupants", 10);

		for (int i = 0; i < Occupants.size(); i++) {
			CompoundTag Occupant = Occupants.getCompound(i);
			String OccupantUUID = Occupant.contains("OccupantUUID", 8) ? Occupant.getString("OccupantUUID") : "";

			UUID foundUUID = !OccupantUUID.isEmpty() ? UUID.fromString(OccupantUUID) : null;
			this.hiveOccupants.add(new HiveOccupantData(foundUUID));
		}

		this.teleportCooldown = compound.getInt("TeleportCooldown");

		super.load(state, compound);
	}

	@Nullable
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return new ClientboundBlockEntityDataPacket(this.worldPosition, 9, this.getUpdateTag());
	}

	@Nonnull
	@Override
	public CompoundTag getUpdateTag() {
		return this.save(new CompoundTag());
	}

	public boolean onlyOpCanSetNbt() {
		return true;
	}

	@Override
	public AABB getRenderBoundingBox() {
		return super.getRenderBoundingBox().inflate(1084);
	}

	@Override
	public double getViewDistance() {
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

		public void tick(Level world) {
			if (this.getOccupant(world) == null) {
				this.occupant = null;
			}
		}

		@Nullable
		public PuffBugEntity getOccupant(Level world) {
			if (!world.isClientSide) {
				Entity entity = ((ServerLevel) world).getEntity(this.occupant);
				if (entity instanceof PuffBugEntity) {
					return (PuffBugEntity) entity;
				}
			}
			return null;
		}

		public static ListTag createCompoundList(PuffBugHiveTileEntity hive) {
			ListTag listnbt = new ListTag();

			for (HiveOccupantData occuptentData : hive.hiveOccupants) {
				CompoundTag compound = new CompoundTag();

				if (occuptentData.occupant == null) {
					compound.putString("OccupantUUID", "");
				} else {
					compound.putString("OccupantUUID", occuptentData.occupant.toString());
				}

				listnbt.add(compound);
			}

			return listnbt;
		}

		public static boolean isHiveSideEmpty(PuffBugHiveTileEntity hive, Direction direction) {
			for (int i = 0; i < hive.getTotalBugsInHive(); i++) {
				PuffBugEntity bug = hive.getHiveOccupants().get(i).getOccupant(hive.level);
				if (bug != null && bug.getAttachedHiveSide() == direction) {
					return false;
				}
			}
			return true;
		}
	}
}