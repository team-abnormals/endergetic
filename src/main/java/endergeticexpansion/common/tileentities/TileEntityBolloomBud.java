package endergeticexpansion.common.tileentities;

import java.util.EnumMap;
import java.util.Map.Entry;
import java.util.Random;
import java.util.UUID;

import javax.annotation.Nullable;

import endergeticexpansion.api.endimator.ControlledEndimation;
import endergeticexpansion.api.util.StringUtils;
import endergeticexpansion.common.blocks.poise.BlockBolloomBud;
import endergeticexpansion.common.entities.bolloom.EntityBolloomFruit;
import endergeticexpansion.core.registry.EETileEntities;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Util;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class TileEntityBolloomBud extends TileEntity implements ITickableTileEntity {
	public final ControlledEndimation PEDAL_PROGRESS = new ControlledEndimation(20, 20);
	private EnumMap<BudSide, SideData> sideData = Util.make(new EnumMap<>(BudSide.class), (side) -> {
		side.put(BudSide.NORTH, new SideData());
		side.put(BudSide.EAST, new SideData());
		side.put(BudSide.SOUTH, new SideData());
		side.put(BudSide.WEST, new SideData());
	});
	private boolean markedForSpawning;

	public TileEntityBolloomBud() {
		super(EETileEntities.BOLLOOM_BUD.get());
	}
	
	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		return super.getRenderBoundingBox().grow(1.0F);
	}
	
	@Override
	public double getMaxRenderDistanceSquared() {
		return super.getMaxRenderDistanceSquared() * 2;
	}
	
	@Override
	public void tick() {
		Random rand = new Random();
		
		if(this.isMarkedForSpawning()) {
			if(!this.world.isRemote) {
				this.markedForSpawning = false;
				this.startGrowing(true);
			}
		}
		
		this.sideData.forEach((side, sideData) -> {
			if(sideData.growTimer > 0 && sideData.growing) sideData.growTimer--;
			
			if(sideData.growing && sideData.growTimer <= 0) {
				if(!this.world.isRemote()) {
					int height = rand.nextInt(7) + 1;
					EntityBolloomFruit fruit = new EntityBolloomFruit(this.world, this.pos, side.offsetPosition(this.pos).up(height - 1), height, side.direction);
					this.world.addEntity(fruit);
					sideData.fruitUUID = fruit.getUniqueID();
				}
				sideData.growing = false;
				sideData.growTimer = 0;
			}
		});
		
		if(this.getBlockState().get(BlockBolloomBud.OPENED)) {
			if(!this.world.isRemote() && this.shouldShutBud() && rand.nextInt(200) == 0) {
				this.world.setBlockState(this.pos, this.getBlockState().with(BlockBolloomBud.OPENED, false), 2);
				this.resetGrowing();
			}
		}
		
		this.PEDAL_PROGRESS.update();
			
		this.PEDAL_PROGRESS.tick();
			
		if(this.world.isAreaLoaded(this.pos, 1)) {
			if(this.PEDAL_PROGRESS.isDescrementing() != this.getBlockState().get(BlockBolloomBud.OPENED)) {
				this.PEDAL_PROGRESS.setDecrementing(this.getBlockState().get(BlockBolloomBud.OPENED));
			}
		}
	}
	
	public void startGrowing(boolean instant) {
		boolean didOneGrow = false;
		
		for(Entry<BudSide, SideData> data : this.sideData.entrySet()) {
			if(this.world.rand.nextBoolean()) {
				data.getValue().growing = true;
				data.getValue().growTimer = instant ? 0 : this.world.rand.nextInt(220) + 60;
				didOneGrow = true;
			}
		}
		
		if(!didOneGrow) {
			SideData sideData = this.sideData.get(BudSide.random(this.world.rand));
			sideData.growing = true;
			sideData.growTimer = instant ? 0 : this.world.rand.nextInt(220) + 60;
		}
	}
	
	public void resetGrowing() {
		this.sideData.forEach((side, sideData) -> {
			sideData.fruitUUID = null;
			sideData.growing = false;
			sideData.growTimer = 0;
		});
	}
	
	@Override
	public void read(CompoundNBT compound) {
		super.read(compound);
		
		this.markedForSpawning = compound.getBoolean("MarkedForSpawning");
		
		this.sideData.forEach((side, sideData) -> {
			String sideName = StringUtils.capitaliseFirstLetter(side.direction.toString());
			String sideUUID = compound.contains(sideName + "FruitUUID", 8) ? compound.getString(sideName + "FruitUUID") : "";
			
			sideData.fruitUUID = !sideUUID.isEmpty() ? UUID.fromString(sideUUID) : null;
			
			sideData.growing = compound.getBoolean("Is" + sideName + "Growing");
			sideData.growTimer = compound.getInt(sideName + "GrowTime");
		});
		
		this.PEDAL_PROGRESS.read(compound);
	}
	
	@Override
	public CompoundNBT write(CompoundNBT compound) {
		super.write(compound);
		
		compound.putBoolean("MarkedForSpawning", this.isMarkedForSpawning());
		
		this.sideData.forEach((side, sideData) -> {
			String sideName = StringUtils.capitaliseFirstLetter(side.direction.toString());
			
			if(sideData.fruitUUID == null) {
				compound.putString(sideName + "FruitUUID", "");
			} else {
				compound.putString(sideName + "FruitUUID", sideData.fruitUUID.toString());
			}
			
			compound.putBoolean("Is" + sideName + "Growing", sideData.growing);
			compound.putInt(sideName + "GrowTime", sideData.growTimer);
		});
		
		this.PEDAL_PROGRESS.write(compound);
		
		return compound;
	}
	
	@Nullable
	public SUpdateTileEntityPacket getUpdatePacket() {
		return new SUpdateTileEntityPacket(this.pos, 100, this.getUpdateTag());
	}
	
	public CompoundNBT getUpdateTag() {
		return this.write(new CompoundNBT());
	}
	
	@Override
	public boolean onlyOpsCanSetNbt() {
		return true;
	}

	public boolean isMarkedForSpawning() {
		return this.markedForSpawning;
	}
	
	public void markForSpawning() {
		this.markedForSpawning = true;
	}
	
	private boolean shouldShutBud() {
		boolean hasAFruit = false;
		for(Entry<BudSide, SideData> data : this.sideData.entrySet()) {
			if(data.getValue().hasFruit(this.world)) {
				hasAFruit = true;
			}
		}
		return !hasAFruit;
	}
	
	enum BudSide {
		NORTH(Direction.NORTH),
		EAST(Direction.EAST),
		SOUTH(Direction.SOUTH),
		WEST(Direction.WEST);
		
		private final Direction direction;
		
		BudSide(Direction direction) {
			this.direction = direction;
		}
		
		public BlockPos offsetPosition(BlockPos pos) {
			return pos.offset(this.direction);
		}
		
		public static BudSide random(Random rand) {
			return values()[rand.nextInt(values().length)];
		}
	}
	
	class SideData {
		@Nullable
		private UUID fruitUUID;
		private int growTimer;
		private boolean growing;
		
		public EntityBolloomFruit getFruit(World world) {
			if(!world.isRemote()) {
				Entity entity = ((ServerWorld) world).getEntityByUuid(this.fruitUUID);
				if(entity instanceof EntityBolloomFruit) {
					return (EntityBolloomFruit) entity;
				}
			}
			return null;
		}
		
		public boolean hasFruit(World world) {
			if(this.growing) {
				return true;
			}
			return this.getFruit(world) != null && this.getFruit(world).isAlive() && !this.getFruit(world).isUntied();
		}
	}
}