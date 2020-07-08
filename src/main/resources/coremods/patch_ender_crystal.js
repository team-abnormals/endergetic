var Opcodes = org.objectweb.asm.Opcodes;
var MethodInsnNode = org.objectweb.asm.tree.MethodInsnNode;

function initializeCoreMod() {
	return {
		'patch_ender_crystal': {
			'target': {
				'type': 'METHOD',
				'class': 'net.minecraft.entity.item.EnderCrystalEntity',
				'methodName': 'func_70071_h_',
				'methodDesc': '()V'
			},
			'transformer': function(method) {
				print('[Endergetic Expansion]: Patching EnderCrystalEntity#tick');
				var instr = method.instructions;

				for(var i = 0; i < instr.size(); i++) {
					var currentInstr = instr.get(i);
					if(currentInstr.getOpcode() == Opcodes.INVOKESTATIC) {
						instr.set(currentInstr, new MethodInsnNode(Opcodes.INVOKESTATIC, "endergeticexpansion/core/EEHooks", "getEnderCrystalFireForPlacement", "(Lnet/minecraft/world/IBlockReader;Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/BlockState;", false));
						break;
					}
				}

				return method;
			}
		}
	}
}