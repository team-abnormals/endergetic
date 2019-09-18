var Opcodes = org.objectweb.asm.Opcodes;
var VarInsnNode = org.objectweb.asm.tree.VarInsnNode;
var FieldInsnNode = org.objectweb.asm.tree.FieldInsnNode;
var MethodInsnNode = org.objectweb.asm.tree.MethodInsnNode;
var InsnList = org.objectweb.asm.tree.InsnList;

function initializeCoreMods() {
    return {
        "EnderCrystalTransformer": {
            "target": {
                "type": "CLASS",
                "name": "net.minecraft.entity.item.EnderCrystalEntity"
            },
            "transformer": patch_ender_crystal_entity
        }
    }
}

function patch_ender_crystal_entity(classNode) {
	var api = Java.type('net.minecraftforge.coremod.api.ASMAPI');
    
	return classNode;
}