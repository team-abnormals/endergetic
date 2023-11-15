package com.teamabnormals.endergetic.core.registry.other;

import com.teamabnormals.endergetic.common.entity.bolloom.BalloonColor;
import com.teamabnormals.endergetic.common.entity.eetle.BroodEetle.HealthStage;
import com.teamabnormals.endergetic.common.entity.eetle.flying.TargetFlyingRotations;
import com.teamabnormals.endergetic.common.entity.purpoid.PurpoidSize;
import com.teamabnormals.endergetic.core.EndergeticExpansion;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Optional;

/**
 * @author SmellyModder (Luke Tonon)
 */
public final class EEDataSerializers {
	public static final DeferredRegister<EntityDataSerializer<?>> SERIALIZERS = DeferredRegister.create(ForgeRegistries.Keys.ENTITY_DATA_SERIALIZERS, EndergeticExpansion.MOD_ID);

	public static final EntityDataSerializer<Optional<Vec3>> OPTIONAL_VEC3D = new EntityDataSerializer<>() {
		@Override
		public void write(FriendlyByteBuf buf, Optional<Vec3> value) {
			buf.writeBoolean(value.isPresent());

			if (value.isPresent()) {
				Vec3 vec3d = value.get();
				buf.writeDouble(vec3d.x());
				buf.writeDouble(vec3d.y());
				buf.writeDouble(vec3d.z());
			}
		}

		@Override
		public Optional<Vec3> read(FriendlyByteBuf buf) {
			return !buf.readBoolean() ? Optional.empty() : Optional.of(new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble()));
		}

		@Override
		public Optional<Vec3> copy(Optional<Vec3> value) {
			return value;
		}
	};

	public static final EntityDataSerializer<BalloonColor> BALLOON_COLOR = new EntityDataSerializer<>() {
		@Override
		public void write(FriendlyByteBuf buf, BalloonColor value) {
			buf.writeEnum(value);
		}

		@Override
		public BalloonColor read(FriendlyByteBuf buf) {
			return buf.readEnum(BalloonColor.class);
		}

		@Override
		public BalloonColor copy(BalloonColor value) {
			return value;
		}
	};

	public static final EntityDataSerializer<TargetFlyingRotations> TARGET_FLYING_ROTATIONS = new EntityDataSerializer<>() {
		@Override
		public void write(FriendlyByteBuf buf, TargetFlyingRotations value) {
			buf.writeFloat(value.getTargetFlyPitch());
			buf.writeFloat(value.getTargetFlyRoll());
		}

		@Override
		public TargetFlyingRotations read(FriendlyByteBuf buf) {
			return new TargetFlyingRotations(buf.readFloat(), buf.readFloat());
		}

		@Override
		public TargetFlyingRotations copy(TargetFlyingRotations value) {
			return value;
		}
	};

	public static final EntityDataSerializer<EntityDimensions> ENTITY_SIZE = new EntityDataSerializer<>() {
		@Override
		public void write(FriendlyByteBuf buf, EntityDimensions value) {
			buf.writeFloat(value.width);
			buf.writeFloat(value.height);
			buf.writeBoolean(value.fixed);
		}

		@Override
		public EntityDimensions read(FriendlyByteBuf buf) {
			return new EntityDimensions(buf.readFloat(), buf.readFloat(), buf.readBoolean());
		}

		@Override
		public EntityDimensions copy(EntityDimensions value) {
			return value;
		}
	};

	public static final EntityDataSerializer<HealthStage> BROOD_HEALTH_STAGE = new EntityDataSerializer<>() {
		public void write(FriendlyByteBuf buf, HealthStage value) {
			buf.writeEnum(value);
		}

		public HealthStage read(FriendlyByteBuf buf) {
			return buf.readEnum(HealthStage.class);
		}

		public HealthStage copy(HealthStage value) {
			return value;
		}
	};

	public static final EntityDataSerializer<PurpoidSize> PURPOID_SIZE = new EntityDataSerializer<>() {
		public void write(FriendlyByteBuf buf, PurpoidSize value) {
			buf.writeEnum(value);
		}

		public PurpoidSize read(FriendlyByteBuf buf) {
			return buf.readEnum(PurpoidSize.class);
		}

		public PurpoidSize copy(PurpoidSize value) {
			return value;
		}
	};

	static {
		SERIALIZERS.register("optional_vec3d", () -> OPTIONAL_VEC3D);
		SERIALIZERS.register("balloon_color", () -> BALLOON_COLOR);
		SERIALIZERS.register("target_flying_rotations", () -> TARGET_FLYING_ROTATIONS);
		SERIALIZERS.register("entity_size", () -> ENTITY_SIZE);
		SERIALIZERS.register("brood_health_stage", () -> BROOD_HEALTH_STAGE);
		SERIALIZERS.register("purpoid_size", () -> PURPOID_SIZE);
	}
}