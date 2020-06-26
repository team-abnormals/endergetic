package endergeticexpansion.common.items.dev;

import java.util.Random;
import java.util.function.Supplier;

import net.minecraft.item.Item;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.server.ServerWorld;

/**
 * DEV ONLY
 */
public class FeatureStick<C extends IFeatureConfig> extends Item {
	private final Supplier<Feature<C>> feature;
	private final C config;
	
	public FeatureStick(Supplier<Feature<C>> feature, C config) {
		super(new Item.Properties());
		this.feature = feature;
		this.config = config;
	}
	
	@Override
	public ActionResultType onItemUse(ItemUseContext context) {
		World world = context.getWorld();
		if(!world.isRemote) {
			ServerWorld serverWorld = (ServerWorld) world;
			serverWorld.playSound(null, context.getPos(), SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.PLAYERS, 1.0F, 1.0F);
			this.feature.get().place(serverWorld, serverWorld.getChunkProvider().getChunkGenerator(), new Random(), context.getPos().up(), this.config);
			return ActionResultType.SUCCESS;
		}
		return super.onItemUse(context);
	}
}