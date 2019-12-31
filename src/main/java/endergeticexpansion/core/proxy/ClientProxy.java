package endergeticexpansion.core.proxy;

import endergeticexpansion.client.render.entity.RenderBolloomBalloon;
import endergeticexpansion.client.render.entity.RenderBolloomFruit;
import endergeticexpansion.client.render.entity.RenderBolloomKnot;
import endergeticexpansion.client.render.entity.RenderBoofBlock;
import endergeticexpansion.client.render.entity.RenderEndergeticBoat;
import endergeticexpansion.client.render.entity.RenderPoiseCluster;
import endergeticexpansion.client.render.entity.RenderPuffBug;
import endergeticexpansion.client.render.entity.booflo.*;
import endergeticexpansion.client.render.tile.RenderTileEntityBolloomBud;
import endergeticexpansion.client.render.tile.RenderTileEntityBoofBlockDispensed;
import endergeticexpansion.client.render.tile.RenderTileEntityCorrockCrown;
import endergeticexpansion.client.render.tile.RenderTileEntityFrisbloomStem;
import endergeticexpansion.client.render.tile.RenderTileEntityPuffBugHive;
import endergeticexpansion.common.entities.EntityBoofBlock;
import endergeticexpansion.common.entities.EntityEndergeticBoat;
import endergeticexpansion.common.entities.EntityPoiseCluster;
import endergeticexpansion.common.entities.EntityPuffBug;
import endergeticexpansion.common.entities.bolloom.EntityBolloomBalloon;
import endergeticexpansion.common.entities.bolloom.EntityBolloomFruit;
import endergeticexpansion.common.entities.bolloom.EntityBolloomKnot;
import endergeticexpansion.common.entities.booflo.*;
import endergeticexpansion.common.tileentities.TileEntityBolloomBud;
import endergeticexpansion.common.tileentities.TileEntityCorrockCrown;
import endergeticexpansion.common.tileentities.TileEntityFrisbloomStem;
import endergeticexpansion.common.tileentities.TileEntityPuffBugHive;
import endergeticexpansion.common.tileentities.boof.TileEntityDispensedBoof;
import endergeticexpansion.core.EndergeticExpansion;
import endergeticexpansion.core.keybinds.KeybindHandler;
import net.minecraft.client.renderer.entity.EnderCrystalRenderer;
import net.minecraft.util.ResourceLocation;
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
		RenderingRegistry.registerEntityRenderingHandler(EntityBooflo.class, RenderBooflo::new);
		
		KeybindHandler.registerKeys();
	}
	
	@Override
	public void overrideVanillaFields() {
		EnderCrystalRenderer.ENDER_CRYSTAL_TEXTURES = new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/entity/end_crystal.png");
	}
	
}
