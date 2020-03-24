package endergeticexpansion.client.render.tile;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import endergeticexpansion.client.model.ModelBoofBlockDispenser;
import endergeticexpansion.common.blocks.poise.boof.BlockDispensedBoof;
import endergeticexpansion.common.tileentities.boof.TileEntityDispensedBoof;
import endergeticexpansion.core.EndergeticExpansion;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;

public class BoofBlockTileEntityRenderer extends TileEntityRenderer<TileEntityDispensedBoof> {
	public ModelBoofBlockDispenser model;
	private static final ResourceLocation TEXTURE = new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/block/boof_block_dispensed.png");
	
	public BoofBlockTileEntityRenderer(TileEntityRendererDispatcher renderDispatcher) {
		super(renderDispatcher);
		this.model = new ModelBoofBlockDispenser();
	}
	
	@Override
	public void render(TileEntityDispensedBoof boof, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLightIn, int combinedOverlayIn) {
		Direction facing = boof.hasWorld() ? boof.getBlockState().get(BlockDispensedBoof.FACING) : Direction.NORTH;
		
		matrixStack.push();
		matrixStack.translate(0.5F, 1.5F, 0.5F);
		
		if(facing == Direction.DOWN) {
			matrixStack.rotate(Vector3f.XP.rotationDegrees(90.0F));
			matrixStack.translate(0.0F, 1.125F, 1.0F);
		} else if(facing == Direction.UP){
			matrixStack.rotate(Vector3f.XP.rotationDegrees(-90.0F));
			matrixStack.translate(0.0F, 1.125F, -1.0F);
		} else if(facing == Direction.NORTH) {
			matrixStack.rotate(Vector3f.YP.rotationDegrees(180.0F));
		} else if(facing == Direction.EAST) {
			matrixStack.rotate(Vector3f.YP.rotationDegrees(90.0F));
		} else if(facing == Direction.WEST) {
			matrixStack.rotate(Vector3f.YP.rotationDegrees(-90.0F));
		}
		
		matrixStack.scale(1.0F, -1.0F, -1.0F);
		
		IVertexBuilder ivertexbuilder = buffer.getBuffer(RenderType.getEntityCutout(TEXTURE));
		this.model.renderAll(matrixStack, ivertexbuilder, combinedLightIn, combinedOverlayIn);
		
		matrixStack.pop();
	}
}