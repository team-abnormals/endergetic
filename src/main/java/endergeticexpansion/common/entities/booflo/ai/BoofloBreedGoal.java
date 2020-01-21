package endergeticexpansion.common.entities.booflo.ai;

import java.util.EnumSet;
import java.util.List;

import javax.annotation.Nullable;

import endergeticexpansion.api.util.NetworkUtil;
import endergeticexpansion.common.entities.booflo.EntityBooflo;
import endergeticexpansion.common.entities.booflo.EntityBooflo.GroundMoveHelperController;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.stats.Stats;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.GameRules;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.BabyEntitySpawnEvent;

public class BoofloBreedGoal extends Goal {
	private static final EntityPredicate MATE_CHECKER = (new EntityPredicate()).setDistance(16.0D).allowInvulnerable().allowFriendlyFire().setLineOfSiteRequired();
	protected final EntityBooflo booflo;
	protected EntityBooflo mate;
	private int impregnateDelay;
	
	public BoofloBreedGoal(EntityBooflo booflo) {
		this.booflo = booflo;
		this.setMutexFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
	}
	
	@Override
	public boolean shouldExecute() {
		if(this.booflo.isBoofed() || !this.booflo.onGround || !this.booflo.isInLove() || this.booflo.isPregnant()) {
			return false;
		} else {
			this.mate = this.getNearbyMate();
			return this.mate != null && !this.mate.isPregnant();
		}
	}
	
	@Override
	public boolean shouldContinueExecuting() {
		return !this.booflo.isBoofed() && this.mate.isAlive() && this.mate.isInLove() && this.impregnateDelay < 100;
	}
	
	public void resetTask() {
		this.mate = null;
		this.impregnateDelay = 0;
	}
	
	public void tick() {
		if(this.booflo.hopDelay == 0 && this.booflo.isAnimationPlaying(EntityBooflo.BLANK_ANIMATION)) {
			NetworkUtil.setPlayingAnimationMessage(this.booflo, EntityBooflo.HOP);
		}
		
		((GroundMoveHelperController) this.booflo.getMoveHelper()).setSpeed(0.1D);
		
		double dx = this.mate.posX - this.booflo.posX;
		double dz = this.mate.posZ - this.booflo.posZ;
		
		float angle = (float) (MathHelper.atan2(dz, dx) * (double) (180F / Math.PI)) - 90.0F;
		
		((GroundMoveHelperController) this.booflo.getMoveHelper()).setDirection(angle, false);
		
		this.booflo.getNavigator().tryMoveToEntityLiving(this.mate, 1.0D);
		
		this.impregnateDelay++;
		if(this.impregnateDelay >= 60 && this.booflo.getDistanceSq(this.mate) < 9.0D) {
			this.impregnateBooflo();
		}
	}
	
	protected void impregnateBooflo() {
		final BabyEntitySpawnEvent event = new net.minecraftforge.event.entity.living.BabyEntitySpawnEvent(this.booflo, this.mate, null);
		final boolean cancelled = MinecraftForge.EVENT_BUS.post(event);
		if(cancelled) {
			this.booflo.resetInLove();
			this.mate.resetInLove();
			return;
		}
		
		ServerPlayerEntity serverplayerentity = this.booflo.getLoveCause();
		if(serverplayerentity == null && this.mate.getLoveCause() != null) {
			serverplayerentity = this.mate.getLoveCause();
		}

		if(serverplayerentity != null) {
			serverplayerentity.addStat(Stats.ANIMALS_BRED);
		}
		
		if(!this.mate.isPregnant()) {
			this.booflo.setPregnant(true);
		}
		
		this.booflo.resetInLove();
		this.mate.resetInLove();
		this.booflo.breedDelay = 1400;
		this.mate.breedDelay = 1400;
        
		this.booflo.world.setEntityState(this.booflo, (byte) 18);
		if(this.booflo.world.getGameRules().getBoolean(GameRules.DO_MOB_LOOT)) {
			this.booflo.world.addEntity(new ExperienceOrbEntity(this.booflo.world, this.booflo.posX, this.booflo.posY, this.booflo.posZ, this.booflo.getRNG().nextInt(7) + 1));
		}
	}
	
	@Nullable
	private EntityBooflo getNearbyMate() {
		List<EntityBooflo> list = this.booflo.world.getTargettableEntitiesWithinAABB(EntityBooflo.class, MATE_CHECKER, this.booflo, this.booflo.getBoundingBox().grow(16.0D));
		double d0 = Double.MAX_VALUE;
		EntityBooflo booflo = null;

		for(EntityBooflo booflos : list) {
			if(this.booflo.canMateWith(booflos) && this.booflo.getDistanceSq(booflos) < d0) {
				booflo = booflos;
				d0 = this.booflo.getDistanceSq(booflos);
			}
		}
		
		return booflo;
	}
}