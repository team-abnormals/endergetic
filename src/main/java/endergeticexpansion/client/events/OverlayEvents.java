package endergeticexpansion.client.events;

import endergeticexpansion.common.entities.booflo.EntityBooflo;
import endergeticexpansion.core.EndergeticExpansion;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = EndergeticExpansion.MOD_ID, value = Dist.CLIENT)
public class OverlayEvents {
	private static final Minecraft MC = Minecraft.getInstance();
	
	@SubscribeEvent
	public static void renderOverlays(RenderGameOverlayEvent.Pre event) {
		ClientPlayerEntity player = MC.player;
		if(!MC.gameSettings.hideGUI && event.getType() == ElementType.EXPERIENCE) {
			if(player.isPassenger() && player.getRidingEntity() instanceof EntityBooflo) {
				
			}
		}
	}
}