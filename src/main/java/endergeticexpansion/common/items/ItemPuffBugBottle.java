package endergeticexpansion.common.items;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;

import endergeticexpansion.api.util.StringUtils;
import endergeticexpansion.core.registry.EEEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.PotionUtils;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class ItemPuffBugBottle extends Item {
	
	public ItemPuffBugBottle(Properties properties) {
		super(properties);
	}
	
	@Override
	public ActionResultType onItemUse(ItemUseContext context) {
		World world = context.getWorld();
		if(world.isRemote) {
			return ActionResultType.SUCCESS;
		} else {
			
			ItemStack itemstack = context.getItem();
			BlockPos blockpos = context.getPos();
			Direction direction = context.getFace();
			BlockState blockstate = world.getBlockState(blockpos);
			
			BlockPos blockpos1;
			if(blockstate.getCollisionShape(world, blockpos).isEmpty()) {
				blockpos1 = blockpos;
			} else {
				blockpos1 = blockpos.offset(direction);
			}
			
			EntityType<?> entitytype = EEEntities.PUFF_BUG.get();
			if(entitytype.spawn(world, itemstack, context.getPlayer(), blockpos1, SpawnReason.BUCKET, true, !Objects.equals(blockpos, blockpos1) && direction == Direction.UP) != null) {
				this.emptyBottle(context.getPlayer(), context.getHand());
			}
			
			return ActionResultType.SUCCESS;
		}
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
		ItemStack itemstack = playerIn.getHeldItem(handIn);
		if (worldIn.isRemote) {
			return new ActionResult<>(ActionResultType.PASS, itemstack);
		} else {
			RayTraceResult raytraceresult = rayTrace(worldIn, playerIn, RayTraceContext.FluidMode.SOURCE_ONLY);
			if (raytraceresult.getType() != RayTraceResult.Type.BLOCK) {
				return new ActionResult<>(ActionResultType.PASS, itemstack);
			} else {
				BlockRayTraceResult blockraytraceresult = (BlockRayTraceResult)raytraceresult;
				BlockPos blockpos = blockraytraceresult.getPos();
				if (!(worldIn.getBlockState(blockpos).getBlock() instanceof FlowingFluidBlock)) {
					return new ActionResult<>(ActionResultType.PASS, itemstack);
				} else if (worldIn.isBlockModifiable(playerIn, blockpos) && playerIn.canPlayerEdit(blockpos, blockraytraceresult.getFace(), itemstack)) {
					EntityType<?> entitytype = EEEntities.PUFF_BUG.get();
					if(entitytype.spawn(worldIn, itemstack, playerIn, blockpos, SpawnReason.SPAWN_EGG, false, false) == null) {
						return new ActionResult<>(ActionResultType.PASS, itemstack);
					} else {
						if (!playerIn.abilities.isCreativeMode) {
							this.emptyBottle(playerIn, handIn);
						}

						playerIn.addStat(Stats.ITEM_USED.get(this));
						return new ActionResult<>(ActionResultType.SUCCESS, itemstack);
					}
				} else {
					return new ActionResult<>(ActionResultType.FAIL, itemstack);
				}
			}
		}
	}
	
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		CompoundNBT nbt = stack.getTag();
		if(nbt != null && nbt.contains("CustomPotionEffects")) {
			tooltip.add(new TranslationTextComponent("tooltip.endergetic.activePotions").applyTextStyles(TextFormatting.DARK_PURPLE));
			for(EffectInstance effects : PotionUtils.getFullEffectsFromTag(nbt)) {
				TextFormatting[] potionTextFormat = new TextFormatting[] {TextFormatting.ITALIC, this.getEffectTextColor(effects)};
				tooltip.add(new StringTextComponent(" " + I18n.format(effects.getEffectName()) + " " + StringUtils.intToRomanNumerals(effects.getAmplifier() + 1)).applyTextStyles(potionTextFormat));
			}
		}
	}
	
	private TextFormatting getEffectTextColor(EffectInstance effect) {
		Map<IAttribute, AttributeModifier> map = effect.getPotion().getAttributeModifierMap();
		if(!map.isEmpty()) {
        	for(Entry<IAttribute, AttributeModifier> entry : map.entrySet()) {
        		AttributeModifier entryValue = entry.getValue();
        		AttributeModifier modifier = new AttributeModifier(entryValue.getName(), effect.getPotion().getAttributeModifierAmount(effect.getAmplifier(), entryValue), entryValue.getOperation());
        		
        		if(modifier.getAmount() <= 0.0F) {
        			return TextFormatting.RED;
        		}
        	}
		}
		return effect.getPotion().isBeneficial() ? TextFormatting.BLUE : TextFormatting.RED;
	}
	
	private void emptyBottle(PlayerEntity player, Hand hand) {
		if(!player.isCreative()) {
			EquipmentSlotType slot = hand == Hand.MAIN_HAND ? EquipmentSlotType.MAINHAND : EquipmentSlotType.OFFHAND;
			player.setItemStackToSlot(slot, new ItemStack(Items.GLASS_BOTTLE));
		}
	}
	
	public static class PuffBugBottleDispenseBehavior extends DefaultDispenseItemBehavior { 
		
		public ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
			Direction direction = source.getBlockState().get(DispenserBlock.FACING);
			if(source.getWorld().getBlockState(source.getBlockPos().offset(direction)).getCollisionShape(source.getWorld(), source.getBlockPos().offset(direction)).isEmpty()) {
				EntityType<?> entitytype = EEEntities.PUFF_BUG.get();
				entitytype.spawn(source.getWorld(), stack, (PlayerEntity)null, source.getBlockPos().offset(direction), SpawnReason.DISPENSER, direction != Direction.UP, false);
				stack = new ItemStack(Items.GLASS_BOTTLE);
			} else {
				return super.dispenseStack(source, stack);
			}
			return stack;
		}
		
	}
}
