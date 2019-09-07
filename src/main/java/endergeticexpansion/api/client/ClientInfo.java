package endergeticexpansion.api.client;

import net.minecraft.client.Minecraft;

/**
 * @author - SmellyModder(Luke Tonon)
 */
public class ClientInfo {
	public static final Minecraft MINECRAFT = Minecraft.getInstance();

	/**
	 * @return - The partial ticks of the minecraft client
	 */
	public static float getPartialTicks() {
		return MINECRAFT.isGamePaused() ? MINECRAFT.renderPartialTicksPaused : MINECRAFT.getRenderPartialTicks();
	}
}
