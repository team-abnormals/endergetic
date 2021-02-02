package com.minecraftabnormals.endergetic.common.world.features.corrock;

import com.minecraftabnormals.endergetic.api.util.GenerationUtils;
import com.minecraftabnormals.endergetic.common.blocks.CorrockCrownStandingBlock;
import com.minecraftabnormals.endergetic.common.world.configs.CorrockArchConfig;
import com.minecraftabnormals.endergetic.core.registry.EEBlocks;
import com.mojang.serialization.Codec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;

import java.util.*;

public class CorrockArchFeature extends AbstractCorrockFeature<CorrockArchConfig> {

	public CorrockArchFeature(Codec<CorrockArchConfig> configFactory) {
		super(configFactory);
	}

	@Override
	public boolean generate(ISeedReader world, ChunkGenerator generator, Random rand, BlockPos pos, CorrockArchConfig config) {
		Block below = world.getBlockState(pos.down()).getBlock();
		if ((below == CORROCK_BLOCK_BLOCK || below == EEBlocks.EUMUS.get()) && world.isAirBlock(pos)) {
			int originX = pos.getX();
			int originY = pos.getY();
			int originZ = pos.getZ();
			if (isGroundSolid(world, originX, originY - 1, originZ)) {
				BlockPos.Mutable mutable = pos.toMutable();
				BlockPos end = null;
				float maxDistance = config.getMaxDistance();
				int maxDistanceInt = (int) maxDistance;
				float minDistance = config.getMinDistance();
				double distance = 0.0F;
				for (int i = 0; i < 6; i++) {
					int xPos = originX + rand.nextInt(maxDistanceInt) - rand.nextInt(maxDistanceInt);
					int zPos = originZ + rand.nextInt(maxDistanceInt) - rand.nextInt(maxDistanceInt);
					mutable.setPos(xPos, originY, zPos);
					mutable.setY(world.getHeight(Heightmap.Type.WORLD_SURFACE_WG, xPos, zPos));
					below = world.getBlockState(mutable.down()).getBlock();
					if ((below == CORROCK_BLOCK_BLOCK || below == EEBlocks.EUMUS.get() || below == Blocks.END_STONE) && isGroundSolid(world, xPos, mutable.getY() - 1, zPos)) {
						distance = MathHelper.sqrt(mutable.distanceSq(pos));
						if (distance >= minDistance && distance <= maxDistance && isGroundSolid(world, xPos, mutable.getY() - 1, zPos) && world.isAirBlock(mutable)) {
							end = mutable.toImmutable();
							break;
						}
					}
				}
				if (end != null) {
					Set<BlockPos> corrockBlockPositions = new HashSet<>();
					ArchSpline archSpline = new ArchSpline(pos, end, rand, config.getMaxArchHeight());
					int steps = (int) (20 + distance * 4);
					BlockPos prevPos = null;
					int failedPositions = 0;
					int startY = pos.getY();
					int endY = end.getY();
					for (int i = 0; i < steps; i++) {
						float progress = i / (float) steps;
						BlockPos interpolatedPos = archSpline.interpolate(progress);
						if (interpolatedPos.equals(prevPos)) {
							continue;
						}
						prevPos = interpolatedPos;

						int radius = 2;
						double offsetRadius = radius - (rand.nextDouble() - 0.5D) * 0.25D;
						for (int y = -radius; y <= radius; y++) {
							for (int x = -radius; x <= radius; x++) {
								for (int z = -radius; z <= radius; z++) {
									BlockPos placingPos = interpolatedPos.add(x, y, z);
									if (MathHelper.sqrt(pos.distanceSq(placingPos)) <= 32.0D && x * x + y * y + z * z <= offsetRadius) {
										if (world.isAirBlock(placingPos)) {
											corrockBlockPositions.add(placingPos);
										} else {
											int placingPosY = placingPos.getY();
											if (placingPosY >= endY && placingPosY >= startY) {
												//If we've collided with too many blocks lets not generate
												if (failedPositions++ > 6) {
													return false;
												}
											}
										}
									}
								}
							}
						}
					}
					BlockState corrockBlockState = CORROCK_BLOCK_STATE.getValue();
					BlockState corrockState = EEBlocks.CORROCK_END.get().getDefaultState();
					float crownChance = config.getCrownChance();
					float plantChance = config.getPlantChance();
					corrockBlockPositions.forEach(corrockPos -> {
						world.setBlockState(corrockPos, corrockBlockState, 2);
						if (rand.nextFloat() < crownChance) {
							BlockPos up = corrockPos.up();
							if (!corrockBlockPositions.contains(up) && world.isAirBlock(up)) {
								world.setBlockState(up, getCorrockCrownStanding(rand.nextInt(16)), 2);
							} else {
								BlockPos down = corrockPos.down();
								if (!corrockBlockPositions.contains(down) && world.isAirBlock(down)) {
									world.setBlockState(down, getCorrockCrownStanding(rand.nextInt(16)).with(CorrockCrownStandingBlock.UPSIDE_DOWN, true), 2);
								} else {
									for (Direction horizontal : Direction.Plane.HORIZONTAL) {
										BlockPos offset = corrockPos.offset(horizontal);
										if (!corrockBlockPositions.contains(offset) && world.isAirBlock(offset)) {
											world.setBlockState(offset, getCorrockCrownWall(horizontal), 2);
										}
									}
								}
							}
						} else if (rand.nextFloat() < plantChance) {
							BlockPos up = corrockPos.up();
							if (!corrockBlockPositions.contains(up) && world.isAirBlock(up)) {
								world.setBlockState(up, corrockState, 2);
							}
						}
					});
					return true;
				}
			}
		}
		return false;
	}

	private static boolean isGroundSolid(ISeedReader world, int x, int y, int z) {
		return GenerationUtils.isAreaCompletelySolid(world, x - 2, y, z - 2, x + 2, y, z + 2);
	}

	/**
	 * Sourced from <a href=https://en.wikipedia.org/wiki/Centripetal_Catmull%E2%80%93Rom_spline>https://en.wikipedia.org/wiki/Centripetal_Catmull%E2%80%93Rom_spline</a>
	 * <p>Catmull Rom-Spline implementation that initializes points in an arch-like formation.</p>
	 *
	 * @author SmellyModder (Luke Tonon)
	 */
	static class ArchSpline {
		private final Vector3d[] points;

		private ArchSpline(BlockPos start, BlockPos end, Random rand, float maxArchHeight) {
			List<Vector3d> points = new ArrayList<>();
			Vector3d startVec = Vector3d.copy(start);
			Vector3d endVec = Vector3d.copy(end);
			Vector3d difference = endVec.subtract(startVec);
			Vector3d normalizedDifference = difference.normalize();
			//Anchor positions drive the spline downwards into the start and end positions
			Vector3d anchorStart = startVec.subtract(normalizedDifference).add(0, -6, 0);
			Vector3d anchorEnd = endVec.add(normalizedDifference).add(0, -6, 0);
			points.add(anchorStart);
			points.add(startVec);
			//Generate positions in between the start and end to form an arch-like path
			Vector3d offset = (new Vector3d(0, 1, 0)).crossProduct(normalizedDifference);
			double offsetX = offset.x;
			double offsetZ = offset.z;
			for (int i = 0; i < 5; i++) {
				points.add(startVec.add(difference.scale(i / 5.0F)).add(offsetX * (rand.nextDouble() - 0.5D) * 3.0D, (rand.nextDouble() - 0.25D) * 8.0D + MathHelper.sin((float) (i / 5.0F * Math.PI)) * maxArchHeight, offsetZ * (rand.nextDouble() - 0.5D) * 3.0D));
			}
			points.add(endVec);
			points.add(anchorEnd);
			this.points = points.toArray(new Vector3d[0]);
		}

		/**
		 * Gets a position on the spline based on a given progress percentage.
		 */
		private BlockPos interpolate(float progress) {
			Vector3d[] points = this.points;
			float sections = points.length - 3;
			int currentPoint = (int) Math.min(Math.floor(progress * sections), sections - 1);

			Vector3d point0 = points[currentPoint];
			Vector3d point1 = points[currentPoint + 1];
			float t0 = computeT(point0, point1, 0.0F);

			Vector3d point2 = points[currentPoint + 2];
			Vector3d point3 = points[currentPoint + 3];
			float t1 = computeT(point1, point2, t0);

			float t = t0 + (progress * sections - (float) currentPoint) * (t1 - t0);

			double A1multiplier1 = (t0 - t) / t0;
			double A1multiplier2 = t / t0;
			double A1x = multiplyPoints(point0.x, point1.x, A1multiplier1, A1multiplier2);
			double A1y = multiplyPoints(point0.y, point1.y, A1multiplier1, A1multiplier2);
			double A1z = multiplyPoints(point0.z, point1.z, A1multiplier1, A1multiplier2);

			double A2Denominator = t1 - t0;
			double A2Multiplier1 = (t1 - t) / A2Denominator;
			double A2Multiplier2 = (t - t0) / A2Denominator;
			double A2x = multiplyPoints(point1.x, point2.x, A2Multiplier1, A2Multiplier2);
			double A2y = multiplyPoints(point1.y, point2.y, A2Multiplier1, A2Multiplier2);
			double A2z = multiplyPoints(point1.z, point2.z, A2Multiplier1, A2Multiplier2);

			float t3 = computeT(point2, point3, t1);
			double A3Denominator = t3 - t1;
			double A3Multiplier1 = (t3 - t) / A3Denominator;
			double A3Multiplier2 = (t - t1) / A3Denominator;
			double A3x = multiplyPoints(point2.x, point3.x, A3Multiplier1, A3Multiplier2);
			double A3y = multiplyPoints(point2.y, point3.y, A3Multiplier1, A3Multiplier2);
			double A3z = multiplyPoints(point2.z, point3.z, A3Multiplier1, A3Multiplier2);

			double B1Multiplier1 = (t1 - t) / t1;
			double B1Multiplier2 = t / t1;
			double B1x = multiplyPoints(A1x, A2x, B1Multiplier1, B1Multiplier2);
			double B1y = multiplyPoints(A1y, A2y, B1Multiplier1, B1Multiplier2);
			double B1z = multiplyPoints(A1z, A2z, B1Multiplier1, B1Multiplier2);

			double B2Denominator = t3 - t0;
			double B2Multiplier1 = (t3 - t) / B2Denominator;
			double B2Multiplier2 = (t - t0) / B2Denominator;
			double B2x = multiplyPoints(A2x, A3x, B2Multiplier1, B2Multiplier2);
			double B2y = multiplyPoints(A2y, A3y, B2Multiplier1, B2Multiplier2);
			double B2z = multiplyPoints(A2z, A3z, B2Multiplier1, B2Multiplier2);

			double CDenominator = t1 - t0;
			double CMultiplier1 = (t1 - t) / CDenominator;
			double CMultiplier2 = (t - t0) / CDenominator;
			return new BlockPos(multiplyPoints(B1x, B2x, CMultiplier1, CMultiplier2), multiplyPoints(B1y, B2y, CMultiplier1, CMultiplier2), multiplyPoints(B1z, B2z, CMultiplier1, CMultiplier2));
		}

		private static double multiplyPoints(double point1, double point2, double multiplier1, double multiplier2) {
			return point1 * multiplier1 + point2 * multiplier2;
		}

		private static float computeT(Vector3d point1, Vector3d point2, float offset) {
			//Raising to power of 0.5 results in a centripetal Catmull–Rom spline, which is equivalent to taking the square root
			return MathHelper.sqrt(point2.subtract(point1).length()) + offset;
		}
	}
}
