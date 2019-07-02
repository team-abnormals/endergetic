package endergeticexpansion.common.capability.balloons;

import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class BalloonProvider implements ICapabilitySerializable<INBT> {
	
	@CapabilityInject(IBalloonStorage.class) 
	public static final Capability<IBalloonStorage> BALLOON_CAP = null;
	
	private IBalloonStorage instance = BALLOON_CAP.getDefaultInstance();
	private LazyOptional<IBalloonStorage> balloonSupplier = LazyOptional.of(() -> instance);

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		return balloonSupplier.cast();
	}

	@Override
	public INBT serializeNBT() {
		return BALLOON_CAP.getStorage().writeNBT(BALLOON_CAP, this.instance, null); 
	}

	@Override
	public void deserializeNBT(INBT nbt) {
		BALLOON_CAP.getStorage().readNBT(BALLOON_CAP, this.instance, null, nbt); 
	}

}
