package com.minecraftabnormals.endergetic.common.items;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.teamabnormals.abnormals_core.core.utils.ItemStackUtils;
import com.minecraftabnormals.endergetic.common.entities.booflo.BoofloEntity;
import com.minecraftabnormals.endergetic.common.entities.puffbug.PuffBugEntity;
import com.minecraftabnormals.endergetic.core.registry.EEEntities;

import java.util.Map.Entry;

import net.minecraft.block.BlockState;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
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

public class PuffBugBottleItem extends Item {

	public PuffBugBottleItem(Properties properties) {
		super(properties);
	}

	@Override
	public ActionResultType onItemUse(ItemUseContext context) {
		World world = context.getWorld();
		if (world.isRemote) {
			return ActionResultType.SUCCESS;
		} else {
			ItemStack itemstack = context.getItem();
			BlockPos blockpos = context.getPos();
			Direction direction = context.getFace();
			BlockState blockstate = world.getBlockState(blockpos);

			BlockPos blockpos1;
			if (blockstate.getCollisionShape(world, blockpos).isEmpty()) {
				blockpos1 = blockpos;
			} else {
				blockpos1 = blockpos.offset(direction);
			}

			EntityType<?> entitytype = EEEntities.PUFF_BUG.get();
			if (entitytype.spawn(world, itemstack, context.getPlayer(), blockpos1, SpawnReason.BUCKET, true, !Objects.equals(blockpos, blockpos1) && direction == Direction.UP) != null) {
				this.emptyBottle(context.getPlayer(), context.getHand());
			}
			return ActionResultType.SUCCESS;
		}
	}

	@Override
	public ActionResultType itemInteractionForEntity(ItemStack stack, PlayerEntity player, LivingEntity target, Hand hand) {
		if (target instanceof BoofloEntity) {
			BoofloEntity booflo = (BoofloEntity) target;
			if (!booflo.hasCaughtFruit() && !booflo.hasCaughtPuffBug() && booflo.isTamed()) {
				World world = player.world;
				PuffBugEntity puffbug = EEEntities.PUFF_BUG.get().create(world);
				puffbug.setPosition(target.getPosX(), target.getPosY(), target.getPosZ());
				EntityType.applyItemNBT(world, player, puffbug, stack.getOrCreateTag());
				puffbug.onInitialSpawn(world, world.getDifficultyForLocation(puffbug.getPosition()), SpawnReason.BUCKET, null, stack.getOrCreateTag());
				world.addEntity(puffbug);
				booflo.catchPuffBug(puffbug);
				if (!player.abilities.isCreativeMode) {
					this.emptyBottle(player, hand);
				}
				return ActionResultType.CONSUME;
			}
		}
		return super.itemInteractionForEntity(stack, player, target, hand);
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
				BlockRayTraceResult blockraytraceresult = (BlockRayTraceResult) raytraceresult;
				BlockPos blockpos = blockraytraceresult.getPos();
				if (!(worldIn.getBlockState(blockpos).getBlock() instanceof FlowingFluidBlock)) {
					return new ActionResult<>(ActionResultType.PASS, itemstack);
				} else if (worldIn.isBlockModifiable(playerIn, blockpos) && playerIn.canPlayerEdit(blockpos, blockraytraceresult.getFace(), itemstack)) {
					EntityType<?> entitytype = EEEntities.PUFF_BUG.get();
					if (entitytype.spawn(worldIn, itemstack, playerIn, blockpos, SpawnReason.SPAWN_EGG, false, false) == null) {
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
		if (nbt != null && nbt.contains("CustomPotionEffects")) {
			tooltip.add(new TranslationTextComponent("tooltip.endergetic.activePotions").mergeStyle(TextFormatting.DARK_PURPLE));
			for (EffectInstance effects : PotionUtils.getFullEffectsFromTag(nbt)) {
				TextFormatting[] potionTextFormat = new TextFormatting[]{TextFormatting.ITALIC, this.getEffectTextColor(effects)};
				tooltip.add(new StringTextComponent(" " + I18n.format(effects.getEffectName()) + " " + ItemStackUtils.intToRomanNumerals(effects.getAmplifier() + 1)).mergeStyle(potionTextFormat));
			}
		}
	}

	private TextFormatting getEffectTextColor(EffectInstance effect) {
		Map<Attribute, AttributeModifier> map = effect.getPotion().getAttributeModifierMap();
		if (!map.isEmpty()) {
			for (Entry<Attribute, AttributeModifier> entry : map.entrySet()) {
				AttributeModifier entryValue = entry.getValue();
				AttributeModifier modifier = new AttributeModifier(entryValue.getName(), effect.getPotion().getAttributeModifierAmount(effect.getAmplifier(), entryValue), entryValue.getOperation());

				if (modifier.getAmount() <= 0.0F) {
					return TextFormatting.RED;
				}
			}
		}
		return effect.getPotion().isBeneficial() ? TextFormatting.BLUE : TextFormatting.RED;
	}

	private void emptyBottle(PlayerEntity player, Hand hand) {
		if (!player.isCreative()) {
			EquipmentSlotType slot = hand == Hand.MAIN_HAND ? EquipmentSlotType.MAINHAND : EquipmentSlotType.OFFHAND;
			player.setItemStackToSlot(slot, new ItemStack(Items.GLASS_BOTTLE));
		}
	}

	public static class PuffBugBottleDispenseBehavior extends DefaultDispenseItemBehavior {

		public ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
			Direction direction = source.getBlockState().get(DispenserBlock.FACING);
			if (source.getWorld().getBlockState(source.getBlockPos().offset(direction)).getCollisionShape(source.getWorld(), source.getBlockPos().offset(direction)).isEmpty()) {
				EntityType<?> entitytype = EEEntities.PUFF_BUG.get();
				entitytype.spawn(source.getWorld(), stack, null, source.getBlockPos().offset(direction), SpawnReason.DISPENSER, direction != Direction.UP, false);
				stack = new ItemStack(Items.GLASS_BOTTLE);
			} else {
				return super.dispenseStack(source, stack);
			}
			return stack;
		}

	}
}
