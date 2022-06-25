package com.minecraftabnormals.endergetic.api.util;

import net.minecraft.client.model.geom.ModelPart;

public class ModelUtil {

	public static void setScale(ModelPart part, float x, float y, float z) {
		part.xScale = x;
		part.yScale = y;
		part.zScale = z;
	}

}
