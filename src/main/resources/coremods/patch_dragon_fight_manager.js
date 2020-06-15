var Opcodes = org.objectweb.asm.Opcodes;
var FieldInsnNode = org.objectweb.asm.tree.FieldInsnNode;
var MethodInsnNode = org.objectweb.asm.tree.MethodInsnNode;
var TypeInsnNode = org.objectweb.asm.tree.TypeInsnNode;
var VarInsnNode = org.objectweb.asm.tree.VarInsnNode;
var InsnList = org.objectweb.asm.tree.InsnList;

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
				print('[Endergetic Expansion]: Patching DragonFightManager#generateGateway');
				var instr = method.instructions;
				
				for(var i = 0; i < instr.size(); i++) {
					var currentInstr = instr.get(i);
					if(currentInstr.getOpcode() == Opcodes.GETSTATIC) {
						instr.set(currentInstr, new FieldInsnNode(Opcodes.GETSTATIC, "endergeticexpansion/common/world/features/EEFeatures", "ENDERGETIC_GATEWAY", "Lnet/minecraft/world/gen/feature/Feature;"));
						break;
					}
				}

				return method;
			}
		}
	};
}