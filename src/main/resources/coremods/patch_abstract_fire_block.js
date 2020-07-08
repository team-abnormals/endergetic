var Opcodes = org.objectweb.asm.Opcodes;
var Type = org.objectweb.asm.Type;
var InsnNode = org.objectweb.asm.tree.InsnNode;
var VarInsnNode = org.objectweb.asm.tree.VarInsnNode;
var MethodInsnNode = org.objectweb.asm.tree.MethodInsnNode;
var FieldInsnNode = org.objectweb.asm.tree.FieldInsnNode;

function initializeCoreMod() {
	return {
		'patch_abstract_fire_block': {
			'target': {
				'type': 'METHOD',
				'class': 'net.minecraft.block.AbstractFireBlock',
				'methodName': 'func_235326_a_',
				'methodDesc': '(Lnet/minecraft/world/IBlockReader;Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/BlockState;'
			},
			'transformer': function(method) {
				var instructions = method.instructions;
				instructions.clear();
				
				instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
				instructions.add(new VarInsnNode(Opcodes.ALOAD, 1));
				instructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "endergeticexpansion/core/EEHooks", "getFireForPlacement", "(Lnet/minecraft/world/IBlockReader;Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/BlockState;", false));
				instructions.add(new InsnNode(Opcodes.ARETURN));
				return method;
			}
		}
	}
}