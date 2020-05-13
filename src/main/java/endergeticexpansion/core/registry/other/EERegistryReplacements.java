package endergeticexpansion.core.registry.other;

import com.teamabnormals.abnormals_core.core.utils.RegistryHelper;

import endergeticexpansion.common.items.EndergeticEnderCrystalItem;
import endergeticexpansion.core.EndergeticExpansion;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

@Mod.EventBusSubscriber(modid = EndergeticExpansion.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EERegistryReplacements {
	
	@ObjectHolder("minecraft:end_crystal")
	public static Item END_CRYSTAL;
	
	@SubscribeEvent
	public static void registerItemReplacements(RegistryEvent.Register<Item> event) {
		event.getRegistry().registerAll(
			new EndergeticEnderCrystalItem(RegistryHelper.createSimpleItemProperty(64, ItemGroup.DECORATIONS)).setRegistryName("minecraft:end_crystal")
		);
	}

}