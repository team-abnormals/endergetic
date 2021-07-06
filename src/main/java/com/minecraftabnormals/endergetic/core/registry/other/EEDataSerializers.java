package com.minecraftabnormals.endergetic.core.registry.other;

import com.minecraftabnormals.endergetic.common.entities.bolloom.BalloonColor;
import com.minecraftabnormals.endergetic.common.entities.eetle.BroodEetleEntity.HealthStage;
import com.minecraftabnormals.endergetic.common.entities.eetle.flying.TargetFlyingRotations;
import com.minecraftabnormals.endergetic.common.entities.purpoid.PurpoidSize;
import com.minecraftabnormals.endergetic.core.EndergeticExpansion;
import net.minecraft.entity.EntitySize;
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
				buf.writeDouble(vec3d.getX());
				buf.writeDouble(vec3d.getY());
				buf.writeDouble(vec3d.getZ());
			}
		}

		@Override
		public Optional<Vector3d> read(PacketBuffer buf) {
			return !buf.readBoolean() ? Optional.empty() : Optional.of(new Vector3d(buf.readDouble(), buf.readDouble(), buf.readDouble()));
		}

		@Override
		public Optional<Vector3d> copyValue(Optional<Vector3d> value) {
			return value;
		}
	};

	public static final IDataSerializer<BalloonColor> BALLOON_COLOR = new IDataSerializer<BalloonColor>() {
		@Override
		public void write(PacketBuffer buf, BalloonColor value) {
			buf.writeEnumValue(value);
		}

		@Override
		public BalloonColor read(PacketBuffer buf) {
			return buf.readEnumValue(BalloonColor.class);
		}

		@Override
		public BalloonColor copyValue(BalloonColor value) {
			return value;
		}
	};

	public static final IDataSerializer<TargetFlyingRotations> TARGET_FLYING_ROTATIONS = new IDataSerializer<TargetFlyingRotations>() {
		@Override
		public void write(PacketBuffer buf, TargetFlyingRotations value) {
			buf.writeFloat(value.getTargetFlyPitch());
			buf.writeFloat(value.getTargetFlyRoll());
		}

		@Override
		public TargetFlyingRotations read(PacketBuffer buf) {
			return new TargetFlyingRotations(buf.readFloat(), buf.readFloat());
		}

		@Override
		public TargetFlyingRotations copyValue(TargetFlyingRotations value) {
			return value;
		}
	};

	public static final IDataSerializer<EntitySize> ENTITY_SIZE = new IDataSerializer<EntitySize>() {
		@Override
		public void write(PacketBuffer buf, EntitySize value) {
			buf.writeFloat(value.width);
			buf.writeFloat(value.height);
			buf.writeBoolean(value.fixed);
		}

		@Override
		public EntitySize read(PacketBuffer buf) {
			return new EntitySize(buf.readFloat(), buf.readFloat(), buf.readBoolean());
		}

		@Override
		public EntitySize copyValue(EntitySize value) {
			return value;
		}
	};

	public static final IDataSerializer<HealthStage> BROOD_HEALTH_STAGE = new IDataSerializer<HealthStage>() {
		public void write(PacketBuffer buf, HealthStage value) {
			buf.writeEnumValue(value);
		}

		public HealthStage read(PacketBuffer buf) {
			return buf.readEnumValue(HealthStage.class);
		}

		public HealthStage copyValue(HealthStage value) {
			return value;
		}
	};

	public static final IDataSerializer<PurpoidSize> PURPOID_SIZE = new IDataSerializer<PurpoidSize>() {
		public void write(PacketBuffer buf, PurpoidSize value) {
			buf.writeEnumValue(value);
		}

		public PurpoidSize read(PacketBuffer buf) {
			return buf.readEnumValue(PurpoidSize.class);
		}

		public PurpoidSize copyValue(PurpoidSize value) {
			return value;
		}
	};

	static {
		SERIALIZERS.register("optional_vec3d", () -> new DataSerializerEntry(OPTIONAL_VEC3D));
		SERIALIZERS.register("balloon_color", () -> new DataSerializerEntry(BALLOON_COLOR));
		SERIALIZERS.register("target_flying_rotations", () -> new DataSerializerEntry(TARGET_FLYING_ROTATIONS));
		SERIALIZERS.register("entity_size", () -> new DataSerializerEntry(ENTITY_SIZE));
		SERIALIZERS.register("brood_health_stage", () -> new DataSerializerEntry(BROOD_HEALTH_STAGE));
		SERIALIZERS.register("purpoid_size", () -> new DataSerializerEntry(PURPOID_SIZE));
	}
}