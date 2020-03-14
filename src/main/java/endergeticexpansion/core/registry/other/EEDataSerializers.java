package endergeticexpansion.core.registry.other;

import java.util.Optional;

import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.IDataSerializer;
import net.minecraft.util.math.Vec3d;

public class EEDataSerializers {
	public static final IDataSerializer<Optional<Vec3d>> OPTIONAL_VEC3D = new IDataSerializer<Optional<Vec3d>>() {
		public void write(PacketBuffer buf, Optional<Vec3d> value) {
			buf.writeBoolean(value.isPresent());
			
			if(value.isPresent()) {
				Vec3d vec3d = value.get();
				buf.writeDouble(vec3d.getX());
				buf.writeDouble(vec3d.getY());
				buf.writeDouble(vec3d.getZ());
			}
		}

		public Optional<Vec3d> read(PacketBuffer buf) {
			return !buf.readBoolean() ? Optional.empty() : Optional.of(new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble()));
		}

		public Optional<Vec3d> copyValue(Optional<Vec3d> value) {
			return value;
		}
	};
	
	public static void registerSerializers() {
		DataSerializers.registerSerializer(OPTIONAL_VEC3D);
	}
}