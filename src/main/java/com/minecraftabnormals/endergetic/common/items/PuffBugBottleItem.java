package com.minecraftabnormals.endergetic.common.items;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.minecraftabnormals.endergetic.common.entities.booflo.BoofloEntity;
import com.minecraftabnormals.endergetic.common.entities.puffbug.PuffBugEntity;
import com.minecraftabnormals.endergetic.core.registry.EEEntities;

import java.util.Map.Entry;

import com.teamabnormals.blueprint.core.util.item.ItemStackUtil;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.BlockSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.HitResult;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;

public class PuffBugBottleItem extends Item {

	public PuffBugBottleItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		Level world = context.getLevel();
		if (world.isClientSide) {
			return InteractionResult.SUCCESS;
		} else {
			ItemStack itemstack = context.getItemInHand();
			BlockPos blockpos = context.getClickedPos();
			Direction direction = context.getClickedFace();
			BlockState blockstate = world.getBlockState(blockpos);

			BlockPos blockpos1;
			if (blockstate.getCollisionShape(world, blockpos).isEmpty()) {
				blockpos1 = blockpos;
			} else {
				blockpos1 = blockpos.relative(direction);
			}

			EntityType<?> entitytype = EEEntities.PUFF_BUG.get();
			if (entitytype.spawn((ServerLevel) world, itemstack, context.getPlayer(), blockpos1, MobSpawnType.BUCKET, true, !Objects.equals(blockpos, blockpos1) && direction == Direction.UP) != null) {
				this.emptyBottle(context.getPlayer(), context.getHand());
			}
			return InteractionResult.SUCCESS;
		}
	}

	@Override
	public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
		if (target instanceof BoofloEntity) {
			BoofloEntity booflo = (BoofloEntity) target;
			if (!booflo.hasCaughtFruit() && !booflo.hasCaughtPuffBug() && booflo.isTamed()) {
				Level world = player.level;
				PuffBugEntity puffbug = EEEntities.PUFF_BUG.get().create(world);
				if (puffbug != null) {
					puffbug.setPos(target.getX(), target.getY(), target.getZ());
					EntityType.updateCustomEntityTag(world, player, puffbug, stack.getOrCreateTag());
					puffbug.finalizeSpawn((ServerLevel) world, world.getCurrentDifficultyAt(puffbug.blockPosition()), MobSpawnType.BUCKET, null, stack.getOrCreateTag());
					world.addFreshEntity(puffbug);
					booflo.catchPuffBug(puffbug);
					if (!player.getAbilities().instabuild) {
						this.emptyBottle(player, hand);
					}
				}
				return InteractionResult.CONSUME;
			}
		}
		return super.interactLivingEntity(stack, player, target, hand);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
		ItemStack itemstack = playerIn.getItemInHand(handIn);
		if (worldIn.isClientSide) {
			return new InteractionResultHolder<>(InteractionResult.PASS, itemstack);
		} else {
			HitResult raytraceresult = getPlayerPOVHitResult(worldIn, playerIn, ClipContext.Fluid.SOURCE_ONLY);
			if (raytraceresult.getType() != HitResult.Type.BLOCK) {
				return new InteractionResultHolder<>(InteractionResult.PASS, itemstack);
			} else {
				BlockHitResult blockraytraceresult = (BlockHitResult) raytraceresult;
				BlockPos blockpos = blockraytraceresult.getBlockPos();
				if (!(worldIn.getBlockState(blockpos).getBlock() instanceof LiquidBlock)) {
					return new InteractionResultHolder<>(InteractionResult.PASS, itemstack);
				} else if (worldIn.mayInteract(playerIn, blockpos) && playerIn.mayUseItemAt(blockpos, blockraytraceresult.getDirection(), itemstack)) {
					EntityType<?> entitytype = EEEntities.PUFF_BUG.get();
					if (entitytype.spawn((ServerLevel) worldIn, itemstack, playerIn, blockpos, MobSpawnType.SPAWN_EGG, false, false) == null) {
						return new InteractionResultHolder<>(InteractionResult.PASS, itemstack);
					} else {
						if (!playerIn.getAbilities().instabuild) {
							this.emptyBottle(playerIn, handIn);
						}

						playerIn.awardStat(Stats.ITEM_USED.get(this));
						return new InteractionResultHolder<>(InteractionResult.SUCCESS, itemstack);
					}
				} else {
					return new InteractionResultHolder<>(InteractionResult.FAIL, itemstack);
				}
			}
		}
	}

	@Override
	public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
		CompoundTag nbt = stack.getTag();
		if (nbt != null && nbt.contains("CustomPotionEffects")) {
			tooltip.add(Component.translatable("tooltip.endergetic.activePotions").withStyle(ChatFormatting.DARK_PURPLE));
			for (MobEffectInstance effects : PotionUtils.getCustomEffects(nbt)) {
				ChatFormatting[] potionTextFormat = new ChatFormatting[]{ChatFormatting.ITALIC, this.getEffectTextColor(effects)};
				tooltip.add(Component.literal(" " + I18n.get(effects.getDescriptionId()) + " " + ItemStackUtil.intToRomanNumerals(effects.getAmplifier() + 1)).withStyle(potionTextFormat));
			}
		}
	}

	private ChatFormatting getEffectTextColor(MobEffectInstance effect) {
		Map<Attribute, AttributeModifier> map = effect.getEffect().getAttributeModifiers();
		if (!map.isEmpty()) {
			for (Entry<Attribute, AttributeModifier> entry : map.entrySet()) {
				AttributeModifier entryValue = entry.getValue();
				AttributeModifier modifier = new AttributeModifier(entryValue.getName(), effect.getEffect().getAttributeModifierValue(effect.getAmplifier(), entryValue), entryValue.getOperation());

				if (modifier.getAmount() <= 0.0F) {
					return ChatFormatting.RED;
				}
			}
		}
		return effect.getEffect().isBeneficial() ? ChatFormatting.BLUE : ChatFormatting.RED;
	}

	private void emptyBottle(Player player, InteractionHand hand) {
		if (!player.isCreative()) {
			EquipmentSlot slot = hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND;
			player.setItemSlot(slot, new ItemStack(Items.GLASS_BOTTLE));
		}
	}

	public static class PuffBugBottleDispenseBehavior extends DefaultDispenseItemBehavior {

		public ItemStack execute(BlockSource source, ItemStack stack) {
			Direction direction = source.getBlockState().getValue(DispenserBlock.FACING);
			if (source.getLevel().getBlockState(source.getPos().relative(direction)).getCollisionShape(source.getLevel(), source.getPos().relative(direction)).isEmpty()) {
				EntityType<?> entitytype = EEEntities.PUFF_BUG.get();
				entitytype.spawn(source.getLevel(), stack, null, source.getPos().relative(direction), MobSpawnType.DISPENSER, direction != Direction.UP, false);
				stack = new ItemStack(Items.GLASS_BOTTLE);
			} else {
				return super.execute(source, stack);
			}
			return stack;
		}

	}
}
