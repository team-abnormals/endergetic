package com.minecraftabnormals.endergetic.common.tileentities;

import java.util.EnumMap;
import java.util.Map.Entry;
import java.util.Random;
import java.util.UUID;

import javax.annotation.Nullable;

import com.minecraftabnormals.endergetic.api.util.StringUtils;
import com.minecraftabnormals.endergetic.common.blocks.poise.BolloomBudBlock;
import com.minecraftabnormals.endergetic.common.entities.bolloom.BolloomFruitEntity;
import com.minecraftabnormals.endergetic.common.entities.puffbug.PuffBugEntity;
import com.minecraftabnormals.endergetic.core.registry.EEBlocks;
import com.minecraftabnormals.endergetic.core.registry.EETileEntities;

import com.teamabnormals.blueprint.core.endimator.TimedEndimation;
import com.teamabnormals.blueprint.core.util.MathUtil;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.Entity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.Util;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;

public class BolloomBudTileEntity extends BlockEntity {
	public final TimedEndimation pedalAnimation = new TimedEndimation(20, 20);
	private EnumMap<BudSide, SideData> sideData = Util.make(new EnumMap<>(BudSide.class), (side) -> {
		side.put(BudSide.NORTH, new SideData());
		side.put(BudSide.EAST, new SideData());
		side.put(BudSide.SOUTH, new SideData());
		side.put(BudSide.WEST, new SideData());
	});
	private int maxFruitHeight = 7;
	private UUID teleportingBug;

	public BolloomBudTileEntity(BlockPos pos, BlockState state) {
		super(EETileEntities.BOLLOOM_BUD.get(), pos, state);
	}

	@Override
	public AABB getRenderBoundingBox() {
		return super.getRenderBoundingBox().inflate(1.0F);
	}

	public static void tick(Level level, BlockPos pos, BlockState state, BolloomBudTileEntity bud) {
		Random rand = new Random();

		bud.sideData.forEach((side, sideData) -> {
			if (sideData.growTimer > 0 && sideData.growing) sideData.growTimer--;

			if (sideData.growing && sideData.growTimer <= 0) {
				if (!bud.level.isClientSide() && bud.getBlockState().getValue(BolloomBudBlock.OPENED)) {
					int height = rand.nextInt(bud.maxFruitHeight) + 1;
					BolloomFruitEntity fruit = new BolloomFruitEntity(bud.level, bud.worldPosition, side.offsetPosition(bud.worldPosition).above(height - 1), height, side.direction);
					bud.level.addFreshEntity(fruit);
					sideData.fruitUUID = fruit.getUUID();
				}
				sideData.growing = false;
				sideData.growTimer = 0;
			}
		});

		if (bud.getBlockState().getValue(BolloomBudBlock.OPENED)) {
			if (!bud.level.isClientSide() && bud.shouldShutBud() && rand.nextInt(200) == 0) {
				bud.level.setBlock(bud.worldPosition, bud.getBlockState().setValue(BolloomBudBlock.OPENED, false), 2);
				bud.resetGrowing();
			}
		}

		bud.pedalAnimation.tick();

		if (bud.level.isAreaLoaded(bud.worldPosition, 1)) {
			boolean opened = bud.getBlockState().getValue(BolloomBudBlock.OPENED);
			if (bud.pedalAnimation.isDecrementing() != opened) {
				bud.pedalAnimation.setDecrementing(opened);
			}
		}

		if (!bud.level.isClientSide) {
			if (bud.teleportingBug != null) {
				Entity entity = ((ServerLevel) bud.level).getEntity(bud.teleportingBug);
				if (entity != null && !entity.isAlive()) {
					bud.teleportingBug = null;
				} else if (entity == null) {
					bud.teleportingBug = null;
				}
			}
		}
	}

	public void startGrowing(Random rand, int maxHeight, boolean instant) {
		boolean didOneGrow = false;
		this.maxFruitHeight = maxHeight;

		if (instant) {
			this.pedalAnimation.setTick(0);
		}

		for (Entry<BudSide, SideData> data : this.sideData.entrySet()) {
			if (rand.nextBoolean()) {
				data.getValue().growing = true;
				data.getValue().growTimer = instant ? 0 : rand.nextInt(220) + 60;
				didOneGrow = true;
			}
		}

		if (!didOneGrow) {
			SideData sideData = this.sideData.get(BudSide.random(rand));
			sideData.growing = true;
			sideData.growTimer = instant ? 0 : rand.nextInt(220) + 60;
		}
	}

	public void resetGrowing() {
		this.sideData.forEach((side, sideData) -> {
			sideData.fruitUUID = null;
			sideData.growing = false;
			sideData.growTimer = 0;
		});
		this.maxFruitHeight = 7;
	}

	public void setTeleportingBug(@Nullable PuffBugEntity puffbug) {
		this.teleportingBug = puffbug != null ? puffbug.getUUID() : null;
	}

	public boolean hasTeleportingBug() {
		return this.teleportingBug != null;
	}

	public boolean canBeOpened() {
		Block block = EEBlocks.BOLLOOM_BUD.get();

		for (Direction directions : Direction.values()) {
			if (this.level.getBlockState(this.worldPosition.relative(directions, 2)).getBlock() == block) {
				return false;
			}
		}

		BlockPos north = this.worldPosition.relative(Direction.NORTH);
		BlockPos south = this.worldPosition.relative(Direction.SOUTH);

		if (this.level.getBlockState(north.east()).getBlock() == block || this.level.getBlockState(south.east()).getBlock() == block || this.level.getBlockState(north.west()).getBlock() == block || this.level.getBlockState(south.west()).getBlock() == block) {
			return false;
		}

		for (BudSide sides : BudSide.values()) {
			BlockPos sidePos = sides.offsetPosition(this.worldPosition);
			if (!this.level.getFluidState(sidePos).isEmpty() || !this.level.getBlockState(sidePos).getCollisionShape(this.level, sidePos).isEmpty()) {
				return false;
			}
		}

		return !this.getBlockState().getValue(BolloomBudBlock.OPENED) && this.calculateFruitMaxHeight() >= 3;
	}

	@Override
	public void load(CompoundTag tag) {
		super.load(tag);

		this.maxFruitHeight = tag.contains("MaxFruitHeight") ? Mth.clamp(tag.getInt("MaxFruitHeight"), 1, 7) : 7;

		this.sideData.forEach((side, sideData) -> {
			String sideName = StringUtils.capitaliseFirstLetter(side.direction.toString());
			String sideUUID = tag.contains(sideName + "FruitUUID", 8) ? tag.getString(sideName + "FruitUUID") : "";

			sideData.fruitUUID = !sideUUID.isEmpty() ? UUID.fromString(sideUUID) : null;

			sideData.growing = tag.getBoolean("Is" + sideName + "Growing");
			sideData.growTimer = tag.getInt(sideName + "GrowTime");
		});

		this.pedalAnimation.read(tag);
	}

	@Override
	protected void saveAdditional(CompoundTag compound) {
		super.saveAdditional(compound);

		if (compound.contains("MaxFruitHeight")) {
			compound.putInt("MaxFruitHeight", this.maxFruitHeight);
		}

		this.sideData.forEach((side, sideData) -> {
			String sideName = StringUtils.capitaliseFirstLetter(side.direction.toString());

			if (sideData.fruitUUID == null) {
				compound.putString(sideName + "FruitUUID", "");
			} else {
				compound.putString(sideName + "FruitUUID", sideData.fruitUUID.toString());
			}

			compound.putBoolean("Is" + sideName + "Growing", sideData.growing);
			compound.putInt(sideName + "GrowTime", sideData.growTimer);
		});

		this.pedalAnimation.write(compound);
	}

	@Nullable
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public CompoundTag getUpdateTag() {
		return this.saveWithoutMetadata();
	}

	@Override
	public boolean onlyOpCanSetNbt() {
		return true;
	}

	private boolean shouldShutBud() {
		boolean hasAFruit = false;
		for (Entry<BudSide, SideData> data : this.sideData.entrySet()) {
			if (data.getValue().hasFruit(this.level)) {
				hasAFruit = true;
			}
		}
		return !hasAFruit;
	}

	public int calculateFruitMaxHeight() {
		int[] maxHeights = new int[4];

		for (BudSide sides : BudSide.values()) {
			for (int y = 1; y < 7; y++) {
				if (this.level.isEmptyBlock(sides.offsetPosition(this.worldPosition.above(y)))) {
					maxHeights[sides.id] = y;
				} else {
					break;
				}
			}
		}

		return MathUtil.getLowestValueInIntArray(maxHeights);
	}

	public enum BudSide {
		NORTH(Direction.NORTH, 0),
		EAST(Direction.EAST, 1),
		SOUTH(Direction.SOUTH, 2),
		WEST(Direction.WEST, 3);

		private final Direction direction;
		public final int id;

		BudSide(Direction direction, int id) {
			this.direction = direction;
			this.id = id;
		}

		public BlockPos offsetPosition(BlockPos pos) {
			return pos.relative(this.direction);
		}

		public static BudSide random(Random rand) {
			return values()[rand.nextInt(values().length)];
		}
	}

	static class SideData {
		@Nullable
		private UUID fruitUUID;
		private int growTimer;
		private boolean growing;

		public BolloomFruitEntity getFruit(Level world) {
			if (!world.isClientSide() && this.fruitUUID != null) {
				Entity entity = ((ServerLevel) world).getEntity(this.fruitUUID);
				if (entity instanceof BolloomFruitEntity) {
					return (BolloomFruitEntity) entity;
				}
			}
			return null;
		}

		public boolean hasFruit(Level world) {
			if (this.growing) {
				return true;
			}
			return this.getFruit(world) != null && this.getFruit(world).isAlive() && !this.getFruit(world).isUntied();
		}
	}
}