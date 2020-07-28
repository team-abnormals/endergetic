package com.minecraftabnormals.endergetic.core.registry.other;

import java.util.Optional;

import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.IDataSerializer;
import net.minecraft.util.math.vector.Vector3d;

public class EEDataSerializers {
	public static final IDataSerializer<Optional<Vector3d>> OPTIONAL_VEC3D = new IDataSerializer<Optional<Vector3d>>() {
		public void write(PacketBuffer buf, Optional<Vector3d> value) {
			buf.writeBoolean(value.isPresent());
			
			if(value.isPresent()) {
				Vector3d vec3d = value.get();
				buf.writeDouble(vec3d.getX());
				buf.writeDouble(vec3d.getY());
				buf.writeDouble(vec3d.getZ());
			}
		}

		public Optional<Vector3d> read(PacketBuffer buf) {
			return !buf.readBoolean() ? Optional.empty() : Optional.of(new Vector3d(buf.readDouble(), buf.readDouble(), buf.readDouble()));
		}

		public Optional<Vector3d> copyValue(Optional<Vector3d> value) {
			return value;
		}
	};
	
	public static void registerSerializers() {
		DataSerializers.registerSerializer(OPTIONAL_VEC3D);
	}
}