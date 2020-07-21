var Opcodes = org.objectweb.asm.Opcodes;
var FieldInsnNode = org.objectweb.asm.tree.FieldInsnNode;
var MethodInsnNode = org.objectweb.asm.tree.MethodInsnNode;

/**
 * @author - SmellyModder(Luke Tonon)
 */
function initializeCoreMod() {
	return {
		'patch_dragon_fight_manager_generate_gateway': {
			'target': {
				'type': 'METHOD',
				'class': 'net.minecraft.world.end.DragonFightManager',
				'methodName': 'func_186089_a',
				'methodDesc': '(Lnet/minecraft/util/math/BlockPos;)V'
			},
			'transformer': function(method) {
				var instr = method.instructions;
				
				for (var i = 0; i < instr.size(); i++) {
					var currentInstr = instr.get(i);
					if (currentInstr.getOpcode() == Opcodes.GETSTATIC) {
						instr.set(currentInstr, new FieldInsnNode(Opcodes.GETSTATIC, "endergeticexpansion/common/world/features/EEFeatures", "ENDERGETIC_GATEWAY", "Lnet/minecraft/world/gen/feature/Feature;"));
						break;
					}
				}

				return method;
			}
		},
		'patch_end_spike_feature': {
			'target': {
				'type': 'METHOD',
				'class': 'net.minecraft.world.gen.feature.EndSpikeFeature',
				'methodName': 'func_214553_a',
				'methodDesc': '(Lnet/minecraft/world/IWorld;Ljava/util/Random;Lnet/minecraft/world/gen/feature/EndSpikeFeatureConfig;Lnet/minecraft/world/gen/feature/EndSpikeFeature$EndSpike;)V'
			},
			'transformer': function(method) {
				var ASM = Java.type('net.minecraftforge.coremod.api.ASMAPI');
				var bedrock_field_name = ASM.mapField("field_150357_h");
				var instr = method.instructions;
				
				for (var i = 0; i < instr.size(); i++) {
					var currentInstr = instr.get(i);
					if (currentInstr instanceof FieldInsnNode && currentInstr.getOpcode() == Opcodes.GETSTATIC && currentInstr.name.equals(bedrock_field_name)) {
						instr.set(currentInstr, new MethodInsnNode(Opcodes.INVOKESTATIC, "endergeticexpansion/core/EEHooks", "getCrystalHolder", "()Lnet/minecraft/block/Block;", false));
						break;
					}
				}

				return method;
			}
		}
	};
}