var Opcodes = org.objectweb.asm.Opcodes;
var MethodInsnNode = org.objectweb.asm.tree.MethodInsnNode;
var TypeInsnNode = org.objectweb.asm.tree.TypeInsnNode;

function initializeCoreMod() {
	return {
		'patch_server_world': {
			'target': {
				'type': 'METHOD',
				'class': 'net.minecraft.world.server.ServerWorld',
				'methodName': '<init>',
				'methodDesc': '(Lnet/minecraft/server/MinecraftServer;Ljava/util/concurrent/Executor;Lnet/minecraft/world/storage/SaveFormat$LevelSave;Lnet/minecraft/world/storage/IServerWorldInfo;Lnet/minecraft/util/RegistryKey;Lnet/minecraft/util/RegistryKey;Lnet/minecraft/world/DimensionType;Lnet/minecraft/world/chunk/listener/IChunkStatusListener;Lnet/minecraft/world/gen/ChunkGenerator;ZJLjava/util/List;Z)V'
			},
			'transformer': function(method) {
				var instructions = method.instructions;
				var newsPassed = 0;
				var specialsPassed = 0;
				
				for (var i = 0; i < instructions.size(); i++) {
					var currentInstr = instructions.get(i);
					if (currentInstr.getOpcode() == Opcodes.NEW) {
						newsPassed++;
						if (newsPassed == 8) {
							instructions.set(currentInstr, new TypeInsnNode(Opcodes.NEW, "endergeticexpansion/common/world/EndergeticDragonFightManager"));
						}
					}
					
					if (currentInstr.getOpcode() == Opcodes.INVOKESPECIAL) {
						specialsPassed++;
						if (specialsPassed == 9) {
							instructions.set(currentInstr, new MethodInsnNode(Opcodes.INVOKESPECIAL, "endergeticexpansion/common/world/EndergeticDragonFightManager", "<init>", "(Lnet/minecraft/world/server/ServerWorld;JLnet/minecraft/nbt/CompoundNBT;)V"));
							break;
						}
					}
				}
				return method;
			}
		}
	};
}