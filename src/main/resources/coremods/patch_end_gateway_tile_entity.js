var Opcodes = org.objectweb.asm.Opcodes;
var FieldInsnNode = org.objectweb.asm.tree.FieldInsnNode;

function initializeCoreMod() {
	return {
		'patch_end_gateway_tile_entity': {
			'target': {
				'type': 'METHOD',
				'class': 'net.minecraft.tileentity.EndGatewayTileEntity',
				'methodName': 'func_227016_a_',
				'methodDesc': '(Lnet/minecraft/world/server/ServerWorld;Lnet/minecraft/util/math/BlockPos;)V'
			},
			'transformer': function(method) {
				print('[Endergetic Expansion]: Patching EndGatewayTileEntity#createExitPortal');
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
	}
}