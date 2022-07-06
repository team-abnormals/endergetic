package com.minecraftabnormals.endergetic.core;

import com.minecraftabnormals.endergetic.core.data.client.EEEndimationProvider;
import com.teamabnormals.blueprint.core.endimator.EndimationKeyframe;
import com.teamabnormals.blueprint.core.endimator.interpolation.InterpolationType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class OldEndimatorToEndimation {
	private static final Pattern KEYFRAME = Pattern.compile("(startKeyframe|resetKeyframe|setStaticKeyframe)\\((\\d+)\\)");
	private static final Pattern POSE = Pattern.compile("(((move|rotate|rotateAdditive|scale|offset)\\((\\w*), ([+-]?([0-9]+\\.?[0-9]*|\\.[0-9]+))F, ([+-]?([0-9]+\\.?[0-9]*|\\.[0-9]+))F, ([+-]?([0-9]+\\.?[0-9]*|\\.[0-9]+))F\\))|(endKeyframe))");

	public static void main(String[] args) {
		System.out.println(getCodeFor("startKeyframe(10);\n" +
				"\t\t\trotate(Jaw, 0.25F, 0.0F, 0.0F);\n" +
				"\t\t\tendKeyframe();\n" +
				"\n" +
				"\t\t\tsetStaticKeyframe(45);\n" +
				"\n" +
				"\t\t\tstartKeyframe(5);\n" +
				"\t\t\trotate(Jaw, -0.0F, 0.0F, 0.0F);\n" +
				"\t\t\tendKeyframe();"));
	}

	public static String getCodeFor(String oldInstructionsCode) {
		float time = 0.0F;
		Map<String, PartKeyframes> partKeyframes = new HashMap<>();
		Matcher matcher = KEYFRAME.matcher(oldInstructionsCode);
		Matcher poseMatcher = POSE.matcher(oldInstructionsCode);
		while (matcher.find()) {
			String ticks = matcher.group(2);
			time += Integer.parseInt(ticks) / 20.0F;
			String type = matcher.group(1);
			if (type.equals("startKeyframe")) {
				while (poseMatcher.find()) {
					String last = poseMatcher.group(11);
					if (last != null) break;
					PartKeyframes part = get(partKeyframes, poseMatcher.group(4));
					String keyframeType = poseMatcher.group(3);
					float x = Float.parseFloat(poseMatcher.group(5));
					float y = Float.parseFloat(poseMatcher.group(7));
					float z = Float.parseFloat(poseMatcher.group(9));
					if (keyframeType.equals("move")) {
						ArrayList<EndimationKeyframe> posFrames = part.pos;
						if (posFrames.isEmpty()) {
							posFrames.add(EEEndimationProvider.linear(0.0F));
						}
						posFrames.add(EEEndimationProvider.catmullRom(time, x, -y, z));
					} else if (keyframeType.equals("rotate") || keyframeType.equals("rotateAdditive")) {
						ArrayList<EndimationKeyframe> rotateFrames = part.rotate;
						if (rotateFrames.isEmpty()) {
							rotateFrames.add(EEEndimationProvider.linear(0.0F));
						}
						rotateFrames.add(EEEndimationProvider.catmullRom(time, (float) Math.toDegrees(x), (float) Math.toDegrees(y), (float) Math.toDegrees(z)));
					} else if (keyframeType.equals("scale")) {
						ArrayList<EndimationKeyframe> scaleFrames = part.scale;
						if (scaleFrames.isEmpty()) {
							scaleFrames.add(EEEndimationProvider.linear(0.0F, 1.0F, 1.0F, 1.0F));
						}
						x += 1.0F;
						y += 1.0F;
						z += 1.0F;
						scaleFrames.add(EEEndimationProvider.catmullRom(time, x, y, z));
					} else if (keyframeType.equals("offset")) {
						ArrayList<EndimationKeyframe> offsetFrames = part.offset;
						if (offsetFrames.isEmpty()) {
							offsetFrames.add(EEEndimationProvider.linear(0.0F));
						}
						offsetFrames.add(EEEndimationProvider.catmullRom(time, x, y, z));
					}
				}
			} else if (type.equals("resetKeyframe")) {
				for (var entry : partKeyframes.entrySet()) {
					var keyframes = entry.getValue();
					var pos = keyframes.pos;
					if (!pos.isEmpty()) pos.add(EEEndimationProvider.catmullRom(time));
					var rotate = keyframes.rotate;
					if (!rotate.isEmpty()) rotate.add(EEEndimationProvider.catmullRom(time));
					var scale = keyframes.scale;
					if (!scale.isEmpty()) scale.add(EEEndimationProvider.catmullRom(time, 1.0F, 1.0F, 1.0F));
					var offset = keyframes.offset;
					if (!offset.isEmpty()) offset.add(EEEndimationProvider.catmullRom(time));
				}
			} else if (type.equals("setStaticKeyframe")) {
				for (var keyframes : partKeyframes.values()) {
					addStaticKeyframe(keyframes.pos, time);
					addStaticKeyframe(keyframes.rotate, time);
					addStaticKeyframe(keyframes.scale, time);
					addStaticKeyframe(keyframes.offset, time);
				}
			}
		}

		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("keyframes()\n");
		stringBuilder.append("\t\t");
		partKeyframes.forEach((name, partKeyframes1) -> {
			stringBuilder.append(".part(");
			stringBuilder.append("\"").append(name).append("\", partKeyframes()");
			appendKeyframes(partKeyframes1.pos, stringBuilder, "pos");
			appendKeyframes(partKeyframes1.rotate, stringBuilder, "rotate");
			appendKeyframes(partKeyframes1.scale, stringBuilder, "scale");
			appendKeyframes(partKeyframes1.offset, stringBuilder, "offset");
			stringBuilder.append(")\n");
		});
		return stringBuilder.toString();
	}

	private static PartKeyframes get(Map<String, PartKeyframes> map, String part) {
		PartKeyframes partKeyframes = map.get(part);
		if (partKeyframes == null) {
			map.put(part, partKeyframes = new PartKeyframes());
		}
		return partKeyframes;
	}

	private static void addStaticKeyframe(ArrayList<EndimationKeyframe> keyframes, float time) {
		if (!keyframes.isEmpty()) {
			EndimationKeyframe posFrame = keyframes.get(keyframes.size() - 1);
			keyframes.add(new EndimationKeyframe(time, new EndimationKeyframe.Transform(posFrame.postX, posFrame.postY, posFrame.postZ), posFrame.interpolator));
		}
	}

	private static void appendKeyframes(ArrayList<EndimationKeyframe> keyframes, StringBuilder stringBuilder, String method) {
		if (!keyframes.isEmpty()) {
			stringBuilder.append(".").append(method).append("(");
			var iterator = keyframes.iterator();
			while (true) {
				EndimationKeyframe keyframe = iterator.next();
				stringBuilder.append(keyframe.interpolator.type() == InterpolationType.LINEAR ? "linear(" : "catmullRom(");
				stringBuilder.append(keyframe.time).append("F").append(", ");
				stringBuilder.append(keyframe.postX.get()).append("F").append(", ");
				stringBuilder.append(keyframe.postY.get()).append("F").append(", ");
				stringBuilder.append(keyframe.postZ.get()).append("F").append(")");
				if (!iterator.hasNext()) break;
				stringBuilder.append(", ");
			}
			stringBuilder.append(")");
		}
	}

	private static record PartKeyframes(ArrayList<EndimationKeyframe> pos, ArrayList<EndimationKeyframe> rotate, ArrayList<EndimationKeyframe> scale, ArrayList<EndimationKeyframe> offset) {
		public PartKeyframes() {
			this(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
		}
	}
}
