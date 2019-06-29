package endergeticexpansion.core.proxy;

import endergeticexpansion.client.render.entity.*;
import endergeticexpansion.client.render.tile.*;
import endergeticexpansion.common.entities.EntityBolloomFruit;
import endergeticexpansion.common.entities.EntityBolloomKnot;
import endergeticexpansion.common.entities.EntityBoofBlock;
import endergeticexpansion.common.entities.EntityPoiseCluster;
import endergeticexpansion.common.tileentities.TileEntityBolloomBud;
import endergeticexpansion.common.tileentities.TileEntityCorrockCrown;
import endergeticexpansion.common.tileentities.TileEntityFrisbloomStem;
import endergeticexpansion.common.tileentities.TileEntityPuffBugHive;
import endergeticexpansion.common.tileentities.boof.TileEntityDispensedBoof;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

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
	}
	
}
