var Opcodes = org.objectweb.asm.Opcodes;
var Type = org.objectweb.asm.Type;
var InsnNode = org.objectweb.asm.tree.InsnNode;
var VarInsnNode = org.objectweb.asm.tree.VarInsnNode;
var MethodInsnNode = org.objectweb.asm.tree.MethodInsnNode;
var FieldInsnNode = org.objectweb.asm.tree.FieldInsnNode;

function initializeCoreMod() {
	return {
		'patch_end_biome_provider': {
			'target': {
				'type': 'METHOD',
				'class': 'net.minecraft.world.biome.provider.EndBiomeProvider',
				'methodName': 'func_225526_b_',
				'methodDesc': '(III)Lnet/minecraft/world/biome/Biome;'
			},
			'transformer': function(method) {
				var instructions = method.instructions;
				instructions.clear();
				
				instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
				instructions.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/world/biome/provider/EndBiomeProvider", "field_235315_h_", Type.LONG_TYPE.getDescriptor()));
				instructions.add(new VarInsnNode(Opcodes.ILOAD, 1));
				instructions.add(new VarInsnNode(Opcodes.ILOAD, 2));
				instructions.add(new VarInsnNode(Opcodes.ILOAD, 3));
				instructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "endergeticexpansion/core/EEHooks", "getEndergeticNoiseBiome", "(JIII)Lnet/minecraft/world/biome/Biome;", false));
				instructions.add(new InsnNode(Opcodes.ARETURN));
				return method;
			}
		}
	}
}