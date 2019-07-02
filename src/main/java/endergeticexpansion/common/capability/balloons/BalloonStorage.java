package endergeticexpansion.common.capability.balloons;

import net.minecraft.nbt.INBT;
import net.minecraft.nbt.IntNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class BalloonStorage implements IStorage<IBalloonStorage> {

	@Override
	public INBT writeNBT(Capability<IBalloonStorage> capability, IBalloonStorage instance, Direction side) {
		return new IntNBT(instance.getBalloonsTied());
	}

	@Override
	public void readNBT(Capability<IBalloonStorage> capability, IBalloonStorage instance, Direction side, INBT nbt) {
		instance.setBalloonsTied((((IntNBT) nbt).getInt())); 
	}

}
