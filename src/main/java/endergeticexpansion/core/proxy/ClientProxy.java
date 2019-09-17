package endergeticexpansion.core.proxy;

import endergeticexpansion.client.render.entity.*;
import endergeticexpansion.client.render.entity.booflo.*;
import endergeticexpansion.client.render.tile.*;
import endergeticexpansion.common.entities.*;
import endergeticexpansion.common.entities.bolloom.*;
import endergeticexpansion.common.entities.booflo.*;
import endergeticexpansion.common.tileentities.*;
import endergeticexpansion.common.tileentities.boof.*;
import endergeticexpansion.core.keybinds.KeybindHandler;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

@OnlyIn(Dist.CLIENT)
public class ClientProxy extends CommonProxy {
	
	@Override
	public void preInit() {
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityFrisbloomStem.class, new RenderTileEntityFrisbloomStem());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCorrockCrown.class, new RenderTileEntityCorrockCrown());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBolloomBud.class, new RenderTileEntityBolloomBud());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPuffBugHive.class, new RenderTileEntityPuffBugHive());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDispensedBoof.class, new RenderTileEntityBoofBlockDispensed());
	
		RenderingRegistry.registerEntityRenderingHandler(EntityBolloomFruit.class, RenderBolloomFruit::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityPoiseCluster.class, RenderPoiseCluster::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityBoofBlock.class, RenderBoofBlock::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityBolloomKnot.class, RenderBolloomKnot::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityBolloomBalloon.class, RenderBolloomBalloon::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityEndergeticBoat.class, RenderEndergeticBoat::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityPuffBug.class, RenderPuffBug::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityBoofloBaby.class, RenderBoofloBaby::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityBoofloAdolescent.class, RenderBoofloAdolescent::new);
		
		//RenderingRegistry.registerEntityRenderingHandler(EnderCrystalEntity.class, EnderCrystalRenderer::new);
		
		KeybindHandler.registerKeys();
	}
}
