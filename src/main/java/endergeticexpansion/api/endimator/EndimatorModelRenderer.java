package endergeticexpansion.api.endimator;

import javax.annotation.Nullable;

import com.mojang.blaze3d.platform.GlStateManager;

import endergeticexpansion.client.model.booflo.ModelAdolescentBooflo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Much like the vanilla ModelRenderer but can store data of default values and has some more advanced features;
 * Such as setting an individual ModelRenderer's opacity, scale, and texture position
 * 
 * @author - SmellyModder(Luke Tonon)
 */
@OnlyIn(Dist.CLIENT)
public class EndimatorModelRenderer extends ModelRenderer {
	public float defaultRotationPointX, defaultRotationPointY, defaultRotationPointZ;
	public float defaultRotateAngleX, defaultRotateAngleY, defaultRotateAngleZ;
	public float defaultOffsetX, defaultOffsetY, defaultOffsetZ;
	public int textureOffsetX, textureOffsetY;
	public boolean scaleChildren = true;
	public float[] scales = {1.0F, 1.0F, 1.0F};
	public float opacity = 1.0F;
	private boolean compiled;
	private int displayList;
	@Nullable
	private EndimatorModelRenderer parentModelRenderer;

	/**
	 * @param entityModel - Entity model this ModelRenderer belongs to
	 */
	public EndimatorModelRenderer(EndimatorEntityModel<? extends Entity> entityModel) {
		super(entityModel);
	}
	
	/**
	 * Texture offset constuctor 
	 * @param entityModel - Entity model this ModelRenderer belongs to
	 * @param textureOffsetX - X offset on the texture
	 * @param textureOffsetY - Y offset on the texture
	 */
	public EndimatorModelRenderer(EndimatorEntityModel<? extends Entity> entityModel, int textureOffsetX, int textureOffsetY) {
		this(entityModel);
		this.setTextureOffset(textureOffsetX, textureOffsetY);
	}
	
	@Override
	public ModelRenderer func_217178_a(String boxName, float offsetX, float offsetY, float offsetZ, int width, int height, int depth, float delta, int textureOffsetX, int textureOffsetY) {
		boxName = this.boxName + "." + boxName;
		this.setTextureOffset(textureOffsetX, textureOffsetY);
		this.cubeList.add((new ModelBox(this, this.textureOffsetX, this.textureOffsetY, offsetX, offsetY, offsetZ, width, height, depth, 0.0F)).setBoxName(boxName));
		return this;
	}
	
	@Override
	public ModelRenderer addBox(float offsetX, float offsetY, float offsetZ, int width, int height, int depth) {
		this.cubeList.add(new ModelBox(this, this.textureOffsetX, this.textureOffsetY, offsetX, offsetY, offsetZ, width, height, depth, 0.0F));
		return this;
	}
	
	@Override
	public ModelRenderer addBox(float offsetX, float offsetY, float offsetZ, int width, int height, int depth, boolean mirrored) {
		this.cubeList.add(new ModelBox(this, this.textureOffsetX, this.textureOffsetY, offsetX, offsetY, offsetZ, width, height, depth, 0.0F, mirrored));
		return this;
	}
	
	/**
	 * Creates a simple textured box
	 * @see ModelRenderer#addBox(float, float, float, int, int, int, float)
	 */
	@Override
    public void addBox(float offsetX, float offsetY, float offsetZ, int width, int height, int depth, float scaleFactor) {
		this.cubeList.add(new ModelBox(this, this.textureOffsetX, this.textureOffsetY, offsetX, offsetY, offsetZ, width, height, depth, scaleFactor));
    }

	/**
	 * A method that sets the default box's values
	 * Should be called after all the boxes in an entity model have been initialized
	 */
	public void setDefaultBoxValues() {
		this.defaultRotationPointX = this.rotationPointX;
		this.defaultRotationPointY = this.rotationPointY;
		this.defaultRotationPointZ = this.rotationPointZ;
		
		this.defaultRotateAngleX = this.rotateAngleX;
		this.defaultRotateAngleY = this.rotateAngleY;
		this.defaultRotateAngleZ = this.rotateAngleZ;
		
		this.defaultOffsetX = this.offsetX;
		this.defaultOffsetY = this.offsetY;
		this.defaultOffsetZ = this.offsetZ;
	}
	
	/**
	 * A method that reverts the current box's values back to the default values
	 * 
	 * Should be called before applying further rotations and/or animations
	 * 
	 * @see ModelAdolescentBooflo#setRotationAngles()
	 */
	public void revertToDefaultBoxValues() {
		this.rotationPointX = this.defaultRotationPointX;
		this.rotationPointY = this.defaultRotationPointY;
		this.rotationPointZ = this.defaultRotationPointZ;
		
		this.rotateAngleX = this.defaultRotateAngleX;
		this.rotateAngleY = this.defaultRotateAngleY;
		this.rotateAngleZ = this.defaultRotateAngleZ;
		
		this.offsetX = this.defaultOffsetX;
		this.offsetY = this.defaultOffsetY;
		this.offsetZ = this.defaultOffsetZ;
	}
	
	/**
	 * @param 
	 */
	public void setScale(float x, float y, float z) {
		this.scales[0] = x;
		this.scales[1] = y;
		this.scales[2] = z;
	}
	
	/**
	 * Sets the scale of the X axis on this ModelRenderer
	 * @param scaleX - Value of scale
	 */
	public void setScaleX(float scaleX) {
		this.scales[0] = scaleX;
	}
	
	/**
	 * Sets the scale of the Y axis on this ModelRenderer
	 * @param scaleY - Value of scale
	 */
	public void setScaleY(float scaleY) {
		this.scales[1] = scaleY;
	}
	
	/**
	 * Sets the scale of the Z axis on this ModelRenderer
	 * @param scaleZ - Value of scale
	 */
	public void setScaleZ(float scaleZ) {
		this.scales[2] = scaleZ;
	}
	
	/**
	 * Sets the opacity of this ModelRenderer
	 * @param opacity - Value of opacity; shouldn't exceed 1.0
	 */
	public void setOpacity(float opacity) {
		this.opacity = opacity;
	}
	
	/**
	 * Sets the parent ModelRenderer of this ModelRenderer
	 * @param parentModelRenderer - The parent ModelRenderer
	 */
	public void setParentModelRenderer(@Nullable EndimatorModelRenderer parentModelRenderer) {
		this.parentModelRenderer = parentModelRenderer;
	}
	
	public void setShouldScaleChildren(boolean scaleChildren) {
		this.scaleChildren = scaleChildren;
	}
	
	/**
	 * Performs the same function as vanilla's setTextureOffset
	 */
	@Override
	public EndimatorModelRenderer setTextureOffset(int x, int y) {
		this.textureOffsetX = x;
		this.textureOffsetY = y;
		return this;
	}
	
	/**
	 * Performs the same function as vanilla's addChild but adjusted to fit EndimatorModelRenderer
	 */
	@Override
	public void addChild(ModelRenderer ModelRenderer) {
		super.addChild(ModelRenderer);
		EndimatorModelRenderer ModelRendererChild = (EndimatorModelRenderer) ModelRenderer;
		ModelRendererChild.setParentModelRenderer(this);
	}
	
	@Override
	public void render(float scale) {
		if(!this.isHidden) {
			if(this.showModel) {
				GlStateManager.pushMatrix();
				if(!this.compiled) {
					this.compileDisplayList(scale);
				}
				GlStateManager.translatef(this.offsetX, this.offsetY, this.offsetZ);
				GlStateManager.translatef(this.rotationPointX * scale, this.rotationPointY * scale, this.rotationPointZ * scale);
				
				if(this.rotateAngleZ != 0.0F) {
					GlStateManager.rotatef((float) Math.toDegrees(this.rotateAngleZ), 0.0F, 0.0F, 1.0F);
				}
				if(this.rotateAngleY != 0.0F) {
	                GlStateManager.rotatef((float) Math.toDegrees(this.rotateAngleY), 0.0F, 1.0F, 0.0F);
				}
				if(this.rotateAngleX != 0.0F) {
					GlStateManager.rotatef((float) Math.toDegrees(this.rotateAngleX), 1.0F, 0.0F, 0.0F);
				}
				if(this.scales[0] != 1.0F || this.scales[1] != 1.0F || this.scales[2] != 1.0F) {
					GlStateManager.scalef(this.scales[0], this.scales[1], this.scales[2]);
				}
				
				if(this.opacity < 1.0F) {
					GlStateManager.enableBlend();
					GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
					GlStateManager.color4f(1F, 1F, 1F, this.opacity);
				}
				
				GlStateManager.callList(this.displayList);
				
				if(this.opacity < 1.0F) {
					GlStateManager.disableBlend();
					GlStateManager.color4f(1F, 1F, 1F, 1F);
				}
				
				if(!this.scaleChildren && (this.scales[0] != 1.0F || this.scales[1] != 1.0F || this.scales[2] != 1.0F)) {
					GlStateManager.popMatrix();
					GlStateManager.pushMatrix();
					GlStateManager.translatef(this.offsetX, this.offsetY, this.offsetZ);
					GlStateManager.translatef(this.rotationPointX * scale, this.rotationPointY * scale, this.rotationPointZ * scale);
					if(this.rotateAngleZ != 0.0F) {
						GlStateManager.rotatef((float) Math.toDegrees(this.rotateAngleZ), 0.0F, 0.0F, 1.0F);
					}
					if(this.rotateAngleY != 0.0F) {
						GlStateManager.rotatef((float) Math.toDegrees(this.rotateAngleY), 0.0F, 1.0F, 0.0F);
					}
					if(this.rotateAngleX != 0.0F) {
						GlStateManager.rotatef((float) Math.toDegrees(this.rotateAngleX), 1.0F, 0.0F, 0.0F);
					}
				}
				
				if(this.childModels != null) {
					for(ModelRenderer childModel : this.childModels) {
						childModel.render(scale);
					}
				}
				GlStateManager.popMatrix();
			}
		}
	}
	
	private void compileDisplayList(float scale) {
		this.displayList = GLAllocation.generateDisplayLists(1);
		GlStateManager.newList(this.displayList, 4864);
		BufferBuilder bufferbuilder = Tessellator.getInstance().getBuffer();

		for(int i = 0; i < this.cubeList.size(); ++i) {
			this.cubeList.get(i).render(bufferbuilder, scale);
		}

		GlStateManager.endList();
		this.compiled = true;
	}
}
