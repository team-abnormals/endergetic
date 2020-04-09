var Opcodes = org.objectweb.asm.Opcodes;
var FieldInsnNode = org.objectweb.asm.tree.FieldInsnNode;
var InsnNode = org.objectweb.asm.tree.InsnNode;

function initializeCoreMod() {
	return {
		'patch_chorus_feature': {
			'target': {
				'type': 'METHOD',
				'class': 'net.minecraft.world.gen.feature.ChorusPlantFeature',
				'methodName': 'func_212245_a',
				'methodDesc': '(Lnet/minecraft/world/IWorld;Lnet/minecraft/world/gen/ChunkGenerator;Ljava/util/Random;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/gen/feature/NoFeatureConfig;)Z'
			},
			'transformer': function(method) {
				var instr = method.instructions;
				var ASM = Java.type('net.minecraftforge.coremod.api.ASMAPI');

				for(var i = 0; i < instr.size(); i++) {
					var currentInstr = instr.get(i);
					print(currentInstr.getOpcode());
					if(currentInstr.getOpcode() == Opcodes.GETSTATIC) {
						instr.set(currentInstr, new FieldInsnNode(Opcodes.GETSTATIC, "endergeticexpansion/core/registry/other/EETags$Blocks", "CHORUS_PLANTABLE", "Lnet/minecraft/tags/Tag;"));
						
						break;
					}
				}
				
				for(var i = 0; i < instr.size(); i++) {
					var currentInstr = instr.get(i);
					if(currentInstr.getOpcode() == Opcodes.IF_ACMPNE) {
						instr.set(currentInstr, new InsnNode(Opcodes.IFEQ));
						break;
					}
				}
				
				return method;
			}
		}
	}
}