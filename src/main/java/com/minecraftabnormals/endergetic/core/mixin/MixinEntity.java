package com.minecraftabnormals.endergetic.core.mixin;

import java.util.UUID;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.minecraftabnormals.endergetic.common.entities.bolloom.BolloomBalloonEntity;

import net.minecraft.command.ICommandSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.util.INameable;
import net.minecraftforge.common.capabilities.CapabilityProvider;
import net.minecraftforge.common.extensions.IForgeEntity;

@Mixin(Entity.class)
public abstract class MixinEntity extends CapabilityProvider<Entity> implements INameable, ICommandSource, IForgeEntity {

	@Shadow
	private UUID entityUniqueID;
	
	private MixinEntity(Class<Entity> baseClass) {
		super(baseClass);
	}
	
	@Inject(at = @At("HEAD"), method = "onRemovedFromWorld()V", remap = false)
	private void onRemovedFromWorld(CallbackInfo info) {
		if (this.getEntity() instanceof BoatEntity) {
			BolloomBalloonEntity.BALLOONS_ON_BOAT_MAP.remove(this.entityUniqueID);
		}
	}
	
}