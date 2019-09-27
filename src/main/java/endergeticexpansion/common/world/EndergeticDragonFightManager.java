package endergeticexpansion.common.world;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Lists;

import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.entity.item.EnderCrystalEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.dimension.EndDimension;
import net.minecraft.world.end.DragonFightManager;
import net.minecraft.world.server.ServerWorld;

public class EndergeticDragonFightManager extends DragonFightManager {
	private static final Logger LOGGER = LogManager.getLogger();

	public EndergeticDragonFightManager(ServerWorld worldIn, CompoundNBT compound, EndDimension dim) {
		super(worldIn, compound, dim);
	}
	
	@Override
	public void tryRespawnDragon() {
		if(this.dragonKilled && this.respawnState == null) {
			BlockPos blockpos = this.exitPortalLocation;
			if(blockpos == null) {
				LOGGER.debug("Tried to respawn, but need to find the portal first.");
				BlockPattern.PatternHelper blockpattern$patternhelper = this.findExitPortal();
				if(blockpattern$patternhelper == null) {
					LOGGER.debug("Couldn't find a portal, so we made one.");
					this.generatePortal(true);
				} else {
					LOGGER.debug("Found the exit portal & temporarily using it.");
				}

				blockpos = this.exitPortalLocation;
			}

			List<EnderCrystalEntity> list1 = Lists.newArrayList();
			BlockPos blockpos1 = blockpos.up(2);

			for(Direction direction : Direction.Plane.HORIZONTAL) {
				List<EnderCrystalEntity> list = this.world.getEntitiesWithinAABB(EnderCrystalEntity.class, new AxisAlignedBB(blockpos1.offset(direction, 2)));
				if(list.isEmpty()) return;

				list1.addAll(list);
			}

			LOGGER.debug("Found all crystals, respawning dragon.");
			this.respawnDragon(list1);
		}
	}
	
}
