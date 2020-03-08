package endergeticexpansion.common.world;

import java.util.List;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Lists;

import endergeticexpansion.core.registry.EEBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.block.pattern.BlockPatternBuilder;
import net.minecraft.block.pattern.BlockPattern.PatternHelper;
import net.minecraft.entity.item.EnderCrystalEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.EndPortalTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.CachedBlockInfo;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.dimension.EndDimension;
import net.minecraft.world.end.DragonFightManager;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.EndPodiumFeature;
import net.minecraft.world.server.ServerWorld;

public class EndergeticDragonFightManager extends DragonFightManager {
	private static final Logger LOGGER = LogManager.getLogger();
	private static final Predicate<BlockState> IS_PORTAL_BLOCK = (state) -> {
		Block block = state.getBlock();
		return block == EEBlocks.MYSTICAL_OBSIDIAN.get() || block == EEBlocks.MYSTICAL_OBSIDIAN_ACTIVATION_RUNE.get() || block == EEBlocks.MYSTICAL_OBSIDIAN_ACTIVATION_RUNE_ACTIVE.get() || block == EEBlocks.MYSTICAL_OBSIDIAN_RUNE.get();
	};
	private final BlockPattern portalPattern = BlockPatternBuilder.start().aisle("       ", "       ", "       ", "   #   ", "       ", "       ", "       ").aisle("       ", "       ", "       ", "   #   ", "       ", "       ", "       ").aisle("       ", "       ", "       ", "   #   ", "       ", "       ", "       ").aisle("  ###  ", " #   # ", "#     #", "#  #  #", "#     #", " #   # ", "  ###  ").aisle("       ", "  ###  ", " ##### ", " ##### ", " ##### ", "  ###  ", "       ").where('#', CachedBlockInfo.hasState(IS_PORTAL_BLOCK)).build();

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
	
	@Override
	@Nullable
	public PatternHelper findExitPortal() {
		for(int x = -8; x <= 8; x++) {
			for(int z = -8; z <= 8; z++) {
				Chunk chunk = this.world.getChunk(x, z);
				
				for(TileEntity tileentity : chunk.getTileEntityMap().values()) {
					if(tileentity instanceof EndPortalTileEntity) {
						BlockPattern.PatternHelper portalPattern = this.portalPattern.match(this.world, tileentity.getPos());
						if(portalPattern != null) {
							BlockPos blockpos = portalPattern.translateOffset(3, 3, 3).getPos();
							if(this.exitPortalLocation == null && blockpos.getX() == 0 && blockpos.getZ() == 0) {
								this.exitPortalLocation = blockpos;
							}

							return portalPattern;
						}
					}
				}
			}
		}
		
		int height = this.world.getHeight(Heightmap.Type.MOTION_BLOCKING, EndPodiumFeature.END_PODIUM_LOCATION.up()).getY();
		
		for(int y = height; y >= 0; y--) {
			BlockPattern.PatternHelper blockpattern$patternhelper1 = this.portalPattern.match(this.world, new BlockPos(EndPodiumFeature.END_PODIUM_LOCATION.getX(), y, EndPodiumFeature.END_PODIUM_LOCATION.getZ()));
			if(blockpattern$patternhelper1 != null) {
				if(this.exitPortalLocation == null) {
					this.exitPortalLocation = blockpattern$patternhelper1.translateOffset(3, 3, 3).getPos();
				}
				return blockpattern$patternhelper1;
			}
		}
		
		return null;
	}
}
