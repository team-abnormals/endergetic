package endergeticexpansion.common.world;

import endergeticexpansion.core.EndergeticExpansion;
import net.minecraft.world.biome.provider.BiomeProviderType;
import net.minecraft.world.biome.provider.EndBiomeProvider;
import net.minecraft.world.biome.provider.EndBiomeProviderSettings;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EndergeticExpansion.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EndOverrideHandler {
	public static BiomeProviderType<EndBiomeProviderSettings, EndBiomeProvider> ENDERGETIC_END = new BiomeProviderType<>(EndergeticBiomeProvider::new, EndBiomeProviderSettings::new);

	@SubscribeEvent
	public static void registerBiomeProvider(RegistryEvent.Register<BiomeProviderType<?, ?>> event) {
		event.getRegistry().register(
			ENDERGETIC_END.setRegistryName(EndergeticExpansion.MOD_ID, "the_end")
		);
	}
	
	public static void overrideEndFactory() {
		DimensionType.THE_END.factory = EndergeticEndDimension::new;
	}
}