package com.minecraftabnormals.endergetic.common.entities.eetle;

import com.minecraftabnormals.endergetic.client.particles.EEParticles;
import com.minecraftabnormals.endergetic.common.blocks.EetleEggsBlock;
import com.minecraftabnormals.endergetic.common.tileentities.EetleEggsTileEntity;
import com.minecraftabnormals.endergetic.core.registry.EEBlocks;
import com.minecraftabnormals.endergetic.core.registry.EEEntities;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FallingBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MoverType;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.DirectionalPlaceContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tags.FluidTags;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.Random;

public class EetleEggsEntity extends Entity implements IEntityAdditionalSpawnData {
	private static final Block EETLE_EGGS_BLOCK = EEBlocks.EETLE_EGGS.get();
	private static final Direction[] DIRECTIONS = Direction.values();
	private final EetleEggsTileEntity.SackGrowth[] sackGrowths = new EetleEggsTileEntity.SackGrowth[]{
			new EetleEggsTileEntity.SackGrowth(),
			new EetleEggsTileEntity.SackGrowth(),
			new EetleEggsTileEntity.SackGrowth(),
			new EetleEggsTileEntity.SackGrowth()
	};
	private EggSize eggSize = EggSize.SMALL;
	private int fallTime;
	private boolean fromBroodEetle;

	public EetleEggsEntity(EntityType<? extends EetleEggsEntity> type, World world) {
		super(EEEntities.EETLE_EGGS.get(), world);
	}

	public EetleEggsEntity(World world, Vector3d pos) {
		super(EEEntities.EETLE_EGGS.get(), world);
		this.setPosition(this.prevPosX = pos.getX(), this.prevPosY = pos.getY(), this.prevPosZ = pos.getZ());
		this.fromBroodEetle = true;
	}

	public EetleEggsEntity(FMLPlayMessages.SpawnEntity spawnEntity, World world) {
		super(EEEntities.EETLE_EGGS.get(), world);
	}

	@Override
	protected void registerData() {
	}

	@Override
	public void tick() {
		this.prevPosX = this.getPosX();
		this.prevPosY = this.getPosY();
		this.prevPosZ = this.getPosZ();
		this.fallTime++;
		if (!this.hasNoGravity()) {
			this.setMotion(this.getMotion().add(0.0D, -0.04D, 0.0D));
		}

		this.move(MoverType.SELF, this.getMotion());

		World world = this.world;
		if (!world.isRemote) {
			BlockPos newPos = this.getPosition();
			if (!this.onGround && !world.getFluidState(newPos).isTagged(FluidTags.WATER)) {
				if (this.fallTime > 100 && (newPos.getY() < 1 || newPos.getY() > 256) || this.fallTime > 600) {
					burstOpenEgg(world, newPos, this.rand, this.eggSize.ordinal(), this.fromBroodEetle);
				}
			} else {
				BlockState state = world.getBlockState(newPos);
				this.setMotion(this.getMotion().mul(0.7D, -0.5D, 0.7D));
				this.remove();
				boolean flag3 = FallingBlock.canFallThrough(world.getBlockState(newPos.down()));
				BlockState placingState = EETLE_EGGS_BLOCK.getDefaultState().with(EetleEggsBlock.SIZE, this.eggSize.ordinal());
				boolean flag4 = placingState.isValidPosition(world, newPos) && !flag3;
				Random random = this.rand;
				if (state.isReplaceable(new DirectionalPlaceContext(world, newPos, Direction.DOWN, ItemStack.EMPTY, Direction.UP)) && flag4) {
					if (placingState.hasProperty(BlockStateProperties.WATERLOGGED) && world.getFluidState(newPos).getFluid() == Fluids.WATER) {
						placingState = placingState.with(BlockStateProperties.WATERLOGGED, true);
					}

					placingState = assignRandomDirection(world, placingState, random, newPos);
					if (world.setBlockState(newPos, placingState, 3)) {
						if (!placingState.get(BlockStateProperties.WATERLOGGED)) {
							TileEntity tileentity = world.getTileEntity(newPos);
							if (tileentity instanceof EetleEggsTileEntity) {
								EetleEggsTileEntity eetleEggsTileEntity = (EetleEggsTileEntity) tileentity;
								eetleEggsTileEntity.fromBroodEetle = this.fromBroodEetle;
								eetleEggsTileEntity.updateHatchDelay(world, random.nextInt(6) + 5);
								eetleEggsTileEntity.bypassSpawningGameRule();
							}
						}
					}
				} else {
					burstOpenEgg(world, newPos, random, this.eggSize.ordinal(), this.fromBroodEetle);
				}
			}
		} else {
			for (EetleEggsTileEntity.SackGrowth growth : this.sackGrowths) {
				growth.tick();
			}
		}

		this.setMotion(this.getMotion().scale(0.98D));
	}

	@Override
	public boolean onLivingFall(float distance, float damageMultiplier) {
		return false;
	}

	@Override
	protected void readAdditional(CompoundNBT compound) {
		this.fallTime = compound.getInt("FallTime");
		this.eggSize = EggSize.getById(Math.min(2, compound.getInt("EggSize")));
		this.fromBroodEetle = compound.getBoolean("FromBroodEetle");
	}

	@Override
	protected void writeAdditional(CompoundNBT compound) {
		compound.putInt("FallTime", this.fallTime);
		compound.putInt("EggSize", this.eggSize.ordinal());
		compound.putBoolean("FromBroodEetle", this.fromBroodEetle);
	}

	@Override
	protected boolean canTriggerWalking() {
		return false;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean canBeCollidedWith() {
		return !this.removed;
	}

	@Override
	public boolean canBeAttackedWithItem() {
		return false;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean canRenderOnFire() {
		return false;
	}

	@Override
	public boolean ignoreItemEntityData() {
		return true;
	}

	@Override
	public void writeSpawnData(PacketBuffer buffer) {
		buffer.writeInt(this.eggSize.ordinal());
	}

	@Override
	public void readSpawnData(PacketBuffer buffer) {
		this.eggSize = EggSize.getById(buffer.readInt());
	}

	@Override
	public IPacket<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	public EetleEggsTileEntity.SackGrowth[] getSackGrowths() {
		return this.sackGrowths;
	}

	public void setEggSize(EggSize eggSize) {
		this.eggSize = eggSize;
	}

	public EggSize getEggSize() {
		return this.eggSize;
	}

	private static void burstOpenEgg(World world, BlockPos pos, Random random, int size, boolean fromBroodEetle) {
		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();
		for (int i = 0; i <= size; i++) {
			AbstractEetleEntity eetle = random.nextFloat() < 0.6F ? EEEntities.CHARGER_EETLE.get().create(world) : EEEntities.GLIDER_EETLE.get().create(world);
			if (eetle != null) {
				eetle.markFromEgg();
				eetle.updateAge(-(random.nextInt(41) + 120));
				eetle.setPositionAndRotation(x + random.nextFloat(), y + 0.1F, z + random.nextFloat(), random.nextFloat() * 360.0F, 0.0F);
				if (fromBroodEetle) {
					eetle.applyDespawnTimer();
				}
				world.addEntity(eetle);
			}
		}
		if (world instanceof ServerWorld) {
			((ServerWorld) world).spawnParticle(EEParticles.EETLE_CROWN.get(), x + 0.5F, y + 0.25F * (size + 1.0F), z + 0.5F, 5 + size, 0.3F, 0.1F, 0.3F, 0.1D);
		}
	}

	private static BlockState assignRandomDirection(World world, BlockState state, Random random, BlockPos pos) {
		EetleEggsBlock.shuffleDirections(DIRECTIONS, random);
		for (Direction direction : DIRECTIONS) {
			BlockState directionState = state.with(EetleEggsBlock.FACING, direction);
			if (directionState.isValidPosition(world, pos)) {
				return directionState;
			}
		}
		return state;
	}

	public enum EggSize {
		SMALL,
		MEDIUM,
		LARGE;

		private static final EggSize[] VALUES = values();

		public static EggSize random(Random random, boolean biased) {
			return biased ? (random.nextFloat() < 0.6F ? SMALL : VALUES[random.nextInt(VALUES.length)]) : VALUES[random.nextInt(VALUES.length)];
		}

		public static EggSize getById(int id) {
			return VALUES[id];
		}
	}
}
