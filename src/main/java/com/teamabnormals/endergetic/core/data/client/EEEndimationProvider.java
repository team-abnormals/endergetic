package com.teamabnormals.endergetic.core.data.client;

import com.teamabnormals.blueprint.core.endimator.EndimationKeyframe;
import com.teamabnormals.blueprint.core.endimator.util.EndimationProvider;
import com.teamabnormals.endergetic.core.EndergeticExpansion;
import net.minecraft.data.DataGenerator;

import static com.teamabnormals.blueprint.core.endimator.Endimation.Builder.Keyframes.keyframes;
import static com.teamabnormals.blueprint.core.endimator.Endimation.PartKeyframes.Builder.partKeyframes;

public final class EEEndimationProvider extends EndimationProvider {

	public EEEndimationProvider(DataGenerator generator) {
		super(generator, EndergeticExpansion.MOD_ID);
	}

	@Override
	protected void addEndimations() {
		this.endimation("adolescent_booflo/boof")
				.keyframes(
						keyframes()
								.part("Jaw", partKeyframes().pos(linear(0.0F), catmullRom(0.15F, 0.0F, 0.2F, 0.0F), catmullRom(0.3F, 0.0F, 0.2F, 0.0F), catmullRom(0.5F, 0.0F, -0.0F, 0.0F), catmullRom(0.7F, 0.0F, -0.0F, 0.0F)).scale(linear(0.0F, 1.0F, 1.0F, 1.0F), catmullRom(0.15F, 1.5F, 0.8F, 1.5F), catmullRom(0.3F, 1.5F, 0.8F, 1.5F), catmullRom(0.5F, 1.0F, 1.0F, 1.0F), catmullRom(0.7F, 1.0F, 1.0F, 1.0F)))
								.part("Head", partKeyframes().pos(linear(0.0F), catmullRom(0.15F, 0.0F, 0.2F, 0.0F), catmullRom(0.3F, 0.0F, 0.2F, 0.0F), catmullRom(0.5F, 0.0F, -0.0F, 0.0F), catmullRom(0.7F, 0.0F, -0.0F, 0.0F)).scale(linear(0.0F, 1.0F, 1.0F, 1.0F), catmullRom(0.15F, 1.5F, 0.8F, 1.5F), catmullRom(0.3F, 1.5F, 0.8F, 1.5F), catmullRom(0.5F, 1.0F, 1.0F, 1.0F), catmullRom(0.7F, 1.0F, 1.0F, 1.0F)))
								.part("ArmLeft", partKeyframes().rotate(linear(0.0F), catmullRom(0.15F, 0.0F, 0.0F, -22.918312F), catmullRom(0.3F, 0.0F, 0.0F, -22.918312F), catmullRom(0.5F, 0.0F, 0.0F, 34.37747F), catmullRom(0.7F, 0.0F, 0.0F, 34.37747F), catmullRom(0.9F, 0.0F, 0.0F, -11.459156F)))
								.part("ArmRight", partKeyframes().rotate(linear(0.0F), catmullRom(0.15F, 0.0F, 0.0F, 22.918312F), catmullRom(0.3F, 0.0F, 0.0F, 22.918312F), catmullRom(0.5F, 0.0F, 0.0F, -34.37747F), catmullRom(0.7F, 0.0F, 0.0F, -34.37747F), catmullRom(0.9F, 0.0F, 0.0F, 11.459156F)))
								.part("KneeLeft", partKeyframes().rotate(linear(0.0F), catmullRom(0.15F, 0.0F, 0.0F, 11.459156F), catmullRom(0.3F, 0.0F, 0.0F, 11.459156F), catmullRom(0.5F, 0.0F, 0.0F, -20.053522F), catmullRom(0.7F, 0.0F, 0.0F, -20.053522F), catmullRom(0.9F, 0.0F, 0.0F, 8.594367F)))
								.part("KneeRight", partKeyframes().rotate(linear(0.0F), catmullRom(0.15F, 0.0F, 0.0F, -11.459156F), catmullRom(0.3F, 0.0F, 0.0F, -11.459156F), catmullRom(0.5F, 0.0F, 0.0F, 20.053522F), catmullRom(0.7F, 0.0F, 0.0F, 20.053522F), catmullRom(0.9F, 0.0F, 0.0F, -8.594367F)))
				);

		this.endimation("adolescent_booflo/eating")
				.keyframes(
						keyframes()
								.part("ArmLeft", partKeyframes().rotate(linear(0.0F), catmullRom(0.25F, 0.0F, -24.064226F, 0.0F), catmullRom(0.5F)))
								.part("Head", partKeyframes().rotate(linear(0.0F), catmullRom(0.25F, -34.37747F, 0.0F, 0.0F), catmullRom(0.5F)))
								.part("ArmRight", partKeyframes().rotate(linear(0.0F), catmullRom(0.25F, 0.0F, 24.064226F, 0.0F), catmullRom(0.5F)))
				);

		this.endimation("booflo/croak")
				.keyframes(
						keyframes()
								.part("Jaw", partKeyframes().rotate(linear(0.0F), catmullRom(0.25F, 14.323945F, 0.0F, 0.0F), catmullRom(2.5F, 14.323945F, 0.0F, 0.0F), catmullRom(2.75F)))
				);

		this.endimation("booflo/hop")
				.keyframes(
						keyframes()
								.part("Jaw", partKeyframes().rotate(linear(0.0F), catmullRom(0.5F, -1.1459155F, 0.0F, 0.0F), catmullRom(0.75F, 11.459156F, 0.0F, 0.0F), catmullRom(1.25F)))
								.part("Head", partKeyframes().rotate(linear(0.0F), catmullRom(0.5F, 2.864789F, 0.0F, 0.0F), catmullRom(0.75F, -22.918312F, 0.0F, 0.0F), catmullRom(1.25F)))
								.part("LegBackLeft", partKeyframes().rotate(linear(0.0F), catmullRom(0.5F, 0.0F, 0.0F, -28.64789F), catmullRom(0.75F, 3.4377468F, -18.907608F, -10.313241F), catmullRom(1.25F)))
								.part("LegLeft", partKeyframes().rotate(linear(0.0F), catmullRom(0.5F, 0.0F, -42.971836F, -8.021409F), catmullRom(0.75F, 0.0F, 26.929016F, 0.0F), catmullRom(1.25F)))
								.part("KneeRight", partKeyframes().rotate(linear(0.0F), catmullRom(0.5F, 0.0F, -34.37747F, -11.459156F), catmullRom(0.75F, -10.313241F, 18.907608F, -11.459156F), catmullRom(1.25F)))
								.part("LegBackRight", partKeyframes().rotate(linear(0.0F), catmullRom(0.5F, 0.0F, 0.0F, 28.64789F), catmullRom(0.75F, 3.4377468F, 18.907608F, 10.313241F), catmullRom(1.25F)))
								.part("KneeLeft", partKeyframes().rotate(linear(0.0F), catmullRom(0.5F, 0.0F, 34.37747F, 11.459156F), catmullRom(0.75F, -10.313241F, -18.907608F, 11.459156F), catmullRom(1.25F)))
								.part("LegRight", partKeyframes().rotate(linear(0.0F), catmullRom(0.5F, 0.0F, -42.971836F, 8.021409F), catmullRom(0.75F, 0.0F, 26.929016F, 0.0F), catmullRom(1.25F)))
				);

		this.endimation("booflo/hurt")
				.keyframes(
						keyframes()
								.part("Jaw", partKeyframes().rotate(linear(0.0F), catmullRom(0.25F, 14.323945F, 0.0F, 0.0F), catmullRom(0.5F, 14.323945F, 0.0F, 0.0F), catmullRom(0.75F)))
								.part("Head", partKeyframes().rotate(linear(0.0F), catmullRom(0.25F, 2.864789F, 0.0F, 0.0F), catmullRom(0.5F, 2.864789F, 0.0F, 0.0F), catmullRom(0.75F)))
								.part("LegBackLeft", partKeyframes().rotate(linear(0.0F), catmullRom(0.25F, 3.4377468F, -13.178029F, -10.313241F), catmullRom(0.5F, 3.4377468F, -13.178029F, -10.313241F), catmullRom(0.75F)))
								.part("LegLeft", partKeyframes().rotate(linear(0.0F), catmullRom(0.25F, 0.0F, 17.188734F, 0.0F), catmullRom(0.5F, 0.0F, 17.188734F, 0.0F), catmullRom(0.75F)))
								.part("KneeRight", partKeyframes().rotate(linear(0.0F), catmullRom(0.25F, -10.313241F, 13.178029F, -11.459156F), catmullRom(0.5F, -10.313241F, 13.178029F, -11.459156F), catmullRom(0.75F)))
								.part("LegBackRight", partKeyframes().rotate(linear(0.0F), catmullRom(0.25F, 3.4377468F, 13.178029F, 10.313241F), catmullRom(0.5F, 3.4377468F, 13.178029F, 10.313241F), catmullRom(0.75F)))
								.part("KneeLeft", partKeyframes().rotate(linear(0.0F), catmullRom(0.25F, -10.313241F, -13.178029F, 11.459156F), catmullRom(0.5F, -10.313241F, -13.178029F, 11.459156F), catmullRom(0.75F)))
								.part("LegRight", partKeyframes().rotate(linear(0.0F), catmullRom(0.25F, 0.0F, 17.188734F, 0.0F), catmullRom(0.5F, 0.0F, 17.188734F, 0.0F), catmullRom(0.75F)))
				);

		this.endimation("booflo/birth")
				.keyframes(
						keyframes()
								.part("Head", partKeyframes().rotate(linear(0.0F), catmullRom(0.5F, 2.864789F, 0.0F, 0.0F), catmullRom(6.5F, 2.864789F, 0.0F, 0.0F), catmullRom(7.0F)))
				);

		this.endimation("booflo/inflate")
				.keyframes(
						keyframes()
								.part("LegBackRightInflated", partKeyframes().rotate(linear(0.0F), catmullRom(0.25F, 0.0F, 0.0F, -22.918312F), catmullRom(0.5F)))
								.part("KneeRightInflated", partKeyframes().rotate(linear(0.0F), catmullRom(0.25F, 0.0F, 0.0F, 22.918312F), catmullRom(0.25F, 0.0F, 0.0F, 11.459156F), catmullRom(0.5F)))
								.part("KneeLeftInflated", partKeyframes().rotate(linear(0.0F), catmullRom(0.25F, 0.0F, 0.0F, -11.459156F), catmullRom(0.5F)))
								.part("LegRightInflated", partKeyframes().rotate(linear(0.0F), catmullRom(0.25F, 0.0F, 0.0F, 34.37747F), catmullRom(0.5F)))
								.part("LegLeftInflated", partKeyframes().rotate(linear(0.0F), catmullRom(0.25F, 0.0F, 0.0F, -34.37747F), catmullRom(0.5F)))
								.part("HeadInflated", partKeyframes().scale(linear(0.0F, 1.0F, 1.0F, 1.0F), catmullRom(0.25F, 1.7F, 1.0F, 1.7F), catmullRom(0.5F, 1.0F, 1.0F, 1.0F)))
				);

		this.endimation("booflo/swim")
				.keyframes(
						keyframes()
								.part("LegBackRightInflated", partKeyframes().rotate(linear(0.0F), catmullRom(0.5F, 45.836624F, 45.836624F, 91.67325F), catmullRom(1.0F)))
								.part("KneeRightInflated", partKeyframes().rotate(linear(0.0F), catmullRom(0.5F, -51.27972F, 0.0F, 0.0F), catmullRom(1.0F)))
								.part("KneeLeftInflated", partKeyframes().rotate(linear(0.0F), catmullRom(0.5F, -51.27972F, 0.0F, 0.0F), catmullRom(1.0F)))
								.part("LegRightInflated", partKeyframes().rotate(linear(0.0F), catmullRom(0.5F, 0.0F, -91.67325F, 0.0F), catmullRom(1.0F)))
								.part("LegLeftInflated", partKeyframes().rotate(linear(0.0F), catmullRom(0.5F, 0.0F, -91.67325F, 0.0F), catmullRom(1.0F)))
								.part("LegBackLeftInflated", partKeyframes().rotate(linear(0.0F), catmullRom(0.5F, 45.836624F, -45.836624F, -91.67325F), catmullRom(1.0F)))
								.part("HeadInflated", partKeyframes().pos(linear(0.0F), catmullRom(0.5F, 0.0F, 0.3F, 0.0F), catmullRom(1.0F)).rotate(linear(0.0F), catmullRom(0.5F, -6.0160565F, 0.0F, 0.0F), catmullRom(1.0F)))
								.part("JawInflated", partKeyframes().rotate(linear(0.0F), catmullRom(0.5F, 11.459156F, 0.0F, 0.0F), catmullRom(1.0F)))
				);

		this.endimation("booflo/eat")
				.keyframes(
						keyframes()
								.part("Jaw", partKeyframes().rotate(linear(0.0F), linear(1.0F), catmullRom(1.5F, 22.918312F, 0.0F, 0.0F), catmullRom(2.0F), catmullRom(2.5F, 22.918312F, 0.0F, 0.0F), catmullRom(3.0F), catmullRom(3.5F, 22.918312F, 0.0F, 0.0F), catmullRom(4.0F), catmullRom(4.5F, 22.918312F, 0.0F, 0.0F), catmullRom(5.0F), catmullRom(5.5F, 22.918312F, 0.0F, 0.0F), catmullRom(6.0F), catmullRom(6.5F, 22.918312F, 0.0F, 0.0F), catmullRom(7.0F), catmullRom(8.0F)))
								.part("Head", partKeyframes().rotate(linear(0.0F), linear(1.0F), catmullRom(1.5F, -5.729578F, 0.0F, 0.0F), catmullRom(2.0F), catmullRom(2.5F, -5.729578F, 0.0F, 0.0F), catmullRom(3.0F), catmullRom(3.5F, -5.729578F, 0.0F, 0.0F), catmullRom(4.0F), catmullRom(4.5F, -5.729578F, 0.0F, 0.0F), catmullRom(5.0F), catmullRom(5.5F, -5.729578F, 0.0F, 0.0F), catmullRom(6.0F), catmullRom(6.5F, -5.729578F, 0.0F, 0.0F), catmullRom(7.0F), catmullRom(8.0F)))
								.part("LegBackLeft", partKeyframes().rotate(linear(0.0F), catmullRom(1.5F, 0.0F, -1.7188734F, -2.864789F), catmullRom(2.0F), catmullRom(2.5F, 0.0F, -1.7188734F, -2.864789F), catmullRom(3.0F), catmullRom(3.5F, 0.0F, -1.7188734F, -2.864789F), catmullRom(4.0F), catmullRom(4.5F, 0.0F, -1.7188734F, -2.864789F), catmullRom(5.0F), catmullRom(5.5F, 0.0F, -1.7188734F, -2.864789F), catmullRom(6.0F), catmullRom(6.5F, 0.0F, -1.7188734F, -2.864789F), catmullRom(7.0F), catmullRom(8.0F)))
								.part("LegLeft", partKeyframes().rotate(linear(0.0F), catmullRom(1.0F, 0.0F, 8.594367F, 51.5662F), catmullRom(1.5F, 0.0F, 14.323945F, 53.285076F), catmullRom(2.0F, 0.0F, 8.594367F, 51.5662F), catmullRom(2.5F, 0.0F, 14.323945F, 53.285076F), catmullRom(3.0F, 0.0F, 8.594367F, 51.5662F), catmullRom(3.5F, 0.0F, 14.323945F, 53.285076F), catmullRom(4.0F, 0.0F, 8.594367F, 51.5662F), catmullRom(4.5F, 0.0F, 14.323945F, 53.285076F), catmullRom(5.0F, 0.0F, 8.594367F, 51.5662F), catmullRom(5.5F, 0.0F, 14.323945F, 53.285076F), catmullRom(6.0F, 0.0F, 8.594367F, 51.5662F), catmullRom(6.5F, 0.0F, 14.323945F, 53.285076F), catmullRom(7.0F, 0.0F, 8.594367F, 51.5662F), catmullRom(8.0F)))
								.part("LegRight", partKeyframes().rotate(linear(0.0F), catmullRom(1.0F, 0.0F, 8.594367F, -51.5662F), catmullRom(1.5F, 0.0F, 14.323945F, -53.285076F), catmullRom(2.0F, 0.0F, 8.594367F, -51.5662F), catmullRom(2.5F, 0.0F, 14.323945F, -53.285076F), catmullRom(3.0F, 0.0F, 8.594367F, -51.5662F), catmullRom(3.5F, 0.0F, 14.323945F, -53.285076F), catmullRom(4.0F, 0.0F, 8.594367F, -51.5662F), catmullRom(4.5F, 0.0F, 14.323945F, -53.285076F), catmullRom(5.0F, 0.0F, 8.594367F, -51.5662F), catmullRom(5.5F, 0.0F, 14.323945F, -53.285076F), catmullRom(6.0F, 0.0F, 8.594367F, -51.5662F), catmullRom(6.5F, 0.0F, 14.323945F, -53.285076F), catmullRom(7.0F, 0.0F, 8.594367F, -51.5662F), catmullRom(8.0F)))
								.part("LegBackRight", partKeyframes().rotate(linear(0.0F), catmullRom(1.5F, 0.0F, 1.7188734F, 2.864789F), catmullRom(2.0F), catmullRom(2.5F, 0.0F, 1.7188734F, 2.864789F), catmullRom(3.0F), catmullRom(3.5F, 0.0F, 1.7188734F, 2.864789F), catmullRom(4.0F), catmullRom(4.5F, 0.0F, 1.7188734F, 2.864789F), catmullRom(5.0F), catmullRom(5.5F, 0.0F, 1.7188734F, 2.864789F), catmullRom(6.0F), catmullRom(6.5F, 0.0F, 1.7188734F, 2.864789F), catmullRom(7.0F), catmullRom(8.0F)))
				);

		this.endimation("booflo/charge")
				.keyframes(
						keyframes()
								.part("LegBackRightInflated", partKeyframes().rotate(linear(0.0F), catmullRom(0.75F, 0.0F, 0.0F, 28.64789F), catmullRom(3.75F, 0.0F, 0.0F, 28.64789F)))
								.part("KneeRightInflated", partKeyframes().rotate(linear(0.0F), catmullRom(0.75F, 0.0F, 0.0F, 22.918312F), catmullRom(3.75F, 0.0F, 0.0F, 22.918312F)))
								.part("KneeLeftInflated", partKeyframes().rotate(linear(0.0F), catmullRom(0.75F, 0.0F, 0.0F, -22.918312F), catmullRom(3.75F, 0.0F, 0.0F, -22.918312F)))
								.part("LegRightInflated", partKeyframes().rotate(linear(0.0F), catmullRom(0.75F, 0.0F, 0.0F, 28.64789F), catmullRom(3.75F, 0.0F, 0.0F, 28.64789F)))
								.part("LegLeftInflated", partKeyframes().rotate(linear(0.0F), catmullRom(0.75F, 0.0F, 0.0F, -28.64789F), catmullRom(3.75F, 0.0F, 0.0F, -28.64789F)))
								.part("LegBackLeftInflated", partKeyframes().rotate(linear(0.0F), catmullRom(0.75F, 0.0F, 0.0F, -28.64789F), catmullRom(3.75F, 0.0F, 0.0F, -28.64789F)))
				);

		this.endimation("booflo/slam")
				.keyframes(
						keyframes()
								.part("LegBackRightInflated", partKeyframes().rotate(linear(0.0F), catmullRom(0.2F, 0.0F, 0.0F, -11.459156F), catmullRom(0.35000002F, 0.0F, 0.0F, 5.729578F), catmullRom(0.5F)))
								.part("KneeRightInflated", partKeyframes().rotate(linear(0.0F), catmullRom(0.2F, 0.0F, 0.0F, -28.64789F), catmullRom(0.35000002F, 0.0F, 0.0F, 40.107044F), catmullRom(0.5F)))
								.part("KneeLeftInflated", partKeyframes().rotate(linear(0.0F), catmullRom(0.2F, 0.0F, 0.0F, 28.64789F), catmullRom(0.35000002F, 0.0F, 0.0F, -40.107044F), catmullRom(0.5F)))
								.part("LegRightInflated", partKeyframes().rotate(linear(0.0F), catmullRom(0.2F, 0.0F, 0.0F, -28.64789F), catmullRom(0.35000002F, 0.0F, 0.0F, 28.64789F), catmullRom(0.5F)))
								.part("LegLeftInflated", partKeyframes().rotate(linear(0.0F), catmullRom(0.2F, 0.0F, 0.0F, 28.64789F), catmullRom(0.35000002F, 0.0F, 0.0F, -28.64789F), catmullRom(0.5F)))
								.part("LegBackLeftInflated", partKeyframes().rotate(linear(0.0F), catmullRom(0.2F, 0.0F, 0.0F, 11.459156F), catmullRom(0.35000002F, 0.0F, 0.0F, -5.729578F), catmullRom(0.5F)))
								.part("HeadInflated", partKeyframes().pos(linear(0.0F), catmullRom(0.2F, 0.0F, -9.9F, 0.0F), catmullRom(0.35000002F, 0.0F, -1.9F, 0.0F), catmullRom(0.5F)).scale(linear(0.0F, 1.0F, 1.0F, 1.0F), catmullRom(0.2F, 0.8F, 0.5F, 0.8F), catmullRom(0.35000002F, 1.8F, 2.5F, 1.8F), catmullRom(0.5F, 1.0F, 1.0F, 1.0F)))
				);

		this.endimation("booflo/growl")
				.keyframes(
						keyframes()
								.part("Jaw", partKeyframes().rotate(linear(0.0F), catmullRom(0.5F, 14.323945F, 0.0F, 0.0F), catmullRom(2.75F, 14.323945F, 0.0F, 0.0F), catmullRom(3.0F)))
				);

		this.endimation("puff_bug/claim_hive")
				.keyframes(
						keyframes()
								.part("Sensor1Deflated", partKeyframes().rotate(linear(0.0F), catmullRom(0.25F, 0.0F, 0.0F, 25.7831F), catmullRom(0.5F), catmullRom(0.75F, 0.0F, 0.0F, 25.7831F), catmullRom(1.0F)))
								.part("Sensor2Deflated", partKeyframes().rotate(linear(0.0F), catmullRom(0.25F, 25.7831F, 0.0F, 0.0F), catmullRom(0.5F), catmullRom(0.75F, 25.7831F, 0.0F, 0.0F), catmullRom(1.0F)))
				);

		this.endimation("puff_bug/puff")
				.keyframes(
						keyframes()
								.part("Head", partKeyframes().rotate(linear(0.0F), catmullRom(0.5F, 31.51268F, 0.0F, 0.0F), catmullRom(1.0F)))
								.part("Sensor1Deflated", partKeyframes().rotate(linear(0.0F), catmullRom(0.5F, 0.0F, 0.0F, -28.64789F), catmullRom(1.0F)))
								.part("Sensor2Deflated", partKeyframes().rotate(linear(0.0F), catmullRom(0.5F, -28.64789F, 0.0F, 0.0F), catmullRom(1.0F)))
								.part("Neck", partKeyframes().rotate(linear(0.0F), catmullRom(0.5F, 22.918312F, 0.0F, 0.0F), catmullRom(1.0F)))
								.part("Body", partKeyframes().rotate(linear(0.0F), catmullRom(0.5F, 14.323945F, 0.0F, 0.0F), catmullRom(1.0F)).scale(linear(0.0F, 1.0F, 1.0F, 1.0F), catmullRom(0.5F, 1.4F, 1.4F, 1.4F), catmullRom(1.0F, 1.0F, 1.0F, 1.0F)))
				);

		this.endimation("puff_bug/teleport_to")
				.keyframes(
						keyframes()
								.part("Body", partKeyframes().scale(linear(0.0F, 1.0F, 1.0F, 1.0F), catmullRom(0.25F, 2.3F, 2.3F, 2.3F), linear(0.4F), linear(0.75F)))
				);

		this.endimation("puff_bug/teleport_from")
				.keyframes(
						keyframes()
								.part("Body", partKeyframes().scale(linear(0.0F, 1.0F, 1.0F, 1.0F), catmullRom(0.25F, 2.3F, 2.3F, 2.3F), catmullRom(0.5F, 1.0F, 1.0F, 1.0F)))
				);

		this.endimation("puff_bug/rotate")
				.keyframes(
						keyframes()
								.part("Head", partKeyframes().rotate(linear(0.0F), catmullRom(0.5F, -28.64789F, 0.0F, 0.0F), catmullRom(1.0F)))
								.part("Neck", partKeyframes().rotate(linear(0.0F), catmullRom(0.5F, -28.64789F, 0.0F, 0.0F), catmullRom(1.0F)))
								.part("Stinger", partKeyframes().rotate(linear(0.0F), catmullRom(0.5F, 22.918312F, 0.0F, 0.0F), catmullRom(1.0F)))
				);

		this.endimation("puff_bug/pollinate")
				.keyframes(
						keyframes()
								.part("Head", partKeyframes().rotate(linear(0.0F), catmullRom(0.25F, 0.0F, 0.0F, 20.053522F), catmullRom(0.5F), catmullRom(0.75F, 0.0F, 0.0F, -20.053522F), catmullRom(1.0F), catmullRom(1.25F, 0.0F, 0.0F, 20.053522F), catmullRom(1.5F), catmullRom(1.75F, 0.0F, 0.0F, -20.053522F), catmullRom(2.0F), catmullRom(2.25F, 0.0F, 0.0F, 20.053522F), catmullRom(2.5F), catmullRom(2.75F, 0.0F, 0.0F, -20.053522F), catmullRom(3.0F), catmullRom(3.25F, 0.0F, 0.0F, 20.053522F), catmullRom(3.5F), catmullRom(3.75F, 0.0F, 0.0F, -20.053522F), catmullRom(4.0F), catmullRom(4.25F, 0.0F, 0.0F, 20.053522F), catmullRom(4.5F), catmullRom(4.75F, 0.0F, 0.0F, -20.053522F), catmullRom(5.0F), catmullRom(6.0F)))
								.part("Sensor1Deflated", partKeyframes().rotate(linear(0.0F), catmullRom(0.25F, 0.0F, 0.0F, -37.242256F), catmullRom(0.5F), catmullRom(0.75F, 0.0F, 0.0F, 28.64789F), catmullRom(1.0F), catmullRom(1.25F, 0.0F, 0.0F, -37.242256F), catmullRom(1.5F), catmullRom(1.75F, 0.0F, 0.0F, 28.64789F), catmullRom(2.0F), catmullRom(2.25F, 0.0F, 0.0F, -37.242256F), catmullRom(2.5F), catmullRom(2.75F, 0.0F, 0.0F, 28.64789F), catmullRom(3.0F), catmullRom(3.25F, 0.0F, 0.0F, -37.242256F), catmullRom(3.5F), catmullRom(3.75F, 0.0F, 0.0F, 28.64789F), catmullRom(4.0F), catmullRom(4.25F, 0.0F, 0.0F, -37.242256F), catmullRom(4.5F), catmullRom(4.75F, 0.0F, 0.0F, 28.64789F), catmullRom(5.0F), catmullRom(6.0F)))
								.part("Sensor2Deflated", partKeyframes().rotate(linear(0.0F), catmullRom(0.25F, 28.64789F, 0.0F, 0.0F), catmullRom(0.5F), catmullRom(0.75F, -37.242256F, 0.0F, 0.0F), catmullRom(1.0F), catmullRom(1.25F, 28.64789F, 0.0F, 0.0F), catmullRom(1.5F), catmullRom(1.75F, -37.242256F, 0.0F, 0.0F), catmullRom(2.0F), catmullRom(2.25F, 28.64789F, 0.0F, 0.0F), catmullRom(2.5F), catmullRom(2.75F, -37.242256F, 0.0F, 0.0F), catmullRom(3.0F), catmullRom(3.25F, 28.64789F, 0.0F, 0.0F), catmullRom(3.5F), catmullRom(3.75F, -37.242256F, 0.0F, 0.0F), catmullRom(4.0F), catmullRom(4.25F, 28.64789F, 0.0F, 0.0F), catmullRom(4.5F), catmullRom(4.75F, -37.242256F, 0.0F, 0.0F), catmullRom(5.0F), catmullRom(6.0F)))
								.part("Neck", partKeyframes().rotate(linear(0.0F), catmullRom(0.25F, 0.0F, 0.0F, 9.740283F), catmullRom(0.5F), catmullRom(0.75F, 0.0F, 0.0F, -9.740283F), catmullRom(1.0F), catmullRom(1.25F, 0.0F, 0.0F, 9.740283F), catmullRom(1.5F), catmullRom(1.75F, 0.0F, 0.0F, -9.740283F), catmullRom(2.0F), catmullRom(2.25F, 0.0F, 0.0F, 9.740283F), catmullRom(2.5F), catmullRom(2.75F, 0.0F, 0.0F, -9.740283F), catmullRom(3.0F), catmullRom(3.25F, 0.0F, 0.0F, 9.740283F), catmullRom(3.5F), catmullRom(3.75F, 0.0F, 0.0F, -9.740283F), catmullRom(4.0F), catmullRom(4.25F, 0.0F, 0.0F, 9.740283F), catmullRom(4.5F), catmullRom(4.75F, 0.0F, 0.0F, -9.740283F), catmullRom(5.0F), catmullRom(6.0F)))
								.part("Body", partKeyframes().scale(linear(0.0F, 1.0F, 1.0F, 1.0F), linear(5.0F, 1.0F, 1.0F, 1.0F), catmullRom(5.5F, 1.4F, 1.4F, 1.4F), catmullRom(6.0F, 1.0F, 1.0F, 1.0F)))
				);

		this.endimation("puff_bug/make_item")
				.keyframes(
						keyframes()
								.part("Head", partKeyframes().rotate(linear(0.0F), linear(4.0F), catmullRom(4.5F, -20.053522F, 0.0F, 0.0F), catmullRom(5.0F)))
								.part("Sensor1Deflated", partKeyframes().rotate(linear(0.0F), catmullRom(0.5F, 0.0F, 0.0F, 28.64789F), catmullRom(1.0F), catmullRom(1.5F, 0.0F, 0.0F, 28.64789F), catmullRom(2.0F), catmullRom(2.5F, 0.0F, 0.0F, 28.64789F), catmullRom(3.0F), catmullRom(3.5F, 0.0F, 0.0F, 28.64789F), catmullRom(4.0F), catmullRom(4.5F, 0.0F, 0.0F, 34.37747F), catmullRom(5.0F)))
								.part("Sensor2Deflated", partKeyframes().rotate(linear(0.0F), catmullRom(0.5F, 28.64789F, 0.0F, 0.0F), catmullRom(1.0F), catmullRom(1.5F, 28.64789F, 0.0F, 0.0F), catmullRom(2.0F), catmullRom(2.5F, 28.64789F, 0.0F, 0.0F), catmullRom(3.0F), catmullRom(3.5F, 28.64789F, 0.0F, 0.0F), catmullRom(4.0F), catmullRom(4.5F, 34.37747F, 0.0F, 0.0F), catmullRom(5.0F)))
								.part("Neck", partKeyframes().rotate(linear(0.0F), linear(4.0F), catmullRom(4.5F, -14.323945F, 0.0F, 0.0F), catmullRom(5.0F)))
								.part("Body", partKeyframes().scale(linear(0.0F, 1.0F, 1.0F, 1.0F), catmullRom(0.5F, 1.2F, 1.2F, 1.2F), catmullRom(1.0F, 1.0F, 1.0F, 1.0F), catmullRom(1.5F, 1.2F, 1.2F, 1.2F), catmullRom(2.0F, 1.0F, 1.0F, 1.0F), catmullRom(2.5F, 1.2F, 1.2F, 1.2F), catmullRom(3.0F, 1.0F, 1.0F, 1.0F), catmullRom(3.5F, 1.2F, 1.2F, 1.2F), catmullRom(4.0F, 1.0F, 1.0F, 1.0F), catmullRom(4.5F, 1.4F, 1.4F, 1.4F), catmullRom(5.0F, 1.0F, 1.0F, 1.0F)))
				);

		this.endimation("puff_bug/land")
				.keyframes(
						keyframes()
								.part("Sensor1DeflatedProjectile", partKeyframes().rotate(linear(0.0F), linear(0.15F), catmullRom(0.3F, 0.0F, 0.0F, 117.456345F), catmullRom(0.45000002F, 0.0F, 0.0F, 117.456345F), catmullRom(0.6F), catmullRom(0.75F), linear(0.9F), linear(1.0F)))
								.part("NeckDeflatedProjectile", partKeyframes().rotate(linear(0.0F), linear(0.15F), catmullRom(0.3F, -28.64789F, 0.0F, 0.0F), catmullRom(0.45000002F, 28.64789F, 0.0F, 0.0F), catmullRom(0.6F, -25.210142F, 0.0F, 0.0F), catmullRom(0.75F, 8.594367F, 0.0F, 0.0F), catmullRom(0.9F, -8.594367F, 0.0F, 0.0F), catmullRom(1.0F)))
								.part("HeadDeflatedProjectile", partKeyframes().rotate(linear(0.0F), linear(0.15F), catmullRom(0.3F, -40.107044F, 0.0F, 0.0F), catmullRom(0.45000002F, 40.107044F, 0.0F, 0.0F), catmullRom(0.6F, -20.053522F, 0.0F, 0.0F), catmullRom(0.75F, 14.896902F, 0.0F, 0.0F), catmullRom(0.9F), linear(1.0F)))
								.part("Sensor2DeflatedProjectile", partKeyframes().rotate(linear(0.0F), linear(0.15F), catmullRom(0.3F, 91.67325F, 0.0F, -25.210142F), catmullRom(0.45000002F, 91.67325F, 0.0F, -25.210142F), catmullRom(0.6F), catmullRom(0.75F), linear(0.9F), linear(1.0F)))
				);

		this.endimation("puff_bug/pull")
				.keyframes(
						keyframes()
								.part("NeckDeflatedProjectile", partKeyframes().rotate(linear(0.0F), catmullRom(0.25F, 37.242256F, 0.0F, 0.0F), catmullRom(0.75F, 13.178029F, 0.0F, 0.0F), catmullRom(1.25F)))
								.part("HeadDeflatedProjectile", partKeyframes().rotate(linear(0.0F), catmullRom(0.25F, 22.918312F, 0.0F, 0.0F), catmullRom(0.75F, 5.729578F, 0.0F, 0.0F), catmullRom(1.25F)))
								.part("BodyDeflatedProjectile", partKeyframes().offset(linear(0.0F), catmullRom(0.25F, 0.0F, -0.08333333333F, 0.0F), catmullRom(0.75F, 0.0F, -0.04166666666F, 0.0F), catmullRom(1.25F)))
				);

		this.endimation("eetle/grow_up")
				.keyframes(
						keyframes()
								.part("body", partKeyframes().scale(linear(0.0F, 1.0F), catmullRom(0.25F, 1.2F, 1.2F, 1.2F), catmullRom(0.5F, 1.0F, 1.0F, 1.0F), catmullRom(0.75F, 1.25F, 1.25F, 1.25F), catmullRom(1.0F, 1.0F, 1.0F, 1.0F), catmullRom(1.5F, 1.5F, 1.5F, 1.5F)))
				);

		this.endimation("eetle/charger_attack")
				.keyframes(
						keyframes()
								.part("head", partKeyframes().rotate(linear(0.0F), catmullRom(0.1F, 14.323945F, 0.0F, 0.0F), catmullRom(0.25F, -42.971836F, 0.0F, 0.0F), catmullRom(0.5F)))
								.part("claw", partKeyframes().rotate(linear(0.0F), catmullRom(0.25F, -2.864789F, 0.0F, 0.0F), catmullRom(0.5F)))
								.part("mouth", partKeyframes().rotate(linear(0.0F), catmullRom(0.25F, -5.1566205F, 0.0F, 0.0F), catmullRom(0.5F)))
				);

		this.endimation("eetle/charger_catapult")
				.keyframes(
						keyframes()
								.part("head", partKeyframes().rotate(linear(0.0F), catmullRom(0.15F, 18.334648F, 0.0F, 0.0F), catmullRom(0.3F, -48.701412F, 0.0F, 0.0F), catmullRom(0.8F)))
								.part("claw", partKeyframes().rotate(linear(0.0F), catmullRom(0.3F, -2.864789F, 0.0F, 0.0F), catmullRom(0.8F)))
								.part("mouth", partKeyframes().rotate(linear(0.0F), catmullRom(0.3F, -5.1566205F, 0.0F, 0.0F), catmullRom(0.8F)))
								.part("leftWing", partKeyframes().rotate(linear(0.0F), catmullRom(0.3F, 18.621128F, 18.621128F, 0.0F), catmullRom(0.55F, 9.310564F, 9.310564F, 0.0F), catmullRom(0.8F)))
								.part("rightWing", partKeyframes().rotate(linear(0.0F), catmullRom(0.3F, 18.621128F, -18.621128F, 0.0F), catmullRom(0.55F, 9.310564F, -9.310564F, 0.0F), catmullRom(0.8F)))
				);

		this.endimation("eetle/charger_flap")
				.keyframes(
						keyframes()
								.part("leftWing", partKeyframes().rotate(linear(0.0F), catmullRom(0.25F, 14.896902F, 14.896902F, 0.0F), catmullRom(0.5F, 7.448451F, 7.448451F, 0.0F), catmullRom(0.75F, 14.896902F, 14.896902F, 0.0F), catmullRom(1.0F)))
								.part("rightWing", partKeyframes().rotate(linear(0.0F), catmullRom(0.25F, 14.896902F, -14.896902F, 0.0F), catmullRom(0.5F, 7.448451F, -7.448451F, 0.0F), catmullRom(0.75F, 14.896902F, -14.896902F, 0.0F), catmullRom(1.0F)))
				);

		this.endimation("eetle/glider_flap")
				.keyframes(
						keyframes()
								.part("rightElytron", partKeyframes().rotate(linear(0.0F), catmullRom(0.25F, 0.0F, -49.84733F, 20.053522F), catmullRom(0.35F, 0.0F, -46.58147F, 20.053522F), catmullRom(0.45F, 0.0F, -43.372906F, 20.053522F), catmullRom(0.55F, 0.0F, -40.107044F, 20.053522F), catmullRom(0.65000004F, 0.0F, -43.372906F, 20.053522F), catmullRom(0.75000006F, 0.0F, -46.58147F, 20.053522F), catmullRom(0.8500001F, 0.0F, -49.84733F, 20.053522F), catmullRom(1.1000001F)))
								.part("leftElytron", partKeyframes().rotate(linear(0.0F), catmullRom(0.25F, 0.0F, 49.84733F, -20.053522F), catmullRom(0.35F, 0.0F, 46.58147F, -20.053522F), catmullRom(0.45F, 0.0F, 43.372906F, -20.053522F), catmullRom(0.55F, 0.0F, 40.107044F, -20.053522F), catmullRom(0.65000004F, 0.0F, 43.372906F, -20.053522F), catmullRom(0.75000006F, 0.0F, 46.58147F, -20.053522F), catmullRom(0.8500001F, 0.0F, 49.84733F, -20.053522F), catmullRom(1.1000001F)))
								.part("leftWing", partKeyframes().rotate(linear(0.0F), catmullRom(0.25F, 6.8754935F, 20.053522F, 0.0F), catmullRom(0.35F, 9.740283F, 18.334648F, 0.0F), catmullRom(0.45F, 2.864789F, 16.615776F, 0.0F), catmullRom(0.55F, 6.8754935F, 14.896902F, 0.0F), catmullRom(0.65000004F, 2.864789F, 20.053522F, 0.0F), catmullRom(0.75000006F, 9.740283F, 20.053522F, 0.0F), catmullRom(0.8500001F, 6.8754935F, 20.053522F, 0.0F), catmullRom(1.1000001F)))
								.part("rightWing", partKeyframes().rotate(linear(0.0F), catmullRom(0.25F, 6.8754935F, -20.053522F, 0.0F), catmullRom(0.35F, 9.740283F, -18.334648F, 0.0F), catmullRom(0.45F, 2.864789F, -16.615776F, 0.0F), catmullRom(0.55F, 6.8754935F, -14.896902F, 0.0F), catmullRom(0.65000004F, 2.864789F, -20.053522F, 0.0F), catmullRom(0.75000006F, 9.740283F, -20.053522F, 0.0F), catmullRom(0.8500001F, 6.8754935F, -20.053522F, 0.0F), catmullRom(1.1000001F)))
				);

		this.endimation("eetle/glider_munch")
				.keyframes(
						keyframes()
								.part("leftMandible", partKeyframes().rotate(linear(0.0F), catmullRom(0.25F, 0.0F, -20.053522F, 0.0F), catmullRom(0.5F, 0.0F, 9.740283F, 0.0F), catmullRom(0.75F, 0.0F, -20.053522F, 0.0F), catmullRom(1.0F, 0.0F, 9.740283F, 0.0F), catmullRom(1.25F)))
								.part("rightMandible", partKeyframes().rotate(linear(0.0F), catmullRom(0.25F, 0.0F, 20.053522F, 0.0F), catmullRom(0.5F, 0.0F, -9.740283F, 0.0F), catmullRom(0.75F, 0.0F, 20.053522F, 0.0F), catmullRom(1.0F, 0.0F, -9.740283F, 0.0F), catmullRom(1.25F)))
				);

		this.endimation("eetle/brood_flap")
				.keyframes(
						keyframes()
								.part("wingRight", partKeyframes().rotate(linear(0.0F), catmullRom(0.25F, 6.8754935F, -34.950428F, 0.0F), catmullRom(0.35F, 9.740283F, -33.231552F, 0.0F), catmullRom(0.45F, 2.864789F, -31.51268F, 0.0F), catmullRom(0.55F, 6.8754935F, -29.793804F, 0.0F), catmullRom(0.65000004F, 2.864789F, -34.950428F, 0.0F), catmullRom(0.75000006F, 9.740283F, -34.950428F, 0.0F), catmullRom(0.8500001F, 6.8754935F, -34.950428F, 0.0F), catmullRom(1.1000001F)))
								.part("leftShell", partKeyframes().rotate(linear(0.0F), catmullRom(0.25F, 0.0F, 49.84733F, -20.053522F), catmullRom(0.35F, 0.0F, 46.58147F, -20.053522F), catmullRom(0.45F, 0.0F, 43.372906F, -20.053522F), catmullRom(0.55F, 0.0F, 40.107044F, -20.053522F), catmullRom(0.65000004F, 0.0F, 43.372906F, -20.053522F), catmullRom(0.75000006F, 0.0F, 46.58147F, -20.053522F), catmullRom(0.8500001F, 0.0F, 49.84733F, -20.053522F), catmullRom(1.1000001F)))
								.part("wingLeft", partKeyframes().rotate(linear(0.0F), catmullRom(0.25F, 6.8754935F, 34.950428F, 0.0F), catmullRom(0.35F, 9.740283F, 33.231552F, 0.0F), catmullRom(0.45F, 2.864789F, 31.51268F, 0.0F), catmullRom(0.55F, 6.8754935F, 29.793804F, 0.0F), catmullRom(0.65000004F, 2.864789F, 34.950428F, 0.0F), catmullRom(0.75000006F, 9.740283F, 34.950428F, 0.0F), catmullRom(0.8500001F, 6.8754935F, 34.950428F, 0.0F), catmullRom(1.1000001F)))
								.part("rightShell", partKeyframes().rotate(linear(0.0F), catmullRom(0.25F, 0.0F, -49.84733F, 20.053522F), catmullRom(0.35F, 0.0F, -46.58147F, 20.053522F), catmullRom(0.45F, 0.0F, -43.372906F, 20.053522F), catmullRom(0.55F, 0.0F, -40.107044F, 20.053522F), catmullRom(0.65000004F, 0.0F, -43.372906F, 20.053522F), catmullRom(0.75000006F, 0.0F, -46.58147F, 20.053522F), catmullRom(0.8500001F, 0.0F, -49.84733F, 20.053522F), catmullRom(1.1000001F)))
				);

		this.endimation("eetle/brood_munch")
				.keyframes(
						keyframes()
								.part("leftMandible", partKeyframes().rotate(linear(0.0F), catmullRom(0.25F, 0.0F, -20.053522F, 0.0F), catmullRom(0.5F, 0.0F, 9.740283F, 0.0F), catmullRom(0.75F, 0.0F, -20.053522F, 0.0F), catmullRom(1.0F, 0.0F, 9.740283F, 0.0F), catmullRom(1.25F)))
								.part("rightMandible", partKeyframes().rotate(linear(0.0F), catmullRom(0.25F, 0.0F, 20.053522F, 0.0F), catmullRom(0.5F, 0.0F, -9.740283F, 0.0F), catmullRom(0.75F, 0.0F, 20.053522F, 0.0F), catmullRom(1.0F, 0.0F, -9.740283F, 0.0F), catmullRom(1.25F)))
				);

		this.endimation("eetle/brood_attack")
				.keyframes(
						keyframes()
								.part("head", partKeyframes().rotate(linear(0.0F), catmullRom(0.2F, 44.69071F, 0.0F, 0.0F), catmullRom(0.35000002F, -14.896902F, 0.0F, 0.0F), catmullRom(0.6F)))
								.part("horn", partKeyframes().rotate(linear(0.0F), catmullRom(0.35000002F, 44.69071F, 0.0F, 0.0F), catmullRom(0.6F)))
				);

		this.endimation("eetle/brood_slam")
				.keyframes(
						keyframes()
								.part("head", partKeyframes().rotate(linear(0.0F), catmullRom(0.5F, -22.918312F, 0.0F, 0.0F), catmullRom(0.75F, 22.918312F, 0.0F, 0.0F), catmullRom(1.0F)))
								.part("leftFrontLeg", partKeyframes().pos(linear(0.0F), catmullRom(0.75F, 0.0F, -6.5F, 0.0F), catmullRom(1.0F)).rotate(linear(0.0F), catmullRom(0.5F, -62.30916F, 0.0F, -90.011665F), catmullRom(0.75F, -68.01009F, 63.02536F, -44.97719F), catmullRom(1.0F)))
								.part("rightFrontLeg", partKeyframes().pos(linear(0.0F), catmullRom(0.75F, 0.0F, -6.5F, 0.0F), catmullRom(1.0F)).rotate(linear(0.0F), catmullRom(0.5F, -62.30916F, 0.0F, 90.011665F), catmullRom(0.75F, -68.01009F, -63.02536F, 44.97719F), catmullRom(1.0F)))
								.part("leftFrontFoot", partKeyframes().rotate(linear(0.0F), catmullRom(0.75F, -17.761692F, 8.021409F, 29.793804F), catmullRom(1.0F)))
								.part("rightFrontFoot", partKeyframes().rotate(linear(0.0F), catmullRom(0.75F, -17.761692F, -8.021409F, -29.793804F), catmullRom(1.0F)))
				);

		this.endimation("eetle/brood_launch")
				.keyframes(
						keyframes()
								.part("eggSack", partKeyframes().scale(linear(0.0F, 1.0F, 1.0F, 1.0F), catmullRom(0.25F, 1.2F, 1.2F, 1.2F), catmullRom(0.45F, 1.1375F, 1.1375F, 1.1375F), catmullRom(0.9F, 1.0F, 1.0F, 1.0F)))
								.part("head", partKeyframes().rotate(linear(0.0F), catmullRom(0.25F, 17.188734F, 0.0F, 0.0F), catmullRom(0.45F, 4.2971835F, 0.0F, 0.0F), catmullRom(0.9F)))
								.part("eggMouthBottom", partKeyframes().rotate(linear(0.0F), catmullRom(0.25F, 0.0F, -9.740283F, 0.0F), catmullRom(0.7F, 0.0F, -4.0107045F, 0.0F), catmullRom(0.9F)))
								.part("eggMouthRight", partKeyframes().rotate(linear(0.0F), catmullRom(0.25F, 0.0F, -9.740283F, 0.0F), catmullRom(0.7F, 0.0F, -4.0107045F, 0.0F), catmullRom(0.9F)))
								.part("horn", partKeyframes().rotate(linear(0.0F), catmullRom(0.25F, 29.793804F, 0.0F, 0.0F), catmullRom(0.45F, 7.448451F, 0.0F, 0.0F), catmullRom(0.9F)))
								.part("egg", partKeyframes().rotate(linear(0.0F), catmullRom(0.25F, 15.469861F, 0.0F, 0.0F), catmullRom(0.7F, 7.448451F, 0.0F, 0.0F), catmullRom(0.9F)).scale(linear(0.0F, 1.0F, 1.0F, 1.0F), catmullRom(0.25F, 1.1F, 1.1F, 1.1F), catmullRom(0.9F, 1.0F, 1.0F, 1.0F)))
								.part("eggMouthTop", partKeyframes().rotate(linear(0.0F), catmullRom(0.25F, 0.0F, -9.740283F, 0.0F), catmullRom(0.7F, 0.0F, -4.0107045F, 0.0F), catmullRom(0.9F)))
								.part("eggMouthLeft", partKeyframes().rotate(linear(0.0F), catmullRom(0.25F, 0.0F, -9.740283F, 0.0F), catmullRom(0.7F, 0.0F, -4.0107045F, 0.0F), catmullRom(0.9F)))
				);

		this.endimation("eetle/brood_drop_eggs")
				.keyframes(
						keyframes()
								.part("eggSack", partKeyframes().scale(linear(0.0F, 1.0F, 1.0F, 1.0F), catmullRom(0.25F, 1.2F, 1.2F, 1.2F), catmullRom(0.45F, 1.1375F, 1.1375F, 1.1375F), catmullRom(0.9F, 1.0F, 1.0F, 1.0F)))
								.part("head", partKeyframes().rotate(linear(0.0F), catmullRom(0.25F, 17.188734F, 0.0F, 0.0F), catmullRom(0.45F, 4.2971835F, 0.0F, 0.0F), catmullRom(0.9F)))
								.part("eggMouthBottom", partKeyframes().rotate(linear(0.0F), catmullRom(0.25F, 0.0F, -9.740283F, 0.0F), catmullRom(0.7F, 0.0F, -4.0107045F, 0.0F), catmullRom(0.9F)))
								.part("eggMouthRight", partKeyframes().rotate(linear(0.0F), catmullRom(0.25F, 0.0F, -9.740283F, 0.0F), catmullRom(0.7F, 0.0F, -4.0107045F, 0.0F), catmullRom(0.9F)))
								.part("horn", partKeyframes().rotate(linear(0.0F), catmullRom(0.25F, 29.793804F, 0.0F, 0.0F), catmullRom(0.45F, 7.448451F, 0.0F, 0.0F), catmullRom(0.9F)))
								.part("egg", partKeyframes().rotate(linear(0.0F), catmullRom(0.25F, -18.563833F, 0.0F, 0.0F), catmullRom(0.7F, -8.938142F, 0.0F, 0.0F), catmullRom(0.9F)).scale(linear(0.0F, 1.0F, 1.0F, 1.0F), catmullRom(0.25F, 1.1F, 1.1F, 1.1F), catmullRom(0.9F, 1.0F, 1.0F, 1.0F)))
								.part("eggMouthTop", partKeyframes().rotate(linear(0.0F), catmullRom(0.25F, 0.0F, -9.740283F, 0.0F), catmullRom(0.7F, 0.0F, -4.0107045F, 0.0F), catmullRom(0.9F)))
								.part("eggMouthLeft", partKeyframes().rotate(linear(0.0F), catmullRom(0.25F, 0.0F, -9.740283F, 0.0F), catmullRom(0.7F, 0.0F, -4.0107045F, 0.0F), catmullRom(0.9F)))
				);

		this.endimation("eetle/brood_air_charge")
				.keyframes(
						keyframes()
								.part("head", partKeyframes().rotate(linear(0.0F), catmullRom(0.5F, -22.918312F, 0.0F, 0.0F), catmullRom(3.5F, -22.918312F, 0.0F, 0.0F), catmullRom(4.0F)))
								.part("leftFrontLeg", partKeyframes().rotate(linear(0.0F), catmullRom(0.5F, -62.30916F, 0.0F, -90.011665F), catmullRom(3.5F, -62.30916F, 0.0F, -90.011665F), catmullRom(4.0F)))
								.part("rightFrontLeg", partKeyframes().rotate(linear(0.0F), catmullRom(0.5F, -62.30916F, 0.0F, 90.011665F), catmullRom(3.5F, -62.30916F, 0.0F, 90.011665F), catmullRom(4.0F)))
				);

		this.endimation("eetle/brood_air_slam")
				.keyframes(
						keyframes()
								.part("head", partKeyframes().rotate(linear(0.0F), catmullRom(0.05F, -22.918312F, 0.0F, 0.0F), catmullRom(0.3F, 22.918312F, 0.0F, 0.0F), catmullRom(0.55F)))
								.part("leftFrontLeg", partKeyframes().pos(linear(0.0F), catmullRom(0.3F, 0.0F, -6.5F, 0.0F), catmullRom(0.55F)).rotate(linear(0.0F), catmullRom(0.05F, -62.30916F, 0.0F, -90.011665F), catmullRom(0.3F, -68.01009F, 63.02536F, -44.97719F), catmullRom(0.55F)))
								.part("rightFrontLeg", partKeyframes().pos(linear(0.0F), catmullRom(0.3F, 0.0F, -6.5F, 0.0F), catmullRom(0.55F)).rotate(linear(0.0F), catmullRom(0.05F, -62.30916F, 0.0F, 90.011665F), catmullRom(0.3F, -68.01009F, -63.02536F, 44.97719F), catmullRom(0.55F)))
								.part("leftFrontFoot", partKeyframes().rotate(linear(0.0F), catmullRom(0.3F, -17.761692F, 8.021409F, 29.793804F), catmullRom(0.55F)))
								.part("rightFrontFoot", partKeyframes().rotate(linear(0.0F), catmullRom(0.3F, -17.761692F, -8.021409F, -29.793804F), catmullRom(0.55F)))
				);

		this.endimation("eetle/brood_death_left")
				.keyframes(
						keyframes()
								.part("head", partKeyframes().rotate(linear(0.0F), catmullRom(1.0F, 25.210142F, 0.0F, -9.740283F), catmullRom(2.75F, 25.210142F, 0.0F, -9.740283F), catmullRom(3.0F, 25.210142F, 0.0F, -9.740283F), catmullRom(3.25F, 25.210142F, 0.0F, -9.740283F), catmullRom(3.5F, 25.210142F, 0.0F, -9.740283F), catmullRom(3.75F, 25.210142F, 0.0F, -9.740283F), catmullRom(4.0F, 25.210142F, 0.0F, -9.740283F), catmullRom(4.25F, 25.210142F, 0.0F, -9.740283F), catmullRom(4.5F, 25.210142F, 0.0F, -9.740283F), catmullRom(4.75F, 25.210142F, 0.0F, -9.740283F), catmullRom(5.5F, 25.210142F, 0.0F, -9.740283F), catmullRom(5.75F, 25.210142F, 0.0F, -9.740283F)))
								.part("eggSack", partKeyframes().scale(linear(0.0F, 1.0F, 1.0F, 1.0F), catmullRom(3.0F, 1.175F, 1.175F, 1.175F), catmullRom(3.25F, 1.0F, 1.0F, 1.0F), catmullRom(3.5F, 1.19F, 1.19F, 1.19F), catmullRom(3.75F, 1.0F, 1.0F, 1.0F), catmullRom(4.0F, 1.1800001F, 1.1800001F, 1.1800001F), catmullRom(4.25F, 1.0F, 1.0F, 1.0F), catmullRom(4.5F, 1.225F, 1.225F, 1.225F), catmullRom(4.75F, 1.0F, 1.0F, 1.0F), catmullRom(5.5F, 1.275F, 1.275F, 1.275F), catmullRom(5.75F, 1.275F, 1.275F, 1.275F)))
								.part("leftFrontLeg", partKeyframes().rotate(linear(0.0F), catmullRom(1.0F, -40.107044F, 0.0F, -32.658592F), catmullRom(2.75F, -40.107044F, 0.0F, -32.658592F), catmullRom(3.0F, -40.107044F, 0.0F, -32.658592F), catmullRom(3.25F, -40.107044F, 0.0F, -32.658592F), catmullRom(3.5F, -40.107044F, 0.0F, -32.658592F), catmullRom(3.75F, -40.107044F, 0.0F, -32.658592F), catmullRom(4.0F, -40.107044F, 0.0F, -32.658592F), catmullRom(4.25F, -40.107044F, 0.0F, -32.658592F), catmullRom(4.5F, -40.107044F, 0.0F, -32.658592F), catmullRom(4.75F, -40.107044F, 0.0F, -32.658592F), catmullRom(5.5F, -40.107044F, 0.0F, -32.658592F), catmullRom(5.75F, -40.107044F, 0.0F, -32.658592F)))
								.part("egg", partKeyframes().scale(linear(0.0F, 1.0F, 1.0F, 1.0F), catmullRom(5.5F, 1.3F, 1.3F, 1.3F), catmullRom(5.75F, 1.3F, 1.3F, 1.3F)))
								.part("leftBackLeg", partKeyframes().rotate(linear(0.0F), catmullRom(1.0F, 10.313241F, 30.939722F, -18.907608F), catmullRom(2.75F, 10.313241F, 30.939722F, -18.907608F), catmullRom(3.0F, 10.313241F, 30.939722F, -18.907608F), catmullRom(3.25F, 10.313241F, 30.939722F, -18.907608F), catmullRom(3.5F, 10.313241F, 30.939722F, -18.907608F), catmullRom(3.75F, 10.313241F, 30.939722F, -18.907608F), catmullRom(4.0F, 10.313241F, 30.939722F, -18.907608F), catmullRom(4.25F, 10.313241F, 30.939722F, -18.907608F), catmullRom(4.5F, 10.313241F, 30.939722F, -18.907608F), catmullRom(4.75F, 10.313241F, 30.939722F, -18.907608F), catmullRom(5.5F, 10.313241F, 30.939722F, -18.907608F), catmullRom(5.75F, 10.313241F, 30.939722F, -18.907608F)))
								.part("rightFrontLeg", partKeyframes().rotate(linear(0.0F), catmullRom(1.0F, -40.107044F, 0.0F, 32.658592F), catmullRom(2.75F, -40.107044F, 0.0F, 32.658592F), catmullRom(3.0F, -40.107044F, 0.0F, 32.658592F), catmullRom(3.25F, -40.107044F, 0.0F, 32.658592F), catmullRom(3.5F, -40.107044F, 0.0F, 32.658592F), catmullRom(3.75F, -40.107044F, 0.0F, 32.658592F), catmullRom(4.0F, -40.107044F, 0.0F, 32.658592F), catmullRom(4.25F, -40.107044F, 0.0F, 32.658592F), catmullRom(4.5F, -40.107044F, 0.0F, 32.658592F), catmullRom(4.75F, -40.107044F, 0.0F, 32.658592F), catmullRom(5.5F, -40.107044F, 0.0F, 32.658592F), catmullRom(5.75F, -40.107044F, 0.0F, 32.658592F)))
								.part("rightBackLeg", partKeyframes().rotate(linear(0.0F), catmullRom(1.0F, 10.313241F, -30.939722F, 18.907608F), catmullRom(2.75F, 10.313241F, -30.939722F, 18.907608F), catmullRom(3.0F, 10.313241F, -30.939722F, 18.907608F), catmullRom(3.25F, 10.313241F, -30.939722F, 18.907608F), catmullRom(3.5F, 10.313241F, -30.939722F, 18.907608F), catmullRom(3.75F, 10.313241F, -30.939722F, 18.907608F), catmullRom(4.0F, 10.313241F, -30.939722F, 18.907608F), catmullRom(4.25F, 10.313241F, -30.939722F, 18.907608F), catmullRom(4.5F, 10.313241F, -30.939722F, 18.907608F), catmullRom(4.75F, 10.313241F, -30.939722F, 18.907608F), catmullRom(5.5F, 10.313241F, -30.939722F, 18.907608F), catmullRom(5.75F, 10.313241F, -30.939722F, 18.907608F)))
								.part("body", partKeyframes().pos(linear(0.0F), catmullRom(1.0F, 0.0F, -7.0F, 0.0F), catmullRom(2.75F, 0.0F, -7.0F, 0.0F), catmullRom(3.0F, 0.0F, -7.0F, 0.0F), catmullRom(3.25F, 0.0F, -7.0F, 0.0F), catmullRom(3.5F, 0.0F, -7.0F, 0.0F), catmullRom(3.75F, 0.0F, -7.0F, 0.0F), catmullRom(4.0F, 0.0F, -7.0F, 0.0F), catmullRom(4.25F, 0.0F, -7.0F, 0.0F), catmullRom(4.5F, 0.0F, -7.0F, 0.0F), catmullRom(4.75F, 0.0F, -7.0F, 0.0F), catmullRom(5.5F, 0.0F, -7.0F, 0.0F), catmullRom(5.75F, 0.0F, -7.0F, 0.0F)))
				);

		this.endimation("eetle/brood_death_right")
				.keyframes(
						keyframes()
								.part("head", partKeyframes().rotate(linear(0.0F), catmullRom(1.0F, 25.210142F, 0.0F, 9.740283F), catmullRom(2.75F, 25.210142F, 0.0F, 9.740283F), catmullRom(3.0F, 25.210142F, 0.0F, 9.740283F), catmullRom(3.25F, 25.210142F, 0.0F, 9.740283F), catmullRom(3.5F, 25.210142F, 0.0F, 9.740283F), catmullRom(3.75F, 25.210142F, 0.0F, 9.740283F), catmullRom(4.0F, 25.210142F, 0.0F, 9.740283F), catmullRom(4.25F, 25.210142F, 0.0F, 9.740283F), catmullRom(4.5F, 25.210142F, 0.0F, 9.740283F), catmullRom(4.75F, 25.210142F, 0.0F, 9.740283F), catmullRom(5.5F, 25.210142F, 0.0F, 9.740283F), catmullRom(5.75F, 25.210142F, 0.0F, 9.740283F)))
								.part("eggSack", partKeyframes().scale(linear(0.0F, 1.0F, 1.0F, 1.0F), catmullRom(3.0F, 1.175F, 1.175F, 1.175F), catmullRom(3.25F, 1.0F, 1.0F, 1.0F), catmullRom(3.5F, 1.19F, 1.19F, 1.19F), catmullRom(3.75F, 1.0F, 1.0F, 1.0F), catmullRom(4.0F, 1.1800001F, 1.1800001F, 1.1800001F), catmullRom(4.25F, 1.0F, 1.0F, 1.0F), catmullRom(4.5F, 1.225F, 1.225F, 1.225F), catmullRom(4.75F, 1.0F, 1.0F, 1.0F), catmullRom(5.5F, 1.275F, 1.275F, 1.275F), catmullRom(5.75F, 1.275F, 1.275F, 1.275F)))
								.part("leftFrontLeg", partKeyframes().rotate(linear(0.0F), catmullRom(1.0F, -40.107044F, 0.0F, -32.658592F), catmullRom(2.75F, -40.107044F, 0.0F, -32.658592F), catmullRom(3.0F, -40.107044F, 0.0F, -32.658592F), catmullRom(3.25F, -40.107044F, 0.0F, -32.658592F), catmullRom(3.5F, -40.107044F, 0.0F, -32.658592F), catmullRom(3.75F, -40.107044F, 0.0F, -32.658592F), catmullRom(4.0F, -40.107044F, 0.0F, -32.658592F), catmullRom(4.25F, -40.107044F, 0.0F, -32.658592F), catmullRom(4.5F, -40.107044F, 0.0F, -32.658592F), catmullRom(4.75F, -40.107044F, 0.0F, -32.658592F), catmullRom(5.5F, -40.107044F, 0.0F, -32.658592F), catmullRom(5.75F, -40.107044F, 0.0F, -32.658592F)))
								.part("egg", partKeyframes().scale(linear(0.0F, 1.0F, 1.0F, 1.0F), catmullRom(5.5F, 1.3F, 1.3F, 1.3F), catmullRom(5.75F, 1.3F, 1.3F, 1.3F)))
								.part("leftBackLeg", partKeyframes().rotate(linear(0.0F), catmullRom(1.0F, 10.313241F, 30.939722F, -18.907608F), catmullRom(2.75F, 10.313241F, 30.939722F, -18.907608F), catmullRom(3.0F, 10.313241F, 30.939722F, -18.907608F), catmullRom(3.25F, 10.313241F, 30.939722F, -18.907608F), catmullRom(3.5F, 10.313241F, 30.939722F, -18.907608F), catmullRom(3.75F, 10.313241F, 30.939722F, -18.907608F), catmullRom(4.0F, 10.313241F, 30.939722F, -18.907608F), catmullRom(4.25F, 10.313241F, 30.939722F, -18.907608F), catmullRom(4.5F, 10.313241F, 30.939722F, -18.907608F), catmullRom(4.75F, 10.313241F, 30.939722F, -18.907608F), catmullRom(5.5F, 10.313241F, 30.939722F, -18.907608F), catmullRom(5.75F, 10.313241F, 30.939722F, -18.907608F)))
								.part("rightFrontLeg", partKeyframes().rotate(linear(0.0F), catmullRom(1.0F, -40.107044F, 0.0F, 32.658592F), catmullRom(2.75F, -40.107044F, 0.0F, 32.658592F), catmullRom(3.0F, -40.107044F, 0.0F, 32.658592F), catmullRom(3.25F, -40.107044F, 0.0F, 32.658592F), catmullRom(3.5F, -40.107044F, 0.0F, 32.658592F), catmullRom(3.75F, -40.107044F, 0.0F, 32.658592F), catmullRom(4.0F, -40.107044F, 0.0F, 32.658592F), catmullRom(4.25F, -40.107044F, 0.0F, 32.658592F), catmullRom(4.5F, -40.107044F, 0.0F, 32.658592F), catmullRom(4.75F, -40.107044F, 0.0F, 32.658592F), catmullRom(5.5F, -40.107044F, 0.0F, 32.658592F), catmullRom(5.75F, -40.107044F, 0.0F, 32.658592F)))
								.part("rightBackLeg", partKeyframes().rotate(linear(0.0F), catmullRom(1.0F, 10.313241F, -30.939722F, 18.907608F), catmullRom(2.75F, 10.313241F, -30.939722F, 18.907608F), catmullRom(3.0F, 10.313241F, -30.939722F, 18.907608F), catmullRom(3.25F, 10.313241F, -30.939722F, 18.907608F), catmullRom(3.5F, 10.313241F, -30.939722F, 18.907608F), catmullRom(3.75F, 10.313241F, -30.939722F, 18.907608F), catmullRom(4.0F, 10.313241F, -30.939722F, 18.907608F), catmullRom(4.25F, 10.313241F, -30.939722F, 18.907608F), catmullRom(4.5F, 10.313241F, -30.939722F, 18.907608F), catmullRom(4.75F, 10.313241F, -30.939722F, 18.907608F), catmullRom(5.5F, 10.313241F, -30.939722F, 18.907608F), catmullRom(5.75F, 10.313241F, -30.939722F, 18.907608F)))
								.part("body", partKeyframes().pos(linear(0.0F), catmullRom(1.0F, 0.0F, -7.0F, 0.0F), catmullRom(2.75F, 0.0F, -7.0F, 0.0F), catmullRom(3.0F, 0.0F, -7.0F, 0.0F), catmullRom(3.25F, 0.0F, -7.0F, 0.0F), catmullRom(3.5F, 0.0F, -7.0F, 0.0F), catmullRom(3.75F, 0.0F, -7.0F, 0.0F), catmullRom(4.0F, 0.0F, -7.0F, 0.0F), catmullRom(4.25F, 0.0F, -7.0F, 0.0F), catmullRom(4.5F, 0.0F, -7.0F, 0.0F), catmullRom(4.75F, 0.0F, -7.0F, 0.0F), catmullRom(5.5F, 0.0F, -7.0F, 0.0F), catmullRom(5.75F, 0.0F, -7.0F, 0.0F)))
				);

		this.endimation("purpoid/teleport_to")
				.keyframes(
						keyframes()
								.part("head", partKeyframes().scale(linear(0.0F, 1.0F, 1.0F, 1.0F), catmullRom(0.25F, 2.3F, 2.3F, 2.3F), linear(0.5F), linear(0.9F)))
								.part("tentacleLarge3", partKeyframes().rotate(linear(0.0F), catmullRom(0.25F, 14.896902F, 0.0F, 0.0F), catmullRom(0.5F, -14.896902F, 0.0F, 0.0F), catmullRom(0.9F, -14.896902F, 0.0F, 0.0F)))
								.part("tentacleSmall3", partKeyframes().rotate(linear(0.0F), catmullRom(0.25F, -14.896902F, 0.0F, 0.0F), catmullRom(0.5F, 14.896902F, 0.0F, 0.0F), catmullRom(0.9F, 14.896902F, 0.0F, 0.0F)))
								.part("tentacleLarge4", partKeyframes().rotate(linear(0.0F), catmullRom(0.25F, -14.896902F, 0.0F, 0.0F), catmullRom(0.5F, 14.896902F, 0.0F, 0.0F), catmullRom(0.9F, 14.896902F, 0.0F, 0.0F)))
								.part("tentacleSmall4", partKeyframes().rotate(linear(0.0F), catmullRom(0.25F, -14.896902F, 0.0F, 0.0F), catmullRom(0.5F, 14.896902F, 0.0F, 0.0F), catmullRom(0.9F, 14.896902F, 0.0F, 0.0F)))
								.part("tentacleLarge1", partKeyframes().rotate(linear(0.0F), catmullRom(0.25F, -14.896902F, 0.0F, 0.0F), catmullRom(0.5F, 14.896902F, 0.0F, 0.0F), catmullRom(0.9F, 14.896902F, 0.0F, 0.0F)))
								.part("tentacleSmall1", partKeyframes().rotate(linear(0.0F), catmullRom(0.25F, 14.896902F, 0.0F, 0.0F), catmullRom(0.5F, -14.896902F, 0.0F, 0.0F), catmullRom(0.9F, -14.896902F, 0.0F, 0.0F)))
								.part("tentacleLarge2", partKeyframes().rotate(linear(0.0F), catmullRom(0.25F, 14.896902F, 0.0F, 0.0F), catmullRom(0.5F, -14.896902F, 0.0F, 0.0F), catmullRom(0.9F, -14.896902F, 0.0F, 0.0F)))
								.part("tentacleSmall2", partKeyframes().rotate(linear(0.0F), catmullRom(0.25F, 14.896902F, 0.0F, 0.0F), catmullRom(0.5F, -14.896902F, 0.0F, 0.0F), catmullRom(0.9F, -14.896902F, 0.0F, 0.0F)))
								.part("gelLayer", partKeyframes().scale(linear(0.0F, 1.0F, 1.0F, 1.0F), catmullRom(0.25F, 2.3F, 2.3F, 2.3F), linear(0.5F), linear(0.9F)))
				);

		this.endimation("purpoid/fast_teleport_to")
				.keyframes(
						keyframes()
								.part("head", partKeyframes().scale(linear(0.0F, 1.0F, 1.0F, 1.0F), catmullRom(0.25F, 2.3F, 2.3F, 2.3F), linear(0.5F), linear(0.75F)))
								.part("tentacleLarge3", partKeyframes().rotate(linear(0.0F), catmullRom(0.25F, 14.896902F, 0.0F, 0.0F), catmullRom(0.5F, -14.896902F, 0.0F, 0.0F), catmullRom(0.75F, -14.896902F, 0.0F, 0.0F)))
								.part("tentacleSmall3", partKeyframes().rotate(linear(0.0F), catmullRom(0.25F, -14.896902F, 0.0F, 0.0F), catmullRom(0.5F, 14.896902F, 0.0F, 0.0F), catmullRom(0.75F, 14.896902F, 0.0F, 0.0F)))
								.part("tentacleLarge4", partKeyframes().rotate(linear(0.0F), catmullRom(0.25F, -14.896902F, 0.0F, 0.0F), catmullRom(0.5F, 14.896902F, 0.0F, 0.0F), catmullRom(0.75F, 14.896902F, 0.0F, 0.0F)))
								.part("tentacleSmall4", partKeyframes().rotate(linear(0.0F), catmullRom(0.25F, -14.896902F, 0.0F, 0.0F), catmullRom(0.5F, 14.896902F, 0.0F, 0.0F), catmullRom(0.75F, 14.896902F, 0.0F, 0.0F)))
								.part("tentacleLarge1", partKeyframes().rotate(linear(0.0F), catmullRom(0.25F, -14.896902F, 0.0F, 0.0F), catmullRom(0.5F, 14.896902F, 0.0F, 0.0F), catmullRom(0.75F, 14.896902F, 0.0F, 0.0F)))
								.part("tentacleSmall1", partKeyframes().rotate(linear(0.0F), catmullRom(0.25F, 14.896902F, 0.0F, 0.0F), catmullRom(0.5F, -14.896902F, 0.0F, 0.0F), catmullRom(0.75F, -14.896902F, 0.0F, 0.0F)))
								.part("tentacleLarge2", partKeyframes().rotate(linear(0.0F), catmullRom(0.25F, 14.896902F, 0.0F, 0.0F), catmullRom(0.5F, -14.896902F, 0.0F, 0.0F), catmullRom(0.75F, -14.896902F, 0.0F, 0.0F)))
								.part("tentacleSmall2", partKeyframes().rotate(linear(0.0F), catmullRom(0.25F, 14.896902F, 0.0F, 0.0F), catmullRom(0.5F, -14.896902F, 0.0F, 0.0F), catmullRom(0.75F, -14.896902F, 0.0F, 0.0F)))
								.part("gelLayer", partKeyframes().scale(linear(0.0F, 1.0F, 1.0F, 1.0F), catmullRom(0.25F, 2.3F, 2.3F, 2.3F), linear(0.5F), linear(0.75F)))
				);

		this.endimation("purpoid/teleport_from")
				.keyframes(
						keyframes()
								.part("head", partKeyframes().scale(linear(0.0F, 1.0F, 1.0F, 1.0F), catmullRom(0.25F, 2.3F, 2.3F, 2.3F), catmullRom(0.5F, 1.0F, 1.0F, 1.0F)))
								.part("tentacleLarge3", partKeyframes().rotate(linear(0.0F), catmullRom(0.25F, 14.896902F, 0.0F, 0.0F), catmullRom(0.5F)))
								.part("tentacleSmall3", partKeyframes().rotate(linear(0.0F), catmullRom(0.25F, -14.896902F, 0.0F, 0.0F), catmullRom(0.5F)))
								.part("tentacleLarge4", partKeyframes().rotate(linear(0.0F), catmullRom(0.25F, -14.896902F, 0.0F, 0.0F), catmullRom(0.5F)))
								.part("tentacleSmall4", partKeyframes().rotate(linear(0.0F), catmullRom(0.25F, -14.896902F, 0.0F, 0.0F), catmullRom(0.5F)))
								.part("tentacleLarge1", partKeyframes().rotate(linear(0.0F), catmullRom(0.25F, -14.896902F, 0.0F, 0.0F), catmullRom(0.5F)))
								.part("tentacleSmall1", partKeyframes().rotate(linear(0.0F), catmullRom(0.25F, 14.896902F, 0.0F, 0.0F), catmullRom(0.5F)))
								.part("tentacleLarge2", partKeyframes().rotate(linear(0.0F), catmullRom(0.25F, 14.896902F, 0.0F, 0.0F), catmullRom(0.5F)))
								.part("tentacleSmall2", partKeyframes().rotate(linear(0.0F), catmullRom(0.25F, 14.896902F, 0.0F, 0.0F), catmullRom(0.5F)))
								.part("gelLayer", partKeyframes().scale(linear(0.0F, 1.0F, 1.0F, 1.0F), catmullRom(0.25F, 2.3F, 2.3F, 2.3F), catmullRom(0.5F, 1.0F, 1.0F, 1.0F)))
				);

		this.endimation("purpoid/telefrag")
				.keyframes(
						keyframes()
								.part("head", partKeyframes().scale(linear(0.0F, 1.0F, 1.0F, 1.0F), catmullRom(0.25F, 2.0F, 2.0F, 2.0F), catmullRom(0.5F, 1.0F, 1.0F, 1.0F)))
								.part("tentacleLarge3", partKeyframes().rotate(linear(0.0F), catmullRom(0.25F, 14.896902F, 0.0F, 0.0F), catmullRom(0.5F)))
								.part("tentacleSmall3", partKeyframes().rotate(linear(0.0F), catmullRom(0.25F, -14.896902F, 0.0F, 0.0F), catmullRom(0.5F)))
								.part("tentacleLarge4", partKeyframes().rotate(linear(0.0F), catmullRom(0.25F, -14.896902F, 0.0F, 0.0F), catmullRom(0.5F)))
								.part("tentacleSmall4", partKeyframes().rotate(linear(0.0F), catmullRom(0.25F, -14.896902F, 0.0F, 0.0F), catmullRom(0.5F)))
								.part("tentacleLarge1", partKeyframes().rotate(linear(0.0F), catmullRom(0.25F, -14.896902F, 0.0F, 0.0F), catmullRom(0.5F)))
								.part("tentacleSmall1", partKeyframes().rotate(linear(0.0F), catmullRom(0.25F, 14.896902F, 0.0F, 0.0F), catmullRom(0.5F)))
								.part("tentacleLarge2", partKeyframes().rotate(linear(0.0F), catmullRom(0.25F, 14.896902F, 0.0F, 0.0F), catmullRom(0.5F)))
								.part("tentacleSmall2", partKeyframes().rotate(linear(0.0F), catmullRom(0.25F, 14.896902F, 0.0F, 0.0F), catmullRom(0.5F)))
								.part("gelLayer", partKeyframes().scale(linear(0.0F, 1.0F, 1.0F, 1.0F), catmullRom(0.25F, 2.0F, 2.0F, 2.0F), catmullRom(0.5F, 1.0F, 1.0F, 1.0F)))
				);

		this.endimation("purpoid/death")
				.keyframes(
						keyframes()
								.part("head", partKeyframes().scale(linear(0.0F, 1.0F, 1.0F, 1.0F), catmullRom(0.25F, 0.6F, 0.6F, 0.6F), catmullRom(0.5F, 1.9F, 1.9F, 1.9F), catmullRom(1.0F, 1.9F, 1.9F, 1.9F)))
								.part("tentacleLarge3", partKeyframes().rotate(linear(0.0F), catmullRom(0.25F, -14.896902F, 0.0F, 0.0F), catmullRom(0.5F, 14.896902F, 0.0F, 0.0F), catmullRom(1.0F, 14.896902F, 0.0F, 0.0F)))
								.part("tentacleSmall3", partKeyframes().rotate(linear(0.0F), catmullRom(0.25F, 14.896902F, 0.0F, 0.0F), catmullRom(0.5F, -14.896902F, 0.0F, 0.0F), catmullRom(1.0F, -14.896902F, 0.0F, 0.0F)))
								.part("tentacleLarge4", partKeyframes().rotate(linear(0.0F), catmullRom(0.25F, 14.896902F, 0.0F, 0.0F), catmullRom(0.5F, -14.896902F, 0.0F, 0.0F), catmullRom(1.0F, -14.896902F, 0.0F, 0.0F)))
								.part("tentacleSmall4", partKeyframes().rotate(linear(0.0F), catmullRom(0.25F, 14.896902F, 0.0F, 0.0F), catmullRom(0.5F, -14.896902F, 0.0F, 0.0F), catmullRom(1.0F, -14.896902F, 0.0F, 0.0F)))
								.part("tentacleLarge1", partKeyframes().rotate(linear(0.0F), catmullRom(0.25F, 14.896902F, 0.0F, 0.0F), catmullRom(0.5F, -14.896902F, 0.0F, 0.0F), catmullRom(1.0F, -14.896902F, 0.0F, 0.0F)))
								.part("tentacleSmall1", partKeyframes().rotate(linear(0.0F), catmullRom(0.25F, -14.896902F, 0.0F, 0.0F), catmullRom(0.5F, 14.896902F, 0.0F, 0.0F), catmullRom(1.0F, 14.896902F, 0.0F, 0.0F)))
								.part("tentacleLarge2", partKeyframes().rotate(linear(0.0F), catmullRom(0.25F, -14.896902F, 0.0F, 0.0F), catmullRom(0.5F, 14.896902F, 0.0F, 0.0F), catmullRom(1.0F, 14.896902F, 0.0F, 0.0F)))
								.part("tentacleSmall2", partKeyframes().rotate(linear(0.0F), catmullRom(0.25F, -14.896902F, 0.0F, 0.0F), catmullRom(0.5F, 14.896902F, 0.0F, 0.0F), catmullRom(1.0F, 14.896902F, 0.0F, 0.0F)))
								.part("gelLayer", partKeyframes().scale(linear(0.0F, 1.0F, 1.0F, 1.0F), catmullRom(0.25F, 0.6F, 0.6F, 0.6F), catmullRom(0.5F, 2.15F, 2.0F, 2.15F), catmullRom(1.0F, 2.15F, 2.0F, 2.15F)))
				);
	}

	public static EndimationKeyframe linear(float time, float f) {
		return linear(time, f, f, f);
	}

}
