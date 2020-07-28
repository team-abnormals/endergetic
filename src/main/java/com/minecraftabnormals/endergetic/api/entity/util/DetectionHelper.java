package com.minecraftabnormals.endergetic.api.entity.util;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.AxisAlignedBB;

public class DetectionHelper {
	
	public static AxisAlignedBB expandDownwards(AxisAlignedBB bb, double y) {
		double d0 = bb.minX;
		double d1 = bb.minY - y;
		double d2 = bb.minZ;
		double d3 = bb.maxX;
		double d4 = bb.maxY;
		double d5 = bb.maxZ;
		return new AxisAlignedBB(d0, d1, d2, d3, d4, d5);
	}
	
	public static AxisAlignedBB checkOnGround(AxisAlignedBB bb) {
		double d0 = bb.minX;
		double d1 = bb.minY - 0.07F;
		double d2 = bb.minZ;
		double d3 = bb.maxX;
		double d4 = bb.maxY - 1.0F;
		double d5 = bb.maxZ;
		return new AxisAlignedBB(d0, d1, d2, d3, d4, d5);
	}
	
	@Nullable
	public static <E extends LivingEntity> E getClosestEntity(List<? extends E> list, double posX, double posY, double posZ) {
		double d0 = -1.0D;
		E entity = null;

		for(E entities : list) {
			double d1 = entities.getDistanceSq(posX, posY, posZ);
			if(d0 == -1.0D || d1 < d0) {
				d0 = d1;
				entity = entities;
			}
		}

	 	return entity;
	}

}