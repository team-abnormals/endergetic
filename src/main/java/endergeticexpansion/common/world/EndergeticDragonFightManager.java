package endergeticexpansion.common.world;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.dimension.EndDimension;
import net.minecraft.world.end.DragonFightManager;
import net.minecraft.world.server.ServerWorld;

public class EndergeticDragonFightManager extends DragonFightManager {

	public EndergeticDragonFightManager(ServerWorld worldIn, CompoundNBT compound, EndDimension dim) {
		super(worldIn, compound, dim);
	}
	
}
