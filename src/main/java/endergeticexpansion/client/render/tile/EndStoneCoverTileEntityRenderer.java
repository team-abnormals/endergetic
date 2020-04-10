package endergeticexpansion.client.render.tile;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import endergeticexpansion.client.model.corrock.ModelCoverUp;
import endergeticexpansion.common.tileentities.TileEntityEndStoneCover;
import endergeticexpansion.core.EndergeticExpansion;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;

public class EndStoneCoverTileEntityRenderer extends TileEntityRenderer<TileEntityEndStoneCover> {
	private ModelCoverUp coverModel;
	private static final ResourceLocation TEXTURE = new ResourceLocation(EndergeticExpansion.MOD_ID, "textures/tile/endstone_cover.png");
	
	public EndStoneCoverTileEntityRenderer(TileEntityRendererDispatcher renderDispatcher) {
		super(renderDispatcher);
		this.coverModel = new ModelCoverUp();
	}
	
	@Override
	public void render(TileEntityEndStoneCover cover, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
		matrixStack.push();
		
		Direction facing = cover.hasWorld() ? cover.getBlockState().get(HorizontalBlock.HORIZONTAL_FACING) : Direction.NORTH;
		
		if(facing == Direction.NORTH) {
			matrixStack.translate(0.5D, 1.51D, -0.5D);
		} else if(facing == Direction.SOUTH) {
			matrixStack.translate(0.5D, 1.51D, 1.5D);
		} else if(facing == Direction.EAST) {
			matrixStack.translate(1.5D, 1.51D, 0.5D);
		} else if(facing == Direction.WEST) {
			matrixStack.translate(-0.5D, 1.51D, 0.5D);
		}
		
		matrixStack.scale(1.01F, -1.01F, -1.01F);
		
		IVertexBuilder ivertexbuilder = bufferIn.getBuffer(RenderType.getEntityCutout(TEXTURE));
		this.coverModel.render(matrixStack, ivertexbuilder, combinedLightIn, combinedOverlayIn);
		
		matrixStack.pop();
	}
}