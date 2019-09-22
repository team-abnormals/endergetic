var Opcodes = org.objectweb.asm.Opcodes;
var FieldInsnNode = org.objectweb.asm.tree.FieldInsnNode;

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
					if(currentInstr.getOpcode() == Opcodes.GETSTATIC) {
						instr.set(currentInstr, new FieldInsnNode(Opcodes.GETSTATIC, "endergeticexpansion/core/registry/EEBlocks", "POISMOSS_EUMUS", "Lnet/minecraft/block/Block;"));
						break;
					}
				}

				return method;
			}
		}
	}
}