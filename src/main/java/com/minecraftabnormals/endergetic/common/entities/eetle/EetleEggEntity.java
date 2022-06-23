package com.minecraftabnormals.endergetic.common.entities.eetle;

import com.minecraftabnormals.endergetic.client.particles.EEParticles;
import com.minecraftabnormals.endergetic.client.particles.data.CorrockCrownParticleData;
import com.minecraftabnormals.endergetic.common.blocks.EetleEggBlock;
import com.minecraftabnormals.endergetic.common.tileentities.EetleEggTileEntity;
import com.minecraftabnormals.endergetic.core.registry.EEBlocks;
import com.minecraftabnormals.endergetic.core.registry.EEEntities;
import com.minecraftabnormals.endergetic.core.registry.EESounds;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.item.context.DirectionalPlaceContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.Random;

public class EetleEggEntity extends Entity implements IEntityAdditionalSpawnData {
	private static final Block EETLE_EGGS_BLOCK = EEBlocks.EETLE_EGG.get();
	private static final Direction[] DIRECTIONS = Direction.values();
	private final EetleEggTileEntity.SackGrowth[] sackGrowths = new EetleEggTileEntity.SackGrowth[]{
			new EetleEggTileEntity.SackGrowth(),
			new EetleEggTileEntity.SackGrowth(),
			new EetleEggTileEntity.SackGrowth(),
			new EetleEggTileEntity.SackGrowth()
	};
	private EggSize eggSize = EggSize.SMALL;
	private int fallTime;
	private boolean fromBroodEetle;

	public EetleEggEntity(EntityType<? extends EetleEggEntity> type, Level world) {
		super(EEEntities.EETLE_EGG.get(), world);
	}

	public EetleEggEntity(Level world, Vec3 pos) {
		super(EEEntities.EETLE_EGG.get(), world);
		this.setPos(this.xo = pos.x(), this.yo = pos.y(), this.zo = pos.z());
		this.fromBroodEetle = true;
	}

	public EetleEggEntity(FMLPlayMessages.SpawnEntity spawnEntity, Level world) {
		super(EEEntities.EETLE_EGG.get(), world);
	}

	@Override
	protected void defineSynchedData() {
	}

	@Override
	public void tick() {
		this.xo = this.getX();
		this.yo = this.getY();
		this.zo = this.getZ();
		this.fallTime++;
		if (!this.isNoGravity()) {
			this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.04D, 0.0D));
		}

		this.move(MoverType.SELF, this.getDeltaMovement());

		Level world = this.level;
		if (!world.isClientSide) {
			BlockPos newPos = this.blockPosition();
			if (!this.onGround && !world.getFluidState(newPos).is(FluidTags.WATER)) {
				if (this.fallTime > 100 && (newPos.getY() < 1 || newPos.getY() > 256) || this.fallTime > 600) {
					burstOpenEgg(world, newPos, this.random, this.eggSize.ordinal(), this.fromBroodEetle);
				}
			} else {
				BlockState state = world.getBlockState(newPos);
				this.setDeltaMovement(this.getDeltaMovement().multiply(0.7D, -0.5D, 0.7D));
				this.remove();
				boolean flag3 = FallingBlock.isFree(world.getBlockState(newPos.below()));
				BlockState placingState = EETLE_EGGS_BLOCK.defaultBlockState().setValue(EetleEggBlock.SIZE, this.eggSize.ordinal());
				boolean flag4 = placingState.canSurvive(world, newPos) && !flag3;
				Random random = this.random;
				if (state.canBeReplaced(new DirectionalPlaceContext(world, newPos, Direction.DOWN, ItemStack.EMPTY, Direction.UP)) && flag4) {
					if (placingState.hasProperty(BlockStateProperties.WATERLOGGED) && world.getFluidState(newPos).getType() == Fluids.WATER) {
						placingState = placingState.setValue(BlockStateProperties.WATERLOGGED, true);
					}

					placingState = assignRandomDirection(world, placingState, random, newPos);
					if (world.setBlock(newPos, placingState, 3)) {
						world.playSound(null, newPos, EESounds.EETLE_EGG_PLACE.get(), SoundSource.BLOCKS, 1.0F - random.nextFloat() * 0.1F, 0.8F + random.nextFloat() * 0.2F);
						if (!placingState.getValue(BlockStateProperties.WATERLOGGED)) {
							BlockEntity tileentity = world.getBlockEntity(newPos);
							if (tileentity instanceof EetleEggTileEntity) {
								EetleEggTileEntity eetleEggsTileEntity = (EetleEggTileEntity) tileentity;
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
			for (EetleEggTileEntity.SackGrowth growth : this.sackGrowths) {
				growth.tick();
			}
		}

		this.setDeltaMovement(this.getDeltaMovement().scale(0.98D));
	}

	@Override
	public boolean causeFallDamage(float distance, float damageMultiplier) {
		return false;
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag compound) {
		this.fallTime = compound.getInt("FallTime");
		this.eggSize = EggSize.getById(Math.min(2, compound.getInt("EggSize")));
		this.fromBroodEetle = compound.getBoolean("FromBroodEetle");
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag compound) {
		compound.putInt("FallTime", this.fallTime);
		compound.putInt("EggSize", this.eggSize.ordinal());
		compound.putBoolean("FromBroodEetle", this.fromBroodEetle);
	}

	@Override
	protected boolean isMovementNoisy() {
		return false;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean isPickable() {
		return !this.removed;
	}

	@Override
	public boolean isAttackable() {
		return false;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean displayFireAnimation() {
		return false;
	}

	@Override
	public boolean onlyOpCanSetNbt() {
		return true;
	}

	@Override
	public void writeSpawnData(FriendlyByteBuf buffer) {
		buffer.writeInt(this.eggSize.ordinal());
	}

	@Override
	public void readSpawnData(FriendlyByteBuf buffer) {
		this.eggSize = EggSize.getById(buffer.readInt());
	}

	@Override
	public Packet<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	public EetleEggTileEntity.SackGrowth[] getSackGrowths() {
		return this.sackGrowths;
	}

	public void setEggSize(EggSize eggSize) {
		this.eggSize = eggSize;
	}

	public EggSize getEggSize() {
		return this.eggSize;
	}

	private static void burstOpenEgg(Level world, BlockPos pos, Random random, int size, boolean fromBroodEetle) {
		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();
		for (int i = 0; i <= size; i++) {
			AbstractEetleEntity eetle = random.nextFloat() < 0.6F ? EEEntities.CHARGER_EETLE.get().create(world) : EEEntities.GLIDER_EETLE.get().create(world);
			if (eetle != null) {
				eetle.markFromEgg();
				eetle.updateAge(-(random.nextInt(41) + 120));
				eetle.absMoveTo(x + random.nextFloat(), y + 0.1F, z + random.nextFloat(), random.nextFloat() * 360.0F, 0.0F);
				if (fromBroodEetle) {
					eetle.applyDespawnTimer();
				}
				world.addFreshEntity(eetle);
			}
		}
		if (world instanceof ServerLevel) {
			world.playSound(null, pos, EESounds.EETLE_EGG_BREAK.get(), SoundSource.BLOCKS, 1.0F - random.nextFloat() * 0.1F, 0.8F + random.nextFloat() * 0.2F);
			((ServerLevel) world).sendParticles(new CorrockCrownParticleData(EEParticles.END_CROWN.get(), true), x + 0.5F, y + 0.25F * (size + 1.0F), z + 0.5F, 5 + size, 0.3F, 0.1F, 0.3F, 0.1D);
		}
	}

	private static BlockState assignRandomDirection(Level world, BlockState state, Random random, BlockPos pos) {
		EetleEggBlock.shuffleDirections(DIRECTIONS, random);
		for (Direction direction : DIRECTIONS) {
			BlockState directionState = state.setValue(EetleEggBlock.FACING, direction);
			if (directionState.canSurvive(world, pos)) {
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
