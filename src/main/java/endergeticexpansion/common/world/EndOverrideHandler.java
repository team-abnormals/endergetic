package endergeticexpansion.common.world;

import endergeticexpansion.core.EndergeticExpansion;
import net.minecraft.world.biome.provider.BiomeProviderType;
import net.minecraft.world.biome.provider.EndBiomeProvider;
import net.minecraft.world.biome.provider.EndBiomeProviderSettings;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EndergeticExpansion.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EndOverrideHandler {
	public static BiomeProviderType<EndBiomeProviderSettings, EndBiomeProvider> ENDERGETIC_END = new BiomeProviderType<>(EndergeticBiomeProvider::new, EndBiomeProviderSettings::new);

	@SubscribeEvent
	public static void overrideEndBiomeProviderEvent(RegistryEvent.Register<BiomeProviderType<?, ?>> event) {
		ENDERGETIC_END.setRegistryName("minecraft:the_end");
		event.getRegistry().register(
			new BiomeProviderType<>(EndergeticBiomeProvider::new, EndBiomeProviderSettings::new).setRegistryName("minecraft:the_end")
		);
		BiomeProviderType.THE_END = ENDERGETIC_END;
	}
}