package endergeticexpansion.common.tileentities;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import endergeticexpansion.common.entities.EntityPuffBug;
import endergeticexpansion.core.registry.EEEntities;
import endergeticexpansion.core.registry.EETileEntities;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraftforge.common.util.Constants;

import java.util.ArrayList;
import java.util.List;

public class TileEntityPuffBugHive extends TileEntity {
	private List<CompoundNBT> bugsInHive = new ArrayList<>();

	public TileEntityPuffBugHive() {
		super(EETileEntities.PUFFBUG_HIVE.get());
	}
	
	public void insertBug(EntityPuffBug entity) {
		bugsInHive.add(entity.serializeNBT());
		entity.remove();
	}
	
	public List<EntityPuffBug> exportBugs(int amount) {
		List<EntityPuffBug> list = new ArrayList<>();
		if (world != null) {
			for (int i = 0; i < amount && getBugsInHive() > 0; i++) {
				EntityPuffBug entity = EEEntities.PUFF_BUG.create(world);
				if (entity != null) {
					entity.deserializeNBT(bugsInHive.remove(i));
					Direction face = Direction.random(world.rand);
					BlockPos offset = pos.offset(face);
					byte tries = 0;
					while (world.getBlockState(offset).getCollisionShape(world, offset) == VoxelShapes.empty()) {
						face = Direction.random(world.rand);
						offset = pos.offset(face);
						if (++tries >= 16) {
							for (EntityPuffBug puffBug : list) puffBug.remove();
							list.clear();
							return list;
						}
					}
					entity.setPositionAndUpdate(offset.getX() + 0.5, offset.getY() + 0.5, offset.getZ() + 0.5);
					world.addEntity(entity);
					list.add(entity);
				}
			}
		}
		return list;
	}
	
	public int getBugsInHive() {
		return bugsInHive.size();
	}
	
	public boolean isHiveFull() {
		return getBugsInHive() >= 5;
	}
	
	@Override
	@Nonnull
	public CompoundNBT write(CompoundNBT compound) {
		ListNBT list = new ListNBT();
		list.addAll(bugsInHive);
		compound.put("bugs", list);
		return super.write(compound);
	}
	
	@Override
	public void read(CompoundNBT compound) {
		ListNBT bugs = compound.getList("bugs", Constants.NBT.TAG_LIST);
		for (int i = 0; i < bugs.size(); i++) bugsInHive.add(bugs.getCompound(i));
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
}
