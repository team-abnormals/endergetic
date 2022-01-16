package com.minecraftabnormals.endergetic.core.registry.other;

import com.minecraftabnormals.endergetic.common.entities.bolloom.BalloonColor;
import com.minecraftabnormals.endergetic.core.EndergeticExpansion;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.IDataSerializer;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.registries.DataSerializerEntry;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Optional;

/**
 * @author SmellyModder (Luke Tonon)
 */
public final class EEDataSerializers {
	public static final DeferredRegister<DataSerializerEntry> SERIALIZERS = DeferredRegister.create(ForgeRegistries.DATA_SERIALIZERS, EndergeticExpansion.MOD_ID);

	public static final IDataSerializer<Optional<Vector3d>> OPTIONAL_VEC3D = new IDataSerializer<Optional<Vector3d>>() {
		@Override
		public void write(PacketBuffer buf, Optional<Vector3d> value) {
			buf.writeBoolean(value.isPresent());

			if (value.isPresent()) {
				Vector3d vec3d = value.get();
				buf.writeDouble(vec3d.x());
				buf.writeDouble(vec3d.y());
				buf.writeDouble(vec3d.z());
			}
		}

		@Override
		public Optional<Vector3d> read(PacketBuffer buf) {
			return !buf.readBoolean() ? Optional.empty() : Optional.of(new Vector3d(buf.readDouble(), buf.readDouble(), buf.readDouble()));
		}

		@Override
		public Optional<Vector3d> copy(Optional<Vector3d> value) {
			return value;
		}
	};

	public static final IDataSerializer<BalloonColor> BALLOON_COLOR = new IDataSerializer<BalloonColor>() {
		@Override
		public void write(PacketBuffer buf, BalloonColor value) {
			buf.writeEnum(value);
		}

		@Override
		public BalloonColor read(PacketBuffer buf) {
			return buf.readEnum(BalloonColor.class);
		}

		@Override
		public BalloonColor copy(BalloonColor value) {
			return value;
		}
	};

	static {
		SERIALIZERS.register("optional_vec3d", () -> new DataSerializerEntry(OPTIONAL_VEC3D));
		SERIALIZERS.register("balloon_color", () -> new DataSerializerEntry(BALLOON_COLOR));
	}
}