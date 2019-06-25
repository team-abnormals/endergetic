package endergeticexpansion.common.items.itemblocks;

import java.util.List;

import javax.annotation.Nullable;

import endergeticexpansion.common.blocks.poise.boof.BlockBoof.BoofDispenseBehavior;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.SoundType;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.state.IProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ItemBlockBoof extends Item {
	@Deprecated
	private final Block block;

	public ItemBlockBoof(Block blockIn, Item.Properties builder) {
		super(builder);
		this.block = blockIn;
		DispenserBlock.registerDispenseBehavior(this, new BoofDispenseBehavior());
	}

	public ActionResultType onItemUse(ItemUseContext context) {
	      ActionResultType actionresulttype = this.tryPlace(new BlockItemUseContext(context));
	      return actionresulttype != ActionResultType.SUCCESS && this.func_219971_r() ? this.onItemRightClick(context.getWorld(), context.getPlayer(), context.getPlayer().getActiveHand()).getType() : actionresulttype;
	   }

	   public ActionResultType tryPlace(BlockItemUseContext p_195942_1_) {
	      if (!p_195942_1_.canPlace()) {
	         return ActionResultType.FAIL;
	      } else {
	         BlockItemUseContext blockitemusecontext = this.func_219984_b(p_195942_1_);
	         if (blockitemusecontext == null) {
	            return ActionResultType.FAIL;
	         } else {
	            BlockState blockstate = this.getStateForPlacement(blockitemusecontext);
	            if (blockstate == null) {
	               return ActionResultType.FAIL;
	            } else if (!this.placeBlock(blockitemusecontext, blockstate)) {
	               return ActionResultType.FAIL;
	            } else {
	               BlockPos blockpos = blockitemusecontext.getPos();
	               World world = blockitemusecontext.getWorld();
	               PlayerEntity playerentity = blockitemusecontext.getPlayer();
	               ItemStack itemstack = blockitemusecontext.getItem();
	               BlockState blockstate1 = world.getBlockState(blockpos);
	               Block block = blockstate1.getBlock();
	               if (block == blockstate.getBlock()) {
	                  blockstate1 = this.func_219985_a(blockpos, world, itemstack, blockstate1);
	                  this.onBlockPlaced(blockpos, world, playerentity, itemstack, blockstate1);
	                  block.onBlockPlacedBy(world, blockpos, blockstate1, playerentity, itemstack);
	                  if (playerentity instanceof ServerPlayerEntity) {
	                     CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayerEntity)playerentity, blockpos, itemstack);
	                  }
	               }

	               SoundType soundtype = blockstate1.getSoundType(world, blockpos, p_195942_1_.getPlayer());
	               world.playSound(playerentity, blockpos, this.func_219983_a(blockstate1), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
	               itemstack.shrink(1);
	               return ActionResultType.SUCCESS;
	            }
	         }
	      }
	   }

	   protected SoundEvent func_219983_a(BlockState p_219983_1_) {
	      return p_219983_1_.getSoundType().getPlaceSound();
	   }

	   @Nullable
	   public BlockItemUseContext func_219984_b(BlockItemUseContext p_219984_1_) {
	      return p_219984_1_;
	   }

	   protected boolean onBlockPlaced(BlockPos pos, World worldIn, @Nullable PlayerEntity player, ItemStack stack, BlockState state) {
	      return setTileEntityNBT(worldIn, player, pos, stack);
	   }

	   @Nullable
	   protected BlockState getStateForPlacement(BlockItemUseContext context) {
	      BlockState blockstate = this.getBlock().getStateForPlacement(context);
	      return blockstate != null && this.canPlace(context, blockstate) ? blockstate : null;
	   }

	   private BlockState func_219985_a(BlockPos p_219985_1_, World p_219985_2_, ItemStack p_219985_3_, BlockState p_219985_4_) {
	      BlockState blockstate = p_219985_4_;
	      CompoundNBT compoundnbt = p_219985_3_.getTag();
	      if (compoundnbt != null) {
	         CompoundNBT compoundnbt1 = compoundnbt.getCompound("BlockStateTag");
	         StateContainer<Block, BlockState> statecontainer = p_219985_4_.getBlock().getStateContainer();

	         for(String s : compoundnbt1.keySet()) {
	            IProperty<?> iproperty = statecontainer.getProperty(s);
	            if (iproperty != null) {
	               String s1 = compoundnbt1.get(s).getString();
	               blockstate = func_219988_a(blockstate, iproperty, s1);
	            }
	         }
	      }

	      if (blockstate != p_219985_4_) {
	         p_219985_2_.setBlockState(p_219985_1_, blockstate, 2);
	      }

	      return blockstate;
	   }

	   private static <T extends Comparable<T>> BlockState func_219988_a(BlockState p_219988_0_, IProperty<T> p_219988_1_, String p_219988_2_) {
	      return p_219988_1_.parseValue(p_219988_2_).map((p_219986_2_) -> {
	         return p_219988_0_.with(p_219988_1_, p_219986_2_);
	      }).orElse(p_219988_0_);
	   }

	   protected boolean canPlace(BlockItemUseContext p_195944_1_, BlockState p_195944_2_) {
	      PlayerEntity playerentity = p_195944_1_.getPlayer();
	      ISelectionContext iselectioncontext = playerentity == null ? ISelectionContext.dummy() : ISelectionContext.forEntity(playerentity);
	      return (!this.func_219987_d() || p_195944_2_.isValidPosition(p_195944_1_.getWorld(), p_195944_1_.getPos())) && p_195944_1_.getWorld().func_217350_a(p_195944_2_, p_195944_1_.getPos(), iselectioncontext);
	   }

	   protected boolean func_219987_d() {
	      return true;
	   }

	   protected boolean placeBlock(BlockItemUseContext context, BlockState state) {
	      return context.getWorld().setBlockState(context.getPos(), state, 11);
	   }

	   public static boolean setTileEntityNBT(World worldIn, @Nullable PlayerEntity player, BlockPos pos, ItemStack stackIn) {
	      MinecraftServer minecraftserver = worldIn.getServer();
	      if (minecraftserver == null) {
	         return false;
	      } else {
	         CompoundNBT compoundnbt = stackIn.getChildTag("BlockEntityTag");
	         if (compoundnbt != null) {
	            TileEntity tileentity = worldIn.getTileEntity(pos);
	            if (tileentity != null) {
	               if (!worldIn.isRemote && tileentity.onlyOpsCanSetNbt() && (player == null || !player.canUseCommandBlock())) {
	                  return false;
	               }

	               CompoundNBT compoundnbt1 = tileentity.write(new CompoundNBT());
	               CompoundNBT compoundnbt2 = compoundnbt1.copy();
	               compoundnbt1.merge(compoundnbt);
	               compoundnbt1.putInt("x", pos.getX());
	               compoundnbt1.putInt("y", pos.getY());
	               compoundnbt1.putInt("z", pos.getZ());
	               if (!compoundnbt1.equals(compoundnbt2)) {
	                  tileentity.read(compoundnbt1);
	                  tileentity.markDirty();
	                  return true;
	               }
	            }
	         }

	         return false;
	      }
	   }

	   /**
	    * Returns the unlocalized name of this item.
	    */
	   public String getTranslationKey() {
	      return this.getBlock().getTranslationKey();
	   }

	   /**
	    * allows items to add custom lines of information to the mouseover description
	    */
	   @OnlyIn(Dist.CLIENT)
	   public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
	      super.addInformation(stack, worldIn, tooltip, flagIn);
	      this.getBlock().addInformation(stack, worldIn, tooltip, flagIn);
	   }

	   public Block getBlock() {
	      return this.getBlockRaw() == null ? null : this.getBlockRaw().delegate.get();
	   }

	   private Block getBlockRaw() {
	      return this.block;
	   }
}
