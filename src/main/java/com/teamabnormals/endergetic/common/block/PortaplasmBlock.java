package com.teamabnormals.endergetic.common.block;

import com.teamabnormals.blueprint.core.util.MathUtil;
import com.teamabnormals.endergetic.client.particle.data.CorrockCrownParticleData;
import com.teamabnormals.endergetic.common.network.entity.S2CEnablePurpoidFlash;
import com.teamabnormals.endergetic.core.EndergeticExpansion;
import com.teamabnormals.endergetic.core.other.tags.EEEntityTypeTags;
import com.teamabnormals.endergetic.core.registry.EEParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.PacketDistributor;

import javax.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.POWERED;

public class PortaplasmBlock extends Block {
	private static final VoxelShape COLLISION_SHAPE = Block.box(0.01D, 0.0D, 0.01D, 15.99D, 16.0D, 15.99D);
	public static final IntegerProperty PHASE = IntegerProperty.create("phase", 0, 3);

	public PortaplasmBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.defaultBlockState().setValue(POWERED, false).setValue(PHASE, 0));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(POWERED, PHASE);
	}

	@Override
	@Nullable
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return this.defaultBlockState().setValue(POWERED, context.getLevel().hasNeighborSignal(context.getClickedPos()));
	}

	@SuppressWarnings("deprecation")
	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext context) {
		return COLLISION_SHAPE;
	}

	@Override
	public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, float amount) {
		if (entity.isSuppressingBounce()) {
			super.fallOn(level, state, pos, entity, amount);
		} else {
			entity.causeFallDamage(amount, 0.0F, level.damageSources().fall());
		}
	}

	@Override
	public void updateEntityAfterFallOn(BlockGetter p_56406_, Entity entity) {
		if (entity.isSuppressingBounce()) {
			super.updateEntityAfterFallOn(p_56406_, entity);
		} else {
			Vec3 vec3 = entity.getDeltaMovement();
			if (vec3.y < 0.0D) {
				double d0 = entity instanceof LivingEntity ? 0.2D : 0.1D;
				entity.setDeltaMovement(vec3.x, -vec3.y * d0, vec3.z);
			}
		}
	}

	@Override
	public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
		if (!entity.isSteppingCarefully()) {
			double d0 = Math.abs(entity.getDeltaMovement().y);
			if (d0 < 0.1D) {
				double d1 = 0.6D + d0 * 0.2D;
				entity.setDeltaMovement(entity.getDeltaMovement().multiply(d1, 1.0D, d1));
			}
			this.tryToStartTeleporting(level, state, pos);
		}

		super.stepOn(level, pos, state, entity);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
		this.tryToStartTeleporting(level, state, pos);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos p_55670_, boolean p_55671_) {
		if (!level.isClientSide) {
			boolean flag = state.getValue(POWERED);
			if (flag != level.hasNeighborSignal(pos)) {
				if (flag) {
					level.scheduleTick(pos, this, 1);
				} else {
					level.setBlock(pos, state.cycle(POWERED), 2);
				}
			}
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		if (state.getValue(POWERED) && !level.hasNeighborSignal(pos)) {
			level.setBlock(pos, state.cycle(POWERED), 2);
		}
		int phase = state.getValue(PHASE);
		if (phase == 1) {
			level.setBlock(pos, state.setValue(PHASE, 2), 2);
			level.scheduleTick(pos, this, 5);
		} else if (phase == 2) {
			level.setBlock(pos, state.setValue(PHASE, 3), 2);
			level.scheduleTick(pos, this, 2);
			var entities = level.getEntities((Entity) null, new AABB(pos).inflate(0.125D), entity -> {
				if (entity.getType().is(EEEntityTypeTags.PORTABLE)) return true;
				return !entity.getType().is(EEEntityTypeTags.TELEPORT_IMMUNE) && (entity instanceof LivingEntity livingEntity && livingEntity.isAlive());
			});
			if (entities.isEmpty()) return;
			double x = pos.getX() + 0.5D;
			double y = pos.getY() + 1.0D;
			double z = pos.getZ() + 0.5D;
			level.sendParticles(new CorrockCrownParticleData(EEParticleTypes.END_CROWN.get(), false, 0.1F), x + 0.5D, y + 0.5D, z + 0.5D, 6, 0.75F, 0.75F, 0.75F, 0.1D);
			var receivers = this.getReceivers(level, pos);
			Collections.shuffle(receivers);
			int maxReceiverIndex = receivers.size() - 1;
			if (maxReceiverIndex > -1) {
				int receiverIndex = maxReceiverIndex;
				nextEntity:
				for (int i = 0; i < entities.size(); i++, receiverIndex = maxReceiverIndex) {
					Entity entity = entities.get(i);
					Vec3 destination;
					if (!(entity instanceof LivingEntity livingEntity)) {
						destination = receivers.get(receiverIndex);
						entity.teleportTo(destination.x() + 0.5D, destination.y(), destination.z() + 0.5D);
						continue;
					}
					for (; receiverIndex > -1; receiverIndex--) {
						destination = receivers.get(receiverIndex);
						if (tryToTeleportLiving(livingEntity, destination.x() + 0.5D, destination.y(), destination.z() + 0.5D)) {
							continue nextEntity;
						}
					}
					for (int j = 0; j < 32; j++) {
						double randomX = x + (random.nextDouble() - 0.5D) * 32.0D;
						double randomY = y + (random.nextInt(33) - 16);
						double randomZ = z + (random.nextDouble() - 0.5D) * 32.0D;
						if (tryToTeleportLiving(livingEntity, randomX, randomY, randomZ)) break;
					}
				}
			} else {
				for (Entity entity : entities) {
					for (int j = 0; j < 32; j++) {
						double randomX = x + (random.nextDouble() - 0.5D) * 32.0D;
						double randomY = y + (random.nextInt(33) - 16);
						double randomZ = z + (random.nextDouble() - 0.5D) * 32.0D;
						if (entity instanceof LivingEntity livingEntity) {
							if (tryToTeleportLiving(livingEntity, randomX, randomY, randomZ)) break;
						} else {
							entity.teleportTo(randomX, randomY, randomZ);
						}
					}
				}
			}
		} else if (phase == 3) {
			level.setBlock(pos, state.setValue(PHASE, 0), 2);
		}
	}

	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
		if (state.getValue(PHASE) != 2) return;
		CorrockCrownParticleData particleData = new CorrockCrownParticleData(EEParticleTypes.END_CROWN.get(), false, 0.1F);
		double centerX = pos.getX() + 0.5D;
		double centerY = pos.getY() + 0.5D;
		double centerZ = pos.getZ() + 0.5D;
		for (int i = 0; i < 6; i++) {
			double dx = MathUtil.makeNegativeRandomly(0.5D + 0.25D * (random.nextDouble() - random.nextDouble()), random);
			double dy = MathUtil.makeNegativeRandomly(0.5D + 0.25D * (random.nextDouble() - random.nextDouble()), random);
			double dz = MathUtil.makeNegativeRandomly(0.5D + 0.25D * (random.nextDouble() - random.nextDouble()), random);
			level.addParticle(particleData,
				centerX + dx,
				centerY + dy,
				centerZ + dz,
				dx * 0.25D,
				dy * 0.25D,
				dz * 0.25D
			);
		}
	}

	@Override
	public boolean isRandomlyTicking(BlockState p_49921_) {
		return super.isRandomlyTicking(p_49921_);
	}

	private void tryToStartTeleporting(Level level, BlockState state, BlockPos pos) {
		if (state.getValue(PHASE) == 0) {
			level.setBlock(pos, state.setValue(PHASE, 1), 2);
			level.scheduleTick(pos, this, 2);
		}
	}

	private ArrayList<Vec3> getReceivers(Level level, BlockPos origin) {
		ArrayList<Vec3> receivers = new ArrayList<>();
		BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
		int originY = origin.getY();
		int originX = origin.getX();
		int originZ = origin.getZ();
		for (int y = -16; y <= 16; y++) {
			mutablePos.setY(originY + y);
			int ySquared = y * y;
			for (int x = -8; x <= 8; x++) {
				mutablePos.setX(originX + x);
				int xSquared = x * x;
				for (int z = -8; z <= 8; z++) {
					mutablePos.setZ(originZ + z);
					int distanceSq = ySquared + xSquared + z * z;
					if (!(distanceSq > 0 && distanceSq <= 256)) continue;
					BlockState state = level.getBlockState(mutablePos);
					if (!(state.is(this) && !state.getValue(POWERED))) continue;
					mutablePos.setY(mutablePos.getY() + 1);
					var shape = level.getBlockState(mutablePos).getCollisionShape(level, mutablePos);
					double elevation = 0.0D;
					if (shape.isEmpty() || (elevation = shape.bounds().maxY) < 1.0D) {
						receivers.add(new Vec3(mutablePos.getX(), mutablePos.getY() + elevation, mutablePos.getZ()));
					}
					mutablePos.setY(mutablePos.getY() - 1);
				}
			}
		}
		return receivers;
	}

	private static boolean tryToTeleportLiving(LivingEntity livingEntity, double randomX, double randomY, double randomZ) {
		if (livingEntity.randomTeleport(randomX, randomY, randomZ, false)) {
			if (livingEntity instanceof ServerPlayer player) {
				EndergeticExpansion.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new S2CEnablePurpoidFlash());
			}
			return true;
		}
		return false;
	}
}
